package com.hunght.solarlunarcalendar;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hunght.data.NoteItem;
import com.hunght.utils.FileChooser;
import com.hunght.utils.MethodsHelper;
import com.hunght.utils.SharedPreferencesUtils;
import com.hunght.utils.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Lenovo on 4/20/2018.
 */

public class NotesView extends LinearLayout {

    FloatingActionButton fbtNotesViewAdd;
    TextView tvNotesViewNoItem, tvNotesViewBackupPath;
    ListView lvNotesViewItems;
    Button btBackup;
    Button btRestore;
    ArrayList<NoteItem> noteItems;

    public NotesView(Context context) {
        super(context);
        initView();
    }

    public NotesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public NotesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NotesView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private String m_chosenDir = "";
    private boolean m_newFolderEnabled = true;

    private void initView() {
        View view = inflate(getContext(), R.layout.notes_view, this);

        fbtNotesViewAdd = view.findViewById(R.id.fbtNotesViewAdd);
        tvNotesViewNoItem = view.findViewById(R.id.tvNotesViewNoItem);
        tvNotesViewBackupPath = view.findViewById(R.id.tvNotesViewBackupPath);
        lvNotesViewItems =  view.findViewById(R.id.lvNotesViewItems);
        btBackup =  view.findViewById(R.id.btBackup);
        btRestore =  view.findViewById(R.id.btRestore);

        File sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        boolean isExternal = Environment.isExternalStorageRemovable(sdCard);
        String saveFolder = Environment.DIRECTORY_DOCUMENTS + "/" + Utils.BACKUP_FOLDER;
        String path = isExternal?"SD card/":"Internal Storage/"+saveFolder;
        tvNotesViewBackupPath.setText("Sao Lưu: "+path + "\n Hoặc: " + sdCard.getPath() +"/" + Utils.BACKUP_FOLDER );
        loadNoteItems();

        fbtNotesViewAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup parent = (ViewGroup) NotesView.this.getParent();
                parent.removeAllViews();
                SaveNoteItemView.setNoteItem(null);
                parent.addView(new SaveNoteItemView(getContext()), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            }
        });

        btBackup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Sao Lưu", Toast.LENGTH_LONG).show();
                if(MethodsHelper.checkPermission(getContext(),MainActivity.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE))
                {
                    Utils.exportToCsv(getContext(), noteItems);
                }
            }
        });

        btRestore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MethodsHelper.checkPermission(getContext(), MainActivity.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)) {
                    FileChooser fileChooser = new FileChooser(getContext());

                    fileChooser.setFileListener(new FileChooser.FileSelectedListener() {
                        @Override
                        public void fileSelected(final File file) {
                            // ....do something with the file
                            String filename = file.getAbsolutePath();
                            Log.i("File Name", filename);
                            String json = Utils.readTextFile(file);
                            try {
                                Gson gson = new Gson();
                                NoteItem[] restoredNoteItems = gson.fromJson(json, NoteItem[].class);
                                for (int i = 0; i < restoredNoteItems.length; i++) {
                                    SharedPreferencesUtils.updateNoteItems(getContext(), restoredNoteItems[i]);
                                }

                                loadNoteItems();

                                Toast.makeText(getContext(), "Phục hồi dữ liệu đã xong", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Log.d("TAG", "Error Write File" + e.getMessage());
                                e.printStackTrace();
                                Toast.makeText(getContext(), "File không đúng định dạng phục hồi", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                    fileChooser.showDialog();
                   // Toast.makeText(getContext(), "Phục Hồi", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loadNoteItems(){
        noteItems = SharedPreferencesUtils.getNoteItems(getContext());
        if(noteItems == null || noteItems.size() == 0)
        {
            tvNotesViewNoItem.setVisibility(VISIBLE);
            lvNotesViewItems.setVisibility(GONE);
            btBackup.setVisibility(GONE);
        }else {
            tvNotesViewNoItem.setVisibility(GONE);
            lvNotesViewItems.setVisibility(VISIBLE);

            NoteItemAdapter adapter = new NoteItemAdapter(getContext(), noteItems, getResources());
            lvNotesViewItems.setAdapter(adapter);
            lvNotesViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    NoteItem item = (NoteItem)view.getTag();
                    ViewGroup parentView = (ViewGroup) NotesView.this.getParent();
                    SaveNoteItemView.setNoteItem(item);
                    parentView.addView(new SaveNoteItemView(getContext()), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                }
            });
        }
    }
}

package com.hunght.solarlunarcalendar;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hunght.data.DateItemForGridview;
import com.hunght.data.NoteItem;
import com.hunght.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Lenovo on 4/20/2018.
 */

public class NotesView extends LinearLayout {

    FloatingActionButton fbtNotesViewAdd;
    TextView tvNotesViewNoItem;
    ListView lvNotesViewItems;
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

    private void initView() {
        View view = inflate(getContext(), R.layout.notes_view, this);

        fbtNotesViewAdd = (FloatingActionButton) view.findViewById(R.id.fbtNotesViewAdd);
        tvNotesViewNoItem = (TextView) view.findViewById(R.id.tvNotesViewNoItem);
        lvNotesViewItems =  (ListView) view.findViewById(R.id.lvNotesViewItems);

        noteItems = SharedPreferencesUtils.getNoteItems(getContext());

        if(noteItems == null || noteItems.size() == 0)
        {
            tvNotesViewNoItem.setVisibility(VISIBLE);
            lvNotesViewItems.setVisibility(GONE);
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
        fbtNotesViewAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup parent = (ViewGroup) NotesView.this.getParent();
                parent.removeAllViews();
                SaveNoteItemView.setNoteItem(null);
                parent.addView(new SaveNoteItemView(getContext()), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            }
        });
    }
}

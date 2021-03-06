package com.hunght.tinchungkhoan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.hunght.data.DanhMucDauTuItem;
import com.hunght.data.DoanhNghiepItem;
import com.hunght.data.MenuLookUpItemKind;
import com.hunght.data.StaticData;
import com.hunght.utils.FileChooser;
import com.hunght.utils.MethodsHelper;
import com.hunght.utils.ParserData;
import com.hunght.utils.SavedValues;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class DanhMucDauTuView extends LinearLayout {
    ExpandableListView lvDanhMucDauTu;
    TextView tvProcessInfo, tvNotification;
    TextView tvLastUpdatedTime;
    private static SavedValues savedValues;
    ArrayList<DanhMucDauTuItem> danhMucDauTuItems;

    TextView tvTongDauTu, tvTongThiTruong, tvTongLoiNhuan;

    public DanhMucDauTuView(Context context) {
        super(context);
        init(null, 0);
    }

    public DanhMucDauTuView(Context context, MenuLookUpItemKind kind) {
        this(context);
    }

    public DanhMucDauTuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DanhMucDauTuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final View view = inflate(getContext(), R.layout.danh_muc_dau_tu_layout, this);
        final Button imDownloadExcel = view.findViewById(R.id.imDownloadExcel);
        final Button btUploadExcel = view.findViewById(R.id.imUploadExcel);
        final ImageButton imUpdate = view.findViewById(R.id.imUpdate);
        final TextView tvSaveFolder = view.findViewById(R.id.tvSaveFolder);
        final Button btThemDauTu = view.findViewById(R.id.btThemDauTu);
        final Button btDieuKienLoc = view.findViewById(R.id.btDieuKienLoc);

        tvTongDauTu = view.findViewById(R.id.tvTongDauTu);
        tvTongThiTruong = view.findViewById(R.id.tvTongThiTruong);
        tvTongLoiNhuan = view.findViewById(R.id.tvTongLoiNhuan);
        tvNotification = view.findViewById(R.id.tvNotification);

        lvDanhMucDauTu = view.findViewById(R.id.lvDanhMucDauTu);
        tvProcessInfo = view.findViewById(R.id.tvProcessInfo);

        tvLastUpdatedTime = view.findViewById(R.id.tvLastUpdatedTime);

        savedValues = new SavedValues(getContext());
        danhMucDauTuItems = savedValues.getDanhMucDauTus();


        lvDanhMucDauTu.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                DanhMucDauTuItem danhMucDauTuItem = (DanhMucDauTuItem) v.getTag();
                if(danhMucDauTuItem != null){
                    showInMenuForItem(danhMucDauTuItem);
                }
                return false;
            }
        });

        updateListDanhMucDauTu(null);
        refeshGiaThiTruong();

        imDownloadExcel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MethodsHelper.checkPermission(getContext(),MainActivity.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE))
                {
                    MethodsHelper.exportDanhMucDauTuToExcel(getContext(), savedValues.getDanhMucDauTus());
                }
            }
        });

        imUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                refeshGiaThiTruong();
            }
        });
        /*this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showInMenu();
                return false;
            }
        });*/
        File sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        boolean isExternal = Environment.isExternalStorageRemovable(sdCard);
        String saveFolder = Environment.DIRECTORY_DOCUMENTS + "/" + MethodsHelper.BACKUP_FOLDER;
        String path = isExternal?"SD card/":"Internal Storage/"+saveFolder;
        tvSaveFolder.setText("Sao Lưu: "+path + "\n Hoặc: " + sdCard.getPath() +"/" + MethodsHelper.BACKUP_FOLDER );

        btUploadExcel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MethodsHelper.checkPermission(getContext(), MainActivity.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)) {
                    FileChooser fileChooser = new FileChooser(getContext());

                    fileChooser.setFileListener(new FileChooser.FileSelectedListener() {
                        @Override
                        public void fileSelected(final File file) {
                            // ....do something with the file
                            String filename = file.getAbsolutePath();
                            Log.i("File Name", filename);
                            ArrayList<DanhMucDauTuItem> restoreDanhMucDauTuItems = MethodsHelper.importDanhMucDauTuFromExcel(file);
                            if (restoreDanhMucDauTuItems == null) {
                                Toast.makeText(getContext(), "Không đọc được file: " + filename, Toast.LENGTH_LONG).show();
                            } else if (restoreDanhMucDauTuItems.size() == 0) {
                                Toast.makeText(getContext(), "Không có dữ liệu", Toast.LENGTH_LONG).show();
                            }else {
                                restoreDanhMucDauTu(restoreDanhMucDauTuItems);
                            }
                        }
                    });
                    fileChooser.showDialog();
                }
            }
        });

        btThemDauTu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                themDanhMucDauTu();
            }
        });

        btDieuKienLoc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                caiDatHienThi();
            }
        });
    }
    private void showDateTimePopup(final DatePickerDialog datePickerDialog){
    // TODO Auto-generated method stub
    datePickerDialog.show();
    View view = ((Activity)getContext()).getCurrentFocus();
    if (view != null) {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
    private void deleteDanhMucDauTu(final DanhMucDauTuItem danhMucDauTuItem)
    {
        if(danhMucDauTuItem != null) {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                    .setTitle("Xóa Dữ Liệu")
                    .setMessage("Bạn muốn xóa mục này")
                    .setNeutralButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    for(DanhMucDauTuItem item: danhMucDauTuItems){
                                        if(item.isTheSame(danhMucDauTuItem)){
                                            danhMucDauTuItems.remove(item);
                                            updateListDanhMucDauTu(null);
                                            break;
                                        }
                                    }
                                }
                            })
                    .setPositiveButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }
    }

    private void chinhSuaDanhMucDauTu(final DanhMucDauTuItem danhMucDauTuItem)
    {
        if(danhMucDauTuItem != null) {
            LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.danh_muc_dau_tu_chinh_sua_popup, null);

            ((EditText)dialogView.findViewById(R.id.etMaCK)).setText(danhMucDauTuItem.getMaCK());
            ((EditText)dialogView.findViewById(R.id.etNgayMua)).setText(danhMucDauTuItem.getNgayMua());
            ((EditText)dialogView.findViewById(R.id.etSoLuong)).setText(MethodsHelper.getStringFromInt(danhMucDauTuItem.getSoLuong()));
            ((EditText)dialogView.findViewById(R.id.etGiaMua)).setText(MethodsHelper.getStringFromFloat(danhMucDauTuItem.getGiaMua()));

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                    .setTitle("Chỉnh Sửa")
                    .setView(dialogView).setCancelable(false)
                    .setPositiveButton("Lưu",null)
                    .setNegativeButton("Bỏ Qua", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            View view = ((Activity) getContext()).getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info);
            final AlertDialog dialog = builder.create();
            dialog.show();
//Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    View view = ((Activity) getContext()).getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    int soLuong = convertStringToInt(((EditText) dialogView.findViewById(R.id.etSoLuong)).getText().toString());
                    float giaMua = convertStringToFloat(((EditText) dialogView.findViewById(R.id.etGiaMua)).getText().toString());
                    if (giaMua > 0 && soLuong > 0) {
                        danhMucDauTuItem.setSoLuong(soLuong);
                        danhMucDauTuItem.setGiaMua(giaMua);
                        updateListDanhMucDauTu(null);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Dữ liệu không hợp lệ", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void restoreDanhMucDauTu(final ArrayList<DanhMucDauTuItem> items)
    {
        new AlertDialog.Builder(getContext())
                .setTitle("Phục Hồi Dữ Liệu")
                .setMessage("Toàn bộ dữ liệu hiện tại sẽ bị xóa và thay thế bằng dữ liệu phục hồi.\nBạn có muốn tiếp tục?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Tiếp Tục", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        danhMucDauTuItems = items;
                        updateListDanhMucDauTu(null);
                        Toast.makeText(getContext(), "Phục hồi dữ liệu thành công", Toast.LENGTH_LONG).show();
                    }})
                .setNegativeButton("Bỏ Qua", null).show();
    }

    private void banDanhMucDauTu(final DanhMucDauTuItem danhMucDauTuItem)
    {
        if(danhMucDauTuItem != null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.danh_muc_dau_tu_ban_popup, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                    .setTitle("Bán " + danhMucDauTuItem.getAllInfo())
                    .setView(dialogView).setCancelable(false)
                    .setPositiveButton("Lưu",null)
                    .setNegativeButton("Bỏ Qua", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            View view = ((Activity) getContext()).getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info);
            final AlertDialog dialog = builder.create();
            dialog.show();

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = ((Activity) getContext()).getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    int soLuong = convertStringToInt(((EditText) dialogView.findViewById(R.id.etSoLuong)).getText().toString());
                    float giaBan = convertStringToFloat(((EditText) dialogView.findViewById(R.id.etGiaBan)).getText().toString());
                    if (giaBan > 0 && soLuong > 0 && danhMucDauTuItem.getSoLuong() >= soLuong) {
                        if (danhMucDauTuItem.getSoLuong() == soLuong) {
                            danhMucDauTuItem.setGiaBan(giaBan);
                            updateListDanhMucDauTu(null);
                        } else {
                            DanhMucDauTuItem banItem = new DanhMucDauTuItem(danhMucDauTuItem.getNgayMua(), danhMucDauTuItem.getMaCK(), danhMucDauTuItem.getTenCongTy(), danhMucDauTuItem.getGiaMua(), soLuong, 0, giaBan);
                            danhMucDauTuItem.setSoLuong(danhMucDauTuItem.getSoLuong() - soLuong);
                            updateDanhMucDauTu(banItem);
                        }
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Dữ liệu không hợp lệ", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void caiDatHienThi(){
        LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.danh_muc_dau_tu_cai_dat_hien_thi, null);

        Switch swHideChungKhoanDauTu = dialogView.findViewById(R.id.swHideChungKhoanDauTu);
        Switch swHideChiMucDauTu = dialogView.findViewById(R.id.swHideChiMucDauTu);
        EditText etCacMaCK = dialogView.findViewById(R.id.etCacMaCK);

        etCacMaCK.setText(savedValues.getRecordCacMaChungKhoanDauTu());

        swHideChungKhoanDauTu.setChecked(savedValues.getRecordHideChungKhoanDauTu());
        swHideChungKhoanDauTu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                savedValues.setRecordHideChungKhoanDauTu(isChecked);
            }
        });

        swHideChiMucDauTu.setChecked(savedValues.getRecordHideChiMucDauTu());
        swHideChiMucDauTu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                savedValues.setRecordHideChiMucDauTu(isChecked);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle("Cài Đặt Hiển Thị")
                .setView(dialogView).setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText etCacMaCK = dialogView.findViewById(R.id.etCacMaCK);
                        String cks = "";
                        if(etCacMaCK != null && etCacMaCK.getText() != null )
                        {
                            cks =etCacMaCK.getText().toString();
                        }
                        Log.d("filterMaCKs", cks);
                        savedValues.setRecordCacMaChungKhoanDauTu(cks.trim());
                        updateListDanhMucDauTu(null);
                        hideKeyboard();
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info);
        final AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void themDanhMucDauTu() {
        LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.danh_muc_dau_tu_them_popup, null);


        final EditText etNgayMua = dialogView.findViewById(R.id.etNgayMua);
        final AutoCompleteTextView etMaCK = dialogView.findViewById(R.id.etMaCK);

        final Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        //myCalendar.add(Calendar.DAY_OF_MONTH, -1);
        final DatePickerDialog.OnDateSetListener fromDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateForEditText(etNgayMua, myCalendar);
            }

        };

        final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), fromDate, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        CongTyAutoCompleteAdapter adapter = new CongTyAutoCompleteAdapter(getContext(), R.layout.cong_ty_auto_complete_item, 5, MainActivity.gethongTinDoanhNghiepsClone());
        etMaCK.setAdapter(adapter);
        etMaCK.setThreshold(1);
        etMaCK.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DoanhNghiepItem doanhNghiepItem = (DoanhNghiepItem)view.getTag();
                etMaCK.setText(doanhNghiepItem.getMaCK(), false);
            }
        });
        etMaCK.setTextColor(Color.RED);

        etNgayMua.setInputType(InputType.TYPE_NULL);
        etNgayMua.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showDateTimePopup(datePickerDialog);
                }
            }
        });
        etNgayMua.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePopup(datePickerDialog);
            }
        });

        updateDateForEditText(etNgayMua, myCalendar);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle("Thêm Mục Đầu Tư")
                .setView(dialogView).setCancelable(false)
                .setPositiveButton("Lưu", null)
                .setNegativeButton("Bỏ Qua", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        hideKeyboard();
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info);
        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();

                EditText etNgayMua = dialogView.findViewById(R.id.etNgayMua);
                EditText etSoLuong = dialogView.findViewById(R.id.etSoLuong);
                AutoCompleteTextView etMaCK = dialogView.findViewById(R.id.etMaCK);
                EditText etGiaMua = dialogView.findViewById(R.id.etGiaMua);

                String ngayMua = etNgayMua.getText().toString().trim();
                String maCK = etMaCK.getText().toString().toUpperCase().trim();
                int soLuong = convertStringToInt(etSoLuong.getText().toString());
                float giaMua = convertStringToFloat(etGiaMua.getText().toString());

                if(ngayMua.isEmpty() || maCK.isEmpty() || soLuong == 0 || giaMua == 0){
                    Toast.makeText(getContext(), "Dữ Liệu Không Hợp Lệ", Toast.LENGTH_LONG).show();
                }else {
                    String name = StaticData.getNameCongTy(maCK);
                    if(name.isEmpty()){
                        Toast.makeText(getContext(), "Mã Chứng Khoán Không Tồn Tại", Toast.LENGTH_LONG).show();
                    }else {
                        String filterMaCKs = savedValues.getRecordCacMaChungKhoanDauTu();
                        if(filterMaCKs != null && !filterMaCKs.isEmpty()) {
                            String[] maCks = filterMaCKs.split(",");
                            if(!isInItems(maCks, maCK)){
                                filterMaCKs = filterMaCKs + ", " + maCK;
                                savedValues.setRecordCacMaChungKhoanDauTu(filterMaCKs);
                            }
                        }
                        DanhMucDauTuItem danhMucDauTuItem = new DanhMucDauTuItem(ngayMua, maCK, name, giaMua, soLuong);
                        updateDanhMucDauTu(danhMucDauTuItem);
                        etMaCK.setText("");
                        etSoLuong.setText("");
                        etGiaMua.setText("");

                        dialog.dismiss();
                    }
                }
            }
        });
    }

    private void hideKeyboard()
    {
        View view = ((Activity) getContext()).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private void showInMenuForItem(final DanhMucDauTuItem danhMucDauTuItem) {
        if (danhMucDauTuItem == null) return;
        String[] colors = {"Bán", "Chỉnh Sửa", "Xóa"};
        if (danhMucDauTuItem.daBan()) {
            colors = new String[]{"Xóa"};
        }

        // Initialize a new array adapter instance
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1, colors
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                TextView text_view = (TextView) super.getView(position, convertView, parent);

                if (position % 2 == 0) {
                    text_view.setBackgroundColor(getContext().getResources().getColor(R.color.item_odd_color));
                } else {
                    text_view.setBackgroundColor(getContext().getResources().getColor(R.color.item_even_color));
                }

                return text_view;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(danhMucDauTuItem.getAllInfo());
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (danhMucDauTuItem.daBan()) {
                    deleteDanhMucDauTu(danhMucDauTuItem);
                } else {
                    switch (which) {
                        case 0:
                            banDanhMucDauTu(danhMucDauTuItem);
                            break;
                        case 1:
                            chinhSuaDanhMucDauTu(danhMucDauTuItem);
                            break;
                        case 2:
                            deleteDanhMucDauTu(danhMucDauTuItem);
                            break;
                    }
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void showInMenu() {
        String[] colors = {"Cập Nhật Giá", "Xuất ra excel"};


        // Initialize a new array adapter instance
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1, colors
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                TextView text_view = (TextView) super.getView(position, convertView, parent);

                if (position % 2 == 0) {
                    text_view.setBackgroundColor(getContext().getResources().getColor(R.color.item_odd_color));
                } else {
                    text_view.setBackgroundColor(getContext().getResources().getColor(R.color.item_even_color));
                }

                return text_view;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("...");
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:
                        refeshGiaThiTruong();
                        break;
                    case 1:
                        if(MethodsHelper.checkPermission(getContext(), MainActivity.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE))
                        {
                            MethodsHelper.exportDanhMucDauTuToExcel(getContext(), savedValues.getDanhMucDauTus());
                        }
                        break;
                    case 2:

                        break;
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private int convertStringToInt(String number)
    {
        number= number.trim();
        if(number.isEmpty()) return 0;
        return Integer.valueOf(number);
    }

    private float convertStringToFloat(String number)
    {
        number= number.trim();
        if(number.isEmpty()) return 0;
        return Float.valueOf(number);
    }

    private void updateDanhMucDauTu(DanhMucDauTuItem item){
        for(DanhMucDauTuItem danhMucDauTuItem: danhMucDauTuItems){
            if(danhMucDauTuItem.isTheSame(item)){
                danhMucDauTuItem.setSoLuong(danhMucDauTuItem.getSoLuong() + item.getSoLuong());
                item = null;
                break;
            }
        }

        if(item!=null){
            if(danhMucDauTuItems == null){
                danhMucDauTuItems = new ArrayList<>();
            }
            danhMucDauTuItems.add(item);
        }
        updateListDanhMucDauTu(null);
        refeshGiaThiTruong();
    }

    private void refeshGiaThiTruong()
    {
        ArrayList<String> maCKs = new ArrayList<>();
        for(DanhMucDauTuItem danhMucDauTuItem : danhMucDauTuItems){
            if(!danhMucDauTuItem.daBan() && !maCKs.contains(danhMucDauTuItem.getMaCK())){
                maCKs.add(danhMucDauTuItem.getMaCK());
            }
        }

        if(!maCKs.isEmpty()){
            for(String maCK: maCKs){
                (new DownloadContentTask()).execute(maCK);
            }
        }
    }

    private void updateDateForEditText(EditText editText, Calendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        editText.setText((day < 10 ? "0" : "") + day + "-" + (month < 10 ? "0" : "") + month + "-" + year);
    }

    private boolean isInItems(String[] items, String item)
    {
        for (String element : items) {
            if (item.equalsIgnoreCase(element.trim())) {
                return true;
            }
        }
        return false;
    }
    ArrayList<String> listDataHeader;
    HashMap<String, List<DanhMucDauTuItem>> listDataChild;
    private void updateListDanhMucDauTu(DanhMucDauTuItem newPrice)
    {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap();
        if(danhMucDauTuItems == null || danhMucDauTuItems.isEmpty())
        {
            if(tvProcessInfo!=null)
            {
                tvProcessInfo.setVisibility(VISIBLE);
                tvProcessInfo.setText("Không có dữ liệu");
            }
            if(lvDanhMucDauTu !=null){
                lvDanhMucDauTu.setVisibility(GONE);
            }
        }else{
            if(tvProcessInfo!=null) {
                tvProcessInfo.setVisibility(GONE);
                tvProcessInfo.setText("");
            }
            float loiNhuan = 0, dauTu = 0, thiTruong = 0;
            for(DanhMucDauTuItem danhMucDauTuItem: danhMucDauTuItems) {
                if (newPrice != null) {
                    if (!danhMucDauTuItem.daBan() && danhMucDauTuItem.getMaCK().equalsIgnoreCase(newPrice.getMaCK())) {
                        danhMucDauTuItem.setGiaThiTruong(newPrice.getGiaThiTruong());
                    }
                }
            }

            savedValues.setDanhMucDauTus(danhMucDauTuItems);
            boolean isHideCKDaBan = savedValues.getRecordHideChungKhoanDauTu();
            boolean isHideChiMucDaBan = savedValues.getRecordHideChiMucDauTu();
            String filterMaCKs = savedValues.getRecordCacMaChungKhoanDauTu();
            String[] maCks = filterMaCKs.split(",");
            Log.d("filterMaCKs", filterMaCKs);
            if(filterMaCKs != null && !filterMaCKs.isEmpty()){
                tvNotification.setVisibility(View.VISIBLE);
                tvNotification.setText("Bộ lọc các mã chứng khoán: " + filterMaCKs);
            }else{
                tvNotification.setVisibility(View.GONE);
            }
            for (DanhMucDauTuItem danhMucDauTuItem:danhMucDauTuItems) {
                if((filterMaCKs != null && !filterMaCKs.isEmpty()) && !isInItems(maCks, danhMucDauTuItem.getMaCK())) continue;

                if(isHideChiMucDaBan && danhMucDauTuItem.daBan()) continue;

                if(listDataHeader.contains(danhMucDauTuItem.getMaCK()))
                {
                    listDataChild.get(danhMucDauTuItem.getMaCK()).add(danhMucDauTuItem);
                }else{
                    listDataHeader.add(danhMucDauTuItem.getMaCK());
                    ArrayList<DanhMucDauTuItem> newList = new ArrayList<>();
                    newList.add(danhMucDauTuItem);
                    listDataChild.put(danhMucDauTuItem.getMaCK(), newList);
                }

            }

            if(isHideCKDaBan)
            {
                ArrayList temp = new ArrayList<>();
                for(String title : listDataHeader)
                {
                    boolean allBan = true;
                    for(DanhMucDauTuItem danhMucDauTuItem : listDataChild.get(title)){
                        if(!danhMucDauTuItem.daBan()){
                            allBan = false;
                        }
                    }
                    if(allBan){
                        listDataChild.remove(title);
                        //listDataHeader.remove(title);
                    }else{
                        temp.add(title);
                    }
                }
                listDataHeader = temp;
            }

            ExpandableDanhMucDauTuListAdapter listAdapter = new ExpandableDanhMucDauTuListAdapter(getContext(), listDataHeader, listDataChild);
            // setting list adapter
            if(lvDanhMucDauTu !=null)
            {
                lvDanhMucDauTu.setVisibility(VISIBLE);
                lvDanhMucDauTu.setAdapter(listAdapter);
                for(int i = 0; i < listDataHeader.size(); i++)
                {
                    lvDanhMucDauTu.expandGroup(i, true);
                    for(DanhMucDauTuItem danhMucDauTuItem : listDataChild.get(listDataHeader.get(i))){
                        loiNhuan += danhMucDauTuItem.getLoiNhan();
                        dauTu += danhMucDauTuItem.getTongDauTu();
                        thiTruong += danhMucDauTuItem.getTongThiTruongHoacBan();
                    }
                }
            }


            if(tvTongDauTu!=null && tvTongThiTruong!=null && tvTongLoiNhuan!=null) {
                tvTongDauTu.setText(MethodsHelper.getStringFromFloat(dauTu));
                tvTongThiTruong.setText(MethodsHelper.getStringFromFloat(thiTruong));
                tvTongLoiNhuan.setText(MethodsHelper.getStringFromFloat(loiNhuan));

                if (loiNhuan == 0) {
                    tvTongLoiNhuan.setTextColor(getContext().getResources().getColor(R.color.giaZero));
                } else if (loiNhuan > 0) {
                    tvTongLoiNhuan.setTextColor(getContext().getResources().getColor(R.color.giaDuong));
                } else {
                    tvTongLoiNhuan.setTextColor(getContext().getResources().getColor(R.color.giaAm));
                }
            }
        }
        if(tvLastUpdatedTime!=null && savedValues!= null){
            tvLastUpdatedTime.setText(savedValues.getLastUpdatePriceTime());
        }

    }

    private class DownloadContentTask extends AsyncTask<String, Integer, DanhMucDauTuItem> {
        protected DanhMucDauTuItem doInBackground(String... maCK) {
            return ParserData.getCurrentPrice(maCK[0]);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(DanhMucDauTuItem danhMucDauTuItem) {
            if(danhMucDauTuItem!=null)
            {
                if(savedValues != null){
                    savedValues.setLastUpdatePriceTime(MethodsHelper.getCurrentDate());
                }
                updateListDanhMucDauTu(danhMucDauTuItem);
            }
        }
    }
}

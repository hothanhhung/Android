package com.hunght.tinchungkhoan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hunght.data.DanhMucDauTuItem;
import com.hunght.data.MenuLookUpItemKind;
import com.hunght.data.StaticData;
import com.hunght.utils.MethodsHelper;
import com.hunght.utils.ParserData;
import com.hunght.utils.SavedValues;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class DanhMucDauTuView extends LinearLayout {
    ExpandableListView lvDanhMucDauTu;
    TextView tvProcessInfo;
    private static SavedValues savedValues;
    ArrayList<DanhMucDauTuItem> danhMucDauTuItems;

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
        final EditText etNgayMua = view.findViewById(R.id.etNgayMua);
        final EditText etSoLuong = view.findViewById(R.id.etSoLuong);
        final EditText etMaCK = view.findViewById(R.id.etMaCK);
        final EditText etGiaMua = view.findViewById(R.id.etGiaMua);
        final Button btLưu = view.findViewById(R.id.btLưu);
        lvDanhMucDauTu = view.findViewById(R.id.lvDanhMucDauTu);
        tvProcessInfo = view.findViewById(R.id.tvProcessInfo);

        savedValues = new SavedValues(getContext());
        danhMucDauTuItems = savedValues.getDanhMucDauTus();

        final Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        myCalendar.add(Calendar.DAY_OF_MONTH, -1);
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

        etNgayMua.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                datePickerDialog.show();
                View view = ((Activity)getContext()).getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

            }
        });

        updateDateForEditText(etNgayMua, myCalendar);

        btLưu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = ((Activity)getContext()).getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

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
                        DanhMucDauTuItem danhMucDauTuItem = new DanhMucDauTuItem(ngayMua, maCK, name, giaMua, soLuong);
                        updateDanhMucDauTu(danhMucDauTuItem);
                        etMaCK.setText("");
                        etSoLuong.setText("");
                        etGiaMua.setText("");
                    }
                }
            }
        });

        lvDanhMucDauTu.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                DanhMucDauTuItem danhMucDauTuItem = (DanhMucDauTuItem) v.getTag();
                if(danhMucDauTuItem != null){
                    showInMenu(danhMucDauTuItem);
                }
                return false;
            }
        });

        updateListDanhMucDauTu(null);
        refeshGiaThiTruong();
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


    private void showInMenu(final DanhMucDauTuItem danhMucDauTuItem) {
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
                tvProcessInfo.setText("Không có dữ liệu");
            }
            if(lvDanhMucDauTu !=null){
                lvDanhMucDauTu.setVisibility(GONE);
            }
        }else{
            if(tvProcessInfo!=null) {
                tvProcessInfo.setText("");
            }
            if(newPrice!=null){
                for(DanhMucDauTuItem danhMucDauTuItem: danhMucDauTuItems){
                    if(!danhMucDauTuItem.daBan() && danhMucDauTuItem.getMaCK().equalsIgnoreCase(newPrice.getMaCK()))
                    {
                        danhMucDauTuItem.setGiaThiTruong(newPrice.getGiaThiTruong());
                    }
                }
            }

            savedValues.setDanhMucDauTus(danhMucDauTuItems);
            for (DanhMucDauTuItem danhMucDauTuItem:danhMucDauTuItems) {
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
            ExpandableDanhMucDauTuListAdapter listAdapter = new ExpandableDanhMucDauTuListAdapter(getContext(), listDataHeader, listDataChild);
            // setting list adapter
            if(lvDanhMucDauTu !=null)
            {
                lvDanhMucDauTu.setVisibility(VISIBLE);
                lvDanhMucDauTu.setAdapter(listAdapter);
                for(int i = 0; i < listDataHeader.size(); i++)
                {
                    lvDanhMucDauTu.expandGroup(i, true);
                }
            }
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
                updateListDanhMucDauTu(danhMucDauTuItem);
            }
        }
    }
}

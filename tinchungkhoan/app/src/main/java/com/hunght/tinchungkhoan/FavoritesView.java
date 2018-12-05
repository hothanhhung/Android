package com.hunght.tinchungkhoan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hunght.data.DanhMucDauTuItem;
import com.hunght.data.DoanhNghiepItem;
import com.hunght.data.HistoryPrice;
import com.hunght.data.MenuLookUpItemKind;
import com.hunght.data.PriceItem;
import com.hunght.data.StaticData;
import com.hunght.data.ThongTinDoanhNghiep;
import com.hunght.utils.MethodsHelper;
import com.hunght.utils.ParserData;
import com.hunght.utils.SavedValues;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * TODO: document your custom view class.
 */
public class FavoritesView extends LinearLayout {
    ListView lvHistoryFavoriteItems;
    HistoryItemAdapter historyItemAdapter;
    AutoCompleteTextView etMaCK;
    SavedValues savedValues;
    public FavoritesView(Context context) {
        super(context);
        init(null, 0);
    }

    public FavoritesView(Context context, MenuLookUpItemKind kind) {
        this(context);
    }

    public FavoritesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FavoritesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final View view = inflate(getContext(), R.layout.favorites_view, this);
        lvHistoryFavoriteItems = view.findViewById(R.id.lvHistoryFavoriteItems);
        savedValues = new SavedValues(getContext());
        ArrayList<String> favoriteItems = savedValues.getFavorites();

        etMaCK = view.findViewById(R.id.etMaCK);

        CongTyAutoCompleteAdapter adapter = new CongTyAutoCompleteAdapter(getContext(), R.layout.cong_ty_auto_complete_item, 10, MainActivity.gethongTinDoanhNghiepsClone());
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

        Button btThemMaCK = view.findViewById(R.id.btThemMaCK);
        btThemMaCK.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String mack = etMaCK.getText().toString();
                if(mack!=null && !mack.trim().isEmpty())
                {
                    addMaCKToFavorite(mack.toUpperCase().trim());
                }
            }
        });

        lvHistoryFavoriteItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    final HistoryPrice historyPrice = (HistoryPrice) view.getTag();
                    if (historyPrice != null) {
                        showInMenu(historyPrice);
                    }
                }
            }
        });

        getHistoryData(favoriteItems);
    }

    private void showChartPopup(final HistoryPrice historyPrice)
    {
        if(historyPrice != null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.favorites_chart_popup, null);

            final ImageView ivBieuDo = dialogView.findViewById(R.id.ivBieuDo);
            final ThongTinDoanhNghiep thongTinDoanhNghiep = new ThongTinDoanhNghiep(historyPrice.getMaCK());

            if (thongTinDoanhNghiep != null && ivBieuDo != null && !thongTinDoanhNghiep.getDoThiSevenDays().isEmpty()) {
                Picasso.with(getContext()).load(thongTinDoanhNghiep.getDoThiSevenToDay()).into(ivBieuDo);
            }

            dialogView.findViewById(R.id.btXemBieuDoToDay).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (thongTinDoanhNghiep != null && ivBieuDo != null && !thongTinDoanhNghiep.getDoThiSevenDays().isEmpty()) {
                        Picasso.with(getContext()).load(thongTinDoanhNghiep.getDoThiSevenToDay()).into(ivBieuDo);
                    }
                }
            });
            dialogView.findViewById(R.id.btXemBieuDoTuan).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (thongTinDoanhNghiep != null && ivBieuDo != null && !thongTinDoanhNghiep.getDoThiSevenDays().isEmpty()) {
                        Picasso.with(getContext()).load(thongTinDoanhNghiep.getDoThiSevenDays()).into(ivBieuDo);
                    }
                }
            });
            dialogView.findViewById(R.id.btXemBieuDoOneThang).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (thongTinDoanhNghiep != null && ivBieuDo != null && !thongTinDoanhNghiep.getDoThiOneMonth().isEmpty()) {
                        Picasso.with(getContext()).load(thongTinDoanhNghiep.getDoThiOneMonth()).into(ivBieuDo);
                    }
                }
            });

            dialogView.findViewById(R.id.btXemBieuDoThreeThang).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (thongTinDoanhNghiep != null && ivBieuDo != null && !thongTinDoanhNghiep.getDoThiOneMonth().isEmpty()) {
                        Picasso.with(getContext()).load(thongTinDoanhNghiep.getDoThiThreeMonth()).into(ivBieuDo);
                    }
                }
            });

            dialogView.findViewById(R.id.btXemBieuDoSixThang).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (thongTinDoanhNghiep != null && ivBieuDo != null && !thongTinDoanhNghiep.getDoThiOneMonth().isEmpty()) {
                        Picasso.with(getContext()).load(thongTinDoanhNghiep.getDoThiSixMonth()).into(ivBieuDo);
                    }
                }
            });
            dialogView.findViewById(R.id.btXemBieuDoOneYear).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (thongTinDoanhNghiep != null && ivBieuDo != null && !thongTinDoanhNghiep.getDoThiOneYear().isEmpty()) {
                        Picasso.with(getContext()).load(thongTinDoanhNghiep.getDoThiOneYear()).into(ivBieuDo);
                    }
                }
            });
            dialogView.findViewById(R.id.btXemBieuDoAll).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (thongTinDoanhNghiep != null && ivBieuDo != null && !thongTinDoanhNghiep.getDoThiAll().isEmpty()) {
                        Picasso.with(getContext()).load(thongTinDoanhNghiep.getDoThiAll()).into(ivBieuDo);
                    }
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                    .setTitle("Biểu Đồ Giá")
                    .setView(dialogView).setCancelable(false)
                    .setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info);
            final AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


    private void deleteFavorite(final HistoryPrice historyPrice)
    {
        if(historyPrice != null) {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                    .setTitle("Xóa Dữ Liệu")
                    .setMessage("Bạn muốn xóa mã " + historyPrice.getMaCK())
                    .setNeutralButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    removeMaCKFromFavorite(historyPrice.getMaCK());
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

    private void showInMenu(final HistoryPrice historyPrice) {
        if (historyPrice == null) return;
        String[] colors = {"Xem Thông Tin Công Ty", "Xem Biểu Đồ Giá", "Xem Lịch Sử Giá", "Xóa"};

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
        builder.setTitle(historyPrice.getFullName());
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        ThongTinDoanhNghiepView.requestCongTy(historyPrice.getMaCK());
                        ((MainActivity) getContext()).changeLayout(StaticData.geMenuItemBasedOnViewClassName("com.hunght.tinchungkhoan.ThongTinDoanhNghiepView"));
                        break;
                    case 1:
                        showChartPopup(historyPrice);
                        break;
                    case 2:
                        showHistoryPopup(historyPrice);
                        break;
                    case 3:
                        deleteFavorite(historyPrice);
                        break;
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    PopupWindow popupWindow;
    private void showHistoryPopup(HistoryPrice historyPrice) {
        if(historyPrice!=null) {
            /*Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.favorites_history_popup);
            dialog.setTitle(historyPrice.getMaCK());
            ListView lvHistoryItems = dialog.findViewById(R.id.lvHistoryItems);
            FavoritesHistoryItemAdapter adapter = new FavoritesHistoryItemAdapter(getContext(), historyPrice.getPrices());
            lvHistoryItems.setAdapter(adapter);
            dialog.show();*/
            View header= ((Activity)getContext()).getLayoutInflater().inflate(R.layout.favorites_history_item,null,false);
            View view= ((Activity)getContext()).getLayoutInflater().inflate(R.layout.favorites_history_popup,null,false);
            ListView lvHistoryItems = view.findViewById(R.id.lvHistoryItems);
            lvHistoryItems.addHeaderView(header);
            FavoritesHistoryItemAdapter adapter = new FavoritesHistoryItemAdapter(getContext(), historyPrice.getPrices());
            lvHistoryItems.setAdapter(adapter);


            popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
            popupWindow.setFocusable(false);
            popupWindow.setOutsideTouchable(false);
            View container = (View) popupWindow.getContentView().getParent();
            WindowManager wm = (WindowManager) ((Activity)getContext()).getSystemService(Context.WINDOW_SERVICE);
            WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
            p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            p.dimAmount = 0.3f;
            wm.updateViewLayout(container, p);

            ((TextView)view.findViewById(R.id.tvMoreInfo)).setText(historyPrice.getFullName());
            (view.findViewById(R.id.btClosePopup)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(popupWindow!=null) popupWindow.dismiss();
                }
            });

        }
    }

    private void removeMaCKFromFavorite(String mack){
        ArrayList<String> favoriteItems = savedValues.getFavorites();
        if(favoriteItems.contains(mack))
        {
            favoriteItems.remove(mack);
            savedValues.setFavorites(favoriteItems);
        }

        historyItemAdapter.deleteItem(mack);
    }

    private void addMaCKToFavorite(String mack){
        HistoryPrice historyPrice = ParserData.getHistoryPrice(mack);

        if(historyPrice == null){
            Toast.makeText(getContext(), "Mã này không tồn tại hoặc có lỗi", Toast.LENGTH_LONG).show();
        }else {
            ArrayList<String> favoriteItems = savedValues.getFavorites();
            if (!favoriteItems.contains(mack)) {
                favoriteItems.add(mack);
                savedValues.setFavorites(favoriteItems);
            }

            historyItemAdapter.saveItem(historyPrice);
        }
    }

    private void getHistoryData(ArrayList<String> items)
    {
        Calendar calendar = Calendar.getInstance();
        ArrayList<HistoryPrice> historyPrices = new ArrayList<>();
        HistoryPrice historyPriceTitle = new HistoryPrice();
        ArrayList<PriceItem> priceItems = new ArrayList<>();
        for(int i = 0; i<10; i++){
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
            {
                calendar.add(Calendar.DAY_OF_MONTH, -1);
            }
            else if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            {
                calendar.add(Calendar.DAY_OF_MONTH, -2);
            }
            priceItems.add( new PriceItem(MethodsHelper.getDDMMYYY(calendar), "..."));
        }
        historyPriceTitle.setPrices((ArrayList<PriceItem>)priceItems.clone());
        historyPrices.add(historyPriceTitle);
        for (String item:items) {
            historyPrices.add(new HistoryPrice(item));
        }

        historyItemAdapter = new HistoryItemAdapter(getContext(), historyPrices);
        lvHistoryFavoriteItems.setAdapter(historyItemAdapter);

        for (String item:items) {
            (new DownloadContentTask()).execute(item);
        }
    }

    private class DownloadContentTask extends AsyncTask<String, Integer, HistoryPrice> {
        protected HistoryPrice doInBackground(String... maCK) {
            return ParserData.getHistoryPrice(maCK[0]);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(HistoryPrice historyPrice) {
            historyItemAdapter.saveItem(historyPrice);
        }
    }
}

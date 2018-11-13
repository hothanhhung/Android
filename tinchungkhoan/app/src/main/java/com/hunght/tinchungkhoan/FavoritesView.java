package com.hunght.tinchungkhoan;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.hunght.data.HistoryPrice;
import com.hunght.data.MenuLookUpItemKind;
import com.hunght.data.PriceItem;
import com.hunght.utils.MethodsHelper;
import com.hunght.utils.ParserData;
import com.hunght.utils.SavedValues;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * TODO: document your custom view class.
 */
public class FavoritesView extends LinearLayout {
    ListView lvHistoryFavoriteItems;
    HistoryItemAdapter historyItemAdapter;
    EditText etMaCK;
    SavedValues savedValues;
    public FavoritesView(Context context) {
        super(context);
        init(null, 0);
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

        lvHistoryFavoriteItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    final HistoryPrice historyPrice = (HistoryPrice)view.getTag();
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
                return false;
            }
        });


        getHistoryData(favoriteItems);
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

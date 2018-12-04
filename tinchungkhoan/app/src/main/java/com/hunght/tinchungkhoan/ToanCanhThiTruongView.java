package com.hunght.tinchungkhoan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * TODO: document your custom view class.
 */
public class ToanCanhThiTruongView extends LinearLayout {
    SavedValues savedValues;
    WebView wvToanCanhThiTruong;
    public ToanCanhThiTruongView(Context context) {
        super(context);
        init(null, 0);
    }

    public ToanCanhThiTruongView(Context context, MenuLookUpItemKind kind) {
        this(context);
    }

    public ToanCanhThiTruongView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ToanCanhThiTruongView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final View view = inflate(getContext(), R.layout.toan_canh_thi_truong_view, this);
        savedValues = new SavedValues(getContext());
        wvToanCanhThiTruong = view.findViewById(R.id.wvToanCanhThiTruong);

        view.findViewById(R.id.btXemBieuDoToDay).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                wvToanCanhThiTruong.loadData(getDataToDay(), "text/html", "UTF-8");
            }
        });
        view.findViewById(R.id.btXemBieuDoTuan).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                wvToanCanhThiTruong.loadData(getComonData(1), "text/html", "UTF-8");
            }
        });
        view.findViewById(R.id.btXemBieuDoOneThang).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                wvToanCanhThiTruong.loadData(getComonData(2), "text/html", "UTF-8");
            }
        });

        view.findViewById(R.id.btXemBieuDoThreeThang).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                wvToanCanhThiTruong.loadData(getComonData(3), "text/html", "UTF-8");
            }
        });

        view.findViewById(R.id.btXemBieuDoSixThang).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                wvToanCanhThiTruong.loadData(getComonData(4), "text/html", "UTF-8");
            }
        });
        view.findViewById(R.id.btXemBieuDoOneYear).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                wvToanCanhThiTruong.loadData(getComonData(5), "text/html", "UTF-8");
            }
        });
        view.findViewById(R.id.btXemBieuDoAll).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                wvToanCanhThiTruong.loadData(getComonData(7), "text/html", "UTF-8");
            }
        });
        wvToanCanhThiTruong.loadData(getDataToDay(), "text/html", "UTF-8");
    }

    private String getDataToDay()
    {
        String VNIndex = "<div style='display: inline-block; background: transparent url(&quot;http://s.cafef.vn/chartindex/chartdulieu.ashx?ran=47381&quot;) no-repeat scroll left top; margin-top: 35px; height: 165px; width: 260px;'></div>";
        String HNXIndex = "<div style='display: inline-block; background: transparent url(&quot;http://s.cafef.vn/chartindex/chartdulieu.ashx?ran=664668&quot;) no-repeat scroll -260px 0px; margin-top: 35px; height: 165px; width: 260px;'></div>";
        String VN30Index = "<div style='display: inline-block; background: transparent url(&quot;http://s.cafef.vn/chartindex/chartdulieu.ashx?ran=664668&quot;) no-repeat scroll 0px -165px; margin-top: 35px; height: 165px; width: 260px;'></div>";
        String UpComIndex = "<div style='display: inline-block; background: transparent url(&quot;http://s.cafef.vn/chartindex/chartdulieu.ashx?ran=53716&quot;) no-repeat scroll -520px 0px; margin-top: 35px; height: 165px; width: 260px;'></div>";

        return "<html><body><div style='width: 100%; text-align: center; align-items: center; align-content: center;'>" + VNIndex + HNXIndex + VN30Index + UpComIndex + "</div></body></html>";
    }

    private String getComonData(int index)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String VNIndex = "";
        String HNXIndex = "";
        String VN30Index = "";
        String UpComIndex = "";

        switch (index){
            case 1:
                VNIndex = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/ho_7days.png";
                HNXIndex = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/ha_7days.png";
                VN30Index = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/vn30_7days.png";
                UpComIndex = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/up_7days.png";
                break;
            case 2:
                VNIndex = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/ho_1month.png";
                HNXIndex = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/ha_1month.png";
                VN30Index = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/vn30_1month.png";
                UpComIndex = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/up_1month.png";
                break;
            case 3:
                VNIndex = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/ho_3months.png";
                HNXIndex = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/ha_3months.png";
                VN30Index = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/vn30_3months.png";
                UpComIndex = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/up_3months.png";
                break;
            case 4:
                VNIndex = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/ho_6months.png";
                HNXIndex = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/ha_6months.png";
                VN30Index = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/vn30_6months.png";
                UpComIndex = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/up_6months.png";
                break;
            case 5:
                VNIndex = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/ho_1year.png";
                HNXIndex = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/ha_1year.png";
                VN30Index = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/vn30_1year.png";
                UpComIndex = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/up_1year.png";
                break;
            case 6:
                VNIndex = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/ho_3years.png";
                HNXIndex = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/ha_3years.png";
                VN30Index = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/vn30_3years.png";
                UpComIndex = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/up_3years.png";
                break;
            default:
                VNIndex = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/ho_all.png";
                HNXIndex = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/ha_all.png";
                VN30Index = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/vn30_all.png";
                UpComIndex = "http://cafef4.vcmedia.vn/" + MethodsHelper.getYYYYMMDD(calendar) + "/up_all.png";
                break;
        }

        String rs = "<html><body><div style='width: 100%; text-align: center; align-items: center; align-content: center;'>" + "<img src='"+VNIndex+"'/> <br/> VN-Index<br/>" + "<br/><img src='"+HNXIndex+"'/> <br/> HNX-Index<br/>" +"<br/><img src='"+VN30Index+"'/> <br/> VN30-Index<br/>" +"<br/><img src='"+UpComIndex+"'/> <br/> UpCom-Index" +"</div></body></html>";
        Log.d("getComonData", rs);
        return rs;
    }
}

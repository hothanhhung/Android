package com.hunght.tinchungkhoan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hunght.data.DoanhNghiepItem;
import com.hunght.data.HistoryPrice;
import com.hunght.data.MenuLookUpItemKind;
import com.hunght.data.PriceItem;
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
public class ThongTinDoanhNghiepView extends LinearLayout {
    AutoCompleteTextView etMaCK;
    SavedValues savedValues;
    TextView tvProcessInfo, tvThongSoKT, tvTenCongTy, tvThongTin, tvTraCoTuc;
    ImageView ivLogo, ivBieuDo;
    WebView wvBaoCaoTaiChinh;
    LinearLayout llInformation;

    public ThongTinDoanhNghiepView(Context context) {
        super(context);
        init(null, 0);
    }

    public ThongTinDoanhNghiepView(Context context, MenuLookUpItemKind kind) {
        this(context);
    }

    public ThongTinDoanhNghiepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ThongTinDoanhNghiepView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final View view = inflate(getContext(), R.layout.thong_tin_doanh_nghiep_view, this);
        savedValues = new SavedValues(getContext());

        etMaCK = view.findViewById(R.id.etMaCK);
        tvProcessInfo = view.findViewById(R.id.tvProcessInfo);

        llInformation = view.findViewById(R.id.llInformation);
        tvThongSoKT = view.findViewById(R.id.tvThongSoKT);
        tvTenCongTy = view.findViewById(R.id.tvTenCongTy);
        tvThongTin = view.findViewById(R.id.tvThongTin);
        tvTraCoTuc = view.findViewById(R.id.tvTraCoTuc);
        ivLogo = view.findViewById(R.id.ivLogo);
        ivBieuDo = view.findViewById(R.id.ivBieuDo);
        wvBaoCaoTaiChinh = view.findViewById(R.id.wvBaoCaoTaiChinh);

        Button btTraCuu = view.findViewById(R.id.btTraCuu);

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

        btTraCuu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = ((Activity)getContext()).getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                traCuu();
            }
        });
        view.findViewById(R.id.btXemBieuDoToDay).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thongTinDoanhNghiep != null && ivBieuDo != null && !thongTinDoanhNghiep.getDoThiSevenDays().isEmpty()) {
                    Picasso.with(getContext()).load(thongTinDoanhNghiep.getDoThiSevenToDay()).into(ivBieuDo);
                }
            }
        });
        view.findViewById(R.id.btXemBieuDoTuan).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thongTinDoanhNghiep != null && ivBieuDo != null && !thongTinDoanhNghiep.getDoThiSevenDays().isEmpty()) {
                    Picasso.with(getContext()).load(thongTinDoanhNghiep.getDoThiSevenDays()).into(ivBieuDo);
                }
            }
        });
        view.findViewById(R.id.btXemBieuDoOneThang).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thongTinDoanhNghiep != null && ivBieuDo != null && !thongTinDoanhNghiep.getDoThiOneMonth().isEmpty()) {
                    Picasso.with(getContext()).load(thongTinDoanhNghiep.getDoThiOneMonth()).into(ivBieuDo);
                }
            }
        });

        view.findViewById(R.id.btXemBieuDoThreeThang).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thongTinDoanhNghiep != null && ivBieuDo != null && !thongTinDoanhNghiep.getDoThiOneMonth().isEmpty()) {
                    Picasso.with(getContext()).load(thongTinDoanhNghiep.getDoThiThreeMonth()).into(ivBieuDo);
                }
            }
        });

        view.findViewById(R.id.btXemBieuDoSixThang).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thongTinDoanhNghiep != null && ivBieuDo != null && !thongTinDoanhNghiep.getDoThiOneMonth().isEmpty()) {
                    Picasso.with(getContext()).load(thongTinDoanhNghiep.getDoThiSixMonth()).into(ivBieuDo);
                }
            }
        });
        view.findViewById(R.id.btXemBieuDoOneYear).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thongTinDoanhNghiep != null && ivBieuDo != null && !thongTinDoanhNghiep.getDoThiOneYear().isEmpty()) {
                    Picasso.with(getContext()).load(thongTinDoanhNghiep.getDoThiOneYear()).into(ivBieuDo);
                }
            }
        });
        view.findViewById(R.id.btXemBieuDoAll).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thongTinDoanhNghiep != null && ivBieuDo != null && !thongTinDoanhNghiep.getDoThiAll().isEmpty()) {
                    Picasso.with(getContext()).load(thongTinDoanhNghiep.getDoThiAll()).into(ivBieuDo);
                }
            }
        });

        view.findViewById(R.id.btXemThongSoKT).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvThongSoKT != null) {
                    if(tvThongSoKT.getVisibility() != GONE)
                    {
                        tvThongSoKT.setVisibility(GONE);
                    }else{
                        tvThongSoKT.setVisibility(VISIBLE);
                    }
                }
            }
        });

        view.findViewById(R.id.btXemTraCoTuc).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvTraCoTuc != null) {
                    if(tvTraCoTuc.getVisibility() != GONE)
                    {
                        tvTraCoTuc.setVisibility(GONE);
                    }else{
                        tvTraCoTuc.setVisibility(VISIBLE);
                    }
                }
            }
        });

        view.findViewById(R.id.btXemBieuDo).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ivBieuDo != null) {
                    if(ivBieuDo.getVisibility() != GONE)
                    {
                        ivBieuDo.setVisibility(GONE);
                    }else{
                        ivBieuDo.setVisibility(VISIBLE);
                    }
                }
                LinearLayout lbButtonImage = view.findViewById(R.id.lbButtonImage);
                if (lbButtonImage != null) {
                    if(lbButtonImage.getVisibility() != GONE)
                    {
                        lbButtonImage.setVisibility(GONE);
                    }else{
                        lbButtonImage.setVisibility(VISIBLE);
                    }
                }
            }
        });

        view.findViewById(R.id.btXemBaoCaoTaiChinh).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wvBaoCaoTaiChinh != null) {
                    if(wvBaoCaoTaiChinh.getVisibility() != GONE)
                    {
                        wvBaoCaoTaiChinh.setVisibility(GONE);
                    }else{
                        wvBaoCaoTaiChinh.setVisibility(VISIBLE);
                    }
                }
            }
        });

        if(requestMaCK != null && !requestMaCK.isEmpty()){
            etMaCK.setText(requestMaCK);
            requestMaCK = null;
            traCuu();
        }
    }

    static public String requestMaCK;
    static public void requestCongTy(String mack){
        requestMaCK = mack;
    }


    private void traCuu()
    {
        String mack = etMaCK.getText().toString();
        if(mack!=null && !mack.trim().isEmpty())
        {
            mack = mack.trim();
            DoanhNghiepItem doanhNghiepItem = null;
            for(DoanhNghiepItem item : MainActivity.gethongTinDoanhNghieps())
            {
                if(item.c.equalsIgnoreCase(mack))
                {
                    doanhNghiepItem = item;
                }
            }
            if(llInformation!=null) llInformation.setVisibility(GONE);
            if(doanhNghiepItem == null && tvProcessInfo != null){
                tvProcessInfo.setText("Không tìm thấy thông tin");
            }else{
                (new DownloadContentTask()).execute(doanhNghiepItem.getCafeFURL());
                tvProcessInfo.setText("Đang yêu cầu dữ liệu");
            }
        }
    }

    private ThongTinDoanhNghiep thongTinDoanhNghiep;
    private class DownloadContentTask extends AsyncTask<String, Integer, ThongTinDoanhNghiep> {
        protected ThongTinDoanhNghiep doInBackground(String... data) {
            return ParserData.getThongTinDoanhNghiep(data[0]);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(ThongTinDoanhNghiep data) {
            thongTinDoanhNghiep = data;
            if(thongTinDoanhNghiep == null && tvProcessInfo != null){
                tvProcessInfo.setText("Không tìm thấy thông tin");
            }else{
                tvProcessInfo.setText("");
                if(llInformation!=null) llInformation.setVisibility(VISIBLE);
                if(tvTenCongTy!=null) tvTenCongTy.setText(thongTinDoanhNghiep.getFullName());
                if(tvThongTin!=null) tvThongTin.setText(thongTinDoanhNghiep.getInformation());
                if(ivLogo!=null && !thongTinDoanhNghiep.getLogoURL().isEmpty()){
                    Picasso.with(getContext()).load(thongTinDoanhNghiep.getLogoURL()).into(ivLogo);
                }
                if(ivBieuDo!=null && !thongTinDoanhNghiep.getDoThiSevenToDay().isEmpty()){
                    Picasso.with(getContext()).load(thongTinDoanhNghiep.getDoThiSevenToDay()).into(ivBieuDo);
                }
                if(tvThongSoKT!=null){
                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvThongSoKT.setText(Html.fromHtml(thongTinDoanhNghiep.getThongSoKT(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        tvThongSoKT.setText(Html.fromHtml(thongTinDoanhNghiep.getThongSoKT()));
                    }*/
                    tvThongSoKT.setText(thongTinDoanhNghiep.getThongSoKT());
                }
                if(tvTraCoTuc!=null){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvTraCoTuc.setText(Html.fromHtml(thongTinDoanhNghiep.getTraCoTuc(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        tvTraCoTuc.setText(Html.fromHtml(thongTinDoanhNghiep.getTraCoTuc()));
                    }
                }
                if(wvBaoCaoTaiChinh!=null) wvBaoCaoTaiChinh.loadData(ParserData.getBaoCaoTaiChinh(thongTinDoanhNghiep.getBaoCaoTaiChinhURL()), "text/html", "UTF-8");
            }
        }
    }
}

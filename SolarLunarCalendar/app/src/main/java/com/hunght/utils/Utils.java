package com.hunght.utils;

import com.hunght.data.LunarDate;
import com.hunght.solarlunarcalendar.R;

/**
 * Created by Lenovo on 5/1/2018.
 */

public class Utils {

    static public int getIconConGiap(int day)
    {
        int lunarDate = day % 12;
        switch (lunarDate)
        {
            case LunarDate.TY: return R.drawable.ty;
            case LunarDate.SUU: return R.drawable.suu;
            case LunarDate.DAN: return R.drawable.dan;
            case LunarDate.MAO: return R.drawable.meo;
            case LunarDate.THIN: return R.drawable.thin;
            case LunarDate.TI: return R.drawable.ti;
            case LunarDate.NGO: return R.drawable.ngo;
            case LunarDate.MUI: return R.drawable.mui;
            case LunarDate.THAN: return R.drawable.than;
            case LunarDate.DAU: return R.drawable.dau;
            case LunarDate.TUAT: return R.drawable.tuat;
            case LunarDate.HOI: return R.drawable.hoi;
        }
        return R.drawable.amduong;
    }
}

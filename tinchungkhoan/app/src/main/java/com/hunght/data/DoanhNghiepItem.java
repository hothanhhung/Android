package com.hunght.data;

public class DoanhNghiepItem {
    //i:12,c:"AAM",m:"Công ty Cổ phần Thủy sản Mekong (MEKONGFISH)",o:"Cong ty Co phan Thuy san Mekong (MEKONGFISH)",san:1
    public int i;
    public String c;
    public String m;
    public String o;
    public int san;


    public String getCafeFURL()
    {
        return c+"-"+o.substring(0, o.indexOf('[')).trim().replace(' ','-');
    }
}

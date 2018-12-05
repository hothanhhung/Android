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

        String sanStr="hose";
        if(san==2) sanStr="hastc";
        if(san==8) sanStr="otc";
        if(san==9) sanStr="upcom";

        String linkName = o;
        int index = linkName.indexOf('(');
        if(index > 0){
            linkName = linkName.substring(0, index);
        }
        linkName = linkName.trim().replace(' ','-');
        return "http://s.cafef.vn//"+sanStr+"/"+c+"-"+linkName+".chn";
    }

    public String getMaCK()
    {
        return c;
    }

    public String getFullInfo()
    {
        return "<b>" + c + "</b> - " + m;
    }

    public boolean compare1(String str)
    {
        return c.toLowerCase().startsWith(str.toLowerCase());
    }

    public boolean compare2(String str)
    {
        return o.toLowerCase().contains(str.toLowerCase()) ;
    }

}

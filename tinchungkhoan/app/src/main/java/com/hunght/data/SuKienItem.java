package com.hunght.data;

public class SuKienItem {
    public String id;
    public String code;
    public String group;
    public String type;
    public String typeDesc;
    public String note;
    public int divYear;
    public String disclosureDate;
    public String effectiveDate;
    public String expiredDate;
    public String actualDate;
    public String locale;

    public String getTieuDe(){
        return typeDesc;
    }
    public String getExtraInfo() {
        return code + " - " + note;
    }
}

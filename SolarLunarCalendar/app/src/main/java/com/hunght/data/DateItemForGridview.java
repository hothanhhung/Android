package com.hunght.data;

import com.hunght.utils.DateTools;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lenovo on 3/26/2018.
 */

public class DateItemForGridview {
    private boolean isOutOfMonth;
    private Date date;
    LunarDate lunarDate;
    private String title;

    public DateItemForGridview()
    {

    }

    static public DateItemForGridview createDateItemForGridviewFromLunar(int d, int m, int y){
        return DateTools.convertSolar2DateItemForGridview(d, m, y);
    }

    static public DateItemForGridview createDateItemForGridview(int d, int m, int y, boolean isLeap, boolean isLunar)
    {
        if(isLunar){
            return DateTools.convertLunar2DateItemForGridview(d, m, y, isLeap);
        }else{
            return DateTools.convertSolar2DateItemForGridview(d, m, y);
        }
    }

    public DateItemForGridview(String title, Date date, boolean isOutOfMonth)
    {
        this.title = title;
        this.isOutOfMonth = isOutOfMonth;
        if(date!=null){
            this.date = new Date(date.getYear(), date.getMonth(), date.getDate());
            this.lunarDate = DateTools.convertSolar2Lunar(getDayOfMonth(), getMonth(), getYear());
        }
    }

    public LunarDate getLunarDate()
    {
        return lunarDate;
    }

    public int compare(DateItemForGridview obj) {
        if (date == null) {
            if (obj.getDate() == null) {
                return 0;
            } else {
                return -1;
            }
        } else if (obj.getDate() == null) {
            return 1;
        } else {

            return date.compareTo(obj.getDate());
        }
    }
    public int difference(DateItemForGridview obj) {
        if (date == null) {
            if (obj.getDate() == null) {
                return 0;
            } else {
                return Integer.MIN_VALUE;
            }
        } else if (obj.getDate() == null) {
            return Integer.MAX_VALUE;
        } else {
            long thisTime = date.getTime();
            long anotherTime = obj.getDate().getTime();
            return (int) java.lang.Math.ceil((thisTime - anotherTime)/(1000.0 * 3600 * 24));
        }
    }

    public void addDate(int number)
    {
        if(date!=null) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, number);
            date = c.getTime();
            lunarDate = DateTools.convertSolar2Lunar(getDayOfMonth(), getMonth(), getYear());
        }
    }

    public void addMonth(int number)
    {
        if(date!=null) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.MONTH, number);
            date = c.getTime();
            lunarDate = DateTools.convertSolar2Lunar(getDayOfMonth(), getMonth(), getYear());
        }
    }

    public void addYear(int number)
    {
        if(date!=null) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.YEAR, number);
            date = c.getTime();
            lunarDate = DateTools.convertSolar2Lunar(getDayOfMonth(), getMonth(), getYear());
        }
    }

    public void setMonthYear(int month, int year)
    {
        if(date!=null) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.YEAR, year);
            date = c.getTime();
            lunarDate = DateTools.convertSolar2Lunar(getDayOfMonth(), getMonth(), getYear());
        }
    }


    public boolean isOutOfMonth()
    {
        return isOutOfMonth;
    }

    public boolean isTheSame(Date toDate)
    {
        if(date != null && toDate != null)
        {
            return toDate.getDate() == date.getDate() && toDate.getMonth() == date.getMonth() && toDate.getYear() == date.getYear();
        }
        return toDate == date;
    }

    public boolean isToday()
    {
        if(date != null)
        {
            Date today = new Date();
            return today.getDate() == date.getDate() && today.getMonth() == date.getMonth() && today.getYear() == date.getYear();

        }
        return false;
    }

    public boolean isTitle() {
        return title!=null && title!="";
    }

    public String getTitle() {
        return title;
    }

    public String getSolarDate()
    {
        if(date!=null)
        {
            return "" + date.getDate();
        }
        return "";
    }

    public String getLunarDateToDisplay()
    {
        String str = "";
        if(date!=null)
        {
            str += lunarDate.getDate();
            if((!isOutOfMonth && date.getDate() == 1) || lunarDate.getDate() == 1)
            {
                str += "/" + lunarDate.getMonth();
            }
        }
        return str;
    }
    public String getSolarInfo(boolean isNewLine)
    {
        String space = isNewLine?"\n":" ";
        String str = "";
        if(date!=null)
        {
            str += "Ngày "+getSolarDate()+ space +"Tháng "+getMonth()+space+"Năm " + getYear();
        }
        return str;
    }
    public String getLunarInfo(boolean isNewLine)
    {
        String space = isNewLine?"\n":" ";
        String str = "";
        if(date!=null)
        {
            str += "Ngày "+lunarDate.getDate()+ space +"Tháng "+lunarDate.getMonth() + (lunarDate.isLeap()?" (N)":"") + space +"Năm Âm Lịch";
        }
        return str;
    }
    public String getLunarInfoWidget(boolean isNewLine)
    {
        String space = isNewLine?"\n":" ";
        String str = "";
        if(date!=null)
        {
            str += "Ngày "+lunarDate.getDate()+ space +"Tháng "+lunarDate.getMonth() + (lunarDate.isLeap()?" (N)":"") + space +"Năm " + lunarDate.getYear() + space +"Âm Lịch";
        }
        return str;
    }
    public String getLunarInfo1(boolean isNewLine)
    {
        String space = isNewLine?"\n":" ";
        String str = "";
        if(date!=null)
        {
            str += "Ngày "+getLunarDateInString()+ space +"Tháng "+lunarDate.getMonthInString()+ space +"Năm "+lunarDate.getYearInString();
        }
        return str;
    }

    public int getDateInLunar()
    {
        Date orginalDate = new Date(2013 - 1900, 8 - 1, 2); //Canh Tý
        int day = new Long(((date.getTime() -orginalDate.getTime())/(1000*24*3600)) % 60).intValue();
        if(day<0) day+=60;
        return day;
    }

    public String getLunarGoodTime() {
        String str = "Giờ hoàng đạo: ";
        if (date != null) {

            int day = getDateInLunar() % 12;
            switch (day) {
                case LunarDate.DAN:
                case LunarDate.THAN:
                    str += LunarDate.getHourInfo(LunarDate.TY);
                    str += ", " + LunarDate.getHourInfo(LunarDate.SUU);
                    str += ", " + LunarDate.getHourInfo(LunarDate.THIN);
                    str += ", " + LunarDate.getHourInfo(LunarDate.TI);
                    str += ", " + LunarDate.getHourInfo(LunarDate.MUI);
                    str += ", " + LunarDate.getHourInfo(LunarDate.TUAT);
                    break;
                case LunarDate.MAO:
                case LunarDate.DAU:
                    str += LunarDate.getHourInfo(LunarDate.TY);
                    str += ", " + LunarDate.getHourInfo(LunarDate.DAN);
                    str += ", " + LunarDate.getHourInfo(LunarDate.MAO);
                    str += ", " + LunarDate.getHourInfo(LunarDate.NGO);
                    str += ", " + LunarDate.getHourInfo(LunarDate.MUI);
                    str += ", " + LunarDate.getHourInfo(LunarDate.DAU);
                    break;
                case LunarDate.THIN:
                case LunarDate.TUAT:
                    str += LunarDate.getHourInfo(LunarDate.DAN);
                    str += ", " + LunarDate.getHourInfo(LunarDate.THIN);
                    str += ", " + LunarDate.getHourInfo(LunarDate.TI);
                    str += ", " + LunarDate.getHourInfo(LunarDate.THAN);
                    str += ", " + LunarDate.getHourInfo(LunarDate.DAU);
                    str += ", " + LunarDate.getHourInfo(LunarDate.HOI);
                    break;
                case LunarDate.TI:
                case LunarDate.HOI:
                    //     str = "Sửu, Thìn, Ngọ, Mùi, Tuất, Hợi";
                    str += LunarDate.getHourInfo(LunarDate.SUU);
                    str += ", " + LunarDate.getHourInfo(LunarDate.THIN);
                    str += ", " + LunarDate.getHourInfo(LunarDate.NGO);
                    str += ", " + LunarDate.getHourInfo(LunarDate.MUI);
                    str += ", " + LunarDate.getHourInfo(LunarDate.TUAT);
                    str += ", " + LunarDate.getHourInfo(LunarDate.HOI);
                    break;
                case LunarDate.TY:
                case LunarDate.NGO:
                    //     str = "Tý, Sửu, Mão, Ngọ, Thân, Dậu";
                    str += LunarDate.getHourInfo(LunarDate.TY);
                    str += ", " + LunarDate.getHourInfo(LunarDate.SUU);
                    str += ", " + LunarDate.getHourInfo(LunarDate.MAO);
                    str += ", " + LunarDate.getHourInfo(LunarDate.NGO);
                    str += ", " + LunarDate.getHourInfo(LunarDate.THAN);
                    str += ", " + LunarDate.getHourInfo(LunarDate.DAU);
                    break;
                case LunarDate.SUU:
                case LunarDate.MUI:
                    // str = "Dần, Mão, Tỵ, Thân, Tuất, Hợi";
                    str += LunarDate.getHourInfo(LunarDate.DAN);
                    str += ", " + LunarDate.getHourInfo(LunarDate.MAO);
                    str += ", " + LunarDate.getHourInfo(LunarDate.TI);
                    str += ", " + LunarDate.getHourInfo(LunarDate.THAN);
                    str += ", " + LunarDate.getHourInfo(LunarDate.TUAT);
                    str += ", " + LunarDate.getHourInfo(LunarDate.HOI);
                    break;
            }
        }
        return str;
    }

    public boolean isGoodDay() {
        if (date != null) {
            int day = getDateInLunar() % 12;
            int month = lunarDate.getMonth();
            switch (month) {
                case 1:
                case 7:
                    if (day == LunarDate.TY || day == LunarDate.SUU || day == LunarDate.TI || day == LunarDate.MUI) {
                        return true;
                    }
                    break;
                case 2:
                case 8:
                    if (day == LunarDate.DAN || day == LunarDate.MAO || day == LunarDate.MUI || day == LunarDate.DAU) {
                        return true;
                    }
                    break;
                case 3:
                case 9:
                    if (day == LunarDate.THIN || day == LunarDate.TI || day == LunarDate.DAU || day == LunarDate.HOI) {
                        return true;
                    }
                    break;
                case 4:
                case 10:
                    if (day == LunarDate.NGO || day == LunarDate.MUI || day == LunarDate.SUU || day == LunarDate.DAU) {
                        return true;
                    }
                    break;
                case 5:
                case 11:
                    if (day == LunarDate.THAN || day == LunarDate.DAU || day == LunarDate.SUU || day == LunarDate.MAO) {
                        return true;
                    }
                    break;
                case 6:
                case 12:
                    if (day == LunarDate.TUAT || day == LunarDate.HOI || day == LunarDate.MAO || day == LunarDate.TI) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    public boolean isBadDay() {
        if (date != null) {
            int day = getDateInLunar() % 12;
            int month = lunarDate.getMonth();
            switch (month) {
                case 1:
                case 7:
                    if (day == LunarDate.NGO || day == LunarDate.MAO || day == LunarDate.HOI || day == LunarDate.DAU) {
                        return true;
                    }
                    break;
                case 2:
                case 8:
                    if (day == LunarDate.THAN || day == LunarDate.TI || day == LunarDate.SUU || day == LunarDate.HOI) {
                        return true;
                    }
                    break;
                case 3:
                case 9:
                    if (day == LunarDate.TUAT || day == LunarDate.MUI || day == LunarDate.SUU || day == LunarDate.HOI) {
                        return true;
                    }
                    break;
                case 4:
                case 10:
                    if (day == LunarDate.TY || day == LunarDate.DAU || day == LunarDate.TI || day == LunarDate.MAO) {
                        return true;
                    }
                    break;
                case 5:
                case 11:
                    if (day == LunarDate.DAN || day == LunarDate.HOI || day == LunarDate.MUI || day == LunarDate.TI) {
                        return true;
                    }
                    break;
                case 6:
                case 12:
                    if (day == LunarDate.THIN || day == LunarDate.SUU || day == LunarDate.DAU || day == LunarDate.MUI) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    public String getLunarDateInString()
    {
        if(date!=null)
        {
            Date orginalDate = new Date(2013 - 1900, 8 - 1, 2); //Canh Tý
            int d = getDateInLunar();
            return LunarDate.CAN[d%10]+" "+ LunarDate.CHI[d%12];
        }
        return "";
    }

    public boolean isWeekend(){
        if(date!=null)
        {
            return date.getDay() == 0;
        }
        return title == "CN";
    }

    public boolean isHoliday(){
        if(date!=null)
        {
            return (date.getDate() == 1 && date.getMonth() == 0)
                    || (date.getDate() == 30 && date.getMonth() == 3)
                    || (date.getDate() == 1 && date.getMonth() == 4)
                    || (date.getDate() == 2 && date.getMonth() == 8)
                    || lunarDate.isGioTo() || lunarDate.isTet();
        }
        return false;
    }

    public String specialDate()
    {
        if(date!=null) {
            String str = lunarDate.specialDate();
            if (str != null && str != "") return str;
            str = "" + date.getDate() + "/" + (date.getMonth() + 1);
            switch (str) {
                case "9/1":
                    return "Ngày Học sinh - Sinh viên Việt Nam";
                case "27/1":
                    return "Ngày ký hiệp định Pari";
                case "3/2":
                    return "Ngày Thành lập Đảng Cộng sản Việt Nam";
                case "14/2":
                    return "Ngày lễ tình yêu";
                case "27/2":
                    return "Ngày Thầy thuốc Việt Nam";
                case "8/3":
                    return "Ngày Quốc tế Phụ nữ";
                case "19/3":
                    return "Ngày Toàn quốc chống Mỹ";
                case "26/3":
                    return "Ngày thành lập Đoàn Thanh niên Cộng sản Hồ Chí Minh";
                case "1/4":
                    return "Ngày cá Tháng Tư";
                case "30/4":
                    return "Ngày giải phóng miền Nam";
                case "1/5":
                    return "Ngày Quốc tế lao động";
                case "7/5":
                    return "Ngày Chiến thắng Điện Biên Phủ";
                case "15/5":
                    return "Ngày Thành lập đội thiếu niên tiền phong Hồ Chí Minh";
                case "19/5":
                    return "Ngày sinh chủ tịch Hồ Chí Minh";
                case "28/5":
                    return "Ngày Tưởng niệm";
                case "1/6":
                    return "Ngày Quốc tế Thiếu nhi";
                case "21/6":
                    return "Ngày báo chí Việt Nam";
                case "28/6":
                    return "Ngày Gia đình Việt Nam";
                case "15/7":
                    return "Ngày Truyền thống thanh niên xung phong";
                case "27/7":
                    return "Ngày Thương binh Liệt sĩ";
                case "28/7":
                    return "Ngày Thành lập tổng liên đoàn lao động Việt Nam";
                case "19/8":
                    return "Ngày Cách mạng tháng Tám thành công";
                case "2/9":
                    return "Ngày Quốc khánh Việt Nam";
                case "10/9":
                    return "Ngày Thành lập Mặt trận tổ quốc Việt Nam";
                case "20/9":
                    return "Ngày Gia nhập Liên Hiệp Quốc";
                case "23/9":
                    return "Ngày Nam Bộ kháng chiến";
                case "1/10":
                    return "Ngày Quốc tế người cao tuổi";
                case "10/10":
                    return "Ngày Giải phóng Thủ đô (Hà Nội)";
                case "14/10":
                    return "Ngày thành lập hội nông dân Việt Nam";
                case "15/10":
                    return "Ngày thành lập Hội liên hiệp thanh niên Việt Nam";
                case "20/10":
                    return "Ngày thành lập Hội Phụ nữ Việt Nam";
                case "31/10":
                    return "Lễ hội Halloween";
                case "7/11":
                    return "Ngày Việt Nam gia nhập WTO";
                case "20/11":
                    return "Ngày Nhà giáo Việt Nam";
                case "23/11":
                    return "Ngày Khởi nghĩa Nam Kỳ";
                case "1/12":
                    return "Ngày thế giới phòng chống AIDS";
                case "19/12":
                    return "Ngày toàn quốc kháng chiến";
                case "22/12":
                    return "Ngày thành lập Quân đội Nhân dân Việt Nam";
                case "25/12":
                    return "Lễ Giáng Sinh";
            }
        }
        return "";
    }

    public Date getDate(){
        return date;
    }

    public String getDayOfWeekInString()
    {
        if(date!=null)
        {
            if(date.getDay() == 0) return "Chủ Nhật";
            else
                return "Thứ " + (date.getDay() + 1);
        }
        return "";
    }

    public int getDayOfYear()
    {
        if(date!=null)
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.DAY_OF_YEAR);
        }
        return 0;
    }

    public int getDayOfMonth()
    {
        if(date!=null)
        {
            return date.getDate();
        }
        return 0;
    }
    public int getDayOfWeek()
    {
        if(date!=null)
        {
            return date.getDay();
        }
        return 0;
    }
    public int getMonth()
    {
        if(date!=null)
        {
            return date.getMonth() + 1;
        }
        return 0;
    }

    public int getYear()
    {
        if(date!=null)
        {
            return date.getYear() + 1900;
        }
        return 0;
    }

    public String getDisplaySolarDate()
    {
        if(date!=null)
        {
            return String.format("%1$02d-%2$02d-%3$04d", getDayOfMonth(), getMonth(), getYear());
        }
        return "";
    }

    public int getThapNhiBatTu()
    {
        if(date!=null) {
            Date orginalDate = new Date(1994 - 1900, 12 - 1, 22); //sao Giác (số 1) ứng với sô 0
            int index = new Long(((date.getTime() - orginalDate.getTime()) / (1000 * 24 * 3600)) % 28).intValue();
            if (index < 0) index += 28;
            return index + 1; //start from 1
        }
        return -1;
    }

    public int getGoodDateLevel()
    {
        int index = getThapNhiBatTu();
        switch (index)
        {
            case ThapNhiBatTu.SaoDe:
            case ThapNhiBatTu.SaoTaam:
            case ThapNhiBatTu.SaoHu:
            case ThapNhiBatTu.SaoChuy:
            case ThapNhiBatTu.SaoLieu:
                return 0;
            case ThapNhiBatTu.SaoCang:
            case ThapNhiBatTu.SaoNguu:
            case ThapNhiBatTu.SaoNu:
            case ThapNhiBatTu.SaoNguy:
            case ThapNhiBatTu.SaoDuc:
                return 1;
            case ThapNhiBatTu.SaoQuy:
                return 2;
            case ThapNhiBatTu.SaoKhue:
            case ThapNhiBatTu.SaoMao:
            case ThapNhiBatTu.SaoTinh:
            case ThapNhiBatTu.SaoSam:
                return 3;
            case ThapNhiBatTu.SaoGiac:
            case ThapNhiBatTu.SaoVix:
            case ThapNhiBatTu.SaoCo:
            case ThapNhiBatTu.SaoDau:
            case ThapNhiBatTu.SaoLau:
            case ThapNhiBatTu.SaoVij:
            case ThapNhiBatTu.SaoTat:
            case ThapNhiBatTu.SaoTirnh:
            case ThapNhiBatTu.SaoTruong:
            case ThapNhiBatTu.SaoChuan:
                return 4;
            case ThapNhiBatTu.SaoPhong:
            case ThapNhiBatTu.SaoThat:
            case ThapNhiBatTu.SaoBich:
                return 5;
        }
        return -1;
    }


}

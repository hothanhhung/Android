package com.hunght.data;

import java.util.Date;

/**
 * Created by Lenovo on 4/20/2018.
 */

public class NoteItem {
    public String Id;
    public int Year;
    public int Month;
    public int DateOfMonth;
    public String Subject;
    public String Content;
    public int RemindType;
    public boolean haveDate;

    DateItemForGridview dateItemForGridviewItem = null;
    public DateItemForGridview getDateItem()
    {
        if(dateItemForGridviewItem == null)
        {
            dateItemForGridviewItem = new DateItemForGridview(null, new Date(Year - 1900, Month - 1, DateOfMonth), false);
        }
        return dateItemForGridviewItem;
    }

    public NoteItem()
    {
        haveDate = false;
    }

    public NoteItem(String id, int d, int m, int y, String subj, String cont,int remindType )
    {
        this.Id = id;
        this.DateOfMonth = d;
        this.Month = m;
        this.Year = y;
        this.Subject = subj;
        this.Content = cont;
        this.RemindType = remindType;
        haveDate = true;
    }

    public void updateDate(int d, int m, int y)
    {
        this.DateOfMonth = d;
        this.Month = m;
        this.Year = y;
        dateItemForGridviewItem = null;
        haveDate = true;
    }

    public void setSubject(String subj)
    {
        this.Subject = subj;
    }

    public void setContent(String cont)
    {
        this.Content = cont;
    }

    public void setSubjectAndContent(String subj, String cont)
    {
        this.Subject = subj;
        this.Content = cont;
    }

    public void setRemindType(int remindType)
    {
        this.RemindType = remindType;
    }

    public String getId() {
        return Id;
    }

    public int getYear() {
        return Year;
    }

    public int getMonth() {
        return Month;
    }

    public int getDateOfMonth() {
        return DateOfMonth;
    }

    public String getSubject() {
        return Subject;
    }

    public String getContent() {
        return Content;
    }

    public String getContentInSomeText() {
        return Content;
    }

    public int getRemindType() {
        return RemindType;
    }

    public DateItemForGridview getRemindDate()
    {
        if(RemindType == 0)
        {
            return getDateItem();
        }
        return getDateItem();
    }

    public boolean haveDate()
    {
        return haveDate;
    }
}

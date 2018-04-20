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
    }

    private void updateDate(int d, int m, int y)
    {
        this.DateOfMonth = d;
        this.Month = m;
        this.Year = y;
        dateItemForGridviewItem = null;
    }

    private void setSubject(String subj)
    {
        this.Subject = subj;
    }

    private void setContent(String cont)
    {
        this.Content = cont;
    }

    private void setSubjectAndContent(String subj, String cont)
    {
        this.Subject = subj;
        this.Content = cont;
    }

    private void setRemindType(int remindType)
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

    public int getRemindType() {
        return RemindType;
    }
}

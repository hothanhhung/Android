package com.hunght.data;

import java.util.Date;

/**
 * Created by Lenovo on 4/20/2018.
 */

public class NoteItem implements Comparable<NoteItem> {
    public String Id;
    public int Year;
    public int Month;
    public int DateOfMonth;
    public String Subject;
    public String Content;
    public int RemindType;
    public boolean haveDate;

    DateItemForGridview dateItemForGridviewItem = null;
    DateItemForGridview remindDateItemForGridviewItem = null;
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
        remindDateItemForGridviewItem = null;
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

    public boolean hasContentInSomeText() {
        return !(Content==null || Content.isEmpty());
    }

    public String getContentInSomeText() {
        return Content;
    }

    public int getRemindType() {
        return RemindType;
    }

    public boolean isToday()
    {
        DateItemForGridview data = getRemindDate();
        if(data!=null) return data.isToday();
        return false;
    }

    public DateItemForGridview getRemindDate() {
        if(remindDateItemForGridviewItem == null){
            remindDateItemForGridviewItem = calRemindDate();
        }
        return remindDateItemForGridviewItem;
    }
    private DateItemForGridview calRemindDate() {

        DateItemForGridview now = new DateItemForGridview(null, new Date(), true);
        DateItemForGridview setupTime = getDateItem();
        DateItemForGridview returnTime = null;
        if(setupTime.compare(now)>=0)
        {
            return setupTime;
        }else {
            int offset = 0;
            switch (RemindType) {
                case 1:
                    returnTime = DateItemForGridview.createDateItemForGridview(setupTime.getDayOfMonth(), now.getMonth(),  now.getYear(), false, false);
                    if(returnTime.getDayOfMonth() < now.getDayOfMonth() )
                    {
                        returnTime.addMonth(1);
                    }
                    return returnTime;
                case 2:
                    returnTime = DateItemForGridview.createDateItemForGridview( setupTime.getLunarDate().getDate(), now.getLunarDate().getMonth(), now.getLunarDate().getYear(), false, true);
                    if(returnTime.getLunarDate().getDate() < now.getLunarDate().getDate())
                    {
                        returnTime = DateItemForGridview.createDateItemForGridview( setupTime.getLunarDate().getDate(), now.getLunarDate().getMonth() + 1, now.getLunarDate().getYear(), false, true);
                    }
                    return returnTime;
                case 3:
                    returnTime = DateItemForGridview.createDateItemForGridview( setupTime.getDayOfMonth(), setupTime.getMonth(), now.getYear(), false, false);
                    if(returnTime.getMonth()< now.getMonth() || (returnTime.getMonth() == now.getMonth() && returnTime.getDayOfMonth()< now.getDayOfMonth()))
                    {
                        returnTime.addYear(1);
                    }
                    return returnTime;
                case 4:
                    returnTime = DateItemForGridview.createDateItemForGridview( setupTime.getLunarDate().getDate(), setupTime.getLunarDate().getMonth(), now.getLunarDate().getYear(), false, true);
                    if(returnTime.getLunarDate().getMonth()< now.getLunarDate().getMonth() || (returnTime.getLunarDate().getMonth() == now.getLunarDate().getMonth() && returnTime.getLunarDate().getDate()< now.getLunarDate().getDate()))
                    {
                        returnTime = DateItemForGridview.createDateItemForGridview( setupTime.getLunarDate().getDate(), setupTime.getLunarDate().getMonth(), now.getLunarDate().getYear() + 1, false, true);
                    }
                    return returnTime;
            }
            return null;
        }
    }

    public boolean haveDate()
    {
        return haveDate;
    }
    @Override
    public int compareTo(NoteItem noteItem) {
        // sort desc
        return -1 * compareToSort(noteItem);
    }
    private int compareToSort(NoteItem noteItem) {
        if(noteItem == null)
        {
            return 1;
        }
        DateItemForGridview thisRemindDate = this.getRemindDate();
        DateItemForGridview thisDate = this.getDateItem();

        DateItemForGridview otherRemindDate = noteItem.getRemindDate();
        DateItemForGridview otherDate = noteItem.getDateItem();

        if(thisRemindDate != null)
        {
            if(otherRemindDate == null)
            {
                return 1;
            }
            int diff = thisRemindDate.difference(otherRemindDate);
            if(diff < 0) return 1;
            if (diff > 0) return -1;
            return 0;
        }
        if(otherRemindDate != null) return -1;

        // compare date
        if(thisDate != null)
        {
            if(otherDate == null)
            {
                return 1;
            }
            int diff = thisDate.difference(otherDate);
            if(diff < 0) return 1;
            if (diff > 0) return -1;
            return 0;
        }
        if(otherDate != null) return -1;

        return 0;
    }
}

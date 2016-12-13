package com.hth.service;

import java.util.Date;

public class Desk {
    public String ID;
    public String Name;

    public String AreaId;
    public Areas Areas;
    public boolean IsActive;
    public boolean IsUpdate;
    public String UserId;
    public String CreatedDate;
    public String UpdatedDate;
    public boolean IsUsing;

    public String getAreaId() {
        return AreaId;
    }

    public com.hth.service.Areas getAreas() {
        return Areas;
    }

    public String getName() {
        return Name;
    }
    public String getFullName() {
        return (Areas==null?"":Areas.getName()+">") + Name;
    }
    public boolean isActive() {
        return IsActive;
    }

    public String getID() {
        return ID;
    }

    public boolean IsUsing() {
        return IsUsing;
    }

    public void setUsing() {
        IsUsing = true;
    }
}

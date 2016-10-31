package com.hth.service;

import java.util.ArrayList;
import java.util.Date;

public class Areas {
    public Areas() {
    }
    public Areas(Areas area) {
        this.ID=area.ID;
        this.Name=area.Name;
        this.Image=area.Image;
        this.IsActive=area.IsActive;
        this.IsUpdate=area.IsUpdate;
        this.UserId=area.UserId;
        this.CreatedDate=area.CreatedDate;
        this.UpdatedDate=area.UpdatedDate;
    }
    public String ID;
    public String Name;
    public byte[] Image;
    public boolean IsActive;
    public boolean IsUpdate;
    public String UserId;
    public String CreatedDate;
    public String UpdatedDate;

    ArrayList<Desk> Desks;

    public ArrayList<Desk> getDesks() {
        if(Desks == null){
            Desks = new ArrayList<>();
        }
        return Desks;
    }

    public void setDesks(ArrayList<Desk> desks) {
        Desks = desks;
    }

    public String getID() {
        return ID;
    }

    public boolean isActive() {
        return IsActive;
    }

    public String getName() {
        return Name;
    }

}
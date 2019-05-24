package com.hth.owner;

public class AdsItem {
    public String Name;
    public String UrlImage;
    public String Desc;
    public String Link;
    public String AdsId;
    public boolean shown;

    public AdsItem(){}

    public AdsItem(String name, String image, String desc, String link){
        this.AdsId = "11111111";
        this.Name = name;
        this.UrlImage = image;
        this.Desc = desc;
        this.Link = link;
        this.shown = false;
    }

    public boolean isTheSame(AdsItem item){
        return (item != null && AdsId.equalsIgnoreCase(item.getId()));
    }

    public boolean haveId() {
        return AdsId!=null && !AdsId.isEmpty();
    }

    public boolean haveLink() {
        return Link!=null && !Link.isEmpty();
    }

    public String getLink() {
        return Link;
    }

    public String getName() {
        return Name;
    }

    public boolean haveImage() {
        return UrlImage!=null && !UrlImage.isEmpty();
    }

    public String getImage() {
        return UrlImage;
    }

    public String getDesc() {
        return Desc;
    }

    public String getId() {
        return AdsId;
    }

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }
}

namespace hthservices.Ads
{
    public class AdFlexData
    {

        public string title;
        public string desc;
        public string type;
        public string link;
        public string appid;
        public IconObject icon;

        public string getAppId()
        {
            if (appid == null) return "";
            return appid;
        }

        public void setName(string name)
        {
            title = name;
        }


        public string getName()
        {
            if (title == null) return "";
            return title;
        }

        public void setDesc(string desc)
        {
            this.desc = desc;
        }

        public string getDesc()
        {
            if (desc == null) return "";
            return desc;
        }

        public string GetFullType()
        {
            if (type == null) return "";
            else if (type.Contains("gplay")) return "Google play";
            else if (type.Contains("apk")) return "File APK";
            return type;
        }

        public void setType(string type)
        {
            this.type = type;
        }

        public string getLink()
        {
            if (link == null) return "";
            return link;
        }

        public void setLink(string link)
        {
            this.link = link;
        }

        public string getUrlImage()
        {
            if (icon != null) return icon.getfullLink();
            return "";
        }
        public void setUrlImage(string fullLink)
        {
            if (icon == null)
            {
                icon = new IconObject();
            }
            icon.setfullLink(fullLink);

        }
        public class IconObject
        {
            public string full;
            public string getfullLink()
            {
                return full;
            }
            public void setfullLink(string fullLink)
            {
                this.full = fullLink;
            }
        }
    }
}
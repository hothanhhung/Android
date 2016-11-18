using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace hthservices.Ads
{
    public class AdItem
    {
        public String NameVN;
        public String Name;
        public String DescVN;
        public String Desc;
        public String Type;
        public String Link;
        public String AppId;
        public String UrlImage;

        /*---------- For owner ads --------- */

        public string InCountries; //VN, EN, 

        public string IgnoreCountries; //VN, EN, 

        public string GetName(string country)
        {
            if ("VN".Equals(country, StringComparison.OrdinalIgnoreCase))
            {
                if (!string.IsNullOrWhiteSpace(NameVN)) return NameVN;
            }
            return Name;
        }

        public string GetDesc(string country)
        {
            if ("VN".Equals(country, StringComparison.OrdinalIgnoreCase))
            {
                if (!string.IsNullOrWhiteSpace(DescVN)) return DescVN;
            }
            return Desc;
        }
        public bool IsAllowForCountry(string country)
        {
            if (!string.IsNullOrWhiteSpace(country))
            {
                string countryInFormat = country.ToLower() + ",";
                if (!string.IsNullOrWhiteSpace(InCountries))
                {
                    string inCountriesInformat = InCountries.Trim().ToLower() + ",";
                    if (inCountriesInformat.ToLower().Contains(countryInFormat.ToLower())) { return true; }
                    else { return false; }
                }
                if (!string.IsNullOrWhiteSpace(IgnoreCountries))
                {
                    string ignoreCountries = IgnoreCountries.Trim().ToLower() + ",";
                    if (ignoreCountries.ToLower().Contains(countryInFormat.ToLower())) { return false; }
                    else { return true; }
                }
            }
            return true;
        }

        private string packageName = null;
        [IgnoreDataMember]
        public string PackageName
        {
            get
            {
                if (packageName == null)
                {
                    if (!String.IsNullOrWhiteSpace(Link))
                    {
                        var start = Link.LastIndexOf("=", StringComparison.OrdinalIgnoreCase);
                        if (start > 0)
                        {
                            packageName = Link.Substring(start + 1).Trim().ToLower();
                        }
                    }
                    else
                    {
                        packageName = String.Empty;
                    }
                }
                return packageName;
            }
        }
    }
}
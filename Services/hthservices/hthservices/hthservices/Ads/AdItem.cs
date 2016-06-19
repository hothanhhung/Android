using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace hthservices.Ads
{
    public class AdItem
    {
        public String Name;
        public String Desc;
        public String Type;
        public String Link;
        public String AppId;
        public String UrlImage;

        /*---------- For owner ads --------- */

        public string InCountries; //VN, EN, 

        public string IgnoreCountries; //VN, EN, 

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
    }
}
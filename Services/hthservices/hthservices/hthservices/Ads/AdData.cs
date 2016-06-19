using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Web;

namespace hthservices.Ads
{
    public class AdData
    {
        private const String ADFLEX_URL = "http://api.adflex.vn/api/?action=campaign&refcode=thanhhung1012&country={0}&os={1}";

        private static List<AdItem> GetAdFromAdFlex(string country, string os = "android")
        {
            string url = String.Format(ADFLEX_URL, country, os);

            List<AdItem> adItems = new List<AdItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    var response = http.GetByteArrayAsync(url).Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                    if (!source.EndsWith("]")) source += "]";
                    var items = JsonConvert.DeserializeObject<List<AdFlexData>>(source);

                    if (items != null)
                    {
                        foreach (var item in items)
                        {
                            var adItem = new AdItem();
                            adItem.Name = item.getName();
                            adItem.Desc = item.getDesc();
                            adItem.Type = item.GetFullType();
                            adItem.Link = item.getLink();
                            adItem.AppId = item.getAppId();
                            adItem.UrlImage = item.getUrlImage();
                            adItems.Add(adItem);
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return adItems;
        }

        public static List<AdItem> GetAds(string country, string os = "android")
        {
            var adItems = new List<AdItem>();
            var ownerAds = OwnerAds.OWNER_ADITEMS.Where(p => p.IsAllowForCountry(country));
            if (ownerAds != null)
            {
                adItems.AddRange(ownerAds);
            }
            adItems.AddRange(GetAdFromAdFlex(country, os));
            return adItems;
        }
    }
}
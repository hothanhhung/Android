using hthservices.Utils;
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
                        for(var i = items.Count; i>0; i--)
                        {
                            var item = items[i-1];
                            if (!item.getAppId().Contains("gameloft"))
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
            }
            catch (Exception ex)
            {

            }

            return adItems;
        }

        public static List<AdItem> GetAds(string country, string os = "android", string requestLink = "", string device = "", string open = "", string version = "", string package = "")
        {
            var adItems = new List<AdItem>();
            var ownerAds = OwnerAds.GetOwnerAds(country, package);
            if (ownerAds != null)
            {
                adItems.AddRange(ownerAds);
            }
            //adItems.AddRange(GetAdFromAdFlex(country, os));
            try
            {
                System.Threading.Thread th = new System.Threading.Thread(() =>
                {
                    SQLiteProcess.SaveRequestInfo("GetAds", DateTime.UtcNow.AddHours(7), adItems == null || adItems.Count == 0, requestLink);
                });
                th.IsBackground = true;
                th.Start();
            }
            catch (Exception ex) { }
            return adItems;
        }

        public static void UserClickAd(string country, string os, string requestLink, string link, string device, string open, string version, string package)
        {            
            try
            {
                System.Threading.Thread th = new System.Threading.Thread(() =>
                {
                    SQLiteProcess.SaveRequestInfo("UserClickAd", DateTime.UtcNow.AddHours(7), false, requestLink);
                });
                th.IsBackground = true;
                th.Start();
            }
            catch (Exception ex) { }
        }

        public static AdItem GetPopupAds(string country, string package = "", string adsId = "")
        {
            var ownerAds = OwnerAds.OWNER_POPUP_ADS_ITEMS.Where(p=>p.PushToAppIds.Contains(package) && !p.AdsId.Equals(adsId)).FirstOrDefault();
            if (ownerAds != null)
            {
                ownerAds = new AdItem()
                {
                    Name = ownerAds.GetName(country),
                    Desc = ownerAds.GetDesc(country),
                    Link = ownerAds.Link,
                    UrlImage = ownerAds.UrlImage,
                    Type = ownerAds.Type,
                    AdsId = ownerAds.AdsId
                };
            }     
            return ownerAds;
        }

    }
}
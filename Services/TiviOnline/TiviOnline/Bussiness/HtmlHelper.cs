using HtmlAgilityPack;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Web;
using TiviOnline.Models;

namespace TiviOnline.Bussiness
{
    public class HtmlHelper
    {
        private static string GetStringContentFromResponse(HttpResponseMessage response, bool needHtmlDecode)
        {
            if (response.IsSuccessStatusCode)
            {
                var content = response.Content.ReadAsByteArrayAsync().Result;
                String source = Encoding.GetEncoding("utf-8").GetString(content, 0, content.Length - 1);
                if (needHtmlDecode) source = WebUtility.HtmlDecode(source);
                return source;
            }
            return string.Empty;
        }

        class ObjectSchedule
        {
            public List<ScheduleItem> Data;
        }

        public static List<ScheduleItem> GetSchedule(string channel, string date)
        {
            List<ScheduleItem> items = null;
            string url = string.Format("http://hunght.com/api/lichtivi/GetSchedules/?channel={0}&date={1}&device=TiviOnline&open={2}&version=4.0.2", channel, date, DateTime.Now.Ticks);
            try
            {
                HttpClient http = new HttpClient();
                var response = http.GetAsync(url).Result;
                if (response.IsSuccessStatusCode)
                {
                    HtmlDocument resultat = new HtmlDocument();
                    var contentJson = GetStringContentFromResponse(response, false).Trim();
                    if (!contentJson.EndsWith("}")) contentJson += "}";
                    var obj = JsonConvert.DeserializeObject<ObjectSchedule>(contentJson);
                    if (obj != null)
                    {
                        items = obj.Data;
                        foreach(var item in items)
                        {
                            item.DecodedProgramName = hthservices.Utils.MethodHelpers.DecodeProgramName(item.ProgramName);
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }
            return items;
        }

        private class TVNetStreamJson
        {
            public string name;
            public string url;
        }
        //http://vn.tvnet.gov.vn/kenh-truyen-hinh/1011/vtv1
        public static string GetUrlStreamFromTVNet(string url)
        {
            string streamUrl = string.Empty;
            try
            {
                HttpClient http = new HttpClient();
                var response = http.GetAsync(url).Result;
                if (response.IsSuccessStatusCode)
                {
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(GetStringContentFromResponse(response, true));

                    var chanelMediaplayer = resultat.DocumentNode.SelectSingleNode("//div[@id='mediaplayer']");
                    if (chanelMediaplayer != null)
                    {
                        var requestStreamUrl = chanelMediaplayer.GetAttributeValue("data-file", string.Empty);
                        if (!string.IsNullOrWhiteSpace(requestStreamUrl))
                        {
                            var responseStreamObject = http.GetAsync(requestStreamUrl).Result;
                            if(responseStreamObject.IsSuccessStatusCode)
                            {
                                var contentJson = GetStringContentFromResponse(responseStreamObject, false).Trim();
                                if (!contentJson.EndsWith("]")) contentJson += "]";
                                var items = JsonConvert.DeserializeObject<List<TVNetStreamJson>>(contentJson);
                                if(items!=null && items.Count > 0)
                                {
                                    streamUrl = items[0].url;
                                }

                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }
            return streamUrl;
        }

        public static string GetUrlFromVietBao(string url)
        {
            string streamUrl = string.Empty;
            try
            {
                HttpClient http = new HttpClient();
                var response = http.GetAsync(url).Result;
                if (response.IsSuccessStatusCode)
                {
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(GetStringContentFromResponse(response, true));

                    var chanelMediaplayer = resultat.DocumentNode.SelectSingleNode("//div[@class='view-online']/iframe");
                    if (chanelMediaplayer != null)
                    {
                        streamUrl = chanelMediaplayer.GetAttributeValue("src", string.Empty);                        
                    }
                }
            }
            catch (Exception ex)
            {

            }
            return streamUrl;
        }
   
        public static string GetContent(string url)
        {
            string streamUrl = string.Empty;
            try
            {
                Uri uri = new Uri(url);
                HttpClient http = new HttpClient();
                http.DefaultRequestHeaders.Add("X-Requested-With", "XMLHttpRequest");
                http.DefaultRequestHeaders.Add("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:33.0) Gecko/20100101 Firefox/33.0");
                http.DefaultRequestHeaders.Add("Referer", uri.Host);
                http.DefaultRequestHeaders.Add("Pragma", "no-cache");
                http.DefaultRequestHeaders.Add("Connection", "keep-alive");
                http.DefaultRequestHeaders.Add("Accept-Language", "en-US,en;q=0.5");
                http.DefaultRequestHeaders.Add("Accept-Encoding", "gzip, deflate");
                var response = http.GetAsync(url).Result;
                if (response.IsSuccessStatusCode)
                {
                    HtmlDocument resultat = new HtmlDocument();
                    streamUrl = GetStringContentFromResponse(response, true);

                }
            }
            catch (Exception ex)
            {

            }
            return streamUrl;
        }


        //http://m.xemtvhd.com/htv7.php
        public static string GetUrlStreamFromXemTVHD(string url)
        {
            string streamUrl = string.Empty;
            try
            {
                HttpClient http = new HttpClient();
                var response = http.GetAsync(url).Result;
                if (response.IsSuccessStatusCode)
                {
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(GetStringContentFromResponse(response, true));

                    var chanelMediaplayer = resultat.DocumentNode.SelectSingleNode("//div[@id='mediaplayer']");
                    if (chanelMediaplayer != null)
                    {
                        var requestStreamUrl = chanelMediaplayer.GetAttributeValue("data-file", string.Empty);
                        if (!string.IsNullOrWhiteSpace(requestStreamUrl))
                        {
                            var responseStreamObject = http.GetAsync(requestStreamUrl).Result;
                            if (responseStreamObject.IsSuccessStatusCode)
                            {
                                var contentJson = GetStringContentFromResponse(responseStreamObject, false).Trim();
                                if (!contentJson.EndsWith("]")) contentJson += "]";
                                var items = JsonConvert.DeserializeObject<List<TVNetStreamJson>>(contentJson);
                                if (items != null && items.Count > 0)
                                {
                                    streamUrl = items[0].url;
                                }

                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }
            return streamUrl;
        }
    }
}
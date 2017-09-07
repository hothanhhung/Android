using HtmlAgilityPack;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Web;

namespace hthservices.Utils
{
    public class HtmlHelper
    {
        const string VIETBAO_HOME_PAGE = "http://tv.vietbao.vn";
        const string VIETBAO_SEARCH_PAGE = VIETBAO_HOME_PAGE + "/tim-kiem-lich-phat-song.html?p={0}&s-st=-1&s-et=-1&s-d={1}&s-t=tat-ca&stationID={2}";
        static public List<GuideItem> GetDataFromVietBaoUrl(Channel channel, DateTime date)
        {
            string url = VIETBAO_HOME_PAGE + channel.LinkVietBao.Replace(".html", "/ngay-" + date.Day + "-" + date.Month + "-" + date.Year + ".html");

            List<GuideItem> guideItems = new List<GuideItem>();
            try{
                HttpClient http = new HttpClient();
                var response = http.GetAsync(url).Result;
                if (url.Equals(response.RequestMessage.RequestUri.ToString(), StringComparison.OrdinalIgnoreCase))
                {
                    var content = response.Content.ReadAsByteArrayAsync().Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(content, 0, content.Length - 1);
                    source = WebUtility.HtmlDecode(source);
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(source);

                    var chanelDetails = resultat.DocumentNode.SelectNodes("//div[@class='chanel-detail']");
                    if (chanelDetails != null && chanelDetails.Count > 0)
                    {
                        var chanelDetail = chanelDetails.LastOrDefault();
                        if (chanelDetail != null)
                        {
                            var items = chanelDetail.SelectNodes("div[@class='row']");
                            if (items != null)
                            {
                                foreach (var item in items)
                                {
                                    string startOn = "", programName = "";
                                    // get start time
                                    var timeTV = item.SelectNodes("div[@class='time-tv']");
                                    if (timeTV != null && timeTV.Count > 0)
                                    {
                                        startOn = timeTV.FirstOrDefault().InnerText.Trim();
                                    }
                                    var tDetail = item.SelectNodes("div[@class='t-detail']");

                                    // get program name
                                    if (tDetail != null && tDetail.Count > 0)
                                    {
                                        programName = tDetail.FirstOrDefault().InnerText.Trim();
                                    }
                                    var guideItem = new GuideItem() { ChannelKey = channel.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                    guideItems.Add(guideItem);
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }
        
            return guideItems;
        }
        static public List<GuideItem> GetVTC14Url(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server);

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                HttpClient http = new HttpClient();
                var response = http.GetByteArrayAsync(url).Result;
                String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                source = WebUtility.HtmlDecode(source);
                HtmlDocument resultat = new HtmlDocument();
                resultat.LoadHtml(source);

                var chanelDetails = resultat.DocumentNode.SelectNodes("//div[@class='lps']");
                if (chanelDetails != null && chanelDetails.Count > 0)
                {
                    var chanelDetail = chanelDetails.FirstOrDefault();
                    if (chanelDetail != null)
                    {
                        var items = chanelDetail.SelectNodes("table//tr");
                        if (items != null && items.Count > 0)
                        {
                            items.RemoveAt(0);
                            foreach (var item in items)
                            {
                                string startOn = "", programName = "";
                                // get start time
                                var tdTags = item.SelectNodes("td");
                                if (tdTags != null && tdTags.Count > 0)
                                {
                                    startOn = tdTags.ElementAt(0).InnerText.Trim();
                                }
                                if (tdTags != null && tdTags.Count > 1)
                                {
                                    programName = tdTags.ElementAt(1).InnerText.Trim();
                                }
                                if (!string.IsNullOrWhiteSpace(startOn))
                                {
                                    var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                    guideItems.Add(guideItem);
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }
        static public List<GuideItem> GetDataFromVOVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server + "day={0}", date.ToString("dd/MM/yyyy"));

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                HttpClient http = new HttpClient();
                var response = http.GetByteArrayAsync(url).Result;
                String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                source = WebUtility.HtmlDecode(source);
                HtmlDocument resultat = new HtmlDocument();
                resultat.LoadHtml(source);

                var chanelDetails = resultat.DocumentNode.SelectNodes("//div[@class='ScheduleDetails']");
                if (chanelDetails != null && chanelDetails.Count > 0)
                {
                    var chanelDetail = chanelDetails.FirstOrDefault();
                    if (chanelDetail != null)
                    {
                        var items = chanelDetail.SelectNodes("div//tr");
                        if (items != null)
                        {
                            foreach (var item in items)
                            {
                                string startOn = "", programName = "";
                                // get start time
                                var tdTags = item.SelectNodes("td");
                                if (tdTags != null && tdTags.Count > 0)
                                {
                                    var time = tdTags.ElementAt(0).InnerText;
                                    int length = time.IndexOf("-");
                                    startOn = time.Substring(0, length).Trim();
                                    startOn = startOn.Replace("H", ":");
                                } 
                                if (tdTags != null && tdTags.Count > 1)
                                {
                                    programName = MethodHelpers.ToTitleCase(tdTags.ElementAt(1).InnerText.Trim());
                                }
                                if (tdTags != null && tdTags.Count > 2)
                                {
                                    var more = tdTags.ElementAt(2).InnerText.Trim();
                                    if(!string.IsNullOrWhiteSpace(more)){
                                        programName = programName + ": " + more;
                                    }
                                }
                                if (!string.IsNullOrWhiteSpace(startOn))
                                {
                                    var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                    guideItems.Add(guideItem);
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        static public List<GuideItem> GetDataFromMyTVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server);

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                HttpWebRequest req = (HttpWebRequest)WebRequest.Create(url);
                {
                    var stringContent = "channelId=" + channelToServer.Value + "&dateSchedule=" + date.ToString("dd/MM/yyyy");
                    byte[] buffer = Encoding.UTF8.GetBytes(stringContent);
                    req.Host = "www.mytv.com.vn";
                    req.Referer = "https://www.mytv.com.vn/lich-phat-song";
                    req.Method = "POST";
                    req.ContentType = "application/x-www-form-urlencoded";
                    req.ContentLength = buffer.Length;
                    req.KeepAlive = true;
                    req.Timeout = System.Threading.Timeout.Infinite;
                    req.ProtocolVersion = HttpVersion.Version10;
                    req.AllowWriteStreamBuffering = false;
                    req.Accept = "application/json, text/javascript, */*; q=0.01";
                    req.UserAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0";

                    WebHeaderCollection myWebHeaderCollection = req.Headers;
                    myWebHeaderCollection.Add("Accept-Encoding: gzip, deflate, br");
                    myWebHeaderCollection.Add("Accept-Language: en-US,en;q=0.5");
                    myWebHeaderCollection.Add("X-Requested-With: XMLHttpRequest");
                    
                    //req.CookieContainer = new CookieContainer();
                    ////req.CookieContainer.Add(new Uri(url), new Cookie("__utma", "1.268565172.1470799796.1474424801.1474426720.10"));
                    ////req.CookieContainer.Add(new Uri(url), new Cookie("__utmz", "1.1470803901.2.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided)"));
                    //req.CookieContainer.Add(new Uri("https://www.mytv.com.vn/lich-phat-song"), new Cookie("PHPSESSID", "kphgfqvsajuipgooosthakvl35"));
                    //req.CookieContainer.Add(new Uri("https://www.mytv.com.vn/lich-phat-song"), new Cookie("__utma", "216719242.2069266193.1495166431.1495166431.1495166431.1"));
                    //req.CookieContainer.Add(new Uri("https://www.mytv.com.vn/lich-phat-song"), new Cookie("__utmb", "216719242.1.10.1495166431"));
                    //req.CookieContainer.Add(new Uri("https://www.mytv.com.vn/lich-phat-song"), new Cookie("__utmc", "216719242"));
                    //req.CookieContainer.Add(new Uri("https://www.mytv.com.vn/lich-phat-song"), new Cookie("__utmz", "216719242.1495166431.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)"));
                    //req.CookieContainer.Add(new Uri("https://www.mytv.com.vn/lich-phat-song"), new Cookie("__utmt", "1"));


                    Stream stream = req.GetRequestStream();
                    stream.Write(buffer, 0, buffer.Length);
                    stream.Close();
                    WebResponse response = req.GetResponse();

                    if (((HttpWebResponse)response).StatusDescription.Equals("OK", StringComparison.OrdinalIgnoreCase))
                    {
                        stream = response.GetResponseStream();
                        StreamReader reader = new StreamReader(stream);
                        string source = reader.ReadToEnd();
                        response.Close();
                        source = System.Text.RegularExpressions.Regex.Unescape(source);
                        source = WebUtility.HtmlDecode(source.Replace("\"", ""));
                        HtmlDocument resultat = new HtmlDocument();
                        resultat.LoadHtml(source);

                        var items = resultat.DocumentNode.SelectNodes("//p");

                        if (items != null)
                        {
                            foreach (var item in items)
                            {
                                string startOn = "", programName = "";
                                // get start time
                                var timeTV = item.SelectNodes("strong");
                                if (timeTV != null && timeTV.Count > 0)
                                {
                                    startOn = timeTV.FirstOrDefault().InnerText.Trim();
                                }
                                var tDetail = item.ChildNodes;

                                // get program name
                                if (tDetail != null && tDetail.Count > 1)
                                {
                                    programName = item.ChildNodes[1].InnerText.Trim();
                                }
                                var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                guideItems.Add(guideItem);
                            }
                        }
                    }
                }
                //using (var http = new HttpClient())
                //{
                //    //http.DefaultRequestHeaders.Add("X-Requested-With", "XMLHttpRequest");
                //    http.DefaultRequestHeaders.Add("Host", "www.mytv.com.vn");
                //    http.DefaultRequestHeaders.Add("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0");
                //    http.DefaultRequestHeaders.Add("Accept", "application/json, text/javascript, */*; q=0.01");
                //    http.DefaultRequestHeaders.Add("Accept-Language", "en-US,en;q=0.5");
                //    http.DefaultRequestHeaders.Add("Accept-Encoding", "gzip, deflate, br");
                //    http.DefaultRequestHeaders.Add("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                //    http.DefaultRequestHeaders.Add("X-Requested-With", "XMLHttpRequest");
                //    http.DefaultRequestHeaders.Add("Referer", "https://www.mytv.com.vn/lich-phat-song");
                //    http.DefaultRequestHeaders.Add("Cookie", "; ; ; =; =216719242.1495166431.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)");

                //    var stringContent = "channelId=" + channelToServer.Value + "&dateSchedule=" + date.ToString("dd/MM/yyyy");
                //    var httpContent = new StringContent(stringContent, Encoding.UTF8, "application/x-www-form-urlencoded");
                //    var response = http.PostAsync(url, httpContent).Result;

                //    if (response.IsSuccessStatusCode)
                //    {
                //        var bytes = response.Content.ReadAsByteArrayAsync().Result;
                //        String source = Encoding.GetEncoding("utf-8").GetString(bytes, 0, bytes.Length - 1);
                //        source = System.Text.RegularExpressions.Regex.Unescape(source);
                //        source = WebUtility.HtmlDecode(source.Replace("\"", ""));
                //        HtmlDocument resultat = new HtmlDocument();
                //        resultat.LoadHtml(source);

                //        var items = resultat.DocumentNode.SelectNodes("//p");

                //        if (items != null)
                //        {
                //            foreach (var item in items)
                //            {
                //                string startOn = "", programName = "";
                //                // get start time
                //                var timeTV = item.SelectNodes("strong");
                //                if (timeTV != null && timeTV.Count > 0)
                //                {
                //                    startOn = timeTV.FirstOrDefault().InnerText.Trim();
                //                }
                //                var tDetail = item.ChildNodes;

                //                // get program name
                //                if (tDetail != null && tDetail.Count > 1)
                //                {
                //                    programName = item.ChildNodes[1].InnerText.Trim();
                //                }
                //                var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                //                guideItems.Add(guideItem);
                //            }
                //        }
                //    }
                //}

            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        static public List<GuideItem> GetDataTVNetUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server, channelToServer.Value, date.ToString("dd/MM/yyyy"));

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                HttpClient http = new HttpClient();
                var response = http.GetByteArrayAsync(url).Result;
                String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                source = WebUtility.HtmlDecode(source);
                HtmlDocument resultat = new HtmlDocument();
                resultat.LoadHtml(source);

                var items = resultat.DocumentNode.SelectNodes("//tr");

                if (items != null)
                {
                    foreach (var item in items)
                    {
                        string startOn = "", programName = "";
                        // get start time
                        var tdTags = item.SelectNodes("td");
                        if (tdTags != null && tdTags.Count > 0)
                        {
                            startOn = tdTags.ElementAt(0).InnerText.Trim();
                        }
                        if (tdTags != null && tdTags.Count > 1)
                        {
                            programName = MethodHelpers.ToTitleCase(tdTags.ElementAt(1).InnerText.Trim());
                        }
                        var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                        guideItems.Add(guideItem);
                    }
                }


            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }
        static public List<GuideItem> GetDataFromTruyenHinhSoUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server, channelToServer.Value, date.ToString("dd/MM/yyyy"));

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                HttpClient http = new HttpClient();
                var response = http.GetByteArrayAsync(url).Result;
                String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                int start = source.IndexOf(",\"content\":\"") + 12, end = source.IndexOf("\",\"isSuccessful\":");
                source = source.Substring(start, end - start);
                source = System.Text.RegularExpressions.Regex.Unescape(source);
                source = WebUtility.HtmlDecode(source);
                HtmlDocument resultat = new HtmlDocument();
                resultat.LoadHtml(source);

                var items = resultat.DocumentNode.SelectNodes("//li");

                if (items != null)
                {
                    foreach (var item in items)
                    {
                        string startOn = "", programName = "";
                        // get start time
                        var divTags = item.SelectNodes("div");
                        if (divTags != null && divTags.Count > 0)
                        {
                            var time = divTags.ElementAt(0).InnerText;
                            int lenght = time.IndexOf("-");
                            startOn = time.Substring(0, lenght).Trim();
                        }
                        var tDetail = item.ChildNodes;

                        // get program name
                        if (divTags != null && divTags.Count > 1)
                        {
                            programName = divTags.ElementAt(1).InnerText.Trim();
                        }
                        var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                        guideItems.Add(guideItem);
                    }
                }


            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        static public List<GuideItem> GetDataFromPhut90Url(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server,channelToServer.Value, date.ToString("ddMMyyyy"));

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                HttpClient http = new HttpClient();
                var response = http.GetByteArrayAsync(url).Result;
                String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                source = WebUtility.HtmlDecode(source);
                HtmlDocument resultat = new HtmlDocument();
                resultat.LoadHtml(source);
                var schedulerMain = resultat.DocumentNode.SelectNodes("//div[@id='scheduler-main']");
                if (schedulerMain != null && schedulerMain.Count > 0)
                {
                    var chanelDetails = schedulerMain.FirstOrDefault().SelectNodes("div//table");
                    if (chanelDetails != null && chanelDetails.Count > 0)
                    {
                        var chanelDetail = chanelDetails.FirstOrDefault();
                        if (chanelDetail != null)
                        {
                            var items = chanelDetail.SelectNodes("tbody//tr");
                            if (items != null)
                            {
                                foreach (var item in items)
                                {
                                    string startOn = "", programName = "";
                                    // get start time
                                    var tdTags = item.SelectNodes("td");
                                    if (tdTags != null && tdTags.Count > 0)
                                    {
                                        startOn = tdTags.ElementAt(0).InnerText.Trim();
                                    }
                                    if (tdTags != null && tdTags.Count > 1)
                                    {
                                        programName = MethodHelpers.ToTitleCase(tdTags.ElementAt(1).InnerText.Trim());
                                    }
                                    if (tdTags != null && tdTags.Count > 2)
                                    {
                                        var more = tdTags.ElementAt(2).InnerText.Trim();
                                        if (!string.IsNullOrWhiteSpace(more))
                                        {
                                            programName = programName + ": " + more;
                                        }
                                    }
                                    if (!string.IsNullOrWhiteSpace(startOn))
                                    {
                                        var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                        guideItems.Add(guideItem);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }
        static public List<GuideItem> GetDataFromHTV3TVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server, date.ToString("yyyy-MM-dd"));

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                HttpClient http = new HttpClient();
                var response = http.GetByteArrayAsync(url).Result;
                String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                source = WebUtility.HtmlDecode(source);
                HtmlDocument resultat = new HtmlDocument();
                resultat.LoadHtml(source);

                var items = resultat.DocumentNode.SelectNodes("li");
                if (items != null)
                {
                    foreach (var item in items)
                    {
                        string startOn = "", programName = "";
                        // get start time
                        var timeTV = item.SelectNodes("div//div[@class='time-show-cal']");
                        if (timeTV != null && timeTV.Count > 0)
                        {
                            var spans = timeTV.FirstOrDefault().SelectNodes("span");
                            if (spans != null && spans.Count > 0)
                            {
                                startOn = spans.ElementAt(0).InnerText.Trim();
                            }
                        }
                        var tDetails = item.SelectNodes("div//div[@class='detail-show']");

                        // get program name
                        if (tDetails != null && tDetails.Count > 0)
                        {
                            var tDetail = tDetails.FirstOrDefault();
                            if(tDetail!=null){
                                var spans = tDetail.SelectNodes("span");
                                var h4s = tDetail.SelectNodes("h4");
                                if (spans != null && spans.Count > 0)
                                {
                                    if (h4s == null || h4s.Count == 0)
                                    {
                                        programName = spans.FirstOrDefault().InnerText.Trim();
                                    }
                                    else
                                    {
                                        programName = spans.FirstOrDefault().InnerText.Trim() + ": " + h4s.FirstOrDefault().InnerText.Trim();
                                    }
                                }
                                else
                                {
                                    if (h4s != null && h4s.Count > 0)
                                    {
                                        programName = h4s.FirstOrDefault().InnerText.Trim();
                                    }
                                }
                            }
                             
                        }
                        if (!string.IsNullOrWhiteSpace(startOn))
                        {
                            var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                            guideItems.Add(guideItem);
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }
        static public List<GuideItem> GetDataFromHTV2ChannelTVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server, date.ToString("yyyy/MM/dd"));

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                HttpClient http = new HttpClient();
                var response = http.GetByteArrayAsync(url).Result;
                String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                source = WebUtility.HtmlDecode(source);
                HtmlDocument resultat = new HtmlDocument();
                resultat.LoadHtml(source);
                var schedulerMain = resultat.DocumentNode.SelectNodes("//div[@class='lichchieu_detail']");
                if (schedulerMain != null && schedulerMain.Count > 0)
                {
                    var chanelDetail = schedulerMain.FirstOrDefault();
                    if (chanelDetail != null)
                    {
                        var items = chanelDetail.SelectNodes("table//tr");
                        if (items != null)
                        {
                            foreach (var item in items)
                            {
                                string startOn = "", programName = "";
                                // get start time
                                var thTags = item.SelectNodes("th");
                                if (thTags != null && thTags.Count > 0)
                                {
                                    startOn = thTags.ElementAt(0).InnerText.Trim();
                                }
                                var tdTags = item.SelectNodes("td");
                                if (tdTags != null && tdTags.Count > 0)
                                {
                                    var pTags = tdTags.FirstOrDefault().SelectNodes("p");
                                    if (pTags != null && pTags.Count > 0)
                                    {
                                        programName = pTags.ElementAt(0).InnerText.Trim();
                                    }
                                    if (pTags != null && pTags.Count > 1)
                                    {
                                        var more = pTags.ElementAt(1).InnerText.Trim();
                                        if (!string.IsNullOrWhiteSpace(more))
                                        {
                                            programName = programName + ": " + more;
                                        }
                                    }
                                }
                                if (!string.IsNullOrWhiteSpace(startOn))
                                {
                                    var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                    guideItems.Add(guideItem);
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }
        static public List<GuideItem> GetFromQPVNUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server,date.ToString("dd/MM/yyyy"));

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                HttpClient http = new HttpClient();
                var response = http.GetByteArrayAsync(url).Result;
                String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                source = WebUtility.HtmlDecode(source);
                HtmlDocument resultat = new HtmlDocument();
                resultat.LoadHtml(source.Replace("<br/>",": "));

                var items = resultat.DocumentNode.SelectNodes("table//tr");
                if (items != null && items.Count > 0)
                {
                    foreach (var item in items)
                    {
                        string startOn = "", programName = "";
                        // get start time
                        var tdTags = item.SelectNodes("td");
                        if (tdTags != null && tdTags.Count > 0)
                        {
                            startOn = tdTags.ElementAt(0).InnerText.Trim();
                        }
                        if (tdTags != null && tdTags.Count > 1)
                        {
                            var str = tdTags.ElementAt(1).InnerText;
                            int length = str.IndexOf(": ");
                            var title = str.Substring(0, length).Trim();
                            var detail = str.Substring(length);
                            programName = MethodHelpers.ToTitleCase(title) + (detail.Length > 3 ? detail : "");
                        }
                        if (!string.IsNullOrWhiteSpace(startOn))
                        {
                            var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                            guideItems.Add(guideItem);
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }
        static public List<GuideItem> GetDataFromHTVPLUSUrl(ChannelToServer channelToServer, int index, DateTime date)
        {
            string url = channelToServer.Server;

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    http.DefaultRequestHeaders.Add("X-Requested-With", "XMLHttpRequest");
                   // http.DefaultRequestHeaders.Add("Referer", "http://hplus.com.vn/xem-kenh-htv1-2631.html");
                   
                    var stringContent = string.Format("id={0}&num={1}", channelToServer.Value, index);
                    var httpContent = new StringContent(stringContent, Encoding.UTF8, "application/x-www-form-urlencoded");
                    var response = http.PostAsync(url, httpContent).Result;
                   // http.BaseAddress = new Uri(url);
                    
                    //var response = http.PostAsync(url, content).Result;
                    if (response.IsSuccessStatusCode)
                    {
                        var bytes = response.Content.ReadAsByteArrayAsync().Result;
                        String source = Encoding.GetEncoding("utf-8").GetString(bytes, 0, bytes.Length - 1);
                        source = System.Text.RegularExpressions.Regex.Unescape(source);
                        source = WebUtility.HtmlDecode(source);
                        HtmlDocument resultat = new HtmlDocument();
                        resultat.LoadHtml(source);

                        var items = resultat.DocumentNode.SelectNodes("/div/div");
                        if (items != null)
                        {
                            foreach (var item in items)
                            {
                                if (item.ChildNodes.Count != 5) continue;

                                string startOn = "", programName = "";
                                // get start time
                                var times = item.ChildNodes[1].InnerText.Trim();
                                if (times != null && times.Length > 5)
                                {
                                    startOn = times.Substring(0, 5);                                    
                                }
                                var details = item.ChildNodes[2].InnerText.Trim();
                                if (details != null && details.Length > 0)
                                {
                                    programName = details.Trim(':').Trim();
                                }
                                if (!string.IsNullOrWhiteSpace(startOn))
                                {
                                    var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                    guideItems.Add(guideItem);
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        static public List<GuideItem> GetDataFromFBNCUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server, date.ToString("ddd+MMM+dd+yyyy"));

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    var response = http.GetByteArrayAsync(url).Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                    source = WebUtility.HtmlDecode(source);
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(source);
                    var schedulerMain = resultat.DocumentNode.SelectNodes("//div[@class='schedule-list']");
                    if (schedulerMain != null && schedulerMain.Count > 0)
                    {
                        var items = schedulerMain.FirstOrDefault().SelectNodes("div");
                        if (items != null && items.Count > 0)
                        {
                            foreach (var item in items)
                            {
                                string startOn = "", programName = "";
                                // get start time
                                var tdTags = item.SelectNodes("div[@class='time']");
                                if (tdTags != null && tdTags.Count > 0)
                                {
                                    startOn = tdTags.ElementAt(0).InnerText.Trim();
                                }

                                tdTags = item.SelectNodes("div[@class='broadcast-title']");
                                if (tdTags != null && tdTags.Count > 0)
                                {
                                    var deatail = tdTags.FirstOrDefault();
                                    var h2 = deatail.SelectNodes("h2");
                                    if (h2 != null && h2.Count > 0)
                                    {
                                        programName = h2.FirstOrDefault().InnerText.Trim();
                                    }
                                    var h3 = deatail.SelectNodes("h3");
                                    if (h3 != null && h3.Count > 0)
                                    {
                                        var more = h3.FirstOrDefault().InnerText.Trim();
                                        if (!string.IsNullOrWhiteSpace(more))
                                        {
                                            if (!string.IsNullOrWhiteSpace(programName))
                                            {
                                                programName += ": " + more;
                                            }
                                            else
                                            {
                                                programName = more;
                                            }
                                        }
                                    }
                                }
                                if (!string.IsNullOrWhiteSpace(startOn))
                                {
                                    var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                    guideItems.Add(guideItem);
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        static public List<GuideItem> GetDataFromVTVCabUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server, channelToServer.Value, date.ToString("dd"), date.ToString("MM"), date.ToString("yyyy"));

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    var response = http.GetByteArrayAsync(url).Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                    source = WebUtility.HtmlDecode(source);
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(source);
                    var schedulerMain = resultat.DocumentNode.SelectNodes("//div[@class='table-schedules']");
                    if (schedulerMain != null && schedulerMain.Count > 0)
                    {
                        var items = schedulerMain.FirstOrDefault().SelectNodes("table//tr");
                        if (items != null && items.Count > 0)
                        {
                            foreach (var item in items)
                            {
                                string startOn = "", programName = "";
                                // get start time
                                var tdTags = item.SelectNodes("td");
                                if (tdTags != null && tdTags.Count > 0)
                                {
                                    startOn = tdTags.ElementAt(0).InnerText;
                                }
                                if (tdTags != null && tdTags.Count > 1)
                                {
                                    programName = MethodHelpers.ToTitleCase(tdTags.ElementAt(1).InnerText.Trim());
                                }
                                if (tdTags != null && tdTags.Count > 2)
                                {
                                    var more = tdTags.ElementAt(2).InnerText.Trim();
                                    if (!string.IsNullOrWhiteSpace(more))
                                    {
                                        programName = programName + ": " + more;
                                    }
                                }
                                if (!string.IsNullOrWhiteSpace(startOn))
                                {
                                    var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                    guideItems.Add(guideItem);
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        static public List<GuideItem> GetDataFromTRAVINHTVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server);

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    var stringContent = "{\"ngay\":\"" + date.ToString("yyyy-MM-dd") + "\"}";
                    var keys = new List<KeyValuePair<string, string>>() { new KeyValuePair<string, string>("ngay", date.ToString("yyyy-MM-dd")) };
                    
                    var content = new System.Net.Http.FormUrlEncodedContent(keys);
                    content.Headers.Clear();
                    content.Headers.Add("Content-Type", "application/x-www-form-urlencoded");
                    content.Headers.Add("X-Requested-With", "XMLHttpRequest");
                 
                    var response = http.PostAsync(url, content).Result;

                    if (response.IsSuccessStatusCode)
                    {
                        var bytes = response.Content.ReadAsByteArrayAsync().Result;
                        String source = Encoding.GetEncoding("utf-8").GetString(bytes, 0, bytes.Length - 1);
                        source = System.Text.RegularExpressions.Regex.Unescape(source);
                        source = WebUtility.HtmlDecode(source);
                        HtmlDocument resultat = new HtmlDocument();
                        resultat.LoadHtml(source);
                        var schedulerMain = resultat.DocumentNode.SelectNodes("//div[@id='lichchitiet']");
                        if (schedulerMain != null && schedulerMain.Count > 0)
                        {
                            var items = schedulerMain.FirstOrDefault().SelectNodes("div");
                            if (items != null)
                            {
                                foreach (var item in items)
                                {
                                    string startOn = "", programName = "";
                                    // get start time
                                    var detail = item.InnerText.Trim();
                                    if (detail.Length > 5)
                                    {
                                        startOn = detail.Substring(0, 5).Replace("g",":").Trim();
                                        programName = detail.Substring(6).Trim();
                                    }

                                    if (!string.IsNullOrWhiteSpace(startOn))
                                    {
                                        var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                        guideItems.Add(guideItem);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }
        static public List<GuideItem> GetDataFromBPTVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server, channelToServer.Value, date.ToString("yyyy-MM-dd"));

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    var response = http.GetByteArrayAsync(url).Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                    source = System.Text.RegularExpressions.Regex.Unescape(source);
                    source = WebUtility.HtmlDecode(source);
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(source);

                    var schedulerMain = resultat.DocumentNode.SelectNodes("//div[@id='main-content']//table");
                    if (schedulerMain != null && schedulerMain.Count > 0)
                    {
                        var items = schedulerMain.FirstOrDefault().SelectNodes("tbody//tr");
                        if (items != null)
                        {
                            foreach (var item in items)
                            {
                                string startOn = "", programName = "";
                                // get start time
                                var tdTags = item.SelectNodes("td");
                                if (tdTags != null && tdTags.Count > 1)
                                {
                                    startOn = tdTags.ElementAt(0).InnerText.Trim();
                                    programName = tdTags.ElementAt(1).InnerText.Trim();
                                }
                                if (!string.IsNullOrWhiteSpace(startOn))
                                {
                                    var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                    guideItems.Add(guideItem);
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }
        static public List<GuideItem> GetDataFromYOUTVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server, date.ToString("dd-MM-yyyy"));

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    var response = http.GetByteArrayAsync(url).Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                    //source = System.Text.RegularExpressions.Regex.Unescape(source);
                    int start = source.IndexOf("<!--BATDAULICH-->")+17, end = source.IndexOf("<!--KETTHUCLICH-->");
                    source = source.Substring(start, end - start);
                    source = WebUtility.HtmlDecode(source);
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(source);

                    var items = resultat.DocumentNode.SelectNodes("div");
                    if (items != null)
                    {
                        foreach (var item in items)
                        {
                            string startOn = "", programName = "";
                            // get start time
                            var tdTags = item.SelectNodes("div");
                            if (tdTags != null && tdTags.Count > 3)
                            {
                                startOn = tdTags.ElementAt(1).InnerText.Trim();
                                programName = MethodHelpers.ToTitleCase(tdTags.ElementAt(2).InnerText.Trim());
                            }
                            if (!string.IsNullOrWhiteSpace(startOn))
                            {
                                var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                guideItems.Add(guideItem);
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        static public List<GuideItem> GetDataFromHITVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server);

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    var response = http.GetByteArrayAsync(url).Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                    source = System.Text.RegularExpressions.Regex.Unescape(source);
                    source = WebUtility.HtmlDecode(source);
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(source);

                    var schedulerMain = resultat.DocumentNode.SelectNodes("//div[@class='lich']");
                    if (schedulerMain != null && schedulerMain.Count > 0)
                    {
                        var items = schedulerMain.FirstOrDefault().SelectNodes("ul//li");
                        if (items != null)
                        {
                            foreach (var item in items)
                            {
                                string startOn = "", programName = "";
                                // get start time
                                var spans = item.SelectNodes("span");
                                if (spans != null && spans.Count > 0)
                                {
                                    startOn = spans.ElementAt(0).InnerText.Replace("h",":").Trim();
                                }
                                var aTags = item.SelectNodes("a");
                                if (aTags != null && aTags.Count > 0)
                                {
                                    programName = aTags.ElementAt(0).InnerText.Trim();
                                }
                                if (!string.IsNullOrWhiteSpace(startOn))
                                {
                                    var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                    guideItems.Add(guideItem);
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }
        static public List<GuideItem> GetDataFromCAMAUVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server, date.ToString("yyyy-MM-dd"));

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    var response = http.GetByteArrayAsync(url).Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                    source = System.Text.RegularExpressions.Regex.Unescape(source);
                    source = WebUtility.HtmlDecode(source);
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(source);

                    var schedulerMain = resultat.DocumentNode.SelectNodes("//div[@class='containerI']");
                    if (schedulerMain != null && schedulerMain.Count > 0)
                    {
                        var contents = schedulerMain.FirstOrDefault().SelectNodes("div//p");
                        if (contents != null && contents.Count > 0)
                        {
                            var str = contents.FirstOrDefault().InnerHtml.Replace("<br>","^");

                            var items = str.Split('^');
                            if (items != null)
                            {
                                foreach (var item in items)
                                {
                                    int middle = item.IndexOf("</b>");
                                    if (middle > 8)
                                    {
                                        string startOn = "", programName = "";
                                        // get start time
                                        startOn = item.Substring(0, middle).Replace("<b>","").Replace(":","").Replace("h", ":").Trim();
                                        programName = item.Substring(middle).Replace("</b>", "").Trim();

                                        if (!string.IsNullOrWhiteSpace(startOn))
                                        {
                                            var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                            guideItems.Add(guideItem);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        static public List<GuideItem> GetDataFromBENTREVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server, date.ToString("yyyy-MM-dd"));

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    var response = http.GetByteArrayAsync(url).Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                    source = System.Text.RegularExpressions.Regex.Unescape(source);
                    source = WebUtility.HtmlDecode(source);
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(source);

                    var schedulerMain = resultat.DocumentNode.SelectNodes("//div[@class='containerI']");
                    if (schedulerMain != null && schedulerMain.Count > 0)
                    {
                        var contents = schedulerMain.FirstOrDefault().SelectNodes("div//p");
                        if (contents != null && contents.Count > 0)
                        {
                            var str = contents.FirstOrDefault().InnerHtml.Replace("<br>", "^");

                            var items = str.Split('^');
                            if (items != null)
                            {
                                foreach (var item in items)
                                {
                                    int middle = item.IndexOf("</b>");
                                    if (middle > 6 && middle < 10)
                                    {
                                        string startOn = "", programName = "";
                                        // get start time
                                        startOn = item.Substring(0, middle).Replace("<b>", "").Trim();
                                        programName = item.Substring(middle).Replace("</b>", "").Trim();

                                        if (!string.IsNullOrWhiteSpace(startOn))
                                        {
                                            var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                            guideItems.Add(guideItem);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        static public List<GuideItem> GetDataFromTV24Url(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server, channelToServer.Value, date.ToString("dd/MM/yyyy"));

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    var response = http.GetByteArrayAsync(url).Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                    source = System.Text.RegularExpressions.Regex.Unescape(source);
                    source = WebUtility.HtmlDecode(source);
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(source);

                    var schedulerMain = resultat.DocumentNode.SelectNodes("//div");
                    if (schedulerMain != null && schedulerMain.Count > 0)
                    {
                        var items = schedulerMain.FirstOrDefault().SelectNodes("table//tr");
                        if (items != null)
                        {
                            foreach (var item in items)
                            {
                                string startOn = "", programName = "";
                                // get start time
                                var tdTags = item.SelectNodes("td");
                                if (tdTags != null && tdTags.Count > 1)
                                {
                                    startOn = tdTags.ElementAt(0).InnerText.Trim();
                                    programName = MethodHelpers.ToTitleCase(tdTags.ElementAt(1).InnerText.Trim());
                                }
                                if (!string.IsNullOrWhiteSpace(startOn) && startOn.Length < 6)
                                {
                                    var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                    guideItems.Add(guideItem);
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        class LetsViet
        {
            public string ScheduleDetailTitle { get; set; }
            public string ScheduleDetailEp { get; set; }
            public string ScheduleDetailProgramName { get; set; }
            public string ScheduleDetailHour { get; set; }
        }
        static public List<GuideItem> GetDataFromLetsVietUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server, date.ToString("yyyy/MM/dd"));

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    var response = http.GetByteArrayAsync(url).Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                    if (!source.EndsWith("]")) source += "]";
                    var items = JsonConvert.DeserializeObject<List<LetsViet>>(source);

                    if (items != null)
                    {
                        foreach (var item in items)
                        {
                            string startOn = "", programName = "";
                            startOn = item.ScheduleDetailHour.Trim();
                            programName = (string.IsNullOrWhiteSpace(item.ScheduleDetailProgramName)? item.ScheduleDetailProgramName.Trim() +": ":"")+ item.ScheduleDetailTitle.Trim() + item.ScheduleDetailEp;

                            if (!string.IsNullOrWhiteSpace(startOn) && startOn.Length < 6)
                            {
                                var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                guideItems.Add(guideItem);
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        static public List<GuideItem> GetDataFromKGTVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server, channelToServer.Value, date.ToString("yyyy-MM-dd"));

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    var response = http.GetByteArrayAsync(url).Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                    source = System.Text.RegularExpressions.Regex.Unescape(source);
                    source = WebUtility.HtmlDecode(source);
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(source);

                    var schedulerMain = resultat.DocumentNode.SelectNodes("//div[@class='containerI']");
                    if (schedulerMain != null && schedulerMain.Count > 0)
                    {
                        var contents = schedulerMain.FirstOrDefault().SelectNodes("div//p");
                        if (contents != null && contents.Count > 0)
                        {
                            var str = contents.FirstOrDefault().InnerHtml.Replace("<br>", "^");

                            var items = str.Split('^');
                            if (items != null)
                            {
                                foreach (var item in items)
                                {
                                    int middle = item.IndexOf("</b>");
                                    if (middle > 7)
                                    {
                                        string startOn = "", programName = "";
                                        // get start time
                                        startOn = item.Substring(0, middle).Replace("<b>", "").Replace(":", "").Replace("h", ":").Trim();
                                        programName = item.Substring(middle).Replace("</b>", "").Replace(":\t", "").Trim();

                                        if (!string.IsNullOrWhiteSpace(startOn))
                                        {
                                            var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                            guideItems.Add(guideItem);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        static public List<GuideItem> GetDataFromBRTUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = channelToServer.Server;

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    var response = http.GetByteArrayAsync(url).Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
               //     source = System.Text.RegularExpressions.Regex.Unescape(source);
                    source = WebUtility.HtmlDecode(source);
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(source);

                    var schedulerMain = resultat.DocumentNode.SelectNodes("//table[@class='MsoNormalTable']");
                    if (schedulerMain != null && schedulerMain.Count > 0)
                    {
                        var items = schedulerMain.FirstOrDefault().SelectNodes("tbody//tr");
                        if (items != null)
                        {
                            foreach (var item in items)
                            {
                                string startOn = "", programName = "";
                                // get start time
                                var tdTags = item.SelectNodes("td");
                                if (tdTags != null && tdTags.Count > 1)
                                {
                                    startOn = tdTags.ElementAt(0).InnerText.Replace("g",":").Trim();
                                    programName = MethodHelpers.ToTitleCase(System.Text.RegularExpressions.Regex.Replace(tdTags.ElementAt(1).InnerText.Trim(), @"\s+", " "));
                                }
                                if (!string.IsNullOrWhiteSpace(startOn) && startOn.Length < 6)
                                {
                                    var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                    guideItems.Add(guideItem);
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        static public List<GuideItem> GetDataFromBTVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server, channelToServer.Value);

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    var stringContent = string.Format("id={0}&num={1}&height=380", channelToServer.Value, date.ToString("yyyy-MM-dd"));
                    var httpContent = new StringContent(stringContent, Encoding.UTF8, "application/x-www-form-urlencoded");
                    var response = http.PostAsync(url, httpContent).Result;

                    if (response.IsSuccessStatusCode)
                    {
                        var bytes = response.Content.ReadAsByteArrayAsync().Result;
                        String source = Encoding.GetEncoding("utf-8").GetString(bytes, 0, bytes.Length - 1);
                        source = System.Text.RegularExpressions.Regex.Unescape(source);
                        source = WebUtility.HtmlDecode(source);
                        HtmlDocument resultat = new HtmlDocument();
                        resultat.LoadHtml(source);

                        var moreDetailItems = resultat.DocumentNode.SelectNodes("//tr");
                        if (moreDetailItems != null && moreDetailItems.Count > 0)
                        {
                            foreach (var moreDetailItem in moreDetailItems)
                            {
                                var tds = moreDetailItem.SelectNodes("td");
                                if (tds != null && tds.Count == 3)
                                {
                                    string startOn = "", programName = "", more = "";
                                    // get start time
                                    startOn = tds[0].InnerText.Trim();
                                    programName = tds[1].InnerText.Trim();
                                    more = tds[2].InnerText.Trim();
                                    if (!String.IsNullOrWhiteSpace(more))
                                    {
                                        programName += " - " + more;
                                    }

                                    if (startOn.Length == 4)
                                    {
                                        startOn = "0" + startOn;
                                    }
                                    if (!string.IsNullOrWhiteSpace(startOn) && startOn.Length == 5)
                                    {
                                        var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                        guideItems.Add(guideItem);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        static public List<GuideItem> GetDataFromLA34Url(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server, date.ToString("yyyy-MM-dd"));

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    var response = http.GetByteArrayAsync(url).Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                    //     source = System.Text.RegularExpressions.Regex.Unescape(source);
                    source = WebUtility.HtmlDecode(source);
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(source);

                    var items = resultat.DocumentNode.SelectNodes("li");
                    if (items != null)
                    {
                        foreach (var item in items)
                        {
                            string startOn = "", programName = "";
                            // get start time
                            var tdTags = item.SelectNodes("span");
                            if (tdTags != null && tdTags.Count == 2)
                            {
                                startOn = tdTags.ElementAt(1).InnerText.Trim();
                                programName = tdTags.ElementAt(0).InnerText.Trim();
                                if (startOn.Length == 4)
                                {
                                    startOn = "0" + startOn;
                                }
                            }
                            if (!string.IsNullOrWhiteSpace(startOn) && startOn.Length < 6)
                            {
                                var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                guideItems.Add(guideItem);
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        //just get current date +-7
        static public List<GuideItem> GetDataFromCanThoTVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = channelToServer.Server;

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                int thu = (int) date.DayOfWeek;
                if (thu == 0) thu = 7;
                using (HttpClient http = new HttpClient())
                {
                    var response = http.GetByteArrayAsync(url).Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                    //     source = System.Text.RegularExpressions.Regex.Unescape(source);
                    source = WebUtility.HtmlDecode(source);
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(source);

                    var items = resultat.DocumentNode.SelectNodes("//table[@class='table_LPS2']//tr[@class='hide"+thu+"']");
                    if (items != null)
                    {
                        foreach (var item in items)
                        {
                            string startOn = "", programName = "";
                            // get start time
                            var tdTags = item.SelectNodes("td");
                            if (tdTags != null && tdTags.Count > 1)
                            {
                                startOn = tdTags.ElementAt(0).InnerText.Trim();
                                if (startOn.Length == 4)
                                {
                                    startOn = "0" + startOn;
                                }
                                programName = MethodHelpers.ToTitleCase(tdTags.ElementAt(1).InnerText.Trim());
                            }
                            if (!string.IsNullOrWhiteSpace(startOn) && startOn.Length < 6)
                            {
                                var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                guideItems.Add(guideItem);
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        //just get current date
        static public List<GuideItem> GetDataFromTRTUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = channelToServer.Server;

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    var response = http.GetByteArrayAsync(url).Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                    //     source = System.Text.RegularExpressions.Regex.Unescape(source);
                    source = WebUtility.HtmlDecode(source);
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(source);

                    var items = resultat.DocumentNode.SelectNodes("//td[contains(@id,'tbProgramChannel')]//tr");
                    if (items != null && items.Count > 0)
                    {
                        items.Remove(0);
                        foreach (var item in items)
                        {
                            string startOn = "", programName = "";
                            // get start time
                            var tdTags = item.SelectNodes("td");
                            if (tdTags != null && tdTags.Count > 1)
                            {
                                startOn = tdTags.ElementAt(0).InnerText.Trim();
                                programName = tdTags.ElementAt(1).InnerText.Trim();
                            }
                            if (!string.IsNullOrWhiteSpace(startOn) && startOn.Length < 6)
                            {
                                var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                guideItems.Add(guideItem);
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        static public List<GuideItem> GetDataFromPhuThoTVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server);

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    //args[date]:"31/06/2016"
                    var keys = new List<KeyValuePair<string, string>>()
                                            { 
                                                new KeyValuePair<string, string>("method", "GetSchedule"), 
                                                new KeyValuePair<string, string>("args[date]", date.ToString("dd/MM/yyyy")) 
                                            };

                    var content = new System.Net.Http.FormUrlEncodedContent(keys);
                    content.Headers.Clear();
                    content.Headers.Add("Content-Type", "application/x-www-form-urlencoded");
                    content.Headers.Add("X-Requested-With", "XMLHttpRequest");

                    var response = http.PostAsync(url, content).Result;
                    // http.BaseAddress = new Uri(url);

                    //var response = http.PostAsync(url, content).Result;
                    if (response.IsSuccessStatusCode)
                    {
                        var bytes = response.Content.ReadAsByteArrayAsync().Result;
                        String source = Encoding.GetEncoding("utf-8").GetString(bytes, 0, bytes.Length - 1);
                        source = System.Text.RegularExpressions.Regex.Unescape(source);
                        int start = source.IndexOf("\"<ul>") + 1;
                        int end = source.LastIndexOf("</ul>\"") + 5;
                        source = source.Substring(start, end - start);
                        source = WebUtility.HtmlDecode(source);
                        HtmlDocument resultat = new HtmlDocument();
                        resultat.LoadHtml(source);

                        var items = resultat.DocumentNode.SelectNodes("//li");
                        if (items != null)
                        {
                            foreach (var item in items)
                            {
                                string startOn = "", programName = "";
                                // get start time
                                var pTags = item.SelectNodes("p");
                                if (pTags != null && pTags.Count > 0)
                                {
                                    startOn = pTags.ElementAt(0).InnerText.Trim();
                                }
                                var details = item.SelectNodes("a");
                                if (details != null && details.Count > 0)
                                {
                                    programName = details.ElementAt(0).InnerText.Trim();
                                }
                                if (pTags != null && pTags.Count > 1)
                                {
                                    var more = pTags.ElementAt(1).InnerText.Trim();
                                    if(!String.IsNullOrWhiteSpace(more)) {
                                        programName = programName + ": " + more;
                                    }
                                }
                                if (!string.IsNullOrWhiteSpace(startOn))
                                {
                                    var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                    guideItems.Add(guideItem);
                                }
                            }
                        }
                    }
                }
        
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        //just get current date
        static public List<GuideItem> GetDataFromTHDTUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = channelToServer.Server;

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    http.DefaultRequestHeaders.TryAddWithoutValidation("Accept", "text/html,application/xhtml+xml,application/xml");
                    http.DefaultRequestHeaders.TryAddWithoutValidation("Accept-Encoding", "gzip, deflate");
                    http.DefaultRequestHeaders.TryAddWithoutValidation("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:19.0) Gecko/20100101 Firefox/19.0");
                    http.DefaultRequestHeaders.TryAddWithoutValidation("Accept-Charset", "ISO-8859-1");

                   // http.DefaultRequestHeaders.AcceptEncoding.Add(new System.Net.Http.Headers.StringWithQualityHeaderValue("gzip"));
                    var rs = http.GetAsync(url).Result;
                    var response = rs.Content.ReadAsByteArrayAsync().Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                    //     source = System.Text.RegularExpressions.Regex.Unescape(source);
                    source = WebUtility.HtmlDecode(source);
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(source);

                    var items = resultat.DocumentNode.SelectNodes("//div[@class='schedule_content']");
                    if (items != null && items.Count > 0)
                    {
                        foreach (var item in items)
                        {
                            string startOn = "", programName = "";
                            // get start time
                            var tdTags = item.SelectNodes("span");
                            if (tdTags != null && tdTags.Count > 1)
                            {
                                startOn = tdTags.ElementAt(0).InnerText.Trim();
                                programName = tdTags.ElementAt(1).InnerText.Trim();
                            }
                            if (!string.IsNullOrWhiteSpace(startOn) && startOn.Length == 5)
                            {
                                var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                guideItems.Add(guideItem);
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        //just get current date
        static public List<GuideItem> GetDataFromLangSonTVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = channelToServer.Server;

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    http.DefaultRequestHeaders.TryAddWithoutValidation("Accept", "text/html,application/xhtml+xml,application/xml");
                    http.DefaultRequestHeaders.TryAddWithoutValidation("Accept-Encoding", "gzip, deflate");
                    http.DefaultRequestHeaders.TryAddWithoutValidation("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:19.0) Gecko/20100101 Firefox/19.0");
                    http.DefaultRequestHeaders.TryAddWithoutValidation("Accept-Charset", "ISO-8859-1");

                    // http.DefaultRequestHeaders.AcceptEncoding.Add(new System.Net.Http.Headers.StringWithQualityHeaderValue("gzip"));
                    var rs = http.GetAsync(url).Result;
                    var response = rs.Content.ReadAsByteArrayAsync().Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                    //     source = System.Text.RegularExpressions.Regex.Unescape(source);
                    source = WebUtility.HtmlDecode(source);
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(source);

                    var items = resultat.DocumentNode.SelectNodes("//div[@class='bmProgram_mainbox_body']");
                    if (items != null && items.Count > 0)
                    {
                        foreach (var item in items)
                        {
                            string startOn = "", programName = "";
                            // get start time
                            var tdTags = item.SelectNodes("span");
                            if (tdTags != null && tdTags.Count > 1)
                            {
                                startOn = tdTags.ElementAt(0).InnerText.Trim();
                                programName = tdTags.ElementAt(1).InnerText.Trim();
                            }
                            if (!string.IsNullOrWhiteSpace(startOn) && startOn.Length == 5)
                            {
                                var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                guideItems.Add(guideItem);
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        static public List<GuideItem> GetDataFromVTC16Url(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server, channelToServer.Value, date.ToString("dd/MM/yyyy"));

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    var response = http.GetByteArrayAsync(url).Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                    //     source = System.Text.RegularExpressions.Regex.Unescape(source);
                    source = WebUtility.HtmlDecode(source);
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(source);

                    var items = resultat.DocumentNode.SelectNodes("//div[@class='item row']");
                    if (items != null && items.Count > 0)
                    {
                        foreach (var item in items)
                        {
                            string startOn = "", programName = "";
                            // get start time
                            var tdTags = item.SelectNodes("div");
                            if (tdTags != null && tdTags.Count > 1)
                            {
                                startOn = tdTags.ElementAt(0).InnerText.Trim();
                                var divs = tdTags.ElementAt(1).SelectNodes("div");
                                if (divs != null && divs.Count > 0)
                                {
                                    programName = divs.ElementAt(0).InnerText.Trim();
                                }
                                if (divs != null && divs.Count > 1)
                                {
                                    var more = divs.ElementAt(1).InnerText.Trim();
                                    if (!string.IsNullOrWhiteSpace(more) && !more.Equals(programName, StringComparison.OrdinalIgnoreCase))
                                    {
                                        programName = programName + ": " + more;
                                    }
                                }
                                programName = MethodHelpers.ToTitleCase(programName);
                            }
                            if (!string.IsNullOrWhiteSpace(startOn) && startOn.Length < 6)
                            {
                                var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                guideItems.Add(guideItem);
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        class TrueLife
        {
            public string strTimeBegin;
            public string title;
        }
        static public List<GuideItem> GetDataFromTrueLifeTVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server, channelToServer.Value, date.ToString("yyyyMMdd"));

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    var response = http.GetByteArrayAsync(url).Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                    source = System.Text.RegularExpressions.Regex.Unescape(source);
                    int start = source.IndexOf("["), end =source.LastIndexOf("]") + 1;
                    source = source.Substring(start, end - start);
                    var items = JsonConvert.DeserializeObject<List<TrueLife>>(source);

                    if (items != null && items.Count > 0)
                    {
                        foreach (var item in items)
                        {
                            string startOn = "", programName = "";
                            startOn = item.strTimeBegin.Substring(11,5).Trim();
                            programName = item.title.Trim();
                            if (!string.IsNullOrWhiteSpace(startOn))
                            {
                                var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                guideItems.Add(guideItem);
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        //just current date
        static public List<GuideItem> GetDataFromATVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = channelToServer.Server;

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    var response = http.GetByteArrayAsync(url).Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                    //     source = System.Text.RegularExpressions.Regex.Unescape(source);
                    source = WebUtility.HtmlDecode(source);
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(source);

                    var items = resultat.DocumentNode.SelectNodes("//div[@style='font-size:12px;font-family:Arial;']//div");
                    if (items != null && items.Count > 3)
                    {
                        for (int i = 0; i < items.Count - 2; i = i + 3)
                        {
                            string startOn = "", programName = "";
                            // get start time
                            startOn = items[i].InnerText.Replace("g",":").Trim();
                            programName = items[i + 1].InnerText.Trim();
                            if (!string.IsNullOrWhiteSpace(startOn) && startOn.Length < 6)
                            {
                                var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                guideItems.Add(guideItem);
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        static public List<GuideItem> GetDataFromNamDinhTVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = channelToServer.Server;

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    http.DefaultRequestHeaders.TryAddWithoutValidation("Accept", "text/html,application/xhtml+xml,application/xml");
                    http.DefaultRequestHeaders.TryAddWithoutValidation("Accept-Encoding", "gzip, deflate");
                    http.DefaultRequestHeaders.TryAddWithoutValidation("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:19.0) Gecko/20100101 Firefox/19.0");
                    
                    var keys = new List<KeyValuePair<string, string>>() 
                    { 
                        new KeyValuePair<string, string>("op", "lichphatsong"),
                        new KeyValuePair<string, string>("data", date.ToString("MM-dd")+"|1") 
                    };

                    var content = new System.Net.Http.FormUrlEncodedContent(keys);
                    
                    var response = http.PostAsync(url, content).Result;

                    if (response.IsSuccessStatusCode)
                    {
                        var bytes = response.Content.ReadAsByteArrayAsync().Result;
                        String source = Encoding.GetEncoding("utf-8").GetString(bytes, 0, bytes.Length - 1);
                        source = System.Text.RegularExpressions.Regex.Unescape(source);
                        source = WebUtility.HtmlDecode(source);
                        HtmlDocument resultat = new HtmlDocument();
                        resultat.LoadHtml(source);
                        var items = resultat.DocumentNode.SelectNodes("//table//tr");
                        if (items != null)
                        {
                            foreach (var item in items)
                            {
                                string startOn = "", programName = "";
                                // get start time
                                var tdTags = item.SelectNodes("td");
                                if (tdTags != null && tdTags.Count > 1)
                                {
                                    startOn = tdTags.ElementAt(0).InnerText;
                                    programName = tdTags.ElementAt(1).InnerText.Trim();
                                }
                                
                                if (!string.IsNullOrWhiteSpace(startOn))
                                {
                                    var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                    guideItems.Add(guideItem);
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        static public List<GuideItem> GetDataFromHauGiangTVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = channelToServer.Server;

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    http.DefaultRequestHeaders.TryAddWithoutValidation("Accept", "text/html,application/xhtml+xml,application/xml");
                    http.DefaultRequestHeaders.TryAddWithoutValidation("Accept-Encoding", "gzip, deflate");
                    http.DefaultRequestHeaders.TryAddWithoutValidation("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:19.0) Gecko/20100101 Firefox/19.0");

                    var keys = new List<KeyValuePair<string, string>>() 
                    { 
                        new KeyValuePair<string, string>("fDay", date.Day.ToString()),
                        new KeyValuePair<string, string>("fMonth", date.Month.ToString()),
                        new KeyValuePair<string, string>("fYear", date.Year.ToString()),
                        new KeyValuePair<string, string>("fKenh", "truyen-hinh"),
                        new KeyValuePair<string, string>("submit", "Xem")
                    };

                    var content = new System.Net.Http.FormUrlEncodedContent(keys);

                    var response = http.PostAsync(url, content).Result;

                    if (response.IsSuccessStatusCode)
                    {
                        var bytes = response.Content.ReadAsByteArrayAsync().Result;
                        String source = Encoding.GetEncoding("utf-8").GetString(bytes, 0, bytes.Length - 1);
                        source = System.Text.RegularExpressions.Regex.Unescape(source);
                        source = WebUtility.HtmlDecode(source);
                        HtmlDocument resultat = new HtmlDocument();
                        resultat.LoadHtml(source);
                        var items = resultat.DocumentNode.SelectNodes("//div[@class='lich_content']//span");
                        if (items != null)
                        {
                            foreach (var item in items)
                            {
                                string detail = item.InnerText.Trim();
                                int index = detail.IndexOf(":");
                                if (index < 1) continue;
                                string startOn = "", programName = "";
                                // get start time
                                startOn = detail.Substring(0, index).Replace("h",":").Trim();
                                programName = detail.Substring(index + 2).Trim();

                                if (!string.IsNullOrWhiteSpace(startOn))
                                {
                                    var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                    guideItems.Add(guideItem);
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        // just current date
        static public List<GuideItem> GetDataFromHaiPhongTVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = channelToServer.Server;

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    var response = http.GetAsync(url).Result;

                    if (response.IsSuccessStatusCode)
                    {
                        var bytes = response.Content.ReadAsByteArrayAsync().Result;
                        String source = Encoding.GetEncoding("utf-8").GetString(bytes, 0, bytes.Length - 1);
                        source = System.Text.RegularExpressions.Regex.Unescape(source);
                        source = WebUtility.HtmlDecode(source);
                        HtmlDocument resultat = new HtmlDocument();
                        resultat.LoadHtml(source);
                        var items = resultat.DocumentNode.SelectNodes("//table[@class='tblLichPhatSong']//tr");
                        if (items != null)
                        {
                            foreach (var item in items)
                            {
                                string startOn = "", programName = "";
                                // get start time
                                var tdTags = item.SelectNodes("td");
                                if (tdTags != null && tdTags.Count > 1)
                                {
                                    startOn = tdTags.ElementAt(0).InnerText.Replace("h",":").Trim();
                                    programName = tdTags.ElementAt(1).InnerText.Trim();
                                }

                                if (!string.IsNullOrWhiteSpace(startOn))
                                {
                                    var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                    guideItems.Add(guideItem);
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        static public List<GuideItem> GetDataFromSonLaTVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = string.Format(channelToServer.Server, date.ToString("dd/MM/yyyy"));

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    var response = http.GetAsync(url).Result;

                    if (response.IsSuccessStatusCode)
                    {
                        var bytes = response.Content.ReadAsByteArrayAsync().Result;
                        String source = Encoding.GetEncoding("utf-8").GetString(bytes, 0, bytes.Length - 1);
                        source = System.Text.RegularExpressions.Regex.Unescape(source);
                        source = WebUtility.HtmlDecode(source);
                        HtmlDocument resultat = new HtmlDocument();
                        resultat.LoadHtml(source);
                        var items = resultat.DocumentNode.SelectNodes("//div[@class='node node-lich-ps']//tr");
                        if (items != null && items.Count > 1)
                        {
                            items.Remove(0);
                            foreach (var item in items)
                            {
                                string startOn = "", programName = "";
                                // get start time
                                var tdTags = item.SelectNodes("td");
                                if (tdTags != null && tdTags.Count > 1)
                                {
                                    startOn = tdTags.ElementAt(0).InnerText.Trim();
                                    programName = tdTags.ElementAt(1).InnerText.Trim();
                                }

                                if (!string.IsNullOrWhiteSpace(startOn))
                                {
                                    var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                    guideItems.Add(guideItem);
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }
        static public List<GuideItem> GetDataFromTHVLUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server, channelToServer.Value, date.ToString("yyyy-MM-dd"));

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                HttpClient http = new HttpClient();
                var response = http.GetByteArrayAsync(url).Result;
                String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                source = WebUtility.HtmlDecode(source);
                HtmlDocument resultat = new HtmlDocument();
                resultat.LoadHtml(source);
                var schedulerMain = resultat.DocumentNode.SelectNodes("//div[@class='content-block']");
                if (schedulerMain != null && schedulerMain.Count > 0)
                {
                    var chanelDetail = schedulerMain.FirstOrDefault();
                    if (chanelDetail != null)
                    {
                        var items = chanelDetail.SelectNodes("div//table//tr");
                        if (items != null)
                        {
                            foreach (var item in items)
                            {
                                string startOn = "", programName = "";
                                var tdTags = item.SelectNodes("td");
                                if (tdTags != null && tdTags.Count > 1)
                                {
                                    startOn = tdTags.ElementAt(0).InnerText.Trim();
                                    programName = tdTags.ElementAt(1).InnerText.Trim();
                                }
                                if (!string.IsNullOrWhiteSpace(startOn))
                                {
                                    var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                    guideItems.Add(guideItem);
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        class ChannelKPlus
        {
            public int Id;
            public String Name;
        }
        class ProgramKPlus
        {
            public String Genres;
            public String Name;
        }

        class ScheduleKPlus
        {
            public ChannelKPlus Channel;
            public ProgramKPlus Program;
            public String ShowingTime;
        }
        class KPlus
        {
            public List<ScheduleKPlus> Schedules;
        }
        static public List<GuideItem> GetDataFromKPlusUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server);

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                HttpWebRequest req = (HttpWebRequest)WebRequest.Create(url);
                {
                    byte[] buffer = Encoding.UTF8.GetBytes("date=" + date.ToString("dd-MM-yyyy") + "&categories=" + channelToServer.ExtraValue);
                    req.Method = "POST";
                    req.ContentType = "application/x-www-form-urlencoded";
                    req.ContentLength = buffer.Length;
                    req.KeepAlive = true;
                    req.Timeout = System.Threading.Timeout.Infinite;
                    req.ProtocolVersion = HttpVersion.Version10;
                    req.AllowWriteStreamBuffering = false;
                    req.Accept = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
                    req.UserAgent="Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0";
                    
                    WebHeaderCollection myWebHeaderCollection = req.Headers;
                    myWebHeaderCollection.Add("Accept-Encoding: gzip, deflate");
                    myWebHeaderCollection.Add("Accept-Language: en-US,en;q=0.5");
                    req.CookieContainer = new CookieContainer();
                    //req.CookieContainer.Add(new Uri(url), new Cookie("__utma", "1.268565172.1470799796.1474424801.1474426720.10"));
                    //req.CookieContainer.Add(new Uri(url), new Cookie("__utmz", "1.1470803901.2.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided)"));
                    req.CookieContainer.Add(new Uri(url), new Cookie("current_site", "1"));
                    req.CookieContainer.Add(new Uri(url), new Cookie("Locale", "vi-VN"));
                    req.CookieContainer.Add(new Uri(url), new Cookie("__utmc", "1"));

                    Stream stream = req.GetRequestStream();
                    stream.Write(buffer, 0, buffer.Length);
                    stream.Close();
                    WebResponse response = req.GetResponse();

                    if (((HttpWebResponse)response).StatusDescription.Equals("OK",StringComparison.OrdinalIgnoreCase))
                    {
                        stream = response.GetResponseStream();
                        StreamReader reader = new StreamReader(stream);
                        string source = reader.ReadToEnd();
                        response.Close();
                        

                        var totalItems = JsonConvert.DeserializeObject<KPlus>(source);

                        if (totalItems != null)
                        {
                            var items = totalItems.Schedules.Where(p => channelToServer.Value.Equals(p.Channel.Id.ToString())).ToList();
                            if (items != null && items.Count > 0)
                            {
                                foreach (var item in items)
                                {
                                    string startOn = "", programName = "";
                                    startOn = item.ShowingTime.Substring(11, 5).Trim();
                                    programName = item.Program.Genres.Trim();
                                    if (!string.IsNullOrWhiteSpace(item.Program.Name))
                                    {
                                        programName += ": " + Utils.MethodHelpers.ToTitleCase(item.Program.Name.Trim());
                                    }
                                    if (!string.IsNullOrWhiteSpace(startOn))
                                    {
                                        var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                        guideItems.Add(guideItem);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        response.Close();
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        static public List<GuideItem> GetDataFromSCTVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string postdata = "ctl00%24RadScriptManager1=ctl00%24RadScriptManager1%7Cctl00%24ContentPlaceHolder1%24ctl00%24ctl01%24ddlChannel&ctl00_RadScriptManager1_TSM=%3B%3BSystem.Web.Extensions%2C%20Version%3D4.0.0.0%2C%20Culture%3Dneutral%2C%20PublicKeyToken%3D31bf3856ad364e35%3Aen-US%3Afa6755fd-da1a-49d3-9eb4-1e473e780ecd%3Aea597d4b%3Ab25378d2%3BTelerik.Web.UI%2C%20Version%3D2012.1.411.35%2C%20Culture%3Dneutral%2C%20PublicKeyToken%3D121fae78165ba3d4%3Aen-US%3A4cad056e-160b-4b85-8751-cc8693e9bcd0%3A16e4e7cd%3Aed16cbdc%3Af7645509%3A7c926187%3A8674cba1%3Ab7778d6c%3Ac08e9f8a%3Aa51ee93e%3A59462f1&ctl00%24txtMasterSearch=&ctl00%24ContentPlaceHolder1%24ctl00%24ctl01%24ddlChannel={0}&ctl00%24ContentPlaceHolder1%24ctl00%24ctl01%24sDate={1}&ctl00%24ContentPlaceHolder1%24ctl00%24ctl01%24sDate%24dateInput={1}-00-00-00&ctl00_ContentPlaceHolder1_ctl00_ctl01_sDate_dateInput_ClientState=%7B%22enabled%22%3Atrue%2C%22emptyMessage%22%3A%22%22%2C%22minDateStr%22%3A%221%2F1%2F1900%200%3A0%3A0%22%2C%22maxDateStr%22%3A%2212%2F31%2F2099%200%3A0%3A0%22%2C%22enteredText%22%3A%22%22%7D&ctl00_ContentPlaceHolder1_ctl00_ctl01_sDate_calendar_SD=%5B%5D&ctl00_ContentPlaceHolder1_ctl00_ctl01_sDate_calendar_AD=%5B%5B1900%2C1%2C1%5D%2C%5B2099%2C12%2C30%5D%2C%5B2016%2C11%2C8%5D%5D&ctl00_ContentPlaceHolder1_ctl00_ctl01_sDate_ClientState=%7B%22minDateStr%22%3A%221%2F1%2F1900%200%3A0%3A0%22%2C%22maxDateStr%22%3A%2212%2F31%2F2099%200%3A0%3A0%22%7D&__EVENTTARGET=ctl00%24ContentPlaceHolder1%24ctl00%24ctl01%24ddlChannel&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE=fRepT7VnRi1tR5gB%2B37gBTI5KGSf3sG4dVKw3AA3FHxSrdhEUY9VU7lbnWOcjoIqSR37oGCbFpl0Mk4CblHGf6DyfQh2Z9XpAbzyCXb5NBwggZNEBiecEaJYQp0bPSQKsevWRjlgDkEtJ0CTTJD5DLzv5rPfrbf8L8MfGEmcuQvMoYBmMQS1frQudH6Uuod8oOUM%2FV5UFSzDbhYCFGOU6a527bjQMsBsuk3Uf3RQzqOm65DPquteyBQDYjwB%2BtruMGa6pUOwU%2BjpInvMEkS%2BGfFXf7QdjFfzYX1RVsIFsgY%2B%2F4k6ly6Ak%2Fwj9J%2FBiPzK1Ws1Il%2B7g3cyzjT6A0apkNnl%2FR5XNuptpEA9fm15JAnBqNgA8ObZATIKCPsfWYTFqOUHyZPKEZ4w%2FLwavVzUnc2J57%2FXB4a9RmOwDI7C%2BmhkPrxDgxOfjb7wKhFa8rHAtMX0LgMhJ3QkVcKFxqvt%2FSdH6a88do%2BXUG1G25yOYgviBaNRHv2EyGVSNqqllp1RZWTSqHLUcFRh9l3q3PqQgGbyXuj8w%2B8DziEuO2kqJEIk658WmjFEWhtiItDyC1xGHxvk5hOWUP2Ke3JfO4IuIOii6OUqp38n28ilHRLZ7e4URi6fnJ%2BQDCGqJA8xnOzNySOJ1v7t4gRg1Q%2F1cn9S2jDIupGwQv8MKMMcXpwvvWzF3%2BT%2BmBqsTUObWUdD3FjkNAlHadlfpylU7%2BzIE1WU2CjGpu7QQvtgrvWT5vwKNQTVKd56Zq27ZDT3ATT1ny321%2BTS9iin%2FTNxlIH7mppNW%2FnLdKUJGSgTVs9i2Hl7wuPf2JPGS8b6e%2F5VUeML10Dc1uAO%2FMHdZglXM3dHgnmmHrGb7DfKTVrJO9AzmB4tzK93gZBDSW40HZMm8UHAyQoAqeylu0SpSRMUs3PwQnD%2BndPlWdhQZDcrbjwm8JEzZMeO3kmSFoBE1QQSSUvqtItEza1DX34ASASEwPhaUNhjQcSNEZGOo9QUJsilPrOReuLYxk3xdCn9MooTb8F4kOqDED9DJCQqQNz3iWCGimFUlmsXE1wLZjVqRADQke%2BpzOOkMXhs43UCQfeRd41iSz7oOPTSuSM%2Fz9L3Swl02HWskGjwPWNrfXjEfuNTMVOq9Hnr9vZYE%2FAdFFljOcQ90CehS8y0%2BUxZNyjqSZ80YHwmUC%2BallfYkK3KwOEL1q1ChK4STEb%2BvrhQFhjQxEzwlxg2m2ghXB2BoCykwSeRNmGBQoENzbQ87t33%2Bnjq%2Faaa9AkzqEuLxdUEYN6r1hIaahta0muBtniFZGhGtSHaRg0UMacztKFfcpo3zG%2BauYHCfNwy3xJHHc%2FheKpAsEOAEqBpGN2AZiO4Lz44p7OjlSKIClmW%2BMA5mFE24V7nIB8pEKYmgBoQYOZYLoYOOXW8nKcVsedcSTW9n0R8qNPKn7RqKYwwOJWbXp%2FBp7DAhfhyQAVqND0T3jf3U1ii9ef%2Ba5Edes0ciru4Zzygvfirxz2OQ9QG0fcGDZwQhk1qoIzKObehqHcxzdOxLlypXPVZTalvMazz1rWMC8JrRmRFA8VNHlfdfhdlBR9SlPXRr6I21%2FUhDiDAPggStnBQMt3MRec0iiVXR6qRNOgAywUzNJJqAYGtbhDfTVKZ%2BDaiEqoKFDHnYSRAAiuine%2BgJ8Dz6TxOJL5ICFXKBartGbCwewYiItmCpdRzVKr7r%2BQ4VPEob%2BTQjtE1pWIEo08ozEznoSBKQ4O%2F2ROE9rzt8GRHfZEQxBy1SEoL9Eni111%2FQXsoXG4yTYINyYYJdSLxAT9OORWnmx1SNs%2Fn44CKAYQcSwDgtB%2BtswvpSv6hgdN2%2BHB1ztu7ewzXCSVq7YrgKk3%2FWD3pzzQVt%2FTLX5GyV3%2FAH68YB5rP1zY17ma%2FUgE2qVQtBnpOvp9AQ6V36JtlkMu9z4VqYQl0dTG0BrPyJftzhHvsurpS19C31FpS27TV2KEyxm4MFDH3vOdXEch1%2F3e38Di9S%2B6GPT7bFf1J6aBICwjkLXty2pVHxp9khNWJiPxu7MgbdO0DVx%2FyvdWg9f4SlhjOtKia3gxlbUtC0pVVnLzKsAD8iToG7ZX0FHwU5LrGWjWpe4sH7AugjP0qPan%2FKHr1I1VfMGp%2F2z2%2BPqWkUKNmlz29mNMA5NB5VHfSomenTTrd%2FbHDiKuiDaRW7XMddn%2FXlnzSNHfaqAmeDPZYRxuKaIBEUYpRnTKQcLgSdRI7zSm7pnGXzVf6vXIDWmKXTkhdL4xdTQMb6lZ7icZJmwJ1hDDOJloxA0QoolhUVDe4FA%2Fk5iWjJA1jbvu26wZph4O60QcISdmg%2FlewYWNJbz3FIvI5oYmw07M6lpGGLPGNCQVPkMtNMK5FbymvpPxTQxAeFn0ytuDdPZ8TAv50DTzHEj8V6cvak2WA1MH5Y0fZFdmhB2kxaKa%2Fo8O137hC%2B77X0zOh4dIZwGmaH2iRKJtFQ4nG%2B1RcmNxeA7VkATWpQc6yxz2V%2FFBMmmOZrN2fSdjq6eR2rvLp2HH7XpjYtGxBFWYwLTr0vQPJ8i2MWyGs5g6nRmayvGu7phs2rzc7Z%2FUvxT2buPBDCZrvsAwyMs1EgX2r3q0wgzwqAxcCRIE1EZFSpxZ3atb1BwUFfX7OAqbelfpvdWnvvqMmYCt%2Bqz5y%2Botudv7jZYmoigDo%2BL1l0X1RgTY7lTfBtmE7U6rXx%2BsSEmYpL%2FKL1gi5OWd7FL36EN%2FHDGI7edPAbrhtnNl5ZW%2F4E3Z7Kymd1hjSt3NrHaN7erJw9K2SD1KBqELZiwAUhfYUK0x5Ypfw%2FHCcK7yH3TdPPFOlwmIc4LHGs%2Fw61bZf0QuRyZBuPt9R6rXxIzc96ODZhGLNx2SIBC1Q9oEtsz1NQTArmBDtzwMjlgpZxYcBc5fC8D0jw6afrvVeNydhC3n6Cs1XxbRBbxQPm5j%2FK6%2FOSu%2FIF4QlsQCjNnNUGrostjDyFBCfBbeB0rCXbNIzysrY9PasXqum73svRwH2yA2wG%2Buwynfp16J3vPBjbOfMAOfEETyiUNQYYri8Y1pOjHzTyd6igl0qTJSke2rw1GWqPRA%2FCm04TRkgnLLd9uqkVfb4SxP8r6N%2B8oZp4MV898YhWrO5rrRKSDpqWdKNhc8J%2FS2uBRd9F3ZHhAIB8GQ6rKcdCLhbw3gizDD2idxEwlIr9hDiY3itQqqP9mkFI6lGIAmtRdsDskHrX710BSYgbK9Q2QTb7EHlzpy0v%2F9YgZotxI5Y4eQk1Txa%2BFCe9zQNsmUpSsdC9ppUHEPEhfCgLB37ENHYepZqum5nniZxWocbVZQmTZcqimePEanU19kYUsSYI73Jf4KnWlENIrYJftRvUOhurajoYguordn8VyGU81VFrs4JMcqyXPrfzuC%2F49AKeMoBr2YnyqSG31kcTvibT%2B9jBMhY3lMNhS2D25t7DzmlkMdSyq3yopRWSWnLvaonoglfOXbx2Q8GLVmswK3ukhJEb6pUJ78wGSTXJiuCP5nsvmmP%2F1ZxnpGTN%2F68EIOidNmGgs6ytioRsLpeyHgIuh9DqQ%2B25dzL7Z4Tdp%2FGn45Xz7%2B2WNg6cFBBshsdPITqSNbX8bkwEZ7rfHymioJbXFqmALylRmC%2ByouQY0bBjJaYjk5H67y7nBuoxf0FnV7OqJPoneixxjne3qQQlDOrZByb%2B3qjguArVsdbTUuhkLxPXnyjGjx0iNJ5wL7ZZ1a%2BhUbtgXnWVW%2BNfNgo%2FTECnkM9gVH55N7ibMnShPkO%2BnGCpPdI0NWKRXHwXSzAB%2FKJVSLluhQjklmGuD6cJGP78luhBPeX%2F8PH1xNJgNo6XenMRInkLZ%2Ba%2BLG5z78%2BVgJxb3lqfK3QXgKgtGbdrjwFILNH40mmMQ8mxR9JdrSJrbBa91%2FkldcWfJRgn3BmAVBhHU55SRDtUfQc2876AlStEw5%2BzZ%2FEbxKff7fs5i2VNm3a48kZYQjcPFJY9RkWY3197QBa6Lq%2F28hzgMMlrQ%2F3jDLmkoV%2FUij5pkHz7%2FCaa%2BXyfEAXk2Ib%2BD5KSSm79A2L11HumiqijOrmcA5fX2FUzlooa4Irk0RZeJg74pka77fJQG%2Bm45d1LTIy%2FLtPdXb5L03e8EEMrhalyohDDIMjmdEoTCUYyvx9FSQT%2BF5iCHaqkWQcTmfLQLgo9Ntl95LMe2ngqYoIYa9OJp9p43TQOT0DuKTSuawWo3XYaPvS8xonVw9AEL6%2BeTIJIdLOoMr8d76c15sxl2D8moZI%2B0VYtaapiyBMrj0d43e9GpATs%2FJBYIZROBM6QS5JSSwhWN9gHsLBxgwEBul4m3V9%2BLxO4nSm%2FWMdIrMMtiOlo6kA95TABJW9bu6XkBIfm2FFDFz5QHJvIVqr8c3PPrGcInnHwb%2Fvlu3638JigTGK0eoyI7%2FOrbcDS1FWEWUV6zkKSAdzKyNiPIQwlIVeTHmSZu727RLjXXXy3Mg58qXCmleBeMemYELysLjCegw6a9GbRiQiAHesnpOrz9PcGoH7JmWA6N2GWoTWMuUxH7n8O08v9vYt1OuWyUOVCgZqvH7GC32rKwMclfejFoPHhxaPKLCdSOWQNqCeTBVF9EhLLxFjh5J0SvSX%2FkLLf4BMRBlwZRQlq21uFuui2ObCTdLQdquxgE1xoxs8QK1I3kz3Ywfk6zEJy9HDnBbCg5SxQ8%2BuQJGBGs22tP%2FEtjzRZo5PA%2BWu0FAOFwMSB8Qy%2F8Wps5ZMD8cWcmddLBYg%2F%2B%2BFXDqs87bjSAGXOSx7L%2FsLTYXEiw%2FWJGsV75sQJgzOVmhzKuADi5XcUBvAxCKg3dnLIAo3ZTOo2pEuBCJ57TskFCSVouVrAMLGNr20c3i97z5wWXTTJRIyYoi5zMe69PlhCqRpEIn61fvQBsCQvNINNeNvJfB%2FCQH0fm3f8XXmb5%2F%2FROLfiICfdFZbbEFXfaSmXUIuLHp%2FFc2hJQGmSGzSuNcqGg1Cd6BKezZcepgjqQkaUChGNcEh4pxoEWT%2FdrQ%2Bra47uXquwSzKxiD2mkEolVdl7tAMM20PeWjOef6zReYtA19yXP%2BovlYv7U4z%2F3rjVvHG9Gggg3lfrVoP%2FKYsNBUfXJbbvAp%2Bd4txvLWmFM7T2a9JPvu0wpusjoFy7k8XzQLKVUgMAxAbNznAF1s%2BKm%2BNo216Qk%2F3cfaLZihystI6ripXNpMfzE0D%2FLoud%2BVeW808Eg8pP0VWg0V8%2FinUecm1K5mDpukRb2RqOQ%2FsAqjAtV6TCtTgJe447C5jtA1JjWPE4Vaz%2FyFCsqRJKvnSwFFiG7nZIwIJ%2FeW2JmB6uL0piQ2WQZ0751IewXt%2BpqA21RE4rBUppO0%2FvCdXb2v0efXLjGmfGoyRROpdmPD7X0zxrxnGpuqhXyHZfQRno1qK6tX86mGevNb1omuhkMi2rL%2FZ3cgCzjyykpQVCU%2F7pB6ORYAs%2BEVgsVI6BEVNQSRnWb5ms4AEjJH4NTqnR6gXBN3x0w0pOi0ZzMVHkLksPj6b6YG7Xp2164JaVaXxAKKT%2BfkrgvxISkfCM2UKuAb6S2RNje%2BhzgQ%2Bd16nnh9qEK3K5XACAXCzkVzYN32ESFEqJfdD3bt9yEv8nXUKYu9t%2B3BPM0XPYdpHHro%2BjL1Z9U8RDyz7w79hnI%2BQ4S011%2FYDQrs6e%2BKBYPz2f1q83mK1BrR0uLaONN9n6szvKPkvDtUw7oOEHPpucFJjtVNWx6%2BGZGBR4W15xpG5lbebdZa6lBkfJ2cETEK07h5y2K8n9seyMmbA%2BgKk8%2FSG5mrzKwjUpnOD7MD%2FxCcmTcmStNFACY%2B0btO7LGexGJUTVQ%2FVr2bq6TlmlSXSF3vB9bxtX0CUi09JeyMtiZLNjHIqJTTnmW0aiTOmEr77ELU0hzVRFnujMlo4T72qIbarXuIph8DIE1Mnzcf9sis%2FPbNxtfkso%2BIPm205LLSilQ9A48w379qYlziOUQtW2JpTxFOCcpHKOKYQSf7s9XmWAWTnSfcI0qZG8BYXjSs%2FLPxJLlZbpcWpSQzUWDpZ7N1q56gTshxT%2BySWzJ5ExdYNHdvNviOAUlE607x950tpCaIjnWfvwz7Kje8bNgsGrD7lHj5WHkmhxq8F%2BuTOeHpH4oPYWT8yjsqhdIxD091Q7pdumeEDgBS1ob%2FOuU5c6mJjk2GEVJiQKlDrF0YwItbdBGaEhp4%2BaxUYHIIYNYnf%2F6Qyh6ENbZxaZLK%2F6s9qGP6fb3EwPRujiT%2FpsMmb8LuB3wLdzw8qlYUKB80XLjR8rRfHGpMSU5naH8qEO5R7uTqnlJ1HosYLbYQLO9myJ3SPdApKRS0ii0K6lU5sm4asW%2BwU8wxpyEX%2FOoHBRHKL0CmvLAml8w8JHYQ6CazJutswAlfgjvNV%2FJELVF1KsjrovHZ2O1P8wMXJ7DHgEG1hlMAfWJcxyIpjWocacHwDoUvDzyOAFTe8VSEpoT2JuhaIOgQHzTpz9ogeSCvaBchkVSlvHjOtBop8xjaG2jLtfPri%2FPiSNwTsV3zAFdS08gi6XItAwEPick%2F3AN5ZWuVvhj%2FqMP4T6GKd%2Bda8ssRCABV1wS94qSUAGMGlwCwvqtdg2vhNvALqdCgF4843C4vri2zX%2FTirur1hFRBkcAn7NcG7Jl3qOZ1kAWswDpZDdYBqXwCX6H27Kr%2FSJyNcO9dqZUn8jJJE4JKCjbZI5PTsSeIuiy8fRZFjBgawpUpFs6EPBep8KjwCp8tMnbJ%2BrPhOZy1QVqJvefVflbvWjdAcB9LwADhdO605WCG09mQpo8U9slAOe%2BGzOFO5DoTkZBMMY3Ly8oUSj5STa6wPJVJBr3N6o4KbFTQf1JjPPiiMDMt3c%2F98Oyp6cdHq1kkkqeP%2FcVPgwYvF%2BcouVnyXfKigMLUTX7zh4cORTI6msk6cfPtkWfB4MSHj51BrQgLIfv8r7ZalUcbXPfcuKtG%2FSmk7yW3wxKOtaFGok3XHgp0GKvigkiPUBnODobePs%2F0JtwSYo9UJ4hMjs9m9pt3JQw%2FuLv8VBTm4jrpEyJSP0fIWWnq9G36%2BINvC4w04mfHZyG2E6VtLEIXnukdwNPi7noTxSg%2BQiJX2n5rRDlJPzMmrxFZnNImgXhaWhoxS4BUobQOOMHPriBlaYG52SekTM%2F%2BrQkuiI8hit66QwP2HhT4D6FMmZABHJtC1VesCa4kszDGsU1MdGUzV7p5JXllGpQtbTkCwF6HGKEo7Ri%2Fy4vBtXB%2F4CP4zSOEvfhknB3HMVh%2FIoj564N6JhNMu71nIaY2a1nZ9Qqs2oNo07w8og01mJN%2F1y8%2FRBdkYMjJRVBbETVPb7SrWZsj9oZRSpDoa9TAt1pTZP4lUmOS5f6RlIAekr6JOpUnxUvVOqBoOai9cX4JgdWwspZQj4q7T%2B4NCkSmd5x%2BG8DHFx0x0Ut0Wjz3aL9Msv26hmHUDzlwTQMOgRNGj4B9ovDA8dnrlJgOIPjM7oaGErSlDY6pr%2FXUH3QHVGPK5o0NevvbKCXyLFCRzVV6m5kRfSA29PoGVzdzWlS8g44oP1aTbnQzI0XLp5TUYPPYxX7PkQVP5%2By%2FbJsyik612D%2FgSPiwwCnJaP4o9rHo2ncn9rcrHZBJfBQgdEbQen0Y%2Be9PJDCx8&__EVENTVALIDATION=s2OfNtxJIZ5NZ7wRujyuK7NK%2FGT5blDyro3hGIWV91ZSX2MVPNce11oY2aruFAGHgY6KrdxAwn9dwwCt3ZH3sAhAPS2eGRxFxIYmC5vz%2Bs1iHefb%2BhejowY%2FnSXLAr4LQoC7ud0X92QF8bhtPoroUW1rFILASWuLrf9C6RBzQeZXMLOBWirvoKSlSlPVpahodKO8kSi8KlIGaTfRzazBmNbVx2S7RV8Uy%2FQHebP4c%2FvsuKml19N9biZytosMTl09NnZ5sQwhlSlQuHBjLBwR6pp%2F0ve%2FfHvV9LthDmYlzwtSM%2FIOpGrA0lJLmJ1AyvCYx%2FX4lzBXVXIYQq%2B%2FkhvB8f1vHxW4AZc9hpqNuuibj5i2tauPKGqhOZwN6688O0HUT%2B%2B4meg8lmxd3eD6fXFFn0gY%2BUPuERjR7w4AtgBmp0z%2BXhICOyj0%2FCAfmzTEvjr0mdrs4Z3Ka7pwJY5VThv97W%2FfbT0w9nYlHsOpiEtkoEjDoFUv6gUoE%2FDTDDTLIXLrqR3tP0PwXDMvHFJxY67cAjRCl8d7NOGpt7jDIa59vW%2BTc1vQCsHtkeS6LnfmauMii2DdFrW6r8sWVa%2FnYt03uNR6TxpMR5N5Vm3W3ocNN4g6HWhdw6OlQFQBCGDfbFcmSNrcTZZsXzJ4Z20okhypYv%2FDg1uIhdIO4V%2FbMPfJtZ1R%2FUhMrn02rnOc0O6ViP7k7DlTUtvr5sMx3anbUJVn48Do1uSoFz4twUL3LCHYYmKDePqYL9V8ph2RPtd3YSrn%2Fh080g62VKpM1PZjwovKNbBNh5y23RA3VJe0rW7QqRqMMC%2FJl6DVSvrgtVvQkhSBh%2FCrP5Ni9OQbEhnL%2FACqoq4BGWp%2FGLxxJtY%2BEA%3D%3D&__ASYNCPOST=false&";//2016-08-15
            string url = String.Format(channelToServer.Server);

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (var http = new HttpClient())
                {
                    http.DefaultRequestHeaders.Add("X-Requested-With", "XMLHttpRequest");

                    var stringContent = string.Format(postdata, channelToServer.Value, date.ToString("yyyy-MM-dd"));
                    var httpContent = new StringContent(stringContent, Encoding.UTF8, "application/x-www-form-urlencoded");
                    var response = http.PostAsync(url, httpContent).Result;

                    if (response.IsSuccessStatusCode)
                    {
                        var bytes = response.Content.ReadAsByteArrayAsync().Result;
                        String source = Encoding.GetEncoding("utf-8").GetString(bytes, 0, bytes.Length - 1);
                        source = System.Text.RegularExpressions.Regex.Unescape(source);
                        source = WebUtility.HtmlDecode(source.Replace("\"", ""));
                        HtmlDocument resultat = new HtmlDocument();
                        resultat.LoadHtml(source);

                        var schedules = resultat.DocumentNode.SelectNodes("//div[@class='schedule']");

                        if (schedules != null && schedules.Count > 0)
                        {
                            var items = schedules.FirstOrDefault().SelectNodes("table//tr");
                            foreach (var item in items)
                            {
                                string startOn = "", programName = "";
                                // get start time
                                var tdTags = item.SelectNodes("td");
                                if (tdTags != null && tdTags.Count > 1)
                                {
                                    startOn = tdTags.ElementAt(0).InnerText.Trim();
                                    programName = tdTags.ElementAt(1).InnerText.Trim();
                                }
                                if (!string.IsNullOrWhiteSpace(startOn))
                                {
                                    var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                    guideItems.Add(guideItem);
                                }
                            }
                        }
                    }
                }

            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        static public List<GuideItem> GetDataFromTHSTUrl(ChannelToServer channelToServer, DateTime date)
        {
            string postdata = "Ngay={0}&X-Requested-With=XMLHttpRequest";//16/08/2016
            string url = String.Format(channelToServer.Server);

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (var http = new HttpClient())
                {
                    http.DefaultRequestHeaders.Add("X-Requested-With", "XMLHttpRequest");

                    var stringContent = string.Format(postdata, date.ToString("dd/MM/yyyy"));
                    var httpContent = new StringContent(stringContent, Encoding.UTF8, "application/x-www-form-urlencoded");
                    var response = http.PostAsync(url, httpContent).Result;

                    if (response.IsSuccessStatusCode)
                    {
                        var bytes = response.Content.ReadAsByteArrayAsync().Result;
                        String source = Encoding.GetEncoding("utf-8").GetString(bytes, 0, bytes.Length - 1);
                        source = System.Text.RegularExpressions.Regex.Unescape(source);
                        HtmlDocument resultat = new HtmlDocument();
                        resultat.LoadHtml(source);

                        var schedules = resultat.DocumentNode.SelectNodes("//marquee");

                        if (schedules != null && schedules.Count > 0)
                        {
                            var items = schedules.FirstOrDefault().SelectNodes("p");
                            foreach (var item in items)
                            {
                                string startOn = "", programName = "";
                                var strdata = HttpUtility.HtmlDecode(item.InnerText).Trim().Trim('-').Trim();
                                var tdTags = strdata.Split(':');
                                if (tdTags != null && tdTags.Length > 1)
                                {
                                    startOn = tdTags.ElementAt(0).Trim();
                                    startOn = startOn.Replace('h', ':').Trim(':');
                                    programName = tdTags.ElementAt(1).Trim();
                                }
                                if (!string.IsNullOrWhiteSpace(startOn))
                                {
                                    var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                    guideItems.Add(guideItem);
                                }
                            }
                        }
                    }
                }

            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        static public List<GuideItem> GetDataFromBinhDinhTVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string postdata = "date={0}&xem=Xem+lịch+phát+sóng";
            string url = String.Format(channelToServer.Server);

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (var http = new HttpClient())
                {
                    http.DefaultRequestHeaders.Add("X-Requested-With", "XMLHttpRequest");

                    var stringContent = string.Format(postdata, date.ToString("yyyy-MM-dd"));
                    var httpContent = new StringContent(stringContent, Encoding.UTF8, "application/x-www-form-urlencoded");
                    var response = http.PostAsync(url, httpContent).Result;

                    if (response.IsSuccessStatusCode)
                    {
                        var bytes = response.Content.ReadAsByteArrayAsync().Result;
                        String source = Encoding.GetEncoding("utf-8").GetString(bytes, 0, bytes.Length - 1);
                        source = System.Text.RegularExpressions.Regex.Unescape(source);
                        HtmlDocument resultat = new HtmlDocument();
                        resultat.LoadHtml(source);

                        var schedules = resultat.DocumentNode.SelectNodes("//div[@id='printReady']");

                        if (schedules != null && schedules.Count > 0)
                        {
                            var items = schedules.FirstOrDefault().SelectNodes("table//tr/td//p");
                            for (int i = 0; i < items.Count; i++ )
                            {
                                var stritem = items[i].InnerText.Trim();
                                if (!string.IsNullOrEmpty(stritem) && stritem.Length > 10 && stritem[2] == 'h')
                                {
                                    string startOn = "", programName = "";
                                    stritem = WebUtility.HtmlDecode(stritem); 
                                    
                                    startOn = stritem.Substring(0, 5).Replace('h',':').Trim();
                                    programName = stritem.Substring(6).Trim();
                                    if (i + 1 < items.Count)
                                    {
                                        var strong = items[i + 1].SelectNodes("strong");
                                        if( strong != null && strong.Count > 0)
                                        {
                                            programName += ": " + WebUtility.HtmlDecode(strong.FirstOrDefault().InnerText).Trim();
                                            i = i + 1;
                                        }
                                    }
                                    if (!string.IsNullOrWhiteSpace(startOn))
                                    {
                                        var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                        guideItems.Add(guideItem);
                                    }
                                }
                            }
                        }
                    }
                }

            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }
        static public List<GuideItem> GetDataFromGIALAITVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server, date.ToString("yyyy-MM-dd"));

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    var response = http.GetByteArrayAsync(url).Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                    source = System.Text.RegularExpressions.Regex.Unescape(source);
                    source = WebUtility.HtmlDecode(source);
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(source);

                    var schedulerMain = resultat.DocumentNode.SelectNodes("//div[@class='lichphatsong']");
                    if (schedulerMain != null && schedulerMain.Count > 0)
                    {
                        var contents = schedulerMain.FirstOrDefault().SelectNodes("div[@class='content']");
                        if (contents != null && contents.Count > 0)
                        {
                            var str = contents.FirstOrDefault().InnerHtml.Replace("<br>", "^");

                            var items = str.Split('^');
                            if (items != null)
                            {
                                foreach (var item in items)
                                {
                                    int middle = item.IndexOf("\t");
                                    if (middle >5)
                                    {
                                        string startOn = "", programName = "";
                                        // get start time
                                        startOn = item.Substring(0, middle).Replace(":", "").Replace("h", ":").Trim();
                                        programName = item.Substring(middle).Trim();

                                        if (!string.IsNullOrWhiteSpace(startOn))
                                        {
                                            var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                            guideItems.Add(guideItem);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }
        static public List<GuideItem> GetDataFromDRTUrl(ChannelToServer channelToServer, DateTime date)
        {
            string postdata = "ScriptManager=dnn%24ctr444%24ViewLPS_UP%7Cdnn%24ctr444%24ViewLPS%24ddlkenh&dnn%24ctr444%24ViewLPS%24ddlkenh={0}&dnn%24ctr444%24ViewLPS%24txtdate={1}%2F{2}%2F{3}&sites=&ScrollTop=&__dnnVariable=&__EVENTTARGET=dnn%24ctr444%24ViewLPS%24ddlkenh&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE=%2FwEPDwUJMjgzMDU3ODk3D2QWBmYPFgIeBFRleHQFeTwhRE9DVFlQRSBodG1sIFBVQkxJQyAiLS8vVzNDLy9EVEQgWEhUTUwgMS4wIFRyYW5zaXRpb25hbC8vRU4iICJodHRwOi8vd3d3LnczLm9yZy9UUi94aHRtbDEvRFREL3hodG1sMS10cmFuc2l0aW9uYWwuZHRkIj5kAgEPZBYMAgEPFgIeB1Zpc2libGVoZAICDxYCHgdjb250ZW50BSlUcmFuZyB0aMO0bmcgdGluIMSRaeG7h24gdOG7rSAtIMSQw6BpIERSVGQCAw8WAh8CBUHEkMOgaSBQaMOhdCB0aGFuaCBUcnV54buBbiBow6xuaCDEkMOgIE7hurVuZyAtIERSVCxEb3ROZXROdWtlLEROTmQCBA8WAh8CBShDb3B5cmlnaHQgMjAwOSBieSBEb3ROZXROdWtlIENvcnBvcmF0aW9uZAIFDxYCHwIFC0RvdE5ldE51a2UgZAIGDxYCHwIFMsSQw6BpIFBow6F0IHRoYW5oIFRydXnhu4FuIGjDrG5oIMSQw6AgTuG6tW5nIC0gRFJUZAICD2QWAgIBD2QWAgIED2QWAmYPZBYOAgMPZBYCZg8PFgYeCENzc0NsYXNzBQR1c2VyHwAFBUxvZ2luHgRfIVNCAgJkZAIHD2QWAgIBD2QWCAIBDw8WAh8BaGRkAgMPZBYCAgEPZBYCAgEPZBYCZg9kFgICAg8WAh8BaGQCBQ8PFgIfAWhkZAIGDw8WAh8BaGRkAgkPZBYEAgEPZBYIAgEPDxYCHwFoZGQCAw9kFgICAQ9kFgICAQ9kFgJmD2QWAgICDxYCHwFoZAIFDw8WAh8BaGRkAgYPDxYCHwFoZGQCAw9kFggCAQ8PFgIfAWhkZAIDD2QWAgIBD2QWAgIBD2QWAmYPZBYCAgIPFgIfAWhkAgUPDxYCHwFoZGQCBg8PFgIfAWhkZAILD2QWAgIBD2QWCAIBDw8WAh8BaGRkAgMPZBYCAgEPZBYCAgEPZBYCZg9kFgICAg8WAh8BaGQCBQ8PFgIfAWhkZAIGDw8WAh8BaGRkAg0PZBYqAgEPZBYIAgEPDxYCHwFoZGQCAw9kFgICAQ9kFgJmD2QWAmYPZBYCAgEPZBYCZg9kFgICAQ8WAh4LXyFJdGVtQ291bnQCBBYMZg9kFgQCAQ8WAh8ABbYBPGRpdiBjbGFzcz0iQ2F0ZWdvcnkiPjxwPjxhIGhyZWY9Imh0dHA6Ly93d3cuZHJ0LmRhbmFuZy52bi9UYWJJRC82NS9DSUQvODgvZGVmYXVsdC5hc3B4Ij48aW1nIHNyYz0iL1BvcnRhbHMvMC9BTkggQkFOTkVSIEtPIFhPQS9DaHV5ZW5NdWMvY2hhb25nYXltb2kuanBnIiBib3JkZXI9IjAiIC8%2BPC9hPjwvcD48L2Rpdj5kAgMPFgIfAWhkAgEPZBYCZg8UKwIGZWUFjQI8ZGl2IGNsYXNzPSJEZXNjcmlwdGlvbiI%2BPHA%2BPGEgb25DbGljaz0ibWVkaWFwbGF5KCcvUG9ydGFscy8wL1ZpZGVvL0NIQU8gTkdBWSBNT0kvY2hhb25nYXltb2kgMTktOC0yMDE2LmZsdicsJycsMzIwLDI4NSkiIGhyZWY9IiN2Ij48aW1nIHdpZHRoPSIxNyIgaGVpZ2h0PSIxNyIgYm9yZGVyPSIwIiBhbGlnbj0ibGVmdCIgYWx0PSIiIHNyYz0iL1BvcnRhbHMvMC9JbWFnZXMvSWNvL2ljb0xpY2guZ2lmIiAvPiAmIzE2MDtOZ8OgeSAxOS04LTIwMTY8L2E%2BPC9wPjwvZGl2PmRlZWQCAg9kFgJmDxQrAgZlZQWNAjxkaXYgY2xhc3M9IkRlc2NyaXB0aW9uIj48cD48YSBvbkNsaWNrPSJtZWRpYXBsYXkoJy9Qb3J0YWxzLzAvVmlkZW8vQ0hBTyBOR0FZIE1PSS9jaGFvbmdheW1vaSAxOC04LTIwMTYuZmx2JywnJywzMjAsMjg1KSIgaHJlZj0iI3YiPjxpbWcgd2lkdGg9IjE3IiBoZWlnaHQ9IjE3IiBib3JkZXI9IjAiIGFsaWduPSJsZWZ0IiBhbHQ9IiIgc3JjPSIvUG9ydGFscy8wL0ltYWdlcy9JY28vaWNvTGljaC5naWYiIC8%2BICYjMTYwO05nw6B5IDE4LTgtMjAxNjwvYT48L3A%2BPC9kaXY%2BZGVlZAIDD2QWAmYPFCsCBmVlBY0CPGRpdiBjbGFzcz0iRGVzY3JpcHRpb24iPjxwPjxhIG9uQ2xpY2s9Im1lZGlhcGxheSgnL1BvcnRhbHMvMC9WaWRlby9DSEFPIE5HQVkgTU9JL2NoYW9uZ2F5bW9pIDE3LTgtMjAxNi5mbHYnLCcnLDMyMCwyODUpIiBocmVmPSIjdiI%2BPGltZyB3aWR0aD0iMTciIGhlaWdodD0iMTciIGJvcmRlcj0iMCIgYWxpZ249ImxlZnQiIGFsdD0iIiBzcmM9Ii9Qb3J0YWxzLzAvSW1hZ2VzL0ljby9pY29MaWNoLmdpZiIgLz4gJiMxNjA7TmfDoHkgMTctOC0yMDE2PC9hPjwvcD48L2Rpdj5kZWVkAgQPZBYCZg8UKwIGZWUFjQI8ZGl2IGNsYXNzPSJEZXNjcmlwdGlvbiI%2BPHA%2BPGEgb25DbGljaz0ibWVkaWFwbGF5KCcvUG9ydGFscy8wL1ZpZGVvL0NIQU8gTkdBWSBNT0kvY2hhb25nYXltb2kgMTYtOC0yMDE2LmZsdicsJycsMzIwLDI4NSkiIGhyZWY9IiN2Ij48aW1nIHdpZHRoPSIxNyIgaGVpZ2h0PSIxNyIgYm9yZGVyPSIwIiBhbGlnbj0ibGVmdCIgYWx0PSIiIHNyYz0iL1BvcnRhbHMvMC9JbWFnZXMvSWNvL2ljb0xpY2guZ2lmIiAvPiAmIzE2MDtOZ8OgeSAxNi04LTIwMTY8L2E%2BPC9wPjwvZGl2PmRlZWQCBQ9kFgICAQ8WAh8BaGQCBQ8PFgIfAWhkZAIGDw8WAh8BaGRkAgMPZBYIAgEPDxYCHwFoZGQCAw9kFgICAQ9kFgJmD2QWAmYPZBYCAgEPZBYCZg9kFgICAQ8WAh8FAgQWDGYPZBYEAgEPFgIfAAWxATxkaXYgY2xhc3M9IkNhdGVnb3J5Ij48cD48YSBocmVmPSJodHRwOi8vd3d3LmRydC5kYW5hbmcudm4vVGFiSUQvNjUvQ0lELzIzL2RlZmF1bHQuYXNweCI%2BPGltZyBzcmM9Ii9Qb3J0YWxzLzAvQU5IIEJBTk5FUiBLTyBYT0EvQ2h1eWVuTXVjL3Rob2lzdS5qcGciIGJvcmRlcj0iMCIgLz48L2E%2BPC9wPjwvZGl2PmQCAw8WAh8BaGQCAQ9kFgJmDxQrAgZlZQWHAjxkaXYgY2xhc3M9IkRlc2NyaXB0aW9uIj48cD48YSBocmVmPSIjdiIgb25DbGljaz0ibWVkaWFwbGF5KCcvUG9ydGFscy8wL1ZpZGVvL1RTdG9pRFJUMS9UU3RvaTE4LTgtMjAxNi5mbHYnLCcnLDMyMCwyODUpIj48aW1nIHdpZHRoPSIxNyIgaGVpZ2h0PSIxNyIgYm9yZGVyPSIwIiBhbGlnbj0ibGVmdCIgYWx0PSIiIHNyYz0iL1BvcnRhbHMvMC9JbWFnZXMvSWNvL2ljb0xpY2guZ2lmIiAvPlRo4budaSBz4buxIHThu5FpIDE4LTgtMjAxNjwvYT48L3A%2BPC9kaXY%2BZGVlZAICD2QWAmYPFCsCBmVlBYcCPGRpdiBjbGFzcz0iRGVzY3JpcHRpb24iPjxwPjxhIGhyZWY9IiN2IiBvbkNsaWNrPSJtZWRpYXBsYXkoJy9Qb3J0YWxzLzAvVmlkZW8vVFN0b2lEUlQxL1RTdG9pMTctOC0yMDE2LmZsdicsJycsMzIwLDI4NSkiPjxpbWcgd2lkdGg9IjE3IiBoZWlnaHQ9IjE3IiBib3JkZXI9IjAiIGFsaWduPSJsZWZ0IiBhbHQ9IiIgc3JjPSIvUG9ydGFscy8wL0ltYWdlcy9JY28vaWNvTGljaC5naWYiIC8%2BVGjhu51pIHPhu7EgdOG7kWkgMTctOC0yMDE2PC9hPjwvcD48L2Rpdj5kZWVkAgMPZBYCZg8UKwIGZWUFhwI8ZGl2IGNsYXNzPSJEZXNjcmlwdGlvbiI%2BPHA%2BPGEgaHJlZj0iI3YiIG9uQ2xpY2s9Im1lZGlhcGxheSgnL1BvcnRhbHMvMC9WaWRlby9UU3RvaURSVDEvVFN0b2kxNi04LTIwMTYuZmx2JywnJywzMjAsMjg1KSI%2BPGltZyB3aWR0aD0iMTciIGhlaWdodD0iMTciIGJvcmRlcj0iMCIgYWxpZ249ImxlZnQiIGFsdD0iIiBzcmM9Ii9Qb3J0YWxzLzAvSW1hZ2VzL0ljby9pY29MaWNoLmdpZiIgLz5UaOG7nWkgc%2BG7sSB04buRaSAxNi04LTIwMTY8L2E%2BPC9wPjwvZGl2PmRlZWQCBA9kFgJmDxQrAgZlZQWHAjxkaXYgY2xhc3M9IkRlc2NyaXB0aW9uIj48cD48YSBocmVmPSIjdiIgb25DbGljaz0ibWVkaWFwbGF5KCcvUG9ydGFscy8wL1ZpZGVvL1RTdG9pRFJUMS9UU3RvaTE1LTgtMjAxNi5mbHYnLCcnLDMyMCwyODUpIj48aW1nIHdpZHRoPSIxNyIgaGVpZ2h0PSIxNyIgYm9yZGVyPSIwIiBhbGlnbj0ibGVmdCIgYWx0PSIiIHNyYz0iL1BvcnRhbHMvMC9JbWFnZXMvSWNvL2ljb0xpY2guZ2lmIiAvPlRo4budaSBz4buxIHThu5FpIDE1LTgtMjAxNjwvYT48L3A%2BPC9kaXY%2BZGVlZAIFD2QWAgIBDxYCHwFoZAIFDw8WAh8BaGRkAgYPDxYCHwFoZGQCBQ9kFggCAQ8PFgIfAWhkZAIDD2QWAgIBD2QWAmYPZBYCZg9kFgICAQ9kFgJmD2QWAgIBDxYCHwUCAhYIZg9kFgQCAQ8WAh8ABa8BPGRpdiBjbGFzcz0iQ2F0ZWdvcnkiPjxwPjxhIGhyZWY9Imh0dHA6Ly93d3cuZHJ0LmRhbmFuZy52bi9UYWJJRC82NS9DSUQvODIvZGVmYXVsdC5hc3B4Ij48aW1nIHNyYz0iL1BvcnRhbHMvMC9BTkggQkFOTkVSIEtPIFhPQS9DaHV5ZW5NdWMvQVRUUC5qcGciIGJvcmRlcj0iMCIgLz48L2E%2BPC9wPjwvZGl2PmQCAw8WAh8BaGQCAQ9kFgJmDxQrAgZlZQWcAjxkaXYgY2xhc3M9IkRlc2NyaXB0aW9uIj48cD48YSBocmVmPSIjdiIgb25DbGljaz0ibWVkaWFwbGF5KCcvUG9ydGFscy8wL1ZpZGVvL0FOIFRPQU4gVEhVQyBQSEFNL0FUVFAgMTMtNy0yMDE2LmZsdicsJycsMzIwLDI4NSkiPjxpbWcgd2lkdGg9IjE3IiBoZWlnaHQ9IjE3IiBib3JkZXI9IjAiIGFsaWduPSJsZWZ0IiBzcmM9Ii9Qb3J0YWxzLzAvSW1hZ2VzL0ljby9pY29MaWNoLmdpZiIgYWx0PSIiIC8%2BICYjMTYwOyBBbiB0b8OgbiB0aOG7sWMgcGjhuqltIDEzLTctMjAxNjwvYT48L3A%2BPC9kaXY%2BZGVlZAICD2QWAmYPFCsCBmVlBZwCPGRpdiBjbGFzcz0iRGVzY3JpcHRpb24iPjxwPjxhIGhyZWY9IiN2IiBvbkNsaWNrPSJtZWRpYXBsYXkoJy9Qb3J0YWxzLzAvVmlkZW8vQU4gVE9BTiBUSFVDIFBIQU0vQVRUUCAxMy02LTIwMTYuZmx2JywnJywzMjAsMjg1KSI%2BPGltZyB3aWR0aD0iMTciIGhlaWdodD0iMTciIGJvcmRlcj0iMCIgYWxpZ249ImxlZnQiIHNyYz0iL1BvcnRhbHMvMC9JbWFnZXMvSWNvL2ljb0xpY2guZ2lmIiBhbHQ9IiIgLz4gJiMxNjA7IEFuIHRvw6BuIHRo4buxYyBwaOG6qW0gMTMtNi0yMDE2PC9hPjwvcD48L2Rpdj5kZWVkAgMPZBYCAgEPFgIfAWhkAgUPDxYCHwFoZGQCBg8PFgIfAWhkZAIHD2QWCAIBDw8WAh8BaGRkAgMPZBYCAgEPZBYCZg9kFgJmD2QWAgIBD2QWAmYPZBYCAgEPFgIfBQICFghmD2QWBAIBDxYCHwAFswE8ZGl2IGNsYXNzPSJDYXRlZ29yeSI%2BPHA%2BPGEgaHJlZj0iaHR0cDovL3d3dy5kcnQuZGFuYW5nLnZuL1RhYklELzY1L0NJRC85NS9kZWZhdWx0LmFzcHgiPjxpbWcgc3JjPSIvUG9ydGFscy8wL0FOSCBCQU5ORVIgS08gWE9BL0NodXllbk11Yy9kcnQgbmV3MS5qcGciIGJvcmRlcj0iMCIgLz48L2E%2BPC9wPjwvZGl2PmQCAw8WAh8BaGQCAQ9kFgJmDxQrAgZlZQWFAjxkaXYgY2xhc3M9IkRlc2NyaXB0aW9uIj48cD48YSBocmVmPSIjdiIgb25DbGljaz0ibWVkaWFwbGF5KCcvUG9ydGFscy8wL1ZpZGVvL1RJRU5HIEFOSC9uZ2F5IDE4LTgtMjAxNi5mbHYnLCcnLDMyMCwyODUpIj48aW1nIHdpZHRoPSIxNyIgaGVpZ2h0PSIxNyIgYm9yZGVyPSIwIiBhbGlnbj0ibGVmdCIgc3JjPSIvUG9ydGFscy8wL0ltYWdlcy9JY28vaWNvTGljaC5naWYiIGFsdD0iIiAvPiAmIzE2MDtEUlQgTmV3cyAxOC04LTIwMTY8L2E%2BPC9wPjwvZGl2PmRlZWQCAg9kFgJmDxQrAgZlZQWFAjxkaXYgY2xhc3M9IkRlc2NyaXB0aW9uIj48cD48YSBocmVmPSIjdiIgb25DbGljaz0ibWVkaWFwbGF5KCcvUG9ydGFscy8wL1ZpZGVvL1RJRU5HIEFOSC9uZ2F5IDE3LTgtMjAxNi5mbHYnLCcnLDMyMCwyODUpIj48aW1nIHdpZHRoPSIxNyIgaGVpZ2h0PSIxNyIgYm9yZGVyPSIwIiBhbGlnbj0ibGVmdCIgc3JjPSIvUG9ydGFscy8wL0ltYWdlcy9JY28vaWNvTGljaC5naWYiIGFsdD0iIiAvPiAmIzE2MDtEUlQgTmV3cyAxNy04LTIwMTY8L2E%2BPC9wPjwvZGl2PmRlZWQCAw9kFgICAQ8WAh8BaGQCBQ8PFgIfAWhkZAIGDw8WAh8BaGRkAgkPZBYIAgEPDxYCHwFoZGQCAw9kFgICAQ9kFgJmD2QWAmYPZBYCAgEPZBYCZg9kFgICAQ8WAh8FAgIWCGYPZBYEAgEPFgIfAAWvATxkaXYgY2xhc3M9IkNhdGVnb3J5Ij48cD48YSBocmVmPSJodHRwOi8vd3d3LmRydC5kYW5hbmcudm4vVGFiSUQvNjUvQ0lELzg3L2RlZmF1bHQuYXNweCI%2BPGltZyBzcmM9Ii9Qb3J0YWxzLzAvQU5IIEJBTk5FUiBLTyBYT0EvQ2h1eWVuTXVjL2FuZ3QuanBnIiBib3JkZXI9IjAiIC8%2BPC9hPjwvcD48L2Rpdj5kAgMPFgIfAWhkAgEPZBYCZg8UKwIGZWUFjQI8ZGl2IGNsYXNzPSJEZXNjcmlwdGlvbiI%2BPHA%2BPGEgaHJlZj0iI3YiIG9uQ2xpY2s9Im1lZGlhcGxheSgnL1BvcnRhbHMvMC9WaWRlby9BVEdUL0FUR1QgMDktOC0yMDE2LmZsdicsJycsMzIwLDI4NSkiPjxpbWcgd2lkdGg9IjE3IiBoZWlnaHQ9IjE3IiBib3JkZXI9IjAiIGFsaWduPSJsZWZ0IiBzcmM9Ii9Qb3J0YWxzLzAvSW1hZ2VzL0ljby9pY29MaWNoLmdpZiIgYWx0PSIiIC8%2BICYjMTYwOyBBbiB0b8OgbiBnaWFvIHRow7RuZyAwOS04LTIwMTY8L2E%2BPC9wPjwvZGl2PmRlZWQCAg9kFgJmDxQrAgZlZQWMAjxkaXYgY2xhc3M9IkRlc2NyaXB0aW9uIj48cD48YSBocmVmPSIjdiIgb25DbGljaz0ibWVkaWFwbGF5KCcvUG9ydGFscy8wL1ZpZGVvL0FUR1QvQVRHVDI2LTctMjAxNi5mbHYnLCcnLDMyMCwyODUpIj48aW1nIHdpZHRoPSIxNyIgaGVpZ2h0PSIxNyIgYm9yZGVyPSIwIiBhbGlnbj0ibGVmdCIgc3JjPSIvUG9ydGFscy8wL0ltYWdlcy9JY28vaWNvTGljaC5naWYiIGFsdD0iIiAvPiAmIzE2MDsgQW4gdG%2FDoG4gZ2lhbyB0aMO0bmcgMjYtNy0yMDE2PC9hPjwvcD48L2Rpdj5kZWVkAgMPZBYCAgEPFgIfAWhkAgUPDxYCHwFoZGQCBg8PFgIfAWhkZAILD2QWCAIBDw8WAh8BaGRkAgMPZBYCAgEPZBYCZg9kFgJmD2QWAgIBD2QWAmYPZBYCAgEPFgIfBQICFghmD2QWBAIBDxYCHwAFrwE8ZGl2IGNsYXNzPSJDYXRlZ29yeSI%2BPHA%2BPGEgaHJlZj0iaHR0cDovL3d3dy5kcnQuZGFuYW5nLnZuL1RhYklELzY1L0NJRC81MS9kZWZhdWx0LmFzcHgiPjxpbWcgc3JjPSIvUG9ydGFscy8wL0FOSCBCQU5ORVIgS08gWE9BL0NodXllbk11Yy90cG10LmpwZyIgYm9yZGVyPSIwIiAvPjwvYT48L3A%2BPC9kaXY%2BZAIDDxYCHwFoZAIBD2QWAmYPFCsCBmVlBZMCPGRpdiBjbGFzcz0iRGVzY3JpcHRpb24iPjxwPjxhIG9uQ2xpY2s9Im1lZGlhcGxheSgnL1BvcnRhbHMvMC9WaWRlby9UUE1UL1RQTVQgMTYtOC0yMDE2LmZsdicsJycsMzIwLDI4NSkiIGhyZWY9IiN2Ij48aW1nIHdpZHRoPSIxNyIgaGVpZ2h0PSIxNyIgYm9yZGVyPSIwIiBhbGlnbj0ibGVmdCIgYWx0PSIiIHNyYz0iL1BvcnRhbHMvMC9JbWFnZXMvSWNvL2ljb0xpY2guZ2lmIiAvPiAmIzE2MDtUaMOgbmggcGjhu5EgbcO0aSB0csaw4budbmcgMDItOC0yMDE2PC9hPjwvcD48L2Rpdj5kZWVkAgIPZBYCZg8UKwIGZWUFkwI8ZGl2IGNsYXNzPSJEZXNjcmlwdGlvbiI%2BPHA%2BPGEgb25DbGljaz0ibWVkaWFwbGF5KCcvUG9ydGFscy8wL1ZpZGVvL1RQTVQvVFBNVCAwMi04LTIwMTYuZmx2JywnJywzMjAsMjg1KSIgaHJlZj0iI3YiPjxpbWcgd2lkdGg9IjE3IiBoZWlnaHQ9IjE3IiBib3JkZXI9IjAiIGFsaWduPSJsZWZ0IiBhbHQ9IiIgc3JjPSIvUG9ydGFscy8wL0ltYWdlcy9JY28vaWNvTGljaC5naWYiIC8%2BICYjMTYwO1Row6BuaCBwaOG7kSBtw7RpIHRyxrDhu51uZyAxOS03LTIwMTY8L2E%2BPC9wPjwvZGl2PmRlZWQCAw9kFgICAQ8WAh8BaGQCBQ8PFgIfAWhkZAIGDw8WAh8BaGRkAg0PZBYIAgEPDxYCHwFoZGQCAw9kFgICAQ9kFgJmD2QWAmYPZBYCAgEPZBYCZg9kFgICAQ8WAh8FAgIWCGYPZBYEAgEPFgIfAAWxATxkaXYgY2xhc3M9IkNhdGVnb3J5Ij48cD48YSBocmVmPSJodHRwOi8vd3d3LmRydC5kYW5hbmcudm4vVGFiSUQvNjUvQ0lELzk0L2RlZmF1bHQuYXNweCI%2BPGltZyBzcmM9Ii9Qb3J0YWxzLzAvQU5IIEJBTk5FUiBLTyBYT0EvQ2h1eWVuTXVjL1ZIVk1EVC5qcGciIGJvcmRlcj0iMCIgLz48L2E%2BPC9wPjwvZGl2PmQCAw8WAh8BaGQCAQ9kFgJmDxQrAgZlZQWDAjxkaXYgY2xhc3M9IkRlc2NyaXB0aW9uIj48cD48YSBocmVmPSIjdiIgb25DbGljaz0ibWVkaWFwbGF5KCcvUG9ydGFscy8wL1ZpZGVvL1ZILVZNLURUL1ZIVk1EVCAwOS03LTIwMTYuZmx2JywnJywzMjAsMjg1KSI%2BPGltZyB3aWR0aD0iMTciIGhlaWdodD0iMTciIGJvcmRlcj0iMCIgYWxpZ249ImxlZnQiIHNyYz0iL1BvcnRhbHMvMC9JbWFnZXMvSWNvL2ljb0xpY2guZ2lmIiBhbHQ9IiIgLz4gJiMxNjA7TmfDoHkgMDktNy0yMDE2PC9hPjwvcD48L2Rpdj5kZWVkAgIPZBYCZg8UKwIGZWUFgwI8ZGl2IGNsYXNzPSJEZXNjcmlwdGlvbiI%2BPHA%2BPGEgaHJlZj0iI3YiIG9uQ2xpY2s9Im1lZGlhcGxheSgnL1BvcnRhbHMvMC9WaWRlby9WSC1WTS1EVC9WSFZNRFQgMjctNi0yMDE2LmZsdicsJycsMzIwLDI4NSkiPjxpbWcgd2lkdGg9IjE3IiBoZWlnaHQ9IjE3IiBib3JkZXI9IjAiIGFsaWduPSJsZWZ0IiBzcmM9Ii9Qb3J0YWxzLzAvSW1hZ2VzL0ljby9pY29MaWNoLmdpZiIgYWx0PSIiIC8%2BICYjMTYwO05nw6B5IDI3LTYtMjAxNjwvYT48L3A%2BPC9kaXY%2BZGVlZAIDD2QWAgIBDxYCHwFoZAIFDw8WAh8BaGRkAgYPDxYCHwFoZGQCDw9kFggCAQ8PFgIfAWhkZAIDD2QWAgIBD2QWAmYPZBYCZg9kFgICAQ9kFgJmD2QWAgIBDxYCHwUCAhYIZg9kFgQCAQ8WAh8ABa8BPGRpdiBjbGFzcz0iQ2F0ZWdvcnkiPjxwPjxhIGhyZWY9Imh0dHA6Ly93d3cuZHJ0LmRhbmFuZy52bi9UYWJJRC82NS9DSUQvNTkvZGVmYXVsdC5hc3B4Ij48aW1nIHNyYz0iL1BvcnRhbHMvMC9BTkggQkFOTkVSIEtPIFhPQS9DaHV5ZW5NdWMvcHN0bC5qcGciIGJvcmRlcj0iMCIgLz48L2E%2BPC9wPjwvZGl2PmQCAw8WAh8BaGQCAQ9kFgJmDxQrAgZlZQWeAjxkaXYgY2xhc3M9IkRlc2NyaXB0aW9uIj48cD48YSBvbkNsaWNrPSJtZWRpYXBsYXkoJy9Qb3J0YWxzLzAvVmlkZW8vUEhPTkcgU1UgVEFJIExJRVUvTWFUdXkuZmx2JywnJywzMjAsMjg1KSIgaHJlZj0iI3YiPjxpbWcgd2lkdGg9IjE3IiBoZWlnaHQ9IjE3IiBib3JkZXI9IjAiIGFsaWduPSJsZWZ0IiBhbHQ9IiIgc3JjPSIvUG9ydGFscy8wL0ltYWdlcy9JY28vaWNvTGljaC5naWYiIC8%2BJiMxNjA7IMSQ4burbmcgdGjhu60gTWEgdHXDvSBkw7kgY2jhu4kgbeG7mXQgbOG6p24gPC9hPjwvcD48L2Rpdj5kZWVkAgIPZBYCZg8UKwIGZWUFugI8ZGl2IGNsYXNzPSJEZXNjcmlwdGlvbiI%2BPHA%2BPGEgb25DbGljaz0ibWVkaWFwbGF5KCcvUG9ydGFscy8wL1ZpZGVvL1BIT05HIFNVIFRBSSBMSUVVL0RORGluaERvY0xhcC5mbHYnLCcnLDMyMCwyODUpIiBocmVmPSIjdiI%2BPGltZyB3aWR0aD0iMTciIGhlaWdodD0iMTciIGJvcmRlcj0iMCIgYWxpZ249ImxlZnQiIGFsdD0iIiBzcmM9Ii9Qb3J0YWxzLzAvSW1hZ2VzL0ljby9pY29MaWNoLmdpZiIgLz4mIzE2MDtOZ8aw4budaSDEkMOgIE7hurVuZyB0cm9uZyBkaW5oIMSQ4buZYyBs4bqtcCBuZ8OgeSDEkeG6oWkgdGjhuq9uZyA8L2E%2BPC9wPjwvZGl2PmRlZWQCAw9kFgICAQ8WAh8BaGQCBQ8PFgIfAWhkZAIGDw8WAh8BaGRkAhEPZBYIAgEPDxYCHwFoZGQCAw9kFgICAQ9kFgJmD2QWAmYPZBYCAgEPZBYCZg9kFgICAQ8WAh8FAgIWCGYPZBYEAgEPFgIfAAWwATxkaXYgY2xhc3M9IkNhdGVnb3J5Ij48cD48YSBocmVmPSJodHRwOi8vd3d3LmRydC5kYW5hbmcudm4vVGFiSUQvNjUvQ0lELzU0L2RlZmF1bHQuYXNweCI%2BPGltZyBzcmM9Ii9Qb3J0YWxzLzAvQU5IIEJBTk5FUiBLTyBYT0EvQ2h1eWVuTXVjL3R2bmRuLmpwZyIgYm9yZGVyPSIwIiAvPjwvYT48L3A%2BPC9kaXY%2BZAIDDxYCHwFoZAIBD2QWAmYPFCsCBmVlBYgCPGRpdiBjbGFzcz0iRGVzY3JpcHRpb24iPjxwPjxhIG9uQ2xpY2s9Im1lZGlhcGxheSgnL1BvcnRhbHMvMC9WaWRlby9UVk5ETi9Nb3R0aG9pLmZsdicsJycsMzIwLDI4NSkiIGhyZWY9IiN2Ij48aW1nIHdpZHRoPSIxNyIgaGVpZ2h0PSIxNyIgYm9yZGVyPSIwIiBhbGlnbj0ibGVmdCIgYWx0PSIiIHNyYz0iL1BvcnRhbHMvMC9JbWFnZXMvSWNvL2ljb0xpY2guZ2lmIiAvPiAmIzE2MDtN4buZdCB0aOG7nWkgdsOgIG3Do2kgbcOjaTwvYT4mIzE2MDs8L3A%2BPC9kaXY%2BZGVlZAICD2QWAmYPFCsCBmVlBagCPGRpdiBjbGFzcz0iRGVzY3JpcHRpb24iPjxwPjxhIG9uQ2xpY2s9Im1lZGlhcGxheSgnL1BvcnRhbHMvMC9WaWRlby9UVk5ETi9WdUR1Y1Nhb0JpZW4uZmx2JywnJywzMjAsMjg1KSIgaHJlZj0iI3YiPjxpbWcgd2lkdGg9IjE3IiBoZWlnaHQ9IjE3IiBib3JkZXI9IjAiIGFsaWduPSJsZWZ0IiBhbHQ9IiIgc3JjPSIvUG9ydGFscy8wL0ltYWdlcy9JY28vaWNvTGljaC5naWYiIC8%2BICYjMTYwO0dpYW8gbMawdSB0w6FjIGdp4bqjIGNhIGtow7pjOiBUaHUgaMOhdCBjaG8gbmfGsOG7nWkgPC9hPiYjMTYwOzwvcD48L2Rpdj5kZWVkAgMPZBYCAgEPFgIfAWhkAgUPDxYCHwFoZGQCBg8PFgIfAWhkZAITD2QWCAIBDw8WAh8BaGRkAgMPZBYCAgEPZBYCZg9kFgJmD2QWAgIBD2QWAmYPZBYCAgEPFgIfBQICFghmD2QWBAIBDxYCHwAF0gI8ZGl2IGNsYXNzPSJDYXRlZ29yeSI%2BPHA%2BPGEgaHJlZj0iaHR0cDovL3d3dy5kcnQuZGFuYW5nLnZuL1RhYklELzY1L0NJRC8xMDAvZGVmYXVsdC5hc3B4Ij48cD48ZW1iZWQgd2lkdGg9IjIwMyIgaGVpZ2h0PSI4MyIgbWVudT0idHJ1ZSIgbG9vcD0idHJ1ZSIgcGxheT0idHJ1ZSIgc3JjPSIvUG9ydGFscy8wL0FOSCBCQU5ORVIgS08gWE9BL0NodXllbk11Yy9WVFVNc3VhLnN3ZiIgcGx1Z2luc3BhZ2U9Imh0dHA6Ly93d3cubWFjcm9tZWRpYS5jb20vZ28vZ2V0Zmxhc2hwbGF5ZXIiIHR5cGU9ImFwcGxpY2F0aW9uL3gtc2hvY2t3YXZlLWZsYXNoIj48L2VtYmVkPjwvcD48L2E%2BPC9wPjwvZGl2PmQCAw8WAh8BaGQCAQ9kFgJmDxQrAgZlZQWgAjxkaXYgY2xhc3M9IkRlc2NyaXB0aW9uIj48cD48YSBocmVmPSIjdiIgb25DbGljaz0ibWVkaWFwbGF5KCcvUG9ydGFscy8wL1ZpZGVvL1ZJRVQgVElFUCBVT0MgTU8vVlRVTSBUNy0yMDE2LmZsdicsJycsMzIwLDI4NSkiPjxpbWcgaGVpZ2h0PSIxNyIgd2lkdGg9IjE3IiBib3JkZXI9IjAiIGFsaWduPSJsZWZ0IiBzcmM9Ii9Qb3J0YWxzLzAvSW1hZ2VzL0ljby9pY29MaWNoLmdpZiIgYWx0PSIiIC8%2BICYjMTYwO1Zp4bq%2FdCB0aeG6v3AgxrDhu5tjIG3GoSB0aMOhbmcgNyAtMjAxNiA8L2E%2BPC9wPjwvZGl2PmRlZWQCAg9kFgJmDxQrAgZlZQWZAjxkaXYgY2xhc3M9IkRlc2NyaXB0aW9uIj48cD48YSBocmVmPSIjdiIgb25DbGljaz0ibWVkaWFwbGF5KCcvUG9ydGFscy8wL1ZpZGVvL1ZJRVQgVElFUCBVT0MgTU8vVlROQSBUNi0yMDE2LmZsdicsJycsMzIwLDI4NSkiPjxpbWcgaGVpZ2h0PSIxNyIgd2lkdGg9IjE3IiBib3JkZXI9IjAiIGFsaWduPSJsZWZ0IiBzcmM9Ii9Qb3J0YWxzLzAvSW1hZ2VzL0ljby9pY29MaWNoLmdpZiIgYWx0PSIiIC8%2BICYjMTYwO1bDsm5nIHRheSBuaMOibiDDoWkgdGjDoW5nIDYtMjAxNjwvYT48L3A%2BPC9kaXY%2BZGVlZAIDD2QWAgIBDxYCHwFoZAIFDw8WAh8BaGRkAgYPDxYCHwFoZGQCFQ9kFggCAQ8PFgIfAWhkZAIDD2QWAgIBD2QWAmYPZBYCZg9kFgICAQ9kFgJmD2QWAgIBDxYCHwUCAhYIZg9kFgQCAQ8WAh8ABbIBPGRpdiBjbGFzcz0iQ2F0ZWdvcnkiPjxwPjxhIGhyZWY9Imh0dHA6Ly93d3cuZHJ0LmRhbmFuZy52bi9UYWJJRC82NS9DSUQvNTIvZGVmYXVsdC5hc3B4Ij48aW1nIHNyYz0iL1BvcnRhbHMvMC9BTkggQkFOTkVSIEtPIFhPQS9DaHV5ZW5NdWMvMTE0IExBWS5qcGciIGJvcmRlcj0iMCIgLz48L2E%2BPC9wPjwvZGl2PmQCAw8WAh8BaGQCAQ9kFgJmDxQrAgZlZQWTAjxkaXYgY2xhc3M9IkRlc2NyaXB0aW9uIj48cD48YSBocmVmPSIjdiIgb25DbGljaz0ibWVkaWFwbGF5KCcvUG9ydGFscy8wL1ZpZGVvL1BDQ0MvUENDQyAyOC03LTIwMTYuZmx2JywnJywzMjAsMjg1KSI%2BPGltZyB3aWR0aD0iMTciIGhlaWdodD0iMTciIGJvcmRlcj0iMCIgYWxpZ249ImxlZnQiIHNyYz0iL1BvcnRhbHMvMC9JbWFnZXMvSWNvL2ljb0xpY2guZ2lmIiBhbHQ9IiIgLz4gJiMxNjA7IFBow7JuZyBjaMOheSBjaOG7r2EgY2jDoXkgMjgtNy0yMDE2IDwvYT48L3A%2BPC9kaXY%2BZGVlZAICD2QWAmYPFCsCBmVlBZMCPGRpdiBjbGFzcz0iRGVzY3JpcHRpb24iPjxwPjxhIGhyZWY9IiN2IiBvbkNsaWNrPSJtZWRpYXBsYXkoJy9Qb3J0YWxzLzAvVmlkZW8vUENDQy9QQ0NDIDE0LTctMjAxNi5mbHYnLCcnLDMyMCwyODUpIj48aW1nIHdpZHRoPSIxNyIgaGVpZ2h0PSIxNyIgYm9yZGVyPSIwIiBhbGlnbj0ibGVmdCIgc3JjPSIvUG9ydGFscy8wL0ltYWdlcy9JY28vaWNvTGljaC5naWYiIGFsdD0iIiAvPiAmIzE2MDsgUGjDsm5nIGNow6F5IGNo4buvYSBjaMOheSAxNC03LTIwMTYgPC9hPjwvcD48L2Rpdj5kZWVkAgMPZBYCAgEPFgIfAWhkAgUPDxYCHwFoZGQCBg8PFgIfAWhkZAIXD2QWCAIBDw8WAh8BaGRkAgMPZBYCAgEPZBYCZg9kFgJmD2QWAgIBD2QWAmYPZBYCAgEPFgIfBQICFghmD2QWBAIBDxYCHwAFrwE8ZGl2IGNsYXNzPSJDYXRlZ29yeSI%2BPHA%2BPGEgaHJlZj0iaHR0cDovL3d3dy5kcnQuZGFuYW5nLnZuL1RhYklELzY1L0NJRC81NS9kZWZhdWx0LmFzcHgiPjxpbWcgc3JjPSIvUG9ydGFscy8wL0FOSCBCQU5ORVIgS08gWE9BL0NodXllbk11Yy9BTkROLnBuZyIgYm9yZGVyPSIwIiAvPjwvYT48L3A%2BPC9kaXY%2BZAIDDxYCHwFoZAIBD2QWAmYPFCsCBmVlBY0CPGRpdiBjbGFzcz0iRGVzY3JpcHRpb24iPjxwPjxhIGhyZWY9IiN2IiBvbkNsaWNrPSJtZWRpYXBsYXkoJy9Qb3J0YWxzLzAvVmlkZW8vQU5ETi9BTkROIDA2LTgtMjAxNi5mbHYnLCcnLDMyMCwyODUpIj48aW1nIHdpZHRoPSIxNyIgaGVpZ2h0PSIxNyIgYm9yZGVyPSIwIiBhbGlnbj0ibGVmdCIgc3JjPSIvUG9ydGFscy8wL0ltYWdlcy9JY28vaWNvTGljaC5naWYiIGFsdD0iIiAvPiAmIzE2MDsgQW4gbmluaCDEkMOgIE7hurVuZyAwNi04LTIwMTYgPC9hPjwvcD48L2Rpdj5kZWVkAgIPZBYCZg8UKwIGZWUFjQI8ZGl2IGNsYXNzPSJEZXNjcmlwdGlvbiI%2BPHA%2BPGEgaHJlZj0iI3YiIG9uQ2xpY2s9Im1lZGlhcGxheSgnL1BvcnRhbHMvMC9WaWRlby9BTkROL0FORE4gMzAtNy0yMDE2LmZsdicsJycsMzIwLDI4NSkiPjxpbWcgd2lkdGg9IjE3IiBoZWlnaHQ9IjE3IiBib3JkZXI9IjAiIGFsaWduPSJsZWZ0IiBzcmM9Ii9Qb3J0YWxzLzAvSW1hZ2VzL0ljby9pY29MaWNoLmdpZiIgYWx0PSIiIC8%2BICYjMTYwOyBBbiBuaW5oIMSQw6AgTuG6tW5nIDMwLTctMjAxNiA8L2E%2BPC9wPjwvZGl2PmRlZWQCAw9kFgICAQ8WAh8BaGQCBQ8PFgIfAWhkZAIGDw8WAh8BaGRkAhkPZBYIAgEPDxYCHwFoZGQCAw9kFgICAQ9kFgICAQ9kFgJmD2QWAgICDxYCHwFoZAIFDw8WAh8BaGRkAgYPDxYCHwFoZGQCGw9kFggCAQ8PFgIfAWhkZAIDD2QWAgIBD2QWAmYPZBYCZg9kFgICAQ9kFgJmD2QWAgIBDxYCHwUCAhYIZg9kFgQCAQ8WAh8ABZQBPGRpdiBjbGFzcz0iQ2F0ZWdvcnkiPjxwPjxhIGhyZWY9Imh0dHA6Ly93d3cuZHJ0LmRhbmFuZy52bi9UYWJJRC82NS9DSUQvMzMvZGVmYXVsdC5hc3B4Ij5DaMawxqFuZyB0csOsbmggdGjhu51pIHPhu7EgUGjDoXQgdGhhbmggdOG7kWk8L2E%2BPC9wPjwvZGl2PmQCAw8WAh8BaGQCAQ9kFgJmDxQrAgZlZQX%2BATxkaXYgY2xhc3M9IkRlc2NyaXB0aW9uIj48cD48YSBvbkNsaWNrPSJtZWRpYXBsYXkoJy9Qb3J0YWxzLzAvQXVkaW8vQU0vVFN0b2kvVFMgMTgtOC0yMDE2Lm1wMycsJycsMzIwLDI4NSkiIGhyZWY9IiN2Ij48aW1nIHdpZHRoPSIxNyIgaGVpZ2h0PSIxNyIgYm9yZGVyPSIwIiBhbGlnbj0ibGVmdCIgYWx0PSIiIHNyYz0iL1BvcnRhbHMvMC9JbWFnZXMvSWNvL2ljb1BoYXRUaGFuaC5naWYiIC8%2BIFThu5FpIDE4LTgtMjAxNjwvYT48L3A%2BPC9kaXY%2BZGVlZAICD2QWAmYPFCsCBmVlBf4BPGRpdiBjbGFzcz0iRGVzY3JpcHRpb24iPjxwPjxhIG9uQ2xpY2s9Im1lZGlhcGxheSgnL1BvcnRhbHMvMC9BdWRpby9BTS9UU3RvaS9UUyAxNy04LTIwMTYubXAzJywnJywzMjAsMjg1KSIgaHJlZj0iI3YiPjxpbWcgd2lkdGg9IjE3IiBoZWlnaHQ9IjE3IiBib3JkZXI9IjAiIGFsaWduPSJsZWZ0IiBhbHQ9IiIgc3JjPSIvUG9ydGFscy8wL0ltYWdlcy9JY28vaWNvUGhhdFRoYW5oLmdpZiIgLz4gVOG7kWkgMTctOC0yMDE2PC9hPjwvcD48L2Rpdj5kZWVkAgMPZBYCAgEPFgIfAWhkAgUPDxYCHwFoZGQCBg8PFgIfAWhkZAIdD2QWCAIBDw8WAh8BaGRkAgMPZBYCAgEPZBYCZg9kFgJmD2QWAgIBD2QWAmYPZBYCAgEPFgIfBQICFghmD2QWBAIBDxYCHwAFfTxkaXYgY2xhc3M9IkNhdGVnb3J5Ij48cD48YSBocmVmPSJodHRwOi8vd3d3LmRydC5kYW5hbmcudm4vVGFiSUQvNjUvQ0lELzQ1L2RlZmF1bHQuYXNweCI%2BxJDDoCBO4bq1bmcgdsOgIELhuqFuIDwvYT48L3A%2BPC9kaXY%2BZAIDDxYCHwFoZAIBD2QWAmYPFCsCBmVlBYkCPGRpdiBjbGFzcz0iRGVzY3JpcHRpb24iPjxwPjxhIG9uQ2xpY2s9Im1lZGlhcGxheSgnL1BvcnRhbHMvMC9BdWRpby9BTS9ETiB2YSBCYW4vRE5WQiAxNy04LTIwMTYubXAzJywnJywzMjAsMjg1KSIgaHJlZj0iI3YiPjxpbWcgYm9yZGVyPSIwIiBhbHQ9IiIgYWxpZ249ImxlZnQiIHdpZHRoPSIxNyIgaGVpZ2h0PSIxNyIgc3JjPSIvUG9ydGFscy8wL0ltYWdlcy9JY28vaWNvUGhhdFRoYW5oLmdpZiIgLz7EkE4gJmFtcDsgQiAxNy04LTIwMTY8L2E%2BPC9wPjwvZGl2PmRlZWQCAg9kFgJmDxQrAgZlZQWJAjxkaXYgY2xhc3M9IkRlc2NyaXB0aW9uIj48cD48YSBvbkNsaWNrPSJtZWRpYXBsYXkoJy9Qb3J0YWxzLzAvQXVkaW8vQU0vRE4gdmEgQmFuL0ROVkIgMTAtOC0yMDE2Lm1wMycsJycsMzIwLDI4NSkiIGhyZWY9IiN2Ij48aW1nIGJvcmRlcj0iMCIgYWx0PSIiIGFsaWduPSJsZWZ0IiB3aWR0aD0iMTciIGhlaWdodD0iMTciIHNyYz0iL1BvcnRhbHMvMC9JbWFnZXMvSWNvL2ljb1BoYXRUaGFuaC5naWYiIC8%2BxJBOICZhbXA7IEIgMTAtOC0yMDE2PC9hPjwvcD48L2Rpdj5kZWVkAgMPZBYCAgEPFgIfAWhkAgUPDxYCHwFoZGQCBg8PFgIfAWhkZAIfD2QWCAIBDw8WAh8BaGRkAgMPZBYCAgEPZBYCZg9kFgJmD2QWAgIBD2QWAmYPZBYCAgEPFgIfBQICFghmD2QWBAIBDxYCHwAFiwE8ZGl2IGNsYXNzPSJDYXRlZ29yeSI%2BPHA%2BPGEgaHJlZj0iaHR0cDovL3d3dy5kcnQuZGFuYW5nLnZuL1RhYklELzY1L0NJRC85Mi9kZWZhdWx0LmFzcHgiPlThu6sgc%2BG6o24geHXhuqV0IMSR4bq%2FbiB0acOqdSBkw7luZzwvYT48L3A%2BPC9kaXY%2BZAIDDxYCHwFoZAIBD2QWAmYPFCsCBmVlBf8BPGRpdiBjbGFzcz0iRGVzY3JpcHRpb24iPjxwPjxhIGhyZWY9IiN2IiBvbkNsaWNrPSJtZWRpYXBsYXkoJy9Qb3J0YWxzLzAvQXVkaW8vQU0vU1hURC9TWFREIDE1LTgtMjAxNi5tcDMnLCcnLDMyMCwyODUpIj48aW1nIHdpZHRoPSIxNyIgaGVpZ2h0PSIxNyIgYm9yZGVyPSIwIiBhbGlnbj0ibGVmdCIgc3JjPSIvUG9ydGFscy8wL0ltYWdlcy9JY28vaWNvUGhhdFRoYW5oLmdpZiIgYWx0PSIiIC8%2BIE5nw6B5IDE1LTgtMjAxNjwvYT48L3A%2BPC9kaXY%2BZGVlZAICD2QWAmYPFCsCBmVlBf8BPGRpdiBjbGFzcz0iRGVzY3JpcHRpb24iPjxwPjxhIGhyZWY9IiN2IiBvbkNsaWNrPSJtZWRpYXBsYXkoJy9Qb3J0YWxzLzAvQXVkaW8vQU0vU1hURC9TWFREIDA4LTgtMjAxNi5tcDMnLCcnLDMyMCwyODUpIj48aW1nIHdpZHRoPSIxNyIgaGVpZ2h0PSIxNyIgYm9yZGVyPSIwIiBhbGlnbj0ibGVmdCIgc3JjPSIvUG9ydGFscy8wL0ltYWdlcy9JY28vaWNvUGhhdFRoYW5oLmdpZiIgYWx0PSIiIC8%2BIE5nw6B5IDA4LTgtMjAxNjwvYT48L3A%2BPC9kaXY%2BZGVlZAIDD2QWAgIBDxYCHwFoZAIFDw8WAh8BaGRkAgYPDxYCHwFoZGQCIQ9kFggCAQ8PFgIfAWhkZAIDD2QWAgIBD2QWAmYPZBYCZg9kFgICAQ9kFgJmD2QWAgIBDxYCHwUCAhYIZg9kFgQCAQ8WAh8ABX88ZGl2IGNsYXNzPSJDYXRlZ29yeSI%2BPHA%2BPGEgaHJlZj0iaHR0cDovL3d3dy5kcnQuZGFuYW5nLnZuL1RhYklELzY1L0NJRC85Ny9kZWZhdWx0LmFzcHgiPkN14buZYyBz4buRbmcgbXXDtG4gbcOgdTwvYT48L3A%2BPC9kaXY%2BZAIDDxYCHwFoZAIBD2QWAmYPFCsCBmVlBYoCPGRpdiBjbGFzcz0iRGVzY3JpcHRpb24iPjxwPjxhIG9uQ2xpY2s9Im1lZGlhcGxheSgnL1BvcnRhbHMvMC9BdWRpby9BTS9DdW9jU29uZ011b25NYXUvQ1NNTSAxMy04LTIwMTYubXAzJywnJywzMjAsMjg1KSIgaHJlZj0iI3YiPjxpbWcgd2lkdGg9IjE3IiBoZWlnaHQ9IjE3IiBhbGlnbj0ibGVmdCIgYm9yZGVyPSIwIiBhbHQ9IiIgc3JjPSIvUG9ydGFscy8wL0ltYWdlcy9JY28vaWNvUGhhdFRoYW5oLmdpZiIgLz4gTmfDoHkgMTMtOC0yMDE2PC9hPjwvcD48L2Rpdj5kZWVkAgIPZBYCZg8UKwIGZWUFigI8ZGl2IGNsYXNzPSJEZXNjcmlwdGlvbiI%2BPHA%2BPGEgb25DbGljaz0ibWVkaWFwbGF5KCcvUG9ydGFscy8wL0F1ZGlvL0FNL0N1b2NTb25nTXVvbk1hdS9DU01NIDEyLTgtMjAxNi5tcDMnLCcnLDMyMCwyODUpIiBocmVmPSIjdiI%2BPGltZyB3aWR0aD0iMTciIGhlaWdodD0iMTciIGFsaWduPSJsZWZ0IiBib3JkZXI9IjAiIGFsdD0iIiBzcmM9Ii9Qb3J0YWxzLzAvSW1hZ2VzL0ljby9pY29QaGF0VGhhbmguZ2lmIiAvPiBOZ8OgeSAxMi04LTIwMTY8L2E%2BPC9wPjwvZGl2PmRlZWQCAw9kFgICAQ8WAh8BaGQCBQ8PFgIfAWhkZAIGDw8WAh8BaGRkAiMPZBYIAgEPDxYCHwFoZGQCAw9kFgICAQ9kFgJmD2QWAmYPZBYCAgEPZBYCZg9kFgICAQ8WAh8FAgIWCGYPZBYEAgEPFgIfAAV9PGRpdiBjbGFzcz0iQ2F0ZWdvcnkiPjxwPjxhIGhyZWY9Imh0dHA6Ly93d3cuZHJ0LmRhbmFuZy52bi9UYWJJRC82NS9DSUQvOTgvZGVmYXVsdC5hc3B4Ij5RdcOgIHThurduZyDDom0gbmjhuqFjPC9hPjwvcD48L2Rpdj5kAgMPFgIfAWhkAgEPZBYCZg8UKwIGZWUFiAI8ZGl2IGNsYXNzPSJEZXNjcmlwdGlvbiI%2BPHA%2BPGEgaHJlZj0iI3YiIG9uQ2xpY2s9Im1lZGlhcGxheSgnL1BvcnRhbHMvMC9BdWRpby9BTS9RdWFUYW5nQW1OaGFjL1FUQU4gMTQtOC0yMDE2Lm1wMycsJycsMzIwLDI4NSkiPjxpbWcgd2lkdGg9IjE3IiBoZWlnaHQ9IjE3IiBib3JkZXI9IjAiIGFsaWduPSJsZWZ0IiBzcmM9Ii9Qb3J0YWxzLzAvSW1hZ2VzL0ljby9pY29QaGF0VGhhbmguZ2lmIiBhbHQ9IiIgLz4gTmfDoHkgMTQtOC0yMDE2PC9hPjwvcD48L2Rpdj5kZWVkAgIPZBYCZg8UKwIGZWUFiAI8ZGl2IGNsYXNzPSJEZXNjcmlwdGlvbiI%2BPHA%2BPGEgaHJlZj0iI3YiIG9uQ2xpY2s9Im1lZGlhcGxheSgnL1BvcnRhbHMvMC9BdWRpby9BTS9RdWFUYW5nQW1OaGFjL1FUQU4gMDctOC0yMDE2Lm1wMycsJycsMzIwLDI4NSkiPjxpbWcgd2lkdGg9IjE3IiBoZWlnaHQ9IjE3IiBib3JkZXI9IjAiIGFsaWduPSJsZWZ0IiBzcmM9Ii9Qb3J0YWxzLzAvSW1hZ2VzL0ljby9pY29QaGF0VGhhbmguZ2lmIiBhbHQ9IiIgLz4gTmfDoHkgMDctOC0yMDE2PC9hPjwvcD48L2Rpdj5kZWVkAgMPZBYCAgEPFgIfAWhkAgUPDxYCHwFoZGQCBg8PFgIfAWhkZAIlD2QWCAIBDw8WAh8BaGRkAgMPZBYCAgEPZBYCZg9kFgJmD2QWAgIBD2QWAmYPZBYCAgEPFgIfBQICFghmD2QWBAIBDxYCHwAFkAE8ZGl2IGNsYXNzPSJDYXRlZ29yeSI%2BPHA%2BPGEgaHJlZj0iaHR0cDovL3d3dy5kcnQuZGFuYW5nLnZuL1RhYklELzY1L0NJRC80OC9kZWZhdWx0LmFzcHgiPkPDonUgbOG6oWMgYuG7mSBWxINuIGjhu41jIG5naOG7hyB0aHXhuq10PC9hPjwvcD48L2Rpdj5kAgMPFgIfAWhkAgEPZBYCZg8UKwIGZWUFhQI8ZGl2IGNsYXNzPSJEZXNjcmlwdGlvbiI%2BPHA%2BPGEgb25DbGljaz0ibWVkaWFwbGF5KCcvUG9ydGFscy8wL0F1ZGlvL0FNL0NMQlZIL0NMQlZITlQgMDktOC0yMDE2Lm1wMycsJycsMzIwLDI4NSkiIGhyZWY9IiN2Ij48aW1nIGJvcmRlcj0iMCIgYWx0PSIiIGFsaWduPSJsZWZ0IiB3aWR0aD0iMTciIGhlaWdodD0iMTciIHNyYz0iL1BvcnRhbHMvMC9JbWFnZXMvSWNvL2ljb1BoYXRUaGFuaC5naWYiIC8%2BIENMQlZITlQgMDktOC0yMDE2PC9hPjwvcD48L2Rpdj5kZWVkAgIPZBYCZg8UKwIGZWUFhQI8ZGl2IGNsYXNzPSJEZXNjcmlwdGlvbiI%2BPHA%2BPGEgb25DbGljaz0ibWVkaWFwbGF5KCcvUG9ydGFscy8wL0F1ZGlvL0FNL0NMQlZIL0NMQlZITlQgMDItOC0yMDE2Lm1wMycsJycsMzIwLDI4NSkiIGhyZWY9IiN2Ij48aW1nIGJvcmRlcj0iMCIgYWx0PSIiIGFsaWduPSJsZWZ0IiB3aWR0aD0iMTciIGhlaWdodD0iMTciIHNyYz0iL1BvcnRhbHMvMC9JbWFnZXMvSWNvL2ljb1BoYXRUaGFuaC5naWYiIC8%2BIENMQlZITlQgMDItOC0yMDE2PC9hPjwvcD48L2Rpdj5kZWVkAgMPZBYCAgEPFgIfAWhkAgUPDxYCHwFoZGQCBg8PFgIfAWhkZAInD2QWCAIBDw8WAh8BaGRkAgMPZBYCAgEPZBYCZg9kFgJmD2QWAgIBD2QWAmYPZBYCAgEPFgIfBQICFghmD2QWBAIBDxYCHwAFfTxkaXYgY2xhc3M9IkNhdGVnb3J5Ij48cD48YSBocmVmPSJodHRwOi8vd3d3LmRydC5kYW5hbmcudm4vVGFiSUQvNjUvQ0lELzEwMS9kZWZhdWx0LmFzcHgiPljDumMgY%2BG6o20gw6JtIG5o4bqhYzwvYT48L3A%2BPC9kaXY%2BZAIDDxYCHwFoZAIBD2QWAmYPFCsCBmVlBf8BPGRpdiBjbGFzcz0iRGVzY3JpcHRpb24iPjxwPjxhIG9uQ2xpY2s9Im1lZGlhcGxheSgnL1BvcnRhbHMvMC9BdWRpby9BTS9YQ0FOL1hDQU4gMTEtOC0yMDE2Lm1wMycsJycsMzIwLDI4NSkiIGhyZWY9IiN2Ij48aW1nIGJvcmRlcj0iMCIgYWx0PSIiIGFsaWduPSJsZWZ0IiB3aWR0aD0iMTciIGhlaWdodD0iMTciIHNyYz0iL1BvcnRhbHMvMC9JbWFnZXMvSWNvL2ljb1BoYXRUaGFuaC5naWYiIC8%2BIE5nw6B5IDExLTgtMjAxNjwvYT48L3A%2BPC9kaXY%2BZGVlZAICD2QWAmYPFCsCBmVlBf8BPGRpdiBjbGFzcz0iRGVzY3JpcHRpb24iPjxwPjxhIG9uQ2xpY2s9Im1lZGlhcGxheSgnL1BvcnRhbHMvMC9BdWRpby9BTS9YQ0FOL1hDQU4gMDQtOC0yMDE2Lm1wMycsJycsMzIwLDI4NSkiIGhyZWY9IiN2Ij48aW1nIGJvcmRlcj0iMCIgYWx0PSIiIGFsaWduPSJsZWZ0IiB3aWR0aD0iMTciIGhlaWdodD0iMTciIHNyYz0iL1BvcnRhbHMvMC9JbWFnZXMvSWNvL2ljb1BoYXRUaGFuaC5naWYiIC8%2BIE5nw6B5IDA0LTgtMjAxNjwvYT48L3A%2BPC9kaXY%2BZGVlZAIDD2QWAgIBDxYCHwFoZAIFDw8WAh8BaGRkAgYPDxYCHwFoZGQCKQ9kFggCAQ8PFgIfAWhkZAIDD2QWAgIBD2QWAmYPZBYCZg9kFgICAQ9kFgJmD2QWAgIBDxYCHwUCAhYIZg9kFgQCAQ8WAh8ABYMBPGRpdiBjbGFzcz0iQ2F0ZWdvcnkiPjxwPjxhIGhyZWY9Imh0dHA6Ly93d3cuZHJ0LmRhbmFuZy52bi9UYWJJRC82NS9DSUQvMTAyL2RlZmF1bHQuYXNweCI%2BQ8OidSBjaHV54buHbiBjdeG7kWkgdHXhuqduPC9hPjwvcD48L2Rpdj5kAgMPFgIfAWhkAgEPZBYCZg8UKwIGZWUFjwI8ZGl2IGNsYXNzPSJEZXNjcmlwdGlvbiI%2BPHA%2BPGEgaHJlZj0iI3YiIG9uQ2xpY2s9Im1lZGlhcGxheSgnL1BvcnRhbHMvMC9BdWRpby9BTS9DYXUgQ2h1eWVuIEN1b2kgVHVhbi9DQ0NUIDE0LTgtMjAxNi5tcDMnLCcnLDMyMCwyODUpIj48aW1nIHdpZHRoPSIxNyIgaGVpZ2h0PSIxNyIgYm9yZGVyPSIwIiBhbGlnbj0ibGVmdCIgc3JjPSIvUG9ydGFscy8wL0ltYWdlcy9JY28vaWNvUGhhdFRoYW5oLmdpZiIgYWx0PSIiIC8%2BIE5nw6B5IDE0LTgtMjAxNjwvYT48L3A%2BPC9kaXY%2BZGVlZAICD2QWAmYPFCsCBmVlBY8CPGRpdiBjbGFzcz0iRGVzY3JpcHRpb24iPjxwPjxhIGhyZWY9IiN2IiBvbkNsaWNrPSJtZWRpYXBsYXkoJy9Qb3J0YWxzLzAvQXVkaW8vQU0vQ2F1IENodXllbiBDdW9pIFR1YW4vQ0NDVCAwNy04LTIwMTYubXAzJywnJywzMjAsMjg1KSI%2BPGltZyB3aWR0aD0iMTciIGhlaWdodD0iMTciIGJvcmRlcj0iMCIgYWxpZ249ImxlZnQiIHNyYz0iL1BvcnRhbHMvMC9JbWFnZXMvSWNvL2ljb1BoYXRUaGFuaC5naWYiIGFsdD0iIiAvPiBOZ8OgeSAwNy04LTIwMTY8L2E%2BPC9wPjwvZGl2PmRlZWQCAw9kFgICAQ8WAh8BaGQCBQ8PFgIfAWhkZAIGDw8WAh8BaGRkAg8PZBYMAgEPZBYIAgEPDxYCHwFoZGQCAw9kFgICAQ9kFgICAQ9kFgJmD2QWAgICDxYCHwFoZAIFDw8WAh8BaGRkAgYPDxYCHwFoZGQCAw9kFggCAQ8PFgIfAWhkZAIDD2QWAgIBD2QWAmYPZBYCZg9kFgICAQ9kFggCAQ8PFgIfAAUlPGI%2BRFJUMSA8L2I%2BIE5nw6B5OiA8Yj4xOS8wOC8yMDE2PC9iPmRkAgUPDxYCHwAFrQ88dGFibGUgd2lkdGg9JzEwMCUnPjx0cj48dGQgIHZhbGlnbj0ndG9wJz48cD4wNmgwMDogQ2jDoG8gbmfDoHkgbeG7m2k8L3A%2BCjxwPjA2aDMwOiBDaHV5w6puIG3hu6VjOiA8YnIgLz4KU%2BG7qWMga2hv4bq7IGNobyBt4buNaSBuaMOgPC9wPgo8cD4wNmg0NTogU%2BG7qWMga2jhu49lIDM2NSBz4buRIDE5OTwvcD4KPHA%2BMDdoMDA6IFBoaW0gdHJ1eeG7h24gQcSQIDxiciAvPgpN4buRaSB0aMO5IGtow7RuZyBwaGFpIFThuq1wIDM0PC9wPgo8cD4wN2g1MDogQuG6o24gdGluIFRp4bq%2FbmcgQW5oPC9wPgo8cD4wOGgwMDogQ2jDoG8gbmfDoHkgbeG7m2k8L3A%2BCjxwPjA4aDMwOiBDYSBuaOG6oWMgdGhp4bq%2FdSBuaGkgPGJyIC8%2BCkPDoW5oIMSR4buTbmcgbmfhu410IG5nw6BvPC9wPgo8cD4wOWgwMDogUGhpbSB0cnV54buHbiBWxakga2jDrSBz4bqvYyDEkeG6uXAgVOG6rXAgNDE8L3A%2BCjxwPjEwaDAwOiBQaGltIHRydXnhu4duIFZOIDxiciAvPgpTYW8gxJHhu5VpIG5nw7RpIFThuq1wIDU8L3A%2BCjxwPjExaDA1OiBUacOqdSDEkWnhu4NtIDI0SDwvcD4KPHA%2BMTFoMjU6IELhuqNuIHRpbiBUw6BpIGNow61uaCB0aOG7iyB0csaw4budbmc8L3A%2BCjxwPjExaDMwOiBUaOG7nWkgc%2BG7sSBQVC1USCDEkE48L3A%2BCjxwPjExaDU1OiBUaMO0bmcgdGluLSBUaMO0bmcgYsOhby0gUXXhuqNuZyBjw6FvPC9wPgo8cD4xMmgwMDogUGhpbSB0cnV54buHbiBBxJAgOiA8YnIgLz4KTeG7kWkgdGjDuSBraMO0bmcgcGhhaSBU4bqtcCAzNTwvcD4KPHA%2BMTNoMDA6IFBoaW0gdHJ1eeG7h24gSFE6IDxiciAvPgpWxakga2jDrSBz4bqvYyDEkeG6uXAgVOG6rXAgNDI8L3A%2BCjxwPjE0aDAwOiBLcG9wIGNo4buNbiBs4buNYzwvcD4KPHA%2BMTRoMzA6IFPhu6ljIGto4buPZSAzNjUgU%2BG7kSAxOTg8L3A%2BCjxwPjE1aDAwOiBQaGltIHTDoGkgbGnhu4d1OjxiciAvPgrEkGnhu4dwIGLDoW8gdmnDqm4gQTEzLSBIb8OgbmcgxJDhuqFvPC9wPgo8cD4xNWgzMDogQ2EgbmjhuqFjOiA8YnIgLz4KVMOsbmgga2jDumMgY2hvIGVtPC9wPgo8cD4xNmgwMDogUGhpbSB0cnV54buHbiBISzogPGJyIC8%2BClPhu6kgbeG7h25oIDM2IGdp4budIC0gUDIgVOG6rXAgNzwvcD4KPHA%2BMTZoNDU6IFBoaW0gdHJ1eeG7h24gSFE6IDxiciAvPgpLaG%2FhuqNuaCBraOG6r2Mgc2luaCB04butIFThuq1wIDIyPC9wPgo8cD4xN2g0MDogSOG7mXAgdGjGsCB0cnV54buBbiBow6xuaDwvcD4KPHA%2BMThoMDU6IFRpw6p1IMSRaeG7g20gMjRIPC9wPgo8cD4xOGgyNTogQuG6o24gdGluIFTDoGkgY2jDrW5oIHRo4buLIHRyxrDhu51uZzwvcD4KPHA%2BMThoMzA6IFRo4budaSBz4buxIFBULVRIIMSQTjwvcD4KPHA%2BMThoNTU6IFRow7RuZyB0aW4tIFRow7RuZyBiw6FvLSBRdeG6o25nIGPDoW88L3A%2BCjxwPjE5aDAwOiBUaeG6v3Agc8OzbmcgVGjhu51pIHPhu7EgxJDDoGkgVEhWTjwvcD4KPHA%2BMTloNTA6IFRow7RuZyB0aW4tIFRow7RuZyBiw6FvLSBRdeG6o25nIGPDoW88L3A%2BCjxwPjIwaDAwOiBQaGltIHRydXnhu4duIEhROiA8YnIgLz4KVsWpIGtow60gc%2BG6r2MgxJHhurlwIFThuq1wIDQzPC9wPgo8cD4yMGg1MDogTmjhu4twIHPhu5FuZyB0aMOgbmggcGjhu5E8L3A%2BCjxwPjIxaDAwOiBQaGltIHRydXnhu4duIEhLOiA8YnIgLz4KU%2BG7qSBt4buHbmggMzYgZ2nhu50gLSBQMiBU4bqtcCA4PC9wPgo8cD4yMWg1MDogUGjDs25nIHPhu7EgPGJyIC8%2BCk5o4buvbmcgY2jhurduZyDEkcaw4budbmcga2jDtG5nIHRo4buDIG7DoG8gcXXDqm48L3A%2BCjxwPjIyaDA1OiBWaXRhbWluIGPGsOG7nWkgU%2BG7kSAxNDwvcD4KPHA%2BMjJoMzU6IEvDvSBz4buxOjxiciAvPgpE4buNYyDEkcaw4budbmcgxJHhuqV0IG7GsOG7m2MgVOG6rXAgMTksMjA8L3A%2BCjxwPjIzaDAwOiBC4bqjbiB0aW4gVGnhur9uZyBBbmg8L3A%2BCjxwPjIzaDEwOiBC4bqjbiB0aW4gUXXhu5FjIHThur88L3A%2BCjxwPiYjMTYwOzwvcD48L3RkPjwvdHI%2BPC90YWJsZT5kZAIJDxAPFgYeDURhdGFUZXh0RmllbGQFCEtlbmhfVGVuHg5EYXRhVmFsdWVGaWVsZAUHS2VuaF9JRB4LXyFEYXRhQm91bmRnZBAVBAREUlQxBERSVDICRk0CQU0VBAExATIBNAEzFCsDBGdnZ2cWAWZkAgsPDxYCHwAFCjE5LzA4LzIwMTZkZAIFDw8WAh8BaGRkAgYPDxYCHwFoZGQCBQ9kFggCAQ8PFgIfAWhkZAIDD2QWAgIBD2QWAgIBD2QWAmYPZBYCAgIPFgIfAWhkAgUPDxYCHwFoZGQCBg8PFgIfAWhkZAIHD2QWCAIBDw8WAh8BaGRkAgMPZBYCAgEPZBYCAgEPZBYCZg9kFgICAg8WAh8BaGQCBQ8PFgIfAWhkZAIGDw8WAh8BaGRkAgkPZBYIAgEPDxYCHwFoZGQCAw9kFgICAQ9kFgICAQ9kFgJmD2QWAgICDxYCHwFoZAIFDw8WAh8BaGRkAgYPDxYCHwFoZGQCCw9kFggCAQ8PFgIfAWhkZAIDD2QWAgIBD2QWAgIBD2QWAmYPZBYCAgIPFgIfAWhkAgUPDxYCHwFoZGQCBg8PFgIfAWhkZAIRD2QWAgIBD2QWCAIBDw8WAh8BaGRkAgMPZBYCAgEPZBYCAgEPZBYCZg9kFgICAg8WAh8BaGQCBQ8PFgIfAWhkZAIGDw8WAh8BaGRkGAEFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYBBRlkbm4kY3RyNDQ0JFZpZXdMUFMkYnRkYXRlN2y3OvVj%2BAem2Wm%2BfeNIEYHhMSo%3D&__AjaxControlToolkitCalendarCssLoaded=&__ASYNCPOST=false&";//19/08/2016
            string url = String.Format(channelToServer.Server);

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (var http = new HttpClient())
                {
                    http.DefaultRequestHeaders.Add("X-Requested-With", "XMLHttpRequest");

                    var stringContent = string.Format(postdata, channelToServer.Value, date.Day, date.Month, date.Year);
                    var httpContent = new StringContent(stringContent, Encoding.UTF8, "application/x-www-form-urlencoded");
                    var response = http.PostAsync(url, httpContent).Result;

                    if (response.IsSuccessStatusCode)
                    {
                        var bytes = response.Content.ReadAsByteArrayAsync().Result;
                        String source = Encoding.GetEncoding("utf-8").GetString(bytes, 0, bytes.Length - 1);
                        source = System.Text.RegularExpressions.Regex.Unescape(source);
                        source = WebUtility.HtmlDecode(source.Replace("\"", ""));
                        HtmlDocument resultat = new HtmlDocument();
                        resultat.LoadHtml(source);

                        var schedules = resultat.DocumentNode.SelectNodes("//span[@id='dnn_ctr444_ViewLPS_Label2']");

                        if (schedules != null && schedules.Count > 0)
                        {
                            var items = schedules.FirstOrDefault().SelectNodes("table//tr//td//p");
                            if (items != null)
                            {
                                foreach (var item in items)
                                {
                                    string startOn = "", programName = "";
                                    // get start time
                                    var strdata = item.InnerText.Trim();
                                    var middle = strdata.IndexOf(":");
                                    if (middle > 4)
                                    {
                                        startOn = strdata.Substring(0, middle).Replace("h", ":").Trim();
                                        programName = strdata.Substring(middle).Replace(":", "").Replace("\n", ": ").Trim();
                                        var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                        guideItems.Add(guideItem);
                                    }
                                }
                            }
                        }
                    }
                }

            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        static private List<GuideItem> GetDataFromMOBITVUrl(string url, ChannelToServer channelToServer, DateTime date)
        {
            string postdata = "RadScriptManager1_TSM=%3B%3BSystem.Web.Extensions%2C+Version%3D4.0.0.0%2C+Culture%3Dneutral%2C+PublicKeyToken%3D31bf3856ad364e35%3Aen-US%3A8f393b2b-3315-402f-b504-cd6d2db001f6%3Aea597d4b%3Ab25378d2&__EVENTTARGET=&__EVENTARGUMENT=&__VIEWSTATE=%2FwEPDwUKMTI0Mjk0NjMyM2QYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgEFEWN0bDA2JGN0bDAxJElzSG90&km=T%E1%BB%AB+kh%C3%B3a+t%C3%ACm+ki%E1%BA%BFm...&ctl06_ctl01_ChooseDate={1}&ctl06%24ctl01%24Channel={0}&ctl06%24ctl01%24Programmer=Ch%E1%BB%8Dn+ch%C6%B0%C6%A1ng+tr%C3%ACnh&ctl06%24ctl01%24HourPeriod=Th%E1%BB%9Di+gian&ctl06%24ctl01%24btnBSSubmit=T%C3%ACm+ki%E1%BA%BFm";//22-08-2016

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (var http = new HttpClient())
                {
                    http.DefaultRequestHeaders.Add("X-Requested-With", "XMLHttpRequest");

                    var stringContent = string.Format(postdata, channelToServer.Value, date.ToString("dd-MM-yyyy"));
                    var httpContent = new StringContent(stringContent, Encoding.UTF8, "application/x-www-form-urlencoded");
                    var response = http.PostAsync(url, httpContent).Result;

                    if (response.IsSuccessStatusCode)
                    {
                        var bytes = response.Content.ReadAsByteArrayAsync().Result;
                        String source = Encoding.GetEncoding("utf-8").GetString(bytes, 0, bytes.Length - 1);
                        source = System.Text.RegularExpressions.Regex.Unescape(source);
                        source = WebUtility.HtmlDecode(source.Replace("\"", ""));
                        HtmlDocument resultat = new HtmlDocument();
                        resultat.LoadHtml(source);

                        var schedules = resultat.DocumentNode.SelectNodes("//table[@class='list-table']");

                        if (schedules != null && schedules.Count > 0)
                        {
                            var items = schedules.FirstOrDefault().SelectNodes("tr");
                            foreach (var item in items)
                            {
                                string startOn = "", programName = "";
                                // get start time
                                var tdTags = item.SelectNodes("td");
                                if (tdTags != null && tdTags.Count > 3)
                                {
                                    startOn = tdTags.ElementAt(0).InnerText.Trim();
                                    programName = tdTags.ElementAt(2).InnerText.Trim();
                                    var more = tdTags.ElementAt(3).InnerText.Trim();
                                    if (!string.IsNullOrWhiteSpace(more))
                                    {
                                        programName += ": " + more;
                                    }
                                }
                                if (!string.IsNullOrWhiteSpace(startOn))
                                {
                                    var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                    guideItems.Add(guideItem);
                                }
                            }
                        }
                    }
                }

            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        static public List<GuideItem> GetDataFromMOBITVUrl(ChannelToServer channelToServer, DateTime date)
        {

            List<GuideItem> guideItems = new List<GuideItem>();

            guideItems.AddRange(GetDataFromMOBITVUrl(channelToServer.Server, channelToServer, date));
            guideItems.AddRange(GetDataFromMOBITVUrl(channelToServer.Server + "page-2/", channelToServer, date));

            return guideItems;
        }

        static public List<GuideItem> GetDataFromTODAYTVUrl(ChannelToServer channelToServer, DateTime date)
        {
            long orignalTime = 1504717200L;
            DateTime origanalDate = new DateTime(2017, 9, 7);
            long time = orignalTime + 86400 * (date - origanalDate).Days;
            List<GuideItem> guideItems = new List<GuideItem>();

            string url = string.Format(channelToServer.Server + "/{0}/{1}", time,date.ToString("dd-MM-yyyy")) ;
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    var response = http.GetByteArrayAsync(url).Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                    //     source = System.Text.RegularExpressions.Regex.Unescape(source);
                    source = WebUtility.HtmlDecode(source);
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(source);

                    var items = resultat.DocumentNode.SelectNodes("//div[@id='content']/div/div/div/div/div");
                    if (items != null && items.Count > 0)
                    {
                        for (int i = 0; i < items.Count; i++)
                        {
                            string startOn = "", programName = "";
                            var divs = items[i].SelectNodes("div");
                            if (divs != null && divs.Count == 5)
                            {
                                startOn = divs[1].InnerText.Trim();
                                var first = divs[2].InnerText.Trim();
                                var last = divs[3].InnerText.Trim();
                                if (string.IsNullOrWhiteSpace(first))
                                {
                                    programName = last;
                                }
                                else if (string.IsNullOrWhiteSpace(last))
                                {
                                    programName = first;
                                }
                                else
                                {
                                    programName = first + " : " + last;
                                }
                                if (!string.IsNullOrWhiteSpace(startOn) && startOn.Length < 6)
                                {
                                    var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                    guideItems.Add(guideItem);
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }


        #region search from vietbao
        static public List<SearchItem> SearchDataFromVietBaoUrl(string query, int stationID, DateTime date)
        {
            string url = string.Format(VIETBAO_SEARCH_PAGE, HttpUtility.UrlEncode(MethodHelpers.RemoveSign4VietnameseString(query)), date.ToString("dd-MM-yyyy"), stationID);

            List<SearchItem> searchItems = new List<SearchItem>();
            try
            {
                HttpClient http = new HttpClient();
                var response = http.GetByteArrayAsync(url).Result;
                String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                source = WebUtility.HtmlDecode(source);
                HtmlDocument resultat = new HtmlDocument();
                resultat.LoadHtml(source);

                var chanelDetails = resultat.DocumentNode.SelectNodes("//div[@class='tv-danh-sach-chuong-trinh']");
                if (chanelDetails != null && chanelDetails.Count > 0)
                {
                    var chanelDetail = chanelDetails.LastOrDefault();
                    if (chanelDetail != null)
                    {
                        var items = chanelDetail.SelectNodes("div//div[@class='row']");
                        if (items != null)
                        {
                            foreach (var item in items)
                            {
                                string imgUrl = "", programName = "", time = "", channelName =" ";

                                var rowCols= item.SelectNodes("div[contains(@class,'row-col')]");
                                if(rowCols.Count > 0)
                                {
                                    var col = rowCols.ElementAt(0);
                                    var imgTabs = col.SelectNodes("div//img");
                                    if (imgTabs != null)
                                    {
                                        var imgTab = imgTabs.FirstOrDefault();
                                        if (imgTab != null)
                                        {
                                            if (imgTab.Attributes["src"]!=null) imgUrl = VIETBAO_HOME_PAGE + imgTab.Attributes["src"].Value;
                                            if (imgTab.Attributes["alt"] != null) channelName = imgTab.Attributes["alt"].Value;
                                        }
                                    }
                                }
                                if (rowCols.Count > 1)
                                {
                                    var col = rowCols.ElementAt(1);
                                    var aTabs = col.SelectNodes("div//a");
                                    if (aTabs != null)
                                    {
                                        var aTab = aTabs.FirstOrDefault();
                                        if (aTab != null)
                                        {
                                            programName = aTab.InnerText.Trim();
                                        }
                                    }
                                }
                                if (rowCols.Count > 2)
                                {
                                    var col = rowCols.ElementAt(2);
                                    var aTabs = col.SelectNodes("div//a");
                                    if (aTabs != null)
                                    {
                                        var aTab = aTabs.FirstOrDefault();
                                        if (aTab != null)
                                        {
                                            time = aTab.InnerText.Trim();
                                        }
                                    }
                                }
                                var guideItem = new SearchItem() {ChannelName = channelName, ImageUrl = imgUrl, ProgramName =programName, Time = time};
                                searchItems.Add(guideItem);
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return searchItems;
        }

        static public List<GuideItem> GetDataFromQUANGBINHUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server, date.ToString("dd/MM/yyyy"));

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    var response = http.GetByteArrayAsync(url).Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                    source = System.Text.RegularExpressions.Regex.Unescape(source);
                    source = WebUtility.HtmlDecode(source);
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(source);

                    var schedulerMain = resultat.DocumentNode.SelectNodes("//div/table");
                    if (schedulerMain != null && schedulerMain.Count > 0)
                    {
                        var items = schedulerMain.FirstOrDefault().SelectNodes("tr");
                        if (items != null)
                        {
                            foreach (var item in items)
                            {
                                string startOn = "", programName = "";
                                // get start time
                                var tdTags = item.SelectNodes("td");
                                if (tdTags != null && tdTags.Count > 1)
                                {
                                    startOn = tdTags.ElementAt(0).InnerText.Replace("h",":").Trim();
                                    programName = tdTags.ElementAt(1).InnerText.Trim();
                                }
                                if (!string.IsNullOrWhiteSpace(startOn))
                                {
                                    var guideItem = new GuideItem() { ChannelKey = channelToServer.ChannelKey, DateOn = MethodHelpers.ConvertDateToCorrectString(date), StartOn = startOn, ProgramName = programName, Note = "" };
                                    guideItems.Add(guideItem);
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }
        #endregion

        static public List<Channel> GetAllChannels()
        {
            List<Channel> channels = new List<Channel>();
            try
            {
                HttpClient http = new HttpClient();
                var response = http.GetByteArrayAsync(HttpContext.Current.Request.Url.ToString() + "/Data/Channels/Channels.xml").Result;
                String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                source = WebUtility.HtmlDecode(source);
                HtmlDocument resultat = new HtmlDocument();
                resultat.LoadHtml(source);

                var optgroups = resultat.DocumentNode.SelectNodes("//optgroup");
                if (optgroups != null && optgroups.Count > 0)
                {
                    foreach (var optgroup in optgroups)
                    {
                        string groupName = optgroup.Attributes["label"] == null ? "" : optgroup.Attributes["label"].Value;
                        if (optgroup != null)
                        {
                            var options = optgroup.SelectNodes("option");
                            if (options != null)
                            {
                                foreach (var item in options)
                                {
                                    string vietbaoLink = "", channelName = "";
                                    var value = item.Attributes["value"];
                                    if (value != null)
                                    {
                                        vietbaoLink = value.Value;
                                    }
                                    var name = item.Attributes["name"];
                                    if (name != null)
                                    {
                                        channelName = name.Value;
                                    }

                                    var channel = new Channel() { ChannelName = channelName, LinkVietBao = vietbaoLink, ChannelGroupName = groupName };
                                    channels.Add(channel);
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return channels;
        }
    }
}
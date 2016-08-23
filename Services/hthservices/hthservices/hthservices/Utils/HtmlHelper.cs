using HtmlAgilityPack;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
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
                using (var http = new HttpClient())
                {
                    http.DefaultRequestHeaders.Add("X-Requested-With", "XMLHttpRequest");
                    
                    var stringContent = "channelId="+channelToServer.Value+"&dateSchedule="+date.ToString("dd/MM/yyyy");
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
        static public List<GuideItem> GetDataFromHTVONLINEUrl(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server);

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                using (HttpClient http = new HttpClient())
                {
                    http.DefaultRequestHeaders.Add("X-Requested-With", "XMLHttpRequest");
                    var obj = new
                    {
                        //must
                        id_live = channelToServer.Value,
                        date = date.ToString("yyyy-MM-dd")
                    };

                    var stringContent = JsonConvert.SerializeObject(obj).ToString();
                    var httpContent = new StringContent(stringContent, Encoding.UTF8, "application/json");
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

                        var items = resultat.DocumentNode.SelectNodes("//p");
                        if (items != null)
                        {
                            foreach (var item in items)
                            {
                                string startOn = "", programName = "";
                                // get start time
                                var times = item.SelectNodes("b");
                                if (times != null && times.Count > 0)
                                {
                                    startOn = times.ElementAt(0).InnerText.Trim();                                    
                                }
                                var details = item.SelectNodes("span");
                                if (details != null && details.Count > 0)
                                {
                                    programName = details.ElementAt(0).InnerText.Trim();
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
                    var response = http.GetByteArrayAsync(url).Result;
                    String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                    //     source = System.Text.RegularExpressions.Regex.Unescape(source);
                    source = WebUtility.HtmlDecode(source);
                    HtmlDocument resultat = new HtmlDocument();
                    resultat.LoadHtml(source);

                    var schedulerMain = resultat.DocumentNode.SelectNodes("//div[@id='schedule-table']");
                    if (schedulerMain != null && schedulerMain.Count > 0)
                    {
                        var timeItems = schedulerMain.FirstOrDefault().SelectNodes("div[@class='col1']");
                        var detailItems = schedulerMain.FirstOrDefault().SelectNodes("div[@class='col2']");
                        var moreDetailItems = schedulerMain.FirstOrDefault().SelectNodes("div[@class='col3']");
                        if (timeItems != null && detailItems != null && moreDetailItems != null && timeItems.Count == detailItems.Count && moreDetailItems.Count == detailItems.Count)
                        {
                            for (int i = 0; i < timeItems.Count; i++)
                            {
                                string startOn = "", programName = "", more="";
                                // get start time
                                startOn = timeItems.ElementAt(i).InnerText.Trim();
                                programName = detailItems.ElementAt(i).InnerText.Trim();
                                more = moreDetailItems.ElementAt(i).InnerText.Trim();
                                if (!String.IsNullOrWhiteSpace(more))
                                {
                                    programName +=" - " + more;
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

        static public List<GuideItem> GetDataFromLA34Url(ChannelToServer channelToServer, DateTime date)
        {
            string url = String.Format(channelToServer.Server, date.ToString("dd-MM-yyyy"));

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

                    var schedulerMain = resultat.DocumentNode.SelectNodes("//div[@class='sch-content']");
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
                                if (tdTags != null && tdTags.Count > 3)
                                {
                                    startOn = tdTags.ElementAt(1).InnerText.Trim();
                                    programName = tdTags.ElementAt(3).InnerText.Trim();
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
                using (HttpClient http = new HttpClient())
                {
                    http.BaseAddress = new Uri("http://www.kplus.vn");
                    http.DefaultRequestHeaders.Add("X-Requested-With", "XMLHttpRequest");
                    http.DefaultRequestHeaders.TryAddWithoutValidation("Accept-Encoding", "gzip, deflate");
                    http.DefaultRequestHeaders.Add("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
                    var stringContent = "";//date=17-8-2016&categories=11";// +date.ToString("dd/MM/yyyy");
                    var httpContent = new StringContent(stringContent, Encoding.UTF8, "application/json");
                    var response = http.PostAsync(url, httpContent).Result;
                    //var response = http.PostAsJsonAsync(url, stringContent).Result;
                    if (response.IsSuccessStatusCode)
                    {
                        var bytes = response.Content.ReadAsByteArrayAsync().Result;
                        String source = Encoding.GetEncoding("utf-8").GetString(bytes, 0, bytes.Length - 1);
                        source = System.Text.RegularExpressions.Regex.Unescape(source);
                        //int start = source.IndexOf("["), end = source.LastIndexOf("]") + 1;
                        //source = source.Substring(start, end - start);
                        var items = JsonConvert.DeserializeObject<KPlus>(source);

                        if (items != null && items.Schedules.Count > 0)
                        {
                            foreach (var item in items.Schedules)
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
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }

        static public List<GuideItem> GetDataFromSCTVUrl(ChannelToServer channelToServer, DateTime date)
        {
            string postdata = "ctl00%24RadScriptManager1=ctl00%24RadScriptManager1%7Cctl00%24ContentPlaceHolder1%24ctl00%24ctl01%24ddlChannel&ctl00_RadScriptManager1_TSM=%3B%3BSystem.Web.Extensions%2C%20Version%3D4.0.0.0%2C%20Culture%3Dneutral%2C%20PublicKeyToken%3D31bf3856ad364e35%3Aen-US%3A50b12c66-1dd3-4ebf-87e6-55014086ad94%3Aea597d4b%3Ab25378d2%3BTelerik.Web.UI%2C%20Version%3D2012.1.411.35%2C%20Culture%3Dneutral%2C%20PublicKeyToken%3D121fae78165ba3d4%3Aen-US%3A4cad056e-160b-4b85-8751-cc8693e9bcd0%3A16e4e7cd%3Aed16cbdc%3Af7645509%3A7c926187%3A8674cba1%3Ab7778d6c%3Ac08e9f8a%3Aa51ee93e%3A59462f1&ctl00%24txtMasterSearch=&ctl00%24ContentPlaceHolder1%24ctl00%24ctl01%24ddlChannel={0}&ctl00%24ContentPlaceHolder1%24ctl00%24ctl01%24sDate={1}&ctl00%24ContentPlaceHolder1%24ctl00%24ctl01%24sDate%24dateInput={1}-00-00-00&ctl00_ContentPlaceHolder1_ctl00_ctl01_sDate_dateInput_ClientState=%7B%22enabled%22%3Atrue%2C%22emptyMessage%22%3A%22%22%2C%22minDateStr%22%3A%221%2F1%2F1900%200%3A0%3A0%22%2C%22maxDateStr%22%3A%2212%2F31%2F2099%200%3A0%3A0%22%2C%22enteredText%22%3A%22%22%7D&ctl00_ContentPlaceHolder1_ctl00_ctl01_sDate_calendar_SD=%5B%5D&ctl00_ContentPlaceHolder1_ctl00_ctl01_sDate_calendar_AD=%5B%5B1900%2C1%2C1%5D%2C%5B2099%2C12%2C30%5D%2C%5B2016%2C8%2C15%5D%5D&ctl00_ContentPlaceHolder1_ctl00_ctl01_sDate_ClientState=%7B%22minDateStr%22%3A%221%2F1%2F1900%200%3A0%3A0%22%2C%22maxDateStr%22%3A%2212%2F31%2F2099%200%3A0%3A0%22%7D&__EVENTTARGET=ctl00%24ContentPlaceHolder1%24ctl00%24ctl01%24ddlChannel&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE=%2FwEPDwUKMTA3MTA2NDk2OA9kFgJmD2QWBAIBD2QWAgIDDxYCHgdjb250ZW50ZGQCAw8WAh4GYWN0aW9uBRQvbGljaC1waGF0LXNvbmcuaHRtbBYCAgMPZBYCAgEPZBYCAgEPZBYGZg9kFgICAQ9kFgJmDxYCHgtfIUl0ZW1Db3VudGZkAgEPZBYCAgEPZBYIAgMPDxYCHhdFbmFibGVBamF4U2tpblJlbmRlcmluZ2hkZAIFDxAPFgYeDURhdGFUZXh0RmllbGQFB0NoYW5uZWweDkRhdGFWYWx1ZUZpZWxkBQxDaGFubmVsTWFwSUQeC18hRGF0YUJvdW5kZ2QQFSoSLS0gQ2jhu41uIGvDqm5oIC0tBVNDVFYxBlNDVFYgMg5TQ1RWIDMgLSBTRUVUVgZTQ1RWIDQGU0NUViA1BlNDVFYgNgZTQ1RWIDcGU0NUViA4BlNDVFYgOQdTQ1RWIDEwEVNDVFYgMTEgLSBUViBTVEFSB1NDVFYgMTIHU0NUViAxMwdTQ1RWIDE0B1NDVFYgMTUHU0NUViAxNhZTQ1RWIFBoaW0gVOG7lW5nIEjhu6NwElNDVFYgSEQgVGjhu4MgVGhhbwVWVEMxNAVWVEMxNgRWVEMyBFZUQzMLVlRDNS1Tb2ZhVFYEVlRDOARUSEJUBFRISEcHVEhEVGhhcARWVEMxCFRvZGF5IFRWDFRIIE5JTkggQklOSAdRUFZOIEhED1ZUQzktTEVUJ1MgVklFVAdWVFYgSFVFA0hCTwdDaW5lbWF4CUZveCBTcG9ydApTVEFSTU9WSUVTClNUQVJTUE9SVFMJU1RBUldPUkxEA0FYTgJEVxUqATABMgEzAjI0ATUBNgE3ATgBOQIxNgI3NwI2MQI2MgMxMDUCMzYCMTADMTAzATEDMTA5AzE1NwMxNTkBMAEwAzE3MQEwAjQ5ATACMzgCNjMCNTkCOTgDMTI4Ajg5AjQwAjcwAjc2AjU3AjcxAjU4AjgyAjgxAjkxFCsDKmdnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZxYBAglkAgcPDxYGHgdNaW5EYXRlBgBAVyBTBVEIHgxTZWxlY3RlZERhdGUGAKhdbWTE04geB01heERhdGUGAEBxb7E%2BMQlkFgRmDxQrAAgPFhIeBFNraW4FB0RlZmF1bHQeBFRleHQFEzIwMTYtMDgtMTUtMDAtMDAtMDAeEUVuYWJsZUFyaWFTdXBwb3J0aB4NTGFiZWxDc3NDbGFzcwUHcmlMYWJlbB8DaB8JBgBAcW%2BxPjEJHgxFbXB0eU1lc3NhZ2VlHwcGAEBXIFMFUQgeEV9za2lwTU1WYWxpZGF0aW9uaGQWBh4FV2lkdGgbAAAAAAAAWUAHAAAAHghDc3NDbGFzcwURcmlUZXh0Qm94IHJpSG92ZXIeBF8hU0ICggIWBh8QGwAAAAAAAFlABwAAAB8RBRFyaVRleHRCb3ggcmlFcnJvch8SAoICFgYfEBsAAAAAAABZQAcAAAAfEQUTcmlUZXh0Qm94IHJpRm9jdXNlZB8SAoICFgYfEBsAAAAAAABZQAcAAAAfEQUTcmlUZXh0Qm94IHJpRW5hYmxlZB8SAoICFgYfEBsAAAAAAABZQAcAAAAfEQUUcmlUZXh0Qm94IHJpRGlzYWJsZWQfEgKCAhYGHxAbAAAAAAAAWUAHAAAAHxEFEXJpVGV4dEJveCByaUVtcHR5HxICggIWBh8QGwAAAAAAAFlABwAAAB8RBRByaVRleHRCb3ggcmlSZWFkHxICggJkAgIPFCsADQ8WFgUERm9jRAYAgLwZn8TTCAUPUmVuZGVySW52aXNpYmxlZwUNU2VsZWN0ZWREYXRlcw8FjwFUZWxlcmlrLldlYi5VSS5DYWxlbmRhci5Db2xsZWN0aW9ucy5EYXRlVGltZUNvbGxlY3Rpb24sIFRlbGVyaWsuV2ViLlVJLCBWZXJzaW9uPTIwMTIuMS40MTEuMzUsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49MTIxZmFlNzgxNjViYTNkNBQrAAAFEUVuYWJsZU11bHRpU2VsZWN0aAUNQ3VsdHVyZU5hbWVJRAUFdmktVk4FC1NwZWNpYWxEYXlzDwWSAVRlbGVyaWsuV2ViLlVJLkNhbGVuZGFyLkNvbGxlY3Rpb25zLkNhbGVuZGFyRGF5Q29sbGVjdGlvbiwgVGVsZXJpay5XZWIuVUksIFZlcnNpb249MjAxMi4xLjQxMS4zNSwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj0xMjFmYWU3ODE2NWJhM2Q0FCsAAAUETWF4RAYAgAdF6D0xCQUETWluRAYAQFcgUwVRCAUNQ3VsdHVyZUluZm9JRCgpbVN5c3RlbS5HbG9iYWxpemF0aW9uLkN1bHR1cmVJbmZvLCBtc2NvcmxpYiwgVmVyc2lvbj00LjAuMC4wLCBDdWx0dXJlPW5ldXRyYWwsIFB1YmxpY0tleVRva2VuPWI3N2E1YzU2MTkzNGUwODkFdmktVk4FCUN1bHR1cmVJRAKqCAUQVmlld1NlbGVjdG9yVGV4dAUBeA8WBh8MaB8KBQdEZWZhdWx0HwNoZGQWBB8RBQtyY01haW5UYWJsZR8SAgIWBB8RBQxyY090aGVyTW9udGgfEgICZBYEHxEFCnJjU2VsZWN0ZWQfEgICZBYEHxEFCnJjRGlzYWJsZWQfEgICFgQfEQUMcmNPdXRPZlJhbmdlHxICAhYEHxEFCXJjV2Vla2VuZB8SAgIWBB8RBQdyY0hvdmVyHxICAhYEHxEFMVJhZENhbGVuZGFyTW9udGhWaWV3IFJhZENhbGVuZGFyTW9udGhWaWV3X0RlZmF1bHQfEgICFgQfEQUJcmNWaWV3U2VsHxICAmQCCQ9kFggCAw8WAh8CAhcWLgIBD2QWAmYPFQIFMDE6MDAcTOG6pFkgQ0jhu5JORyBHScOAVSBTQU5HIFQzOGQCAg9kFgJmDxUCBTAyOjAwF1TDglkgRFUgS8OdIFQzMCAoSOG6vlQpZAIDD2QWAmYPFQIFMDM6MDAdUEjDmkMgVsWoIFbDgCBQSEnDik4gVsOCTiBUMzJkAgQPZBYCZg8VAgUwNDowMBtBTkggSMOZTkcgVEjDgE5IIFRS4bqgSSBUMTJkAgUPZBYCZg8VAgUwNTowMCBUUlVZ4buATiBUSFVZ4bq%2BVCBMScOKVSBUUkFJIFQxNWQCBg9kFgJmDxUCBTA2OjAwHVBIw5pDIFbFqCBWw4AgUEhJw4pOIFbDgk4gVDMzZAIHD2QWAmYPFQIFMDc6MDAmR0lBIMSQw4xOSCBWVUkgVuG6uiBISeG7hk4gxJDhuqBJIFQxNDlkAggPZBYCZg8VAgUwODowMBNUw4JZIERVIEvDnSBQMiAtIFQxZAIJD2QWAmYPFQIFMDk6MDAgVFJVWeG7gE4gVEhVWeG6vlQgTEnDilUgVFJBSSBUMTZkAgoPZBYCZg8VAgUxMDowMBxM4bqkWSBDSOG7kk5HIEdJw4BVIFNBTkcgVDM5ZAILD2QWAmYPFQIFMTE6MDAbQU5IIEjDmU5HIFRIw4BOSCBUUuG6oEkgVDEzZAIMD2QWAmYPFQIFMTI6MDATVMOCWSBEVSBLw50gUDIgLSBUMmQCDQ9kFgJmDxUCBTEzOjAwHEzhuqRZIENI4buSTkcgR0nDgFUgU0FORyBUNDBkAg4PZBYCZg8VAgUxNDowMCZHSUEgxJDDjE5IIFZVSSBW4bq6IEhJ4buGTiDEkOG6oEkgVDE1MGQCDw9kFgJmDxUCBTE1OjAwG0FOSCBIw5lORyBUSMOATkggVFLhuqBJIFQxNGQCEA9kFgJmDxUCBTE2OjAwHVBIw5pDIFbFqCBWw4AgUEhJw4pOIFbDgk4gVDM0ZAIRD2QWAmYPFQIFMTc6MDAmR0lBIMSQw4xOSCBWVUkgVuG6uiBISeG7hk4gxJDhuqBJIFQxNTFkAhIPZBYCZg8VAgUxODowMBNUw4JZIERVIEvDnSBQMiAtIFQzZAITD2QWAmYPFQIFMTk6MDAcTkfGr%2BG7nEkgQuG7kCBUSMOCTiBZw4pVIFQxMWQCFA9kFgJmDxUCBTIwOjAwG0FOSCBIw5lORyBUSMOATkggVFLhuqBJIFQxNWQCFQ9kFgJmDxUCBTIxOjAwHEzhuqRZIENI4buSTkcgR0nDgFUgU0FORyBUNDFkAhYPZBYCZg8VAgUyMjowMB1QSMOaQyBWxaggVsOAIFBIScOKTiBWw4JOIFQzNWQCFw9kFgJmDxUCBTIzOjAwJkdJQSDEkMOMTkggVlVJIFbhurogSEnhu4ZOIMSQ4bqgSSBUMTQ5ZAIHDw8WAh4LTmF2aWdhdGVVcmwFPC9ob20tbmF5LXhlbS1naS9ob20tbmF5LXhlbS1naS84MzM3L2FuaC1odW5nLXRoYW5oLXRyYWkuaHRtbGQWAmYPDxYEHghJbWFnZVVybAU7L01lZGlhL0FydGljbGVzLzIwMTYvNy8zODg3YTFlZWEwNWY0MmY0OTQzMDU1Mjc1YTFhMTI1Ni5qcGceDUFsdGVybmF0ZVRleHQFE2FuaCBodW5nIHRoYW5oIHRyYWlkZAIJDw8WBB8LBRdBbmggaMO5bmcgdGjDoG5oIHRy4bqhaR8TBTwvaG9tLW5heS14ZW0tZ2kvaG9tLW5heS14ZW0tZ2kvODMzNy9hbmgtaHVuZy10aGFuaC10cmFpLmh0bWxkZAINDxYCHwICAxYGZg9kFgZmDxUBOi9ob20tbmF5LXhlbS1naS9ob20tbmF5LXhlbS1naS84MjcwL25ndW9pLWJvLXRoYW4teWV1Lmh0bWxkAgEPDxYEHxQFPH4vTWVkaWEvQXJ0aWNsZXMvMjAxNi83LzAwMGE2NWFhMTNmMTRhNDRiMGZkY2UwODJhOGY2NGUyLmpwZx8VBRFuZ3VvaSBibyB0aGFuIHlldWRkAgIPFQI6L2hvbS1uYXkteGVtLWdpL2hvbS1uYXkteGVtLWdpLzgyNzAvbmd1b2ktYm8tdGhhbi15ZXUuaHRtbBhOZ8aw4budaSBi4buRIHRow6JuIHnDqnVkAgEPZBYGZg8VAU8vaG9tLW5heS14ZW0tZ2kvaG9tLW5heS14ZW0tZ2kvNzYwMS8tdGF5LWR1LWt5LS10dmItdGFpLW5nby1raGFuLWdpYS1zY3R2OS5odG1sZAIBDw8WBB8UBTx%2BL01lZGlhL0FydGljbGVzLzIwMTYvNy9jOTljYzc3YmJmOTY0ZDhkYTI1YTFjNmYxNmU0MTQwMy5qcGcfFQUmIHRheSBkdSBreSAgdHZiIHRhaSBuZ28ga2hhbiBnaWEgc2N0djlkZAICDxUCTy9ob20tbmF5LXhlbS1naS9ob20tbmF5LXhlbS1naS83NjAxLy10YXktZHUta3ktLXR2Yi10YWktbmdvLWtoYW4tZ2lhLXNjdHY5Lmh0bWwuIlTDonkgZHUga8O9IiBUVkIgdMOhaSBuZ%2BG7mSBraMOhbiBnaeG6oyBTQ1RWOWQCAg9kFgZmDxUBOy9ob20tbmF5LXhlbS1naS9waGltLWRhLWNoaWV1LzczNDYvbGF5LWNob25nLWdpYXUtc2FuZy5odG1sZAIBDw8WBB8UBTx%2BL01lZGlhL0FydGljbGVzLzIwMTYvNy81NTVkNGJmNDg1OTY0YTBmOTY5NTExOWUwMzZjM2YxYS5qcGcfFQUTbGF5IGNob25nIGdpYXUgc2FuZ2RkAgIPFQI7L2hvbS1uYXkteGVtLWdpL3BoaW0tZGEtY2hpZXUvNzM0Ni9sYXktY2hvbmctZ2lhdS1zYW5nLmh0bWwYTOG6pXkgY2jhu5NuZyBnacOgdSBzYW5nZAICD2QWAgIBD2QWAgIBDxYCHwICGRYyZg9kFgRmDxUBAGQCAQ8WAh4Dc3JjBSJ%2BL01lZGlhL1Nob3J0Q3V0L2NoYW5uZWwvc2N0djEucG5nZAIBD2QWBGYPFQEAZAIBDxYCHxYFIn4vTWVkaWEvU2hvcnRDdXQvY2hhbm5lbC9zY3R2Mi5wbmdkAgIPZBYEZg8VAQBkAgEPFgIfFgUifi9NZWRpYS9TaG9ydEN1dC9jaGFubmVsL3NjdHYzLnBuZ2QCAw9kFgRmDxUBAGQCAQ8WAh8WBSJ%2BL01lZGlhL1Nob3J0Q3V0L2NoYW5uZWwvc2N0djQucG5nZAIED2QWBGYPFQEAZAIBDxYCHxYFIn4vTWVkaWEvU2hvcnRDdXQvY2hhbm5lbC9zY3R2NS5wbmdkAgUPZBYEZg8VAQBkAgEPFgIfFgUifi9NZWRpYS9TaG9ydEN1dC9jaGFubmVsL3NjdHY2LnBuZ2QCBg9kFgRmDxUBAGQCAQ8WAh8WBSJ%2BL01lZGlhL1Nob3J0Q3V0L2NoYW5uZWwvc2N0djcucG5nZAIHD2QWBGYPFQEAZAIBDxYCHxYFIn4vTWVkaWEvU2hvcnRDdXQvY2hhbm5lbC9zY3R2OC5wbmdkAggPZBYEZg8VAQBkAgEPFgIfFgUifi9NZWRpYS9TaG9ydEN1dC9jaGFubmVsL3NjdHY5LnBuZ2QCCQ9kFgRmDxUBAGQCAQ8WAh8WBSN%2BL01lZGlhL1Nob3J0Q3V0L2NoYW5uZWwvc2N0djEwLnBuZ2QCCg9kFgRmDxUBAGQCAQ8WAh8WBSN%2BL01lZGlhL1Nob3J0Q3V0L2NoYW5uZWwvc2N0djExLnBuZ2QCCw9kFgRmDxUBAGQCAQ8WAh8WBSN%2BL01lZGlhL1Nob3J0Q3V0L2NoYW5uZWwvc2N0djEyLnBuZ2QCDA9kFgRmDxUBAGQCAQ8WAh8WBSN%2BL01lZGlhL1Nob3J0Q3V0L2NoYW5uZWwvc2N0djEzLnBuZ2QCDQ9kFgRmDxUBAGQCAQ8WAh8WBSN%2BL01lZGlhL1Nob3J0Q3V0L2NoYW5uZWwvc2N0djE0LnBuZ2QCDg9kFgRmDxUBAGQCAQ8WAh8WBSN%2BL01lZGlhL1Nob3J0Q3V0L2NoYW5uZWwvc2N0djE1LnBuZ2QCDw9kFgRmDxUBAGQCAQ8WAh8WBSN%2BL01lZGlhL1Nob3J0Q3V0L2NoYW5uZWwvc2N0djE2LnBuZ2QCEA9kFgRmDxUBAGQCAQ8WAh8WBSR%2BL01lZGlhL1Nob3J0Q3V0L2NoYW5uZWwvc2N0dnB0aC5wbmdkAhEPZBYEZg8VAQBkAgEPFgIfFgUjfi9NZWRpYS9TaG9ydEN1dC9jaGFubmVsL3NjdHZ0dC5wbmdkAhIPZBYEZg8VAQBkAgEPFgIfFgUlfi9NZWRpYS9TaG9ydEN1dC9jaGFubmVsL3NjdHZwbmdkLnBuZ2QCEw9kFgRmDxUBAGQCAQ8WAh8WBSN%2BL01lZGlhL1Nob3J0Q3V0L2NoYW5uZWwvc2N0dnRuLnBuZ2QCFA9kFgRmDxUBAGQCAQ8WAh8WBSN%2BL01lZGlhL1Nob3J0Q3V0L2NoYW5uZWwvc2N0dnB2LmpwZ2QCFQ9kFgRmDxUBAGQCAQ8WAh8WBSV%2BL01lZGlhL1Nob3J0Q3V0L2NoYW5uZWwvc2N0dmd0dGgucG5nZAIWD2QWBGYPFQEAZAIBDxYCHxYFJH4vTWVkaWEvU2hvcnRDdXQvY2hhbm5lbC9zY3R2aGFpLnBuZ2QCFw9kFgRmDxUBAGQCAQ8WAh8WBSF%2BL01lZGlhL1Nob3J0Q3V0L2NoYW5uZWwvYnR2My5qcGdkAhgPZBYEZg8VAQBkAgEPFgIfFgUhfi9NZWRpYS9TaG9ydEN1dC9jaGFubmVsL2J0djUucG5nZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAwUrY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRjdGwwMCRjdGwwMSRzRGF0ZQU0Y3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRjdGwwMCRjdGwwMSRzRGF0ZSRjYWxlbmRhcgU0Y3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRjdGwwMCRjdGwwMSRzRGF0ZSRjYWxlbmRhcn8FEEA1gjRQVuuPnaI8HA%2BA9ngqVOLxxMYWvN3e9kTU&__VIEWSTATEGENERATOR=CA0B0334&__EVENTVALIDATION=%2FwEWLgLLwZTvAQLcktymCQLEs6DQDQK8j%2BfPBwKs4M2hCwKy4M2hCwKx4M2hCwKy4L2iCwK34M2hCwK24M2hCwK14M2hCwKk4M2hCwKr4M2hCwKz4LWiCwK14KmiCwK24IGiCwK24IWiCwKMs8XrCQKx4LWiCwKz4I2iCwLG5oG2DgKz4M2hCwLQwtvqAwLaga2BBQLQws%2FqAwKs4M2hCwKs4M2hCwL4mOiAAgKs4M2hCwKw4OGhCwKs4M2hCwKx4O2hCwK24LmiCwK34OGhCwKr4O2hCwLt6%2FHfDQKk4OGhCwKw4I2iCwK14I2iCwK14LWiCwK34KmiCwK14IGiCwK34O2hCwKk4IWiCwKk4IGiCwKr4IGiCyZp08t0uTG3CNFnMh5pfTLTkxfioivjV0%2BqIIw4VzVm&__ASYNCPOST=false&";//2016-08-15
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

        #region search from vietbao
        static public List<SearchItem> SearchDataFromVietBaoUrl(string query, int stationID, DateTime date)
        {
            string url = string.Format(VIETBAO_SEARCH_PAGE, query, date.ToString("dd-MM-yyyy"), stationID);

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
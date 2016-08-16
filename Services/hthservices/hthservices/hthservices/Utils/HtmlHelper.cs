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
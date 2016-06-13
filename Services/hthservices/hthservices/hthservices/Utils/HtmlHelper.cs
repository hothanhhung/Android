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
            string url = String.Format(channelToServer.Server + "channelId={0}&dateSchedule={1}", channelToServer.Value, date.ToString("dd/MM/yyyy"));

            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                HttpClient http = new HttpClient();
                var response = http.GetByteArrayAsync(url).Result;
                String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                source = System.Text.RegularExpressions.Regex.Unescape(source);
                source = WebUtility.HtmlDecode(source.Replace("\"",""));
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
using HtmlAgilityPack;
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
                var response = http.GetByteArrayAsync(url).Result;
                String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                source = WebUtility.HtmlDecode(source);
                HtmlDocument resultat = new HtmlDocument();
                resultat.LoadHtml(source);

                var chanelDetails = resultat.DocumentNode.SelectNodes("//div[@class='chanel-detail']");
                if(chanelDetails !=null && chanelDetails.Count > 0)
                {
                    var chanelDetail = chanelDetails.LastOrDefault();
                    if (chanelDetail != null)
                    {
                        var items = chanelDetail.SelectNodes("div[@class='row']");
                        if(items!=null)
                        {
                            foreach(var item in items)
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
            catch (Exception ex)
            {

            }

            return guideItems;
        }
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
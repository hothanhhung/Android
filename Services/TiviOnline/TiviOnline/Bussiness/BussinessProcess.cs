using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;
using TiviOnline.Models;

namespace TiviOnline.Bussiness
{
    public class BussinessProcess
    {
        public static List<ScheduleItem> GetSchedule(string channel, string date)
        {
            return HtmlHelper.GetSchedule(channel, date);
        }

        public static string GetUrlStream(StreamServer streamServer)
        {
            if (streamServer != null)
            {
                switch (streamServer.ServerId)
                {
                    case 0:
                        return streamServer.URL;
                    case 1:
                        return HtmlHelper.GetUrlStreamFromTVNet(streamServer.URL);
                    case 2:
                        return HtmlHelper.GetUrlFromVietBao(streamServer.URL);
                }
            }
            return string.Empty;
        }

        public static string GetContent(string url)
        {
            if (!string.IsNullOrWhiteSpace(url))
            {
                return HtmlHelper.GetContent(url);
            }
            return string.Empty;
        }
    }
}
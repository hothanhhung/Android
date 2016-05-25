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
    public class DataProcess
    {
        static public List<Channel> GetAllChannels()
        {
            List<Channel> channels = SQLiteProcess.GetAllChannels();
            return channels;
        }
        static public List<Channel> GenarateChannels()
        {
            List<Channel> channels = HtmlHelper.GetAllChannels();            
            var cngr = channels.GroupBy(p => p.ChannelGroupName);
            foreach (var gp in cngr)
            {
                if (gp.Key.Equals("Kênh TH quốc tế",StringComparison.OrdinalIgnoreCase)
                    || gp.Key.Equals("Đài TH địa phương", StringComparison.OrdinalIgnoreCase)
                    || gp.Key.Equals("Đài truyền hình cáp Hà Nội", StringComparison.OrdinalIgnoreCase)
                    || gp.Key.Equals("Đài Truyền hình Cáp TP Hồ Chí Minh - HTVC", StringComparison.OrdinalIgnoreCase)
                    || gp.Key.Equals("Đài truyền hình An Viên AVG", StringComparison.OrdinalIgnoreCase))
                {
                    foreach (var channel in gp.OrderBy(p => p.DataFileName))
                    {
                        SQLiteProcess.SaveChannel(channel);
                    }
                }
                else {
                    foreach (var channel in gp)
                    {
                        SQLiteProcess.SaveChannel(channel);
                    }
                }
            }
            return channels;
        }

        static public List<GuideItem> GetSchedulesOfChannel(string channelKey, DateTime date)
        {
            List<GuideItem> guideItems = new List<GuideItem>();
            guideItems = SQLiteProcess.GetSchedulesOfChannel(channelKey, date);
            if (guideItems == null || guideItems.Count == 0)
            {
                var channel = SQLiteProcess.GetChannel(channelKey);
                if(channel != null && channel.ChannelId > 0)
                {
                    if(!String.IsNullOrWhiteSpace(channel.LinkVietBao)){
                        guideItems = HtmlHelper.GetDataFromVietBaoUrl(channel, date);
                        SQLiteProcess.SetSchedules(guideItems);
                    }
                }
            }
            return guideItems;
        }
    }
}
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
            DateTime VN_Now;
            if (guideItems == null || guideItems.Count == 0)
            {
                var channelToServer = DataStatic.GetChannelToServer(channelKey);
                switch (channelToServer.Server)
                {
                    case DataStatic.FROM_MYTIVI_PAGE:
                        guideItems = HtmlHelper.GetDataFromMyTVUrl(channelToServer, date);
                        break;
                    case DataStatic.FROM_VOV_PAGE:
                        guideItems = HtmlHelper.GetDataFromVOVUrl(channelToServer, date);
                        break;
                    case DataStatic.FROM_VTC14_PAGE:
                        VN_Now = DateTime.UtcNow.AddHours(7);
                        if (VN_Now.Day == date.Day && VN_Now.Month == date.Month && VN_Now.Year == date.Year)
                        {
                            guideItems = HtmlHelper.GetVTC14Url(channelToServer, date);
                        }
                        break;
                    case DataStatic.FROM_TRUYENHINHSO_PAGE:
                        guideItems = HtmlHelper.GetDataFromTruyenHinhSoUrl(channelToServer, date);
                        break;
                    case DataStatic.FROM_TVNET_PAGE:
                        guideItems = HtmlHelper.GetDataTVNetUrl(channelToServer, date);
                        break;
                    case DataStatic.FROM_PHUT91_PAGE:
                        guideItems = HtmlHelper.GetDataFromPhut90Url(channelToServer, date);
                        break;
                    case DataStatic.FROM_HTV3TV_PAGE:
                        guideItems = HtmlHelper.GetDataFromHTV3TVUrl(channelToServer, date);
                        break;
                    case DataStatic.FROM_HTV2CHANNEL_PAGE:
                        guideItems = HtmlHelper.GetDataFromHTV2ChannelTVUrl(channelToServer, date);
                        break;
                    case DataStatic.FROM_QPVN_PAGE:
                        guideItems = HtmlHelper.GetFromQPVNUrl(channelToServer, date);
                        break;
                    case DataStatic.FROM_HTVONLINE_PAGE:
                        guideItems = HtmlHelper.GetDataFromHTVONLINEUrl(channelToServer, date);
                        break;
                    case DataStatic.FROM_FBNC_PAGE:
                        guideItems = HtmlHelper.GetDataFromFBNCUrl(channelToServer, date);
                        break;
                    case DataStatic.FROM_VTVCab_PAGE:
                        guideItems = HtmlHelper.GetDataFromVTVCabUrl(channelToServer, date);
                        break;
                    case DataStatic.FROM_TRAVINH_PAGE:
                        guideItems = HtmlHelper.GetDataFromTRAVINHTVUrl(channelToServer, date);
                        break;
                    case DataStatic.FROM_BPTV_PAGE:
                        guideItems = HtmlHelper.GetDataFromBPTVUrl(channelToServer, date);
                        break;
                    case DataStatic.FROM_HITV_PAGE:
                        VN_Now = DateTime.UtcNow.AddHours(7);
                        if (VN_Now.Day == date.Day && VN_Now.Month == date.Month && VN_Now.Year == date.Year)
                        {
                            guideItems = HtmlHelper.GetDataFromHITVUrl(channelToServer, date);
                        }
                        break;
                    case DataStatic.FROM_YOUTV_PAGE:
                        guideItems = HtmlHelper.GetDataFromYOUTVUrl(channelToServer, date);
                        break; ;
                    case DataStatic.FROM_CAMAU_PAGE:
                        guideItems = HtmlHelper.GetDataFromCAMAUVUrl(channelToServer, date);
                        break;
                    case DataStatic.FROM_BENTRE_PAGE:
                        guideItems = HtmlHelper.GetDataFromBENTREVUrl(channelToServer, date);
                        break;
                    case DataStatic.FROM_QUANGBINH_PAGE:
                        guideItems = HtmlHelper.GetDataFromQUANGBINHUrl(channelToServer, date);
                        break;
                    case DataStatic.FROM_TV24_PAGE:
                        guideItems = HtmlHelper.GetDataFromTV24Url(channelToServer, date);
                        break;
                    case DataStatic.FROM_LETSVIET_PAGE:
                        guideItems = HtmlHelper.GetDataFromLetsVietUrl(channelToServer, date);
                        break;
                    case DataStatic.FROM_KGTV_PAGE:
                        guideItems = HtmlHelper.GetDataFromKGTVUrl(channelToServer, date);
                        break;
                    case DataStatic.FROM_BRT_PAGE:
                        VN_Now = DateTime.UtcNow.AddHours(7);
                        if (VN_Now.Day == date.Day && VN_Now.Month == date.Month && VN_Now.Year == date.Year)
                        {
                            guideItems = HtmlHelper.GetDataFromBRTUrl(channelToServer, date);
                        }
                        break;
                    case DataStatic.FROM_BTV_PAGE:
                        VN_Now = DateTime.UtcNow.AddHours(7);
                        if (VN_Now.Day == date.Day && VN_Now.Month == date.Month && VN_Now.Year == date.Year)
                        {
                            guideItems = HtmlHelper.GetDataFromBTVUrl(channelToServer, date);
                        }
                        break;
                    case DataStatic.FROM_LA34_PAGE:
                        guideItems = HtmlHelper.GetDataFromLA34Url(channelToServer, date);
                        break;
                    default:
                        var channel = SQLiteProcess.GetChannel(channelKey);
                        if (channel != null && channel.ChannelId > 0)
                        {
                            if (!String.IsNullOrWhiteSpace(channel.LinkVietBao))
                            {
                                guideItems = HtmlHelper.GetDataFromVietBaoUrl(channel, date);

                            }
                        }
                        break;
                }
                if (guideItems != null && guideItems.Count > 0)
                {
                    try
                    {
                        System.Threading.Thread th = new System.Threading.Thread(() =>
                        {
                            SQLiteProcess.SetSchedules(guideItems);
                        });
                        th.IsBackground = true;
                        th.Start();
                    }
                    catch (Exception ex) { }
                }
            }
            try
            {
                System.Threading.Thread th = new System.Threading.Thread(() =>
                {
                    SQLiteProcess.SaveScheduleRequestLogs(channelKey, date, guideItems == null || guideItems.Count == 0);
                });
                th.IsBackground = true;
                th.Start();
            }
            catch (Exception ex) { }
            return guideItems;
        }
   
        static public List<SearchItem> SearchDataFromVietBaoUrl(string query, int groupId, DateTime date)
        {
            int index = groupId - 11;
            if (index < 0 || index > DataStatic.Stations.Length - 1) index = 0;
            return hthservices.Utils.HtmlHelper.SearchDataFromVietBaoUrl(query, DataStatic.Stations[index], date);
        }

        #region Logger
        public static List<ScheduleRequestLog> GetScheduleRequestLogs()
        {
            return SQLiteProcess.GetScheduleRequestLogs();
        }
        public static List<ScheduleRequestLog> GetScheduleFailedRequestLogs()
        {
            return SQLiteProcess.GetScheduleFailedRequestLogs();
        }
        public static List<ScheduleRequestLog> GetGroupScheduleRequestLogs(bool noChannelKey, bool noCurrentDate, bool noDateOn)
        {
            return SQLiteProcess.GetGroupScheduleRequestLogs(noChannelKey, noCurrentDate, noDateOn);
        }

        public static List<ScheduleRequestLog> GetGroupScheduleFailedRequestLogs(bool noChannelKey, bool noCurrentDate, bool noDateOn)
        {
            return SQLiteProcess.GetGroupScheduleFailedRequestLogs(noChannelKey, noCurrentDate, noDateOn);
        }

        public static void DeleteScheduleRequestLog(int id)
        {
            SQLiteProcess.DeleteScheduleRequestLog(id);
        }
        public static void DeleteScheduleFailedRequestLog(int id)
        {
            SQLiteProcess.DeleteScheduleFailedRequestLog(id);
        }
        public static void DeleteScheduleRequestLog(string channelKey, string currentDate, string dateOn)
        {
            SQLiteProcess.DeleteScheduleRequestLog(channelKey, currentDate, dateOn);
        }
        public static void DeleteScheduleFailedRequestLog(string channelKey, string currentDate, string dateOn)
        {
            SQLiteProcess.DeleteScheduleFailedRequestLog(channelKey, currentDate, dateOn);
        }
        #endregion
    }
}
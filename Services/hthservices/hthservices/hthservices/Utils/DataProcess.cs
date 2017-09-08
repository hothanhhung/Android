using hthservices.Models;
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

        private static List<GuideItem> GetScheduleFromServer(String channelKey, DateTime date, ChannelToServer channelToServer)
        {
            DateTime VN_Now;
            List<GuideItem> guideItems = new List<GuideItem>();
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
                case DataStatic.FROM_HTVPLUS_PAGE:
                    VN_Now = DateTime.UtcNow.AddHours(7);
                    int index = (date - VN_Now).Days;
                    if (index >= 0 && index < 5)
                    {
                        guideItems = HtmlHelper.GetDataFromHTVPLUSUrl(channelToServer, index, date);
                    }
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
                   // VN_Now = DateTime.UtcNow.AddHours(7);
                 //   if (VN_Now.Day == date.Day && VN_Now.Month == date.Month && VN_Now.Year == date.Year)
                    {
                        guideItems = HtmlHelper.GetDataFromBTVUrl(channelToServer, date);
                    }
                    break;
                case DataStatic.FROM_LA34_PAGE:
                    guideItems = HtmlHelper.GetDataFromLA34Url(channelToServer, date);
                    break;
                case DataStatic.FROM_CANTHOTV_PAGE:
                    VN_Now = DateTime.UtcNow.AddHours(7);
                    int offset = (int)VN_Now.DayOfWeek - (int)date.DayOfWeek;
                    if (offset > 6 || offset < -6) break;
                    guideItems = HtmlHelper.GetDataFromCanThoTVUrl(channelToServer, date);
                    break;
                case DataStatic.FROM_TRT_PAGE:
                    VN_Now = DateTime.UtcNow.AddHours(7);
                    if (VN_Now.Day == date.Day && VN_Now.Month == date.Month && VN_Now.Year == date.Year)
                    {
                        guideItems = HtmlHelper.GetDataFromTRTUrl(channelToServer, date);
                    }
                    break;
                case DataStatic.FROM_PHUTHOTV_PAGE:
                    guideItems = HtmlHelper.GetDataFromPhuThoTVUrl(channelToServer, date);
                    break;
                case DataStatic.FROM_THDT_PAGE:
                    VN_Now = DateTime.UtcNow.AddHours(7);
                    if (VN_Now.Day == date.Day && VN_Now.Month == date.Month && VN_Now.Year == date.Year)
                    {
                        guideItems = HtmlHelper.GetDataFromTHDTUrl(channelToServer, date);
                    }
                    break;
                case DataStatic.FROM_VTC16_PAGE:
                    guideItems = HtmlHelper.GetDataFromVTC16Url(channelToServer, date);
                    break;
                case DataStatic.FROM_TRUELIFE_PAGE:
                    guideItems = HtmlHelper.GetDataFromTrueLifeTVUrl(channelToServer, date);
                    break;
                case DataStatic.FROM_ATV_PAGE:
                    VN_Now = DateTime.UtcNow.AddHours(7);
                    if (VN_Now.Day == date.Day && VN_Now.Month == date.Month && VN_Now.Year == date.Year)
                    {
                        guideItems = HtmlHelper.GetDataFromATVUrl(channelToServer, date);
                    }
                    break;
                case DataStatic.FROM_NAMDINHTV_PAGE:
                    guideItems = HtmlHelper.GetDataFromNamDinhTVUrl(channelToServer, date);
                    break;
                case DataStatic.FROM_HAUGIANGTV_PAGE:
                    guideItems = HtmlHelper.GetDataFromHauGiangTVUrl(channelToServer, date);
                    break;
                case DataStatic.FROM_HAIPHONG_PAGE:
                    VN_Now = DateTime.UtcNow.AddHours(7);
                    if (VN_Now.Day == date.Day && VN_Now.Month == date.Month && VN_Now.Year == date.Year)
                    {
                        guideItems = HtmlHelper.GetDataFromHaiPhongTVUrl(channelToServer, date);
                    }
                    break;
                case DataStatic.FROM_SONLA_PAGE:
                    guideItems = HtmlHelper.GetDataFromSonLaTVUrl(channelToServer, date);
                    break;
                case DataStatic.FROM_THVL_PAGE:
                    guideItems = HtmlHelper.GetDataFromTHVLUrl(channelToServer, date);
                    break;
                case DataStatic.FROM_SCTV_PAGE:
                    guideItems = HtmlHelper.GetDataFromSCTVUrl(channelToServer, date);
                    break;
                case DataStatic.FROM_THST_PAGE:
                    guideItems = HtmlHelper.GetDataFromTHSTUrl(channelToServer, date);
                    break;
                case DataStatic.FROM_BINHDINHTV_PAGE:
                    guideItems = HtmlHelper.GetDataFromBinhDinhTVUrl(channelToServer, date);
                    break;
                case DataStatic.FROM_GIALAITV_PAGE:
                    guideItems = HtmlHelper.GetDataFromGIALAITVUrl(channelToServer, date);
                    break;
                case DataStatic.FROM_DRT_PAGE:
                    guideItems = HtmlHelper.GetDataFromDRTUrl(channelToServer, date);
                    break;
                case DataStatic.FROM_MOBITV_PAGE:
                    guideItems = HtmlHelper.GetDataFromMOBITVUrl(channelToServer, date);
                    break;
                case DataStatic.FROM_KPLUS_PAGE:
                    VN_Now = DateTime.UtcNow.AddHours(7);
                    if (VN_Now.AddDays(7) > date && VN_Now.AddDays(-7) < date)
                    {
                        guideItems = HtmlHelper.GetDataFromKPlusUrl(channelToServer, date);
                    }
                    break;
                case DataStatic.FROM_TODAYTV_PAGE:                    
                        guideItems = HtmlHelper.GetDataFromTODAYTVUrl(channelToServer, date);
                    break;
                case DataStatic.FROM_DNRTV_PAGE:
                        guideItems = HtmlHelper.GetDataFromDNRTVUrl(channelToServer, date);
                    break;                    
                case DataStatic.FROM_VIETBAO_PAGE:
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
            return guideItems;
        }
        static public List<GuideItem> GetSchedulesOfChannel(string channelKey, DateTime date, string requestLink, string device = "", string open = "", string version = "")
        {
            List<GuideItem> guideItems = new List<GuideItem>();
            guideItems = SQLiteProcess.GetSchedulesOfChannel(channelKey, date);
            if (guideItems == null || guideItems.Count == 0)
            {
                var channelToServers = DataStatic.GetListChannelToServer(channelKey);
                foreach (var channelToServer in channelToServers)
                {
                    guideItems = GetScheduleFromServer(channelKey, date, channelToServer);
                    if (guideItems == null || guideItems.Count == 0)
                    {
                        var channelToServer2 = DataStatic.GetChannelToServer2(channelKey, channelToServer);
                        if (channelToServer2 != null)
                        {
                            guideItems = GetScheduleFromServer(channelKey, date, channelToServer2);
                        }
                    }

                    if (guideItems != null && guideItems.Count > 0)
                    {
                        break;
                    }
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
                    SQLiteProcess.SaveScheduleRequestLogs(channelKey, date, guideItems == null || guideItems.Count == 0, device, open, version);
                });
                th.IsBackground = true;
                th.Start();
            }
            catch (Exception ex) { }

            //try
            //{
            //    System.Threading.Thread th = new System.Threading.Thread(() =>
            //    {
            //        SQLiteProcess.SaveRequestInfo("GetSchedules", date, guideItems == null || guideItems.Count == 0, requestLink);
            //    });
            //    th.IsBackground = true;
            //    th.Start();
            //}
            //catch (Exception ex) { }

            return guideItems;
        }

        static public List<SearchItem> SearchDataFromVietBaoUrl(string query, int groupId, DateTime date, string requestLink)
        {
            int index = groupId - 11;
            if (index < 0 || index > DataStatic.Stations.Length - 1) index = 0;
            var guideItems = hthservices.Utils.HtmlHelper.SearchDataFromVietBaoUrl(query, DataStatic.Stations[index], date);
            try
            {
                System.Threading.Thread th = new System.Threading.Thread(() =>
                {
                    SQLiteProcess.SaveRequestInfo("SearchProgram", date, guideItems == null || guideItems.Count == 0, requestLink);
                });
                th.IsBackground = true;
                th.Start();
            }
            catch (Exception ex) { }
            return guideItems;
        }

        #region Logger
        public static List<RequestInfo> GetRequestInfoStatistic(string type, string fromDate, string toDate)
        {
            return SQLiteProcess.GetRequestInfoStatistic(type, fromDate, toDate);
        }
        public static List<RequestInfo> GetRequestInfo(string type, string date, string order = "", bool desc = true, int page = 1, int size = 30)
        {
            return SQLiteProcess.GetRequestInfo(type, date, order, desc, page, size);
        }       
        public static List<ScheduleRequestLog> GetGroupScheduleRequestLogs(ReportFilter filter)
        {
            return SQLiteProcess.GetGroupScheduleRequestLogs(filter, false);
        }
        public static int GetCountGroupScheduleRequestLogs(ReportFilter filter)
        {
            return SQLiteProcess.GetCountGroupScheduleRequestLogs(filter, false);
        }
        public static List<ScheduleRequestLog> GetGroupScheduleFailedRequestLogs(ReportFilter filter)
        {
            return SQLiteProcess.GetGroupScheduleRequestLogs(filter, true);
        }
        public static int GetCountGroupScheduleFailedRequestLogs(ReportFilter filter)
        {
            return SQLiteProcess.GetCountGroupScheduleRequestLogs(filter, true);
        }
        public static void DeleteScheduleRequestLog(int id)
        {
            SQLiteProcess.DeleteScheduleRequestLog(id);
        }
        public static void DeleteScheduleFailedRequestLog(int id)
        {
            SQLiteProcess.DeleteScheduleRequestLog(id, true);
        }
        public static void DeleteScheduleRequestLog(ScheduleRequestLog scheduleLog, bool isFailRequest = false)
        {
            if (scheduleLog != null) SQLiteProcess.DeleteScheduleRequestLog(scheduleLog, isFailRequest);
        }
        public static List<ScheduleRequestLog> GetReportForCurrentDate(string fromDate, string toDate, bool isFailRequest = false)
        {
            return SQLiteProcess.GetReportForCurrentDate(fromDate, toDate, isFailRequest);
        }
        #endregion
    }
}
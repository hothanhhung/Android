using hthservices.Models;
using System;
using System.Collections.Generic;
using System.Data.SQLite;
using System.Linq;
using System.Text;
using System.Web;

namespace hthservices.Utils
{
    public class SQLiteProcess
    {
        private static string connectString;
        private static string ConnectString
        {
            get
            {
                //return @"D:\hung\github\android\Android\Services\hthservices\hthservices\hthservices\Data\TVSchedules.db3";
                if (string.IsNullOrWhiteSpace(connectString))
                {
                    if (HttpContext.Current == null)
                    {
                        if (HttpRuntime.AppDomainAppPath.EndsWith("\\"))
                        {
                            connectString = "Data Source=" + HttpRuntime.AppDomainAppPath + "Data\\TVSchedules.db3;Version=3;New=False;Compress=True;UTF8Encoding=True";
                        }
                        else
                        {
                            connectString = "Data Source=" + HttpRuntime.AppDomainAppPath + "\\Data\\TVSchedules.db3;Version=3;New=False;Compress=True;UTF8Encoding=True";
                        }
                    }
                    else
                    {
                        connectString = "Data Source=" + HttpContext.Current.Server.MapPath("~/Data/TVSchedules.db3") + ";Version=3;New=False;Compress=True;UTF8Encoding=True";
                    }
                }
                return connectString;
            }
        }

        private static string reportConnectString;
        private static string ReportConnectString
        {
            get
            {
                if (string.IsNullOrWhiteSpace(reportConnectString))
                {
                    if (HttpContext.Current == null)
                    {
                        if (HttpRuntime.AppDomainAppPath.EndsWith("\\"))
                        {
                            reportConnectString = "Data Source=" + HttpRuntime.AppDomainAppPath + "Data\\Reports.db3;Version=3;New=False;Compress=True;UTF8Encoding=True";
                        }
                        else
                        {
                            reportConnectString = "Data Source=" + HttpRuntime.AppDomainAppPath + "\\Data\\Reports.db3;Version=3;New=False;Compress=True;UTF8Encoding=True";
                        }
                    }
                    else
                    {
                        reportConnectString = "Data Source=" + HttpContext.Current.Server.MapPath("~/Data/Reports.db3") + ";Version=3;New=False;Compress=True;UTF8Encoding=True";
                    }
                }
                return reportConnectString;
            }
        }

        #region Channel
        public static void SaveChannel(Channel channel)
        {
            string sqlSaveChannel = "";
            if (channel.ChannelId > 0)
            {
                sqlSaveChannel = " UPDATE Channels SET " +
                                " ChannelKey = @ChannelKey, ChannelGroupName = @ChannelGroupName, ChannelName = @ChannelName, VietBaoLink = @VietBaoLink " +
                                " WHERE ChannelId = " + channel.ChannelId;
            }
            else
            {
                sqlSaveChannel = " INSERT INTO Channels(ChannelKey, ChannelGroupName, ChannelName, VietBaoLink) " +
                                 "    VALUES(@ChannelKey, @ChannelGroupName, @ChannelName,@VietBaoLink) ";
            }
            using (var sql_con = new SQLiteConnection(ConnectString))
            {
                sql_con.Open();
                using (var sql_cmd = sql_con.CreateCommand())
                {
                    sql_cmd.CommandText = sqlSaveChannel;
                    SQLiteParameterCollection myParameters = sql_cmd.Parameters;
                    myParameters.AddWithValue("@ChannelKey", channel.ChannelKey);
                    myParameters.AddWithValue("@ChannelGroupName", channel.ChannelGroupName);
                    myParameters.AddWithValue("@ChannelName", channel.ChannelName);
                    myParameters.AddWithValue("@VietBaoLink", channel.LinkVietBao);
                    sql_cmd.ExecuteNonQuery();
                }
                sql_con.Close();
            }
        }

        public static Channel GetChannel(int channelId)
        {
            Channel channel = new Channel();
            string sqlSaveChannel = "SELECT ChannelId, ChannelKey, ChannelGroupName, ChannelName, VietBaoLink FROM Channels WHERE ChannelId = @ChannelId ";
            using (var sql_con = new SQLiteConnection(ConnectString))
            {
                sql_con.Open();
                using (var sql_cmd = sql_con.CreateCommand())
                {
                    sql_cmd.CommandText = sqlSaveChannel;
                    SQLiteParameterCollection myParameters = sql_cmd.Parameters;
                    myParameters.AddWithValue("@ChannelId", channelId);
                    using (var reader = sql_cmd.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            channel.ChannelId = reader.GetInt32(0);
                            channel.ChannelGroupName = reader.GetString(2);
                            channel.ChannelName = reader.GetString(3);
                            channel.LinkVietBao = reader.GetString(4);
                        }
                    }
                }
                sql_con.Close();
            }

            return channel;
        }


        public static Channel GetChannel(string channelKey)
        {
            Channel channel = new Channel();
            string sqlSaveChannel = "SELECT ChannelId, ChannelKey, ChannelGroupName, ChannelName, VietBaoLink FROM Channels WHERE ChannelKey like @ChannelKey ";
            using (var sql_con = new SQLiteConnection(ConnectString))
            {
                sql_con.Open();
                using (var sql_cmd = sql_con.CreateCommand())
                {
                    sql_cmd.CommandText = sqlSaveChannel;
                    SQLiteParameterCollection myParameters = sql_cmd.Parameters;
                    myParameters.AddWithValue("@ChannelKey", channelKey);
                    using (var reader = sql_cmd.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            channel.ChannelId = reader.GetInt32(0);
                            channel.ChannelGroupName = reader.GetString(2);
                            channel.ChannelName = reader.GetString(3);
                            channel.LinkVietBao = reader.GetString(4);
                        }
                    }
                }
                sql_con.Close();
            }

            return channel;
        }


        public static List<Channel> GetAllChannels()
        {
            List<Channel> channels = new List<Channel>();
            string sqlSaveChannel = "SELECT ChannelId, ChannelKey, ChannelGroupName, ChannelName, VietBaoLink FROM Channels";
            using (var sql_con = new SQLiteConnection(ConnectString))
            {
                sql_con.Open();
                using (var sql_cmd = sql_con.CreateCommand())
                {
                    sql_cmd.CommandText = sqlSaveChannel;
                    using (var reader = sql_cmd.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            var channel = new Channel();
                            channel.ChannelId = reader.GetInt32(0);
                            channel.ChannelGroupName = reader.GetString(2);
                            channel.ChannelName = reader.GetString(3);
                            channel.LinkVietBao = reader.GetString(4);
                            channels.Add(channel);
                        }
                    }
                }
                sql_con.Close();
            }

            return channels;
        }

        #endregion

        #region Schedules
        public static List<GuideItem> GetSchedulesOfChannel(string channelKey, DateTime date)
        {
            List<GuideItem> schedules = new List<GuideItem>();
            string sqlSaveChannel = "SELECT ScheduleId, ChannelKey, DateOn, StartOn, ProgramName, Note FROM Schedules WHERE DateOn = @DateOn AND ChannelKey = @ChannelKey";
            using (var sql_con = new SQLiteConnection(ConnectString))
            {
                sql_con.Open();
                using (var sql_cmd = sql_con.CreateCommand())
                {
                    sql_cmd.CommandText = sqlSaveChannel;
                    var myParameters = sql_cmd.Parameters;
                    myParameters.AddWithValue("@DateOn", MethodHelpers.ConvertDateToCorrectString(date));
                    myParameters.AddWithValue("@ChannelKey", channelKey);
                    using (var reader = sql_cmd.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            var guideItem = new GuideItem();
                            guideItem.ScheduleId = reader.GetInt32(0);
                            guideItem.ChannelKey = reader.GetString(1);
                            guideItem.DateOn = reader.GetString(2);
                            guideItem.StartOn = reader.GetString(3);
                            guideItem.ProgramName = reader.GetString(4);
                            guideItem.Note = reader.GetString(5);
                            schedules.Add(guideItem);
                        }
                    }
                }
                sql_con.Close();
            }

            return schedules;
        }

        public static void SetSchedules(List<GuideItem> guideItems)
        {
            if(guideItems == null || guideItems.Count == 0) return;

            List<GuideItem> schedules = new List<GuideItem>();
            StringBuilder  sqlSaveChannel = new StringBuilder("INSERT INTO Schedules(ChannelKey, DateOn, StartOn, ProgramName, Note) VALUES ");

            foreach (var guideItem in guideItems)
            {
                sqlSaveChannel.Append("('" + guideItem.ChannelKey + "','" + guideItem.DateOn + "','" + MethodHelpers.EncodeString(guideItem.StartOn) + "','" + MethodHelpers.EncodeString(guideItem.ProgramName) + "','" + MethodHelpers.EncodeString(guideItem.Note) + "'),");
            }

            sqlSaveChannel.Replace(',',';',sqlSaveChannel.Length - 1, 1);
            using (var sql_con = new SQLiteConnection(ConnectString))
            {
                sql_con.Open();
                using (var sql_cmd = sql_con.CreateCommand())
                {
                    sql_cmd.CommandText = sqlSaveChannel.ToString();
                    sql_cmd.ExecuteNonQuery();
                }
                sql_con.Close();
            }

        }

        #endregion

        #region Log Process

        public static void SaveRequestInfo(string type, DateTime date, bool isNoData, string requestLink)
        {
            string sqlSaveChannel = "";

            sqlSaveChannel = " INSERT INTO RequestInfo(Type, CurrentDate, IsFailed, RequestLink) " +
                             " VALUES(@Type, @CurrentDate, @IsFailed, @RequestLink);";

            try
            {
                using (var sql_con = new SQLiteConnection(ReportConnectString))
                {
                    sql_con.Open();
                    using (var sql_cmd = sql_con.CreateCommand())
                    {
                        sql_cmd.CommandText = sqlSaveChannel;
                        SQLiteParameterCollection myParameters = sql_cmd.Parameters;
                        myParameters.AddWithValue("@Type", type);
                        myParameters.AddWithValue("@CurrentDate", MethodHelpers.ConvertDateToCorrectString(MethodHelpers.GetVNCurrentDate()));
                        myParameters.AddWithValue("@IsFailed", isNoData?"1":"0");
                        myParameters.AddWithValue("@RequestLink", requestLink);
                        sql_cmd.ExecuteNonQuery();
                    }
                    sql_con.Close();
                }
            }
            catch (Exception ex) { }
        }

        public static List<RequestInfo> GetRequestInfoStatistic(string type, string fromDate, string toDate)
        {
            String where = "";
            
            if (!string.IsNullOrWhiteSpace(type))
            {
                where += "WHERE Type=@Type ";
            }
            if (!string.IsNullOrWhiteSpace(fromDate))
            {
                if (!string.IsNullOrWhiteSpace(where))
                {
                    where += " AND CurrentDate>=@FromDate ";
                }
                else
                {
                    where += "WHERE CurrentDate>=@FromDate ";
                }
            }
            if (!string.IsNullOrWhiteSpace(toDate))
            {
                if (!string.IsNullOrWhiteSpace(where))
                {
                    where += " AND CurrentDate<=@ToDate ";
                }
                else
                {
                    where += "WHERE CurrentDate<=@ToDate ";
                }
            }

            List<RequestInfo> requestInfos = new List<RequestInfo>();
            string sqlSaveChannel = "SELECT * FROM RequestInfo " + where;
            using (var sql_con = new SQLiteConnection(ReportConnectString))
            {
                sql_con.Open();
                using (var sql_cmd = sql_con.CreateCommand())
                {
                    sql_cmd.CommandText = sqlSaveChannel;
                    var myParameters = sql_cmd.Parameters;
                    if (!string.IsNullOrWhiteSpace(type))
                    {
                        myParameters.AddWithValue("@Type", type);
                    }
                    if (!string.IsNullOrWhiteSpace(fromDate))
                    {
                        myParameters.AddWithValue("@FromDate", fromDate);
                    }  
                    if (!string.IsNullOrWhiteSpace(toDate))
                    {
                        myParameters.AddWithValue("@ToDate", toDate);
                    }                   
                    

                    using (var reader = sql_cmd.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            var requestInfo = new RequestInfo();
                            requestInfo.Id = Int32.Parse((reader["Id"] ?? "").ToString());
                            requestInfo.CurrentDate = (reader["CurrentDate"] ?? "").ToString();
                            requestInfo.Type = (reader["Type"] ?? "").ToString();
                            requestInfo.IsFailed = (reader["IsFailed"] ?? "").ToString();
                            requestInfo.RequestLink = (reader["RequestLink"] ?? "").ToString();
                            requestInfos.Add(requestInfo);
                        }
                    }
                }
                sql_con.Close();
            }

            return requestInfos;
        }

        public static List<RequestInfo> GetRequestInfo(string type, string date, string order = "", bool desc = true, int page = 1, int size = 30)
        {
            String orderBy = "";
            String where = "";
            if (!string.IsNullOrWhiteSpace(order))
            {
                if (order.Equals("Id", StringComparison.OrdinalIgnoreCase))
                {
                    orderBy = " Order by Id " + (desc ? "DESC" : "ASC");
                }
                else if (order.Equals("CurrentDate", StringComparison.OrdinalIgnoreCase))
                {
                    orderBy = " Order by CurrentDate" + (desc ? "DESC" : "ASC");
                }
            }
            if (!string.IsNullOrWhiteSpace(type))
            {
                where += "WHERE Type=@Type ";
            }
            if (!string.IsNullOrWhiteSpace(date))
            {
                if (!string.IsNullOrWhiteSpace(where))
                {
                    where += " AND CurrentDate=@CurrentDate ";
                }
                else
                {
                    where += "WHERE CurrentDate=@CurrentDate ";
                }
            }

            List<RequestInfo> requestInfos = new List<RequestInfo>();
            string sqlSaveChannel = "SELECT * FROM RequestInfo " + where + orderBy + " LIMIT " + size + " OFFSET " + (page - 1) * size;
            using (var sql_con = new SQLiteConnection(ReportConnectString))
            {
                sql_con.Open();
                using (var sql_cmd = sql_con.CreateCommand())
                {
                    sql_cmd.CommandText = sqlSaveChannel;
                    var myParameters = sql_cmd.Parameters;
                    if (!string.IsNullOrWhiteSpace(type))
                    {
                        myParameters.AddWithValue("@Type", type);
                    }
                    if (!string.IsNullOrWhiteSpace(date))
                    {
                        myParameters.AddWithValue("@CurrentDate", date);
                    }                   
                    

                    using (var reader = sql_cmd.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            var requestInfo = new RequestInfo();
                            requestInfo.Id = Int32.Parse((reader["Id"] ?? "").ToString());
                            requestInfo.CurrentDate = (reader["CurrentDate"] ?? "").ToString();
                            requestInfo.Type = (reader["Type"] ?? "").ToString();
                            requestInfo.IsFailed = (reader["IsFailed"] ?? "").ToString();
                            requestInfo.RequestLink = (reader["RequestLink"] ?? "").ToString();
                            requestInfos.Add(requestInfo);
                        }
                    }
                }
                sql_con.Close();
            }

            return requestInfos;
        }

        public static void SaveScheduleRequestLogs(string channelKey, DateTime date, bool isNoData, string device = "", string open = "", string version = "")
        {
            string sqlSaveChannel = "";
            string tableName = "ScheduleRequestLogs";
            if (isNoData)
            {
                tableName = "ScheduleFailedRequestLogs";
            }
            sqlSaveChannel = " INSERT INTO " + tableName + "(ChannelKey, CurrentDate, DateOn, NumberOfRequests, Note, DeviceId, OpenKey, AppVersion) " +
                             " SELECT @ChannelKey, @CurrentDate, @DateOn, 0, '', @DeviceId, @OpenKey, @AppVersion" +
                             " WHERE NOT EXISTS (SELECT * FROM " + tableName + " WHERE ChannelKey= @ChannelKey AND CurrentDate =@CurrentDate AND DateOn=@DateOn AND DeviceId=@DeviceId AND OpenKey=@OpenKey AND AppVersion=@AppVersion); " +
                             " UPDATE " + tableName + " SET NumberOfRequests=NumberOfRequests+1 WHERE ChannelKey=@ChannelKey AND CurrentDate =@CurrentDate AND DateOn=@DateOn AND DeviceId=@DeviceId AND OpenKey=@OpenKey AND AppVersion=@AppVersion; ";

            try
            {
                using (var sql_con = new SQLiteConnection(ReportConnectString))
                {
                    sql_con.Open();
                    using (var sql_cmd = sql_con.CreateCommand())
                    {
                        sql_cmd.CommandText = sqlSaveChannel;
                        SQLiteParameterCollection myParameters = sql_cmd.Parameters;
                        myParameters.AddWithValue("@ChannelKey", channelKey);
                        myParameters.AddWithValue("@CurrentDate", MethodHelpers.ConvertDateToCorrectString(MethodHelpers.GetVNCurrentDate()));
                        myParameters.AddWithValue("@DateOn", MethodHelpers.ConvertDateToCorrectString(date));
                        myParameters.AddWithValue("@DeviceId", device);
                        myParameters.AddWithValue("@OpenKey", open);
                        myParameters.AddWithValue("@AppVersion", version);
                        sql_cmd.ExecuteNonQuery();
                    }
                    sql_con.Close();
                }
            }
            catch (Exception ex) { }
        }

        public static List<ScheduleRequestLog> GetGroupScheduleRequestLogs(ReportFilter filter, bool isFailRequest = false)
        {
            if (filter == null) filter = new ReportFilter();
            List<ScheduleRequestLog> schedules = new List<ScheduleRequestLog>();

            using (var sql_con = new SQLiteConnection(ReportConnectString))
            {
                sql_con.Open();
                using (var sql_cmd = sql_con.CreateCommand())
                {
                    sql_cmd.CommandText = filter.GenSelectCommand(isFailRequest);
                    var myParameters = sql_cmd.Parameters;
                    using (var reader = sql_cmd.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            var scheduleRequestLog = new ScheduleRequestLog();
                            if (!filter.NoChannelKey || (filter.IsAllFalse)) scheduleRequestLog.ChannelKey = (reader["ChannelKey"] ?? "").ToString();
                            if (!filter.NoCurrentDate || (filter.IsAllFalse)) scheduleRequestLog.CurrentDate = (reader["CurrentDate"] ?? "").ToString();
                            if (!filter.NoDateOn || (filter.IsAllFalse)) scheduleRequestLog.DateOn = (reader["DateOn"] ?? "").ToString();
                            if (!filter.NoDeviceId || (filter.IsAllFalse)) scheduleRequestLog.DeviceId = (reader["DeviceId"] ?? "").ToString();
                            if (!filter.NoOpenKey || (filter.IsAllFalse)) scheduleRequestLog.OpenKey = (reader["OpenKey"] ?? "").ToString();
                            if (!filter.NoAppVersion || (filter.IsAllFalse)) scheduleRequestLog.AppVersion = (reader["AppVersion"] ?? "").ToString();

                            scheduleRequestLog.NumberOfRequests = Int32.Parse((reader["NumberOfRequests"] ?? "").ToString());
                            schedules.Add(scheduleRequestLog);
                        }
                    }
                }
                sql_con.Close();
            }

            return schedules;
        }

        public static List<ScheduleRequestLog> GetReportForCurrentDate(string fromDate, string toDate, bool isFailRequest = false)
        {
            List<ScheduleRequestLog> schedules = new List<ScheduleRequestLog>();

            using (var sql_con = new SQLiteConnection(ReportConnectString))
            {
                sql_con.Open();
                using (var sql_cmd = sql_con.CreateCommand())
                {
                    sql_cmd.CommandText = "SELECT CurrentDate, SUM(NumberOfRequests) AS NumberOfRequests FROM " + (isFailRequest ? "ScheduleFailedRequestLogs" : "ScheduleRequestLogs") + " WHERE CurrentDate >= @fromDate AND CurrentDate <= @toDate GROUP BY CurrentDate ";
                    var myParameters = sql_cmd.Parameters;
                    myParameters.AddWithValue("@fromDate", fromDate);
                    myParameters.AddWithValue("@toDate", toDate);
                    using (var reader = sql_cmd.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            var scheduleRequestLog = new ScheduleRequestLog();
                            scheduleRequestLog.CurrentDate = (reader["CurrentDate"] ?? "").ToString();
                            scheduleRequestLog.NumberOfRequests = Int32.Parse((reader["NumberOfRequests"] ?? "").ToString());
                            schedules.Add(scheduleRequestLog);
                        }
                    }
                }
                sql_con.Close();
            }

            return schedules;
        }

        public static int GetCountGroupScheduleRequestLogs(ReportFilter filter, bool isFailRequest = false)
        {
            if (filter == null) filter = new ReportFilter();
            int count = 0;

            using (var sql_con = new SQLiteConnection(ReportConnectString))
            {
                sql_con.Open();
                using (var sql_cmd = sql_con.CreateCommand())
                {
                    sql_cmd.CommandText = filter.GenCountCommand(isFailRequest);
                    var myParameters = sql_cmd.Parameters;
                    using (var reader = sql_cmd.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            count = reader.GetInt32(0);
                            break;
                        }
                    }
                }
                sql_con.Close();
            }

            return count;
        }
        public static void DeleteScheduleRequestLog(int Id, bool isFailRequest = false)
        {
            string sqlSaveChannel = "DELETE FROM  "+ (isFailRequest ? "ScheduleFailedRequestLogs" : "ScheduleRequestLogs") + " WHERE Id =@Id";
            using (var sql_con = new SQLiteConnection(ReportConnectString))
            {
                sql_con.Open();
                using (var sql_cmd = sql_con.CreateCommand())
                {
                    sql_cmd.CommandText = sqlSaveChannel;
                    var myParameters = sql_cmd.Parameters;
                    myParameters.AddWithValue("@Id", Id);
                    sql_cmd.ExecuteNonQuery();
                }
                sql_con.Close();
            }

        }


        public static void DeleteScheduleRequestLog(ScheduleRequestLog scheduleLog, bool isFailRequest = false)
        {
            string where = (scheduleLog.ChannelKey == null ? "" : " AND ChannelKey=@ChannelKey") + (scheduleLog.CurrentDate == null ? "" : " AND CurrentDate=@CurrentDate") + (scheduleLog.DateOn == null ? "" : " AND DateOn=@DateOn") + (scheduleLog.DeviceId == null ? "" : " AND DeviceId=@DeviceId") + (scheduleLog.OpenKey == null ? "" : " AND OpenKey=@OpenKey") + (scheduleLog.AppVersion == null ? "" : " AND AppVersion=@AppVersion");
            if (!string.IsNullOrWhiteSpace(where))
            {
                where = " WHERE" + where;
                where = where.Replace("WHERE AND ", "WHERE ");
            }
            else
            {
                where = " WHERE ChannelKey=@ChannelKey  AND CurrentDate=@CurrentDate AND DateOn=@DateOn  AND DeviceId=@DeviceId  AND OpenKey=@OpenKey  AND AppVersion=@AppVersion ";
            }
            string sqlSaveChannel = "DELETE FROM " + (isFailRequest ? "ScheduleFailedRequestLogs" : "ScheduleRequestLogs") + " " + where;
            using (var sql_con = new SQLiteConnection(ReportConnectString))
            {
                sql_con.Open();
                using (var sql_cmd = sql_con.CreateCommand())
                {
                    sql_cmd.CommandText = sqlSaveChannel;
                    var myParameters = sql_cmd.Parameters;
                    if (scheduleLog.ChannelKey != null) myParameters.AddWithValue("@ChannelKey", scheduleLog.ChannelKey);
                    if (scheduleLog.CurrentDate != null) myParameters.AddWithValue("@CurrentDate", scheduleLog.CurrentDate);
                    if (scheduleLog.DateOn != null) myParameters.AddWithValue("@DateOn", scheduleLog.DateOn);
                    if (scheduleLog.DeviceId != null) myParameters.AddWithValue("@DeviceId", scheduleLog.DeviceId);
                    if (scheduleLog.OpenKey != null) myParameters.AddWithValue("@OpenKey", scheduleLog.OpenKey);
                    if (scheduleLog.AppVersion != null) myParameters.AddWithValue("@AppVersion", scheduleLog.AppVersion);
                    sql_cmd.ExecuteNonQuery();
                }
                sql_con.Close();
            }

        }
        
        #endregion
    }
}
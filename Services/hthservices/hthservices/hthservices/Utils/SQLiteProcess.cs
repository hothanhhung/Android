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
                    connectString = "Data Source=" + HttpContext.Current.Server.MapPath("~/Data/TVSchedules.db3") + ";Version=3;New=False;Compress=True;UTF8Encoding=True";
                }
                return connectString;
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
            string sqlSaveChannel = "SELECT ChannelId, ChannelKey, ChannelGroupName, ChannelName, VietBaoLink FROM Channels WHERE ChannelKey = @ChannelKey ";
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
        public static void SaveScheduleRequestLogs(string channelKey, DateTime date, bool isNoData)
        {
            string sqlSaveChannel = "";

            sqlSaveChannel = " INSERT INTO ScheduleRequestLogs(ChannelKey, CurrentDate, DateOn, NumberOfRequests, Note) " +
                             " SELECT @ChannelKey, @CurrentDate, @DateOn, 0, '' " +
                             " WHERE NOT EXISTS (SELECT * FROM ScheduleRequestLogs WHERE ChannelKey= @ChannelKey AND CurrentDate =@CurrentDate AND DateOn=@DateOn); " +
                             " UPDATE ScheduleRequestLogs SET NumberOfRequests=NumberOfRequests+1 WHERE ChannelKey=@ChannelKey AND CurrentDate =@CurrentDate AND DateOn=@DateOn; ";

            if (isNoData)
            {
                sqlSaveChannel += " INSERT INTO ScheduleFailedRequestLogs(ChannelKey, CurrentDate, DateOn, NumberOfRequests, Note) " +
                                 " SELECT @ChannelKey, @CurrentDate, @DateOn, 0, '' " +
                                 " WHERE NOT EXISTS (SELECT * FROM ScheduleFailedRequestLogs WHERE ChannelKey= @ChannelKey AND CurrentDate =@CurrentDate AND DateOn=@DateOn); " +
                                 " UPDATE ScheduleFailedRequestLogs SET NumberOfRequests=NumberOfRequests+1 WHERE ChannelKey=@ChannelKey AND CurrentDate =@CurrentDate AND DateOn=@DateOn; ";
            }
            try
            {
                using (var sql_con = new SQLiteConnection(ConnectString))
                {
                    sql_con.Open();
                    using (var sql_cmd = sql_con.CreateCommand())
                    {
                        sql_cmd.CommandText = sqlSaveChannel;
                        SQLiteParameterCollection myParameters = sql_cmd.Parameters;
                        myParameters.AddWithValue("@ChannelKey", channelKey);
                        myParameters.AddWithValue("@CurrentDate", MethodHelpers.ConvertDateToCorrectString(MethodHelpers.GetVNCurrentDate()));
                        myParameters.AddWithValue("@DateOn", MethodHelpers.ConvertDateToCorrectString(date));
                        sql_cmd.ExecuteNonQuery();
                    }
                    sql_con.Close();
                }
            }
            catch (Exception ex) { }
        }

        public static List<ScheduleRequestLog> GetScheduleRequestLogs()
        {
            List<ScheduleRequestLog> schedules = new List<ScheduleRequestLog>();
            string sqlSaveChannel = "SELECT Id, ChannelKey, CurrentDate, DateOn, NumberOfRequests, Note FROM ScheduleRequestLogs";
            using (var sql_con = new SQLiteConnection(ConnectString))
            {
                sql_con.Open();
                using (var sql_cmd = sql_con.CreateCommand())
                {
                    sql_cmd.CommandText = sqlSaveChannel;
                    var myParameters = sql_cmd.Parameters;
                    using (var reader = sql_cmd.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            var scheduleRequestLog = new ScheduleRequestLog();
                            scheduleRequestLog.ID = reader.GetInt32(0);
                            scheduleRequestLog.ChannelKey = reader.GetString(1);
                            scheduleRequestLog.CurrentDate = reader.GetString(2);
                            scheduleRequestLog.DateOn = reader.GetString(3);
                            scheduleRequestLog.NumberOfRequests = reader.GetInt32(4);
                            scheduleRequestLog.Note = reader.GetString(5);
                            schedules.Add(scheduleRequestLog);
                        }
                    }
                }
                sql_con.Close();
            }

            return schedules;
        }

        public static List<ScheduleRequestLog> GetGroupScheduleFailedRequestLogs(bool noChannelKey, bool noCurrentDate, bool noDateOn)
        {
            string group = (noChannelKey ? "" : ", ChannelKey") + (noCurrentDate ? "" : ", CurrentDate") + (noDateOn ? "" : ", DateOn");
            string select = (noChannelKey ? "" : ", ChannelKey") + (noCurrentDate ? "" : ", CurrentDate") + (noDateOn ? "" : ", DateOn");
            group = group.Trim(',');
            select = select.Trim(',');
            if(!string.IsNullOrWhiteSpace(group))
            {
                group = " GROUP BY " + group;
            }
            else
            {
                group = " GROUP BY ChannelKey, CurrentDate, DateOn";
                select = "ChannelKey, CurrentDate, DateOn";
            }
            List<ScheduleRequestLog> schedules = new List<ScheduleRequestLog>();
            string sqlSaveChannel = "SELECT " + select + ", SUM(NumberOfRequests) AS NumberOfRequests FROM ScheduleFailedRequestLogs " + group;
            using (var sql_con = new SQLiteConnection(ConnectString))
            {
                sql_con.Open();
                using (var sql_cmd = sql_con.CreateCommand())
                {
                    sql_cmd.CommandText = sqlSaveChannel;
                    var myParameters = sql_cmd.Parameters;
                    using (var reader = sql_cmd.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            var scheduleRequestLog = new ScheduleRequestLog();
                            if (!noChannelKey || (noCurrentDate && noDateOn)) scheduleRequestLog.ChannelKey = (reader["ChannelKey"] ?? "").ToString();
                            if (!noCurrentDate || (noChannelKey && noDateOn)) scheduleRequestLog.CurrentDate = (reader["CurrentDate"] ?? "").ToString();
                            if (!noDateOn || (noCurrentDate && noChannelKey)) scheduleRequestLog.DateOn = (reader["DateOn"] ?? "").ToString();
                            scheduleRequestLog.NumberOfRequests = Int32.Parse((reader["NumberOfRequests"] ?? "").ToString());
                            schedules.Add(scheduleRequestLog);
                        }
                    }
                }
                sql_con.Close();
            }

            return schedules;
        }
        public static List<ScheduleRequestLog> GetGroupScheduleRequestLogs(bool noChannelKey, bool noCurrentDate, bool noDateOn, int page, int size)
        {
            string group = (noChannelKey ? "" : ", ChannelKey") + (noCurrentDate ? "" : ", CurrentDate") + (noDateOn ? "" : ", DateOn");
            string select = (noChannelKey ? "" : ", ChannelKey") + (noCurrentDate ? "" : ", CurrentDate") + (noDateOn ? "" : ", DateOn");
            group = group.Trim(',');
            select = select.Trim(',');
            if (!string.IsNullOrWhiteSpace(group))
            {
                group = " GROUP BY " + group;
            }
            else
            {
                group = " GROUP BY ChannelKey, CurrentDate, DateOn";
                select = "ChannelKey, CurrentDate, DateOn";
            }
            List<ScheduleRequestLog> schedules = new List<ScheduleRequestLog>();
            string sqlSaveChannel = "SELECT " + select + ", SUM(NumberOfRequests) AS NumberOfRequests FROM ScheduleRequestLogs " + group + " LIMIT "+size+" OFFSET " + (page - 1) * size;
            using (var sql_con = new SQLiteConnection(ConnectString))
            {
                sql_con.Open();
                using (var sql_cmd = sql_con.CreateCommand())
                {
                    sql_cmd.CommandText = sqlSaveChannel;
                    var myParameters = sql_cmd.Parameters;
                    using (var reader = sql_cmd.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            var scheduleRequestLog = new ScheduleRequestLog();
                            if (!noChannelKey || (noCurrentDate && noDateOn)) scheduleRequestLog.ChannelKey = (reader["ChannelKey"] ?? "").ToString();
                            if (!noCurrentDate || (noChannelKey && noDateOn)) scheduleRequestLog.CurrentDate = (reader["CurrentDate"] ?? "").ToString();
                            if (!noDateOn || (noCurrentDate && noChannelKey)) scheduleRequestLog.DateOn = (reader["DateOn"] ?? "").ToString();
                            scheduleRequestLog.NumberOfRequests = Int32.Parse((reader["NumberOfRequests"] ?? "").ToString());
                            schedules.Add(scheduleRequestLog);
                        }
                    }
                }
                sql_con.Close();
            }

            return schedules;
        }

        public static List<ScheduleRequestLog> GetGroupScheduleRequestLogs(ReportFilter filter, bool isFailRequest = false)
        {
            if (filter == null) filter = new ReportFilter();
            List<ScheduleRequestLog> schedules = new List<ScheduleRequestLog>();

            using (var sql_con = new SQLiteConnection(ConnectString))
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
                            if (!filter.NoChannelKey || (filter.NoCurrentDate && filter.NoDateOn)) scheduleRequestLog.ChannelKey = (reader["ChannelKey"] ?? "").ToString();
                            if (!filter.NoCurrentDate || (filter.NoChannelKey && filter.NoDateOn)) scheduleRequestLog.CurrentDate = (reader["CurrentDate"] ?? "").ToString();
                            if (!filter.NoDateOn || (filter.NoCurrentDate && filter.NoChannelKey)) scheduleRequestLog.DateOn = (reader["DateOn"] ?? "").ToString();
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

            using (var sql_con = new SQLiteConnection(ConnectString))
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

            using (var sql_con = new SQLiteConnection(ConnectString))
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
        public static void DeleteScheduleRequestLog(int Id)
        {
            string sqlSaveChannel = "DELETE FROM ScheduleRequestLogs WHERE Id =@Id";
            using (var sql_con = new SQLiteConnection(ConnectString))
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

        public static void DeleteScheduleFailedRequestLog(int Id)
        {
            string sqlSaveChannel = "DELETE FROM ScheduleFailedRequestLogs WHERE Id =@Id";
            using (var sql_con = new SQLiteConnection(ConnectString))
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
        public static void DeleteScheduleRequestLog(string channelKey, string currentDate, string dateOn)
        {
            string where = (channelKey == null ? "" : " AND ChannelKey=@ChannelKey") + (currentDate == null ? "" : " AND CurrentDate=@CurrentDate") + (dateOn == null ? "" : " AND DateOn=@DateOn");
            if (!string.IsNullOrWhiteSpace(where))
            {
                where = " WHERE" + where;
                where = where.Replace("WHERE AND ", "WHERE ");
            }
            else
            {
                where = " WHERE ChannelKey=@ChannelKey  AND CurrentDate=@CurrentDate  AND DateOn=@DateOn";
            }
            string sqlSaveChannel = "DELETE FROM ScheduleRequestLogs " + where;
            using (var sql_con = new SQLiteConnection(ConnectString))
            {
                sql_con.Open();
                using (var sql_cmd = sql_con.CreateCommand())
                {
                    sql_cmd.CommandText = sqlSaveChannel;
                    var myParameters = sql_cmd.Parameters;
                    if (channelKey != null) myParameters.AddWithValue("@ChannelKey", channelKey);
                    if (currentDate != null) myParameters.AddWithValue("@CurrentDate", currentDate);
                    if (dateOn != null) myParameters.AddWithValue("@DateOn", dateOn);
                    sql_cmd.ExecuteNonQuery();
                }
                sql_con.Close();
            }

        }
        
        public static void DeleteScheduleFailedRequestLog(string channelKey, string currentDate, string dateOn)
        {
            string where = (channelKey == null ? "" : " AND ChannelKey=@ChannelKey") + (currentDate == null ? "" : " AND CurrentDate=@CurrentDate") + (dateOn == null ? "" : " AND DateOn=@DateOn");
            if (!string.IsNullOrWhiteSpace(where))
            {
                where = " WHERE" + where;
                where = where.Replace("WHERE AND ", "WHERE ");
            }
            else
            {
                where = " WHERE ChannelKey=@ChannelKey  AND CurrentDate=@CurrentDate  AND DateOn=@DateOn";
            }
            string sqlSaveChannel = "DELETE FROM ScheduleFailedRequestLogs " + where;
            using (var sql_con = new SQLiteConnection(ConnectString))
            {
                sql_con.Open();
                using (var sql_cmd = sql_con.CreateCommand())
                {
                    sql_cmd.CommandText = sqlSaveChannel;
                    var myParameters = sql_cmd.Parameters;
                    if (channelKey != null) myParameters.AddWithValue("@ChannelKey", channelKey);
                    if (currentDate != null) myParameters.AddWithValue("@CurrentDate", currentDate);
                    if (dateOn != null) myParameters.AddWithValue("@DateOn", dateOn);
                    sql_cmd.ExecuteNonQuery();
                }
                sql_con.Close();
            }

        }

        public static List<ScheduleRequestLog> GetScheduleFailedRequestLogs()
        {
            List<ScheduleRequestLog> schedules = new List<ScheduleRequestLog>();
            string sqlSaveChannel = "SELECT Id, ChannelKey, CurrentDate, DateOn, NumberOfRequests, Note FROM ScheduleFailedRequestLogs";
            using (var sql_con = new SQLiteConnection(ConnectString))
            {
                sql_con.Open();
                using (var sql_cmd = sql_con.CreateCommand())
                {
                    sql_cmd.CommandText = sqlSaveChannel;
                    var myParameters = sql_cmd.Parameters;
                    using (var reader = sql_cmd.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            var scheduleRequestLog = new ScheduleRequestLog();
                            scheduleRequestLog.ID = reader.GetInt32(0);
                            scheduleRequestLog.ChannelKey = reader.GetString(1);
                            scheduleRequestLog.CurrentDate = reader.GetString(2);
                            scheduleRequestLog.DateOn = reader.GetString(3);
                            scheduleRequestLog.NumberOfRequests = reader.GetInt32(4);
                            scheduleRequestLog.Note = reader.GetString(5);
                            schedules.Add(scheduleRequestLog);
                        }
                    }
                }
                sql_con.Close();
            }

            return schedules;
        }
        #endregion
    }
}
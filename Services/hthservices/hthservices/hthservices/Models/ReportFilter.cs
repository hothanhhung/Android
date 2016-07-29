using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace hthservices.Models
{
    public class ReportFilter
    {
        public bool NoChannelKey {get; set;}
        public bool NoCurrentDate {get; set;} 
        public bool NoDateOn {get; set;} 
        public int? Page {get; set;}
        public int? Size { get; set; }
        public bool? Desc { get; set; }
        public string OrderField {get; set;}


        public int GetCorrectSize()
        {
            return Size ?? 25;
        }
        public string GenSelectCommand(bool isFailRequest = false)
        {
            string group = (NoChannelKey ? "" : ", ChannelKey") + (NoCurrentDate ? "" : ", CurrentDate") + (NoDateOn ? "" : ", DateOn");
            string select = (NoChannelKey ? "" : ", ChannelKey") + (NoCurrentDate ? "" : ", CurrentDate") + (NoDateOn ? "" : ", DateOn");
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
            string sqlSaveChannel = "SELECT " + select + ", SUM(NumberOfRequests) AS NumberOfRequests FROM "+ (isFailRequest?"ScheduleFailedRequestLogs" : "ScheduleRequestLogs") +" " + group + " LIMIT " + (Size ?? 25) + " OFFSET " + (((Page ?? 1) - 1) * (Size ?? 25));
            
            return sqlSaveChannel;
        }

        public string GenCountCommand(bool isFailRequest = false)
        {
            string group = (NoChannelKey ? "" : ", ChannelKey") + (NoCurrentDate ? "" : ", CurrentDate") + (NoDateOn ? "" : ", DateOn");
            group = group.Trim(',');
            if (!string.IsNullOrWhiteSpace(group))
            {
                group = " GROUP BY " + group;
            }
            else
            {
                group = " GROUP BY ChannelKey, CurrentDate, DateOn";
            }
            string sqlSaveChannel = "SELECT count(*) FROM (SELECT * FROM " + (isFailRequest ? "ScheduleFailedRequestLogs" : "ScheduleRequestLogs") + " " + group +" )";

            return sqlSaveChannel;
        }
    }
}
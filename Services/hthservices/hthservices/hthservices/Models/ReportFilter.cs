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
        public string FromDate { get; set; }
        public string ToDate { get; set; }

        public int GetCorrectSize()
        {
            return Size ?? 25;
        }
        public string GenSelectCommand(bool isFailRequest = false)
        {
            string order_by = "";
            string group = (NoChannelKey ? "" : ", ChannelKey") + (NoCurrentDate ? "" : ", CurrentDate") + (NoDateOn ? "" : ", DateOn");
            string select = (NoChannelKey ? "" : ", ChannelKey") + (NoCurrentDate ? "" : ", CurrentDate") + (NoDateOn ? "" : ", DateOn");

            string where = "";

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

            if(!string.IsNullOrWhiteSpace(OrderField) && (OrderField.Equals("NumberOfRequests", StringComparison.OrdinalIgnoreCase) ||
                                                            select.Split(',').Any(p=> OrderField.Equals(p.Trim(), StringComparison.OrdinalIgnoreCase))))
            {
                order_by = " ORDER BY " + OrderField + ((Desc??true)?" DESC":" ASC");
            }

            if(!string.IsNullOrWhiteSpace(FromDate) && !string.IsNullOrWhiteSpace(ToDate))
            {
                where = " WHERE CurrentDate >= '" + FromDate + "' AND CurrentDate <= '" + ToDate + "' ";
            }

            string sqlSaveChannel = "SELECT " + select + ", SUM(NumberOfRequests) AS NumberOfRequests FROM " + (isFailRequest ? "ScheduleFailedRequestLogs" : "ScheduleRequestLogs") + " " + where + group + order_by + " LIMIT " + GetCorrectSize() + " OFFSET " + (((Page ?? 1) - 1) * GetCorrectSize());
            
            return sqlSaveChannel;
        }

        public string GenCountCommand(bool isFailRequest = false)
        {
            string group = (NoChannelKey ? "" : ", ChannelKey") + (NoCurrentDate ? "" : ", CurrentDate") + (NoDateOn ? "" : ", DateOn");
            group = group.Trim(',');
            string where = "";
            if (!string.IsNullOrWhiteSpace(group))
            {
                group = " GROUP BY " + group;
            }
            else
            {
                group = " GROUP BY ChannelKey, CurrentDate, DateOn";
            }
            if (!string.IsNullOrWhiteSpace(FromDate) && !string.IsNullOrWhiteSpace(ToDate))
            {
                where = " WHERE CurrentDate >= '" + FromDate + "' AND CurrentDate <= '" + ToDate + "' ";
            }
            string sqlSaveChannel = "SELECT count(*) FROM (SELECT * FROM " + (isFailRequest ? "ScheduleFailedRequestLogs" : "ScheduleRequestLogs") + " " + where + group + " )";

            return sqlSaveChannel;
        }
    }
}
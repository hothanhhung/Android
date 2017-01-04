using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace hthservices.Models
{
    public class ReportFilter
    {
        private readonly string[] Fields = new string[] { "ChannelKey", "CurrentDate", "DateOn", "DeviceId", "OpenKey", "AppVersion" };
        public bool NoChannelKey {get; set;}
        public bool NoCurrentDate {get; set;} 
        public bool NoDateOn {get; set;}
        public bool NoDeviceId { get; set; }
        public bool NoOpenKey { get; set; }
        public bool NoAppVersion { get; set; } 

        public int? Page {get; set;}
        public int? Size { get; set; }
        public bool? Desc { get; set; }
        public string OrderField {get; set;}
        public string FromDate { get; set; }
        public string ToDate { get; set; }

        public List<KeyValuePair<string, string>> Conditions { get; set; }

        public int GetCorrectSize()
        {
            return Size ?? 25;
        }

        public bool IsAllFalse
        {
            get { return !NoChannelKey && !NoCurrentDate && !NoDateOn && !NoDeviceId && !NoOpenKey && !NoAppVersion; }
        }

        public string GenSelectCommand(bool isFailRequest = false)
        {
            string order_by = "";
            string group = (NoChannelKey ? "" : ", ChannelKey") + (NoCurrentDate ? "" : ", CurrentDate") + (NoDateOn ? "" : ", DateOn") + (NoDeviceId ? "" : ", DeviceId") + (NoOpenKey ? "" : ", OpenKey") + (NoAppVersion ? "" : ", AppVersion");
            string select = (NoChannelKey ? "" : ", ChannelKey") + (NoCurrentDate ? "" : ", CurrentDate") + (NoDateOn ? "" : ", DateOn") + (NoDeviceId ? "" : ", DeviceId") + (NoOpenKey ? "" : ", OpenKey") + (NoAppVersion ? "" : ", AppVersion");

            group = group.Trim(',');
            select = select.Trim(',');
            if (!string.IsNullOrWhiteSpace(group))
            {
                group = " GROUP BY " + group;
            }
            else
            {
                group = " GROUP BY ChannelKey, CurrentDate, DateOn, DeviceId, OpenKey, AppVersion";
                select = "ChannelKey, CurrentDate, DateOn, DeviceId, OpenKey, AppVersion";
            }

            if(!string.IsNullOrWhiteSpace(OrderField) && (OrderField.Equals("NumberOfRequests", StringComparison.OrdinalIgnoreCase) ||
                                                            select.Split(',').Any(p=> OrderField.Equals(p.Trim(), StringComparison.OrdinalIgnoreCase))))
            {
                order_by = " ORDER BY " + OrderField + ((Desc??true)?" DESC":" ASC");
            }


            string sqlSaveChannel = "SELECT " + select + ", SUM(NumberOfRequests) AS NumberOfRequests FROM " + (isFailRequest ? "ScheduleFailedRequestLogs" : "ScheduleRequestLogs") + " " + WhereCondition + group + order_by + " LIMIT " + GetCorrectSize() + " OFFSET " + (((Page ?? 1) - 1) * GetCorrectSize());
            
            return sqlSaveChannel;
        }

        public string GenCountCommand(bool isFailRequest = false)
        {
            string group = (NoChannelKey ? "" : ", ChannelKey") + (NoCurrentDate ? "" : ", CurrentDate") + (NoDateOn ? "" : ", DateOn") + (NoDeviceId ? "" : ", DeviceId") + (NoOpenKey ? "" : ", OpenKey") + (NoAppVersion ? "" : ", AppVersion");
            group = group.Trim(',');
            
            if (!string.IsNullOrWhiteSpace(group))
            {
                group = " GROUP BY " + group;
            }
            else
            {
                group = " GROUP BY ChannelKey, CurrentDate, DateOn, DeviceId, OpenKey, AppVersion";
            }

            string sqlSaveChannel = "SELECT count(*) FROM (SELECT * FROM " + (isFailRequest ? "ScheduleFailedRequestLogs" : "ScheduleRequestLogs") + " " + WhereCondition + group + " )";

            return sqlSaveChannel;
        }

        private string WhereCondition
        {
            get
            {
                string where = "";
                if (!string.IsNullOrWhiteSpace(FromDate) && !string.IsNullOrWhiteSpace(ToDate))
                {
                    where = " WHERE CurrentDate >= '" + FromDate + "' AND CurrentDate <= '" + ToDate + "' ";
                }
                if (Conditions != null && Conditions.Count > 0)
                {
                    string field;
                    foreach (var condition in Conditions)
                    {
                        field = GetFieldName(condition.Key);
                        if (!string.IsNullOrWhiteSpace(field))
                        {
                            if (string.IsNullOrWhiteSpace(where))
                            {
                                where += " WHERE " + field + " like '" + condition.Value + "' ";
                            }
                            else
                            {
                                where += " AND " + field + " like '" + condition.Value + "' ";
                            }
                        }
                    }
                }
                return where;
            }
        }

        private string GetFieldName(string field)
        {
            return Fields.Where(p => p.Equals(field, StringComparison.OrdinalIgnoreCase)).FirstOrDefault();
        }
    }
}
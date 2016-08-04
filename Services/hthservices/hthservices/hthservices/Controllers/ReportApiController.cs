﻿using hthservices.Data;
using hthservices.Models;
using hthservices.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;

namespace hthservices.Controllers
{
    public class ReportApiController : ApiController
    {
        //
        // GET: /ReportApi/

        public ActionResult Index()
        {
            return ResponseJson.GetResponseJson(string.Empty, false);
        }

        [System.Web.Http.HttpGet]
        [System.Web.Http.HttpPost]
        [System.Web.Http.ActionName("Delete")]
        public ResponseJson Delete(string token, string isFailed, ScheduleRequestLog scheduleLog)
        {
            if(!AuthData.Validate(token))
            {
                return ResponseJson.GetResponseJson(string.Empty, false);
            }
            int id = scheduleLog.ID;
            if (id > 0)
            {
                if ("true".Equals(isFailed, StringComparison.OrdinalIgnoreCase))
                {
                    DataProcess.DeleteScheduleFailedRequestLog(id);
                }
                else
                {
                    DataProcess.DeleteScheduleRequestLog(id);
                }
            }
            else
            {
                if ("true".Equals(isFailed, StringComparison.OrdinalIgnoreCase))
                {
                    DataProcess.DeleteScheduleFailedRequestLog(scheduleLog.ChannelKey, scheduleLog.CurrentDate, scheduleLog.DateOn);
                }
                else
                {
                    DataProcess.DeleteScheduleRequestLog(scheduleLog.ChannelKey, scheduleLog.CurrentDate, scheduleLog.DateOn);
                }

            }
            var resultObject = new { IsSuccess = true, Message = "Deleted"};
            return ResponseJson.GetResponseJson(resultObject);
        }

        [System.Web.Http.HttpGet]
        [System.Web.Http.HttpPost]
        [System.Web.Http.ActionName("GroupFailedRequest")]
        public ResponseJson GroupFailedRequest(string token, [FromBody] ReportFilter filter)
        {
            if (!AuthData.Validate(token))
            {
                return ResponseJson.GetResponseJson(string.Empty, false);
            }
            if (filter == null) filter = new ReportFilter();
            var data = DataProcess.GetGroupScheduleFailedRequestLogs(filter);
            var total = DataProcess.GetCountGroupScheduleFailedRequestLogs(filter);
            var resultObject = new ScheduleReport() { ScheduleLogs = data, Total = total, NumberOfPages = (total + filter.GetCorrectSize() - 1) / filter.GetCorrectSize() };
            return ResponseJson.GetResponseJson(resultObject);
        }

        [System.Web.Http.HttpGet]
        [System.Web.Http.HttpPost]
        [System.Web.Http.ActionName("GroupRequest")]
        public ResponseJson GroupRequest(string token, ReportFilter filter)
        {
            if (!AuthData.Validate(token))
            {
                return ResponseJson.GetResponseJson(string.Empty, false);
            }
            if (filter == null) filter = new ReportFilter();
            var data = DataProcess.GetGroupScheduleRequestLogs(filter);
            var total = DataProcess.GetCountGroupScheduleRequestLogs(filter);
            var resultObject = new ScheduleReport() { ScheduleLogs = data, Total = total, NumberOfPages = (total + filter.GetCorrectSize() - 1) / filter.GetCorrectSize() };
            return ResponseJson.GetResponseJson(resultObject);
        }

        [System.Web.Http.HttpGet]
        public ResponseJson Dashboard(string token, string fromDate, string toDate)
        {
            if (!AuthData.Validate(token))
            {
                return ResponseJson.GetResponseJson(string.Empty, false);
            }
            var logs = DataProcess.GetReportForCurrentDate(fromDate, toDate, false);
            var failLogs = DataProcess.GetReportForCurrentDate(fromDate, toDate, true);

            var resultObject =
                        from log in logs
                        join failLog in failLogs on log.CurrentDate equals failLog.CurrentDate
                        select new ReportForCurrentDate
                        {
                            CurrentDate = log.CurrentDate,
                            NumberOfRequest = log.NumberOfRequests, 
                            NumberOfFailedRequest = failLog.NumberOfRequests 
                        };
            return ResponseJson.GetResponseJson(resultObject != null ? resultObject.ToList() : null);
        }
    }
}

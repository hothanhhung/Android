using hthservices.Data;
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
        public ResponseJson GroupFailedRequest(string token, [FromBody] ReportFilter filter)
        {
            if (!AuthData.Validate(token))
            {
                return ResponseJson.GetResponseJson(string.Empty, false);
            }
            var data = DataProcess.GetGroupScheduleFailedRequestLogs(filter);
            var total = DataProcess.GetCountGroupScheduleFailedRequestLogs(filter);
            var resultObject = new ScheduleReport() { ScheduleLogs = data, Total = total, NumberOfPages = (total + filter.GetCorrectSize() - 1) / filter.GetCorrectSize() };
            return ResponseJson.GetResponseJson(resultObject);
        }

        [System.Web.Http.HttpGet]
        [System.Web.Http.HttpPost]
        public ResponseJson GroupRequest(string token, ReportFilter filter)
        {
            if (!AuthData.Validate(token))
            {
                return ResponseJson.GetResponseJson(string.Empty, false);
            }

            var data = DataProcess.GetGroupScheduleRequestLogs(filter);
            var total = DataProcess.GetCountGroupScheduleRequestLogs(filter);
            var resultObject = new ScheduleReport() { ScheduleLogs = data, Total = total, NumberOfPages = (total + filter.GetCorrectSize() - 1) / filter.GetCorrectSize() };
            return ResponseJson.GetResponseJson(resultObject);
        }

    }
}

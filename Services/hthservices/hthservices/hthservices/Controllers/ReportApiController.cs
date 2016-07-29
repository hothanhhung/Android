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

        public ActionResult Delete(string token, string log, string type, string cn, string cd, string don)
        {
            if(!AuthData.Validate(token))
            {
                return ResponseJson.GetResponseJson(string.Empty, false);
            }
            int id = 0;
            if (Int32.TryParse(log, out id))
            {
                if ("log".Equals(type, StringComparison.OrdinalIgnoreCase))
                {
                    DataProcess.DeleteScheduleRequestLog(id);
                }
                else
                {
                    DataProcess.DeleteScheduleFailedRequestLog(id);
                }
            }
            else
            {
                if ("log".Equals(type, StringComparison.OrdinalIgnoreCase))
                {
                    DataProcess.DeleteScheduleRequestLog(cn, cd, don);
                }
                else
                {
                    DataProcess.DeleteScheduleFailedRequestLog(cn, cd, don);
                }

            }
            var resultObject = new { IsSuccess = true, Message = "Deleted"};
            return ResponseJson.GetResponseJson(resultObject);
        }

        public ActionResult GroupRequest1(string token, string nocn, string nocd, string nodon, string page, string size)
        {
            int pagei = 0, sizei = 25;
            if(!Int32.TryParse(size, out sizei))
            {
                sizei = 25;
            }
            if (!Int32.TryParse(page, out pagei))
            {
                pagei = 1;
            }

            if (!AuthData.Validate(token))
            {
                return ResponseJson.GetResponseJson(string.Empty, false);
            }
            bool bnocn = nocn == "1", bnocd = nocd == "1", bnodon = nodon == "1";
            var resultObject = DataProcess.GetGroupScheduleRequestLogs(bnocn, bnocd, bnodon, pagei, sizei);
            return ResponseJson.GetResponseJson(resultObject);
        }

        public ActionResult GroupFailedRequest1(string token, string nocn, string nocd, string nodon)
        {
            if (!AuthData.Validate(token))
            {
                return ResponseJson.GetResponseJson(string.Empty, false);
            }
            bool bnocn = nocn == "1", bnocd = nocd == "1", bnodon = nodon == "1";
            var resultObject = DataProcess.GetGroupScheduleFailedRequestLogs(bnocn, bnocd, bnodon);
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

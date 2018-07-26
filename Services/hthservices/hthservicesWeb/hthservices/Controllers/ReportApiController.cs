using hthservices.Ads;
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
        [System.Web.Http.ActionName("Delete")]
        public ResponseJson Delete(string token, string isFailed, ScheduleRequestLog scheduleLog)
        {
            if(AuthData.GetRole(token) != Role.Admin)
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
                    DataProcess.DeleteScheduleRequestLog(scheduleLog, true);
                }
                else
                {
                    DataProcess.DeleteScheduleRequestLog(scheduleLog, false);
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
            if (AuthData.GetRole(token) == Role.NoLogin)
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
            if (AuthData.GetRole(token) == Role.NoLogin)
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
        public ResponseJson RequestInfo(string token, string type, string date, string order = "", bool desc = true, int page = 1, int size = 30)
        {
            if (AuthData.GetRole(token) == Role.NoLogin)
            {
                return ResponseJson.GetResponseJson(string.Empty, false);
            }
            var requestInfos = DataProcess.GetRequestInfo(type, date, order, desc, page, size);

            return ResponseJson.GetResponseJson(requestInfos);
        }

        [System.Web.Http.HttpGet]
        public ResponseJson GetRequestInfoStatistic(string token, string type, string fromDate, string toDate = "")
        {
            if (AuthData.GetRole(token) == Role.NoLogin)
            {
                return ResponseJson.GetResponseJson(string.Empty, false);
            }
            var requestInfo = DataProcess.GetRequestInfoStatistic(type, fromDate, toDate);
            var ownerAds = OwnerAds.OWNER_ADITEMS;
            List<AdsItemReport> adsItemReports = new List<AdsItemReport>();
            foreach(var ad in ownerAds)
            {
                if (!string.IsNullOrWhiteSpace(ad.PackageName))
                {
                    var adsItemReport = new AdsItemReport();
                    adsItemReport.Name = ad.Name;
                    adsItemReport.NameVN = ad.NameVN;
                    adsItemReport.UrlImage = ad.UrlImage;
                    adsItemReport.TotalClickOnItems = new List<int>();
                    var requestInfoPackage = requestInfo.Where(p => p.PackageRequest.Equals(ad.PackageName, StringComparison.OrdinalIgnoreCase));
                    adsItemReport.Total = requestInfoPackage.Count();
                    foreach (var adItem in ownerAds)
                    {
                        if (!string.IsNullOrWhiteSpace(adItem.PackageName))
                        {
                            var requestInfoItem = requestInfoPackage.Where(p => !String.IsNullOrWhiteSpace(p.InfoRequest) && p.InfoRequest.EndsWith(adItem.PackageName, StringComparison.OrdinalIgnoreCase));
                            adsItemReport.TotalClickOnItems.Add(requestInfoItem.Count());
                        }

                    }
                    adsItemReports.Add(adsItemReport);
                }
            }
            return ResponseJson.GetResponseJson(adsItemReports);
        }

        [System.Web.Http.HttpGet]
        public ResponseJson Dashboard(string token, string fromDate, string toDate)
        {
            if (AuthData.GetRole(token) == Role.NoLogin)
            {
                return ResponseJson.GetResponseJson(string.Empty, false);
            }
            var logs = DataProcess.GetReportForCurrentDate(fromDate, toDate, false);
            var failLogs = DataProcess.GetReportForCurrentDate(fromDate, toDate, true);

            var resultObject =
                        from log in logs
                        join failLog in failLogs on log.CurrentDate equals failLog.CurrentDate into ps
                        from failLog in ps.DefaultIfEmpty()
                        select new ReportForCurrentDate
                        {
                            CurrentDate = log.CurrentDate,
                            NumberOfRequest = log.NumberOfRequests,
                            NumberOfFailedRequest = failLog == null ? 0 : failLog.NumberOfRequests
                        };
            return ResponseJson.GetResponseJson(resultObject != null ? resultObject.ToList() : null);
        }
    }
}

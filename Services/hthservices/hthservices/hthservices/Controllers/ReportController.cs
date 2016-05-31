using hthservices.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace hthservices.Controllers
{
    public class ReportController : Controller
    {
        //
        // GET: /Report/

        public ActionResult Index()
        {
            ViewBag.ScheduleRequestLogs = DataProcess.GetScheduleRequestLogs();
            ViewBag.ScheduleFailedRequestLogs = DataProcess.GetScheduleFailedRequestLogs();
            return View();
        }

        public ActionResult Delete()
        {
            int id = 0;
            if (!String.IsNullOrWhiteSpace(Request["log"]) && Int32.TryParse(Request["log"], out id))
            {
                if ("log".Equals(Request["type"], StringComparison.OrdinalIgnoreCase))
                {
                    DataProcess.DeleteScheduleRequestLog(id);
                }
                else
                {
                    DataProcess.DeleteScheduleFailedRequestLog(id);
                }
                return RedirectToAction("", "Report");
            }
            else
            {
                if ("log".Equals(Request["type"], StringComparison.OrdinalIgnoreCase))
                {
                    DataProcess.DeleteScheduleRequestLog(Request["cn"], Request["cd"], Request["don"]);
                }
                else
                {
                    DataProcess.DeleteScheduleFailedRequestLog(Request["cn"], Request["cd"], Request["don"]);
                }
                
                return RedirectToAction("Group", "Report", new { nocn = Request["nocn"]?? "0", nocd = Request["nocd"]??"0", nodon = Request["nodon"]?? "0" });
            }
        }

        public ActionResult Group()
        {
            bool nocn = Request["nocn"] == "1", nocd = Request["nocd"] == "1", nodon = Request["nodon"] == "1";
            ViewBag.ScheduleRequestLogs = DataProcess.GetGroupScheduleRequestLogs(nocn,nocd, nodon);
            ViewBag.ScheduleFailedRequestLogs = DataProcess.GetGroupScheduleFailedRequestLogs(nocn, nocd, nodon);
            return View();
        }
    }
}

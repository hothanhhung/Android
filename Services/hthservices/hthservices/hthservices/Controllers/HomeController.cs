using hthservices.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace hthservices.Controllers
{
    public class HomeController : Controller
    {
        public ActionResult Index()
        {
            //var channels = hthservices.Utils.DataProcess.GetAllChannels();
            //var channels1 = hthservices.Utils.SQLiteProcess.GetAllChannels();
            ViewBag.Message = "Modify this template to jump-start your ASP.NET MVC application.";

            return View();
        }

        public ActionResult About()
        {
            ViewBag.Message = "Your app description page.";

            return View();
        }

        public ActionResult Contact()
        {
            ViewBag.Message = "Your contact page.";

            return View();
        }

    }
}

using hthservices.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Mvc;

namespace hthservices.Controllers
{
    public class TraCuuOnlineController : Controller
    {
        public ActionResult Index()
        {
            return Redirect("/htmlpage/LookupOnline/index.html");
        }

        public ActionResult VietlottMega645(string strDate="")
        {
            DateTime date;
            if (!String.IsNullOrWhiteSpace(strDate) && DateTime.TryParseExact(strDate, "dd-mm-yyyy", System.Globalization.CultureInfo.InvariantCulture, System.Globalization.DateTimeStyles.None, out date))
            {
                ViewBag.Content = TraCuuOnlineHelper.GetVietlottMega645(date);
            }
            else
            {
                ViewBag.Content = TraCuuOnlineHelper.GetVietlottMega645(null);
            }
            return View();
        }

        public ActionResult VietlottMax4D(string strDate = "")
        {
            DateTime date;
            if (!String.IsNullOrWhiteSpace(strDate) && DateTime.TryParseExact(strDate, "dd-mm-yyyy", System.Globalization.CultureInfo.InvariantCulture, System.Globalization.DateTimeStyles.None, out date))
            {
                ViewBag.Content = TraCuuOnlineHelper.GetVietlottMax4D(date);
            }
            else
            {
                ViewBag.Content = TraCuuOnlineHelper.GetVietlottMax4D(null);
            }
            return View("VietlottMega645");
        }

        //public ActionResult GiaOto()
        //{
        //    return Json(TraCuuOnlineHelper.GetGiaOto(), JsonRequestBehavior.AllowGet);
        //}

        public ActionResult GiaOto()
        {
            var data = TraCuuOnlineHelper.GetGiaOto();
            ViewBag.CacHang = System.Web.Helpers.Json.Encode(data.Select(p => p.HangXe).Distinct().OrderBy(p=>p).ToList());
            ViewBag.CacNguonGoc = System.Web.Helpers.Json.Encode(data.Select(p => p.NguonGoc).Distinct().OrderBy(p => p).ToList());
            ViewBag.CacLoai = System.Web.Helpers.Json.Encode(data.Select(p => p.LoaiXe).Distinct().OrderBy(p => p).ToList());
            ViewBag.Data = TraCuuOnlineHelper.GetOtoInfo(data);
            return View();
        }


        public ActionResult GetDetailOto(string url)
        {
            if (string.IsNullOrEmpty(url))
            {
                url = "https://vnexpress.net/interactive/2016/bang-gia-xe/sedan-cerato-1-6at-694.html";
            }
            return Json(new { data = TraCuuOnlineHelper.GetDetailOto(url) }, JsonRequestBehavior.AllowGet);
        }
    }
}

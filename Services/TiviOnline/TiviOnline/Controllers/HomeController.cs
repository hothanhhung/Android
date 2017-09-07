using hthservices.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using TiviOnline.Bussiness;
using TiviOnline.Models;

namespace TiviOnline.Controllers
{
    public class HomeController : Controller
    {
        //
        // GET: /Home/

        public ActionResult Update()
        {
            DataJsonProcess.ResetJsonData();
            return Content("Updated!");
        }

        public ActionResult Index(string channel = "VTV1", string id = "")
        {
            if (string.IsNullOrWhiteSpace(channel)) channel = "VTV1";
            var channelObject = DataJsonProcess.GetChannel(channel);
            var streamServers = DataJsonProcess.GetStreamServersOfChannel(channel);
            var streamServer = DataJsonProcess.GetStreamServer(id);
            if (streamServer == null && streamServers != null && streamServers.Count > 0)
            {
                streamServer = streamServers.First();
            }

            if (streamServer == null)
            {
                streamServer = new StreamServer();
            }
            if (channelObject == null)
            {
                channelObject = new Channel();
            }

            ViewBag.StreamServer = streamServer;
            if (streamServer.IsIframe)
            {
                ViewBag.StreamUrl = BussinessProcess.GetUrlStream(streamServer);
            }
            else
            {
                ViewBag.StreamUrl = BussinessProcess.GetUrlStream(streamServer);
            }
            ViewBag.StreamServers = streamServers;
            ViewBag.Channel = channelObject;
            //stop load video to work on UI
            //ViewBag.StreamUrl = "";
            ViewBag.Tab = 1;
            return View();
        }

        public ActionResult ChannelGroupsPartial()
        {
            ViewBag.HotChannels = DataJsonProcess.GetHotChannels();
            ViewBag.VTV_VTCChannels = DataJsonProcess.GetVTV_VTCChannels();
            ViewBag.SCTV_HTVChannels = DataJsonProcess.GetSCTV_HTVChannels();
            ViewBag.ForeignChannels = DataJsonProcess.GetForeignChannels();
            ViewBag.LocalChannels = DataJsonProcess.GetLocalChannels();
            ViewBag.FootballChannels = DataJsonProcess.GetFootballChannels();
            ViewBag.OthersChannels = DataJsonProcess.GetOthersChannels();
            return PartialView();
        }

        public ActionResult Schedule(string channel = "VTV1", string date = "")
        {
            string channelKey = "VTV1";
            DateTime dateOn = DateTime.Now;
            if (!String.IsNullOrWhiteSpace(channel))
            {
                channelKey = MethodHelpers.DecodeString(channel);
            }
            if (!String.IsNullOrWhiteSpace(date))
            {
                if (DateTime.TryParseExact(date, "dd-MM-yyyy", System.Globalization.CultureInfo.InvariantCulture, System.Globalization.DateTimeStyles.None, out dateOn)) ;
                else dateOn = DateTime.Now;
            }

            ViewBag.ScheduleItems = BussinessProcess.GetSchedule(channelKey, dateOn.ToString("dd-MM-yyyy", System.Globalization.CultureInfo.InvariantCulture));
            ViewBag.Channel = channelKey;
            ViewBag.Date = dateOn.ToString("dd-MM-yyyy", System.Globalization.CultureInfo.InvariantCulture);
            ViewBag.Tab = 2;
            return View();
        }

        public ActionResult Contact()
        {
            ViewBag.Tab = 3;
            return View();
        }

        public ActionResult GetContent(string url)
        {
            return Content(BussinessProcess.GetContent(url), "text/html");
        }

        public ActionResult Report(string channel = "", string id = "")
        {
            if (!string.IsNullOrWhiteSpace(channel) && !string.IsNullOrWhiteSpace(id))
            {
                if (MethodHelpers.SendEmail(string.Format("channel {0} with server {1}", channel, id)))
                {
                    return Content("Thanks");
                }
                else
                {
                    return Content("Error");
                }
            }
            return Content("No Data");
        }
    
    }
}
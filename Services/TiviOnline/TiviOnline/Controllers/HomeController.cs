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

        public ActionResult Update(){
            DataJsonProcess.ResetJsonData();
            return Content("Updated!");
        }

        public ActionResult Index(string channel = "VTV1", string id="")
        {
            if (string.IsNullOrWhiteSpace(channel)) channel = "VTV1";
            var channelObject = DataJsonProcess.GetChannel(channel);
            var streamServers = DataJsonProcess.GetStreamServersOfChannel(channel);
            var streamServer = DataJsonProcess.GetStreamServer(id);
            if (streamServer == null && streamServers!= null && streamServers.Count > 0)
            {
                streamServer = streamServers.First();
            }

            if (streamServer == null )
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
                ViewBag.StreamUrl = streamServer.URL;
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
            ViewBag.Tab = 2;
            return View();
        }

        public ActionResult Contact()
        {
            ViewBag.Tab = 3;
            return View();
        }
    }
}

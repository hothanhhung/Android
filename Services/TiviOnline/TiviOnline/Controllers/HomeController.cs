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

        public ActionResult Index(string channel = "VTV1", string id="")
        {
            if (string.IsNullOrWhiteSpace(channel)) channel = "VTV1";
            var streamServers = DataJsonProcess.GetStreamServersOfChannel(channel);
            var streamServer = DataJsonProcess.GetStreamServer(id);
            if (streamServer == null && streamServers!= null && streamServers.Count > 0)
            {
                streamServer = streamServers.First();
            }
            ViewBag.StreamUrl = BussinessProcess.GetUrlStream(streamServer);
            ViewBag.StreamServers = streamServers;

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

    }
}

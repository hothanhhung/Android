using hthservices.Utils;
using System;
using System.Web.Mvc;
using System.Collections.Generic;
using hthservices.Models;
using hthservices.Ads;

namespace hthservices.Controllers
{
    public class RetrieveDataController : Controller
    {
        public ActionResult GetSchedules(string channel, string date)
        {
            string channelKey = "VTV1";
            DateTime dateOn = DateTime.Now;
            if (!String.IsNullOrWhiteSpace(channel))
            {
                channelKey = MethodHelpers.DecodeString(channel);
            }
            if (!String.IsNullOrWhiteSpace(date))
            {
                if(DateTime.TryParseExact(date, "dd-MM-yyyy", System.Globalization.CultureInfo.InvariantCulture, System.Globalization.DateTimeStyles.None, out dateOn));
                else dateOn = DateTime.Now;
            }
            if(channelKey.StartsWith("K", StringComparison.OrdinalIgnoreCase))
            {
                if(channelKey.EndsWith("PM", StringComparison.OrdinalIgnoreCase)) channelKey = "K+PM";
                else if(channelKey.EndsWith("1", StringComparison.OrdinalIgnoreCase)) channelKey = "K+1";
                else if(channelKey.EndsWith("NS", StringComparison.OrdinalIgnoreCase)) channelKey = "K+NS";
                else if(channelKey.EndsWith("PC", StringComparison.OrdinalIgnoreCase)) channelKey = "K+PC";
            }
            else if(channelKey.Equals("HTVC ", StringComparison.OrdinalIgnoreCase))channelKey = "HTVC+";

            var channels = hthservices.Utils.DataProcess.GetSchedulesOfChannel(channelKey, dateOn);
            return Json(channels, JsonRequestBehavior.AllowGet);
        }

        public ActionResult GetChannels()
        {
            List<Object> jsonChannels = new List<object>();
            var channels = hthservices.Utils.DataProcess.GetAllChannels();
            foreach (var channel in channels)
            {
                var obj = new { ID = channel.ChannelKey, Name = channel.ChannelName, GroupName = channel.ChannelGroupName };
                jsonChannels.Add(obj);
            }
            return Json(jsonChannels, JsonRequestBehavior.AllowGet);
        }

        public ActionResult SearchProgram(string query, string group, string date)
        {
            if (string.IsNullOrWhiteSpace(query))
            {
                return Json(new List<SearchItem>(), JsonRequestBehavior.AllowGet);
            }

            DateTime selectedDate = DateTime.Now;
            int groupOrder = 0;
            if (!String.IsNullOrWhiteSpace(group))
            {
                Int32.TryParse(group, out groupOrder);
            }
            if (!String.IsNullOrWhiteSpace(date))
            {
                if (DateTime.TryParseExact(date, "dd-MM-yyyy", System.Globalization.CultureInfo.InvariantCulture, System.Globalization.DateTimeStyles.None, out selectedDate)) ;
                else selectedDate = DateTime.Now;
            }

            var searchItems = hthservices.Utils.DataProcess.SearchDataFromVietBaoUrl(query, groupOrder, selectedDate);
            return Json(searchItems, JsonRequestBehavior.AllowGet);
        }


        public ActionResult GetAds(string country, string os)
        {
            var adItems = Ads.AdData.GetAds(country, os);
            return Json(ResponseJson.GetResponseJson(adItems), JsonRequestBehavior.AllowGet);
        }
    }
}

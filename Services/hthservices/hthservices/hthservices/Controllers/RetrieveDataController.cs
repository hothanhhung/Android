using hthservices.Utils;
using System;
using System.Web.Mvc;
using System.Collections.Generic;

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
                dateOn = DateTime.ParseExact(date, "dd-MM-yyyy", System.Globalization.CultureInfo.InvariantCulture);
            } 

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
    }
}

using hthservices.Utils;
using System;
using System.Web.Mvc;
using System.Collections.Generic;
using hthservices.Models;
using hthservices.Ads;

namespace hthservices.Controllers
{
    public class LichTiviController : System.Web.Http.ApiController
    {
        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("GetSchedules")]
        public ResponseJson GetSchedules(string channel, string date, string device = "", string open = "", string version = "")
        {
            var ip = MethodHelpers.GetClientIp(Request);
            if (DenyInfo.IsDenyUserRequest(Request))
            {
                return ResponseJson.GetResponseJson(DenyInfo.DenyMessage);
            }

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

            var channels = hthservices.Utils.DataProcess.GetSchedulesOfChannel(channelKey, dateOn, Request.RequestUri.ToString(), device, ip, version);

            Random rand = new Random();
            channels.ForEach(p => p.ProgramName = EncrypeString.EncodeRandomly(p.ProgramName, rand.NextDouble()>0.5));
            return ResponseJson.GetResponseJson(channels);
        }

        public ResponseJson GetChannels()
        {
            List<Object> jsonChannels = new List<object>();
            var channels = hthservices.Utils.DataProcess.GetAllChannels();
            foreach (var channel in channels)
            {
                var obj = new { ID = channel.ChannelKey, Name = channel.ChannelName, GroupName = channel.ChannelGroupName };
                jsonChannels.Add(obj);
            }
            return ResponseJson.GetResponseJson(jsonChannels);
        }

        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("SearchProgram")]
        public ResponseJson SearchProgram(string query, string group, string date, string device = "", string open = "", string version = "")
        {
            if (string.IsNullOrWhiteSpace(query))
            {
                return ResponseJson.GetResponseJson(new List<SearchItem>());
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

            var searchItems = hthservices.Utils.DataProcess.SearchDataFromVietBaoUrl(query, groupOrder, selectedDate, Request.RequestUri.ToString());
            Random rand = new Random();
            searchItems.ForEach(p => p.ProgramName = EncrypeString.EncodeRandomly(p.ProgramName, rand.NextDouble() > 0.5));
            return ResponseJson.GetResponseJson(searchItems);
        }

    }
}

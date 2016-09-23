using hthservices.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace hthservices.Controllers
{
    public class AdsController : ApiController
    {
        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("GetAds")]
        public ResponseJson GetAds(string country, string os, string device = "", string open = "", string version = "", string package = "")
        {
            var adItems = Ads.AdData.GetAds(country, os, Request.RequestUri.ToString());
            return ResponseJson.GetResponseJson(adItems);
        }
    }
}

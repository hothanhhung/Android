﻿using hthservices.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Web.Http;

namespace hthservices.Controllers
{
    public class SolarLunarController : ApiController
    {
        [System.Web.Http.HttpGet]
        public HttpResponseMessage GetInfoDate(string date, int index)
        {
            return new HttpResponseMessage()
            {
                Content = new StringContent(ThapNhiBatTuInfo.GetThapNhiBatTu(index), Encoding.UTF8, "text/html")
            };
        }

        [System.Web.Http.HttpGet]
        public HttpResponseMessage GetInfoChamNgon(string date, int index)
        {
            return new HttpResponseMessage()
            {
                Content = new StringContent(ChamNgonInfo.GetThapNhiBatTuHTML(index), Encoding.UTF8, "text/html")
            };
         
        }

        [System.Web.Http.HttpGet]
        public string Reset()
        {
            ThapNhiBatTuInfo.Reset();
            ChamNgonInfo.Reset();
            return "Reset";
        }
    }
}

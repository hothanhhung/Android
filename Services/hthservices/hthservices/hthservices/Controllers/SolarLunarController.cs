using hthservices.Utils;
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
        [GZipOrDeflate]
        [System.Web.Http.HttpGet]
        public HttpResponseMessage GetInfoDate1(string date, int index)
        {
            return new HttpResponseMessage()
            {
                Content = new StringContent(ThapNhiBatTuInfo.GetThapNhiBatTu(index), Encoding.UTF8, "text/html")
            };
        }

        [System.Web.Http.HttpGet]
        public HttpResponseMessage GetInfoDate(string date, int index)
        {
            return new HttpResponseMessage()
            {
                Content = new StringContent(ThapNhiBatTuInfo.GetThapNhiBatTu(index), Encoding.UTF8, "text/html")
            };
        }

        [System.Web.Http.HttpGet]
        public HttpResponseMessage GetGoodDateBadDate(string date, int index)
        {

            return new HttpResponseMessage()
            {
                Content = new StringContent(ThapNhiBatTuInfo.GetThapNhiBatTu(index), Encoding.UTF8, "text/html")
            };
        }

        [System.Web.Http.HttpGet]
        public HttpResponseMessage GetGoodDateBadDateShort(string date, int index)
        {

            return new HttpResponseMessage()
            {
                Content = new StringContent(ThapNhiBatTuInfo.GetThapNhiBatTuShort(index), Encoding.UTF8, "text/html")
            };
        }

        [System.Web.Http.HttpGet]
        public HttpResponseMessage GetInfoChamNgon(string date, int index, int type = 0)
        {
            return new HttpResponseMessage()
            {
                Content = type == 0? new StringContent(ChamNgonInfo.GetThapNhiBatTuHTML(index), Encoding.UTF8, "text/html") 
                                    : new StringContent(ChamNgonInfo.GetThapNhiBatTuText(index), Encoding.UTF8, "text/html")
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

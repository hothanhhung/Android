using hthservices.Utils;
using System;
using System.Collections.Generic;
using System.Globalization;
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
            if (index < 0) index = DateTime.Now.DayOfYear;
            var dateTime = ConvertToDateTime(date);
            return new HttpResponseMessage()
            {
                Content = type == 0? new StringContent(ChamNgonInfo.GetThapNhiBatTuHTML(dateTime.HasValue? dateTime.Value.DayOfYear : index), Encoding.UTF8, "text/html") 
                                    : new StringContent(ChamNgonInfo.GetThapNhiBatTuText(dateTime.HasValue ? dateTime.Value.DayOfYear : index), Encoding.UTF8, "text/html")
            };
         
        }

        [System.Web.Http.HttpGet]
        public HttpResponseMessage GetTuVi(int days, int months)
        {
            return new HttpResponseMessage()
            {
                Content = new StringContent(CrawlTuViHelper.Crawl(days, months), Encoding.UTF8, "text/plain")
            };

        }

        [System.Web.Http.HttpGet]
        public string Reset()
        {
            ThapNhiBatTuInfo.Reset();
            ChamNgonInfo.Reset();
            return "Reset";
        }

        private DateTime? ConvertToDateTime(string value)
        {

            try {
                return DateTime.ParseExact(value, "yyyyMMdd", new CultureInfo("en-US"));
            }
            catch(Exception ex)
            {
                return null;
            }
        }
    }
}

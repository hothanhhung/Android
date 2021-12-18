using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using hthservices.Models;
using hthservices.Data;
using System.Web.Http;
using System.Web.Mvc;
using hthservices.Utils;

namespace hthservices.Controllers
{
    public class StockNewsController : ApiController
    {
        [System.Web.Http.HttpGet]
        public string Process()
        {
            var result = CrawlStockHelper.Crawl();
            return string.Empty;
        }

    }
}

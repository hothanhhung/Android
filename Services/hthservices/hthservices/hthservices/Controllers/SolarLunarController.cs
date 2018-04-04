using hthservices.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace hthservices.Controllers
{
    public class SolarLunarController : ApiController
    {
        public string GetInfoDate(string date, int index)
        {
            return ThapNhiBatTuInfo.GetThapNhiBatTu(index);
        }

        public string GetInfoChamNgon(string date, int index)
        {
            return ChamNgonInfo.GetThapNhiBatTuHTML(index);
        }

        public string Reset()
        {
            ThapNhiBatTuInfo.Reset();
            ChamNgonInfo.Reset();
            return "Reset";
        }
    }
}

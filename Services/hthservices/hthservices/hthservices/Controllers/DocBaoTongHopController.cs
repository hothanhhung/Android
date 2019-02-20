using hthservices.Utils;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace hthservices.Controllers
{
    public class DocBaoTongHopController : ApiController
    {
        private static ConfigAds configAds;

        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("zip")]
        public string zip(string str)
        {            
                var dataInByte = MethodHelpers.ZipStr(str);
                string data = MethodHelpers.ConvertByteArrayToString(dataInByte);
                return data;
            
        }

        [System.Web.Http.ActionName("unzip")]
        public string unzip(string str)
        {
            var dataInByte = MethodHelpers.ConvertStringToByteArray(str);
            var data = MethodHelpers.UnZipStr(dataInByte);
            return data;

        }

        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("GetConfigAds")]
        public string GetConfigAds(int version)
        {
            if(configAds == null)
            {
                configAds = GetConfigAdsData();
            }

            if(configAds.Version > version)
            {
                var config = JsonConvert.SerializeObject(configAds);
                var dataInByte = MethodHelpers.ZipStr(config);
                string data = MethodHelpers.ConvertByteArrayToString(dataInByte);
                return data;
            }
            return string.Empty;
        }

        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("ResetConfigAds")]
        public string ResetConfigAds()
        {
            configAds = GetConfigAdsData();
            return $"Reset: Version {configAds.Version}";
        }

        private ConfigAds GetConfigAdsData()
        {
            try
            {
                using (StreamReader r = new StreamReader(Utils.MethodHelpers.GetAbsolutePathToDataFolder() + "\\DocBaoTongHop.json"))
                {
                    string json = r.ReadToEnd();
                    var ro = JsonConvert.DeserializeObject<ConfigAds>(json);
                    return ro;
                }
            }
            catch (Exception ex)
            {

            }
            return new ConfigAds();
        }

        class ConfigAds
        {
            public int Version;
            public List<string> DenyLinks;
            public List<CSSWeb> CSSWebs;
        }

        class CSSWeb
        {
            public string Name { get; set; }
            public string CSS { get; set; }
        }
    }
}

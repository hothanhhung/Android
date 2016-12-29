using hthservices.Models;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Web;

namespace hthservices.Utils
{
    public class DenyInfo
    {
        class DeniedConfig{
            public List<string> DeniedIps { get; set; }
            public List<string> DeniedDeviceIds { get; set; }
            public List<string> AcceptedAppVersions { get; set; }
            public List<string> AffectedLinks { get; set; }
            public bool NeedCheckDeny(string link)
            {
                if (link == null) link = string.Empty;
                if (AffectedLinks != null)
                {
                    if (AffectedLinks.Any(p=> link.IndexOf(p, StringComparison.OrdinalIgnoreCase) != -1)) return true;
                }
                return false;
            }

            public bool IsDeniedIp(string ip)
            {
                if(DeniedIps != null)
                {
                    if(DeniedIps.Contains(ip == null? null : ip.Trim())) return true;
                }
                return false;
            }

            public bool IsDeniedDeviceId(string deviceId)
            {
                if (DeniedDeviceIds != null)
                {
                    if (DeniedDeviceIds.Contains(deviceId == null ? null : deviceId.Trim())) return true;
                }
                return false;
            }

            public bool IsDeniedAppVersion(string version)
            {
                if (AcceptedAppVersions != null)
                {
                    if (AcceptedAppVersions.Contains(version == null? null : version.Trim())) return false;
                    else return true;
                }
                return false;
            }
        }
        private static DeniedConfig DeniedConfigs
        {
            get
            {
                try
                {
                    using (StreamReader r = new StreamReader(MethodHelpers.GetAbsolutePathToDataFolder() + "\\DeniedConfig.json"))
                    {
                        string json = r.ReadToEnd();
                        DeniedConfig ro = JsonConvert.DeserializeObject<DeniedConfig>(json);
                        return ro;
                    }
                }
                catch (Exception ex)
                {

                }
                return new DeniedConfig();
            }
        }

        public static string DenyMessage = "Cảm ơn vì đã sử dụng ứng dụng này. Nhưng dường như bạn đang cố gắng phá hoại hệ thống nên chúng tôi không thể tiếp tục phục vụ bạn. Nếu có thắc mắc vui lòng liên hệ với chúng tôi theo địa chỉ email trên Play Store.";

        public static ResponseJson denyResponseMessage;
        public static ResponseJson DenyResponseMessage
        {
            get
            {
                if (denyResponseMessage == null)
                {
                    denyResponseMessage = hthservices.Models.ResponseJson.GetResponseJson(DenyInfo.DenyMessage);
                }
                return denyResponseMessage;
            }
        }
        public static bool IsDenyUserRequest(HttpRequestMessage request)
        {
            DeniedConfig deniedConfig = DeniedConfigs; // each request shouble be read ontime
            if (deniedConfig.NeedCheckDeny(request.RequestUri.AbsolutePath))
            {
                string ip = MethodHelpers.GetClientIp(request);
                string deviceId = HttpUtility.ParseQueryString(request.RequestUri.Query).Get("device");
                string appVersion = HttpUtility.ParseQueryString(request.RequestUri.Query).Get("version");
                return deniedConfig.IsDeniedIp(ip) || deniedConfig.IsDeniedDeviceId(deviceId) || deniedConfig.IsDeniedAppVersion(appVersion);
            }
            return false;
        }
    }
}
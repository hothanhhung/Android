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
        class ConnectedClientInfo
        {
            public string IP { get; set; }
            public int Count { get; set; }
            public DateTime Start { get; set; }
        }
        class DeniedConfig{
            private static List<ConnectedClientInfo> ConnectedClients = new List<ConnectedClientInfo>();
            public List<string> DeniedIps { get; set; }
            public List<string> DeniedDeviceIds { get; set; }
            public List<string> AcceptedAppVersions { get; set; }
            public List<string> AffectedLinks { get; set; }
            public const int MaxConnectedClient = 30;
            public bool UseMessage { get; set; }
            
            public bool IsTooMuchConnectedClient(string ip)
            {
                var now = DateTime.Now;
                ConnectedClients.RemoveAll(i => (now - i.Start).TotalHours > 24);

                ip = ip.Trim();
                var client = ConnectedClients.FirstOrDefault(p => p.IP.Equals(ip, StringComparison.OrdinalIgnoreCase));
                if(client != null)
                {
                    if((DateTime.Now - client.Start).TotalHours > 24)
                    {
                        client.Count = 1;
                        client.Start = DateTime.Now;
                    }
                    else
                    {
                        client.Count++;
                        if (client.Count > MaxConnectedClient)
                        {
                            return true;
                        }
                    }
                }
                else
                {
                    ConnectedClients.Add(new ConnectedClientInfo() { IP = ip, Count = 1, Start = DateTime.Now });
                }
                return false;
            }
            
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
        public static bool UseMessage;

        public static ResponseJson denyResponseMessage;
        public static ResponseJson DenyResponseMessage
        {
            get
            {
                if (UseMessage)
                {
                    denyResponseMessage = hthservices.Models.ResponseJson.GetResponseJson(DenyInfo.DenyMessage);
                }
                else
                {
                    {
                        denyResponseMessage = hthservices.Models.ResponseJson.GetResponseJson(DenyInfo.DenyObject);
                    }
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
                UseMessage = deniedConfig.UseMessage;
                return deniedConfig.IsTooMuchConnectedClient(ip) || deniedConfig.IsDeniedIp(ip) || deniedConfig.IsDeniedDeviceId(deviceId) || deniedConfig.IsDeniedAppVersion(appVersion);
            }
            return false;
        }

        public static List<GuideItem> DenyObject
        {
            get
            {
                var lst = new List<GuideItem>();
                lst.Add(new GuideItem() { ChannelKey = "VTV1", DateOn = "2016-12-23", StartOn = "00:00", ProgramName = "<i1 ựr jờiU" });
                lst.Add(new GuideItem() { ChannelKey = "VTV1", DateOn = "2016-12-23", StartOn = "01:00", ProgramName = "Thế giới góc nhìn" });
                lst.Add(new GuideItem() { ChannelKey = "VTV1", DateOn = "2016-12-23", StartOn = "01:20", ProgramName = "Văn hoá - Sự kiện và Nhân vật : Nhạc sĩ Trần Mạnh Hùng" });
                lst.Add(new GuideItem() { ChannelKey = "VTV1", DateOn = "2016-12-23", StartOn = "03:25", ProgramName = "Thời sự" });
                lst.Add(new GuideItem() { ChannelKey = "VTV1", DateOn = "2016-12-23", StartOn = "05:00", ProgramName = "<sậtis ệihz bói zău zbjh hzôiJ" });
                lst.Add(new GuideItem() { ChannelKey = "VTV1", DateOn = "2016-12-23", StartOn = "06:05", ProgramName = "Phát huy vai trò của mặt trận : Công giáo đồng hành cùng dân tộc" });
                lst.Add(new GuideItem() { ChannelKey = "VTV1", DateOn = "2016-12-23", StartOn = "07:30", ProgramName = "Phim tài liệu : U Minh một cõi đi về" });
                lst.Add(new GuideItem() { ChannelKey = "VTV1", DateOn = "2016-12-23", StartOn = "08:00", ProgramName = "Văn hoá - Sự kiện và Nhân vật : Nhạc sĩ Trần Mạnh Hùng" });
                lst.Add(new GuideItem() { ChannelKey = "VTV1", DateOn = "2016-12-23", StartOn = "10:20", ProgramName = "<abO sệjW - W" });
                lst.Add(new GuideItem() { ChannelKey = "VTV1", DateOn = "2016-12-23", StartOn = "13:20", ProgramName = "Tài chính - Tiêu dùng" });
                lst.Add(new GuideItem() { ChannelKey = "VTV1", DateOn = "2016-12-23", StartOn = "15:25", ProgramName = "<i32 ựr jờiU" });
                lst.Add(new GuideItem() { ChannelKey = "VTV1", DateOn = "2016-12-23", StartOn = "16:20", ProgramName = "Sáng tạo khởi nghiệp : Sáng tạo khởi nghiệp 40" });
                lst.Add(new GuideItem() { ChannelKey = "VTV1", DateOn = "2016-12-23", StartOn = "18:10", ProgramName = "Vì cộng đồng : Khi bác sỹ tăng cường về cơ sở" });
                lst.Add(new GuideItem() { ChannelKey = "VTV1", DateOn = "2016-12-23", StartOn = "20:20", ProgramName = "Văn học nghệ thuật" });
                lst.Add(new GuideItem() { ChannelKey = "VTV1", DateOn = "2016-12-23", StartOn = "22:35", ProgramName = "<i1 ựr jờiU" });
                lst.Add(new GuideItem() { ChannelKey = "VTV1", DateOn = "2016-12-23", StartOn = "23:20", ProgramName = "<dộs zâe hzùd izài hzồđ nájh hzôB : zậqs sặa bủd òqs jbu xti sáiQ" });
                return lst;
            }
        }
    }
}
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;

namespace hthservices.Utils
{
    public class DenyInfo
    {
        public static List<string> DENY_IPS
        {
            get
            {
                try
                {
                    using (StreamReader r = new StreamReader(MethodHelpers.GetAbsolutePathToDataFolder() + "\\DenyIPs.json"))
                    {
                        string json = r.ReadToEnd();
                        List<string> ro = JsonConvert.DeserializeObject<List<string>>(json);
                        return ro;
                    }
                }
                catch (Exception ex)
                {

                }
                return new List<string>();
            }
        }

        public static string DenyMessage = "Cảm ơn vì đã sử dụng ứng dụng này. Nhưng dường như bạn đang cố gắng phá hoại hệ thống nên chúng tôi không thể tiếp tục phục vụ bạn. Nếu có thắc mắc vui lòng liên hệ với chúng tôi theo địa chỉ email trên Play Store.";

        public static bool IsDenyUserIp(string ip)
        {
            if (DENY_IPS.Contains(ip.Trim())) return true;
            return false;
        }
    }
}
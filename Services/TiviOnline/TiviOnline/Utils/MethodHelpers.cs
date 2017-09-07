using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Mail;
using System.Text;
using System.Web;

namespace hthservices.Utils
{
    public class MethodHelpers
    {
        private static readonly string[] VietnameseSigns = new string[]
                                                            {
                                                                "aAeEoOuUiIdDyY",
                                                                "áàạảãâấầậẩẫăắằặẳẵ",
                                                                "ÁÀẠẢÃÂẤẦẬẨẪĂẮẰẶẲẴ",
                                                                "éèẹẻẽêếềệểễ",
                                                                "ÉÈẸẺẼÊẾỀỆỂỄ",
                                                                "óòọỏõôốồộổỗơớờợởỡ",
                                                                "ÓÒỌỎÕÔỐỒỘỔỖƠỚỜỢỞỠ",
                                                                "úùụủũưứừựửữ",
                                                                "ÚÙỤỦŨƯỨỪỰỬỮ",
                                                                "íìịỉĩ",
                                                                "ÍÌỊỈĨ",
                                                                "đ",
                                                                "Đ",
                                                                "ýỳỵỷỹ",
                                                                "ÝỲỴỶỸ"
                                                            };


        public static string RemoveSign4VietnameseString(string str)
        {
            if (String.IsNullOrWhiteSpace(str)) return str;

            //Tiến hành thay thế , lọc bỏ dấu cho chuỗi
            for (int i = 1; i < VietnameseSigns.Length; i++)
            {
                for (int j = 0; j < VietnameseSigns[i].Length; j++)
                {
                    str = str.Replace(VietnameseSigns[i][j].ToString(), VietnameseSigns[0][i - 1].ToString());
                }
            }
            for (int i = 0; i < str.Length; i++)
            {
                if (str[i] > 500)
                {
                    str = str.Remove(i, 1);
                    i--;
                }
            }
            return str;
        }

        public static string EncodeString(string str)
        {
            return System.Web.HttpUtility.HtmlEncode(str);
        }

        public static string DecodeString(string str)
        {
            return System.Web.HttpUtility.HtmlDecode(str);
        }

        public static string ConvertDateToCorrectString(DateTime date)
        {
            if (date != null)
            {
                return date.ToString("yyyy-MM-dd");
            }
            return string.Empty;
        }

        public static string ToTitleCase(string str)
        {
            return CultureInfo.CurrentCulture.TextInfo.ToTitleCase(str.ToLower());
        }
        public static DateTime GetVNCurrentDate()
        {
            return DateTime.UtcNow.AddHours(7);
        }


        private static string absolutePathToDataFolder;
        public static string GetAbsolutePathToDataFolder()
        {
            if (string.IsNullOrWhiteSpace(absolutePathToDataFolder))
            {
                if (HttpContext.Current == null)
                {
                    if (HttpRuntime.AppDomainAppPath.EndsWith("\\"))
                    {
                        absolutePathToDataFolder = HttpRuntime.AppDomainAppPath + "Data";
                    }
                    else
                    {
                        absolutePathToDataFolder = HttpRuntime.AppDomainAppPath + "\\Data";
                    }
                }
                else
                {
                    absolutePathToDataFolder = HttpContext.Current.Server.MapPath("~/Data");
                }
            }
            return absolutePathToDataFolder;

        }

        public static string GetClientIp(HttpRequestMessage request)
        {
            if (request.Properties.ContainsKey("MS_HttpContext"))
            {
                var ip = ((HttpContextWrapper)request.Properties["MS_HttpContext"]).Request.UserHostAddress;
                try
                {
                    if (ip != null)
                    {
                        return ip.Trim();
                    }
                }
                catch { }
            }

            return string.Empty;
        }

        public static string GetUrlToLog(HttpRequestMessage request)
        {
            return request.RequestUri.ToString() + "&ipUser=" + MethodHelpers.GetClientIp(request);
        }

        static public String DecodeProgramName(String str)
        {
            if (!string.IsNullOrWhiteSpace(str) && str.StartsWith("<"))
            {
                StringBuilder rs = new StringBuilder();
                char ch;
                for (int i = str.Length; i > 1; i--)
                {
                    if (str[i - 1] == '0')
                    {
                        ch = '5';
                    }
                    else if (str[i - 1] == '9')
                    {
                        ch = '6';
                    }
                    else if (str[i - 1] == 'a')
                    {
                        ch = 'm';
                    }
                    else if (str[i - 1] == 'z')
                    {
                        ch = 'n';
                    }
                    else if (str[i - 1] == 'M')
                    {
                        ch = 'A';
                    }
                    else if (str[i - 1] == 'N')
                    {
                        ch = 'Z';
                    }
                    else if (str[i - 1] > '0' && str[i - 1] <= '5')
                    {
                        ch = (char)(str[i - 1] - 1);
                    }
                    else if (str[i - 1] >= '6' && str[i - 1] < '9')
                    {
                        ch = (char)(str[i - 1] + 1);
                    }
                    else if (str[i - 1] > 'a' && str[i - 1] <= 'm')
                    {
                        ch = (char)(str[i - 1] - 1);
                    }
                    else if (str[i - 1] >= 'n' && str[i - 1] < 'z')
                    {
                        ch = (char)(str[i - 1] + 1);
                    }
                    else if (str[i - 1] >= 'A' && str[i - 1] < 'M')
                    {
                        ch = (char)(str[i - 1] + 1);
                    }
                    else if (str[i - 1] > 'N' && str[i - 1] <= 'Z')
                    {
                        ch = (char)(str[i - 1] - 1);
                    }
                    else
                    {
                        ch = str[i - 1];
                    }
                    rs.Append(ch);
                }

                return rs.ToString();
            }
            return str;
        }

        public static bool SendEmail(String content)
        {
            try
            {
                var fromAddress = new MailAddress("connit712@gmail.com", "TiviOnline");
                var toAddress = new MailAddress("connit712@gmail.com", "TiviOnline");
                const string fromPassword = "thanhhung";
                const string subject = "TiviOnline Report";

                var smtp = new SmtpClient
                {
                    Host = "smtp.gmail.com",
                    Port = 587,
                    EnableSsl = true,
                    DeliveryMethod = SmtpDeliveryMethod.Network,
                    UseDefaultCredentials = false,
                    Credentials = new NetworkCredential(fromAddress.Address, fromPassword),
                    Timeout = 20000
                };
                using (var message = new MailMessage(fromAddress, toAddress)
                {
                    Subject = subject,
                    Body = content
                })
                {
                    smtp.Send(message);
                }
            }
            catch (Exception ex)
            {
                return false;
            }
            return true;
        }
    }
}
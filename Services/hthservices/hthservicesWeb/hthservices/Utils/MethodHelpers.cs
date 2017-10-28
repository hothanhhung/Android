using HtmlAgilityPack;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.IO.Compression;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Web;
using System.Web.Mvc;

namespace hthservices.Utils
{
    public static class MethodHelpers
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

        public static string GetCurrentVNDateTimeInCorrectString()
        {
            var date = DateTime.UtcNow.AddHours(7);
            return date.ToString("yyyy-MM-dd HH:mm:ss");
        }

        public static bool IsEarlierToDay(string publishedDate, string updatedDate)
        {
            string dateToCompare = string.IsNullOrWhiteSpace(publishedDate) ? updatedDate : publishedDate;
            var date = DateTime.UtcNow.AddHours(7);
            return date.ToString("yyyy-MM-dd HH:mm:ss").CompareTo(dateToCompare) > 0;
        }
        public static string ConvertDateTimeToCorrectString(DateTime date)
        {
            if (date != null)
            {
                return date.ToString("yyyy-MM-dd HH:mm:ss");
            }
            return string.Empty;
        }

        public static DateTime? ConvertCorrectStringToDateTime(string str)
        {
            DateTime date;
            if (DateTime.TryParseExact(str, "yyyy-MM-dd HH:mm:ss", System.Globalization.CultureInfo.InvariantCulture, System.Globalization.DateTimeStyles.None, out date))
            {
                return date;
            }
            return null;
        }

        public static string ToTitleCase(string str)
        {
            return CultureInfo.CurrentCulture.TextInfo.ToTitleCase(str.ToLower());
        }
        public static DateTime GetVNCurrentDate()
        {
            return DateTime.UtcNow.AddHours(7);
        }
        public static string ToFriendlyUrl(this UrlHelper url, string urlToEncode)
        {
            urlToEncode = (urlToEncode ?? "").Trim().ToLower();
            urlToEncode = RemoveSign4VietnameseString(urlToEncode);
            urlToEncode = urlToEncode.Replace(" ", "-");
            urlToEncode = System.Text.RegularExpressions.Regex.Replace(urlToEncode, @"[^a-z0-9]", "-");
            return urlToEncode.ToString();
        }

        public static string GetShortTextFromHmlContent(this String content)
        {
            if (content == null) return string.Empty;
            HtmlDocument doc = new HtmlDocument();
            doc.LoadHtml(content);
            string text = doc.DocumentNode.InnerText;
            text = Regex.Replace(text, @"\s+", " ").Trim();
            return text == null ? string.Empty : (text.Count() > 400 ? text.Substring(0, 400)+"..." : text);
        }
        //public static string ToUnsign(this UrlHelper helper,
        //string urlToEncode)
        //{
        //    urlToEncode = (urlToEncode ?? "").Trim().ToLower();
        //    urlToEncode = UnicodeToNormal.ConvertToUnSign(urlToEncode);
        //    urlToEncode = urlToEncode.Replace("-", " ");
        //    return urlToEncode.ToString();
        //}
        //public static string ToPlainText(this UrlHelper helper,
        //string urlToEncode)
        //{
        //    urlToEncode = (urlToEncode ?? "").Trim().ToLower();
        //    //convert to plaintext
        //    //convert to unsign
        //    urlToEncode = UnicodeToNormal.ConvertToUnSign(urlToEncode);
        //    return urlToEncode.ToString();
        //}
        public static void CopyTo(Stream src, Stream dest)
        {
            byte[] bytes = new byte[4096];

            int cnt;

            while ((cnt = src.Read(bytes, 0, bytes.Length)) != 0)
            {
                dest.Write(bytes, 0, cnt);
            }
        }

        public static byte[] Zip(string str)
        {
            var bytes = Encoding.UTF8.GetBytes(str);

            using (var msi = new MemoryStream(bytes))
            using (var mso = new MemoryStream())
            {
                using (var gs = new GZipStream(mso, CompressionMode.Compress))
                {
                    //msi.CopyTo(gs);
                    CopyTo(msi, gs);
                }

                return mso.ToArray();
            }
        }

        public static string Unzip(byte[] bytes)
        {
            using (var msi = new MemoryStream(bytes))
            using (var mso = new MemoryStream())
            {
                using (var gs = new GZipStream(msi, CompressionMode.Decompress))
                {
                    //gs.CopyTo(mso);
                    CopyTo(gs, mso);
                }

                return Encoding.UTF8.GetString(mso.ToArray());
            }
        }

        public static List<int> ConvertToListIntFromString(String str, char split = ',')
        {
            List<int> result = new List<int>();
            if (!string.IsNullOrWhiteSpace(str))
            {

                var items = str.Split(split);
                foreach (var item in items)
                {
                    int value = 0;
                    if (int.TryParse(item, out value))
                    {
                        result.Add(value);
                    }
                }
            }
            return result.Distinct().ToList();
        }

        public static string ConvertByteArrayToString(byte[] data)
        {
            StringBuilder stringBuilder = new StringBuilder();
            if (data != null)
            {
                foreach (var value in data)
                {
                    stringBuilder.Append(" ").Append(value);
                }
            }
            return stringBuilder.ToString();
        }

        public static byte[] ConvertStringToByteArray(string str)
        {
            List<int> values = ConvertToListIntFromString(str, ' ');
            byte[] data = new byte[values.Count];
            if (values != null)
            {
                for (int i = 0; i < values.Count; i++ )
                {
                    data[i] = (byte) values[i];
                }
            }
            return data;
        }
    }
}
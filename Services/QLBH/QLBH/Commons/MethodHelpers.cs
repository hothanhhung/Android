using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Web;

namespace QLBH.Commons
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
            for (int i =0;i< str.Length; i++)
            {
                if (str[i] > 500)
                {
                    str = str.Remove(i, 1);
                    i--;
                }
            }
            return str;
        }

       
        public static string ConvertDateTimeToCorrectString(DateTime date)
        {
            if(date != null)
            {
                return date.ToString("yyyy-MM-dd HH:mm:ss");
            }
            return string.Empty;
        }

        public static DateTime GetVNCurrentDate()
        {
            return DateTime.UtcNow.AddHours(7);
        }

    }
}
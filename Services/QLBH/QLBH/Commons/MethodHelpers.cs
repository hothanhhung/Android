using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Web;
using System.Windows.Forms;

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

        public static string ConvertDateToCorrectString(DateTime date)
        {
            if (date != null)
            {
                return date.ToString("yyyy-MM-dd")+" 00:00:00";
            }
            return string.Empty;
        }

        public static DateTime ConvertStringDateTimeToDateTime(string strDate)
        {
            DateTime date = DateTime.Now;
            if (!string.IsNullOrWhiteSpace(strDate))
            {
                if(!DateTime.TryParseExact(strDate, "yyyy-MM-dd HH:mm:ss", CultureInfo.CurrentCulture, DateTimeStyles.None, out date))
                {
                    DateTime.TryParseExact(strDate, "yyyy-MM-dd", CultureInfo.CurrentCulture, DateTimeStyles.None, out date);
                }
            }
            return date;
        }

        public static DateTime GetVNCurrentDate()
        {
            return DateTime.UtcNow.AddHours(7);
        }

        public static void UpdateDateFieldsBasedOnQuickSelection(DateTimePicker dtMinDate, DateTimePicker dtMaxDate, int index)
        {
            DateTime now = DateTime.Now;
            switch (index + 1)
            {
                case 1:
                    dtMinDate.Value = now;
                    dtMaxDate.Value = now;
                    break;
                case 2:
                    dtMinDate.Value = now.AddDays(-1);
                    dtMaxDate.Value = now.AddDays(-1);
                    break;
                case 3:
                    dtMinDate.Value = now.AddDays(-7);
                    dtMaxDate.Value = now;
                    break;
                case 4:
                    dtMinDate.Value = now.AddDays(-(int)now.DayOfWeek);
                    dtMaxDate.Value = now;
                    break;
                case 5:
                    dtMinDate.Value = now.AddDays(-(int)now.DayOfWeek - 6);
                    dtMaxDate.Value = dtMinDate.Value.AddDays(6);
                    break;
                case 6:
                    dtMinDate.Value = new DateTime(now.Year, now.Month, 1);
                    dtMaxDate.Value = now;
                    break;
                case 7:
                    dtMinDate.Value = new DateTime(now.Year, now.Month, 1);
                    dtMaxDate.Value = (new DateTime(now.Year, now.Month + 1, 1)).AddDays(-1);
                    break;
                case 8:
                    dtMinDate.Value = new DateTime(now.Year, 1, 1);
                    dtMaxDate.Value = now;
                    break;
                case 9:
                    dtMinDate.Value = new DateTime(now.Year - 1, 1, 1);
                    dtMaxDate.Value = new DateTime(now.Year - 1, 12, 31);
                    break;
            }
        }

    }
}
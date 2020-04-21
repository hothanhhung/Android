using HtmlAgilityPack;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Linq;

namespace hthservices.Utils
{
    public static class CrawlTuViHelper
    {
        private static string GetTuViCungHoangDao(DateTime date)
        {
            string dateOfWeek = date.DayOfWeek == 0 ? "chu-nhat" : $"thu-{(int)date.DayOfWeek + 1}";
            string url = $"https://xemtuvi.mobi/tu-vi-ngay-moi/tu-vi-hang-ngay/xem-boi-tu-vi-{dateOfWeek}-ngay-{date.Day}-{date.Month}-{date.Year}-cua-12-cung-hoang-dao.html";
            return url;
        }

        private static string GetTuViConGiap(DateTime date)
        {
            string dateOfWeek = date.DayOfWeek == 0 ? "chu-nhat" : $"thu-{(int)date.DayOfWeek + 1}";
            string url = $"https://xemtuvi.mobi/tu-vi-ngay-moi/tu-vi-hang-ngay-12-con-giap/tu-vi-ngay-{date.Day}-{date.Month}-{date.Year}-{dateOfWeek}-cua-12-con-giap.html";
            return url;
        }

        private static List<string> CrawlXemTuVi(string url)
        {
            var result = new List<string>();
            try
            {
                HttpClient hc = new HttpClient();
                HttpResponseMessage response = hc.GetAsync(url).Result;

                var pageContents = response.Content.ReadAsStringAsync().Result;
                HtmlDocument pageDocument = new HtmlDocument();
                pageDocument.LoadHtml(pageContents);
                var contentNode = pageDocument.DocumentNode.SelectNodes("//div[@class='text-detail news_content fit-content']").FirstOrDefault();
                if (contentNode != null)
                {
                    bool removedUnuseItem = false;
                    var stringBuilder = new StringBuilder();
                    for (int i = 0; i < contentNode.ChildNodes.Count - 1; i++)
                    {
                        var item = contentNode.ChildNodes[i];
                        var style = item.GetAttributeValue("style", "empty");
                        if (style.Contains("text-align:center") || style.Contains("text-align:right")) continue;

                        if (item.Name.Equals("h2"))
                        {
                            if (stringBuilder.Length > 0)
                            {
                                result.Add(stringBuilder.ToString().Trim());
                                stringBuilder = new StringBuilder();
                            }
                            if (!removedUnuseItem)
                            {
                                removedUnuseItem = true;
                            }
                        }
                        else if (removedUnuseItem)
                        {
                            if (item.InnerText.Contains("---------------"))
                            {
                                removedUnuseItem = false;
                                continue;
                            }

                            if (item.Name.Equals("p") && !string.IsNullOrWhiteSpace(item.InnerText))
                            {
                                stringBuilder.Append("&emsp;" + item.InnerText.Replace('"', '“').Trim() + "<br/><br/>");
                            }
                            if (item.Name.Equals("ul"))
                            {
                                foreach (var li in item.ChildNodes)
                                {
                                    if (!string.IsNullOrWhiteSpace(li.InnerText))
                                    {
                                        stringBuilder.Append("&emsp;" + li.InnerText.Replace('"', '“').Trim() + "<br/><br/>");
                                    }
                                }

                            }
                        }
                    }
                    result.Add(stringBuilder.ToString().Trim());
                }
            }catch(Exception ex)
            {

            }

            return result;
        }

        private static string buildJson(List<string> congiap, List<string> cunghoangDao, string dateInString)
        {
            if (congiap.Count == 12 && cunghoangDao.Count == 12)
            {
                var strBuilder = new StringBuilder();
                strBuilder.AppendLine($"\"{dateInString}\": {{");

                strBuilder.AppendLine($"    \"Ty\":\"{congiap[0]}\",");
                strBuilder.AppendLine($"	\"Suu\":\"{congiap[1]}\",");
                strBuilder.AppendLine($"	\"Dan\":\"{congiap[2]}\",");
                strBuilder.AppendLine($"	\"Meo\":\"{congiap[3]}\",");
                strBuilder.AppendLine($"	\"Thin\":\"{congiap[4]}\",");
                strBuilder.AppendLine($"	\"Ti\":\"{congiap[5]}\",");
                strBuilder.AppendLine($"	\"Ngo\":\"{congiap[6]}\",");
                strBuilder.AppendLine($"	\"Mui\":\"{congiap[7]}\",");
                strBuilder.AppendLine($"	\"Than\":\"{congiap[8]}\",");
                strBuilder.AppendLine($"	\"Dau\":\"{congiap[9]}\",");
                strBuilder.AppendLine($"	\"Tuat\":\"{congiap[10]}\",");
                strBuilder.AppendLine($"	\"Hoi\":\"{congiap[11]}\",");
                strBuilder.AppendLine($"	\"BachDuong\":\"{cunghoangDao[0]}\",");
                strBuilder.AppendLine($"	\"KimNguu\":\"{cunghoangDao[1]}\",");
                strBuilder.AppendLine($"	\"SongTu\":\"{cunghoangDao[2]}\",");
                strBuilder.AppendLine($"	\"CuGiai\":\"{cunghoangDao[3]}\",");
                strBuilder.AppendLine($"	\"SuTu\":\"{cunghoangDao[4]}\",");
                strBuilder.AppendLine($"	\"XuNu\":\"{cunghoangDao[5]}\",");
                strBuilder.AppendLine($"	\"ThienBinh\":\"{cunghoangDao[6]}\",");
                strBuilder.AppendLine($"	\"BoCap\":\"{cunghoangDao[7]}\",");
                strBuilder.AppendLine($"	\"NhanMa\":\"{cunghoangDao[8]}\",");
                strBuilder.AppendLine($"	\"MaKet\":\"{cunghoangDao[9]}\",");
                strBuilder.AppendLine($"	\"BaoBinh\":\"{cunghoangDao[10]}\",");
                strBuilder.AppendLine($"	\"SongNgu\":\"{cunghoangDao[11]}\"");
                strBuilder.Append("    }");

                return strBuilder.ToString();
            }

            return $"\"{dateInString}\": {{\"Error\":true}}";
        }
        private static string CrawlTuVi(DateTime date)
        {
            var congiap = CrawlXemTuVi(GetTuViConGiap(date));
            var cunghoangDao = CrawlXemTuVi(GetTuViCungHoangDao(date));
            return buildJson(congiap, cunghoangDao, date.ToString("yyyyMMdd"));
        }

        private static string CrawlTuViBasedMonth(DateTime date)
        {
            var congiap = CrawlXemTuVi($"https://xemtuvi.mobi/tu-vi-hang-thang/tu-vi-thang-{date.Month}-{date.Year}-dong-phuong-cua-12-con-giap.html");
            var cunghoangDao = CrawlXemTuVi($"https://xemtuvi.mobi/tu-vi-hang-thang/tu-vi-thang-{date.Month}-{date.Year}-tay-phuong-cua-12-cung-hoang-dao.html");
            return buildJson(congiap, cunghoangDao, date.ToString("yyyyMM"));
        }

        public static string Crawl(int days, int month)
        {
            StringBuilder stringBuilder = new StringBuilder("{\n");
            var date = DateTime.UtcNow.AddHours(7);
            for (int i = 0; i < month; i++)
            {
                var data = CrawlTuViBasedMonth(date.AddMonths(i));
                stringBuilder.AppendLine($"{data},");
            }
            for (int i = 0; i < days; i++)
            {
                var data = CrawlTuVi(date.AddDays(i));
                stringBuilder.AppendLine($"{data},");
            }
            return stringBuilder.ToString().Trim().Trim(',')+"\n}";
        }
    }
}
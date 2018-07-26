using HtmlAgilityPack;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Web;

namespace hthservices.Utils
{
    public class TraCuuOnlineHelper
    {
        static public string GetVietlottMega645(DateTime? date)
        {
            string url = "http://iketqua.com/";
            if (date.HasValue)
            {
                url = String.Format("http://iketqua.com/ket-qua-so-xo-vietlott-mega-6-45/{0}", date.Value.ToString("dd-mm-yyyy"));
            }
            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                HttpClient http = new HttpClient();
                var response = http.GetByteArrayAsync(url).Result;
                String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                source = WebUtility.HtmlDecode(source);
                HtmlDocument resultat = new HtmlDocument();
                resultat.LoadHtml(source);
                if (date.HasValue)
                {
                    var content = resultat.DocumentNode.SelectNodes("//div[@id='main']").FirstOrDefault();
                    if (content != null)
                    {
                        return content.InnerHtml;
                    }
                }
                else
                {
                    var content = resultat.DocumentNode.SelectNodes("//div[@id='t_mega645']").FirstOrDefault();
                    if (content != null)
                    {
                        return content.OuterHtml;
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return "Không Tìm Thấy Dữ Liệu";
        }

        static public string GetVietlottMax4D(DateTime? date)
        {
            string url = "http://iketqua.com/";
            if (date.HasValue)
            {
                url = String.Format("http://iketqua.com/ket-qua-so-xo-vietlott-max-4d/{0}", date.Value.ToString("dd-mm-yyyy"));
            }
            List<GuideItem> guideItems = new List<GuideItem>();
            try
            {
                HttpClient http = new HttpClient();
                var response = http.GetByteArrayAsync(url).Result;
                String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                source = WebUtility.HtmlDecode(source);
                HtmlDocument resultat = new HtmlDocument();
                resultat.LoadHtml(source);
                if (date.HasValue)
                {
                    var content = resultat.DocumentNode.SelectNodes("//div[@id='main']").FirstOrDefault();
                    if (content != null)
                    {
                        return content.InnerHtml;
                    }
                }
                else
                {
                    var content = resultat.DocumentNode.SelectNodes("//div[@id='t_max4d']").FirstOrDefault();
                    if (content != null)
                    {
                        return content.OuterHtml;
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return "Không Tìm Thấy Dữ Liệu";
        }
    }
}
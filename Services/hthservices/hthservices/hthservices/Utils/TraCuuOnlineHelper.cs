using hthservices.Models;
using HtmlAgilityPack;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Reflection;
using System.Text;
using System.Web;
using System.Web.Helpers;

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

        static public List<GiaOto> GetGiaOto()
        {
            string url = "https://vnexpress.net/interactive/2016/bang-gia-xe#all;all";
            //if (date.HasValue)
            //{
            //    url = String.Format("http://iketqua.com/ket-qua-so-xo-vietlott-mega-6-45/{0}", date.Value.ToString("dd-mm-yyyy"));
            //}
            List<GiaOto> giaOtos = new List<GiaOto>();
            try
            {
                HttpClient http = new HttpClient();
                var response = http.GetByteArrayAsync(url).Result;
                String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                source = WebUtility.HtmlDecode(source);
                int start = source.IndexOf("listCarsByBrand") + 16;
                int length = source.IndexOf(";var listPriceChange=") - start;
                String jsonString = source.Substring(start, length);
                var data = Json.Decode(jsonString);

                var caHang = ((IEnumerable<string>)data.GetDynamicMemberNames()).ToList<String>();
                foreach (var hang in caHang)
                {
                    var item = data[hang];
                    var cacLoaiXe = ((IEnumerable<string>)item.GetDynamicMemberNames()).ToList<String>();
                    foreach(var loaiXe in cacLoaiXe)
                    {
                        var xe = item[loaiXe];
                        var giaOto = new GiaOto();
                        var pros = ((IEnumerable<string>)xe.GetDynamicMemberNames()).ToList<String>();
                        Type giaOtoType = typeof(GiaOto);
                        foreach (var pro in pros)
                        {
                            var value = xe[pro] as string;
                            var piInstance = giaOtoType.GetProperty(pro);
                            if (piInstance!=null)
                            {
                                piInstance.SetValue(giaOto, value);
                            }
                        }
                        giaOtos.Add(giaOto);
                    }
                }

                //HtmlDocument resultat = new HtmlDocument();
                //resultat.LoadHtml(source);
                //var content = resultat.DocumentNode.SelectNodes("//table[@id='top100']/table").FirstOrDefault();
                //if (content != null)
                //{
                //    var trs = content.SelectNodes("//tr");
                //    if (trs != null && trs.Count > 0)
                //    {
                //        foreach (var tr in trs) {
                //            var tds = tr.SelectNodes("//td");
                //            if (tds != null && tds.Count > 8)
                //            {
                //                var giaOto = new GiaOto()
                //                {
                //                    MauXe = tds[0].InnerText.Trim(),
                //                    HangXe = tds[1].InnerText.Trim(),
                //                    LoaiXe = tds[2].InnerText.Trim(),
                //                    NguonGoc = tds[3].InnerText.Trim(),
                //                    DongCo = tds[6].InnerText.Trim()
                //                };
                //                giaOto.GiaNiemYet = Int32.Parse(tds[4].InnerText.Trim());
                //                giaOto.GiaDamPhan = Int32.Parse(tds[5].InnerText.Trim());
                //                giaOto.CongSuat = Int32.Parse(tds[7].InnerText.Trim());
                //                giaOto.Momen = Int32.Parse(tds[8].InnerText.Trim());
                //                giaOtos.Add(giaOto);
                //            };
                //        }
                //    }
                //}
            }
            catch (Exception ex)
            {

            }

            return giaOtos;
        }

        static public String GetOtoInfo(List<GiaOto> data)
        {
            //var data = GetGiaOto();
            var str = Json.Encode(data);
            var otoInfo = Json.Decode<List<OtoInfo>>(str);
            return Json.Encode(otoInfo);
        }

        static public string GetDetailOto(string url)
        {
            try
            {
                HttpClient http = new HttpClient();
                var response = http.GetByteArrayAsync(url).Result;
                String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                source = WebUtility.HtmlDecode(source);
                HtmlDocument resultat = new HtmlDocument();
                resultat.LoadHtml(source);

                StringBuilder stringBuilder = new StringBuilder();
                var content = resultat.DocumentNode.SelectNodes("//div[@class='detail_info']").FirstOrDefault();
                if (content != null)
                {
                    var removeItem = content.SelectSingleNode("//div[@id='loadMore']");
                    if (removeItem != null)
                    {
                        removeItem.Remove();
                    }
                    removeItem = content.SelectSingleNode("//div[@id='loadLess']");
                    if (removeItem != null)
                    {
                        removeItem.Remove();
                    }
                    stringBuilder.Append(content.InnerHtml.Replace("none", "block"));
                }

                content = resultat.DocumentNode.SelectNodes("//div[@class='detail_info info_chi_phi']").LastOrDefault();
                if (content != null)
                {
                    stringBuilder.Append(content.InnerHtml);
                }

                int start = source.IndexOf("function changeLocation()");
                if (start > 0)
                {
                    int length = source.IndexOf("function getCookie", start) - start;

                    if (length > 0)
                    {
                        var js = source.Substring(start, length);
                        js.Replace("Seats=;", "");
                        stringBuilder.Append("<script type=\"text/javascript\">").Append(js).Append("</script>");
                    }
                }
                return stringBuilder.ToString();
            }
            catch (Exception ex)
            {

            }

            return string.Empty;
        }

    }
}
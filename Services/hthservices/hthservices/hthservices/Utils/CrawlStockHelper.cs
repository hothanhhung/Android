using HtmlAgilityPack;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using hthservices.Models;

namespace hthservices.Utils
{
    public static class CrawlStockHelper
    {
        private static List<StockNews> CrawlXemStock(string url)
        {
            var result = new List<StockNews>();
            try
            {
                HttpClient hc = new HttpClient();
                HttpResponseMessage response = hc.GetAsync(url).Result;

                var pageContents = response.Content.ReadAsStringAsync().Result;
                HtmlDocument pageDocument = new HtmlDocument();
                pageDocument.LoadHtml(pageContents);
                var contentNodes = pageDocument.DocumentNode.SelectNodes("//h3");
                if (contentNodes != null)
                {
                    bool removedUnuseItem = false;
                    var stringBuilder = new StringBuilder();
                    for (int i = 0; i < contentNodes.Count; i++)
                    {
                        var item = contentNodes[i].FirstChild;
                        if (!"a".Equals(item.Name)) continue;
                        var stockNews = new StockNews();
                        var title = item.GetAttributeValue("title", string.Empty);
                        var href = item.GetAttributeValue("href", string.Empty);
                        var style = item.GetAttributeValue("style", "empty");
                        if (style.Contains("text-align:center") || style.Contains("text-align:right")) continue;

                        
                        result.Add(stockNews);
                    }
                }
            }catch(Exception ex)
            {

            }

            return result;
        }


        public static List<StockNews> Crawl()
        {
            var result = CrawlXemStock("https://cafef.vn/timeline/31/trang-1.chn");
            return result;
        }
    }
}
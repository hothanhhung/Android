using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.Mvc;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using hthservices;
using hthservices.Controllers;
using hthservices.Utils;

namespace hthservices.Tests.Controllers
{
    [TestClass]
    public class HomeControllerTest
    {
        [TestMethod]
        public void Index()
        {
            // Arrange
            HomeController controller = new HomeController();

            // Act
            ViewResult result = controller.Index() as ViewResult;

            // Assert
            Assert.AreEqual("Modify this template to jump-start your ASP.NET MVC application.", result.ViewBag.Message);
        }

        [TestMethod]
        public void About()
        {
            // Arrange
            HomeController controller = new HomeController();

            // Act
            ViewResult result = controller.About() as ViewResult;

            // Assert
            Assert.IsNotNull(result);
        }

        [TestMethod]
        public void Contact()
        {
            // Arrange
            HomeController controller = new HomeController();

            // Act
            ViewResult result = controller.Contact() as ViewResult;

            // Assert
            Assert.IsNotNull(result);
        }

        [TestMethod]
        public void HtmlHelper()
        {
           // var list = hthservices.Utils.HtmlHelper.SearchDataFromVietBaoUrl("Phim", 1, DateTime.Now);
           // var cn = new Channel() { LinkVietBao = "/lich-phat-song/vtv1.html" };
           // var list = hthservices.Utils.HtmlHelper.GetDataFromVietBaoUrl(cn, DateTime.Now.AddDays(1));
          // var list = hthservices.Utils.DataProcess.GetSchedulesOfChannel("VTV1", DateTime.Now.AddDays(10));

            //var cn = new ChannelToServer() { Server = DataStatic.FROM_MYTIVI_PAGE, ChannelKey = "HTV7", Value = "19" };
            //var list = hthservices.Utils.HtmlHelper.GetDataFromMyTVUrl(cn, DateTime.Now);
            //var str = System.Text.RegularExpressions.Regex.Unescape("Ti\u1ebfp t\u1ee5c ch\u01b0\u01a1ng tr\u00ecnh"); ;
            //var cn = new ChannelToServer() { Server = DataStatic.FROM_VTC14_PAGE, ChannelKey = "VTC14", Value = "" };
            //var list = hthservices.Utils.HtmlHelper.GetVTC14Url(cn, DateTime.Now);
            var VN_Now = DateTime.UtcNow.AddHours(7);

            var cn = new ChannelToServer() { Server = DataStatic.FROM_KPLUS_PAGE, ChannelKey = "K+NS", Value = "7010", ExtraValue = "11" };
            var list = hthservices.Utils.HtmlHelper.GetDataFromKPlusUrl(cn, DateTime.Now.AddDays(1));
            //var list = hthservices.Utils.DataProcess.GetSchedulesOfChannel("K+1", DateTime.Now.AddDays(1),"");
           // var query = "đồng+tiền";
           // var enc = System.Web.HttpUtility.UrlEncode(query);
           // var res = hthservices.Utils.DataProcess.SearchDataFromVietBaoUrl(query, 11, DateTime.Now.AddDays(-1));
            
           // var url = "http://www.menneske.no/sudoku/eng/random.html?diff=1";
           // var diff = 1;
           // var res = hthservices.Sudoku.HtmlHelper.GenSudoku(diff);
            
        }
    }
}

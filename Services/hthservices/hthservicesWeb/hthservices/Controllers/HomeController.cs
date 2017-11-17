using hthservices.Utils;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Text;
using System.IO;
using System.Linq;
using System.Text;
using System.Web;
using System.Web.Mvc;

namespace hthservices.Controllers
{
    public class HomeController : Controller
    {
        public ActionResult Index()
        {
            return Redirect("Programming");
        }

        public ActionResult About()
        {
            ViewBag.Message = "Your app description page.";

            return View();
        }

        public class ContactModel
        {
            public string FullName { get; set; }
            public string PhoneNumber { get; set; }
            public string Email { get; set; }
            public string Content { get; set; }
            public bool IsEmpty { get { return string.IsNullOrWhiteSpace(FullName) || string.IsNullOrWhiteSpace(Email) || string.IsNullOrWhiteSpace(Content); } }
        }

        [ValidateInput(false)]
        public ActionResult Contact(ContactModel contact = null)
        {
            if (contact != null && !contact.IsEmpty)
            {
                SendEmailHelpers.SendEmail(contact.FullName, contact.PhoneNumber, contact.Email, contact.Content);
                ViewBag.Message = "Cảm bạn đã liên hệ, chúng tôi sẽ trả lời sớm nhất có thể.";
            }
            else
            {
                ViewBag.Message = string.Empty;
            }
            return View();
        }

        [System.Web.Http.Route("Common/GetImage/{text:string}")]
        public FileResult GenerateImage(string text)
        {
            var image = GenerateImage(text, 0);
            using (var ms = new MemoryStream())
            {
                image.Save(ms, System.Drawing.Imaging.ImageFormat.Png);

                return File(ms.ToArray(), "image/png");
            }
        }

        public FileResult Capcha()
        {
            var ran = new Random();
            var strBuilder = new StringBuilder();
            for(int i =0; i< 5; i++)
            {
                strBuilder.Append((char)ran.Next('A', 'Z'));
            }
            Session["Capcha"] = strBuilder.ToString();
            var image = GenerateImage(Session["Capcha"].ToString(), 1);
            using (var ms = new MemoryStream())
            {
                image.Save(ms, System.Drawing.Imaging.ImageFormat.Png);

                return File(ms.ToArray(), "image/png");
            }
        }

        private Bitmap GenerateImage(string text, int kind = 0)
        {
            int height = 250, width = 250;
            FontFamily fontFamily = new FontFamily("Arial");
            Color textColor = Color.DarkCyan;
            switch (kind)
            {
                case 0:
                    //Color.DarkCyan, Color.Brown, Color.DarkBlue, Color.DarkGreen, Color.Purple, Color.Navy, Color.DarkViolet, Color.DeepSkyBlue
                    var colors = new Color[] { Color.DarkCyan, Color.Brown, Color.DarkBlue, Color.DarkGreen, Color.Purple, Color.Navy, Color.DarkViolet, Color.DeepSkyBlue };
                    PrivateFontCollection pfc = new PrivateFontCollection();
                    pfc.AddFontFile(Server.MapPath("~/fonts/grease__.ttf"));
                    fontFamily = pfc.Families[0];
                    height = 250;
                    width = 250;
                    var ran = new Random();
                    textColor = colors[ran.Next(colors.Length)];
                    break;
                case 1:
                    height = 35;
                    width = 100;
                    break;
            }


            var image = MethodHelpers.ConvertTextToImage(text, fontFamily, Color.WhiteSmoke, textColor, width, height);
            return image;
        }
    }
}

using hthservices.Utils;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace hthservices.Controllers
{
    public class HomeController : Controller
    {
        public ActionResult Index()
        {
            return View();
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

        [System.Web.Http.Route("Common/GetImage")]
        public FileResult GenerateImage(string text)
        {
            var image = MethodHelpers.ConvertTextToImage(text, "Bookman Old Style", Color.WhiteSmoke, Color.Black, 250, 250);
            using (var ms = new MemoryStream())
            {
                image.Save(ms, System.Drawing.Imaging.ImageFormat.Png);

                return File(ms.ToArray(), "image/png");
            }
        }
    }
}

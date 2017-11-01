﻿using hthservices.Utils;
using System;
using System.Collections.Generic;
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

    }
}

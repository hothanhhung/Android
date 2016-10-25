using hthservices.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace hthservices.Controllers
{
    public class ProgrammingController : Controller
    {
        public ActionResult Index()
        {
            ViewBag.Contents = DataBusiness.DataProcess.GetProgrammingContentsForUser(null);
            return View();
        }

        public ActionResult Category(int? id)
        {
            ViewBag.Contents = DataBusiness.DataProcess.GetProgrammingContentsForUser(id);

            return View();
        }

        public ActionResult Content(int id)
        {

            var content = DataBusiness.DataProcess.GetProgrammingContentForUser(id);
            if (content != null)
            {
                ViewBag.Content = content;
                ViewBag.RelatedContents = DataBusiness.DataProcess.GetProgrammingContentsForUser(content.CategoryId).Where(p => p.Id != content.Id).Take(8).ToList();
            }
            return View();
        }

        public ActionResult RightProgrammingPartial()
        {
            ViewBag.Categories = DataBusiness.DataProcess.GetProgrammingCategoriesForUser();
            ViewBag.CurrentComments = DataBusiness.DataProcess.GetProgrammingCurrentCommentsForUser();
            return PartialView();
        }
    }
}

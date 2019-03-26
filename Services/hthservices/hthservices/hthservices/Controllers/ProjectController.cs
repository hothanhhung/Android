using hthservices.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;

namespace hthservices.Controllers
{
    public class ProjectController : Controller
    {
        public ActionResult Index(int page = 1, int size = 20)
        {
            ViewBag.Contents = DataBusiness.DataProcess.GetProjectsForUser(page - 1, size);
            ViewBag.TotalPage = Math.Ceiling(1.0 * DataBusiness.DataProcess.CountProjectsForUser() / size);
            ViewBag.CurrentPage = page;
            ViewBag.PageSize = size;
            return View();
        }

        public ActionResult SeeContent(string id)
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

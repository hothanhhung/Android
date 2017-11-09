using hthservices.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;

namespace hthservices.Controllers
{
    public class ProgrammingController : Controller
    {
        public ActionResult Index(int page = 1, int size = 5)
        {
            ViewBag.Contents = DataBusiness.DataProcess.GetProgrammingContentsForUser(null, page - 1, size);
            ViewBag.TotalPage = Math.Ceiling(1.0 * DataBusiness.DataProcess.CountProgrammingContentsForUser(null) / size);
            ViewBag.CurrentPage = page;
            ViewBag.PageSize = size;
            return View();
        }

        public ActionResult Category(string id, int page = 1, int size = 5)
        {
            ViewBag.Contents = DataBusiness.DataProcess.GetProgrammingContentsForUser(id, page - 1, size);
            ViewBag.TotalPage = Math.Ceiling(1.0*DataBusiness.DataProcess.CountProgrammingContentsForUser(id) / size);
            if (!string.IsNullOrWhiteSpace(id))
            {
                ViewBag.Category = DataBusiness.DataProcess.GetProgrammingCategory(id);
            }
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
                ViewBag.RelatedContents = DataBusiness.DataProcess.GetProgrammingContentsForUser(content.CategoryId).Where(p => p.Id != content.Id).Take(4).ToList();
            }
            return View();
        }

        public ActionResult RightProgrammingPartial()
        {
            ViewBag.Categories = DataBusiness.DataProcess.GetProgrammingCategoriesForUser();
            ViewBag.TopNews10ProgrammingContents = DataBusiness.DataProcess.GetTopNews10ProgrammingContentsForUser();
            ViewBag.CurrentComments = DataBusiness.DataProcess.GetProgrammingCurrentCommentsForUser();
            return PartialView();
        }
    }
}

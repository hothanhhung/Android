using hthservices.Models;
using hthservices.Models.Website;
using hthservices.Utils;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
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
        [NotMapped]
        public class ProgrammingCommentModel : ProgrammingComment
        {
            public string Capcha { get; set; }
        }

        [System.Web.Http.HttpPost, ValidateInput(false)]
        [System.Web.Http.ActionName("SaveComment")]
        public JsonResult SaveComment([FromBody] ProgrammingCommentModel comment)
        {
           
            var token = comment.Capcha;
            if (token == null || token.Count() == 0 || !token.Equals(Session["Capcha"]))
            {
                return Json(ResponseJson.GetResponseJson("Capcha Không Đúng", false));
            }

            if (string.IsNullOrWhiteSpace(comment.Name))
            {
                return Json(ResponseJson.GetResponseJson("Chưa nhập tên", false));
            }

            if (string.IsNullOrWhiteSpace(comment.Email))
            {
                return Json(ResponseJson.GetResponseJson("Chưa nhập email", false));
            }

            var emailFormat = new System.ComponentModel.DataAnnotations.EmailAddressAttribute();
            if (!emailFormat.IsValid(comment.Email))
            {
                return Json(ResponseJson.GetResponseJson("Email không đúng định dạng", false));
            }
            if (string.IsNullOrWhiteSpace(comment.Message))
            {
                return Json(ResponseJson.GetResponseJson("Chưa nhập nội dung", false));
            }

            bool success = false;
            if (comment != null)
            {
                var com = new ProgrammingComment()
                {
                    ContentId = comment.ContentId,
                    Email = comment.Email,
                    IsDisplay = 1,
                    Name = comment.Name,
                    Subject = comment.Subject,
                    Message = comment.Message
                };
                success = DataBusiness.DataProcess.SaveProgrammingComment(com);
            }
            var resultObject = new { IsSuccess = true, Message = "SaveComment" };
            return Json(ResponseJson.GetResponseJson(resultObject));
        }
    }
}

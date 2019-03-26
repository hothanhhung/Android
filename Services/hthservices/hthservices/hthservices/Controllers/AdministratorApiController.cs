using hthservices.Data;
using hthservices.DataBusiness;
using hthservices.Models;
using hthservices.Models.Website;
using MethodHelpers = hthservices.Utils.MethodHelpers;
using System.Linq;
using System.Web.Http;
using System.Net.Http;
using System.Web.Http.Filters;

namespace hthservices.Controllers
{
    public class AuthFilterAttribute : ActionFilterAttribute
    {
        bool needCheckLogin = true;
        public override void OnActionExecuting(System.Web.Http.Controllers.HttpActionContext httpContext)
        {
            if (needCheckLogin)
            {
                if (!"localhost".Equals(httpContext.Request.RequestUri.Host, System.StringComparison.OrdinalIgnoreCase))
                {
                    var token = httpContext.Request.Headers.GetValues("token");
                    if (token == null || token.Count() == 0 || AuthData.GetRole(token.ElementAt(0)) == Role.NoLogin)
                    {
                        httpContext.Response = httpContext.Request.CreateResponse(
                                                    System.Net.HttpStatusCode.OK,
                                                     ResponseJson.GetResponseJsonForError("Chưa Login"),
                                                    httpContext.ControllerContext.Configuration.Formatters.JsonFormatter
                                                );
                    }
                }
            }
        }
    }


    [AuthFilter]
    public class AdministratorApiController : ApiController
    {
        bool needCheckLogin = true;

        #region Category
        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("GetCategories")]
        public ResponseJson GetCategories()
        {
            var data = DataProcess.GetProgrammingCategories()
                .Select(x=> new ProgrammingCategory {
                    Id=x.Id,
                    IsDisplay=x.IsDisplay,
                    Name=x.Name,
                    Note=x.Note,
                    NumberOfAccess=x.NumberOfAccess,
                    Sort = x.Sort,
                    NumberOfContents = x.NumberOfContents
            }).ToList();
            return ResponseJson.GetResponseJson(data);
        }

        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("GetCategory")]
        public ResponseJson GetCategory(string categoryId)
        {
            
            var data = DataProcess.GetProgrammingCategory(categoryId);
            if(data != null)
            {
                data = new ProgrammingCategory()
                {
                    Id = data.Id,
                    IsDisplay = data.IsDisplay,
                    Name = data.Name,
                    Note = data.Note,
                    NumberOfAccess = data.NumberOfAccess,
                    Sort = data.Sort,
                    NumberOfContents = data.NumberOfContents
                };
            }
            return ResponseJson.GetResponseJson(data);
        }

        [System.Web.Http.HttpPost]
        [System.Web.Http.ActionName("SaveCategory")]
        public ResponseJson SaveCategory([FromBody] ProgrammingCategory category)
        {
            bool success = false;
            if (category != null)
            {
                success = DataProcess.SaveProgrammingCategory(category);
            }
            var resultObject = new { IsSuccess = true, Message = "SaveCategory" };
            return ResponseJson.GetResponseJson(resultObject);
        }
        
        [System.Web.Http.HttpPut]
        [System.Web.Http.ActionName("SaveCategory")]
        public ResponseJson SaveCategory1([FromBody] ProgrammingCategory category)
        {
            bool success = false;
            if (category != null)
            {
                success = DataProcess.SaveProgrammingCategory(category);
            }
            var resultObject = new { IsSuccess = true, Message = "SaveCategory" };
            return ResponseJson.GetResponseJson(resultObject);
        }

        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("DeleteCategory")]
        public ResponseJson DeleteCategory(string categoryId)
        {
            bool success = DataProcess.DeleteProgrammingCategory(categoryId);

            var resultObject = new { IsSuccess = true, Message = "SaveCategory" };
            return ResponseJson.GetResponseJson(resultObject);
        }
        #endregion

        #region Content

        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("GetContentsInString")]
        public ResponseJson GetContents(string contentIds)
        {
            var contentIdList = MethodHelpers.ConvertToListStringFromString(contentIds);

            var contents = DataProcess.GetProgrammingContents(contentIdList);
            var dataInByte = MethodHelpers.ZipStr(contents);
            string data = MethodHelpers.ConvertByteArrayToString(dataInByte);
            return ResponseJson.GetResponseJson(data);
        }
        public class SaveModel
        {
            public string Contents { get; set; }
        }
        [System.Web.Http.HttpPost]
        [System.Web.Http.ActionName("SaveContentsFromString")]
        public ResponseJson SaveContents([FromBody] SaveModel saveModel)
        {
            var dataInByte = MethodHelpers.ConvertStringToByteArray(saveModel.Contents);
            var contentsInString = MethodHelpers.UnZipStr(dataInByte);

            var count = DataProcess.UpdateProgrammingContents(contentsInString);
            string data = string.Format("Updated for {0}", count);
            return ResponseJson.GetResponseJson(data);
        }

        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("GetContents")]
        public ResponseJson GetContents(string categoryId = null, int page = 0, int size = 10)
        {
            var data = DataProcess.GetProgrammingContents(categoryId, page, size)
                .Select(x => new ProgrammingContent
                {
                    Id = x.Id,
                    Title = x.Title,
                    CategoryId = x.CategoryId,
                    IsDisplay = x.IsDisplay,
                    ImageUrl = x.ImageUrl,
                    ShortContent = x.ShortContent,
                    Content = x.Content,
                    NumberOfViews = x.NumberOfViews,
                    CreatedDate = x.CreatedDate,
                    PublishedDate = x.PublishedDate,
                    UpdatedDate = x.UpdatedDate,
                    Keywords = x.Keywords,
                    Subject = x.Subject,
                    CategoryName = x.Category == null ? "" : x.Category.Name,
                    NumberOfComments = x.Comments.Count
                }).ToList();
            return ResponseJson.GetResponseJson(data);
        }

        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("GetContent")]
        public ResponseJson GetContent(string contentId)
        {
            var data = DataProcess.GetProgrammingContent(contentId);
            if (data != null)
            {
                data = new ProgrammingContent()
                {
                    Id = data.Id,
                    Title = data.Title,
                    CategoryId = data.CategoryId,
                    IsDisplay = data.IsDisplay,
                    ImageUrl = data.ImageUrl,
                    ShortContent = data.ShortContent,
                    Content = data.Content,
                    NumberOfViews = data.NumberOfViews,
                    CreatedDate = data.CreatedDate,
                    PublishedDate = data.PublishedDate,
                    UpdatedDate = data.UpdatedDate,
                    Keywords = data.Keywords,
                    Subject = data.Subject,
                    CategoryName = data.Category == null ? "" : data.Category.Name,
                    NumberOfComments = data.Comments.Count        
                };
            }
            return ResponseJson.GetResponseJson(data);
        }

        [System.Web.Http.HttpPost]
        [System.Web.Http.ActionName("SaveContent")]
        public ResponseJson SaveContent([FromBody] ProgrammingContent content)
        {
            bool success = false;
            if (content != null)
            {
                success = DataProcess.SaveProgrammingContent(content);
            }
            var resultObject = new { IsSuccess = true, Message = "SaveContent" };
            return ResponseJson.GetResponseJson(resultObject);
        }

        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("DeleteContent")]
        public ResponseJson DeleteContent(string contentId)
        {
            bool success = DataProcess.DeleteProgrammingContent(contentId);

            var resultObject = new { IsSuccess = true, Message = "DeleteContent" };
            return ResponseJson.GetResponseJson(resultObject);
        }
        #endregion

        #region Comment
        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("GetComments")]
        public ResponseJson GetComments(string contentId = null, int page = 0, int size = 10)
        {

            var data = DataProcess.GetProgrammingComments(contentId, page, size)
                .Select(x => new ProgrammingComment
                {
                    Id = x.Id,
                    Name = x.Name,
                    Email = x.Email,
                    Subject = x.Subject,
                    Message = x.Message,
                    IsDisplay = x.IsDisplay,
                    ContentId = x.ContentId,
                    CreatedDate = x.CreatedDate,
                    UpdatedDate = x.UpdatedDate,
                }).ToList();
            return ResponseJson.GetResponseJson(data);
        }

        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("GetCommentsInString")]
        public ResponseJson GetComments(string commentIds)
        {
            var contentIdList = MethodHelpers.ConvertToListStringFromString(commentIds);

            var contents = DataProcess.GetProgrammingComments(contentIdList);
            var dataInByte = MethodHelpers.ZipStr(contents);
            string data = MethodHelpers.ConvertByteArrayToString(dataInByte);
            return ResponseJson.GetResponseJson(data);
        }

        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("SaveCommentsFromString")]
        public ResponseJson SaveComments(string comments)
        {
            var dataInByte = MethodHelpers.ConvertStringToByteArray(comments);
            var contents = MethodHelpers.UnZipStr(dataInByte);

            var count = DataProcess.UpdateProgrammingComments(contents);
            string data = string.Format("Updated for {0}", count);
            return ResponseJson.GetResponseJson(data);
        }

        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("GetComment")]
        public ResponseJson GetComment(string commentId)
        {
            var data = DataProcess.GetProgrammingComment(commentId);
            if (data != null)
            {
                data = new ProgrammingComment()
                {
                    Id = data.Id,
                    Name = data.Name,
                    Email = data.Email,
                    Subject = data.Subject,
                    Message = data.Message,
                    IsDisplay = data.IsDisplay,
                    ContentId = data.ContentId,
                    CreatedDate = data.CreatedDate,
                    UpdatedDate = data.UpdatedDate,
                };
            }
            return ResponseJson.GetResponseJson(data);
        }

        [System.Web.Http.HttpPost]
        [System.Web.Http.ActionName("SaveComment")]
        public ResponseJson SaveComment([FromBody] ProgrammingComment comment)
        {

            bool success = false;
            if (comment != null)
            {
                success = DataProcess.SaveProgrammingComment(comment);
            }
            var resultObject = new { IsSuccess = true, Message = "SaveComment" };
            return ResponseJson.GetResponseJson(resultObject);
        }

        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("DeleteComment")]
        public ResponseJson DeleteComment(string commentId)
        {
            bool success = DataProcess.DeleteProgrammingComment(commentId);

            var resultObject = new { IsSuccess = true, Message = "DeleteComment" };
            return ResponseJson.GetResponseJson(resultObject);
        }
        #endregion

        #region Project
        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("GetProjects")]
        public ResponseJson GetProjects(int page = 0, int size = 10)
        {
            var data = DataProcess.GetProjects(page, size)
                .Select(x => new Project
                {
                    Id = x.Id,
                    Title = x.Title,
                    Technical = x.Technical,
                    IsDisplay = x.IsDisplay,
                    ImageUrl = x.ImageUrl,
                    ShortContent = x.ShortContent,
                    Content = x.Content,
                    CreatedDate = x.CreatedDate,
                    UpdatedDate = x.UpdatedDate
                }).ToList();
            return ResponseJson.GetResponseJson(data);
        }

        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("GetProject")]
        public ResponseJson GetProject(string contentId)
        {

            var data = DataProcess.GetProject(contentId);
            if (data != null)
            {
                data = new Project()
                {
                    Id = data.Id,
                    Title = data.Title,
                    Technical = data.Technical,
                    IsDisplay = data.IsDisplay,
                    ImageUrl = data.ImageUrl,
                    ShortContent = data.ShortContent,
                    Content = data.Content,
                    CreatedDate = data.CreatedDate,
                    UpdatedDate = data.UpdatedDate
                };
            }
            return ResponseJson.GetResponseJson(data);
        }

        [System.Web.Http.HttpPost]
        [System.Web.Http.ActionName("SaveProject")]
        public ResponseJson SaveProject([FromBody] Project content)
        {

            bool success = false;
            if (content != null)
            {
                success = DataProcess.SaveProject(content);
            }
            var resultObject = new { IsSuccess = true, Message = "SaveProject" };
            return ResponseJson.GetResponseJson(resultObject);
        }

        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("DeleteProject")]
        public ResponseJson DeleteProject(string contentId)
        {
            bool success = DataProcess.DeleteProject(contentId);

            var resultObject = new { IsSuccess = true, Message = "DeleteProject" };
            return ResponseJson.GetResponseJson(resultObject);
        }
        #endregion
    }
}
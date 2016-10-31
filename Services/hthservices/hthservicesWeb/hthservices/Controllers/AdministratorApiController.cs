﻿using hthservices.Data;
using hthservices.DataBusiness;
using hthservices.Models;
using hthservices.Models.Website;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;

namespace hthservices.Controllers
{
    public class AdministratorApiController : ApiController
    {
        bool needCheckLogin = false;

        #region Category
        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("GetCategories")]
        public ResponseJson GetCategories()
        {
            if (needCheckLogin)
            {
                var token = Request.Headers.GetValues("token");
                if (token == null || AuthData.GetRole(token.ToString()) == Role.NoLogin)
                {
                    return ResponseJson.GetResponseJson(string.Empty, false);
                }
            }

            var data = DataProcess.GetProgrammingCategories()
                .Select(x=> new ProgrammingCategory {
                    Id=x.Id,
                    IsDisplay=x.IsDisplay,
                    Name=x.Name,
                    Note=x.Note,
                    NumberOfAccess=x.NumberOfAccess,
                    Sort = x.Sort,
                    NumberOfContents = x.Contents.Count
            }).ToList();
            return ResponseJson.GetResponseJson(data);
        }

        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("GetCategory")]
        public ResponseJson GetCategory(int categoryId)
        {
            if (needCheckLogin)
            {
                var token = Request.Headers.GetValues("token");
                if (token == null || AuthData.GetRole(token.ToString()) == Role.NoLogin)
                {
                    return ResponseJson.GetResponseJson(string.Empty, false);
                }
            }

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
                    NumberOfContents = data.Contents.Count
                };
            }
            return ResponseJson.GetResponseJson(data);
        }

        [System.Web.Http.HttpPost]
        [System.Web.Http.ActionName("SaveCategory")]
        public ResponseJson SaveCategory([FromBody] ProgrammingCategory category)
        {
            if (needCheckLogin)
            {
                var token = Request.Headers.GetValues("token");
                if (token == null || AuthData.GetRole(token.ToString()) == Role.NoLogin)
                {
                    return ResponseJson.GetResponseJson(string.Empty, false);
                }
            }

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
            if (needCheckLogin)
            {
                var token = Request.Headers.GetValues("token");
                if (token == null || AuthData.GetRole(token.ToString()) == Role.NoLogin)
                {
                    return ResponseJson.GetResponseJson(string.Empty, false);
                }
            }

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
        public ResponseJson DeleteCategory(int categoryId)
        {
            if (needCheckLogin)
            {
                var token = Request.Headers.GetValues("token");
                if (token == null || AuthData.GetRole(token.ToString()) == Role.NoLogin)
                {
                    return ResponseJson.GetResponseJson(string.Empty, false);
                }
            }

            bool success = DataProcess.DeleteProgrammingCategory(categoryId);

            var resultObject = new { IsSuccess = true, Message = "SaveCategory" };
            return ResponseJson.GetResponseJson(resultObject);
        }
        #endregion

        #region Content
        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("GetContents")]
        public ResponseJson GetContents(int? categoryId, int page = 0, int size = 10)
        {
            if (needCheckLogin)
            {
                var token = Request.Headers.GetValues("token");
                if (token == null || AuthData.GetRole(token.ToString()) == Role.NoLogin)
                {
                    return ResponseJson.GetResponseJson(string.Empty, false);
                }
            }

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
                    UpdatedDate = x.UpdatedDate,
                    CategoryName = x.Category == null ? "" : x.Category.Name,
                    NumberOfComments = x.Comments.Count
                }).ToList();
            return ResponseJson.GetResponseJson(data);
        }

        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("GetContent")]
        public ResponseJson GetContent(int contentId)
        {
            if (needCheckLogin)
            {
                var token = Request.Headers.GetValues("token");
                if (token == null || AuthData.GetRole(token.ToString()) == Role.NoLogin)
                {
                    return ResponseJson.GetResponseJson(string.Empty, false);
                }
            }

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
                    UpdatedDate = data.UpdatedDate,
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
            if (needCheckLogin)
            {
                var token = Request.Headers.GetValues("token");
                if (token == null || AuthData.GetRole(token.ToString()) == Role.NoLogin)
                {
                    return ResponseJson.GetResponseJson(string.Empty, false);
                }
            }

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
        public ResponseJson DeleteContent(int contentId)
        {
            if (needCheckLogin)
            {
                var token = Request.Headers.GetValues("token");
                if (token == null || AuthData.GetRole(token.ToString()) == Role.NoLogin)
                {
                    return ResponseJson.GetResponseJson(string.Empty, false);
                }
            }

            bool success = DataProcess.DeleteProgrammingContent(contentId);

            var resultObject = new { IsSuccess = true, Message = "DeleteContent" };
            return ResponseJson.GetResponseJson(resultObject);
        }
        #endregion

        #region Comment
        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("GetComments")]
        public ResponseJson GetComments(int? contentId, int page = 0, int size = 10)
        {
            if (needCheckLogin)
            {
                var token = Request.Headers.GetValues("token");
                if (token == null || AuthData.GetRole(token.ToString()) == Role.NoLogin)
                {
                    return ResponseJson.GetResponseJson(string.Empty, false);
                }
            }

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
        [System.Web.Http.ActionName("GetComment")]
        public ResponseJson GetComment(int commentId)
        {
            if (needCheckLogin)
            {
                var token = Request.Headers.GetValues("token");
                if (token == null || AuthData.GetRole(token.ToString()) == Role.NoLogin)
                {
                    return ResponseJson.GetResponseJson(string.Empty, false);
                }
            }

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
            if (needCheckLogin)
            {
                var token = Request.Headers.GetValues("token");
                if (token == null || AuthData.GetRole(token.ToString()) == Role.NoLogin)
                {
                    return ResponseJson.GetResponseJson(string.Empty, false);
                }
            }

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
        public ResponseJson DeleteComment(int commentId)
        {
            if (needCheckLogin)
            {
                var token = Request.Headers.GetValues("token");
                if (token == null || AuthData.GetRole(token.ToString()) == Role.NoLogin)
                {
                    return ResponseJson.GetResponseJson(string.Empty, false);
                }
            }

            bool success = DataProcess.DeleteProgrammingComment(commentId);

            var resultObject = new { IsSuccess = true, Message = "DeleteComment" };
            return ResponseJson.GetResponseJson(resultObject);
        }
        #endregion
    }
}
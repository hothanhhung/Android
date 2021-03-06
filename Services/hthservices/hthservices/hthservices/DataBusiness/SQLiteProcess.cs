﻿using hthservices.Models.Website;
using hthservices.Utils;
using System;
using System.Collections.Generic;
using System.Data.SQLite;
using System.Linq;
using System.Text;
using System.Web;

namespace hthservices.DataBusiness
{
    public class SQLiteProcess
    {
        private static string connectString;
        private static string ConnectString
        {
            get
            {
                //return @"D:\hung\github\android\Android\Services\hthservices\hthservices\hthservices\Data\TVSchedules.db3";
                if (string.IsNullOrWhiteSpace(connectString))
                {
                    if (HttpContext.Current == null)
                    {
                        if (HttpRuntime.AppDomainAppPath.EndsWith("\\"))
                        {
                            connectString = "Data Source=" + HttpRuntime.AppDomainAppPath + "Data\\WebsiteData.db3;Version=3;New=False;Compress=True;UTF8Encoding=True";
                        }
                        else
                        {
                            connectString = "Data Source=" + HttpRuntime.AppDomainAppPath + "\\Data\\WebsiteData.db3;Version=3;New=False;Compress=True;UTF8Encoding=True";
                        }
                    }
                    else
                    {
                        connectString = "Data Source=" + HttpContext.Current.Server.MapPath("~/Data/WebsiteData.db3") + ";Version=3;New=False;Compress=True;UTF8Encoding=True";
                    }
                }
                return connectString;
            }
        }



        #region Category
        public static int CountProgrammingCategories(bool isDisplay)
        {
            int total = 0;
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                var categories = context.ProgrammingCategories.Where(p => isDisplay == false || (p.IsDisplay ?? 0) > 0);

                total = categories.Count();
            }
            return total;
        }

        public static List<ProgrammingCategory> GetProgrammingCategories(bool isDisplay, bool isOrder = false)
        {
            List<ProgrammingCategory> programmingCategories = new List<ProgrammingCategory>();
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                var categories = context.ProgrammingCategories.Where(p => isDisplay == false || (p.IsDisplay ?? 0) > 0);

                if (isOrder) programmingCategories = categories.OrderBy(p => p.Sort).ToList();
                else programmingCategories = categories.ToList();

                foreach(var cat in programmingCategories)
                {
                    cat.NumberOfContents = context.ProgrammingContents.Where(p => p.CategoryId == cat.Id && (isDisplay == false || (p.IsDisplay ?? 0) > 0)).Count();
                }
            }
            return programmingCategories;
        }
        public static ProgrammingCategory GetProgrammingCategory(string categorId, bool isDisplay)
        {
            ProgrammingCategory programmingCategory = null;
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                var categories = context.ProgrammingCategories.Where(p => p.Id == categorId && (isDisplay == false || (p.IsDisplay ?? 0) > 0));

                programmingCategory = categories.FirstOrDefault();
                if (programmingCategory != null)
                {
                    //context.Entry(programmingCategory).Collection(p => p.Contents).Load();
                    programmingCategory.NumberOfContents = context.ProgrammingContents.Where(p => p.CategoryId == programmingCategory.Id && (isDisplay == false || (p.IsDisplay ?? 0) > 0)).Count();
                }
            }
            return programmingCategory;
        }

        public static bool SaveProgrammingCategory(ProgrammingCategory category)
        {
            bool succ = false;
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                var programmingCategory = context.ProgrammingCategories.FirstOrDefault(p => p.Id == category.Id);
                if (programmingCategory == null)
                {
                    if (string.IsNullOrWhiteSpace(category.Id) || category.Id == "0")
                    {
                        category.Id = MethodHelpers.IdFromDateTimeUTCGenarator();
                    }
                    context.ProgrammingCategories.Add(category);
                }
                else
                {
                    programmingCategory.IsDisplay = category.IsDisplay;
                    programmingCategory.Name = category.Name;
                    programmingCategory.Note = category.Note;
                    programmingCategory.NumberOfAccess = category.NumberOfAccess;
                    programmingCategory.Sort = category.Sort;
                }
                succ = context.SaveChanges() > 0;
            }
            return succ;
        }
        public static bool DeleteProgrammingCategory(string categoryId)
        {
            bool succ = false;
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                var programmingCategory = context.ProgrammingCategories.FirstOrDefault(p => p.Id == categoryId);
                if (programmingCategory != null)
                {
                    context.ProgrammingCategories.Remove(programmingCategory);
                    succ = context.SaveChanges() > 0;
                }
            }
            return succ;
        }
        #endregion

        #region Content

        public static int CountProgrammingContents(string categoryId, bool isDisplay)
        {
            string today = MethodHelpers.GetCurrentVNDateTimeInCorrectString();
            int total = 0;
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                var contents = context.ProgrammingContents.Where(p => isDisplay == false || ((p.IsDisplay ?? 0) > 0 && today.CompareTo(p.PublishedDate) > 0));
                if (!string.IsNullOrWhiteSpace(categoryId))
                {
                    contents = contents.Where(p => p.CategoryId == categoryId);
                }
                total = contents.Count();
            }
            return total;
        }

        public static List<ProgrammingContent> GetProgrammingContents(string categoryId, bool isDisplay, int page=0, int size = 10)
        {
            string today = MethodHelpers.GetCurrentVNDateTimeInCorrectString();
            List<ProgrammingContent> programmingContents = new List<ProgrammingContent>();
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                var contents = context.ProgrammingContents.Include("Comments").Include("Category").Where(p => isDisplay == false || ((p.IsDisplay ?? 0) > 0 && today.CompareTo(p.PublishedDate) > 0));
                if (!string.IsNullOrWhiteSpace(categoryId))
                {
                    contents = contents.Where(p => p.CategoryId == categoryId);
                }
                contents = contents.OrderByDescending(p => p.PublishedDate);
                contents = contents.Skip(page * size).Take(size);
                programmingContents = contents.ToList();
            }
            return programmingContents;
        }

        public static List<ProgrammingContent> GetProgrammingContents(List<string> contentIds)
        {
            List<ProgrammingContent> programmingContents = new List<ProgrammingContent>();
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                var contents = context.ProgrammingContents.AsQueryable();
                if (contentIds != null && contentIds.Count > 0)
                {
                    contents = contents.Where(p => contentIds.Contains(p.Id));
                }
                foreach (var item in contents)
                {
                    programmingContents.Add(new ProgrammingContent()
                    {
                        CategoryId = item.CategoryId,
                        Content = item.Content,
                        CreatedDate = item.CreatedDate,
                        Id = item.Id,
                        ImageUrl = item.ImageUrl,
                        IsDisplay = item.IsDisplay,
                        Keywords = item.Keywords,
                        PublishedDate = item.PublishedDate,
                        ShortContent = item.ShortContent,
                        Subject = item.Subject,
                        Title = item.Title,
                        UpdatedDate = item.UpdatedDate
                    });
                }
            }
            return programmingContents;
        }
        public static ProgrammingContent GetProgrammingContent(string id, bool isDisplay)
        {
            string today = MethodHelpers.GetCurrentVNDateTimeInCorrectString();
            ProgrammingContent programmingContent = null;
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                var contents = context.ProgrammingContents.Where(p => p.Id == id && (isDisplay == false || ((p.IsDisplay ?? 0) > 0 && today.CompareTo(p.PublishedDate) > 0)));
                programmingContent = contents.FirstOrDefault();
                if (programmingContent != null)
                {
                    context.Entry(programmingContent).Reference(p => p.Category).Load();
                    context.Entry(programmingContent).Collection(p => p.Comments).Load();
                }
            }
            return programmingContent;
        }

        public static int UpdateProgrammingContents(List<ProgrammingContent> contents)
        {
            int count = 0;
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                foreach (var content in contents)
                {
                    var programmingContent = context.ProgrammingContents.FirstOrDefault(p => p.Id == content.Id);
                    if (programmingContent == null)
                    {
                        if (string.IsNullOrWhiteSpace(content.Id) || content.Id == "0")
                        {
                            content.Id = MethodHelpers.IdFromDateTimeUTCGenarator();
                        }
                        if (string.IsNullOrWhiteSpace(content.CreatedDate))
                        {
                            content.CreatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
                        }
                        if (string.IsNullOrWhiteSpace(content.UpdatedDate))
                        {
                            content.UpdatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();

                        }
                        context.ProgrammingContents.Add(content);
                    }
                    else
                    {
                        programmingContent.Title = content.Title;
                        programmingContent.CategoryId = content.CategoryId;
                        programmingContent.IsDisplay = content.IsDisplay;
                        programmingContent.ImageUrl = content.ImageUrl;
                        programmingContent.ShortContent = content.ShortContent;
                        programmingContent.Content = content.Content;
                        programmingContent.NumberOfViews = content.NumberOfViews;
                        programmingContent.PublishedDate = content.PublishedDate;
                        programmingContent.Keywords = content.Keywords;
                        programmingContent.Subject = content.Subject;
                        programmingContent.UpdatedDate = content.UpdatedDate;

                    }
                }
                count = context.SaveChanges();
            }
            return count;
        }

        public static bool SaveProgrammingContent(ProgrammingContent content)
        {
            bool succ = false;
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                var programmingContent = context.ProgrammingContents.FirstOrDefault(p => p.Id == content.Id);
                
                if (programmingContent == null)
                {
                    if (string.IsNullOrWhiteSpace(content.Id) || content.Id == "0")
                    {
                        content.Id = MethodHelpers.IdFromDateTimeUTCGenarator();
                    }
                    content.CreatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
                    content.UpdatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
                    context.ProgrammingContents.Add(content);
                }
                else
                {
                    programmingContent.Title = content.Title;
                    programmingContent.CategoryId = content.CategoryId;
                    programmingContent.IsDisplay = content.IsDisplay;
                    programmingContent.ImageUrl = content.ImageUrl;
                    programmingContent.ShortContent = content.ShortContent;
                    programmingContent.Content = content.Content;
                    programmingContent.NumberOfViews = content.NumberOfViews;
                    programmingContent.PublishedDate = content.PublishedDate;
                    programmingContent.Keywords = content.Keywords;
                    programmingContent.Subject = content.Subject;
                    programmingContent.UpdatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
            
                }
                succ = context.SaveChanges() > 0;
            }
            return succ;
        }
        public static bool DeleteProgrammingContent(string contentId)
        {
            bool succ = false;
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                var programmingContent = context.ProgrammingContents.FirstOrDefault(p => p.Id == contentId);
                if (programmingContent != null)
                {
                    context.ProgrammingContents.Remove(programmingContent);
                    succ = context.SaveChanges() > 0;
                }
            }
            return succ;
        }
        
        #endregion

        #region Comment

        public static int CountProgrammingComments(string contentId, bool isDisplay)
        {
            int total = 0;
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                var contents = context.ProgrammingComments.Where(p => isDisplay == false || (p.IsDisplay ?? 0) > 0);
                if (!string.IsNullOrWhiteSpace(contentId))
                {
                    contents = contents.Where(p => p.ContentId == contentId);
                }
                total = contents.Count();
            }
            return total;
        }

        public static List<ProgrammingComment> GetProgrammingComments(string contentId, bool isDisplay, int page = 0, int size = 10, bool isDesc = false)
        {
            List<ProgrammingComment> programmingComments = new List<ProgrammingComment>();
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                var contents = context.ProgrammingComments.Include("ReplyTo").Include("Content").Include("Content.Category").Where(p => isDisplay == false || (p.IsDisplay ?? 0) > 0);
                if (!string.IsNullOrWhiteSpace(contentId))
                {
                    contents = contents.Where(p => p.ContentId == contentId);
                }
                if (isDesc)
                {
                    contents = contents.OrderByDescending(p => p.UpdatedDate);
                }
                else
                {
                    contents = contents.OrderBy(p => p.UpdatedDate);
                }
                contents = contents.Skip(page * size).Take(size);
                programmingComments = contents.ToList();
            }
            return programmingComments;
        }
        public static List<ProgrammingComment> GetProgrammingComments(List<string> commentIds)
        {
            List<ProgrammingComment> programmingComments = new List<ProgrammingComment>();
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                var contents = context.ProgrammingComments.AsQueryable();
                if (commentIds != null && commentIds.Count > 0)
                {
                    contents = contents.Where(p=> commentIds.Contains(p.Id));
                }
                foreach (var item in contents)
                {
                    programmingComments.Add(new ProgrammingComment()
                    {
                        ContentId = item.ContentId,
                        CreatedDate = item.CreatedDate,
                        Email = item.Email,
                        Id = item.Id,
                        IsDisplay = item.IsDisplay,
                        Message = item.Message,
                        Name = item.Name,
                        ReplyToCommentId = item.ReplyToCommentId,
                        Subject = item.Subject,
                        UpdatedDate = item.UpdatedDate
                    });
                }
            }
            return programmingComments;
        }

        public static int UpdateProgrammingComments(List<ProgrammingComment> programmingComments)
        {
            int count = 0;
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                foreach (var pc in programmingComments)
                {
                    var programmingComment = context.ProgrammingComments.FirstOrDefault(p => p.Id == pc.Id);
                    if (programmingComment == null)
                    {
                        if (string.IsNullOrWhiteSpace(pc.Id) || pc.Id == "0")
                        {
                            pc.Id = MethodHelpers.IdFromDateTimeUTCGenarator();
                        }
                        if (string.IsNullOrWhiteSpace(pc.CreatedDate))
                        {
                            pc.CreatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
                        }
                        if (string.IsNullOrWhiteSpace(pc.UpdatedDate))
                        {
                            pc.UpdatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();

                        }
                        context.ProgrammingComments.Add(pc);
                    }
                    else
                    {
                        programmingComment.Name = pc.Name;
                        programmingComment.ContentId = pc.ContentId;
                        programmingComment.ReplyToCommentId = pc.ReplyToCommentId;
                        programmingComment.Email = pc.Email;
                        programmingComment.IsDisplay = pc.IsDisplay;
                        programmingComment.Subject = pc.Subject;
                        programmingComment.Message = pc.Message;
                        programmingComment.UpdatedDate = pc.UpdatedDate;

                    }
                }
                count = context.SaveChanges();
            }
            return count;
        }

        public static ProgrammingComment GetProgrammingComment(string commentId, bool isDisplay)
        {
            ProgrammingComment programmingComment = null;
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                var comments = context.ProgrammingComments.Include("ReplyTo").Include("Content").Include("Content.Category").Where(p => p.Id == commentId && (isDisplay == false || (p.IsDisplay ?? 0) > 0));
                programmingComment = comments.FirstOrDefault();
            }
            return programmingComment;
        }
        public static bool SaveProgrammingComment(ProgrammingComment comment)
        {
            bool succ = false;
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                var programmingComment = context.ProgrammingComments.FirstOrDefault(p => p.Id == comment.Id);
                if (programmingComment == null)
                {
                    if (string.IsNullOrWhiteSpace(comment.Id) || comment.Id == "0")
                    {
                        comment.Id = MethodHelpers.IdFromDateTimeUTCGenarator();
                    }
                    comment.CreatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
                    comment.UpdatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
                    context.ProgrammingComments.Add(comment);
                }
                else
                {
                    programmingComment.Name = comment.Name;
                    programmingComment.Email = comment.Email;
                    programmingComment.IsDisplay = comment.IsDisplay;
                    programmingComment.Subject = comment.Subject;
                    programmingComment.Message = comment.Message;
                    programmingComment.UpdatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();

                }
                succ = context.SaveChanges() > 0;
            }
            return succ;
        }
        public static bool DeleteProgrammingComment(string commentId)
        {
            bool succ = false;
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                var programmingComment = context.ProgrammingComments.FirstOrDefault(p => p.Id == commentId);
                if (programmingComment != null)
                {
                    context.ProgrammingComments.Remove(programmingComment);
                    succ = context.SaveChanges() > 0;
                }
            }
            return succ;
        }
        #endregion

        #region Project

        public static int CountProjects(bool isDisplay)
        {
            int total = 0;
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                var contents = context.Projects.Where(p => isDisplay == false || (p.IsDisplay ?? 0) > 0);
                total = contents.Count();
            }
            return total;
        }

        public static List<Project> GetProjects(bool isDisplay, int page = 0, int size = 10)
        {
            List<Project> projects = new List<Project>();
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                var contents = context.Projects.Where(p => isDisplay == false || (p.IsDisplay ?? 0) > 0);                
                contents = contents.OrderBy(p => p.UpdatedDate);
                contents = contents.Skip(page * size).Take(size);
                projects = contents.ToList();
            }
            return projects;
        }

        public static Project GetProject(string id, bool isDisplay)
        {
            Project project = null;
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                var contents = context.Projects.Where(p => p.Id == id && (isDisplay == false || (p.IsDisplay ?? 0) > 0));
                project = contents.FirstOrDefault();

            }
            return project;
        }
        public static bool SaveProject(Project content)
        {
            bool succ = false;
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                var project = context.Projects.FirstOrDefault(p => p.Id == content.Id);
                if (project == null)
                {
                    if (string.IsNullOrWhiteSpace(content.Id) || content.Id == "0")
                    {
                        content.Id = MethodHelpers.IdFromDateTimeUTCGenarator();
                    }
                    content.CreatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
                    content.UpdatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
                    context.Projects.Add(content);
                }
                else
                {
                    project.Title = content.Title;
                    project.Technical = content.Technical;
                    project.IsDisplay = content.IsDisplay;
                    project.ImageUrl = content.ImageUrl;
                    project.ShortContent = content.ShortContent;
                    project.Content = content.Content;
                    project.UpdatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();

                }
                succ = context.SaveChanges() > 0;
            }
            return succ;
        }
        public static bool DeleteProject(string contentId)
        {
            bool succ = false;
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                var project = context.Projects.FirstOrDefault(p => p.Id == contentId);
                if (project != null)
                {
                    context.Projects.Remove(project);
                    succ = context.SaveChanges() > 0;
                }
            }
            return succ;
        }

        #endregion

        #region create data

        private static List<ProgrammingContent> CreateProgrammingContents()
        {
            List<ProgrammingContent> programmingContents = new List<ProgrammingContent>();
            //var programmingContent = new ProgrammingContent();
            //programmingContent.Title = "Some Tittle Goes Here";
            //programmingContent.CategoryId = 1;
            //programmingContent.IsDisplay = 1;
            //programmingContent.ImageUrl = "/Web/images/1.jpg";
            //programmingContent.ShortContent = "Phasellus vel arcu vitae neque sagittis aliquet ac at purus. Vestibulum varius eros in dui sagittis non ultrices orci hendrerit. Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
            //programmingContent.Content = "Sed euismod feugiat sodales. Vivamus dui ipsum, laoreet vitae euismod sit amet, euismod ac est. Sed turpis massa, convallis vitae facilisis eget, malesuada ullamcorper nibh. Nunc pulvinar augue non felis dictum ultricies. Donec lacinia, enim sit amet volutpat sodales, lorem velit fringilla metus, et semper metus sapien non odio. Nulla facilisi.Praesent gravida suscipit leo, eget fermentum magna malesuada ac. Maecenas pulvinar malesuada elementum. Sed euismod feugiat sodales. Vivamus dui ipsum, laoreet vitae euismod sit amet, euismod ac est. Sed turpis massa, convallis vitae facilisis eget, malesuada ullamcorper nibh. Nunc pulvinar augue non felis dictum ultricies. Donec lacinia, enim sit amet volutpat sodales, lorem velit fringilla metus, et semper metus sapien non odio. Nulla facilisi.Praesent gravida suscipit leo, eget fermentum magna malesuada ac. Maecenas pulvinar malesuada elementum.";
            //programmingContent.CreatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
            //programmingContent.UpdatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
            //programmingContents.Add(programmingContent);

            //programmingContent = new ProgrammingContent();
            //programmingContent.Title = "Some Tittle Goes Here2";
            //programmingContent.CategoryId = 1;
            //programmingContent.IsDisplay = 1;
            //programmingContent.ImageUrl = "/Web/images/2.jpg";
            //programmingContent.ShortContent = "Phasellus vel arcu vitae neque sagittis aliquet ac at purus. Vestibulum varius eros in dui sagittis non ultrices orci hendrerit. Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
            //programmingContent.Content = "Sed euismod feugiat sodales. Vivamus dui ipsum, laoreet vitae euismod sit amet, euismod ac est. Sed turpis massa, convallis vitae facilisis eget, malesuada ullamcorper nibh. Nunc pulvinar augue non felis dictum ultricies. Donec lacinia, enim sit amet volutpat sodales, lorem velit fringilla metus, et semper metus sapien non odio. Nulla facilisi.Praesent gravida suscipit leo, eget fermentum magna malesuada ac. Maecenas pulvinar malesuada elementum. Sed euismod feugiat sodales. Vivamus dui ipsum, laoreet vitae euismod sit amet, euismod ac est. Sed turpis massa, convallis vitae facilisis eget, malesuada ullamcorper nibh. Nunc pulvinar augue non felis dictum ultricies. Donec lacinia, enim sit amet volutpat sodales, lorem velit fringilla metus, et semper metus sapien non odio. Nulla facilisi.Praesent gravida suscipit leo, eget fermentum magna malesuada ac. Maecenas pulvinar malesuada elementum.";
            //programmingContent.CreatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
            //programmingContent.UpdatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
            //programmingContents.Add(programmingContent);

            //programmingContent = new ProgrammingContent();
            //programmingContent.Title = "Some Tittle Goes Here3";
            //programmingContent.CategoryId = 2;
            //programmingContent.IsDisplay = 1;
            //programmingContent.ImageUrl = "/Web/images/2.jpg";
            //programmingContent.ShortContent = "Phasellus vel arcu vitae neque sagittis aliquet ac at purus. Vestibulum varius eros in dui sagittis non ultrices orci hendrerit. Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
            //programmingContent.Content = "Sed euismod feugiat sodales. Vivamus dui ipsum, laoreet vitae euismod sit amet, euismod ac est. Sed turpis massa, convallis vitae facilisis eget, malesuada ullamcorper nibh. Nunc pulvinar augue non felis dictum ultricies. Donec lacinia, enim sit amet volutpat sodales, lorem velit fringilla metus, et semper metus sapien non odio. Nulla facilisi.Praesent gravida suscipit leo, eget fermentum magna malesuada ac. Maecenas pulvinar malesuada elementum. Sed euismod feugiat sodales. Vivamus dui ipsum, laoreet vitae euismod sit amet, euismod ac est. Sed turpis massa, convallis vitae facilisis eget, malesuada ullamcorper nibh. Nunc pulvinar augue non felis dictum ultricies. Donec lacinia, enim sit amet volutpat sodales, lorem velit fringilla metus, et semper metus sapien non odio. Nulla facilisi.Praesent gravida suscipit leo, eget fermentum magna malesuada ac. Maecenas pulvinar malesuada elementum.";
            //programmingContent.CreatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
            //programmingContent.UpdatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
            //programmingContents.Add(programmingContent);

            //programmingContent = new ProgrammingContent();
            //programmingContent.Title = "Some Tittle Goes Here4";
            //programmingContent.CategoryId = 2;
            //programmingContent.IsDisplay = 1;
            //programmingContent.ImageUrl = "/Web/images/1.jpg";
            //programmingContent.ShortContent = "Phasellus vel arcu vitae neque sagittis aliquet ac at purus. Vestibulum varius eros in dui sagittis non ultrices orci hendrerit. Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
            //programmingContent.Content = "Sed euismod feugiat sodales. Vivamus dui ipsum, laoreet vitae euismod sit amet, euismod ac est. Sed turpis massa, convallis vitae facilisis eget, malesuada ullamcorper nibh. Nunc pulvinar augue non felis dictum ultricies. Donec lacinia, enim sit amet volutpat sodales, lorem velit fringilla metus, et semper metus sapien non odio. Nulla facilisi.Praesent gravida suscipit leo, eget fermentum magna malesuada ac. Maecenas pulvinar malesuada elementum. Sed euismod feugiat sodales. Vivamus dui ipsum, laoreet vitae euismod sit amet, euismod ac est. Sed turpis massa, convallis vitae facilisis eget, malesuada ullamcorper nibh. Nunc pulvinar augue non felis dictum ultricies. Donec lacinia, enim sit amet volutpat sodales, lorem velit fringilla metus, et semper metus sapien non odio. Nulla facilisi.Praesent gravida suscipit leo, eget fermentum magna malesuada ac. Maecenas pulvinar malesuada elementum.";
            //programmingContent.CreatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
            //programmingContent.UpdatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
            //programmingContents.Add(programmingContent);


            return programmingContents;
        }

        private static List<ProgrammingComment> CreateProgrammingComments()
        {
            List<ProgrammingComment> programmingComments = new List<ProgrammingComment>();
            //var programmingComment = new ProgrammingComment();
            //programmingComment.Name = "Hung";
            //programmingComment.Email = "";
            //programmingComment.Subject = "";
            //programmingComment.Message = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
            //programmingComment.ContentId = 1;
            //programmingComment.IsDisplay = 1;
            //programmingComment.CreatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
            //programmingComment.UpdatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
            //programmingComment.ReplyToCommentId = null;
            //programmingComments.Add(programmingComment);

            //programmingComment = new ProgrammingComment();
            //programmingComment.Name = "ABC";
            //programmingComment.Email = "";
            //programmingComment.Subject = "";
            //programmingComment.Message = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
            //programmingComment.ContentId = 1;
            //programmingComment.IsDisplay = 1;
            //programmingComment.CreatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
            //programmingComment.UpdatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
            //programmingComment.ReplyToCommentId = null;
            //programmingComments.Add(programmingComment);


            //programmingComment = new ProgrammingComment();
            //programmingComment.Name = "Name";
            //programmingComment.Email = "";
            //programmingComment.Subject = "";
            //programmingComment.Message = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
            //programmingComment.ContentId = 1;
            //programmingComment.IsDisplay = 1;
            //programmingComment.CreatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
            //programmingComment.UpdatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
            //programmingComment.ReplyToCommentId = null;
            //programmingComments.Add(programmingComment);


            //programmingComment = new ProgrammingComment();
            //programmingComment.Name = "Hung";
            //programmingComment.Email = "";
            //programmingComment.Subject = "";
            //programmingComment.Message = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
            //programmingComment.ContentId = 1;
            //programmingComment.IsDisplay = 1;
            //programmingComment.CreatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
            //programmingComment.UpdatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
            //programmingComment.ReplyToCommentId = 1;
            //programmingComments.Add(programmingComment);


            //programmingComment = new ProgrammingComment();
            //programmingComment.Name = "Hung";
            //programmingComment.Email = "";
            //programmingComment.Subject = "";
            //programmingComment.Message = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
            //programmingComment.ContentId = 1;
            //programmingComment.IsDisplay = 1;
            //programmingComment.CreatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
            //programmingComment.UpdatedDate = hthservices.Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
            //programmingComment.ReplyToCommentId = null;
            //programmingComments.Add(programmingComment);

            return programmingComments;
        }

        public static void CreateData()
        {
            using (var context = new WebsiteDataContext(new SQLiteConnection(ConnectString)))
            {
                if(context.ProgrammingContents.Count() <= 0)
                {
                    context.ProgrammingContents.AddRange(CreateProgrammingContents());
                    context.SaveChanges();
                }

                if (context.ProgrammingComments.Count() <= 0)
                {
                    context.ProgrammingComments.AddRange(CreateProgrammingComments());
                    context.SaveChanges();
                }
            }
        }
        #endregion
    }
}
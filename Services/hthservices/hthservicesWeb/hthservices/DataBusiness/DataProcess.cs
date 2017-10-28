using hthservices.Models.Website;
using HtmlAgilityPack;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Web;

namespace hthservices.DataBusiness
{
    public class DataProcess
    {
        #region Category
        public static int CountProgrammingCategories()
        {
            return SQLiteProcess.CountProgrammingCategories(false);
        }
        public static List<ProgrammingCategory> GetProgrammingCategories()
        {
            return SQLiteProcess.GetProgrammingCategories(false, false);
        }
        public static ProgrammingCategory GetProgrammingCategory(int categoryId)
        {
            return SQLiteProcess.GetProgrammingCategory(categoryId, false);
        }

        public static bool SaveProgrammingCategory(ProgrammingCategory category)
        {
            return SQLiteProcess.SaveProgrammingCategory(category);
        }
        public static bool DeleteProgrammingCategory(int categoryId)
        {
            return SQLiteProcess.DeleteProgrammingCategory(categoryId);
        }
        #endregion

        #region Content
        public static int CountProgrammingContents(int? categoryId)
        {
            return SQLiteProcess.CountProgrammingContents(categoryId, false);
        }
        public static List<ProgrammingContent> GetProgrammingContents(int? categoryId, int page = 0, int size = 10)
        {
            return SQLiteProcess.GetProgrammingContents(categoryId, false, page, size);
        }
        public static ProgrammingContent GetProgrammingContent(int contentId)
        {
            return SQLiteProcess.GetProgrammingContent(contentId, false);
        }

        public static string GetProgrammingContents(List<int> contentIds)
        {
            List<ProgrammingContent> programContents = SQLiteProcess.GetProgrammingContents(contentIds);
            var str = JsonConvert.SerializeObject(programContents);
            return str;
        }

        public static int UpdateProgrammingContent(string contentsJson)
        {
            if (string.IsNullOrWhiteSpace(contentsJson))
            {
                return 0;
            }
            List<ProgrammingContent> contents = JsonConvert.DeserializeObject<List<ProgrammingContent>>(contentsJson);
            return SQLiteProcess.UpdateProgrammingContents(contents);
        }

        public static bool SaveProgrammingContent(ProgrammingContent content)
        {
            return SQLiteProcess.SaveProgrammingContent(content);
        }
        public static bool DeleteProgrammingContent(int contentId)
        {
            return SQLiteProcess.DeleteProgrammingContent(contentId);
        }
        #endregion

        #region Comment
        public static int CountProgrammingComments(int? contentId)
        {
            return SQLiteProcess.CountProgrammingComments(contentId, false);
        }
        public static List<ProgrammingComment> GetProgrammingComments(int? contentId, int page = 0, int size = 10, bool isDesc = false)
        {
            return SQLiteProcess.GetProgrammingComments(contentId, false, page, size, isDesc);
        }
        public static ProgrammingComment GetProgrammingComment(int commentId)
        {
            return SQLiteProcess.GetProgrammingComment(commentId, false);
        }

        public static string GetProgrammingComments(List<int> commentIds)
        {
            List<ProgrammingComment> programComments = SQLiteProcess.GetProgrammingComments(commentIds);
            var str = JsonConvert.SerializeObject(programComments);
            return str;
        }

        public static int UpdateProgrammingComments(string commentsJson)
        {
            if(string.IsNullOrWhiteSpace(commentsJson))
            {
                return 0;
            }
            List<ProgrammingComment> comments = JsonConvert.DeserializeObject<List<ProgrammingComment>>(commentsJson);
            return SQLiteProcess.UpdateProgrammingComments(comments);
        }

        public static bool SaveProgrammingComment(ProgrammingComment comment)
        {
            return SQLiteProcess.SaveProgrammingComment(comment);
        }
        public static bool DeleteProgrammingComment(int commentId)
        {
            return SQLiteProcess.DeleteProgrammingComment(commentId);
        }
        #endregion

        #region Project
        public static int CountProjects()
        {
            return SQLiteProcess.CountProjects(false);
        }
        public static List<Project> GetProjects(int page = 0, int size = 10)
        {
            return SQLiteProcess.GetProjects(false, page, size);
        }
        public static Project GetProject(int contentId)
        {
            return SQLiteProcess.GetProject(contentId, false);
        }

        public static bool SaveProject(Project content)
        {
            return SQLiteProcess.SaveProject(content);
        }
        public static bool DeleteProject(int contentId)
        {
            return SQLiteProcess.DeleteProject(contentId);
        }
        #endregion
        #region User
        public static int CountProgrammingCategoriesForUser()
        {
            return SQLiteProcess.CountProgrammingCategories(true);
        }
        public static List<ProgrammingCategory> GetProgrammingCategoriesForUser()
        {
            return SQLiteProcess.GetProgrammingCategories(true, true);
        }
        public static int CountProgrammingContentsForUser(int? categoryId)
        {
            return SQLiteProcess.CountProgrammingContents(categoryId, true);
        }
        public static List<ProgrammingContent> GetProgrammingContentsForUser(int? categoryId, int page = 0, int size = 10)
        {
            return SQLiteProcess.GetProgrammingContents(categoryId, true, page, size);
        }
        public static ProgrammingContent GetProgrammingContentForUser(int id)
        {
            return SQLiteProcess.GetProgrammingContent(id, true);
        }
        public static List<ProgrammingComment> GetProgrammingCurrentCommentsForUser()
        {
            return SQLiteProcess.GetProgrammingComments(null, true, 0, 10, true);
        }
        public static int CountProjectsForUser()
        {
            return SQLiteProcess.CountProjects(true);
        }
        public static List<Project> GetProjectsForUser(int page = 0, int size = 10)
        {
            return SQLiteProcess.GetProjects(true, page, size);
        }
        public static Project GetProjectForUser(int id)
        {
            return SQLiteProcess.GetProject(id, true);
        }

        #endregion
    }
}
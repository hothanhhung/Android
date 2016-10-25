using hthservices.Models.Website;
using HtmlAgilityPack;
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
        public static List<ProgrammingContent> GetProgrammingContents(int? categoryId, int page = 0, int size = 10)
        {
            return SQLiteProcess.GetProgrammingContents(categoryId, false, page, size);
        }
        public static ProgrammingContent GetProgrammingContent(int contentId)
        {
            return SQLiteProcess.GetProgrammingContent(contentId, false);
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
        public static List<ProgrammingComment> GetProgrammingComments(int? contentId, int page = 0, int size = 10)
        {
            return SQLiteProcess.GetProgrammingComments(contentId, false, page, size);
        }
        public static ProgrammingComment GetProgrammingComment(int commentId)
        {
            return SQLiteProcess.GetProgrammingComment(commentId, false);
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

        #region User
        public static List<ProgrammingCategory> GetProgrammingCategoriesForUser()
        {
            return SQLiteProcess.GetProgrammingCategories(true, true);
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
            return SQLiteProcess.GetProgrammingComments(null, true);
        }
        #endregion
    }
}
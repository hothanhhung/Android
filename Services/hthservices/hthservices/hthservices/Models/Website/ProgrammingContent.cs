using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;
using hthservices.Utils;
namespace hthservices.Models.Website
{
    public class ProgrammingContent
    {
        [Key]
        public string Id { get; set; }
        public string Title { get; set; }
        public string ShortContent { get; set; }
        public string Content { get; set; }
        public string ImageUrl { get; set; }
        public int? IsDisplay { get; set; }
        public string CreatedDate { get; set; }
        public string UpdatedDate { get; set; }
        public string PublishedDate { get; set; }
        public string NumberOfViews { get; set; }
        [NotMapped]
        public int NumberOfComments { get; set; }

        public string CategoryId { get; set; }
        public string Keywords { get; set; }
        public string Subject { get; set; }
        [NotMapped]
        public string CategoryName { get; set; }
        [NotMapped]
        public string TextAvatar { get {
            if (!string.IsNullOrWhiteSpace(Keywords))
            {
                return Keywords.Split(',')[0].Trim();
            }
            else
            {
                if (!string.IsNullOrWhiteSpace(Title))
                {
                    return Title.Substring(0,1);
                }
                else
                {
                    return string.Empty;
                }
            }
        }
        }
        [IgnoreDataMemberAttribute]
        [ForeignKey("CategoryId")]
        public virtual ProgrammingCategory Category { get; set; }
        [IgnoreDataMemberAttribute]
        public virtual ICollection<ProgrammingComment> Comments { get; set; }

        public string GetSomeContent()
        {
            return Content == null ? string.Empty : Content.GetShortTextFromHmlContent();
        }

        public string GetUpdatedDateToShow()
        {
            DateTime? date = Utils.MethodHelpers.ConvertCorrectStringToDateTime(UpdatedDate);
            if (date.HasValue)
            {
                return date.Value.ToString("HH:mm:ss MMM dd, yyyy");
            }
            return string.Empty;
        }

        public string GetPublishedDateToShow()
        {
            DateTime? date = Utils.MethodHelpers.ConvertCorrectStringToDateTime(PublishedDate);
            if (!date.HasValue)
            {
                date = Utils.MethodHelpers.ConvertCorrectStringToDateTime(UpdatedDate);
            }
            if (date.HasValue)
            {
                return date.Value.ToString("HH:mm 'ngày' dd-MM-yyyy");
            }
            return string.Empty;
        }
        [NotMapped]
        public string PublishedInDateTime
        {
            get
            {
                string dateToCompare = string.IsNullOrWhiteSpace(PublishedDate) ? UpdatedDate : PublishedDate;
                if (string.IsNullOrWhiteSpace(dateToCompare))
                {
                    dateToCompare = Utils.MethodHelpers.GetCurrentVNDateTimeInCorrectString();
                }
                return dateToCompare.Replace(' ', 'T').Substring(0, 13) + ":00";
            }
        }
    }
}
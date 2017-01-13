﻿using System;
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
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }
        public string Title { get; set; }
        public string ShortContent { get; set; }
        public string Content { get; set; }
        public string ImageUrl { get; set; }
        public int? IsDisplay { get; set; }
        public string CreatedDate { get; set; }
        public string UpdatedDate { get; set; }
        public string NumberOfViews { get; set; }
        [NotMapped]
        public int NumberOfComments { get; set; }

        public int? CategoryId { get; set; }
        [NotMapped]
        public string CategoryName { get; set; }
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
                return date.Value.ToString("hh:mm:ss MMM dd, yyyy");
            }
            return string.Empty;
        }
    }
}
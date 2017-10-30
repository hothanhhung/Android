using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace hthservices.Models.Website
{
    public class Project
    {
        [Key]
        public string Id { get; set; }
        public string Title { get; set; }
        public string ShortContent { get; set; }
        public string Technical { get; set; }
        public string Content { get; set; }
        public string ImageUrl { get; set; }
        public int? IsDisplay { get; set; }
        public string CreatedDate { get; set; }
        public string UpdatedDate { get; set; }

        public string GetSomeContent()
        {
            return Content == null? "":(Content.Count() > 100 ? Content.Substring(100) : Content);
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
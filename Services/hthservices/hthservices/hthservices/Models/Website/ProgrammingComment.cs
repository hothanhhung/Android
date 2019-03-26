using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace hthservices.Models.Website
{
    public class ProgrammingComment
    {
        [Key]
        public string Id { get; set; }
        public string Name { get; set; }
        public string Email { get; set; }
        public string Subject { get; set; }
        public string Message { get; set; }
        public string ContentId { get; set; }
        [IgnoreDataMemberAttribute]
        [ForeignKey("ContentId")]
        public ProgrammingContent Content { get; set; }
        public int? IsDisplay { get; set; }
        public string CreatedDate { get; set; }
        public string UpdatedDate { get; set; }
        public string ReplyToCommentId { get; set; }
        [IgnoreDataMemberAttribute]
        [ForeignKey("ReplyToCommentId")]
        public ProgrammingComment ReplyTo { get; set; }

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
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace hthservices.Models.Website
{
    public class ProgrammingCategory
    {
        [Key]
        public string Id { get; set; }
        public string Name { get; set; }
        public int? IsDisplay { get; set; }
        public int? Sort { get; set; }
        public int? NumberOfAccess { get; set; }
        [NotMapped]
        public int? NumberOfContents { get; set; }
        public string Note { get; set; }
        [IgnoreDataMemberAttribute]
        public virtual ICollection<ProgrammingContent> Contents { get; set; }
    }
}
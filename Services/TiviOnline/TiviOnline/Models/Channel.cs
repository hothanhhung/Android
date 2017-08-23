using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace TiviOnline.Models
{
    public class Channel
    {
        public string ID { get; set; }
        public string Logo { get; set; }
        public bool IsActive { get; set; }
        public List<int> Group { get; set; }
        public string Description { get; set; }
       
    }
}
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace hthservices.Models
{
    public class RequestInfo
    {
        public int Id {get; set;}  
        public string CurrentDate {get; set;}   
        public string Type  {get; set;}
        public string IsFailed { get; set; }    
        public string RequestLink {get; set;}  
    }
}
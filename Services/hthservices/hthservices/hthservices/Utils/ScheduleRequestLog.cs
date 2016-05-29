using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace hthservices.Utils
{
    public class ScheduleRequestLog
    {
        public int ID {get; set;}
        public string ChannelKey {get; set;}
        public string CurrentDate {get; set;}
        public string DateOn {get; set;}        
        public int NumberOfRequests {get; set;}
        public string Note { get; set; }
    }
}
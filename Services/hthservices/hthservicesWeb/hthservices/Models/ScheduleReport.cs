using hthservices.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace hthservices.Models
{
    public class ScheduleReport
    {
        public List<ScheduleRequestLog> ScheduleLogs { get; set; }
        public int Total { get; set; }
        public int NumberOfPages { get; set; }
    }
}
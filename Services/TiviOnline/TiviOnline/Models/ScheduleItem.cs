using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace TiviOnline.Models
{
    public class ScheduleItem
    {
        public int ScheduleId;
        public String ChannelKey;
        public String DateOn;
        public String StartOn;
        public String ProgramName;
        public String DecodedProgramName;
        public String Note;
    }
}
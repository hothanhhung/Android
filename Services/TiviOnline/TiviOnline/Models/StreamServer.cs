using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace TiviOnline.Models
{
    public class StreamServer
    {
        public string ID { get; set; }
        public string URL { get; set; }
        public int ServerId { get; set; }
        public string ChannelId { get; set; }
        public bool IsActive { get; set; }
    }
}
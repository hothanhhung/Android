using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace hthservices.Utils
{
    public class Channel
    {
        public int ChannelId;
        public string ChannelName;
        public string ChannelGroupName;
        public string LinkVietBao;

        private string _dataFileName = null;
        public string DataFileName
        {
            get
            {
                if (_dataFileName == null)
                {
                    _dataFileName = MethodHelpers.RemoveSign4VietnameseString(ChannelName);
                }
                return _dataFileName;
            }
        }

        public string ChannelKey
        {
            get
            {
                return DataFileName.Replace(" ","");
            }
        }
    }
}
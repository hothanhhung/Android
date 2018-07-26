using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
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

        private string packageRequest = null;
        [IgnoreDataMember]
        public string PackageRequest
        {
            get
            {
                if (packageRequest == null)
                {
                    if (!String.IsNullOrWhiteSpace(RequestLink))
                    {
                        var start = RequestLink.IndexOf("package=", StringComparison.OrdinalIgnoreCase);
                        if (start > 0)
                        {
                            var end = RequestLink.IndexOf("&", start, StringComparison.OrdinalIgnoreCase);
                            if (end < 0)
                            {
                                packageRequest = RequestLink.Substring(start + 8).Trim().ToLower();
                            }
                            else
                            {
                                packageRequest = RequestLink.Substring(start + 8, end - start - 8).Trim().ToLower();
                            }
                        }
                        else
                        {
                            packageRequest = String.Empty;
                        }
                    }
                    else
                    {
                        packageRequest = String.Empty;
                    }
                }
                return packageRequest;
            }
        }
            
        private string infoRequest = null;
        [IgnoreDataMember]
        public string InfoRequest
        {
            get
            {
                if (infoRequest == null)
                {
                    if (!String.IsNullOrWhiteSpace(RequestLink))
                    {
                        var start = RequestLink.IndexOf("info=", StringComparison.OrdinalIgnoreCase);
                        if (start > 0)
                        {
                            var end = RequestLink.IndexOf("&", start, StringComparison.OrdinalIgnoreCase);
                            if (end < 0)
                            {
                                infoRequest = RequestLink.Substring(start + 5).Trim().ToLower();
                            }
                            else
                            {
                                infoRequest = RequestLink.Substring(start + 5, end - start - 5).Trim().ToLower();
                            }
                        }
                        else
                        {
                            infoRequest = String.Empty;
                        }

                    }
                    else
                    {
                        infoRequest = String.Empty;
                    }
                }
                return infoRequest;
            }
        }

        private string ipRequest = null;
        [IgnoreDataMember]
        public string IpRequest
        {
            get
            {
                if (ipRequest == null)
                {
                    if (!String.IsNullOrWhiteSpace(RequestLink))
                    {
                        var start = RequestLink.IndexOf("ipUser=", StringComparison.OrdinalIgnoreCase);
                        if (start > 0)
                        {
                            var end = RequestLink.IndexOf("&", start, StringComparison.OrdinalIgnoreCase);
                            if (end < 0)
                            {
                                ipRequest = RequestLink.Substring(start + 7).Trim().ToLower();
                            }
                            else
                            {
                                ipRequest = RequestLink.Substring(start + 7, end - start - 7).Trim().ToLower();
                            }
                        }
                        else
                        {
                            ipRequest = String.Empty;
                        }

                    }
                    else
                    {
                        ipRequest = String.Empty;
                    }
                }
                return ipRequest;
            }
        }
    }
}
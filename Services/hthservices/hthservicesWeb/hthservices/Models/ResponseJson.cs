﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;
using hthservices.Utils;
using hthservices.Ads;
using hthservices.Models.Website;

namespace hthservices.Models
{
    [Serializable]
    [KnownType(typeof(List<RequestInfo>))]
    [KnownType(typeof(List<GuideItem>))]
    [KnownType(typeof(List<SearchItem>))]
    [KnownType(typeof(List<AdItem>))]
    [KnownType(typeof(List<ScheduleRequestLog>))]
    [KnownType(typeof(ScheduleReport))]
    [KnownType(typeof(ReportForCurrentDate))]
    [KnownType(typeof(List<ReportForCurrentDate>))]
    [KnownType(typeof(List<ProgrammingCategory>))]
    [KnownType(typeof(List<ProgrammingContent>))] 
    public class ResponseJson
    {
        public bool IsSuccess;
        public bool NeedChangeDomain;
        public string NewDomain;
        public dynamic Data;

        public static dynamic GetResponseJson(dynamic data, bool isSuccess = true)
        {
            var responseJson = new ResponseJson()
            {
                IsSuccess = isSuccess,
                NeedChangeDomain = false,
                Data = data
            };
            return responseJson; 
        }
    }
}
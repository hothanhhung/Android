using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace hthservices.Models
{
    public class ResponseJson<T>
    {
        public bool IsSuccess;
        public bool NeedChangeDomain;
        public string NewDomain;
        public List<T> Data;

        public static ResponseJson<T> GetResponseJson(List<T> data)
        {
            var responseJson = new ResponseJson<T>()
            {
                IsSuccess = true,
                NeedChangeDomain = false,
                Data = data
            };
            return responseJson; 
        }
    }
}
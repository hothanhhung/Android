using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using hthservices.Models;
using hthservices.Data;
using System.Web.Http;

namespace hthservices.Controllers
{
    public class AccountController : ApiController
    {
        [System.Web.Http.HttpPost]
        public ResponseJson Login(dynamic obj )
        {
            string username="",  password="";
            bool isSuccess = false;
            string message = string.Empty, token = string.Empty;

            if (obj["username"] != null)
            {
                username = obj["username"].Value;
            }

            if (obj["password"] != null)
            {
                password = obj["password"].Value;
            }

            token = AuthData.Login(username, password);
            if (string.IsNullOrEmpty(token))
            {
                isSuccess = false;
                message = "Login failed";
            }
            else
            {
                isSuccess = true;
                message = "Login successfully";
            }
            var resultObject = new { IsSuccess = isSuccess, Message = message, Token = token };
            return ResponseJson.GetResponseJson(resultObject);
        }

        [System.Web.Http.HttpGet]
        public ResponseJson Logout(string token)
        {
            bool isSuccess = false;
            string message = string.Empty;
            if (AuthData.Logout(token))
            {
                isSuccess = true;
                message = "Logout successfully";
            }
            else
            {
                isSuccess = true;
                message = "Logout failed";
            }
            var resultObject = new { IsSuccess = isSuccess, Message = message, Token = token };
            return ResponseJson.GetResponseJson(resultObject);
        }
    }
}

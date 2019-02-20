using System;
using System.Collections.Generic;
using System.IO;
using System.Web.Http;

namespace hthservices.Controllers
{
    public class MockAPIController : ApiController
    {
        static private List<KeyValuePair<DateTime, string>> storage = new List<KeyValuePair<DateTime, string>>();

        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("get")]
        public string Get(string data)
        {
            if (storage == null) storage = new List<KeyValuePair<DateTime, string>>();
            storage.Add(new KeyValuePair<DateTime, string>(DateTime.Now, data));
            return "Success";

        }

        [System.Web.Http.HttpPost]
        [System.Web.Http.ActionName("post")]
        public string Post()
        {
            if (storage == null) storage = new List<KeyValuePair<DateTime, string>>();
            storage.Add(new KeyValuePair<DateTime, string>(DateTime.Now, Request.Content.ReadAsStringAsync().Result));
            return "Success";

        }

        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("list")]
        public List<KeyValuePair<DateTime, string>> List()
        {
            return storage;

        }

        [System.Web.Http.HttpGet]
        [System.Web.Http.ActionName("reset")]
        public string Reset()
        {
            storage.Clear();
            return "Success";

        }
    }
}

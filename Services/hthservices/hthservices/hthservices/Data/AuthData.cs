using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace hthservices.Data
{
    public class AuthData
    {
        private const string USERNAME = "admin";
        private const string PASSWORD = "admin";
        private const int TIMEOUT = 3600; //seconds
        private static List<KeyValuePair<string, DateTime>> listLogins = new List<KeyValuePair<string, DateTime>>();
        public static string Login(string username, string password)
        {
            if (USERNAME.Equals(username) && PASSWORD.Equals(password))
            {
                string key = Guid.NewGuid().ToString();
                listLogins.Add(new KeyValuePair<string, DateTime>(key, DateTime.Now));
                return key;
            }
            return string.Empty;
        }

        public static bool Logout(string token)
        {
            if (listLogins != null)
            {
                listLogins.RemoveAll(p => p.Key.Equals(token));
            }
            return true;
        }

        public static bool Validate(string token)
        {
            if (listLogins!=null)
            {
                var now = DateTime.Now;
                listLogins.RemoveAll(p=>(now - p.Value).Seconds > TIMEOUT);

                var item = listLogins.Where(p => p.Key.Equals(token));
                if (item != null && item.Count() > 0)
                {
                    return true;
                }
            }
            return false;
        }
    }
}
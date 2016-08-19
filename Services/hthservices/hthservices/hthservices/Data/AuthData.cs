using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace hthservices.Data
{
    public class AuthData
    {
        private const string USERNAME = "admin";
        private const string PASSWORD = "hungadmin";
        private const int TIMEOUT = 1800; //seconds
        private static Dictionary<string, DateTime> listLogins = new Dictionary<string, DateTime>();
        public static string Login(string username, string password)
        {
            if (USERNAME.Equals(username) && PASSWORD.Equals(password))
            {
                string key = Guid.NewGuid().ToString();
                listLogins.Add(key, DateTime.Now);
                return key;
            }
            return string.Empty;
        }

        public static bool Logout(string token)
        {
            if (listLogins != null)
            {
                listLogins.Remove(token);
            }
            return true;
        }

        public static bool Validate(string token)
        {
            if (listLogins!=null)
            {

                if ( listLogins.ContainsKey(token))
                {
                    var now = DateTime.Now;
                    if ((now - listLogins[token]).TotalSeconds > TIMEOUT)
                    {
                        listLogins.Remove(token);
                        return false;
                    }
                    else
                    {
                        listLogins[token] = now;
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
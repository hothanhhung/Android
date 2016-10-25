using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace hthservices.Data
{
    public enum Role
    {
        NoLogin = 0,
        Admin = 1,
        User = 2,
    }
    public class HTHLogin
    {
        public string UserName { get; set; }
        public string Password { get; set; }
        public Role Role { get; set; }
    }

    public class CurrentLogin
    {
        public string UserName { get; set; }
        public DateTime LastAction { get; set; }
    }

    public class AuthData
    {
        private static readonly List<HTHLogin> HTHLogins = new List<HTHLogin>(){
            new HTHLogin(){UserName = "admin", Password="hungadmin", Role = Role.Admin},
            new HTHLogin(){UserName = "user", Password="admin", Role = Role.User},
        };

        private const string USERNAME = "admin";
        private const string PASSWORD = "hungadmin";
        private const int TIMEOUT = 1800; //seconds
        private static Dictionary<string, CurrentLogin> listLogins = new Dictionary<string, CurrentLogin>();
        public static string Login(string username, string password)
        {
            var user = HTHLogins.FirstOrDefault(p => p.UserName.Equals(username) && p.Password.Equals(password));
            if (user != null)
            {
                string key = (int)user.Role + Guid.NewGuid().ToString();
                listLogins.Add(key, new CurrentLogin() { UserName = username, LastAction = DateTime.Now });
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

        public static Role GetRole(string token)
        {
            if (listLogins!=null)
            {

                if ( listLogins.ContainsKey(token))
                {
                    var now = DateTime.Now;
                    if ((now - listLogins[token].LastAction).TotalSeconds > TIMEOUT)
                    {
                        listLogins.Remove(token);
                        return Role.NoLogin;
                    }
                    else
                    {
                        var user = HTHLogins.FirstOrDefault(p => p.UserName.Equals(listLogins[token].UserName));
                        if (user != null)
                        {
                            listLogins[token].LastAction = now;
                            return user.Role;
                        }
                        else
                        {

                        }
                        return Role.NoLogin; ;
                    }
                }
            }
            return Role.NoLogin;
        }
    }
}
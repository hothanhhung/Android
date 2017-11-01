using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web;

namespace hthservices.Utils
{
    public class EncrypeString
    {
        static public String EncodeRandomly(String str, bool needEncode)
        {
            if(needEncode)
            {
                return Encode(str);
            }else
            {
                return str;
            }
        }
        static public String Encode(String str)
        {
            StringBuilder rs = new StringBuilder("<");
            char ch;
            for(int i = str.Length;  i>0;i--)
            {
                if (str[i - 1] == '5')
                {
                    ch = '0';
                }
                else if (str[i - 1] == '6')
                {
                    ch = '9';
                }
                else if (str[i - 1] == 'm')
                {
                    ch = 'a';
                }
                else if (str[i - 1] == 'n')
                {
                    ch = 'z';
                }
                else if (str[i - 1] == 'A')
                {
                    ch = 'M';
                }
                else if (str[i - 1] == 'Z')
                {
                    ch = 'N';
                }
                else if( str[i - 1] >= '0' && str[i - 1]<'5')
                {
                    ch = (char)( str[i - 1] + 1);
                }else if( str[i - 1] > '6' && str[i - 1]<='9')
                {
                    ch = (char)( str[i - 1] - 1);
                }else if( str[i - 1] >= 'a' && str[i - 1]<'m')
                {
                     ch = (char)( str[i - 1] + 1);
                }
                else if (str[i - 1] > 'n' && str[i - 1] <= 'z')
                {
                    ch = (char)(str[i - 1] - 1);
                }
                else if (str[i - 1] > 'A' && str[i - 1] <= 'M')
                {
                    ch = (char)(str[i - 1] - 1);
                }
                else if (str[i - 1] >= 'N' && str[i - 1] < 'Z')
                {
                     ch = (char)( str[i - 1] + 1);
                }else 
                {
                     ch = str[i - 1];
                }
                rs.Append(ch);
            }

            return rs.ToString();
        }

        static public string CorrectString(string str)
        {
            if(string.IsNullOrWhiteSpace(str))
            {
                return string.Empty;
            }
            else
            {
                var value = new StringBuilder();
                for(int i = str.Length; i> 0; i--)
                {
                    value.Append(str[i-1]);
                }
                return value.ToString();
            }
        }
    }
}
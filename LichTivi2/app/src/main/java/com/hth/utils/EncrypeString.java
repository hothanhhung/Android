package com.hth.utils;

/**
 * Created by Lenovo on 9/12/2016.
 */
public class EncrypeString {
    static public String tryDecode(String str)
    {
        if(str != null && !str.isEmpty() && str.charAt(0)=='<')
        {
            return decode(str);
        }else
        {
            return str;
        }
    }
    static public String decode(String str)
    {
        StringBuilder rs = new StringBuilder();
        char ch, ch1;
        for(int i = str.length(); i> 1;i--)
        {
            ch1 = str.charAt(i - 1);
            if (ch1 == '0')
            {
                ch = '5';
            }
            else if (ch1 == '9')
            {
                ch = '6';
            }
            else if (ch1 == 'a')
            {
                ch = 'm';
            }
            else if (ch1 == 'z')
            {
                ch = 'n';
            }
            else if (ch1 == 'M')
            {
                ch = 'A';
            }
            else if (ch1 == 'N')
            {
                ch = 'Z';
            }
            else if( ch1 > '0' && ch1<='5')
            {
                ch = (char)(ch1 - 1);
            }else if( ch1 >= '6' &&ch1<'9')
            {
                ch = (char)( ch1 + 1);
            }else if( ch1 > 'a' && ch1<='m')
            {
                ch = (char)(ch1 - 1);
            }
            else if (ch1 >= 'n' &&ch1 < 'z')
            {
                ch = (char)(ch1 + 1);
            }
            else if (ch1 >= 'A' && ch1 < 'M')
            {
                ch = (char)(ch1 + 1);
            }
            else if (ch1 > 'N' && ch1 <= 'Z')
            {
                ch = (char)(ch1 - 1);
            }else
            {
                ch = ch1;
            }
            rs.append(ch);
        }

        return rs.toString();
    }
}

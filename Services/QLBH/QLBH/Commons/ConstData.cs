using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace QLBH.Commons
{
    public class ConstData
    {
        public static string _connectionString = null;
        public static string ConnectionString{
            get{if(string.IsNullOrWhiteSpace(_connectionString)){
                string path = System.IO.Path.GetFullPath(".\\Data\\QuanLyBanHang.db3");
                if( System.IO.File.Exists(path))
                {
                    _connectionString = string.Format("Data Source={0};Version=3;New=False;Compress=True;UTF8Encoding=True", path);
                }
                else
                {
                    path = System.IO.Path.GetFullPath(".\\..\\..\\Data\\QuanLyBanHang.db3");
                    if (System.IO.File.Exists(path))
                    {
                        _connectionString = string.Format("Data Source={0};Version=3;New=False;Compress=True;UTF8Encoding=True", path);
                    }
                    else
                    {
                        _connectionString = string.Empty;
                    }
                }
            }
            return _connectionString;
            }
            
        }
    }
}

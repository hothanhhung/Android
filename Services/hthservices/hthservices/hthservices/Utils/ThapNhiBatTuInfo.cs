using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;

namespace hthservices.Utils
{
    public class ThapNhiBatTu
    {
        public string Name;
        public int Level;
        public string Summary;
        public string Detail;
        public string ShouldDo;
        public string ShouldNotDo;
        public string Exception;
    }

    public class ThapNhiBatTuInfo
    {
        private static List<ThapNhiBatTu> _thapNhiBatTus;
        public static List<ThapNhiBatTu> ThapNhiBatTus
        { get
            {
                if (_thapNhiBatTus == null)
                {
                    try
                    {
                        using (StreamReader r = new StreamReader(MethodHelpers.GetAbsolutePathToDataFolder() + "\\ThapNhiBatTu.json"))
                        {
                            string json = r.ReadToEnd();
                            _thapNhiBatTus = JsonConvert.DeserializeObject<List<ThapNhiBatTu>>(json);
                        }
                    }
                    catch (Exception ex)
                    {
                        _thapNhiBatTus = new List<ThapNhiBatTu>();
                    }
                }
                return _thapNhiBatTus;
            }
        }

        public static string GetThapNhiBatTu(int index)
        {
            string str = string.Empty;
            if (ThapNhiBatTus.Count > 1)
            {
                index = index % ThapNhiBatTus.Count;
                var thapNhiBatTu = ThapNhiBatTus[index];
                string color = "Red";
                switch(thapNhiBatTu.Level)
                {
                    case 0: color = "#3f5f6f"; break;
                    case 1: color = "#607d8b"; break;
                    case 2: color = "#acbcc3"; break;
                    case 3: color = "#ce245e"; break;
                    case 4: color = "#e91e63"; break;
                    case 5: color = "#c50b0b"; break;
                }
                str = String.Format(ThapNhiBatTus[0].Name, thapNhiBatTu.Name, thapNhiBatTu.Summary, thapNhiBatTu.ShouldDo, thapNhiBatTu.ShouldNotDo, thapNhiBatTu.Exception, color);
            }
            return str;
        }
        public static void Reset()
        {
            _thapNhiBatTus = null;
        }
    }
}
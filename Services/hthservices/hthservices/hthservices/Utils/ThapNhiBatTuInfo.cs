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
                str = String.Format(ThapNhiBatTus[0].Name, thapNhiBatTu.Name, thapNhiBatTu.ShouldDo, thapNhiBatTu.ShouldNotDo, thapNhiBatTu.Exception);
            }
            return str;
        }
        public static void Reset()
        {
            _thapNhiBatTus = null;
        }
    }
}
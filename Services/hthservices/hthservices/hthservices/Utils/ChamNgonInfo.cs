using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;

namespace hthservices.Utils
{
    public class ChamNgon
    {
        public string Author;
        public string Content;
    }

    public class ChamNgonInfo
    {
        private static List<ChamNgon> _chamNgons;
        public static List<ChamNgon> ChamNgons
        {
            get
            {
                if (_chamNgons == null)
                {
                    try
                    {
                        using (StreamReader r = new StreamReader(MethodHelpers.GetAbsolutePathToDataFolder() + "\\ChamNgon.json"))
                        {
                            string json = r.ReadToEnd();
                            _chamNgons = JsonConvert.DeserializeObject<List<ChamNgon>>(json);
                        }
                    }
                    catch (Exception ex)
                    {
                        _chamNgons = new List<ChamNgon>();
                    }
                }
                return _chamNgons;
            }
        }

        public static string GetThapNhiBatTuHTML(int index)
        {
            string str = string.Empty;
            if (ChamNgons.Count > 1)
            {
                index = (index % (ChamNgons.Count - 1)) + 1;
                var chamngon = ChamNgons[index];
                str = String.Format(ChamNgons[0].Content, chamngon.Content.Replace(". ", ".<br>"), chamngon.Author);
            }
            return str;
        }

        public static string GetThapNhiBatTuText(int index)
        {
            string str = string.Empty;
            if (ChamNgons.Count > 1)
            {
                index = (index % (ChamNgons.Count - 1)) + 1;
                var chamngon = ChamNgons[index];
                str = String.Format("{0}\n{1}", chamngon.Content.Replace(". ", ".\n"), chamngon.Author);
            }
            return str;
        }

        public static void Reset()
        {
            _chamNgons = null;
        }
    }
}
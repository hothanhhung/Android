using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace hthservices.Utils
{
    public class ChannelToServer
    {
        public string Server { get; set; }
        public string ChannelKey { get; set; }
        public string Value { get; set; }
    }

    public class DataStatic
    {

        #region Stations
        public static readonly int[] Stations = new int[13] {
                                            1, //Đài Truyền hình Việt Nam - VTV
	                                        14, //Đài Tiếng nói Việt Nam
	                                        15, //Đài Phát thanh và Truyền hình Hà Nội
	                                        2, //Đài Truyền hình TP Hồ Chí Minh - HTV
	                                        18, //Đài truyền hình An Viên AVG
	                                        16, //Đài Truyền hình Cáp Việt Nam
	                                        17, //Đài Truyền hình Cáp TP Hồ Chí Minh - HTVC
	                                        3, //Đài truyền hình cáp Hà Nội
	                                        4, //Đài truyền hình cáp Sài Gòn
	                                        6, //Đài truyền hình số VTC
	                                        8, //Đài TH địa phương
	                                        10, //Kênh TH quốc tế
	                                        12 //Truyền hình số vệ tinh k+
                                          };
        #endregion

        #region Server
        public const string FROM_VOV_PAGE = "http://vovtv.vov.vn/Schedule.aspx?day=27-5-2016";
        public const string FROM_MYTIVI_PAGE = "http://www.mytv.com.vn/module/ajax/ajax_get_schedule.php?"; //?channelId=22&dateSchedule=28/05/2016
        #endregion

        #region Channel to server
        public static readonly List<ChannelToServer> ChannelsToServers = new List<ChannelToServer>(){
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="K+1", Value="187"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="K+NS", Value="188"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="K+PM", Value="189"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="K+PC", Value="205"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="NATIONALGEOGRAPHICCHANNEL", Value="7"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="HBO", Value="9"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="StarMovies", Value="11"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="Channel[V]", Value="13"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="FoxSport", Value="14"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="FoxSport2", Value="15"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="HTV7", Value="19"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="HTV9", Value="20"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="AXN", Value="21"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="SCTV8-VITV", Value="27"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="CartoonNetworks", Value="34"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="DISCOVERY", Value="45"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="TRAVEL&LIVING", Value="46"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="AnimalPlanet", Value="47"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="TruyenHinhVinhLongTHVL1", Value="65"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="TruyenhinhDaNangDRT2", Value="74"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="StarMovies", Value="121"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="StarWorld", Value="120"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="ANTV", Value="138"},   //chưa có trên client
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="HTV4", Value="142"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="VTVcab5-Echannel", Value="154"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="DIVAUniversal", Value="178"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="AsianFoodChannel", Value="180"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="TruyenhinhHaTinhHTTV", Value="195"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="TruyenhinhHaGiangHTV", Value="199"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="TruyenHinhTienGiangTHTG", Value="203"}
        };
        #endregion

        public static ChannelToServer GetChannelToServer(string channelKey)
        {
            var channelToServer = ChannelsToServers.Where(p => p.ChannelKey.Equals(channelKey, StringComparison.OrdinalIgnoreCase)).FirstOrDefault();
            if (channelToServer != null)
            {
                return channelToServer;
            }
            return new ChannelToServer() { Server = "", ChannelKey = channelKey, Value = "" };
        }
    }
}
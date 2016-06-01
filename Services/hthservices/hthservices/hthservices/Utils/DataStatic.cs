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
        public const string FROM_VOV_PAGE = "http://vovtv.vov.vn/Schedule.aspx?";//day=29/05/2016
        public const string FROM_MYTIVI_PAGE = "http://www.mytv.com.vn/module/ajax/ajax_get_schedule.php?"; //?channelId=22&dateSchedule=28/05/2016
        public const string FROM_VTC14_PAGE = "http://vtc14.vn/";
        public const string FROM_TRUYENHINHSO_PAGE = "http://truyenhinhso.vn/Pages/Ajax/GetSchedule.aspx?channelid={0}&date={1}";//date=28/05/2016&channelid
        public const string FROM_TVNET_PAGE = "http://vn.tvnet.gov.vn/modules/process.php?option=epg&channel_id={0}&date={1}"; //channel_id=1&date=28/05/2016"
        public const string FROM_PHUT91_PAGE = "http://phut91.com/lich-phat-song/lich-phat-song-{0}.{1}.html"; //lich-phat-song-htvc-thuan-viet-42.30052016.html
        public const string FROM_HTV3TV_PAGE = "http://www.htv3tv.vn/schedules/jview?day={0}"; //day=2016-05-31 start from 06:00
        public const string FROM_HTV2CHANNEL_PAGE = "https://htv2channel.vn/lich-chieu/{0}"; //2016/5/31 start from 06:00
        public const string FROM_QPVN_PAGE = "http://qpvn.vn/ajax/getChannelSchedule?channel=QPVN&date={0}"; //date=03/06/2016
        public const string FROM_HTVONLINE_PAGE = "http://htvonline.com.vn/livetv/show-schedule";// POST date:"2016-06-01"  id_live:"22"
        public const string FROM_FBNC_PAGE = "http://fbnc.vn/schedule/update/?state=all&date={0}";//=Thu+Jun+02+2016
        public const string FROM_VTVCab_PAGE = "http://www.vtvcab.vn/lich-phat-song?day={1}&month={2}&year={3}&channel={0}";//=03&month=06&year=2016&channel=4077
        public const string FROM_TRAVINH_PAGE = "http://travinhtv.vn/thtv/ajax/ajax.php";//ngay:"2016-06-02"
        public const string FROM_BPTV_PAGE = "http://www.bptv.com.vn/?cat=110&kenh={0}&ngay={1}"; //kenh=THBP2&ngay=2016-06-02
        #endregion

        #region Channel to server
        public static readonly List<ChannelToServer> ChannelsToServers = new List<ChannelToServer>(){
             new ChannelToServer(){Server=FROM_VOV_PAGE, ChannelKey="VOVTV", Value=""},
             new ChannelToServer(){Server=FROM_VTC14_PAGE, ChannelKey="VTC14", Value=""},
             new ChannelToServer(){Server=FROM_TRUYENHINHSO_PAGE, ChannelKey="VTCHD3", Value="42"},
             new ChannelToServer(){Server=FROM_TRUYENHINHSO_PAGE, ChannelKey="VTC3", Value="42"},
             new ChannelToServer(){Server=FROM_TVNET_PAGE, ChannelKey="VTCHD1", Value="1"},
             new ChannelToServer(){Server=FROM_TVNET_PAGE, ChannelKey="VTC1", Value="1"},
             new ChannelToServer(){Server=FROM_TVNET_PAGE, ChannelKey="VTC10-NetViet", Value="4"},
             new ChannelToServer(){Server=FROM_TVNET_PAGE, ChannelKey="VTC16-3NTV", Value="5"},
             new ChannelToServer(){Server=FROM_TVNET_PAGE, ChannelKey="TruyenhinhThongtanxaVietNamTTXVN", Value="75"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="K+1", Value="187"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="K+NS", Value="188"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="K+PM", Value="189"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="K+PC", Value="205"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="NATIONALGEOGRAPHICCHANNEL", Value="7"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="HBO", Value="9"},
             //new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="StarMovies", Value="11"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="Channel[V]", Value="13"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="FoxSport", Value="14"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="FoxSport2", Value="15"},
             //new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="HTV7", Value="19"},
             //new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="HTV9", Value="20"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="AXN", Value="21"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="SCTV8-VITV", Value="27"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="CartoonNetworks", Value="34"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="DISCOVERY", Value="45"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="TRAVEL&LIVING", Value="46"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="AnimalPlanet", Value="47"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="TruyenHinhVinhLongTHVL1", Value="65"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="TruyenhinhDaNangDRT2", Value="74"},
             //new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="StarMovies", Value="121"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="StarWorld", Value="120"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="TruyenhinhCongAnNhanDanANTV", Value="138"}, 
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="HTV4", Value="142"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="VTVcab5-Echannel", Value="154"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="DIVAUniversal", Value="178"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="AsianFoodChannel", Value="180"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="TruyenhinhHaTinhHTTV", Value="195"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="TruyenhinhHaGiangHTV", Value="199"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="TruyenHinhTienGiangTHTG", Value="203"},
             //new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="HTVThethao", Value="htv-the-thao-11"}, ///////phut 91
             new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="SCTVHDThethao", Value="sctv-the-thao-18"},
             new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="VTC7-TodayTV", Value="today-tv-36"},
             new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="CINEMAX", Value="cenimax-125"},
             new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="CinemaWorldHD", Value="cenimax-world-hd-126"},
            // new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="HTVCPHIM", Value="htv-phim-hd-37"}, //htvc phim
             //new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="ThuanViet", Value="htvc-thuan-viet-42"},
             //new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="HTVCPHUNU", Value="htv-phu-nu-121"},
             //new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="HTVCDuLichCuocSong", Value="htv-du-lich-cuoc-song-122"},
             //new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="HTVCGIADINH", Value="htv-gia-dinh-127"},
             new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="SCTV1", Value="sctv1-103"},
             new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="SCTV4", Value="sctv4-106"},
             new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="SCTV9", Value="sctv9-111"},
             new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="SCTV13-TVF", Value="sctv13-115"},
             new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="SCTV14", Value="sctv14-116"},
             new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="SCTV15", Value="sctv15-20"},
             new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="SCTV16", Value="sctv16-117"},
             new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="SCTVPhimtonghop", Value="sctv-phim-tong-hop-118"},
             //new ChannelToServer(){Server=FROM_HTV3TV_PAGE, ChannelKey="HTV3", Value=""},       // start from 06:00   
             //new ChannelToServer(){Server=FROM_HTV2CHANNEL_PAGE, ChannelKey="HTV2", Value=""},       // start from 06:00 
             new ChannelToServer(){Server=FROM_QPVN_PAGE, ChannelKey="TruyenhinhQuocPhongQPVN", Value=""},
             new ChannelToServer(){Server=FROM_HTVONLINE_PAGE, ChannelKey="HTV1", Value="22"},
             new ChannelToServer(){Server=FROM_HTVONLINE_PAGE, ChannelKey="HTVCDuLichCuocSong", Value="39"},///// phut 91
             new ChannelToServer(){Server=FROM_HTVONLINE_PAGE, ChannelKey="HTVCGIADINH", Value="45"},///// phut 91,
             new ChannelToServer(){Server=FROM_HTVONLINE_PAGE, ChannelKey="HTVCCANHAC", Value="47"},
             new ChannelToServer(){Server=FROM_HTVONLINE_PAGE, ChannelKey="HTVThethao", Value="48"},///// phut 91,
             new ChannelToServer(){Server=FROM_HTVONLINE_PAGE, ChannelKey="HTV3", Value="49"},       // FROM_HTV3TV_PAGE start from 00:00   
             new ChannelToServer(){Server=FROM_HTVONLINE_PAGE, ChannelKey="HTVC+", Value="51"}, 
             new ChannelToServer(){Server=FROM_HTVONLINE_PAGE, ChannelKey="HTVCPHUNU", Value="55"}, //phut 91,
             new ChannelToServer(){Server=FROM_HTVONLINE_PAGE, ChannelKey="ThuanViet", Value="109"},///// phut 91,
             new ChannelToServer(){Server=FROM_HTVONLINE_PAGE, ChannelKey="HTVCPHIM", Value="110"},///// phut 91,
             new ChannelToServer(){Server=FROM_HTVONLINE_PAGE, ChannelKey="VTC4-Yeah1Family", Value="116"},///// phut 91,
             new ChannelToServer(){Server=FROM_HTVONLINE_PAGE, ChannelKey="HTV9", Value="75"},       // FROM_HTV2TV_PAGE start from 00:00   
             new ChannelToServer(){Server=FROM_HTVONLINE_PAGE, ChannelKey="HTV7", Value="43"}, 
             new ChannelToServer(){Server=FROM_HTVONLINE_PAGE, ChannelKey="HTV2", Value="122"}, 
             new ChannelToServer(){Server=FROM_HTVONLINE_PAGE, ChannelKey="KBSWorld", Value="168"},
             new ChannelToServer(){Server=FROM_FBNC_PAGE, ChannelKey="HTVCFBNC", Value=""},
             new ChannelToServer(){Server=FROM_VTVCab_PAGE, ChannelKey="StarMovies", Value="4703"},
             new ChannelToServer(){Server=FROM_TRAVINH_PAGE, ChannelKey="TruyenhinhTraVinhTHTV", Value=""},
             new ChannelToServer(){Server=FROM_BPTV_PAGE, ChannelKey="TruyenHinhBinhPhuocBPTV1", Value="THBP1"},
             new ChannelToServer(){Server=FROM_BPTV_PAGE, ChannelKey="TruyenhinhBinhPhuocBPTV2", Value="THBP2"}
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
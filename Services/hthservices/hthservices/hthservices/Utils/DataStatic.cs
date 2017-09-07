using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace hthservices.Utils
{
    public class ChannelToServer
    {
        public string Server { get; set; }
        public string Server2 { get; set; }
        public string ChannelKey { get; set; }
        public string Value { get; set; }
        public string ExtraValue { get; set; }
        public string Value2 { get; set; }
        public string ExtraValue2 { get; set; }
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
        public const string FROM_VIETBAO_PAGE = "http://tv.vietbao.vn";
        public const string FROM_VOV_PAGE = "http://vovtv.vov.vn/Schedule.aspx?";//day=29/05/2016
        public const string FROM_MYTIVI_PAGE = "https://www.mytv.com.vn/module/ajax/ajax_get_schedule.php"; //?channelId=22&dateSchedule=28/05/2016
        public const string FROM_VTC14_PAGE = "http://vtc14.vn/";
        public const string FROM_TRUYENHINHSO_PAGE = "http://truyenhinhso.vn/Pages/Ajax/GetSchedule.aspx?channelid={0}&date={1}";//date=28/05/2016&channelid
        public const string FROM_TVNET_PAGE = "http://vn.tvnet.gov.vn/modules/process.php?option=epg&channel_id={0}&date={1}"; //channel_id=1&date=28/05/2016"
        public const string FROM_PHUT91_PAGE = "http://phut91.com/lich-phat-song/lich-phat-song-{0}.{1}.html"; //lich-phat-song-htvc-thuan-viet-42.30052016.html
        public const string FROM_HTV3TV_PAGE = "http://www.htv3tv.vn/schedules/jview?day={0}"; //day=2016-05-31 start from 06:00
        public const string FROM_HTV2CHANNEL_PAGE = "https://htv2channel.vn/lich-chieu/{0}"; //2016/5/31 start from 06:00
        public const string FROM_QPVN_PAGE = "http://qpvn.vn/ajax/getChannelSchedule?channel=QPVN&date={0}"; //date=03/06/2016
        public const string FROM_HTVPLUS_PAGE = "http://hplus.com.vn/content/ajax_schedule/";
        public const string FROM_FBNC_PAGE = "http://fbnc.vn/schedule/update/?state=all&date={0}";//=Thu+Jun+02+2016
        public const string FROM_VTVCab_PAGE = "http://www.vtvcab.vn/lich-phat-song?day={1}&month={2}&year={3}&channel={0}";//=03&month=06&year=2016&channel=4077
        public const string FROM_TRAVINH_PAGE = "http://travinhtv.vn/thtv/ajax/ajax.php";//ngay:"2016-06-02"
        public const string FROM_BPTV_PAGE = "http://www.bptv.com.vn/?cat=110&kenh={0}&ngay={1}"; //kenh=THBP2&ngay=2016-06-02
        public const string FROM_YOUTV_PAGE = "http://youtv.vn/lich-chieu/{0}.imc"; ///02-06-2016.imc
        public const string FROM_HITV_PAGE = "http://hitv.vn/truyenhinh/";
        public const string FROM_CAMAU_PAGE = "http://ctvcamau.vn/category/lich-phat-song?kenh=TV&ngay={0}"; //ngay=2016-06-02
        public const string FROM_BENTRE_PAGE = "http://thbt.vn/category/lich-phat-song?kenh=THBT-TV&ngay={0}";//2016-06-01
        public const string FROM_QUANGBINH_PAGE = "http://qbtv.vn/jsHienthiLich.aspx?year={0}"; //year=01/06/2016
        public const string FROM_TV24_PAGE = "http://tv24.vn/schedule/filter?id={0}&date={1}"; //id=2&date=01/06/2016
        public const string FROM_LETSVIET_PAGE = "http://letsviet.vn/Control/ListHotProgram.aspx?date={0}&channel=vi";//date=2016-06-03&channel=vi
        public const string FROM_KGTV_PAGE = "http://kgtv.vn/category/lich-phat-song/?kenh={0}&ngay={1}";//kenh=TV&ngay=2016-06-14
        public const string FROM_BRT_PAGE = "http://brt.vn/";//
        public const string FROM_BTV_PAGE = "http://www.btv.org.vn/home/vod/ajax_schedule/";//http://www.btv.org.vn/live/btv1.html
        public const string FROM_LA34_PAGE = "http://la34.com.vn/lichphatsong.php?ngay={0}&kenh=TV";//ngay=2017-09-08
        public const string FROM_CANTHOTV_PAGE = "http://canthotv.vn/";
        public const string FROM_TRT_PAGE = "http://www.trt.com.vn/lichPhatSong/tabid/59/Default.aspx";// default is current date
        public const string FROM_PHUTHOTV_PAGE = "http://phuthotv.vn/Modules/Video/Components/VideoHandler.ashx";//
        public const string FROM_THDT_PAGE = "http://thdt.vn/lich-phat-song/index.html";// default is current date
        public const string FROM_LANGSONTV_PAGE = "http://www.langsontv.vn/";// default is current date
        public const string FROM_VTC16_PAGE = "http://vtc16.vn/ajaxs/tvshow/loadchannel.aspx?channelid={0}&date={1}";//channelid=3&date=16/06/2016
        public const string FROM_TRUELIFE_PAGE = "http://truelife.vn/offica/tvchannel/tvshow/action?_f=6&communityId={0}&d={1}&jsoncallback=stateChanged";// d=20160616
        public const string FROM_ATV_PAGE = "http://atv.org.vn/lich-phat-song.aspx";// default is current date
        public const string FROM_NAMDINHTV_PAGE = "http://namdinhtv.vn/ctv/ajax";// post op:"lichphatsong" data:"06-18|1"
        public const string FROM_HAUGIANGTV_PAGE = "http://haugiangtivi.vn/?page_id=17";// post fDay:"16" fMonth:"6" fYear:"2016"  fKenh:"truyen-hinh"  submit:"Xem"
        public const string FROM_HAIPHONG_PAGE = "http://thp.org.vn/";// get current date
        public const string FROM_SONLA_PAGE = "http://sonlatv.vn/lich-phat-song?loai=truyenhinh&chonngay%5Bvalue%5D%5Bdate%5D={0}";// 18/06/2016
        public const string FROM_THVL_PAGE = "http://www.thvl.vn/?cat=40&kenh={0}&ngay={1}";// 2016-08-13
        public const string FROM_KPLUS_PAGE = "http://www.kplus.vn/Schedule/getSchedule";

        public const string FROM_SCTV_PAGE = "http://www.sctv.com.vn/lich-phat-song.html";
        public const string FROM_THST_PAGE = "http://thst.vn/Home/NewLichPhatSong";// default is current date
        public const string FROM_BINHDINHTV_PAGE = "http://www.binhdinhtv.vn/tvschedule.php";// default is current date
        public const string FROM_GIALAITV_PAGE = "http://gialaitv.vn/lich-phat-song/?ngay={0}&kenh=1";// 2016-08-20
        public const string FROM_DRT_PAGE = "http://www.drt.danang.vn/default.aspx";
        public const string FROM_MOBITV_PAGE = "http://mobitv.net.vn/lich-phat-song/";
        public const string FROM_TODAYTV_PAGE = "http://todaytv.vn/lichchieu";
        
        #endregion

        #region Channel to server
        public static readonly List<ChannelToServer> ChannelsToServers = new List<ChannelToServer>(){
             new ChannelToServer(){Server=FROM_VOV_PAGE, ChannelKey="VOVTV", Value=""},
             //new ChannelToServer(){Server=FROM_TRUYENHINHSO_PAGE, ChannelKey="VTCHD3", Value="42"},
             //new ChannelToServer(){Server=FROM_TRUYENHINHSO_PAGE, ChannelKey="VTC3", Value="42"},
             //new ChannelToServer(){Server=FROM_TRUYENHINHSO_PAGE, ChannelKey="DisneyChannel", Value="378"},
             //new ChannelToServer(){Server=FROM_TVNET_PAGE, ChannelKey="VTCHD1", Value="1"},
             //new ChannelToServer(){Server=FROM_TVNET_PAGE, ChannelKey="VTC1", Value="1"},
             //new ChannelToServer(){Server=FROM_TVNET_PAGE, ChannelKey="VTC10-NetViet", Value="4"},
             //new ChannelToServer(){Server=FROM_TVNET_PAGE, ChannelKey="VTC16-3NTV", Value="5"},
             new ChannelToServer(){Server=FROM_TVNET_PAGE, ChannelKey="TruyenhinhThongtanxaVietNamTTXVN", Value="75"},
             //new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="K+1", Value="187"},
             //new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="K+NS", Value="188"},
             //new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="K+PM", Value="189"},
             //new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="K+PC", Value="205"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="NATIONALGEOGRAPHICCHANNEL", Value="7"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="HBO", Value="9"},
             //new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="StarMovies", Value="11"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="Channel[V]", Value="13"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="FoxSport", Value="14"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="FoxSport2", Value="15"},
             //new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="HTV7", Value="19"},
             //new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="HTV9", Value="20"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="AXN", Value="21"},
             //new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="SCTV8-VITV", Value="27"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="CartoonNetworks", Value="34"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="DISCOVERY", Value="45"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="TRAVEL&LIVING", Value="46"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="AnimalPlanet", Value="47"},
             //new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="TruyenHinhVinhLongTHVL1", Value="65"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="TruyenhinhDaNangDRT2", Value="74"},
             //new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="StarMovies", Value="121"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="StarWorld", Value="120"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="TruyenhinhCongAnNhanDanANTV", Value="138"}, 
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="HTV4", Value="142"},
             //new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="VTVcab5-Echannel", Value="154"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="DIVAUniversal", Value="178"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="AsianFoodChannel", Value="180"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="TruyenhinhHaTinhHTTV", Value="195"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="TruyenhinhHaGiangHTV", Value="199"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="TruyenHinhTienGiangTHTG", Value="203"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="NHKWORLD", Value="126"},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="VTV1", Value="1", Server2=FROM_VIETBAO_PAGE},
             new ChannelToServer(){Server=FROM_MYTIVI_PAGE, ChannelKey="VTV3", Value="2", Server2=FROM_VIETBAO_PAGE},
             //new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="HTVThethao", Value="htv-the-thao-11"}, ///////phut 91
             //new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="SCTVHDThethao", Value="sctv-the-thao-18"},
             //new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="CINEMAX", Value="cenimax-125"},
             new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="CinemaWorldHD", Value="cenimax-world-hd-126"},
            // new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="HTVCPHIM", Value="htv-phim-hd-37"}, //htvc phim
             //new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="ThuanViet", Value="htvc-thuan-viet-42"},
             //new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="HTVCPHUNU", Value="htv-phu-nu-121"},
             //new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="HTVCDuLichCuocSong", Value="htv-du-lich-cuoc-song-122"},
             //new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="HTVCGIADINH", Value="htv-gia-dinh-127"},
             //new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="SCTV1", Value="sctv1-103"},
             //new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="SCTV4", Value="sctv4-106"},
             //new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="SCTV9", Value="sctv9-111"},
             //new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="SCTV13-TVF", Value="sctv13-115"},
             //new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="SCTV14", Value="sctv14-116"},
             //new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="SCTV15", Value="sctv15-20"},
             //new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="SCTV16", Value="sctv16-117"},
             //new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="SCTVPhimtonghop", Value="sctv-phim-tong-hop-118"},
             //new ChannelToServer(){Server=FROM_HTV3TV_PAGE, ChannelKey="HTV3", Value=""},       // start from 06:00   
             //new ChannelToServer(){Server=FROM_HTV2CHANNEL_PAGE, ChannelKey="HTV2", Value=""},       // start from 06:00 
             new ChannelToServer(){Server=FROM_QPVN_PAGE, ChannelKey="TruyenhinhQuocPhongQPVN", Value=""},
             new ChannelToServer(){Server=FROM_HTVPLUS_PAGE, ChannelKey="HTV1", Value="http://hplus.com.vn/xem-kenh-htv1-2631.html"},
            //new ChannelToServer(){Server=FROM_HTVONLINE_PAGE, ChannelKey="HTVCDuLichCuocSong", Value="39"},///// phut 91
            // new ChannelToServer(){Server=FROM_HTVONLINE_PAGE, ChannelKey="HTVCGIADINH", Value="45"},///// phut 91,
            // new ChannelToServer(){Server=FROM_HTVONLINE_PAGE, ChannelKey="HTVCCANHAC", Value="47"},
           //  new ChannelToServer(){Server=FROM_HTVONLINE_PAGE, ChannelKey="HTVThethao", Value="48"},///// phut 91,
             new ChannelToServer(){Server=FROM_HTVPLUS_PAGE, ChannelKey="HTV3", Value="http://hplus.com.vn/xem-kenh-htv3-2535.html"},       // FROM_HTV3TV_PAGE start from 00:00   
          //   new ChannelToServer(){Server=FROM_HTVONLINE_PAGE, ChannelKey="HTVC+", Value="51"}, 
         //    new ChannelToServer(){Server=FROM_HTVONLINE_PAGE, ChannelKey="HTVCPHUNU", Value="55"}, //phut 91,
             new ChannelToServer(){Server=FROM_HTVPLUS_PAGE, ChannelKey="ThuanViet", Value="http://hplus.com.vn/xem-kenh-htvc-thuan-viet-2396.html"},///// phut 91,
             new ChannelToServer(){Server=FROM_HTVPLUS_PAGE, ChannelKey="HTVCPHIM", Value="http://hplus.com.vn/xem-kenh-htvc-phim-hd-2399.html"},///// phut 91,
            // new ChannelToServer(){Server=FROM_HTVONLINE_PAGE, ChannelKey="VTC4-Yeah1Family", Value="116"},///// phut 91,
             new ChannelToServer(){Server=FROM_HTVPLUS_PAGE, ChannelKey="HTV9", Value="http://hplus.com.vn/xem-kenh-htv9-hd-2667.html"},       // FROM_HTV2TV_PAGE start from 00:00   
             new ChannelToServer(){Server=FROM_HTVPLUS_PAGE, ChannelKey="HTV7", Value="http://hplus.com.vn/xem-kenh-htv7-hd-256.html"}, 
             new ChannelToServer(){Server=FROM_HTVPLUS_PAGE, ChannelKey="HTV2", Value="http://hplus.com.vn/xem-kenh-htv2-hd-2669.html"}, 
       //      new ChannelToServer(){Server=FROM_HTVONLINE_PAGE, ChannelKey="KBSWorld", Value="168"},
           
             new ChannelToServer(){Server=FROM_FBNC_PAGE, ChannelKey="HTVCFBNC", Value=""},

             new ChannelToServer(){Server=FROM_VTVCab_PAGE, ChannelKey="StarMovies", Value="4703"},
             new ChannelToServer(){Server=FROM_VTVCab_PAGE, ChannelKey="VTVcab1-GiaitriTV", Value="224"},
             new ChannelToServer(){Server=FROM_VTVCab_PAGE, ChannelKey="VTVcab2-PhimViet", Value="4077"},
             new ChannelToServer(){Server=FROM_VTVCab_PAGE, ChannelKey="VTVcab3-TheThaoTV", Value="226"},
             new ChannelToServer(){Server=FROM_VTVCab_PAGE, ChannelKey="VTVcab4-Vanhoa", Value="227"},
             new ChannelToServer(){Server=FROM_VTVCab_PAGE, ChannelKey="VTVcab5-Echannel", Value="228"},
             new ChannelToServer(){Server=FROM_VTVCab_PAGE, ChannelKey="VTVcab6-HayTV", Value="229"},
             new ChannelToServer(){Server=FROM_VTVCab_PAGE, ChannelKey="VTVcab7-DDramas", Value="232"},
             new ChannelToServer(){Server=FROM_VTVCab_PAGE, ChannelKey="VTVcab8-BiBi", Value="4068"}, //nodata
             new ChannelToServer(){Server=FROM_VTVCab_PAGE, ChannelKey="VTVcab9-INFOTV", Value="4291"},
             new ChannelToServer(){Server=FROM_VTVCab_PAGE, ChannelKey="VTVcab10-O2TV", Value="4069"},//nodata
             //new ChannelToServer(){Server=FROM_VTVCab_PAGE, ChannelKey="VTVcab11-TVShopping", Value=""},
             new ChannelToServer(){Server=FROM_VTVCab_PAGE, ChannelKey="VTVcab12-StyleTV", Value="4079"},
             //new ChannelToServer(){Server=FROM_VTVCab_PAGE, ChannelKey="VTVcab14-LotteShopping", Value=""},
             new ChannelToServer(){Server=FROM_VTVCab_PAGE, ChannelKey="VTVcab15-InvestTV", Value="355"},//nodata
             new ChannelToServer(){Server=FROM_VTVCab_PAGE, ChannelKey="VTVcab16-BongdaTV", Value="4080"},
             new ChannelToServer(){Server=FROM_VTVCab_PAGE, ChannelKey="VTVcab17-Yeah1TV", Value="4070"},
             new ChannelToServer(){Server=FROM_VTVCab_PAGE, ChannelKey="VTVcab19-KenhPhim", Value="4071"},
             new ChannelToServer(){Server=FROM_VTVCab_PAGE, ChannelKey="VTVcab20-VFamily", Value="17134"},
             new ChannelToServer(){Server=FROM_VTVCab_PAGE, ChannelKey="VTVcab21", Value="23926"}, //no client yet
             new ChannelToServer(){Server=FROM_VTVCab_PAGE, ChannelKey="WarnerTV", Value="4707"},
             new ChannelToServer(){Server=FROM_VTVCab_PAGE, ChannelKey="DisneyJunior", Value="4109"},

             new ChannelToServer(){Server=FROM_KPLUS_PAGE, ChannelKey="K+1", Value="7008", ExtraValue="11", Server2=FROM_MYTIVI_PAGE, Value2="187"},
             new ChannelToServer(){Server=FROM_KPLUS_PAGE, ChannelKey="K+NS", Value="7010", ExtraValue="11", Server2=FROM_MYTIVI_PAGE, Value2="188"},
             new ChannelToServer(){Server=FROM_KPLUS_PAGE, ChannelKey="K+PM", Value="7012", ExtraValue="11", Server2=FROM_MYTIVI_PAGE, Value2="189"},
             new ChannelToServer(){Server=FROM_KPLUS_PAGE, ChannelKey="K+PC", Value="7014", ExtraValue="11", Server2=FROM_MYTIVI_PAGE, Value2="205"},
             new ChannelToServer(){Server=FROM_KPLUS_PAGE, ChannelKey="DiscoveryChannel", Value="7057", ExtraValue="8"},
             new ChannelToServer(){Server=FROM_KPLUS_PAGE, ChannelKey="MTVVietNam", Value="4533", ExtraValue="3"},
             new ChannelToServer(){Server=FROM_KPLUS_PAGE, ChannelKey="DisneyChannel", Value="4536", ExtraValue="3"},
             new ChannelToServer(){Server=FROM_KPLUS_PAGE, ChannelKey="TV5Monde", Value="7068", ExtraValue="7"},

             new ChannelToServer(){Server=FROM_TRAVINH_PAGE, ChannelKey="TruyenhinhTraVinhTHTV", Value=""},
             new ChannelToServer(){Server=FROM_BPTV_PAGE, ChannelKey="TruyenHinhBinhPhuocBPTV1", Value="THBP1"},
             new ChannelToServer(){Server=FROM_BPTV_PAGE, ChannelKey="TruyenhinhBinhPhuocBPTV2", Value="THBP2"},
             new ChannelToServer(){Server=FROM_YOUTV_PAGE, ChannelKey="YOUTV", Value=""},
             new ChannelToServer(){Server=FROM_HITV_PAGE, ChannelKey="HiTV", Value=""},
             new ChannelToServer(){Server=FROM_CAMAU_PAGE, ChannelKey="TruyenhinhCaMauCTV", Value=""},
             new ChannelToServer(){Server=FROM_BENTRE_PAGE, ChannelKey="TruyenHinhBenTreTHBT", Value=""},
             new ChannelToServer(){Server=FROM_QUANGBINH_PAGE, ChannelKey="TruyenhinhQuangBinhQBTV", Value=""},
             /*
             new ChannelToServer(){Server=FROM_TV24_PAGE, ChannelKey="SCTVPhimtonghop", Value="1"}, //phut 91
             new ChannelToServer(){Server=FROM_TV24_PAGE, ChannelKey="SCTV1", Value="2"}, //phut 91
             new ChannelToServer(){Server=FROM_TV24_PAGE, ChannelKey="SCTV9", Value="16"}, //phut 91
             new ChannelToServer(){Server=FROM_TV24_PAGE, ChannelKey="SCTV4", Value="5"}, //phut 91
             new ChannelToServer(){Server=FROM_TV24_PAGE, ChannelKey="SCTV6-SNTV", Value="7"}, //phut 91
             new ChannelToServer(){Server=FROM_TV24_PAGE, ChannelKey="SCTV7", Value="8"}, //phut 91
             new ChannelToServer(){Server=FROM_TV24_PAGE, ChannelKey="SCTV13-TVF", Value="105"}, //phut 91
             new ChannelToServer(){Server=FROM_TV24_PAGE, ChannelKey="SCTV14", Value="36"}, //phut 91
             new ChannelToServer(){Server=FROM_TV24_PAGE, ChannelKey="SCTV15", Value="10"}, //phut 91
             new ChannelToServer(){Server=FROM_TV24_PAGE, ChannelKey="SCTV16", Value="103"}, //phut 91
             new ChannelToServer(){Server=FROM_TV24_PAGE, ChannelKey="SCTVHDThethao", Value="109"}, //phut 91
             new ChannelToServer(){Server=FROM_TV24_PAGE, ChannelKey="SCTV11", Value="61"}, 
             new ChannelToServer(){Server=FROM_TV24_PAGE, ChannelKey="SCTV12", Value="62"}, 
             */
             new ChannelToServer(){Server=FROM_SCTV_PAGE, ChannelKey="SCTVPhimtonghop", Value="1"}, 
             new ChannelToServer(){Server=FROM_SCTV_PAGE, ChannelKey="SCTV1", Value="2"}, 
             new ChannelToServer(){Server=FROM_SCTV_PAGE, ChannelKey="SCTV2-YanTV", Value="3"}, 
             new ChannelToServer(){Server=FROM_SCTV_PAGE, ChannelKey="SCTV3-SaoTV", Value="24"},
             new ChannelToServer(){Server=FROM_SCTV_PAGE, ChannelKey="SCTV4", Value="5"},
             new ChannelToServer(){Server=FROM_SCTV_PAGE, ChannelKey="SCTV5-SCJTVShopping", Value="6"},
             new ChannelToServer(){Server=FROM_SCTV_PAGE, ChannelKey="SCTV6-SNTV", Value="7"},
             new ChannelToServer(){Server=FROM_SCTV_PAGE, ChannelKey="SCTV7", Value="8"},
             new ChannelToServer(){Server=FROM_SCTV_PAGE, ChannelKey="SCTV8-VITV", Value="9"}, //no data
             new ChannelToServer(){Server=FROM_SCTV_PAGE, ChannelKey="SCTV9", Value="16"},
             new ChannelToServer(){Server=FROM_SCTV_PAGE, ChannelKey="SCTV10-HomeshoppingNetwork", Value="77"},//no data
             new ChannelToServer(){Server=FROM_SCTV_PAGE, ChannelKey="SCTV11", Value="61"}, 
             new ChannelToServer(){Server=FROM_SCTV_PAGE, ChannelKey="SCTV12", Value="62"}, 
             new ChannelToServer(){Server=FROM_SCTV_PAGE, ChannelKey="SCTV13-TVF", Value="105"},
             new ChannelToServer(){Server=FROM_SCTV_PAGE, ChannelKey="SCTV14", Value="36"},
             new ChannelToServer(){Server=FROM_SCTV_PAGE, ChannelKey="SCTV15", Value="10"},
             new ChannelToServer(){Server=FROM_SCTV_PAGE, ChannelKey="SCTV16", Value="103"},
             new ChannelToServer(){Server=FROM_SCTV_PAGE, ChannelKey="SCTVHDThethao", Value="109"},
             new ChannelToServer(){Server=FROM_SCTV_PAGE, ChannelKey="DW", Value="91"},
             new ChannelToServer(){Server=FROM_SCTV_PAGE, ChannelKey="StarWorld", Value="82"},
             new ChannelToServer(){Server=FROM_SCTV_PAGE, ChannelKey="CINEMAX", Value="76"},
             
             new ChannelToServer(){Server=FROM_MOBITV_PAGE, ChannelKey="MienTay-THDT2", Value="Mien+Tay"},
             new ChannelToServer(){Server=FROM_MOBITV_PAGE, ChannelKey="AnNinhTheGioi", Value="ANTG"},
             new ChannelToServer(){Server=FROM_MOBITV_PAGE, ChannelKey="AnVien-BTV9", Value="An+Viên"},
             new ChannelToServer(){Server=FROM_MOBITV_PAGE, ChannelKey="Phimhay", Value="Phim+Hay"},
             new ChannelToServer(){Server=FROM_MOBITV_PAGE, ChannelKey="VietTeen-BTV62idol", Value="Viet+Teen"},
             new ChannelToServer(){Server=FROM_MOBITV_PAGE, ChannelKey="SAM-BTV11", Value="SAM"},
             
             new ChannelToServer(){Server=FROM_KGTV_PAGE, ChannelKey="TruyenhinhKienGiang", Value="TV"}, //TV2
             new ChannelToServer(){Server=FROM_BRT_PAGE, ChannelKey="TruyenhinhBaRia-VungTauBRT", Value=""},
             new ChannelToServer(){Server=FROM_BTV_PAGE, ChannelKey="TruyenhinhBinhDuongBTV1", Value="1"},
             new ChannelToServer(){Server=FROM_BTV_PAGE, ChannelKey="TruyenhinhBinhDuongBTV2", Value="2"},
             new ChannelToServer(){Server=FROM_LA34_PAGE, ChannelKey="TruyenHinhLongAnLA34", Value=""},
             new ChannelToServer(){Server=FROM_CANTHOTV_PAGE, ChannelKey="TruyenhinhCanThoTHTPCT", Value=""},
             new ChannelToServer(){Server=FROM_TRT_PAGE, ChannelKey="TruyenHinhThuaThienHueTRT", Value=""},
             new ChannelToServer(){Server=FROM_PHUTHOTV_PAGE, ChannelKey="TruyenhinhPhuThoPTV", Value=""},
             new ChannelToServer(){Server=FROM_THDT_PAGE, ChannelKey="TruyenhinhDongThapTHDT", Value=""},
             new ChannelToServer(){Server=FROM_LANGSONTV_PAGE, ChannelKey="TruyenhinhLangSonLSTV", Value=""},
             new ChannelToServer(){Server=FROM_VTC16_PAGE, ChannelKey="VTC1", Value="1"},
             new ChannelToServer(){Server=FROM_VTC16_PAGE, ChannelKey="VTCHD1", Value="1"},
             new ChannelToServer(){Server=FROM_VTC16_PAGE, ChannelKey="VTC2", Value="2"},
             new ChannelToServer(){Server=FROM_VTC16_PAGE, ChannelKey="VTCHD2", Value="2"},
             new ChannelToServer(){Server=FROM_VTC16_PAGE, ChannelKey="VTC3", Value="3"},
             new ChannelToServer(){Server=FROM_VTC16_PAGE, ChannelKey="VTCHD3", Value="3"},
             new ChannelToServer(){Server=FROM_VTC16_PAGE, ChannelKey="VTC6", Value="4"},
             new ChannelToServer(){Server=FROM_TODAYTV_PAGE, ChannelKey="VTC7-TodayTV", Value=""},
             //new ChannelToServer(){Server=FROM_PHUT91_PAGE, ChannelKey="VTC7-TodayTV", Value="today-tv-36"},
             new ChannelToServer(){Server=FROM_LETSVIET_PAGE, ChannelKey="VTC9-LETSVIET", Value=""}, 
             new ChannelToServer(){Server=FROM_VTC16_PAGE, ChannelKey="VTC10-NetViet", Value="7"},
             new ChannelToServer(){Server=FROM_VTC16_PAGE, ChannelKey="VTC12", Value="6"},
             //new ChannelToServer(){Server=FROM_VTC16_PAGE, ChannelKey="VTC14", Value="8"},
             new ChannelToServer(){Server=FROM_VTC14_PAGE, ChannelKey="VTC14", Value=""},
             new ChannelToServer(){Server=FROM_VTC16_PAGE, ChannelKey="VTC16-3NTV", Value="9"},
             new ChannelToServer(){Server=FROM_TRUELIFE_PAGE, ChannelKey="TruyenhinhLamDongLTV", Value="10804"},
             new ChannelToServer(){Server=FROM_TRUELIFE_PAGE, ChannelKey="TruyenhinhNgheAnNTV", Value="9463481"},
             new ChannelToServer(){Server=FROM_TRUELIFE_PAGE, ChannelKey="TruyenhinhNinhBinhNTV", Value="3566981"},
             new ChannelToServer(){Server=FROM_ATV_PAGE, ChannelKey="TruyenHinhAnGiangATV", Value=""},
             new ChannelToServer(){Server=FROM_NAMDINHTV_PAGE, ChannelKey="TruyenhinhNamDinhNTV", Value=""},
             new ChannelToServer(){Server=FROM_HAUGIANGTV_PAGE, ChannelKey="TruyenHinhHauGiangHGTV", Value=""},
             new ChannelToServer(){Server=FROM_HAIPHONG_PAGE, ChannelKey="TruyenhinhHaiPhongTHP", Value=""},
             new ChannelToServer(){Server=FROM_SONLA_PAGE, ChannelKey="TruyenhinhSonLaSTV", Value=""},
             new ChannelToServer(){Server=FROM_THVL_PAGE, ChannelKey="TruyenHinhVinhLongTHVL1", Value="THVL1"},
             new ChannelToServer(){Server=FROM_THVL_PAGE, ChannelKey="TruyenHinhVinhLongTHVL2", Value="THVL2"},
             new ChannelToServer(){Server=FROM_THST_PAGE, ChannelKey="TruyenhinhSocTrangSTV", Value=""},
             new ChannelToServer(){Server=FROM_BINHDINHTV_PAGE, ChannelKey="TruyenhinhBinhDinhBTV", Value=""},
             new ChannelToServer(){Server=FROM_GIALAITV_PAGE, ChannelKey="TruyenhinhGiaLaiTHGL", Value=""},
             new ChannelToServer(){Server2=FROM_DRT_PAGE, ChannelKey="TruyenhinhDaNangDRT1", Value2="1", Server=FROM_MYTIVI_PAGE, Value="73"},
             new ChannelToServer(){Server2=FROM_DRT_PAGE, ChannelKey="TruyenhinhDaNangDRT2", Value2="2", Server=FROM_MYTIVI_PAGE, Value="74"}
             
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

        public static ChannelToServer GetChannelToServer2(string channelKey, ChannelToServer channelToServer)
        {
            if (channelToServer != null && !string.IsNullOrEmpty(channelToServer.Server2))
            {
                return new ChannelToServer() { 
                    Server = channelToServer.Server2, 
                    ChannelKey = channelKey, 
                    Value = channelToServer.Value2,
                    ExtraValue = channelToServer.ExtraValue2
                }; ;
            }
            return null;
        }

        public static List<ChannelToServer> GetListChannelToServer(string channelKey)
        {
            List<ChannelToServer> channelToServers = new List<ChannelToServer>();
            var channelToServer = ChannelsToServers.Where(p => p.ChannelKey.Equals(channelKey, StringComparison.OrdinalIgnoreCase)).ToList();
            if (channelToServer != null)
            {
                channelToServers.AddRange(channelToServer);
            }
            channelToServers.Add(new ChannelToServer() { Server = "", ChannelKey = channelKey, Value = "" });
            return channelToServers;
        }
    }
}
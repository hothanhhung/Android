package com.hth.docbaotonghop;

import com.hth.utils.FileProcess;

public enum WebsitePage {

	NgoiSaoDotNet(1, "NgoiSaoDotNet.css", "NgoiSao.Net", "http://ngoisao.net", "http://ngoisao.net", R.drawable.icon_ngoisao),
	VNExpressDotNet(2, "VNExpressDotNet.css", "VNExpress.Net", "http://vnexpress.net", "http://vnexpress.net", R.drawable.icon_vnexpress),
	VietNamNetDotVN(3, "VietNamNetDotVN.css", "VietNamNet.VN", "http://vietnamnet.vn", "http://m.vietnamnet.vn", R.drawable.icon_vietnamnet),
	NLDDotComDotVN(4, "NLDDotComDotVN.css", "NLD.Com.VN", "http://nld.com.vn", "http://m.nld.com.vn", R.drawable.icon_nld),
	ThanhNienDotComDotVN(5, "ThanhNienDotComDotVN.css", "ThanhNien.Com.VN", "http://www.thanhnien.com.vn", "http://m.thanhnien.com.vn", R.drawable.icon_thanhnien),
	VTCDotVN(6, "VTCDotVN.css", "VTC.VN", "http://vtc.vn", "http://m.vtc.vn", R.drawable.icon_vtcvn),
	NguoiDuaTinDotVN(7, "NguoiDuaTinDotVN.css", "NguoiDuaTin.VN", "http://www.nguoiduatin.vn", "http://m.nguoiduatin.vn", R.drawable.icon_nguoiduatin),
	YahooNews(8, "YahooNews.css", "YahooNews", "https://vn.news.yahoo.com", "https://vn.news.yahoo.com", R.drawable.icon_yahoonews),
	XaLuanDotCom(9, "XaLuanDotCom.css", "XaLuan.Com", "http://www.xaluan.com", "http://www.xaluan.com/m.php", R.drawable.icon_xaluan),
	ZingDotVN(10, "ZingDotVN.css", "Zing.VN", "http://news.zing.vn", "http://news.zing.vn", R.drawable.icon_zing),
	HaiSaoDotVN(11, "HaiSaoDotVN.css", "HaiSao.VN", "http://2sao.vn", "http://m.2sao.vn", R.drawable.icon_2sao),
	GiaiTriVietDotCom(12, "GiaiTriVietDotCom.css", "VietGiaiTri.Com", "http://www.vietgiaitri.com", "http://m.vietgiaitri.com/", R.drawable.icon_vietgiaitri),
    DantriDotComDotVN(13, "DantriDotComDotVN.css", "DanTri.Com.VN", "http://dantri.com.vn", "http://m.dantri.com.vn", R.drawable.icon_dantri),
    TinMoiDotVN(14, "TinMoiDotVN.css", "TinMoi.VN", "http://www.tinmoi.vn", "http://m.tinmoi.vn", R.drawable.icon_tinmoi),
    Kenh14DotVN(15, "Kenh14DotVN.css", "Kenh14.VN", "http://www.kenh14.vn", "http://m.kenh14.vn", R.drawable.icon_kenh14),
    TiinDotVN(16, "TiinDotVN.css", "Tiin.VN", "http://www.tiin.vn", "http://m.tiin.vn/", R.drawable.icon_tiin),
    DoiSongPhapLuatDotCom(17, "DoiSongPhapLuatDotCom.css", "DoiSongPhapLuat.com", "http://m.doisongphapluat.com/", "http://www.doisongphapluat.com", R.drawable.icon_doisongphapluat),
    HaiBonHDotComDotVN(18, "HaiBonHDotComDotVN.css", "24h.Com.vn", "http://24h.com.vn", "http://m.24h.com.vn/", R.drawable.icon_24h),
	TuoiTreDotVN(19, "TuoiTreDotVN.css", "tuoitre.vn", "http://tuoitre.vn/", "http://tuoitre.vn/", R.drawable.icon_tuoitre),
    NgoiSaoDotVN(20, "NgoiSaoDotVN.css", "ngoisao.vn", "http://ngoisao.vn/", "http://m.ngoisao.vn/", R.drawable.icon_ngoisaovn),
    BaoDatVietDotVN(21, "BaoDatVietDotVN.css", "baodatviet.vn", "http://baodatviet.vn/", "http://m.baodatviet.vn/", R.drawable.icon_baodatviet),
    SoHaDotVN(22, "SoHaDotVN.css", "soha.vn", "http://soha.vn/", "http://m.soha.vn/", R.drawable.icon_soha),
    TinTucOnlineDotComDotVN(23, "TinTucOnlineDotComDotVN.css", "tintuconline.com.vn", "http://m.tintuconline.com.vn/", "http://m.tintuconline.com.vn/", R.drawable.icon_tintuconline),
    VietNamPlusDotVN(24, "VietNamPlusDotVN.css", "www.vietnamplus.vn", "http://www.vietnamplus.vn/", "http://www.vietnamplus.vn/", R.drawable.icon_vietnamplus),
	VOVDotVN(25, "VOVDotVN.css", "vov.vn", "http://vov.vn/", "http://m.vov.vn/", R.drawable.icon_vov),
	BongDaPlusDotVN(26, "BongDaPlusDotVN.css", "bongdaplus.vn", "http://m.bongdaplus.vn/", "http://m.bongdaplus.vn/", R.drawable.icon_bongdaplus),
	TheThao247DotVN(27, "TheThao247DotVN.css", "thethao247.vn", "http://thethao247.vn/", "http://m.thethao247.vn/", R.drawable.icon_thethao247),
	BongDaDotComDotVN(28, "BongDaDotComDotVN.css", "bongda.com.vn", "http://www.bongda.com.vn/", "http://www.bongda.com.vn/", R.drawable.icon_bongda),
	TheThaoVanHoaDotVN(29, "TheThaoVanHoaDotVN.css", "thethaovanhoa.vn", "http://thethaovanhoa.vn/", "http://thethaovanhoa.vn/", R.drawable.icon_thethaovanhoa),
	ICTNewsDotVN(30, "ICTNewsDotVN.css", "ictnews.vn", "http://ictnews.vn/", "http://ictnews.vn/", R.drawable.icon_ictnews),
	VNReviewDotVN(31, "VNReviewDotVN.css", "vnreview.vn", "http://vnreview.vn/", "http://m.vnreview.vn/", R.drawable.icon_vnreview),
	GenkDotVN(32, "GenkDotVN.css", "genk.vn", "http://genk.vn/", "http://m.genk.vn/", R.drawable.icon_genk),
	TinhTeDotVN(33, "TinhTeDotVN.css", "tinhte.vn", "https://www.tinhte.vn/", "https://www.tinhte.vn/", R.drawable.icon_tinhte),
	BaoMoiDotCom(34, "BaoMoiDotCom.css", "baomoi.com", "http://www.baomoi.com/", "http://m.baomoi.com/", R.drawable.icon_baomoi),
	VietTimesDotVN(35, "VietTimesDotVN.css", "viettimes.vn", "http://viettimes.vn/", "http://m.viettimes.vn/", R.drawable.icon_viettimes),
    DaiKyNguynVNDotCom(36, "DaiKyNgueynVNDotCom.css", "daikynguyenvn.com", "http://daikynguyenvn.com/", "http://mb.daikynguyenvn.com/", R.drawable.icon_daikynguyen),
	GiaDinhDotNet(37, "GiaDinhDotNetDotVN.css", "giadinh.net.vn", "http://giadinh.net.vn/", "http://m.giadinh.net.vn/", R.drawable.icon_giadinh);


	private String stringValue;
	private String homePageLink;
    private int intValue;
    private int iconID;
    private String cssUrl;
    private String cssContent;
    
    private final String resourceUrl = "";
    public static boolean isHideAds = true;
    
    public String GetReformatCssContent()
    {
    	if(!isHideAds) return "";

    	if(this.cssContent == null || this.cssContent.isEmpty())
    	{
    		this.cssContent = FileProcess.readTextFileFromAssets(GetCssUrl(), MainActivity.getAppContext().getAssets());
    	}
    	
    	if(this.cssContent == null || this.cssContent.isEmpty())
    	{
    		this.cssContent =  ".adsbygoogle {display:none !important;}";
    	}
    	return this.cssContent;
    }
    
    public String GetReformatCss()
    {
    	return "<LINK href=\""+GetCssUrl()+"\" type=\"text/css\" rel=\"stylesheet\"/>";
    }
    public String GetCssUrl()
    {
    	if(this.cssUrl == null || this.cssUrl.trim().isEmpty())
    	{
    		return resourceUrl + "removestyle.css";
    	}else
    	{
    		return resourceUrl + this.cssUrl;
    	}
    }
    
    public String getHomePageMobile() {
        return homePageMobile.trim();
    }

    private String homePageMobile;
    
    private WebsitePage(int intValue, String cssUrl, String strValue, String homePageLink, String homePageMobile, int iconID)
	{
		this.stringValue = strValue;
	    this.intValue = intValue;
	    this.cssUrl = cssUrl;
	    this.iconID = iconID;
	    this.homePageLink = homePageLink;
        this.homePageMobile = homePageMobile;
	}
    
	@Override
    public String toString() {
        return stringValue;
    }
	
    public int toInt() {
        return this.intValue;
    }
    
    public String getHomePageLink()
    {
    	return this.homePageLink;
    }
    
    public int getIcon()
    {
    	return this.iconID;
    }
    
    public static WebsitePage valueOf(int value)
    {
    	WebsitePage [] listWbPage = WebsitePage.values();
    	for(int i = 0; i < listWbPage.length; i++)
    		if(listWbPage[i].toInt() == value) return listWbPage[i];
    	return null;
    }
	
};

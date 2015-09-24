using UnityEngine;
using GoogleMobileAds.Api;

public class AdmobService{

	private const string adUnitId = "ca-app-pub-4576847792571626/7489703991";
	private const string adUnitIdInterstitial = "ca-app-pub-4576847792571626/8966437193";

	public static void RequestBanner()
	{
		// Create a 320x50 banner at the top of the screen.
		BannerView bannerView = new BannerView(adUnitId, AdSize.Banner, AdPosition.Bottom);
		// Create an empty ad request.
		AdRequest request = new AdRequest.Builder().Build();
		// Load the banner with the request.
		bannerView.LoadAd(request);
	}

	static InterstitialAd interstitial;
	public static void RequestInterstitial(bool isShow)
	{
		// Initialize an InterstitialAd.
		interstitial = new InterstitialAd(adUnitIdInterstitial);
		// Create an empty ad request.
		AdRequest request = new AdRequest.Builder().Build();
		// Load the interstitial with the request.
		interstitial.LoadAd(request);
		if(isShow)
			interstitial.AdLoaded += HandleAdLoaded;
	}

	public static void ShowInterstitial()
	{
		if (interstitial.IsLoaded()) {
			interstitial.Show();
		}
	}


	public static void HandleAdLoaded(object sender, System.EventArgs args)
	{
		if (interstitial.IsLoaded()) {
			interstitial.Show();
		}
	}

}

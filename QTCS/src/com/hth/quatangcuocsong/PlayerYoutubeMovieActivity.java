package com.hth.qtcs;

import java.util.ArrayList;

import mobi.mclick.ad.AdsRequest;
import mobi.mclick.ad.AdsView;
import com.google.android.gms.ads.AdView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.hth.data.Data;
import com.hth.data.DeveloperConst;
import com.hth.data.ObjectFavorite;
import com.hth.data.YouTubeParserJSObject;
import com.hth.data.YouTubeService;
import com.hth.data.YouTubeVideo;
import com.hth.data.YouTubeVideoDetail;
import com.hth.qtcs.R;
import com.hth.utils.UIUtils;

import android.os.Bundle;
import android.app.ActionBar;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerYoutubeMovieActivity extends YouTubeBaseActivity implements
		YouTubePlayer.OnInitializedListener {

	private AdView mAdView = null;
	private AdsView adsView;
	private static final int RECOVERY_DIALOG_REQUEST = 1;
	private YouTubeVideoDetail youTubeVideo = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_youtube_movie);

		ActionBar actionBar = getActionBar();
		actionBar.hide();

		String MovieID = getIntent().getStringExtra(
				com.hth.data.DeveloperConst.EXTAR_MOVIE_ID);
		loadUI(MovieID);
		
		/*mAdView = (AdView) this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);*/
		/*tạo AdsView*/
        adsView= new AdsView(this);
        LinearLayout layout = (LinearLayout)findViewById(R.id.adFlexView);
        layout.addView(adsView);
        adsView.loadAds(new AdsRequest());
       // MobileAd.showGift(this,	MobileAd.GIFT_CENTER_RIGHT);
	}
	
	@Override
    protected void onPause() {
        if(mAdView!=null) mAdView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mAdView!=null) mAdView.resume();
    }

    @Override
    protected void onDestroy() {
    	if(mAdView!=null) mAdView.destroy();
        super.onDestroy();
    }
	
	private void loadUI(String movieID)
	{
		youTubeVideo = YouTubeService.getDetailsMovie(movieID);
		if (youTubeVideo != null) {
			if (Data.wasFavorited(getApplicationContext(), youTubeVideo.getID())) {
				findViewById(R.id.btFavoriteMovie).setVisibility(View.GONE);
			}

			((TextView) findViewById(R.id.youtube_title)).setText(youTubeVideo
					.getTitle());

			String infor = "Đăng lúc : " + youTubeVideo.getPublishedTime();
			infor += "\nSố lượt xem : " + youTubeVideo.getViewCount();
			infor += "\nSố yêu thích : " + youTubeVideo.getLikeCount();
			//infor += "\nSố lượt phản hồi : " + youTubeVideo.getCommentCount();
			//infor += youTubeVideo.getDescription();
			((TextView) findViewById(R.id.inforPlayMovie)).setText(infor);

			YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
			youTubeView.initialize(DeveloperConst.DEVELOPER_KEY, this);

			// the older videos
			ArrayList<YouTubeVideo> lstOlderPublishYouTubeVideo = new ArrayList<YouTubeVideo>();
			YouTubeParserJSObject olderYouTubeParserJSObject = YouTubeService
					.getOlderPublishYouTubeVideo(
							youTubeVideo.getOrginalPublishedTime());
			if(olderYouTubeParserJSObject!=null)
				lstOlderPublishYouTubeVideo = olderYouTubeParserJSObject.getYouTubeVideos();
			if(lstOlderPublishYouTubeVideo.size() > 0) lstOlderPublishYouTubeVideo.remove(0);
			GridView grvOlderMovies = (GridView) findViewById(R.id.grvOlderMovies);
			MovieYoutubeItemGridViewAdapter adapter = new MovieYoutubeItemGridViewAdapter(
					PlayerYoutubeMovieActivity.this,
					lstOlderPublishYouTubeVideo, getResources());
			grvOlderMovies.setAdapter(adapter);
			grvOlderMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// check internet
					if (!UIUtils.isOnline(getApplicationContext())) {
						UIUtils.showAlertErrorNoInternet(
								PlayerYoutubeMovieActivity.this, false);
						return;
					}
					Intent playerYoutubeMovieIntent = new Intent(
							PlayerYoutubeMovieActivity.this,
							PlayerYoutubeMovieActivity.class);
					playerYoutubeMovieIntent.putExtra(
							com.hth.data.DeveloperConst.EXTAR_MOVIE_ID, view
									.findViewById(R.id.title).getTag()
									.toString());
					startActivity(playerYoutubeMovieIntent);
					//finish();
					//String nextMovieID =  view.findViewById(R.id.title).getTag().toString();
					//loadUI(nextMovieID);
				}
			});
		}
	}

	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider,
			YouTubePlayer player, boolean wasRestored) {
		if (!wasRestored && youTubeVideo != null) {
			player.cueVideo(youTubeVideo.getID());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RECOVERY_DIALOG_REQUEST) {
			// Retry initialization if user performed a recovery action
			getYouTubePlayerProvider().initialize(DeveloperConst.DEVELOPER_KEY,
					this);
		}
	}

	protected YouTubePlayer.Provider getYouTubePlayerProvider() {
		return (YouTubePlayerView) findViewById(R.id.youtube_view);
	}

	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult errorReason) {
		// TODO Auto-generated method stub
		if (errorReason.isUserRecoverableError()) {
			errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
		} else {
			String errorMessage = String.format(
					getString(R.string.error_player), errorReason.toString());
			Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
		}
	}

	public void favoriteMovie(View v) {
		if (youTubeVideo != null) {
			ObjectFavorite objectFavorite = new ObjectFavorite();
			objectFavorite.setId(youTubeVideo.getID());
			objectFavorite.setTitle(youTubeVideo.getTitle());
			objectFavorite.setPath(youTubeVideo.getThumbnailDefaut());
			objectFavorite.setTime(youTubeVideo.getPublishedTime());

			Data.addFavorite(getApplicationContext(), objectFavorite);
			findViewById(R.id.btFavoriteMovie).setVisibility(View.GONE);
		}
	}

}

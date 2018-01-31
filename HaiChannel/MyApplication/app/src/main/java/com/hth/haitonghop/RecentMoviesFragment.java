package com.hth.haitonghop;

import java.util.ArrayList;

import com.hth.data.YouTubeService;
import com.hth.data.YouTubeVideo;
import com.hth.haitonghop.R;
import com.hth.utils.UIUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


public class RecentMoviesFragment extends Fragment {

	private ArrayList<YouTubeVideo> lstRecentPublishYouTubeVideo = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_recentmovies, container, false);
		if(lstRecentPublishYouTubeVideo==null || lstRecentPublishYouTubeVideo.isEmpty()) {
			lstRecentPublishYouTubeVideo = YouTubeService.getRecentPublishVideo().getYouTubeVideos();
		}
		ListView listView = (ListView)rootView.findViewById(R.id.lvMovies);
		MovieYoutubeItemAdapter adapter = new MovieYoutubeItemAdapter(getActivity(), lstRecentPublishYouTubeVideo, getResources());

		//GridView listView = (GridView)rootView.findViewById(R.id.lvMovies);
		//MovieYoutubeItemGridViewAdapter adapter = new MovieYoutubeItemGridViewAdapter(getActivity(), lstRecentPublishYouTubeVideo, getResources());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	//check internet
                if(!UIUtils.isOnline(getActivity())){
                	UIUtils.showAlertErrorNoInternet(getActivity(), false);
                	return;
                }                
                Intent playerYoutubeMovieIntent = new Intent(getActivity(), PlayerYoutubeMovieActivity.class);
                playerYoutubeMovieIntent.putExtra(com.hth.data.DeveloperConst.EXTAR_MOVIE_ID, view.findViewById(R.id.title).getTag().toString());
                startActivity(playerYoutubeMovieIntent);
            }
        });
		return rootView;
	}
}

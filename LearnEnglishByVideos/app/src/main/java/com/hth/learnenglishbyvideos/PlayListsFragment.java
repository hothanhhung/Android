package com.hth.learnenglishbyvideos;

import java.util.ArrayList;
import java.util.List;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.hth.data.DeveloperConst;
import com.hth.data.YouTubeDetailParserJSObject;
import com.hth.data.YouTubeService;
import com.hth.data.YouTubeVideoDetail;
import com.hth.learnenglishbyvideos.R;
import com.hth.utils.UIUtils;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class PlayListsFragment extends Fragment {

	private static final int REQ_START_STANDALONE_PLAYER = 1;
	private static final int REQ_RESOLVE_SERVICE_MISSING = 2;
	private ArrayList<YouTubeVideoDetail> lstPlaylists = null;
	private YouTubeDetailParserJSObject request = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_playlists,
				container, false);
		if(request == null) {
			request = YouTubeService.getPlaylist();
		}
		if (request != null)
			lstPlaylists = request.getYouTubeVideoDetail();
		else
			lstPlaylists = new ArrayList<YouTubeVideoDetail>();
		
		//lstPlaylists = YouTubeService.getRecentPublishVideo().getYouTubeVideos();

		ListView listView = (ListView) rootView.findViewById(R.id.lvMovies);
		PlaylistYoutubeItemAdapter adapter = new PlaylistYoutubeItemAdapter(
				getActivity(), lstPlaylists, getResources());

		// GridView listView = (GridView)rootView.findViewById(R.id.lvMovies);
		// MovieYoutubeItemGridViewAdapter adapter = new
		// MovieYoutubeItemGridViewAdapter(getActivity(),
		// lstRecentPublishYouTubeVideo, getResources());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// check internet
				if (!UIUtils.isOnline(getActivity())) {
					UIUtils.showAlertErrorNoInternet(getActivity(), false);
					return;
				}

				String playListID = view.findViewById(R.id.title).getTag()
						.toString();

				Intent intent = null;
				intent = YouTubeStandalonePlayer.createPlaylistIntent(
						getActivity(), DeveloperConst.DEVELOPER_KEY,
						playListID, 0, 0, false, false);

				if (intent != null) {
					if (canResolveIntent(intent)) {
						startActivityForResult(intent,
								REQ_START_STANDALONE_PLAYER);
					} else {
						// Could not resolve the intent - must need to install
						// or update the YouTube API service.
						YouTubeInitializationResult.SERVICE_MISSING
								.getErrorDialog(getActivity(),
										REQ_RESOLVE_SERVICE_MISSING).show();
					}
				}
			}
		});
		return rootView;
	}

	private boolean canResolveIntent(Intent intent) {
		List<ResolveInfo> resolveInfo = getActivity().getPackageManager()
				.queryIntentActivities(intent, 0);
		return resolveInfo != null && !resolveInfo.isEmpty();
	}
}

package com.hth.qtcs;

import java.util.ArrayList;

import com.hth.data.*;
import com.hth.qtcs.R;
import com.hth.utils.UIUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class FavoritesFragment extends Fragment {

	ArrayList<ObjectFavorite> lstFavorites;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
		lstFavorites = Data.getFavorites(inflater.getContext());//
		//chapterFull = getChapterFullFromData();
		ListView listView = (ListView)rootView.findViewById(R.id.lvFavorites);
		FavoriteItemAdapter adapter = new FavoriteItemAdapter(inflater.getContext(), lstFavorites, getResources());
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	
            	//check internet
                if(!UIUtils.isOnline(getActivity())){
                	UIUtils.showAlertErrorNoInternet(getActivity(), false);
                	return;
                }

            	ObjectFavorite selectedObject = (ObjectFavorite) view.findViewById(R.id.title).getTag();
            	Intent playerYoutubeMovieIntent = new Intent(getActivity(), PlayerYoutubeMovieActivity.class);
                playerYoutubeMovieIntent.putExtra(DeveloperConst.EXTAR_MOVIE_ID, selectedObject.getId());
                startActivity(playerYoutubeMovieIntent);
            }
        });
		return rootView;
	}
}

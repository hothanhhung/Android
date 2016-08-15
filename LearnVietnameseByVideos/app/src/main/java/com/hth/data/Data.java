package com.hth.data;

import java.util.ArrayList;

import android.content.Context;

public class Data {

	static private ArrayList<ObjectFavorite> _lstFavorites = null;

	public static boolean wasFavorited(Context ctx, String ID) {
		if (_lstFavorites == null) {
			_lstFavorites = DataFavorite.getFavorite(ctx);
		}
		
		for(ObjectFavorite obj : _lstFavorites)
	    {
	    	if(obj.getId().trim().equalsIgnoreCase(ID))
	    		{
	    			return true;
	    		}
	    }
		return false;
	}
	
	public static void addFavorite(Context ctx, ObjectFavorite obj) {
		if (_lstFavorites == null)
			_lstFavorites = new ArrayList<ObjectFavorite>();
		_lstFavorites.add(obj);
		DataFavorite.setFavorite(ctx, obj);
	}

	public static void removeFavorite(Context ctx, ObjectFavorite obj) {
		int index = -1;
		for (int i = 0; i < _lstFavorites.size(); i++)
			if (_lstFavorites.get(i).equals(obj)) {
				index = i;
				break;
			}
		if (index > -1) {
			_lstFavorites.remove(index);
			DataFavorite.setFavorites(ctx, _lstFavorites);
		}
	}

	public static ArrayList<ObjectFavorite> getFavorites(Context ctx) {
		if (_lstFavorites == null) {
			_lstFavorites = DataFavorite.getFavorite(ctx);
		}
		return _lstFavorites;
	}
	/*
	public static ArrayList<ObjectFavorite> getFavorites(Context ctx, String channelID) {
		if (_lstFavorites == null) {
			_lstFavorites = DataFavorite.getFavorite(ctx);
		}
		
		 ArrayList<ObjectFavorite> resultlist = new ArrayList<ObjectFavorite>();
		 for(int i=0; i< _lstFavorites.size(); i++)
			 if(_lstFavorites.get(i).getChannelId().equalsIgnoreCase(channelID)) 
				 resultlist.add(_lstFavorites.get(i));
		 resultlist.trimToSize();
		 
		return resultlist;
	}
	*/
	private static ArrayList<ObjectChannel> _lstChannels = null;
	public static ArrayList<ObjectChannel> getChannels()
	{
		if (_lstChannels == null) {
			_lstChannels = new ArrayList<ObjectChannel>();
			_lstChannels.add(new ObjectChannel("UCoHqnZy_SFICoaC0M-iLlRg","Learn Vietnamese With Annie","https://yt3.ggpht.com/-Pav9B13JC_0/AAAAAAAAAAI/AAAAAAAAAAA/WLgUf1uNpvI/s88-c-k-no-rj-c0xffffff/photo.jpg"));
			_lstChannels.add(new ObjectChannel("UC-iXpqxzCgodyAc-BoNsMeQ","123 VIETNAMESE","https://yt3.ggpht.com/-cZKxmamEEf0/AAAAAAAAAAI/AAAAAAAAAAA/FrlsdSBTFPI/s88-c-k-no-rj-c0xffffff/photo.jpg"));
			_lstChannels.add(new ObjectChannel("UCKqkQmZ7GtOtBqfX3ecv-JQ","Learn Vietnamese","https://i.ytimg.com/i/KqkQmZ7GtOtBqfX3ecv-JQ/1.jpg"));
			_lstChannels.add(new ObjectChannel("UCbnmwpaIq5Iq6XAg0S98m-A","VietnamesePod101.com","https://yt3.ggpht.com/-fxXT5SXbHk8/AAAAAAAAAAI/AAAAAAAAAAA/vBjyp8r2Fjk/s88-c-k-no-mo-rj-c0xffffff/photo.jpg"));
			_lstChannels.add(new ObjectChannel("UCRzx7nBM4GPyMoZpe7CiNmg","Learning Vietnamese Network","https://yt3.ggpht.com/-DtVUyTxMSEI/AAAAAAAAAAI/AAAAAAAAAAA/Cd22NB36Oss/s88-c-k-no-mo-rj-c0xffffff/photo.jpg"));

		}
		return _lstChannels;
	}
}

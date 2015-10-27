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
}

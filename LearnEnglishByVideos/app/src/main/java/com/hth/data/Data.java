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
	
	private static ArrayList<ObjectChannel> _lstChannels = null;
	public static ArrayList<ObjectChannel> getChannels()
	{
		if (_lstChannels == null) {
			_lstChannels = new ArrayList<ObjectChannel>();
			_lstChannels.add(new ObjectChannel("UCWxOpOr_KomHHPXmzD-YB-Q","podEnglish","https://i.ytimg.com/i/WxOpOr_KomHHPXmzD-YB-Q/1.jpg"));
			_lstChannels.add(new ObjectChannel("UCeTVoczn9NOZA9blls3YgUg","Learn English with EnglishClass101.com","https://yt3.ggpht.com/-IQvNSV3k_0A/AAAAAAAAAAI/AAAAAAAAAAA/nvf9vp5xB10/s88-c-k-no-mo-rj-c0xffffff/photo.jpg"));
			_lstChannels.add(new ObjectChannel("UC8pPDhxSn1nee70LRKJ0p3g","Speak English With Misterduncan","https://lh6.googleusercontent.com/-JtRMyAgEXXI/AAAAAAAAAAI/AAAAAAAAAAA/aaN-IV31DOQ/photo.jpg"));
			//_lstChannels.add(new ObjectChannel("UCdTzWalWGkOSyGfywvaj8JA","EnglishcafeDotCom","https://i.ytimg.com/i/dTzWalWGkOSyGfywvaj8JA/1.jpg"));
			_lstChannels.add(new ObjectChannel("UCEKXieT70wByfvZwP1CxdPQ","JenniferESL","https://lh5.googleusercontent.com/-V9g3h7_1Y4g/AAAAAAAAAAI/AAAAAAAAAAA/1OE_Z8cKyJk/photo.jpg"));
			_lstChannels.add(new ObjectChannel("UCKyTokYo0nK2OA-az-sDijA","VOA Learning English","https://lh3.googleusercontent.com/-kUj64nvpiek/AAAAAAAAAAI/AAAAAAAAAAA/YJl93chwato/photo.jpg"));
			_lstChannels.add(new ObjectChannel("UCHaHD477h-FeBbVh9Sh7syA","BBC Learning English","https://lh5.googleusercontent.com/-RjBlhjWy7Nc/AAAAAAAAAAI/AAAAAAAAAAA/L6TjhoxhuGU/photo.jpg"));
			_lstChannels.add(new ObjectChannel("UCtgpDqkeOToveUgh8igrvXQ","KidsTV123","https://lh5.googleusercontent.com/-g_tUdVsQVnU/AAAAAAAAAAI/AAAAAAAAAAA/KqZGxwkomOo/photo.jpg"));
			_lstChannels.add(new ObjectChannel("UCOtnu-KKoAbN47IuYMeDPOg","British Council","https://lh6.googleusercontent.com/-xHKEK7c317E/AAAAAAAAAAI/AAAAAAAAAAA/8oQq8vaN0Ac/photo.jpg"));
			_lstChannels.add(new ObjectChannel("UC4cmBAit8i_NJZE8qK8sfpA","EnglishLessons4U - Learn English with Ronnie!","https://lh6.googleusercontent.com/-JobG09RDhg0/AAAAAAAAAAI/AAAAAAAAAAA/m86B7f7v23A/photo.jpg"));
			_lstChannels.add(new ObjectChannel("UCaRMivfyupj3ucUyJbZbCNg","Anglo-Link","https://lh3.googleusercontent.com/--K1FereyFIw/AAAAAAAAAAI/AAAAAAAAAAA/mXCe3Dmuqw0/photo.jpg"));
			_lstChannels.add(new ObjectChannel("UCIiFsAO2Gh_vu8OStpjl16A","Business English Pod","https://lh6.googleusercontent.com/-n-qGLLzQg6I/AAAAAAAAAAI/AAAAAAAAAAA/E-gaPWB-OPI/photo.jpg"));
			
		}
		return _lstChannels;
	}
}

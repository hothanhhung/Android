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
			_lstChannels.add(new ObjectChannel("UCZOnKo2JsXRX5AGVLa9Jd9w","Hài tuyển chọn","https://lh6.googleusercontent.com/-xHKEK7c317E/AAAAAAAAAAI/AAAAAAAAAAA/8oQq8vaN0Ac/photo.jpg"));
			_lstChannels.add(new ObjectChannel("UCFIDd6HoHgWHBiOEJQcOrCQ","5S Online Official","https://yt3.ggpht.com/-ttdgS_2GxAw/AAAAAAAAAAI/AAAAAAAAAAA/uUe4VPHLN94/s88-c-k-no-mo-rj-c0xffffff/photo.jpg"));
		//	_lstChannels.add(new ObjectChannel("UCL2BnoeVmDODuNEPuxobpug","5S Online","https://yt3.ggpht.com/-wfuM7XK1ZHk/AAAAAAAAAAI/AAAAAAAAAAA/khmtfMcO97Q/s88-c-k-no/photo.jpg"));
			_lstChannels.add(new ObjectChannel("UCxVbAgOxi2HtYFmptzvILrA","YEAH1TV","https://yt3.ggpht.com/-nzeruT2sJnQ/AAAAAAAAAAI/AAAAAAAAAAA/Zq_xBKRC-f4/s88-c-k-no/photo.jpg"));
			_lstChannels.add(new ObjectChannel("UCBNPvdFXpc0XhJ0UG1solTg","DAM tv","https://yt3.ggpht.com/-MOb-z58if5o/AAAAAAAAAAI/AAAAAAAAAAA/xj3Mh0K1Mig/s88-c-k-no/photo.jpg"));
			_lstChannels.add(new ObjectChannel("UCASFPrjAPvzvpGrS-tsF5Fw","BB&BG Entertainment","https://yt3.ggpht.com/-swObDvGJKjY/AAAAAAAAAAI/AAAAAAAAAAA/VMJJwxVGwnY/s88-c-k-no/photo.jpg"));
			_lstChannels.add(new ObjectChannel("UCq6ApdQI0roaprMAY1gZTgw","Ghiền Mì Gõ","https://yt3.ggpht.com/-j4q2f0hwC7E/AAAAAAAAAAI/AAAAAAAAAAA/kNQHp7x2ycw/s88-c-k-no/photo.jpg"));
			_lstChannels.add(new ObjectChannel("UC0jDoh3tVXCaqJ6oTve8ebA","FAP TV","https://yt3.ggpht.com/-bR_nQqJQnWs/AAAAAAAAAAI/AAAAAAAAAAA/lKHB_DPfUeM/s88-c-k-no/photo.jpg"));
			_lstChannels.add(new ObjectChannel("UCC7ruh9xQCr0ze5bdd7FQxw","Đu Đồ Đút","https://yt3.ggpht.com/-cTn2n56ZhXM/AAAAAAAAAAI/AAAAAAAAAAA/4hF6VCWey1s/s88-c-k-no/photo.jpg"));
			_lstChannels.add(new ObjectChannel("UCfa352_VIp2AI7R9Euh0QfA","JVevermind","https://yt3.ggpht.com/-3ZqfqOeaZgA/AAAAAAAAAAI/AAAAAAAAAAA/mozKXdQ2BdA/s88-c-k-no/photo.jpg"));
			_lstChannels.add(new ObjectChannel("UCweRo_U12UUd_Q-9P8RNFrg","102Productions","https://yt3.ggpht.com/-U4mOVDmMWlA/AAAAAAAAAAI/AAAAAAAAAAA/n34ZGtSAnyI/s100-c-k-no/photo.jpg"));
			_lstChannels.add(new ObjectChannel("UCj6B4cj9mB9JXWW65T4Sh4w","TIN VIỆT","https://yt3.ggpht.com/-BW-IAVFmDYE/AAAAAAAAAAI/AAAAAAAAAAA/J4P2N_VcJRs/s88-c-k-no/photo.jpg"));
			//	_lstChannels.add(new ObjectChannel("UC4cmBAit8i_NJZE8qK8sfpA","EnglishLessons4U - Learn English with Ronnie!","https://lh6.googleusercontent.com/-JobG09RDhg0/AAAAAAAAAAI/AAAAAAAAAAA/m86B7f7v23A/photo.jpg"));

		}
		return _lstChannels;
	}
}

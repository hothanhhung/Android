package com.hth.utils;

import com.hth.docbaotonghop.MainActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;
import android.util.Log;


public class DatabaseOperator {
	final String DATABASE_NAME = "cachefile.db";
	final String CACHE_FILE_TABLE_NAME = "tblCacheFile";
	final String CACHE_FILE_TABLE_FIELD_URL_OF_FILE = "URLOfFile";
	final String CACHE_FILE_TABLE_FIELD_DATA_OF_FILE = "DataOfFile";
	final String CACHE_FILE_TABLE_FIELD_CREATED_DATE = "CreatedDate";
	final String CACHE_FILE_TABLE_FIELD_LAST_USED_DATE = "LastUsedDate";
	final String CACHE_FILE_TABLE_FIELD_SIZE_OF_FILE = "SizeOfFile";
    final String BOOKMARK_TABLE_NAME = "tblBookmark";
    final String BOOKMARK_TABLE_FIELD_URL_OF_PAGE = "URLOfPage";
    final String BOOKMARK_TABLE_FIELD_DATA_OF_PAGE = "DataOfPage";
    final String BOOKMARK_TABLE_FIELD_TITLE_OF_PAGE = "TitleOfPage";
    final String BOOKMARK_TABLE_FIELD_URL_OF_IMAGE = "UrlOfImage";
    final String BOOKMARK_TABLE_FIELD_CREATED_DATE = "CreatedDate";
    final String BOOKMARK_TABLE_FIELD_KIND = "Kind";
	
	final boolean IS_SHOW_NOTIFY = true;
		
	SQLiteDatabase database = null;
	
	public void doOpenOrCreateDB()
	{
		try{
			//database=SQLiteDatabase.openOrCreateDatabase(MainActivity.getAppContext().getDatabasePath(DATABASE_NAME), null);
			database = MainActivity.getAppContext().openOrCreateDatabase(DATABASE_NAME, android.content.Context.MODE_PRIVATE, null);
			
		}catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	public DatabaseOperator() {
		// TODO Auto-generated constructor stub
		//doDeleteDB();
		doOpenOrCreateDB();
		if(!isTableExists(CACHE_FILE_TABLE_NAME))doCreateCacheFileTableIfNotExists();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		if(database != null) database.close();
		super.finalize();
	}

	boolean isTableExists(String tableName)
	{
		if(database==null || !database.isOpen()) doOpenOrCreateDB();
		
	    Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName});
	    if (!cursor.moveToFirst())
	    {
	        return false;
	    }
	    int count = cursor.getInt(0);
	    cursor.close();
	    return count > 0;
	}
	
	public boolean doDeleteDB()
	{	
		if(MainActivity.getAppContext().deleteDatabase(DATABASE_NAME)){
			return true;
		}
		return false;
	}
	
	public void doCreateCacheFileTableIfNotExists()
	{
		Log.d("doCreateCacheFileTableIfNotExists","start");
		String sql ="CREATE TABLE if not exists " + CACHE_FILE_TABLE_NAME + " (" +
					CACHE_FILE_TABLE_FIELD_URL_OF_FILE + " TEXT primary key, " +
					CACHE_FILE_TABLE_FIELD_DATA_OF_FILE + " BLOB, " +
					CACHE_FILE_TABLE_FIELD_CREATED_DATE + " TEXT, " +
					CACHE_FILE_TABLE_FIELD_LAST_USED_DATE + " TEXT," +
					CACHE_FILE_TABLE_FIELD_SIZE_OF_FILE + " INT);";
		if(database==null || !database.isOpen()) doOpenOrCreateDB();
		database.execSQL(sql);
		Log.d("doCreateCacheFileTableIfNotExists","success");
	}
	
	public boolean insertIntoCacheFileTable(String URLOfFile, byte[] DataOfFile)
	{
		try{
			ContentValues values = new ContentValues();
			values.put(CACHE_FILE_TABLE_FIELD_URL_OF_FILE, URLOfFile);
			values.put(CACHE_FILE_TABLE_FIELD_DATA_OF_FILE, DataOfFile);
			Time today = new Time(Time.getCurrentTimezone());
			today.setToNow();
			values.put(CACHE_FILE_TABLE_FIELD_CREATED_DATE, today.format("YYYY-MM-DD HH:MM:SS.SSS"));
			values.put(CACHE_FILE_TABLE_FIELD_LAST_USED_DATE, today.format("YYYY-MM-DD HH:MM:SS.SSS"));
			values.put(CACHE_FILE_TABLE_FIELD_SIZE_OF_FILE, DataOfFile.length);
			
			if(database==null || !database.isOpen()) doOpenOrCreateDB();
			
			if(database.insert(CACHE_FILE_TABLE_NAME, null, values)==-1) return false;
			database.close();
			Log.d("insertIntoCacheFileTable",URLOfFile);
			return true;
		}catch(Exception ex){
			Log.d("insertIntoCacheFileTable","Error "+URLOfFile);
		}
		return false;
	}
	
	public boolean updateLastUsedDateFromCacheFileTable(String URLOfFile)
	{
		ContentValues values = new ContentValues();

		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		values.put(CACHE_FILE_TABLE_FIELD_LAST_USED_DATE, today.format("YYYY-MM-DD HH:MM:SS.SSS"));

		if(database==null|| !database.isOpen()) doOpenOrCreateDB();
		
		if(database.update(CACHE_FILE_TABLE_NAME, values, CACHE_FILE_TABLE_FIELD_URL_OF_FILE + "=?", new String[]{URLOfFile}) == 0) return false;
		database.close();
		
		return true;
	}
	
	public byte[] getDataOfFileFromCacheFileTable(String URLOfFile)
	{
		byte[] data = null;
		if(database==null || !database.isOpen()) doOpenOrCreateDB();
		
		Cursor cursor = database.query(CACHE_FILE_TABLE_NAME, new String[]{CACHE_FILE_TABLE_FIELD_DATA_OF_FILE}, CACHE_FILE_TABLE_FIELD_URL_OF_FILE + "=?", new String[]{URLOfFile}, null, null, null);
		cursor.moveToFirst();
		if(!cursor.isAfterLast())
		{
			data = cursor.getBlob(0);
		}
		cursor.close();
		database.close();
		return data;
	}
}

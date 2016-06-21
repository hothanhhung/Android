package com.hth.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hth.sudoku.SudokuItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 6/7/2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String APP_SHARED_PREFS = "com.hth.sudoku";
    private static final String RECODE_VERSION_DB = "RECODE_VERSION_DB";

    private static String DB_PATH = "/data/data/com.hth.sudoku/databases/";
    private static String DB_NAME = "Sudoku.db3";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private static int currentVersion;
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        try {
            currentVersion = myContext.getPackageManager().getPackageInfo(myContext.getPackageName(), 0).versionCode;
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void setVersion(int newVersion) {
        SharedPreferences appSharedPrefs = myContext.getSharedPreferences(APP_SHARED_PREFS, 0);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putInt(RECODE_VERSION_DB, newVersion);
        prefsEditor.commit();
    }

    private int getVersion()
    {
        SharedPreferences appSharedPrefs = myContext.getSharedPreferences(APP_SHARED_PREFS, 0);
        return appSharedPrefs.getInt(RECODE_VERSION_DB, 0);
    }

    private boolean updateDatabase(){

        try {
            String tempName = "NewSudoku.db3";

            /*------------copy file db tempory------------------*/
            //Open your local db as the input stream
            InputStream myInput = myContext.getAssets().open(DB_NAME);
            // Path to the just created empty db
            String outFileName = DB_PATH + tempName;
            //Open the empty db as the output stream
            File dbfile = new File(outFileName);
            if(!dbfile.exists())
            {
                dbfile.createNewFile();
            }else{
                dbfile.delete();
                dbfile.createNewFile();
            }

            OutputStream myOutput = new FileOutputStream(outFileName);
            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }

            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();

            /*--------------------get data from new-------------------*/
            String myPath = DB_PATH + tempName;
            SQLiteDatabase dbInAssert = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
            List<SudokuItem> listSudokuItem = new ArrayList<SudokuItem>();
            Cursor c;

            c = dbInAssert.rawQuery("SELECT OriginalMap, Difficulty FROM GAMES", null);
            if (c == null) return false;

            if (c.moveToFirst()) {
                do {
                    SudokuItem sudokuItem = new SudokuItem(c.getString(0), c.getInt(1));
                    listSudokuItem.add(sudokuItem);
                } while (c.moveToNext());
            }
            c.close();

            dbInAssert.close();

            /*--------------------update data to old-------------------*/
            SQLiteDatabase db = this.getWritableDatabase();
            String sqlSaveSudoku = "";
            for (SudokuItem sudokuItem:listSudokuItem) {

                sqlSaveSudoku = " INSERT INTO Games(OriginalMap, Difficulty) " +
                                "    SELECT ?, ? " +
                                " WHERE NOT EXISTS (SELECT * FROM Games WHERE OriginalMap= ?)";
                Object[] parameters = new Object[]{
                        sudokuItem.getOriginalMap(), sudokuItem.getDifficulty(), sudokuItem.getOriginalMap()
                };
                db.execSQL(sqlSaveSudoku, parameters);
            }
            db.close();
            dbfile.delete();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if(dbExist && currentVersion != getVersion())
        {
            //Log.d("createDataBase", "vao  dbExist "+currentVersion);
            //do nothing - database already exist
            Dialog dialog = UIUtils.showProgressDialog(null, myContext);
            if(updateDatabase()){
                setVersion(currentVersion);
            }
            dialog.dismiss();
            //Log.d("createDataBase", "save "+currentVersion);
        }else
        {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            try {
                copyDataBase();
                setVersion(currentVersion);
            } catch (IOException e) {
                e.printStackTrace();
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    //Check database already exist or not
    private boolean checkDataBase()
    {
        boolean checkDB = false;
        try
        {
            String myPath = DB_PATH;
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();
        }
        catch(SQLiteException e)
        {
        }
        return checkDB;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;
        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLiteException {
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    public SudokuItem getSudoku(String originalMap){
        SudokuItem sudokuItem = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;

        try {
            c = db.rawQuery("SELECT OriginalMap, Difficulty, StartAt, EndAt, ResolvedMap, TotalTime, Changes, Name, Comment, OrderViaEndAt FROM GAMES WHERE OriginalMap=?" , new String[]{originalMap});
            if(c == null) return null;

            if(c.moveToFirst()) {
                do {
                    sudokuItem = new SudokuItem(c.getString(0), c.getInt(1));
                    sudokuItem.setStartAt(c.getString(2));
                    sudokuItem.setEndAt(c.getString(3));
                    sudokuItem.setResolvedMap(c.getString(4));
                    sudokuItem.setTotalTime(c.getString(5));
                    sudokuItem.setChanges(c.getInt(6));
                    sudokuItem.setName(c.getString(7));
                    sudokuItem.setComment(c.getString(8));
                    sudokuItem.setOrderViaEndAt(c.getString(9));
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {

        }

        db.close();

        return sudokuItem;
    }

    public List<SudokuItem> getUsedSudokus(int diff){
        List<SudokuItem> listSudokuItem = new ArrayList<SudokuItem>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;

        try {
            c = db.rawQuery("SELECT OriginalMap, Difficulty, StartAt, EndAt, ResolvedMap, TotalTime, Changes, Name, Comment, OrderViaEndAt FROM GAMES WHERE ResolvedMap IS NOT NULL AND Difficulty="+diff+" ORDER BY OrderViaEndAt DESC" , null);
            if(c == null) return null;

            if(c.moveToFirst()) {
                do {
                    SudokuItem sudokuItem = new SudokuItem(c.getString(0), c.getInt(1));
                    sudokuItem.setStartAt(c.getString(2));
                    sudokuItem.setEndAt(c.getString(3));
                    sudokuItem.setResolvedMap(c.getString(4));
                    sudokuItem.setTotalTime(c.getString(5));
                    sudokuItem.setChanges(c.getInt(6));
                    sudokuItem.setName(c.getString(7));
                    sudokuItem.setComment(c.getString(8));
                    sudokuItem.setOrderViaEndAt(c.getString(9));
                    listSudokuItem.add(sudokuItem);
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {

        }

        db.close();

        return listSudokuItem;
    }

    public List<SudokuItem> getUnusedSudokus(int diff){
        List<SudokuItem> listSudokuItem = new ArrayList<SudokuItem>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;

        try {
            c = db.rawQuery("SELECT OriginalMap, Difficulty, StartAt, EndAt, ResolvedMap, TotalTime, Changes, Name, Comment, OrderViaEndAt FROM GAMES WHERE ResolvedMap IS NULL AND Difficulty=" + diff , null);
            if(c == null) return null;

            if(c.moveToFirst()) {
                do {
                    SudokuItem sudokuItem = new SudokuItem(c.getString(0), c.getInt(1));
                    listSudokuItem.add(sudokuItem);
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {

        }

        db.close();

        return listSudokuItem;
    }

    public void saveWingame(String originalMap, int difficulty, String resolvedMap, String levelName, String time, int changes, String startAt, String endAt, String comment, String orderViaEndAt)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            String sqlSave = "";
            Cursor c = db.rawQuery("SELECT * FROM GAMES WHERE OriginalMap=?", new String[]{originalMap});
            if(!c.moveToFirst()) {
                c.close();
                sqlSave = " INSERT INTO GAMES(OriginalMap, Difficulty, StartAt, EndAt, ResolvedMap, TotalTime, Changes, Name, Comment, OrderViaEndAt) " +
                        " SELECT ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ";
                Object[] parameters = new Object[]{
                        originalMap, difficulty, startAt, endAt, resolvedMap, time, changes, levelName, comment, orderViaEndAt
                };
                db.execSQL(sqlSave, parameters);
            }else{
                c.close();
                sqlSave = "UPDATE GAMES SET Difficulty=?, StartAt=?, EndAt=?, ResolvedMap=?, TotalTime=?, Changes=?, Name=?, Comment=?, OrderViaEndAt=? " +
                        " WHERE OriginalMap =?";
                Object[] parameters = new Object[]{ difficulty, startAt, endAt, resolvedMap, time, changes, levelName, comment, orderViaEndAt,
                    originalMap };
                db.execSQL(sqlSave, parameters);
            };

        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();

    }

    public void saveCommentWin(String originalMap, String comment)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            String sqlSave = "UPDATE GAMES SET Comment=? "+
                    " WHERE OriginalMap =?";
            Object[] parameters = new Object[]{
                    comment, originalMap
            };
            db.execSQL(sqlSave, parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();

    }

    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.

}

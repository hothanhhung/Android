package com.hunght.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hunght.data.LunarDate;
import com.hunght.data.NoteItem;
import com.hunght.solarlunarcalendar.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Lenovo on 5/1/2018.
 */

public class Utils {
    public static String BACKUP_FOLDER="LichAmDuong";
    private static final String TAG = "AmDuong";
    static public int getIconConGiap(int day)
    {
        int lunarDate = day % 12;
        switch (lunarDate)
        {
            case LunarDate.TY: return R.drawable.ty;
            case LunarDate.SUU: return R.drawable.suu;
            case LunarDate.DAN: return R.drawable.dan;
            case LunarDate.MAO: return R.drawable.meo;
            case LunarDate.THIN: return R.drawable.thin;
            case LunarDate.TI: return R.drawable.ti;
            case LunarDate.NGO: return R.drawable.ngo;
            case LunarDate.MUI: return R.drawable.mui;
            case LunarDate.THAN: return R.drawable.than;
            case LunarDate.DAU: return R.drawable.dau;
            case LunarDate.TUAT: return R.drawable.tuat;
            case LunarDate.HOI: return R.drawable.hoi;
        }
        return R.drawable.amduong;
    }

    public static AlertDialog showAlertErrorNoInternet(final Activity activity, final Boolean isCloseThis)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setTitle("Error")
                .setMessage("No internet access")
                .setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if(isCloseThis) activity.finish();
                            }
                        })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
        return alertDialog;
    }

    public static LinearLayout BuildGetMoreAppsServer(final Activity activity)
    {
        final ParseJSONAds parseJSONAds = new ParseJSONAds(activity, "android");
        ArrayList<AdItem> adItems = parseJSONAds.getAds();

        ListView listView = new ListView(activity);
        FlexibleRowAdapter adapter = new FlexibleRowAdapter(activity, adItems, activity.getResources());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0){
                    //check internet
                    if(!Utils.isOnline(activity)){
                        Utils.showAlertErrorNoInternet(activity, false);
                        return;
                    }
                    final AdItem adItem = (AdItem) view.findViewById(R.id.title).getTag();
                    if(adItem.getLink() != null && adItem.getLink()!="") {
                        Thread background = new Thread(new Runnable() {
                            public void run() {
                                try {
                                    parseJSONAds.userClickAd(adItem.getLink());
                                } catch (Throwable t) {}
                            }
                        });
                        background.start();

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(adItem.getLink()));
                        activity.startActivity(i);
                    }
                }
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, 0, 50);
        listView.setLayoutParams(layoutParams);
        LinearLayout linearLayout = new LinearLayout(activity);
        if (android.os.Build.VERSION.SDK_INT>=17) {
            // call something for API Level 11+
            linearLayout.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_LTR);
        }

        //linearLayout.addView(progressBar1);
        linearLayout.addView(listView);
        return linearLayout;
    }

    static LinearLayout linearLayout;
    static AlertDialog alertDialog;
    static AlertDialog.Builder alert;
    public static void showAlertGetMoreAppsServer(final Activity activity) {
        alert = new AlertDialog.Builder(activity);
        TextView textView = new TextView(activity);
        textView.setText("Loading...");
        textView.setPadding(10, 10, 10, 10);
        textView.setTextSize(15);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        alert.setView(textView);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                alertDialog = null;
            }
        });
        //alert.setIcon(android.R.drawable.box_new)
        alertDialog = alert.show();

        (new Thread(new Runnable() {
            @Override
            public void run() {
                linearLayout = BuildGetMoreAppsServer(activity);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(alertDialog!=null && alertDialog.isShowing()) {
                            alert.setView(linearLayout);
                            alertDialog.dismiss();
                            alertDialog = alert.show();
                        }
                    }
                });
            }
        }
        )).start();
    }

    public static Boolean isOnline(Context ctx)
    {
        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnected();

    }

    public static void exportToCsv(Context context, ArrayList<NoteItem> noteItems) {
        final String fileName = "LichAmDuong_"+MethodsHelper.getYYYYMMDDhhmmss(Calendar.getInstance())+".json";
        Log.d("TAG", "exportToExcel");
        //Saving file in external storage
        //File sdCard = Environment.getExternalStorageDirectory();
        File sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File directory = new File(sdCard.getAbsolutePath() + "/" + BACKUP_FOLDER);
        //create directory if not exist
        if(!directory.isDirectory()){
            directory.mkdirs();
        }
        //file path
        File file = new File(directory, fileName);

        Log.d("TAG", "Create File: " + file.getPath());
            try {
                if(file.createNewFile()) {
                    try {
                        Gson gson = new Gson();
                        String json = gson.toJson(noteItems);
                        PrintWriter writer = new PrintWriter(file);
                        writer.print(json);
                        writer.close();
                    } catch (Exception e) {
                        Log.d("TAG", "Error Write File" + e.getMessage());
                        e.printStackTrace();
                    }
                    try {
                        Toast.makeText(context, file.getPath(), Toast.LENGTH_LONG).show();
                        Uri uri = Uri.parse(directory.getPath());

//                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                        intent.addCategory(Intent.CATEGORY_OPENABLE);
//                        intent.setDataAndType(uri, "*/*");
//                        context.startActivity(Intent.createChooser(intent, "Mở với ứng dụng"));
                        /*Uri path = FileProvider.getUriForFile(context,
                                BuildConfig.APPLICATION_ID + ".provider",
                                file);

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(path, "application/json");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        try {
                            context.startActivity(intent);
                        } catch (Exception e) {
                            Log.d("TAG", "Error Open File" + e.getMessage());
                            e.printStackTrace();
                        }*/

                        return;
                    } catch (Exception e) {
                        Log.d("TAG", "Error Create File" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        Toast.makeText(context, "Lỗi Khi Tạo File", Toast.LENGTH_LONG).show();
    }

    public static String readTextFile(File file){
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (Exception e) {
            Log.d("TAG", "Error Read File" + e.getMessage());
            e.printStackTrace();
        }

        return text.toString();
    }
}

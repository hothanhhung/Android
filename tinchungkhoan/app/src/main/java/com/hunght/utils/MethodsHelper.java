package com.hunght.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.hunght.data.DanhMucDauTuItem;
import com.hunght.tinchungkhoan.BuildConfig;
import com.hunght.tinchungkhoan.MainActivity;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.biff.RowsExceededException;

/**
 * Created by Lenovo on 6/9/2016.
 */
public class MethodsHelper {

    public static String BACKUP_FOLDER="TinChungKhoan";

    public static String stripAccentsAndD(String s)
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        s = s.replaceAll("[đ]", "d");
        s = s.replaceAll("[Đ]", "D");
        return s;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getApplicationContext().getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    static public String getCurrentDateToOrder(){
        return getCurrentDate("yyyyMMdd-HHmmss");
    }

    static public String getCurrentDate(){
        return getCurrentDate("HH:mm:ss dd-MM-yyyy");
    }

    static public String getCurrentDate(String format){
        Date now = new Date();
        String formattedDate = new SimpleDateFormat(format).format(now);
        return formattedDate;
    }

    static public String getScoreInFormat(int score){
        return String.format("%1$05d", score);

    }

    static public String getTimeFormat(Long var1) {
        String var2 = String.valueOf((int) (var1.longValue() / 3600L));
        String var3 = String.valueOf((int) (var1.longValue() % 3600L / 60L));
        String var4 = String.valueOf((int) (var1.longValue() % 60L));
        if (var2.length() == 1) {
            var2 = "0".concat(var2);
        }

        if (var3.length() == 1) {
            var3 = "0".concat(var3);
        }

        if (var4.length() == 1) {
            var4 = "0".concat(var4);
        }

        return var2.concat(":").concat(var3).concat(":").concat(var4);
    }


    public static String getAppVersion(Context context)
    {
        String version = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return version;

    }

    public static String getAndroidId(Context context)
    {
        String android_id = "";
        try {
            android_id = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return android_id;

    }

    public static String getStringFromFloat(float number) {
        int num = Math.round(number * 1000);
        return getStringFromInt(num);
    }

    public static String getStringFromInt(int number) {
//        return String.format("%.d%n", number);
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMANY);
        DecimalFormat myFormatter = (DecimalFormat)nf;
        String output = myFormatter.format(number);
        return output;
    }

    public static String getPackageName(Context context)
    {
        String packageName = "";
        try {
            packageName = context.getPackageName();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return packageName;

    }

    public static String getDDMMYYY(Calendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        return (day < 10 ? "0" : "") + day + "-" + (month < 10 ? "0" : "") + month + "-" + year;
    }

    public static String getYYYYMMDD(Calendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        return year + "" + (month < 10 ? "0" : "") + month + "" + (day < 10 ? "0" : "") + day;
    }

    public static String getYYYYMMDDhhmmss(Calendar calendar) {
        int second = calendar.get(Calendar.SECOND);
        int minute = calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        return (year + "" + (month < 10 ? "0" : "") + month + "" + (day < 10 ? "0" : "") + day
                + (hour < 10 ? "0" : "") + hour + "" + (minute < 10 ? "0" : "") + minute+ "" + (second < 10 ? "0" : "") + second);
    }

    private static File createExportFile(String fileName){

        //Saving file in external storage
        //File sdCard = Environment.getExternalStorageDirectory();
        File sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File directory = new File(sdCard.getAbsolutePath() + "/"+BACKUP_FOLDER);

        //create directory if not exist
        if(!directory.isDirectory()){
            directory.mkdirs();
        }

        //file path
        File file = new File(directory, fileName);
        return file;
    }
    private static WritableWorkbook createWritableWorkbook(File file)
    {
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));

        try {
            return Workbook.createWorkbook(file, wbSettings);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void tryToOpenExcelFile(Context context, File file){
        try {

            //open file
            //Uri path = Uri.fromFile(file);
            Uri path = FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/vnd.ms-excel");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exportFavoriteToExcel(Context context, ArrayList<String> favoriteItems) {
        final String fileName = "DanhMucYeuThich_"+MethodsHelper.getYYYYMMDDhhmmss(Calendar.getInstance())+".xls";

        try {
            File file = createExportFile(fileName);
            WritableWorkbook workbook = createWritableWorkbook(file);
            if (workbook != null) {
                WritableSheet sheet = workbook.createSheet("DanhMucYeuThich_", 0);

                try {
                    int index = 0;
                    sheet.addCell(new Label(index, 0, "Mã Chứng Khoán"));
                    if (favoriteItems != null) {
                        for (int i = 0; i < favoriteItems.size(); i++) {
                            index = 0;
                            String item = favoriteItems.get(i);
                            sheet.addCell(new Label(index++, i + 1, "" + item));
                        }
                    }
                    workbook.write();
                    workbook.close();
                    Toast.makeText(context, file.getPath(), Toast.LENGTH_LONG).show();

                    tryToOpenExcelFile(context, file);
                    return;
                } catch (RowsExceededException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(context, "Lỗi Khi Tạo File", Toast.LENGTH_LONG).show();
    }

    public static void exportDanhMucDauTuToExcel(Context context, ArrayList<DanhMucDauTuItem> danhMucDauTuItems) {
        final String fileName = "DanhMucDauTu_"+MethodsHelper.getYYYYMMDDhhmmss(Calendar.getInstance())+".xls";

        try {
            File file = createExportFile(fileName);
            WritableWorkbook workbook = createWritableWorkbook(file);
            if (workbook != null) {
                WritableSheet sheet = workbook.createSheet("DanhMucDauTu", 0);

                try {
                    int index = 0;
                    sheet.addCell(new Label(index, 0, "Id"));
                    sheet.addCell(new Label(index++, 0, "Ngày Mua"));
                    sheet.addCell(new Label(index++, 0, "Tên Công Ty"));
                    sheet.addCell(new Label(index++, 0, "Mã CK"));
                    sheet.addCell(new Label(index++, 0, "Giá Mua"));
                    sheet.addCell(new Label(index++, 0, "Số Lượng"));
                    sheet.addCell(new Label(index++, 0, "Giá Thị Trường"));
                    sheet.addCell(new Label(index++, 0, "Giá Bán"));
                    if (danhMucDauTuItems != null) {
                        for (int i = 0; i < danhMucDauTuItems.size(); i++) {
                            index = 0;
                            DanhMucDauTuItem danhMucDauTuItem = danhMucDauTuItems.get(i);
                            sheet.addCell(new Label(index++, i + 1, "" + danhMucDauTuItem.getId()));
                            sheet.addCell(new Label(index++, i + 1, danhMucDauTuItem.getNgayMua()));
                            sheet.addCell(new Label(index++, i + 1, danhMucDauTuItem.getTenCongTy()));
                            sheet.addCell(new Label(index++, i + 1, danhMucDauTuItem.getMaCK()));
                            sheet.addCell(new Label(index++, i + 1, danhMucDauTuItem.getGiaMuaInString()));
                            sheet.addCell(new Label(index++, i + 1, danhMucDauTuItem.getSoLuongInString()));
                            sheet.addCell(new Label(index++, i + 1, danhMucDauTuItem.getGiaThiTruongInString()));
                            sheet.addCell(new Label(index++, i + 1, danhMucDauTuItem.getGiaBanInString()));
                        }
                    }
                    workbook.write();
                    workbook.close();
                    Toast.makeText(context, file.getPath(), Toast.LENGTH_LONG).show();

                    tryToOpenExcelFile(context, file);
                    return;
                } catch (RowsExceededException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(context, "Lỗi Khi Tạo File", Toast.LENGTH_LONG).show();
    }

    public static ArrayList<DanhMucDauTuItem> importDanhMucDauTuFromExcel(File file) {
        ArrayList<DanhMucDauTuItem> danhMucDauTuItems = new ArrayList<>();
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));

        try {
            Workbook workbook = Workbook.getWorkbook(file) ;
            Sheet sheet = workbook.getSheet(0);
            for(int i = 1; i< sheet.getRows(); i++){
                Cell[] cells = sheet.getRow(i);
                int index = 0;
                DanhMucDauTuItem danhMucDauTuItem = new DanhMucDauTuItem();
                danhMucDauTuItem.setId(Long.parseLong(cells[index++].getContents()));
                danhMucDauTuItem.setNgayMua(cells[index++].getContents());
                danhMucDauTuItem.setTenCongTy(cells[index++].getContents());
                danhMucDauTuItem.setMaCK(cells[index++].getContents());
                danhMucDauTuItem.setGiaMua(Float.parseFloat(cells[index++].getContents()));
                danhMucDauTuItem.setSoLuong(Integer.parseInt(cells[index++].getContents()));
                danhMucDauTuItem.setGiaThiTruong(Float.parseFloat(cells[index++].getContents()));
                danhMucDauTuItem.setGiaBan(Float.parseFloat(cells[index++].getContents()));

                danhMucDauTuItems.add(danhMucDauTuItem);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
        return danhMucDauTuItems;
    }

    public static ArrayList<String> importDanhMucYeuThichFromExcel(File file) {
        ArrayList<String> danhMucYeuThichItems = new ArrayList<>();
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));

        try {
            Workbook workbook = Workbook.getWorkbook(file) ;
            Sheet sheet = workbook.getSheet(0);
            for(int i = 1; i< sheet.getRows(); i++){
                Cell[] cells = sheet.getRow(i);
                int index = 0;
                danhMucYeuThichItems.add(cells[index++].getContents());
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
        return danhMucYeuThichItems;
    }

    public static boolean checkPermission(Context context, int permissionKey)
    {
        switch (permissionKey) {
            case MainActivity.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MainActivity.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    return false;
                } else {
                    // Permission has already been granted
                    return true;
                }
            case MainActivity.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MainActivity.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    return false;
                } else {
                    // Permission has already been granted
                    return true;
                }
        }
        return false;
    }
}

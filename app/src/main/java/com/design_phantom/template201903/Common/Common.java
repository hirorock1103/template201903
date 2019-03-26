package com.design_phantom.template201903.Common;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Common {


    public static String DB_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String DATE_FORMAT_SAMPLE_1 = "yyyy/MM/dd";

    /**
     * 日付変更
     * @param str
     * @param pattern1
     * @param patten2
     * @return
     */
    public static String formatStrToDate(String str, String pattern1 , String patten2){

        String formatdate = "";

        SimpleDateFormat sdf = new SimpleDateFormat(pattern1);
        SimpleDateFormat tosdf = new SimpleDateFormat(patten2);

        try {
            Date date = sdf.parse(str);
            formatdate = tosdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formatdate;

    }

    public static String formatDate(Date date , String pattern){

        String formatdate = "";
        SimpleDateFormat tosdf = new SimpleDateFormat(pattern);
        formatdate = tosdf.format(date);

        return formatdate;
    }

    /**
     * 日付加算　減算
     */
    public static Date addDateFromToday(String target, int value){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        Date date = new Date();

        switch (target){
            case "MONTH":
                calendar.add(Calendar.MONTH, value);
                break;
            case "YEAR":
                calendar.add(Calendar.YEAR, value);
                break;
            case "DAY":
                calendar.add(Calendar.DATE, value);
                break;
        }

        date = calendar.getTime();


        return date;

    }


    public static Date getDateByStr(String str, String pattern){

        Date newDate = null;

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            newDate = sdf.parse(str);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return newDate;

    }


    public static Date addDateFromTargetDate(String targetDate, String pattern, String target, int value){

        //str to date
        Date newDate = getDateByStr(targetDate, pattern);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newDate);

        switch (target){
            case "MONTH":
                calendar.add(Calendar.MONTH, value);
                break;
            case "YEAR":
                calendar.add(Calendar.YEAR, value);
                break;
            case "DAY":
                calendar.add(Calendar.DATE, value);
                break;
        }

        Date date = calendar.getTime();

        return date;

    }


    /**
     * log
     */
    public static void log(String str){

        Log.i("INFO_TEST", str);

    }

    /**
     * toast
     */
    public static void toast(Context context , String str){

        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

    }

    public static class ActionManager{

        public void setKeyBoardHide(Context context, ConstraintLayout layout){

            InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(), inputMethodManager.HIDE_NOT_ALWAYS);
            layout.requestFocus();

        }
    }
    public static void sendMail(Context context,String subject, String contents){
        Intent emailIntent = new Intent(
                android.content.Intent.ACTION_SEND);
        emailIntent.setAction(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                new String[] { "" });
        emailIntent.putExtra(android.content.Intent.EXTRA_CC, "");
        emailIntent.putExtra(android.content.Intent.EXTRA_BCC, "");
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(contents));
        emailIntent.setType("text/html");
        context.startActivity(Intent.createChooser(emailIntent,
                "Send Email Using: "));
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static byte[] convertBitMapToByteArrray(Bitmap bitmap){
        ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(byteBuffer);
        byte[] bmparr = byteBuffer.array();
        return bmparr;
    }

    public static byte[] convertBitMapToByteArray2(Bitmap resizeImage){
        byte[] byteImage = null;
        //bitmapをblob保存用に変換
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        resizeImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byteImage = stream.toByteArray();
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteImage;
    }

    public static byte[] getImageByteFromUri(Context context, Uri imageUri, int picwidth){

        byte[] byteImage = null;

        InputStream inputStream;
        BitmapFactory.Options imageOptions;

        try{

            inputStream = context.getContentResolver().openInputStream(imageUri);

            //画像サイズ情報
            imageOptions = new BitmapFactory.Options();
            imageOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream,null,imageOptions);
            inputStream.close();

            //再度読み込み
            inputStream = context.getContentResolver().openInputStream(imageUri);
            int width = imageOptions.outWidth;

            int p = 1;
            while(width > picwidth){
                //縮小率を決める
                p *= 2;
                width /= p;
            }

            Bitmap imageBitmap;
            if(p > 1){
                //縮小
                imageOptions = new BitmapFactory.Options();
                imageOptions.inSampleSize = p;
                imageBitmap = BitmapFactory.decodeStream(inputStream, null,imageOptions);

            }else{
                //等倍
                imageBitmap = BitmapFactory.decodeStream(inputStream, null,null);
            }

            inputStream.close();

            //bitmapをblob保存用に変換
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteImage = stream.toByteArray();
            stream.close();

        }catch (FileNotFoundException e){
            Common.log(e.getMessage());

        }catch (IOException e){
            Common.log(e.getMessage());
        }

        return byteImage;

    }

    public static String convertUrlFromDrawableResId(Context context, int drawableResId) {
        StringBuilder sb = new StringBuilder();
        sb.append(ContentResolver.SCHEME_ANDROID_RESOURCE);
        sb.append("://");
        sb.append(context.getResources().getResourcePackageName(drawableResId));
        sb.append("/");
        sb.append(context.getResources().getResourceTypeName(drawableResId));
        sb.append("/");
        sb.append(context.getResources().getResourceEntryName(drawableResId));
        return sb.toString();
        // Uriに変換したい時は Uri.parse(sb.toString())
    }
}
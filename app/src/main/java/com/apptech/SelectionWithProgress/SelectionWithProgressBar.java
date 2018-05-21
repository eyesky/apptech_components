package com.apptech.SelectionWithProgress;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.apptech.apptechcomponents.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectionWithProgressBar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_with_progress_bar);

        Camera camera = Camera.open(0);    // For Back Camera
        android.hardware.Camera.Parameters params = camera.getParameters();
        List sizes = params.getSupportedPictureSizes();
        Camera.Size result = null;

        ArrayList<Integer> arrayListForWidth = new ArrayList<Integer>();
        ArrayList<Integer> arrayListForHeight = new ArrayList<Integer>();

        for (int i = 0; i < sizes.size(); i++) {
            result = (Camera.Size) sizes.get(i);
            arrayListForWidth.add(result.width);
            arrayListForHeight.add(result.height);
            Log.d("PictureSize", "Supported Size: " + result.width + "height : " + result.height);
            System.out.println("BACK PictureSize Supported Size: " + result.width + "height : " + result.height);
        }
        if (arrayListForWidth.size() != 0 && arrayListForHeight.size() != 0) {
            System.out.println("Back max W :" + Collections.max(arrayListForWidth));              // Gives Maximum Width
            System.out.println("Back max H :" + Collections.max(arrayListForHeight));                 // Gives Maximum Height
            System.out.println("Back Megapixel :" + Math.ceil((double)Collections.max(arrayListForWidth) * Collections.max(arrayListForHeight) / 1024000 ) );
//            System.out.println("Back Megapixel :" + (((Collections.max(arrayListForWidth)) * (Collections.max(arrayListForHeight))) / 1024000));
        }
        camera.release();

        arrayListForWidth.clear();
        arrayListForHeight.clear();

        camera=Camera.open(1);        //  For Front Camera
        android.hardware.Camera.Parameters params1 = camera.getParameters();
        List sizes1 = params1.getSupportedPictureSizes();
        Camera.Size  result1 = null;
        for (int i=0;i<sizes1.size();i++){
            result1 = (Camera.Size) sizes1.get(i);
            arrayListForWidth.add(result1.width);
            arrayListForHeight.add(result1.height);
            Log.d("PictureSize", "Supported Size: " + result1.width + "height : " + result1.height);
            System.out.println("FRONT PictureSize Supported Size: " + result1.width + "height : " + result1.height);
        }
        if(arrayListForWidth.size() != 0 && arrayListForHeight.size() != 0){
            System.out.println("FRONT max W :"+Collections.max(arrayListForWidth));
            System.out.println("FRONT max H :"+Collections.max(arrayListForHeight));
            System.out.println("FRONT Megapixel :"+Math.ceil((double)Collections.max(arrayListForWidth) * Collections.max(arrayListForHeight) / 1024000 ) );
//            System.out.println("FRONT Megapixel :"+( ((Collections.max(arrayListForWidth)) * (Collections.max(arrayListForHeight))) / 1024000 ) );
        }

        camera.release();

        System.out.println("Megapixel :" + Build.BRAND+" - "+ android.os.Build.MODEL);

        System.out.println("Megapixel BRAND: " + android.os.Build.BRAND);
        System.out.println("Megapixel PRODUCT: " + android.os.Build.PRODUCT);
        System.out.println("Megapixel DEVICE: " + android.os.Build.DEVICE);
        System.out.println("Megapixel MANUFACTURER: " + android.os.Build.MANUFACTURER);
        System.out.println("Megapixel BASE_OS: " + Build.VERSION.BASE_OS);
        System.out.println("Megapixel RELEASE: " + Build.VERSION.RELEASE);
        System.out.println("Megapixel SDK: " + Build.VERSION.SDK_INT);
        System.out.println("Megapixel CODENAME: " + Build.VERSION.CODENAME);
        System.out.println("Megapixel FINGERPRINT: " + Build.FINGERPRINT);
        System.out.println("Megapixel getRadioVersion: " + Build.getRadioVersion());
        System.out.println("Megapixel INCREMENTAL: " + Build.VERSION.INCREMENTAL);
        System.out.println("Megapixel BASE: " + Build.VERSION_CODES.BASE);
        System.out.println("Megapixel BASE_1_1: " + Build.VERSION_CODES.BASE_1_1);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        String osName = fields[Build.VERSION.SDK_INT + 1].getName();

        System.out.println("Megapixel VERSION_NAME: " + osName);

        // get app version code and versionName
        PackageInfo pinfo = null;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int versionNumber = pinfo.versionCode;
        String versionName = pinfo.versionName;

        System.out.println("Megapixel "+versionName+"-------"+versionNumber);


    }
}

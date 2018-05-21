package com.apptech.ExtractingColorsFromImages;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.apptech.apptechcomponents.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ExtractingColorsFromImages extends AppCompatActivity {

    ImageView imageView;
    RelativeLayout colorPallet;

    public static final int REQUEST_CODE_NEW_GALLERY = 0x4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extracting_colors_from_images);

        imageView = (ImageView) findViewById(R.id.iv);
        colorPallet = (RelativeLayout) findViewById(R.id.color_pallet);

    }

    public void ChoosePhoto(View v) {
        imageFromGallery();
    }


    private void imageFromGallery() {
        try {
            Intent getImageFromGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            getImageFromGalleryIntent.setType("image/*");
            startActivityForResult(getImageFromGalleryIntent, REQUEST_CODE_NEW_GALLERY);

        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case REQUEST_CODE_NEW_GALLERY:

                Uri selectedImage = data.getData();
                if (selectedImage == null) {
                    return;
                } else {
                    String imagePath = getPath(selectedImage);

                    final int THUMBSIZE = 64;
                    Log.e("onActivityResult: ",imagePath);

                    Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imagePath),
                            THUMBSIZE, THUMBSIZE);

                    imageView.setImageBitmap(ThumbImage);
                    colorPallet.setBackgroundColor(generateColorCode(selectedImage));
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private int generateColorCode(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return GetAppropriatePalletColorFromImage(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /**
     * Getting prominent color from image, This method is returning any random color
     * which is not zero. If this method get zero , that means it is failed fo get any
     * value from image
     *
     * @param bitmap
     * @return int
     */
    public static int GetAppropriatePalletColorFromImage(Bitmap bitmap) {
        try {
            if (bitmap != null) {

                Palette palette = Palette.from(bitmap).generate();


                int colorVibrant = palette.getVibrantColor(0x000000);
                if (colorVibrant != 0) {
                    return colorVibrant;
                }

                int colorDarkVibrant = palette.getDarkVibrantColor(0x000000);
                if (colorDarkVibrant != 0) {
                    return colorDarkVibrant;
                }

                int colorLightMuted = palette.getLightMutedColor(0x000000);
                if (colorLightMuted != 0) {
                    return colorLightMuted;
                }
                int colorMutedColor = palette.getMutedColor(0x000000);
                if (colorMutedColor != 0) {
                    return colorMutedColor;
                }

                int colorDarkMuted = palette.getDarkMutedColor(0x000000);
                if (colorDarkMuted != 0) {
                    return colorDarkMuted;
                }

                int colorLightVibrant = palette.getLightVibrantColor(0x000000);
                if (colorLightVibrant != 0) {
                    return colorLightVibrant;
                }
                //Log.e("Palette Code", Pcolor1+", "+Pcolor2+", "+Pcolor3+", "+Pcolor4+", "+Pcolor5+", "+Pcolor6);
                return 0;

            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;

        }
    }
}

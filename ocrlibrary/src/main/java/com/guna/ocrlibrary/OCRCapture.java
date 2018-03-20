package com.guna.ocrlibrary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

/**
 * Created by INTEL on 20-03-2018.
 */

public class OCRCapture {
    private Activity activity;
    private boolean useFlash;
    private boolean autoFocus;

    private OCRCapture(Activity activity) {
        this.activity = activity;
    }

    public static OCRCapture Builder(Activity activity) {
        return new OCRCapture(activity);
    }

    public boolean isUseFlash() {
        return useFlash;
    }

    public OCRCapture setUseFlash(boolean useFlash) {
        this.useFlash = useFlash;
        return this;
    }

    public boolean isAutoFocus() {
        return autoFocus;
    }

    public OCRCapture setAutoFocus(boolean autoFocus) {
        this.autoFocus = autoFocus;
        return this;
    }

    public String getTextFromImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            if (bitmap != null) {
                return getTextFromBitmap(bitmap);
            } else {
                return "Cannot read image from given path";
            }
        } else {
            return "Image path is not valid";
        }
    }

    public String getTextFromUri(Uri imageUri) throws IOException {
        if (imageUri != null) {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), imageUri);
            if (bitmap != null) {
                return getTextFromBitmap(bitmap);
            } else {
                return "Cannot read image from given path";
            }
        } else {
            return "Image path is not valid";
        }
    }

    public String getTextFromBitmap(Bitmap bitmap) {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(activity).build();
        Frame imageFrame = new Frame.Builder()
                .setBitmap(bitmap)
                .build();
        StringBuilder imageText = new StringBuilder();
        SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);

        for (int i = 0; i < textBlocks.size(); i++) {
            TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
            if (imageText.toString().equals("")) {
                imageText.append(textBlock.getValue());
            } else {
                imageText.append("\n");
                imageText.append(textBlock.getValue());
            }
        }
        return imageText.toString();
    }

    public void buildWithRequestCode(int requestCode) {
        Intent intent = new Intent(activity, OcrCaptureActivity.class);
        intent.putExtra("UseFlash", useFlash);
        intent.putExtra("AutoFocus", autoFocus);
        activity.startActivityForResult(intent, requestCode);
    }
}

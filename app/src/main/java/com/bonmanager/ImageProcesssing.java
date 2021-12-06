package com.bonmanager;

import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

public class ImageProcesssing {

   /* static void detectTxt(Bitmap imageBitmap) {
        // this is a method to detect a text from image.
        // below line is to create variable for firebase
        // vision image and we are getting image bitmap.
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);

        // below line is to create a variable for detector and we
        // are getting vision text detector from our firebase vision.
        FirebaseVisionTextDetector detector = FirebaseVision.getInstance().getVisionTextDetector();

        // adding on success listener method to detect the text from image.
        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                // calling a method to process
                // our text after extracting.
                processTxt(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // handling an error listener.
                Toast.makeText(MainActivity.this, "Fail to detect the text from image..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    static void processTxt(FirebaseVisionText text) {
        // below line is to create a list of vision blocks which
        // we will get from our firebase vision text.
        List<FirebaseVisionText.Block> blocks = text.getBlocks();

        // checking if the size of the
        // block is not equal to zero.
        if (blocks.size() == 0) {
            // if the size of blocks is zero then we are displaying
            // a toast message as no text detected.
            Toast.makeText(MainActivity.this, "No Text ", Toast.LENGTH_LONG).show();
            return;
        }
        // extracting data from each block using a for loop.
        for (FirebaseVisionText.Block block : text.getBlocks()) {
            // below line is to get text
            // from each block.
            String txt = block.getText();

            // below line is to set our
            // string to our text view.
            textview.setText(txt);
        }
    }*/
}

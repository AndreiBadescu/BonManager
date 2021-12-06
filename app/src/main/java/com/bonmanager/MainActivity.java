package com.bonmanager;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicConvolve3x3;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bonmanager.databinding.ActivityMainBinding;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
/*    private ImageButton addImageButton;
    private ImageButton selectImageFromGalleyButton;
    private ImageView receiptImage;
    private Bitmap imageBitmap;
    private TextView resultTV;*/

/*    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_IMAGE = 5;
    private static int SELECT_PICTURE = 200;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

/*        receiptImage = (ImageView) findViewById(R.id.image);
        resultTV = (TextView) findViewById(R.id.resulted_text);

        addImageButton = (ImageButton) findViewById(R.id.add_image_btn);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        selectImageFromGalleyButton = (ImageButton) findViewById(R.id.select_image_from_gallery);
        selectImageFromGalleyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchSelectImageFromGalleryIntent();
            }
        });*/
    }

/*    private void dispatchTakePictureIntent() {
        // in the method we are displaying an intent to capture our image.
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // on below line we are calling a start activity
        // for result method to get the image captured.
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchSelectImageFromGalleryIntent() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    private String getRealPathFromURIForGallery(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(uri, projection, null,
                null, null);
        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        assert false;
        cursor.close();
        return uri.getPath();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) { return; }
        // calling on activity result method.
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            // on below line we are getting
            // data from our bundles. .
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            // below line is to set the
            // image bitmap to our image.
            receiptImage.setImageBitmap(imageBitmap);
            detectText();
        } else if (requestCode == PICK_IMAGE) {
            Uri selectedImageUri = data.getData();
            String selectedImagePath = getRealPathFromURIForGallery(selectedImageUri);
            System.out.println(selectedImagePath);
            imageBitmap = (Bitmap) BitmapFactory.decodeFile(selectedImagePath);
            receiptImage.setImageBitmap(imageBitmap);
            detectText();
        } else if (requestCode == SELECT_PICTURE) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                String selectedImagePath = getRealPathFromURIForGallery(selectedImageUri);
                System.out.println(selectedImagePath);
                // update the preview image in the layout
                receiptImage.setImageURI(selectedImageUri);
                BitmapDrawable drawable = (BitmapDrawable) receiptImage.getDrawable();
                imageBitmap = drawable.getBitmap();
            }
            detectText();
        }
    }

    private void detectText() {
        imageBitmap = toGrayscale(imageBitmap);
        //imageBitmap = doSharpen(imageBitmap, 1.0f);
        receiptImage.setImageBitmap(imageBitmap);
        System.out.println(imageBitmap);
        InputImage image = InputImage.fromBitmap(imageBitmap, 0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        Task<Text> result = recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(@NonNull Text text) {
                StringBuilder result = new StringBuilder();
                for (Text.TextBlock block : text.getTextBlocks()) {
                    String blockText = block.getText();
                    Point[] blockCounterPoint = block.getCornerPoints();
                    Rect blockFrame = block.getBoundingBox();
                    for (Text.Line line: block.getLines()) {
                        String lineText = line.getText();
                        Point[] lineCornerPoint = line.getCornerPoints();
                        Rect lineRect = line.getBoundingBox();
                        for (Text.Element element: line.getElements()) {
                            String elementText = element.getText();
                            result.append(elementText);
                        }
                        System.out.println(blockText);
                        resultTV.setText(blockText);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed to detect text from image: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Bitmap doSharpen(Bitmap original, float multiplier) {
        float[] sharp = { 0, -multiplier, 0, -multiplier, 5f*multiplier, -multiplier, 0, -multiplier, 0};
        Bitmap bitmap = Bitmap.createBitmap(
                original.getWidth(), original.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript rs = RenderScript.create(MainActivity.this);

        Allocation allocIn = Allocation.createFromBitmap(rs, original);
        Allocation allocOut = Allocation.createFromBitmap(rs, bitmap);

        ScriptIntrinsicConvolve3x3 convolution
                = ScriptIntrinsicConvolve3x3.create(rs, Element.U8_4(rs));
        convolution.setInput(allocIn);
        convolution.setCoefficients(sharp);
        convolution.forEach(allocOut);

        allocOut.copyTo(bitmap);
        rs.destroy();

        return bitmap;
    }

    public static Bitmap toGrayscale(Bitmap srcImage) {

        Bitmap bmpGrayscale = Bitmap.createBitmap(srcImage.getWidth(), srcImage.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bmpGrayscale);
        Paint paint = new Paint();

        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(srcImage, 0, 0, paint);

        return bmpGrayscale;
    }*/
}
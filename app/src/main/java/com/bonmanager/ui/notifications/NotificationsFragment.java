package com.bonmanager.ui.notifications;

import android.content.Context;
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
import android.provider.OpenableColumns;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicConvolve3x3;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bonmanager.MainActivity;
import com.bonmanager.R;
import com.bonmanager.Receipt;
import com.bonmanager.databinding.FragmentNotificationsBinding;
import com.bonmanager.ui.home.HomeFragment;
import com.bonmanager.ui.home.HomeViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.Objects;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    private ImageButton addImageButton;
    private ImageButton selectImageFromGalleyButton;
    private ImageView receiptImage;
    private Bitmap imageBitmap;
    private TextView resultTV;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_IMAGE = 5;
    private static int SELECT_PICTURE = 200;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        receiptImage = (ImageView) root.findViewById(R.id.image);
        receiptImage.setVisibility(View.GONE);
        resultTV = (TextView) root.findViewById(R.id.resulted_text);
        resultTV.setVisibility(View.GONE);

        addImageButton = (ImageButton) root.findViewById(R.id.add_image_btn);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("1");
                dispatchTakePictureIntent();
            }
        });

        selectImageFromGalleyButton = (ImageButton) root.findViewById(R.id.select_image_from_gallery);
        selectImageFromGalleyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchSelectImageFromGalleryIntent();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void dispatchTakePictureIntent() {
        // in the method we are displaying an intent to capture our image.
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // on below line we are calling a start activity
        // for result method to get the image captured.
        //if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            System.out.println("2");
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        //}
        System.out.println("5");
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
        Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null,
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

    private void detectText() {
        //imageBitmap = toGrayscale(imageBitmap);
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
                        //resultTV.setText(blockText);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireActivity(), "Failed to detect text from image: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String trimFileName(String fileName) {
        String[] ext;
        ext = fileName.split("\\.");
        return fileName.replace("." +  ext[ext.length - 1], "");
    }

    private String getFileName(Uri myUri) {
        Cursor returnCursor = requireContext().getContentResolver().query(myUri, null, null, null, null);
        returnCursor.moveToFirst();
        System.out.println(returnCursor);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        System.out.println(nameIndex);
        return returnCursor.getString(nameIndex);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != requireActivity().RESULT_OK) { return; }
        // calling on activity result method.
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            // on below line we are getting
            // data from our bundles. .
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            // below line is to set the
            // image bitmap to our image.
            receiptImage.setImageBitmap(imageBitmap);
            receiptImage.setVisibility(View.VISIBLE);
            detectText();
        } else if (requestCode == PICK_IMAGE) {
            Uri selectedImageUri = data.getData();
            String selectedImagePath = getRealPathFromURIForGallery(selectedImageUri);
            System.out.println(selectedImagePath);
            imageBitmap = (Bitmap) BitmapFactory.decodeFile(selectedImagePath);
            receiptImage.setImageBitmap(imageBitmap);
            receiptImage.setVisibility(View.VISIBLE);
            detectText();
        } else if (requestCode == SELECT_PICTURE) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                //String selectedImagePath = getRealPathFromURIForGallery(selectedImageUri);
                String filename = getFileName(selectedImageUri);
                System.out.println("###################################################################################################################");
                System.out.println(trimFileName(filename));
                System.out.println("###################################################################################################################");
                // update the preview image in the layout
                receiptImage.setImageURI(selectedImageUri);
                receiptImage.setVisibility(View.VISIBLE);
                BitmapDrawable drawable = (BitmapDrawable) receiptImage.getDrawable();
                imageBitmap = drawable.getBitmap();
                detectText();
                showResult(trimFileName(filename));
            }
        }
    }

    public Bitmap doSharpen(Bitmap original, float multiplier) {
        float[] sharp = { 0, -multiplier, 0, -multiplier, 5f*multiplier, -multiplier, 0, -multiplier, 0};
        Bitmap bitmap = Bitmap.createBitmap(
                original.getWidth(), original.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript rs = RenderScript.create(requireContext());

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
    }

    private void showResult(String id) {
        resultTV.setVisibility(View.VISIBLE);
        resultTV.setText(Bon[Integer.parseInt(id)].toString2());
        HomeFragment.AddReceipt(Bon[Integer.parseInt(id)]);
    }

    private static Receipt[] Bon = {
            new Receipt(
                    "RYLKE PROD SRL",
                    "RO 4665546",
                    "21-10-2021",
                    "15:45",
                    "9.00%",
                    "16.00",
                    new String[]{"PAINE"},
                    new String[]{"16.00"}
            ),
            new Receipt(
                    "RYLKE PROD SRL",
                    "RO 4665546",
                    "24-09-2021",
                    "23:05",
                    "9.00%",
                    "5.30",
                    new String[]{"PEPSI 2 L"},
                    new String[]{"5.30"}
            ),
            new Receipt(
                    "S.C. OMV PETROM MARKETING S.R.L.",
                    "RO11201891",
                    "04.08.2021",
                    "22:02:22",
                    "19.00%",
                    "200.10",
                    new String[]{"* 3 MOTORINA STANDARD"},
                    new String[]{"200.10"}
            ),
            new Receipt(
                    "SUCCES DEZVOLTARE 1991 SRL",
                    "RO33030628",
                    "02-12-2021",
                    "12:08",
                    "19%",
                    "14.75",
                    new String[]{"AQUATIQUE 5L APA PLATA", "FAIRY EXT PLUS LILAC"},
                    new String[]{"5.75", "9.00"}
            )
    };
}
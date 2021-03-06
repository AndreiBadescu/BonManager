package com.bonmanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicConvolve3x3;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bonmanager.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bonmanager.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * MainActivity class
 * This is the first class android calls when the app starts
 */
public class MainActivity extends AppCompatActivity {

    private Connection connection;
    private final String connectionResult = "";
    private static Context context;
    private static boolean loginScreen = true;
    private ActivityMainBinding binding;

    /**
     * On create
     * @param savedInstanceState saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = MainActivity.this;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout_button) {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        if (loginScreen) {
                            finish();
                        }
                        Objects.requireNonNull(getSupportActionBar()).setTitle("Bon Manager");
                        setContentView(R.layout.activity_login);
                        loginScreen = true;
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Are you sure you want to logout ?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Store receipts on local storage and later sync with the database
     * @param listArray list array
     */
    public static void SaveArrayList(List<Receipt> listArray){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listArray);
        editor.putString("TAG_LIST", json);
        editor.apply();
    }

    /**
     * Get receipts from local storage and later sync with database
     * @return
     */
    public static ArrayList<Receipt> getArrayList(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString("TAG_LIST", null);
        Type listType = new TypeToken<ArrayList<Receipt>>() {}.getType();
        ArrayList<Receipt> myArray = (ArrayList<Receipt>)gson.fromJson(json, listType);
        if (myArray == null) {
            return new ArrayList<>();
        }
        return gson.fromJson(json, listType);
    }

    /**
     * Sign-in screen that connects to database
     * @param view
     * @throws InterruptedException
     */
    public void changelayout(View view) throws InterruptedException {
        EditText usernameET = (EditText) findViewById(R.id.username);
        EditText passwordET = (EditText) findViewById(R.id.password);
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();

        System.out.println("user: " + username);
        System.out.println("pass: " + password);
        try {
            System.out.println("step1");
            ConnectionHelper connectionHelper = new ConnectionHelper(username, password);
            System.out.println("step1 - succeded");
            if (connection == null) {
                System.out.println("connection failed");
            }
        } catch (Exception e) {
            return;
        }
        //Thread.sleep(2000);
        //if (!password.equals("Andrei1234")) return;

        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        loginScreen = false;
    }
}
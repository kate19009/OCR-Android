package com.example.myapplication;

import android.Manifest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.AccessController;
import java.util.List;

import static android.Manifest.permission.*;

public class StartActivityA extends MainActivity
{
    private static final int REQUEST_CODE = 1;
    private static final String LOG_TAG = "Text API";
    private static final int PHOTO_REQUEST = 10;
    private TextView scanResults;
    private Uri imageUri;
    private TextRecognizer detector;
    private static String tmpText = "";
    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_RESULT = "result";
    private  Bitmap bitmap ;
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        detector = new TextRecognizer.Builder(getApplicationContext()).build();
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_CODE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phstart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final TextRecognized textRecognized1 = new TextRecognized();
        getSupportActionBar().setTitle(" Text Recognition App");
        scanResults = (TextView) findViewById(R.id.results);
        scanResults.setTextIsSelectable(true);
        if (savedInstanceState != null)
        {
            imageUri = Uri.parse(savedInstanceState.getString(SAVED_INSTANCE_URI));
            scanResults.setText(savedInstanceState.getString(SAVED_INSTANCE_RESULT));
        }

        final Button button = findViewById(R.id.button);
        final Button button1 = findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent t= new Intent(StartActivityA.this,StartActivityA.class);
                startActivity(t);
                finish();
            }
        });
        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                textRecognized1.loadResult();
                scanResults.setText(textRecognized1.text);

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }


                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(getApplicationContext(), "Permissions aren't granted by user!", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE)
        {
            super.onActivityResult(requestCode, resultCode, data);

                if (data != null) {
                    Uri contentURI = data.getData();
                    try {
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        startActivityForResult(intent, PHOTO_REQUEST);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(StartActivityA.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }



         }

        if (requestCode == PHOTO_REQUEST && resultCode == RESULT_OK)
        {
            launchMediaScanIntent();
            try
            {
                if (detector.isOperational() && bitmap != null)
                {

                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    TextRecognized textRecognized1 =  new TextRecognized();
                    textRecognized1.textBlocks = detector.detect(frame);
                    textRecognized1.saveResult(tmpText);
                    String blocks = "";
                    for (int index = 0; index < textRecognized1.textBlocks.size(); index++)
                    {
                        TextBlock tBlock = textRecognized1.textBlocks.valueAt(index);
                        blocks = blocks + tBlock.getValue() + "\n";
                    }
                    if (textRecognized1.textBlocks.size() == 0)
                    {
                        scanResults.setText("Scan Failed: Found nothing to scan");
                    }
                    else
                    {
                        scanResults.setText(scanResults.getText() + "Text: " + "\n");
                        textRecognized1.text = blocks + "\n";
                        scanResults.setText(textRecognized1.text);
                        tmpText = textRecognized1.text;
                       // textRecognized1.saveResult(blocks);

                    }
                }
                else
                {
                    scanResults.setText("Could not set up the detector!");
                }
            }
            catch (Exception e)
            {
                Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT)
                        .show();
                Log.e(LOG_TAG, e.toString());
            }
        }
    }

    private void takePicture()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "picture.jpg");
        imageUri = FileProvider.getUriForFile(StartActivityA.this,
                BuildConfig.APPLICATION_ID + ".provider", photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, PHOTO_REQUEST);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        if (imageUri != null)
        {
            outState.putString(SAVED_INSTANCE_URI, imageUri.toString());
            outState.putString(SAVED_INSTANCE_RESULT, scanResults.getText().toString());
        }
        super.onSaveInstanceState(outState);
    }

    private void launchMediaScanIntent()
    {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        this.sendBroadcast(mediaScanIntent);
    }


}

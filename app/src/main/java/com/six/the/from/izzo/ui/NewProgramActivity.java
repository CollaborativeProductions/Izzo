package com.six.the.from.izzo.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.six.the.from.izzo.R;
import com.six.the.from.izzo.util.IzzoEditText;
import com.six.the.from.izzo.util.Validation;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class NewProgramActivity extends ActionBarActivity {
    private static final int SELECT_PHOTO = 100;
    private ImageView imgProgramIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout);
        initTextView();
        initImageView();
    }

    private void initTextView() {
        final IzzoEditText etViewProgramName = (IzzoEditText) findViewById(R.id.et_new_program_name);
        etViewProgramName.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0) etViewProgramName.setError(null);
            }
        });
    }

    private void initImageView() {
        imgProgramIcon = (ImageView) findViewById(R.id.img_program_icon);
        imgProgramIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        Bitmap bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int MAX_WIDTH = bmp.getWidth() / 4;
        final int MAX_HEIGHT = bmp.getHeight() / 4;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < MAX_WIDTH
                    || height_tmp / 2 < MAX_HEIGHT) {
                break;
            }
            if (!(width_tmp / 2 < MAX_WIDTH)) {
                width_tmp /= 2;
            } else if (!(height_tmp / 2 < MAX_WIDTH)) {
                height_tmp /= 2;
            }
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        Bitmap imgBitmap = decodeUri(selectedImage);
                        imgProgramIcon.setImageBitmap(imgBitmap);
                    } catch (FileNotFoundException fnf) {
                        fnf.printStackTrace();
                    }
                }
                break;
            default:
                Toast.makeText(NewProgramActivity.this,
                        "There was a problem uploading your image. Please try again.",
                        Toast.LENGTH_LONG).show();
                break;
        }
    }

    private String createImageFileFromBitmap(Bitmap bitmap) {
        String fileName = "izzoProgramIconImage";//no .png or .jpg needed
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            // remember close file output
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_program, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_next:
                IzzoEditText etViewProgramName = (IzzoEditText) findViewById(R.id.et_new_program_name);
                if (Validation.hasText(etViewProgramName)) {
                    Intent intent = new Intent(this, NewProgramDetailsActivity.class);
                    intent.putExtra("programName", etViewProgramName.getText().toString());

                    Bitmap bmpImage = ((BitmapDrawable)imgProgramIcon.getDrawable()).getBitmap();
                    intent.putExtra("imageFile", createImageFileFromBitmap(bmpImage));
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

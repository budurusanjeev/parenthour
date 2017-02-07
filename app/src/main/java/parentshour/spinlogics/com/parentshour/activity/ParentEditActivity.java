package parentshour.spinlogics.com.parentshour.activity;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crashlytics.android.Crashlytics;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.utilities.AppConstants;
import parentshour.spinlogics.com.parentshour.utilities.FontStyle;
import parentshour.spinlogics.com.parentshour.utilities.LoadingText;
import parentshour.spinlogics.com.parentshour.utilities.NetworkUtils;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;
import parentshour.spinlogics.com.parentshour.utilities.Utility;

import static parentshour.spinlogics.com.parentshour.R.id.iv_upload_profile_photo;

/**
 * Created by SPINLOGICS ic_on 12/7/2016.
 */

public class ParentEditActivity extends AppCompatActivity {
    String imageData;
    Context context;
    ImageView profilePic;
    File destination;
    Button bt_save,bt_cancel;
    EditText edt_name,
            edt_zipcode,
            edt_email,
            edt_phone, edt_age, edt_occupation, edt_education;
    RadioGroup rg_gender;
    RadioButton rb_male, rb_female;
    String cb_weekdaysValue,
            rg_gender_value = null,
            value = null,
            valueTimimgs = null ,
            cb_weekendsValue,
            cb_morningValue,
            cb_afternoonValue,
            cb_eveningValue;
    CheckBox cb_weekdays, cb_weekends, cb_morning, cb_afternoon, cb_evening;
    Spinner ethnicitySpinner;
    List<String> Lines = null;
    LoadingText loadingText;
    TextView toolbarTextView;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask, ethnicity;
    private PreferenceUtils preferenceUtils;

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_editprofile);
        context = ParentEditActivity.this;
        preferenceUtils = new PreferenceUtils(context);
        loadingText = new LoadingText(context);
        initViewControll();
        getProfileData();
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_name.getText().length() > 0 &&
                        edt_zipcode.getText().length() == 5 &&
                        edt_age.getText().length() > 0 &&
                        edt_occupation.getText().length() > 0 &&
                        edt_education.getText().length() > 0 && rg_gender_value != null)
                {
                    if(cb_weekdaysValue != null ||
                            cb_weekendsValue != null)
                    {

                        if(cb_morningValue != null ||
                                cb_afternoonValue != null ||
                                cb_eveningValue != null )
                        {
                            if (NetworkUtils.isNetworkConnectionAvailable(context)) {
                                sendData();
                            } else {
                                Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
                            }


                        }else
                        {
                            showAlertValidation("Please select the available timings");
                        }

                    }else
                    {
                        showAlertValidation("Please select the available days");
                    }

                } else {
                    if (edt_name.getText().length() == 0) {
                        showAlertValidation("Please enter the ic_name");
                    } else if (edt_zipcode.getText().length() == 0 || edt_zipcode.getText().length() < 5) {
                        showAlertValidation("Please enter the valid zip code");
                    } else if (edt_age.getText().length() == 0) {
                        showAlertValidation("Please enter the ic_age");
                    } else if (edt_occupation.getText().length() == 0) {
                        showAlertValidation("Please enter the valid ic_occupation");
                    } else if (edt_education.getText().length() == 0) {
                        showAlertValidation("Please enter the ic_education");
                    }
                    else if (rg_gender_value == null) {
                        showAlertValidation("Please select the gender ");
                    }
                }
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.rb_male)
                {
                    rg_gender_value = "male";
                }else
                {

                    rg_gender_value = "female";
                }
            }
        });
        cb_weekdays.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()) {
                    cb_weekdaysValue ="Weekdays";

                }else
                {
                    cb_weekdaysValue = null;
                }
                Log.v("","res checked "+cb_weekdaysValue);
            }
        });

        cb_weekends.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()) {
                    cb_weekendsValue = "Weekends";

                }else
                {
                    cb_weekendsValue = null;
                }
                Log.v("","res checked "+cb_weekendsValue);
            }
        });

        cb_morning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()) {
                    cb_morningValue = "Morning";
                }
                else
                {
                    cb_morningValue = null;
                }
            }
        });

        cb_afternoon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()) {
                    cb_afternoonValue = "Afternoon";

                }else
                {
                    cb_afternoonValue = null;
                }
            }
        });

        cb_evening.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(compoundButton.isChecked()) {
                    cb_eveningValue = "Evening";

                }else
                {
                    cb_eveningValue = null;
                }
            }
        });
    }

    private void sendData() {
        loadingText.showLoaderNew(ParentEditActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.EDIT_PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("Success")) {
                                loadingText.hideloader(ParentEditActivity.this);
                                if(imageData != null)
                                {
                                    Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Success"), Toast.LENGTH_LONG).show();

                                   /* Glide.with(context)
                                            .load(destination)
                                            .thumbnail(0.5f)
                                            .crossFade()
                                            .error(R.drawable.ic_addprofile)
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(img_header);*/
                                    // PreferenceUtils preferenceUtils = new PreferenceUtils(getApplicationContext());
                                    // preferenceUtils.saveString("p_pic", destination.toString());
                                    // preferenceUtils.saveString("p_name",edt_name.getText().toString());
                                    // BaseActivity.setProfileImage(context,destination.toString(),edt_name.getText().toString());
                                }

                                                              finish();
                                Log.v("res ", "res  success" + jsonObject.getString("Success"));
                            } else {
                                jsonObject.getString("error");
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                                Log.v("res ", "res  success" + jsonObject.getString("Error"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Crashlytics.logException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingText.hideloader(ParentEditActivity.this);
                        Log.v("error", "error " + error.toString());
                        Crashlytics.logException(error);
                        Toast.makeText(ParentEditActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                String credentials;
                if(imageData != null)
                {
                    credentials = "p_id="+ preferenceUtils.getStringFromPreference("p_id","")
                            +"&p_name="+ edt_name.getText().toString()+
                            "&p_zip="+ edt_zipcode.getText().toString()+
                            "&p_email="+ edt_email.getText().toString()+
                            "&p_mobile="+ edt_phone.getText().toString()+
                            "&p_age="+ edt_age.getText().toString()+
                            "&p_occupation="+ edt_occupation.getText().toString()+
                            "&p_gender="+ rg_gender_value+
                            "&p_education="+ edt_education.getText().toString()+
                            "&p_ethnicity=" + ethnicity +
                            "&p_avail_days="+validateSignup() +
                            "&p_avail_time="+validateTimings() +
                            "&photo="+imageData;

                }
                else
                {
                    credentials = "p_id="+ preferenceUtils.getStringFromPreference("p_id","")
                            +"&p_name="+ edt_name.getText().toString()+
                            "&p_zip="+ edt_zipcode.getText().toString()+
                            "&p_email="+ edt_email.getText().toString()+
                            "&p_mobile="+ edt_phone.getText().toString()+
                            "&p_age="+ edt_age.getText().toString()+
                            "&p_occupation="+ edt_occupation.getText().toString()+
                            "&p_gender="+ rg_gender_value+
                            "&p_education="+ edt_education.getText().toString()+
                            "&p_ethnicity=" + ethnicity +
                            "&p_avail_days="+validateSignup() +
                            "&p_avail_time="+validateTimings() ;
                }


                try {
                    return credentials.getBytes(getParamsEncoding());
                } catch (UnsupportedEncodingException uee) {
                    throw new RuntimeException("Encoding not supported: "
                            + getParamsEncoding(), uee);
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorisation", "e4f507ddf306806gc7dcg77ed1e52f97");
                return params;
            }

            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
       // Toast.makeText(getApplicationContext(),"Sending Data ........",Toast.LENGTH_LONG).show();
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    Toast.makeText(getApplicationContext(), "User has denied request", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageData = getStringImage(thumbnail);
        profilePic.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                // Log.v("file path ","file path "+getRealPathFromURI(data.getData())); ;
                imageData = getStringImage(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // imagePath(data.getData());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//android.os.Build.VERSION.SDK_INT < ICE_CREAM_SANDWICH
                destination = new File(imagePath(data.getData()));
            } else {
                destination = new File(getRealPathFromURI(data.getData()));
            }

            Log.v("file path ", "file path " + imagePath(data.getData()));

        }
        profilePic.setImageBitmap(bm);
    }

    // And to convert the image URI to the direct file system path of the image file
    public String getRealPathFromURI(Uri contentUri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(contentUri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    String imagePath(Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
        }


        return null;
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ParentEditActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ParentEditActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void getProfileData()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.GET_PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        try {
                        JSONObject    jsonObject = new JSONObject(response);

                            if (!jsonObject.has("Error"))
                            {
                                edt_name.setText(jsonObject.getString("p_name"));
                                edt_zipcode.setText(jsonObject.getString("p_zip"));
                                edt_email.setText(jsonObject.getString("p_email"));
                                edt_phone.setText(jsonObject.getString("p_mobile"));
                                edt_age.setText(jsonObject.getString("p_age"));
                                edt_occupation.setText(jsonObject.getString("p_occupation"));
                                ethnicitySpinner.setSelection(Lines.indexOf(jsonObject.getString("p_ethnicity")));
                                if(jsonObject.getString("p_gender").equals("male"))
                                {
                                    rb_male.setChecked(true);
                                }else if(jsonObject.getString("p_gender").equals("female"))
                                {
                                    rb_female.setChecked(true);
                                }
                                if(jsonObject.getString("p_avail_days").equals("Weekdays,Weekends"))
                                {
                                    cb_weekdays.setChecked(true);
                                    cb_weekends.setChecked(true);
                                }else
                                {
                                    if (jsonObject.getString("p_avail_days").equals("Weekdays") || jsonObject.getString("p_avail_days").equals(",Weekdays"))
                                    {
                                        cb_weekdays.setChecked(true);
                                    }
                                    if (jsonObject.getString("p_avail_days").equals("Weekends") || jsonObject.getString("p_avail_days").equals(",Weekends"))
                                    {
                                        cb_weekends.setChecked(true);
                                    }

                                }
                                if(jsonObject.getString("p_avail_time").equals("Morning,Afternoon,Evening"))
                                {
                                    cb_morning.setChecked(true);
                                    cb_afternoon.setChecked(true);
                                    cb_evening.setChecked(true);
                                }
                                else
                                {
                                    String r =  jsonObject.getString("p_avail_time");
                                    String d[] = r.split(",");
                                    Log.v("","");
                                    for(int s= 0; s <d.length;s++)

                                    {Log.v("name ","selected name "+d[s]);
                                        switch (s)
                                        {
                                            case 0:
                                                if (d[s].equals("Morning") || d[s].equals(",Morning"))
                                                {
                                                    cb_morning.setChecked(true);

                                                }
                                                if (d[s].equals("Afternoon") || d[s].equals(",Afternoon"))
                                                {
                                                    cb_afternoon.setChecked(true);

                                                }
                                                if (d[s].equals("Evening") || d[s].equals(",Evening"))
                                                {
                                                    cb_evening.setChecked(true);
                                                }
                                                   break;
                                            case 1:
                                                if (d[s].equals("Morning") || d[s].equals(",Morning"))
                                                {
                                                    cb_morning.setChecked(true);

                                                }
                                                if (d[s].equals("Afternoon") || d[s].equals(",Afternoon"))
                                                {
                                                    cb_afternoon.setChecked(true);

                                                }
                                                if (d[s].equals("Evening") || d[s].equals(",Evening"))
                                                {
                                                    cb_evening.setChecked(true);
                                                }
                                                  break;
                                            case 2:
                                                if (d[s].equals("Morning") || d[s].equals(",Morning"))
                                                {
                                                    cb_morning.setChecked(true);

                                                }
                                                if (d[s].equals("Afternoon") || d[s].equals(",Afternoon"))
                                                {
                                                    cb_afternoon.setChecked(true);

                                                }
                                                if (d[s].equals("Evening") || d[s].equals(",Evening"))
                                                {
                                                    cb_evening.setChecked(true);
                                                }
                                                break;
                                        }

                                    }


                                }
                                edt_education.setText(jsonObject.getString("p_education"));

                                Glide.with(context).load(jsonObject.getString("p_pic"))
                                        .thumbnail(0.5f)
                                        .crossFade()
                                        .error(R.drawable.ic_addprofile)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(profilePic);
                                Log.v("res ", "res  success" + jsonObject.toString());
                            } else {
                                jsonObject.getString("Error");
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Error"), Toast.LENGTH_LONG).show();
                                if (jsonObject.getString("Error").equals("Parent Account doesn't exist.")) {
                                    //TODO if parent doesn't exist
                                }
                                Log.v("res ", "res  success" + jsonObject.toString());
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Crashlytics.logException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("error", "error " + error.toString());
                        Toast.makeText(ParentEditActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Crashlytics.logException(error);
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                String credentials ="p_id="+preferenceUtils.getStringFromPreference("p_id","");

                try {
                    return credentials.getBytes(getParamsEncoding());
                } catch (UnsupportedEncodingException uee) {
                    throw new RuntimeException("Encoding not supported: "
                            + getParamsEncoding(), uee);
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorisation", "e4f507ddf306806gc7dcg77ed1e52f97");
                return params;
            }

            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private String validateSignup()
    {

        if(cb_weekdaysValue != null &&
                cb_weekendsValue != null)
        {
            value = "Weekdays,Weekends";
        }else if(cb_weekdaysValue != null)
        {
            value = "Weekdays";
            if(cb_weekendsValue != null)
            {
                value = "Weekends";
            }
        }else if(cb_weekendsValue != null)
        {
            value = "Weekends";
        }

        return value;
    }
    private String validateTimings()
    {
        if(cb_morningValue != null&&
                cb_afternoonValue != null&&
                cb_eveningValue != null)
        {
            valueTimimgs = "Morning,Afternoon,Evening";
        }
        else if(cb_morningValue != null)
        {
            valueTimimgs = "Morning";
            if(cb_afternoonValue != null)
            {
                valueTimimgs =  valueTimimgs + ",Afternoon";
            }else if(cb_eveningValue != null)
            {
                valueTimimgs = valueTimimgs + ",Evening";
            }
        }else if(cb_afternoonValue != null)
        {
            valueTimimgs = "Afternoon";
            if(cb_eveningValue != null)
            {
                valueTimimgs = valueTimimgs + ",Evening";
            }
        }else if(cb_eveningValue != null)
        {
            valueTimimgs = "Evening";
        }
        return valueTimimgs;
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbarTextView.setText("Edit Parent Info");
    }

    public void showAlertValidation(String error)
    {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.notification_dailog, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        final TextView tv_errorTitle = (TextView) dialogView.findViewById(R.id.tvTitle);
        final TextView tv_ok = (TextView) dialogView.findViewById(R.id.btnYes);
        tv_errorTitle.setText(error);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });
        b.show();
    }
    private void initViewControll()
    {
        View test1View = findViewById(R.id.toolbarLayout);
        toolbarTextView = (TextView) test1View.findViewById(R.id.page_heading);
        profilePic = (ImageView) findViewById(iv_upload_profile_photo);

        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_zipcode = (EditText) findViewById(R.id.edt_zipcode);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_phone = (EditText) findViewById(R.id.edt_phone);
        edt_age = (EditText) findViewById(R.id.edt_age);
        edt_occupation = (EditText) findViewById(R.id.edt_occupation);
        edt_education = (EditText) findViewById(R.id.edt_education);

        cb_weekdays = (CheckBox) findViewById(R.id.cb_weekdays);
        cb_weekends = (CheckBox) findViewById(R.id.cb_weekends);
        cb_morning= (CheckBox) findViewById(R.id.cb_morning);
        cb_afternoon = (CheckBox) findViewById(R.id.cb_afternoon);
        cb_evening = (CheckBox) findViewById(R.id.cb_evening);

        rg_gender = (RadioGroup) findViewById(R.id.rg_gender);
        rb_male = (RadioButton) findViewById(R.id.rb_male);
        rb_female = (RadioButton) findViewById(R.id.rb_female);

        bt_save = (Button)findViewById(R.id.btn_save);
        bt_cancel = (Button)findViewById(R.id.btn_cancel);

        ethnicitySpinner = (Spinner) findViewById(R.id.spinner_ethnicity);
        //
        Lines = Arrays.asList(getResources().getStringArray(R.array.enthincityArray));
      /* ArrayAdapter adapterBusinessType = new ArrayAdapter<String>
               (this,R.layout.row_spinner,Lines);*/
        ArrayAdapter adapterBusinessType = new ArrayAdapter<String>(this, R.layout.row_spinner, Lines) {
            @Override
            public int getCount() {
                return Lines.size();
            }

            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row;

                if (null == convertView) {
                    row = LayoutInflater.from(context).inflate(R.layout.row_spinner, null);
                } else {
                    row = convertView;
                }

                TextView tv = (TextView) row.findViewById(R.id.tv_ethnicity_row);
                tv.setText(getItem(position));

                return row;
            }
        };
        adapterBusinessType.setDropDownViewResource(R.layout.row_spinner);

        ethnicitySpinner.setAdapter(adapterBusinessType);
/*
        TextView toolbarTextView = (TextView)findViewById(R.id.page_heading);
        toolbarTextView.setText("Edit Profile");*/
        LinearLayout par_edit_layout = (LinearLayout)findViewById(R.id.par_edit_layout);
        FontStyle.applyFont(getApplicationContext(),par_edit_layout, FontStyle.Lato_Medium);
        ethnicitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ethnicity = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}

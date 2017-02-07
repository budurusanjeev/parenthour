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
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.utilities.AppConstants;
import parentshour.spinlogics.com.parentshour.utilities.FontStyle;
import parentshour.spinlogics.com.parentshour.utilities.LoadingText;
import parentshour.spinlogics.com.parentshour.utilities.NetworkUtils;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;
import parentshour.spinlogics.com.parentshour.utilities.Utility;


/**
 * Created by SPINLOGICS ic_on 12/8/2016.
 */

public class AssistantEditActivity extends AppCompatActivity {

    String imageData;
    Context context;
    ImageView profilePic;
    Button bt_save, bt_cancel;
    EditText edt_name,
            edt_experience,
            edt_email,
            edt_phone, edt_city, edt_zipcode, edt_state, edt_billing, edt_about_me, edt_gigs;
    LinearLayout ass_edit_parent;
    LoadingText loadingText;
    File destination;
    TextView toolbarTextView;
    CheckBox cb_assisting_with_children, cb_cooking, cb_House_errands, cb_bet_friendly;
    String valueSkillls, cb_assisting_with_children_Value, cb_cooking_Value, cb_House_errands_Value, cb_bet_friendly_Value;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private PreferenceUtils preferenceUtils;

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

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
        setContentView(R.layout.activity_assistant_editprofile);
        context = AssistantEditActivity.this;
        preferenceUtils = new PreferenceUtils(context);
        loadingText = new LoadingText(context);
        initViewControll();
        cb_assisting_with_children.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (compoundButton.isChecked()) {
                    cb_assisting_with_children_Value = "assisting with children";

                } else {
                    cb_assisting_with_children_Value = null;
                }
                Log.v("", "res checked " + cb_cooking_Value);
            }
        });

        cb_cooking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    cb_cooking_Value = "Cooking";

                } else {
                    cb_cooking_Value = null;
                }
                Log.v("", "res checked " + cb_cooking_Value);
            }
        });

        cb_House_errands.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    cb_House_errands_Value = "house errands";

                } else {
                    cb_House_errands_Value = null;
                }
                Log.v("", "res checked " + cb_House_errands_Value);
            }
        });

        cb_bet_friendly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    cb_bet_friendly_Value = "betting";

                } else {
                    cb_bet_friendly_Value = null;
                }
                Log.v("", "res checked " + cb_bet_friendly_Value);
            }
        });
        Fabric.with(this, new Crashlytics());
        if (NetworkUtils.isNetworkConnectionAvailable(context)) {
            //  showLoaderNew();
            getProfileData();
        }
        else {
            Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
        }

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
                        edt_experience.getText().length() > 0 &&
                        //  edt_zipcode.getText().length() == 5 &&
                        edt_email.getText().length() > 0 &&
                        edt_phone.getText().length() > 0 &&
                        edt_city.getText().length() > 0 &&
                        edt_state.getText().length() > 0 &&
                        edt_billing.getText().length() > 0 &&
                        edt_about_me.getText().length() > 0 &&
                        edt_gigs.getText().length() > 0 ||
                        validateSkills() != null &&
                        edt_city.getText().length() > 0) {

                    if (isValidEmail(edt_email.getText().toString())) {
                        if (NetworkUtils.isNetworkConnectionAvailable(context)) {
                            loadingText.showLoaderNew(AssistantEditActivity.this);
                            sendData();
                        } else {
                            Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        String error = "Invalid Email";
                        showAlertValidation(error);
                    }
                } else {
                    if (edt_name.getText().length() == 0) {
                        showAlertValidation("Please enter the name");
                    } else if (edt_experience.getText().length() == 0) {
                        showAlertValidation("Please enter the experience");
                    } else if (edt_email.getText().length() == 0) {
                        showAlertValidation("Please enter the email");
                    } else if (edt_phone.getText().length() == 0) {
                        showAlertValidation("Please enter the phone");
                    } else if (edt_city.getText().length() == 0) {
                        showAlertValidation("Please enter the city");
                    } else if (validateSkills() == null || edt_gigs.getText().length() == 0) {
                        showAlertValidation("Please check the Skills");
                    } else if (edt_state.getText().length() == 0) {
                        showAlertValidation("Please enter the state");
                    } else if (edt_billing.getText().length() == 0) {
                        showAlertValidation("Please enter the billing");
                    } else if (edt_about_me.getText().length() == 0) {
                        showAlertValidation("Please enter the About me");
                    }/* else if (edt_gigs.getText().length() == 0) {
                        showAlertValidation("Please enter the types of Gigs");
                    }*/
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void showAlertValidation(String error) {
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

    private void sendData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.ASSISTANT_EDIT_PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        try {
                            loadingText.hideloader(AssistantEditActivity.this);
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("Success")) {
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Success"), Toast.LENGTH_LONG).show();
                                if (destination != null) {
                                    PreferenceUtils preferenceUtils = new PreferenceUtils(getApplicationContext());
                                    preferenceUtils.saveString("a_pic", destination.toString());
                                }
                                //  BaseActivity.setProfileImage(context,destination.toString(),edt_name.getText().toString());
                                finish();
                                Log.v("res ", "res  success" + jsonObject.getString("Success"));
                            } else {
                                jsonObject.getString("error");
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                                Log.v("res ", "res  success" + jsonObject.getString("Error"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("error", "error " + error.toString());
                        loadingText.hideloader(AssistantEditActivity.this);
                        Toast.makeText(AssistantEditActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                String credentials, skillsValue = null;
                if (validateSkills() != null) {
                    skillsValue = edt_gigs.getText().toString() + "," + validateSkills();
                } else {
                    skillsValue = edt_gigs.getText().toString();
                }
                if (imageData != null) {


                    credentials = "a_id=" + preferenceUtils.getStringFromPreference("a_id", "")
                            + "&a_name=" + edt_name.getText().toString() +
                            //"&a_zip=" + edt_zipcode.getText().toString() +
                            "&a_email=" + edt_email.getText().toString() +
                            "&a_mobile=" + edt_phone.getText().toString() +
                            "&a_experience=" + edt_experience.getText().toString() +
                            "&a_city=" + edt_city.getText().toString() +
                            "&a_hourly_rate=" + edt_billing.getText().toString() +
                            "&a_state=" + edt_state.getText().toString() +
                            "&a_about_me=" + edt_about_me.getText().toString() +
                            "&a_skill="
                            + skillsValue +
                            "&photo=" + imageData;
                    Log.v("res ","res if "+credentials);
                } else {

                    credentials = "a_id=" + preferenceUtils.getStringFromPreference("a_id", "")
                            + "&a_name=" + edt_name.getText().toString() +
                            "&a_email=" + edt_email.getText().toString() +
                            "&a_mobile=" + edt_phone.getText().toString() +
                            "&a_experience=" + edt_experience.getText().toString() +
                            "&a_city=" + edt_city.getText().toString() +
                            "&a_hourly_rate=" + edt_billing.getText().toString() +
                            "&a_state=" + edt_state.getText().toString() +
                            "&a_about_me=" + edt_about_me.getText().toString() +
                            "&a_skill=" + skillsValue;
               Log.v("res ","res else "+credentials);
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

    private String validateSkills() {
        if (cb_assisting_with_children_Value != null &&
                cb_cooking_Value != null &&
                cb_bet_friendly_Value != null &&
                cb_House_errands_Value != null) {
            valueSkillls = "assisting with children,Cooking,betting,house errands";
        } else if (cb_assisting_with_children_Value != null) {
            valueSkillls = "assisting with children";
            if (cb_cooking_Value != null) {
                valueSkillls = valueSkillls + ",Cooking";
            } else if (cb_bet_friendly_Value != null) {
                valueSkillls = valueSkillls + ",betting";
            } else if (cb_House_errands_Value != null) {
                valueSkillls = valueSkillls + ",house errands";
            }
        } else if (cb_cooking_Value != null) {
            valueSkillls = "Cooking";
            if (cb_bet_friendly_Value != null) {
                valueSkillls = valueSkillls + ",betting";
            } else if (cb_House_errands_Value != null) {
                valueSkillls = valueSkillls + ",house errands";
            }
        } else if (cb_bet_friendly_Value != null) {
            valueSkillls = "betting";
            if (cb_House_errands_Value != null) {
                valueSkillls = valueSkillls + ",house errands";
            }
        } else if (cb_House_errands_Value != null) {
            valueSkillls = valueSkillls + "house errands";
        }
        return valueSkillls;
    }

    public String getStringImage(Bitmap bmp) {
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
            throw new RuntimeException("crash" + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("crash" + e.toString());
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
                destination = new File(imagePath(data.getData()));
                imageData = getStringImage(bm);
                Log.v("file path ", "file path " + imagePath(data.getData()));
                profilePic.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AssistantEditActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(AssistantEditActivity.this);

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

    private void getProfileData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.ASSISTANT_GET_PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (!jsonObject.has("Error")) {

                                edt_name.setText(jsonObject.getString("a_name"));
                                edt_experience.setText(jsonObject.getString("a_experience"));
                                edt_email.setText(jsonObject.getString("a_email"));
                                edt_phone.setText(jsonObject.getString("a_mobile"));
                                edt_city.setText(jsonObject.getString("a_city"));
                                edt_state.setText(jsonObject.getString("a_state"));
                                edt_billing.setText(jsonObject.getString("a_hourly_rate"));
                                edt_about_me.setText(jsonObject.getString("a_about_me"));
                                // edt_gigs.setText(jsonObject.getString("a_skill"));

                                if (jsonObject.getString("a_skill").equals
                                        ("assisting with children," +
                                                "Cooking," +
                                                "betting," +
                                                "house errands")) {
                                    cb_assisting_with_children.setChecked(true);
                                    cb_cooking.setChecked(true);
                                    cb_House_errands.setChecked(true);
                                    cb_bet_friendly.setChecked(true);
                                } else {
                                    String r = jsonObject.getString("a_skill");
                                    String d[] = r.split(",");
                                    for (int s = 0; s < d.length; s++)

                                    {
                                        Log.v("name ", "selected name " + d[s]);
                                        switch (s) {
                                            case 0:
                                                if (d[s].equals("assisting with children")) {
                                                    cb_assisting_with_children.setChecked(true);

                                                }
                                                if (d[s].equals("Cooking")) {
                                                    cb_cooking.setChecked(true);

                                                }
                                                if (d[s].equals("betting")) {
                                                    cb_bet_friendly.setChecked(true);
                                                }
                                                if (d[s].equals("house errands")) {
                                                    cb_House_errands.setChecked(true);
                                                }
                                                if (!d[s].equals("assisting with children") &&
                                                        !d[s].equals("Cooking") &&
                                                        !d[s].equals("betting") &&
                                                        !d[s].equals("house errands")) {
                                                    edt_gigs.setText(d[s]);
                                                }

                                                break;
                                            case 1:
                                                if (d[s].equals("assisting with children")) {
                                                    cb_assisting_with_children.setChecked(true);

                                                }
                                                if (d[s].equals("Cooking")) {
                                                    cb_cooking.setChecked(true);

                                                }
                                                if (d[s].equals("betting")) {
                                                    cb_bet_friendly.setChecked(true);
                                                }
                                                if (d[s].equals("house errands")) {
                                                    cb_House_errands.setChecked(true);
                                                }
                                                if (!d[s].equals("assisting with children") &&
                                                        !d[s].equals("Cooking") &&
                                                        !d[s].equals("betting") &&
                                                        !d[s].equals("house errands")) {
                                                    edt_gigs.setText(d[s]);
                                                }
                                                break;
                                            case 2:
                                                if (d[s].equals("assisting with children")) {
                                                    cb_assisting_with_children.setChecked(true);

                                                }
                                                if (d[s].equals("Cooking")) {
                                                    cb_cooking.setChecked(true);

                                                }
                                                if (d[s].equals("betting")) {
                                                    cb_bet_friendly.setChecked(true);
                                                }
                                                if (d[s].equals("house errands")) {
                                                    cb_House_errands.setChecked(true);
                                                }
                                                if (!d[s].equals("assisting with children") &&
                                                        !d[s].equals("Cooking") &&
                                                        !d[s].equals("betting") &&
                                                        !d[s].equals("house errands")) {
                                                    edt_gigs.setText(d[s]);
                                                }
                                                break;
                                            case 3:
                                                if (d[s].equals("assisting with children")) {
                                                    cb_assisting_with_children.setChecked(true);

                                                }
                                                if (d[s].equals("Cooking")) {
                                                    cb_cooking.setChecked(true);

                                                }
                                                if (d[s].equals("betting")) {
                                                    cb_bet_friendly.setChecked(true);
                                                }
                                                if (d[s].equals("house errands")) {
                                                    cb_House_errands.setChecked(true);
                                                }
                                                if (!d[s].equals("assisting with children") &&
                                                        !d[s].equals("Cooking") &&
                                                        !d[s].equals("betting") &&
                                                        !d[s].equals("house errands")) {
                                                    edt_gigs.setText(d[s]);
                                                }
                                                break;
                                        }

                                    }
                                }


                                // edt_zipcode.setText(jsonObject.getString("a_zip"));
                                Glide.with(context).load(jsonObject.getString("a_pic"))
                                        .thumbnail(0.5f)
                                        .crossFade()
                                        .error(R.drawable.ic_addprofile)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(profilePic);
                                //  Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Success-msg"), Toast.LENGTH_LONG).show();
                                //startActivity(new Intent(getApplicationContext(), ParentRegisterActivity.class));
                                Log.v("res ", "res  success" + response);
                            } else {
                                jsonObject.getString("Error");
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Error"), Toast.LENGTH_LONG).show();
                                Log.v("res ", "res  success" + jsonObject.getString("Error"));

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            //  throw new RuntimeException("crash" + e.toString());
                            Crashlytics.logException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("error", "error " + error.toString());
                        Toast.makeText(AssistantEditActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        //throw new RuntimeException("crash" + error.toString());
                        Crashlytics.logException(error);
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                String credentials = "a_id=" + preferenceUtils.getStringFromPreference("a_id", "");

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

    @Override
    protected void onResume() {
        super.onResume();
        toolbarTextView.setText("Edit Profile");
    }

    private void initViewControll() {
        View test1View = findViewById(R.id.toolbarLayout);
        toolbarTextView = (TextView) test1View.findViewById(R.id.page_heading);
        profilePic = (ImageView)findViewById(R.id.iv_upload_profile_photo);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_experience = (EditText) findViewById(R.id.edt_experience);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_phone = (EditText) findViewById(R.id.edt_phone);
        edt_city = (EditText) findViewById(R.id.edt_city);
        edt_state = (EditText) findViewById(R.id.edt_state);
        edt_billing = (EditText) findViewById(R.id.edt_billing);
        edt_about_me = (EditText) findViewById(R.id.edt_about_me);
        edt_gigs = (EditText) findViewById(R.id.edt_gigs);
        // edt_zipcode  =(EditText)findViewById(R.id.edt_zipcode);
        bt_save = (Button)findViewById(R.id.btn_save);
        bt_cancel = (Button)findViewById(R.id.btn_cancel);
        ass_edit_parent  = (LinearLayout)findViewById(R.id.ass_edit_parent);

        cb_assisting_with_children = (CheckBox) findViewById(R.id.cb_assisting_with_children);
        cb_cooking = (CheckBox) findViewById(R.id.cb_cooking);
        cb_House_errands = (CheckBox) findViewById(R.id.cb_House_errands);
        cb_bet_friendly = (CheckBox) findViewById(R.id.cb_bet_friendly);


       /* /*//*//*View test1View = findViewById(R.id.toolbarLayout);
        toolbarTextView  = (TextView)findViewById(R.id.page_heading);
        toolbarTextView.setText("Edit Profile");*/
        FontStyle.applyFont(getApplicationContext(),toolbarTextView,FontStyle.Lato_Medium);
        FontStyle.applyFont(getApplicationContext(),ass_edit_parent,FontStyle.Lato_Medium);

    }

}

package parentshour.spinlogics.com.parentshour.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import parentshour.spinlogics.com.parentshour.utilities.NetworkUtils;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;
import parentshour.spinlogics.com.parentshour.utilities.Utility;


/**
 * Created by SPINLOGICS ic_on 12/8/2016.
 */

public class AssistantEditActivity extends BaseActivity {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    String imageData;
    Context context;
    ImageView profilePic;
    private PreferenceUtils preferenceUtils;
    Button bt_save, bt_cancel;
    EditText edt_name,
            edt_experience,
            edt_email,
            edt_phone, edt_city, edt_zipcode, edt_state, edt_billing, edt_about_me, edt_gigs;
    LinearLayout ass_edit_parent;
    TextView toolbarTextView;
    @Override
    public void initialize() {
        context = AssistantEditActivity.this;

        preferenceUtils = new PreferenceUtils(context);

        llContent.addView(inflater.inflate(R.layout.activity_assistant_editprofile, null), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        initViewControll();
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
                        edt_zipcode.getText().length() == 5 &&
                        edt_email.getText().length() > 0 &&
                        edt_phone.getText().length() > 0 &&
                        edt_city.getText().length() > 0 &&
                        edt_state.getText().length() > 0 &&
                        edt_billing.getText().length() > 0 &&
                        edt_about_me.getText().length() > 0 &&
                        edt_gigs.getText().length() > 0 &&
                        edt_city.getText().length() > 0) {

                    if (isValidEmail(edt_email.getText().toString())) {
                        if (NetworkUtils.isNetworkConnectionAvailable(context)) {
                            showLoaderNew();
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
                        showAlertValidation("Please enter the ic_name");
                    } else if (edt_experience.getText().length() == 0) {
                        showAlertValidation("Please enter the experience");
                    } else if (edt_email.getText().length() == 0) {
                        showAlertValidation("Please enter the ic_email");
                    } else if (edt_phone.getText().length() == 0) {
                        showAlertValidation("Please enter the phone");
                    } else if (edt_city.getText().length() == 0) {
                        showAlertValidation("Please enter the city");
                    } else if (edt_zipcode.getText().length() == 0 ||
                            edt_zipcode.getText().length() < 5) {
                        showAlertValidation("Please enter the valid zip code");
                    } else if (edt_state.getText().length() == 0) {
                        showAlertValidation("Please enter the state");
                    } else if (edt_billing.getText().length() == 0) {
                        showAlertValidation("Please enter the billing");
                    } else if (edt_about_me.getText().length() == 0) {
                        showAlertValidation("Please enter the About me");
                    } else if (edt_gigs.getText().length() == 0) {
                        showAlertValidation("Please enter the types od Gigs");
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
showLoaderNew();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.ASSISTANT_EDIT_PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        try {
                            hideloader();
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("Success")) {
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Success"), Toast.LENGTH_LONG).show();
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
                        hideloader();
                        Toast.makeText(AssistantEditActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                String credentials;
                if (imageData != null) {

                    credentials = "a_id=" + preferenceUtils.getStringFromPreference("a_id", "")
                            + "&a_name=" + edt_name.getText().toString() +
                            "&a_zip=" + edt_zipcode.getText().toString() +
                            "&a_email=" + edt_email.getText().toString() +
                            "&a_mobile=" + edt_phone.getText().toString() +
                            "&a_experience=" + edt_experience.getText().toString() +
                            "&a_city=" + edt_city.getText().toString() +
                            "&a_hourly_rate=" + edt_billing.getText().toString() +
                            "&a_state=" + edt_state.getText().toString() +
                            "&a_about_me=" + edt_about_me.getText().toString() +
                            "&a_skill=" + edt_gigs.getText().toString() +
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
                            "&a_skill=" + edt_gigs.getText().toString() ;
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

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
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
                imageData = getStringImage(bm);
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
                                edt_gigs.setText(jsonObject.getString("a_skill"));
                                edt_zipcode.setText(jsonObject.getString("a_zip"));
                                 Glide.with(basecontext).load(jsonObject.getString("a_pic"))
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
                            throw new RuntimeException("crash" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("error", "error " + error.toString());
                        Toast.makeText(AssistantEditActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        throw new RuntimeException("crash" + error.toString());
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

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbarTextView.setText("Edit Profile");
    }

    private void initViewControll() {
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
        edt_zipcode  =(EditText)findViewById(R.id.edt_zipcode);
        bt_save = (Button)findViewById(R.id.btn_save);
        bt_cancel = (Button)findViewById(R.id.btn_cancel);
        ass_edit_parent  = (LinearLayout)findViewById(R.id.ass_edit_parent);

        //View test1View = findViewById(R.id.toolbarLayout);
        toolbarTextView  = (TextView)findViewById(R.id.page_heading);
        toolbarTextView.setText("Edit Profile");
        FontStyle.applyFont(getApplicationContext(),toolbarTextView,FontStyle.Lato_Medium);
        FontStyle.applyFont(getApplicationContext(),ass_edit_parent,FontStyle.Lato_Medium);

        // mtoolbar.setTitle("Edit Profile");
    }

    @Override
    public void goto_playDateSearch_method() {

    }

    @Override
    public void goto_SearchAssistant_method() {

    }

    @Override
    public void goto_AssistantRequests_method() {

    }

    @Override
    public void goto_Friends_method() {

    }

    @Override
    public void goto_Notifications_method() {

    }

    @Override
    public void goto_PlaydateEvents_method() {

    }

    @Override
    public void goto_Settings_method() {

    }

    @Override
    public void goto_Chats_method() {

    }
}

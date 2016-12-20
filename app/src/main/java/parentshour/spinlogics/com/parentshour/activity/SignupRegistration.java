package parentshour.spinlogics.com.parentshour.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
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

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.utilities.AppConstants;
import parentshour.spinlogics.com.parentshour.utilities.FontStyle;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;
import parentshour.spinlogics.com.parentshour.utilities.Utility;

/**
 * Created by SPINLOGICS ic_on 12/5/2016.
 */

public class SignupRegistration extends AppCompatActivity {
    public Dialog dialog;
    Context mContext;
    ImageView iv_upload_profile_photo, iv_add;
    TextView tv_submit, tv_verify;
    EditText edt_name,
            edt_zipcode,
            edt_email,
            edt_phone,
            edt_password,
            edt_conform_password;
    TextView toolbarTextView;
    String imageData;
    PreferenceUtils preferenceUtils;
    LinearLayout parentLayout;
    private AnimationDrawable animationDrawable;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_registration);
        mContext = SignupRegistration.this;
        initializeControls();
        preferenceUtils  = new PreferenceUtils(getApplicationContext());
       String email =  preferenceUtils.getStringFromPreference("email","");

        if(!email.equals(""))
        {
            edt_email.setText(email);
            edt_email.setEnabled(false);
        }
        if(preferenceUtils.getStringFromPreference("select", "").equals("parent"))
        {
            if(!preferenceUtils.getStringFromPreference("p_pic", "").equals(""))
            {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    showLoaderNew();
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            imageData =  getDataFromUrl(preferenceUtils.getStringFromPreference("p_pic", ""));
                        } catch (Exception e) {
                            // log error
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                    hideloader();
                    }

                }.execute();
                Glide.with(getApplicationContext())
                        .load(preferenceUtils.getStringFromPreference("p_pic", ""))
                        .thumbnail(0.5f)
                        .crossFade()
                        .error(R.drawable.ic_addprofile)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(iv_upload_profile_photo);
                iv_add.setVisibility(View.GONE);
            }
        }
       if(preferenceUtils.getStringFromPreference("select", "").equals("assistant"))
        {
            if(!preferenceUtils.getStringFromPreference("a_pic", "").equals(""))
            {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    showLoaderNew();
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            imageData =  getDataFromUrl(preferenceUtils.getStringFromPreference("a_pic", ""));
                        } catch (Exception e) {
                            // log error
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                    hideloader();
                    }

                }.execute();
                Glide.with(getApplicationContext())
                        .load(preferenceUtils.getStringFromPreference("a_pic", ""))
                        .thumbnail(0.5f)
                        .crossFade()
                        .error(R.drawable.ic_addprofile)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(iv_upload_profile_photo);
                iv_add.setVisibility(View.GONE);
            }
        }

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(getApplicationContext(), AssitantFinalRegistration.class));

                if (edt_name.getText().length() > 0 &&
                        edt_zipcode.getText().length() == 5 &&
                        edt_email.getText().length() > 0 &&
                        edt_phone.getText().length() == 10 &&
                        edt_password.getText().length() > 0 &&
                        edt_conform_password.getText().length() > 0) {
                    if (!isValidEmail(edt_email.getText().toString())) {
                        showAlertValidation("Please enter the valid email");
                    } else if (edt_password.getText().toString().equals(edt_conform_password.getText().toString())) {

                        if(preferenceUtils.getStringFromPreference("select","").equals("parent"))
                        {

                            if(imageData != null)
                            { showLoaderNew();
                                sendData();
                            }else
                            {
                                showAlertValidation("Please upload Image");
                                //Toast.makeText(getApplicationContext(),"Please upload Image",Toast.LENGTH_LONG).show();
                            }
                            //startActivity(new Intent(getApplicationContext(), ParentRegisterActivity.class));

                        }else if(preferenceUtils.getStringFromPreference("select","").equals("assistant"))
                        {


                            if(imageData != null)
                            {
                                showLoaderNew();
                                registerAssistant();
                            }else
                            {
                                showAlertValidation("Please upload Image");
                               // Toast.makeText(getApplicationContext(),"Image is missing ",Toast.LENGTH_LONG).show();
                            }
                        }


                    } else {
                        showAlertValidation("password and confirm password doesn't match");
                    }

                } else {
                    if (edt_name.getText().length() == 0) {
                        showAlertValidation("Please enter the name");
                    } else if (edt_zipcode.getText().length() == 0 || edt_zipcode.getText().length() < 5) {
                        showAlertValidation("Please enter the valid zip code");
                    } else if (edt_email.getText().length() == 0) {
                        showAlertValidation("Please enter the email");
                    } else if (edt_phone.getText().length() == 0 || edt_phone.getText().length() < 10) {
                        showAlertValidation("Please enter the valid phone");
                    } else if (edt_password.getText().length() == 0) {
                        showAlertValidation("Please enter the password");
                    } else if (edt_conform_password.getText().length() == 0) {
                        showAlertValidation("Please enter the confirm password");
                    }

                }

            }
        });
        iv_upload_profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
             // Toast.makeText(getApplicationContext(),"Image capture",Toast.LENGTH_LONG).show();
            }
        });

        tv_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_verify, null);
                dialogBuilder.setView(dialogView);
                final AlertDialog b = dialogBuilder.create();
                final EditText edt_otp = (EditText) dialogView.findViewById(R.id.edt_verify_phone);
                final TextView tv_verify_ok = (TextView) dialogView.findViewById(R.id.tv_verify_ok);
                final TextView tv_verify_cancel = (TextView) dialogView.findViewById(R.id.tv_verify_cancel);
                tv_verify_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edt_otp.getText().toString();
                    }
                });
                tv_verify_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        b.dismiss();
                    }
                });
                b.show();
            }
        });
    }

    private void registerAssistant()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.ASSISTANT_SIGNUP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("Success-msg")) {
                                hideloader();
                                jsonObject.getString("Success-msg");
                                jsonObject.getString("Success-aid");
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Success-msg"), Toast.LENGTH_LONG).show();
                                PreferenceUtils preferenceUtils = new PreferenceUtils(getApplicationContext());
                                preferenceUtils.saveString("a_id",jsonObject.getString("Success-aid"));
                                startActivity(new Intent(getApplicationContext(), AssitantFinalRegistration.class));
                                Log.v("res ", "res  success" + jsonObject.getString("Success-msg"));
                            } else {
                                hideloader();
                                jsonObject.getString("Error");
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Error"), Toast.LENGTH_LONG).show();

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
                        Toast.makeText(SignupRegistration.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                String credentials =
                        "a_name=" + edt_name.getText().toString() +
                                "&a_zip=" + edt_zipcode.getText().toString() +
                                "&a_email=" + edt_email.getText().toString() +
                                "&a_mobile=" + edt_phone.getText().toString() +
                                "&a_password=" + edt_password.getText().toString() +
                                "&photo="+imageData;
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
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SignupRegistration.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(SignupRegistration.this);

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

        iv_upload_profile_photo.setImageBitmap(thumbnail);
        iv_add.setVisibility(View.GONE);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                imageData = getStringImage(bm);
                iv_upload_profile_photo.setImageBitmap(bm);
                iv_add.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }



    private void sendData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.SIGNUP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("Success-msg")) {
                                hideloader();
                                jsonObject.getString("Success-msg");
                                jsonObject.getString("Success-pid");
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Success-msg"), Toast.LENGTH_LONG).show();
                                PreferenceUtils preferenceUtils = new PreferenceUtils(getApplicationContext());

                               if(preferenceUtils.getStringFromPreference("select","").equals("parent"))
                               {
                                   preferenceUtils.saveString("loggedin", "loggedin");
                                   preferenceUtils.saveString("p_id",jsonObject.getString("Success-pid"));
                                   startActivity(new Intent(getApplicationContext(), ParentRegisterActivity.class));
                                   finish();
                               }

                                if(preferenceUtils.getStringFromPreference("select","").equals("assistant"))
                               {
                                   preferenceUtils.saveString("loggedin", "loggedin");
                                   preferenceUtils.saveString("a_id",jsonObject.getString("Success-aid"));
                                   startActivity(new Intent(getApplicationContext(), AssitantFinalRegistration.class));
                                   finish();
                               }
                                Log.v("res ", "res  success" + jsonObject.getString("Success-msg"));
                            } else {
                                hideloader();
                                jsonObject.getString("error");
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Error"), Toast.LENGTH_LONG).show();
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
                        hideloader();
                        Log.v("error", "error " + error.toString());
                        Toast.makeText(SignupRegistration.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                String credentials =
                        "p_name=" + edt_name.getText().toString() +
                                "&p_zip=" + edt_zipcode.getText().toString() +
                                "&p_email=" + edt_email.getText().toString() +
                                "&p_mobile=" + edt_phone.getText().toString() +
                                "&p_password=" + edt_password.getText().toString() +
                                "&photo="+imageData;
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
       // Toast.makeText(getApplicationContext(), "Sending data ......", Toast.LENGTH_LONG).show();
    }

    public void showAlertValidation(String error) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
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

    public String getDataFromUrl(String urlLink)
    {
        String encodedImage = null;
        try {
            InputStream in = new URL(urlLink).openStream();
            Bitmap  bmp = BitmapFactory.decodeStream(in);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return encodedImage ;
    }
    public String getDataFromUri(Uri data)
    {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data);
                imageData = getStringImage(bm);
                iv_upload_profile_photo.setImageBitmap(bm);
                iv_add.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imageData;
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void initializeControls() {
        View test1View = findViewById(R.id.toolbarLayout);
        toolbarTextView = (TextView) test1View.findViewById(R.id.page_heading);
        iv_upload_profile_photo = (ImageView) findViewById(R.id.iv_upload_profile_photo);
        iv_add = (ImageView) findViewById(R.id.iv_add_profile);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_zipcode = (EditText) findViewById(R.id.edt_zipcode);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_phone = (EditText) findViewById(R.id.edt_phone);
        edt_password = (EditText) findViewById(R.id.edt_password);
        edt_conform_password = (EditText) findViewById(R.id.edt_conform_password);
       // edt_name = (EditText) findViewById(R.id.edt_name);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        tv_verify = (TextView) findViewById(R.id.tv_verify);
        toolbarTextView.setText("Registration");
        parentLayout = (LinearLayout)findViewById(R.id.parentLayout);
        FontStyle.applyFont(getApplicationContext(),parentLayout,FontStyle.Lato_Medium);
    }

    public void showLoaderNew() {
        runOnUiThread(new SignupRegistration.Runloader(getResources().getString(R.string.loading)));
    }

    public void hideloader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class Runloader implements Runnable {
        private String strrMsg;

        public Runloader(String strMsg) {
            this.strrMsg = strMsg;
        }

        @SuppressWarnings("ResourceType")
        @Override
        public void run() {
            try {
                if (dialog == null)
                {
                    dialog = new Dialog(mContext,R.style.Theme_Dialog_Translucent);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                }
                dialog.setContentView(R.layout.loading);
                dialog.setCancelable(false);

                if (dialog != null && dialog.isShowing())
                {
                    dialog.dismiss();
                    dialog=null;
                }
                dialog.show();

                ImageView imgeView = (ImageView) dialog
                        .findViewById(R.id.imgeView);
                TextView tvLoading = (TextView) dialog
                        .findViewById(R.id.tvLoading);
                if (!strrMsg.equalsIgnoreCase(""))
                    tvLoading.setText(strrMsg);
                animationDrawable = (AnimationDrawable) imgeView
                        .getBackground();
                imgeView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (animationDrawable != null)
                            animationDrawable.start();
                    }
                });
            } catch (Exception e)
            {

            }
        }
    }
}

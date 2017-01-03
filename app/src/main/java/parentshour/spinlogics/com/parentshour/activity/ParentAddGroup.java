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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.adapter.ParentGroupRowAdapter;
import parentshour.spinlogics.com.parentshour.models.ParentFriendModel;
import parentshour.spinlogics.com.parentshour.models.PlaySearchDateModel;
import parentshour.spinlogics.com.parentshour.utilities.AppConstants;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;
import parentshour.spinlogics.com.parentshour.utilities.Utility;

/**
 * Created by SPINLOGICS on 12/27/2016.
 */

public class ParentAddGroup extends BaseActivity {
    Context context;
    EditText et_groupName;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter adapter;
    TextView iv_addMemberToGroup, tv_done, tv_cancel;
    ArrayList<PlaySearchDateModel> parentAddGroupArrayList;
    ArrayList<ParentFriendModel> parentFriendModels;
    ParentFriendModel groupId;
    String imageData;
    ImageView iv_upload_profile_photo;
    private String userChoosenTask, friendsList;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1, SELECT_FRIENDS = 2;

    @Override
    public void initialize() {
        context = ParentAddGroup.this;

        preferenceUtils = new PreferenceUtils(context);
        parentFriendModels = new ArrayList<ParentFriendModel>();
        llContent.addView(inflater.inflate(R.layout.activity_parent_addgroup, null), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initViewControll();
        if (getIntent().getExtras().get("groupId") != null) {
            groupId = getIntent().getExtras().getParcelable("groupId");
            Log.v("get group", "get group " + groupId.getpImgUrl() + "\t " + groupId.getpName() + "\t " + groupId.getpId());
            et_groupName.setText(groupId.getpName());
            Glide.with(context)
                    .load(groupId.getpImgUrl())
                    .error(R.drawable.ic_profilelogo)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_upload_profile_photo);
            getGroupMembers();
        }


        Fabric.with(this, new Crashlytics());
    }

    private void initViewControll() {
        TextView toolbarTextView = (TextView) findViewById(R.id.page_heading);
        toolbarTextView.setText("Add Group");
        RelativeLayout uploadImageLayout = (RelativeLayout) findViewById(R.id.uploadImageLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerAddedGroupMembers);
        iv_addMemberToGroup = (TextView) findViewById(R.id.tv_addMemberToGroup);
        iv_upload_profile_photo = (ImageView) findViewById(R.id.iv_upload_profile_photo);
        et_groupName = (EditText) findViewById(R.id.edt_name);

        tv_done = (TextView) findViewById(R.id.tv_done);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

        parentAddGroupArrayList = new ArrayList<PlaySearchDateModel>();

        iv_addMemberToGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startActivity(new Intent(getApplicationContext(), ParentAddFriendList.class));
                startActivityForResult(new Intent(getApplicationContext(), ParentAddFriendList.class), SELECT_FRIENDS);
            }
        });
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getExtras().get("groupId") != null) {

                    if (et_groupName.getText().length() > 0 || friendsList.length() > 0) {
                        editedGroup();
                    } else {
                        if (friendsList.length() > 0) {
                            showAlertValidation("Add a friend");
                        }
                        if (et_groupName.getText().length() > 0) {
                            showAlertValidation("Group name cannot be empty");
                        }
                    }
                } else {
                    if (et_groupName.getText().length() > 0 || friendsList.length() > 0) {
                        createGroup();
                    } else {
                        if (friendsList.length() > 0) {
                            showAlertValidation("Add a friend");
                        }
                        if (et_groupName.getText().length() > 0) {
                            showAlertValidation("Group name cannot be empty");
                        }
                    }
                }

            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        uploadImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    private void createGroup() {
        showLoaderNew();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.PARENT_CREATE_GROUP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        Log.v("res ", "res  " + preferenceUtils.getStringFromPreference("p_id", ""));
                        hideloader();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("Success")) {
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Success"), Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                jsonObject.getString("Error");
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Error"), Toast.LENGTH_LONG).show();
                                Log.v("res ", "res  success" + jsonObject.getString("Error"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Crashlytics.logException(e);
                            // throw new RuntimeException("crash" + e.toString());
                            //throw new RuntimeException("crash" + e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideloader();
                        Log.v("error", "error " + error.toString());
                        Toast.makeText(ParentAddGroup.this, error.toString(), Toast.LENGTH_LONG).show();
                        Crashlytics.logException(error);
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                String credentials;
                credentials = "p_id=" + preferenceUtils.getStringFromPreference("p_id", "") + "&grp_name=" + et_groupName.getText().toString() + "&grp_friends=" + friendsList + "&grp_pic=" + imageData;  /*+ preferenceUtils.getStringFromPreference("p_id", "")*/
                Log.v("credentials", "credentials " + et_groupName.getText().toString());
                Log.v("credentials", "credentials " + credentials);

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

    private void editedGroup() {

        showLoaderNew();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.PARENT_EDIT_GROUP_MEMBERS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        Log.v("res ", "res  " + preferenceUtils.getStringFromPreference("p_id", ""));
                        hideloader();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("Success")) {
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Success"), Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                jsonObject.getString("Error");
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Error"), Toast.LENGTH_LONG).show();
                                Log.v("res ", "res  success" + jsonObject.getString("Error"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Crashlytics.logException(e);
                            // / throw new RuntimeException("crash" + e.toString());
                            //throw new RuntimeException("crash" + e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideloader();
                        Log.v("error", "error " + error.toString());
                        Toast.makeText(ParentAddGroup.this, error.toString(), Toast.LENGTH_LONG).show();
                        Crashlytics.logException(error);
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                String credentials;

                if (imageData != null) {
                    credentials = "p_id=" + preferenceUtils.getStringFromPreference("p_id", "") + "&grp_name=" + et_groupName.getText().toString() + "&grp_friends=" + friendsList + "&grp_id=" + groupId.getpId() + "&grp_pic=" + imageData;  /*+ preferenceUtils.getStringFromPreference("p_id", "")*/
                    Log.v("credentials", "credentials " + et_groupName.getText().toString());
                    Log.v("credentials", "credentials " + groupId.getpId());
                    Log.v("credentials", "credentials " + imageData);
                    Log.v("credentials", "credentials " + credentials);

                } else {
                    credentials = "p_id=" + preferenceUtils.getStringFromPreference("p_id", "") + "&grp_name=" + et_groupName.getText().toString() + "&grp_friends=" + friendsList + "&grp_id=" + groupId.getpId() + "&grp_pic=";
                    Log.v("credentials", "credentials " + et_groupName.getText().toString());
                    Log.v("credentials", "credentials " + groupId.getpId());
                    Log.v("credentials", "credentials " + credentials);
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


    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ParentAddGroup.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ParentAddGroup.this);

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
        //Log.v("friend list","friend list: "+ requestCode+" data "+data.getStringExtra("friendsList"));
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            } else if (requestCode == 2) {
                arrayToString(data);
                //  Log.v("friend list","friend list: "+data.getStringExtra("friendsList"));
            }
        }
    }

    private void arrayToString(Intent data) {
        friendsList = data.getStringExtra("friendsList");
        Log.v("friend list", "friend list: " + data.getStringExtra("friendsList"));
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
        Glide.with(context)
                .load(destination)
                .error(R.drawable.ic_profilelogo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_upload_profile_photo);

        //  iv_upload_profile_photo.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                imageData = getStringImage(bm);
                iv_upload_profile_photo.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    private void getGroupMembers() {
        showLoaderNew();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.PARENT_GROUP_MEMBERS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        Log.v("res ", "res  " + preferenceUtils.getStringFromPreference("p_id", ""));
                        hideloader();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.has("Error")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("grp_friends2");
                                int sz = jsonArray.length();
                                for (int g = 0; g < sz; g++) {
                                    JSONObject friendObject = jsonArray.getJSONObject(g);
                                    ParentFriendModel parentFriendModel = new ParentFriendModel();
                                    parentFriendModel.setpId(friendObject.getString("p_id"));
                                    parentFriendModel.setpName(friendObject.getString("p_name"));
                                    //   parentFriendModel.setpImgUrl(friendObject.getString("p_pic"));
                                    parentFriendModels.add(parentFriendModel);
                                }
                                adapter = new ParentGroupRowAdapter(parentFriendModels, context);
                                mRecyclerView.setAdapter(adapter);
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
                        hideloader();
                        Log.v("error", "error " + error.toString());
                        Toast.makeText(ParentAddGroup.this, error.toString(), Toast.LENGTH_LONG).show();
                        // throw new RuntimeException("crash" + error.toString());
                        Crashlytics.logException(error);
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                String credentials = "p_id=" + preferenceUtils.getStringFromPreference("p_id", "") + "&grp_id=" + groupId.getpId();
                //  String credentials = "p_id=42&grp_id=" + groupId;
                Log.v("url", "url " + credentials);
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

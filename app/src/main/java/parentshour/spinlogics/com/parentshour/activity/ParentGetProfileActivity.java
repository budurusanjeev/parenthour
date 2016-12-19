package parentshour.spinlogics.com.parentshour.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.utilities.AppConstants;
import parentshour.spinlogics.com.parentshour.utilities.FontStyle;
import parentshour.spinlogics.com.parentshour.utilities.NetworkUtils;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;

/**
 * Created by SPINLOGICS ic_on 12/7/2016.
 */

public class ParentGetProfileActivity extends BaseActivity {

    Context context;
    JSONObject jsonObject;
   // private View decorView;
    ImageView profilePic;
    //LinearLayout ll_parent_dashboard, ll_child_dashboard;
    private boolean doubleBackToExitPressedOnce = false;
    private PreferenceUtils preferenceUtils;
    TextView tv_name,tv_zipcode,tv_email,tv_mobile,
             tv_age,tv_occupation,tv_gender,tv_education,tv_ethnicity,
             tv_available_days,tv_available_time,tv_edit,tv_username;

    @Override
    public void initialize() {
        context = ParentGetProfileActivity.this;

        preferenceUtils = new PreferenceUtils(context);

        llContent.addView(inflater.inflate(R.layout.activity_parent_getprofile, null), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        initViewControll();
        Fabric.with(this, new Crashlytics());
       /* if (NetworkUtils.isNetworkConnectionAvailable(context)) {
            //  showLoaderNew();
        //    postData();
            getProfileData();
        } else {
            Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
        }*/

        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
startActivity(new Intent(getApplicationContext(),ParentEditActivity.class));
            }
        });
    }

    private void getProfileData()
    {
        showLoaderNew();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.GET_PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        hideloader();
                        try {
                            jsonObject = new JSONObject(response);

                            if (!jsonObject.has("Error"))
                            {
                                tv_name.setText(jsonObject.getString("p_name"));
                                tv_username.setText(jsonObject.getString("p_name"));
                                tv_zipcode.setText(jsonObject.getString("p_zip"));
                                tv_email.setText(jsonObject.getString("p_email"));
                                tv_mobile.setText(jsonObject.getString("p_mobile"));
                                tv_age.setText(jsonObject.getString("p_age"));
                                tv_occupation.setText(jsonObject.getString("p_occupation"));
                                tv_gender.setText(jsonObject.getString("p_gender"));
                                tv_education.setText(jsonObject.getString("p_education"));
                                tv_ethnicity.setText(jsonObject.getString("p_ethnicity"));
                                tv_available_days.setText(jsonObject.getString("p_avail_days"));
                                tv_available_time.setText(jsonObject.getString("p_avail_time"));
                                Glide.with(basecontext).load(jsonObject.getString("p_pic"))
                                        .thumbnail(0.5f)
                                        .crossFade().error(R.drawable.ic_addprofile)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(profilePic);

                                // Toast.makeText(getApplicationContext(), "" , Toast.LENGTH_LONG).show();
                                //startActivity(new Intent(getApplicationContext(), ParentRegisterActivity.class));
                              //  Log.v("res ", "res  success" + jsonObject.getString("Success-msg"));
                            } else {
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
                        hideloader();
                        Toast.makeText(ParentGetProfileActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        throw new RuntimeException("crash" + error.toString());
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

    private void initViewControll() {
        profilePic = (ImageView)findViewById(R.id.iv_upload_profile_photo);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_zipcode = (TextView)findViewById(R.id.tv_zipcode) ;
        tv_email = (TextView)findViewById(R.id.tv_email);
        tv_mobile = (TextView)findViewById(R.id.tv_mobile);
        tv_age = (TextView)findViewById(R.id.tv_age);
        tv_occupation = (TextView)findViewById(R.id.tv_occupation);
        tv_gender = (TextView)findViewById(R.id.tv_gender);
        tv_education = (TextView)findViewById(R.id.tv_education);
        tv_ethnicity = (TextView)findViewById(R.id.tv_ethnicity);
        tv_available_days = (TextView)findViewById(R.id.tv_available_days);
        tv_available_time = (TextView)findViewById(R.id.tv_available_time);
        tv_username = (TextView)findViewById(R.id.tv_userName_edit);
        tv_edit = (TextView)findViewById(R.id.tv_edit_text);
  //      View test1View = findViewById(R.id.toolbarLayout);
        TextView toolbarTextView = (TextView)findViewById(R.id.page_heading);
        toolbarTextView.setText("Profile");
        LinearLayout par_get_layout = (LinearLayout)findViewById(R.id.par_get_layout);
        FontStyle.applyFont(getApplicationContext(),par_get_layout,FontStyle.Lato_Medium);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkUtils.isNetworkConnectionAvailable(context)) {
            //  showLoaderNew();
            //    postData();
            getProfileData();
        } else {
            Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
        }
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

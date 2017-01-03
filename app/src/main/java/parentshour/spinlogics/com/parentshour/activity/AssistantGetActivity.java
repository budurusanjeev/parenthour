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

import static parentshour.spinlogics.com.parentshour.R.id.tv_phone;

/**
 * Created by SPINLOGICS ic_on 12/8/2016.
 */

public class AssistantGetActivity extends BaseActivity {
    Context context;
    JSONObject jsonObject;
    ImageView profilePic;
    TextView tv_name,tv_userName_edit,tv_email,tv_mobile,
            tv_experience,tv_city,tv_state,tv_billing,tv_about,
            tv_type_gigs,tv_edit;
    LinearLayout ass_get_parent;
    private PreferenceUtils preferenceUtils;

    @Override
    public void initialize() {
        context = AssistantGetActivity.this;

        preferenceUtils = new PreferenceUtils(context);

        llContent.addView(inflater.inflate(R.layout.activity_assistant_getprofile, null), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        initViewControll();
        //getProfileData();
        Fabric.with(this, new Crashlytics());
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AssistantEditActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbarTextView.setText("Profile");
        if (NetworkUtils.isNetworkConnectionAvailable(context)) {
            // showLoaderNew();

            getProfileData();
        }
        else {
            Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private void getProfileData()
    {
        showLoaderNew();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.ASSISTANT_GET_PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideloader();
                        Log.v("res ", "res  " + response);
                        try {
                            jsonObject = new JSONObject(response);

                            if (!jsonObject.has("Error"))
                            {
                                tv_name.setText(jsonObject.getString("a_name"));
                                tv_userName_edit.setText(jsonObject.getString("a_name"));
                                tv_email.setText(jsonObject.getString("a_email"));
                                tv_mobile.setText(jsonObject.getString("a_mobile"));
                                tv_experience.setText(jsonObject.getString("a_experience"));
                                tv_city.setText(jsonObject.getString("a_city"));
                                tv_state.setText(jsonObject.getString("a_state"));
                                tv_billing.setText(jsonObject.getString("a_hourly_rate"));
                                tv_about.setText(jsonObject.getString("a_about_me"));
                                tv_type_gigs.setText(jsonObject.getString("a_skill"));
                                Glide.with(basecontext).load(jsonObject.getString("a_pic"))
                                        .thumbnail(0.5f).error(R.drawable.ic_addprofile)
                                        .crossFade()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(profilePic);
//                                Log.v("res ", "res  success" + jsonObject.getString("Success"));
                            } else {
                                jsonObject.getString("Error");
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Error"), Toast.LENGTH_LONG).show();
                              //  Log.v("res ", "res  success" + jsonObject.getString("Error"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                         //   public void forceCrash(View view) {
                            //  throw new RuntimeException("crash" + e.toString());
                           // }
                            Crashlytics.logException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("error", "error " + error.toString());
                        hideloader();
                        Toast.makeText(AssistantGetActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Crashlytics.logException(error);
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                String credentials ="a_id="+preferenceUtils.getStringFromPreference("a_id","");
               // Log.v("error", "error " + credentials);
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
        // stringRequest.setRetryPolicy(new DefaultRetryPolicy(1000,2,));
        requestQueue.add(stringRequest);
    }

    private void initViewControll() {
        ass_get_parent = (LinearLayout)findViewById(R.id.ass_get_parent);
        profilePic =(ImageView)findViewById(R.id.iv_upload_profile_photo);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_userName_edit = (TextView)findViewById(R.id.tv_userName_edit) ;
        tv_email = (TextView)findViewById(R.id.tv_email);
        tv_mobile = (TextView)findViewById(tv_phone);
        tv_experience = (TextView)findViewById(R.id.tv_experience);
        tv_city = (TextView)findViewById(R.id.tv_city);
        tv_state = (TextView)findViewById(R.id.tv_state);
        tv_billing = (TextView)findViewById(R.id.tv_billing);
        tv_about=(TextView)findViewById(R.id.tv_about);
        tv_type_gigs=(TextView)findViewById(R.id.tv_type_gigs);
        tv_edit = (TextView)findViewById(R.id.tv_edit_text);
        //View test1View = findViewById(R.id.toolbarLayout);
         toolbarTextView = (TextView)findViewById(R.id.page_heading);
        toolbarTextView.setText("Profile");
       // mtoolbar.setTitle("Profile");
        FontStyle.applyFont(getApplicationContext(),toolbarTextView,FontStyle.Lato_Medium);
        FontStyle.applyFont(getApplicationContext(),ass_get_parent,FontStyle.Lato_Medium);
        //  FontStyle.setFont(ass_get_parent, context);
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

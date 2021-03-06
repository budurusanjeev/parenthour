package parentshour.spinlogics.com.parentshour.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import parentshour.spinlogics.com.parentshour.utilities.LoadingText;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;

/**
 * Created by SPINLOGICS on 12/28/2016.
 */

public class ParentPlaySearchDateDetailViewActivity extends AppCompatActivity {
    TextView toolbarTextView;
    Context context;
    ImageView profilePic;
    PreferenceUtils preferenceUtils;
    TextView tv_name, tv_zipcode, tv_email, tv_mobile,
            tv_age, tv_occupation, tv_gender, tv_education, tv_ethnicity,
            tv_available_days, tv_available_time, tv_username;
    String parentId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_search_detailview);
        context = ParentPlaySearchDateDetailViewActivity.this;
        preferenceUtils = new PreferenceUtils(context);
        parentId = getIntent().getExtras().getString("id");
        initViewControll();
        getProfileData();
        // Toast.makeText(getApplicationContext(),"\t:  "+parentId,Toast.LENGTH_LONG).show();
        Fabric.with(this, new Crashlytics());


    }

    private void getProfileData() {
        // showLoaderNew();
        final LoadingText loadingText = new LoadingText(ParentPlaySearchDateDetailViewActivity.this);
        loadingText.showLoaderNew(ParentPlaySearchDateDetailViewActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.GET_PARENT_PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        // hideloader();
                        loadingText.hideloader(ParentPlaySearchDateDetailViewActivity.this);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (!jsonObject.has("Error")) {
                                tv_name.setText(jsonObject.getString("p_name"));
                                tv_username.setText(jsonObject.getString("p_name"));
                                tv_zipcode.setText(jsonObject.getString("p_zip"));
                                //tv_email.setText(jsonObject.getString("p_email"));
                                // tv_mobile.setText(jsonObject.getString("p_mobile"));
                                tv_email.setVisibility(View.GONE);
                                tv_mobile.setVisibility(View.GONE);
                                tv_age.setText(jsonObject.getString("p_age"));
                                tv_occupation.setText(jsonObject.getString("p_occupation"));
                                tv_gender.setText(jsonObject.getString("p_gender"));
                                tv_education.setText(jsonObject.getString("p_education"));
                                tv_ethnicity.setText(jsonObject.getString("p_ethnicity"));
                                tv_available_days.setText(jsonObject.getString("p_avail_days"));
                                tv_available_time.setText(jsonObject.getString("p_avail_time"));
                                Glide.with(context).load(jsonObject.getString("p_pic"))
                                        .thumbnail(0.5f)
                                        .crossFade().error(R.drawable.ic_addprofile)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(profilePic);
                            } else {
                                loadingText.hideloader(ParentPlaySearchDateDetailViewActivity.this);
                                jsonObject.getString("Error");
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Error"), Toast.LENGTH_LONG).show();
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
                        Log.v("error", "error " + error.toString());
                        //  hideloader();
                        loadingText.hideloader(ParentPlaySearchDateDetailViewActivity.this);
                        Toast.makeText(ParentPlaySearchDateDetailViewActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        //throw new RuntimeException("crash" + error.toString());
                        Crashlytics.logException(error);
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                String credentials = "p_id=" + preferenceUtils.getStringFromPreference("p_id", "") + "&parent_id=" + parentId;
                // String credentials = "p_id=12&parent_id=" + parentId;
                Log.v("credentials ", "credentials " + credentials);
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
        profilePic = (ImageView) findViewById(R.id.iv_upload_profile_photo);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_zipcode = (TextView) findViewById(R.id.tv_zipcode);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_mobile = (TextView) findViewById(R.id.tv_mobile);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_occupation = (TextView) findViewById(R.id.tv_occupation);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        tv_education = (TextView) findViewById(R.id.tv_education);
        tv_ethnicity = (TextView) findViewById(R.id.tv_ethnicity);
        tv_available_days = (TextView) findViewById(R.id.tv_available_days);
        tv_available_time = (TextView) findViewById(R.id.tv_available_time);
        tv_username = (TextView) findViewById(R.id.tv_userName_edit);
        View test1View = findViewById(R.id.toolbarLayout);
        LinearLayout emailLayout = (LinearLayout) findViewById(R.id.emailLayout);
        LinearLayout mobileLayout = (LinearLayout) findViewById(R.id.mobileLayout);
        emailLayout.setVisibility(View.GONE);
        mobileLayout.setVisibility(View.GONE);
        toolbarTextView = (TextView) test1View.findViewById(R.id.page_heading);
        toolbarTextView.setText("Parent Profile");

        TextView tv_Done = (TextView) test1View.findViewById(R.id.setting_Save);
        tv_Done.setVisibility(View.VISIBLE);
        tv_Done.setText("Done");
        tv_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

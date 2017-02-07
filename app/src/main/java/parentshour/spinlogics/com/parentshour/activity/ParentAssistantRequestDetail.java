package parentshour.spinlogics.com.parentshour.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;

/**
 * Created by SPINLOGICS on 1/12/2017.
 */

public class ParentAssistantRequestDetail extends AppCompatActivity {
    Context context;
    TextView tv_name, tv_rating, tv_experience, tv_state, tv_city, tv_billing, tv_about, tv_type_of_gigs, tv_request, tv_cancel, tv_userName_edit;
    ImageView iv_profile_pic;
    String aid;
    View test1View;
    PreferenceUtils preferenceUtils;
    TextView toolbarTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = ParentAssistantRequestDetail.this;

        preferenceUtils = new PreferenceUtils(context);
        setContentView(R.layout.parent_assistant_request_detail);
        //llContent.addView(inflater.inflate(R.layout.parent_assistant_request_detail, null), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        initViewControll();
        if (getIntent().getExtras().get("aid") != null) {
            aid = getIntent().getExtras().getString("aid");
            Log.v("get group", "get group " + aid);
            getAssistantDetails();
        }
        Fabric.with(this, new Crashlytics());
    }

    private void getAssistantDetails() {
        // showLoaderNew();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.PARENT_ASSISTANT_DETAIL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        Log.v("res ", "res  " + preferenceUtils.getStringFromPreference("p_id", ""));
                        //         hideloader();

                        try {

                            if (response != null) {
                                JSONObject jsonObject = new JSONObject(response);
                                tv_userName_edit.setText(jsonObject.getString("a_name"));
                                tv_name.setText(jsonObject.getString("a_name"));
                                tv_experience.setText(jsonObject.getString("a_experience"));
                                tv_state.setText(jsonObject.getString("a_state"));
                                tv_city.setText(jsonObject.getString("a_city"));
                                tv_billing.setText(jsonObject.getString("a_hourly_rate"));
                                tv_about.setText(jsonObject.getString("a_about_me"));
                                tv_type_of_gigs.setText(jsonObject.getString("a_skill"));
                                tv_rating.setText(jsonObject.getString("a_rating"));
                                Glide.with(context).load(jsonObject.getString("a_pic"))
                                        .thumbnail(0.5f).error(R.drawable.ic_profilelogo)
                                        .crossFade()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(iv_profile_pic);
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
                        //  hideloader();
                        Log.v("error", "error " + error.toString());
                        Toast.makeText(ParentAssistantRequestDetail.this, error.toString(), Toast.LENGTH_LONG).show();
                        Crashlytics.logException(error);
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                String credentials = "p_id=" + preferenceUtils.getStringFromPreference("p_id", "") + "&a_id=" + aid;

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

    private void initViewControll() {
        tv_userName_edit = (TextView) findViewById(R.id.tv_userName_edit);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_experience = (TextView) findViewById(R.id.tv_experience);
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_billing = (TextView) findViewById(R.id.tv_billing);
        tv_about = (TextView) findViewById(R.id.tv_about);
        tv_rating = (TextView) findViewById(R.id.tv_rating);
        tv_type_of_gigs = (TextView) findViewById(R.id.tv_type_gigs);
        tv_request = (TextView) findViewById(R.id.tv_request);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        iv_profile_pic = (ImageView) findViewById(R.id.iv_upload_profile_photo);
        test1View = findViewById(R.id.toolbarLayout);

        tv_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ParentAssistantSetRequest.class)
                        .putExtra("aid", aid));
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        toolbarTextView = (TextView) test1View.findViewById(R.id.page_heading);
        toolbarTextView.setText("Assistant Profile");
    }
}

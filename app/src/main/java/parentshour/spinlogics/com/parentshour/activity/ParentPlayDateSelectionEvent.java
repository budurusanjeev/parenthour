package parentshour.spinlogics.com.parentshour.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.adapter.ParentPlayDateEventAdapter;
import parentshour.spinlogics.com.parentshour.models.ParentFriendModel;
import parentshour.spinlogics.com.parentshour.models.PlayDateEventModelParcel;
import parentshour.spinlogics.com.parentshour.utilities.AppConstants;
import parentshour.spinlogics.com.parentshour.utilities.NetworkUtils;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;

/**
 * Created by SPINLOGICS on 1/2/2017.
 */

public class ParentPlayDateSelectionEvent extends BaseActivity {
    Context context;
    RecyclerView mRecyclerView;
    ArrayList<PlayDateEventModelParcel> playSearchArrayList;
    RelativeLayout searchLayout;

    @Override
    public void initialize() {
        context = ParentPlayDateSelectionEvent.this;
        preferenceUtils = new PreferenceUtils(context);
        llContent.addView(inflater.inflate(R.layout.activity_assistant_dashboard, null), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        playSearchArrayList = new ArrayList<PlayDateEventModelParcel>();
        initViewControll();
        Fabric.with(this, new Crashlytics());
    }

    private void initViewControll() {
        TextView toolbarTextView = (TextView) findViewById(R.id.page_heading);
        toolbarTextView.setText("PlayDate Events");
        TextView tv_save = (TextView) findViewById(R.id.setting_Save);
        tv_save.setVisibility(View.VISIBLE);
        tv_save.setText("Done");
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        searchLayout = (RelativeLayout) findViewById(R.id.searchLayout);
        searchLayout.setVisibility(View.GONE);
        if (NetworkUtils.isNetworkConnectionAvailable(context)) {

            mRecyclerView = (RecyclerView) findViewById(R.id.assistant_dashborad_recyclerView);
            mRecyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(layoutManager);

        } else {
            Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
        }
    }


    private void getCounts() {
        showLoaderNew();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.PARENT_NOTIFICATIONS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        hideloader();
                        Log.v("res ", "res  " + preferenceUtils.getStringFromPreference("p_id", ""));
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("Success")) {
                                JSONObject jsonObjectSuccess = jsonObject.getJSONObject("Success");

                                JSONArray jsonArrayPlayDateEvents = jsonObjectSuccess.getJSONArray("Play_Date_Events");
                                int u = jsonArrayPlayDateEvents.length();
                                for (int v = 0; v < u; v++) {
                                    JSONObject jsonObjectTitle = jsonArrayPlayDateEvents.getJSONObject(v);
                                    PlayDateEventModelParcel parentFriendModel = new PlayDateEventModelParcel();

                                    parentFriendModel.setpEid(jsonObjectTitle.getString("pe_id"));
                                    parentFriendModel.setDate(jsonObjectTitle.getString("pe_date"));
                                    parentFriendModel.setTime(jsonObjectTitle.getString("pe_time"));
                                    parentFriendModel.setAddress(jsonObjectTitle.getString("pe_address"));
                                    JSONArray playDateJsonArray = jsonObjectTitle.getJSONArray("pid_list");
                                    int q = playDateJsonArray.length();
                                    ArrayList<ParentFriendModel> arrayList = new ArrayList<ParentFriendModel>();
                                    for (int h = 0; h < q; h++) {
                                        JSONObject jsonObjectParent = playDateJsonArray.getJSONObject(h);
                                        ParentFriendModel parentFriendModelLoop = new ParentFriendModel();
                                        parentFriendModelLoop.setpId(jsonObjectParent.getString("p_id"));
                                        parentFriendModelLoop.setpName(jsonObjectParent.getString("p_name"));
                                        parentFriendModelLoop.setpImgUrl(jsonObjectParent.getString("p_pic"));
                                        Log.v("zzz", "zzz " + parentFriendModelLoop.getpName() + "\t" + parentFriendModelLoop.getpImgUrl());
                                        arrayList.add(parentFriendModelLoop);
                                    }
                                    parentFriendModel.setPlayDateMembers(arrayList);
                                    playSearchArrayList.add(parentFriendModel);
                                    Log.v("zzz", "zzz " + parentFriendModel.getPlayDateMembers().size());
                                }
                                mRecyclerView.setAdapter(new ParentPlayDateEventAdapter(playSearchArrayList));

                            } else {
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
                        hideloader();
                        Log.v("error", "error " + error.toString());
                        Toast.makeText(ParentPlayDateSelectionEvent.this, error.toString(), Toast.LENGTH_LONG).show();
                        Crashlytics.logException(error);
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                String credentials = "p_id=" + preferenceUtils.getStringFromPreference("p_id", "");
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

    public void acceptRequest(final String friendId) {
        showLoaderNew();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.PARENT_ACCEPT_EVENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        hideloader();
                        Log.v("res ", "res  " + preferenceUtils.getStringFromPreference("p_id", ""));
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
                            throw new RuntimeException("crash" + e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideloader();
                        Log.v("error", "error " + error.toString());
                        Toast.makeText(ParentPlayDateSelectionEvent.this, error.toString(), Toast.LENGTH_LONG).show();
                        throw new RuntimeException("crash" + error.toString());
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                String credentials = "p_id=" + preferenceUtils.getStringFromPreference("p_id", "") + "&pe_id=" + friendId;
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

    public void rejectRequest(final String friendId) {
        showLoaderNew();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.PARENT_REJECT_EVENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        hideloader();
                        Log.v("res ", "res  " + preferenceUtils.getStringFromPreference("p_id", ""));
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("Success")) {

                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Success"), Toast.LENGTH_LONG).show();
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
                        hideloader();
                        Log.v("error", "error " + error.toString());
                        Toast.makeText(ParentPlayDateSelectionEvent.this, error.toString(), Toast.LENGTH_LONG).show();
                        throw new RuntimeException("crash" + error.toString());
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                String credentials = "p_id=" + preferenceUtils.getStringFromPreference("p_id", "") + "&pe_id=" + friendId;
                Log.v("credentials", "credentials " + credentials);
                // String credentials = "p_id=12"; /*+ preferenceUtils.getStringFromPreference("p_id", "");*/

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

  /*  private void getPlayDate() {
        showLoaderNew();

    }*/

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

    @Override
    protected void onResume() {
        super.onResume();
        playSearchArrayList.clear();
        // getPlayDate();
        getCounts();
    }
}
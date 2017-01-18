package parentshour.spinlogics.com.parentshour.activity;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import java.util.Random;

import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.adapter.AssistantDashBoardAdapter;
import parentshour.spinlogics.com.parentshour.adapter.AssistantNotificationAdapter;
import parentshour.spinlogics.com.parentshour.models.AssistantDashboardModel;
import parentshour.spinlogics.com.parentshour.utilities.AppConstants;
import parentshour.spinlogics.com.parentshour.utilities.NetworkUtils;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;

/**
 * Created by SPINLOGICS on 1/12/2017.
 */

public class AssistantNotificationsActivity extends BaseActivity {
    Context context;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<AssistantDashboardModel> assistantList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Random mRandom = new Random();

    @Override
    public void initialize() {
        context = AssistantNotificationsActivity.this;

        preferenceUtils = new PreferenceUtils(context);

        llContent.addView(inflater.inflate(R.layout.activity_assistant_dashboard, null), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if (NetworkUtils.isNetworkConnectionAvailable(context)) {

            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.assistant_swipeRefreshLayout);
            mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
            mRecyclerView = (RecyclerView) findViewById(R.id.assistant_dashborad_recyclerView);
            mRecyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(layoutManager);
            assistantList = new ArrayList<AssistantDashboardModel>();
            getAssistantList();

            adapter = new AssistantDashBoardAdapter(assistantList, context);
            mRecyclerView.setAdapter(adapter);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getAssistantList();
                    mSwipeRefreshLayout.setRefreshing(false);

                }
            });
        } else {
            Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private void getAssistantList() {
        showLoaderNew();
        // PARENT_SEARCH_PLAY_DATE_URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.ASSISTANT_NOTIFICATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response.trim());
                        hideloader();
                        Log.v("res ", "res  " + preferenceUtils.getStringFromPreference("a_id", ""));
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("Success")) {
                                JSONArray jsonArrayFriends = jsonObject.getJSONArray("Success");
                                int s = jsonArrayFriends.length();
                                for (int p = 0; p < s; p++) {
                                    JSONObject jsonObjectData = jsonArrayFriends.getJSONObject(p);
                                    AssistantDashboardModel assistantDashboardModel = new AssistantDashboardModel();
                                    assistantDashboardModel.setpId(jsonObjectData.getString("p_id"));
                                    assistantDashboardModel.setpName(jsonObjectData.getString("p_name"));
                                    assistantDashboardModel.setDate(jsonObjectData.getString("a_date"));
                                    assistantDashboardModel.setName(jsonObjectData.getString("p_name"));
                                    assistantDashboardModel.setA_req_id(jsonObjectData.getString("a_req_id"));
                                    // assistantDashboardModel.setStatus(jsonObjectData.getString("a_status"));
                                    assistantDashboardModel.setTime(jsonObjectData.getString("a_start_time"));
                                    assistantDashboardModel.setEndtime(jsonObjectData.getString("a_end_time"));
                                    assistantDashboardModel.setTitle(jsonObjectData.getString("a_task_name"));
                                    assistantDashboardModel.setImgUrl(jsonObjectData.getString("p_pic"));
                                    assistantList.add(assistantDashboardModel);
                                }
                                adapter = new AssistantNotificationAdapter(assistantList, context);
                                mRecyclerView.setAdapter(adapter);

                            } else {
                                jsonObject.getString("Error");
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Error"), Toast.LENGTH_LONG).show();
                                Log.v("res ", "res  success" + jsonObject.getString("Error"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //throw new RuntimeException("crash" + e.toString());
                            Crashlytics.logException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideloader();
                        Log.v("error", "error " + error.toString());
                        Toast.makeText(AssistantNotificationsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        //throw new RuntimeException("crash " + error);
                        Crashlytics.logException(error);
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                String credentials = "a_id=" + preferenceUtils.getStringFromPreference("a_id", "");
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

    public void acceptAssistant(final String aid) {
        showLoaderNew();
        // PARENT_SEARCH_PLAY_DATE_URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.ASSISTANT_ACCEPT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response.trim());
                        hideloader();
                        Log.v("res ", "res  " + preferenceUtils.getStringFromPreference("a_id", ""));
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
                            //throw new RuntimeException("crash" + e.toString());
                            Crashlytics.logException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideloader();
                        Log.v("error", "error " + error.toString());
                        Toast.makeText(AssistantNotificationsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        //throw new RuntimeException("crash " + error);
                        Crashlytics.logException(error);
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                String credentials = "a_id=" + preferenceUtils.getStringFromPreference("a_id", "") + "&a_req_id=" + aid;
                // String credentials = "p_id=12"; /*+ preferenceUtils.getStringFromPreference("p_id", "");*/
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

    public void rejectAssistant(final String aid) {
        showLoaderNew();
        // PARENT_SEARCH_PLAY_DATE_URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.ASSISTANT_REJECT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response.trim());
                        hideloader();
                        Log.v("res ", "res  " + preferenceUtils.getStringFromPreference("a_id", ""));
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
                            //throw new RuntimeException("crash" + e.toString());
                            Crashlytics.logException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideloader();
                        Log.v("error", "error " + error.toString());
                        Toast.makeText(AssistantNotificationsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        //throw new RuntimeException("crash " + error);
                        Crashlytics.logException(error);
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                String credentials = "a_id=" + preferenceUtils.getStringFromPreference("a_id", "") + "&a_req_id=" + aid;
                // String credentials = "p_id=12"; /*+ preferenceUtils.getStringFromPreference("p_id", "");*/
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
        toolbarTextView.setText("Notification");
    }
}


package parentshour.spinlogics.com.parentshour.activity;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
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
import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.adapter.ListChipsAdapter;
import parentshour.spinlogics.com.parentshour.models.PlayDateEventsModel;
import parentshour.spinlogics.com.parentshour.utilities.AppConstants;
import parentshour.spinlogics.com.parentshour.utilities.NetworkUtils;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;

/**
 * Created by SPINLOGICS on 12/28/2016.
 */

public class ParentPlayDateEvents extends BaseActivity {
    Context context;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<PlayDateEventsModel> playDateArrayList;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void initialize() {
        context = ParentPlayDateEvents.this;

        preferenceUtils = new PreferenceUtils(context);

        llContent.addView(inflater.inflate(R.layout.activity_assistant_dashboard, null), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        initViewControll();
        getPlayDateEvents();
        Fabric.with(this, new Crashlytics());
    }

    private void initViewControll() {
        TextView toolbarTextView = (TextView) findViewById(R.id.page_heading);
        toolbarTextView.setText("Play Date");

        if (NetworkUtils.isNetworkConnectionAvailable(context)) {

            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.assistant_swipeRefreshLayout);
            mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
            mRecyclerView = (RecyclerView) findViewById(R.id.assistant_dashborad_recyclerView);
            mRecyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(layoutManager);
            playDateArrayList = new ArrayList<PlayDateEventsModel>();

            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Refresh the data
                    // Calls setRefreshing(false) when it is finish

                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playDateArrayList.clear();
                            getPlayDateEvents();
                            mRecyclerView.setAdapter(adapter);

                        }
                    }, 1500);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        } else {
            Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private void getPlayDateEvents() {
        showLoaderNew();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.PARENT_PLAY_DATE_EVENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        Log.v("res ", "res  " + preferenceUtils.getStringFromPreference("p_id", ""));
                        hideloader();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("Success")) {
                                JSONArray jsonArrayFriends = jsonObject.getJSONArray("Success");
                                int s = jsonArrayFriends.length();
                                for (int g = 0; g < s; g++) {
                                    JSONObject jsonObjectParent = jsonArrayFriends.getJSONObject(g);
                                    PlayDateEventsModel playDateEventsModel = new PlayDateEventsModel();
                                    playDateEventsModel.setDate(jsonObjectParent.getString("pe_date"));
                                    playDateEventsModel.setTime(jsonObjectParent.getString("pe_time"));
                                    playDateEventsModel.setpEid(jsonObjectParent.getString("pe_id"));
                                    int c = jsonObjectParent.getJSONArray("pid_list").length();
                                    JSONArray profileArray = jsonObjectParent.getJSONArray("pid_list");
                                    ArrayList<PlayDateEventsModel> subProfileArray = new ArrayList<PlayDateEventsModel>();
                                    for (int a = 0; a < c; a++) {
                                        JSONObject profileObject = profileArray.getJSONObject(a);
                                        PlayDateEventsModel subPlayDateEventsModel = new PlayDateEventsModel();
                                        subPlayDateEventsModel.setpId(profileObject.getString("p_id"));
                                        subPlayDateEventsModel.setName(profileObject.getString("p_name"));
                                        subPlayDateEventsModel.setImgurl(profileObject.getString("p_pic"));
                                        subProfileArray.add(subPlayDateEventsModel);
                                        playDateEventsModel.setPlayDateMembers(subProfileArray);
                                    }
                                    playDateArrayList.add(playDateEventsModel);
                                }
                                mRecyclerView.setAdapter(new ListChipsAdapter(playDateArrayList));
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
                        Toast.makeText(ParentPlayDateEvents.this, error.toString(), Toast.LENGTH_LONG).show();
                        //throw new RuntimeException("crash" + error.toString());
                        Crashlytics.logException(error);
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                String credentials = "p_id=" + preferenceUtils.getStringFromPreference("p_id", "");
                // String credentials = "p_id=12" /*+ preferenceUtils.getStringFromPreference("p_id", "")*/;

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

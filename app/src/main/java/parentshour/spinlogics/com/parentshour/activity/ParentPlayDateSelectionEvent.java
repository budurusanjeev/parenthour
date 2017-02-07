package parentshour.spinlogics.com.parentshour.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import parentshour.spinlogics.com.parentshour.adapter.ParentChipsAdapter;
import parentshour.spinlogics.com.parentshour.adapter.ParentPlayDateEventAdapter;
import parentshour.spinlogics.com.parentshour.models.ParentFriendModel;
import parentshour.spinlogics.com.parentshour.models.PlayDateEventModelParcel;
import parentshour.spinlogics.com.parentshour.utilities.AppConstants;
import parentshour.spinlogics.com.parentshour.utilities.LoadingText;
import parentshour.spinlogics.com.parentshour.utilities.NetworkUtils;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;

/**
 * Created by SPINLOGICS on 1/2/2017.
 */

public class ParentPlayDateSelectionEvent extends AppCompatActivity {
    Context context;
    RecyclerView mRecyclerView;
    ArrayList<PlayDateEventModelParcel> playSearchArrayList;
    RelativeLayout searchLayout;
    TextView toolbarTextView;
    PreferenceUtils preferenceUtils;
    LoadingText loadingText;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_play_date_selection);
        context = ParentPlayDateSelectionEvent.this;
        preferenceUtils = new PreferenceUtils(context);
        loadingText = new LoadingText(context);
        playSearchArrayList = new ArrayList<PlayDateEventModelParcel>();
        initViewControll();
        Fabric.with(this, new Crashlytics());
    }

    private void initViewControll() {
        View test1View = findViewById(R.id.toolbarLayout);
        toolbarTextView = (TextView) test1View.findViewById(R.id.page_heading);
        // toolbarTextView = (TextView) findViewById(R.id.page_heading);
        TextView tv_save = (TextView) test1View.findViewById(R.id.setting_Save);
        tv_save.setVisibility(View.VISIBLE);
        tv_save.setText("Done");
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.assistant_swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRecyclerView = (RecyclerView) findViewById(R.id.assistant_dashborad_recyclerView);
        mRecyclerView.setHasFixedSize(true);

        searchLayout = (RelativeLayout) findViewById(R.id.searchLayout);
        searchLayout.setVisibility(View.GONE);
        if (NetworkUtils.isNetworkConnectionAvailable(context)) {

            mRecyclerView = (RecyclerView) findViewById(R.id.assistant_dashborad_recyclerView);
            mRecyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(layoutManager);

            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    playSearchArrayList.clear();
                    getCounts();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        } else {
            Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
        }
    }


    private void getCounts() {
        loadingText.showLoaderNew(ParentPlayDateSelectionEvent.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.PARENT_NOTIFICATIONS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        loadingText.hideloader(ParentPlayDateSelectionEvent.this);
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
                        loadingText.hideloader(ParentPlayDateSelectionEvent.this);
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

    public void removeItem(int position, ParentChipsAdapter adapter) {
        playSearchArrayList.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, playSearchArrayList.size());
    }

    public void acceptRequest(final String friendId, final int position, final ParentChipsAdapter adapter) {
        loadingText.showLoaderNew(ParentPlayDateSelectionEvent.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.PARENT_ACCEPT_EVENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        loadingText.hideloader(ParentPlayDateSelectionEvent.this);
                        Log.v("res ", "res  " + preferenceUtils.getStringFromPreference("p_id", ""));
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("Success")) {

                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Success"), Toast.LENGTH_LONG).show();
                                //finish();
                                removeItem(position, adapter);
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
                        loadingText.hideloader(ParentPlayDateSelectionEvent.this);
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

    public void rejectRequest(final String friendId, final int position, final ParentChipsAdapter adapter) {
        loadingText.showLoaderNew(ParentPlayDateSelectionEvent.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.PARENT_REJECT_EVENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        loadingText.hideloader(ParentPlayDateSelectionEvent.this);
                        Log.v("res ", "res  " + preferenceUtils.getStringFromPreference("p_id", ""));
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("Success")) {
                                removeItem(position, adapter);
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
                        loadingText.hideloader(ParentPlayDateSelectionEvent.this);
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

    @Override
    protected void onResume() {
        super.onResume();
        playSearchArrayList.clear();
        // getPlayDate();
        getCounts();
        toolbarTextView.setText("PlayDate Events");
    }
}
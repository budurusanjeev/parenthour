package parentshour.spinlogics.com.parentshour.activity;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import parentshour.spinlogics.com.parentshour.adapter.ParentAssistantRequestsAdapter;
import parentshour.spinlogics.com.parentshour.models.ParentAssistantRequestsModel;
import parentshour.spinlogics.com.parentshour.utilities.AppConstants;
import parentshour.spinlogics.com.parentshour.utilities.NetworkUtils;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;

/**
 * Created by SPINLOGICS on 1/11/2017.
 */

public class ParentAssistantRequests extends BaseActivity {
    Context context;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter adapter;
    EditText editTextPlayDateSearch;
    ArrayList<ParentAssistantRequestsModel> playSearchArrayList;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void initialize() {
        context = ParentAssistantRequests.this;

        preferenceUtils = new PreferenceUtils(context);

        llContent.addView(inflater.inflate(R.layout.activity_assistant_dashboard, null), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        initViewControll();
        Fabric.with(this, new Crashlytics());
    }

    private void initViewControll() {


        if (NetworkUtils.isNetworkConnectionAvailable(context)) {

            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.assistant_swipeRefreshLayout);
            mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
            mRecyclerView = (RecyclerView) findViewById(R.id.assistant_dashborad_recyclerView);
            mRecyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(layoutManager);
            playSearchArrayList = new ArrayList<ParentAssistantRequestsModel>();
            editTextPlayDateSearch = (EditText) findViewById(R.id.searchAutoComplete);

            /*editTextPlayDateSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    charSequence = charSequence.toString().toLowerCase();

                    final ArrayList<PlaySearchDateModel> filteredList = new ArrayList<PlaySearchDateModel>();

                    for (int v = 0; v < playSearchArrayList.size(); v++) {

                        final String text = playSearchArrayList.get(v).getpName().toLowerCase();
                        if (text.contains(charSequence)) {

                            filteredList.add(playSearchArrayList.get(v));
                        }
                    }
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(ParentAssistantRequests.this));
                    adapter = new ParentSearchPlayAdapter(filteredList, context);
                    mRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void afterTextChanged(Editable editable) {


                }
            });*/
            // getSearchAssistant();
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playSearchArrayList.clear();
                            getSearchAssistant();
                        }
                    }, 1500);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        } else {
            Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
        }
    }

    public void ratingDialog(final String aId, final String aReqId) {
        Log.v("credentials method", "credentials method " + aId + " " + aReqId);
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_rating, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        final RatingBar rb_userRating = (RatingBar) dialogView.findViewById(R.id.assistant_rating);
        final TextView tv_rating = (TextView) dialogView.findViewById(R.id.tv_rate_me);

        tv_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext()," "+ rb_userRating.getRating(),Toast.LENGTH_LONG).show();
                setAssistantRating(aId, aReqId, String.valueOf(rb_userRating.getRating()));
                b.dismiss();
            }
        });

        b.show();
    }

    public void setAssistantRating(final String aId, final String aReqId, final String rating) {
        showLoaderNew();
        // PARENT_SEARCH_PLAY_DATE_URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.ASSISTANT_SET_RATING_URL,
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
                        Toast.makeText(ParentAssistantRequests.this, error.toString(), Toast.LENGTH_LONG).show();
                        //throw new RuntimeException("crash " + error);
                        Crashlytics.logException(error);
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                String credentials = "p_id=" + preferenceUtils.getStringFromPreference("p_id", "") +
                        "&a_id=" + aId +
                        "&p_req_id=" + aReqId + "&p_stars=" + rating;
                // String credentials = "p_id=12"; /*+ preferenceUtils.getStringFromPreference("p_id", "");*/
//p_id=12&a_id=2&a_req_id=1&a_stars=5

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
    private void getSearchAssistant() {
        showLoaderNew();
        // PARENT_SEARCH_PLAY_DATE_URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.PARENT_ASSISTANT_REQUESTS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response.trim());
                        hideloader();
                        Log.v("res ", "res  " + preferenceUtils.getStringFromPreference("p_id", ""));
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("Success")) {
                                JSONArray jsonArrayFriends = jsonObject.getJSONArray("Success");
                                int s = jsonArrayFriends.length();
                                for (int g = 0; g < s; g++) {
                                    JSONObject dateJsonObject = jsonArrayFriends.getJSONObject(g);
                                    ParentAssistantRequestsModel parentAssistantRequestsModel = new ParentAssistantRequestsModel();
                                    parentAssistantRequestsModel.setA_id(dateJsonObject.getString("a_id"));
                                    parentAssistantRequestsModel.setA_name(dateJsonObject.getString("a_name"));
                                    parentAssistantRequestsModel.setA_pic(dateJsonObject.getString("a_pic"));
                                    parentAssistantRequestsModel.setA_task_name(dateJsonObject.getString("a_task_name"));
                                    parentAssistantRequestsModel.setA_date(dateJsonObject.getString("a_date"));
                                    parentAssistantRequestsModel.setA_start_time(dateJsonObject.getString("a_start_time"));
                                    parentAssistantRequestsModel.setA_end_time(dateJsonObject.getString("a_end_time"));
                                    parentAssistantRequestsModel.setA_req_id(dateJsonObject.getString("a_req_id"));
                                    parentAssistantRequestsModel.setA_status(dateJsonObject.getString("a_status"));

                                    playSearchArrayList.add(parentAssistantRequestsModel);
                                }
                                adapter = new ParentAssistantRequestsAdapter(playSearchArrayList, context);
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
                        Toast.makeText(ParentAssistantRequests.this, error.toString(), Toast.LENGTH_LONG).show();
                        //throw new RuntimeException("crash " + error);
                        Crashlytics.logException(error);
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                String credentials = "p_id=" + preferenceUtils.getStringFromPreference("p_id", "");
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
        TextView toolbarTextView = (TextView) findViewById(R.id.page_heading);
        toolbarTextView.setText("Assistant Requests");
        if (NetworkUtils.isNetworkConnectionAvailable(context)) {
            getSearchAssistant();
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

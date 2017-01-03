package parentshour.spinlogics.com.parentshour.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
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
import parentshour.spinlogics.com.parentshour.models.ParentFriendModel;
import parentshour.spinlogics.com.parentshour.models.PlayDateEventModelParcel;
import parentshour.spinlogics.com.parentshour.utilities.AppConstants;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;

/**
 * Created by SPINLOGICS on 12/30/2016.
 */

public class ParentNotificationsActivity extends BaseActivity {
    Context context;
    TextView tv_friendCount, tv_play_date_count, tv_play_cou_next, tv_fri_cou_next;
    ArrayList<ParentFriendModel> friendRequestArray;
    ArrayList<PlayDateEventModelParcel> playDateEvent;
    int u, s;

    @Override
    public void initialize() {
        context = ParentNotificationsActivity.this;

        preferenceUtils = new PreferenceUtils(context);

        llContent.addView(inflater.inflate(R.layout.activity_notifications, null), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        friendRequestArray = new ArrayList<ParentFriendModel>();
        playDateEvent = new ArrayList<PlayDateEventModelParcel>();
        initViewControll();

        Fabric.with(this, new Crashlytics());
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
                                if (!jsonObjectSuccess.getJSONArray("Friend_Requests").equals("No value for Friend Requests")) {
                                    JSONArray jsonArrayFriends = jsonObjectSuccess.getJSONArray("Friend_Requests");
                                    s = jsonArrayFriends.length();
                                    for (int g = 0; g < s; g++) {
                                        JSONObject jsonObjectParent = jsonArrayFriends.getJSONObject(g);
                                        ParentFriendModel parentFriendModel = new ParentFriendModel();
                                        parentFriendModel.setpId(jsonObjectParent.getString("p_id"));
                                        parentFriendModel.setpName(jsonObjectParent.getString("p_name"));
                                        parentFriendModel.setpImgUrl(jsonObjectParent.getString("p_pic"));
                                        friendRequestArray.add(parentFriendModel);
                                    }
                                    tv_friendCount.setText(String.valueOf(s));
                                    JSONArray jsonArrayPlayDateEvents = jsonObjectSuccess.getJSONArray("Play_Date_Events");
                                    u = jsonArrayPlayDateEvents.length();
                                    /*for(int v=0; v<u; v++){
                                        JSONObject jsonObjectTitle= jsonArrayPlayDateEvents.getJSONObject(v);
                                        PlayDateEventModelParcel parentFriendModel = new PlayDateEventModelParcel();

                                        parentFriendModel.setpEid(jsonObjectTitle.getString("pe_id"));
                                        parentFriendModel.setDate(jsonObjectTitle.getString("pe_date"));
                                        parentFriendModel.setTime(jsonObjectTitle.getString("pe_time"));
                                        parentFriendModel.setAddress( jsonObjectTitle.getString("pe_address"));
                                     JSONArray playDateJsonArray = jsonObjectTitle.getJSONArray("pid_list");
                                        int q = playDateJsonArray.length();
                                        ArrayList<ParentFriendModel> arrayList = new ArrayList<ParentFriendModel>();
                                        for (int h = 0; h < q; h++) {
                                            JSONObject jsonObjectParent = playDateJsonArray.getJSONObject(h);
                                            ParentFriendModel parentFriendModelLoop = new ParentFriendModel();
                                            parentFriendModelLoop.setpId(jsonObjectParent.getString("p_id"));
                                            parentFriendModelLoop.setpName(jsonObjectParent.getString("p_name"));
                                            parentFriendModelLoop.setpImgUrl(jsonObjectParent.getString("p_pic"));
                                            Log.v("zzz","zzz "+parentFriendModelLoop.getpName()+"\t"+parentFriendModelLoop.getpImgUrl());
                                            arrayList.add(parentFriendModelLoop);
                                        }
                                        parentFriendModel.setPlayDateMembers(arrayList);
                                        playDateEvent.add(parentFriendModel);
                                    Log.v("zzz","zzz "+parentFriendModel.getPlayDateMembers().size());
                                    }
*/
                                    tv_play_date_count.setText(String.valueOf(u));
                                } else {
                                    Toast.makeText(getApplicationContext(), "" + jsonObject.getJSONArray("Friend Requests"), Toast.LENGTH_LONG).show();
                                }


                            } else {
                                jsonObject.getString("Error");
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Error"), Toast.LENGTH_LONG).show();
                                Log.v("res ", "res  success" + jsonObject.getString("Error"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            // throw new RuntimeException("crash" + e.toString());
                            Crashlytics.logException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideloader();
                        Log.v("error", "error " + error.toString());
                        Toast.makeText(ParentNotificationsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        // throw new RuntimeException("crash" + error.toString());
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

    private void initViewControll() {
        tv_friendCount = (TextView) findViewById(R.id.tv_friend_request_count);
        tv_play_date_count = (TextView) findViewById(R.id.tv_play_date_events_count);
        tv_play_cou_next = (TextView) findViewById(R.id.tv_play_cou_next);
        tv_fri_cou_next = (TextView) findViewById(R.id.tv_fri_cou_next);
        tv_fri_cou_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (s > 0) {
                    startActivity(new Intent(getApplicationContext(), ParentFriendRequestActivity.class)
                            .putParcelableArrayListExtra("friends", friendRequestArray));
                } else {
                    Toast.makeText(getApplicationContext(), "No friend requests", Toast.LENGTH_LONG).show();
                }

            }
        });
        tv_play_cou_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (u > 0) {
                    startActivity(new Intent(getApplicationContext(), ParentPlayDateSelectionEvent.class)
                            .putParcelableArrayListExtra("events", playDateEvent));
                } else {
                    Toast.makeText(getApplicationContext(), "No play date events", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCounts();
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

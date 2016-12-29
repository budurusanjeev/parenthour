package parentshour.spinlogics.com.parentshour.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.models.PlaySearchDateModel;
import parentshour.spinlogics.com.parentshour.utilities.AppConstants;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;

/**
 * Created by SPINLOGICS on 12/27/2016.
 */

public class ParentAddGroup extends BaseActivity {
    Context context;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter adapter;
    TextView iv_addMemberToGroup, tv_done, tv_cancel;
    ArrayList<PlaySearchDateModel> parentAddGroupArrayList;
    String groupId;

    @Override
    public void initialize() {
        context = ParentAddGroup.this;

        preferenceUtils = new PreferenceUtils(context);

        llContent.addView(inflater.inflate(R.layout.activity_parent_addgroup, null), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        initViewControll();
        if (!getIntent().getExtras().getString("groupId").equals("")) {
            groupId = getIntent().getExtras().getString("groupId");
        }
        Fabric.with(this, new Crashlytics());
    }

    private void initViewControll() {
        TextView toolbarTextView = (TextView) findViewById(R.id.page_heading);
        toolbarTextView.setText("Add Group");
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerAddedGroupMembers);
        iv_addMemberToGroup = (TextView) findViewById(R.id.tv_addMemberToGroup);
        tv_done = (TextView) findViewById(R.id.tv_done);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        parentAddGroupArrayList = new ArrayList<PlaySearchDateModel>();
        getGroupMembers();
        iv_addMemberToGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ParentAddFriendList.class));
            }
        });
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getGroupMembers() {
        showLoaderNew();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.PARENT_GROUP_MEMBERS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        Log.v("res ", "res  " + preferenceUtils.getStringFromPreference("p_id", ""));
                        hideloader();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("Success")) {

                                //adapter = new ParentGroupAdapter(parentAddGroupArrayList, context);
                                mRecyclerView.setAdapter(adapter);

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
                        Toast.makeText(ParentAddGroup.this, error.toString(), Toast.LENGTH_LONG).show();
                        throw new RuntimeException("crash" + error.toString());
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                //   String credentials = "p_id=" + preferenceUtils.getStringFromPreference("p_id", "");
                String credentials = "p_id=12&grp_id=" + groupId; /*+ preferenceUtils.getStringFromPreference("p_id", "")*/

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

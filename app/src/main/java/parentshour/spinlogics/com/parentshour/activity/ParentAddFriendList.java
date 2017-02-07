package parentshour.spinlogics.com.parentshour.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.adapter.ParentAddFriendAdapter;
import parentshour.spinlogics.com.parentshour.adapter.ParentFriendAdapter;
import parentshour.spinlogics.com.parentshour.models.ParentFriendModel;
import parentshour.spinlogics.com.parentshour.utilities.AppConstants;
import parentshour.spinlogics.com.parentshour.utilities.LoadingText;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;

/**
 * Created by SPINLOGICS on 12/27/2016.
 */

public class ParentAddFriendList extends AppCompatActivity {
    Context context;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<ParentFriendModel> parentAddedFriendArrayList;
    // ArrayList<String> parentselectedFriendArrayList;
    List<ParentFriendModel> stList;
    int size = 0;
    List<String> myFriendsList;
    SwipeRefreshLayout mSwipeRefreshLayout;
    EditText editTextFriendSearch;
    PreferenceUtils preferenceUtils;
    LoadingText loadingText;
    TextView toolbarTextView, tv_done;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parents_add_friendlist_layout);
        context = ParentAddFriendList.this;
        loadingText = new LoadingText(context);
        preferenceUtils = new PreferenceUtils(context);
        initViewControll();

        if (getIntent().getParcelableArrayListExtra("selected") != null) {

            ArrayList<ParentFriendModel> parentAddedFriendArrayList = getIntent().getParcelableArrayListExtra("selected");
            size = parentAddedFriendArrayList.size();
            myFriendsList = new ArrayList<String>();
            for (int s = 0; s < size; s++) {
                myFriendsList.add(parentAddedFriendArrayList.get(s).getpId());
                Log.v("names ", "names " + parentAddedFriendArrayList.get(s).getpName());
            }
        }

        Fabric.with(this, new Crashlytics());
    }

    private void initViewControll() {
        //TextView toolbarTextView = (TextView) findViewById(R.id.page_heading);
        View test1View = findViewById(R.id.toolbarLayout);
        toolbarTextView = (TextView) test1View.findViewById(R.id.page_heading);
        tv_done = (TextView) findViewById(R.id.setting_Save);

        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = "";
                stList = ((ParentAddFriendAdapter) adapter)
                        .getStudentist();
                ArrayList<ParentFriendModel> parentFriendModels = new ArrayList<ParentFriendModel>();
                if (((ParentAddFriendAdapter) adapter)
                        .getStudentist().size() > 0) {
                    for (int i = 0; i < stList.size(); i++) {
                        ParentFriendModel singleStudent = stList.get(i);
                        if (singleStudent.getSelectFriend()) {
                            data = data + "," + singleStudent.getpId();
                            ParentFriendModel parentFriendModel = new ParentFriendModel();
                            parentFriendModel.setpId(singleStudent.getpId());
                            parentFriendModel.setpName(singleStudent.getpName());
                            parentFriendModel.setpImgUrl(singleStudent.getpImgUrl());
                            parentFriendModels.add(parentFriendModel);
                        }
                    }

                    Intent intent = getIntent();
                    intent.putExtra("friendsList", data);
                    intent.putParcelableArrayListExtra("friendObject", parentFriendModels);
                    setResult(Activity.RESULT_OK, intent);
                    Log.v("friend list selected ", "friend list selected: " + data);
                    finish();
                } else {
                    finish();
                }
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.assistant_swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView = (RecyclerView) findViewById(R.id.assistant_dashborad_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        parentAddedFriendArrayList = new ArrayList<ParentFriendModel>();
        editTextFriendSearch = (EditText) findViewById(R.id.searchAutoComplete);


        editTextFriendSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                charSequence = charSequence.toString().toLowerCase();

                final ArrayList<ParentFriendModel> filteredList = new ArrayList<ParentFriendModel>();

                for (int v = 0; v < parentAddedFriendArrayList.size(); v++) {

                    final String text = parentAddedFriendArrayList.get(v).getpName().toLowerCase();
                    if (text.contains(charSequence)) {

                        filteredList.add(parentAddedFriendArrayList.get(v));
                    }
                }
                mRecyclerView.setLayoutManager(new LinearLayoutManager(ParentAddFriendList.this));
                adapter = new ParentFriendAdapter(filteredList, context);
                mRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        // parentselectedFriendArrayList = new ArrayList<ParentFriendModel>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbarTextView.setText("Add Friends");
        tv_done.setVisibility(View.VISIBLE);
        tv_done.setText("Done");
        parentAddedFriendArrayList.clear();
        getAddedFriends();

    }

    private void getAddedFriends() {
        loadingText.showLoaderNew(ParentAddFriendList.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.PARENT_ADDED_FRIENDS_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingText.hideloader(ParentAddFriendList.this);
                        Log.v("res ", "res  " + response);
                        Log.v("res ", "res  " + preferenceUtils.getStringFromPreference("p_id", ""));
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            //  parentAddedFriendArrayList = new ArrayList<ParentFriendModel>();
                            if (jsonObject.has("Success")) {
                                JSONArray jsonArrayFriends = jsonObject.getJSONArray("Success");
                                int s = jsonArrayFriends.length();
                                for (int g = 0; g < s; g++) {
                                    ParentFriendModel playSearchDateModel = new ParentFriendModel();
                                    JSONObject jsonObjectParent = jsonArrayFriends.getJSONObject(g);
                                    playSearchDateModel.setpId(jsonObjectParent.getString("p_id"));
                                    playSearchDateModel.setpName(jsonObjectParent.getString("p_name"));
                                    playSearchDateModel.setpImgUrl(jsonObjectParent.getString("p_pic"));
                                    if (size > 0) {
                                        if (myFriendsList.contains(jsonObjectParent.getString("p_id"))) {
                                            playSearchDateModel.setSelectFriend(true);
                                            Log.v("name ", "name true" + jsonObjectParent.getString("p_id"));
                                        } else {
                                            playSearchDateModel.setSelectFriend(false);
                                            Log.v("name ", "name false" + jsonObjectParent.getString("p_id"));
                                        }
                                    } else {
                                        playSearchDateModel.setSelectFriend(false);
                                    }

                                    parentAddedFriendArrayList.add(playSearchDateModel);
                                }
                                adapter = new ParentAddFriendAdapter(parentAddedFriendArrayList, context);
                                mRecyclerView.setAdapter(adapter);

                            } else {
                                jsonObject.getString("Error");
                                loadingText.hideloader(ParentAddFriendList.this);
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Error"), Toast.LENGTH_LONG).show();
                                Log.v("res ", "res  success" + jsonObject.getString("Error"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //   throw new RuntimeException("crash" + e.toString());
                            Crashlytics.logException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingText.hideloader(ParentAddFriendList.this);
                        Log.v("error", "error " + error.toString());
                        Toast.makeText(ParentAddFriendList.this, error.toString(), Toast.LENGTH_LONG).show();
                        //     throw new RuntimeException("crash" + error.toString());
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
}

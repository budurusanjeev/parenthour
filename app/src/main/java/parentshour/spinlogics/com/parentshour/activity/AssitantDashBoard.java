package parentshour.spinlogics.com.parentshour.activity;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.adapter.AssistantDashBoardAdapter;
import parentshour.spinlogics.com.parentshour.models.AssistantDashboardModel;
import parentshour.spinlogics.com.parentshour.utilities.NetworkUtils;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;

/**
 * Created by SPINLOGICS ic_on 12/8/2016.
 */

public class AssitantDashBoard extends BaseActivity {
    Context context;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<AssistantDashboardModel> countries;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Random mRandom = new Random();
    @Override
    public void initialize() {
        context = AssitantDashBoard.this;

        preferenceUtils = new PreferenceUtils(context);

        llContent.addView(inflater.inflate(R.layout.activity_assistant_dashboard, null), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if (NetworkUtils.isNetworkConnectionAvailable(context)) {
            //  showLoaderNew();
            // getData();

            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.assistant_swipeRefreshLayout);
            mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
            mRecyclerView = (RecyclerView) findViewById(R.id.assistant_dashborad_recyclerView);
            mRecyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(layoutManager);
            countries = new ArrayList<AssistantDashboardModel>();
            for (int s = 0; s < 10; s++) {
                if (s % 2 == 0) {
                    AssistantDashboardModel assistantDashboardModel = new AssistantDashboardModel();
                    assistantDashboardModel.setDate("20/12/2016");
                    assistantDashboardModel.setName("Jhon");
                    assistantDashboardModel.setStatus("Accepted");
                    assistantDashboardModel.setTime("06:00 pm to 09:00 pm");
                    assistantDashboardModel.setTitle("Play");
                    countries.add(assistantDashboardModel);
                } else {
                    AssistantDashboardModel assistantDashboardModel = new AssistantDashboardModel();
                    assistantDashboardModel.setDate("15/11/2016");
                    assistantDashboardModel.setName("Tom");
                    assistantDashboardModel.setStatus("Play date completed");
                    assistantDashboardModel.setTime("01:00 pm to 05:00 pm");
                    assistantDashboardModel.setTitle("Chess");
                    countries.add(assistantDashboardModel);
                }
            }

            adapter = new AssistantDashBoardAdapter(countries, context);
            mRecyclerView.setAdapter(adapter);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Refresh the data
                    // Calls setRefreshing(false) when it is finish

                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int num = mRandom.nextInt(10);
                            Log.v("number", "number: " + num);
                            if (num % 2 == 0) {
                                countries.clear();
                                updateOperation(num);
                                mRecyclerView.setAdapter(adapter);
                            } else {
                                countries.clear();
                                updateOperation(num);
                                mRecyclerView.setAdapter(adapter);
                            }

                        }
                    }, 1500);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        } else {
            Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private void updateOperation(int w) {
        // countries = new ArrayList<AssistantDashboardModel>();
        Log.v("number", "number: " + w);
        if (w % 2 == 0) {
            for (int s = 0; s < 10; s++) {
                if (s % 2 != 0) {
                    AssistantDashboardModel assistantDashboardModel = new AssistantDashboardModel();
                    assistantDashboardModel.setDate("20/12/2016");
                    assistantDashboardModel.setName("Jhon");
                    assistantDashboardModel.setStatus("Accepted");
                    assistantDashboardModel.setTime("06:00 pm to 09:00 pm");
                    assistantDashboardModel.setTitle("Play");
                    countries.add(assistantDashboardModel);
                } else {
                    AssistantDashboardModel assistantDashboardModel = new AssistantDashboardModel();
                    assistantDashboardModel.setDate("15/11/2016");
                    assistantDashboardModel.setName("Tom");
                    assistantDashboardModel.setStatus("Play date completed");
                    assistantDashboardModel.setTime("01:00 pm to 05:00 pm");
                    assistantDashboardModel.setTitle("Chess");
                    countries.add(assistantDashboardModel);
                }
            }
        } else {
            for (int s = 0; s < 10; s++) {
                if (s % 2 == 0) {
                    AssistantDashboardModel assistantDashboardModel = new AssistantDashboardModel();
                    assistantDashboardModel.setDate("20/12/2016");
                    assistantDashboardModel.setName("Jhon");
                    assistantDashboardModel.setStatus("Accepted");
                    assistantDashboardModel.setTime("06:00 pm to 09:00 pm");
                    assistantDashboardModel.setTitle("Play");
                    countries.add(assistantDashboardModel);
                } else {
                    AssistantDashboardModel assistantDashboardModel = new AssistantDashboardModel();
                    assistantDashboardModel.setDate("15/11/2016");
                    assistantDashboardModel.setName("Tom");
                    assistantDashboardModel.setStatus("Play date completed");
                    assistantDashboardModel.setTime("01:00 pm to 05:00 pm");
                    assistantDashboardModel.setTitle("Chess");
                    countries.add(assistantDashboardModel);
                }
            }
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

    @Override
    protected void onResume() {
        super.onResume();
        toolbarTextView.setText("Parenthour");
    }
}

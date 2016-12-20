package parentshour.spinlogics.com.parentshour.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

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

    @Override
    public void initialize() {
        context = AssitantDashBoard.this;

        preferenceUtils = new PreferenceUtils(context);

        llContent.addView(inflater.inflate(R.layout.activity_assistant_dashboard, null), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if (NetworkUtils.isNetworkConnectionAvailable(context)) {
            //  showLoaderNew();
            // getData();
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.assistant_dashborad_recyclerView);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            ArrayList<AssistantDashboardModel> countries = new ArrayList<AssistantDashboardModel>();
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

            RecyclerView.Adapter adapter = new AssistantDashBoardAdapter(countries, context);
            recyclerView.setAdapter(adapter);

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

    @Override
    protected void onResume() {
        super.onResume();
        toolbarTextView.setText("Parenthour");
    }
}

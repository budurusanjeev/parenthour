package parentshour.spinlogics.com.parentshour.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.utilities.FontStyle;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;


public class ParentDashboard extends BaseActivity {

    Context context;
    LinearLayout ll_parent_dashboard, ll_child_dashboard;
    TextView tv_search_for_playdate, tv_search_for_assistant, tv_playdate_events;
    private View decorView;
    private boolean doubleBackToExitPressedOnce = false;
    private PreferenceUtils preferenceUtils;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {

            finish();

            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }



    @Override
    public void initialize() {


        context = ParentDashboard.this;

        preferenceUtils = new PreferenceUtils(context);

        llContent.addView(inflater.inflate(R.layout.activity_dashboard, null), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        decorView = getWindow().getDecorView();

        //getTaxInformation()
        initViewControll();

    }

    @Override
    public void goto_playDateSearch_method() {

        Intent intent = new Intent(context, PlaydateSearchActivity.class);
        startActivity(intent);

    }

    @Override
    public void goto_SearchAssistant_method() {

        Intent intent = new Intent(context, SearchAssistanceActivity.class);
        startActivity(intent);

    }

    @Override
    public void goto_AssistantRequests_method() {

        Intent intent = new Intent(context, AssistanceRequestActivity.class);
        startActivity(intent);

    }


    @Override
    public void goto_Friends_method() {

        Intent intent = new Intent(context, ParentFriendsActivity.class);
        startActivity(intent);
    }

    @Override
    public void goto_Notifications_method() {

        Intent intent = new Intent(context, NotificationActivity.class);
        startActivity(intent);

    }


    @Override
    public void goto_PlaydateEvents_method() {

        Intent intent = new Intent(context, PlayDateEventsActivity.class);
        startActivity(intent);

    }

    @Override
    public void goto_Settings_method() {

        Intent intent = new Intent(context, SettiongsActivity.class);
        startActivity(intent);

    }

    @Override
    public void goto_Chats_method() {

       /* Intent intent = new Intent(context, ChatsActivity.class);
        startActivity(intent);*/

    }


    private void initViewControll() {

        ll_parent_dashboard =(LinearLayout) findViewById(R.id.ll_parent_dashboard);
        //  ll_child_dashboard =(LinearLayout) findViewById(R.id.ll_child_dashboard);

        tv_search_for_playdate =(TextView) findViewById(R.id.tv_search_for_playdate);
        tv_search_for_assistant =(TextView) findViewById(R.id.tv_search_for_assistant);
        tv_playdate_events =(TextView) findViewById(R.id.tv_playdate_events);
        FontStyle.applyFont(context, ll_parent_dashboard, FontStyle.Lato_Medium);

    }




}

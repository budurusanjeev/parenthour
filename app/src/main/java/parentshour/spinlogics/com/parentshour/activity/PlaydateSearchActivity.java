package parentshour.spinlogics.com.parentshour.activity;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;

/**
 * Created by SPINLOGICS ic_on 11/7/2016.
 */
public class PlaydateSearchActivity extends BaseActivity{

    Context context;
    private View decorView;
    LinearLayout ll_parent_dashboard, ll_child_dashboard;
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
        context = PlaydateSearchActivity.this;

        preferenceUtils = new PreferenceUtils(context);

       /* ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);*/
        llContent.addView(inflater.inflate(R.layout.activity_playdatesearch, null), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        decorView = getWindow().getDecorView();

        //getTaxInformation()
        initViewControll();
    }

    private void initViewControll() {
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

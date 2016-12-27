package parentshour.spinlogics.com.parentshour.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;
import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.models.PlaySearchDateModel;
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

    @Override
    public void initialize() {
        context = ParentAddGroup.this;

        preferenceUtils = new PreferenceUtils(context);

        llContent.addView(inflater.inflate(R.layout.activity_parent_addgroup, null), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        initViewControll();
        Fabric.with(this, new Crashlytics());
    }

    private void initViewControll() {
        TextView toolbarTextView = (TextView) findViewById(R.id.page_heading);
        toolbarTextView.setText("Add Group");
        // mRecyclerView = (RecyclerView) findViewById(R.id.addedGroupMembers);
        iv_addMemberToGroup = (TextView) findViewById(R.id.tv_addMemberToGroup);
        tv_done = (TextView) findViewById(R.id.tv_done);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        // mRecyclerView.setHasFixedSize(true);
        //   RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        //  mRecyclerView.setLayoutManager(layoutManager);
        // parentAddGroupArrayList = new ArrayList<PlaySearchDateModel>();
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

package parentshour.spinlogics.com.parentshour.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;

import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.adapter.NavDrawerListAdapter;
import parentshour.spinlogics.com.parentshour.domain.NavDrawerItem;
import parentshour.spinlogics.com.parentshour.utilities.FontStyle;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;

public abstract class BaseActivity extends AppCompatActivity {

    public TextView txt_usrname,tv_userLogout,toolbarTextView;

    public LinearLayout llContent, ll_userDetail;

    public LayoutInflater inflater;

    public Context basecontext;

    public  Toolbar mtoolbar;

    public String[] nav_item_titlenames;

    public Bundle savedInstanceState;

    public ListView mdrawerlistview;

    public ImageView iv_filter;

    public DrawerLayout mdrawerlayout;

    public ActionBarDrawerToggle mdrawertoogle;

    public View header_view,footer_view;
    public ArrayList<NavDrawerItem> navDrawerItems;
    public NavDrawerListAdapter adapter;
    public Dialog dialog;
    public ImageView img_header;
    PreferenceUtils preferenceUtils;
    GoogleApiClient mGoogleApiClient;
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base);

        basecontext = BaseActivity.this;

        preferenceUtils = new PreferenceUtils(basecontext);

        this.savedInstanceState=savedInstanceState;

        inflater = this.getLayoutInflater();

        navDrawerItems = new ArrayList<>();
        FacebookSdk.sdkInitialize(getApplicationContext());
        baseInitializeControls();

        generateIconAndStringForDrawer();

        initialize();
        initdrawer();

        mdrawertoogle.syncState();

        mdrawerlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NavDrawerItem drawerobj = (NavDrawerItem) parent.getItemAtPosition(position);

                toolbarTextView.setText(drawerobj.title);
                //  Toast.makeText(getApplicationContext(),preferenceUtils.getStringFromPreference("select","")+" No: "+position,Toast.LENGTH_LONG).show();

switch (position)
{
    /*case 0:

        if(preferenceUtils.getStringFromPreference("select","").equals("parent"))
        {

            mdrawerlayout.closeDrawers();
        }
        else if(preferenceUtils.getStringFromPreference("select","").equals("assistant"))
        {

            mdrawerlayout.closeDrawers();
        }
        break;*/
    case 1:
        if(preferenceUtils.getStringFromPreference("select","").equals("parent"))
        {
            startActivity(new Intent(getApplicationContext(), ParentDashboard.class));
            mdrawerlayout.closeDrawers();
        }
        else if(preferenceUtils.getStringFromPreference("select","").equals("assistant"))
        {
            startActivity(new Intent(getApplicationContext(), AssitantDashBoard.class));
            mdrawerlayout.closeDrawers();
        }
         break;
    case 2:
        if(preferenceUtils.getStringFromPreference("select","").equals("parent"))
        {
            mdrawerlayout.closeDrawers();
        }
        else if(preferenceUtils.getStringFromPreference("select","").equals("assistant"))
        {
            mdrawerlayout.closeDrawers();
        }
        break;
    case 3:
        if(preferenceUtils.getStringFromPreference("select","").equals("parent"))
        {
            startActivity(new Intent(getApplicationContext(), ParentFriendsActivity.class));
            mdrawerlayout.closeDrawers();
        }
        else if(preferenceUtils.getStringFromPreference("select","").equals("assistant"))
        {

            startActivity(new Intent(getApplicationContext(),AssistantSettingActivity.class));
            mdrawerlayout.closeDrawers();
        }
        break;
    case 4:
        if(preferenceUtils.getStringFromPreference("select","").equals("parent"))
        {
            startActivity(new Intent(getApplicationContext(), ParentNotificationsActivity.class));
            mdrawerlayout.closeDrawers();
        }
        else if(preferenceUtils.getStringFromPreference("select","").equals("assistant"))
        {

            mdrawerlayout.closeDrawers();
        }
        break;
    case 5:
        if(preferenceUtils.getStringFromPreference("select","").equals("parent"))
        {
            startActivity(new Intent(getApplicationContext(),ParentSettingAcitivity.class));
            mdrawerlayout.closeDrawers();
        }
        else if(preferenceUtils.getStringFromPreference("select","").equals("assistant"))
        {

            mdrawerlayout.closeDrawers();
        }
        break;
}



            }
        });
        tv_userLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "" + preferenceUtils.getStringFromPreference("social", ""), Toast.LENGTH_LONG).show();
               if(preferenceUtils.getStringFromPreference("social","").equals("fb"))
               {

                   if (AccessToken.getCurrentAccessToken() == null) {
                       // Toast.makeText()
                       preferenceUtils.saveString("loggedin","ic_logout");
                       preferenceUtils.logoutUser();
                       startActivity(new Intent(getApplicationContext(),PreLoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                       finish();
                       return; // already logged out

                   }

                   new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                           .Callback() {
                       @Override
                       public void onCompleted(GraphResponse graphResponse) {

                           LoginManager.getInstance().logOut();
                           preferenceUtils.saveString("loggedin","ic_logout");
                           preferenceUtils.logoutUser();
                           startActivity(new Intent(getApplicationContext(),PreLoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                           finish();
                       }
                   }).executeAsync();
               }else if(preferenceUtils.getStringFromPreference("social","").equals("gp"))
                {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    preferenceUtils.saveString("loggedin", "ic_logout");
                                    preferenceUtils.logoutUser();
                                    startActivity(new Intent(getApplicationContext(),PreLoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                    finish();
                                }
                            });

                } else
               //if(preferenceUtils.getStringFromPreference("social","").equals("general"))
               {
                   preferenceUtils.saveString("loggedin","ic_logout");
                   preferenceUtils.logoutUser();
                   startActivity(new Intent(getApplicationContext(),PreLoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                   finish();

               }

            }
        });
    }

    public abstract void initialize();

     public abstract void goto_playDateSearch_method();

     public abstract void goto_SearchAssistant_method();

    public abstract void goto_AssistantRequests_method();

    public abstract void goto_Friends_method();

    public abstract void goto_Notifications_method();

    public abstract void goto_PlaydateEvents_method();

    public abstract void goto_Settings_method();

    public abstract void goto_Chats_method();

    public void showAlertValidation(String error) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(basecontext);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.notification_dailog, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        final TextView tv_errorTitle = (TextView) dialogView.findViewById(R.id.tvTitle);
        final TextView tv_ok = (TextView) dialogView.findViewById(R.id.btnYes);
        LinearLayout alert_layout = (LinearLayout) findViewById(R.id.alert_layout);
        FontStyle.applyFont(getApplicationContext(), alert_layout, FontStyle.Lato_Medium);
        tv_errorTitle.setText(error);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });
        b.show();

    }
    private void initdrawer() {

        mtoolbar.setTitle("Parent Hour");
        mdrawertoogle=new ActionBarDrawerToggle(this,mdrawerlayout,mtoolbar,R.string.opendrawer,R.string.closedrawer)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }


        };

        mdrawerlayout.setDrawerListener(mdrawertoogle);
    }

    private void baseInitializeControls() {


        llContent= (LinearLayout) findViewById(R.id.llContent);

        header_view=getLayoutInflater().inflate(R.layout.header_navi, null);
        FontStyle.applyFont(getApplicationContext(),header_view,FontStyle.Lato_Medium);

        footer_view=getLayoutInflater().inflate(R.layout.footer_navi,null);
        FontStyle.applyFont(getApplicationContext(),footer_view,FontStyle.Lato_Medium);

        mdrawerlistview= (ListView) findViewById(R.id.left_drawer);

          mtoolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbarTextView = (TextView)findViewById(R.id.page_heading);

        FontStyle.applyFont(getApplicationContext(),toolbarTextView,FontStyle.Lato_Medium);

        mdrawerlayout= (DrawerLayout) findViewById(R.id.drawerLayout);

        iv_filter = (ImageView) findViewById(R.id.iv_filter);

        adapter = new NavDrawerListAdapter(basecontext, navDrawerItems);

        ll_userDetail = (LinearLayout)header_view.findViewById(R.id.ll_userDetail);

        txt_usrname=(TextView)header_view.findViewById(R.id.tv_userNameNav);
        img_header = (ImageView)header_view.findViewById(R.id.img_header);
        tv_userLogout=(TextView)footer_view.findViewById(R.id.tv_userLogout);
        FontStyle.applyFont(getApplicationContext(),tv_userLogout,FontStyle.Lato_Medium);

        mdrawerlistview.addHeaderView(header_view, null, false);
        mdrawerlistview.addFooterView(footer_view,null,false);
        mdrawerlistview.setAdapter(adapter);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        connectionResult.isSuccess();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        if(mtoolbar!=null)
        {
            setSupportActionBar(mtoolbar);
            toolbarTextView.setText("Parentshour");
            FontStyle.applyFont(getApplicationContext(),toolbarTextView,FontStyle.Lato_Medium);
        }
    }

    public void generateIconAndStringForDrawer() {

        if(preferenceUtils.getStringFromPreference("select","").equals("parent"))
        {
            if(!preferenceUtils.getStringFromPreference("p_name", "").equals(""))
            {
                txt_usrname.setText(preferenceUtils.getStringFromPreference("p_name", ""));
            }

            if(!preferenceUtils.getStringFromPreference("p_pic", "").equals(""))
            {
                Glide.with(basecontext)
                        .load(preferenceUtils.getStringFromPreference("p_pic", ""))
                        .thumbnail(0.5f)
                        .crossFade()
                        .error(R.drawable.ic_addprofile)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(img_header);
            }
        }
        else if(preferenceUtils.getStringFromPreference("select","").equals("assistant"))
        {
            if(!preferenceUtils.getStringFromPreference("a_name", "").equals(""))
            {
                txt_usrname.setText(preferenceUtils.getStringFromPreference("a_name", ""));
            }

            if(!preferenceUtils.getStringFromPreference("a_pic", "").equals(""))
            {
                Glide.with(basecontext).load(preferenceUtils.getStringFromPreference("a_pic", ""))
                        .thumbnail(0.5f)
                        .crossFade()
                        .error(R.drawable.ic_addprofile)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(img_header);
            }
        }


img_header.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        if(preferenceUtils.getStringFromPreference("select","").equals("parent"))
        {
            startActivity(new Intent(getApplicationContext(), ParentGetProfileActivity.class));
            mdrawerlayout.closeDrawers();
        }
        else if(preferenceUtils.getStringFromPreference("select","").equals("assistant"))
        {
            startActivity(new Intent(getApplicationContext(), AssistantGetActivity.class));
            mdrawerlayout.closeDrawers();
        }

    }
});
          if(preferenceUtils.getStringFromPreference("select","").equals("parent"))
        {
            nav_item_titlenames = getResources().getStringArray(R.array.navdraweritemsbeforelogin);
            txt_usrname.setText(preferenceUtils.getStringFromPreference("p_name",""));
            mdrawerlayout.closeDrawers();
        }
        else if(preferenceUtils.getStringFromPreference("select","").equals("assistant"))
        {
            nav_item_titlenames = getResources().getStringArray(R.array.navDraweritemsAssistant);
            txt_usrname.setText(preferenceUtils.getStringFromPreference("a_name",""));
            mdrawerlayout.closeDrawers();
        }
        if(preferenceUtils.getStringFromPreference("select","").equals("parent")) {
            TypedArray navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

            navDrawerItems.add(new NavDrawerItem(nav_item_titlenames[0], navMenuIcons.getResourceId(0, -1)));

            navDrawerItems.add(new NavDrawerItem(nav_item_titlenames[1], navMenuIcons.getResourceId(1, -1)));

            navDrawerItems.add(new NavDrawerItem(nav_item_titlenames[2], navMenuIcons.getResourceId(2, -1)));

            navDrawerItems.add(new NavDrawerItem(nav_item_titlenames[3], navMenuIcons.getResourceId(3, -1)));

            navDrawerItems.add(new NavDrawerItem(nav_item_titlenames[4], navMenuIcons.getResourceId(4, -1)));

            navDrawerItems.add(new NavDrawerItem(nav_item_titlenames[5], navMenuIcons.getResourceId(5, -1)));
        }
        else if(preferenceUtils.getStringFromPreference("select","").equals("assistant"))
        {
            TypedArray navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_assistant_icons);

            navDrawerItems.add(new NavDrawerItem(nav_item_titlenames[0], navMenuIcons.getResourceId(0, -1)));

            navDrawerItems.add(new NavDrawerItem(nav_item_titlenames[1], navMenuIcons.getResourceId(1, -1)));

            navDrawerItems.add(new NavDrawerItem(nav_item_titlenames[2], navMenuIcons.getResourceId(2, -1)));

            navDrawerItems.add(new NavDrawerItem(nav_item_titlenames[3], navMenuIcons.getResourceId(3, -1)));

        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

        mdrawertoogle.syncState();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mdrawertoogle.syncState();
    }

    public void showLoaderNew() {
        runOnUiThread(new Runloader(getResources().getString(R.string.loading)));
    }

    public void hideloader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class Runloader implements Runnable {
        private String strrMsg;

        public Runloader(String strMsg) {
            this.strrMsg = strMsg;
        }

        @SuppressWarnings("ResourceType")
        @Override
        public void run() {
            try {
                if (dialog == null)
                {
                    dialog = new Dialog(basecontext,R.style.Theme_Dialog_Translucent);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                }
                dialog.setContentView(R.layout.loading);
                dialog.setCancelable(false);

                if (dialog != null && dialog.isShowing())
                {
                    dialog.dismiss();
                    dialog=null;
                }
                dialog.show();

                final ImageView imgeView = (ImageView) dialog
                        .findViewById(R.id.imgeView);
                TextView tvLoading = (TextView) dialog
                        .findViewById(R.id.tvLoading);
                if (!strrMsg.equalsIgnoreCase(""))

                    animationDrawable = (AnimationDrawable) imgeView
                        .getBackground();
                imgeView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (animationDrawable != null)
                            imgeView.setVisibility(View.VISIBLE);
                            animationDrawable.start();
                    }
                });
                tvLoading.setText(strrMsg);
            } catch (Exception e)
            {

            }
        }
    }


}

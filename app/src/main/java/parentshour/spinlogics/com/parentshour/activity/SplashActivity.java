package parentshour.spinlogics.com.parentshour.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.util.Timer;
import java.util.TimerTask;

import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.utilities.AppConstants;
import parentshour.spinlogics.com.parentshour.utilities.CartDatabase;
import parentshour.spinlogics.com.parentshour.utilities.NetworkUtils;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;


public class SplashActivity extends AppCompatActivity {

    Timer timer;

    private LayoutInflater minflater;

    Context mcontext;

    AlertDialog alertDialog;

    PreferenceUtils preferenceUtils;

    Boolean loginstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
        mcontext = SplashActivity.this;
        minflater = this.getLayoutInflater();
        CartDatabase.init(mcontext);
        timer = new Timer();
        preferenceUtils = new PreferenceUtils(mcontext);

        loginstatus= preferenceUtils.isLoggedIn();

    }
    protected void onPause() {
        super.onPause();

        if (alertDialog != null) {
            alertDialog.dismiss();
        }

        System.gc();
    }


    protected void onResume() {
        super.onResume();
            if (NetworkUtils.isNetworkConnectionAvailable(mcontext)) {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                       // preferenceUtils.saveString("loggedin",jsonObject.getString("loggedin"));
                        Log.v("splash ","splash "+ preferenceUtils.getStringFromPreference("loggedin",""));

                        if(preferenceUtils.getStringFromPreference("loggedin","").equals("loggedin")) {

                            if(preferenceUtils.getStringFromPreference("select","").equals("parent"))
                            {
                                Intent dashbordactivity = new Intent(mcontext, ParentDashboard.class);
                                startActivity(dashbordactivity);
                                finish();
                            }
                            else if(preferenceUtils.getStringFromPreference("select","").equals("assistant"))
                            {
                                Intent dashbordactivity = new Intent(mcontext, AssitantDashBoard.class);
                                startActivity(dashbordactivity);
                                finish();
                            }

                        }
                        else
                        {
                            Intent loginActivity = new Intent(mcontext, PreLoginActivity.class);
                            startActivity(loginActivity);
                            finish();
                        }
                    }
                }, 1000);
            } else {
                // getCustomToast(AppConstant.CHECK_NETWORK_CONN);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showAlertDialog(mcontext, "INTERNET CONNECTION", AppConstants.CHECK_NETWORK_CONN, false);
                    }
                });

            }
        }

    private void showAlertDialog(Context context, String title, String message,
                                 Boolean status) {
        alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // disable touch outside dialog.
        alertDialog.setCanceledOnTouchOutside(false);

        if (status != null)
            // Setting alert dialog icon
            alertDialog.setIcon((status) ? R.mipmap.ic_launcher : R.mipmap.ic_launcher);

        // Setting OK Button
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Setting",new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {

                open_device_setting_screen();

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void open_device_setting_screen() {

        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
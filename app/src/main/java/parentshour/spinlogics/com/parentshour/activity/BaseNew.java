package parentshour.spinlogics.com.parentshour.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;


public class BaseNew extends AppCompatActivity {
    public Dialog dialog;
    private AnimationDrawable animationDrawable;
    Context mContext;
    public AlertDialog alertDialog;
    public AlertDialog.Builder alertBuilder;
    public Bundle savedInstanceState;
    PreferenceUtils preferenceUtils;
    public android.support.v7.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState=savedInstanceState;
        setContentView(R.layout.base_new);

        mContext=BaseNew.this;
    }

    public void showAlertDialog(String strMessage, String firstBtnName)
    {
        runOnUiThread(new RunshowCustomDialogs(strMessage, firstBtnName));
    }


    class RunshowCustomDialogs implements Runnable
    {
        private String strMessage;// Message to be shown in dialog
        private String firstBtnName;
        private int titleGravity;
        private boolean isShowNestedDialog;
        private String dialogFrom;

        public RunshowCustomDialogs(String strMessage, String firstBtnName)
        {
            this.strMessage 	= strMessage;
            this.firstBtnName 	= firstBtnName;
        }

        @Override
        public void run()
        {
            closeAlertDialog();
            alertBuilder = new AlertDialog.Builder(mContext);
            alertBuilder.setCancelable(true);

            final LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.notification_dailog, null);

            TextView dialogtvTitle = (TextView)linearLayout.findViewById(R.id.tvTitle);
            TextView btnYes = (TextView)linearLayout.findViewById(R.id.btnYes);


            if(titleGravity!=0)
            {
                // Only in the case of Crash Report Dialog, i am customizing it with custom padding.
                dialogtvTitle.setGravity(titleGravity);
                dialogtvTitle.setPadding(35, 35, 0, 35);

            }
            dialogtvTitle.setText(strMessage);
            btnYes.setText(firstBtnName);
            btnYes.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    alertDialog.cancel();

                }
            });

            try
            {
                alertDialog = alertBuilder.create();
                alertDialog.setView(linearLayout,0,0,0,0);
                alertDialog.setInverseBackgroundForced(true);
                alertDialog.show();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    public void closeAlertDialog()
    {
        if (alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();
    }




    public void hideKeyBoard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    public void showLoaderNew() {
        runOnUiThread(new Runloader(getResources().getString(R.string.loading)));
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
                    dialog = new Dialog(mContext,R.style.Theme_Dialog_Translucent);
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

                ImageView imgeView = (ImageView) dialog
                        .findViewById(R.id.imgeView);
                TextView tvLoading = (TextView) dialog
                        .findViewById(R.id.tvLoading);
                if (!strrMsg.equalsIgnoreCase(""))
                    tvLoading.setText(strrMsg);

              // imgeView.setBackgroundResource(R.anim.frame);

                animationDrawable = (AnimationDrawable) imgeView
                        .getBackground();
                imgeView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (animationDrawable != null)
                            animationDrawable.start();
                    }
                });
            } catch (Exception e)
            {

            }
        }
    }

        public void hideloader() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        if (dialog != null && dialog.isShowing())
                            dialog.dismiss();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }); }


    }




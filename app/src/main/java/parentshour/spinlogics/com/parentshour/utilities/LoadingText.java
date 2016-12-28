package parentshour.spinlogics.com.parentshour.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import parentshour.spinlogics.com.parentshour.R;

/**
 * Created by SPINLOGICS on 12/19/2016.
 */

public class LoadingText {
    public Dialog dialog;
    Context activity;
    private AnimationDrawable animationDrawable;

    public LoadingText(Context activityName) {
        activity = activityName;
    }

    public void showLoaderNew(Activity context) {
        context.runOnUiThread(new Runloader(context.getResources().getString(R.string.loading)));
    }

    public void hideloader(Activity activity) {
        activity.runOnUiThread(new Runnable() {
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
                if (dialog == null) {
                    dialog = new Dialog(activity, R.style.Theme_Dialog_Translucent);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                }
                dialog.setContentView(R.layout.loading);
                dialog.setCancelable(false);

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
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
            } catch (Exception e) {

            }
        }
    }

}

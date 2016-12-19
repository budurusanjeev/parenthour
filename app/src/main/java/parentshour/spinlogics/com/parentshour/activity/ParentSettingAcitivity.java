package parentshour.spinlogics.com.parentshour.activity;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.utilities.AppConstants;
import parentshour.spinlogics.com.parentshour.utilities.FontStyle;
import parentshour.spinlogics.com.parentshour.utilities.NetworkUtils;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;

import static parentshour.spinlogics.com.parentshour.R.id.edt_conformpassword;
import static parentshour.spinlogics.com.parentshour.R.id.tb_gender;


/**
 * Created by SPINLOGICS ic_on 12/8/2016.
 */

public class ParentSettingAcitivity extends BaseActivity implements ToggleButton.OnCheckedChangeListener{
   Context context;
 EditText edt_oldPwd,edt_newpwd,edt_conpwd;
 TextView tv_changepassword;
 String oldPwd,newPwd;
    ToggleButton tg_age,tg_occupation,tg_education,tg_gender,tg_available_days,tg_available_time;
    String tg_ageValue,tg_occupationValue,tg_educationValue,tg_genderValue,tg_available_daysValue,tg_available_timeValue;
    @Override
    public void initialize() {
        context = ParentSettingAcitivity.this;

        preferenceUtils = new PreferenceUtils(context);

        llContent.addView(inflater.inflate(R.layout.activity_parent_settings, null), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if (NetworkUtils.isNetworkConnectionAvailable(context)) {
            //  showLoaderNew();
            getData();
        } else {
            Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
        }
        initViewControll();
     tv_changepassword.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
       final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
       LayoutInflater inflater = getLayoutInflater();
       final View dialogView = inflater.inflate(R.layout.dialog_parent_changepassword, null);
       dialogBuilder.setView(dialogView);
       final AlertDialog b = dialogBuilder.create();
       final EditText edt_oldpassword = (EditText) dialogView.findViewById(R.id.edt_oldpassword);
       final EditText edt_newpassword = (EditText) dialogView.findViewById(R.id.edt_newpassword);
       final EditText edt_conformpassword = (EditText) dialogView.findViewById(R.id.edt_conformpassword);
       final TextView tv_done = (TextView) dialogView.findViewById(R.id.tv_done);
       final TextView tv_cancel = (TextView) dialogView.findViewById(R.id.tv_cancel);
          LinearLayout par_changepwd_layout = (LinearLayout)findViewById(R.id.par_changepwd_layout);
          FontStyle.applyFont(getApplicationContext(),par_changepwd_layout, FontStyle.Lato_Medium);

          tv_cancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
         b.dismiss();
        }
       });
       tv_done.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
         if(edt_oldpassword.getText().length()>0&&
                 edt_newpassword.getText().length()>0 &&
                 edt_conformpassword.getText().length()>0)
         {
          oldPwd = edt_oldpassword.getText().toString();
          newPwd = edt_conformpassword.getText().toString();
          if(edt_conformpassword.getText().toString().equals(edt_newpassword.getText().toString()))
          {
              if (NetworkUtils.isNetworkConnectionAvailable(context)) {

                  updateNewPassword();
              } else {
                  Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
              }


              b.dismiss();
          }else
          {
           showAlertValidation("New ic_password and Conform ic_password not equal");
          }

         }else {
          if (edt_oldpassword.getText().length() == 0 &&
                  edt_newpassword.getText().length() == 0 &&
                  edt_conformpassword.getText().length() == 0) {
           showAlertValidation("Please enter all fields ");
          } else if (edt_oldpassword.getText().length() == 0) {
           showAlertValidation("Please enter old ic_password ");
          } else if (edt_newpassword.getText().length() == 0) {
           showAlertValidation("Please enter new ic_password");
          } else if (edt_conformpassword.getText().length() == 0) {
           showAlertValidation("Please enter conform ic_password");
          }
         }
        }
       });
       b.show();
      }
     });
    }

    private void getData()
    {
        showLoaderNew();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.PARENT_GET_SETTINGS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        try {
                            hideloader();
                            JSONObject jsonObject = new JSONObject(response);
                          String r =  jsonObject.getString("p_settings");
                        String d[] = r.split(",");
                            for(int s= 0; s <d.length;s++)
                            {
                                switch (s)
                            {
                                case 0:
                                    if(d[0].equals("1"))
                                       {
                                           tg_ageValue ="1";
                                           tg_age.setChecked(true);
                                       }
                                else
                                {
                                    tg_ageValue ="0";
                                    tg_age.setChecked(false);
                                }
                                       break;
                                case 1:
                                    if(d[1].equals("1"))
                                    {
                                        tg_occupationValue = "1";
                                        tg_occupation.setChecked(true);
                                    }else
                                    {
                                        tg_occupationValue = "0";
                                        tg_occupation.setChecked(false);
                                    }
                                        break;
                                case 2:
                                    if(d[2].equals("1"))
                                    {
                                        tg_educationValue="1";
                                        tg_education.setChecked(true);
                                    }else
                                    {
                                        tg_educationValue="0";
                                        tg_education.setChecked(false);
                                    }
                                    break;
                                case 3:
                                    if(d[3].equals("1"))
                                    {
                                        tg_genderValue="1";
                                        tg_gender.setChecked(true);
                                    }else
                                    {
                                        tg_genderValue="0";
                                        tg_gender.setChecked(false);
                                    }
                                    break;
                                case 4:
                                    if(d[4].equals("1"))
                                    {
                                        tg_available_daysValue= "1";
                                        tg_available_days.setChecked(true);
                                    }else
                                    {
                                        tg_available_daysValue= "0";
                                        tg_available_days.setChecked(false);
                                    }
                                    break;
                                case 5:
                                    if(d[5].equals("1"))
                                    {
                                        tg_available_timeValue="1";
                                        tg_available_time.setChecked(true);
                                    }else
                                    {
                                        tg_available_timeValue="0";
                                        tg_available_time.setChecked(false);
                                    }
                                    break;

                            }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("error", "error " + error.toString());
                        hideloader();
                        Toast.makeText(ParentSettingAcitivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                String credentials;

                credentials = "p_id=" + preferenceUtils.getStringFromPreference("p_id", "");
                Log.v("res ","res else "+credentials);
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


    private void updateNewPassword()
 {

  showLoaderNew();
  StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.PARENT_CHANGE_PASSWORD_URL,
          new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
            Log.v("res ", "res  " + response);
            try {
             hideloader();
             JSONObject jsonObject = new JSONObject(response);

             if (jsonObject.has("Success")) {
              Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Success"), Toast.LENGTH_LONG).show();
              Log.v("res ", "res  success" + jsonObject.getString("Success"));
             } else {
              jsonObject.getString("error");
              Toast.makeText(getApplicationContext(), "" + jsonObject.getString("error"), Toast.LENGTH_LONG).show();
              Log.v("res ", "res  success" + jsonObject.getString("Error"));
             }

            } catch (Exception e) {
             e.printStackTrace();
            }
           }
          },
          new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
            Log.v("error", "error " + error.toString());
            hideloader();
            Toast.makeText(ParentSettingAcitivity.this, error.toString(), Toast.LENGTH_LONG).show();
           }
          }) {
   @Override
   public byte[] getBody() throws AuthFailureError {

    String credentials;

    credentials = "p_id=" + preferenceUtils.getStringFromPreference("p_id", "")
            + "&p_oldpassword="+ oldPwd+
            "&p_newpassword="+newPwd;
    Log.v("res ","res else "+credentials);
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
 public void showAlertValidation(String error) {
  final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
  LayoutInflater inflater = getLayoutInflater();
  final View dialogView = inflater.inflate(R.layout.notification_dailog, null);
  dialogBuilder.setView(dialogView);
  final AlertDialog b = dialogBuilder.create();
  final TextView tv_errorTitle = (TextView) dialogView.findViewById(R.id.tvTitle);
  final TextView tv_ok = (TextView) dialogView.findViewById(R.id.btnYes);
  tv_errorTitle.setText(error);
  tv_ok.setOnClickListener(new View.OnClickListener() {
   @Override
   public void onClick(View view) {
    b.dismiss();
   }
  });
  b.show();

 }
    private void initViewControll()
    {
        TextView toolbarTextView = (TextView)findViewById(R.id.page_heading);
        toolbarTextView.setText("Settings");
        TextView  tv_save = (TextView)findViewById(R.id.setting_Save);
        tv_save.setVisibility(View.VISIBLE);
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
sendSettings();
            }
        });

        tg_age = (ToggleButton)findViewById(R.id.tb_age);
        tg_occupation = (ToggleButton)findViewById(R.id.tb_occupation);
        tg_education = (ToggleButton)findViewById(R.id.tb_education);
        tg_gender = (ToggleButton)findViewById(tb_gender);
        tg_available_days = (ToggleButton)findViewById(R.id.tb_available_days);
        tg_available_time = (ToggleButton)findViewById(R.id.tb_available_time);
        tg_age.setOnCheckedChangeListener(this);
        tg_occupation.setOnCheckedChangeListener(this);
        tg_education.setOnCheckedChangeListener(this);
        tg_gender.setOnCheckedChangeListener(this);
        tg_available_days.setOnCheckedChangeListener(this);
        tg_available_time.setOnCheckedChangeListener(this);

     edt_oldPwd = (EditText) findViewById(R.id.edt_oldpassword);
     edt_newpwd = (EditText) findViewById(R.id.edt_newpassword);
     edt_conpwd = (EditText) findViewById(edt_conformpassword);
     tv_changepassword = (TextView) findViewById(R.id.tv_changepassword);
        LinearLayout par_settings_layout = (LinearLayout)findViewById(R.id.par_settings_layout);
        FontStyle.applyFont(getApplicationContext(),par_settings_layout, FontStyle.Lato_Medium);

    }

    private void sendSettings()
    {
        showLoaderNew();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.PARENT_SETTING_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        try {
                            hideloader();
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("Success")) {
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Success"), Toast.LENGTH_LONG).show();
                                Log.v("res ", "res  success" + jsonObject.getString("Success"));
                            } else {
                                jsonObject.getString("Error");
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Error"), Toast.LENGTH_LONG).show();
                                Log.v("res ", "res  success" + jsonObject.getString("Error"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("error", "error " + error.toString());
                        hideloader();
                        Toast.makeText(ParentSettingAcitivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                String credentials;

                credentials = "p_id=" + preferenceUtils.getStringFromPreference("p_id", "")
                        + "&p_settings="+tg_ageValue+","+tg_occupationValue+","+tg_educationValue+","+tg_genderValue+","+tg_available_daysValue+","+tg_available_timeValue;
                Log.v("res ","res else "+credentials);
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
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId())
        {
            case R.id.tb_age:
                             if(compoundButton.isChecked())
                             {
                                 tg_ageValue = "1";
                             }else
                             {
                                 tg_ageValue = "0";
                             }
                             break;
            case R.id.tb_occupation:
                if(compoundButton.isChecked())
                {
                    tg_occupationValue = "1";
                }else
                {
                    tg_occupationValue = "0";

                }
                             break;
            case R.id.tb_education:
                if(compoundButton.isChecked())
                {
                    tg_educationValue="1";
                }else
                {
                    tg_educationValue="0";
                }
                             break;
            case tb_gender:
                if(compoundButton.isChecked())
                {
                    tg_genderValue="1";

                }else
                {
                    tg_genderValue="0";
                }
                             break;
            case R.id.tb_available_days:
                if(compoundButton.isChecked())
                {
                    tg_available_daysValue="1";

                }else
                {
                    tg_available_daysValue="0";
                }
                             break;
            case R.id.tb_available_time:
                if(compoundButton.isChecked())
                {
                    tg_available_timeValue="1";
                }else
                {
                    tg_available_timeValue="0";
                }
                             break;
        }
    }
}

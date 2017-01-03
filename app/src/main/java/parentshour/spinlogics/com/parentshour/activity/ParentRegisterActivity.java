package parentshour.spinlogics.com.parentshour.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.utilities.AppConstants;
import parentshour.spinlogics.com.parentshour.utilities.NetworkUtils;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;

import static parentshour.spinlogics.com.parentshour.R.layout.registration_layout;


public class ParentRegisterActivity extends BaseNew {

    TextView toolbarTextView;
   /* Typeface custom_font;
    FontType fontType;*/
    LinearLayout ll_age, ll_occ, ll_gender, ll_edu, ll_origin, ll_avail_day, ll_avail_time;
    ImageView iv_age, iv_occupation, iv_education;
    View view_age, view_occ, view_edu;
    EditText edt_age, edt_occupation, edt_education;
    TextView tv_gender, tv_origin, tv_avail_Day, tv_avail_time;
    RadioGroup rg_gender;
    RadioButton rb_male, rb_female;
    CheckBox cb_weekdays, cb_weekends, cb_morning, cb_afternoon, cb_evening;
    Button btn_signup;
    PreferenceUtils preferenceUtils;
    String cb_weekdaysValue,rg_gender_value ="",value,valueTimimgs, cb_weekendsValue, cb_morningValue, cb_afternoonValue, cb_eveningValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(registration_layout);
        mContext=ParentRegisterActivity.this;

        initializeControls();
        Fabric.with(this, new Crashlytics());

        preferenceUtils=new PreferenceUtils(mContext);
        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.rb_male)
                {
                    rg_gender_value = "male";
                }else
                {

                    rg_gender_value = "female";
                }
            }
        });
        cb_weekdays.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()) {
                    cb_weekdaysValue ="Weekdays";

                }else
                {
                    cb_weekdaysValue =null;
                }
                Log.v("","res checked "+cb_weekdaysValue);
            }
        });

        cb_weekends.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()) {
                   cb_weekendsValue = "Weekends";

                }else
                {
                    cb_weekendsValue = null;
                }
                Log.v("","res checked "+cb_weekendsValue);
            }
        });

        cb_morning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()) {
                    cb_morningValue = "Morning";
                }
                else
                {
                    cb_morningValue = null;
                }
            }
        });

        cb_afternoon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()) {
                    cb_afternoonValue = "Afternoon";

                }else
                {
                    cb_afternoonValue = null;
                }
            }
        });
        cb_evening.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(compoundButton.isChecked()) {
                    cb_eveningValue = "Evening";

                }else
                {
                    cb_eveningValue = null;
                }
            }
        });

        btn_signup.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (NetworkUtils.isNetworkConnectionAvailable(mContext)) {

                    if(validateSignup() == null ||
                            validateTimings() == null)
                    {
                        showAlertValidation("Please select the weekdays and week timings");
                    }
                    else
                    {
                        if (NetworkUtils.isNetworkConnectionAvailable(mContext)) {
                            showLoaderNew();
                            sendData();
                        } else {
                            Toast.makeText(mContext, "Please check internet connection", Toast.LENGTH_LONG).show();
                        }

                    }
                } else {
                    Toast.makeText(mContext, "Please check internet connection", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private String validateSignup()
    {

             if(cb_weekdaysValue != null&&
                     cb_weekendsValue != null)
             {
                 value = "Weekdays,Weekends";
             }else if(cb_weekdaysValue != null)
             {
                 value = "Weekdays";
                 if(cb_weekendsValue != null)
                 {
                     value = "Weekends";
                 }
             }else if(cb_weekendsValue != null)
             {
                 value = "Weekends";
             }

        return value;
    }
 private String validateTimings()
 {
   //  String value =null;

     if(cb_morningValue != null&&
             cb_afternoonValue != null&&
             cb_eveningValue != null)
     {
         valueTimimgs = "Morning,Afternoon,Evening";
     }
     else if(cb_morningValue != null)
     {
         valueTimimgs = "Morning";
         if(cb_afternoonValue != null)
         {
             valueTimimgs =  valueTimimgs + ",Afternoon";
         }else if(cb_eveningValue != null)
         {
             valueTimimgs = valueTimimgs + ",Evening";
         }
     }else if(cb_afternoonValue != null)
     {
         valueTimimgs = "Afternoon";
         if(cb_eveningValue != null)
         {
             valueTimimgs = valueTimimgs + ",Evening";
         }
     }else if(cb_eveningValue != null)
     {
         valueTimimgs = "Evening";
     }
     return valueTimimgs;
 }
    private void sendData()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.SIGNUP_FINAL_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("Success")) {
                                jsonObject.getString("Success");
                                hideloader();
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Success"), Toast.LENGTH_LONG).show();
                                Intent dashbordactivity = new Intent(mContext, ParentDashboard.class);
                                startActivity(dashbordactivity);
                                Log.v("res ", "res  success" + jsonObject.getString("Success"));
                            } else {
                                jsonObject.getString("Error");
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Error"), Toast.LENGTH_LONG).show();
                                Log.v("res ", "res  success" + jsonObject.getString("Error"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //throw new RuntimeException("crash" + e.toString());
                            Crashlytics.logException(e);
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("error", "error " + error.toString());

                        Toast.makeText(ParentRegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Crashlytics.logException(error);
                        // throw new RuntimeException("crash" + error.toString());
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                String age = edt_age.getText().toString();
                String occupaction = edt_occupation.getText().toString();
                String education = edt_education.getText().toString();
                String credentials = "p_id="+ preferenceUtils.getStringFromPreference("p_id","")+"&p_age="+age+
                        "&p_occupation="+occupaction+
                        "&p_gender="+rg_gender_value+
                        "&p_education="+education+
                        "&p_ethnicity="+
                        "&p_avail_days="+validateSignup()+
                        "&p_avail_time="+validateTimings();
Log.v("res cre","res cre "+credentials);
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

   /* private void setFontToAll() {
      //  edt_userid.setTypeface(custom_font);
    }*/

  /*  public void clearEditText() {
      // edt_userpwd.setText("");
    }*/

    public void showAlertValidation(String error) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
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

    private void getTextSignup()
    {
      /*  UserName=edt_userName.getText().toString();
        EmailId=edt_email.getText().toString();
        MobileNum=edt_mobile.getText().toString();
        UserPass=edt_userpwd.getText().toString();
        UserCnfmPass=edt_cnfrmpwd.getText().toString();*/
    }



    private void initializeControls() {

         btn_signup = (Button) findViewById(R.id.btn_signup);

        cb_weekdays = (CheckBox) findViewById(R.id.cb_weekdays);
        cb_weekends = (CheckBox) findViewById(R.id.cb_weekends);
        cb_morning= (CheckBox) findViewById(R.id.cb_morning);
        cb_afternoon = (CheckBox) findViewById(R.id.cb_afternoon);
        cb_evening = (CheckBox) findViewById(R.id.cb_evening);

        rg_gender = (RadioGroup) findViewById(R.id.rg_gender);
        rb_male = (RadioButton) findViewById(R.id.rb_male);
        rb_female = (RadioButton) findViewById(R.id.rb_female);

        tv_gender = (TextView) findViewById(R.id.tv_gender);
        tv_origin = (TextView) findViewById(R.id.tv_origin);
        tv_avail_Day = (TextView) findViewById(R.id.tv_avail_Day);
        tv_avail_time = (TextView) findViewById(R.id.tv_avail_time);

        ll_age = (LinearLayout) findViewById(R.id.ll_age);
        ll_occ = (LinearLayout) findViewById(R.id.ll_occ);
        ll_edu = (LinearLayout) findViewById(R.id.ll_edu);
        ll_gender = (LinearLayout) findViewById(R.id.ll_gender);
        ll_origin = (LinearLayout) findViewById(R.id.ll_origin);
        ll_avail_day = (LinearLayout) findViewById(R.id.ll_avail_day);
        ll_avail_time = (LinearLayout) findViewById(R.id.ll_avail_time);

        iv_age = (ImageView) findViewById(R.id.iv_age);
        iv_occupation = (ImageView) findViewById(R.id.iv_occupation);
        iv_education = (ImageView) findViewById(R.id.iv_education);

        view_age = findViewById(R.id.view_age);
        view_occ = findViewById(R.id.view_occ);
        view_edu = findViewById(R.id.view_edu);

        View test1View = findViewById(R.id.toolbarLayout);
        toolbarTextView = (TextView) test1View.findViewById(R.id.page_heading);
        toolbarTextView.setText("Registration");

        edt_age = (EditText) findViewById(R.id.edt_age);
        edt_occupation = (EditText) findViewById(R.id.edt_occupation);
        edt_education = (EditText) findViewById(R.id.edt_education);


    }

}

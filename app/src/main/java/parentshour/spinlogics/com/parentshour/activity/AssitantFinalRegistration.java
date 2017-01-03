package parentshour.spinlogics.com.parentshour.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.utilities.AppConstants;
import parentshour.spinlogics.com.parentshour.utilities.FontStyle;
import parentshour.spinlogics.com.parentshour.utilities.NetworkUtils;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;


/**
 * Created by SPINLOGICS ic_on 12/6/2016.
 */

public class AssitantFinalRegistration extends AppCompatActivity {
    TextView toolbarTextView,tv_signup;
    Context mContext;
    EditText edt_experience, edt_hourly, edt_city, edt_state, edt_about_me,edt_others;
    CheckBox cb_assisting_with_children, cb_cooking, cb_House_errands, cb_bet_friendly;
    String valueSkillls,cb_assisting_with_children_Value, cb_cooking_Value, cb_House_errands_Value, cb_bet_friendly_Value;
    LinearLayout ass_reg_parentLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_registration);
        mContext = AssitantFinalRegistration.this;
        initializeControls();

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(edt_hourly.getText().length()>0&&
                        cb_assisting_with_children_Value != null||
                        cb_bet_friendly_Value != null||
                        cb_cooking_Value != null||
                        cb_House_errands_Value != null)
                {
                    if (NetworkUtils.isNetworkConnectionAvailable(mContext))
                    {
                        sendData();
                    } else
                    {
                        Toast.makeText(mContext, "Please check internet connection", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(edt_hourly.getText().length() == 0)
                    {
                        showAlertValidation("Please give the hourly rate");
                    }else if(cb_assisting_with_children_Value == null)
                    {
                        showAlertValidation("Please check the any one skill");
                    }else if( cb_bet_friendly_Value == null)
                    {
                        showAlertValidation("Please check the any one skill");
                    }else if(cb_cooking_Value == null)
                    {
                        showAlertValidation("Please check the any one skill");
                    }else if(cb_House_errands_Value == null)
                    {
                        showAlertValidation("Please check the any one skill");
                    }

                }

            }
        });

        cb_assisting_with_children.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(compoundButton.isChecked()) {
                    cb_assisting_with_children_Value ="assisting with children";

                }else
                {
                    cb_assisting_with_children_Value = null;
                }
                Log.v("","res checked "+cb_cooking_Value);
            }
        });

        cb_cooking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()) {
                    cb_cooking_Value ="Cooking";

                }else
                {
                    cb_cooking_Value = null;
                }
                Log.v("","res checked "+cb_cooking_Value);
            }
        });

        cb_House_errands.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()) {
                    cb_House_errands_Value ="house errands";

                }else
                {
                    cb_House_errands_Value = null;
                }
                Log.v("","res checked "+cb_House_errands_Value);
            }
        });

        cb_bet_friendly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()) {
                    cb_bet_friendly_Value ="betting";

                }else
                {
                    cb_bet_friendly_Value = null;
                }
                Log.v("","res checked "+cb_bet_friendly_Value);
            }
        });
    }

    private void sendData()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConstants.ASSISTANT_SIGNUP_FINAL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("Success"))
                            {
                              //  Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Success"), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),AssitantDashBoard.class));
                                Log.v("res ", "res  success" + jsonObject.getString("Success"));
                            } else {
                                jsonObject.getString("Error");
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                                Log.v("res ", "res  success" + jsonObject.getString("Error"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Crashlytics.logException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("error", "error " + error.toString());
                        Toast.makeText(AssitantFinalRegistration.this, error.toString(), Toast.LENGTH_LONG).show();
                        Crashlytics.logException(error);
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                Log.v("test","test1 "+ validateSkills());
                String credentials;
PreferenceUtils preferenceUtils = new PreferenceUtils(getApplicationContext());
                    credentials = "a_id="+ preferenceUtils.getStringFromPreference("a_id","")
                            +"&a_experience="+ edt_experience.getText().toString()+
                            "&a_hourly_rate="+ edt_hourly.getText().toString()+
                            "&a_city="+ edt_city.getText().toString()+
                            "&a_state="+ edt_state.getText().toString()+
                            "&a_about_me="+ edt_about_me.getText().toString()+
                            "&a_skill="+ validateSkills()+edt_others.getText().toString();
                Log.v("test","test2 "+ credentials);
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

    private String validateSkills()
    {
        if(cb_assisting_with_children_Value != null&&
                cb_cooking_Value != null&&
                cb_bet_friendly_Value != null&&
                cb_House_errands_Value != null)
        {
            valueSkillls = "assisting with children,Cooking,betting,house errands";
        }
        else if(cb_assisting_with_children_Value != null)
        {
            valueSkillls = "assisting with children";
            if(cb_cooking_Value != null)
            {
                valueSkillls =  valueSkillls + ",Cooking";
            }else if(cb_bet_friendly_Value != null)
            {
                valueSkillls = valueSkillls + ",betting";
            }else if(cb_House_errands_Value != null)
            {
                valueSkillls = valueSkillls + ",house errands";
            }
        }else if(cb_cooking_Value != null)
        {
            valueSkillls = "Cooking";
            if(cb_bet_friendly_Value != null)
            {
                valueSkillls = valueSkillls + ",betting";
            }
            else if(cb_House_errands_Value != null)
            {
                valueSkillls = valueSkillls + ",house errands";
            }
        }else if(cb_bet_friendly_Value != null)
        {
            valueSkillls = "betting";
            if(cb_House_errands_Value != null)
            {
                valueSkillls = valueSkillls + ",house errands";
            }
        }else if(cb_House_errands_Value != null)
        {
            valueSkillls = valueSkillls + "house errands";
        }
    return valueSkillls;
    }

    private void initializeControls() {
        View test1View = findViewById(R.id.toolbarLayout);
        toolbarTextView = (TextView) test1View.findViewById(R.id.page_heading);
        toolbarTextView.setText("Registration");
        edt_experience = (EditText) findViewById(R.id.edt_experience);
        edt_hourly = (EditText) findViewById(R.id.edt_hourly);
        edt_city = (EditText) findViewById(R.id.edt_city);
        edt_state = (EditText) findViewById(R.id.edt_state);
        edt_about_me = (EditText) findViewById(R.id.edt_about_me);
        edt_others = (EditText)findViewById(R.id.edt_other);

        cb_assisting_with_children = (CheckBox)findViewById(R.id.cb_assisting_with_children);
        cb_cooking = (CheckBox)findViewById(R.id.cb_cooking);
        cb_House_errands = (CheckBox)findViewById(R.id.cb_House_errands);
        cb_bet_friendly =(CheckBox)findViewById(R.id.cb_bet_friendly);
        tv_signup = (TextView)findViewById(R.id.tv_signup);
        ass_reg_parentLayout = (LinearLayout)findViewById(R.id.ass_reg_parentLayout);
        FontStyle.applyFont(getApplicationContext(),ass_reg_parentLayout,FontStyle.Lato_Medium);
    }
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
}

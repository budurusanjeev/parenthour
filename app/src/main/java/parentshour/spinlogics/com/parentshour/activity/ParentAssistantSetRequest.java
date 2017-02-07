package parentshour.spinlogics.com.parentshour.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.utilities.AppConstants;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;

/**
 * Created by SPINLOGICS on 1/12/2017.
 */

public class ParentAssistantSetRequest extends BaseActivity {
    private static final int FROM_TIME_PICKER_ID = 1;
    private static final int TO_TIME_PICKER_ID = 2;
    private static SimpleDateFormat dateFormatter;
    Context context;
    TextView tv_date, tv_start_time, tv_end_time, tv_request, tv_cancel;
    EditText edt_task_name;
    String a_id;

    @Override
    public void initialize() {
        context = ParentAssistantSetRequest.this;

        preferenceUtils = new PreferenceUtils(context);

        llContent.addView(inflater.inflate(R.layout.activity_parent_assistant_set_request, null), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        a_id = getIntent().getExtras().getString("aid");
        initViewControll();
        Fabric.with(this, new Crashlytics());
    }

    private void initViewControll() {
        TextView toolbarTextView = (TextView) findViewById(R.id.page_heading);
        toolbarTextView.setText("Request Assistant");
        TextView tv_save = (TextView) findViewById(R.id.setting_Save);
        tv_save.setText("Done");
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        tv_date = (TextView) findViewById(R.id.tv_date_value);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time_value);
        tv_end_time = (TextView) findViewById(R.id.tv_end_time_value);
        tv_request = (TextView) findViewById(R.id.tv_set_request);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        edt_task_name = (EditText) findViewById(R.id.edt_task_name_value);
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dFragment = new DatePickerFragment();

                // Show the date picker dialog fragment
                dFragment.show(getSupportFragmentManager(), "Date Picker");
            }
        });
        tv_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* DialogFragment dFragment = new TimePickerFragment();

                // Show the time picker dialog fragment
                dFragment.show(getSupportFragmentManager(), "Time Picker");*/
                TimePickerFragment newFragment = TimePickerFragment.newInstance(FROM_TIME_PICKER_ID);
                newFragment.show(getSupportFragmentManager(), "timePicker");

            }
        });
        tv_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* DialogFragment dFragment = new TimePickerFragment();

                // Show the time picker dialog fragment
                dFragment.show(getSupportFragmentManager(), "Time Picker");*/
                TimePickerFragment newFragmentNight = TimePickerFragment.newInstance(TO_TIME_PICKER_ID);
                newFragmentNight.show(getSupportFragmentManager(), "timePicker");
            }
        });

        tv_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tv_date.getText().equals("")
                        && !tv_start_time.getText().equals("")
                        && !tv_end_time.getText().equals("")
                        && !edt_task_name.getText().toString().equals("")) {
                    sendSetRequest();
                } else {
                    if (tv_date.getText().equals("")) {
                        showAlertValidation("Date Cannot be empty");
                    } else if (tv_start_time.getText().equals("")) {
                        showAlertValidation("Start Time Cannot be empty");
                    } else if (tv_end_time.getText().equals("")) {
                        showAlertValidation("End Time Cannot be empty");
                    } else if (edt_task_name.getText().toString().equals("")) {
                        showAlertValidation("Task Name Cannot be empty");
                    }
                }

            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void sendSetRequest() {
        showLoaderNew();
        // PARENT_SEARCH_PLAY_DATE_URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.PARENT_SET_ASSISTANT_REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response.trim());
                        hideloader();
                        Log.v("res ", "res  " + preferenceUtils.getStringFromPreference("p_id", ""));
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("Success")) {

                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Success"), Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                jsonObject.getString("error");
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
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideloader();
                        Log.v("error", "error " + error.toString());
                        Toast.makeText(ParentAssistantSetRequest.this, error.toString(), Toast.LENGTH_LONG).show();
                        //throw new RuntimeException("crash " + error);
                        Crashlytics.logException(error);
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                String credentials = "p_id=" + preferenceUtils.getStringFromPreference("p_id", "") + "&a_id=" + a_id +
                        "&a_date=" + tv_date.getText().toString() + "&a_start_time=" + tv_start_time.getText().toString() +
                        "&a_end_time=" + tv_end_time.getText().toString() + "&a_task_name=" + edt_task_name.getText().toString();
                // p_id=12&a_id=2&a_date=2016-12-01&a_start_time=12:00&a_end_time=22:00&a_task_name=aaa

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

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_DARK, this, year, month, day);

            // Add 3 days to Calendar
            //calendar.add(Calendar.DATE, 10);

            // Set the Calendar new date as maximum date of date picker
            // dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis()+900000);

            // Subtract 6 days from Calendar updated date
            // calendar.add(Calendar.DATE, -6);

            // Set the Calendar new date as minimum date of date picker
            dpd.getDatePicker().setMinDate(calendar.getTimeInMillis());

            // So, now date picker selectable date range is 7 days only

            // Return the DatePickerDialog
            return dpd;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the chosen date
            TextView tv_date = (TextView) getActivity().findViewById(R.id.tv_date_value);

            // Create a Date variable/object with user chosen date
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(year, month, day, 0, 0, 0);
            Date chosenDate = cal.getTime();

            // Format the date using style and locale
            // DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
            String formattedDate = dateFormatter.format(chosenDate);
            // Display the chosen date to app interface
            tv_date.setText(formattedDate);
        }
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        private int mId;

        static TimePickerFragment newInstance(int id) {
            Bundle args = new Bundle();
            args.putInt("picker_id", id);
            TimePickerFragment fragment = new TimePickerFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            mId = getArguments().getInt("picker_id");
            // Get a Calendar instance
            final Calendar calendar = Calendar.getInstance();
            // Get the current hour and minute
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK, this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }


        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the returned time
            TextView tv_start_time = (TextView) getActivity().findViewById(R.id.tv_start_time_value);
            TextView tv_end_time = (TextView) getActivity().findViewById(R.id.tv_end_time_value);

            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);

            if (mId == 1) {

                tv_start_time.setText(hourOfDay + ":" + minute);
            } else {
                tv_end_time.setText(hourOfDay + ":" + minute);
            }
        }
    }
}


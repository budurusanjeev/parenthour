package parentshour.spinlogics.com.parentshour.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.adapter.ParentGroupRowAdapter;
import parentshour.spinlogics.com.parentshour.models.ParentFriendModel;
import parentshour.spinlogics.com.parentshour.models.PlaySearchDateModel;
import parentshour.spinlogics.com.parentshour.utilities.AppConstants;
import parentshour.spinlogics.com.parentshour.utilities.LoadingText;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;

/**
 * Created by SPINLOGICS on 1/5/2017.
 */


public class ParentEventCreation extends AppCompatActivity {
    private static SimpleDateFormat dateFormatter;
    Context context;
    TextView tv_date, tv_time, tv_addFriends, tv_create, tv_cancel;
    EditText edt_address_value;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter adapter;
    String eventId;
    ArrayList<PlaySearchDateModel> parentAddGroupArrayList;
    ArrayList<ParentFriendModel> parentFriendModels;
    String friendsList;
    TextView toolbarTextView;
    PreferenceUtils preferenceUtils;
    LoadingText loadingText;
    private int SELECT_FRIENDS = 2;

    @SuppressLint("SetTextI18n")

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation);
        context = ParentEventCreation.this;
        preferenceUtils = new PreferenceUtils(context);
        loadingText = new LoadingText(context);
        initViewControll();
        if (getIntent().getExtras().get("eventId") != null) {
            eventId = getIntent().getExtras().getString("eventId");
            Log.v("get group", "get group " + eventId);
            getEventDetails();
            tv_create.setText("Update");

            toolbarTextView.setText("Edit Play Date");
        }
        Fabric.with(this, new Crashlytics());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getExtras().get("eventId") != null) {

            toolbarTextView.setText("Edit Play Date");
        } else {
            toolbarTextView.setText("Add Play Date");
        }
    }

    private void getEventDetails() {
        loadingText.showLoaderNew(ParentEventCreation.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.PARENT_GET_EVENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        Log.v("res ", "res  " + preferenceUtils.getStringFromPreference("p_id", ""));
                        loadingText.hideloader(ParentEventCreation.this);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            tv_date.setText(jsonObject.getString("pe_date"));
                            tv_time.setText(jsonObject.getString("pe_time"));
                            edt_address_value.setText(jsonObject.getString("pe_address"));
                            friendsList = jsonObject.getString("pid_list");
                            JSONArray jsonArray = jsonObject.getJSONArray("frnd_list");
                            int sz = jsonArray.length();
                            for (int s = 0; s < sz; s++) {
                                JSONObject friendObject = jsonArray.getJSONObject(s);
                                ParentFriendModel parentFriendModel = new ParentFriendModel();
                                parentFriendModel.setpId(friendObject.getString("p_id"));
                                parentFriendModel.setpName(friendObject.getString("p_name"));
                                parentFriendModel.setpImgUrl(friendObject.getString("p_pic"));
                                parentFriendModels.add(parentFriendModel);
                                // friendsList = friendObject.getString("p_id") + ",";
                            }
                            adapter = new ParentGroupRowAdapter(parentFriendModels, context, "event");
                            mRecyclerView.setAdapter(adapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Crashlytics.logException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingText.hideloader(ParentEventCreation.this);
                        Log.v("error", "error " + error.toString());
                        Toast.makeText(ParentEventCreation.this, error.toString(), Toast.LENGTH_LONG).show();
                        Crashlytics.logException(error);
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                String credentials = "p_id=" + preferenceUtils.getStringFromPreference("p_id", "") + "&pe_id=" + eventId;

                Log.v("url", "url " + credentials);
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

    private void initViewControll() {
        tv_date = (TextView) findViewById(R.id.tv_date_value);
        tv_time = (TextView) findViewById(R.id.tv_time_value);
        tv_addFriends = (TextView) findViewById(R.id.tv_addFriends);
        edt_address_value = (EditText) findViewById(R.id.edt_address_value);
        tv_create = (TextView) findViewById(R.id.tv_create);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerAddedGroupMembers);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        parentFriendModels = new ArrayList<ParentFriendModel>();
        parentAddGroupArrayList = new ArrayList<PlaySearchDateModel>();
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePick();

            }
        });
        View test1View = findViewById(R.id.toolbarLayout);
        toolbarTextView = (TextView) test1View.findViewById(R.id.page_heading);

        // toolbarTextView = (TextView) findViewById(R.id.page_heading);
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dFragment = new TimePickerFragment();

                // Show the time picker dialog fragment
                dFragment.show(getSupportFragmentManager(), "Time Picker");
            }
        });

        tv_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getExtras().get("eventId") != null) {
                    editEvent();
                } else {
                    if (!tv_date.getText().toString().equals("") &&
                            !tv_time.getText().toString().equals("") &&
                            !edt_address_value.getText().toString().equals("") &&
                            parentFriendModels.size() > 0) {
                        createEvent();
                    } else {
                        if (tv_date.getText().toString().equals("")) {
                            showAlertValidation("Please give date");
                        } else if (tv_time.getText().toString().equals("")) {
                            showAlertValidation("Please give time");
                        } else if (edt_address_value.getText().toString().equals("")) {
                            showAlertValidation("Please give address");
                        } else if (parentFriendModels.size() == 0) {
                            showAlertValidation("Please add a friend");
                        }

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

        tv_addFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(new Intent(getApplicationContext(), ParentAddFriendList.class)
                        .putParcelableArrayListExtra("selected", parentFriendModels), SELECT_FRIENDS);
            }
        });

    }

    public void removeFriend(int newPosition) {
        friendsList = "";
        parentFriendModels.remove(newPosition);
        adapter.notifyItemRemoved(newPosition);
        for (int s = 0; s < parentFriendModels.size(); s++) {
            friendsList = friendsList + "," + parentFriendModels.get(s).getpId();
            Log.v("friends", "friends: " + friendsList);
        }
        adapter.notifyItemRangeChanged(newPosition, parentFriendModels.size());

    }

    public void showAlertValidation(String error) {
        final android.support.v7.app.AlertDialog.Builder dialogBuilder =
                new android.support.v7.app.AlertDialog.Builder(ParentEventCreation.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.notification_dailog, null);
        dialogBuilder.setView(dialogView);
        final android.support.v7.app.AlertDialog b = dialogBuilder.create();
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

    private void editEvent() {
        loadingText.showLoaderNew(ParentEventCreation.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.PARENT_EDIT_EVENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        Log.v("res ", "res  " + preferenceUtils.getStringFromPreference("p_id", ""));
                        loadingText.hideloader(ParentEventCreation.this);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("Success")) {
                                Toast.makeText(ParentEventCreation.this, jsonObject.getString("Success"), Toast.LENGTH_LONG).show();
                                finish();
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
                        loadingText.hideloader(ParentEventCreation.this);
                        Log.v("error", "error " + error.toString());
                        Toast.makeText(ParentEventCreation.this, error.toString(), Toast.LENGTH_LONG).show();
                        Crashlytics.logException(error);
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                String credentials = "p_id=" + preferenceUtils.getStringFromPreference("p_id", "") +
                        "&pe_date=" + tv_date.getText().toString() + "&pe_time=" + tv_time.getText().toString()
                        + "&pe_address=" + edt_address_value.getText().toString() +
                        "&p_id_list=" + friendsList + "&pe_id=" + eventId;

                // p_id=12&pe_date=2016-12-24&pe_time=10:30&pe_address=hyderabad&p_id_list=13,12

                Log.v("url", "url " + credentials);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.v("friend list","friend list: "+ requestCode+" data "+data.getStringExtra("friendsList"));
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
                arrayToString(data);
            }
        }
    }

    private void arrayToString(Intent data) {
        friendsList = data.getStringExtra("friendsList");
        parentFriendModels.clear();
        parentFriendModels = data.getParcelableArrayListExtra("friendObject");
        adapter = new ParentGroupRowAdapter(parentFriendModels, context, "event");
        mRecyclerView.setAdapter(adapter);
    }

    private void datePick() {

        DialogFragment dFragment = new DatePickerFragment();

        // Show the date picker dialog fragment
        dFragment.show(getSupportFragmentManager(), "Date Picker");
    }

    private void createEvent() {
        loadingText.showLoaderNew(ParentEventCreation.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.PARENT_CREATE_EVENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        Log.v("res ", "res  " + preferenceUtils.getStringFromPreference("p_id", ""));
                        loadingText.hideloader(ParentEventCreation.this);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("Success")) {
                                Toast.makeText(ParentEventCreation.this, jsonObject.getString("Success"), Toast.LENGTH_LONG).show();
                                finish();
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
                        loadingText.hideloader(ParentEventCreation.this);
                        Log.v("error", "error " + error.toString());
                        Toast.makeText(ParentEventCreation.this, error.toString(), Toast.LENGTH_LONG).show();
                        Crashlytics.logException(error);
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                String credentials = "p_id=" + preferenceUtils.getStringFromPreference("p_id", "") +
                        "&pe_date=" + tv_date.getText().toString() + "&pe_time=" + tv_time.getText().toString()
                        + "&pe_address=" + edt_address_value.getText().toString() +
                        "&p_id_list=" + friendsList;

                // p_id=12&pe_date=2016-12-24&pe_time=10:30&pe_address=hyderabad&p_id_list=13,12

                Log.v("url", "url " + credentials);
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

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get a Calendar instance
            final Calendar calendar = Calendar.getInstance();
            // Get the current hour and minute
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // TimePickerDialog Theme : THEME_HOLO_DARK
            TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_DARK, this, hour, minute, false);


            // Return the TimePickerDialog
            return tpd;
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the returned time
            TextView tv_time = (TextView) getActivity().findViewById(R.id.tv_time_value);
            // tv_time.setText(hourOfDay + ":" + minute);
            tv_time.setText(updateTime(hourOfDay, minute));
            //;
        }

        // Used to convert 24hr format to 12hr format with AM/PM values
        private String updateTime(int hours, int mins) {

            String timeSet = "";
            if (hours > 12) {
                hours -= 12;
                timeSet = "PM";
            } else if (hours == 0) {
                hours += 12;
                timeSet = "AM";
            } else if (hours == 12)
                timeSet = "PM";
            else
                timeSet = "AM";
            String hrs = "";
            if (hours < 10) {
                hrs = "0" + hours;
            } else {
                hrs = String.valueOf(hours);
            }

            String minutes = "";
            if (mins < 10)
                minutes = "0" + mins;
            else
                minutes = String.valueOf(mins);

            // Append in a StringBuilder
            String aTime = new StringBuilder().append(hrs).append(':')
                    .append(minutes).append(" ").append(timeSet).toString();

            return aTime;
            // output.setText(aTime);
        }



    }

}

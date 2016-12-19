package parentshour.spinlogics.com.parentshour.utilities;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class CurrentDate {

    public static final String SLASH_DD_MM_YYYY = "dd/mm/yyyy";
    public static final String IPEN_YYYY_MM_DD = "yyyy-mm-dd";

    public String currentDate, kotdate, crt_ts;
    public String fullDateTime, slashDate, onlytime, billdate, mealorderdate;
    Date date = null;

    public CurrentDate(String dateinString, String type) {

        SimpleDateFormat format;
        try {
            switch (type) {
                case SLASH_DD_MM_YYYY:
                    format = new SimpleDateFormat("dd/MM/yyyy");

                    date = format.parse(dateinString);

                    break;
                case IPEN_YYYY_MM_DD:
                    format = new SimpleDateFormat("yyyy-mm-dd");
                    date = format.parse(dateinString);
                    break;
                default:
                    date = new Date();


            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        SimpleDateFormat sdf1 = new SimpleDateFormat("ddMMMyy");
        currentDate = sdf1.format(date);

        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        fullDateTime = sdf2.format(date);

        SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy");
        slashDate = sdf3.format(date);

        SimpleDateFormat sdf4 = new SimpleDateFormat("h:mm a");
        onlytime = sdf4.format(date);

        SimpleDateFormat sdf5 = new SimpleDateFormat("ddMMyy");
        kotdate = sdf5.format(date);

        SimpleDateFormat sdf6 = new SimpleDateFormat("yyMMdd");
        billdate = sdf6.format(date);

        SimpleDateFormat sdf7 = new SimpleDateFormat("yyyy-MM-dd");
        mealorderdate = sdf7.format(date);


        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
        String inputcrt_ts = df.format(date);

        DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            date = df.parse(inputcrt_ts);

            crt_ts = outputformat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public CurrentDate() {

        date = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("ddMMMyy");
        currentDate = sdf1.format(new Date());

        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        fullDateTime = sdf2.format(new Date());

        SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy");
        slashDate = sdf3.format(new Date());

        SimpleDateFormat sdf4 = new SimpleDateFormat("h:mm a");
        onlytime = sdf4.format(new Date());

        SimpleDateFormat sdf5 = new SimpleDateFormat("ddMMyy");
        kotdate = sdf5.format(new Date());

        SimpleDateFormat sdf6 = new SimpleDateFormat("yyMMdd");
        billdate = sdf6.format(new Date());

        SimpleDateFormat sdf7 = new SimpleDateFormat("yyyy-MM-dd");
        mealorderdate = sdf7.format(new Date());


        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
        String inputcrt_ts = df.format(new Date());

        DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            date = df.parse(inputcrt_ts);

            crt_ts = outputformat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public CurrentDate(Date date) {
        this.date = date;
        SimpleDateFormat sdf1 = new SimpleDateFormat("ddMMMyy");
        currentDate = sdf1.format(date);

        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        fullDateTime = sdf2.format(date);

        SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy");
        slashDate = sdf3.format(date);

        SimpleDateFormat sdf4 = new SimpleDateFormat("h:mm a");
        onlytime = sdf4.format(date);

        SimpleDateFormat sdf5 = new SimpleDateFormat("ddMMyy");
        kotdate = sdf5.format(date);

        SimpleDateFormat sdf6 = new SimpleDateFormat("yyMMdd");
        billdate = sdf6.format(date);

        SimpleDateFormat sdf7 = new SimpleDateFormat("yyyy-MM-dd");
        mealorderdate = sdf7.format(date);


        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
        String inputcrt_ts = df.format(date);

        DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            date = df.parse(inputcrt_ts);

            crt_ts = outputformat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    public ArrayList<String> timeslotsforweedays(String startTime, String endTime) {

        DateFormat dateintimeformat = new SimpleDateFormat("h:mm a");

        ArrayList<String> timeSlots = new ArrayList<>();

        try {
            Date startTimeDate = dateintimeformat.parse(startTime);
            Date endTimeDate = dateintimeformat.parse(endTime);

            if (startTimeDate.before(endTimeDate)) {

                for (Date i = startTimeDate; i.before(endTimeDate); i = increase15minutes(i)) {

                    CurrentDate currentDate = new CurrentDate(i);
                    timeSlots.add(currentDate.onlytime);
                }
            } else {
                endTimeDate = addDaysTodate(endTimeDate);
                for (Date i = startTimeDate; i.before(endTimeDate); i = increase15minutes(i)) {

                    CurrentDate currentDate = new CurrentDate(i);
                    timeSlots.add(currentDate.onlytime);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return timeSlots;
    }

    public Date addDaysTodate(Date date) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.add(Calendar.DATE, 1);

        return calendar.getTime();
    }

    private Date increase15minutes(Date i) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(i);
        calendar.add(Calendar.MINUTE, 15);
        return calendar.getTime();
    }

    public Date getDateFromSlashDate(String slashDate) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        Date opdate = new Date();
        try {
            opdate = df.parse(slashDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return opdate;
    }


    public String getcurrentRoundTimeaddHours(int roundingMinutes, int addHours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (calendar.get(Calendar.MINUTE) < roundingMinutes) {
            calendar.set(Calendar.MINUTE, 15);
        } else {
            calendar.set(Calendar.MINUTE, 0);
        }

        calendar.add(Calendar.HOUR, addHours);

        Date changedDate = calendar.getTime();

        CurrentDate currentDate = new CurrentDate(changedDate);
        return currentDate.onlytime;
    }

    public static String convert24hrsto12hrsformat(String time_HH_MM) {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");

        Date date = new Date();
        try {
            date = formatter.parse(time_HH_MM);
        } catch (ParseException e) {

        }

        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");

        return sdf.format(date);
    }

    public String converTimeto24HrsFormatWithMillis(String time_HH_MM_A) {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");

        Date date = new Date();
        try {
            date = formatter.parse(time_HH_MM_A);
        } catch (ParseException e) {

        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");

        return sdf.format(date);
    }

    public Date getDate() {
        return date;
    }
}

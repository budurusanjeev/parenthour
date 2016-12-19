package parentshour.spinlogics.com.parentshour.activity;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import parentshour.spinlogics.com.parentshour.utilities.LoggerUtils;

@SuppressLint("SimpleDateFormat")
public class CurrentDate {
    public String currentDate,kotdate,crt_ts;
    public String fullDateTime,slashDate,onlytime,buyerregisterdate,
            billdate,mealorderdate,sellersubsribedate;
    Date date = null;

    public CurrentDate(){
        SimpleDateFormat sdf1 = new SimpleDateFormat("ddMMMyy");
        currentDate = sdf1.format(new Date());

        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        fullDateTime = sdf2.format(new Date());

        SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy");
        slashDate = sdf3.format(new Date());

        SimpleDateFormat sdf4 = new SimpleDateFormat("h:mm a");
        onlytime = sdf4.format(new Date());

        SimpleDateFormat sdf5=new SimpleDateFormat("ddMMyy");
        kotdate=sdf5.format(new Date());

        SimpleDateFormat sdf6=new SimpleDateFormat("yyMMdd");
        billdate=sdf6.format(new Date());

        SimpleDateFormat sdf7=new SimpleDateFormat("yyyy-M-d");
        mealorderdate= sdf7.format(new Date());

        SimpleDateFormat sdf8=new SimpleDateFormat("dd-MMM-yyyy");
        sellersubsribedate=sdf8.format(new Date());

        SimpleDateFormat sdf9=new SimpleDateFormat("dd-MMM-yyyy");
        buyerregisterdate= sdf9.format(new Date());

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
        String inputcrt_ts=df.format(new Date());

        DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            date=df.parse(inputcrt_ts);

            crt_ts=outputformat.format(date);

        } catch (ParseException e) {
            // TODO Auto-generated catch ic_block
            e.printStackTrace();
        }

    }

    /**
     * We are getting the Indain timezone Time and Date in 24 hour format
     * @return String
     */
    public String getOrdertime() {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
        DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sbCurrentTimestamp = null;
        Calendar cSchedStartCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        long gmtTime = cSchedStartCal.getTime().getTime();

        long timezoneAlteredTime = gmtTime + TimeZone.getTimeZone("Asia/Calcutta").getRawOffset();
        Calendar cSchedStartCal1 = Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta"));
        cSchedStartCal1.setTimeInMillis(timezoneAlteredTime);


        Date date = cSchedStartCal1.getTime();

        String input_crt_ts = df.format(date);

        Date outputDate = null;
        try {
            outputDate = df.parse(input_crt_ts);
        } catch (ParseException e) {
            // TODO Auto-generated catch ic_block
            e.printStackTrace();
        }
        sbCurrentTimestamp = outputformat.format(outputDate);

        return sbCurrentTimestamp;
    }


    public String getOrdertime(Date passed_date) {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
        DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sbCurrentTimestamp = null;
		/*Calendar cSchedStartCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		long gmtTime = cSchedStartCal.getTime().getTime();

		long timezoneAlteredTime = gmtTime + TimeZone.getTimeZone("Asia/Calcutta").getRawOffset();
		Calendar cSchedStartCal1 = Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta"));
		cSchedStartCal1.setTimeInMillis(timezoneAlteredTime);


		Date ic_date = cSchedStartCal1.getTime();*/

        String input_crt_ts = df.format(passed_date);

        Date outputDate = null;
        try {
            outputDate = df.parse(input_crt_ts);
        } catch (ParseException e) {
            // TODO Auto-generated catch ic_block
            e.printStackTrace();
        }
        sbCurrentTimestamp = outputformat.format(outputDate);

        return sbCurrentTimestamp;
    }

    public String getforamtteddate(String datetoformat)
    {
        String formatteddate = null;

        SimpleDateFormat sdf7=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date date=sdf7.parse(datetoformat);

            formatteddate=sdf3.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatteddate;
    }


    public boolean menutimevalidator(String cutofftime, String appsystemtime)
    {
        DateFormat dateintimeformat=new SimpleDateFormat("h:mm a");

        DateFormat appsystimeformat=new SimpleDateFormat("hh:mm:ss");

        Date cutofftimeindate=new Date(),currenttime=new Date();

        //String systime="12:51:20";

        try {
            cutofftimeindate=dateintimeformat.parse(cutofftime);

            //currenttime=dateintimeformat.parse(onlytime);

            currenttime=appsystimeformat.parse(appsystemtime);

        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return cutofftimeindate.getTime() - currenttime.getTime() > 0;

    }


    public String gettommoroworderdate(Date inputdate)
    {
        Date outputdate;

        String tommorowdate;

        SimpleDateFormat sdf7=new SimpleDateFormat("yyyy-M-d");


        Calendar calendar= Calendar.getInstance();

        calendar.setTime(inputdate);

        calendar.add(Calendar.DATE, 1);

        outputdate=calendar.getTime();

        tommorowdate=sdf7.format(outputdate);

        return tommorowdate;
    }

    public Date nextdaydate(Date inputdate)
    {
        Date outputdate;

        Calendar calendar= Calendar.getInstance();

        calendar.setTime(inputdate);

        calendar.add(Calendar.DATE, 1);

        outputdate=calendar.getTime();

        return outputdate;

    }

    public String getmealorderdate(String inputdate)  {
        String outputdate = null;

        Date outputDate;

        SimpleDateFormat sdf7=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf11=new SimpleDateFormat("yyyy-MM-dd");

        try {
            outputDate=sdf7.parse(inputdate);

            outputdate=sdf11.format(outputDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return  outputdate;
    }
    public Date addDaysTodate(Date date, int numofDays) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.add(Calendar.DATE, numofDays);

        return calendar.getTime();
    }


    public String startDateFormat(Date inputdate)  {
        String outputdate = null;

        Date outputDate;

        SimpleDateFormat sdf7=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf11=new SimpleDateFormat("yyyy-MM-dd");

        /*try {
           // outputDate=sdf7.parse(inputdate);

            outputdate=sdf11.format(inputdate);

        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        outputdate=sdf11.format(inputdate);

        LoggerUtils.info(CurrentDate.class.getSimpleName(), outputdate);


        return  outputdate;
    }


    public String getafteroneyeardate()
    {
        String nextyeardate;

        SimpleDateFormat sdf7=new SimpleDateFormat("dd-MMM-yyyy");

        Calendar cal = Calendar.getInstance();

        Date today = cal.getTime();

        cal.add(Calendar.YEAR, 1); // to get previous year ic_add -1

        Date nextYear = cal.getTime();

        nextyeardate=sdf7.format(nextYear);

        return nextyeardate;
    }

    public String getafterthreeyeardate()
    {
        String nextyeardate;

        SimpleDateFormat sdf7=new SimpleDateFormat("dd-MMM-yyyy");

        Calendar cal = Calendar.getInstance();

        Date today = cal.getTime();

        cal.add(Calendar.YEAR, 3); // to get previous year ic_add -1

        Date nextYear = cal.getTime();

        nextyeardate=sdf7.format(nextYear);

        return nextyeardate;
    }

    public String getafterfiveyeardate()
    {
        String nextyeardate;

        SimpleDateFormat sdf7=new SimpleDateFormat("dd-MMM-yyyy");

        Calendar cal = Calendar.getInstance();

        Date today = cal.getTime();

        cal.add(Calendar.YEAR, 5); // to get previous year ic_add -1

        Date nextYear = cal.getTime();

        nextyeardate=sdf7.format(nextYear);

        return nextyeardate;
    }

}

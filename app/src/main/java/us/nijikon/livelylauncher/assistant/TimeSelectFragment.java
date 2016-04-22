package us.nijikon.livelylauncher.assistant;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.launcher.AppFragment;
import us.nijikon.livelylauncher.launcher.Launcher;
import us.nijikon.livelylauncher.models.Event;

public class TimeSelectFragment extends Fragment {

    public static final String tag = "TimeSelect";
    static String finalDate;
    static String finalTime;
    static final String KEY = "us.nijikon.livelylauncher.assistant.key";
    Event event;
    private Launcher launcher;
    //static int yearG,monthG,dateG,hourG,minuteG=0;

    OnClickAtFrameListener callback;


    public interface OnClickAtFrameListener {
        void saveDate(Event ev, Date date);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        callback = (OnClickAtFrameListener)activity;
    }


    public TimeSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_time_select, container, false);

        Button btn1 = (Button) v.findViewById(R.id.btn1);
        Button btn2 = (Button) v.findViewById(R.id.btn2);
        Button btn3 = (Button) v.findViewById(R.id.btn3);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dateTime = finalDate + " " + finalTime;
                Date date = TimeSelectFragment.stringToDate(dateTime);
                if(date == null){
                    date = new Date();
                }
                event = new Event();

                event.setDate(dateTime);

                callback.saveDate(event,date);

                ((Launcher)getActivity()).goFragment(CategoryFragment.TAG);

            }
        });

        return v;
    }



    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
        //Log.e("test:", "date error1");
    }

    public void showTimePickerDialog(View v) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
        //Log.e("test:", "time error1");
    }

//    public void confirm(Event event){
//
//        //eventDate = new EventDate(dateTime);
//        Intent intent = new Intent(this, CategoryActivity.class);
//        intent.putExtra(KEY, event);
//        Log.e("check AT MAIN:", event.getDate());
//        startActivity(intent);
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Date stringToDate(String string){

        String DateFormat = "dd/MM/yy HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
        try {
            return sdf.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("check0:", "exeception");
        }
        return null;
    }



    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        Calendar c = Calendar.getInstance();
        Date time ;
        String timeString;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // the current time as the default values
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);

            String myFormat = "HH:mm:ss"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            TextView txv2 =(TextView)getActivity().findViewById(R.id.textView2);
            time = c.getTime();
            finalTime = sdf.format(time);
            txv2.setText(finalTime);

//            hourG=hourOfDay;
//            minuteG=minute;
        }

        public String getInnerTime(){
            return timeString;
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        Calendar c = Calendar.getInstance();

        Date date;
        String dateString;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);

            String myFormat = "dd/MM/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            TextView txv1 =(TextView)getActivity().findViewById(R.id.textView1);
            date = c.getTime();
            finalDate = sdf.format(date);
            txv1.setText(sdf.format(c.getTime()));
            Log.e("check:", "pass to outer");


        }

        public String getInnerDate(){
            return dateString;
        }

    }

    public TimeSelectFragment setParent(Launcher launcher){
        this.launcher = launcher;
        return this;
    }
}

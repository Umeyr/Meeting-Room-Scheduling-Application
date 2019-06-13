package com.example.zorlu.myapplication;



import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.org.apache.http.client.utils.DateUtils;
import android.org.apache.http.conn.ConnectTimeoutException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;


import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BasePropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.search.LogicalOperator;
import microsoft.exchange.webservices.data.core.exception.service.remote.ServiceRequestException;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.ItemSchema;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.property.complex.Mailbox;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.property.complex.StringList;
import microsoft.exchange.webservices.data.search.CalendarView;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;
import microsoft.exchange.webservices.data.search.filter.SearchFilter;


public class MainActivity extends AppCompatActivity implements Frame_Log.OnNameSetListener, Frame_Info.OnNameSetListener2{


    List<CustomAppointment> appointmentList;

    String roomname2;

    static Adaptor adaptor;
    String errorName;
    TextView roomname;

    String Mail, Pass, Url;
    View frag1, frag2;
    String invalidInput;
    Context mContext;

    Long thistime;
    Boolean firstClick = true;


    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        roomname = (TextView)findViewById(R.id.roomname);
        frag1 = findViewById(R.id.fragment_log);
        frag2 = findViewById(R.id.fragment_info);
        frag2.setVisibility(View.INVISIBLE);
        roomname.setVisibility(View.INVISIBLE);

        invalidInput = "Invalid input. Retype your email or password";
        mContext = this;


        SharedPreferences prefs = getSharedPreferences("Info", MODE_PRIVATE);
        String restoredMail = prefs.getString("mail", null);
        String restoredPassword = prefs.getString("password", null);
        String restoredUrl = prefs.getString("url", null);

        editor = getSharedPreferences("Info", MODE_PRIVATE).edit();

        if (restoredMail != null) {
            Frame_Log.textmail.setText(restoredMail);
            editor.remove("mail");
        }

        if (restoredPassword != null) {
            Frame_Log.textpasw.setText(restoredPassword);
            editor.remove("password");
        }
        if (restoredUrl != null) {
            Frame_Log.texturl.setText(restoredUrl);
            editor.remove("url");
        }

        editor.commit();


        //check the internet connection
        if(!isNetworkAvailable())
        {
            Toast toast = Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 100);
            toast.show();

            // set a delay for 0.75 second
            new CountDownTimer(750, 1000) {
                public void onFinish() {
                    // When timer is finished
                    // Execute your code here
                    finish();
                }
                public void onTick(long millisUntilFinished) {
                    // millisUntilFinished    The amount of time until finished.
                }
            }.start();

        }

    }

    @Override
    public void onBackPressed() {

        if(frag2.isShown())
        {
            frag2.setVisibility(View.INVISIBLE);
            frag1.setVisibility(View.VISIBLE);
        }
        else if(firstClick)
        {
            Toast toast = Toast.makeText(mContext, "To exit press back again", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 100);
            toast.show();
            firstClick = false;
            thistime = Calendar.getInstance().getTimeInMillis();
        }
        else if( ( Calendar.getInstance().getTimeInMillis() - thistime ) <= 3000)
        {
            finish();
        }
        else
        {
            Toast toast = Toast.makeText(mContext, "Press again to exit", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 100);
            toast.show();
            firstClick = false;
            thistime = Calendar.getInstance().getTimeInMillis();
        }
    }

    public class getData extends AsyncTask<String, String, String> {
        ExchangeCredentials credentials;
        ExchangeService service;
        @Override
        protected void onPreExecute() {

                //credentials = new WebCredentials(textmail.getText().toString(), textpasw.getText().toString());
                credentials = new WebCredentials(Mail, Pass);
               //credentials = new WebCredentials("vestektoplanti2@zh.corp", "abc.1234");

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            // UI opr

            if( errorName == "Invalid")
           {
               System.out.println("Invalid input. Retype your email or password");

               //Frame_Log.tv.setText("Invalid input. Retype your email or password");
               Toast toast =  Toast.makeText(mContext, invalidInput, Toast.LENGTH_SHORT);
               toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 100);

               toast.show();

               editor.remove("mail");
               editor.remove("password");
               editor.remove("url");

               editor.commit();
           }
            else
            {
                if (!appointmentList.isEmpty())
                {
                    //show the room name on the top of the page
                    roomname2 = " " + appointmentList.get(0).location + " ";
                }
                else
                {
                    roomname2 = "Room is empty";
                }

                Frame_Info f2 = (Frame_Info) getFragmentManager().findFragmentById(R.id.fragment_info);
                f2.updateInfo(roomname2);

                frag2.setVisibility(View.VISIBLE);
                frag1.setVisibility(View.INVISIBLE);

                //adaptor for listview
                adaptor = new Adaptor(MainActivity.this, appointmentList);
                Frame_Info.Lv.setAdapter(adaptor);


                //to hide the keyboard after clicking the button
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Frame_Log.texturl.getWindowToken(), 0);
            }
            super.onPostExecute(s);

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ExchangeVersion exchangeVersion = ExchangeVersion.Exchange2010_SP2;
                ExchangeService service = new ExchangeService(exchangeVersion);

                service.setTimeout(10 * 1000);
                service.setCredentials(credentials);

                //service.setUrl(new URI("https://owa.zorlu.com/EWS/Exchange.asmx"));
                service.setUrl(new URI(Url));

                appointmentList = new ArrayList<>();

                Calendar cal = Calendar.getInstance();

                cal.setTime(new Date());

                cal.set(Calendar.HOUR_OF_DAY, 0);

                cal.set(Calendar.MINUTE, 0);

                cal.set(Calendar.SECOND, 0);

                cal.set(Calendar.MILLISECOND, 0);



                Date startDate = cal.getTime();

                cal.add(Calendar.DATE, 1);

                Date endDate = cal.getTime();


                CalendarView cView = new CalendarView(startDate, endDate);


                PropertySet prop = new PropertySet();

                cView.setPropertySet(prop);

                FolderId folderId = new FolderId(WellKnownFolderName.Calendar, new Mailbox(new EmailAddress(Mail).getAddress()));

                FindItemsResults<Appointment> findResults = service.findAppointments(folderId, cView);

                ArrayList<Appointment> calItem = findResults.getItems();

                PropertySet itemPropertySet = new PropertySet(BasePropertySet.FirstClassProperties);

                itemPropertySet.setRequestedBodyType(BodyType.Text);

                int numItems = findResults.getTotalCount();

                for (int i = 0; i < numItems; i++) {

                    Appointment Details = Appointment.bind(service, calItem.get(i).getId(), itemPropertySet);

                    calItem.get(i).load();

                    String StartDate = null;
                    String EndDate = null;
                    String Subject = null;
                    String Participants = null;
                    String Location = null;
                    String Organiser = null;

                    CustomAppointment app = new CustomAppointment(StartDate, EndDate, Subject, Participants, Location, Organiser);

                    System.out.println("SUBJECT====="+calItem.get(i).getSubject());
                    System.out.println("Location========"+calItem.get(i).getLocation());
                    System.out.println("Start Time========"+calItem.get(i).getStart());
                    System.out.println("End Time========"+calItem.get(i).getEnd());
                    System.out.println("participants======= "+ calItem.get(i).getDisplayTo());
                    System.out.println("Organiser======="+ calItem.get(i).getOrganizer().getName());

                    StartDate = calItem.get(i).getStart().toString();
                    EndDate = calItem.get(i).getEnd().toString();
                    Subject = calItem.get(i).getSubject();
                    Participants = calItem.get(i).getDisplayTo();
                    Location = calItem.get(i).getLocation();
                    Organiser = calItem.get(i).getOrganizer().getName();

                    app = new CustomAppointment(StartDate, EndDate, Subject, Participants, Location, Organiser);

                    appointmentList.add(app);
                }
                errorName = "";

            }
            catch (Exception e) {

                Log.d("main act", e + "");
                errorName = "Invalid";
                e.printStackTrace();
            }
            return null;
        }
    }

    public class CustomAppointment {
        String startDate; // appt.getStart()
        String endDate;     //appt.getEnd...
        String subject;     //appt.getSubject()
        String participants; // appt.getDisplayTo()
        String location;
        String Organiser;

        public CustomAppointment(String startDate, String endDate, String subject, String participants, String location, String Organiser) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.subject = subject;
            this.participants = participants;
            this.location = location;
            this.Organiser = Organiser;
        }
    }

    @Override
    public void setName(String mail, String password, String url) {

        Mail = mail;
        Pass = password;
        Url = url;

        new getData().execute(null, null, null);
    }

    @Override
    public void setRemember(Boolean remember)
    {
        if(remember)
        {
            String putmail, putpassword, puturl;
            putmail = Frame_Log.textmail.getText().toString();
            putpassword = Frame_Log.textpasw.getText().toString();
            puturl = Frame_Log.texturl.getText().toString();

            editor = getSharedPreferences("Info", MODE_PRIVATE).edit();
            editor.putString("mail", putmail);
            editor.putString("password", putpassword);
            editor.putString("url", puturl);
            editor.commit();
        }
        else
        {
            editor = getSharedPreferences("Info", MODE_PRIVATE).edit();
            editor.remove("mail");
            editor.remove("password");
            editor.remove("url");
            editor.commit();
        }
    }

    @Override
    public void back() {


        frag2.setVisibility(View.INVISIBLE);
        frag1.setVisibility(View.VISIBLE);


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

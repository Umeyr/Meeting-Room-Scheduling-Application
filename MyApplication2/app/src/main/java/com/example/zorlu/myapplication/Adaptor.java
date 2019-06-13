package com.example.zorlu.myapplication;

/**
 * Created by ZORLU on 26.08.2016.
 */
import android.app.Activity;
        import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.text.NumberFormat;
        import java.util.List;


public class Adaptor extends BaseAdapter {


    LayoutInflater layoutInflater;
    List<MainActivity.CustomAppointment> list;
    Activity activity;

    public Adaptor(Activity activity, List<MainActivity.CustomAppointment> mList){

        layoutInflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        list=mList;
        this.activity=activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View satirView;

        satirView=layoutInflater.inflate(R.layout.rows, null);


        TextView tvSubject=(TextView)satirView.findViewById(R.id.tvSubject);
        TextView tvParticipants=(TextView)satirView.findViewById(R.id.tvParticipants);
        TextView tvStartTime=(TextView)satirView.findViewById(R.id.tvStartTime);
        TextView tvEndTime=(TextView)satirView.findViewById(R.id.tvEndTime);
        TextView tvOrganiser=(TextView)satirView.findViewById(R.id.tvOrganiser);


        final MainActivity.CustomAppointment customappt=list.get(position);

        tvSubject.setText(Html.fromHtml("<font color='#84FFFF'>Subject: </font>" + customappt.subject));
        tvParticipants.setText(Html.fromHtml("<font color='#84FFFF'>Participants: </font>" + customappt.participants));
        tvStartTime.setText(Html.fromHtml("<font color='#84FFFF'>StartDate: </font>" + customappt.startDate));
        tvEndTime.setText(Html.fromHtml("<font color='#84FFFF'>EndDate: </font>" + customappt.endDate));
        tvOrganiser.setText(Html.fromHtml("<font color='#84FFFF'>Organiser: </font>" + customappt.Organiser));
        return satirView;
    }
}

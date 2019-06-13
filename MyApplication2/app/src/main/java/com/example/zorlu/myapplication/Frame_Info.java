package com.example.zorlu.myapplication;

/**
 * Created by ZORLU on 07.09.2016.
 */
import android.app.Activity;
import android.app.Fragment;

/**
 * Created by ZORLU on 07.09.2016.
 */
import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by ZORLU on 07.09.2016.
 */
public class Frame_Info extends Fragment {

    TextView roomname;
    static ListView Lv;
    Button bt_back;
    OnNameSetListener2 onNameSetListener2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.frame_info, container, false);


        roomname = (TextView) view.findViewById(R.id.roomname);
        Lv =(ListView) view.findViewById(R.id.lv);
        bt_back =(Button) view.findViewById(R.id.bt_back);

        bt_back.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                onNameSetListener2.back();

            }
        } );

        return view;
    }

    public void updateInfo(String Roomname)
    {
        roomname.setText(Roomname);
    }

    public interface OnNameSetListener2
    {
        public void back();
    }

    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try {
            onNameSetListener2 = (OnNameSetListener2) activity;
        }
        catch (Exception e){}

    }




}

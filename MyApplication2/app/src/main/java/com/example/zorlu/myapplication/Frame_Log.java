package com.example.zorlu.myapplication;

/**
 * Created by ZORLU on 07.09.2016.
 */
import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.org.apache.commons.lang3.BooleanUtils;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by ZORLU on 07.09.2016.
 */
public class Frame_Log extends Fragment {
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.

    static TextView tv;
    static EditText textmail, textpasw;
    static EditText texturl;
    Button bt;
    OnNameSetListener onNameSetListener;
    CheckBox checkBox, checkBox2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.frame_log, container, false);

        tv= (TextView) view.findViewById(R.id.tv);
        textmail = (EditText) view.findViewById(R.id.textmail);
        textpasw = (EditText) view.findViewById(R.id.textpasw);
        texturl= (EditText) view.findViewById(R.id.texturl);
        bt = (Button) view.findViewById(R.id.bt);
        checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        checkBox2 = (CheckBox) view.findViewById(R.id.checkBox2);

        bt.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String mail = textmail.getText().toString();
                String password = textpasw.getText().toString();
                String url = texturl.getText().toString();

                //textpasw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);


                onNameSetListener.setName(mail, password, url);

            }
        } );

        //show or hide the password
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                {
                    textpasw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                else
                {
                    textpasw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });


        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onNameSetListener.setRemember(isChecked);
            }
        });

        return view;
    }


    public interface OnNameSetListener
    {
        public void setName(String name, String password, String url);
        public void setRemember(Boolean remember);
    }

    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try {
            onNameSetListener = (OnNameSetListener) activity;
        }
        catch (Exception e){}

    }

}

package com.lead.infosystems.schooldiary;


import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lead.infosystems.schooldiary.Data.UserDataSP;
import com.lead.infosystems.schooldiary.Generic.MyVolley;
import com.lead.infosystems.schooldiary.Generic.Utils;
import com.lead.infosystems.schooldiary.Main.MainTabAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlertAll extends DialogFragment {
    public AlertAll() {}


    private View rootView;
    private Button sendBtn;
    private TextView notificationText;
    public static final String ALERT_NOTIFICATION = "ALERT_NOTIFICATION";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_alert_all, container, false);
        sendBtn = (Button) rootView.findViewById(R.id.sendBtn);
        notificationText = (TextView) rootView.findViewById(R.id.alertText);
        getDialog().setTitle("ALERT NOTIFICATION");
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notificationText.getText().toString().length() > 0){
                    MyVolley volley = new MyVolley(getActivity().getApplicationContext(), new IVolleyResponse() {
                        @Override
                        public void volleyResponse(String result) {
                            if(result.length() > 0 && result.contains("DONE")){
                                Toast.makeText(getActivity(),"DONE",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainTabAdapter.NOTIFICATION_BC_FILTER);
                                intent.setAction(ALERT_NOTIFICATION);
                                getActivity().sendBroadcast(intent);
                                getDialog().dismiss();
                            }
                        }
                    });
                    volley.setUrl(Utils.ALERT_ALL);
                    volley.setParams("alert_text",notificationText.getText().toString());
                    volley.setParams(UserDataSP.SCHOOL_NUMBER, new UserDataSP(getActivity()
                            .getApplicationContext()).getUserData(UserDataSP.SCHOOL_NUMBER));
                    volley.connect();
                }else{
                    Toast.makeText(getActivity(),"No Text..",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

}

package com.lead.infosystems.schooldiary.Suggestion;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lead.infosystems.schooldiary.Data.UserDataSP;
import com.lead.infosystems.schooldiary.Generic.MyVolley;
import com.lead.infosystems.schooldiary.Generic.Utils;
import com.lead.infosystems.schooldiary.IVolleyResponse;
import com.lead.infosystems.schooldiary.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Suggestion_Post extends DialogFragment {
    public static final String INTENTFILTER_SUGGESTION ="intent_filter_suggestion";
    public static final String SC_ID = "sc_id";
    public static final String SUGGESTER_FIRSTNAME = "first_name_suggestion";
    public static final String SUGGESTER_LASTNAME = "last_name_suggestion";
    public static final String SUGGESTER_CLASS = "class_suggestion";
    public static final String SUGGESTER_DIVISION = "division_suggestion";
    public static final String SUGGESTER_PROFILEPIC = "pic_suggestion";
    public static final String SUGGESTION_TITLE = "title_suggestion";
    public static final String SUGGESTION_CONTENT = "content_suggestion";
    public static final String SUGGESTION_DATE = "date_suggestion";
    private MyVolley myVolley;
    EditText edit_content;
    EditText edit_title;
    public Button sugg_post;
    UserDataSP userdatasp;

    View rootview;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.suggestion_post, null);

        sugg_post=(Button)rootview.findViewById(R.id.button_post);
        edit_title=(EditText)rootview.findViewById(R.id.editText_title);
        edit_content=(EditText)rootview.findViewById(R.id.editText_content);
        userdatasp=new UserDataSP(getActivity().getApplicationContext());
        getDialog().setTitle("New Suggestion");
        sugg_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Processing.....", Toast.LENGTH_LONG).show();
                 InsertData();
            }
        });
        return rootview;
    }

    private void InsertData() {
        myVolley = new MyVolley(getActivity().getApplicationContext(), new IVolleyResponse() {
            @Override
            public void volleyResponse(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String date = jsonObject.getString("date").split(" ")[0];
                    getDialog().dismiss();
                    if (!result.equals("null")){
                        parseDataSuggestion(jsonObject.getString("sc_id"),
                                userdatasp.getUserData(UserDataSP.FIRST_NAME),
                                userdatasp.getUserData(UserDataSP.LAST_NAME),
                                userdatasp.getUserData(UserDataSP.CLASS),
                                userdatasp.getUserData(UserDataSP.DIVISION),
                                userdatasp.getUserData(UserDataSP.PROPIC_URL),
                                edit_title.getText().toString(),
                                edit_content.getText().toString(), date);
                        Toast.makeText(getApplicationContext(),"Done", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        myVolley.setUrl(Utils.SUGGESTION_COMPLAIN);
        myVolley.setParams("title",edit_title.getText().toString());
        myVolley.setParams("content",edit_content.getText().toString());
        myVolley.setParams(UserDataSP.NUMBER_USER,userdatasp.getUserData(UserDataSP.NUMBER_USER));
        myVolley.setParams(UserDataSP.SCHOOL_NUMBER,userdatasp.getUserData(UserDataSP.SCHOOL_NUMBER));
        myVolley.connect();
    }

    public void parseDataSuggestion(String sc_id, String firstName, String lastName, String class_user, String division_user, String user_pic, String s_title, String s_content, String s_date)
    {
        Intent intent = new Intent(INTENTFILTER_SUGGESTION);
        intent.putExtra(SC_ID,sc_id);
        intent.putExtra(SUGGESTER_FIRSTNAME, firstName);
        intent.putExtra(SUGGESTER_LASTNAME, lastName);
        intent.putExtra(SUGGESTER_CLASS, class_user);
        intent.putExtra(SUGGESTER_DIVISION, division_user);
        intent.putExtra(SUGGESTER_PROFILEPIC, user_pic);
        intent.putExtra(SUGGESTION_TITLE, s_title);
        intent.putExtra(SUGGESTION_CONTENT, s_content);
        intent.putExtra(SUGGESTION_DATE, s_date);
        try {
            getActivity().sendBroadcast(intent);
        }catch (Exception e){
            e.printStackTrace();
        }

    }



}

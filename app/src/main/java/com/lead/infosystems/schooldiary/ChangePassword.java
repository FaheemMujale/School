package com.lead.infosystems.schooldiary;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lead.infosystems.schooldiary.Data.UserDataSP;
import com.lead.infosystems.schooldiary.Generic.MyVolley;
import com.lead.infosystems.schooldiary.Generic.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChangePassword extends AppCompatActivity {


    private UserDataSP userDataSP;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        final EditText old = (EditText) findViewById(R.id.old_password);
        final EditText newPass = (EditText) findViewById(R.id.new_pass);
        final EditText rePass = (EditText) findViewById(R.id.reenter_pass);
        Button done = (Button) findViewById(R.id.pass_done);
        userDataSP = new UserDataSP(getApplicationContext());
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = getApplicationContext();

                if(newPass.getText().toString().length() > 6) {
                    Log.e(newPass.getText().toString(),rePass.getText().toString()+":"+old.getText().toString());
                    if (newPass.getText().toString().contentEquals(rePass.getText().toString())) {

                        progressDialog.show();
                        MyVolley volley = new MyVolley(context, new IVolleyResponse() {
                            @Override
                            public void volleyResponse(String result) {
                                try {
                                    JSONArray jsonArray = new JSONArray(result);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    String oldPass = jsonObject.getString("password");
                                    if (old.getText().toString().trim().contentEquals(oldPass)) {
                                        changePassword(newPass.getText().toString());
                                    }else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),
                                                "Incorrect old Password", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        volley.setUrl(Utils.CHANGE_PASSWORD);
                        volley.setParams("number_user",userDataSP.getUserData(UserDataSP.NUMBER_USER));
                        volley.connect();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Re-Entered Password doesn't match", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Password length should me more than 6", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void changePassword(String password){
        MyVolley volley = new MyVolley(getApplicationContext(), new IVolleyResponse() {
            @Override
            public void volleyResponse(String result) {
                progressDialog.dismiss();
                if(result.contentEquals("DONE")){
                    Toast.makeText(getApplicationContext(),
                            "Done",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
        volley.setUrl(Utils.CHANGE_PASSWORD);
        volley.setParams("number_user",userDataSP.getUserData(UserDataSP.NUMBER_USER));
        volley.setParams("new_password",password);
        volley.connect();
    }
}

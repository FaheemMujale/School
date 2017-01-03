package com.lead.infosystems.schooldiary.Attendance;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lead.infosystems.schooldiary.Data.UserDataSP;
import com.lead.infosystems.schooldiary.Generic.MyVolley;
import com.lead.infosystems.schooldiary.IVolleyResponse;
import com.lead.infosystems.schooldiary.R;
import com.lead.infosystems.schooldiary.Generic.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Division extends AppCompatActivity {

    UserDataSP userDataSP;

    SPData spData;

    ListView dlist;
    String class_list;
    public static List<String> division= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_division);
        Intent intent = getIntent();
        class_list = intent.getStringExtra("class");
        dlist=(ListView)findViewById(R.id.div_list);
        userDataSP=new UserDataSP(this);
        spData =new SPData(this);
        getDivisionData();
    }

    public void getDivisionData(){
        MyVolley volley = new MyVolley(getApplicationContext(), new IVolleyResponse() {
            @Override
            public void volleyResponse(String result) {
                try {
                    getJsonData(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        volley.setUrl(Utils.ATTENDANCE);
        volley.setParams(UserDataSP.SCHOOL_NUMBER,userDataSP.getUserData(UserDataSP.SCHOOL_NUMBER));
        volley.setParams(UserDataSP.CLASS,class_list);
        volley.connect();
    }

    private void getJsonData(String re) throws JSONException {
        JSONArray json = new JSONArray(re);
        division.clear();

        for (int i = 0; i <= json.length() - 1; i++) {
            JSONObject jsonobj = json.getJSONObject(i);
            division.add(jsonobj.getString(UserDataSP.DIVISION));
        }

        dlist.setAdapter(new MyAdaptor());
        dlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(view.getContext(), Student_list.class);
                intent.putExtra(UserDataSP.DIVISION, division.get(position));
                intent.putExtra(UserDataSP.CLASS, class_list);
                startActivity(intent);

            }
        });
    }
    class MyAdaptor extends ArrayAdapter<String> {

        public MyAdaptor() {
            super(getApplicationContext(), R.layout.class_div,division);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View ItemView = convertView;
            if (ItemView == null) {
                ItemView = getLayoutInflater().inflate(R.layout.class_div, parent, false);
            }

            String div=division.get(position);
            TextView class_text=(TextView)ItemView.findViewById(R.id.class_id) ;
            class_text.setText("Division");
            ImageView img = (ImageView) ItemView.findViewById(R.id.class_image);
            String firstletter = String.valueOf(div.charAt(0));
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getColor(getItem(position));
            TextDrawable drawable = TextDrawable.builder().buildRoundRect(firstletter.toUpperCase(),color,20);
            img.setImageDrawable(drawable);
            return ItemView;

        }
    }
}

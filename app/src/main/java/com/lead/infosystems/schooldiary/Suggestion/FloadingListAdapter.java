package com.lead.infosystems.schooldiary.Suggestion;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lead.infosystems.schooldiary.Data.UserDataSP;
import com.lead.infosystems.schooldiary.Generic.MyVolley;
import com.lead.infosystems.schooldiary.Generic.Utils;
import com.lead.infosystems.schooldiary.IVolleyResponse;
import com.lead.infosystems.schooldiary.R;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Naseem on 02-01-2017.
 */

public class FloadingListAdapter extends ArrayAdapter<sc_items>{
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private UserDataSP userDataSP;
    private Context context;
    List<sc_items> list = new ArrayList<>();
    public FloadingListAdapter(Context context, List<sc_items> objects) {
        super(context,0, objects);
        this.context = context;
        userDataSP = new UserDataSP(context);
        this.list = objects;
    }


    public void sortData()
    {
        Collections.sort(list, new MyComparator());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final sc_items item = list.get(position);
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell_suggestion, parent, false);


            viewHolder.name_title = (TextView) cell.findViewById(R.id.sugg_name);
            viewHolder.delete_btn = (TextView) cell.findViewById(R.id.suggestion_delete);
            viewHolder.name_content = (TextView)cell.findViewById(R.id.sugg_name_content);
            viewHolder.sug_title_t= (TextView)cell.findViewById(R.id.sugg_title) ;
            viewHolder.class_title=(TextView)cell.findViewById(R.id.sugg_class);
            viewHolder.division_title=(TextView)cell.findViewById(R.id.sugg_division);
            viewHolder.sugg_title_c = (TextView) cell.findViewById(R.id.sugg_title_content);
            viewHolder.sugg_content = (TextView)cell.findViewById(R.id.suggestion_content);
            viewHolder.sugg_date = (TextView)cell.findViewById(R.id.sugg_date);
            viewHolder.class_content=(TextView)cell.findViewById(R.id.sugg_class_content);
            viewHolder.division_content=(TextView)cell.findViewById(R.id.sugg_division_content);
            viewHolder.title_image=(ImageView)cell.findViewById(R.id.circlecell_sugT);
            viewHolder.content_image=(ImageView)cell.findViewById(R.id.circlecell_sugC);
            cell.setTag(viewHolder);
        }
        else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (FloadingListAdapter.ViewHolder) cell.getTag();
        }

        if(userDataSP.getUserData(UserDataSP.PROPIC_URL).contentEquals(item.getProfilePic_link()) || !userDataSP.isStudent()){
            viewHolder.delete_btn.setVisibility(View.VISIBLE);
            viewHolder.delete_btn.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {


                    android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(context);
                    alert.setTitle("Alert");
                    alert.setMessage("Are you sure to delete");
                    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MyVolley volley = new MyVolley(context, new IVolleyResponse() {
                                @Override
                                public void volleyResponse(String result) {
                                    if(result.contains("DONE")){
                                        list.remove(position);
                                        notifyDataSetChanged();
                                        Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            volley.setUrl(Utils.SUGGESTION_COMPLAIN);
                            volley.setParams("sc_id",item.getSc_id());
                            volley.connect();
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();
                }
            });
        }else{
            viewHolder.delete_btn.setVisibility(View.GONE);
        }
        cell.initialize(1000, Color.WHITE, 1 + (int)item.getContent().length()/50);
        viewHolder.name_title.setText(item.getFirst_name()+" "+item.getLast_name()+"");
        viewHolder.name_content.setText(item.getFirst_name()+" "+item.getLast_name()+"");
        viewHolder.class_title.setText("Class: "+""+item.getClass_t()+"");
        viewHolder.division_title.setText("Division: "+""+item.getDivision_t()+"");
        viewHolder.sug_title_t.setText(item.getSubject()+"");
        viewHolder.sugg_title_c.setText(item.getSubject()+"");
        viewHolder.sugg_content.setText(item.getContent()+"");
        viewHolder.sugg_date.setText(item.getDate()+"");
        viewHolder.class_content.setText("Class: "+""+item.getClass_t()+"");
        viewHolder.division_content.setText("Division: "+""+item.getDivision_t()+"");
        Picasso.with(getContext()).load(Utils.SERVER_URL+item.getProfilePic_link())
                .placeholder(R.drawable.defaultpropic).into(viewHolder.title_image);
        Picasso.with(getContext()).load(Utils.SERVER_URL+item.getProfilePic_link())
                .placeholder(R.drawable.defaultpropic).into(viewHolder.content_image);


        return cell;
    }

    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    private class MyComparator implements Comparator<sc_items> {

        @Override
        public int compare(sc_items lhs, sc_items rhs) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date1 = dateFormat.parse(rhs.getDate());
                Date date2 = dateFormat.parse(lhs.getDate());
                Log.e("date1", date2.compareTo(date1) + "");
                return date1.compareTo(date2);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }


    public static class ViewHolder {
        TextView name_title;
        TextView name_content;
        TextView class_title;
        TextView division_title;
        TextView sug_title_t;
        TextView sugg_title_c;
        TextView sugg_content;
        TextView sugg_date;
        TextView class_content;
        TextView delete_btn;
        TextView division_content;
        ImageView title_image;
        ImageView content_image;

    }



}

package parentshour.spinlogics.com.parentshour.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.activity.AssitantDashBoard;
import parentshour.spinlogics.com.parentshour.models.AssistantDashboardModel;

/**
 * Created by SPINLOGICS on 12/20/2016.
 */

public class AssistantDashBoardAdapter extends RecyclerView.Adapter<AssistantDashBoardAdapter.ViewHolder> {
    Context activity;
    private ArrayList<AssistantDashboardModel> list;

    public AssistantDashBoardAdapter(ArrayList<AssistantDashboardModel> countries, Context context) {
        this.list = countries;
        this.activity = context;
    }

    @Override
    public AssistantDashBoardAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_assistant_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AssistantDashBoardAdapter.ViewHolder viewHolder, int i) {
        final int q = i;

        viewHolder.tv_title.setText(list.get(i).getTitle());
        viewHolder.tv_date.setText(list.get(i).getDate());
        viewHolder.tv_time.setText(list.get(i).getTime() + " to " + list.get(i).getEndtime());
        viewHolder.tv_status.setText(list.get(i).getStatus());
        viewHolder.tv_name.setText(list.get(i).getName());

        if (list.get(i).getStatus().equals("accept")) {
            viewHolder.iv_chat.setImageResource(R.drawable.ic_chat);
        }

        if (list.get(i).getStatus().equals("pending")) {
            viewHolder.iv_chat.setVisibility(View.INVISIBLE);
        }

        if (list.get(i).getStatus().equals("completed")) {
            viewHolder.iv_chat.setImageResource(R.drawable.ic_rateme);
        }
        Glide.with(activity)
                .load(list.get(i).getImgUrl())
                .error(R.drawable.ic_profilelogo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.iv_profile_pic);
        viewHolder.iv_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* activity.startActivity(new Intent(activity, ParentPlaySearchDateDetailViewActivity.class)
                        .putExtra("id", list.get(q).getpId()));*/
            }
        });
        viewHolder.iv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(q).getStatus().equals("completed")) {
                    ((AssitantDashBoard) activity).ratingDialog(list.get(q).getpId(),
                            list.get(q).getA_req_id(), q);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile_pic, iv_chat;
        //LinearLayout row_assistant_layout;
        private TextView tv_title, tv_date, tv_time, tv_status, tv_name;

        public ViewHolder(View view) {
            super(view);
            iv_profile_pic = (ImageView) view.findViewById(R.id.profile_pic);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_status = (TextView) view.findViewById(R.id.tv_status);
            tv_name = (TextView) view.findViewById(R.id.profile_name);
            iv_chat = (ImageView) view.findViewById(R.id.iv_chat);
            Typeface faceBold = Typeface.createFromAsset(activity.getAssets(), "Lato-Bold.ttf");
            tv_status.setTypeface(faceBold);

            Typeface faceMedium = Typeface.createFromAsset(activity.getAssets(), "Lato-Medium.ttf");
            tv_title.setTypeface(faceMedium);
            tv_date.setTypeface(faceMedium);
            tv_time.setTypeface(faceMedium);
            tv_name.setTypeface(faceMedium);
        }
    }
}

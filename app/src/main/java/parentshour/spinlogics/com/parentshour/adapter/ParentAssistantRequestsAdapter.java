package parentshour.spinlogics.com.parentshour.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.activity.ParentAssistantRequestDetail;
import parentshour.spinlogics.com.parentshour.models.ParentAssistantRequestsModel;
import parentshour.spinlogics.com.parentshour.utilities.FontStyle;

/**
 * Created by SPINLOGICS on 1/11/2017.
 */

public class ParentAssistantRequestsAdapter extends RecyclerView.Adapter<ParentAssistantRequestsAdapter.ViewHolder> {
    Context activity;
    private ArrayList<ParentAssistantRequestsModel> list;

    public ParentAssistantRequestsAdapter(ArrayList<ParentAssistantRequestsModel> countries, Context context) {
        this.list = countries;
        this.activity = context;
    }

    @Override
    public ParentAssistantRequestsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_search_assistant, viewGroup, false);

        return new ParentAssistantRequestsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParentAssistantRequestsAdapter.ViewHolder viewHolder, int i) {
        final int s = i;
        viewHolder.tv_title.setText(list.get(i).getA_task_name());
        viewHolder.tv_date.setText(list.get(i).getA_date());
        viewHolder.tv_time.setText(list.get(i).getA_start_time() + " to " + list.get(i).getA_end_time());
        viewHolder.profile_name.setText(list.get(i).getA_name());
        viewHolder.tv_status.setText(list.get(i).getA_status());
        //viewHolder.tv_title.setText(list.get(i).getA_task_name());
        viewHolder.iv_image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, ParentAssistantRequestDetail.class)
                        .putExtra("aid", list.get(s).getA_id()));
            }
        });
        viewHolder.titleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, ParentAssistantRequestDetail.class)
                        .putExtra("aid", list.get(s).getA_id()));
            }
        });
        Glide.with(activity)
                .load(list.get(i).getA_pic())
                .error(R.drawable.ic_profilelogo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.iv_profile_pic);

        FontStyle.applyfontBasedOnSelection(viewHolder.tv_title, FontStyle.Lato_Medium, activity);
        FontStyle.applyfontBasedOnSelection(viewHolder.tv_date, FontStyle.Lato_Medium, activity);
        FontStyle.applyfontBasedOnSelection(viewHolder.tv_time, FontStyle.Lato_Medium, activity);
        FontStyle.applyfontBasedOnSelection(viewHolder.profile_name, FontStyle.Lato_Medium, activity);
        FontStyle.applyfontBasedOnSelection(viewHolder.tv_status, FontStyle.Lato_Medium, activity);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile_pic, iv_rate_group;
        LinearLayout iv_image_layout, titleLayout;
        private TextView tv_title, tv_date, tv_time, tv_status, profile_name;

        public ViewHolder(View view) {
            super(view);
            iv_image_layout = (LinearLayout) view.findViewById(R.id.iv_image_layout);
            titleLayout = (LinearLayout) view.findViewById(R.id.titleLayout);
            iv_profile_pic = (ImageView) view.findViewById(R.id.profile_pic);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_status = (TextView) view.findViewById(R.id.tv_status);
            profile_name = (TextView) view.findViewById(R.id.profile_name);
            // tv_title = (TextView) view.findViewById(R.id.tv_title);

        }
    }
}

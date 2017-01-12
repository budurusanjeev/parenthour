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
import parentshour.spinlogics.com.parentshour.activity.ParentAssistantSetRequest;
import parentshour.spinlogics.com.parentshour.models.ParentSearchAssistantModel;
import parentshour.spinlogics.com.parentshour.utilities.FontStyle;

/**
 * Created by SPINLOGICS on 1/11/2017.
 */

public class ParentSearchAssistantAdapter extends RecyclerView.Adapter<ParentSearchAssistantAdapter.ViewHolder> {
    Context activity;
    private ArrayList<ParentSearchAssistantModel> list;

    public ParentSearchAssistantAdapter(ArrayList<ParentSearchAssistantModel> countries, Context context) {
        this.list = countries;
        this.activity = context;
    }

    @Override
    public ParentSearchAssistantAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_search_ass, viewGroup, false);

        return new ParentSearchAssistantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParentSearchAssistantAdapter.ViewHolder viewHolder, int i) {
        final int s = i;
        viewHolder.tv_years.setText(list.get(i).getA_experience());
        viewHolder.tv_miles.setText(list.get(i).getA_distance());
        viewHolder.profile_name.setText(list.get(i).getA_name());

        Glide.with(activity)
                .load(list.get(i).getA_pic())
                .error(R.drawable.ic_profilelogo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.iv_profile_pic);
        viewHolder.tv_rate_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, ParentAssistantSetRequest.class)
                        .putExtra("aid", list.get(s).getA_id()));
            }
        });
        viewHolder.iv_rate_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        viewHolder.titleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, ParentAssistantRequestDetail.class)
                        .putExtra("aid", list.get(s).getA_id()));
            }
        });

        viewHolder.iv_image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, ParentAssistantRequestDetail.class)
                        .putExtra("aid", list.get(s).getA_id()));
            }
        });
        FontStyle.applyfontBasedOnSelection(viewHolder.tv_years, FontStyle.Lato_Medium, activity);
        FontStyle.applyfontBasedOnSelection(viewHolder.tv_miles, FontStyle.Lato_Medium, activity);
        FontStyle.applyfontBasedOnSelection(viewHolder.tv_rate_me, FontStyle.Lato_Medium, activity);
        FontStyle.applyfontBasedOnSelection(viewHolder.profile_name, FontStyle.Lato_Medium, activity);
        // FontStyle.applyfontBasedOnSelection(viewHolder.tv_status, FontStyle.Lato_Medium, activity);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile_pic, iv_rate_group;
        LinearLayout iv_image_layout, titleLayout;
        private TextView tv_years, tv_miles, tv_rate_me, tv_status, profile_name;

        public ViewHolder(View view) {
            super(view);
            iv_profile_pic = (ImageView) view.findViewById(R.id.profile_pic);
            tv_miles = (TextView) view.findViewById(R.id.tv_miles);
            tv_years = (TextView) view.findViewById(R.id.tv_years);
            iv_rate_group = (ImageView) view.findViewById(R.id.iv_rate_me);
            tv_rate_me = (TextView) view.findViewById(R.id.tv_rate_me);
            profile_name = (TextView) view.findViewById(R.id.profile_name);
            iv_image_layout = (LinearLayout) view.findViewById(R.id.iv_image_layout);
            titleLayout = (LinearLayout) view.findViewById(R.id.titleLayout);
        }
    }
}

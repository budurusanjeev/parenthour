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
import parentshour.spinlogics.com.parentshour.activity.ParentPlaySearchDateDetailViewActivity;
import parentshour.spinlogics.com.parentshour.models.PlaySearchDateModel;
import parentshour.spinlogics.com.parentshour.utilities.FontStyle;

/**
 * Created by SPINLOGICS on 12/28/2016.
 */

public class ParentPlayDateAdapter extends RecyclerView.Adapter<ParentPlayDateAdapter.ViewHolder> {
    Context activity;
    private ArrayList<PlaySearchDateModel> list;

    public ParentPlayDateAdapter(ArrayList<PlaySearchDateModel> countries, Context context) {
        this.list = countries;
        this.activity = context;
    }

    @Override
    public ParentPlayDateAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_parent_date, viewGroup, false);

        return new ParentPlayDateAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParentPlayDateAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.tv_name.setText(list.get(i).getpName());
        viewHolder.tv_education.setText(list.get(i).getpEducation());
        viewHolder.tv_age.setText(list.get(i).getpAge());
        viewHolder.tv_ethnicity.setText(list.get(i).getpEthnicity());
        Glide.with(activity)
                .load(list.get(i).getpImageUrl())
                .error(R.drawable.ic_profilelogo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.iv_profile_pic);

        viewHolder.iv_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, ParentPlaySearchDateDetailViewActivity.class).putExtra("id", list.get(i).getpId()));
            }
        });
        viewHolder.titleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, ParentPlaySearchDateDetailViewActivity.class).putExtra("id", list.get(i).getpId()));
            }
        });


        FontStyle.applyfontBasedOnSelection(viewHolder.tv_name, FontStyle.Lato_Medium, activity);
        FontStyle.applyfontBasedOnSelection(viewHolder.tv_education, FontStyle.Lato_Medium, activity);
        FontStyle.applyfontBasedOnSelection(viewHolder.tv_age, FontStyle.Lato_Medium, activity);
        FontStyle.applyfontBasedOnSelection(viewHolder.tv_ethnicity, FontStyle.Lato_Medium, activity);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile_pic;
        LinearLayout titleLayout;
        private TextView tv_name, tv_education, tv_age, tv_ethnicity;

        public ViewHolder(View view) {
            super(view);
            iv_profile_pic = (ImageView) view.findViewById(R.id.profile_pic);
            tv_name = (TextView) view.findViewById(R.id.profile_name);
            tv_education = (TextView) view.findViewById(R.id.tv_date);
            tv_age = (TextView) view.findViewById(R.id.tv_title);
            tv_ethnicity = (TextView) view.findViewById(R.id.tv_time);
            titleLayout = (LinearLayout) view.findViewById(R.id.titleLayout);
        }
    }
}

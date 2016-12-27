package parentshour.spinlogics.com.parentshour.adapter;

import android.content.Context;
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
import parentshour.spinlogics.com.parentshour.models.PlaySearchDateModel;
import parentshour.spinlogics.com.parentshour.utilities.FontStyle;

/**
 * Created by SPINLOGICS on 12/27/2016.
 */

public class ParentSearchPlayAdapter extends RecyclerView.Adapter<ParentSearchPlayAdapter.ViewHolder> {
    Context activity;
    private ArrayList<PlaySearchDateModel> list;

    public ParentSearchPlayAdapter(ArrayList<PlaySearchDateModel> countries, Context context) {
        this.list = countries;
        this.activity = context;
    }

    @Override
    public ParentSearchPlayAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_playdate_search, viewGroup, false);

        return new ParentSearchPlayAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParentSearchPlayAdapter.ViewHolder viewHolder, int i) {

        viewHolder.tv_name.setText(list.get(i).getpName());
        viewHolder.tv_education.setText(list.get(i).getpEducation());
        viewHolder.tv_age.setText(list.get(i).getpAge());
        viewHolder.tv_ethnicity.setText(list.get(i).getpEthnicity());
        Glide.with(activity)
                .load(list.get(i).getpImageUrl())
                .error(R.drawable.ic_profilelogo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.iv_profile_pic);

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
        private TextView tv_name, tv_education, tv_age, tv_ethnicity;

        public ViewHolder(View view) {
            super(view);
            iv_profile_pic = (ImageView) view.findViewById(R.id.profile_pic);
            tv_name = (TextView) view.findViewById(R.id.profile_name);
            tv_education = (TextView) view.findViewById(R.id.tv_date);
            tv_age = (TextView) view.findViewById(R.id.tv_title);
            tv_ethnicity = (TextView) view.findViewById(R.id.tv_time);
            //tv_name = (TextView) view.findViewById(R.id.profile_name);
            /*
            tv_status = (TextView) view.findViewById(tv_status);
            tv_name = (TextView) view.findViewById(R.id.profile_name);*/
            //  row_assistant_layout = (LinearLayout) view.findViewById(R.id.row_assistant_layout);
            // FontStyle.applyFont(activity, row_assistant_layout, FontStyle.Lato_Medium);

        }
    }
}

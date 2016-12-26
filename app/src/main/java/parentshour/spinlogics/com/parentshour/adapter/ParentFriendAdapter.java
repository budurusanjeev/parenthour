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
import parentshour.spinlogics.com.parentshour.models.ParentFriendModel;
import parentshour.spinlogics.com.parentshour.utilities.FontStyle;

/**
 * Created by SPINLOGICS on 12/26/2016.
 */

public class ParentFriendAdapter extends RecyclerView.Adapter<ParentFriendAdapter.ViewHolder> {
    Context activity;
    private ArrayList<ParentFriendModel> list;

    public ParentFriendAdapter(ArrayList<ParentFriendModel> countries, Context context) {
        this.list = countries;
        this.activity = context;
    }

    @Override
    public ParentFriendAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_assistant_dashboard, viewGroup, false);

        return new ParentFriendAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParentFriendAdapter.ViewHolder viewHolder, int i) {

        viewHolder.tv_title.setText(list.get(i).getpName());
        Glide.with(activity)
                .load(list.get(i).getpImgUrl())
                .error(R.drawable.ic_addprofile)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.iv_profile_pic);

        FontStyle.applyfontBasedOnSelection(viewHolder.tv_title, FontStyle.Lato_Medium, activity);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile_pic;
        private TextView tv_title;

        public ViewHolder(View view) {
            super(view);
            iv_profile_pic = (ImageView) view.findViewById(R.id.profile_pic);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
           /* tv_date = (TextView) view.findViewById(tv_date);
            tv_time = (TextView) view.findViewById(tv_time);
            tv_status = (TextView) view.findViewById(tv_status);
            tv_name = (TextView) view.findViewById(R.id.profile_name);*/
            //  row_assistant_layout = (LinearLayout) view.findViewById(R.id.row_assistant_layout);
            // FontStyle.applyFont(activity, row_assistant_layout, FontStyle.Lato_Medium);

        }
    }
}

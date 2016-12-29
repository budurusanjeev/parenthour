package parentshour.spinlogics.com.parentshour.adapter;

import android.content.Context;
import android.content.Intent;
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
import parentshour.spinlogics.com.parentshour.activity.ParentAddGroup;
import parentshour.spinlogics.com.parentshour.models.ParentFriendModel;
import parentshour.spinlogics.com.parentshour.utilities.FontStyle;

/**
 * Created by SPINLOGICS on 12/27/2016.
 */

public class ParentGroupAdapter extends RecyclerView.Adapter<ParentGroupAdapter.ViewHolder> {
    Context activity;
    private ArrayList<ParentFriendModel> list;

    public ParentGroupAdapter(ArrayList<ParentFriendModel> countries, Context context) {
        this.list = countries;
        this.activity = context;
    }

    @Override
    public ParentGroupAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_parent_groups, viewGroup, false);

        return new ParentGroupAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParentGroupAdapter.ViewHolder viewHolder, int i) {
        final int s = i;
        viewHolder.tv_title.setText(list.get(i).getpName());
        Glide.with(activity)
                .load(list.get(i).getpImgUrl())
                .error(R.drawable.ic_profilelogo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.iv_profile_pic);

        FontStyle.applyfontBasedOnSelection(viewHolder.tv_title, FontStyle.Lato_Medium, activity);
        viewHolder.iv_edit_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, ParentAddGroup.class)
                        .putExtra("groupId", list.get(s).getpId()));
            }
        });
        viewHolder.iv_chat_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, ParentAddGroup.class)
                        .putExtra("groupId", list.get(s).getpId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile_pic, iv_edit_group, iv_chat_history;
        private TextView tv_title;

        public ViewHolder(View view) {
            super(view);
            iv_profile_pic = (ImageView) view.findViewById(R.id.profile_pic);
            tv_title = (TextView) view.findViewById(R.id.tv_name);
            iv_edit_group = (ImageView) view.findViewById(R.id.tv_edit_group);
            iv_chat_history = (ImageView) view.findViewById(R.id.tv_chat_friend);
        }
    }
}

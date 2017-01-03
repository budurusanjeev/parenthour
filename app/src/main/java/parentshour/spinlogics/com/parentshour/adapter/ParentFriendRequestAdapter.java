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
import parentshour.spinlogics.com.parentshour.activity.ParentFriendRequestActivity;
import parentshour.spinlogics.com.parentshour.models.ParentFriendModel;
import parentshour.spinlogics.com.parentshour.utilities.FontStyle;

/**
 * Created by SPINLOGICS on 12/30/2016.
 */

public class ParentFriendRequestAdapter extends RecyclerView.Adapter<ParentFriendRequestAdapter.ViewHolder> {
    Context activity;
    private ArrayList<ParentFriendModel> list;

    public ParentFriendRequestAdapter(ArrayList<ParentFriendModel> countries, Context context) {
        this.list = countries;
        this.activity = context;
    }

    @Override
    public ParentFriendRequestAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_friend_request, viewGroup, false);

        return new ParentFriendRequestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParentFriendRequestAdapter.ViewHolder viewHolder, int i) {

        final int s = i;
        viewHolder.tv_title.setText(list.get(i).getpName());
        Glide.with(activity)
                .load(list.get(i).getpImgUrl())
                .error(R.drawable.ic_profilelogo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.iv_profile_pic);
        viewHolder.tv_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ParentFriendRequestActivity) activity).acceptFriend(list.get(s).getpId());
            }
        });

        viewHolder.tv_ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ParentFriendRequestActivity) activity).ignoreFriend(list.get(s).getpId());
            }
        });

        viewHolder.tv_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ParentFriendRequestActivity) activity).blockFriend(list.get(s).getpId());
            }
        });
        FontStyle.applyfontBasedOnSelection(viewHolder.tv_title, FontStyle.Lato_Medium, activity);
        FontStyle.applyfontBasedOnSelection(viewHolder.tv_accept, FontStyle.Lato_Medium, activity);
        FontStyle.applyfontBasedOnSelection(viewHolder.tv_ignore, FontStyle.Lato_Medium, activity);
        FontStyle.applyfontBasedOnSelection(viewHolder.tv_block, FontStyle.Lato_Medium, activity);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile_pic;
        private TextView tv_title, tv_accept, tv_ignore, tv_block;

        public ViewHolder(View view) {
            super(view);
            iv_profile_pic = (ImageView) view.findViewById(R.id.profile_img);
            tv_title = (TextView) view.findViewById(R.id.tv_name);
            tv_accept = (TextView) view.findViewById(R.id.tv_accept);
            tv_ignore = (TextView) view.findViewById(R.id.tv_igonre);
            tv_block = (TextView) view.findViewById(R.id.tv_block);
        }
    }
}

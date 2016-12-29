package parentshour.spinlogics.com.parentshour.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.models.PlaySearchDateModel;
import parentshour.spinlogics.com.parentshour.utilities.FontStyle;

/**
 * Created by SPINLOGICS on 12/29/2016.
 */

public class ParentAddFriendAdapter extends RecyclerView.Adapter<ParentAddFriendAdapter.ViewHolder> {
    private Context activity;
    private ArrayList<PlaySearchDateModel> list;

    public ParentAddFriendAdapter(ArrayList<PlaySearchDateModel> countries, Context context) {
        this.list = countries;
        this.activity = context;
    }

    @Override
    public ParentAddFriendAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_add_friend_layout, viewGroup, false);

        return new ParentAddFriendAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParentAddFriendAdapter.ViewHolder viewHolder, int i) {
        // final int s = i;
        viewHolder.tv_name.setText(list.get(i).getpName());

        Glide.with(activity)
                .load(list.get(i).getpImageUrl())
                .error(R.drawable.ic_profilelogo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.iv_profile_pic);

        FontStyle.applyfontBasedOnSelection(viewHolder.tv_name, FontStyle.Lato_Medium, activity);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile_pic;
        CheckBox cb_friend;
        private TextView tv_name;

        public ViewHolder(View view) {
            super(view);
            iv_profile_pic = (ImageView) view.findViewById(R.id.profile_pic);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            cb_friend = (CheckBox) view.findViewById(R.id.tv_chat_friend);

        }
    }
}

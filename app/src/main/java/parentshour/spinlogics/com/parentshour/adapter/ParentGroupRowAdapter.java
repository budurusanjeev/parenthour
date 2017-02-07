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
import parentshour.spinlogics.com.parentshour.activity.ParentEventCreation;
import parentshour.spinlogics.com.parentshour.activity.ParentPlaySearchDateDetailViewActivity;
import parentshour.spinlogics.com.parentshour.models.ParentFriendModel;
import parentshour.spinlogics.com.parentshour.utilities.FontStyle;

/**
 * Created by SPINLOGICS on 1/3/2017.
 */

public class ParentGroupRowAdapter extends RecyclerView.Adapter<ParentGroupRowAdapter.ViewHolder> {
    Context activity;
    String typeName;
    private ArrayList<ParentFriendModel> list;

    public ParentGroupRowAdapter(ArrayList<ParentFriendModel> countries, Context context, String type) {
        this.list = countries;
        this.activity = context;
        this.typeName = type;
    }

    @Override
    public ParentGroupRowAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_group_parent, viewGroup, false);

        return new ParentGroupRowAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ParentGroupRowAdapter.ViewHolder viewHolder, int i) {
        final int s = i;
        viewHolder.tv_title.setText(list.get(i).getpName());
        Glide.with(activity)
                .load(list.get(i).getpImgUrl())
                .error(R.drawable.ic_profilelogo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.iv_profile);
        viewHolder.iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, ParentPlaySearchDateDetailViewActivity.class)
                        .putExtra("id", list.get(s).getpId()));
            }
        });
        FontStyle.applyfontBasedOnSelection(viewHolder.tv_title, FontStyle.Lato_Medium, activity);
        viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (typeName.equals("group")) {
                    ((ParentAddGroup) activity).removeFriend(viewHolder.getAdapterPosition());
                } else {
                    ((ParentEventCreation) activity).removeFriend(viewHolder.getAdapterPosition());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;
        private ImageView iv_profile, iv_delete;
        public ViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_name);
            iv_profile = (ImageView) view.findViewById(R.id.iv_profile);
            iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
        }
    }
}

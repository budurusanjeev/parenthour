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
import parentshour.spinlogics.com.parentshour.activity.AssistantNotificationsActivity;
import parentshour.spinlogics.com.parentshour.models.AssistantDashboardModel;

/**
 * Created by SPINLOGICS on 1/12/2017.
 */

public class AssistantNotificationAdapter extends RecyclerView.Adapter<AssistantNotificationAdapter.ViewHolder> {
    Context activity;
    private ArrayList<AssistantDashboardModel> list;

    public AssistantNotificationAdapter(ArrayList<AssistantDashboardModel> countries, Context context) {
        this.list = countries;
        this.activity = context;
    }

    @Override
    public AssistantNotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_assistant_notification, viewGroup, false);

        return new AssistantNotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AssistantNotificationAdapter.ViewHolder viewHolder, int i) {

        final int q = i;
        final int e = viewHolder.getAdapterPosition();
        viewHolder.tv_title.setText(list.get(i).getTitle());
        viewHolder.tv_date.setText(list.get(i).getDate());
        viewHolder.tv_time.setText(list.get(i).getTime() + " to " + list.get(i).getEndtime());
        viewHolder.tv_name.setText(list.get(i).getName());
        Glide.with(activity)
                .load(list.get(i).getImgUrl())
                .error(R.drawable.ic_profilelogo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.iv_profile_pic);

        viewHolder.tv_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AssistantNotificationsActivity) activity).acceptAssistant(list.get(q).getA_req_id(), e);
            }
        });

        viewHolder.tv_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((AssistantNotificationsActivity) activity).rejectAssistant(list.get(q).getA_req_id(), e);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile_pic;
        //LinearLayout row_assistant_layout;
        private TextView tv_title, tv_date, tv_time, tv_accept, tv_reject, tv_name;

        public ViewHolder(View view) {
            super(view);
            iv_profile_pic = (ImageView) view.findViewById(R.id.profile_pic);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_name = (TextView) view.findViewById(R.id.profile_name);
            tv_accept = (TextView) view.findViewById(R.id.tv_accept);
            tv_reject = (TextView) view.findViewById(R.id.tv_reject);

            Typeface faceMedium = Typeface.createFromAsset(activity.getAssets(), "Lato-Medium.ttf");
            tv_title.setTypeface(faceMedium);
            tv_date.setTypeface(faceMedium);
            tv_time.setTypeface(faceMedium);
            tv_accept.setTypeface(faceMedium);
            tv_reject.setTypeface(faceMedium);
            tv_name.setTypeface(faceMedium);
        }
    }
}

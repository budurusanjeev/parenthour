package parentshour.spinlogics.com.parentshour.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.models.AssistantDashboardModel;
import parentshour.spinlogics.com.parentshour.utilities.FontStyle;

/**
 * Created by SPINLOGICS on 12/20/2016.
 */

public class AssistantDashBoardAdapter extends RecyclerView.Adapter<AssistantDashBoardAdapter.ViewHolder> {
    Context activity;
    private ArrayList<AssistantDashboardModel> list;

    public AssistantDashBoardAdapter(ArrayList<AssistantDashboardModel> countries, Context context) {
        this.list = countries;
        this.activity = context;
    }

    @Override
    public AssistantDashBoardAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_assistant_dashboard, viewGroup, false);
     /*   //iv_profile_pic = (ImageView) view.findViewById(R.id.profile_pic);
        TextView  tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_date = (TextView) view.findViewById(R.id.tv_date);
        TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
        TextView tv_status = (TextView) view.findViewById(R.id.tv_status);
        TextView tv_name = (TextView) view.findViewById(R.id.profile_name);
     */ /*  FontStyle.applyfontBasedOnSelection(tv_title,FontStyle.Lato_Medium,activity);
        FontStyle.applyfontBasedOnSelection(tv_date,FontStyle.Lato_Medium,activity);
        FontStyle.applyfontBasedOnSelection(tv_time,FontStyle.Lato_Medium,activity);
        FontStyle.applyfontBasedOnSelection(tv_status,FontStyle.Lato_Medium,activity);
        FontStyle.applyfontBasedOnSelection(tv_name,FontStyle.Lato_Medium,activity);
*/
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AssistantDashBoardAdapter.ViewHolder viewHolder, int i) {

        viewHolder.tv_title.setText(list.get(i).getTitle());
        viewHolder.tv_date.setText(list.get(i).getDate());
        viewHolder.tv_time.setText(list.get(i).getTime());
        viewHolder.tv_status.setText(list.get(i).getStatus());
        viewHolder.tv_name.setText(list.get(i).getName());
        FontStyle.applyfontBasedOnSelection(viewHolder.tv_title, FontStyle.Lato_Medium, activity);
        FontStyle.applyfontBasedOnSelection(viewHolder.tv_date, FontStyle.Lato_Medium, activity);
        FontStyle.applyfontBasedOnSelection(viewHolder.tv_time, FontStyle.Lato_Medium, activity);
        FontStyle.applyfontBasedOnSelection(viewHolder.tv_status, FontStyle.Lato_Medium, activity);
        FontStyle.applyfontBasedOnSelection(viewHolder.tv_name, FontStyle.Lato_Medium, activity);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile_pic;
        //LinearLayout row_assistant_layout;
        private TextView tv_title, tv_date, tv_time, tv_status, tv_name;

        public ViewHolder(View view) {
            super(view);
            iv_profile_pic = (ImageView) view.findViewById(R.id.profile_pic);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_status = (TextView) view.findViewById(R.id.tv_status);
            tv_name = (TextView) view.findViewById(R.id.profile_name);
            //  row_assistant_layout = (LinearLayout) view.findViewById(R.id.row_assistant_layout);
            // FontStyle.applyFont(activity, row_assistant_layout, FontStyle.Lato_Medium);

        }
    }
}

package parentshour.spinlogics.com.parentshour.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.models.ParentFriendModel;
import parentshour.spinlogics.com.parentshour.utilities.FontStyle;

/**
 * Created by SPINLOGICS on 1/3/2017.
 */

public class ParentGroupRowAdapter extends RecyclerView.Adapter<ParentGroupRowAdapter.ViewHolder> {
    Context activity;
    private ArrayList<ParentFriendModel> list;

    public ParentGroupRowAdapter(ArrayList<ParentFriendModel> countries, Context context) {
        this.list = countries;
        this.activity = context;
    }

    @Override
    public ParentGroupRowAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_group_parent, viewGroup, false);

        return new ParentGroupRowAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParentGroupRowAdapter.ViewHolder viewHolder, int i) {

        viewHolder.tv_title.setText(list.get(i).getpName());

        FontStyle.applyfontBasedOnSelection(viewHolder.tv_title, FontStyle.Lato_Medium, activity);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;

        public ViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_name);
        }
    }
}

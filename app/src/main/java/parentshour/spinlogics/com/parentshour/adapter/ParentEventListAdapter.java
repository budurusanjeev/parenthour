package parentshour.spinlogics.com.parentshour.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import parentshour.spinlogics.com.parentshour.models.PlayDateEventModelParcel;

/**
 * Created by SPINLOGICS on 1/2/2017.
 */

public class ParentEventListAdapter extends RecyclerView.Adapter {

    private ArrayList<PlayDateEventModelParcel> chipsArray;

    public ParentEventListAdapter(ArrayList<PlayDateEventModelParcel> chipsArray) {
        this.chipsArray = chipsArray;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_assistant_dashboard, parent, false);

        return new ParentEventListAdapter.ChipsViewHolder(new ParentRowChips(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((ParentRowChips) holder.itemView).setAdapter(chipsArray.get(position), new ParentChipsAdapter(chipsArray.get(position).getPlayDateMembers()));
    }

    @Override
    public int getItemCount() {
        return chipsArray.size();
    }

    private class ChipsViewHolder extends RecyclerView.ViewHolder {

        public ChipsViewHolder(View itemView) {
            super(itemView);
        }
    }
}

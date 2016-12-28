package parentshour.spinlogics.com.parentshour.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import parentshour.spinlogics.com.parentshour.models.PlayDateEventsModel;

public class ChipsAdapter extends RecyclerView.Adapter {

    private ArrayList<PlayDateEventsModel> chipsArray;

    public ChipsAdapter(ArrayList<PlayDateEventsModel> chipsArray) {
        this.chipsArray = chipsArray;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChipViewHolder(new ChipView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((ChipView) holder.itemView).displayItem(chipsArray.get(position));
    }

    @Override
    public int getItemCount() {
        return chipsArray.size();
    }

    private class ChipViewHolder extends RecyclerView.ViewHolder {

        public ChipViewHolder(View itemView) {
            super(itemView);
        }
    }
}
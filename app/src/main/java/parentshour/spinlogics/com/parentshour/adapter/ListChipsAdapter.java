package parentshour.spinlogics.com.parentshour.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import parentshour.spinlogics.com.parentshour.models.PlayDateEventsModel;

public class ListChipsAdapter extends RecyclerView.Adapter {

    private ArrayList<PlayDateEventsModel> chipsArray;
    private String namerow;

    public ListChipsAdapter(ArrayList<PlayDateEventsModel> chipsArray, String name) {
        this.chipsArray = chipsArray;
        this.namerow = name;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_assistant_dashboard, parent, false);

        return new ChipsViewHolder(new RowChipsView(parent.getContext(), namerow));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((RowChipsView) holder.itemView).setAdapter(chipsArray.get(position), new ChipsAdapter(chipsArray.get(position).getPlayDateMembers()));
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
package parentshour.spinlogics.com.parentshour.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import parentshour.spinlogics.com.parentshour.models.PlayDateEventModelParcel;

/**
 * Created by SPINLOGICS on 1/2/2017.
 */

public class ParentPlayDateEventAdapter extends RecyclerView.Adapter {

    private ArrayList<PlayDateEventModelParcel> chipsArray;

    public ParentPlayDateEventAdapter(ArrayList<PlayDateEventModelParcel> chipsArray) {
        this.chipsArray = chipsArray;
        // Log.v("zzz","zzz zzz "+chipsArray.get(0).getPlayDateMembers().size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ParentPlayDateEventAdapter.ChipsViewHolder(new ParentChipsView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((ParentChipsView) holder.itemView).setAdapter(chipsArray.get(position),
                new ParentChipsAdapter(chipsArray.get(position).getPlayDateMembers()), position);
    }

    @Override
    public int getItemCount() {
        return chipsArray.size();
    }

    public class ChipsViewHolder extends RecyclerView.ViewHolder {

        public ChipsViewHolder(View itemView) {
            super(itemView);
        }
    }
}

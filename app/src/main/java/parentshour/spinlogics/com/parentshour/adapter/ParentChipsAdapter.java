package parentshour.spinlogics.com.parentshour.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import parentshour.spinlogics.com.parentshour.models.ParentFriendModel;

/**
 * Created by SPINLOGICS on 1/2/2017.
 */

public class ParentChipsAdapter extends RecyclerView.Adapter {

    private ArrayList<ParentFriendModel> chipsArray;

    public ParentChipsAdapter(ArrayList<ParentFriendModel> chipsArray) {
        this.chipsArray = chipsArray;
        Log.v("zzz", "zzz zzz zzz" + chipsArray.get(0).getpName());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ParentChipsAdapter.ChipViewHolder(new ParentView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((ParentView) holder.itemView).displayItem(chipsArray.get(position));
    }

    @Override
    public int getItemCount() {
        if (chipsArray != null) {
            return chipsArray.size();
        }
        return 0;
    }

    class ChipViewHolder extends RecyclerView.ViewHolder {

        public ChipViewHolder(View itemView) {
            super(itemView);
        }
    }
}

package parentshour.spinlogics.com.parentshour.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.models.PlayDateEventModelParcel;

/**
 * Created by SPINLOGICS on 1/2/2017.
 */

public class ParentRowChips extends FrameLayout {

    public ParentRowChips(Context context) {
        super(context);
        initializeView(context);
    }

    private void initializeView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.row_horizontal_recyclerview, this);
        ((RecyclerView) findViewById(R.id.recyclerViewHorizontal)).setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    }

    public void setAdapter(PlayDateEventModelParcel playDateEventsModel, ParentChipsAdapter adapter) {

        //((TextView)findViewById(R.id.chipTextView)).setText(playDateEventsModel.getName());
        ((TextView) findViewById(R.id.tv_date)).setText(playDateEventsModel.getDate());
        ((TextView) findViewById(R.id.tv_time)).setText(playDateEventsModel.getTime());
        ((RecyclerView) findViewById(R.id.recyclerViewHorizontal)).setAdapter(adapter);
    }

}

package parentshour.spinlogics.com.parentshour.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.activity.ParentEventCreation;
import parentshour.spinlogics.com.parentshour.activity.ParentPlayDateEvents;
import parentshour.spinlogics.com.parentshour.models.PlayDateEventsModel;

public class RowChipsView extends FrameLayout {
    Context contexts;

    public RowChipsView(Context context, String nameRow) {
        super(context);
        initializeView(context, nameRow);
        this.contexts = context;
    }

    private void initializeView(Context context, String name) {
        LayoutInflater.from(context).inflate(R.layout.row_horizontal_recyclerview, this);
        if (name.equals("events")) {
            findViewById(R.id.tv_yes).setVisibility(GONE);
            findViewById(R.id.tv_no).setVisibility(GONE);
            findViewById(R.id.tv_attend).setVisibility(GONE);

        }
        /*else
        {

        }*/
        ((RecyclerView) findViewById(R.id.recyclerViewHorizontal)).setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    }

    public void setAdapter(final PlayDateEventsModel playDateEventsModel, final int pos, ChipsAdapter adapter) {

        ((TextView) findViewById(R.id.tv_date)).setText(playDateEventsModel.getDate());
        ((TextView) findViewById(R.id.tv_time)).setText(playDateEventsModel.getTime());
        (findViewById(R.id.tv_yes)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ParentPlayDateEvents) contexts).acceptRequest(playDateEventsModel.getpEid());
                ((ParentPlayDateEvents) contexts).removeItem(pos);
            }
        });

        (findViewById(R.id.tv_no)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ParentPlayDateEvents) contexts).rejectRequest(playDateEventsModel.getpEid());
                ((ParentPlayDateEvents) contexts).removeItem(pos);
            }
        });
        (findViewById(R.id.iv_location)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://maps.google.com/maps?daddr=" + playDateEventsModel.getpAddress();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                contexts.startActivity(intent);
            }
        });
        if (playDateEventsModel.getPe_edit().equals("1")) {
            findViewById(R.id.iv_edit_events).setVisibility(VISIBLE);
            findViewById(R.id.iv_edit_events).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    contexts.startActivity(new Intent(contexts, ParentEventCreation.class)
                            .putExtra("eventId", playDateEventsModel.getpEid()));
                }
            });
        } else {
            findViewById(R.id.iv_edit_events).setVisibility(GONE);
        }

        ((RecyclerView) findViewById(R.id.recyclerViewHorizontal)).setAdapter(adapter);
    }
}
package parentshour.spinlogics.com.parentshour.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.models.PlayDateEventsModel;

public class ChipView extends FrameLayout {
    Context context;

    public ChipView(Context context) {
        super(context);
        this.context = context;
        initializeView(context);
    }

    private void initializeView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.row_horizontal, this);
    }

    public void displayItem(final PlayDateEventsModel playDateEventsModel) {
        TextView name = (TextView) findViewById(R.id.chipTextView);
        name.setText(playDateEventsModel.getName());
        ImageView imageView = (ImageView) findViewById(R.id.profile_img);
        Glide.with(context).load(playDateEventsModel.getImgurl())
                .thumbnail(0.5f).error(R.drawable.ic_profilelogo)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "clicked:  " + playDateEventsModel.getpId(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
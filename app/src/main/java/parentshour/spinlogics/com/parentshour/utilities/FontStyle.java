package parentshour.spinlogics.com.parentshour.utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by User ic_on 24-Mar-16.
 */
public class FontStyle {
    Context context;

    public static String Lato_Regular = "Lato-Regular.ttf";
    public static String Lato_Bold = "Lato-Bold.ttf";
    public static String Lato_Medium = "Lato-Medium.ttf";

    public Typeface typeFace_AmTypeWriter;

    public FontStyle(Context context) {
        this.context = context;
    }


    public void applyfontBasedOnSelection(View view, String type) {
        if (type.equalsIgnoreCase(Lato_Regular)) {
            typeFace_AmTypeWriter = Typeface.createFromAsset(context.getAssets(), "Lato-Regular.ttf");

            ((TextView) view).setTypeface(typeFace_AmTypeWriter);
        } else if (type.equalsIgnoreCase(Lato_Bold)) {
            typeFace_AmTypeWriter = Typeface.createFromAsset(context.getAssets(), "Lato-Bold.ttf");

            ((TextView) view).setTypeface(typeFace_AmTypeWriter);
        } else if (type.equalsIgnoreCase(Lato_Medium)) {
            typeFace_AmTypeWriter = Typeface.createFromAsset(context.getAssets(), "Lato-Medium.ttf");

            ((TextView) view).setTypeface(typeFace_AmTypeWriter);
        }

    }
    public static void applyFont(final Context context, final View root, final String fontName) {
        try {

            if (root instanceof ViewGroup) {

                ViewGroup viewGroup = (ViewGroup) root;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {

                    applyFont(context, viewGroup.getChildAt(i), fontName);
                }
            }
            else if (root instanceof TextView)
            {
                ((TextView)root).setTypeface(Typeface.createFromAsset(context.getAssets(),fontName));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,"Error occured ");

        }
    }

    /*public void applyfontToGroup(ViewGroup viewGroup, String type) {

        if (type.equalsIgnoreCase(openSansRegular)) {

            typeFace_AmTypeWriter = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");

            setFont(viewGroup, typeFace_AmTypeWriter);
        } else if (type.equalsIgnoreCase(opensanscondensedBold)) {

            typeFace_AmTypeWriter = Typeface.createFromAsset(context.getAssets(), "OpenSans-CondBold.ttf");

            setFont(viewGroup, typeFace_AmTypeWriter);
        } else if (type.equalsIgnoreCase(BLENDE_SCRIPT)) {

            typeFace_AmTypeWriter = Typeface.createFromAsset(context.getAssets(), "BLENDA SCRIPT.OTF");

            setFont(viewGroup, typeFace_AmTypeWriter);
        }
    }*/

 /*   public void applyfontToGroup(ViewGroup viewGroup) {


        typeFace_AmTypeWriter = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");

        setFont(viewGroup, typeFace_AmTypeWriter);
    }

    public void applyfontToView(View view) {
        typeFace_AmTypeWriter = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");

        ((TextView) view).setTypeface(typeFace_AmTypeWriter);
    }*/

    public void setFont(ViewGroup group, Typeface font) {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(font);
            } else if (v instanceof ViewGroup)
                setFont((ViewGroup) v, font);
        }
    }
}

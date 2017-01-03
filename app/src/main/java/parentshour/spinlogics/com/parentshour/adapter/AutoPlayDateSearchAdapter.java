package parentshour.spinlogics.com.parentshour.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.models.PlaySearchDateModel;

/**
 * Created by SPINLOGICS on 1/2/2017.
 */

public class AutoPlayDateSearchAdapter extends ArrayAdapter<PlaySearchDateModel> {

    Context context;
    ArrayList<PlaySearchDateModel> items, suggestions, tempItems;
    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((PlaySearchDateModel) resultValue).getpName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (PlaySearchDateModel people : tempItems) {
                    if (people.getpName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<PlaySearchDateModel> filterList = (ArrayList<PlaySearchDateModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (PlaySearchDateModel people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };

    public AutoPlayDateSearchAdapter(Context context, int resource, ArrayList<PlaySearchDateModel> items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
        suggestions = new ArrayList<PlaySearchDateModel>();
        tempItems = new ArrayList<PlaySearchDateModel>(items);
//        Log.v("name ","name: "+items.get(0).getpName());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_auto_complete, parent, false);
        }
        PlaySearchDateModel people = items.get(position);
        TextView lblName = (TextView) view.findViewById(R.id.tv_searchWord);
        if (lblName != null)
            lblName.setText(people.getpName());
        Log.v("name ", "name: " + people.getpName());
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

}

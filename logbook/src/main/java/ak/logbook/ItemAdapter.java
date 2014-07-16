package ak.logbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter {
	private ArrayList<?> ArItems;
	private int listrawlayout;
	private Context context;

	public ItemAdapter(Context cntxt, int textViewResourceId, ArrayList<?> items) {
		super(cntxt, textViewResourceId, items);
		ArItems = items;
		listrawlayout = textViewResourceId;
		context = cntxt;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Item itm = null;
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(listrawlayout, null);
		}
		String className = ArItems.get(position).getClass().getName();

        if (className.equals("ak.logbook.Car")) {
			itm = (Car) ArItems.get(position);
		} else if (className.equals("ak.logbook.Part")) {
			itm = (Part) ArItems.get(position);
		} else if (className.equals("ak.logbook.Currency")) {
			itm = (Currency) ArItems.get(position);
		} else if (className.equals("ak.logbook.Provider")) {
			itm = (Provider) ArItems.get(position);
		} else if (className.equals("ak.logbook.Category")) {
			itm = (Category) ArItems.get(position);
		} else if (className.equals("ak.logbook.Catalog")) {
			itm = (Catalog) ArItems.get(position);
		}

        if (itm != null) {
			v = itm.inflateView(v);
		}
		return v;
	}
}
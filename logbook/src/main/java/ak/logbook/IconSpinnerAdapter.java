package ak.logbook;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Drew on 15.01.14.
 */
public class IconSpinnerAdapter extends ArrayAdapter {
    private ArrayList<IconsEnum> icons;
    private ArrayList<ContentValues> cvs;
    private Context context;
    private int rawRes;
    private IconicFontDrawable drawable;


    public IconSpinnerAdapter(Context cntxt, int textViewResourceId, ArrayList<ContentValues> array) {
        super(cntxt, textViewResourceId, array);
        context = cntxt;
        icons = Icon.getArrayList();
        cvs = array;
        rawRes = textViewResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }


    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ContentValues cv = cvs.get(position);
        drawable = new IconicFontDrawable(context, icons.get(cv.getAsInteger(Main.CVicon)));
        if (cv.containsKey(Main.CVcolor))
            drawable.setIconColor(cv.getAsInteger(Main.CVcolor));
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(rawRes, parent, false);
        }
        View icon = v.findViewById(R.id.view_icon);
        TextView txt = (TextView) v.findViewById(R.id.txtTitle);
        if (icon != null) {
            icon.setBackgroundDrawable(drawable);
        }
        if (txt != null) {
            txt.setText(cv.getAsString(Main.CVname));
        }
        return v;
    }
}


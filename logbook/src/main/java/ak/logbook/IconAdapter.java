package ak.logbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Drew on 02.06.13.
 */
public class IconAdapter extends ArrayAdapter {
    private ArrayList<IconsEnum> icons;
    Context context;
    int rawRes;
    IconicFontDrawable drawable;


    public IconAdapter(Context cntxt, int textViewResourceId, ArrayList <IconsEnum> icns) {
        super(cntxt, textViewResourceId, icns);
        context = cntxt;
        icons = icns;
        rawRes = textViewResourceId;
//        drawable = new IconicFontDrawable(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }


    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        drawable = new IconicFontDrawable(context, icons.get(position));
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(rawRes, parent, false);
        }
        View icon=v.findViewById(R.id.view_l_icon);
        if (icon != null){
            icon.setBackgroundDrawable(drawable);
        }
//        if (Main.enableLog) Log.w("ak.Log", "icons.get("+position+")= " + icons.get(position)+","
//                +icons.get(position).getIconUtfValue());

        return v;
    }


}
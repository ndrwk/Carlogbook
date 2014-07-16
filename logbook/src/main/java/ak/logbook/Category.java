package ak.logbook;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Category extends Item {

	public Category (Context cntxt){
		RowLayout=R.layout.l_category;
        menuLevel=1;
        columns = Table_Category.COLUMNS;
        tableName = Table_Category.TableName;
        context = cntxt;
    }
	

	private String category = null;
	private int icon = 0;
    private  int color = 0;
    private IconicFontDrawable icon_draw;
		

	@Override
	public void setItem(Cursor cursor) {
		id=cursor.getInt(cursor.getColumnIndex(Table_Category.ID));
		category=cursor.getString(cursor.getColumnIndex(Table_Category.CATEGORY));
		icon=cursor.getInt(cursor.getColumnIndex(Table_Category.ICON));
        color=cursor.getInt(cursor.getColumnIndex(Table_Category.COLOR));
	}

	@Override
	public void setItem(Intent intent) {
        id=intent.getIntExtra(Table_Category.ID, 0);
        category=intent.getStringExtra(Table_Category.CATEGORY);
        icon=intent.getIntExtra(Table_Category.ICON, 0);
        color=intent.getIntExtra(Table_Category.COLOR, 0);
	}

	@Override
	public ContentValues getCV(){
		ContentValues cv = new ContentValues();
		cv.put(Table_Category.CATEGORY, category);
		cv.put(Table_Category.ICON, icon);
        cv.put(Table_Category.COLOR, color);
		return cv;
	}

	@Override
	public View inflateView (View v){
        TextView tt = (TextView) v.findViewById(R.id.categorytext);
		View icn = v.findViewById(R.id.view_cat_icon);
        LinearLayout lay = (LinearLayout) v.findViewById(R.id.categoryLayout);
        if (lay != null){
            lay.setBackgroundColor(color);
        }

        if (tt != null) {
			tt.setText(getCategory());
		}
		if (icn != null) {
            icon_draw = new IconicFontDrawable(context);
            icon_draw.setIcon(Icon.getIcon(icon));
//            icon_draw.setIconColor(color);
            icn.setBackgroundDrawable(icon_draw);
		}
		return v;
	}

	public String getCategory (){
		return category;
	}
	
	public int getIcon (){
		return icon;
	}

	@Override
	public String toString() {
		return getCategory();
	}

	@Override
	public Intent toIntent(Intent intent) {
        intent.putExtra (Table_Category.ID, id);
        intent.putExtra (Table_Category.CATEGORY, category);
        intent.putExtra (Table_Category.ICON, icon);
        intent.putExtra (Table_Category.COLOR, color);
        return intent;
	}
}



package ak.logbook;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;



public class Catalog extends Item {
    private int icon, id;
    private String title;
    private String childName;

    public Catalog(Context cntxt){
    	RowLayout=R.layout.l_catalog;
        menuLevel=0;
        columns = Table_Catalog.COLUMNS;
        tableName = Table_Catalog.TableName;
        context = cntxt;
    }

	@Override
	public void setItem(Cursor cursor) {
        id=cursor.getInt(cursor.getColumnIndex(Table_Catalog.ID));
        title=cursor.getString(cursor.getColumnIndex(Table_Catalog.ITEMNAME));
        icon=cursor.getInt(cursor.getColumnIndex(Table_Catalog.ICON));
        childName=cursor.getString(cursor.getColumnIndex(Table_Catalog.CHILDNAME));

    }

	@Override
	public void setItem(Intent intent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ContentValues getCV() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View inflateView (View v){
		TextView tt = (TextView) v.findViewById(R.id.txtTitle);
        View view_icon = v.findViewById(R.id.view_icon);
        if (tt != null) {
            tt.setText(title);
        }
        if (view_icon != null){
            IconicFontDrawable icon_draw = new IconicFontDrawable(context);
            icon_draw.setIcon(Icon.getIcon(icon));
            view_icon.setBackgroundDrawable(icon_draw);
        }
		return v;
	}

	@Override
	public String toString() {
		return title;
	}

	public int getIcon(){
		return icon;
	}

    public String getChildName(){
        return childName;
    }

    public String getTitle(){
		return title;
	}

	@Override
	public Intent toIntent(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
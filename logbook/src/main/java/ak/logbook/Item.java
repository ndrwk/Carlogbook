package ak.logbook;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;

public abstract class Item {

	int RowLayout;
    int menuLevel;
    int id;
    String [] columns;
    String tableName;
    Context context;

	abstract public void setItem(Cursor cursor);

	abstract public void setItem(Intent intent);

	abstract public ContentValues getCV();
	
	abstract public Intent toIntent (Intent intent);

	abstract public View inflateView(View v);
	
	abstract public String toString();

    public String getTblName (){
        return tableName;
    }

    public String [] getColumns(){
        return columns;
    }

	public int getRawLayout() {
		return RowLayout;
	}

    public int getmenuLevel() {
        return menuLevel;
    }

    public String getName(){
        return getClass().getName();
    }

    public int getId () {
        return id;
    }
}
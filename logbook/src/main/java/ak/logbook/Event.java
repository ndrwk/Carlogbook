package ak.logbook;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;

public class Event extends Item {
/*
	"CREATE TABLE event (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
    "car INTEGER NOT NULL, date LONG NOT NULL, category INTEGER NOT NULL,"+
    "odometr INTEGER NOT NULL, provider INTEGER NOT NULL);",
*/
	public Event (){
//		RowLayout=R.layout.listitemforevent;
	}


	private int id, car, category, odometr, provider = 0;
	private long date;

	public View inflateView (View v){
		return null;
	}

	
	@Override
	public void setItem(Cursor cursor) {
		id=cursor.getInt(cursor.getColumnIndex(Table_Event.ID));
		car=cursor.getInt(cursor.getColumnIndex(Table_Event.CAR));
		date=cursor.getLong(cursor.getColumnIndex(Table_Event.DATE));
		odometr=cursor.getInt(cursor.getColumnIndex(Table_Event.ODOMETR));
		provider=cursor.getInt(cursor.getColumnIndex(Table_Event.PROVIDER));
	}

	@Override
	public void setItem(Intent intent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ContentValues getCV(){
		ContentValues cv = new ContentValues();
		cv.put(Table_Event.CAR, car);
		cv.put(Table_Event.DATE, date);
		cv.put(Table_Event.ODOMETR, odometr);
		cv.put(Table_Event.PROVIDER, provider);
		return cv;
	}

	public int getCategory (){
		return category;
	}
	
	public int getCar (){
		return car;
	}


	@Override
	public String toString() {
		return getCategory()+", "+getCar();
	}


	@Override
	public Intent toIntent(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}

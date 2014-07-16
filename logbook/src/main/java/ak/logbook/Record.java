package ak.logbook;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;

public class Record extends Item {
/*    "CREATE TABLE record (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
    "event INTEGER NOT NULL, part INTEGER, qty INTEGER, summparts REAL, "+
    "summwork REAL, currency INTEGER, note TEXT);",   */

	private int id, event, part, qty, currency, category = 0;
	private double summparts = 0;
	private String note = null;
	
	public Record (){
	}
	
	@Override
	public void setItem(Cursor cursor) {
		id=cursor.getInt(cursor.getColumnIndex(Table_Record.ID));
		event=cursor.getInt(cursor.getColumnIndex(Table_Record.EVENT));
        category = cursor.getInt(cursor.getColumnIndex(Table_Record.CATEGORY));
		part=cursor.getInt(cursor.getColumnIndex(Table_Record.PART));
		qty=cursor.getInt(cursor.getColumnIndex(Table_Record.QTY));
		summparts=cursor.getDouble(cursor.getColumnIndex(Table_Record.SUMM));
		currency=cursor.getInt(cursor.getColumnIndex(Table_Record.CURRENCY));
		note=cursor.getString(cursor.getColumnIndex(Table_Record.NOTE));
	}

	@Override
	public ContentValues getCV(){
		ContentValues cv = new ContentValues();
		cv.put(Table_Record.EVENT, event);
        cv.put(Table_Record.CATEGORY, category);
		cv.put(Table_Record.PART, part);
		cv.put(Table_Record.QTY, qty);
		cv.put(Table_Record.SUMM, summparts);
		cv.put(Table_Record.CURRENCY, currency);
		cv.put(Table_Record.NOTE, note);
		return cv;
	}

	@Override
	public void setItem(Intent intent) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public View inflateView(View v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Intent toIntent(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}

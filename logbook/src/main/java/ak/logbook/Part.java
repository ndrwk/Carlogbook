package ak.logbook;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
"CREATE TABLE part (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
"part TEXT NOT NULL, partnumber TEXT, category INTEGER);",
*/

public class Part extends Item {

    private String part, partnumber = null;
//    private int category = 0;

    public Part(Context cntxt){
		RowLayout=R.layout.l_part;
        menuLevel=1;
        columns = Table_Part.COLUMNS;
        tableName = Table_Part.TableName;
        context = cntxt;

    }


	public ContentValues getCV(){
		ContentValues cv = new ContentValues();
		cv.put(Table_Part.PART, part);
		cv.put(Table_Part.PARTNUMBER, partnumber);
//		cv.put(Table_Part.CATEGORY, category);
		return cv;
	}
	
	public void setID (int _id){
		id=_id;
	}
	
	public String getPart (){
		return part;
	}
	
	public String getPartnumber (){ return partnumber; }

	public int getID (){
		return id;
	}
/*
	public int getCategory (){
		return category;
	}
*/
	public View inflateView (View v){
		TextView tt = (TextView) v.findViewById(R.id.parttoptext);
		TextView bt = (TextView) v.findViewById(R.id.partbottomtext);
		if (tt != null) {
			tt.setText(getPart());
		}

        if (bt != null) {
            if (partnumber != null) {
                if (!partnumber.equals("")) {
                    bt.setText("№ " + partnumber);
                } else bt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
            } else bt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
        }


        return v;
	}


	@Override
	public void setItem(Cursor cursor) {
		id=cursor.getInt(cursor.getColumnIndex(Table_Part.ID));
		part=cursor.getString(cursor.getColumnIndex(Table_Part.PART));
		partnumber=cursor.getString(cursor.getColumnIndex(Table_Part.PARTNUMBER));
//		category=cursor.getInt(cursor.getColumnIndex(Table_Part.CATEGORY));
	}


	@Override
	public void setItem(Intent intent) {
        id=intent.getIntExtra(Table_Part.ID, 0);
        part=intent.getStringExtra(Table_Part.PART);
        partnumber=intent.getStringExtra(Table_Part.PARTNUMBER);
//        category=intent.getIntExtra(Table_Part.CATEGORY, 0);

	}

	@Override
	public String toString() {
		return getPart()+" "+getPartnumber();
	}

	@Override
	public Intent toIntent(Intent intent) {
        intent.putExtra (Table_Part.ID, id);
        intent.putExtra (Table_Part.PART, part);
        intent.putExtra (Table_Part.PARTNUMBER, partnumber);
//        intent.putExtra (Table_Part.CATEGORY, category);
        return intent;
	}
	
}

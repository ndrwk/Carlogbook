package ak.logbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Drew on 29.01.14.
 */
public class ImportExportCSV {

    private logbookDB db;
    private SQLiteDatabase dbs;
    private Context context;
    protected File backupFolder, importfile;


    public ImportExportCSV (Context context){
        this.context = context;
        backupFolder = new File(Environment.getExternalStorageDirectory(), "CarLogbook");
    }


    public boolean exportToCSV (Table table){
        Boolean returnCode = false;
        db = new logbookDB(context);
        dbs = db.getReadableDatabase();

        try {
            if (!backupFolder.exists()) {
                backupFolder.mkdirs();
            }
            File outFile = new File(backupFolder, table.Name);
            FileWriter fileWriter = new FileWriter(outFile);
            BufferedWriter out = new BufferedWriter(fileWriter);
            Cursor cursor = dbs.query(table.Name, table.COLUMNS,
                    null, null, null, null, null);

            if (cursor != null) {
                out.write(table.getCSVheader());
                while (cursor.moveToNext()) {
                    out.write(table.getCSVline(cursor));
                }
            }
            out.close();
            cursor.close();
            returnCode = true;
        } catch (Exception e) {
            returnCode = false;
        }
        dbs.close();
        db.close();
        return returnCode;
    }

    public boolean importFromCSV (Table table){
        importfile = new File(backupFolder, table.Name);
        int i = 0;
        File file = new File(importfile.getPath());
        if (!file.exists()) {
            return false;
        }
        BufferedReader bufRdr = null;
        try {
            bufRdr = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context,R.string.filenotfound,Toast.LENGTH_LONG).show();
            return false;
        }
        String line = null;
        boolean flag_is_header = false;
        db = new logbookDB(context);
        dbs = db.getWritableDatabase();
        try {
            while ((line = bufRdr.readLine()) != null) {
                String[] insertValues = line.split(",");
                if (flag_is_header) {
                    ContentValues cv = table.getCV(insertValues);
                    if (cv==null){
                        Toast.makeText(context,R.string.filecorrupted,Toast.LENGTH_LONG).show();
                        return false;
                    }
                    dbs.insert(table.Name, null, cv);
                } else {
                    flag_is_header = true;
                }
            }
            bufRdr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dbs.close();
        db.close();
        return true;
    }

    public boolean importFromAssets (Table table){
        String fileName = table.getTableName();
        BufferedReader bufRdr;
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            bufRdr = new BufferedReader(inputStreamReader);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context,R.string.filenotfound,Toast.LENGTH_LONG).show();
            return false;
        }
        String line = null;
        boolean flag_is_header = false;
        db = new logbookDB(context);
        dbs = db.getWritableDatabase();
        try {
            while ((line = bufRdr.readLine()) != null) {
                String[] insertValues = line.split(",");
                if (flag_is_header) {
                    ContentValues cv = table.getCV(insertValues);
                    if (cv==null){
                        Toast.makeText(context,R.string.filecorrupted,Toast.LENGTH_LONG).show();
                        return false;
                    }
                    dbs.insert(table.Name, null, cv);
                } else {
                    flag_is_header = true;
                }
            }
            bufRdr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbs.close();
        db.close();
        return true;
    }

}

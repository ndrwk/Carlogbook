package ak.logbook;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Drew on 01.02.14.
 */
public class FragmentPrefs extends Fragment{
    Button btnsavetoCSV, btnloadfromCSV, btnloadDemo, btnclearDB;
    logbookDB db;
    SQLiteDatabase dbs;

    public interface PrefsCallback {
        public void loadDemoData();
        public void clearDB();
    }

    PrefsCallback prefsCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.f_preferences, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        btnsavetoCSV = (Button) getView().findViewById(R.id.btnsavetoCSV);
        btnsavetoCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savetoCSV();
            }
        });
        btnloadfromCSV = (Button) getView().findViewById(R.id.btnloadfromCSV);
        btnloadfromCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadfromCSV();
            }
        });
        btnclearDB = (Button) getView().findViewById(R.id.btnclearDB);
        btnclearDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefsCallback.clearDB();
            }
        });
        btnloadDemo = (Button) getView().findViewById(R.id.btnloadDemo);
        btnloadDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefsCallback.loadDemoData();
            }
        });
    }


    private boolean savetoCSV (){
        ImportExportCSV impexp = new ImportExportCSV(getActivity());
        impexp.exportToCSV(new Table_Car());
        impexp.exportToCSV(new Table_Category());
        impexp.exportToCSV(new Table_Currency());
        impexp.exportToCSV(new Table_Event());
        impexp.exportToCSV(new Table_Part());
        impexp.exportToCSV(new Table_Provider());
        impexp.exportToCSV(new Table_Record());
        Toast.makeText(getActivity(),"база сохранена в CSV",Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean loadfromCSV (){
        db = new logbookDB(getActivity());
        dbs = db.getWritableDatabase();
        ImportExportCSV impexp = new ImportExportCSV(getActivity());
        impexp.importFromCSV(new Table_Car());
        impexp.importFromCSV(new Table_Category());
        impexp.importFromCSV(new Table_Currency());
        impexp.importFromCSV(new Table_Event());
        impexp.importFromCSV(new Table_Part());
        impexp.importFromCSV(new Table_Provider());
        impexp.importFromCSV(new Table_Record());
        dbs.close();
        db.close();
        Toast.makeText(getActivity(),"loaded from CSV",Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            prefsCallback = (PrefsCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }


}

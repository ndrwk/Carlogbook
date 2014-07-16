package ak.logbook;

/**
 * Created by Drew on 17.08.13.
 */

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



public class EventActivity extends ActionBarActivity {


    private boolean isDirty, add, isEditRecord, isAddRec, enUndo, waitFlag, addNewRec,
            isAmode, isEditAmodes, callAdd = false;
    private logbookDB db;
    private SQLiteDatabase dbs;
    private ActionBar bar;
    private MenuItem btnSave, btnDel, btnAdd;
    private Spinner spinnerCar, spinnerCategory, spinnerProvider, spinnerPart, spinnerCurrency;
    private TextView tvData;
    private EditText odometrET, qtyET, summET, noteET;
    private ListView lView;
    private ArrayList<Long> recListId;
    private Context context;
    private int eventDate, carId, categoryId, providerId, partId, currencyId, listId, odometr, qty;
    private String note;
    private Double summ;
    private Date dNow;
    private long eventId, recordId, unixdate;
    private ActionMode amode, amodeEdit;
    private ArrayList<String>  carArray, providerArray, partArray;
    private ArrayList<ContentValues> categoryArray, currencyArray;
    private TextWatcher ETWatcher;
    private LinearLayout recLine;
    private DatePickerDialog.OnDateSetListener DateCallBack;
    private SimpleDateFormat dateFormat;
    private Item itm;



        @Override
        protected void onRestoreInstanceState(Bundle savedInstanceState) {
            super.onRestoreInstanceState(savedInstanceState);
            isDirty=savedInstanceState.getBoolean("isDirty");
            add=savedInstanceState.getBoolean("add");
            isEditRecord=savedInstanceState.getBoolean("isEditRecord");
            isAddRec=savedInstanceState.getBoolean("isAddRec");
            enUndo=savedInstanceState.getBoolean("enUndo");
            waitFlag=savedInstanceState.getBoolean("waitFlag");
            addNewRec=savedInstanceState.getBoolean("addNewRec");
            isAmode=savedInstanceState.getBoolean("isAmode");
            isEditAmodes=savedInstanceState.getBoolean("isEditAmodes");
            callAdd=savedInstanceState.getBoolean("callAdd");
            eventDate=savedInstanceState.getInt("eventDate");
            carId=savedInstanceState.getInt("carId");
            categoryId=savedInstanceState.getInt("categoryId");
            providerId=savedInstanceState.getInt("providerId");
            partId=savedInstanceState.getInt("partId");
            currencyId=savedInstanceState.getInt("currencyId");
            listId=savedInstanceState.getInt("listId");
            odometr=savedInstanceState.getInt("odometr");
            qty=savedInstanceState.getInt("qty");
            note=savedInstanceState.getString("note");
            summ=savedInstanceState.getDouble("Summ", 0);
            eventId=savedInstanceState.getLong("eventId");
            recordId=savedInstanceState.getLong("recordId");
            unixdate=savedInstanceState.getLong("unixdate");
        }

        @Override
        protected void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putBoolean("isDirty", isDirty);
            outState.putBoolean("add", add);
            outState.putBoolean("isEditRecord", isEditRecord);
            outState.putBoolean("isAddRec", isAddRec);
            outState.putBoolean("enUndo", enUndo);
            outState.putBoolean("waitFlag", waitFlag);
            outState.putBoolean("addNewRec", addNewRec);
            outState.putBoolean("isAmode", isAmode);
            outState.putBoolean("isEditAmodes", isEditAmodes);
            outState.putBoolean("callAdd", callAdd);
            outState.putInt("eventDate", eventDate);
            outState.putInt("carId", carId);
            outState.putInt("categoryId", categoryId);
            outState.putInt("providerId", providerId);
            outState.putInt("partId", partId);
            outState.putInt("currencyId", currencyId);
            outState.putInt("listId", listId);
            outState.putInt("odometr", odometr);
            outState.putInt("qty", qty);
            outState.putString("note", note);
            outState.putLong("eventId", eventId);
            outState.putLong("recordId", recordId);
            outState.putLong("unixdate", unixdate);
        }
 
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_event);
        context = this;
        tvData = (TextView) findViewById(R.id.eventData);
        spinnerCar = (Spinner) findViewById(R.id.spinnerCar);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        spinnerProvider = (Spinner) findViewById(R.id.spinnerProvider);
        spinnerPart = (Spinner) findViewById(R.id.spinnerPart);
        spinnerCurrency = (Spinner) findViewById(R.id.spinnerCurrency);
        noteET = (EditText) findViewById(R.id.NOTE);
        odometrET = (EditText) findViewById(R.id.Odometr);
        qtyET = (EditText) findViewById(R.id.QTY);
        summET = (EditText) findViewById(R.id.SUMM);
        lView = (ListView) findViewById(R.id.listView);
        recLine = (LinearLayout) findViewById(R.id.recline);
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        DateCallBack = new DatePickerDialog.OnDateSetListener(){
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year, monthOfYear, dayOfMonth);
                dNow = c.getTime();
                tvData.setText(dateFormat.format(dNow));
                unixdate = dNow.getTime();
                getDateOdoRange();
            }
        };

        tvData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment DatePicK = new DatePickerFragment(DateCallBack,dNow);
                DatePicK.show(getSupportFragmentManager(), "date");
            }
        });

        ETWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                setDirty();
            }
        };



    }

    private void listviewClick() {
        setListView(null);
        recLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        recLine.setVisibility(View.VISIBLE);
        setSummaryForEditRecord(listId);
        amodeEdit = startSupportActionMode(new Amodes(R.menu.editamodes));
    }

    @Override
    protected void onResume (){
        super.onResume();
        db = new logbookDB(this);
        dbs = db.getReadableDatabase();

        categoryArray = getArrayList(Table_Category.TableName, new String [] {Table_Category.CATEGORY, Table_Category.ICON, Table_Category.COLOR});
        currencyArray = getArrayList(Table_Currency.TableName, new String[]{Table_Currency.CURRENCY, Table_Currency.ICON});

        carArray = getArrayList(Table_Car.TableName, new String [] {Table_Car.FIRM});
        providerArray = getArrayList(Table_Provider.TableName, new String [] {Table_Provider.NAME});
        partArray = getArrayList(Table_Part.TableName, new String [] {Table_Part.PART});

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!add) {
                    listId = position;
                    isEditAmodes = true;
                    listviewClick();
                }
            }
        });

        Intent intent = getIntent();
        if (intent.getAction().equals(Main.ACTION_ADD)) {
            setupActionBar("Новый чек");
            add = true;
            dNow = new Date();
            unixdate = dNow.getTime();
            carId = getLast(Table_Event.TableName, Table_Event.CAR);
            providerId = getLast(Table_Event.TableName, Table_Event.PROVIDER);
            odometrET.setHint("> "+getLast(Table_Event.TableName, Table_Event.ODOMETR));
            ContentValues cv = getLastForProvider(providerId);
            partId = cv.getAsInteger(Table_Record.PART);
            categoryId = cv.getAsInteger((Table_Record.CATEGORY));
            currencyId = cv.getAsInteger(Table_Record.CURRENCY);
            qty = cv.getAsInteger(Table_Record.QTY);
            summ = cv.getAsDouble(Table_Record.SUMM);
            setIconSpinner(spinnerCategory,categoryArray,categoryId);
            setIconSpinner(spinnerCurrency, currencyArray, currencyId);
            setSpinner(spinnerCar, carArray, carId);
            setSpinner(spinnerProvider, providerArray, providerId);
            setSpinner(spinnerPart, partArray, partId);
            qtyET.setText("" + qty);
            summET.setText("" + summ);
            isDirty = false;
            odometrET.addTextChangedListener(ETWatcher);
            qtyET.addTextChangedListener(ETWatcher);
            summET.addTextChangedListener(ETWatcher);
            noteET.addTextChangedListener(ETWatcher);
        } else if (intent.getAction().equals(Main.ACTION_EDIT)) {
            setupActionBar("Правка чека");
            eventId = intent.getLongExtra(Main.EXTRA_EVENTID, 0);
            unixdate = Long.parseLong(getItem(Table_Event.TableName, Table_Event.DATE, "" + eventId));
            carId = Integer.parseInt(getItem(Table_Event.TableName, Table_Event.CAR, "" + eventId));
            providerId = Integer.parseInt(getItem(Table_Event.TableName, Table_Event.PROVIDER, "" + eventId));
            odometr = Integer.parseInt(getItem(Table_Event.TableName, Table_Event.ODOMETR, "" + eventId));
            dNow = new Date(unixdate);
            setSpinner(spinnerCar, carArray, carId);
            setSpinner(spinnerProvider, providerArray, providerId);
            odometrET.setText(odometr + "");
            setListView(eventId);
            setSummaryForEditRecord(0);
            isDirty = false;
            odometrET.addTextChangedListener(ETWatcher);
            recLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
            recLine.setVisibility(View.INVISIBLE);
        }
        dbs.close();
        db.close();
        tvData.setText(dateFormat.format(dNow));

        waitFlag = true;
        setListView(eventId);
        if (isEditAmodes & isAmode) {
            listviewClick();
        }

        if (!isEditAmodes & isAmode) {
            amode = startSupportActionMode(new Amodes(R.menu.addamodes));
        }
        db = new logbookDB(this);
        dbs = db.getReadableDatabase();

        if (itm != null) {
            String className = itm.getClass().getName();
            if (className.equals("ak.logbook.Car")) {
//                carId = getLast(Table_Car.TableName,Table_Car.ID);
                carArray = getArrayList(Table_Car.TableName, new String [] {Table_Car.MODEL});
                setSpinner(spinnerCar, carArray, carArray.size()-1);
            } else if (className.equals("ak.logbook.Part")) {
//                partId = getLast(Table_Part.TableName, Table_Part.ID);
                partArray = getArrayList(Table_Part.TableName, new String [] {Table_Part.PART});
                setSpinner(spinnerPart, partArray, partArray.size()-1);
            } else if (className.equals("ak.logbook.Currency")) {
//                currencyId = getLast(Table_Currency.TableName, Table_Currency.ID);
                currencyArray = getArrayList(Table_Currency.TableName, new String[]{Table_Currency.CURRENCY, Table_Currency.ICON});
                setIconSpinner(spinnerCurrency, currencyArray, currencyArray.size()-1);
            } else if (className.equals("ak.logbook.Provider")) {
//                providerId = getLast(Table_Provider.TableName, Table_Provider.ID);
                providerArray = getArrayList(Table_Provider.TableName, new String [] {Table_Provider.NAME});
                setSpinner(spinnerProvider, providerArray, providerArray.size()-1);
            } else if (className.equals("ak.logbook.Category")) {
//                categoryId = getLast(Table_Category.TableName, Table_Category.ID);
                categoryArray = getArrayList(Table_Category.TableName, new String [] {Table_Category.CATEGORY, Table_Category.ICON, Table_Category.COLOR});
                setIconSpinner(spinnerCategory,categoryArray,categoryArray.size()-1);
            }
            callAdd = false;
        }
        dbs.close();
        db.close();
    }


    private void setDirty (){
//        Editable ee = ed;
        if (!isDirty) {
            isDirty = true;
            supportInvalidateOptionsMenu();
        }
    }

    private void setSummaryForEditRecord (int recordId){
        db = new logbookDB(this);
        dbs = db.getReadableDatabase();
        long rowId = recListId.get(recordId);
        Cursor recCursor = dbs.query(Table_Record.TableName, Table_Record.COLUMNS,
                Table_Record.ID + "=" + rowId,
                null, null, null, null);
        recCursor.moveToFirst();
        categoryId = recCursor.getInt(recCursor.getColumnIndex(Table_Record.CATEGORY));
        partId = recCursor.getInt(recCursor.getColumnIndex(Table_Record.PART));
        qty = recCursor.getInt(recCursor.getColumnIndex(Table_Record.QTY));
        summ = recCursor.getDouble(recCursor.getColumnIndex(Table_Record.SUMM));
        currencyId = recCursor.getInt(recCursor.getColumnIndex(Table_Record.CURRENCY));
        note = recCursor.getString(recCursor.getColumnIndex(Table_Record.NOTE));
        recCursor.close();
        dbs.close();
        db.close();
        setSpinner(spinnerPart, partArray, partId);
        setIconSpinner(spinnerCategory,categoryArray,categoryId);
        setIconSpinner(spinnerCurrency, currencyArray, currencyId);
//        spinnerPart.setSelection(partId);
//        spinnerCategory.setSelection(categoryId);
//        spinnerCurrency.setSelection(currencyId);
        qtyET.setText("" + qty);
        summET.setText("" + summ);
        noteET.setText(note);
    }

    private void dispForProvider() {
        db = new logbookDB(this);
        dbs = db.getReadableDatabase();
        ContentValues cv = getLastForProvider(providerId);
        partId = cv.getAsInteger(Table_Record.PART);
        categoryId = cv.getAsInteger((Table_Record.CATEGORY));
        currencyId = cv.getAsInteger(Table_Record.CURRENCY);
        qty = cv.getAsInteger(Table_Record.QTY);
        summ = cv.getAsDouble(Table_Record.SUMM);
        spinnerCategory.setSelection(categoryId);
        spinnerProvider.setSelection(providerId);
        spinnerPart.setSelection(partId);
        spinnerCurrency.setSelection(currencyId);
        dbs.close();
        db.close();
    }


    private void dispForCategory() {
        db = new logbookDB(this);
        dbs = db.getReadableDatabase();
        ContentValues cv = getLastForCategory(categoryId);
        partId = cv.getAsInteger(Table_Record.PART);
        currencyId = cv.getAsInteger(Table_Record.CURRENCY);
        qty = cv.getAsInteger(Table_Record.QTY);
        summ = cv.getAsDouble(Table_Record.SUMM);
        spinnerCategory.setSelection(categoryId);
        spinnerPart.setSelection(partId);
        spinnerCurrency.setSelection(currencyId);
        qtyET.setText("" + qty);
        summET.setText("" + summ);
        dbs.close();
        db.close();
    }


    private void setupActionBar(String title) {
        bar = getSupportActionBar();
        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle(title);
//        bar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
    }

    private void setListView(Long eventID) {
        if (eventID == null){
            lView.setVisibility(View.INVISIBLE);
            return;
        }
        db = new logbookDB(this);
        dbs = db.getReadableDatabase();
        ArrayList<String> recList = new ArrayList<String>();
        recListId = new ArrayList<Long>();
        Cursor recCursor = dbs.query(Table_Record.TableName, Table_Record.COLUMNS,
                Table_Record.EVENT + "=" + eventID,
                null, null, null, null);
        String tmpRecString;
        while (recCursor.moveToNext()) {
            tmpRecString = getItem(Table_Category.TableName, Table_Category.CATEGORY, recCursor.getString(recCursor.getColumnIndex(Table_Record.CATEGORY)))+"  "+
                    getItem(Table_Part.TableName, Table_Part.PART, recCursor.getString(recCursor.getColumnIndex(Table_Record.PART))) + "  " +
                    recCursor.getString(recCursor.getColumnIndex(Table_Record.QTY)) + "  " +
                    recCursor.getString(recCursor.getColumnIndex(Table_Record.SUMM)) + "  " +
                    getItem(Table_Currency.TableName, Table_Currency.CURRENCY, recCursor.getString(recCursor.getColumnIndex(Table_Record.CURRENCY))) + "  " +
                    recCursor.getString(recCursor.getColumnIndex(Table_Record.NOTE));
//                if (Main.enableLog) Log.w("ak.Log", "tmpRecString =  "+tmpRecString);
            recList.add(tmpRecString);
            recListId.add(recCursor.getLong(recCursor.getColumnIndex(Table_Record.ID)));
        }
        recCursor.close();
        dbs.close();
        db.close();

        ArrayAdapter<String> eventAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recList);
        lView.setVisibility(View.VISIBLE);
        lView.setAdapter(eventAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        return onMenuItemClick(menuItem);
    }

    private boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_save:
                if (!checkCorrect()) return false;
                if (addNewRec) {
                    addRecord();
                    recLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                    recLine.setVisibility(View.INVISIBLE);
//                    noteET.setVisibility(View.INVISIBLE);
                    addNewRec = false;
                    setListView(eventId);
                    supportInvalidateOptionsMenu();
                }else if (add) {
                    spinnerCar.setEnabled(false);
//                    spinnerCategory.setEnabled(false);
                    spinnerProvider.setEnabled(false);
                    odometrET.setEnabled(false);
                    if (!isAddRec) {
                        addEvent();
                    }
                    addRecord();
                    recLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                    recLine.setVisibility(View.INVISIBLE);
//                    noteET.setVisibility(View.INVISIBLE);
                    isAddRec = false;
                    if (!isAmode) {
                        amode = startSupportActionMode(new Amodes(R.menu.addamodes));
                    }
                    enUndo = true;
                } else {
                    updEvent();
                    finish();
                }
                return true;
            case R.id.addrec:
                recLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                recLine.setVisibility(View.VISIBLE);
//                noteET.setVisibility(View.VISIBLE);
//                spinnerCategory.setVisibility(View.VISIBLE);
                isAddRec = true;
                if (add) amode.finish();
                return true;

            case R.id.undobtn:
                if (enUndo) listId = recListId.size() - 1;
                delRowfromDB(recListId.get(listId), Table_Record.TableName);
                recLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                recLine.setVisibility(View.INVISIBLE);
//                noteET.setVisibility(View.INVISIBLE);
                setListView(eventId);
                if (recListId.size() == 0) {
                    delRowfromDB(eventId, Table_Event.TableName);
                    setResult(RESULT_OK);
                    finish();
                }
                if (isEditAmodes) amodeEdit.finish();
                return true;
            case R.id.editbtn:

                return true;
            case R.id.savefromeditbtn:
                if (!checkCorrect()) return false;
                editRecord();
                recLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                recLine.setVisibility(View.INVISIBLE);
//                noteET.setVisibility(View.INVISIBLE);
                setListView(eventId);
                if (isEditAmodes) amodeEdit.finish();
                return true;
            case R.id.del_btn:
                setListView(eventId);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(getResources().getString(R.string.delete));
                alertDialog.setMessage(getResources().getString(R.string.delevent));
                alertDialog.setIcon(android.R.drawable.ic_delete);
                alertDialog.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < recListId.size(); i++) {
                            delRowfromDB(recListId.get(i), Table_Record.TableName);
                        }
                        delRowfromDB(eventId, Table_Event.TableName);
                        setResult(RESULT_OK);
                        finish();
                    }
                });
                alertDialog.setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(EventActivity.this, "Отмена", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                alertDialog.show();
                return true;
            case R.id.add_btn:
                spinnerCar.setEnabled(false);
                spinnerCategory.setEnabled(true);
                spinnerProvider.setEnabled(false);
                odometrET.setEnabled(false);
                recLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                recLine.setVisibility(View.VISIBLE);
//                noteET.setVisibility(View.VISIBLE);
//                spinnerCategory.setVisibility(View.VISIBLE);
                addNewRec = true;
                supportInvalidateOptionsMenu();
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }

    }

    private boolean checkCorrect() {
        if ((odometrET.getText().toString().length() == 0) |
                (summET.getText().toString().length() == 0) |
                (qtyET.getText().toString().length() == 0)) {
            Toast.makeText(this, "Некорректные данные", Toast.LENGTH_LONG).show();
            if (odometrET.getText().toString().length() == 0) {
                odometrET.setBackgroundResource(R.color.redColor);
            }
            if (summET.getText().toString().length() == 0) {
                summET.setBackgroundResource(R.color.redColor);
            }
            if (qtyET.getText().toString().length() == 0) {
                qtyET.setBackgroundResource(R.color.redColor);
            }
            return false;
        }
        return getDateOdoRange();
    }

    private boolean getDateOdoRange() {
        db = new logbookDB(this);
        dbs = db.getReadableDatabase();
        Cursor cursor;
        int maxOdometr;
        long maxDate;
        cursor = dbs.query(Table_Event.TableName, new String[]{"MAX(" + Table_Event.ODOMETR + ")"}, null, null, null, null, null);
        if (cursor.moveToLast()) {
            maxOdometr = cursor.getInt(cursor.getColumnIndex("MAX(" + Table_Event.ODOMETR + ")"));
        } else {
            maxOdometr = 0;
        }
        cursor = dbs.query(Table_Event.TableName, new String[]{"MAX(" + Table_Event.DATE + ")"}, null, null, null, null, null);
        if (cursor.moveToLast()) {
            maxDate = cursor.getLong(cursor.getColumnIndex("MAX(" + Table_Event.DATE + ")"));
        } else {
            maxDate = 0;
        }
        cursor.close();
        if (!odometrET.getText().toString().equals("")) {
            odometr = Integer.parseInt(odometrET.getText().toString());
        }else {
            odometr = getLast(Table_Event.TableName, Table_Event.ODOMETR)+1;
        }
        String s_unixdate = dateFormat.format(unixdate);
        String S_maxdate = dateFormat.format(maxDate);
        if ((odometr >= maxOdometr) & (unixdate/86400000 >= maxDate/86400000)) {
            return true;
        }
        long[] dateRange = new long[2];
        int [] odoRange = new int[2];
        cursor = dbs.query(Table_Event.TableName, new String[]{Table_Event.DATE}, Table_Event.ODOMETR + "<?",
                new String[]{odometr + ""}, null, null, Table_Event.DATE);
        if (cursor.moveToLast()) {
            dateRange[0] = cursor.getLong(0);
        } else {
            dateRange[0] = 0;
        }
        cursor = dbs.query(Table_Event.TableName, new String[]{Table_Event.DATE}, Table_Event.ODOMETR + ">?",
                new String[]{odometr + ""}, null, null, Table_Event.DATE);
        if (cursor.moveToFirst()) dateRange[1] = cursor.getLong(0);
        cursor = dbs.query(Table_Event.TableName, new String[]{Table_Event.ODOMETR}, Table_Event.DATE + "<?",
                new String[]{unixdate + ""}, null, null, Table_Event.ODOMETR);
        if (cursor.moveToLast()) {
            odoRange[0] = cursor.getInt(0);
        } else {
            odoRange[0] = 0;
        }
        cursor = dbs.query(Table_Event.TableName, new String[]{Table_Event.ODOMETR}, Table_Event.DATE + ">?",
                new String[]{unixdate + ""}, null, null, Table_Event.ODOMETR);
        if (cursor.moveToFirst()) odoRange[1] = cursor.getInt(0);
        cursor.close();
        dbs.close();
        db.close();
        if ((odometr < maxOdometr) & (unixdate/86400000 >= maxDate/86400000)) {
            odometrET.setBackgroundResource(R.color.redColor);
            Toast.makeText(this, "На " + dateFormat.format(dNow) + " пробег не может быть меньше " + maxOdometr + "км.", Toast.LENGTH_LONG).show();
            return false;
        }
        if ((odometr >= maxOdometr) & (unixdate/86400000 < maxDate/86400000)) {
            odometrET.setBackgroundResource(R.color.redColor);
/*
            Toast.makeText(this, "Для пробега "+odometr+" подходят даты между "+dateFormat.format(new Time(dateRange[0]))+
                    " и "+dateFormat.format(new Time(dateRange[1])), Toast.LENGTH_LONG).show();
*/
            odometrET.setHint(odoRange[0]+"< пробег  <"+odoRange[1]);
            return false;
        }
        if ((odometr > odoRange[0]) & (odometr < odoRange[1]) & (unixdate/86400000 > dateRange[0]/86400000) & (unixdate/86400000 < dateRange[1]/86400000)){
            return true;
        }
        odometrET.requestFocus();
        return false;
    }

    private void addEvent() {
        odometr = Integer.parseInt(odometrET.getText().toString());
        eventId = addRowtoDB(getEventCV(), Table_Event.TableName);
    }

    private void updEvent(){
        odometr = Integer.parseInt(odometrET.getText().toString());
        updRowInDB(getEventCV(),eventId,Table_Event.TableName);
    }

    private ContentValues getEventCV() {
        ContentValues cvEvent = new ContentValues();
        cvEvent.put(Table_Event.CAR, carId);
        cvEvent.put(Table_Event.DATE, unixdate);
        cvEvent.put(Table_Event.ODOMETR, odometr);
        cvEvent.put(Table_Event.PROVIDER, providerId);
        return cvEvent;
    }

    private void addRecord() {
        ContentValues cvRecord = getRecordCV();
        recordId = addRowtoDB(cvRecord, Table_Record.TableName);
        setListView(eventId);
        qtyET.setText("");
        summET.setText("");
        noteET.setText("");
    }

    private void editRecord (){
        ContentValues cvRecord = getRecordCV();
        updRowInDB(cvRecord, recListId.get(listId), Table_Record.TableName);
        setListView(eventId);
    }


    private ContentValues getRecordCV() {
        qty = Integer.parseInt(qtyET.getText().toString());
        summ = Double.parseDouble(summET.getText().toString());
        note = noteET.getText().toString();
        ContentValues cvRecord = new ContentValues();
        cvRecord.put(Table_Record.EVENT, eventId);
        cvRecord.put(Table_Record.PART, partId);
        cvRecord.put(Table_Record.QTY, qty);
        cvRecord.put(Table_Record.SUMM, summ);
        cvRecord.put(Table_Record.CURRENCY, currencyId);
        cvRecord.put(Table_Record.NOTE, note);
        cvRecord.put(Table_Record.CATEGORY, categoryId);
        return cvRecord;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.eventmenu, menu);
        btnSave = menu.findItem(R.id.menu_save);
        btnDel = menu.findItem(R.id.del_btn);
        btnAdd = menu.findItem(R.id.add_btn);
        if (!isDirty) btnSave.setVisible(false);
        if ((addNewRec) | (add)) {
            btnDel.setVisible(false);
            btnAdd.setVisible(false);
        }
        return true;
    }

    private void callAddItems (AdapterView<?> parent){
        callAdd = true;
        if (parent.equals(spinnerCar)){
            Intent intent = new Intent(EventActivity.this, CarActivity.class);
            intent.setAction(Main.ACTION_ADD);
            startActivityForResult(intent, Main.CALLCAR);
        } else
        if (parent.equals(spinnerCategory)){
            Intent intent = new Intent(EventActivity.this, CategoryActivity.class);
            intent.setAction(Main.ACTION_ADD);
            startActivityForResult(intent, Main.CALLCATEGORY);
        } else
        if (parent.equals(spinnerCurrency)){
            Intent intent = new Intent(EventActivity.this, CurrencyActivity.class);
            intent.setAction(Main.ACTION_ADD);
            startActivityForResult(intent, Main.CALLCURRENCY);
        } else
        if (parent.equals(spinnerPart)){
            Intent intent = new Intent(EventActivity.this, PartActivity.class);
            intent.setAction(Main.ACTION_ADD);
            startActivityForResult(intent, Main.CALLPART);
        } else
        if (parent.equals(spinnerProvider)){
            Intent intent = new Intent(EventActivity.this, ProviderActivity.class);
            intent.setAction(Main.ACTION_ADD);
            startActivityForResult(intent, Main.CALLPROVIDER);
        }
    }


    private void setSpinner(Spinner spin, ArrayList arraylist, int lastposition) {

        ArrayAdapter<String> Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraylist);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(Adapter);
        if (arraylist.size()<=1){
            callAddItems(spin);
            return;
        }
        spin.setSelection(lastposition);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    callAddItems(parent);
                } else {
                    if (waitFlag) {
                        isDirty = true;
                        supportInvalidateOptionsMenu();
                    }
                    if (parent.equals(spinnerPart)) {
                        partId = position;
                    } else if (parent.equals(spinnerCar)) {
                        carId = position;
                    } else if (parent.equals(spinnerCategory)) {
                        categoryId = position;
                        if (add&!callAdd) dispForCategory();
                    } else if (parent.equals(spinnerProvider)) {
                        providerId = position;
                        if (add&!callAdd) dispForProvider();
                    } else if (parent.equals(spinnerCurrency)) {
                        currencyId = position;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }


    private void setIconSpinner(Spinner spin, ArrayList <ContentValues> arraylist, int lastposition) {

        IconSpinnerAdapter Adapter = new IconSpinnerAdapter(this, R.layout.l_iconspinner, arraylist);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(Adapter);
        if (arraylist.size()<=1){
            callAddItems(spin);
            return;
        }
        spin.setSelection(lastposition);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    callAddItems(parent);
                } else {
                    if (waitFlag) {
                        isDirty = true;
                        supportInvalidateOptionsMenu();
                    }
                    if (parent.equals(spinnerPart)) {
                        partId = position;
                    } else if (parent.equals(spinnerCar)) {
                        carId = position;
                    } else if (parent.equals(spinnerCategory)) {
                        categoryId = position;
                        if (add&!callAdd) dispForCategory();
                    } else if (parent.equals(spinnerProvider)) {
                        providerId = position;
                        if (add&!callAdd) dispForProvider();
                    } else if (parent.equals(spinnerCurrency)) {
                        currencyId = position;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private ArrayList getArrayList(String TableName, String [] Columns) {
        ArrayList Array = new ArrayList();
        final int lenghtforCurrency = 2;
        if (TableName.equals(Table_Part.TableName)) {
            Array.add(context.getString(R.string.newPart));
        } else if (TableName.equals(Table_Car.TableName)) {
            Array.add(context.getString(R.string.newCar));
        } else if (TableName.equals(Table_Category.TableName)) {
            ContentValues cv0 = new ContentValues();
            cv0.put(Main.CVname, context.getString(R.string.newCategory));
            cv0.put(Main.CVicon, 8);
            if (Columns.length > lenghtforCurrency)
                cv0.put(Main.CVcolor, -500000);
            Array.add(cv0);
        } else if (TableName.equals(Table_Provider.TableName)) {
            Array.add(context.getString(R.string.newProvider));
        } else if (TableName.equals(Table_Currency.TableName)) {
            ContentValues cv0 = new ContentValues();
            cv0.put(Main.CVname, context.getString(R.string.newCurrency));
            cv0.put(Main.CVicon, 644);
            Array.add(cv0);
        }
        Cursor cursor = dbs.query(TableName, Columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            if (TableName.equals(Table_Category.TableName) | TableName.equals(Table_Currency.TableName)) {
                ContentValues cv = new ContentValues();
                cv.put(Main.CVname, cursor.getString(cursor.getColumnIndex(Columns[0])));
                cv.put(Main.CVicon, cursor.getInt(cursor.getColumnIndex(Columns[1])));
                if (Columns.length > lenghtforCurrency)
                    cv.put(Main.CVcolor, cursor.getInt(cursor.getColumnIndex(Columns[2])));
                Array.add(cv);

            } else {
                Array.add(cursor.getString(cursor.getColumnIndex(Columns[0])));

            }
        }
        cursor.close();
        return Array;
    }



    private int getLast(String TableName, String Column) {
        int res;
        Cursor cursor = dbs.query(TableName, new String[]{Column}, null, null, null, null, null);
        if (cursor.moveToLast()) {
            res = cursor.getInt(cursor.getColumnIndex(Column));
        } else {
            res = 1;
        }
        cursor.close();
        return res;
    }


    private ContentValues getLastForCategory(int ctgryid) {
        ContentValues cv = new ContentValues();
        Cursor recCursor = dbs.query(Table_Record.TableName, Table_Record.COLUMNS,
                Table_Record.CATEGORY + "=" + ctgryid, null, null, null, null);
        if (recCursor.moveToLast()) {
            cv.put(Table_Record.PART, recCursor.getInt(recCursor.getColumnIndex(Table_Record.PART)));
            cv.put(Table_Record.QTY, recCursor.getInt(recCursor.getColumnIndex(Table_Record.QTY)));
            cv.put(Table_Record.SUMM, recCursor.getDouble(recCursor.getColumnIndex(Table_Record.SUMM)));
            cv.put(Table_Record.CURRENCY, recCursor.getInt(recCursor.getColumnIndex(Table_Record.CURRENCY)));
        } else {
            cv.put(Table_Record.PART, 1);
            cv.put(Table_Record.QTY, 1);
            cv.put(Table_Record.SUMM, 1);
            cv.put(Table_Record.CURRENCY, 1);
        }
        recCursor.close();
        return cv;
    }

    private ContentValues getLastForProvider (int prov) {
        ContentValues cv = new ContentValues();
        Cursor eventCursor = dbs.query(Table_Event.TableName, Table_Event.COLUMNS, Table_Event.PROVIDER + "=" + prov, null, null, null, null);
        if (eventCursor.moveToLast()) {
            Cursor recCursor = dbs.query(Table_Record.TableName, Table_Record.COLUMNS,
                    Table_Record.EVENT + "=" + eventCursor.getString(eventCursor.getColumnIndex(Table_Event.ID)),
                    null, null, null, null);
            if (recCursor.moveToLast()) {
                cv.put(Table_Record.PART, recCursor.getInt(recCursor.getColumnIndex(Table_Record.PART)));
                cv.put(Table_Record.QTY, recCursor.getInt(recCursor.getColumnIndex(Table_Record.QTY)));
                cv.put(Table_Record.SUMM, recCursor.getDouble(recCursor.getColumnIndex(Table_Record.SUMM)));
                cv.put(Table_Record.CURRENCY, recCursor.getInt(recCursor.getColumnIndex(Table_Record.CURRENCY)));
                cv.put(Table_Record.CATEGORY, recCursor.getInt(recCursor.getColumnIndex(Table_Record.CATEGORY)));
            } else {
                cv.put(Table_Record.PART, 1);
                cv.put(Table_Record.QTY, 1);
                cv.put(Table_Record.SUMM, 1);
                cv.put(Table_Record.CURRENCY, 1);
                cv.put(Table_Record.CATEGORY, recCursor.getInt(recCursor.getColumnIndex(Table_Record.CATEGORY)));
            }
            recCursor.close();
        } else {
            cv.put(Table_Record.PART, 1);
            cv.put(Table_Record.QTY, 1);
            cv.put(Table_Record.SUMM, 1);
            cv.put(Table_Record.CURRENCY, 1);
            cv.put(Table_Record.CATEGORY, 1);
        }
        eventCursor.close();
        return cv;
    }


    private final class Amodes implements ActionMode.Callback {

        private int ActionModeLayout;

        public Amodes (int layout){
            ActionModeLayout = layout;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getMenuInflater().inflate(ActionModeLayout, menu);
            isAmode = true;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
            return onMenuItemClick(menuItem);
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            switch (ActionModeLayout) {
                case R.menu.editamodes:
                    isEditAmodes = false;
                    recLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                    recLine.setVisibility(View.INVISIBLE);
                    setListView(eventId);
                    break;
                case R.menu.addamodes:
                    isAmode = false;
                    if (!isAddRec) {
                        setResult(RESULT_OK);
                        finish();
                    }
                    break;

            }
        }
    }




    public long addRowtoDB(ContentValues CV, String TableName) {
        long ID;
        db = new logbookDB(this);
        dbs = db.getWritableDatabase();
        ID = dbs.insert(TableName, null, CV);
        dbs.close();
        db.close();
        return ID;
    }

    public void delRowfromDB(long id, String TableName) {
        db = new logbookDB(this);
        dbs = db.getWritableDatabase();
        dbs.delete(TableName, "_id=" + id, null);
        dbs.close();
        db.close();
    }

    public void updRowInDB(ContentValues CV, long id, String TableName) {
        db = new logbookDB(this);
        dbs = db.getWritableDatabase();
        if (Main.enableLog)
            Log.w("ak.Log", "Обновлена " + TableName + " по id=" + id);
        dbs.update(TableName, CV, "_id=" + id, null);
        dbs.close();
        db.close();
    }

    private String getItem(String table, String column, String id) {
        Cursor ItemCursor = dbs.query(table, new String[]{"_id", column}, "_id=" + id, null, null, null, null);
        ItemCursor.moveToFirst();
        String tmp = ItemCursor.getString(ItemCursor.getColumnIndex(column));
        //Log.w("ak.Log", "вернул "+tmp);
        ItemCursor.close();
        return tmp;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        super.onActivityResult(requestCode, resultCode, resultIntent);
        switch (requestCode) {
            case Main.CALLCAR:
                switch (resultCode) {
                    case RESULT_OK:
                        Car car = new Car(context);
                        car.setItem(resultIntent);
                        if (resultIntent.getAction().equals(Main.ACTION_ADD)){
                            addRowtoDB(car.getCV(), car.getTblName());
                            itm = car;
                        }
                        break;
                    case RESULT_CANCELED:
                        itm = null;
                        break;
                }
                break;
            case Main.CALLCATEGORY:
                switch (resultCode) {
                    case RESULT_OK:
                        Category category = new Category(context);
                        category.setItem(resultIntent);
                        if (resultIntent.getAction().equals(Main.ACTION_ADD)){
                            addRowtoDB(category.getCV(), category.getTblName());
                            itm = category;
                        }
                        break;
                    case RESULT_CANCELED:
                        itm = null;
                        break;
                }
                break;
            case Main.CALLCURRENCY:
                switch (resultCode) {
                    case RESULT_OK:
                        Currency currency = new Currency(context);
                        currency.setItem(resultIntent);
                        if (resultIntent.getAction().equals(Main.ACTION_ADD)){
                            addRowtoDB(currency.getCV(), currency.getTblName());
                            itm = currency;
                        }
                        break;
                    case RESULT_CANCELED:
                        itm = null;
                        break;
                }
                break;
            case Main.CALLPART:
                switch (resultCode) {
                    case RESULT_OK:
                        Part part = new Part(context);
                        part.setItem(resultIntent);
                        if (resultIntent.getAction().equals(Main.ACTION_ADD)){
                            addRowtoDB(part.getCV(), part.getTblName());
                            itm = part;
                        }
                        break;
                    case RESULT_CANCELED:
                        itm = null;
                        break;
                }
                break;
            case Main.CALLPROVIDER:
                switch (resultCode) {
                    case RESULT_OK:
                        Provider provider = new Provider(context);
                        provider.setItem(resultIntent);
                        if (resultIntent.getAction().equals(Main.ACTION_ADD)){
                            addRowtoDB(provider.getCV(), provider.getTblName());
                            itm = provider;
                        }
                        break;
                    case RESULT_CANCELED:
                        itm = null;
                        break;
                }
                break;
        }
    }



/*
    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch(keycode) {
            case KeyEvent.KEYCODE_BACK:
                actionBack();
                return true;
        }
        return super.onKeyDown(keycode, e);
    }
*/






    }


package ak.logbook;

/**
 * Created by Drew on 13.10.13.
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main extends ActionBarActivity implements AdapterView.OnItemClickListener, FragmentLogList.ClickListenerCallback {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] logbook_children;
    private Fragment fragment;

    private ArrayList <Long> eventListId;

    private logbookDB db;
    private SQLiteDatabase dbs;
    private SimpleDateFormat dateFormat;

    private int posInDrawer = LOGLISTFRAG;
    private ArrayAdapter <?> AdapterforFrag;
    private static final int LOGLISTFRAG = 0;
    private static final int CATALOGFRAG = 1;
    private static final int PREFERENCESFRAG = 2;

    private int menuLevel=0;
    private String className = null;
    public static final String ACTION_ADD = "add";
    public static final String ACTION_EDIT = "edit";
    public static final String EXTRA_EVENTID = "eventId";

    public static final String CVname = "CVname";
    public static final String CVicon = "CVicon";
    public static final String CVcolor = "CVcolor";


    public static final int CALLCAR = 0;
    public static final int CALLPART = 1;
    public static final int CALLCATEGORY = 2;
    public static final int CALLCURRENCY = 3;
    public static final int CALLPROVIDER = 4;
    public static final int CALLEVENT = 5;

    private Item itm;
    private ItemFactory itemFactory = new ItemFactory();
    private Context context;
    private ActionBar bar;
    private ActionMode amode;
    private boolean isAmode = false;
    private ActionBarDrawerToggle mDrawerToggle;


    final static boolean enableLog = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mTitle = mDrawerTitle = getTitle();
        logbook_children = getResources().getStringArray(R.array.drawer_items);

        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, logbook_children));
        mDrawerList.setOnItemClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer,
                R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
            }
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
/*
        if (savedInstanceState == null) {
           posInDrawer = 0;
        }
*/
    }

    @Override
    protected void onResume (){

        super.onResume();
        db = new logbookDB(this);
        dbs = db.getReadableDatabase();
        switch (posInDrawer){
            case LOGLISTFRAG:
                selectDrawerItem(posInDrawer);
                break;
            case CATALOGFRAG:
                FragmentLogList frag = (FragmentLogList) getSupportFragmentManager().findFragmentById(R.id.content_frame);
                if ((frag != null) & (className != null)) {
                    itm = itemFactory.getItemOnClass(className, this);
                    AdapterforFrag = new ItemAdapter(this, itm.getRawLayout(), getArrayFromTable(itm.getTblName(), itm.getColumns()));
                    frag.dispList(AdapterforFrag);
                } else {
                    selectDrawerItem(posInDrawer);
                }
                break;
        }

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        menuLevel=savedInstanceState.getInt("menuLevel");
        className=savedInstanceState.getString("className");
        posInDrawer=savedInstanceState.getInt("posInDrawer", CATALOGFRAG);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("menuLevel", menuLevel);
        outState.putInt("posInDrawer", posInDrawer);
        try {
            className = AdapterforFrag.getItem(0).getClass().getName();
            outState.putString("className", className);
        } catch (IndexOutOfBoundsException E) {
            outState.putString("className", null);

        }

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        posInDrawer = position;
        selectDrawerItem(position);
        supportInvalidateOptionsMenu();
    }

    private void selectDrawerItem(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragment = new FragmentLogList();
//        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
//        getSupportFragmentManager().executePendingTransactions();
        mDrawerList.setItemChecked(position, true);
        setTitle(logbook_children[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
        switch (position) {
            case LOGLISTFRAG:
                fragment = new FragmentLogList();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                getSupportFragmentManager().executePendingTransactions();
                FragmentLogList frag0 = (FragmentLogList) getSupportFragmentManager().findFragmentById(R.id.content_frame);
                AdapterforFrag = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, setArrayforLogList());
                frag0.dispList(AdapterforFrag);
                break;
            case CATALOGFRAG:
                menuLevel = 0;
                Item item = new Catalog(this);
                itm = item;
                fragment = new FragmentLogList();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                getSupportFragmentManager().executePendingTransactions();
                FragmentLogList frag1 = (FragmentLogList) getSupportFragmentManager().findFragmentById(R.id.content_frame);
                ArrayList <Item> itmArray = getArrayFromTable(item.getTblName(), item.getColumns());
                AdapterforFrag = new ItemAdapter(this, item.getRawLayout(), itmArray);
                frag1.dispList(AdapterforFrag);
                break;
            case PREFERENCESFRAG:
                fragment = new FragmentPrefs();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                getSupportFragmentManager().executePendingTransactions();
                FragmentPrefs fragPref = new FragmentPrefs();

                break;
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    public ArrayList <Item> getArrayFromTable(String table, String[] columns) {
        if (Main.enableLog)
            Log.w("ak.Log", "getArrayFromTable " + table);
        Cursor CursorforList = dbs.query(table, columns, null, null, null, null, null);
        ArrayList<Item> Array = new ArrayList<Item>();
        ItemFactory tf = new ItemFactory();
        Item item = null;
        while (CursorforList.moveToNext()) {
            item = tf.getItemOnTableName(table, this);
//			Log.w("ak.Log", "getItemFromTable " + table+" from "+item.getClass().getName());
            item.setItem(CursorforList);
//			Log.w("ak.Log", "setItem " + table);
            Array.add(item);
        }
        CursorforList.close();
        return Array;
    }


    public ArrayList <String> setArrayforLogList(){
        Cursor eventCursor = dbs.query(Table_Event.TableName, Table_Event.COLUMNS, null, null, null, null, Table_Event.ODOMETR);
        ArrayList<String> eventList = new ArrayList<String>();
        eventListId = new ArrayList <Long> ();
        String tmpEventString = "";
        while (eventCursor.moveToNext()){
            tmpEventString = dateFormat.format(new Date(eventCursor.getLong(eventCursor.getColumnIndex(Table_Event.DATE))))+"--"+
                    getItem(Table_Car.TableName, Table_Car.FIRM, eventCursor.getString(eventCursor.getColumnIndex(Table_Event.CAR)))+"--"+
                    eventCursor.getString(eventCursor.getColumnIndex(Table_Event.ODOMETR))+"--"+
                    getItem(Table_Provider.TableName, Table_Provider.NAME, eventCursor.getString(eventCursor.getColumnIndex(Table_Event.PROVIDER)));
//            if (Main.enableLog) Log.w("ak.Log", "tmpEventString =  "+tmpEventString);
            eventListId.add(Long.parseLong(eventCursor.getString(eventCursor.getColumnIndex(Table_Event.ID))));
            Cursor recCursor = dbs.query(Table_Record.TableName, Table_Record.COLUMNS,
                    Table_Record.EVENT+"="+eventCursor.getString(eventCursor.getColumnIndex(Table_Event.ID)),
                    null, null, null, null);
            String tmpRecString = "";
            while (recCursor.moveToNext()){
                tmpRecString = tmpRecString + getItem(Table_Category.TableName, Table_Category.CATEGORY, recCursor.getString(recCursor.getColumnIndex(Table_Record.CATEGORY)))+"--"+
                        getItem(Table_Part.TableName, Table_Part.PART, recCursor.getString(recCursor.getColumnIndex(Table_Record.PART)))+"__"+
                        recCursor.getString(recCursor.getColumnIndex(Table_Record.QTY))+"__"+
                        recCursor.getString(recCursor.getColumnIndex(Table_Record.SUMM))+"__"+
                        getItem(Table_Currency.TableName, Table_Currency.CURRENCY, recCursor.getString(recCursor.getColumnIndex(Table_Record.CURRENCY)))+"__"+
                        recCursor.getString(recCursor.getColumnIndex(Table_Record.NOTE))+ "\n";
//                if (Main.enableLog) Log.w("ak.Log", "tmpRecString =  "+tmpRecString);
            }
            recCursor.close();
            tmpEventString = tmpEventString + "\n" + tmpRecString;
            eventList.add (tmpEventString);
        }
        eventCursor.close();
        return eventList;

    }

    private String getItem (String table, String column, String id){
        Cursor ItemCursor = dbs.query(table, new String[] {"_id", column}, "_id="+id, null, null, null, null);
        ItemCursor.moveToFirst();
        String tmp = ItemCursor.getString(ItemCursor.getColumnIndex(column));
        ItemCursor.close();
        return tmp;
    }


    @Override
    public void ClickCallback(int pos) {
        switch (posInDrawer){
            case LOGLISTFRAG:
                callEventforEdit(eventListId.get(pos));
                break;
            case CATALOGFRAG:
                CatalogNavigation(pos);
                break;
        }
    }

    @Override
    public void LongClickCallback(int pos) {
        switch (posInDrawer){
            case LOGLISTFRAG:
                break;
            case CATALOGFRAG:
                itm = (Item) AdapterforFrag.getItem(pos);
                String className = itm.getClass().getName();
                if (menuLevel != 0)
                    amode = startSupportActionMode(new Amodes());
                break;
        }

    }

    private void CatalogNavigation (int position){
        Item item = null;
        className = AdapterforFrag.getItem(position).getClass().getName();
        itm = (Item) AdapterforFrag.getItem(position);
        switch (menuLevel) {
            case 0:	//корень меню
                menuLevel++;
                DispCatalogRoot();
                break;
            case 1: //уровень таблиц
                Intent intent;
//                menuLevel--;
                if (className.equals("ak.logbook.Car")) {
                    item = (Car) itm;
                    intent = new Intent(Main.this, CarActivity.class);
                    item.toIntent(intent);
                    intent.setAction(ACTION_EDIT);
                    startActivityForResult(intent, CALLCAR);
                } else if (className.equals("ak.logbook.Part")) {
                    item = (Part) itm;
                    intent = new Intent(Main.this, PartActivity.class);
                    item.toIntent(intent);
                    intent.setAction(ACTION_EDIT);
                    startActivityForResult(intent, CALLPART);
                } else if (className.equals("ak.logbook.Currency")) {
                    item = (Currency) itm;
                    intent = new Intent(Main.this, CurrencyActivity.class);
                    item.toIntent(intent);
                    intent.setAction(ACTION_EDIT);
                    startActivityForResult(intent, CALLCURRENCY);
                } else if (className.equals("ak.logbook.Provider")) {
                    item = (Provider) itm;
                    intent = new Intent(Main.this, ProviderActivity.class);
                    item.toIntent(intent);
                    intent.setAction(ACTION_EDIT);
                    startActivityForResult(intent, CALLPROVIDER);
                } else if (className.equals("ak.logbook.Category")) {
                    item = (Category) itm;
                    intent = new Intent(Main.this, CategoryActivity.class);
                    item.toIntent(intent);
                    intent.setAction(ACTION_EDIT);
                    startActivityForResult(intent, CALLCATEGORY);
                }
        }

    }

    private void DispCatalogRoot() {
        ArrayList<Item> Array;
        setTitle(itm.toString());
        supportInvalidateOptionsMenu();
        Catalog cat = (Catalog) itm;
        String child = cat.getChildName();
        itm = itemFactory.getItemOnClass(child, context);
        Array = getArrayFromTable(itm.getTblName(), itm.getColumns());
        FragmentLogList frag = (FragmentLogList) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        AdapterforFrag = new ItemAdapter(this, itm.getRawLayout(), Array);
        frag.dispList(AdapterforFrag);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        super.onActivityResult(requestCode, resultCode, resultIntent);
        switch (requestCode) {
            case CALLCAR:
                switch (resultCode) {
                    case RESULT_OK:
                        Car car = new Car(context);
                        car.setItem(resultIntent);
                        if (resultIntent.getAction().equals(ACTION_ADD)){
                            addRowtoDB(car.getCV(),car.getTblName());
                        } else {
                            updRowInDB(car.getCV(), car.getId(), car.getTblName());
                        }
                        className = car.getClass().getName();
                        break;
                    case RESULT_CANCELED:
                        break;
                }
                break;
            case CALLCATEGORY:
                switch (resultCode) {
                    case RESULT_OK:
                        Category category = new Category(context);
                        category.setItem(resultIntent);
                        if (resultIntent.getAction().equals(ACTION_ADD)){
                            addRowtoDB(category.getCV(),category.getTblName());
                        } else {
                            updRowInDB(category.getCV(), category.getId(), category.getTblName());
                        }
                        className = category.getClass().getName();
                        break;
                    case RESULT_CANCELED:
                        break;
                }
                break;
            case CALLCURRENCY:
                switch (resultCode) {
                    case RESULT_OK:
                        Currency currency = new Currency(context);
                        currency.setItem(resultIntent);
                        if (resultIntent.getAction().equals(ACTION_ADD)){
                            addRowtoDB(currency.getCV(),currency.getTblName());
                        } else {
                            updRowInDB(currency.getCV(), currency.getId(), currency.getTblName());
                        }
                        className = currency.getClass().getName();
                        break;
                    case RESULT_CANCELED:
                        break;
                }
                break;
            case CALLPART:
                switch (resultCode) {
                    case RESULT_OK:
                        Part part = new Part(context);
                        part.setItem(resultIntent);
                        if (resultIntent.getAction().equals(ACTION_ADD)){
                            addRowtoDB(part.getCV(),part.getTblName());
                        } else {
                            updRowInDB(part.getCV(), part.getId(), part.getTblName());
                        }
                        className = part.getClass().getName();
                        break;
                    case RESULT_CANCELED:
                        break;
                }
                break;
            case CALLPROVIDER:
                switch (resultCode) {
                    case RESULT_OK:
                        Provider provider = new Provider(context);
                        provider.setItem(resultIntent);
                        if (resultIntent.getAction().equals(ACTION_ADD)){
                            addRowtoDB(provider.getCV(),provider.getTblName());
                        } else {
                            updRowInDB(provider.getCV(), provider.getId(), provider.getTblName());
                        }
                        className = provider.getClass().getName();
                        break;
                    case RESULT_CANCELED:
                        break;
                }
                break;
            case CALLEVENT:
                switch (resultCode) {
                    case RESULT_OK:
                        break;
                    case RESULT_CANCELED:
                        break;
                }
                break;


        }
    }


    public long addRowtoDB(ContentValues CV, String TableName) {
        long ID;
        db = new logbookDB (this);
        dbs = db.getWritableDatabase();
        if (Main.enableLog)
            Log.w("ak.Log", "Запись в " + TableName);
        ID = dbs.insert(TableName, null, CV);
        dbs.close();
        db.close();
        return ID;
    }

    public void delRowfromDB(long id, String TableName) {
//		int count;
//		db = new logbookDB (this);
//		dbs = db.getWritableDatabase();
        dbs.delete(TableName, "_id=" + id, null);
        if (Main.enableLog)
            Log.w("ak.Log", "Удаление из " + TableName + " по id=" + id);

//		dbs.close();
//		db.close();
        // return count;
    }

    public void updRowInDB(ContentValues CV, long id, String TableName) {
        db = new logbookDB (this);
        dbs = db.getWritableDatabase();
        if (Main.enableLog)
            Log.w("ak.Log", "Обновлена " + TableName + " по id=" + id);
        dbs.update(TableName, CV, "_id=" + id, null);
        dbs.close();
        db.close();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (Main.enableLog)
            Log.w("ak.Log", "onPause Main");
        dbs.close();
        db.close();
		/*
		 * Editor settingsEditor = Settings.edit();
		 * settingsEditor.putInt(COLOR_PREF, backColor);
		 * settingsEditor.commit();
		 */
    }


    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch(keycode) {
            case KeyEvent.KEYCODE_BACK:
                if (Main.enableLog)
                    Log.w("ak.Log", "KeyEvent.KEYCODE_BACK");
                actionBack();
                return true;
        }
        return super.onKeyDown(keycode, e);
    }

    private void actionBack() {
        switch (menuLevel) {
            case 0:
                finish();
                break;
            case 1:
                menuLevel = 0;
                supportInvalidateOptionsMenu();
                selectDrawerItem(posInDrawer);
                break;
            case 2:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (posInDrawer){
            case LOGLISTFRAG:
                getMenuInflater().inflate(R.menu.catalogmenu, menu);
                break;
            case CATALOGFRAG:
                if (menuLevel!=0){
                    getMenuInflater().inflate(R.menu.catalogmenu, menu);
                }
                break;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (posInDrawer){
            case LOGLISTFRAG:
                LogListMenuItemOnClick(item);
                break;
            case CATALOGFRAG:
                CatalogMenuItemOnClick(item);
                break;
        }
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void LogListMenuItemOnClick (MenuItem menuitem){
        switch (menuitem.getItemId()){
//           case android.R.id.home:
//                Toast.makeText(this,menuitem.toString()+" Home",Toast.LENGTH_SHORT).show();
//                break;
            case R.id.addbutton:
                callEventforAdd();
                break;
        }
    }

    private void CatalogMenuItemOnClick(MenuItem menuitem) {
        Item item = null;
        String tmpName = itm.getName();
        switch (menuitem.getItemId()){
//           case android.R.id.home:
//                Toast.makeText(this,menuitem.toString()+" Home",Toast.LENGTH_SHORT).show();
//                break;
            case R.id.addbutton:
                if (menuLevel==1) {
                    if (Main.enableLog) Log.w("ak.Log", "ACTION_ADD itm - " + tmpName);
                    if (tmpName.equals("ak.logbook.Car")){
                        Intent intent = new Intent(Main.this, CarActivity.class);
                        intent.setAction(ACTION_ADD);
                        startActivityForResult(intent, CALLCAR);
                    } else
                    if (tmpName.equals("ak.logbook.Category")){
                        Intent intent = new Intent(Main.this, CategoryActivity.class);
                        intent.setAction(ACTION_ADD);
                        startActivityForResult(intent, CALLCATEGORY);
                    } else
                    if (tmpName.equals("ak.logbook.Currency")){
                        Intent intent = new Intent(Main.this, CurrencyActivity.class);
                        intent.setAction(ACTION_ADD);
                        startActivityForResult(intent, CALLCURRENCY);
                    } else
                    if (tmpName.equals("ak.logbook.Part")){
                        Intent intent = new Intent(Main.this, PartActivity.class);
                        intent.setAction(ACTION_ADD);
                        startActivityForResult(intent, CALLPART);
                    } else
                    if (tmpName.equals("ak.logbook.Provider")){
                        Intent intent = new Intent(Main.this, ProviderActivity.class);
                        intent.setAction(ACTION_ADD);
                        startActivityForResult(intent, CALLPROVIDER);
                    }
                }
                break;
            case R.id.delbutton:
                if (menuLevel==1){
                    if (tmpName.equals("ak.logbook.Car")){
                        item = (Car) itm;
                    } else
                    if (tmpName.equals("ak.logbook.Category")){
                        item = (Category) itm;
                    } else
                    if (tmpName.equals("ak.logbook.Currency")){
                        item = (Currency) itm;
                    } else
                    if (tmpName.equals("ak.logbook.Part")){
                        item = (Part) itm;
                    } else
                    if (tmpName.equals("ak.logbook.Provider")){
                        item = (Provider) itm;
                    }
                    delRowfromDB(item.getId(), item.getTblName());
                    selectDrawerItem(posInDrawer);
                }

                break;

        }
    }


    private final class Amodes implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//            menu.add("удалить").setIcon().setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            if (menuLevel!=0)
                getMenuInflater().inflate(R.menu.catalogamodes, menu);
            isAmode=true;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
            if (Main.enableLog)
                Log.w("ak.Log", "onActionItemClicked");
            CatalogMenuItemOnClick(menuItem);
            mode.finish();
            isAmode=false;
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (Main.enableLog)
                Log.w("ak.Log", "onDestroyActionMode");
            isAmode=false;
        }
    }


    private void callEventforEdit (long eventId){
        Intent callIntent = new Intent (Main.this, EventActivity.class);
        callIntent.putExtra(EXTRA_EVENTID, eventId);
        callIntent.setAction(ACTION_EDIT);
        startActivityForResult(callIntent, CALLEVENT);
    }

    public void callEventforAdd() {
        Intent callIntent = new Intent(Main.this, EventActivity.class);
        callIntent.setAction(ACTION_ADD);
        startActivityForResult(callIntent, CALLEVENT);

    }


}

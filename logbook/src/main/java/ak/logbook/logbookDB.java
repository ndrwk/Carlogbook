package ak.logbook;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

public class logbookDB extends SQLiteOpenHelper {

// TODO таблица с наблюдениями для ведения истории неисправностей и событий

	private static final String DB_NAME="logbook.db";
	private static final int DB_VERSION = 1;
// TODO значки в меню
	private final String SQL_CREATE []= {
        Table_Catalog.TABLECREATESTRING,
        "INSERT INTO "+Table_Catalog.TableName+" VALUES (1,'Автомобили',"+56+",'ak.logbook.Car');",
        "INSERT INTO "+Table_Catalog.TableName+" VALUES (2,'Статьи расходов',"+521+",'ak.logbook.Category');",
        "INSERT INTO "+Table_Catalog.TableName+" VALUES (3,'Валюта',"+373+",'ak.logbook.Currency');",
        "INSERT INTO "+Table_Catalog.TableName+" VALUES (4,'Расходы',"+852+",'ak.logbook.Part');",
        "INSERT INTO "+Table_Catalog.TableName+" VALUES (5,'Контрагенты',"+175+",'ak.logbook.Provider');",
        Table_Car.TABLECREATESTRING,
//        "INSERT INTO "+Table_Car.TableName+" VALUES (1,'Mercedes','230e',7,'КАКОЙ-ТО VIN',1,1000);",
        Table_Category.TABLECREATESTRING,
//        "INSERT INTO "+Table_Category.TableName+" VALUES(1,'ТО',904,-16763393);",
//        "INSERT INTO "+Table_Category.TableName+" VALUES(2,'Заправка',450,-6684466);",
//        "INSERT INTO "+Table_Category.TableName+" VALUES(3,'Мойка',1072,-9583616);",
//        "INSERT INTO "+Table_Category.TableName+" VALUES(4,'Страховка',905,-29952);",
//        "INSERT INTO "+Table_Category.TableName+" VALUES(5,'Шиномонтаж',873,-65536);",
//        "INSERT INTO "+Table_Category.TableName+" VALUES(6,'Ремонт',479,-5189132);",
//        "INSERT INTO "+Table_Category.TableName+" VALUES(7,'Штрафы',17,-2313241);",
//        "INSERT INTO "+Table_Category.TableName+" VALUES(8,'Налоги',1251,-3215684);",
        Table_Currency.TABLECREATESTRING,
//        "INSERT INTO "+Table_Currency.TableName+" VALUES (1,'руб.',1,1198);",
        Table_Part.TABLECREATESTRING,
//        "INSERT INTO "+Table_Part.TableName+" VALUES(1,'Работа', null);",
//        "INSERT INTO "+Table_Part.TableName+" VALUES(2,'Бензин А92', null);",
        Table_Provider.TABLECREATESTRING,
//        "INSERT INTO "+Table_Provider.TableName+" VALUES (1, 'Топлайн', '551111', 'ул. Зеленая');",
        Table_Event.TABLECREATESTRING,
        Table_Record.TABLECREATESTRING};

/*
            Table_Catalog.TABLECREATESTRING,
            "INSERT INTO "+Table_Catalog.TableName+" VALUES (1,'Автомобили',"+56+",'ak.logbook.Car');",
            "INSERT INTO "+Table_Catalog.TableName+" VALUES (2,'Статьи расходов',"+521+",'ak.logbook.Category');",
            "INSERT INTO "+Table_Catalog.TableName+" VALUES (3,'Валюта',"+373+",'ak.logbook.Currency');",
            "INSERT INTO "+Table_Catalog.TableName+" VALUES (4,'Расходы',"+852+",'ak.logbook.Part');",
            "INSERT INTO "+Table_Catalog.TableName+" VALUES (5,'Контрагенты',"+175+",'ak.logbook.Provider');",
            Table_Car.TABLECREATESTRING,
            "INSERT INTO "+Table_Car.TableName+" VALUES (1,'ведро','ржавое',7,'КАКОЙ-ТО VIN',1,1000);",
            "INSERT INTO "+Table_Car.TableName+" VALUES (2,'ведро','оцинкованное',7,'КАКОЙ-ТО VIN',1,3000);",
            "INSERT INTO "+Table_Car.TableName+" VALUES (3,'ведро','эмалированное',10,'КАКОЙ-ТО VIN',1,4000);",
            Table_Category.TABLECREATESTRING,
            "INSERT INTO "+Table_Category.TableName+" VALUES(1,'ТО',904,-16763393);",
            "INSERT INTO "+Table_Category.TableName+" VALUES(2,'Заправка',450,-6684466);",
            "INSERT INTO "+Table_Category.TableName+" VALUES(3,'Мойка',1072,-9583616);",
            "INSERT INTO "+Table_Category.TableName+" VALUES(4,'Страховка',905,-29952);",
            "INSERT INTO "+Table_Category.TableName+" VALUES(5,'Шиномонтаж',873,-65536);",
            "INSERT INTO "+Table_Category.TableName+" VALUES(6,'Ремонт',479,-5189132);",
            "INSERT INTO "+Table_Category.TableName+" VALUES(7,'Штрафы',17,-2313241);",
            "INSERT INTO "+Table_Category.TableName+" VALUES(8,'Налоги',1251,-3215684);",

            Table_Currency.TABLECREATESTRING,
            "INSERT INTO "+Table_Currency.TableName+" VALUES (1,'руб.',1,1198);",
            "INSERT INTO "+Table_Currency.TableName+" VALUES (2,'тэнге',0.21,1288);",
            Table_Part.TABLECREATESTRING,
            "INSERT INTO "+Table_Part.TableName+" VALUES(1,'Работа', null);",
            "INSERT INTO "+Table_Part.TableName+" VALUES(2,'Бензин А92', null);",
            "INSERT INTO "+Table_Part.TableName+" VALUES(3,'Бензин А95', null);",
            "INSERT INTO "+Table_Part.TableName+" VALUES(4,'Бензин А80', null);",
            "INSERT INTO "+Table_Part.TableName+" VALUES(5,'Газ', null);",
            "INSERT INTO "+Table_Part.TableName+" VALUES(6,'ДТ', null);",
            "INSERT INTO "+Table_Part.TableName+" VALUES(7,'Масло 5W-40', null);",
            "INSERT INTO "+Table_Part.TableName+" VALUES(8,'Фильтр масляный', null);",
            "INSERT INTO "+Table_Part.TableName+" VALUES(9,'фильтр воздушный', null);",
            "INSERT INTO "+Table_Part.TableName+" VALUES(10,'фильтр салонный', null);",
            Table_Provider.TABLECREATESTRING,
            "INSERT INTO "+Table_Provider.TableName+" VALUES (1, 'Топлайн', '551111', 'ул. Зеленая');",
            "INSERT INTO "+Table_Provider.TableName+" VALUES (2, 'Exist', '551122', 'ул. Синяя');",
            "INSERT INTO "+Table_Provider.TableName+" VALUES (3, 'автомойка', '551133', 'ул. Красная');",
            "INSERT INTO "+Table_Provider.TableName+" VALUES (4, 'автосервис', '551144', 'ул. Желтая');",
            "INSERT INTO "+Table_Provider.TableName+" VALUES (5, 'страховая компания', '551155', 'ул. Фиолетовая');",
            "INSERT INTO "+Table_Provider.TableName+" VALUES (6, 'шиномонтажка', '551166', 'ул. Оранжевая');",
            "INSERT INTO "+Table_Provider.TableName+" VALUES (7, 'дилер KIA', '551177', 'ул. Голубая');",

            Table_Event.TABLECREATESTRING,

            Table_Record.TABLECREATESTRING,

            "INSERT INTO "+Table_Event.TableName+" VALUES (1,1,100113,122,1);",
            "INSERT INTO "+Table_Event.TableName+" VALUES (2,1,220213,13333,2);",

            "INSERT INTO "+Table_Record.TableName+" VALUES (1,1,1,2,30,700,1,'запись1');",
            "INSERT INTO "+Table_Record.TableName+" VALUES (2,2,2,2,30,700,1,'запись2');"};
*/

    public logbookDB (Context context){
		super (context, DB_NAME, null, DB_VERSION);
		}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		for (int i=0; i<SQL_CREATE.length; i++){
//		Log.w("ak.Log", "i="+i+" "+SQL_CREATE[i]);
		db.execSQL(SQL_CREATE[i]);
//		Log.w("ak.Log", "done");
		}
//		Log.w("ak.Log", "Создание БД версии " + DB_VERSION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("ak.Log", "Обновление БД с версии " + oldVersion
				+ " до " + newVersion);
//		db.execSQL(SQL_DELETE);
	//	onCreate(db);
	}

}

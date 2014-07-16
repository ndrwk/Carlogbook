package ak.logbook;

import android.content.Context;
import android.util.Log;

public class ItemFactory {

	public Item getItemOnClass(String Name, Context cntxt){
		if (Name.equals("ak.logbook.Car")) return new Car(cntxt);
        else if (Name.equals("ak.logbook.Catalog")) return new Catalog(cntxt);
         else if (Name.equals("ak.logbook.Category")) return new Category(cntxt);
          else if (Name.equals("ak.logbook.Currency")) return new Currency(cntxt);
           else if (Name.equals("ak.logbook.Part")) return new Part(cntxt);
            else if (Name.equals("ak.logbook.Provider")) return new Provider(cntxt);
		     else return null;
	}
    public Item getItemOnTableName(String Name, Context cntxt){
        if (Name.equals(Table_Car.TableName)) return new Car(cntxt);
        else if (Name.equals(Table_Catalog.TableName)) return new Catalog(cntxt);
         else if (Name.equals(Table_Category.TableName)) return new Category(cntxt);
          else if (Name.equals(Table_Currency.TableName)) return new Currency(cntxt);
           else if (Name.equals(Table_Part.TableName)) return new Part(cntxt);
            else if (Name.equals(Table_Provider.TableName)) return new Provider(cntxt);
             else return null;
    }

}

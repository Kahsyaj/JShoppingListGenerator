package personnal.ahsyaj.jshoppinglistgenerator.lib.Managers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import personnal.ahsyaj.jshoppinglistgenerator.MainActivity;

public class DbHandler extends SQLiteOpenHelper {

    //Constuctors
    public DbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //Ingredients table
        db.execSQL("CREATE TABLE IF NOT EXISTS Ingredient (id_ingredient INTEGER PRIMARY KEY " +
                "AUTOINCREMENT, name_ingredient TEXT(50) UNIQUE, deleted INTEGER(1) DEFAULT 0)");

        //Meals table
        db.execSQL("CREATE TABLE IF NOT EXISTS Meal (id_meal INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name_meal TEXT(50) UNIQUE, deleted INTEGER(1) DEFAULT 0);");

        //Recipes table
        db.execSQL("CREATE TABLE IF NOT EXISTS Recipe (id_ingredient INTEGER NOT NULL, id_meal " +
                "INTEGER NOT NULL, quantity INTEGER, deleted INTEGER(1) DEFAULT 0, CONSTRAINT Recipte_PK " +
                "PRIMARY KEY (id_meal, id_ingredient), CONSTRAINT Recipe_Meal_FK FOREIGN KEY(id_meal) " +
                "REFERENCES Meal(id_meal), CONSTRAINT Recipe_Ingredient_FK FOREIGN KEY (id_ingredient) " +
                "REFERENCES Ingredient(id_ingredient));");

        //ShoppingLists table
        db.execSQL("CREATE TABLE IF NOT EXISTS ShoppingList (id_shoppinglist INTEGER PRIMARY KEY " +
                "AUTOINCREMENT, date_shoppinglist TEXT, deleted INTEGER(1) DEFAULT 0, CONSTRAINT " +
                "ShoppingList_PK PRIMARY KEY (id_shoppinglist));");

        //Purchases table
        db.execSQL("CREATE TABLE IF NOT EXISTS Purchase ( id_shoppinglist INTEGER NOT NULL, " +
                "id_meal INTEGER NOT NULL, deleted INTEGER(1) DEFAULT 0, " +
                "CONSTRAINT Purchase_PK PRIMARY KEY (id_shoppinglist, id_meal), CONSTRAINT " +
                "Purchase_Meal_FK FOREIGN KEY (id_meal) REFERENCES Meal(id_meal), " +
                "CONSTRAINT Purchase_ShoppingList_FK FOREIGN KEY (id_shoppinglist) REFERENCES " +
                "ShoppingList(id_shoppinglist));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS Ingredient;");
        db.execSQL("DROP TABLE IF EXISTS Meal;");
        db.execSQL("DROP TABLE IF EXISTS Recipe;");
        db.execSQL("DROP TABLE IF EXISTS Purchase;");
    }
}

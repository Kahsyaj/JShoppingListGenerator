package personnal.ahsyaj.jshoppinglistgenerator.lib.Managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.text.Editable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Ingredient;

public class IngredientManager extends Manager {
    private String[] EDIT_FIELDS = {"name_ingredient"};
    private String[] UNEDIT_FIELDS = {"id_ingredient", "deleted"};

    //Constructors
    public IngredientManager(Context context) {
        super(context);
        this.setTable("Ingredient");
    }

    public IngredientManager() {
        super();
        this.setTable("Ingredient");
    }

    //Other methods
    public boolean dbCreate(Ingredient ingredient) {
        try {
            int currentId = this.getCurrentId();
            ContentValues data = new ContentValues();

            data.put(UNEDIT_FIELDS[0], currentId);
            data.put(UNEDIT_FIELDS[1], ingredient.getName());
            this.database.insert(this.table, null, data);
            ingredient.setId(currentId);
            return true;
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s creating.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean dbUpdate(Ingredient ingredient) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", UNEDIT_FIELDS[0]);
            String[] whereArgs = {String.valueOf(ingredient.getId())};

            data.put(EDIT_FIELDS[0], ingredient.getName());
            return (this.database.update(this.table, data, whereClause, whereArgs) != 0);
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s updating.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public Ingredient dbLoad(int id) {
        try {
            String[] selectArgs = {String.valueOf(id)};
            Cursor rslt = this.database.rawQuery(String.format("SELECT * FROM %s WHERE %s = ? AND %s = 0",
                    this.getTable(), UNEDIT_FIELDS[0], UNEDIT_FIELDS[1]), selectArgs);

            rslt.moveToNext();
            return new Ingredient(rslt, true);
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s loading.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }

    public Ingredient dbLoad(String name) {
        try {
            String[] selectArgs = {name};
            Cursor rslt = this.database.rawQuery(String.format("SELECT * FROM %s WHERE %s = ? AND %s = 0",
                    this.getTable(), EDIT_FIELDS[0], UNEDIT_FIELDS[1]), selectArgs);

            rslt.moveToNext();
            return new Ingredient(rslt, true);
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s loading.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }

    public ArrayList<Ingredient> dbLoadAll() {
        try {
            ArrayList<Ingredient> ingLst = new ArrayList<>();
            Cursor rslt = this.database.rawQuery(String.format("SELECT * FROM %s WHERE %s = 0",
                    this.getTable(), UNEDIT_FIELDS[1]), null);

            while (rslt.moveToNext()) {
                ingLst.add(new Ingredient(rslt, false));
            }
            rslt.close();
            return ingLst;
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the whole %s loading.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }
}

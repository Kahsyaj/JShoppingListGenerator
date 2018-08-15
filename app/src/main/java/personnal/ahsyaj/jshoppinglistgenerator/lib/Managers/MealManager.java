package personnal.ahsyaj.jshoppinglistgenerator.lib.Managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Meal;

public class MealManager extends Manager {
    private String[] EDIT_FIELDS = {"name_meal"};
    private String[] UNEDIT_FIELDS = {"id_meal", "deleted"};

    //Constructors
    public MealManager(Context context) {
        super(context);
        this.setTable("Meal");
    }

    public MealManager() {
        super();
        this.setTable("Meal");
    }

    //Other methods
    public boolean dbCreate(Meal meal) {
        try {
            int currentId = this.getCurrentId();
            ContentValues data = new ContentValues();

            data.put(UNEDIT_FIELDS[0], currentId);
            data.put(EDIT_FIELDS[0], meal.getName());

            this.database.insert(this.table, null, data);

            meal.setId(currentId);

            return true;
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s creating.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public boolean fullDbCreate(Meal meal) {
        try {
            RecipeManager rcp_mgr = new RecipeManager();

            return (this.dbCreate(meal) && rcp_mgr.dbCreate(meal.getRecipe()));
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s full creating.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public boolean dbUpdate(Meal meal) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", UNEDIT_FIELDS[0]);
            String[] whereArgs = {String.valueOf(meal.getId())};

            data.put(EDIT_FIELDS[0], meal.getName());

            return (this.database.update(this.table, data, whereClause, whereArgs) != 0);
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s updating.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public boolean fullDbUpdate(Meal meal) {
        try {
            RecipeManager rcp_mgr = new RecipeManager();

            return (rcp_mgr.dbUpdate(meal.getRecipe()) && this.dbUpdate(meal));
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s full updating.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public boolean fullDbSoftDelete(Meal meal) {
        try {
            return this.fullDbSoftDelete(meal.getId());
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the full %s soft deletion.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public boolean fullDbHardDelete(Meal meal) {
        try {
            RecipeManager rcp_mgr = new RecipeManager();

            return (rcp_mgr.dbHardDelete(meal.getRecipe()) && this.dbHardDelete(meal));
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the full %s hard deletion.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public boolean fullDbSoftDelete(int id) {
        try {
            RecipeManager rcp_mgr = new RecipeManager();

            return (rcp_mgr.dbSoftDelete(id) && this.dbSoftDelete(id));
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the full %s soft deletion.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public boolean fullDbHardDelete(int id) {
        try {
            RecipeManager rcp_mgr = new RecipeManager();

            return (rcp_mgr.dbHardDelete(id) && this.dbHardDelete(id));
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the full %s hard deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public Meal dbLoad(int id) {
        try {
            String[] selectArgs = {String.valueOf(id)};
            Cursor rslt = this.database.rawQuery(String.format("SELECT * FROM %s WHERE %s = ? AND %s = 0",
                    this.getTable(), UNEDIT_FIELDS[0], UNEDIT_FIELDS[1]), selectArgs);

            rslt.moveToNext();
            return new Meal(rslt, true);

        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s loading.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }

    public Meal dbLoad(String name) {
        try {
            String[] selectArgs = {name};
            Cursor rslt = this.database.rawQuery(String.format("SELECT * FROM %s WHERE %s = ? AND %s = 0",
                    this.getTable(), EDIT_FIELDS[0], UNEDIT_FIELDS[1]), selectArgs);

            rslt.moveToNext();
            return new Meal(rslt, true);
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s loading.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }

    public ArrayList<Meal> dbLoadAll() {
        try {
            ArrayList<Meal> mealLst = new ArrayList<>();
            Cursor rslt = this.database.rawQuery(String.format("SELECT * FROM %s WHERE %s = 0", this.getTable(), UNEDIT_FIELDS[1]), null);

            while (rslt.moveToNext()) {
                mealLst.add(new Meal(rslt, false));
            }
            rslt.close();
            return mealLst;
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the whole %s loading.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }

    public boolean fullRestoreSoftDeleted(int id) {
        try {
            RecipeManager rcp_mgr = new RecipeManager();

            return (rcp_mgr.restoreSoftDeleted(id)) && (this.restoreSoftDeleted(id));
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the soft deleted %s full restoring.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }
}

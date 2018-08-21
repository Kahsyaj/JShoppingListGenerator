package personnal.ahsyaj.jshoppinglistgenerator.lib.Managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import java.util.ArrayList;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Entity;
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
    public boolean dbCreate(Entity elt) {
        try {
            Meal meal = (Meal) elt;
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

    public boolean fullDbCreate(Entity elt) {
        try {
            Meal meal = (Meal) elt;
            RecipeManager rcp_mgr = new RecipeManager();

            return (this.dbCreate(meal) && rcp_mgr.dbCreate(meal.getRecipe()));
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s full creating.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public boolean dbUpdate(Entity elt) {
        try {
            Meal meal = (Meal) elt;
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

    public boolean fullDbUpdate(Entity elt) {
        try {
            Meal meal = (Meal) elt;
            RecipeManager rcp_mgr = new RecipeManager();

            return (rcp_mgr.dbUpdate(meal.getRecipe()) && this.dbUpdate(meal));
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s full updating.\n", this.getTable()) + e.getMessage());

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

    public ArrayList<Entity> dbLoadAll() {
        try {
            ArrayList<Entity> mealLst = new ArrayList<>();
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

    public boolean dbSoftDelete(int id) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", UNEDIT_FIELDS[0]);
            String[] whereArgs = {String.valueOf(id)};

            data.put(UNEDIT_FIELDS[1], 1);
            return (this.database.update(this.table, data, whereClause, whereArgs) != 0);
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s soft deletion.\n", this.getTable()) + e.getMessage());
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

    public boolean dbSoftDelete(Entity element) {
        try {
            return this.dbSoftDelete(element.getId());
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s soft deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbSoftDelete(Entity elt) {
        try {
            return this.fullDbSoftDelete(elt.getId());
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the full %s soft deletion.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public boolean dbHardDelete(int id) {
        try {
            String whereClause = String.format("%s = ?", UNEDIT_FIELDS[0]);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return (this.database.delete(this.table, whereClause, whereArgs) != 0);
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s hard deletion.\n", this.getTable()) + e.getMessage());
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

    public boolean dbHardDelete(Entity elt) {
        try {
            return this.dbHardDelete(elt.getId());
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s hard deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbHardDelete(Entity elt) {
        try {
            return this.fullDbHardDelete(elt.getId());
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the full %s hard deletion.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public boolean restoreSoftDeleted(int id) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("deleted = ? AND %s = ?", UNEDIT_FIELDS[0]);
            String[] whereArgs = {"1", String.valueOf(id)};

            data.put(UNEDIT_FIELDS[1], 0);
            return (this.database.update(this.table, data, whereClause, whereArgs) != 1);
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the soft deleted %s restoring.\n", this.getTable()) + e.getMessage());
            return false;
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

    public int getCurrentId() {
        try {
            Cursor rslt = this.database.rawQuery(String.format("SELECT MAX(%s) as %s FROM %s",
                    UNEDIT_FIELDS[0], UNEDIT_FIELDS[0], this.getTable()), null);
            if (rslt == null || !rslt.moveToNext()) {
                return 1;
            }
            int currentId = rslt.getInt(0) + 1;
            rslt.close();
            return currentId;
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s id querying.\n", this.getTable()) + e.getMessage());
            return 0;
        }
    }

    public ArrayList<Integer> getIds() {
        try {
            ArrayList<Integer> idLst = new ArrayList<>();
            Cursor rslt = this.database.rawQuery(String.format("SELECT %s FROM %s WHERE %s = 0",
                    UNEDIT_FIELDS[0], this.getTable(), UNEDIT_FIELDS[1]), null);

            while (rslt.moveToNext()) {
                idLst.add(rslt.getInt(0));
            }
            if (idLst.size() == 0) {
                return null;
            } else {
                return idLst;
            }
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s ids querying.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }

    public String className() {
        return "MealManager";
    }
}

package personnal.ahsyaj.jshoppinglistgenerator.lib.Managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteException;
import java.util.ArrayList;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Entity;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Ingredient;

public final class IngredientManager extends Manager {
    private static final String[] EDIT_FIELDS = {"name_ingredient"};
    private static final String[] UNEDIT_FIELDS = {"id_ingredient", "deleted"};

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
    public boolean dbCreate(Entity elt) {
        try {
            Ingredient ingredient = (Ingredient) elt;
            int currentId = this.getCurrentId();
            ContentValues data = new ContentValues();

            data.put(UNEDIT_FIELDS[0], currentId);
            data.put(EDIT_FIELDS[0], ingredient.getName());
            this.database.insertOrThrow(this.table, null, data);
            ingredient.setId(currentId);

            return true;
        } catch (SQLiteConstraintException e) {
            System.err.println(String.format("The %s already exists, it has been restored.\n", this.getTable()) + e.getMessage());
            this.restoreSoftDeleted(((Ingredient) elt).getName());
            elt.setId(this.getId(((Ingredient) elt).getName()));
            
            return true;
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s creating.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public boolean fullDbCreate(Entity elt) {
        return this.dbCreate(elt);
    }

    public boolean dbUpdate(Entity elt) {
        try {
            Ingredient ingredient = (Ingredient) elt;
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", UNEDIT_FIELDS[0]);
            String[] whereArgs = {String.valueOf(ingredient.getId())};

            data.put(EDIT_FIELDS[0], ingredient.getName());
            data.put(UNEDIT_FIELDS[1], 0);

            return (this.database.update(this.table, data, whereClause, whereArgs) != 0);
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s updating.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public boolean fullDbUpdate(Entity elt) {
        return this.dbUpdate(elt);
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
        } catch (CursorIndexOutOfBoundsException e) {
            System.err.println(String.format("An error occurred with the %s loading.\n", this.getTable()) + e.getMessage());

            return null;
        }
    }

    public ArrayList<Entity> dbLoadAll() {
        try {
            ArrayList<Entity> ingLst = new ArrayList<>();
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
        return this.dbSoftDelete(id);
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
        return this.dbSoftDelete(elt);
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
        return this.dbHardDelete(id);
    }

    public boolean dbHardDelete(Entity element) {
        try {
            return this.dbHardDelete(element.getId());
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s hard deletion.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public boolean fullDbHardDelete(Entity elt) {
        return this.dbHardDelete(elt);
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

    public boolean restoreSoftDeleted(String name) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("deleted = ? AND %s = ?", EDIT_FIELDS[0]);
            String[] whereArgs = {"1", name};

            data.put(UNEDIT_FIELDS[1], 0);

            return (this.database.update(this.table, data, whereClause, whereArgs) != 1);
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the soft deleted %s restoring.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public boolean fullRestoreSoftDeleted(int id) {
        return this.restoreSoftDeleted(id);
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

    public int getId(String name) {
        try {
            int id;

            String[] selectArgs = {name};
            Cursor rslt = this.database.rawQuery(String.format("SELECT %s FROM %s WHERE %s = ? AND %s = 0",
                    UNEDIT_FIELDS[0], this.table, EDIT_FIELDS[0], UNEDIT_FIELDS[1]), selectArgs);

            rslt.moveToNext();

            id = rslt.getInt(0);

            rslt.close();

            return id;

        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s id querying.\n", this.getTable()) + e.getMessage());

            return -1;
        }
    }

    public void createOrRestore(Entity elt) {
        try {
            this.fullDbCreate(elt);
        } catch (SQLiteException e) {
            this.restoreSoftDeleted(((Ingredient) elt).getName());
        }
    }

    public boolean isDeleted(Entity entity) {
        String[] selectionArgs = {((Ingredient)entity).getName()};
        String query = String.format("SELECT deleted FROM %s WHERE %s = ?", this.getTable(), EDIT_FIELDS[0]);
        Cursor cursor = this.database.rawQuery(query, selectionArgs);
        boolean deleted = cursor.moveToNext();

        cursor.close();

        return deleted;
    }

    public String className() {
        return "IngredientManager";
    }
}

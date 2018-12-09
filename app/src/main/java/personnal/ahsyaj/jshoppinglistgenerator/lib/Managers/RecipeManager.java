package personnal.ahsyaj.jshoppinglistgenerator.lib.Managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteException;

import java.sql.SQLTransactionRollbackException;
import java.util.ArrayList;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Entity;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Recipe;


public final class RecipeManager extends Manager {
    private static final String[] EDIT_FIELDS = {"id_ingredient", "quantity"};
    private static final String[] UNEDIT_FIELDS = {"id_meal", "deleted"};

    //Constructors
    public RecipeManager(Context context) {
        super(context);
        this.setTable("Recipe");
    }

    public RecipeManager() {
        super();
        this.setTable("Recipe");
    }

    //Other methods
    public boolean dbCreate(Entity elt) {
        try {
            Recipe recipe = (Recipe) elt;
            for (int i = 0; i < recipe.size(); i++) {
                ContentValues data = new ContentValues();

                data.put(UNEDIT_FIELDS[0], recipe.getId());
                data.put(EDIT_FIELDS[0], recipe.getIngredient(i).getId());
                data.put(EDIT_FIELDS[1], recipe.getQuantity(i));
                this.database.insertOrThrow(this.table, null, data);
            }

            return true;
        } catch (SQLiteConstraintException e) {
            System.err.println(String.format("The %s already exists, it has been restored.\n", this.getTable()) + e.getMessage());
            this.restoreSoftDeleted(elt.getId());

            return true;
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s creating.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public boolean fullDbCreate(Entity elt) {
        try {
            Recipe recipe = (Recipe) elt;
            boolean success = true;
            IngredientManager ing_mgr = new IngredientManager();

            for (int i = 0; i < recipe.size(); i++) {
                success = (success && ing_mgr.dbCreate(recipe.getIngredient(i)));
            }
            this.dbCreate(recipe);

            return true;
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s full creating.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    //Pretty dirty... But better than comparing all the ingredients and setting/deleting for each differences
    public boolean dbUpdate(Entity elt) {
        try {
            Recipe recipe = (Recipe) elt;
            this.dbHardDelete(recipe);

            return this.dbCreate(recipe);
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s updating.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public boolean fullDbUpdate(Entity elt) {
        try {
            Recipe recipe = (Recipe) elt;
            boolean success = true;
            IngredientManager ing_mgr = new IngredientManager();

            for (int i = 0; i < recipe.size(); i++) {
                success = (success && ing_mgr.dbUpdate(recipe.getIngredient(i)));
            }

            return this.dbUpdate(recipe);
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s full updating.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public Recipe dbLoad(int id) {
        try {
            String[] selectArgs = {String.valueOf(id)};
            Cursor rslt = this.database.rawQuery(String.format("SELECT * FROM %s WHERE %s = ? AND %s = 0", this.getTable(), UNEDIT_FIELDS[0], UNEDIT_FIELDS[1]), selectArgs);

            rslt.moveToNext();

            return new Recipe(rslt, true);
        } catch (CursorIndexOutOfBoundsException e) {
            System.err.println(String.format("An error occurred with the %s loading.\n", this.getTable()) + e.getMessage());

            return null;
        }
    }

    public ArrayList<Entity> dbLoadAll() {
        try {
            ArrayList<Entity> recipeLst = new ArrayList<>();
            Cursor rslt = this.database.rawQuery(String.format("SELECT * FROM %s WHERE %s = 0", this.getTable(), UNEDIT_FIELDS[1]), null);

            while (rslt.moveToNext()) {
                recipeLst.add(new Recipe(rslt, false));
            }
            rslt.close();

            return recipeLst;
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
            boolean success = true;
            IngredientManager ing_mgr = new IngredientManager();
            Recipe rcp = this.dbLoad(id);

            for (int i = 0; i < rcp.size(); i++) {
                success = (success && ing_mgr.dbSoftDelete(rcp.getIngredient(i)));
            }

            return (success && this.dbSoftDelete(id));
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
            boolean success = this.dbHardDelete(id);
            IngredientManager ing_mgr = new IngredientManager();
            Recipe rcp = this.dbLoad(id);

            for (int i = 0; i < rcp.size(); i++) {
                success = (success && ing_mgr.dbHardDelete(rcp.getIngredient(i)));
            }

            return success;
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the full %s hard deletion.\n", this.getTable()) + e.getMessage());

            return false;
        }
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
        try {
            return this.fullDbHardDelete(elt.getId());
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the full %s hard deletion.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public boolean dbHardDeleteIngredient(Integer id) {
        try {
            String whereClause = String.format("%s = ?", EDIT_FIELDS[0]);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return (this.database.delete(this.table, whereClause, whereArgs) != 0);
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s's ingredients hard deletion.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public boolean dbSoftDeleteIngredient(Integer id) {
        try {
            String whereClause = String.format("%s = ?", EDIT_FIELDS[0]);
            ContentValues data = new ContentValues();
            String[] whereArgs = {String.valueOf(id)};

            data.put(UNEDIT_FIELDS[1], 1);

            return (this.database.update(this.table, data, whereClause, whereArgs) != 0);
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s's ingredients hard deletion.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public boolean restoreSoftDeletedIngredient(int id) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("deleted = ? AND %s = ?", EDIT_FIELDS[0]);
            String[] whereArgs = {"1", String.valueOf(id)};

            data.put(UNEDIT_FIELDS[1], 0);

            return (this.database.update(this.table, data, whereClause, whereArgs) != 1);
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the soft deleted %s's ingredient restoring.\n", this.getTable()) + e.getMessage());

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
            boolean success = true;
            IngredientManager ing_mgr = new IngredientManager();
            Recipe rcp = this.dbLoad(id);

            for (int i = 0; i < rcp.size(); i++) {
                success = (success && ing_mgr.restoreSoftDeleted(rcp.getIngredient(i).getId()));
            }

            return (success && this.restoreSoftDeleted(id));
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

    public void createOrRestore(Entity elt) {
        try {
            this.fullDbCreate(elt);
        } catch (SQLiteException e) {
            this.restoreSoftDeleted(elt.getId());
        }
    }

    public boolean isDeleted(Entity entity) {
        String[] selectionArgs = {String.valueOf(entity.getId())};
        String query = String.format("SELECT deleted FROM %s WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[0]);
        Cursor cursor = this.database.rawQuery(query, selectionArgs);
        boolean deleted = cursor.moveToNext();

        cursor.close();

        return deleted;
    }

    public String className() {
        return "RecipeManager";
    }
}

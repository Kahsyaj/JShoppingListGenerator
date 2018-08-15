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

import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.ShoppingList;

public class ShoppingListManager extends Manager {
    private String[] EDIT_FIELDS = {"date_shoppinglist"};
    private String[] UNEDIT_FIELDS = {"id_shoppinglist", "deleted"};

    //Constructors
    public ShoppingListManager(Context context) {
        super(context);
        this.setTable("ShoppingList");
    }

    public ShoppingListManager() {
        super();
        this.setTable("ShoppingList");
    }

    //Other methods
    public boolean dbCreate(ShoppingList shoppinglist) {
        try {
            int currentId = this.getCurrentId();
            ContentValues data = new ContentValues();

            data.put(UNEDIT_FIELDS[0], currentId);
            data.put(EDIT_FIELDS[0], shoppinglist.getDate());
            this.database.insert(this.table, null, data);
            shoppinglist.setId(currentId);
            return true;
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s creating.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean FullDbCreate(ShoppingList shoppinglist) {
        try {
            boolean success = this.dbCreate(shoppinglist);
            PurchaseManager p_mgr = new PurchaseManager();

            return (success && p_mgr.fullDbCreate(shoppinglist.getPurchase()));
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s full creating.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean dbUpdate(ShoppingList shoppinglist) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", UNEDIT_FIELDS[0]);
            String[] whereArgs = {String.valueOf(shoppinglist.getId())};

            data.put(EDIT_FIELDS[0], shoppinglist.getDate());
            return (this.database.update(this.table, data, whereClause, whereArgs) != 0);
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s updating.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbUpdate(ShoppingList shoppinglist) {
        try {
            PurchaseManager p_mgr = new PurchaseManager();
            boolean success = p_mgr.fullDbUpdate(shoppinglist.getPurchase());

            return (success && this.dbUpdate(shoppinglist));
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s full updating.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbSoftDelete(ShoppingList shoppinglist) {
        try {
            return this.fullDbSoftDelete(shoppinglist.getId());
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the full %s soft deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbHardDelete(ShoppingList shoppinglist) {
        try {
            return this.fullDbHardDelete(shoppinglist.getId());
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the full %s hard deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbSoftDelete(int id) {
        try {
            boolean success = this.dbSoftDelete(id);
            PurchaseManager p_mgr = new PurchaseManager();

            return (success && p_mgr.fullDbSoftDelete(id));
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the full %s soft deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbHardDelete(int id) {
        try {
            PurchaseManager p_mgr = new PurchaseManager();
            boolean success = p_mgr.fullDbHardDelete(id);

            return (success && this.dbHardDelete(id));
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the full %s hard deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public ShoppingList dbLoad(int id) {
        try {
            String[] selectArgs = {String.valueOf(id)};
            Cursor rslt = this.database.rawQuery(String.format("SELECT * FROM %s WHERE %s = ? AND %s = 0",
                    this.getTable(), UNEDIT_FIELDS[0], UNEDIT_FIELDS[1]), selectArgs);
            PurchaseManager p_mgr = new PurchaseManager();

            rslt.moveToNext();
            return new ShoppingList(rslt, true);
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s loading.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }

    public ArrayList<ShoppingList> dbLoadAll() {
        try {
            Cursor rslt = this.database.rawQuery(String.format("SELECT * FROM %s WHERE %s = 0",
                    this.getTable(), UNEDIT_FIELDS[1]), null);
            ArrayList<ShoppingList> shpgLst = new ArrayList<>();

            while (rslt.moveToNext()) {
                shpgLst.add(new ShoppingList(rslt, false));
            }
            rslt.close();
            return shpgLst;
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the whole %s loading.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }

    public boolean fullRestoreSoftDeleted(int id) {
        try {
            boolean success = this.restoreSoftDeleted(id);
            PurchaseManager p_mgr = new PurchaseManager();

            return (success && p_mgr.fullRestoreSoftDeleted(id));
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the soft deleted %s full restoring.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }
}

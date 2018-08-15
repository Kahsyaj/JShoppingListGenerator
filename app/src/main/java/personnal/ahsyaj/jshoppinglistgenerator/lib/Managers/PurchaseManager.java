package personnal.ahsyaj.jshoppinglistgenerator.lib.Managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.widget.EdgeEffect;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Meal;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Purchase;

public class PurchaseManager extends Manager {
    private String[] EDIT_FIELDS = {"id_meal"};
    private String[] UNEDIT_FIELDS = {"id_shoppinglist", "deleted"};

    //Constructors
    public PurchaseManager(Context context) {
        super(context);
        this.setTable("Purchase");
    }

    public PurchaseManager() {
        super();
        this.setTable("Purchase");
    }

    //Other methods
    public boolean dbCreate(Purchase purchase) {
        try {
            for (int i = 0; i < purchase.size(); i++) {
                Meal currentMeal = purchase.getMeals().get(i);
                ContentValues data = new ContentValues();

                data.put(UNEDIT_FIELDS[0], purchase.getId());
                data.put(EDIT_FIELDS[0], currentMeal.getId());
                this.database.insert(this.table, null, data);
            }
            return true;
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s creating.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbCreate(Purchase purchase) {
        try {
            boolean success = true;
            MealManager m_mgr = new MealManager();

            for (int i = 0; i < purchase.getMeals().size(); i++) {
                Meal currentMeal = purchase.getMeals().get(i);
                success = (success && m_mgr.fullDbCreate(currentMeal));
            }
            return (success && this.dbCreate(purchase));
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s full creating.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean dbUpdate(Purchase purchase) {
        try {
            this.dbHardDelete(purchase);
            return this.dbCreate(purchase);
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s updating.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbUpdate(Purchase purchase) {
        try {
            boolean success = true;
            MealManager m_mgr = new MealManager();

            for (int i = 0; i < purchase.size(); i++) {
                success = (success && m_mgr.fullDbUpdate(purchase.getMeal(i)));
            }
            return (success && this.dbUpdate(purchase));
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s full updating.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbSoftDelete(Purchase purchase) {
        try {
            return this.fullDbSoftDelete(purchase.getId());
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the full %s soft deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbHardDelete(Purchase purchase) {
        try {
            return this.fullDbHardDelete(purchase.getId());
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the full %s hard deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbSoftDelete(int id) {
        try {
            boolean success = true;
            MealManager m_mgr = new MealManager();
            Purchase purchase = this.dbLoad(id);

            for (int i = 0; i < purchase.size(); i++) {
                success = (success && m_mgr.fullDbSoftDelete(purchase.getMeal(i)));
            }
            return (success && this.dbSoftDelete(id));
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the full %s soft deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbHardDelete(int id) {
        try {
            boolean success = this.dbHardDelete(id);
            MealManager m_mgr = new MealManager();
            Purchase purchase = this.dbLoad(id);

            for (int i = 0; i < purchase.size(); i++) {
                success = (success && m_mgr.fullDbHardDelete(purchase.getMeal(i)));
            }
            return success;
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the full %s hard deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public Purchase dbLoad(int id) {
        try {
            String[] selectArgs = {String.valueOf(id)};
            Cursor rslt = this.database.rawQuery(String.format("SELECT * FROM %s WHERE %s = ? AND %s = 0", this.getTable(), UNEDIT_FIELDS[0], UNEDIT_FIELDS[1]), selectArgs);

            rslt.moveToNext();
            return new Purchase(rslt, true);
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s loading.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }

    public ArrayList<Purchase> dbLoadAll() {
        try {
            ArrayList<Purchase> purchaseLst = new ArrayList<>();
            Cursor rslt = this.database.rawQuery(String.format("SELECT * FROM %s WHERE %s = 0",
                    this.getTable(), UNEDIT_FIELDS[1]), null);

            while (rslt.moveToNext()) {
                purchaseLst.add(new Purchase(rslt, false));
            }
            rslt.close();
            return purchaseLst;
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the whole %s loading.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }

    public boolean fullRestoreSoftDeleted(int id) {
        try {
            boolean success = true;
            MealManager m_mgr = new MealManager();
            Purchase purchase = this.dbLoad(id);

            for (int i = 0; i < purchase.size(); i++) {
                success = (success && m_mgr.fullRestoreSoftDeleted(purchase.getMeal(i).getId()));
            }

            return (success && this.restoreSoftDeleted(id));
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the soft deleted %s full restoring.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }
}
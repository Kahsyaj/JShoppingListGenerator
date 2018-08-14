package personnal.ahsyaj.jshoppinglistgenerator.lib.Managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.ShoppingList;

public class ShoppingListManager extends Manager {
    public static final String[] EDIT_FIELDS = {"date_shoppinglist"};
    public static final String[] UNEDIT_FIELDS = {"id_shoppinglist", "deleted"};

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
            PurchaseManager p_mgr = new PurchaseManager();

            this.dbCreate(shoppinglist);

            p_mgr.fullDbCreate(shoppinglist.getPurchase());

            return true;
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s full creating.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public boolean dbUpdate(ShoppingList shoppinglist) {
        try {
            String query = String.format("UPDATE %s SET %s = ? WHERE %s = ?", this.getTable(), EDIT_FIELDS[0], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setString(1, shoppinglist.getDate());
            st.setInt(2, shoppinglist.getId());
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s updating.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbUpdate(ShoppingList shoppinglist) {
        try {
            PurchaseManager p_mgr = new PurchaseManager();
            p_mgr.fullDbUpdate(shoppinglist.getPurchase());
            String query = String.format("UPDATE %s SET %s = ? WHERE %s = ?", this.getTable(), EDIT_FIELDS[0], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setString(1, shoppinglist.getDate());
            st.setInt(2, shoppinglist.getId());
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s full updating.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean dbSoftDelete(ShoppingList shoppinglist) {
        try {
            String query = String.format("UPDATE %s SET %s = 1 WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[1], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, shoppinglist.getId());
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s soft deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean dbHardDelete(ShoppingList shoppinglist) {
        try {
            return this.dbHardDelete(shoppinglist.getId());
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s hard deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbSoftDelete(ShoppingList shoppinglist) {
        try {
            PurchaseManager p_mgr = new PurchaseManager();
            String query = String.format("UPDATE %s SET %s = 1 WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[1], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, shoppinglist.getId());
            boolean status = st.executeUpdate() != 0;
            p_mgr.fullDbSoftDelete(shoppinglist.getPurchase());
            return status;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the full %s soft deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbHardDelete(ShoppingList shoppinglist) {
        try {
            PurchaseManager p_mgr = new PurchaseManager();
            String query = String.format("DELETE FROM %s WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, shoppinglist.getId());
            boolean status = st.executeUpdate() != 0;
            p_mgr.fullDbHardDelete(shoppinglist.getPurchase());
            return status;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the full %s hard deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean dbSoftDelete(int id) {
        try {
            String whereClause = String.format("%s = ?", UNEDIT_FIELDS[0]);
            String[] whereArgs = {String.valueOf(id)};

            return (this.database.delete(this.table, whereClause, whereArgs) != 0);
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s soft deletion.\n", this.getTable()) + e.getMessage());

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

    public boolean fullDbSoftDelete(int id) {
        try {
            PurchaseManager p_mgr = new PurchaseManager();
            p_mgr.fullDbSoftDelete(id);
            String query = String.format("UPDATE %s SET %s = 1 WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[1], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, id);
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the full %s soft deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbHardDelete(int id) {
        try {
            PurchaseManager p_mgr = new PurchaseManager();
            boolean status = this.dbHardDelete(id);

            p_mgr.fullDbHardDelete(id);

            return status;
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the full %s hard deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public ShoppingList dbLoad(int id) {
        try {
            PurchaseManager p_mgr = new PurchaseManager();
            String query = String.format("SELECT * FROM %s WHERE %s = ? AND %s = 0", this.getTable(), UNEDIT_FIELDS[0], UNEDIT_FIELDS[1]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, id);
            ResultSet rslt = st.executeQuery();
            rslt.next();
            return new ShoppingList(rslt, true);
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s loading.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }

    public ArrayList<ShoppingList> dbLoadAll() {
        try {
            ArrayList<ShoppingList> shpgLst = new ArrayList<>();
            String query = String.format("SELECT * FROM %s WHERE %s = 0", this.getTable(), UNEDIT_FIELDS[1]);
            Statement st = this.getConnector().createStatement();
            ResultSet rslt = st.executeQuery(query);
            while (rslt.next()) {
                shpgLst.add(new ShoppingList(rslt, false));
            }
            rslt.close();
            return shpgLst;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the whole %s loading.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }

    public boolean fullRestoreSoftDeleted(int id) {
        try {
            PurchaseManager p_mgr = new PurchaseManager();
            p_mgr.fullRestoreSoftDeleted(id);
            String query = String.format("UPDATE %s SET %s = 0 WHERE %s = 1 AND %s = ?", this.getTable(), UNEDIT_FIELDS[1], UNEDIT_FIELDS[1], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, id);
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the soft deleted %s full restoring.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public int getCurrentId() {
        try {
            String query = String.format("SELECT MAX(%s) as %s FROM %s", UNEDIT_FIELDS[0], UNEDIT_FIELDS[0], this.getTable());
            Statement st = this.getConnector().createStatement();
            ResultSet rslt = st.executeQuery(query);
            if (!rslt.next()) {
                return 1;
            }
            int currentId = (rslt.getInt(String.format("%s", UNEDIT_FIELDS[0])) + 1);
            rslt.close();
            return currentId;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s id querying.\n", this.getTable()) + e.getMessage());
            return 0;
        }
    }

    public ArrayList<Integer> getIds() {
        try {
            ArrayList<Integer> idLst = new ArrayList<>();
            String query = String.format("SELECT %s FROM %s WHERE %s = 0", UNEDIT_FIELDS[0], this.getTable(), UNEDIT_FIELDS[1]);
            Statement st = this.getConnector().createStatement();
            ResultSet rslt = st.executeQuery(query);
            while (rslt.next()) {
                idLst.add(rslt.getInt(UNEDIT_FIELDS[0]));
            }
            if (idLst.size() == 0) {
                return null;
            } else {
                return idLst;
            }
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s ids querying.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }
}

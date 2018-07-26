package personnal.ahsyaj.jshoppinglistgenerator.lib.Managers;

import android.widget.EdgeEffect;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Meal;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Purchase;

public class PurchaseManager extends Manager {
    public static final String[] EDIT_FIELDS = {"id_meal"};
    public static final String[] UNEDIT_FIELDS = {"id_shoppinglist", "deleted"};

    //Constructors
    public PurchaseManager() {
        super();
        this.setTable("Purchase");
    }

    public PurchaseManager(DbFactory dbF) {
        super(dbF);
        this.setTable("Purchase");
    }

    //Other methods
    public boolean dbCreate(Purchase purchase) {
        try {
            for (int i = 0; i < purchase.size(); i++) {
                Meal currentMeal = purchase.getMeals().get(i);
                String query = String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)",
                        this.getTable(), UNEDIT_FIELDS[0], EDIT_FIELDS[0]);
                PreparedStatement st = this.getConnector().prepareStatement(query);
                st.setInt(1, purchase.getId());
                st.setInt(2, currentMeal.getId());
                if (st.executeUpdate() == 0) {
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s creating.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbCreate(Purchase purchase) {
        try {
            MealManager m_mgr = new MealManager();
            for (int i = 0; i < purchase.getMeals().size(); i++) {
                Meal currentMeal = purchase.getMeals().get(i);
                m_mgr.fullDbCreate(currentMeal);
                String query = String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)",
                        this.getTable(), UNEDIT_FIELDS[0], EDIT_FIELDS[0]);
                PreparedStatement st = this.getConnector().prepareStatement(query);
                st.setInt(1, purchase.getId());
                st.setInt(2, currentMeal.getId());
                if (st.executeUpdate() == 0) {
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s full creating.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean dbUpdate(Purchase purchase) {
        this.dbHardDelete(purchase);
        return this.dbCreate(purchase);
    }

    public boolean fullDbUpdate(Purchase purchase) {
        MealManager m_mgr = new MealManager();
        for (int i = 0; i < purchase.size(); i++) {
            m_mgr.fullDbUpdate(purchase.getMeal(i));
        }
        return this.dbUpdate(purchase);
    }

    public boolean dbSoftDelete(Purchase purchase) {
        try {
            String query = String.format("UPDATE %s SET %s = 1 WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[1], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, purchase.getId());
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s soft deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean dbHardDelete(Purchase purchase) {
        try {
            String query = String.format("DELETE FROM %s WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, purchase.getId());
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s hard deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbSoftDelete(Purchase purchase) {
        try {
            MealManager m_mgr = new MealManager();
            for (int i = 0; i < purchase.size(); i++) {
                m_mgr.fullDbSoftDelete(purchase.getMeal(i));
            }
            String query = String.format("UPDATE %s SET %s = 1 WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[1], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, purchase.getId());
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the full %s soft deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbHardDelete(Purchase purchase) {
        try {
            MealManager m_mgr = new MealManager();
            String query = String.format("DELETE FROM %s WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, purchase.getId());
            boolean status = st.executeUpdate() != 0;
            for (int i = 0; i < purchase.size(); i++) {
                m_mgr.fullDbHardDelete(purchase.getMeal(i));
            }
            return status;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the full %s hard deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean dbSoftDelete(int id) {
        try {
            String query = String.format("UPDATE %s SET %s = 1 WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[1], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, id);
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s soft deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean dbHardDelete(int id) {
        try {
            String query = String.format("DELETE FROM %s WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, id);
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s hard deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbSoftDelete(int id) {
        try {
            MealManager m_mgr = new MealManager();
            Purchase purchase = this.dbLoad(id);
            for (int i = 0; i < purchase.size(); i++) {
                m_mgr.fullDbSoftDelete(purchase.getMeal(i));
            }
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
            MealManager m_mgr = new MealManager();
            Purchase purchase = this.dbLoad(id);
            String query = String.format("DELETE FROM %s WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, id);
            boolean status = st.executeUpdate() != 0;
            for (int i = 0; i < purchase.size(); i++) {
                m_mgr.fullDbHardDelete(purchase.getMeal(i));
            }
            return status;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the full %s hard deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public Purchase dbLoad(int id) {
        try {
            String query = String.format("SELECT * FROM %s WHERE %s = ? AND %s = 0", this.getTable(), UNEDIT_FIELDS[0], UNEDIT_FIELDS[1]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, id);
            ResultSet rslt = st.executeQuery();
            rslt.next();
            return new Purchase(rslt, true);
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s loading.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }

    public ArrayList<Purchase> dbLoadAll() {
        try {
            ArrayList<Purchase> purchaseLst = new ArrayList<>();
            String query = String.format("SELECT * FROM %s WHERE %s = 0", this.getTable(), UNEDIT_FIELDS[1]);
            Statement st = this.getConnector().createStatement();
            ResultSet rslt = st.executeQuery(query);
            while (rslt.next()) {
                purchaseLst.add(new Purchase(rslt, false));
            }
            rslt.close();
            return purchaseLst;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the whole %s loading.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }

    public boolean fullRestoreSoftDeleted(int id) {
        try {
            MealManager m_mgr = new MealManager();
            Purchase purchase = this.dbLoad(id);
            for (int i = 0; i < purchase.size(); i++) {
                m_mgr.fullRestoreSoftDeleted(purchase.getMeal(i).getId());
            }
            String query = String.format("UPDATE %s SET %s = 0 WHERE %s = 1 AND %s = ?", this.getTable(), UNEDIT_FIELDS[1], UNEDIT_FIELDS[1], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, id);
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the soft deleted %s full restoring.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public ArrayList<Integer> getIds() {
        try {
            ArrayList<Integer> idLst = new ArrayList<>();
            String query = String.format("SELECT %s FROM %s WHERE %s = 0", UNEDIT_FIELDS[0], this.getTable(), UNEDIT_FIELDS[1]);
            Statement st = this.getConnector().createStatement();
            ResultSet rslt = st.executeQuery(query);
            while (rslt.next()) {
                Integer id = rslt.getInt(UNEDIT_FIELDS[0]);
                if (!idLst.contains(id)) {
                    idLst.add(id);
                }
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
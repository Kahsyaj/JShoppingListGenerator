package personnal.ahsyaj.jshoppinglistgenerator.lib.Managers;

import android.widget.EdgeEffect;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Meal;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Purchase;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Recipe;

public class PurchaseManager extends Manager {
    public final static String[] EDIT_FIELDS = {"id_meal"};
    public final static String[] UNEDIT_FIELDS = {"id_shoppinglist", "deleted"};

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
            for (int i = 0; i < purchase.getMeals().size(); i++) {
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
            System.err.println("An error occurred with the purchase creating.\n" + e.getMessage());
            return false;
        }
    }


    public boolean dbUpdate(Purchase purchase) {
        this.dbHardDelete(purchase);
        return this.dbCreate(purchase);
    }

    public boolean dbSoftDelete(Purchase purchase) {
        try {
            String query = String.format("UPDATE %s SET %s = 1 WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[1], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, purchase.getId());
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println("An error occurred with the purchase's soft delete.\n" + e.getMessage());
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
            return new Purchase(rslt);
        } catch (SQLException e) {
            System.err.println("An error occurred with the purchase loading.\n" + e.getMessage());
            return null;
        }
    }

    public boolean dbHardDelete(Purchase purchase) {
        try {
            String query = String.format("DELETE FROM %s WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, purchase.getId());
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println("An error occurred with the purchase's update.\n" + e.getMessage());
            return false;
        }
    }

    public boolean restoreSoftDeleted() {
        try {
            String query = String.format("UPDATE %s SET %s = 1 WHERE %s = 1", this.getTable(), UNEDIT_FIELDS[1], UNEDIT_FIELDS[1]);
            Statement st = this.getConnector().createStatement();
            return st.executeUpdate(query) != 0;
        } catch (SQLException e) {
            System.err.println("An error occurred with the soft deleted purchases restoring.\n" + e.getMessage());
            return false;
        }
    }

    public boolean restoreSoftDeleted(int id) {
        try {
            String query = String.format("UPDATE %s SET %s = 1 WHERE %s = 1 AND %s = ?", this.getTable(), UNEDIT_FIELDS[1], UNEDIT_FIELDS[1], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, id);
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println("An error occurred with the soft deleted purchases restoring.\n" + e.getMessage());
            return false;
        }
    }
}
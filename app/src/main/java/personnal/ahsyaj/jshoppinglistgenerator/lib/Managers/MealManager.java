package personnal.ahsyaj.jshoppinglistgenerator.lib.Managers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Meal;

public class MealManager extends Manager {
    public final static String[] EDIT_FIELDS = {"name_meal"};
    public final static String[] UNEDIT_FIELDS = {"id_meal", "deleted"};

    //Constructors
    public MealManager() {
        super();
        this.setTable("Meal");
    }

    //Other methods
    public boolean dbCreate(Meal meal) {
        try {
            int currentId = this.getCurrentId();
            String query = String.format("INSERT INTO %s (%s) VALUES (?)", this.getTable(), EDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setString(1, meal.getName());
            st.executeUpdate();
            meal.setId(currentId);
            return true;
        } catch (SQLException e) {
            System.err.println("An error occurred with the meal creating.\n" + e.getMessage());
            return false;
        }
    }

    public boolean dbUpdate(Meal meal) {
        try {
            String query = String.format("UPDATE %s SET %s = ? WHERE %s = ?", this.getTable(), EDIT_FIELDS[0], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setString(1, meal.getName());
            st.setInt(2, meal.getId());
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println("An error occurred with the meal's update.\n" + e.getMessage());
            return false;
        }
    }

    public boolean dbSoftDelete(Meal meal) {
        try {
            String query = String.format("UPDATE %s SET %s = 1 WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[1], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, meal.getId());
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println("An error occurred with the meal's soft delete.\n" + e.getMessage());
            return false;
        }
    }

    public boolean dbHardDelete(Meal meal) {
        try {
            String query = String.format("DELETE FROM %s WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, meal.getId());
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println("An error occurred with the meal's update.\n" + e.getMessage());
            return false;
        }
    }

    public Meal dbLoad(int id) {
        try {
            String query = (String.format("SELECT * FROM %s WHERE %s = ? AND %s = 0", this.getTable(), UNEDIT_FIELDS[0], UNEDIT_FIELDS[1]));
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, id);
            ResultSet rslt = st.executeQuery();
            rslt.next();
            return new Meal(rslt);

        } catch (SQLException e) {
            System.err.println("An error occurred with the meal loading.");
            return null;
        }
    }

    public Meal dbLoad(String name) {
        try {
            String query = (String.format("SELECT * FROM %s WHERE %s = ? AND %s = 0", this.getTable(), EDIT_FIELDS[0], UNEDIT_FIELDS[1]));
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setString(1, name);
            ResultSet rslt = st.executeQuery();
            rslt.next();
            return new Meal(rslt);

        } catch (SQLException e) {
            System.err.println("An error occurred with the meal loading.");
            return null;
        }
    }

    public boolean restoreSoftDeleted() {
        try {
            String query = String.format("UPDATE %s SET %s = 1 WHERE %s = 1", this.getTable(), UNEDIT_FIELDS[1], UNEDIT_FIELDS[1]);
            Statement st = this.getConnector().createStatement();
            return st.executeUpdate(query) != 0;
        } catch (SQLException e) {
            System.err.println("An error occurred with the soft deleted meals restoring.\n" + e.getMessage());
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
            System.err.println("An error occurred with the soft deleted meal restoring.\n" + e.getMessage());
            return false;
        }
    }

    public int getCurrentId() {
        try {
            String query = String.format("SELECT MAX(%s) FROM %s", UNEDIT_FIELDS[0], this.getTable());
            Statement st = this.getConnector().createStatement();
            ResultSet rslt = st.executeQuery(query);
            if (!rslt.next()) {
                return 1;
            }
            int currentId = (rslt.getInt(String.format("%s", UNEDIT_FIELDS[0])) + 1);
            rslt.close();
            return currentId;
        } catch (SQLException e) {
            System.err.println("An error occurred with the meal id query.\n" + e.getMessage());
            return 0;
        }
    }
}

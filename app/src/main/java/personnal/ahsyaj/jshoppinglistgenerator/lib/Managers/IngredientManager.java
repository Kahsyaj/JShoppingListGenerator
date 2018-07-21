package personnal.ahsyaj.jshoppinglistgenerator.lib.Managers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Ingredient;

public class IngredientManager extends Manager {
    public final static String[] EDIT_FIELDS = {"name_ingredient"};
    public final static String[] UNEDIT_FIELDS = {"id_ingredient", "deleted"};

    //Constructors
    public IngredientManager() {
        super();
        this.setTable("Ingredient");
    }

    //Other methods
    public boolean dbCreate(Ingredient ingredient) {
        try {
            int currentId = this.getCurrentId();
            String query = String.format("INSERT INTO %s (%s) VALUES (?)", this.getTable(), EDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setString(1, ingredient.getName());
            st.executeUpdate();
            ingredient.setId(currentId);
            return true;
        } catch (SQLException e) {
            System.err.println("An error occurred with the ingredient creating.\n" + e.getMessage());
            return false;
        }
    }

    public boolean dbUpdate(Ingredient ingredient) {
        try {
            String query = String.format("UPDATE %s SET %s = ? WHERE %s = ?", this.getTable(), EDIT_FIELDS[0], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setString(1, ingredient.getName());
            st.setInt(2, ingredient.getId());
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println("An error occurred with the ingredient's update.\n" + e.getMessage());
            return false;
        }
    }

    public boolean dbSoftDelete(Ingredient ingredient) {
        try {
            String query = String.format("UPDATE %s SET %s = 1 WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[1], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, ingredient.getId());
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println("An error occurred with the ingredient's soft delete.\n" + e.getMessage());
            return false;
        }
    }

    public boolean dbHardDelete(Ingredient ingredient) {
        try {
            String query = String.format("DELETE FROM %s WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, ingredient.getId());
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println("An error occurred with the ingredient's update.\n" + e.getMessage());
            return false;
        }
    }

    /*
    public Ingredient dbLoad(int id) {
        try {
            String query = (String.format("SELECT * FROM %s WHERE %s = ? AND %s = 0", this.getTable(), UNEDIT_FIELDS[0], UNEDIT_FIELDS[1]));
            PreparedStatement st = this.getConnector().prepareStatement(query);

        } catch (SQLException e) {
            System.err.println("An error occurred with the ingredient loading.");

        }
    }

    public Ingredient dbLoad(String name) {
        try {

        }
    }*/

    public boolean restoreSoftDeleted() {
        try {
            String query = String.format("UPDATE %s SET %s = 1 WHERE %s = 1", this.getTable(), UNEDIT_FIELDS[1], UNEDIT_FIELDS[1]);
            Statement st = this.getConnector().createStatement();
            return st.executeUpdate(query) != 0;
        } catch (SQLException e) {
            System.err.println("An error occurred with the soft deleted ingredients restoring.\n" + e.getMessage());
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
            System.err.println("An error occurred with the soft deleted ingredient restoring.\n" + e.getMessage());
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
            int currentId = rslt.getInt(String.format("%s", UNEDIT_FIELDS[0])) + 1;
            rslt.close();
            return currentId;
        } catch (SQLException e) {
            System.err.println("An error occurred with the ingredient id query.\n" + e.getMessage());
            return 0;
        }
    }
}

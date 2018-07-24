package personnal.ahsyaj.jshoppinglistgenerator.lib.Managers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Recipe;


public class RecipeManager extends Manager {
    public final static String[] EDIT_FIELDS = {"id_ingredient", "quantity"};
    public final static String[] UNEDIT_FIELDS = {"id_meal", "deleted"};

    //Constructors
    public RecipeManager() {
        super();
        this.setTable("Recipe");
    }

    public RecipeManager(DbFactory dbF) {
        super(dbF);
        this.setTable("Recipe");
    }

    //Other methods
    public boolean dbCreate(Recipe recipe) {
        try {
            for (int i = 0; i < recipe.size(); i++) {
                String query = String.format("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)", this.getTable(), UNEDIT_FIELDS[0], EDIT_FIELDS[0], EDIT_FIELDS[1]);
                PreparedStatement st = this.getConnector().prepareStatement(query);
                st.setInt(1, recipe.getId());
                st.setInt(2, recipe.getIngredients().get(i).getId());
                st.setInt(3, recipe.getQuantities().get(i));
                st.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            System.err.println("An error occurred with the recipe creating.\n" + e.getMessage());
            return false;
        }
    }

    //Pretty dirty... But easier than comparing all the ingredients and setting/deleting for each differences
    public boolean dbUpdate(Recipe recipe) {
        this.dbHardDelete(recipe);
        return this.dbCreate(recipe);
    }

    public boolean dbSoftDelete(Recipe recipe) {
        try {
            String query = String.format("UPDATE %s SET %s = 1 WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[1], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, recipe.getId());
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println("An error occurred with the recipe's soft delete.\n" + e.getMessage());
            return false;
        }
    }

    public boolean dbHardDelete(Recipe recipe) {
        try {
            String query = String.format("DELETE FROM %s WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, recipe.getId());
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println("An error occurred with the recipe's update.\n" + e.getMessage());
            return false;
        }
    }

    public Recipe dbLoad(int id) {
        try {
            String query = (String.format("SELECT * FROM %s WHERE %s = ? AND %s = 0", this.getTable(), UNEDIT_FIELDS[0], UNEDIT_FIELDS[1]));
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, id);
            ResultSet rslt = st.executeQuery();
            rslt.next();
            return new Recipe(rslt);
        } catch (SQLException e) {
            System.err.println("An error occurred with the recipe loading.\n" + e.getMessage());
            return null;
        }
    }

    public boolean restoreSoftDeleted() {
        try {
            String query = String.format("UPDATE %s SET %s = 1 WHERE %s = 1", this.getTable(), UNEDIT_FIELDS[1], UNEDIT_FIELDS[1]);
            Statement st = this.getConnector().createStatement();
            return st.executeUpdate(query) != 0;
        } catch (SQLException e) {
            System.err.println("An error occurred with the soft deleted recipes restoring.\n" + e.getMessage());
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
            System.err.println("An error occurred with the soft deleted recipe restoring.\n" + e.getMessage());
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
            System.err.println("An error occurred with the recipe id query.\n" + e.getMessage());
            return 0;
        }
    }
}

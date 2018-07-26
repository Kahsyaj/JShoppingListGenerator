package personnal.ahsyaj.jshoppinglistgenerator.lib.Managers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Ingredient;

public class IngredientManager extends Manager {
    public static final String[] EDIT_FIELDS = {"name_ingredient"};
    public static final String[] UNEDIT_FIELDS = {"id_ingredient", "deleted"};

    //Constructors
    public IngredientManager() {
        super();
        this.setTable("Ingredient");
    }

    public IngredientManager(DbFactory dbF) {
        super(dbF);
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
            System.err.println(String.format("An error occurred with the %s creating.\n", this.getTable()) + e.getMessage());
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
            System.err.println(String.format("An error occurred with the %s updating.\n", this.getTable()) + e.getMessage());
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
            System.err.println(String.format("An error occurred with the %s soft deletion.\n", this.getTable()) + e.getMessage());
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
            System.err.println(String.format("An error occurred with the %s hard deletion.\n", this.getTable()) + e.getMessage());
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

    public Ingredient dbLoad(int id) {
        try {
            String query = String.format("SELECT * FROM %s WHERE %s = ? AND %s = 0", this.getTable(), UNEDIT_FIELDS[0], UNEDIT_FIELDS[1]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, id);
            ResultSet rslt = st.executeQuery();
            rslt.next();
            return new Ingredient(rslt, true);

        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s loading.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }

    public Ingredient dbLoad(String name) {
        try {
            String query = String.format("SELECT * FROM %s WHERE %s = ? AND %s = 0", this.getTable(), EDIT_FIELDS[0], UNEDIT_FIELDS[1]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setString(1, name);
            ResultSet rslt = st.executeQuery();
            rslt.next();
            return new Ingredient(rslt, true);

        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s loading.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }

    public ArrayList<Ingredient> dbLoadAll() {
        try {
            ArrayList<Ingredient> ingLst = new ArrayList<>();
            String query = String.format("SELECT * FROM %s WHERE %s = 0", this.getTable(), UNEDIT_FIELDS[1]);
            Statement st = this.getConnector().createStatement();
            ResultSet rslt = st.executeQuery(query);
            while (rslt.next()) {
                ingLst.add(new Ingredient(rslt, false));
            }
            rslt.close();
            return ingLst;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the whole %s loading.\n", this.getTable()) + e.getMessage());
            return null;
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
            int currentId = rslt.getInt(UNEDIT_FIELDS[0]) + 1;
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

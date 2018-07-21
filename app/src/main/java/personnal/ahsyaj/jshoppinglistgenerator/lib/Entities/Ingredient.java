package personnal.ahsyaj.jshoppinglistgenerator.lib.Entities;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Ingredient extends Entity {
    public static String[] DB_FIELDS = {"id_ingredient", "name_ingredient", "deleted"};
    private String name;

    //Constructors
    public Ingredient() {
        super();
        this.name = "";
    }

    public Ingredient(ResultSet rslt) {
        super();
        this.init(rslt);
    }

    public Ingredient(String name) {
        super();
        this.name = name;
    }

    public Ingredient(int id, String name) {
        super(id);
        this.name = name;
    }

    //Getters
    public String getName() {
        return this.name;
    }

    //Setters
    public void setName(String name) {
        this.name = name;
    }

    //Other methods

    @Override
    public String toString() {
        return String.format("[Ingredient]\n[id_ingredient] : %s - [name_ingredient] : %s",
                String.valueOf(this.getId()), this.getName());
    }

    public void init(ResultSet rslt) {
        try {
                this.setId(rslt.getInt(DB_FIELDS[0]));
                this.setName(rslt.getNString(DB_FIELDS[1]));
                this.setDeleted(rslt.getInt(DB_FIELDS[2]));
        } catch (SQLException e) {
            System.err.println("An error occurred with the ingredient init.\n" + e.getMessage());
        }
    }
}

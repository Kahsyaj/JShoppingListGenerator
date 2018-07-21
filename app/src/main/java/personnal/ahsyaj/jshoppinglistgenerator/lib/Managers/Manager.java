package personnal.ahsyaj.jshoppinglistgenerator.lib.Managers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Manager {
    private Connection connector;
    private String table;

    //Constructors
    public Manager() {
        DbFactory dbF = new DbFactory();
        this.connector = dbF.getConnector();
    }

    public Manager(DbFactory dbF) {
        this.connector = dbF.getConnector();
    }

    //Getters
    public Connection getConnector() {
        return this.connector;
    }

    public String getTable() {
        return this.table;
    }

    //Setters
    public void setConnector(Connection connector) {
        this.connector = connector;
    }

    public void setTable(String table) {
        this.table = table;
    }

    //Other methods
    public void initDb() {
        try {
            Statement st = this.connector.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS Ingredient (id_ingredient INT NOT NULL " +
                    "AUTO_INCREMENT, name_ingredient VARCHAR(50), deleted INT(1) DEFAULT 0, UNIQUE" +
                    "(name_ingredient), CONSTRAINT Ingredient_PK PRIMARY KEY (id_ingredient)) ENGINE=InnoDB");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS Meal (id_meal INT NOT NULL AUTO_INCREMENT, " +
                    "name_meal VARCHAR(50), deleted INT(1) DEFAULT 0, UNIQUE (name_meal), CONSTRAINT " +
                    "Meal_PK PRIMARY KEY (id_meal)) ENGINE=InnoDB");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS Recipe (id_ingredient INT NOT NULL, id_meal " +
                    "INT NOT NULL, quantity INT, deleted INT(1) DEFAULT 0, CONSTRAINT Recipte_PK PRIMARY " +
                    "KEY (id_meal, id_ingredient), CONSTRAINT Recipe_Meal_FK FOREIGN KEY(id_meal) REFERENCES " +
                    "Meal(id_meal), CONSTRAINT Recipe_Ingredient_FK FOREIGN KEY (id_ingredient) REFERENCES " +
                    "Ingredient(id_ingredient)) ENGINE=InnoDB");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS ShoppingList (id_shoppinglist INT NOT NULL " +
                    "AUTO_INCREMENT, date_shoppinglist DATE, deleted INT(1) DEFAULT 0, CONSTRAINT " +
                    "ShoppingList_PK PRIMARY KEY (id_shoppinglist)) ENGINE=InnoDB");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS Purchase ( id_shoppinglist INT NOT NULL, " +
                    "id_ingredient Int NOT NULL, quantity INT NOT NULL, deleted INT(1) DEFAULT 0, " +
                    "CONSTRAINT Purchase_PK PRIMARY KEY (id_shoppinglist, id_ingredient), CONSTRAINT " +
                    "Purchase_Ingredient_FK FOREIGN KEY (id_ingredient) REFERENCES Ingredient(id_ingredient), " +
                    "CONSTRAINT Purchase_ShoppingList_FK FOREIGN KEY (id_shoppinglist) REFERENCES " +
                    "ShoppingList(id_shoppinglist)) ENGINE=InnoDB");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void finalize() {
        try {
            this.connector.close();
        } catch (SQLException e) {
            System.err.println("The connector can't be closed, it may not exists.");
        }
    }
}

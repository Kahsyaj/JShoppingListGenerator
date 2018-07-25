package personnal.ahsyaj.jshoppinglistgenerator.lib.Entities;

import java.sql.ResultSet;
import java.sql.SQLException;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.PurchaseManager;

public class ShoppingList extends Entity {
    public static String[] DB_FIELDS = {"id_shoppinglist", "date_shoppinglist", "deleted"};
    private String date;
    private Purchase purchase;

    //Constructors
    public ShoppingList() {
        super();
        this.date = "";
        this.purchase = new Purchase();
    }

    public ShoppingList(ResultSet rslt, boolean close) {
        super();
        this.init(rslt, close);
    }

    public ShoppingList(int id) {
        super(id);
        this.date = "";
        this.purchase = new Purchase();
    }

    public ShoppingList(int id, String date) {
        super(id);
        this.date = date;
        this.purchase = new Purchase();
    }

    public ShoppingList(int id, String date, Purchase purchase) {
        super(id);
        this.date = date;
        this.purchase = purchase;
    }

    //Getters
    public String getDate() {
        return this.date;
    }

    public Purchase getPurchase() {
        return this.purchase;
    }

    //Setters
    public void setDate(String date) {
        this.date = date;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    //Other methods
    @Override
    public String toString() {
        String repr = String.format("[ShoppingList]\n[%s] : %s - [%s] : %s\n", DB_FIELDS[0], String.valueOf(this.getId()), DB_FIELDS[1], this.getDate());
        repr += this.purchase.toString();
        return repr;
    }

    public void init(ResultSet rslt, boolean close) {
        try {
            PurchaseManager p_mgr = new PurchaseManager();
            this.setId(rslt.getInt(DB_FIELDS[0]));
            this.setDate(rslt.getString(DB_FIELDS[1]));
            this.setDeleted(rslt.getInt(DB_FIELDS[2]));
            this.purchase = p_mgr.dbLoad(this.getId());
            if (close) {
                rslt.close();
            }
        } catch (SQLException e) {
            System.err.println("An error occurred with the recipe init.\n" + e.getMessage());
        }
    }
}

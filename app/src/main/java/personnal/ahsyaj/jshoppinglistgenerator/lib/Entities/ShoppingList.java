package personnal.ahsyaj.jshoppinglistgenerator.lib.Entities;

public class ShoppingList extends Entity {
    public static String[] DB_FIELDS = {"id_meal", "name_meal", "deleted"};
    private String date;
    private Purchase purchase;

    //Constructors
    public ShoppingList() {
        super();
        this.date = "";
        this.purchase = new Purchase();
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
}

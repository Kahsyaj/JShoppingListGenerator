package personnal.ahsyaj.jshoppinglistgenerator.lib.Entities;

public abstract class Entity {
    private int id;
    private int deleted;

    //Constructors
    public Entity() {
        this.id = 0;
        this.deleted = 0;
    }

    public Entity(int entityId) {
        this.id = entityId;
        this.deleted = 0;
    }

    //Getters
    public int getId() {
        return this.id;
    }

    public int getDeleted() {
        return deleted;
    }

    //Setters
    public void setId(int newId) {
        this.id = newId;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    //Other methods
}

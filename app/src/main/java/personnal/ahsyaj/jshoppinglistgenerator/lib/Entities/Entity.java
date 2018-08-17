package personnal.ahsyaj.jshoppinglistgenerator.lib.Entities;

import android.content.Intent;

import java.io.Serializable;

public abstract class Entity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
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
    public Integer getId() {
        return this.id;
    }

    public int getDeleted() {
        return deleted;
    }

    //Setters
    public void setId(Integer newId) {
        this.id = newId;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    //Other methods
    public abstract String className();
}

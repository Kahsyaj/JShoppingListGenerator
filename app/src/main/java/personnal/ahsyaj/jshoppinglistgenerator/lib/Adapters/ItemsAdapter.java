package personnal.ahsyaj.jshoppinglistgenerator.lib.Adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import personnal.ahsyaj.jshoppinglistgenerator.R;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Entity;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Ingredient;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Meal;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Recipe;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.ShoppingList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    private List<Entity> items;
    private int itemLayout;

    //Constructors
    public ItemsAdapter(List<Entity> items, int itemLayout) {
        this.items = items;
        this.itemLayout = itemLayout;
    }

    //Getters
    public List<Entity> getItems() {
        return items;
    }

    public int getItemLayout() {
        return itemLayout;
    }

    //Setter
    public void setItems(List<Entity> items) {
        this.items = items;
    }

    public void setItemLayout(int itemLayout) {
        this.itemLayout = itemLayout;
    }

    //Other methods
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.itemLayout, parent, false);
        return new ViewHolder(view);
    }

    public int getItemCount() {
        return this.items.size();
    }

    public void onBindViewHolder(ItemsAdapter.ViewHolder holder, int pos) {
        Entity elt = this.items.get(pos);

        holder.itemId.setText(elt.getId().toString());
        switch(elt.className()) {
            case "Ingredient":
                holder.itemName.setText(((Ingredient) elt).getName());
                break;
            case "Meal":
                holder.itemName.setText(((Meal) elt).getName());
                break;
            case "ShoppingList":
                holder.itemName.setText(((ShoppingList) elt).getDate());
                break;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemId;
        public TextView itemName;
        public ImageButton setButton;
        public ImageButton delButton;

        public ViewHolder(View view) {
            super(view);

            this.itemId = view.findViewById(R.id.itemId);
            this.itemName = view.findViewById(R.id.itemName);
            this.setButton = view.findViewById(R.id.setButton);
            this.delButton = view.findViewById(R.id.delButton);
        }
    }
}

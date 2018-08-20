package personnal.ahsyaj.jshoppinglistgenerator.lib.Adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import personnal.ahsyaj.jshoppinglistgenerator.CategoryActivity;
import personnal.ahsyaj.jshoppinglistgenerator.ItemActivity;
import personnal.ahsyaj.jshoppinglistgenerator.R;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Entity;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Ingredient;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Meal;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.ShoppingList;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.IngredientManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.Manager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.MealManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.ShoppingListManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Models.ActivityGetter;

public class FieldsAdapter extends RecyclerView.Adapter<FieldsAdapter.ViewHolder> {
    private List<Entity> items;
    private int itemLayout;

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

    //Constructors
    public FieldsAdapter(List<Entity> items, int itemLayout) {
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
    public FieldsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.itemLayout, parent, false);
        return new FieldsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

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
        holder.setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryActivity catAct = (CategoryActivity) ActivityGetter.getActivity("CategoryActivity");
                Intent intent = new Intent(catAct, ItemActivity.class);
                intent.putExtra("item", elt);
                catAct.startActivity(intent);
            }
        });
        holder.delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Manager mgr = null;
                switch (elt.className()) {
                    case "Ingredient":
                        mgr = new IngredientManager();
                        break;
                    case "Meal":
                        mgr = new MealManager();
                        break;
                    case "ShoppingList":
                        mgr = new ShoppingListManager();
                        break;
                    default:
                        return;
                }
                mgr.dbSoftDelete(elt.getId());
                ((CategoryActivity) ActivityGetter.getActivity("CategoryActivity")).initView();
            }
        });
    }
}

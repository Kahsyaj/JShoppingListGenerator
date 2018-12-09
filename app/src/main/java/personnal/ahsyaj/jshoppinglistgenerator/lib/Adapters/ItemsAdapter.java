package personnal.ahsyaj.jshoppinglistgenerator.lib.Adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.PurchaseManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.RecipeManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.ShoppingListManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.ActivityGetter;

public final class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
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

    public void onBindViewHolder(@NonNull ItemsAdapter.ViewHolder holder, int pos) {
        holder.feedView(this.items.get(pos));
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Entity elt;
        private final TextView itemId;
        private final TextView itemName;
        private final ImageButton delButton;
        private final ConstraintLayout itemRow;

        private ViewHolder(View view) {
            super(view);

            this.itemId = view.findViewById(R.id.item_id);
            this.itemName = view.findViewById(R.id.item_name);
            this.delButton = view.findViewById(R.id.del_button);
            this.itemRow = view.findViewById(R.id.content);
        }
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.del_button) {
                Manager mgr = null;

                switch (ViewHolder.this.elt.className()) {
                    case "Ingredient":
                        mgr = new IngredientManager();
                        RecipeManager rMgr = new RecipeManager();

                        rMgr.dbSoftDeleteIngredient(ViewHolder.this.elt.getId());
                        break;
                    case "Meal":
                        mgr = new MealManager();
                        PurchaseManager pMgr = new PurchaseManager();
                        RecipeManager rcpMgr = new RecipeManager();

                        pMgr.dbSoftDeleteMeal(ViewHolder.this.elt.getId());
                        rcpMgr.dbSoftDelete(ViewHolder.this.elt.getId());
                        break;
                    case "ShoppingList":
                        mgr = new ShoppingListManager();

                        break;
                    default:
                        return;
                }

                mgr.dbSoftDelete(elt.getId());
                ((CategoryActivity) ActivityGetter.getActivity("CategoryActivity")).refreshView();
            } else if (view.getId() == R.id.content) {
                CategoryActivity catAct = (CategoryActivity) ActivityGetter.getActivity("CategoryActivity");
                Intent intent = new Intent(catAct, ItemActivity.class);

                intent.putExtra("target", ViewHolder.this.elt);
                intent.putExtra("action", "Update");
                catAct.startActivity(intent);
            }
        }

        private void feedView(Entity entity) {
            this.elt = entity;

            String name;

            this.itemId.setText(entity.getId().toString());

            switch(elt.className()) {
                case "Ingredient":
                    name = ((Ingredient) entity).getName();

                    break;
                case "Meal":
                    name = ((Meal) entity).getName();

                    break;
                case "ShoppingList":
                    name = ((ShoppingList) entity).getDate();

                    break;
                default:
                    return;
            }

            this.itemName.setText(name);
            this.delButton.setOnClickListener(this);
            this.itemRow.setOnClickListener(this);
        }
    }
}

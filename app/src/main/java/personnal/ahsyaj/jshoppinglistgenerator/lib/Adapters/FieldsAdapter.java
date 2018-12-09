package personnal.ahsyaj.jshoppinglistgenerator.lib.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;
import personnal.ahsyaj.jshoppinglistgenerator.ItemActivity;
import personnal.ahsyaj.jshoppinglistgenerator.MainActivity;
import personnal.ahsyaj.jshoppinglistgenerator.R;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Entity;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Ingredient;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Meal;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.ShoppingList;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.MealManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.ShoppingListManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.ActivityGetter;

public final class FieldsAdapter extends RecyclerView.Adapter<FieldsAdapter.ViewHolder> {
    private List<List> items;
    private int itemLayout;
    private Entity target;

    //Constructors
    public FieldsAdapter(Entity target, List<List> items, int itemLayout) {
        this.target = target;
        this.items = items;
        this.itemLayout = itemLayout;
    }

    //Getters
    public List<List> getItems() {
        return items;
    }

    public int getItemLayout() {
        return itemLayout;
    }

    //Setter
    public void setItems(List<List> items) {
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

    public void add(ArrayList element) {
        this.items.add(element);
        this.notifyItemInserted(this.getItemCount()-1);
    }

    @Override
    public void onBindViewHolder(FieldsAdapter.ViewHolder holder, int pos) {
        holder.feedView(this.items.get(pos));
    }

    public int getItemCount() {
        return this.items.size();
    }

    protected final class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewGroup currentView;
        private final EditText itemName;
        private final EditText quantity;
        private final ImageButton delButton;

        private ViewHolder(View view) {
            super(view);

            this.currentView = (ViewGroup) view;
            this.itemName = view.findViewById(R.id.item_name);
            this.quantity = view.findViewById(R.id.item_quantity);
            this.delButton = view.findViewById(R.id.del_button);
        }

        private void feedView(List item) {
            Ingredient ing = ((Ingredient) item.get(0));

            this.itemName.setText(ing.getName());
            this.quantity.setText(item.get(1).toString());
            this.delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (FieldsAdapter.this.target != null) {
                        if (!ing.getName().equals("")) {
                            switch (MainActivity.category) {
                                case "Meals":
                                    MealManager mealMgr = new MealManager();

                                    ((Meal) FieldsAdapter.this.target).getRecipe().removeIngredient(ing);
                                    mealMgr.fullDbUpdate(FieldsAdapter.this.target);
                                    break;
                                case "ShoppingLists":
                                    ShoppingListManager shpLstMgr = new ShoppingListManager();

                                    ((ShoppingList) FieldsAdapter.this.target).getList().remove(item);
                                    shpLstMgr.dbUpdate((FieldsAdapter.this.target));
                                    break;
                            }
                        } else {
                            FieldsAdapter.this.items.remove(item);
                        }

                        ItemActivity itemAct = (ItemActivity) ActivityGetter.getActivity("ItemActivity");
                        itemAct.refreshView();
                    } else {
                        ViewHolder.this.currentView.removeView(ViewHolder.this.delButton);
                    }
                }
            });
        }
    }
}

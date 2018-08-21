package personnal.ahsyaj.jshoppinglistgenerator.lib.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import java.util.List;
import personnal.ahsyaj.jshoppinglistgenerator.ItemActivity;
import personnal.ahsyaj.jshoppinglistgenerator.R;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Ingredient;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.IngredientManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Models.ActivityGetter;

public class FieldsAdapter extends RecyclerView.Adapter<FieldsAdapter.ViewHolder> {
    private List<List> items;
    private int itemLayout;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public EditText itemName;
        public EditText quantity;
        public ImageButton delButton;

        public ViewHolder(View view) {
            super(view);

            this.itemName = view.findViewById(R.id.itemName);
            this.quantity = view.findViewById(R.id.itemQuantity);
            this.delButton = view.findViewById(R.id.delButton);
        }
    }

    //Constructors
    public FieldsAdapter(List<List> items, int itemLayout) {
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

    @Override
    public void onBindViewHolder(FieldsAdapter.ViewHolder holder, int pos) {
        holder.itemName.setText(((Ingredient) this.items.get(pos).get(0)).getName());
        holder.quantity.setText(this.items.get(pos).get(1).toString());
        holder.delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IngredientManager mgr = new IngredientManager();
                mgr.dbSoftDelete(((Ingredient) FieldsAdapter.this.items.get(pos).get(0)).getId());
                ItemActivity itemAct = (ItemActivity) ActivityGetter.getActivity("ItemActivity");
                itemAct.initView();
            }
        });
    }
    public int getItemCount() {
        return this.items.size();
    }
}

package personnal.ahsyaj.jshoppinglistgenerator.lib.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import personnal.ahsyaj.jshoppinglistgenerator.R;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Ingredient;

import java.util.List;

public final class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {
    private List<List> items;

    public ShoppingListAdapter(List items) {
        this.items = items;
    }

    public void update(List newList) {
        this.items = newList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int pos) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shoppinglist_row, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int pos) {
        viewHolder.feedView(this.items.get(pos));
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    protected final class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemName;
        private final TextView quantity;

        private ViewHolder(View generatedView) {
            super(generatedView);

            this.itemName = generatedView.findViewById(R.id.item_name);
            this.quantity = generatedView.findViewById(R.id.quantity);

            CheckBox checkBox = generatedView.findViewById(R.id.item_checkbox);

            generatedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkBox.setChecked(!checkBox.isChecked());
                }
            });
        }

        private void feedView(List item) {
            this.itemName.setText(((Ingredient) item.get(0)).getName());
            this.quantity.setText(item.get(1).toString());
        }
    }
}

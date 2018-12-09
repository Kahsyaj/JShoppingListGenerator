package personnal.ahsyaj.jshoppinglistgenerator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Adapters.ShoppingListAdapter;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.ShoppingList;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.ActivityGetter;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.ShoppingListGenerator;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.ShoppingListManager;

public final class GenerateActivity extends AppCompatActivity implements ActivityGetter, View.OnClickListener {
    private static final String NAME = "GenerateActivity";
    private RecyclerView list;
    private ShoppingList shoppingList;
    private ShoppingListAdapter adapter;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reload_button:
                new ShoppingListManager().dbSoftDelete(this.shoppingList.getId());

                this.shoppingList = ShoppingListGenerator.generate(10);

                this.adapter.update(this.shoppingList.getList());

                break;
            case R.id.back_button:
                GenerateActivity.this.finish();

                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityGetter.putActivity(NAME, this);
        this.setContentView(R.layout.activity_generate);
        this.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityGetter.removeActivity(NAME);
    }

    private void init() {
        this.findViewById(R.id.reload_button).setOnClickListener(this);
        this.findViewById(R.id.back_button).setOnClickListener(this);

        this.shoppingList = ShoppingListGenerator.generate(10);
        this.list = this.findViewById(R.id.shopping_list);
        this.adapter = new ShoppingListAdapter(this.shoppingList.getList());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        this.list.setLayoutManager(layoutManager);
        this.list.setAdapter(adapter);
    }
}

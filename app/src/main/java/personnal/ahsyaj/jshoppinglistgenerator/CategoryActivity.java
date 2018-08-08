package personnal.ahsyaj.jshoppinglistgenerator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private Button backButton = null;
    private Button addButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.category_layout);

        this.backButton = findViewById(R.id.genBackButton);
        this.addButton = findViewById(R.id.addButton);

        this.backButton.setOnClickListener(this);
        this.addButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Button clicked = (Button) view;
        if (view.getId() == this.addButton.getId()) {
            TextView category = this.findViewById(R.id.subTitle);

            this.setContentView(R.layout.item_layout);

            TextView itemName = this.findViewById(R.id.itemName);

            itemName.setHint(String.format("%s name", category.getText().subSequence(0, (category.getText().length() - 1 ))));

        } else {
            this.finish();
        }
    }
}


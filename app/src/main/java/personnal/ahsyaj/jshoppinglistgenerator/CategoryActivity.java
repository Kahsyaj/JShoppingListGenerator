package personnal.ahsyaj.jshoppinglistgenerator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CategoryActivity extends AppCompatActivity {

    private Button backButton = null;
    private Button addButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView category = this.findViewById(R.id.subTitle);

        this.setContentView(R.layout.category_layout);

        TextView itemName = this.findViewById(R.id.itemName);

        itemName.setHint(String.format("%s name", category.getText().subSequence(0, (category.getText().length() - 1 ))));

        this.initButtons();
    }

    public boolean initButtons() {
        this.backButton = this.findViewById(R.id.catBackButton);
        this.addButton = this.findViewById(R.id.addButton);

        if (this.backButton == null || this.addButton == null) {
            return false;
        }
        this.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryActivity.this.finish();
            }
        });
        this.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, ItemActivity.class);
                CategoryActivity.this.startActivity(intent);
            }
        });
        return true;
    }
}

package personnal.ahsyaj.jshoppinglistgenerator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class CategoryActivity extends AppCompatActivity {

    private Button backButton = null;
    private Button addButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.category_layout);

        this.backButton = findViewById(R.id.backButton);
        this.addButton = findViewById(R.id.addButton);
    }
}


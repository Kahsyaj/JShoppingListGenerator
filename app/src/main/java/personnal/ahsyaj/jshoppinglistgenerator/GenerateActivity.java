package personnal.ahsyaj.jshoppinglistgenerator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GenerateActivity extends AppCompatActivity {
    private Button backButton = null;
    private Button reloadButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);
        this.intiButtons();
    }

    public boolean intiButtons() {
        this.reloadButton = this.findViewById(R.id.reloadButton);
        this.backButton = this.findViewById(R.id.genBackButton);

        if (this.reloadButton == null || this.backButton == null) {
            return false;
        }
        this.reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        this.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GenerateActivity.this.finish();
            }
        });
        return true;
    }
}

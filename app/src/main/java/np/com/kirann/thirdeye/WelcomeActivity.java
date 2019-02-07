package np.com.kirann.thirdeye;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    EditText txtUserName;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        txtUserName=findViewById(R.id.txtUserName);
        btnRegister=findViewById(R.id.btnRegisterUser);
        btnRegister.setOnClickListener(this);
        if(getPreference("username").length()>0){
            txtUserName.setText(getPreference("username"));
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnRegisterUser){
            savePreference("username",txtUserName.getText().toString());
            Intent i = new Intent(WelcomeActivity.this, ExploreActivity.class);
            startActivity(i);
        }
    }


    void savePreference(String first, String second){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(first,second);
        editor.apply();
    }

    String getPreference(String first){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString(first, "");
    }
}

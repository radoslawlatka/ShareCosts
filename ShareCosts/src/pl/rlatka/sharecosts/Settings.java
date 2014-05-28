package pl.rlatka.sharecosts;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Settings extends Activity {

	private EditText databaseName, databaseAddress, username, password, port;
	private Button okButton, applyButton, cancelButton;
	private SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity);
		
		prefs = getSharedPreferences(ShareCosts.PREFS_NAME, 0);
		
		okButton = (Button) findViewById(R.id.button_ok);
		applyButton = (Button) findViewById(R.id.button_apply);
		cancelButton = (Button) findViewById(R.id.button_cancel);
		databaseName = (EditText) findViewById(R.id.database_name);
		databaseAddress = (EditText) findViewById(R.id.database_address);
		username = (EditText) findViewById(R.id.database_user);
		password = (EditText) findViewById(R.id.database_password);
		port = (EditText) findViewById(R.id.database_port);
		
		databaseName.setText(prefs.getString(ShareCosts.PREFS_DB_NAME, "tmii"));
		databaseAddress.setText(prefs.getString(ShareCosts.PREFS_DB_ADDRESS, "192.168.2.103"));
		username.setText(prefs.getString(ShareCosts.PREFS_DB_USERNAME, "radek"));
		password.setText(prefs.getString(ShareCosts.PREFS_DB_PASSWORD, "asd123"));
		port.setText(prefs.getString(ShareCosts.PREFS_DB_PORT, "3306"));
		
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				save();
				finish();
			}
		});
		
		applyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				save();
			}
		});
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void save() {
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putString(ShareCosts.PREFS_DB_NAME, databaseName.getText().toString());
		editor.putString(ShareCosts.PREFS_DB_ADDRESS, databaseAddress.getText().toString() );
		editor.putString(ShareCosts.PREFS_DB_USERNAME, username.getText().toString() );
		editor.putString(ShareCosts.PREFS_DB_PASSWORD, password.getText().toString() );
		editor.putString(ShareCosts.PREFS_DB_PORT, port.getText().toString() );
		
		editor.commit();
	}

}
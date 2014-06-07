package pl.rlatka.sharecosts;

import java.sql.SQLException;
import java.util.ArrayList;

import pl.rlatka.sharecosts.database.ShareCostsDatabase;
import pl.rlatka.sharecosts.model.Flat;
import pl.rlatka.sharecosts.model.Flatmate;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class ActivityRegister extends Activity {

	private EditText flatNameEdit, passwordEdit;
	private Button registerButton, cancelButton, addFlatmateButton;
	private ListView flatmatesList;
	private AdapterFlatmatesList flatmatesAdapter;
	private ProgressBar progressBar;
	//private ArrayList<Flatmate> flatmates;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		flatmatesAdapter = new AdapterFlatmatesList(this, R.layout.row_registration_list, new ArrayList<Flatmate>());
		
		flatNameEdit = (EditText) findViewById(R.id.edit_flat_name);
		passwordEdit = (EditText) findViewById(R.id.edit_password);
		registerButton = (Button) findViewById(R.id.button_register);
		cancelButton = (Button) findViewById(R.id.button_cancel);
		addFlatmateButton = (Button) findViewById(R.id.button_add_flatmate);
		flatmatesList = (ListView) findViewById(R.id.list_flatmates);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		
		flatmatesList.setAdapter(flatmatesAdapter);
		
/*		flatmatesAdapter.add(new Flatmate(0, 1, "Janusz", "asf", "500500500", "") );
		flatmatesAdapter.add(new Flatmate(0, 2, "Andrzej", "asf", "500500500", "") );
		flatmatesAdapter.add(new Flatmate(0, 3, "asdfasf", "asf", "500500500", "") );
		
		flatmatesAdapter.add(new Flatmate(0, 1, "Janusz", "asf", "500500500", "") );
		flatmatesAdapter.add(new Flatmate(0, 2, "Andrzej", "asf", "500500500", "") );
		flatmatesAdapter.add(new Flatmate(0, 3, "asdfasf", "asf", "500500500", "") );
		
		flatmatesAdapter.add(new Flatmate(0, 1, "Janusz", "asf", "500500500", "") );
		flatmatesAdapter.add(new Flatmate(0, 2, "Andrzej", "asf", "500500500", "") );
		flatmatesAdapter.add(new Flatmate(0, 3, "asdfasf", "asf", "500500500", "") );*/
		
		initListeners();
	}

	private void initListeners() {
		registerButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				new Register().execute();
			}
		});
		
		cancelButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		addFlatmateButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				

				initDialog(v.getContext());

			}
		});
		
	}
	
	private void initDialog(Context context) {
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialog_add_flatmate);
		dialog.setTitle(getString(R.string.label_add_flatmate));

		final EditText nameEdit = (EditText) dialog.findViewById(R.id.edit_flatmate_name);
		Button okButton = (Button) dialog.findViewById(R.id.button_ok);
		Button cancelButton = (Button) dialog.findViewById(R.id.button_cancel);
		
		dialog.show();
		
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if(nameEdit.getText().toString().equals(""))
					showToast(getApplicationContext(), getString(R.string.toast_empty_fields));	
				else {
					flatmatesAdapter.add( new Flatmate(-1, -1, nameEdit.getText().toString(),"","","") );
					dialog.dismiss();
				}
			}

		});

		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
	}
	
	private void showToast(Context applicationContext, String string) {
		Toast.makeText(applicationContext, string, Toast.LENGTH_SHORT).show();
	}
	
	private class Register extends AsyncTask<Void, Void, Boolean> {

		private ShareCostsDatabase db = ShareCosts.getDatabase();

		@Override
		protected Boolean doInBackground(Void... params) {

			try {
				db.open();

				int flatId = db.addFlat(new Flat(-1, flatNameEdit.getText().toString(), ""));

				if (flatId != 0) {
					for (Flatmate f : flatmatesAdapter.getFlatmates()) {
						f.setFlat(flatId);
						f.setPassword(Sha256.encode(f.getName(), passwordEdit.getText().toString(), 500));
						db.addFlatmate(f);
					}
				}
			} catch (MySQLIntegrityConstraintViolationException e) { 
				e.printStackTrace();
				showToast(getString(R.string.toast_registration_exist));
				return false;
			}catch (SQLException e) {
				e.printStackTrace();
				showToast(getString(R.string.toast_registration_failed));
				return false;
			} finally {
				db.close();
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			if(result) {
				finish();
			} else {
				progressBar.setVisibility(View.GONE);
			}
			
		}

		@Override
		protected void onPreExecute() {
			progressBar.setVisibility(View.VISIBLE);
		}
		

		private void showToast(final String string) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();				}
			});
		}
		
	}

}

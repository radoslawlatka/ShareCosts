package pl.rlatka.sharecosts;

import java.sql.SQLException;

import pl.rlatka.sharecosts.database.ShareCostsDatabase;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentProfile extends Fragment {

	private EditText descriptionEdit, phoneNumberEdit, oldPasswordEdit, newPasswordEdit, newPassword2Edit;
	private TextView flatmateName;
	private String description, phoneNumber, oldPassword, newPassword, newPassword2;
	private Button okButton, cancelButton;
	private ProgressBar progressBar;
	private ShareCosts shareCosts;
	private ShareCostsDatabase db;

	public FragmentProfile() {
		shareCosts = ShareCosts.getInstance();
		db = ShareCosts.getDatabase();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_profile, container, false);

		flatmateName = (TextView) view.findViewById(R.id.text_flatmate_name);
		descriptionEdit = (EditText) view.findViewById(R.id.edit_description);
		phoneNumberEdit = (EditText) view.findViewById(R.id.edit_phone_number);
		oldPasswordEdit = (EditText) view.findViewById(R.id.edit_old_password);
		newPasswordEdit = (EditText) view.findViewById(R.id.edit_new_password);
		newPassword2Edit = (EditText) view.findViewById(R.id.edit_new_password2);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		okButton = (Button) view.findViewById(R.id.button_ok);
		cancelButton = (Button) view.findViewById(R.id.button_cancel);

		flatmateName.setText(shareCosts.getFlatmate().getName());
		descriptionEdit.setText(shareCosts.getFlatmate().getDescription());
		phoneNumberEdit.setText(shareCosts.getFlatmate().getPhone());
		
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				description = descriptionEdit.getText().toString();
				phoneNumber = phoneNumberEdit.getText().toString();
				oldPassword = oldPasswordEdit.getText().toString();
				newPassword = newPasswordEdit.getText().toString();
				newPassword2 = newPassword2Edit.getText().toString();
				
				
				String flat = shareCosts.getFlat().getName();
				String flatmate = shareCosts.getFlatmate().getName();
				String password = getActivity().getSharedPreferences(ShareCosts.PREFS_NAME, 0).getString(ShareCosts.PREFS_FLATMATE_PASSWORD, "");
				
				if((!Sha256.encode(flatmate, oldPassword, 500).equals(password) && !oldPassword.equals("") )
						|| !newPassword.equals(newPassword2)) {
					Toast.makeText(getActivity(), "Nieprawid³owe has³o!", Toast.LENGTH_SHORT).show();
				} else {
					oldPassword = Sha256.encode(flatmate, oldPassword, 500);
					new UpdateFlatmate().execute(flat, flatmate, password);
				}
				
				
				
			}
		});
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				replaceFragment(new FragmentMain());
				
			}
		});

		return view;
	}

	private void showToast(final String text) {
		getActivity().runOnUiThread(new Runnable() {	
			@Override
			public void run() {
				Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void replaceFragment(Fragment fragment) {
		getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
	}
	
	private class UpdateFlatmate extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			progressBar.setVisibility(View.INVISIBLE);

			if(result) {
				showToast("Zaktualizowano profil");
				replaceFragment(new FragmentMain());
			} else {
				showToast("Coœ posz³o nie tak...");
			}
			
		}

		@Override
		protected Boolean doInBackground(String... params) {

			try {
				db.open();
				Log.d("", "#### " + params[0] + "  " + params[1] + "  " + params[2]);
				if (db.login(params[0], params[1], params[2])) {
					Log.d("Login", "Logged");
					db.updateDescription(shareCosts.getFlatmate().getId(), description);
					db.updatePhoneNumber(shareCosts.getFlatmate().getId(), phoneNumber);
					
					if(!newPassword.equals("") && !oldPassword.equals("")) {
						db.updatePassword(shareCosts.getFlatmate().getId(), newPassword);
					}
					
					

					return true;
				}

				Log.d("Login", "Lipa");

			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			} finally {
				db.close();
			}
			return false;
		}

	}
}


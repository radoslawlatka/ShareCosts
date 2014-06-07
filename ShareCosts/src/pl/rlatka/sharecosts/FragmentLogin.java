package pl.rlatka.sharecosts;

import java.sql.SQLException;

import pl.rlatka.sharecosts.database.ShareCostsDatabase;
import pl.rlatka.sharecosts.model.Flatmate;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class FragmentLogin extends Fragment {

	private EditText flatNameEdit, flatmateNameEdit, passwordEdit;
	private Button loginButton, registerButton;
	private ProgressBar progressBar;
	private ShareCosts shareCosts;
	private ShareCostsDatabase db;

	public FragmentLogin() {
		shareCosts = ShareCosts.getInstance();
		db = ShareCosts.getDatabase();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_login, container, false);

		flatNameEdit = (EditText) view.findViewById(R.id.flat_name);
		flatmateNameEdit = (EditText) view.findViewById(R.id.flatmate_name);
		passwordEdit = (EditText) view.findViewById(R.id.password);
		loginButton = (Button) view.findViewById(R.id.button_login);
		registerButton = (Button) view.findViewById(R.id.button_register);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(passwordEdit.getWindowToken(), 0);

				new Login().execute(flatNameEdit.getText().toString(),
						flatmateNameEdit.getText().toString(), 
						Sha256.encode(flatmateNameEdit.getText().toString(), passwordEdit.getText().toString(), 500));
			}
		});
		
		registerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(getActivity(), ActivityRegister.class);
				startActivity(intent);
			}
		});

		return view;
	}

	private class Login extends AsyncTask<String, Boolean, Integer> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			progressBar.setVisibility(View.INVISIBLE);
			switch (result) {
			case 1:
				Toast.makeText(getActivity(), "Zalogowano!", Toast.LENGTH_SHORT).show();

				getFragmentManager().beginTransaction().replace(R.id.container, new FragmentMain()).commit();
				
				break;
			case 2:
				Toast.makeText(getActivity(), "Nie mo¿na zalogowaæ!",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(getActivity(), "Brak po³¹czenia!",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}

		@Override
		protected Integer doInBackground(String... params) {

			try {
				db.open();
				if (db.login(params[0], params[1], params[2])) {

					shareCosts.setFlat(db.getFlatByName(flatNameEdit.getText().toString()));
					shareCosts.setFlatmates(db.getAllFlatmates(shareCosts.getFlat().getId()));

					for (Flatmate f : shareCosts.getFlatmates()) {
						if (f.getName().equals(
								flatmateNameEdit.getText().toString())) {
							shareCosts.setFlatmate(f);
							shareCosts.getFlatmates().remove(f);
							break;
						}
					}

					SharedPreferences.Editor edit = getActivity().getSharedPreferences(ShareCosts.PREFS_NAME, 0).edit();
					edit.putString(ShareCosts.PREFS_FLAT_NAME, params[0]);
					edit.putString(ShareCosts.PREFS_FLATMATE_NAME, params[1]);
					edit.putString(ShareCosts.PREFS_FLATMATE_PASSWORD, params[2]);
					edit.commit();

					return 1;
				}

				else
					return 2;

			} catch (SQLException e) {
				return 3;
			} finally {
				db.close();
			}
		}

	}
}

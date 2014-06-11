package pl.rlatka.sharecosts;

import java.sql.SQLException;

import pl.rlatka.sharecosts.database.ShareCostsDatabase;
import pl.rlatka.sharecosts.model.Flatmate;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
public class MainActivity extends FragmentActivity {

	private ShareCostsDatabase db;
	private ShareCosts shareCosts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		db = ShareCosts.getDatabase();
		shareCosts = ShareCosts.getInstance();
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				try {

					SharedPreferences prefs = getSharedPreferences(ShareCosts.PREFS_NAME, 0);
					String flat = prefs.getString(ShareCosts.PREFS_FLAT_NAME, "");
					String flatmate = prefs.getString(ShareCosts.PREFS_FLATMATE_NAME, "");
					String password = prefs.getString(ShareCosts.PREFS_FLATMATE_PASSWORD, "");
					
					ShareCosts.getDatabase().open();

					if( ShareCosts.getDatabase().login(flat, flatmate, password) ) {

						shareCosts.setFlat(db.getFlatByName(flat));
						shareCosts.setFlatmates(db.getAllFlatmates(shareCosts.getFlat().getId()));

						for (Flatmate f : shareCosts.getFlatmates()) {
							if (f.getName().equals(flatmate)) {
								shareCosts.setFlatmate(f);
								shareCosts.getFlatmates().remove(f);
								break;
							}
						}
						setContainer(new FragmentMain());
					} else
						setContainer(new FragmentLogin());
					
		
						
					ShareCosts.getDatabase().close();
					
				} catch (SQLException e) {
					
					e.printStackTrace();
					setContainer(new FragmentLogin());
				}
			}
			
		}).start();
	        
	}

	private void setContainer(Fragment fragment) {
			getFragmentManager().beginTransaction()
				.add(R.id.container, fragment).commitAllowingStateLoss();
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {

		case R.id.action_settings:
			Intent settingActivity = new Intent(this, Settings.class);
			startActivity(settingActivity);
			return true;

		case R.id.action_login:
			SharedPreferences.Editor edit = getApplicationContext().getSharedPreferences(ShareCosts.PREFS_NAME, 0).edit();
			edit.putString(ShareCosts.PREFS_FLAT_NAME, "");
			edit.putString(ShareCosts.PREFS_FLATMATE_NAME, "");
			edit.putString(ShareCosts.PREFS_FLATMATE_PASSWORD, "");
			edit.commit();
			
			getFragmentManager().beginTransaction().replace(R.id.container, new FragmentLogin()).commitAllowingStateLoss();
			
			return true;

		}

		return super.onOptionsItemSelected(item);
	}
}

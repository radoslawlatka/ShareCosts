package pl.rlatka.sharecosts;

import java.sql.SQLException;

import pl.rlatka.sharecosts.database.ShareCostsDatabase;
import pl.rlatka.sharecosts.model.Flatmate;
import android.content.Intent;
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

					
					ShareCosts.getDatabase().open();

					if(ShareCosts.getDatabase().login("aka", "a", Sha256.encode("a", "a", 500))) {

						shareCosts.setFlat(db.getFlatByName("aka"));
						shareCosts.setFlatmates(db.getAllFlatmates(shareCosts.getFlat().getId()));

						for (Flatmate f : shareCosts.getFlatmates()) {
							if (f.getName().equals("a")) {
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
					Log.e("", "Connection failed");
					e.printStackTrace();
				}
			}
			
		}).start();
		
		//setContainer(new FragmentLogin());
	        
	}

	private void setContainer(Fragment fragment) {
			getFragmentManager().beginTransaction()
				.add(R.id.container, fragment).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch(id) {
		
			case R.id.action_settings:
				Intent settingActivity = new Intent(this, Settings.class);
				startActivity(settingActivity);
			return true;

		}

		return super.onOptionsItemSelected(item);
	}
}

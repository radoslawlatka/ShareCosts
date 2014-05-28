package pl.rlatka.sharecosts;

import java.util.ArrayList;

import pl.rlatka.sharecosts.database.ShareCostsDatabase;
import pl.rlatka.sharecosts.model.Flat;
import pl.rlatka.sharecosts.model.Flatmate;
import android.app.Application;
import android.content.SharedPreferences;

public class ShareCosts extends Application {

	private static ShareCosts instance = null;
	private static ShareCostsDatabase database = null;
	
	public static final String PREFS_NAME = "ShareCostsPreferences";
	public static final String PREFS_DB_NAME = "db_name";
	public static final String PREFS_DB_ADDRESS = "db_address";
	public static final String PREFS_DB_USERNAME = "db_username";
	public static final String PREFS_DB_PASSWORD = "db_password";
	public static final String PREFS_DB_PORT = "db_port";

	private SharedPreferences prefs;
	private Flat flat;
	private Flatmate flatmate;
	private ArrayList<Flatmate> flatmates;

	public ShareCosts() {
		instance = this;
	}
	
	public static ShareCosts getInstance() {
	    if (instance == null)
	        synchronized (ShareCosts.class) {
	            if (instance == null)
	                instance = new ShareCosts();
	        }
	    return instance;
	}

	public static ShareCostsDatabase getDatabase() {
		if (database == null)
			synchronized (ShareCosts.class) {
				if (database == null)
					database = new ShareCostsDatabase( getInstance().getApplicationContext() );
			}
		return database;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		prefs = getSharedPreferences(PREFS_NAME, 0);

	}
	
	public SharedPreferences getPrefs() {
		return prefs;
	}

	public Flat getFlat() {
		return flat;
	}

	public void setFlat(Flat flat) {
		this.flat = flat;
	}

	public Flatmate getFlatmate() {
		return flatmate;
	}

	public void setFlatmate(Flatmate flatmate) {
		this.flatmate = flatmate;
	}

	public ArrayList<Flatmate> getFlatmates() {
		return flatmates;
	}

	public void setFlatmates(ArrayList<Flatmate> flatmates) {
		this.flatmates = flatmates;
	}

}

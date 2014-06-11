package pl.rlatka.sharecosts;

import java.sql.SQLException;
import java.util.ArrayList;

import pl.rlatka.sharecosts.database.ShareCostsDatabase;
import pl.rlatka.sharecosts.model.Expense;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

public class DialogDetails extends Activity {

	private ListView list;
	private RadioGroup radioGroup;
	private Button closeButton;
	private AdapterDebtsList debtsListAdapter;
	private AdapterExpensesList expensesListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_details);
		
		list = (ListView) findViewById(R.id.list);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		closeButton = (Button) findViewById(R.id.button_close);
		
		initListeners();
		
		new GetDebts().execute();
	}

	private void initListeners() {
		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup arg0, int id) {
				switch (id) {
				case R.id.radio_debts:
					Log.d("DialogDetails", "GetDebts");
					new GetDebts().execute();
					break;
				case R.id.radio_expenses:
					Log.d("DialogDetails", "GetExpenses");
					new GetExpenses().execute();
					break;
				}
			}
		});
		
	}
	
	private class GetDebts extends AsyncTask<Void, Void, Boolean> {

		private ShareCostsDatabase db = ShareCosts.getDatabase();
		private ArrayList<Expense> debts = new ArrayList<>();
		private int flatmateId = -1;
		
		@Override
		protected Boolean doInBackground(Void... arg0) {

			try {
				db.open();
				debts = db.getDebts(flatmateId);
			} catch (SQLException e) {
				e.printStackTrace();
				showToast(getString(R.string.toast_connection_failed));
				return false;
			} finally {
				db.close();
			}
			
			return true;
		}
		
		private void showToast(final String text) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
				}
			});
		}

		@Override
		protected void onPostExecute(Boolean result) {
			debtsListAdapter = new AdapterDebtsList(getApplicationContext(), R.layout.row_debt, debts, ShareCosts.getInstance().getFlatmates());
			list.setAdapter(debtsListAdapter);
		}

		@Override
		protected void onPreExecute() {
			flatmateId = ShareCosts.getInstance().getFlatmate().getId();
		}
		
	}
	
	private class GetExpenses extends AsyncTask<Void, Void, Boolean> {
		
		private ShareCostsDatabase db = ShareCosts.getDatabase();
		private ArrayList<Expense> expenses = new ArrayList<>();
		private int flatmateId = -1;
		
		@Override
		protected Boolean doInBackground(Void... arg0) {
			
			try {
				db.open();
				expenses = db.getExpenses(flatmateId);
			} catch (SQLException e) {
				e.printStackTrace();
				showToast(getString(R.string.toast_connection_failed));
				return false;
			} finally {
				db.close();
			}
			
			return true;
		}
		
		private void showToast(final String text) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
				}
			});
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			expensesListAdapter = new AdapterExpensesList(getApplicationContext(), R.layout.row_expense, expenses, ShareCosts.getInstance().getFlatmates());
			list.setAdapter(expensesListAdapter);
		}
		
		@Override
		protected void onPreExecute() {
			flatmateId = ShareCosts.getInstance().getFlatmate().getId();
		}
		
	}

}

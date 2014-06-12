package pl.rlatka.sharecosts;

import java.sql.SQLException;
import java.util.ArrayList;

import pl.rlatka.sharecosts.database.ShareCostsDatabase;
import pl.rlatka.sharecosts.model.Expense;
import pl.rlatka.sharecosts.model.ExpenseType;
import pl.rlatka.sharecosts.model.Flatmate;
import pl.rlatka.sharecosts.model.Status;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DialogAddExpense extends Activity {
	
	private int expenseTypeId;
	private TextView categoryName;
	private EditText amountEdit, descriptionEdit;
	private ListView flatmatesList;
	private Button okButton, cancelButton;
	private AdapterFlatmatesListAdd flatmatesListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_add_expense);
		setResult(Activity.RESULT_CANCELED);
		
		this.expenseTypeId = getIntent().getExtras().getInt("expenseId");

		okButton = (Button) findViewById(R.id.button_ok);
		cancelButton = (Button) findViewById(R.id.button_cancel);
		categoryName = (TextView) findViewById(R.id.text_category);
		amountEdit = (EditText) findViewById(R.id.edit_amount);
		descriptionEdit = (EditText) findViewById(R.id.edit_description);
		flatmatesList = (ListView) findViewById(R.id.list_flatmates);
		
		flatmatesListAdapter = new AdapterFlatmatesListAdd(this, R.layout.row_select_flatmate, ShareCosts.getInstance().getFlatmates());
		flatmatesList.setAdapter(flatmatesListAdapter);
		
		initListeners();
		
		new GetExpesneName().execute(expenseTypeId);
	}

	private void initListeners() {
		okButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {

				if(!amountEdit.getText().toString().matches("[1-9][0-9]*([.|,]{0,1}[0-9]{1,2})")) {
					Toast.makeText(getApplicationContext(), getString(R.string.toast_incorrect_amount), Toast.LENGTH_SHORT).show();
				} else if (flatmatesListAdapter.getCheckedFlatmates().size()==0) {
					Toast.makeText(getApplicationContext(), getString(R.string.toast_any_flatmate), Toast.LENGTH_SHORT).show();
				} else {
					new AddExpense().execute();
				}
				
			}
		});
		
		cancelButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
		
	}
	
	private ArrayList<Expense> splitExpense(ArrayList<Flatmate> flatmates, Flatmate creditor, int expenseTypeId, double amount) {
		ArrayList<Expense> expenses = new ArrayList<>();
		double expenseAmount = Math.round(amount/(flatmates.size()+1)*100.)/100.;
		
		ExpenseType expenseType = new ExpenseType(expenseTypeId, categoryName.getText().toString(), "");
		
		for(Flatmate f :  flatmates) {
			expenses.add(new Expense(-1, creditor, f, expenseType, new Status(1, "unpaid"), expenseAmount, descriptionEdit.getText().toString()));
		}
		
		return expenses;
	}
	
	private class AddExpense extends AsyncTask<Integer, Void, Boolean> {

		private ShareCostsDatabase db = ShareCosts.getDatabase();
		private ArrayList<Expense> expenses;
		private ArrayList<Flatmate> debtors;
		
		@Override
		protected Boolean doInBackground(Integer... arg) {

			try {
				db.open();
				ShareCosts sc = ShareCosts.getInstance();
				for(Expense e : splitExpense(flatmatesListAdapter.getCheckedFlatmates(), sc.getFlatmate(), expenseTypeId, Double.valueOf(amountEdit.getText().toString()))) {
					db.addExpense(e);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				showToast(getString(R.string.toast_connection_failed));
				return false;
			} finally {
				db.close();
			}
			
			return true;
		}

		private void showToast(final String string) {
			runOnUiThread(new Runnable() {				
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();					
				}
			});
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if(result) {
				showToast(getString(R.string.toast_expense_added));
				setResult(Activity.RESULT_OK);
				finish();
			}
		}

		@Override
		protected void onPreExecute() {
			expenses = new ArrayList<>();
			debtors = flatmatesListAdapter.getCheckedFlatmates(); 
		}
	}
	
	
	private class GetExpesneName extends AsyncTask<Integer, Void, String> {
		
		private ShareCostsDatabase db = ShareCosts.getDatabase();
		
		@Override
		protected String doInBackground(Integer... arg) {
			ExpenseType exType;
			try {
				db.open();
				exType = db.getDebtType(arg[0]);
			} catch (SQLException e) {
				e.printStackTrace();
				showToast(getString(R.string.toast_connection_failed));
				return "";
			} finally {
				db.close();
			}
			
			return exType.getName();
		}
		
		private void showToast(final String string) {
			runOnUiThread(new Runnable() {				
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();					
				}
			});
		}
		
		@Override
		protected void onPostExecute(String result) {
			categoryName.setText(result);
		}
		
	}

}

package pl.rlatka.sharecosts;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class DialogDetails extends Activity {

	private ListView list;
	private AdapterDebtsList debtsListAdapter;
	private AdapterExpensesList expensesListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_details);
	}

}

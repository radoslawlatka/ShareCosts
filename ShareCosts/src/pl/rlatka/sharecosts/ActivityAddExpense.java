package pl.rlatka.sharecosts;

import android.app.Activity;
import android.os.Bundle;

public class ActivityAddExpense extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_add_expense);
	}

}

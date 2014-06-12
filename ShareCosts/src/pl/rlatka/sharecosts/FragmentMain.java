package pl.rlatka.sharecosts;

import java.sql.SQLException;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class FragmentMain extends Fragment {

	private ShareCosts shareCosts;
	private TextView nameText, balanceText, debtsText, creditsText;
	private ImageButton detailsButton;
	private Button shoppingButton, billsButton, rentButton, partyButton, loanButton, otherButton, remindButton;
	
	public FragmentMain() {
		shareCosts = ShareCosts.getInstance();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		
		nameText = (TextView) view.findViewById(R.id.flatmate_name);
		balanceText = (TextView) view.findViewById(R.id.balance);
		debtsText = (TextView) view.findViewById(R.id.debts);
		creditsText = (TextView) view.findViewById(R.id.credits);
		detailsButton = (ImageButton) view.findViewById(R.id.button_details);
		shoppingButton = (Button) view.findViewById(R.id.button_shopping);
		billsButton = (Button) view.findViewById(R.id.button_bills);
		rentButton = (Button) view.findViewById(R.id.button_rent);
		partyButton = (Button) view.findViewById(R.id.button_party);
		loanButton = (Button) view.findViewById(R.id.button_loan);
		otherButton = (Button) view.findViewById(R.id.button_others);
		remindButton = (Button) view.findViewById(R.id.button_remind);
		
		nameText.setText(shareCosts.getFlatmate().getName());
	
		initListeners();
		
		new GetData().execute(0);
		
		return view;
	}
	
	private void initListeners() {
		nameText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				replaceFragment(new FragmentProfile());
			}
		});
		detailsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				Intent details = new Intent(getActivity(), DialogDetails.class);
				startActivity(details);
				
			}
		});
		
		shoppingButton.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				showAddExpenseDialog(2);
			}
		});
		
		billsButton.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				showAddExpenseDialog(1);
			}
		});
		
		partyButton.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				showAddExpenseDialog(3);
			}
		});
		
		rentButton.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				showAddExpenseDialog(4);
			}
		});
		
		loanButton.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				showAddExpenseDialog(5);
			}
		});
		
		otherButton.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				showAddExpenseDialog(6);
			}
		});
	}

	private void replaceFragment(Fragment fragment) {
		getFragmentManager().beginTransaction().replace(R.id.container, fragment).commitAllowingStateLoss();
	}
	
	private void showAddExpenseDialog(int categoryId) {
		Intent addShoppingExpense = new Intent(getActivity(), DialogAddExpense.class);
		addShoppingExpense.putExtra("expenseId", categoryId);
		startActivity(addShoppingExpense);
	}
	
	private void setDebts(double debts) {
		debtsText.setText("-" + debts);
	}

	private void setCredits(double credits) {
		creditsText.setText("" + credits);
	}
	
	private void setBalance() {
		Double balance = Double.parseDouble(debtsText.getText().toString()) + Double.parseDouble(creditsText.getText().toString());

		if(balance<-5)
			balanceText.setTextColor(getResources().getColor(R.color.red)); 
		else if ( balance >= 0 )
			balanceText.setTextColor(getResources().getColor(R.color.green));
		else
			balanceText.setTextColor(getResources().getColor(R.color.yellow));
		
		balanceText.setText(Math.round(balance*100)/100.0 +" z³");		
	}
	
	private class GetData extends AsyncTask<Integer, Integer, Integer> {

		private double debts, credits;
		
		@Override
		protected Integer doInBackground(Integer... params) {
			try {
				shareCosts.getDatabase().open();
				
				debts = shareCosts.getDatabase().getExpensesAmmount(shareCosts.getFlatmate());
				credits = shareCosts.getDatabase().getCreditsAmmount(shareCosts.getFlatmate());

			} catch (SQLException e) {}
			finally {
				shareCosts.getDatabase().close();
			}

			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			setDebts(debts);
			setCredits(credits);
			setBalance();
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		super.onCreateOptionsMenu(menu, inflater);
	}

}
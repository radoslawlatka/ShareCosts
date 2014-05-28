package pl.rlatka.sharecosts;

import java.sql.SQLException;

import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentMain extends Fragment {

	private ShareCosts shareCosts;
	private TextView nameText, balanceText, debtsText, creditsText;
	
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
		
		nameText.setText(shareCosts.getFlatmate().getName());
	
		new GetData().execute(0);
		
		return view;
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
				
				debts = shareCosts.getDatabase().getDebtsAmmount(shareCosts.getFlatmate());
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

}
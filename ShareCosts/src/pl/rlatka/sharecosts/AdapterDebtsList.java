package pl.rlatka.sharecosts;

import java.util.ArrayList;
import java.util.List;

import pl.rlatka.sharecosts.model.Expense;
import pl.rlatka.sharecosts.model.Flatmate;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class AdapterDebtsList extends ArrayAdapter<Expense> {

	private ArrayList<Expense> debts;
	private ArrayList<Flatmate> flatmates;
	private Context context;
	private int resource;

	public AdapterDebtsList(Context context, int resource, List<Expense> debts, List<Flatmate> flatmates) {
		super(context, resource, debts);

		this.debts = (ArrayList<Expense>) debts;
		this.flatmates = (ArrayList<Flatmate>) flatmates;
		this.resource = resource;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		DebtsHolder holder = null;
		final int pos = position;
		if (row == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			row = inflater.inflate(resource, parent, false);

			holder = new DebtsHolder();
			holder.flatmateName = (TextView) row.findViewById(R.id.text_flatmate_name);
			holder.amount = (TextView) row.findViewById(R.id.text_amount);
			holder.description = (TextView) row.findViewById(R.id.text_description);
			row.setTag(holder);
		} else {
			holder = (DebtsHolder) row.getTag();
		}

		Expense debt = debts.get(position);
		holder.flatmateName.setText(getFlatmateById(debt.getCreditor().getId()).getName());
		holder.amount.setText(String.format("%.2f", debt.getAmount()) + "z³");
		holder.description.setText(debt.getDescription());

		return row;
	}

	private static class DebtsHolder {
		TextView flatmateName;
		TextView amount;
		TextView description;
	}
	
	private Flatmate getFlatmateById(int id) {
		Flatmate flatmate = new Flatmate(-1, -1, "not found", "", "", "");
		
		for(Flatmate f : flatmates)
			if(f.getId() == id)
				flatmate = f;
		
		return flatmate;
	}

}
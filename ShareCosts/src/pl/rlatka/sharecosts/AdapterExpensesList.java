package pl.rlatka.sharecosts;

import java.util.ArrayList;
import java.util.List;

import pl.rlatka.sharecosts.model.Expense;
import pl.rlatka.sharecosts.model.Flatmate;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class AdapterExpensesList  extends ArrayAdapter<Expense> {

	private ArrayList<Expense> expenses;
	private ArrayList<Flatmate> flatmates;
	private Context context;
	private int resource;

	public AdapterExpensesList(Context context, int resource, List<Expense> expenses, List<Flatmate> flatmates) {
		super(context, resource, expenses);

		this.expenses = (ArrayList<Expense>) expenses;
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
			holder.deleteButton = (ImageButton) row.findViewById(R.id.button_delete);
			row.setTag(holder);
		} else {
			holder = (DebtsHolder) row.getTag();
		}

		Expense expense = expenses.get(position);
		holder.flatmateName.setText(getFlatmateById(expense.getDebtor().getId()).getName());
		Log.e("", "### " +expense.getDebtor().getId());
		holder.amount.setText(String.format("%.2f", expense.getAmount()) + "z³");
		holder.description.setText(expense.getDescription());
		holder.deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				expenses.remove(pos);
				notifyDataSetChanged();
			}
		});
		
		return row;
	}

	private static class DebtsHolder {
		TextView flatmateName;
		TextView amount;
		TextView description;
		ImageButton deleteButton;
	}
	
	private Flatmate getFlatmateById(int id) {
		Flatmate flatmate = new Flatmate(-1, -1, "not found", "", "", "");
		
		for(Flatmate f : flatmates)
			if(f.getId() == id)
				flatmate = f;
		
		return flatmate;
	}

}


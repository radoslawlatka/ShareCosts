package pl.rlatka.sharecosts;

import java.util.ArrayList;
import java.util.List;

import pl.rlatka.sharecosts.model.Flatmate;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class AdapterFlatmatesListReg extends ArrayAdapter<Flatmate> {

	private ArrayList<Flatmate> flatmates;
	private Context context;
	private int resource;

	public AdapterFlatmatesListReg(Context context, int resource, List<Flatmate> objects) {
		super(context, resource, objects);

		this.flatmates = (ArrayList<Flatmate>) objects;
		this.resource = resource;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		FlatmateHolder holder = null;
		final int pos = position;
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(resource, parent, false);

			holder = new FlatmateHolder();
			holder.flatmateName = (TextView) row.findViewById(R.id.text_flatmate_name);
			holder.deleteButton = (ImageButton) row.findViewById(R.id.button_delete);
			row.setTag(holder);
		} else {
			holder = (FlatmateHolder) row.getTag();
		}

		Flatmate flatmate = flatmates.get(position);
		holder.flatmateName.setText(flatmate.getName());
		holder.deleteButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				flatmates.remove(pos);
				notifyDataSetChanged();
			}
		});
		return row;
	}

	private static class FlatmateHolder {
		TextView flatmateName;
		ImageButton deleteButton;
	}

	public ArrayList<Flatmate> getFlatmates() {
		return flatmates;
	}

	public void setFlatmates(ArrayList<Flatmate> flatmates) {
		this.flatmates = flatmates;
	}
}

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class AdapterFlatmatesListAdd  extends ArrayAdapter<Flatmate> {

	private ArrayList<Flatmate> flatmates;
	private ArrayList<Flatmate> checkedFlatmates;
	private ArrayList<Flatmate> uncheckedFlatmates;
	private Context context;
	private int resource;

	public AdapterFlatmatesListAdd(Context context, int resource, List<Flatmate> objects) {
		super(context, resource, objects);

		this.flatmates = (ArrayList<Flatmate>) objects;
		
		this.resource = resource;
		this.context = context;
		this.checkedFlatmates = new ArrayList<>();
		this.uncheckedFlatmates = new ArrayList<>();
		
		for(Flatmate f : flatmates)
			checkedFlatmates.add(f);
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
			holder.checkBox = (CheckBox) row.findViewById(R.id.checkBox);
			row.setTag(holder);
		} else {
			holder = (FlatmateHolder) row.getTag();
		}

		Flatmate flatmate = flatmates.get(position);
		holder.flatmateName.setText(flatmate.getName());
		holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

		       @Override
		       public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		    	   if(isChecked) {
		    		   uncheckedFlatmates.remove(flatmates.get(pos));
		    		   checkedFlatmates.add(flatmates.get(pos));
		    	   } else {
		    		   uncheckedFlatmates.add(flatmates.get(pos));
		    		   checkedFlatmates.remove(flatmates.get(pos));
		    	   }
		       }
		   }
		); 
		return row;
	}

	private static class FlatmateHolder {
		TextView flatmateName;
		CheckBox checkBox;
	}

	public ArrayList<Flatmate> getFlatmates() {
		return flatmates;
	}

	public void setFlatmates(ArrayList<Flatmate> flatmates) {
		this.flatmates = flatmates;
	}

	public ArrayList<Flatmate> getCheckedFlatmates() {
		return checkedFlatmates;
	}

	public ArrayList<Flatmate> getUncheckedFlatmates() {
		return uncheckedFlatmates;
	}
}

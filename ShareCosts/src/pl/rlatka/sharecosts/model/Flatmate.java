package pl.rlatka.sharecosts.model;

import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Flatmate {
	
	private int id;
	private int flatId;
	private String name;
	private String password;
	private String phone;
	private String description;
	
	public Flatmate(int id, int flatId, String name, String password, String phone, String description) {
		this.id = id;
		this.flatId = flatId;
		this.name = name;
		this.password = password;
		this.phone = phone;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFlatId() {
		return flatId;
	}

	public void setFlat(int flatId) {
		this.flatId = flatId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return id + ", " + name + ", " + description
				+ ", flat id: " + flatId;
	}
	
	
}

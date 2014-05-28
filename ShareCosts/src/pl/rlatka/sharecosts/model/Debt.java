package pl.rlatka.sharecosts.model;

public class Debt {
	private int id;
	private Flatmate creditor;
	private Flatmate debtor;
	private DebtType type;
	private Status status;
	private double ammount;
	private String description;
	
	public Debt(int id, Flatmate creditor, Flatmate debtor, DebtType type,
			Status status, double ammount, String description) {
		this.id = id;
		this.creditor = creditor;
		this.debtor = debtor;
		this.type = type;
		this.status = status;
		this.ammount = ammount;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Flatmate getCreditor() {
		return creditor;
	}

	public void setCreditor(Flatmate creditor) {
		this.creditor = creditor;
	}

	public Flatmate getDebtor() {
		return debtor;
	}

	public void setDebtor(Flatmate debtor) {
		this.debtor = debtor;
	}

	public DebtType getType() {
		return type;
	}

	public void setType(DebtType type) {
		this.type = type;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public double getAmmount() {
		return ammount;
	}

	public void setAmmount(double ammount) {
		this.ammount = ammount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		if( 1 == status.getId() )
			return id + ", " + debtor.getName() + " LECI HAJS "
				+ String.format("%.2f", ammount) + "z³ dla "
				+ creditor.getName() + " za " + type.getName();
		else 
			return id + ", " + debtor.getName() + " ZAP£ACI£ "
			+ String.format("%.2f", ammount) + "z³ dla "
			+ creditor.getName() + " za " + type.getName();
	}
	
}

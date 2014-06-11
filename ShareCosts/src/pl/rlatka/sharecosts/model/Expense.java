package pl.rlatka.sharecosts.model;

public class Expense {
	private int id;
	private Flatmate creditor;
	private Flatmate debtor;
	private ExpenseType type;
	private Status status;
	private double amount;
	private String description;
	
	public Expense(int id, Flatmate creditor, Flatmate debtor, ExpenseType type,
			Status status, double amount, String description) {
		this.id = id;
		this.creditor = creditor;
		this.debtor = debtor;
		this.type = type;
		this.status = status;
		this.amount = amount;
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

	public ExpenseType getType() {
		return type;
	}

	public void setType(ExpenseType type) {
		this.type = type;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
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
				+ String.format("%.2f", amount) + "z³ dla "
				+ creditor.getName() + " za " + type.getName();
		else 
			return id + ", " + debtor.getName() + " ZAP£ACI£ "
			+ String.format("%.2f", amount) + "z³ dla "
			+ creditor.getName() + " za " + type.getName();
	}
	
}

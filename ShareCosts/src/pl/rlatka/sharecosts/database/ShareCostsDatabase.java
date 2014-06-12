package pl.rlatka.sharecosts.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import pl.rlatka.sharecosts.ShareCosts;
import pl.rlatka.sharecosts.model.Expense;
import pl.rlatka.sharecosts.model.ExpenseType;
import pl.rlatka.sharecosts.model.Flat;
import pl.rlatka.sharecosts.model.Flatmate;
import pl.rlatka.sharecosts.model.Status;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class ShareCostsDatabase {
	private static final String DTAG = "ShareCostsDatabase";
	
    private static String URL;
    private static String ADDRESS;
    private static String PORT;
    private static String DATABASE_NAME;
    private static String USER;
    private static String PASSWORD;
    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

	// table FLAT
	public static final String FLAT_TABLE = "flat";
	public static final String FLAT_ID = "id";
	public static final String FLAT_NAME = "name";
	public static final String FLAT_DESCRIPTION = "description";
	
	// table FLATMATE
	public static final String FLATMATE_TABLE = "flatmate";
	public static final String FLATMATE_ID = "id";
	public static final String FLATMATE_FLAT_ID = "flat_id";
	public static final String FLATMATE_NAME = "name";
	public static final String FLATMATE_PASSWORD = "password";
	public static final String FLATMATE_PHONE = "phone";
	public static final String FLATMATE_PICTURE = "picture";
	public static final String FLATMATE_DESCRIPTION = "description";
    
	// table DEBT
	public static final String EXPENSE_TABLE = "debt";
	public static final String EXPENSE_ID = "id";
	public static final String EXPENSE_CREDITOR_ID = "creditor_id";
	public static final String EXPENSE_DEBTOR_ID = "debtor_id";
	public static final String EXPENSE_DEBT_TYPE_ID = "debt_type_id";
	public static final String EXPENSE_STATUS_ID = "status_id";
	public static final String EXPENSE_AMOUNT = "amount";
	public static final String EXPENSE_DESCRIPTION = "description";
	
	// table STATUS
	public static final String STATUS_TABLE = "status";
	public static final String STATUS_ID = "id";
	public static final String STATUS_NAME = "name";
	
	// table DEBT_TYPE
	public static final String EXPENSE_TYPE_TABLE = "debt_type";
	public static final String EXPENSE_TYPE_ID = "id";
	public static final String EXPENSE_TYPE_NAME = "name";
	public static final String EXPENSE_TYPE_DESCRIPTION = "description";

	private Connection conn = null;
	private Context context;
	private SharedPreferences prefs;
	private PreparedStatement pStatement;
	private ResultSet rs;
	
	public ShareCostsDatabase(Context _context) { 
		context = _context;
		prefs = context.getSharedPreferences(ShareCosts.PREFS_NAME, 0);		
    	try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
        	Log.e(DTAG, "Failed to load JDBC!");
        }
	}
	
	public double getCreditsAmmount(Flatmate flatmate) throws SQLException {
		Log.d(DTAG, "Getting credits ammount...");
		try {			
			pStatement = conn.prepareStatement("SELECT SUM(" + EXPENSE_AMOUNT + ") FROM " + EXPENSE_TABLE + " WHERE " + EXPENSE_CREDITOR_ID + "=?");
			pStatement.setInt(1, flatmate.getId());
			pStatement.executeQuery();
			
			rs = pStatement.executeQuery();
			
			if( rs.first() ){
				return Math.round(rs.getDouble(1)*100)/100.0;
			} else {
				return 0;
			}

		} finally {
			if( rs != null )
				rs.close();
		}	
	}
	

	public void updateDescription(int flatmateId, String description) throws SQLException {
		Log.d(DTAG, "Update flatmate description...");
		try {			
			pStatement = conn.prepareStatement("UPDATE " + FLATMATE_TABLE + " SET " + FLATMATE_DESCRIPTION + "=? WHERE " + FLATMATE_ID + "=?");
			pStatement.setString(1, description);
			pStatement.setInt(2, flatmateId);
			pStatement.executeUpdate();
		} finally {
			if( rs != null )
				rs.close();
		}
	} 
	
	public void updatePassword(int flatmateId, String password) throws SQLException {
		Log.d(DTAG, "Update flatmate description...");
		try {			
			pStatement = conn.prepareStatement("UPDATE " + FLATMATE_TABLE + " SET " + FLATMATE_PASSWORD + "=? WHERE " + FLATMATE_ID + "=?");
			pStatement.setString(1, password);
			pStatement.setInt(2, flatmateId);
			pStatement.executeUpdate();
		} finally {
			if( rs != null )
				rs.close();
		}
	} 
	
	public void updatePhoneNumber(int flatmateId, String phoneNumber) throws SQLException {
		Log.d(DTAG, "Update flatmate description...");
		try {			
			pStatement = conn.prepareStatement("UPDATE " + FLATMATE_TABLE + " SET " + FLATMATE_PHONE + "=? WHERE " + FLATMATE_ID + "=?");
			pStatement.setString(1, phoneNumber);
			pStatement.setInt(2, flatmateId);
			pStatement.executeUpdate();
		} finally {
			if( rs != null )
				rs.close();
		}
	} 
	
	public double getExpensesAmmount(Flatmate flatmate) throws SQLException {
		Log.d(DTAG, "Getting credits ammount...");
		try {			
			pStatement = conn.prepareStatement("SELECT SUM(" + EXPENSE_AMOUNT + ") FROM " + EXPENSE_TABLE + " WHERE " + EXPENSE_DEBTOR_ID + "=?");
			pStatement.setInt(1, flatmate.getId());
			
			rs = pStatement.executeQuery();
			
			if( rs.first() ){
				return Math.round(rs.getDouble(1)*100)/100.0;
			} else {
				return 0;
			}
			
		} finally {
			if( rs != null )
				rs.close();
		}	
	}
	
	public void removeExpenses(int id) throws SQLException {
		Log.d(DTAG, "Deleting expense...");
		try {			
			pStatement = conn.prepareStatement("DELETE FROM " + EXPENSE_TABLE + " WHERE " + EXPENSE_ID + "=?");
			pStatement.setInt(1, id);	
			pStatement.execute();

		} finally {
			if( rs != null )
				rs.close();
		}	
	}
	
	public void setStatus(Expense debt, Status status) throws SQLException {
		Log.d(DTAG, "Setting status...");
			
			pStatement = conn.prepareStatement("UPDATE " + EXPENSE_TABLE + " SET "  + 
					EXPENSE_STATUS_ID + "=?" + 
					" WHERE " + EXPENSE_ID + "=?");

			pStatement.setInt(1, status.getId());
			pStatement.setInt(2, debt.getId());

			if( pStatement.executeUpdate() == 0 )
				throw new SQLException("Creating flat failed, no rows affected");

	}

	public boolean login(String flatName, String flatmateName, String password) throws SQLException {
		Log.d(DTAG, "Login to database...");
		
		try {			
			pStatement = conn.prepareStatement("SELECT * FROM " + FLATMATE_TABLE + " WHERE " +
					FLATMATE_FLAT_ID + "=(SELECT " + FLAT_ID + " FROM " + FLAT_TABLE + " WHERE " + 
											FLAT_NAME + "=?) AND " +
					FLATMATE_NAME + "=? AND " + FLATMATE_PASSWORD + "=?");

			pStatement.setString(1, flatName);
			pStatement.setString(2, flatmateName);
			pStatement.setString(3, password);
			
			rs = pStatement.executeQuery();
			
			if( rs.first() ){
				return true;
			} else {
				return false;
			}
			
		} finally {
			if( rs != null )
				rs.close();
		}
	}
	
	public void addExpense(Expense debt) throws SQLException {
		Log.d(DTAG, "Inserting new debt...");
		
		try {			
			pStatement = conn.prepareStatement("INSERT INTO " + EXPENSE_TABLE + " (" +
					EXPENSE_CREDITOR_ID + ", " +
					EXPENSE_DEBTOR_ID + ", " +
					EXPENSE_DEBT_TYPE_ID + ", " +
					EXPENSE_AMOUNT + ", " +
					EXPENSE_DESCRIPTION + ") " +
					" VALUES (?, ? ,? , ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			pStatement.setInt(1, debt.getCreditor().getId());
			pStatement.setInt(2, debt.getDebtor().getId());
			pStatement.setInt(3, debt.getType().getId());
			pStatement.setDouble(4, debt.getAmount());
			pStatement.setString(5, debt.getDescription());
			
			if( pStatement.executeUpdate() == 0 )
				throw new SQLException("Creating flat failed, no rows affected");
			
			rs = pStatement.getGeneratedKeys();
			
			if( rs.next() ) {
				debt.setId(rs.getInt(1));
			} else {
				throw new SQLException("Creating flat failed, no generated key obtained");
			}

		} finally {
			if( rs != null )
				rs.close();
		}
	}
	
	public void addFlatmate(Flatmate flatmate) throws SQLException {
		Log.d(DTAG, "Inserting new flatmate...");
		try {
			pStatement = conn.prepareStatement("INSERT INTO " + FLATMATE_TABLE + " (" 
					+ FLATMATE_FLAT_ID + ", " 
					+ FLATMATE_NAME + ", " 
					+ FLATMATE_PASSWORD + ", " 
					+ FLATMATE_PHONE + ", "
					+ FLATMATE_DESCRIPTION + ") "
					+ "VALUES (?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			pStatement.setInt(1, flatmate.getFlatId());
			pStatement.setString(2, flatmate.getName());
			pStatement.setString(3, flatmate.getPassword());
			pStatement.setString(4, flatmate.getPhone());
			pStatement.setString(5, flatmate.getDescription());
			
			if( pStatement.executeUpdate() == 0 )
				throw new SQLException("Creating flat failed, no rows affected");
			
			rs = pStatement.getGeneratedKeys();
			
			if( rs.next() ) {
				flatmate.setId(rs.getInt(1));
			} else {
				throw new SQLException("Creating flat failed, no generated key obtained");
			}
			
		} finally {
			if( rs != null )
				rs.close();
		}
	}
	
	public int addFlat(Flat flat) throws SQLException {
		Log.d(DTAG, "Inserting new flat...");
		try {
			pStatement = conn.prepareStatement("INSERT INTO " + FLAT_TABLE + " (" + FLAT_NAME +
					", " + FLAT_DESCRIPTION + ") VALUES (?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			pStatement.setString(1, flat.getName());
			pStatement.setString(2, flat.getDescription());
			
			if( pStatement.executeUpdate() == 0 )
				throw new SQLException("Creating flat failed, no rows affected");
			
			rs = pStatement.getGeneratedKeys();
			
			if( rs.next() ) {
				flat.setId(rs.getInt(1));
				return rs.getInt(1);
			} else {
				throw new SQLException("Creating flat failed, no generated key obtained");
			}
			
		} finally {
			if( rs != null )
				rs.close();
		}
	}
	
	public ArrayList<Expense> getDebts(int flatmateId) throws SQLException {
		return getExpensess(EXPENSE_DEBTOR_ID, flatmateId);
	}

	public ArrayList<Expense> getExpenses(int flatmateId) throws SQLException {
		return getExpensess(EXPENSE_CREDITOR_ID, flatmateId);
	}
	
	private ArrayList<Expense> getExpensess(String whoId,int flatmateId) throws SQLException {
		ArrayList<Expense> debts = new ArrayList<>();
		
		try {
			pStatement = conn.prepareStatement("SELECT * FROM " + EXPENSE_TABLE
					+ " WHERE " + whoId + "=?");
			pStatement.setInt(1, flatmateId);
			
			ResultSet rs = pStatement.executeQuery();
			
			while( rs.next() ) {
				debts.add( new Expense(
						rs.getInt(EXPENSE_ID), 
						getFlatmateById(rs.getInt(EXPENSE_CREDITOR_ID)), 
						getFlatmateById(rs.getInt(EXPENSE_DEBTOR_ID)), 
						getDebtType(rs.getInt(EXPENSE_DEBT_TYPE_ID)),
						getStatus(rs.getInt(EXPENSE_STATUS_ID)),
						rs.getDouble(EXPENSE_AMOUNT),
						rs.getString(EXPENSE_DESCRIPTION)
						) );
			}
		} finally {
			if( rs != null )
				rs.close();
		}
		
		return debts;
	}

	public ArrayList<ExpenseType> getDebtTypes() throws SQLException {
		Log.d(DTAG, "Getting all debt types...");
		ArrayList<ExpenseType> types = new ArrayList<>();
		
		try {
			pStatement = conn.prepareStatement("SELECT * FROM " + EXPENSE_TYPE_TABLE);
			rs = pStatement.executeQuery();

			while( rs.next() ) {
				types.add( new ExpenseType(rs.getInt(EXPENSE_TYPE_ID), rs.getString(EXPENSE_TYPE_NAME),
						rs.getString(EXPENSE_TYPE_DESCRIPTION)) );
			}
		} finally {
			if( rs != null )
				rs.close();
		}
		return types;
	}

	public ExpenseType getDebtType(int id) throws SQLException {
		Log.d(DTAG, "Getting debt type by id...");
		
		try {
			pStatement = conn.prepareStatement("SELECT * FROM " + EXPENSE_TYPE_TABLE
					+ " WHERE " + EXPENSE_TYPE_ID + "=?");
			pStatement.setInt(1, id);
			
			rs = pStatement.executeQuery();
			
			if( rs.first())
				return new ExpenseType(rs.getInt(EXPENSE_TYPE_ID), rs.getString(EXPENSE_TYPE_NAME),
						rs.getString(EXPENSE_TYPE_DESCRIPTION));
		} finally {
			if( rs != null )
				rs.close();
		}
		return null;
	}

	public Status getStatus(int id) throws SQLException {
		Log.d(DTAG, "Getting debt status by id...");
		
		try {
			pStatement = conn.prepareStatement("SELECT * FROM " + STATUS_TABLE
					+ " WHERE " + STATUS_ID + "=?");
			pStatement.setInt(1, id);
			rs = pStatement.executeQuery();
			
			if( rs.first()) {
				return new Status(rs.getInt(STATUS_ID), rs.getString(STATUS_NAME));
			}
				
		} finally {
			if( rs != null )
				rs.close();
		}
		return null;
	}	
	
	public ArrayList<Flatmate> getAllFlatmates(int flatId) throws SQLException {
		Log.d(DTAG, "Getting all flatmates by id...");
		ArrayList<Flatmate> flatmates = new ArrayList<>();

		try {
			pStatement = conn.prepareStatement("SELECT * FROM " + FLATMATE_TABLE
					+ " WHERE " + FLATMATE_FLAT_ID + "=?");
			pStatement.setInt(1, flatId);

			rs = pStatement.executeQuery();

			while( rs.next() ) {				
				flatmates.add( new Flatmate(rs.getInt(FLATMATE_ID), rs.getInt(FLATMATE_FLAT_ID), 
						rs.getString(FLATMATE_NAME), rs.getString(FLATMATE_PASSWORD), 
						rs.getString(FLATMATE_PHONE), rs.getString(FLATMATE_DESCRIPTION)) );
			}
		} finally {
			if( rs != null )
				rs.close();
		}
		return flatmates;
	}

	public Flatmate getFlatmateById(int id) throws SQLException {
		Log.d(DTAG, "Getting flatmate by id...");
		
		try {
			pStatement = conn.prepareStatement("SELECT * FROM " + FLATMATE_TABLE
					+ " WHERE " + FLATMATE_ID + "=?");
			pStatement.setInt(1, id);
			
			rs = pStatement.executeQuery();
			
			if( rs.first() ) {				
				return new Flatmate(rs.getInt(FLATMATE_ID), rs.getInt(FLATMATE_FLAT_ID), 
						rs.getString(FLATMATE_NAME), rs.getString(FLATMATE_PASSWORD), 
						rs.getString(FLATMATE_PHONE), rs.getString(FLATMATE_DESCRIPTION)) ;
			}
		} finally {
			if( rs != null )
				rs.close();
		}
		return null;
	}

	public Flat getFlatById(int id) throws SQLException {
		Log.d(DTAG, "Getting flat by id...");
		try {
			pStatement = conn.prepareStatement("SELECT * FROM " + FLAT_TABLE
					+ " WHERE " + FLAT_ID + "=?");
			pStatement.setInt(1, id);
			
			rs = pStatement.executeQuery();
			
			if( rs.first() ) {
				return new Flat(rs.getInt(FLAT_ID), rs.getString(FLAT_NAME), rs.getString(FLAT_DESCRIPTION));
			}
		} finally {
			if( rs != null )
				rs.close();
		}
		return null;
	}
	
	public Flat getFlatByName(String name) throws SQLException {
		Log.d(DTAG, "Getting flat by name...");
		try {

			pStatement = conn.prepareStatement("SELECT * FROM " + FLAT_TABLE
							+ " WHERE " + FLAT_NAME + "=?");
			pStatement.setString(1, name);
			
			rs = pStatement.executeQuery();
			
			if( rs.first() ) {
				return new Flat(rs.getInt(FLAT_ID), rs.getString(FLAT_NAME), rs.getString(FLAT_DESCRIPTION));
			}
			
		} finally {
			if( rs != null )
				rs.close();
		}
		return null;
	}

    public void open() throws SQLException {	
    	conn = createConnection();
    }

    public void close() {
		try {
			if(conn != null)
				conn.close();
			Log.d(DTAG, "Connection closed");
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public boolean connectionTest() {
    	prepareDataToLogin();
    	try {
    		Log.d(DTAG, "Connecting to database...");
    			conn = DriverManager.getConnection(URL, USER, PASSWORD);
    		Log.d(DTAG, "Connected");
    		return true;
    	} catch (SQLException e) {
    		Log.d(DTAG, "Connection failed!");
    		e.printStackTrace();
    		return false;
    	} finally {
    		try {
    			conn.close();
    			Log.d(DTAG, "Connection closed");
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}
    }
	
    private Connection createConnection() throws SQLException {    	
        Connection connection = null;
		
		prepareDataToLogin();
    	
    	Log.d(DTAG, "Connecting to database...");
    	DriverManager.setLoginTimeout(4);
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        Log.d(DTAG, "Connected");
        return connection;
    } 	
    
	private void prepareDataToLogin() {
		ADDRESS 		= prefs.getString(ShareCosts.PREFS_DB_ADDRESS, ""); 
    	PORT 			= prefs.getString(ShareCosts.PREFS_DB_PORT, "");
    	DATABASE_NAME 	= prefs.getString(ShareCosts.PREFS_DB_NAME, "");
    	USER 			= prefs.getString(ShareCosts.PREFS_DB_USERNAME, "") ;
    	PASSWORD 		= prefs.getString(ShareCosts.PREFS_DB_PASSWORD, "");
    	
    	URL = "jdbc:mysql://" + ADDRESS + ":" + PORT + "/" + DATABASE_NAME + "?useUnicode=true&characterEncoding=utf8";
    	
    	System.out.println(URL);
    	System.out.println(USER + " " + PASSWORD);
	}
	

}

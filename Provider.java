package bank;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Provider {
	ServerSocket providerSocket;
	Socket connection = null;

	ObjectOutputStream out;
	ObjectInputStream in;
	Scanner input;
	String message;
	String user_input;
	int num1;
	int num2;
	int result;
	boolean login = false;

	String name, city, username, password, dummy;
	String choice, ch, operation;
	Transaction transac = new Transaction();
	Scanner sc = new Scanner(System.in);

	Provider() {
		input = new Scanner(System.in);
	}

	@SuppressWarnings("unused")
	void listener() throws ClassNotFoundException {
		try {
			// 1. creating a server socket
			providerSocket = new ServerSocket(2004, 10);
			// 2. Wait for connection
			System.out.println("Waiting for connection");

			connection = providerSocket.accept();

			System.out.println("Connection received from " + connection.getInetAddress().getHostName());
			// 3. get Input and Output streams
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			// 4. The two parts communicate via the input and output streams
			do {
				try
				{ 
					if (login == false) { // if user not logged in going to login menu
						do {
							sendMessage("Welcome to login menu.\n" + "Please choose: \n" + "(1) Register \n"
									+ "(2) Login\n" + "(q) Exit");
							message = (String) in.readObject();
							choice = new String(message);
							String LogUsername;
							String LogPassword;
							switch (choice) {
							case "1":
								double amount;
								long accountNo;
								double openingBalance;
								// city, username, password

								// ask for balance
								sendMessage("Please Enter your full name :");
								message = (String) in.readObject();
								name = new String(message);

								sendMessage("Please Enter your Address :");
								message = (String) in.readObject();
								city = new String(message);

								sendMessage("Please Enter your username :");
								message = (String) in.readObject();
								username = new String(message);

								sendMessage("Please Enter your password :");
								message = (String) in.readObject();
								password = new String(message);

								// ask for account number
								sendMessage("Enter the opening balance :");
								message = (String) in.readObject();
								openingBalance = new Double(message);

								transac.transaction(0, "Opening", name, city, username, password, openingBalance);
								break;

							case "2":									
								sendMessage("Please enter username:");
								message = (String) in.readObject();
								LogUsername = new String(message);

								sendMessage("Please enter password:");
								message = (String) in.readObject();
								LogPassword = new String(message);							
								operation = "logining";
								transac.transaction(0, "logining", name, city, LogUsername, LogPassword, 0);
								break;
																						
							case "q":
								sendMessage("Thank you!");
								in.close();
								out.close();
								providerSocket.close();
								break;
								
							case "Thank You":
								sendMessage("Thank you!");
								break;

							default:
								sendMessage("Wrong choose!" + "\nEnter any letter to continue.");
								message = (String) in.readObject();
								dummy = new String(message);
							}
						} while (login == false && choice != "q");
					} // end of if

					else { // if user logged in going to user menu
						do {
							sendMessage("Welcome to user menu:\n" + "Please choice: \n" + "(1) New account \n"
									+ "(2) Transaction\n" + "(3) View Account Information\n" + "(q) Exit");

							message = (String) in.readObject();
							choice = new String(message);

							long accountNo;
							double amount;
							switch (choice) {
							case "1":
								double openingBalance;
								// ask for user name
								sendMessage("Please Enter your full name :");
								message = (String) in.readObject();
								name = new String(message);

								sendMessage("Please Enter your address :");
								message = (String) in.readObject();
								city = new String(message);

								sendMessage("Please Enter your username :");
								message = (String) in.readObject();
								username = new String(message);

								sendMessage("Please Enter your password :");
								message = (String) in.readObject();
								password = new String(message);

								// ask for account number
								sendMessage("Enter the opening balance :");
								message = (String) in.readObject();
								openingBalance = new Double(message);

								transac.transaction(0, "Opening", name, city, username, password, openingBalance);
								break;

							case "2":
								sendMessage("Please choice: \n" + "a. Deposit\n" + "b. Withdraw");
								message = (String) in.readObject();
								ch = new String(message);

								if (ch.equalsIgnoreCase("a"))
									operation = "Deposit";
								else if (ch.equalsIgnoreCase("b"))
									operation = "Withdraw";
								else {
									operation = "Invalid option";
								}
								sendMessage("Enter Account Number:");
								message = (String) in.readObject();
								accountNo = new Integer(message);

								sendMessage("Enter Amount:");
								message = (String) in.readObject();
								amount = new Double(message);

								transac.transaction(accountNo, operation, name, city, username, password, amount);
								break;

							case "3":
								sendMessage("Enter Account Number:");
								message = (String) in.readObject();
								accountNo = new Integer(message);

								operation = "showInfo";
								transac.transaction(accountNo, operation, name, city, username, password, 0);
								break;

							case "q":
								// log out customer if "q"
								login = false;
								sendMessage("Thank you!");
								in.close();
								out.close();
								providerSocket.close();
								break;
								
							case "Thank You":
								sendMessage("Thank you!");
								break;								

							default:
								sendMessage("Wrong choose!!" + "\nEnter any letter to continue.");
								message = (String) in.readObject();
								dummy = new String(message);
							}
						} while (choice != "q");
						sc.close();
					} // end of else
				} catch (ClassNotFoundException classnot) {
					sendMessage("Data received in unknown format" + "\nEnter any letter to continue.");
					message = (String) in.readObject();
					dummy = new String(message);

				}
			} while (!message.equals("Thank You!"));
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			// 4: Closing connection
			try {
				in.close();
				out.close();
				providerSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("server>" + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public static void main(String args[]) throws ClassNotFoundException {
		Provider server = new Provider();
		while (true) {
			server.listener();
		}
	}

	//Bank class

	public class Bank {
			// HashMap<Integer, String> hmap = new HashMap<Integer, String>();
			private ArrayList<Balance> balanceList;
			Iterator<Balance> itr;
	
			public Bank() {
				balanceList = new ArrayList<Balance>();
			}
	
			public void newAccount(Balance e) {
				balanceList.add(e);
			}
			
			
		//Check if login user and password matching 	
		public Balance searchAccount(String LogUsername, String LogPassword) {
			itr = balanceList.iterator();
			while (itr.hasNext()) {
				Balance b = new Balance(itr.next());
				if (Objects.equals(LogUsername, b.getUsername()) && Objects.equals(LogPassword, b.getPassword()))
				{
					login = true;
					return b;
				}
			}
			return null;
		}	
		//search for matching account number
		public Balance searchAccount(long accountNum) {
			itr = balanceList.iterator();
			while (itr.hasNext()) {
				Balance b = new Balance(itr.next());
				if (b.getAccountNum() == accountNum)
					return b;
			}
			return null;
		}
				
	
			public long getBalanceListSize() {
				return balanceList.size();
			}
	}

	// Balance class

	public class Balance {
		private String name;
		private String city;
		private String username;
		private String password;
		private double balance;
		private Date date;
		private long accountNum;
		Scanner sc = new Scanner(System.in);

		public Balance(long aNo, String nam, String cit, String use, String pas, double money, Date aDate)
				throws ClassNotFoundException, IOException {
			accountNum = aNo;
			name = nam;
			city = cit;			
			username = use;
			password = pas;
			balance = money;
			date = (Date) aDate.clone();

			sendMessage("\nNew account created for : " + name + "\nCity : " + city + "\nusername : " + username
					+ "\nWith account no :" + accountNum + "\nOpening balance : " + balance + "\nAccount created on: "
					+ date.toString() + "\nEnter any letter to continue.");
			message = (String) in.readObject();
			dummy = new String(message);
		}

		public Balance(Balance b) {
			name = b.name;
			city = b.city;
			username = b.username;
			password = b.password;
			balance = b.balance;
			date = b.date;
			accountNum = b.accountNum;
		}

		public Balance() {

		}

		public long getAccountNum() {
			return accountNum;
		}

		public void setAccountNum(long accountNum) {
			this.accountNum = accountNum;
		}

		public double getBalance() {
			return balance;
		}

		public void setBalance(double balance) {
			this.balance = balance;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public Scanner getSc() {
			return sc;
		}

		public void setSc(Scanner sc) {
			this.sc = sc;
		}

		public String toString() {
			return "A/C no.: " + accountNum + "\nCurrent balance: " + balance + "\nLast date of update: " + date
					+ "\nEnter any letter to continue.";

		}

	}

	// Transaction class

	public class Transaction {

		private String transactionType;
		private String name;
		private String city;
		private String username;
		private String password;
		private String message;
		private double amount;
		private long accountNum;
		private Date date;
		private Bank b;
		private Balance balance = new Balance();
		
		private static final String FILENAME = "log.txt";
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		Scanner sc = new Scanner(System.in);
		
		public Transaction() {
			b = new Bank();
		}

		public String transaction(long accountNum, String transactionType, String name, String city, String username,
				String password, double amount) throws ClassNotFoundException, IOException {
			this.accountNum = accountNum;
			this.transactionType = transactionType;
			this.name = name;
			this.city = city;
			this.username = username;
			this.password = password;
			this.amount = amount;
			date = new Date();
			operation();
			return message;
		}

		private void operation() throws ClassNotFoundException, IOException {
			
		try {
			
			String data;

			File file = new File(FILENAME);

			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			// true = append file
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			
			if (transactionType.equalsIgnoreCase("Opening")) {
				// 	credit	limit	of	ˆ1000
				if (amount < -1000) {
					System.out.println("Opening balance cannot be less than zero.");
					return;
				}

				balance = new Balance(b.getBalanceListSize() + 1, name, city, username, password, amount, date);
				b.newAccount(balance);
			}		
			// Withdraw
			else if (transactionType.equalsIgnoreCase("withdraw")) {
				balance = b.searchAccount(accountNum);
				if (balance == null) {
					sendMessage("Account not found. \nEnter any letter to continue. ");
					message = (String) in.readObject();
					dummy = new String(message);
					return;
				}
				if (balance.getBalance() < amount) {
					sendMessage("Insufficient Balance.\nEnter any letter to continue.");
					message = (String) in.readObject();
					dummy = new String(message);
					return;
				}
				sendMessage("Balance before transaction:" + balance.toString());
				message = (String) in.readObject();
				dummy = new String(message);
				balance.setBalance(balance.getBalance() - amount);
				sendMessage("Balance after transaction:\n" + balance.toString());
				//set string data to write to log file
				data = ("\nBalance after transaction:\n" + balance.toString());
				message = (String) in.readObject();
				dummy = new String(message);
				//writes to log file
				bw.write(data);
			}
			
			// Deposit
			else if (transactionType.equalsIgnoreCase("deposit")) {
				balance = b.searchAccount(accountNum);
				if (balance == null) {
					sendMessage("Account not found \nEnter any letter to continue.");
					message = (String) in.readObject();
					dummy = new String(message);
					return;
				}
				sendMessage("Balance before transaction:" + balance.toString());
				message = (String) in.readObject();
				dummy = new String(message);
				balance.setBalance(balance.getBalance() + amount);				
				sendMessage("Balance after transaction:\n" + balance.toString());
				//set string data to write to log file
				data = ("\nBalance after transaction:\n" + balance.toString());
				message = (String) in.readObject();
				dummy = new String(message);
				//writes to log file
				bw.write(data);
			}
		
			else if (transactionType.equalsIgnoreCase("showInfo")) {
				balance = b.searchAccount(accountNum);
				if (balance == null) {
					sendMessage("Account not found \nEnter any letter to continue.");
					message = (String) in.readObject();
					dummy = new String(message);
					return;
				}
				sendMessage(balance.toString());
				message = (String) in.readObject();
				dummy = new String(message);
			}
			
			else if (transactionType.equalsIgnoreCase("logining")) {
				balance = b.searchAccount(username, password);
				if (balance == null) {
					sendMessage("Account not found \nEnter any letter to continue.");
					message = (String) in.readObject();
					dummy = new String(message);
					return;
				}
				sendMessage(balance.toString());
				message = (String) in.readObject();
				dummy = new String(message);
			}			

			else {
				sendMessage("Invalid option" + "\nEnter any letter to continue.");
				message = (String) in.readObject();
				dummy = new String(message);
				return;
			}
			
		//Catch for file writing	
		} catch (IOException e) {

			e.printStackTrace();

			} finally {
	
				try {
					//close file
					if (bw != null)
						bw.close();
	
					if (fw != null)
						fw.close();
	
				} catch (IOException ex) {
	
					ex.printStackTrace();
	
				}
			}			
		}
	}
}

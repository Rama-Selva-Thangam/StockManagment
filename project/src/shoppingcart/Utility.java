package shoppingcart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class Utility {

	private static HashMap<String, ShoppingUser> usersList = new HashMap<String, ShoppingUser>();
	private static LinkedHashMap<String, String> productList = new LinkedHashMap<String, String>();
	private static HashMap<Long, Card> cardList = new HashMap<Long, Card>();
	private static HashMap<Long, Boolean> overallTransactions = new HashMap<Long, Boolean>();

	public static HashMap<String, ShoppingUser> getUserList() {
		return usersList;
	}

	public static void addNewUser(String mail, ShoppingUser Newuser) {
		usersList.put(mail, Newuser);
	}

	public static LinkedHashMap<String, String> getProducts() {
		return productList;
	}

	public static void addProducts(String code, String productDetails) {
		productList.put(code, productDetails);
	}

	public static HashMap<Long, Card> getCardList() {
		return cardList;
	}

//	public static void setCardList(HashMap<Long, Card> cardList) {
//		Utility.cardList = cardList;
//	}
	public static void addCardDetails(long cardNumber, Card card) {
		cardList.put(cardNumber, card);
	}

	public static HashMap<Long, Boolean> getOverallTransactions() {
		return overallTransactions;
	}

//	public static void setOverallTransactions(HashMap<String, Boolean> overallTransactions) {
//		Utility.overallTransactions = overallTransactions;
//	}

	public static void addTransactionStatus(Long transactionId, boolean status) {
		overallTransactions.put(transactionId, status);
	}

	public static boolean isExistUser(String mail) throws SignInException {
		if (usersList.containsKey(mail))
			return true;
		throw new SignInException("User Not Found");
	}

	public static boolean verifyPassword(ShoppingUser user, String password) throws SignInException {
		if (user.getPassword().equals(password))
			return true;
		throw new SignInException("Invalid Password");
	}

	public static boolean isNewUser(String mail) throws SignUpException {
		if (!usersList.containsKey(mail))
			return true;
		throw new SignUpException("User already Exist");
	}

	public static boolean isValidMail(String newMail) throws SignUpException {
		String mailregex = "^[\\w.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		if (newMail.matches(mailregex))
			return true;
		throw new SignUpException("Invalid Email Format");
	}

	public static boolean isValidPassword(String password) throws SignUpException {
		String passwordregex = "^(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=.{8,})\\S+$";
		if (password.matches(passwordregex))
			return true;
		throw new SignUpException("Password is weak");
	}

	public static boolean isPasswordMatched(String newPassword, String cofirmPassword) throws SignUpException {
		if (newPassword.equals(cofirmPassword))
			return true;
		throw new SignUpException("Password Mismatch");
	}

	public static boolean isValidMobile(String mobileNumber) throws SignUpException {
		String mobileRegex = "^(?:\\+91|91)?[6789]\\d{9}$";
		if (mobileNumber.matches(mobileRegex))
			return true;
		throw new SignUpException("Invalid Mobile Number");
	}

	public static boolean isValidUserName(String userName) throws SignUpException {
		String usernameRegex = "^[a-zA-Z]{4,}$";
		if (userName.matches(usernameRegex))
			return true;
		throw new SignUpException("Invalid UserName");
	}

	public static int checkStocks(Ordered ordered) {
		int status = 1;
		String stockChecked = Utility.getInsufficientStocks(ordered.getItempurchased());
		double originalPrice = ordered.getAmount();
		double reducePrice = 0;

		if (!stockChecked.isEmpty()) {
			String[] productCodes = stockChecked.split(",");
			Iterator<String> iterator = ordered.getItempurchased().iterator();

			while (iterator.hasNext()) {
				status = 2;
				String product = iterator.next();
				String[] productInfo = product.split("   ");

				if (Arrays.asList(productCodes).contains(product)) {
					System.out.println(productInfo[1] + " is out of stock");
					System.out.println("\nDo you want to ignore the product and order the remaining ? Press 1");
					System.out.println("Do you want to order it later ? Press any other key");

					int choice = ShoppingMain.getIntegerInput();

					if (choice == 1) {
						reducePrice += Double.parseDouble(productInfo[4]);
						iterator.remove();
						status = 1;
					}
				}
			}

			ordered.setAmount(originalPrice - reducePrice);
			System.out.format("%-20s%-20s%-20s%-20s\n", "Product", "Quantity", "Price", "Total");
			System.out.println("---------------------------------------------------------------");
			for (String s : ordered.getItempurchased()) {
				String[] parts = s.split("   ");
				System.out.format("%-20s%-20s%-20s%-20s\n", parts[1], parts[2], parts[3], parts[4]);
			}
			System.out.println("---------------------------------------------------------------");
			System.out.format("%-40s%-20.2f\n", "Total : ", ordered.getAmount());
			System.out.format("%-40s%-20.2f\n", "Discounted Price : ", ordered.getAmount());
			System.out.println("---------------------------------------------------------------");

		}

		return status;
	}

	public static String getInsufficientStocks(ArrayList<String> purchased) {
		StringBuilder insufficientStocks = new StringBuilder();
		for (String purchase : purchased) {
			String[] parts = purchase.split("   ");
			String productCode = parts[0];
			int quantity = Integer.parseInt(parts[2]);

			if (productList.containsKey(productCode)) {
				String productInfo = productList.get(productCode);
				String[] productParts = productInfo.split(" - ");
				int stock = Integer.parseInt(productParts[2]);

				if (quantity > stock) {
					insufficientStocks.append(purchase).append(",");
				}
			} else {
				insufficientStocks.append(purchase).append(",");
			}
		}

		if (insufficientStocks.length() > 0) {
			insufficientStocks.setLength(insufficientStocks.length() - 1);
		}

		return insufficientStocks.toString();
	}

	public static void changeStocks(Ordered ordered) {
		ArrayList<String> orderedItems = ordered.getItempurchased();
		for (String items : orderedItems) {
			String[] itemParts = items.split("   ");
			String code = itemParts[0];
			int quantity = Integer.parseInt(itemParts[2]);
			if (Utility.productList.containsKey(code)) {
				String productInfo = Utility.productList.get(code);
				String[] productParts = productInfo.split(" - ");
				int stocks = Integer.parseInt(productParts[2]);
				int reducedStock = stocks - quantity;
				String updated = productParts[0] + " - " + productParts[1] + " - " + reducedStock;
				Utility.productList.replace(code, updated);
			}
		}
	}

}

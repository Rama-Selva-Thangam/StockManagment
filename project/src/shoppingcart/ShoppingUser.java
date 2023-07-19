
package shoppingcart;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class ShoppingUser {
	private String mail;
	private String password;
	private String userName;
	private String mobileNumber;
	private String address;
	private Ordered temporaryorder;
	private HashMap<Long, Ordered> orderList = new HashMap<Long, Ordered>();
	private HashMap<Long, Ordered> cartList = new HashMap<Long, Ordered>();
	private HashMap<Long, Boolean> transactionList = new HashMap<Long, Boolean>();

	public ShoppingUser(String mail, String password, String userName, String mobileNumber, String address) {
		this.mail = mail;
		this.password = password;
		this.userName = userName;
		this.mobileNumber = mobileNumber;
		this.address = address;

	}

	public String getMail() {
		return mail;
	}

//	public void setMail(String mail) {
//		this.mail = mail;
//	}

	public String getPassword() {
		return password;
	}

//	public void setPassword(String password) {
//		this.password = password;
//	}

	public String getUserName() {
		return userName;
	}

//	public void setUserName(String userName) {
//		this.userName = userName;
//	}

	public String getMobileNumber() {
		return mobileNumber;
	}

//	public void setMobileNumber(String mobileNumber) {
//		this.mobileNumber = mobileNumber;
//	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public HashMap<Long, Ordered> getOrderList() {
		return orderList;
	}

	public void setOrderList(HashMap<Long, Ordered> orderList) {
		this.orderList = orderList;
	}

	public HashMap<Long, Ordered> getCartList() {
		return cartList;
	}

	public void setCartList(HashMap<Long, Ordered> cartList) {
		this.cartList = cartList;
	}

	public HashMap<Long, Boolean> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(HashMap<Long, Boolean> transactionList) {
		this.transactionList = transactionList;
	}

	public int takeOrder() {
		Products products = new Products();
		ArrayList<String> productsChoosed = products.chooseDress();
		if (!productsChoosed.isEmpty()) {
			double amount = products.getAmount();
			long orderedId = 0;
			long transactionId =0;
			Ordered order = new Ordered(amount, orderedId, transactionId, productsChoosed);
			showPurchasedList(order);
			this.temporaryorder = order;
			return 1;
		} else
			return 2;
	}

	public void showPurchasedList(Ordered ordered) {
		if (!ordered.getItempurchased().isEmpty()) {
			System.out.format("%-20s%-20s%-20s%-20s\n", "Product", "Quantity", "Price", "Total");
			System.out.println("---------------------------------------------------------------");
			for (String s : ordered.getItempurchased()) {
				String[] parts = s.split("   ");
				System.out.format("%-20s%-20s%-20s%-20s\n", parts[1], parts[2], parts[3], parts[4]);
			}
			System.out.println("---------------------------------------------------------------");
			System.out.format("%-40s%-20.2f\n", "Grand Total :", ordered.getAmount());
			System.out.println("---------------------------------------------------------------");
		}
	}

	public int initiateTransaction() {
		int sufficientStocks = Utility.checkStocks(temporaryorder);
		int statuscode = 0;
		if (sufficientStocks == 1) {
			Payment payment = new Payment(temporaryorder);
			statuscode = payment.getPaymentstatus();
			if (statuscode == 10) {
				orderList.put(temporaryorder.getOrderId(), temporaryorder);
				transactionList.put(temporaryorder.getTransactionId(), true);
				Utility.addTransactionStatus(temporaryorder.getTransactionId(), false);
				Utility.changeStocks(temporaryorder);

			} else if (statuscode == 11 || statuscode == 20 || statuscode == 21) {
				cartList.put(temporaryorder.getOrderId(), temporaryorder);
				transactionList.put(temporaryorder.getTransactionId(), false);
				Utility.addTransactionStatus(temporaryorder.getTransactionId(), false);
			}
		} else if (sufficientStocks == 2) {
			statuscode = 22;
		}

		return statuscode;
	}

	public void addOrderToCart() {
		cartList.put(temporaryorder.getOrderId(), temporaryorder);
	}

	public int cartToOrder() {
		if (!cartList.isEmpty()) {
			System.out.print("Enter the Order ID : ");
			String orderIdstr = ShoppingMain.getStringInput();
			long orderId=Long.parseLong(orderIdstr);
			if (cartList.containsKey(orderId)) {
				this.temporaryorder = cartList.get(orderId);
				int status = initiateTransaction();
				if (status == 10) {
					cartList.remove(orderId);
				}
				return status;

			} else {
				return 30;
			}
		}
		return 0;

	}

	public void showOrderList() {

		System.out.println("++++++++++++++++ YOUR ORDERED LIST ++++++++++++++++");
		if (!orderList.isEmpty()) {
			int i = 1;
			for (Map.Entry<Long, Ordered> entry : orderList.entrySet()) {

				Ordered value = entry.getValue();
				System.out.println(i + " . " + "Ordered on     : " + value.getDate());
				System.out.println("    Order Id       : " + value.getOrderId());
				System.out.println("    Transaction Id : " + value.getTransactionId());
				ArrayList<String> pur = value.getItempurchased();
				System.out
						.println("-----------------------------------------------------------------------------------");
				System.out.printf("%-20s%-20s%-20s%-20s%-20s\n", "Product Code", "Product", "Quantity", "Price",
						"Total");
				System.out.println(
						"------------------------------------------------------------------------------------");
				for (String s : pur) {
					String[] parts = s.split("   ");
					System.out.printf("%-20s%-20s%-20s%-20s%-20s\n", parts[0], parts[1], parts[2], parts[3], parts[4]);
				}
				System.out
						.println("----------------------------------------------------------------------------------");
				System.out.printf("%-40s%-20.2f\n", "Total Amount:", value.getAmount());
				System.out.println("----------------------------------------------------------------------------");
				System.out.println("Delivery Address:\n" + address);
				System.out.println("***************************************************");
				i++;
			}
		} else {
			System.out.println("Your Order List is Empty");
		}

	}

	public void showCartList() {
		System.out.println("++++++++++++++++ YOUR CART LIST ++++++++++++++++");
		if (!cartList.isEmpty()) {
			int i = 1;
			for (Map.Entry<Long, Ordered> entry : cartList.entrySet()) {

				Ordered value = entry.getValue();
				System.out.println(i + " . " + "Ordered on     : " + value.getDate());
				System.out.println("    Order Id       : " + value.getOrderId());
				System.out.println("    Transaction Id : " + value.getTransactionId());
				ArrayList<String> pur = value.getItempurchased();
				System.out
						.println("-----------------------------------------------------------------------------------");
				System.out.printf("%-20s%-20s%-20s%-20s%-20s\n", "Product Code", "Product", "Quantity", "Price",
						"Total");
				System.out.println(
						"------------------------------------------------------------------------------------");
				for (String s : pur) {
					String[] parts = s.split("   ");
					System.out.printf("%-20s%-20s%-20s%-20s%-20s\n", parts[0], parts[1], parts[2], parts[3], parts[4]);
				}
				System.out.println("---------------------------------------------------");
				System.out.printf("%-40s%-20.2f\n", "Total Amount:", value.getAmount());
				System.out.println("---------------------------------------------------");
				System.out.println("Delivery Address:\n" + address);
				System.out.println("***************************************************");
				i++;
			}
		} else {
			System.out.println("Your Cart list is Empty");
		}

	}

	public void showTransactionList() {
		System.out.println("++++++++++++++++ YOUR TRANSACTION LIST ++++++++++++++++");
		if (!transactionList.isEmpty()) {
			for (Map.Entry<Long, Boolean> entry : transactionList.entrySet()) {
				Long key = entry.getKey();
				Boolean value = entry.getValue();
				System.out.print("TransactionId : " + key);
				System.out.println("\t\tTransaction Status : " + value);
			}

		} else {
			System.out.println("Your Transaction list is Empty");
		}
	}

}

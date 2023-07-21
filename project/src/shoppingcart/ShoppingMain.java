package shoppingcart;

import java.util.Scanner;

public class ShoppingMain {

	public static final byte SIGN_IN = 1;
	public static final byte SIGN_UP = 2;
	public static final byte EXIT = 3;
	public static final byte TAKE_ORDER = 1;
	public static final byte SHOW_ORDER_LIST = 2;
	public static final byte SHOW_CART_LIST = 3;
	public static final byte SHOW_TRANSACTION_LIST = 4;
	public static final byte ORDER_FROM_CART = 5;
	public static final byte LOGOUT = 6;
	public static final byte CONFIRM_ORDER = 1;
	public static final byte ADD_TO_CART = 2;
	public static final byte CANCEL_ORDER = 3;
	public static final byte TRANSACTION_SUCCESSED = 10;
	public static final byte INSUFFICIENT_BALANCE = 11;
	public static final byte INVALID_CARD_DETAILS = 20;
	public static final byte CARD_NOT_FOUND = 21;
	public static final byte INSUFFICIENT_STOCKS = 22;
	public static final byte ORDERID_NOT_FOUND = 30;
	public static final byte EMPTY_CART = 31;

	public static void main(String[] args) {
		ShoppingMain main = new ShoppingMain();
		Scanner sc = new Scanner(System.in);
		loadDefaultObjects();

		System.out.println("----------------- SHOPPING CART -----------------");
		boolean signInloop = true;
		do {
			System.out.println("1.Sign In\n2.Sign Up\n3.Exit");
			System.out.print("Enter the Option : ");
			byte option = getByteInput();
			System.out.println();
			switch (option) {
			case SIGN_IN:
				System.out.println("++++++++++++++++ SignIn ++++++++++++++++");
				try {
					main.signIn();
				} catch (SignInException s) {
					s.printStackTrace();
					System.out.println("Please Try again");
				}
				System.out.println("++++++++++++++++++++++++++++++++++++++++++");
				break;
			case SIGN_UP:
				System.out.println("++++++++++++++++ SignUp ++++++++++++++++");
				try {
					main.signUp();
				} catch (SignUpException e) {
					e.printStackTrace();
					System.out.println("Please Try again");
				}
				System.out.println("++++++++++++++++++++++++++++++++++++++++++");
				break;

			case EXIT:
				System.out.println("++++++++++++++++++++++++++++++++++++++++++");
				signInloop = false;
				break;
			default:
				System.out.println("Enter Valid Key");
			}

		} while (signInloop);
		System.out.println("\n\t\t-------------- Thanks For Using Shopping Cart --------------");
		sc.close();
	}

	public static int getIntegerInput() {
		Scanner sc = new Scanner(System.in);
		int num = 0;
		boolean flag = true;
		while (flag) {
			try {
				num = sc.nextInt();
				flag = false;
			} catch (Exception e) {
				System.out.println("Enter Valid Number");
				sc.nextLine();
			}
		}

		return num;
	}

	public static byte getByteInput() {
		Scanner sc = new Scanner(System.in);
		byte num = 0;
		boolean flag = true;
		while (flag) {
			try {
				num = sc.nextByte();
				flag = false;
			} catch (Exception e) {
				System.out.println("Enter Valid Number");
				sc.nextLine();
			}
		}
		return num;
	}

	public static String getStringInput() {
		Scanner scanner = new Scanner(System.in);
		return scanner.nextLine().trim();
	}

	public void signIn() throws SignInException {
		ShoppingUser user = null;
		System.out.print("Enter Your Mail : ");
		String mail = getStringInput().toLowerCase();
		if (Utility.isExistUser(mail)) {
			user = Utility.getUserList().get(mail);
			System.out.print("Enter the Password : ");
			String password = getStringInput();
			if (Utility.verifyPassword(user, password)) {
				System.out.println("you are Logged In");
				userHomePage(user);
			}
		}
	}

	public void signUp() throws SignUpException {
		System.out.print("Enter the Email : ");
		String newMail = getStringInput().toLowerCase();
		if (Utility.isNewUser(newMail)) {
			if (Utility.isValidMail(newMail)) {
				System.out.print("Enter the Password : ");
				String newPassword = getStringInput();
				if (Utility.isValidPassword(newPassword)) {
					System.out.print("Confirm Your Password : ");
					String confirmPassword = getStringInput();
					if (Utility.isPasswordMatched(newPassword, confirmPassword)) {
						System.out.print("Enter Your Mobile Number : ");
						String mobileNumber = getStringInput();
						if (Utility.isValidMobile(mobileNumber)) {
							System.out.print("Enter User Name : ");
							String userName = getStringInput().trim();
							if (Utility.isValidUserName(userName)) {
								System.out.print("Enter your Door Number : ");
								String door = getStringInput();
								System.out.print("Enter your Street Number : ");
								String street = getStringInput();
								System.out.print("Enter your Village Name : ");
								String village = getStringInput();
								System.out.print("Enter your PIN Code : ");
								String pincode = getStringInput();

								ShoppingUser newUser = new ShoppingUser(newMail, confirmPassword, userName,
										mobileNumber, (door + "," + street + "," + village + "," + pincode));
								Utility.addNewUser(newMail, newUser);
								System.out.println("You are Registered Successfully\nPlease Sign In to Continue");
							}
						}
					}
				}
			}
		}
	}

	public void userHomePage(ShoppingUser user) {
		boolean userLoop = true;
		do {
			System.out.println("+++++++++++++++++ Welcome " + user.getUserName().toUpperCase() + " +++++++++++++++++");
			System.out.print(
					"1.Order\n2.Show Order List\n3.Show Cart\n4.Show Transaction List\n5.Order from cart\n6.Log Out\nEnter an Option : ");
			byte useroption = getByteInput();
			switch (useroption) {
			case TAKE_ORDER:
				int orderStatus = user.takeOrder();
				if (orderStatus == 1) {
					System.out.print("Enter 1 For confirm Order\nEnter 2 for Add to Cart\nPress 3 for Cancel Order : ");
					int placeorder = getByteInput();
					if (placeorder == CONFIRM_ORDER) {
						int transfer = user.initiateTransaction();
						if (transfer == TRANSACTION_SUCCESSED) {
							System.out.println("Your Order Hasbeen Placed");
						} else if (transfer == INSUFFICIENT_BALANCE) {
							System.out.println("Your Order hasbeen moved to cart due to insufficient Balance");
						} else if (transfer == INVALID_CARD_DETAILS) {
							System.out.println("Your Order hasbeen moved to cart due to invalid card details");
						} else if (transfer == CARD_NOT_FOUND) {
							System.out.println("Your Order hasbeen moved to cart due to card not found");
						} else if (transfer == INSUFFICIENT_STOCKS) {
							System.out.println("Your Order hasbeen moved to cart due to Insuffcient Stock");
						}
					} else if (placeorder == ADD_TO_CART) {
						user.addOrderToCart();
						System.out.println("Your order moved to cart");
					} else if (placeorder == CANCEL_ORDER) {
						System.out.println("Your Order Cancelled");
					} else {
						System.out.println("Invalid Key your order moved to cart.");
						user.addOrderToCart();
					}
				} else {
					System.out.println("Sorry You ordered Nothing");
				}

				break;
			case SHOW_ORDER_LIST:
				user.showOrderList();
				break;
			case SHOW_CART_LIST:
				user.showCartList();
				break;
			case SHOW_TRANSACTION_LIST:
				user.showTransactionList();
				break;
			case ORDER_FROM_CART:
				user.showCartList();
				int status = user.cartToOrder();
				if (status == TRANSACTION_SUCCESSED) {
					System.out.println("Your Order Hasbeen Placed");
				} else if (status == INSUFFICIENT_BALANCE) {
					System.out.println("Your Order not placed due to insufficient Balance");
				} else if (status == INVALID_CARD_DETAILS) {
					System.out.println("Your Order not placed due to invalid card details");
				} else if (status == CARD_NOT_FOUND) {
					System.out.println("Your Order not placed due to card not found");
				} else if (status == INSUFFICIENT_STOCKS) {
					System.out.println("Your Order hasbeen moved to cart due to Insuffcient Stock");
				} else if (status == ORDERID_NOT_FOUND) {
					System.out.println("OrderID not Found");
				} else if (status == EMPTY_CART) {
					System.out.println("Your Cart is Empty");
				}
				break;
			case LOGOUT:
				userLoop = false;
				break;
			default:
				System.out.println("Enter valid option");
			}

		} while (userLoop);

	}

	public static void loadDefaultObjects() {
		Products.populateProducts();
		Utility.addNewUser("ramaselvathangam1999@gmail.com", new ShoppingUser("ramaselvathangam1999@gmail.com",
				"1#Thangam", "Rama selva thangam", "8870627424", "2/144 A,Thiruvalluvar Street,Kidarakulam-627854"));

		Utility.addCardDetails(98765432109L, new Card("Thangam", 98765432109L, 8870627424L, 663, "04/27", 6000));
		Utility.addCardDetails(87654321098L, new Card("Ram Kumar", 87654321098L, 7548829151L, 877, "03/28", 8000));
		Utility.addCardDetails(76543210987L, new Card("Prabakar", 76543210987L, 8220516727L, 998, "02/25", 500000));

	}

}

package shoppingcart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class Products {
	public static int count = 1;

	private double totalAmount;
	private static String[] colors = { "Red", "Blue", "White" };
	private static ArrayList<String> menWear = new ArrayList<String>(
			Arrays.asList("T-Shirt", "Formal Shirt", "Casual Shirt"));
	private static ArrayList<Integer> menWearamt = new ArrayList<Integer>(Arrays.asList(350, 400, 450));
	private static ArrayList<Integer> menStocks = new ArrayList<Integer>(Arrays.asList(5, 8, 10));
	private static ArrayList<String> womenWear = new ArrayList<String>(
			Arrays.asList("Silk Saree", "Cotton Saree", "Tops"));
	private static ArrayList<Integer> womenWearamt = new ArrayList<Integer>(Arrays.asList(350, 400, 450));
	private static ArrayList<Integer> womenStocks = new ArrayList<Integer>(Arrays.asList(10, 40, 20));
	private static ArrayList<String> kidsWear = new ArrayList<String>(
			Arrays.asList("Baby Dress", "Boy Dress", "Girl Dress"));
	private static ArrayList<Integer> kidsWearamt = new ArrayList<Integer>(Arrays.asList(300, 450, 500));
	private static ArrayList<Integer> kidsStocks = new ArrayList<Integer>(Arrays.asList(12, 10, 10));

	private ArrayList<String> purchased = new ArrayList<String>();

	Products() {
		showMen();
		showWomen();
		showKids();
	}

	public double getAmount() {
		return this.totalAmount;
	}

	public void setAmount(double amount) {
		this.totalAmount = amount;
	}

	public void showMen() {
		System.out.println("--------------------------------------------------------");
		System.out.println("\t\t    MEN WEAR ");
		System.out.println("--------------------------------------------------------");
		System.out.printf("%-15s%-30s%-20s%s\n", "Product Code", "Product", "Amount", "Stocks");
		System.out.println("--------------------------------------------------------");
		for (Map.Entry<String, String> entry : Utility.getProducts().entrySet()) {
			String code = entry.getKey();
			if (code.startsWith("100")) {
				String productInfo = entry.getValue();
				String[] parts = productInfo.split(" - ");
				System.out.printf("%-15s%-30s%-20s%s\n", code, parts[0], parts[1], parts[2]);
			}
		}
		System.out.println("--------------------------------------------------------");
	}

	public void showWomen() {
		System.out.println("--------------------------------------------------------");
		System.out.println("\t\t    WOMEN WEAR ");
		System.out.println("--------------------------------------------------------");
		System.out.printf("%-15s%-30s%-20s%s\n", "Product Code", "Product", "Amount", "Stocks");
		System.out.println("--------------------------------------------------------");
		for (Map.Entry<String, String> entry : Utility.getProducts().entrySet()) {
			String code = entry.getKey();
			if (code.startsWith("200")) {
				String productInfo = entry.getValue();
				String[] parts = productInfo.split(" - ");
				System.out.printf("%-15s%-30s%-20s%s\n", code, parts[0], parts[1], parts[2]);
			}
		}
		System.out.println("--------------------------------------------------------");
	}

	public void showKids() {
		System.out.println("--------------------------------------------------------");
		System.out.println("\t\t    KIDS WEAR ");
		System.out.println("--------------------------------------------------------");
		System.out.printf("%-15s%-30s%-20s%s\n", "Product Code", "Product", "Amount", "Stocks");
		System.out.println("--------------------------------------------------------");
		for (Map.Entry<String, String> entry : Utility.getProducts().entrySet()) {
			String code = entry.getKey();
			if (code.startsWith("300")) {
				String productInfo = entry.getValue();
				String[] parts = productInfo.split(" - ");
				System.out.printf("%-15s%-30s%-20s%s\n", code, parts[0], parts[1], parts[2]);
			}
		}
		System.out.println("--------------------------------------------------------");
	}

	public static void populateProducts() {
		int productCount = 1;
		for (int i = 0; i < menWear.size(); i++) {
			for (int j = 0; j < colors.length; j++) {
				String code = "100" + String.format("%01d", productCount++);
				String product = menWear.get(i) + " " + colors[j];
				String amount = String.valueOf(menWearamt.get(i));
				int stocks = menStocks.get(i);
				Utility.addProducts(code, product + " - " + amount + " - " + stocks);
			}
		}
		productCount = 1;
		for (int i = 0; i < womenWear.size(); i++) {
			for (int j = 0; j < colors.length; j++) {
				String code = "200" + String.format("%01d", productCount++);
				String product = womenWear.get(i) + " " + colors[j];
				String amount = String.valueOf(womenWearamt.get(i));
				int stocks = womenStocks.get(i);
				Utility.addProducts(code, product + " - " + amount + " - " + stocks);
			}
		}
		productCount = 1;
		for (int i = 0; i < kidsWear.size(); i++) {
			for (int j = 0; j < colors.length; j++) {
				String code = "300" + String.format("%01d", productCount++);
				String product = kidsWear.get(i) + " " + colors[j];
				String amount = String.valueOf(kidsWearamt.get(i));
				int stocks = kidsStocks.get(i);
				Utility.addProducts(code, product + " - " + amount + " - " + stocks);
			}
		}

	}

	public ArrayList<String> chooseDress() {
		HashMap<String, String> tempproducts = new HashMap<>(Utility.getProducts());
		boolean isDone = false;
		while (!isDone) {
			System.out.print("Enter the product code to choose the dress\n(or '-1' to finish): ");
			String code = ShoppingMain.getStringInput();
			if (code.equalsIgnoreCase("-1")) {
				isDone = true;
			} else if (tempproducts.containsKey(code)) {
				String dress = tempproducts.get(code);
				String[] parts = dress.split(" - ");
				double amount = Double.parseDouble(parts[1]);
				int stock = Integer.parseInt(parts[2]);

				if (stock == 0) {
					System.out.println("Sorry, the product is out of stock.");
					continue; // Skip further processing and ask for the next product
				}

				System.out.println("Chosen " + parts[0] + " Available Quantity: " + stock);

				System.out.print("Enter the Quantity: ");
				int quantity = ShoppingMain.getIntegerInput();
				boolean availableStock = true;
				while (availableStock) {
					if (quantity <= stock) {
						int reduced = stock - quantity;
						tempproducts.replace(code, parts[0] + " - " + parts[1] + " - " + reduced);
						availableStock = false;
					} else if (quantity == -1) {
						availableStock = false;
					} else {
						System.out.print("Enter a lower Quantity or press -1 to ignore: ");
						quantity = ShoppingMain.getIntegerInput();
					}
				}

				if (quantity > 0) {
					double total = amount * quantity;
					totalAmount += total;
					String purchase = code + "   " + parts[0] + "   " + quantity + "   " + amount + "   " + total;
					System.out.println("---------------------------------------------");
					System.out.println("| " + purchase + " |");
					System.out.println("---------------------------------------------");
					purchased.add(purchase);
				}
			} else {
				System.err.println("Invalid product code.");
			}
		}

		return purchased;

	}

}

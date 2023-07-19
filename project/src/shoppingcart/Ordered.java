package shoppingcart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Ordered {
	private static int orderCount=1;
	private long orderId;
	private long transactionId;
	private double totalAmount;
	private Date date = new Date();
	private ArrayList<String> itempurchased = new ArrayList<String>();

	Ordered(double amount, long orderId, long transactionId, ArrayList<String> itempurchased) {
		this.totalAmount =amount;
		this.orderId = orderId;
		this.transactionId = transactionId;
		this.itempurchased = itempurchased;
		generateOrderId();
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public String getDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		String dateString = dateFormat.format(date);
		String timeString = timeFormat.format(date);
		return dateString + " ON " + timeString;
	}


	public void setDate(Date date) {
		this.date = date;
	}

	public double getAmount() {
		return totalAmount;
	}

	public void setAmount(double amount) {
		this.totalAmount = amount;
	}

	public ArrayList<String> getItempurchased() {
		return itempurchased;
	}

	public void setItempurchased(ArrayList<String> itempurchased) {
		this.itempurchased = itempurchased;
	}

	public void generateOrderId() {
		Date currentDate=new Date();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMYY");
	    String dateString = dateFormat.format(currentDate);
	    if (!dateString.equals(dateFormat.format(date))) {
	        orderCount = 1;
	    }
	    String orderNumberString = String.format("%03d", orderCount++);
	    String orderIdString = dateString + orderNumberString;
	    this.orderId = Long.parseLong(orderIdString);
	}
}

package shoppingcart;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Payment {
	private static int transactionCount=1;
	private Ordered order;
	private Card card;
	private int paymentstatus;
	private Date date = new Date();

	Payment(Ordered order) {
		this.order = order;
		generateTransactionId();
		getTransaction();
	}

	public int getPaymentstatus() {
		return paymentstatus;
	}

	public void setPaymentstatus(int paymentstatus) {
		this.paymentstatus = paymentstatus;
	}

	private void getTransaction() {
		long cardNum = 0;
		int cvv = 0;
		System.out.print("Enter Card Number : ");
		String cardNumstr = ShoppingMain.getStringInput();
		System.out.print("Enter cvv : ");
		String cvvstr = ShoppingMain.getStringInput();
		System.out.print("Enter Expiry date as  MM/YY : ");
		String expirydate = ShoppingMain.getStringInput();
		try {
			cardNum = Long.parseLong(cardNumstr);
			cvv = Integer.parseInt(cvvstr);
			String match = "^(0[1-9]|1[0-2])\\/\\d{2}$";
			if (!expirydate.matches(match))
				throw new Exception();
		} catch (Exception e) {
			this.paymentstatus = 20;
		}
		this.card = Utility.getCardList().get(cardNum);
		if (this.card != null) {
			if (cardNum == card.getCardNumber() && (cvv == card.getCvv() && expirydate.equals(card.getExpirydate()))) {
				if (order.getAmount() <= card.getAmount()) {
					card.setAmount(card.getAmount() - order.getAmount());
					this.paymentstatus = 10;
				} else {
					this.paymentstatus = 11;
				}
			} else {
				this.paymentstatus = 20;
			}
		} else {
			this.paymentstatus = 30;
		}
	}

	private void generateTransactionId() {
		Date currentDate=new Date();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMYY");
	    String dateString = dateFormat.format(currentDate);
	    if (!dateString.equals(dateFormat.format(date))) {
	    	transactionCount = 1;
	    }
	    String transactionNumberString = String.format("%03d", transactionCount++);
	    String transactionIdString = dateString + transactionNumberString;
	    order.setTransactionId(Long.parseLong(transactionIdString));

	}

}

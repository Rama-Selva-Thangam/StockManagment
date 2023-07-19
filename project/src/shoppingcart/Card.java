package shoppingcart;

public class Card {
	private String holderName;
	private long cardNumber;
	private long mobileNumber;
	private int cvv;
	private String expirydate;
	private double balance;

	Card(String holderName, long cardNumber, long mobileNumber, int cvv, String expirydate, double balance) {
		this.holderName = holderName;
		this.cardNumber = cardNumber;
		this.mobileNumber = mobileNumber;
		this.cvv = cvv;
		this.expirydate = expirydate;
		this.balance = balance;
	}

	String getHolderName() {
		return holderName;
	}

//	void setHolderName(String holderName) {
//		this.holderName = holderName;
//	}

	long getCardNumber() {
		return cardNumber;
	}

//	void setCardNumber(long cardNumber) {
//		this.cardNumber = cardNumber;
//	}

	int getCvv() {
		return cvv;
	}

//	void setCvv(int cvv) {
//		this.cvv = cvv;
//	}

	String getExpirydate() {
		return expirydate;
	}

//	void setExpirydate(String expirydate) {
//		this.expirydate = expirydate;
//	}

	double getAmount() {
		return balance;
	}

	void setAmount(double amount) {
		this.balance = amount;
	}

	long getMobileNumber() {
		return mobileNumber;
	}

//	void setMobileNumber(long mobileNumber) {
//		this.mobileNumber = mobileNumber;
//	}

}

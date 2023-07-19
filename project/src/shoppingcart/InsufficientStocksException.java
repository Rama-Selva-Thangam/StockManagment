package shoppingcart;

public class InsufficientStocksException extends Exception {
	InsufficientStocksException(){
		System.out.println("Insufficient Stocks");
	}
}

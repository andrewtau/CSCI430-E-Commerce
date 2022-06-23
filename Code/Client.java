// CSCI 530
// Andrew Thao
// Project 1 - Client Class
import java.util.UUID; // Library to generate unique random id's for clients
import java.io.Serializable;
import java.util.ArrayList; // Library for Array List

public class Client implements Serializable
{
    
    private ArrayList<Product> cart;
    private double balance;
    private String ID;
    private ArrayList<Transaction> transactionList;
    private ArrayList<Product> waitlist;
    String waitListedProduct;

    // Constructor
    public Client()
    {
        cart = new ArrayList<Product>();
        balance = 0;
        ID = null;
        transactionList = new ArrayList<Transaction>();
        waitlist = new ArrayList<Product>();
    } // End Client Default Constructor

    // Constructor
    public Client(ArrayList<Product> shoppingCart, double customerBalance)
    {
        cart = shoppingCart;
        balance = customerBalance;
        ID = UUID.randomUUID().toString(); // No setter function since user ID is generated upon creation
        transactionList = new ArrayList<Transaction>();
        waitlist = new ArrayList<Product>();
    } // End Client Constructor

    public Client(double customerBalance)
    {
        cart = new ArrayList<>();
        balance = customerBalance;
        ID = UUID.randomUUID().toString(); // No setter function since user ID is generated upon creation
        transactionList = new ArrayList<Transaction>();
        waitlist = new ArrayList<Product>();
    } // End Client Constructor

    // Mutators
    // Setter function for cart
    void setCart(ArrayList<Product> shoppingCart)
    {
        this.cart = shoppingCart;
    } // End setCart method

    //Adder function for shopping card.
    void addToCart(Product p)
    {
        this.cart.add(p);
    } //End addToCart method

    // Getter function for shoppingCart
    ArrayList<Product> getCart()
    {
        return cart;
    } // End getCart method

    // Setter function for balance
    void setBalance(double customerBalance)
    {  
        balance = customerBalance;
    } // End setBalance method

    // Getter function for balance
    double getBalance()
    {   
        return balance;
    } // End getBalance method

    // Getter function for ID
    String getID()
    {
        return ID;
    } // End getID method

    // Adding transactions to the transaction list
    void addTransaction(Transaction t)
    {
        transactionList.add(t);
    }

    ArrayList<Transaction> getTransactionList()
    {
        return transactionList;
    }

    // displaying Client's Transactions
    void displayTransactions(Transaction t)
    {
        System.out.println(t);
    }

    // Adding products to the client's wait listed orders
    void addWaitlistedOrder(Product p)
    {
        waitlist.add(p);
    }

    // Unsure what to do with this method at the moment.
    String getWaitlistedOrders()
    {
        return waitListedProduct;
    }

    public ArrayList<Product> getWaitlist() 
    {
        return waitlist;
    }

    @Override
    public String toString()
    {
        return getClass().getName() + "[ID: " + ID + " Balance: " + balance + "]";
    } // End Overridden toString method

} // End Client Class
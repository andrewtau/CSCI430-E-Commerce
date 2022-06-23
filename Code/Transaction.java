// Andrew Thao
// CSCI 530 Project
import java.io.Serializable;
import java.util.ArrayList; // Library for Array List

public class Transaction extends Client implements Serializable
{
 
private ArrayList<Product> tTransactionList;
private double total;
    
// Contructor
public Transaction()
{
    tTransactionList = new ArrayList<Product>();
    total = 0;
}
// Mutators
// Setter function for Client's Transactions
void setTransaction(Client c)
{
    tTransactionList = c.getCart();
    total = c.getBalance();
}

void addProduct(Product p)
{
    tTransactionList.add(p);
}

void addToTotal(double i)
{
    total = total + i;
}

@Override
public String toString()
{
    return getClass().getName() + "[Transaction: " + tTransactionList + " Total: $" + total+ "]";
} // End Overridden toString method
}

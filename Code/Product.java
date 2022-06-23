// Justin Vang
// CSCI 430
// Project 1 - Product Class

import java.io.Serializable;
import java.util.ArrayList; //Not needed yet, but adding in case we need it later. 
import java.util.UUID;

public class Product implements Serializable
{
	private String id;
    public String name;
	private float price;
    private int quantity;
	ArrayList<Client> waitlistedOrders;



	// Constructor
	public Product()
	{
		//id = "0";
		id = UUID.randomUUID().toString();
		name = null;
		price = 0; //Important that this stays 0
		quantity = 0;
		waitlistedOrders = new ArrayList<Client>();
	}



	// Set Constructor
	public Product (String productName, float productPrice, int productQuantity)
	{
		//int min = 100000; // Minimum of product ID
		//int max = 999999; // Maximum of product ID
		//id = (int)(Math.random() * (max - min + 1) + min); // Generate random ID for product
		id = UUID.randomUUID().toString();
		name = productName;
		price = productPrice;
		quantity = productQuantity;
		waitlistedOrders = new ArrayList<Client>();
	} 



	// Functions for class
	// Set function for id
	void setID(String productID)
	{
		this.id = productID;
	}


	// Get functoin for id
	String getID()
	{
		return this.id;
	}


	// Set function for name
	void setName(String productName)
	{
		this.name = productName;
	}


	// Get function for name
	String getName()
	{
		return this.name;
	}

	void setQuantity(int i)
	{
		this.quantity = i;
	}


	// Set function for price
	void setPrice(float productPrice)
	{
		this.price = productPrice;
	}


	// Get function for price
	float getPrice()
	{
		return this.price;
	}


	// Add function for quantity
	void addQuantity(int productQuantity)
	{
			this.quantity = this.quantity + productQuantity;
			/*if(this.quantity < 0)
			this.waitlistedOrders = this.quantity * -1;
			else
			this.waitlistedOrders = 0;*/
	}


	// Subtract function for quantity
	void subtractQuantity(int productQuantity)
	{
		this.quantity = this.quantity - productQuantity;
		/*if(this.quantity < 0)
			this.waitlistedOrders = this.quantity * -1;
		else
			this.waitlistedOrders = 0;*/
	}


	// Get function for quantity
	int getQuantity()
	{
		if(quantity < 0)
		{
			return 0;
		}
		return this.quantity;
	}


	int getWaitlistedOrders()
	{
		return this.waitlistedOrders.size();
	}

	void addWaitlistClient(Client c)
	{
		waitlistedOrders.add(c);
	}

	void removeWaitlistedClient(Client c)
	{
		waitlistedOrders.remove(c);
		c.setBalance(c.getBalance()-this.price);
	}

	@Override
	public String toString()
	{
		return getClass().getName() + "[Name: " + name + " Price: " + price + " Quantity: " + quantity + " ID: " + id + "]";
	}

}

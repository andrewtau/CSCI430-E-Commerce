//Skylar Wolf
//CSCI 430
//Project 1 - Supplier Class

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Iterator;

public class Supplier implements Serializable
{
    String name; 
    String id;
    private ArrayList<Product> products;

    public Supplier() //Default constructor
    {
        name = "Unspecified Supplier";
        products = new ArrayList<Product>();
        id = UUID.randomUUID().toString();
    }

    public Supplier(String iName, ArrayList<Product> iProducts) //Name + Product list constructor
    {
        name = iName;
        products = iProducts;
        id = UUID.randomUUID().toString();
    }

    public Supplier(String iName) //Name only constructor
    {
        name = iName;
        products = new ArrayList<Product>();
        id = UUID.randomUUID().toString();
    }

    public String getSupplierInfo()
    {
        return "Name: " + name + " ID: " + id + " Product list: \n" + this.getProductList() + "\n";
    }

    public String getProductList()
    {
        String output = new String();
        for(int i = 0; i < products.size(); i++) {
            output = output + "ID: " + this.products.get(i).getID() + " " +this.products.get(i).getQuantity() + " " + this.products.get(i).getName() + " for $" +this.products.get(i).getPrice() + " (" + this.products.get(i).getWaitlistedOrders() + " waitlisted)";
            if(i != (products.size()-1)) {output = output + ", ";}
        }
        return output;
    }

    public ArrayList<Product> getProductListObj() 
    {
        return products;
    }

    public int getProductCount()
    {
        return this.products.size();
    }

    Iterator<Product> getProductIterator()
    {
        return this.products.iterator();
    }

    public void addProduct(Product product)
    {
        products.add(product);
        return;
    }

    public Product getProductByID(String id)
    {
        for(int i = 0; i < this.products.size(); i++) 
        {
            if(this.products.get(i).getID() == id)
            {
                return this.products.get(i);
            }
        }
        return null;
    }

    @Override
    public String toString()
    {
        return getClass().getName() + "[Name: " + name + " id: " + id + "]";
    }
} //End of Supplier Class
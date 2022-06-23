import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
public class Facade implements Serializable
{
    private ArrayList<Supplier> suppList;
    private ArrayList<Client> clientList;

    /**
     * This function finds a product by ID.
     * @param clientID The ID of the client to be searched for.
     * @return Client, or null if none were found.
     */
    private Client findClient(String clientID)
    {
        Iterator<Client> clientIterator = clientList.iterator();
        Client curClient = null;
        while(clientIterator.hasNext()) //For every item in the client list
        {
            curClient = clientIterator.next(); //Store in a variable to void skipping items (By calling .next() again too early)
            if(curClient.getID() == clientID)
            {
                return curClient;
            }
        }
        return null;
    }

    /**
     * This function finds a supplier by ID.
     * @param supplierID The ID of the Supplier to be searched for.
     * @return Supplier, or null if none were found.
     */
    private Supplier findSupplier(String supplierID)
    {
        Iterator<Supplier> suppIterator = suppList.iterator();
        Supplier curSupplier = null;
        while(suppIterator.hasNext()) //For every item in the supplier list.
        {
            curSupplier = suppIterator.next(); //Store in a variable to void skipping items
            if(curSupplier.id == supplierID)
            {
                return curSupplier;
            }
        }
        return null;
    }


    /**
     * This function finds a product by ID. If multiple suppliers have the same product, it uses the cheapest product it can find. 
     * @param productID The ID of the product to be searched for.
     * @return Product, or null if none were found.
     */
    private Product findProduct(String productID)
    {
        //int i,j;
        Product tempProduct = null;
        Product curProduct = null;
        Supplier curSupplier = null;
        Iterator<Supplier> suppIterator = suppList.iterator();
        Iterator<Product> prodIterator;
        while(suppIterator.hasNext()) //For every supplier in the supplier list
        {
            curSupplier = suppIterator.next();
            prodIterator = curSupplier.getProductIterator();
            while(prodIterator.hasNext()) //For every product in the current supplier.
            {
                curProduct = prodIterator.next();
                if(curProduct.getID() == productID) //If the product is found
                {
                    if(tempProduct == null) //If there is no currently selected product, create a new one so the price can be compared.
                    {
                        tempProduct = new Product();
                    }
                    if( ( (curProduct.getPrice() < tempProduct.getPrice()) && curProduct.getQuantity() > 0) || tempProduct.getPrice() == 0) //If the current product has a lesser price than the current selection, or if the current selection has no price
                    {
                        tempProduct = curProduct;
                    }
                }
            }
        }
        if(tempProduct.getPrice() == 0) //To avoid returning the temp product if no products are found. (assuming no products are free)
        {
            tempProduct = null;
        }
        return tempProduct;
    }

    /**
     * This function returns a list of products in a Supplier. 
     * @param s The supplier to get products from.
     * @return ArrayList<Product>
     */
    private ArrayList<Product> getSupplierProducts(Supplier s)
    {
        Iterator<Product> prodIterator = s.getProductIterator();
        ArrayList<Product> tempList = new ArrayList<Product>();
        while(prodIterator.hasNext())
        {
            tempList.add(prodIterator.next());
        }
        return tempList;
    }

    /**
     * This returns an arraylist of the current products in the system. Items with matching IDs are only displayed once, with the lowest priced item being returned.
     * @return Arraylist of products
     */
    private ArrayList<Product> getProductList()
    {
        int i = 0;
        Iterator<Supplier> suppIter = suppList.iterator();
        Supplier tempSupplier = null;
        Iterator<Product> prodIter = null;
        Product tempProduct = null;
        ArrayList<Product> tempList = new ArrayList<Product>();
        while(suppIter.hasNext())
        {
            tempSupplier = suppIter.next();
            prodIter = tempSupplier.getProductIterator();
            while(prodIter.hasNext())
            {
                tempProduct = prodIter.next();
                for(i = 0; i < tempList.size(); i++) //For every element in the tempList
                {
                    if((tempList.get(i).getID() == tempProduct.getID()) && (tempList.get(i).getPrice() > tempProduct.getPrice())) //If the current potential addition is included in the list already, check if it has a cheaper price. If so, add it and remove the duplicate.
                    {
                        tempList.remove(i);
                        tempList.add(tempProduct);
                        i = tempList.size(); //To escape the for loop
                    }
                    else if(tempList.get(i).getID() == tempProduct.getID()) //If the product already exists but is also already cheapter.
                    {
                        i = tempList.size(); //To escape the for loop
                    }
                    else if(i == tempList.size()-1) //If this is the last element
                    {
                        tempList.add(tempProduct);
                        i = tempList.size(); //To escape the for loop
                    }
                }
                if(tempList.size() == 0) //If this is the first element
                {
                    tempList.add(tempProduct);
                }
            }

        }
        return tempList;
    }
    
    /**
     * Default constructor. Creates a new supplier list and client list.
     */
    public Facade() //Default constructor
    {
        suppList = new ArrayList<Supplier>();
        clientList = new ArrayList<Client>();
    }

    /**
     * This function adds a product to a client's cart..
     * @param curClientID the ID to search for, productID the ID of the product to search for, Amt the quantity to add .
     * @return Supplier, or null if none were found.
     */
    public Boolean addToCart(String curClientID, String productID, int Amt) // Add product to customer's cart. 
    {
        Client tempClient = findClient(curClientID);
        Product tempProd = findProduct(productID);
        int i;
        if(tempClient == null || tempProd == null) {return false;} //If either the client or product are not found, return false.
        else
        {
            for(i = 0; i < Amt; i++) //Just adds the item until the correct amount is added. 
            {
                tempClient.addToCart(tempProd);
            }
            return true;
        }
    }

    /**
     * Removes the specified number of items from the cart.
     * @param clientID ClientID to modify
     * @param ProductID ID of product to remove
     * @param Amt Amount of items to remove
     * @return True if sucessful, otherwise false.
     */
    public Boolean removeFromCart(String clientID, String ProductID, int Amt)
    {
        int i;
        int itemCount = 0;
        int removedCount = 0;
        Client tempClient = findClient(clientID);
        ArrayList<Product> cart = tempClient.getCart();

        for(i = 0; i < cart.size(); i++) //First check if enough items exist.
        {
            if(cart.get(i).getID().contentEquals(ProductID)) 
            {
                itemCount++;
            }
        }
        if(itemCount < Amt) {
            System.out.println("ERROR: Could not find enough items to remove.");
            return false;
        }

        for(i = 0; i < cart.size(); i++) //If the check passed, remove the items
        {
            if(cart.get(i).getID().contentEquals(ProductID) && removedCount < Amt)
            {
                cart.remove(i);
                removedCount++;
                i=i-1;
            }
        }
        return true;
    }

    /**
     * Retrieves number of items of a specific type in the client's cart.
     * @param ClientID ID of client to get cart size from
     * @param productID Product to search for
     * @return Quantity of the selected item in the client's cart.
     */
    public int getCartQuantity(String ClientID, String productID)
    {
        int count = 0;
        int i;
        ArrayList<Product> tempCliCart = findClient(ClientID).getCart();
        for(i = 0; i < tempCliCart.size(); i++) 
        {
            if(tempCliCart.get(i).getID().contentEquals(productID))
            {
                count++;
            }
        }
        return count;
    }
    
    /**
     * This fuction changes the price of a product.
	 * @param productID the ID of the product to search for, price the new price of the product.
     */
    public void changeSalePrice(String productID, float price)
    {
        Product p = findProduct(productID);
        p.setPrice(price);
        return;
    }

    /**
     * This fuction adds a new Client to the clientList.
	 * @param balance the balance of the new Client.
	 * @return true if new Client is added to List, false if otherwise.
     */
    public Boolean addClient(double balance)
    {
        Client newClient = new Client(balance);
        clientList.add(newClient);
        if(clientList.get(clientList.size()-1).getID() == newClient.getID())
        {
            return true;
        }
        return false;
    }

    /**
     * This fuction adds a new Supplier to the suppList.
	 * @param name the name of the new Supplier.
	 * @return true if new Supplier is added to list, false if otherwise.
     */
    public Boolean addSupplier(String name)
    {
        Supplier newSupplier = new Supplier(name);
        suppList.add(newSupplier);
        if(suppList.get(suppList.size()-1).id == newSupplier.id)
        {
            return true;
        }
        return false;
    }
    
    /**
     * This fuction gets the Supplier by their name.
	 * @param name the name of the Supplier to search for.
	 * @return Supplier ID or null if not found.
     */
    public String getSupplierByName(String name)
    {
        int i;
        for(i = 0; i < suppList.size(); i++)
        {
            if(suppList.get(i).name.contentEquals(name))
            {
                return suppList.get(i).id;
            }
        }
        return null;
    }

    /**
     * This fuction gets the Supplier by their index location.
	 * @param i the integer of the index location used to locate Supplier. 
	 * @return supplierID the ID of the Supplier.
     */
    public String getSupplierByIndex(int i) //Returns the supplier stored in the associated index location of the supplier list.
    {
        return suppList.get(i).id;
    }

    /**
     * This fuction gets the Product by their index location.
	 * @param i the integer of the index location used to locate Product. 
	 * @return productID the ID of the Product.
     */
    public String getProductByIndex(int i)
    {
        ArrayList<Product> list = getProductList();
        return list.get(i).getID();
    }

    /**
     * Gets size of product list.
     * @return Int, size of product list
     */
    public int getProductListSize()
    {
        ArrayList<Product> list = getProductList();
        return list.size();
    }
  
    /**
     * This fuction gets the Client by their index location.
	 * @param i the integer of the index location used to locate Client. 
	 * @return clientID the ID of the Client.
     */
    public String getClientByIndex(int i) //Returns the client stored in the associated index location of the client list.
    {
        return this.clientList.get(i).getID();
    }

    /**
     * Creates a string of the client's info, including: ID, Balance, Cart
     * @param clientID ID of client to get info for
     * @return String
     */
    public String getClientInfo(String clientID)
    {
        String clientInfo = findClient(clientID).toString() + " Cart: ";
        ArrayList<Product> cart = findClient(clientID).getCart();
        ArrayList<Product> cartP = new ArrayList<Product>();
        ArrayList<Integer> cartPamnt = new ArrayList<>();
        int index, tempVal, i;

        for(i = 0; i < cart.size(); i++)
        {
            if(cartP.contains(cart.get(i)))
            {
                index = cartP.indexOf(cart.get(i));
                cartPamnt.set(index, cartPamnt.get(index)+1);
            }
            else 
            {
                cartP.add(cart.get(i));
                cartPamnt.add(1);
            }
        }

        for(i = 0; i < cartP.size(); i++)
        {
            clientInfo = clientInfo + cartPamnt.get(i) + " " + cartP.get(i).getName() + ", ";
        }
        return clientInfo;
    }

    /**
     *  Gets the client's waitlist as a string, for UI use. 
     * @param clientID ID of client to get waitlist for
     * @return String, client waitlist
     */
    public String getClientWaitlist(String clientID)
    {
        return findClient(clientID).getWaitlist().toString();
    }

    /**
     * Retrieves the current supplier list. 
     * @return Output - String, output to be presented to UI.
     */
    public String retSupplierList() //Prints every supplier in an indexed list, without products listed.
    {
        String output = "Supplier list\n---------------------\nFormat: name, id\n";
        for(int i = 0; i < suppList.size(); i++) //For every supplier in the supplier list.
        {
            output = output + i + ". " + suppList.get(i).name + " " + suppList.get(i).id + "\n";
        }
        return output;
    }

    /**
     * Retrieves the current supplier list.
     * @return Output - String, output to be presented to UI.
     */
    public String retClientList() //Retrieves client list
    {
        int i;
        String output = "Client List\n--------------------\n";
        for(i = 0; i < clientList.size(); i++)
        {
            output = output + i + ". " + "ID: " + clientList.get(i).getID() + "\n";
        }
        return output;
    }
	
	public String retClientListWithNoTransactions()
	{
		int i;
		String output = "Client List\n--------------------\n";
		for(i = 0; i < clientList.size(); i++)
		{
			if(clientList.get(i).getTransactionList().isEmpty()) // Ensure we're only adding clients with no transactions
			{
				output = output + i + ". " + "ID: " + clientList.get(i).getID() + "\n";	
			}
		}
		return output;
	}

    /**
     * Retrieves the current supplier list.
     * @return Output - String, output to be presented to UI.
     */
    public String retProductList()
    {
        int i = 0;
        String output = "Product List\n--------------------\n";
        ArrayList<Product> prodList = getProductList();
        Iterator<Product> prodListIter = prodList.iterator();
        Product tempProduct = null;
        while(prodListIter.hasNext())
        {
            tempProduct = prodListIter.next();
            output = output + i + ". " +" Name: " + tempProduct.getName() + " Lowest Price: " + tempProduct.getPrice() + " In-Stock @ price: " + tempProduct.getQuantity() + "\n";
            i++;
        }
        return output;
    }

    /**
     * Lists suppliers. 
     * @return Output - String, output to be presented to UI.
     */
    public String listSuppliers()
    {
        String output = new String();
        for(int i = 0; i<suppList.size(); i++) 
        {
            output = output + suppList.get(i).getSupplierInfo() + "\n";
        }
        return output;
    }

   /**
     * Lists all suppliers that have a product.
     * @param id - id of product to search with.
     * @return Output - String, output to be presented to UI.
     */
    public String listSuppliersWithProduct(String id)
    {
        String output = "Suppliers With Matching Products:\n ---------------------------------------\n";
        Supplier tempSupplier = new Supplier();
        for(int i = 0; i<suppList.size(); i++) 
        {
            tempSupplier = suppList.get(i);
            if(tempSupplier.getProductByID(id) != null)
            {
                output = output + "Name: " + tempSupplier.name + " Price: " + tempSupplier.getProductByID(id).getPrice() + "\n";
            }
        }
        return output;
    }

    /**
     * This function retrieves the complete list of products, including any duplicates.
     * @return string Output - A string of the output to be printed.
     */
    public String getCompleteProductList()
    {
        String output = "Complete product list\n----------------------------------------------------\n";
        Iterator<Supplier> suppIter = suppList.iterator();
        Iterator<Product> prodIter = null;
        while(suppIter.hasNext())
        {
            Supplier curSupplier = suppIter.next();
            prodIter = curSupplier.getProductIterator();
            while(prodIter.hasNext())
            {
                Product curProduct = prodIter.next();
                output = output + "Name: " + curProduct.getName() + " ID : " + curProduct.getID() + " Quantity: " + curProduct.getQuantity() + " Waitlist amnt: " + curProduct.getWaitlistedOrders() + " Supplier: " + curSupplier.name + "\n";
            }
        }
        return output;
    }

    /**
     * Adds a product to a supplier's product list.
     * @param p a product that is to be added to the supplier. 
     * @param id the id of the supplier to add the product to. 
     * @return A boolean value based on whether or not the function succeeded. 
     */
    public boolean addProductToSupplier(Product p, String id)
    {
        Supplier targSupplier = findSupplier(id);
        if(targSupplier == null) {return false;}
        targSupplier.addProduct(p);
        return true;
    }
    
    /**
     * Lists all clients with an outstanding balance. 
     * @return Output, a string which contains the output to present to the UI.
     */
    public String listOutstandingClients()
	{
		String output = new String();
		for(int i = 0; i < clientList.size(); i++)
		{
			if(clientList.get(i).getBalance() < 0)
			output = output + clientList.get(i).toString() + "\n";
		}
		return output;
	}
	
	/**
     * Lists the current waitlisted products.
     * @param ClentID - id of client to get transactions of
     * @return Prints the transactions to terminal.
     */
	public void getClientTransactions(String clientID)
	{
        Client c = findClient(clientID);
	    System.out.println("Client's ID: " + c.getID());
	    for(int i = 0; i < c.getTransactionList().size(); i++)
	    {
            System.out.println("--------------");
            System.out.println("Transaction " + (i+1));
            System.out.println("--------------");
            c.displayTransactions(c.getTransactionList().get(i));
	    }
	}  // End getClientTransactions function

    /**
     * Processes order for a selected client
     * @param Client - Id of client to process order of
     * @return Transaction - Transaction resulting from Client's order.
     */
    public Transaction ProcessOrder(String client)
    {
        Client c = findClient(client);
        ArrayList<Product> cart = c.getCart();
        Transaction newTransaction = new Transaction();
        for(int i = 0; i < cart.size(); i++)
        {
            if(cart.get(i).getQuantity()>0) //If the product is available.
            {
                cart.get(i).subtractQuantity(1);
                newTransaction.addProduct(cart.get(i));
                newTransaction.addToTotal((double)cart.get(i).getPrice());
                c.setBalance(c.getBalance()-cart.get(i).getPrice());
            }
            else //If the product needs to be waitlisted.
            {
                c.addWaitlistedOrder(cart.get(i));
                cart.get(i).addWaitlistClient(c);
            }
        }
        ArrayList<Product> emptyCart = new ArrayList<Product>();
        c.setCart(emptyCart); //Empty the cart as all orders have been processed.
        c.addTransaction(newTransaction);
        return newTransaction;
    }

    /**
     * Accepts payment from a client.
     * @param  ID - ID of client to accept payment from.
     * @return May print results to console.
     */
    public void acceptPayment(String ID)
    {
        Client targClient = findClient(ID);
        Scanner scanner = new Scanner(System.in);
        double balance = targClient.getBalance();
        System.out.println("--------------------\nBalance = " + balance + "\nEnter Payment Amount:");
        double tempBalance = scanner.nextDouble();
        double newBalance = balance + tempBalance;
        targClient.setBalance(newBalance);
        System.out.println("--------------------\nID: " + ID + "\nNew Balance = " + newBalance);
    }

    /**
     * Accepts additional stock of items from supplier. Will loop through every product in the supplier's list and ask if they have incoming stock. If waitlisted orders are present, the actor will be prompted to be confirmed if they should be fulfilled.
     * @param ID - ID of supplier to accept shipment from.
     */
    public void acceptShipment(String ID)
    {
        int i;
        Supplier targSupp = findSupplier(ID);
        ArrayList<Product> products = getSupplierProducts(targSupp);
        ArrayList<Product> tempWaitlist = new ArrayList<Product>();
        ArrayList<Client> tempClientList = new ArrayList<Client>();
        Scanner scanner = new Scanner(System.in);
        Product curProduct = null;
        Client tempClient = null;
        String input;
        int intInput;
        for(i = 0; i < products.size(); i++)
        {
            curProduct = products.get(i);
            System.out.println("Product " + (i+1) + " of " + products.size() + ": ");
            System.out.println("Name: " + curProduct.getName() + " Price: " + curProduct.getPrice() + " Quantity: " + curProduct.getQuantity() + "\n");
            System.out.println("Is this item included in the shipment? (Y/N) ");
            input = scanner.nextLine();
            if(input.equalsIgnoreCase("y"))
            {
                System.out.println("Please enter the amount of products in the shipment: ");
                intInput = scanner.nextInt();
                scanner.nextLine();
                if(curProduct.getWaitlistedOrders() > 0)
                {
                    Iterator<Client> tempList = curProduct.waitlistedOrders.iterator();
                    while(tempList.hasNext()) 
                    {
                        tempClient = tempList.next();
                        System.out.println("Do you want to fufill the waitlisted order for " + tempClient.getID() + ", who has a balance of $" + tempClient.getBalance() + "? (y/n) ");
                        input = scanner.nextLine();
                        if(input.equalsIgnoreCase("y"))
                        {
                            tempClient.getWaitlist().remove(curProduct);
                            tempWaitlist.add(curProduct); //The product waitlist cannot be modified while traversing it, so the changes are stored for later.
                            tempClientList.add(tempClient);
                            Transaction newTransaction = new Transaction();
                            newTransaction.addProduct(curProduct);
                            newTransaction.addToTotal((double)curProduct.getPrice());
                            tempClient.addTransaction(newTransaction);
                            intInput--;
                        }
                        else if (input.equalsIgnoreCase("n") == false) //As of now, just assumes N if not Y or N.
                        {
                            System.out.println("Invalid input. Assuming N...\n");
                        }
                    }
                    
                    Iterator<Product> tempIt = tempWaitlist.iterator();
                    Iterator<Client> tempItClient = tempClientList.iterator();
                    while(tempIt.hasNext()) //Product waitlist changes are processed here.
                    {
                        tempIt.next().removeWaitlistedClient(tempItClient.next());
                    }
                }
                curProduct.addQuantity(intInput);
            }
            else if (input.equalsIgnoreCase("n") == false)
            {
                System.out.println("Invalid input.\n");
                i = i-1; //To redo the current input
            }
        }
    } //End of Accept Shipment

    /**
     * Copies a product so as to not create a reference. Does not copy waitlist. 
     * @param productID - Product to copy from
     * @return tempProduct - A product copied from the input product. 
     */
    public Product copyProduct(String productID)
    {
        Product tempProduct = new Product();
        Product copyTarg = findProduct(productID);
        tempProduct.setID(copyTarg.getID());
        tempProduct.setName(copyTarg.getName());
        tempProduct.setPrice(copyTarg.getPrice());
        tempProduct.setQuantity(copyTarg.getQuantity());
        return tempProduct;
    }

    /**
     * Checks if a supplier has a product. 
     * @param supplierID - ID of supplier to check for the product.
     * @param  productID - ID of the product to check if the supplier has. 
     * @return Boolean value. True if the suppleir has the input product. False otherwise.
     */
    public boolean doesSupplierHaveProduct(String supplierID, String productID)
    {
        Supplier targSupplier = findSupplier(supplierID);
        Iterator<Product> suppProds = targSupplier.getProductIterator();
        while(suppProds.hasNext())
        {
            if(suppProds.next().getID() == productID)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Displays all items in a clients cart. The actor can choose items to remove, which loops until they exit.
     * @param clientID - ID of client to edit the cart of.
     */
    public void editCart(String clientID)
    {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        int i = 0;
        int intInput;
        ArrayList<Product> tempCart = findClient(clientID).getCart();
        while(exit == false)
        {
            System.out.println("Please select a product to remove from the cart. Or type -1 to exit.");
            Iterator<Product> productIt = tempCart.iterator();
            i = 0;
            while(productIt.hasNext())
            {
                Product tempProduct = productIt.next();
                System.out.println(i + ". Name: " + tempProduct.getName() + " Price: " + tempProduct.getPrice());
                i++;
            }
            intInput = scanner.nextInt();
            scanner.nextLine();
            if(intInput == -1)
            {
                exit = true;
                System.out.println("Exiting to menu...");
            }
            else if(intInput<i)
            {
                tempCart.remove(intInput);
            }
            else
            {
                System.out.println("Invalid input!");
            }
            i = 0;

        }
    }

    /**
     * Removes a product from a supplier with a prompt.
     * @param ID ID of supplier to modify
     * @return True if sucessful, otherwise false.
     */
    public boolean removeSupplierProduct(String ID)
    {
        Scanner scanner = new Scanner(System.in);
        int i, choice;
        ArrayList<Product> ProductList = findSupplier(ID).getProductListObj();
        System.out.println("Product list for supplier with ID " + ID);
        for(i = 0; i < ProductList.size(); i++)
        {
            System.out.println(i + ". " + ProductList.get(i).getName() + " " + ProductList.get(i).getID());
        }
        System.out.println("Please select a product to remove (Enter -1 to cancel): ");
        choice = scanner.nextInt();
        if(choice == -1 || (choice >= ProductList.size())) {return false;} //Invalid selection
        ProductList.remove(choice);
        return true;
    }

    /**
     * Changes the price for a supplier's product with a prompt.
     * @param ID ID of supplier to modify
     * @return True if sucessful, otherwise false.
     */
    public boolean changeSupplierProductPrice(String ID)
    {
        Scanner scanner = new Scanner(System.in);
        int i, choice;
        float newPrice;
        ArrayList<Product> ProductList = findSupplier(ID).getProductListObj();
        System.out.println("Product list for supplier with ID " + ID);
        for(i = 0; i < ProductList.size(); i++)
        {
            System.out.println(i + ". " + ProductList.get(i).getName() + " " + ProductList.get(i).getID());
        }
        System.out.println("Please select a product to modify (Enter -1 to cancel): ");
        choice = scanner.nextInt();
        if(choice == -1 || (choice >= ProductList.size())) {return false;} //Invalid selection
        System.out.println("What should the new price be? ");
        newPrice = scanner.nextFloat();
        if(newPrice < 0) {return false;} //Items cannot have negative price
        ProductList.get(choice).setPrice(newPrice);
        scanner.nextLine();
        return true;
    }

    /**
     *  Creates string that lists supplier's products.
     * @param ID ID of supplier to check.
     * @return String
     */
    public String getSupplierProductList(String ID)
    {
        ArrayList<Product> tempList = getSupplierProducts(findSupplier(ID));
        String output = "Supplier product list\n---------------------------------\n";
        for(int i = 0; i < tempList.size(); i++)
        {
            output = output + i + ". Name: " + tempList.get(i).getName() + " Price: $" + tempList.get(i).getPrice() + "\n";
        }
        return output;
    }

    /**
     * Gets waitlisted orders from a product
     * @param ID Id of product to check
     * @return String
     */
    public String retWaitListProduct(String ID) {
        Product tempProduct = findProduct(ID);
        return tempProduct.waitlistedOrders.toString();
    }
} //End of Facade
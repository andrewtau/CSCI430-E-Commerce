import java.util.Scanner;

public class Project2UI {
    static Facade businessSystem = new Facade(); //Facade is declared globally for the class this time. No need to pass the facade to helper functions anymore.
    
    /**
     * Displays a menu of suppliers and retrieves the ID of the selected supplier.
     * @return ID of selected supplier
     */
    static String selectSupplier()
    {
        Scanner scanner = new Scanner(System.in);
        int sel = 0;
        System.out.println(businessSystem.retSupplierList());
        System.out.println("Please enter an integer to select a supplier: ");
        sel = scanner.nextInt();
        return businessSystem.getSupplierByIndex(sel);
    }

    /**
     * Displays a menu of Clients and retrieves the ID of the selected supplier.
     * @return ID of client
     */
    static String selectClient()
    {
        Scanner scanner = new Scanner(System.in);
        int sel = 0;
        System.out.println(businessSystem.retClientList());
        System.out.println("Please enter an integer to select a target client: ");
        sel = scanner.nextInt();
        return businessSystem.getClientByIndex(sel);
    }

    /**
     * Displays a menu of products and retrieves the ID of the selected supplier.
     * @return ID of product
     */
    static String selectProduct()
    {
        Scanner scanner = new Scanner(System.in);
        int sel = 0;
        System.out.println(businessSystem.retProductList());
        System.out.println("Please enter an integer to select a Product: ");
        sel = scanner.nextInt();
        return businessSystem.getProductByIndex(sel);
    }

    /**
     * Displays the client menu in a loop, until the user decides to logout.
     * @param ID ID of client to access the system as.
     */
    static void ClientMenu(String ID) 
    {
        int choice = 1000;
        String menu = "Please select an option:\n   0. Get Client Details\n   1. Show product list with sale prices\n   2. Get Transactions\n   3. Modify Cart\n   4. Display waitlist\n   5. Logout";
        boolean canExit = false;
        Scanner scanner = new Scanner(System.in);
        System.out.println(menu); //Initial menu print
        while(canExit == false)
        {
            System.out.println("Please select an option (To display the menu, enter -1): ");
            choice = scanner.nextInt();
            scanner.nextLine(); //To prevent skipping the next input.
            switch(choice)
            {
                case 0: //Get Client Details
                    System.out.println(businessSystem.getClientInfo(ID));
                    break;
                case 1: //Show product list with sale prices
                    System.out.println(businessSystem.retProductList());
                    break;
                case 2: //Get Transactions
                    businessSystem.getClientTransactions(ID);
                    break;
                case 3: //Modify shopping cart (Display content, actor can add, remove, or change quantity)
                    while(canExit == false) {
                        System.out.println("Cart Menu:\n   0. Leave Cart Menu\n   1. Add product\n   2. Remove product\n   3. Change quantity");
                        System.out.println("Please select an option: ");
                        choice = scanner.nextInt();
                        scanner.nextLine(); //To avoid input skipping

                        switch(choice)
                        {
                            case 0: //exit
                                canExit = true;
                                break;
                            case 1: //Add product
                                if(businessSystem.addToCart(ID, selectProduct(), 1) == false)
                                {
                                    System.out.println("ERROR: Could not add product to cart.");
                                }
                                break;
                            case 2: //Remove product
                                businessSystem.editCart(ID);
                                break;
                            case 3: //Change quantity 
                                String tempProduct = selectProduct();
                                int productQuant = businessSystem.getCartQuantity(ID, tempProduct);
                                System.out.println("Please select the new quantity (Current quantity is " + productQuant + "):");
                                int desiredQuant = scanner.nextInt();
                                scanner.nextLine();
                                if(desiredQuant == productQuant)
                                {
                                    break;
                                }
                                else if(productQuant > desiredQuant)
                                {
                                    businessSystem.removeFromCart(ID, tempProduct, (productQuant - desiredQuant));
                                }
                                else if(productQuant < desiredQuant)
                                {
                                    businessSystem.addToCart(ID, tempProduct, (desiredQuant - productQuant));
                                }
                                break;
                            default:
                                System.out.println("ERROR: Invalid input.");
                                break;
                        } //End of switch(choice) 2
                    } //End of while(canExit == false) 2
                    canExit = false; //Reset so it does not exit the main loop
                    break;
                case 4: //Display client waitlist
                    System.out.println(businessSystem.getClientWaitlist(ID));
                    break;
                case 5: //Logout
                    canExit = true;
                    break;
                case -1:
                    System.out.println(menu);
                    break;
                default:
                    System.out.println("ERROR: Invalid input.");
                    break;
            } //End of switch(choice)
        } //End of while(canExit == false)
        return;
    }
    
    /**
     * Displays the clerk menu in a loop, until the user decides to logout.
     */
    static void ClerkMenu()
    {
        int choice = 1000;
        String menu = "Please select an option:\n   0. Add a client\n   1. Show list of products, prices, and quantities\n   2. Display Client Menu\n   3. Become a client\n   4. Display the waitlist for a product\n   5. Recieve a shipment\n   6. Process orders for a client\n   7. Logout";
        boolean canExit = false;
		double clientBalance;
        Scanner scanner = new Scanner(System.in);
        System.out.println(menu); //Initial menu print
        while(canExit == false)
        {
            System.out.println("Please select an option (To display the menu, enter -1): ");
            choice = scanner.nextInt();
            scanner.nextLine(); //To prevent skipping the next input.
            switch(choice)
            {
                case 0: //Add a client
		            System.out.println("Please enter the client's balance:");
                    clientBalance = scanner.nextDouble();
                    if(businessSystem.addClient(clientBalance))
                    {
                        System.out.println("Success! New Client was added!");
                    }
                    else
                    {
                        System.out.println("ERROR: New Client could not be added!");
                    }
                    break;
                case 1: //Show list of products with quantities and sales prices
                    System.out.println(businessSystem.getCompleteProductList());
                    break;
                case 2: //Show client list --> Merging Case 2 and Case 3 into a Menu
						while(canExit == false)
						{
							System.out.println("Client List Menu:\n   0. Leave Client List Menu\n   1. Display All Clients\n   2. Display List of Outstanding customers\n   3. Display List of Customers with no transactions");
							System.out.println("Please select an option: ");
							choice = scanner.nextInt();
							scanner.nextLine(); //To avoid input skipping
							switch(choice)
							{
								case 0: // Leave Client List Menu
									canExit = true;
									break;
								case 1: // Display All Clients
									System.out.println(businessSystem.retClientList());
									break;
								case 2: // Display List of Outstanding Customers
									System.out.println(businessSystem.listOutstandingClients());
									break;
								case 3: // Display List of Customers with no transactions
									System.out.println(businessSystem.retClientListWithNoTransactions());
									break;
							}// End Switch
						} // End While
		            System.out.println(businessSystem.retClientList());
                    break;
                /*case 3: //Show clients with outstanding balances
		            System.out.println(businessSystem.listOutstandingClients());
                    break;*/
                case 3: //Become a client
                    ClientMenu(selectClient());
                    System.out.println("--------------------Returned to clerk menu--------------------");
                    System.out.println(menu); //Forcing a menu print to avoid confusion.
                    break;
                case 4: //Display the waitlist for a product
                    System.out.println(businessSystem.retWaitListProduct(selectProduct()));
                    break;
                case 5: //Recieve a shipment
		            businessSystem.acceptShipment(selectSupplier());
                    break;
                case 6: //Process orders
                    System.out.println(businessSystem.ProcessOrder(selectClient()).toString());
                    break;
                case 7: //Logout
                    canExit = true;
                    break;
                case -1:
                    System.out.println(menu);
                    break;
                default:
                    System.out.println("ERROR: Invalid input.");
                    break;
            }
        }
        return;
    }

    /**
     * Displays the manager menu in a loop, until the user decides to logout.
     */
    static void ManagerMenu()
    {
        int choice = 1000;
        String menu = "Please select an option:\n   0. Add a new product\n   1. Add a supplier\n   2. Show list of suppliers.\n   3. Show list of suppliers for a product, with prices.\n   4. Show list of products for a supplier with prices\n   5. Update products for a supplier\n   6. Become a salesclerk\n   7. Logout";
        boolean canExit = false;
        boolean success = false;
        Scanner scanner = new Scanner(System.in);
        Product tempProduct = new Product();
        System.out.println(menu); //Initial menu print
        while(canExit == false)
        {
            System.out.println("Please select an option (To display the menu, enter -1): ");
            choice = scanner.nextInt();
            scanner.nextLine(); //To prevent skipping the next input.
            switch(choice)
            {
                case 0: //Add a product (Note: Existing products are now added in case 5.)
                    System.out.println("Please enter the product's name: ");
                    tempProduct.setName(scanner.nextLine());
                    System.out.println("Please enter the amount of the product: ");
                    tempProduct.addQuantity(scanner.nextInt());
                    scanner.nextLine();
                    System.out.println("Please enter the price of the product: ");
                    tempProduct.setPrice(scanner.nextFloat());
                    scanner.nextLine();

                    System.out.println("Please select a supplier to add the product to:"); //Supplier is selected as a parameter to addProductToSupplier
                    success = businessSystem.addProductToSupplier(tempProduct, selectSupplier());
                    if(success == false) {System.out.println("ERROR: Could not add product to supplier.");}
                    break;
                case 1: //Add a supplier
                    System.out.println("Please enter the name of the new supplier: ");
                    if(businessSystem.addSupplier(scanner.nextLine()) == false) {System.out.println("ERROR: Could not add supplier.");}
                    break;
                case 2: //Show list of suppliers
                    System.out.println(businessSystem.listSuppliers());
                    break;
                case 3: //Show list of suppliers for product, with purchase prices
                    System.out.println(businessSystem.listSuppliersWithProduct(selectProduct()));
                    break;
                case 4: //Show list of products for a supplier, with purchase prices.
                    System.out.println(businessSystem.getSupplierProductList(selectSupplier()));
                    break;
                case 5: //Update products and purchase prices for a particular supplier. Actor can add/remove products or change purchase prices. 
                    String tempSupp = selectSupplier();
                    while(canExit == false)
                    {
                        System.out.println("Supplier Modification Menu\n   0. Exit\n   1. Add existing product to supplier\n   2. Remove product from supplier\n   3. Change price of product");
                        System.out.println("Please select an option: ");
                        choice = scanner.nextInt();
                        scanner.nextLine();

                        switch (choice) {
                            case 0: //Exit
                                canExit = true;
                                break;
                            case 1: //Copy existing product to supplier
                                tempProduct = businessSystem.copyProduct(selectProduct());
                                System.out.println("Copying from " + tempProduct.toString());
                                if(businessSystem.doesSupplierHaveProduct(tempSupp, tempProduct.getID())) 
                                {
                                    System.out.println("ERROR: Supplier already has this product.");
                                    break;
                                }

                                System.out.println("Please enter the new price: ");
                                tempProduct.setPrice(scanner.nextFloat());
                                scanner.nextLine();

                                System.out.println("Please enter the new quantity: ");
                                tempProduct.setQuantity(scanner.nextInt());
                                scanner.nextLine();

                                System.out.println("Adding " + tempProduct.toString() + " to supplier with ID " + tempSupp);
                                if(businessSystem.addProductToSupplier(tempProduct, tempSupp) == false) {System.out.println("ERROR: Could not add product to supplier.");}
                                tempProduct = new Product();
                                break;
                            case 2: //Remove product
                                if(businessSystem.removeSupplierProduct(tempSupp) == false) {System.out.println("ERROR: Operation was cancelled or the selected index is invalid.");}
                                break;
                            case 3: //Change price
                                if(businessSystem.changeSupplierProductPrice(tempSupp) == false) {System.out.println("ERROR: Invalid product selected, or invalid price input.");}
                                break;
                            default:
                                break;
                                
                        }
                    }
                    canExit = false; //Reset
                    break;
                case 6: //Become a salesclerk
                    ClerkMenu();
                    System.out.println("--------------------Returned to manager menu--------------------");
                    System.out.println(menu); //Forcing a menu print to avoid confusion.
                    break;
                case 7: //Logout
                    canExit = true;
                    break;
                case -1:
                    System.out.println(menu);
                    break;
                default:
                    System.out.println("ERROR: Invalid input.");
                    break;
            }
        }
        return;
    }

    public static void main(String[] args) 
    {
		        //----------------------------Default System State----------------------------
        Product p1,p2,p3,p4,p5;
            p1 = new Product("Apple", (float)0.50, 30);
            p2 = new Product("Pear", (float)0.35, 47);
            p3 = new Product("Laptop", (float)350, 2);
            p4 = new Product("Screwdriver", (float)2.75, 0);
            p5 = new Product("Ice Cream", (float)6.50, 45);
        businessSystem.addSupplier("SupplierONE");
        businessSystem.addSupplier("SupplierTWO");
        businessSystem.addSupplier("SupplierTHREE");
        businessSystem.addProductToSupplier(p1, businessSystem.getSupplierByIndex(0));
        businessSystem.addProductToSupplier(p2, businessSystem.getSupplierByIndex(0));
        businessSystem.addProductToSupplier(p3, businessSystem.getSupplierByIndex(1));
        businessSystem.addProductToSupplier(p4, businessSystem.getSupplierByIndex(1));
        businessSystem.addProductToSupplier(p5, businessSystem.getSupplierByIndex(2));
        businessSystem.addClient(13);
        businessSystem.addClient(12);
        businessSystem.addClient(3);
        businessSystem.addClient(100);
		
        boolean canExit = false;
        int choice = 1000;
        Scanner scanner = new Scanner(System.in);
        Simport importer = new Simport(); //Importer for importing different system state files.
        importer.importTestFile("./test1.txt", businessSystem); //Default system

        while(canExit == false) 
        {
            System.out.println("Login Menu:\n   0. Exit\n   1. Login as client\n   2. Login as clerk\n   3. Login as manager");
            System.out.println("Please select an option: ");
            choice = scanner.nextInt();
            scanner.nextLine(); //To prevent input skipping

            switch(choice)
            {
                case 0: //Exit
                    canExit = true;
                    break;
                case 1: //Login as client
                    ClientMenu(selectClient()); //Obviously the client wont be able to just see a list and pick an account in the future, but we'll do this until we set up logins.
                    break;
                case 2: //Login as clerk
                    ClerkMenu();
                    break;
                case 3: //Login as manager
                    ManagerMenu();
                    break;
                default:
                    System.out.println("ERROR: Invalid input.");
                    break;
            }

        }
    }
}
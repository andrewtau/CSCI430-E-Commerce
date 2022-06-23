import java.util.Scanner;

//Note: UI is currently in a basic testing stage.
public class UI {
    static String selectSupplier(Facade f)
    {
        Scanner scanner = new Scanner(System.in);
        int sel = 0;
        System.out.println(f.retSupplierList());
        System.out.println("Please enter an integer to select a supplier: ");
        sel = scanner.nextInt();
        return f.getSupplierByIndex(sel);
    }

    static String selectClient(Facade f)
    {
        Scanner scanner = new Scanner(System.in);
        int sel = 0;
        System.out.println(f.retClientList());
        System.out.println("Please enter an integer to select a target client: ");
        sel = scanner.nextInt();
        return f.getClientByIndex(sel);
    }

    static String selectProduct(Facade f)
    {
        Scanner scanner = new Scanner(System.in);
        int sel = 0;
        System.out.println(f.retProductList());
        System.out.println("Please enter an integer to select a Product: ");
        sel = scanner.nextInt();
        return f.getProductByIndex(sel);
    }
    public static void main(String[] args) 
    {
        String menu = "Please select an option:\n0. Exit\n1. Add a product to cart\n2. Change Price of a Product\n3. Add a Client\n4. Add a Supplier\n5. Show a list of every client\n6. Show a list of every supplier\n7. List Suppliers that provide a specific product\n8. Add a Product to a Supplier\n9. List Clients with outstanding balances\n10. Process Orders\n11. Accept Payment\n12. Accept Shipment from Supplier\n13. Edit a Client's Shopping Cart\n14. Get complete product list\n15. Get a client's transaction list";
        int choice = 1000;
        String name = new String();
        Supplier tempSup = new Supplier();
        String tempCli;
        String ID_to_be_searched;
        String choiceYN;
        float priceChange;
        Product tempProd = new Product();
        boolean canExit = false;
        boolean success = false;
        Facade businessSystem = new Facade();
        Scanner scanner = new Scanner(System.in);
        double clientBalance;

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
        //----------------------------Default System State----------------------------

        System.out.println(menu); // Moved menu outside of do-while to allow for easier readability of output
        while(canExit == false)
        {
            System.out.println("Please select an option (To display the menu, enter -1): ");
            choice = scanner.nextInt();
            scanner.nextLine(); //Required to prevent skipping the next input.
            switch (choice) 
            {
                case 0: //Exit
                    canExit = true;
                    break;
                case 1: //Add a product to the cart
                    tempCli = selectClient(businessSystem);
                    String selectedProduct = selectProduct(businessSystem);
                    Boolean addSuccess = businessSystem.addToCart(tempCli, selectedProduct, 1);
                    if(addSuccess == false) {System.out.println("ERROR: Could not add to cart.\n");}
                    break;
                case 2: //Change price of a product
                    ID_to_be_searched = selectProduct(businessSystem);
                    System.out.println("Enter the new price: ");
                    priceChange = scanner.nextFloat();
                    businessSystem.changeSalePrice(ID_to_be_searched, priceChange);
                    System.out.println("Success! Price of product: " + ID_to_be_searched + " has been successfully changed to: $" + priceChange);
                    break;
                case 3: //Add a client
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
                case 4: //Add a supplier
                    System.out.println("Please enter the name of the supplier: ");
                    name = scanner.nextLine();
                    if(businessSystem.addSupplier(name))
                    {
                        System.out.println("Success! New Supplier was added!");
                    }
                    else
                    {
                        System.out.println("ERROR: New Supplier could not be added!");
                    }
                    break;
                case 5: //List every client
                    System.out.println(businessSystem.retClientList());
                    break;
                case 6: //List every supplier
                    System.out.println(businessSystem.retSupplierList());
                    break;
                case 7: //List suppliers that supply a product
                    ID_to_be_searched = selectProduct(businessSystem);
                    System.out.println(businessSystem.listSuppliersWithProduct(ID_to_be_searched));
                    break;
                case 8: //Add a product to a supplier
                    String targSupplier = selectSupplier(businessSystem);
                    System.out.println("Does the desired product exist in the system? (y/n)");
                    choiceYN = scanner.nextLine();
                    if(choiceYN.equalsIgnoreCase("y")) //If an existed product is to be copied.
                    {
                        tempProd = businessSystem.copyProduct(selectProduct(businessSystem)); //Copy a product to the temp product.
                        if(businessSystem.doesSupplierHaveProduct(targSupplier, tempProd.getID()))
                        {
                            System.out.println("Error: Product already in supplier's product list.");
                            break;
                        }
                        System.out.println("What price does this supplier offer?");
                        tempProd.setPrice(scanner.nextFloat());
                        scanner.nextLine();
                        System.out.println("What quantity does this supplier have?");
                        tempProd.setQuantity(scanner.nextInt());
                        scanner.nextLine();
                    }
                    else if(choiceYN.equalsIgnoreCase("n")) //If a new product is to be added.
                    {
                        System.out.println("Please enter the product's name: ");
                        tempProd.setName(scanner.nextLine());
                        System.out.println("Please enter the amount of the product: ");
                        tempProd.addQuantity(scanner.nextInt());
                        scanner.nextLine();
                        System.out.println("Please enter the price of the product: ");
                        tempProd.setPrice(scanner.nextFloat());
                        scanner.nextLine();
                    }
                    else
                    {
                        System.out.println("ERROR: Invalid input.");
                        break;
                    }
                    success = businessSystem.addProductToSupplier(tempProd, targSupplier);
                    tempProd = new Product();
                    if(success == false)
                    {
                        System.out.println("ERROR: Could not find the supplier.");
                        scanner.nextLine();
                    }
                    else {System.out.println("Product added sucessfully!");}
                    break;

                case 9: //List customers with outstanding balances.
                    System.out.println(businessSystem.listOutstandingClients());
                    break;

                case 10: //Process orders
                    tempCli = selectClient(businessSystem);
                    System.out.println(businessSystem.ProcessOrder(tempCli).toString());
                    break;

                case 11: //Accept payment
                    businessSystem.acceptPayment(selectClient(businessSystem));
                    break;

                case 12: //Accept Shipment
                    //ID_to_be_searched = selectSupplier(businessSystem);
                    businessSystem.acceptShipment(selectSupplier(businessSystem));
                    break;
                case 13: //Edit cart
                    businessSystem.editCart(selectClient(businessSystem));
                    break;
                case 14: //Get complete product list
                    System.out.println(businessSystem.getCompleteProductList());
                    break;
                case 15: //Get Client transactions
                    businessSystem.getClientTransactions(selectClient(businessSystem));
                    break;
                case -1: // New case to redisplay menu for user
                    System.out.println(menu);
                    break;
            
                default:
                    System.out.println("ERROR: Invalid input.");
                    scanner.nextLine();
                    break;
            } //End of switch statement
        } //End of While Loop
    } // End of main()
} //End of UI class
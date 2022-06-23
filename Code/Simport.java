//This class is used to import system states.

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/*
   ----------------TEST FILE FORMATTING INFO----------------
    General Syntax:
        TYPE PARAMETERS
        note: Spaces in names must be represented by a _
    Supplier Formatting + Example:
        SUPPLIER NAME
        ie. SUPPLIER Johnsons_Supplies
        ie. SUPPLIER SupplyCo
    Product Formatting + Example
        PRODUCT NAME TARGETSUPPLIER PRICE QUANTITY
        ie. PRODUCT Ice_Cream SupplyCo 6.50 200
        ie. PRODUCT Screwdriver Johnsons_Supplies 2.75 368
        ie. PRODUCT Laptop SupplyCo 350 25
    Client Formatting + Example
        CLIENT BALANCE
        ie. CLIENT 200
        ie. CLIENT 189.25
        
*/

public class Simport {
    /**
     * Imports a test file, to help initalize a system faster.
     * @param file File to load
     * @param f Facade to load into
     * @return boolean, true if sucessful otherwise false.
     */
    public boolean importTestFile(String file, Facade f)
    {
        try {
            Scanner scanner = new Scanner (new File(file));
            String buffer = new String();
            Product tempProduct = new Product();
            String tempSuppName = new String();
            ArrayList<String> productNames = new ArrayList<String>();
            ArrayList<String> productIDs = new ArrayList<String>();

            while(scanner.hasNextLine())
            {
                buffer = scanner.next();
                //System.out.println(buffer);
                if(buffer.contentEquals("SUPPLIER"))
                {
                    f.addSupplier(scanner.next().replace('_', ' ')); //Using replace to replace underscored with spaces, so spaced named can be specified.
                    //System.out.println("Adding supplier");
                }
                else if(buffer.contentEquals("PRODUCT"))
                {
                    tempProduct.setName(scanner.next().replace('_', ' '));
                    if(productNames.contains(tempProduct.getName()))
                    {
                        int index = productNames.indexOf(tempProduct.getName());
                        tempProduct = f.copyProduct(productIDs.get(index));
                    }
                    else
                    {
                        productNames.add(tempProduct.name);
                        productIDs.add(tempProduct.getID());
                    }
                    tempSuppName = scanner.next().replace('_', ' ');
                    tempProduct.setPrice(scanner.nextFloat());
                    tempProduct.setQuantity(scanner.nextInt());
                    //System.out.println(tempProduct.toString());
                    //System.out.println("Target supplier: " + tempSuppName + " who has id " + f.getSupplierByName(tempSuppName));
                    if( f.addProductToSupplier( tempProduct, f.getSupplierByName(tempSuppName) ) == false)
                    {
                        System.out.println("ERROR: Could not add product " + tempProduct.toString() + " to " + tempSuppName + ". Target supplier likely does not exist. Make sure the supplier is defined before the product in the importer text file.");
                    }
                    //System.out.println("Adding product");
                    tempProduct = new Product();
                }
                else if(buffer.contentEquals("CLIENT"))
                {
                    f.addClient(scanner.nextDouble());
                    //System.out.println("Adding client");
                }
                if(scanner.hasNextLine()) {
                    scanner.nextLine();
                }
            }
            scanner.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    } //End of ImportTestFile();

    /**
     * Imports a serializable file to the facade.
     * @param file File to manipulate
     * @param f The facade to import to
     * @return boolean, true if sucessful otherwise false.
     */
    public boolean serImport(String file, Facade f) {
        try 
        {
            FileInputStream fIn = new FileInputStream("./" + file);
            ObjectInputStream in = new ObjectInputStream(fIn);
            f = (Facade)in.readObject();
            in.close();
            fIn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    } //end of serImport()

    /**
     * Exports a serializable file from the facade.
     * @param file File to manipulate
     * @param f The facade to export from
     * @return boolean, true if sucessful otherwise false.
     */
    public boolean serExport(String file, Facade f) {
        try 
        {
            FileOutputStream fOut = new FileOutputStream("./" + file);
            ObjectOutputStream out = new ObjectOutputStream(fOut);
            out.writeObject(f);
            out.close();
            fOut.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    } //end of serExport()
}
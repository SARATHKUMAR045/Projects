//Medicine Class

import java.util.Date;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Admin Access
class Admin {
    private String userName;
    private String passWord;
    private static final String FILE_PATH = "admin_credentials.txt";

    public Admin(String username, String password) {
        this.userName = username;
        this.passWord = password;
    }

    public Admin() {
        loadCredentials();

        if (userName == null || passWord == null) {
            setCredentials();
        }
    }

    // Save a Admin Credentials
    public void saveCredentials(String username, String password) {
        try (FileWriter writer = new FileWriter(FILE_PATH, false)) {
            writer.write(username + "\n" + password);
            System.out.println("Credentials Saved Successfully");
        } catch (IOException e) {
            System.out.println("Error to save a Admin Credentials");
            e.printStackTrace();
        }
    }

    // load a admin credentials from file
    public void loadCredentials() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            this.userName = reader.readLine();
            this.passWord = reader.readLine();
        } catch (FileNotFoundException e) {
            System.out.println("Admin credentials not found");
        } catch (IOException e) {
            System.out.println("Error reading admin credentials");
        }
    }

    // set Credentials
    private void setCredentials() {
        Scanner input = new Scanner(System.in);
        System.out.println("Credentials is not yet set Please enter a Admin Username: ");
        this.userName = input.nextLine();
        System.out.println("Please Enter the Admin Password: ");
        this.passWord = input.nextLine();
        input.close();

        saveCredentials(this.userName, this.passWord);
    }

    public boolean login(String username, String password) {
        if (this.userName == null || this.passWord == null) {
            System.out.println("Admin creditdentials not set!");
            return false;
        }
        return this.userName.equals(username) && this.passWord.equals(password);
    }

}

class Medicine {
    private String name;
    private String batchNumber;
    private int quantity;
    private double price;
    private Date expiryDate;

    public Medicine(String name, String batchnumber, int quantity, double price, Date expiryDate) {
        this.name = name;
        this.batchNumber = batchnumber;
        this.quantity = quantity;
        this.price = price;
        this.expiryDate = expiryDate;
    }

    public String getName() {
        return name;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return "Name: " + name + ", Batch: " + batchNumber + ", Quantity: " + quantity + ", Price: ₹ " + price
                + "Expiry Date: " + dateFormat.format(expiryDate);
    }
}

// Manage Inventory
class Inventory {
    private HashMap<String, Medicine> medicineInventory;

    public Inventory() {
        medicineInventory = new HashMap<>();
    }

    // Add a Medicine to Inventory
    public void addMedicine(Medicine newMedicine) {
        if (newMedicine != null) {
            medicineInventory.put(newMedicine.getBatchNumber(), newMedicine);
            System.out.print("Medicine added Successfully");
        } else {
            System.out.println("Error: Medicine object is null");
        }

    }

    // Remove Medicine
    public void removeMedicine(String batchNumber) {
        if (medicineInventory.containsKey(batchNumber)) {
            medicineInventory.remove(batchNumber);
            System.out.println("Removed Medicine from inventory");
        } else {
            System.out.println("Medicine not found");
        }
    }

    // Update Quantity of Medicine
    public void updateQuantity(String batchNumber, int newquantity) {
        if (medicineInventory.containsKey(batchNumber)) {
            Medicine existingMedicine = medicineInventory.get(batchNumber);
            existingMedicine.setQuantity(newquantity);
            System.out.println("Quantity Updated");
        } else {
            System.out.println("Medicine Not Found");
        }
    }

    // Get medicine batchnumber
    public Medicine getMedicine(String batchNumber) {
        return medicineInventory.get(batchNumber);
    }

    // Search Medicine
    public List<Medicine> searchByNameOrBatch(String keyword) {
        List<Medicine> results = new ArrayList<>();
        for (Medicine med : medicineInventory.values()) {
            if (med.getName().equalsIgnoreCase(keyword) ||
                    med.getBatchNumber().equalsIgnoreCase(keyword)) {
                results.add(med);
            }

        }
        return results;
    }

    // Display all medicines
    public void displayMedicine() {
        if (medicineInventory.isEmpty()) {
            System.out.println("No Medicine available");
            return;
        }
        for (Medicine med : medicineInventory.values()) {
            System.out.println(med);
        }

    }

}

// Sales
class Sale {
    private Inventory inventory;

    public Sale(Inventory inventory) {
        this.inventory = inventory;

    }

    public void processSale(String batchnumber, int quantity) {
        Medicine selectMedicine = inventory.getMedicine(batchnumber);
        if (selectMedicine != null) {
            Date today = new Date();
            if (selectMedicine.getExpiryDate().before(today)) {
                System.out.println("The Medicine is expired. ");
                return;
            }

            if (selectMedicine.getQuantity() >= quantity) {
                double total = selectMedicine.getPrice() * quantity;
                selectMedicine.setQuantity(selectMedicine.getQuantity() - quantity);
                System.out.println("Purchase Successfully done");

                // Print Bill
                PurchaseBill bill = new PurchaseBill(selectMedicine, quantity, total);
                bill.saveToFile();

            } else {
                System.out.println("Purchase failed insufficient stock. ");
            }

        } else {
            System.out.println("Medicine Not found");
        }
    }
}

// Purchase bill
class PurchaseBill {
    private Medicine medicine;
    private int quantityPurchased;
    private double totalAmount;
    private Date purchaseDate;

    public PurchaseBill(Medicine medicine, int quantityPurchased, double totalAmount) {
        this.medicine = medicine;
        this.quantityPurchased = quantityPurchased;
        this.totalAmount = totalAmount;
        this.purchaseDate = new Date();

    }

    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return "Purchase Bill" +
                "---------------------\n" +
                "Medicine: " + medicine.getName() + "\n" +
                "Batch: " + medicine.getBatchNumber() + "\n" +
                "Quantity Purchased: " + quantityPurchased + "\n" +
                "Price per Unit: " + String.format("%.2f", medicine.getPrice()) + "\n" +
                "Total Amount: " + String.format("%.2f", totalAmount) + "\n" +
                "Purchase Date: " + dateFormat.format(purchaseDate) + "\n";

    }

    public void saveToFile() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
        String timestamp = dateFormat.format(purchaseDate);
        String fileName = "Bill_" + timestamp + ".txt";
        String PurchaseBill = "Purchase Bill" +
                "---------------------\n" +
                "Medicine: " + medicine.getName() + "\n" +
                "Batch: " + medicine.getBatchNumber() + "\n" +
                "Quantity Purchased: " + quantityPurchased + "\n" +
                "Price per Unit: " + String.format("%.2f", medicine.getPrice()) + "\n" +
                "Total Amount: " + String.format("%.2f", totalAmount) + "\n" +
                "Purchase Date: " + dateFormat.format(purchaseDate) + "\n";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(PurchaseBill);
            System.out.println("Bill saved successfully");
        } catch (IOException e) {
            System.out.println("An error occurred while saving this bill");
            e.printStackTrace();
        }
    }

}

public class MediStock_Navigator {
    private static Inventory inventory = new Inventory();
    private static Admin admin;

    public static void main(String[] args) {
        admin = new Admin();
        Scanner input = new Scanner(System.in);

        // Admin login
        System.out.println("Enter the Admin Username");
        String username = input.nextLine();
        System.out.println("Enter your password");
        String password = input.nextLine();

        if (admin.login(username, password)) {
            System.out.println("Login Succssfully");
            mainMenu(input);
        } else {
            System.out.println("Invalid credentials!");
        }
        input.close();
    }

    private static void mainMenu(Scanner input) {
        while (true) {
            System.out.println("Welcome to SA PHARMACY");
            System.out.println("1. Add Medicine");
            System.out.println("2. Remove Medicine");
            System.out.println("3. Update Medicine Quantity");
            System.out.println("4. Display Medicine");
            System.out.println("5. Process Sale");
            System.out.println("6. Exit");
            System.out.print("Select a option");

            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    addMedicine(input);
                    break;
                case 2:
                    removeMedicine(input);
                    break;
                case 3:
                    updateQuantity(input);
                    break;
                case 4:
                    inventory.displayMedicine();
                    break;
                case 5:
                    processSale(input);
                    break;
                case 6:
                    System.out.print("Exiting...");
                    return;
                default:
                    System.out.println("Invalid Input Please Try again");
            }

        }

    }

    private static void addMedicine(Scanner input) {
        System.out.println("Enter Medicine Name: ");
        String name = input.nextLine();

        System.out.println("Enter Batch Number: ");
        String Batch = input.nextLine();

        System.out.println("Enter Quantity: ");
        int quantity = input.nextInt();
        input.nextLine();

        System.out.println("Enter the Price: ");
        double price = input.nextDouble();
        input.nextLine();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);
        Date expriyDate = null;
        boolean valiDate = false;

        while (!valiDate) {
            System.out.println("Enter the expiry date (dd-MM-yyyy)");
            String ExpiryDatestr = input.nextLine();

            try {
                expriyDate = dateFormat.parse(ExpiryDatestr);
                valiDate = true;
            } catch (ParseException e) {
                System.out.println("Invalid date format: please use this format- dd-MM-yyyy");
            }
            System.out.println("Valid Expiry Date Entered: " + dateFormat.format(expriyDate));
        }

        Medicine medicine = new Medicine(name, Batch, quantity, price, expriyDate);
        inventory.addMedicine(medicine);
    }

    private static void removeMedicine(Scanner input) {
        System.out.println("Enter the batch number to remove: ");
        String batchNumber = input.nextLine();
        inventory.removeMedicine(batchNumber);
    }

    private static void updateQuantity(Scanner input) {
        System.out.println("Enter batch number to update: ");
        String batchNumber = input.nextLine();

        System.out.println("Enter the quantity: ");
        int newQuantity = input.nextInt();

        inventory.updateQuantity(batchNumber, newQuantity);
    }

    private static void processSale(Scanner input) {
        System.out.println("Enter the batch number for sale: ");
        String batchNumber = input.nextLine();

        System.out.println("Enter the quantity: ");
        int quantity = input.nextInt();

        Sale sale = new Sale(inventory);
        sale.processSale(batchNumber, quantity);
    }

}

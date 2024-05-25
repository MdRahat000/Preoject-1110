import java.io.*;
import java.util.Scanner;

public class Main {
    static final String USERNAME = "rahat";
    static final String PASSWORD = "1110";
    static final int MAX = 20;

    static class Item implements Serializable {
        String productCode;
        String productName;
        int rate;
        int quantity;
        float weight;
        String description;
    }

    static Item item = new Item();

    public static void main(String[] args) {
        login();
    }

    static boolean isCodeAvailable(String code) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Record.txt"))) {
            while (true) {
                Item tempItem = (Item) ois.readObject();
                if (code.equals(tempItem.productCode)) {
                    return true;
                }
            }
        } catch (EOFException e) {
           
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    static boolean isProductAvailable(int quantity) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Record.txt"))) {
            while (true) {
                Item tempItem = (Item) ois.readObject();
                if (tempItem.quantity >= quantity) {
                    return true;
                }
            }
        } catch (EOFException e) {
            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    static int getInt() {
        Scanner scanner = new Scanner(System.in);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.println("\033[1;31m");
            System.out.println("\n\t\t\t\t\t\tMust be positive integer.\n");
            System.out.println("\033[0m");
            System.out.print("\t\t\t\t\tEnter Positive integer value, such as 1,2,3...: ");
        }
        return scanner.nextInt();
    }

    static int checkRate() {
        Scanner scanner = new Scanner(System.in);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.println("\033[1;31m");
            System.out.println("\n\t\t\t\t\t\tRate must be positive Integer.\n");
            System.out.println("\033[0m");
            System.out.print("\t\t\t\t\tEnter rate of the product in positive integer: ");
        }
        return scanner.nextInt();
    }

    static void addProduct() {
        System.out.println("\n\t\t\t\t\t\t\tAdd Product");
        System.out.println("\t\t\t\t\t\t**");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Record.txt", true))) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\n\t\t\t\t\tEnter \"end\" to exit from here");
            System.out.print("\n\t\t\t\t\tEnter Product code: ");
            String code = scanner.next();
            if ("end".equalsIgnoreCase(code)) {
                options();
                return;
            }
            if (isCodeAvailable(code)) {
                System.out.println("\033[1;31m");
                System.out.println("\n\t\t\t\t\t\t* Product is already there.\n");
                System.out.println("\033[0m");
                options();
                return;
            }
            item.productCode = code;

            System.out.print("\t\t\t\t\tEnter Product Name: ");
            item.productName = scanner.next();
            System.out.print("\n\t\t\t\t\tEnter Product Rate: ");
            item.rate = checkRate();
            System.out.print("\n\t\t\t\t\tEnter Quantity: ");
            item.quantity = getInt();
            System.out.print("\n\t\t\t\t\tEnter product Weight(in grams): ");
            item.weight = scanner.nextFloat();
            System.out.print("\n\t\t\t\t\tEnter product descriptions: ");
            item.description = scanner.next();

            oos.writeObject(item);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void display() {
        System.out.println("\n\t\t\t\t\t\t\tAvailable Products");
        System.out.println("\t\t\t\t\t\t*");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Record.txt"))) {
            System.out.println("\t\t\t------------------------------------------------------------------------------------------------------");
            System.out.println("\t\t\tCODE\t||\tNAME\t||\tRATE\t||\tQUANTITY\t||\tWEIGHT\t||\tDESCRIPTION");
            System.out.println("\t\t\t------------------------------------------------------------------------------------------------------");
            int count = 0;
            while (true) {
                Item tempItem = (Item) ois.readObject();
                System.out.printf("\t\t\t%s\t||\t%s\t||\t%d\t||\t%d\t\t||\t%.2f\t||\t%s \n",
                        tempItem.productCode, tempItem.productName, tempItem.rate,
                        tempItem.quantity, tempItem.weight, tempItem.description);
                count++;
            }
        } catch (EOFException e) {
           
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static void closeApp() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\n Do you want to close the application? (Y/y) ");
        String choice = scanner.next();
        if ("Y".equalsIgnoreCase(choice)) {
            System.exit(0);
        }
    }

    static void search() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Record.txt"))) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\n\t\t\t\t\tEnter \"end\" to go back to menu.");
            System.out.print("\n\t\t\t\t\tEnter the Product code to search: ");
            String code = scanner.next();
            if ("end".equalsIgnoreCase(code)) {
                options();
                return;
            }
            System.out.println("\n\t\t\t\t\t\tProduct information");
            System.out.println("\t\t\t\t\t\t**");
            if (!isCodeAvailable(code)) {
                System.out.println("\033[1;31m");
                System.out.println("\n\t\t\t\t\t\tProduct code not found.\n");
                System.out.println("\033[0m");
                return;
            }
            while (true) {
                Item tempItem = (Item) ois.readObject();
                if (code.equals(tempItem.productCode)) {
                    System.out.printf("\n\t\t\t\t\t\tProduct Code:        %s\n", tempItem.productCode);
                    System.out.printf("\n\t\t\t\t\t\tName of Product:     %s\n", tempItem.productName);
                    System.out.printf("\n\t\t\t\t\t\tRate of Product(RS): %d\n", tempItem.rate);
                    System.out.printf("\n\t\t\t\t\t\tProduct Weight:      %.2f\n", tempItem.weight);
                    System.out.printf("\n\t\t\t\t\t\tProduct Description: %s\n", tempItem.description);
                    break;
                }
            }
        } catch (EOFException e) {
           
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static void deleteRecord() {
        System.out.println("\n\t\t\t\t\t\t\tDelete Product");
        display();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Record.txt"));
             ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("tempfile.txt"))) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("\n\t\t\t\t\t\tEnter the Product code to delete: ");
            String code = scanner.next();
            if (!isCodeAvailable(code)) {
                System.out.println("\033[1;31m");
                System.out.println("\n\t\t\t\t\t\t* Product is not available.\n");
                System.out.println("\033[0m");
                return;
            }
            while (true) {
                try {
                    Item tempItem = (Item) ois.readObject();
                    if (!code.equals(tempItem.productCode)) {
                        oos.writeObject(tempItem);
                    }
                } catch (EOFException e) {
                    break;
                }
            }
            File oldFile = new File("Record.txt");
            oldFile.delete();
            File newFile = new File("tempfile.txt");
            newFile.renameTo(oldFile);
            System.out.println("\n\t\t\t\t\t\tProduct deleted successfully!!\n\n");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static void updateProduct() {
        System.out.println("\n\t\t\t\t\t\t\tUpdate Product");
        System.out.println("\t\t\t\t\t\t**");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Product code to update the record: ");
        String code = scanner.next();
        boolean found = false;
        try (RandomAccessFile file = new RandomAccessFile("Record.txt", "rw")) {
            while (true) {
                long pos = file.getFilePointer();
                try {
                    item = (Item) new ObjectInputStream(new FileInputStream(file.getFD())).readObject();
                    if (code.equals(item.productCode)) {
                        found = true;
                        System.out.println("\n Updating data for the previous product " + code);
                        System.out.print("Enter Product Name: ");
                        item.productName = scanner.next();
                        System.out.print("Enter Product Rate: ");
                        item.rate = checkRate();
                        System.out.print("Enter Quantity: ");
                        item.quantity = getInt();
                        System.out.print("Enter weight: ");
                        item.weight = scanner.nextFloat();
                        System.out.print("Enter product descriptions: ");
                        item.description = scanner.next();
                        file.seek(pos);
                        new ObjectOutputStream(new FileOutputStream(file.getFD())).writeObject(item);
                        System.out.println("\n\t\t\t\t\t\tProduct updated successfully!!\n\n");
                        break;
                    }
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (!found) {
            System.out.println("\033[1;31m");
            System.out.println("\n\t\t\t\t\t\t* No Product is found for update.\n");
            System.out.println("\033[0m");
        }
    }

    static void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\t\t\t\t\t\t\tLogin ");
        System.out.println("\t\t\t\t\t\t**");
        while (true) {
            System.out.print("\n\t\t\tEnter username: ");
            String username = scanner.next();
            System.out.print("\t\t\tEnter password: ");
            String password = scanner.next();
            if (USERNAME.equals(username) && PASSWORD.equals(password)) {
                System.out.println("\033[1;32m");
                System.out.println("\t\t\t\t\tLogin successfully!!");
                System.out.println("\033[0m");
                options();
                break;
            } else {
                System.out.println("\033[1;31m");
                System.out.println("\n\t\t\tSorry, you entered the wrong information.");
                System.out.println("\n\t\t\tPlease try again.");
                System.out.println("\033[0m");
            }
        }
    }

    static void saleProduct() {
        System.out.println("\n\t\t\t\t\t\t\tSale Product");
        System.out.println("\t\t\t\t\t\t**");
        Scanner scanner = new Scanner(System.in);
        int gtotal = 0;
        try (RandomAccessFile file = new RandomAccessFile("Record.txt", "rw")) {
            System.out.println("\t\t\t\t\tEnter \"end\" to finish input");
            while (true) {
                System.out.print("\n\t\t\t\tEnter Item Code: ");
                String x = scanner.next();
                if ("end".equalsIgnoreCase(x)) {
                    break;
                }
                if (!isCodeAvailable(x)) {
                    System.out.println("\033[1;31m");
                    System.out.println("\n\t\t\t\t\t\t* No Product is found.\n");
                    System.out.println("\033[0m");
                    continue;
                }
                System.out.print("\t\t\t\tEnter Quantity: ");
                int q = getInt();
                boolean updated = false;
                while (true) {
                    long pos = file.getFilePointer();
                    try {
                        item = (Item) new ObjectInputStream(new FileInputStream(file.getFD())).readObject();
                        if (x.equals(item.productCode)) {
                            if (item.quantity >= q) {
                                item.quantity -= q;
                                int total = item.rate * q;
                                gtotal += total;
                                System.out.printf("\n\t\t\t%s %s %d %d %d\n", x, item.productName, q, item.rate, total);
                                file.seek(pos);
                                new ObjectOutputStream(new FileOutputStream(file.getFD())).writeObject(item);
                                updated = true;
                                break;
                            } else {
                                System.out.println("\033[1;31m");
                                System.out.println("\n\t\t\t\t\t\t* Out of stock.\n");
                                System.out.println("\033[0m");
                                break;
                            }
                        }
                    } catch (EOFException e) {
                        break;
                    }
                }
                if (!updated) {
                    System.out.println("\033[1;31m");
                    System.out.println("\n\t\t\t\t\t\t* Update failed.\n");
                    System.out.println("\033[0m");
                }
            }
            if (gtotal != 0) {
                System.out.printf("\n\t\t\t\t\t\tTOTAL AMOUNT = NRs. %d", gtotal);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static void options() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\n\t\t\t\t\t\t\t\t--Welcome to RAHAT SuperStore--");
        System.out.println("\t\t\t\t\t\t\t\t**");
        while (true) {
            System.out.println("\n\t\t\t\t\t\t\t\t\t1. Add product");
            System.out.println("\n\t\t\t\t\t\t\t\t\t2. Display");
            System.out.println("\n\t\t\t\t\t\t\t\t\t3. Search");
            System.out.println("\n\t\t\t\t\t\t\t\t\t4. Delete");
            System.out.println("\n\t\t\t\t\t\t\t\t\t5. Update");
            System.out.println("\n\t\t\t\t\t\t\t\t\t6. Close");
            System.out.println("\n\t\t\t\t\t\t\t\t\t7. Sale product");
            System.out.print("\n\t\t\t\t\t\t\t\t\tEnter your choice: ");
            int choice = getInt();
            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    display();
                    break;
                case 3:
                    search();
                    break;
                case 4:
                    deleteRecord();
                    break;
                case 5:
                    updateProduct();
                    break;
                case 6:
                    closeApp();
                    break;
                case 7:
                    saleProduct();
                    break;
                default:
                    System.out.println("\033[1;31m");
                    System.out.println("\t\t* Invalid choice.\n");
                    System.out.println("\033[0m");
                    break;
            }
        }
    }
}
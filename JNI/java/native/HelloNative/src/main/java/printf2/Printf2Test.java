package printf2;

import java.util.Scanner;

public class Printf2Test {

    public static void main(String[] args) {
        double price, tax;
        Scanner sc = new Scanner(System.in);

        price = enterValue(sc, "Enter price: ");
        tax = enterValue(sc, "Enter tax: ");

        double amountDue = price * (1 + tax / 100);

        String s = Printf2.sprint("Amount due = %8.2f", amountDue);
        System.out.println(s);
    }

    private static double enterValue(Scanner sc, String msg) {
        double enteredValue = 0.0;
        boolean repeatInput;
        do {
            repeatInput = false;
            System.out.print(msg);
            String strVal = sc.nextLine();
            try {
                enteredValue = Double.parseDouble(strVal);
            } catch (Exception ignore) {
                System.out.println("Incorrect double number was entered. Please try again.");
                repeatInput = true;
            }
        } while (repeatInput);
        System.out.print(msg);
        return enteredValue;
    }
}

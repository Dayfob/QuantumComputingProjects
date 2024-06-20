package edu.pacific.cs;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        // user input:
        System.out.println("Factor Calculation!");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a number:");
        int number = scanner.nextInt();

        // calculation and output of factors
        System.out.println("The factors of " + number + " are:");
        for (int i = 1; i <= number; ++i) {
            if (number % i == 0) {
                System.out.println(i);
            }
        }
    }
}

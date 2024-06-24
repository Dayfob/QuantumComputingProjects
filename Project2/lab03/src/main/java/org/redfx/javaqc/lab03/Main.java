package org.redfx.javaqc.lab03;
import java.util.Scanner;

import org.redfx.strange.algorithm.Classic;

public class Main {
    public static void main(String[] args) {
        // user input:
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the first number:");
        int number1 = scanner.nextInt();
        System.out.println("Enter the second number:");
        int number2 = scanner.nextInt();

        // calculation:
        int result = Classic.qsum(number1, number2);

        // output:
        System.out.println("Sum: " + result);
    }
}

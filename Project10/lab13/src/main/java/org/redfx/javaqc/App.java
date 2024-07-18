package org.redfx.javaqc;

import java.util.Scanner;

import org.redfx.strange.*;
import org.redfx.strange.gate.*;
import org.redfx.strange.local.*;
import org.redfx.strangefx.render.*;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select Oracle (type 1, 2, 3, 4 or 5):");
        int circuit = scanner.nextInt();

        if (circuit == 1) {
            oneInputFunction(1);
        } else if (circuit == 2) {
            oneInputFunction(2);
        } else if (circuit == 3) {
            oneInputFunction(3);
        } else if (circuit == 4) {
            oneInputFunction(4);
        } else if (circuit == 5) {
            for (int a = 0; a < 2; a++) {
                for (int x2 = 0; x2 < 2; x2++) {
                    for (int x1 = 0; x1 < 2; x1++) {
                        twoInputFunction(x1, x2, a);
                    }
                }
            }
        } else {
            System.out.println("Incorrect Oracle number!");
        }
    }

    static Oracle createOracle1() { //f1(x)
        Complex[][] matrix = new Complex[4][4];

        matrix[0][0] = Complex.ONE;
        matrix[1][1] = Complex.ONE;
        matrix[2][2] = Complex.ONE;
        matrix[3][3] = Complex.ONE;

        return new Oracle(matrix);
    }

    static Oracle createOracle2() { //f2(x)
        Complex[][] matrix = new Complex[4][4];

        matrix[0][0] = Complex.ONE;
        matrix[1][3] = Complex.ONE;
        matrix[2][2] = Complex.ONE;
        matrix[3][1] = Complex.ONE;

        return new Oracle(matrix);
    }

    static Oracle createOracle3() { //f3(x)
        Complex[][] matrix = new Complex[4][4];

        matrix[0][2] = Complex.ONE;
        matrix[1][1] = Complex.ONE;
        matrix[2][0] = Complex.ONE;
        matrix[3][3] = Complex.ONE;

        return new Oracle(matrix);
    }

    static Oracle createOracle4() { //f4(x)
        Complex[][] matrix = new Complex[4][4];

        matrix[0][2] = Complex.ONE;
        matrix[1][3] = Complex.ONE;
        matrix[2][0] = Complex.ONE;
        matrix[3][1] = Complex.ONE;

        return new Oracle(matrix);
    }

    static Oracle createOracle5() { //f(x1,x2)
        Complex[][] matrix = new Complex[8][8];

        matrix[0][4] = Complex.ONE;
        matrix[1][5] = Complex.ONE;
        matrix[2][2] = Complex.ONE;
        matrix[3][3] = Complex.ONE;
        matrix[4][0] = Complex.ONE;
        matrix[5][1] = Complex.ONE;
        matrix[6][6] = Complex.ONE;
        matrix[7][7] = Complex.ONE;

        return new Oracle(matrix);
    }

    static void oneInputFunction(int circuit) {
        QuantumExecutionEnvironment simulator = new SimpleQuantumExecutionEnvironment();
        Program program = new Program(2);

        Step step = new Step();
        if (circuit == 1) {
            Oracle oracle = createOracle1();
            step.addGate(oracle);
        } else if (circuit == 2) {
            Oracle oracle = createOracle2();
            step.addGate(oracle);
        } else if (circuit == 3) {
            Oracle oracle = createOracle3();
            step.addGate(oracle);
        } else if (circuit == 4) {
            Oracle oracle = createOracle4();
            step.addGate(oracle);
        } else if (circuit == 5) {
            Oracle oracle = createOracle4();
            step.addGate(oracle);
        }
        program.addStep(step);

        Result result = simulator.runProgram(program);
        Qubit[] qubits = result.getQubits();
        System.out.println("00 => " + qubits[1].measure() + "" + qubits[0].measure());
        Renderer.renderProgram(program);
    }

    static void twoInputFunction(int x1, int x2, int a) {
        QuantumExecutionEnvironment simulator = new SimpleQuantumExecutionEnvironment();
        Program program = new Program(3);

        Step init = new Step();
        if (x1 == 1) {
            init.addGate(new X(0));
        }
        if (x2 == 1) {
            init.addGate(new X(1));
        }
        if (a == 1) {
            init.addGate(new X(2));
        }

        Step step = new Step();
        Oracle oracle = createOracle5();
        step.addGate(oracle);

        program.addSteps(init, step);

        Result result = simulator.runProgram(program);
        Qubit[] qubits = result.getQubits();
        if ((a == 0 && x2 == 0 && x1 == 0) || (a == 1 && x2 == 1 && x1 == 0)) {
            Renderer.renderProgram(program);
        }
        System.out.println(a + "" + x2 + "" + x1 + " => " +
                qubits[2].measure() + "" + qubits[1].measure() + "" + qubits[0].measure());
    }
}

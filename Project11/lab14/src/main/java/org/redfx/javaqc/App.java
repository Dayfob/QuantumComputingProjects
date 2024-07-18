package org.redfx.javaqc;

import java.util.Scanner;

import org.redfx.strange.*;
import org.redfx.strange.gate.*;
import org.redfx.strange.local.*;
import org.redfx.strangefx.render.*;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select 1-4 Deutsch algorithm or 5 Deutsch-Jozsa (type 1, 2, 3, 4 or 5):");
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
            twoInputFunction();
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
        matrix[1][1] = Complex.ONE;
        matrix[2][2] = Complex.ONE;
        matrix[3][3] = Complex.ONE;
        matrix[4][0] = Complex.ONE;
        matrix[5][5] = Complex.ONE;
        matrix[6][6] = Complex.ONE;
        matrix[7][7] = Complex.ONE;

        return new Oracle(matrix);
    }

    static void oneInputFunction(int circuit) { // Deutsch algorithm
        QuantumExecutionEnvironment simulator = new SimpleQuantumExecutionEnvironment();
        Program program = new Program(2);

        Step step0 = new Step(new X(1));
        Step step1 = new Step(new Hadamard(0), new Hadamard(1));
        Step step2 = new Step(new Hadamard(0));

        Step stepOracle = new Step();
        if (circuit == 1) {
            stepOracle.addGate(createOracle1());
        } else if (circuit == 2) {
            stepOracle.addGate(createOracle2());
        } else if (circuit == 3) {
            stepOracle.addGate(createOracle3());
        } else if (circuit == 4) {
            stepOracle.addGate(createOracle4());
        }
        program.addSteps(step0, step1, stepOracle, step2);

        Result result = simulator.runProgram(program);
        Qubit[] qubits = result.getQubits();
        System.out.println(qubits[0].measure() == 0 ? "constant" : "balanced");
        Renderer.renderProgram(program);
    }

    static void twoInputFunction() { // Deutsch-Jozsa algorithm
        QuantumExecutionEnvironment simulator = new SimpleQuantumExecutionEnvironment();
        Program program = new Program(3);

        Step step0 = new Step(new X(2));
        Step step1 = new Step(new Hadamard(0), new Hadamard(1), new Hadamard(2));
        Step step2 = new Step(new Hadamard(0), new Hadamard(1));
        Step stepOracle = new Step(createOracle5());

        program.addSteps(step0, step1, stepOracle, step2);

        simulator.runProgram(program);
        Renderer.renderProgram(program);
    }
}

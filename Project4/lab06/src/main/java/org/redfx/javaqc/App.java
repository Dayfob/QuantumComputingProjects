package org.redfx.javaqc;

import java.util.Scanner;

import org.redfx.strange.*;
import org.redfx.strange.gate.*;
import org.redfx.strange.local.*;
import org.redfx.strangefx.render.Renderer;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select quantum circuit (type 0 or 1):");
        int circuit = scanner.nextInt();

        if (circuit == 0) {
            // the input is qubit 0
            QuantumExecutionEnvironment simulator = new SimpleQuantumExecutionEnvironment();
            Program program = new Program(1);

            Step step1 = new Step();
            Step step2 = new Step();
            step1.addGate(new Hadamard(0));
            step2.addGate(new Hadamard(0));

            program.addStep(step1);
            program.addStep(step2);

            Result result = simulator.runProgram(program);
            Qubit[] qubits = result.getQubits();
            Qubit zero = qubits[0];
            int value = zero.measure();

            System.out.println("Value is = " + value);
            Renderer.renderProgram(program);
        } else if (circuit == 1) {
            // the input is qubit 1
            QuantumExecutionEnvironment simulator = new SimpleQuantumExecutionEnvironment();
            Program program = new Program(1);

            Step step1 = new Step();
            Step step2 = new Step();
            Step step3 = new Step();
            step1.addGate(new X(0));
            step2.addGate(new Hadamard(0));
            step3.addGate(new Hadamard(0));

            program.addStep(step1);
            program.addStep(step2);
            program.addStep(step3);

            Result result = simulator.runProgram(program);
            Qubit[] qubits = result.getQubits();
            Qubit zero = qubits[0];
            int value = zero.measure();

            System.out.println("Value is = " + value);
            Renderer.renderProgram(program);
        } else {
            System.out.println("Incorrect quantum circuit number!");
        }
    }
}

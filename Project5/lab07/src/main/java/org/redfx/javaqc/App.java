package org.redfx.javaqc;

import java.util.Scanner;

import org.redfx.strange.*;
import org.redfx.strange.gate.*;
import org.redfx.strange.local.*;
import org.redfx.strangefx.render.Renderer;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select quantum circuit (type 0, 1 or 2):");
        int circuit = scanner.nextInt();

        QuantumExecutionEnvironment simulator = new SimpleQuantumExecutionEnvironment();
        Program program = new Program(2);

        Step step1 = new Step();
        Step step2 = new Step();
        Step step3 = new Step();

        if (circuit == 0) {
            // the input is 01
            System.out.println("The input is 01");
            step1.addGate(new X(1));
        } else if (circuit == 1) {
            // the input is 10
            System.out.println("The input is 10");
            step1.addGate(new X(0));
        } else if (circuit == 2) {
            // the input is 11
            System.out.println("The input is 11");
            step1.addGate(new X(0));
            step1.addGate(new X(1));
        } else {
            System.out.println("Incorrect quantum circuit number!");
            return;
        }

        step2.addGate(new Hadamard(0));
        step3.addGate(new Cnot(0,1));

        program.addStep(step1);
        program.addStep(step2);
        program.addStep(step3);

        simulator.runProgram(program);
        Renderer.renderProgram(program);
        Renderer.showProbabilities(program, 10000);
    }
}

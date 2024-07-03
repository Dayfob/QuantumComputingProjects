package org.redfx.javaqc;

import java.util.Scanner;

import org.redfx.strange.*;
import org.redfx.strange.gate.*;
import org.redfx.strange.local.*;
import org.redfx.strangefx.render.Renderer;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select quantum circuit (type 1, 2 or 3):");
        int circuit = scanner.nextInt();

        QuantumExecutionEnvironment simulator = new SimpleQuantumExecutionEnvironment();
        Program program = new Program(2);

        Step step1 = new Step();
        Step step2 = new Step();

        if (circuit == 1) {
            // question 1
            System.out.println("Circuit 1:");

            step1.addGate(new Hadamard(0));
            step2.addGate(new Cz(0,1));

            program.addStep(step1);
            program.addStep(step2);
        } else if (circuit == 2) {
            // question 2
            System.out.println("Circuit 2:");

            step1.addGate(new Hadamard(0));
            step1.addGate(new X(1));
            step2.addGate(new Cz(0,1));

            program.addStep(step1);
            program.addStep(step2);
        } else if (circuit == 3) {
            // question 3
            System.out.println("Circuit 3:");

            Step step3 = new Step();
            step1.addGate(new Hadamard(0));
            step1.addGate(new X(1));
            step2.addGate(new Cz(0,1));
            step3.addGate(new Measurement(0));
            step3.addGate(new Measurement(1));

            program.addStep(step1);
            program.addStep(step2);
            program.addStep(step3);
        } else {
            System.out.println("Incorrect quantum circuit number!");
            return;
        }

        simulator.runProgram(program);
        Renderer.renderProgram(program);
        Renderer.showProbabilities(program, 1000);
    }
}

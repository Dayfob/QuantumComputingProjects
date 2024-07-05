package org.redfx.javaqc;

import java.util.Scanner;

import org.redfx.strange.*;
import org.redfx.strange.gate.*;
import org.redfx.strange.local.*;
import org.redfx.strangefx.render.Renderer;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select quantum circuit (type 1 or 2):");
        int circuit = scanner.nextInt();

        QuantumExecutionEnvironment simulator = new SimpleQuantumExecutionEnvironment();
        Program program = new Program(3);

        if (circuit == 1) {
            // question 1
            System.out.println("Circuit 1:");

            Step step0 = new Step();
            step0.addGate(new X(0));
            program.addStep(step0);
        } else if (circuit == 2) {
            // question 2
            System.out.println("Circuit 2:");

            Step step0 = new Step();
            step0.addGate(new Hadamard(0));
            program.addStep(step0);
        } else {
            System.out.println("Incorrect quantum circuit number!");
            return;
        }

        Step step1 = new Step();
        step1.addGate(new Hadamard(1));

        Step step2 = new Step();
        step2.addGate(new Cnot(1,2));

        Step step3 = new Step();
        step3.addGate(new Cnot(0,1));

        Step step4 = new Step();
        step4.addGate(new Hadamard(0));

        Step step5 = new Step();
        step5.addGate(new Measurement(0));
        step5.addGate(new Measurement(1));

        Step step6 = new Step();
        step6.addGate(new Cnot(1,2));

        Step step7 = new Step();
        step7.addGate(new Cz(0,2));

        program.addStep(step1);
        program.addStep(step2);
        program.addStep(step3);
        program.addStep(step4);
        program.addStep(step5);
        program.addStep(step6);
        program.addStep(step7);

        simulator.runProgram(program);
        Renderer.renderProgram(program);
        Renderer.showProbabilities(program, 10000);
    }
}

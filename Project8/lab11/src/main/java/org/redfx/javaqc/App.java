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

        if (circuit == 1) {
            int sum = add();
            System.err.println("Adding |Q1> + |Q2> = " + sum);
        } else if (circuit == 2 || circuit == 3) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    if (circuit == 2) {
                        int and = and(i, j);
                        System.err.println(i + " AND " + j + " = " + and);
                    } else if (circuit == 3) {
                        int or = or(i, j);
                        System.err.println(i + " OR " + j + " = " + or);
                    }
                }
            }
        } else {
            System.out.println("Incorrect quantum circuit number!");
            return;
        }
    }

    static int add() {
        QuantumExecutionEnvironment qee = new SimpleQuantumExecutionEnvironment();
        Program program = new Program(3);

        Step superposition = new Step(new Hadamard(0), new Hadamard(1));

        Step step0 = new Step(new Toffoli(0, 1, 2));
        Step step1 = new Step(new Cnot(0, 1));

        Step p0 = new Step(new ProbabilitiesGate(0));
        Step p1 = new Step(new ProbabilitiesGate(0));
        Step p2 = new Step(new ProbabilitiesGate(0));
        Step p3 = new Step(new ProbabilitiesGate(0));

        program.addSteps(p0, superposition, p1, step0, p2, step1, p3);
        Result result = qee.runProgram(program);
        Renderer.renderProgram(program);
        Renderer.showProbabilities(program, 10000);

        Qubit[] qubits = result.getQubits();
        return qubits[1].measure() + (qubits[2].measure() << 1);
    }

    static int and(int a, int b) {
        QuantumExecutionEnvironment qee = new SimpleQuantumExecutionEnvironment();
        Program program = new Program(3);

        Step prep = new Step();
        if (a == 1) prep.addGate(new X(0));
        if (b == 1) prep.addGate(new X(1));

        Step step0 = new Step(new Toffoli(0, 1, 2));

        program.addSteps(prep, step0);
        Result result = qee.runProgram(program);
        Renderer.renderProgram(program);

        Qubit[] qubits = result.getQubits();
        return qubits[2].measure();
    }

    static int or(int a, int b) {
        QuantumExecutionEnvironment qee = new SimpleQuantumExecutionEnvironment();
        Program program = new Program(3);

        Step prep = new Step();
        if (a == 1) prep.addGate(new X(0));
        if (b == 1) prep.addGate(new X(1));

        Step step0 = new Step();
        step0.addGate(new X(0));
        step0.addGate(new X(1));
        Step step1 = new Step(new Toffoli(0, 1, 2));
        Step step2 = new Step(new X(2));

        program.addSteps(prep, step0, step1, step2);
        Result result = qee.runProgram(program);
        Renderer.renderProgram(program);

        Qubit[] qubits = result.getQubits();
        return qubits[2].measure();
    }
}

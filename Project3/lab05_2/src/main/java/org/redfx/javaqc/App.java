package org.redfx.javaqc;

import org.redfx.strange.*;
import org.redfx.strange.gate.*;
import org.redfx.strange.local.*;
import org.redfx.strangefx.render.Renderer;


public class App {
    public static void main(String[] args) {
        QuantumExecutionEnvironment simulator = new SimpleQuantumExecutionEnvironment();
        Program program = new Program(2);

        Step step = new Step();
        step.addGate(new X(0));
        step.addGate(new X(1));
        program.addStep(step);

        Result result = simulator.runProgram(program);
        Qubit[] qubits = result.getQubits();
        Qubit zero = qubits[0];
        int value = zero.measure();
        System.out.println("Value is = " + value);
        Renderer.renderProgram(program);
    }
}

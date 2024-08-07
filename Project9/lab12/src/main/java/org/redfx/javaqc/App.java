package org.redfx.javaqc;

import org.redfx.strange.*;
import org.redfx.strange.gate.*;
import org.redfx.strange.local.*;
import org.redfx.strangefx.render.*;

import java.util.Random;

public class App {
    public static void main(String[] args) {
        // Alice and Bob will exchange SIZE qubits, hence the resulting key will be maximum SIZE bits.
        final int SIZE = 8;
        Random random = new Random();

        boolean[] aliceBits = new boolean[SIZE]; // random bits chosen by Alice
        boolean[] bobBits = new boolean[SIZE]; // bits measured by Bob
        boolean[] aliceBase = new boolean[SIZE]; // random bases chosen by Alice
        boolean[] bobBase = new boolean[SIZE]; // random bases chosen by Bob
        boolean[] noise = new boolean[SIZE]; // communication channels with noise

        QuantumExecutionEnvironment simulator = new SimpleQuantumExecutionEnvironment();
        Program program = new Program(SIZE);
        Step prepareStep = new Step();
        Step superPositionStep = new Step();
        Step noiseStep = new Step(); // Communication channel noise
        Step superPositionStep2 = new Step();
        Step measureStep = new Step();

        for (int i = 0; i < SIZE; i++) {
            aliceBits[i] = random.nextBoolean();
            // if Alice's bit is 1, apply a X gate to the |0> state
            if (aliceBits[i]) prepareStep.addGate(new X(i));
            aliceBase[i] = random.nextBoolean();
            // if Alice's base for this bit is 1, apply a H gate
            if (aliceBase[i]) superPositionStep.addGate(new Hadamard(i));

            // Noise in the communication channel
            if (random.nextInt(10) == 0) {
                noise[i] = true;
                if (random.nextInt(2) == 0) {
                    noiseStep.addGate(new X(i));
                } else {
                    noiseStep.addGate(new Z(i));
                }
            } else {
                noise[i] = false;
            }

            bobBase[i] = random.nextBoolean();
            // if Bob decides to measure in base 1, apply a H gate
            if (bobBase[i]) superPositionStep2.addGate(new Hadamard(i));
            // Finally, Bob measures the result
            measureStep.addGate(new Measurement(i));
        }

        program.addStep(prepareStep);
        program.addStep(superPositionStep);
        program.addStep(noiseStep); // Noise step
        program.addStep(superPositionStep2);
        program.addStep(measureStep);

        Result result = simulator.runProgram(program);
        Qubit[] qubit = result.getQubits();

        int[] measurement = new int[SIZE];
        StringBuffer key = new StringBuffer();
        for (int i = 0; i < SIZE; i++) {
            measurement[i] = qubit[i].measure();
            bobBits[i] = measurement[i] == 1;
            if (aliceBase[i] != bobBase[i]) {
                // If the random bases chosen by Alice and Bob for this bit are different, ignore values
                System.err.println(i + ") Different bases used, ignore values " +
                        (aliceBits[i] ? "1" : "0") + " and " + (bobBits[i] ? "1" : "0") +
                        (noise[i] ? " (NOISE)" : ""));
            } else {
                // Alice and Bob picked the same bases. The inital value from Alice matches the measurement from Bob.
                // this bit now becomes part of the secret key
                System.err.println(i + ") Same bases used. Alice sent " +
                        (aliceBits[i] ? "1" : "0") +
                        " and Bob received " +
                        (bobBits[i] ? "1" : "0") +
                        (noise[i] ? " (NOISE)" : ""));
                key.append(aliceBits[i] ? "1" : "0");
            }
        }
        System.err.println("Secret key = " + key);

        Renderer.renderProgram(program);
    }
}

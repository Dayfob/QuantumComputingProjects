package org.redfx.javaqc;

import org.redfx.strange.*;
import org.redfx.strange.algorithm.Classic;
import org.redfx.strange.gate.*;
import org.redfx.strange.local.*;
import org.redfx.strangefx.render.Renderer;

import java.util.*;

public class App {

    static SimpleQuantumExecutionEnvironment sqee = new SimpleQuantumExecutionEnvironment();

    public static void main(String[] args) {
        doGrover(3, 6);
    }

    private static void doGrover(int dim, int solution) {
        int N = 1 << dim;
        double cnt = Math.PI * Math.sqrt(N) / 4;
        Program p = new Program(dim);
        Step s0 = new Step();
        for (int i = 0; i < dim; i++) {
            s0.addGate(new Hadamard(i));
        }
        p.addStep(s0);
        Oracle oracle = createOracle(dim, solution);
        oracle.setCaption("O");
        Complex[][] dif = createDiffMatrix(dim);
        Oracle difOracle = new Oracle(dif);
        difOracle.setCaption("D");
        for (int i = 1; i < cnt; i++) {
            Step s1 = new Step("Oracle " + i);
            s1.addGate(oracle);
            Step s2 = new Step("Diffusion " + i);
            s2.addGate(difOracle);
            Step s3 = new Step("Prob " + i);
            s3.addGate(new ProbabilitiesGate(0));
            p.addStep(s1);
            p.addStep(s2);
            p.addStep(s3);
        }
        System.out.println(" n = " + dim + ", steps = " + cnt);

        Result res = sqee.runProgram(p);
        Complex[] probability = res.getProbability();
        for (int i = 1; i < cnt; i++) {
            Complex[] ip0 = res.getIntermediateProbability(3 * i);
            System.out.println("results after step " + i + ": " + ip0[solution].abssqr());

        }
        System.out.println("\n");
        Renderer.renderProgram(p);
    }

    static Complex[][] createDiffMatrix(int dim) {
        int N = 1 << dim;
        double N2 = 2. / N;
        Complex[][] answer = new Complex[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                answer[i][j] = (i == j ? new Complex(N2 - 1) : new Complex(N2));
            }
        }
        return answer;
    }

    // solution must be < dim*dim
    static Oracle createOracle(int dim, int solution) {
        int N = 1 << dim;//dim<<1;
        System.err.println("dim = " + dim + " hence N = " + N);
        Complex[][] matrix = new Complex[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i != j) {
                    matrix[i][j] = Complex.ZERO;
                } else {
                    if (i == solution) {
                        matrix[i][j] = Complex.ONE.mul(-1);
                    } else {
                        matrix[i][j] = Complex.ONE;
                    }
                }
            }
        }
        Oracle answer = new Oracle(matrix);
        return answer;
    }
}

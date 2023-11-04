package org.example.Java.Iteratif;

import org.example.Instance;

import static org.example.Java.Iteratif.Plane.*;

public class BruteForceRun {

    public static void Solve(Instance i, boolean debug, boolean display) {
        long start = System.currentTimeMillis();
        dividers(i, debug, display);
        long end = System.currentTimeMillis();
        System.out.println("\nExecution = " + (end - start) + "ms");
    }
}

package org.example.Recursif;

import org.example.Instance;

public class RecursiveSolver {
    Instance inst;

    public RecursiveSolver(Instance inst){
        this.inst = inst;
    }

    public void solve(boolean allSol){
        long start = System.currentTimeMillis();
        int divider[] = new int[inst.nb_dividers];
        divider[0] = 0;
        divider[inst.nb_dividers-1]= inst.capacity;
        int distance[] = new int[(inst.nb_dividers*(inst.nb_dividers-1))/2];
        distance[0] = inst.capacity;
        recursiveCall(divider, distance, 2);
        long end = System.currentTimeMillis();
        System.out.println("Execution = " + (end - start) + "ms");
    }

    private boolean recursiveCall(int divider[], int distance[], int numDiv){

        if (numDiv == inst.nb_dividers){
            int count = 1;
            System.out.print("Solution : ");
            for (int i: divider) {
                System.out.print("D" + count + " : " + i + "  ");
                count ++;
            }
            System.out.print("\n");
        } else {
            int nextDis = -1;
            int newDistance[] = copyTab(distance);
            int newDivider[] = copyTab(divider);
            do {
                if (numDiv == 2) {
                    if (nextDis < 0)
                        nextDis = 2;
                    else
                        nextDis++;
                } else {
                    if (nextDis < 0)
                        nextDis = divider[numDiv - 2] + 1;
                    else
                        nextDis++;
                }

                nextDis = getNewPos(newDivider, newDistance, nextDis, numDiv);

                if (nextDis == -1)
                    return false;
                else {
                    newDivider[numDiv - 1] = nextDis;
                }
            } while (!recursiveCall(newDivider, newDistance, numDiv+1));
        }
        return true;
    }

    private int getNewPos(int divider[], int distance[], int start, int numDiv){
        int pos = start-1;
        int newDistance[] = null;

        while (newDistance == null) {
            pos++;

            if (pos> inst.capacity)
                return -1;

            newDistance = verifDistance(divider, distance, pos, numDiv);
        }

        putAllValTab(distance, newDistance, numDiv);
        return pos;
    }

    private int[] verifDistance(int divider[], int distance[], int pos, int numDiv){
        int newDistance[] = new int[numDiv];
        int count = 0;

        if (contains(inst.exits, pos))
            return null;

        for (int i = 0; i<numDiv-1; i++) {
            if (containDistance(distance, Math.abs(pos-divider[i]), ((numDiv*(numDiv-1))/2)-1) || containDistance(newDistance, Math.abs(pos-divider[i]), count-1)){
                return null;
            } else {
                newDistance[count] = Math.abs(pos-divider[i]);
                count++;
            }
        }

        if (containDistance(distance, Math.abs(pos-divider[divider.length-1]), ((numDiv*(numDiv-1))/2)-1) || containDistance(newDistance, Math.abs(pos-divider[divider.length-1]), count-1)){
            return null;
        } else {
            newDistance[count] = Math.abs(pos-divider[divider.length-1]);
        }

        return newDistance;
    }

    private boolean contains(int tab[], int val){
        for (int i: tab) {
            if (i==val)
                return true;
        }
        return false;
    }

    private boolean containDistance(int tab[], int val, int max){
        int i = 0;
        while(i<=max) {
            if (tab[i]==val)
                return true;
            i++;
        }
        return false;
    }

    private int[] copyTab(int tab[]){
        int[] newTab = new int[tab.length];
        for (int i=0; i<tab.length; i++) {
            newTab[i] = tab[i];
        }
        return newTab;
    }

    private void putAllValTab(int[] receive, int[] sender, int numDiv){
        int count = 0;
        for (int i = (numDiv*(numDiv-1))/2; count<sender.length; i++){
            receive[i] = sender[count];
            count++;
        }
    }
}

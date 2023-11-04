package org.example.Iteratif;

import org.example.Instance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Plane {
    private final int n;
    private final int m;
    private final int[] exits;
    private final PlaneElement[] elements;

    public Plane(int n, int m, int[] exits) {
        this.n = n;
        this.m = m;
        this.exits = exits;
        this.elements = new PlaneElement[m+1]; //SI OUT OF BOUNDS, REGARDE ICI MON BRO

        //A SECURISER (exits vide ou length > m etc...)
        for (int i = 0 ; i < exits.length ; i++) {
            this.elements[exits[i]] = new Exit(i, exits[i]);
        }
    }


    //GETTERS SETTERS
    public int getN() {
        return n;
    }
    public int getM() {
        return m;
    }
    public int[] getExits() {
        return exits;
    }
    public PlaneElement[] getElements() {
        return elements;
    }


    //OTHER ACCESSORS

    public Divider getDiv(int num) {
        if (num > this.n || num < 0) return null;
        for (PlaneElement e : this.elements) {
            if (e != null && e.getType() == elemType.DIVIDER && e.getNum() == num) return (Divider) e;
        }
        return null;
    }
    public Divider[] getAllDivs() {
        Divider[] divs = new Divider[this.n];
        int i = 0;
        for (PlaneElement e : this.elements) {
            if (e != null && e.getType() == elemType.DIVIDER) {
                divs[i] = (Divider) e;
                i++;
            }
        }
        return divs;
    }
    public Exit getExit(int num) {
        if (num > this.exits.length || num < 0) return null;
        for (PlaneElement e : this.elements) {
            if (e != null && e.getType() == elemType.EXIT && e.getNum() == num) return (Exit) e;
        }
        return null;
    }
    public void placeDiv(int n, int i) {
        //Desormais inutile, à supprimer
        this.elements[i] = new Divider(n, i);
    }


    //METHODES UTILES
    public int getDistance(Divider d1, Divider d2) {
        return Math.abs(d1.getPos() - d2.getPos());
    }
    public void display(){

        ArrayList<String> s1 = new ArrayList<String>();

        for (int i = 0; i < this.m; i++) {
            s1.add("| ");
        }
        s1.add("|");
        for ( int i : this.exits) {
            s1.set(i, "|#");
        }

        ArrayList<String> s2 = new ArrayList<String>(s1);

        String res = "\n\nSoit :\n";

        for ( Divider d : this.getAllDivs()) {
            s2.set(d.getPos(), "|"+d.getNum());
            res += "D" + (d.getNum()+1) + " = " + d.getPos() + "; ";
        }

        System.out.println("\nAvant resolution : ");
        for (String s : s1) {
            System.out.print(s);
        }
        System.out.println("\nAprès resolution : ");
        for (String s : s2) {
            System.out.print(s);
        }
        System.out.println(res);
    }

    public void debug(boolean debug){
        if (!debug) return;
        ArrayList<String> deb = new ArrayList<String>();

        for (int i = 0; i < this.m; i++) {
            deb.add("|_");
        }
        deb.add("|");
        for ( int i : this.exits) {
            deb.set(i, "|#");
        }
        for ( Divider d : this.getAllDivs()) {
            if (d != null) deb.set(d.getPos(), "|"+d.getNum());
        }

        System.out.print("debug : ");
        for (String s : deb) {
            System.out.print(s);
        }
        System.out.println();
    }

    //CONTRAINTES
    public boolean DistisDiff(boolean display) {
        //on va calculer le nombre de blocs de sièges entre chaque couple de séparateurs
        //et vérifier que ces distances sont toutes différentes

        if (display) System.out.println("\n ---------------\n  Testing for distinct distances\n");

        ArrayList<Divider> dividers = new ArrayList<Divider>();
        List<Integer> distances = new ArrayList<Integer>();

        //on récupère tous les séparateurs
        for (PlaneElement e : this.elements) {
            if (e != null && e.getType() == elemType.DIVIDER) dividers.add((Divider) e);
        }

        //on affiche les séparateurs et leurs positions
        StringBuilder s = new StringBuilder(), s2 = new StringBuilder();
        for (Divider d : dividers) {
            s.append(d.getNum()).append(" ");
        }
        for (Divider d : dividers) {
            s2.append(d.getPos()).append(" ");
        }
        if (display) {
            System.out.println("dividers : " + s);
            System.out.println("position : " + s2 + "\n");
        }

        for (int i = 0; i < dividers.size(); i++) {
            for (int j = i+1; j < dividers.size(); j++) {
                if (display)System.out.print("div" + dividers.get(i).getNum() +
                        " ; div" + dividers.get(j).getNum());
                Divider d1 = dividers.get(i);
                Divider d2 = dividers.get(j);

                int dist = getDistance(d1, d2);
                if (display) System.out.println(" dist = " + dist);
                //si la distance est déjà présente dans la liste, on retourne false
                if (distances.contains(dist)) {
                    if(display) System.out.println("constraint [distinct distances] not ok");
                    return false;
                }
                //sinon on l'ajoute à la liste
                distances.add(dist);
            }
        }

        if (display) System.out.println("distances : " + distances.toString());

        if(display) System.out.println("constraint [distinct distances] ok");

        return true;
    }
    public boolean firstClassIsTwoBlocks(boolean display) {
        //on vérifie que la classe à l'avant de l'avion dispose d'au moins deux blocs
        //si un le premier séparateur est placé au niveau de la cabine de pilotage, on vérifie que sa distance avec le suivant est supérieure à 1
        //sinon on vérifie qu'il est positionné à au moins 2
        boolean res = false;
        if(display) System.out.println("\n ---------------\n  Testing for first Class Is Two Blocks wide");

        Divider d = getDiv(0);
        Divider d1 = getDiv(1);

        if (d1 == null) {
            res = (d.getPos() == 0 || d.getPos() >= 2);
        }
        else if (d != null) {
            if (d.getPos() == 0) {
                {
                    int dist = getDistance(d, d1);
                    if(display) System.out.println("dist = " + dist);
                    return dist >= 2;
                }
            } else res = d.getPos() >= 2;
        }

        if(display && res) System.out.println("constraint [2Blocs] ok");
        else if(display) System.out.println("constraint [2Blocs] not ok");
        return res;
    }
    public boolean allExitsFree(boolean display) {
        //on vérifie que les sorties de secours sont toutes libres
        ArrayList<Integer> divs = new ArrayList<Integer>();
        ArrayList<Integer> tmp = new ArrayList<Integer>();
        boolean res = true;

        if(display) System.out.println("\n ---------------\n  Testing for Exits Free\n");

        for (int i : this.exits) {
            tmp.add(i);
        }

        for (PlaneElement p : this.elements) {
            if (p != null && p.getType() == elemType.DIVIDER) {
                res &= !tmp.contains(p.getPos());

            }
        }

        if(display) System.out.println("All exits free : " + res);

        if(display && res) System.out.println("constraint [1 exit free] ok");
        else if(display) System.out.println("constraint [1 exit free] not ok");
        return res;
    }

    //TEST DE FIN
    public boolean allDivsPlaced(boolean display) {
        //on vérifie que tous les séparateurs ont été placés

        if(display) System.out.println("\n ---------------\n  Testing if all dividers are placed");

        int cpt = 0;
        for (PlaneElement e : this.elements) {
            if (e != null && e.getType() == elemType.DIVIDER) {
                cpt++;
            }
        }
        if(display && cpt == this.n) System.out.println("constraint [all dividers placed] ok");
        else if(display) System.out.println("constraint [all dividers placed] not ok");
        return cpt == this.n;
    }

    //RUN

    public static void dividers(int nbDivs, int nbBlocks, int[] exits, boolean display, boolean debug) {

        //initialisation de l'avion
        Plane plane = new Plane(nbDivs, nbBlocks, exits);

        //liste des séparateurs et des sorties de secours
        ArrayList<Divider> dividers = new ArrayList<Divider>();
        //ArrayList<Exit> exits = new ArrayList<Exit>();

        //recherche des solutions
        int i = 0;
        int n = 0;

        //System.out.println("n = " + n + ", i = " + i);
        //plane.debug(display);

        while( n < plane.m && n > -1 && !plane.allDivsPlaced(display) ) {

            if (display) {
                System.out.print("\nEtat actuel de la recherche : ");
                plane.debug(debug);
                System.out.println(" début de boucle n = " + n + ", i = " + i);
            }

            //si la postion est hors de la taille de l'avion, la combinaison n'est pas bonne
            if (i > plane.m) {

                if (display) System.out.println("##################\nOUT OF BOUNDS : (" + i + " >=  " + plane.m + ")");

                //on se place sur le diviseur n-1
                n--;
                i = dividers.get(n).getPos();

                //on supprime ce diviseur pour éviter un doublon
                dividers.remove(n);
                plane.elements[i] = null;

                //on place le curseur i sur l'espace suivant
                i++;

                //et on reprend la recherche
                if (display) System.out.println("reprise à n = " + n + ", i = " + i+"\n##################");

                //mais si on est encore en dehors de l'avion, on recule encore plus
            }
            else{
                plane.placeDiv(n, i);
                dividers.add((Divider) plane.elements[i]);

                i = dividers.get(n).getPos() + 1;
                //Overkill ? => checker si juste i++ fait pareil

                if (display) System.out.println("div placé : n = " + n + ", i++ = " + i);
                plane.debug(display);

                if (plane.DistisDiff(display) && plane.firstClassIsTwoBlocks(display) && plane.allExitsFree(display)) {
                    n++;
                    if (display)System.out.println("tests ok\n");
                } else {
                    if (display)System.out.println("tests pas ok\n");
                    plane.elements[dividers.get(n).getPos()] = null;
                    dividers.remove(n);
                }
            }


        }
        if (n == -1) {
            System.out.println("Aucune solution");
        } else {
            System.out.println("Solution trouvée");
            plane.display();
        }

    }
    public static void dividers(Instance inst, boolean display, boolean debug) {
        System.out.println("Instance : n = " + inst.nb_dividers + ", m = " + inst.capacity + ", exits = " + Arrays.toString(inst.exits)+ "\n");
        dividers(inst.nb_dividers, inst.capacity, inst.exits, display, debug);
    }


}



package org.example;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.Variable;

import static org.chocosolver.solver.search.strategy.Search.*;

public class GLIAAirlines {

	
	IntVar[] dividers;

	Model model;

	//constructor
	public GLIAAirlines() {
	}

	public GLIAAirlines(Instance inst, long timeout, boolean allSolutions) {
		buildModel(inst);
		model.getSolver().limitTime(timeout);
		StringBuilder st = new StringBuilder(
				String.format(model.getName() + "-- %s\n", inst.nb_dividers, " X ", inst.capacity));
	}



	public void solve(Instance inst, long timeout, boolean allSolutions) {

		buildModel(inst);
		model.getSolver().limitTime(timeout);
		StringBuilder st = new StringBuilder(
				String.format(model.getName() + "-- %s\n", inst.nb_dividers, " X ", inst.capacity));

				//solver call!

		Solution solution = model.getSolver().findSolution();

		if(allSolutions){
			int countSol = 1;
			while (solution != null){
				prettyPrintSol(countSol);
				solution = model.getSolver().findSolution();
				countSol++;
			}
		} else {
			prettyPrintSol(1);
		}

		model.getSolver().printStatistics();
		
	}

	public void buildModel(Instance inst) {
		// A new model instance
		model = new Model("Aircraft Class Divider ");

		// VARIABLES
		dividers = new IntVar[inst.nb_dividers];

		dividers[0] = model.intVar("D"+1, 0);
		for(int i = 1; i < inst.nb_dividers-1; i++){
			dividers[i] = model.intVar("D"+(i+1), 1, inst.capacity-1);
		}
		dividers[inst.nb_dividers-1] = model.intVar("D"+inst.nb_dividers, inst.capacity);

		IntVar[] distances = new IntVar[(inst.nb_dividers*inst.nb_dividers-inst.nb_dividers)/2];

		int count = 0;
		for (int i = 0; i < inst.nb_dividers; i++) {
			for (int j = i+1; j < inst.nb_dividers; j++) {
				distances[count] = model.intVar("D"+(i+1) + "_sub_" + "D"+(j+1), 0, inst.capacity);
				count++;
			}
		}

		// CONSTRAINTS
		model.arithm(dividers[1], "-", dividers[0], ">=", 2).post();
		count = 0;
		for (int i = 0; i < inst.nb_dividers; i++) {
			model.notMember(dividers[i], inst.exits).post();
			for (int j = i+1; j < inst.nb_dividers; j++) {
				/*model.arithm(dividers[j], "-", dividers[i], "=", distances[count]).post();
				count++;*/
				model.scalar(new IntVar[]{dividers[j], dividers[i]}, new int[]{1, -1}, "=", distances[count]).post();
				count++;
			}
		}

		model.allDifferent(distances).post();

		model.increasing(dividers, 1).post();
	}

	private void prettyPrintSol(int i){
		String sol = "Solution " + i + ": ";
		for (Variable var : model.getVars()) {
				sol += var + "; ";
		}
		System.out.println(sol);
	}

	void testStrats(){
		Solver solver = model.getSolver();
		AbstractStrategy<IntVar>[] strats = new AbstractStrategy[]{
				minDomLBSearch(model.retrieveIntVars(false)),
				Search.minDomUBSearch(model.retrieveIntVars(false)),
				intVarSearch(model.retrieveIntVars(false)),
				Search.inputOrderLBSearch(model.retrieveIntVars(false)),
				Search.inputOrderUBSearch(model.retrieveIntVars(false)),
				Search.activityBasedSearch(model.retrieveIntVars(false)),
				randomSearch(model.retrieveIntVars(false), 37),
		};

		for (AbstractStrategy<IntVar> strat : strats) {
			solver.reset();
			solver.setSearch(strat);
			solver.findAllSolutions();
			solver.printStatistics();
		}
	}
}

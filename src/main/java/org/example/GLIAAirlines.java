package org.example;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

import java.util.Arrays;

import static org.chocosolver.solver.search.strategy.Search.minDomLBSearch;
import static org.chocosolver.util.tools.ArrayUtils.append;

public class GLIAAirlines {

	
	IntVar[] dividers;

	Model model;

	

	public void solve(Instance inst, long timeout, boolean allSolutions) {

		buildModel(inst);
		model.getSolver().limitTime(timeout);
		StringBuilder st = new StringBuilder(
				String.format(model.getName() + "-- %s\n", inst.nb_dividers, " X ", inst.capacity));

				//solver call!

		Solution solution = model.getSolver().findSolution();

		if(allSolutions){
			while (solution != null){
				System.out.println(solution.retrieveIntVars(false));
				solution = model.getSolver().findSolution();
			}
		}

		model.getSolver().printStatistics();
		
	}

	public void buildModel(Instance inst) {
		// A new model instance
		model = new Model("Aircraft Class Divider ");

		// VARIABLES
		dividers = new IntVar[inst.nb_dividers];

		dividers[0] = model.intVar("D"+1, 0);
		dividers[inst.nb_dividers-1] = model.intVar("D"+inst.nb_dividers, inst.capacity);
		for(int i = 1; i < inst.nb_dividers-1; i++){
			dividers[i] = model.intVar("D"+(i+1), 1, inst.capacity-1);
		}

		IntVar[] distances = new IntVar[(inst.nb_dividers*inst.nb_dividers-inst.nb_dividers)/2];

		int count = 0;
		for (int i = 0; i < inst.nb_dividers; i++) {
			for (int j = i+1; j < inst.nb_dividers; j++) {
				distances[count] = dividers[j].sub(dividers[i]).intVar();
				count++;
			}
		}

		// CONSTRAINTS
		model.arithm(dividers[1].sub(dividers[0]).intVar(), ">=", 2).post();
		model.allDifferent(distances).post();
		model.among(model.intVar(inst.exits.length-1), dividers, inst.exits).post();
		model.increasing(dividers, 1).post();

		/*for (Constraint c: model.getCstrs()) {
			System.out.println(c);
		}*/
	}




	public void configureSearch() {
		model.getSolver().setSearch(minDomLBSearch(append(dividers)));

	}


}

package org.example;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.example.Java.Iteratif.BruteForceRun;
import org.example.Java.Recursif.RecursiveSolver;


public class App
{
	
	private static Instance inst = Instance.inst1;
	private static long timeout = 300000; // five minutes
	private static boolean allSolutions;
	private static boolean bi;
	private static boolean br;
	private static boolean compareStrat;

	
	public static void main(String[] args) throws ParseException {

		final Options options = configParameters();
		final CommandLineParser parser = new DefaultParser();
		final CommandLine line = parser.parse(options, args);

		boolean helpMode = line.hasOption("help") || args.length == 0;
		if (helpMode) {
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("LightningAirlines", options, true);
			System.exit(0);
		}
		// Check arguments and options
		for (Option opt : line.getOptions()) {
			checkOption(line, opt.getLongOpt());
		}
		if (bi) new BruteForceRun().Solve(inst, false, true);
		else if (br) new RecursiveSolver(inst).solve(false);
		else if (compareStrat) new GLIAAirlines(inst, timeout, allSolutions).testStrats();

		else new GLIAAirlines().solve(inst, timeout, allSolutions);
	}
	
	
	// Add options here
	private static Options configParameters() {

		final Option helpFileOption = Option.builder("h").longOpt("help").desc("Display help message").build();

		final Option instOption = Option.builder("i").longOpt("instance").hasArg(true).argName("aircraft instance")
				.desc("aircraft instance (#dividers, capacity, exit doors) - from inst1 to inst10").required(false)
				.build();

		final Option allsolOption = Option.builder("a").longOpt("all").hasArg(false).desc("all solutions")
				.required(false).build();

		final Option limitOption = Option.builder("t").longOpt("timeout").hasArg(true).argName("timeout in ms")
				.desc("Set the timeout limit to the specified time").required(false).build();

		final Option BasicIterativeVersion = Option.builder("bi").longOpt("basic_iterative").hasArg(false).desc("Search solution without Choco Solver iteratively")
				.required(false).build();

		final Option BasicRecursiveVersion = Option.builder("br").longOpt("basic_recursive").hasArg(false).desc("Search solution without Choco Solver recursively")
				.required(false).build();

		final Option CompareStrat = Option.builder("c").longOpt("compare").hasArg(false).desc("search solution with different strategies and compare them")
				.required(false).build();

		// Create the options list
		final Options options = new Options();
		options.addOption(instOption);
		options.addOption(allsolOption);
		options.addOption(limitOption);
		options.addOption(helpFileOption);
		options.addOption(BasicIterativeVersion);
		options.addOption(BasicRecursiveVersion);
		options.addOption(CompareStrat);

		return options;
	}
	

	// Check all parameters values
	public static void checkOption(CommandLine line, String option) {

		switch (option) {
		case "instance":
			inst = Instance.valueOf(line.getOptionValue(option));
			break;
		case "timeout":
			timeout = Long.parseLong(line.getOptionValue(option));
			break;
		case "all":
			allSolutions = true;
			break;
		case "basic_iterative":
			bi = true;
			break;
		case "basic_recursive":
			br = true;
			break;
		case "compare":
			compareStrat = true;
			break;
		default: {
			System.err.println("Bad parameter: " + option);
			System.exit(2);
		}

		}

	}
	

	
}

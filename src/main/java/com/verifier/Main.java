package com.verifier;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import com.verifier.ast.Program;
import com.verifier.ast.Tree;
import com.verifier.graph.CntrlFlowGen;
import com.verifier.graph.Graph;
import com.verifier.lexer.JSymbol;
import com.verifier.lexer.Lexer;
import com.verifier.lexer.Token;
import com.verifier.parser.ASTPrinter;
import com.verifier.parser.ParseException;
import com.verifier.parser.Parser;

/**
 * Simple test driver for the java lexer. Just runs it on some input files and
 * produces debug output. Needs symbol class from parser.
 */
public class Main {

	// private static String DOT = "/usr/bin/dot"; // Linux
	private static String DOT = "C:/Program Files (x86)/Graphviz2.38/bin/dot.exe"; // Windows

	public static void main(String argv[]) {

		Main main = new Main();

		if (argv.length < 2) {
			System.err.println("USAGE: java eminijava.Main <OPTION> <FILENAME>");
			System.exit(1);
		}

		switch (argv[0]) {
		case "--lex": {
			main.lex(argv);
		}
			break;
		case "--ast": {
			main.parse(argv);
		}
			break;

		case "-c": {
			main.genCfg(argv);
		}
			break;

		default: {
			System.err.println("Invalid option " + argv[0]);
		}

		}

	}

	public void lex(String[] argv) {

		for (int i = 1; i < argv.length; i++) {

			try {

				if (!isFileValid(argv[i])) {
					continue;
				}

				System.out.println("Lexing file [" + argv[i] + "]");
				Lexer lexer = new Lexer(new FileReader(argv[i]));
				PrintWriter writer = new PrintWriter(getFileName(argv[i]) + ".lexed", "UTF-8");

				JSymbol s;
				do {
					s = lexer.yylex();
					writer.println(s);
				} while (s.token != Token.EOF);

				writer.close();
			} catch (Exception e) {
				e.printStackTrace(System.out);
				System.exit(1);
			}
			System.out.println("Generated output file");
		}

	}

	public void parse(String argv[]) {
		ASTPrinter printer = new ASTPrinter();
		for (int i = 1; i < argv.length; i++) {
			try {

				if (!isFileValid(argv[i])) {
					continue;
				}

				System.out.println("Parsing file [" + argv[i] + "]");

				Lexer lexer = new Lexer(new FileReader(argv[i]));
				Parser p = new Parser(lexer);
				Tree tree = p.parse();
				if (tree != null) {
					String sexpr = printer.visit((Program) tree);
					PrintWriter writer = new PrintWriter(getFileName(argv[i]) + ".ast", "UTF-8");
					writer.println(sexpr);
					writer.close();
					System.out.println("Generated output file");
				}

			} catch (ParseException e) {
				System.err.println(e.getMessage());
				System.exit(1);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	public void genCfg(String argv[]) {
		CntrlFlowGen cfgGen = new CntrlFlowGen();
		for (int i = 1; i < argv.length; i++) {
			try {

				if (!isFileValid(argv[i])) {
					continue;
				}

				System.out.println("Parsing file [" + argv[i] + "]");

				Lexer lexer = new Lexer(new FileReader(argv[i]));
				Parser p = new Parser(lexer);
				Tree tree = p.parse();
				if (tree != null) {
					cfgGen.visit((Program) tree);
					Graph g = cfgGen.getG();

					String dot = getFileName(argv[i]) + ".gv";
					String png = getFileName(argv[i]) + ".png";
					PrintWriter writer = new PrintWriter(dot, "UTF-8");
					writer.println(g.getDotSrc());
					writer.close();
					System.out.println("Generated output file");

					Runtime rt = Runtime.getRuntime();
					String[] args = { DOT, "-Tpng", dot, "-o", png };
					Process proc = rt.exec(args);
					proc.waitFor();
				}

			} catch (ParseException e) {
				System.err.println(e.getMessage());
				System.exit(1);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	private boolean isFileValid(String filename) {
		File f = new File(filename);

		if (!f.exists()) {
			System.err.println(filename + ": No such file!");
			return false;
		}

		String fileExtension = getFileExtension(f);
		if (!"imp".equals(fileExtension)) {
			System.err.println(filename + ": Invalid file extension!");
			return false;
		}

		return true;
	}

	private String getFileExtension(File file) {
		String name = file.getName();
		try {
			return name.substring(name.lastIndexOf(".") + 1);
		} catch (Exception e) {
			return "";
		}
	}

	private String getFileName(String filename) {
		try {
			return filename.substring(0, filename.lastIndexOf("."));
		} catch (Exception e) {
			return "";
		}
	}

	private String getFileDirPath(String filename) {
		try {
			File f = new File(filename);
			String path = f.getParent();
			if (path == null) {
				return "";
			}
			return path + File.separator;
		} catch (Exception e) {
			return "";
		}
	}
}

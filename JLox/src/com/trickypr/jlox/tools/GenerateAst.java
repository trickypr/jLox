package com.trickypr.jlox.tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("Usage: generate_ast <output_dir>");
			System.exit(64);
		}
		
		String outDir = args[0];
		defineAst(outDir, "Expr", Arrays.asList(
			"Binary   : Expr left, Token operator, Expr right",
			"Grouping : Expr expression",                      
			"Literal  : Object value",                         
			"Unary    : Token operator, Expr right"
		));
	}
	
	private static void defineAst(
		String outDir,
		String baseName,
		List<String> types
	) throws IOException {
		String path = outDir + "/" + baseName + ".java";
		PrintWriter writer = new PrintWriter(path, "UTF-8");
		
		writer.println("package com.trickypr.jlox.parsing;");
		writer.println();
		writer.println("import java.util.List;");
		writer.println();
		writer.println("import com.trickypr.jlox.scanner.Token;");
		writer.println();
		writer.println("abstract class " + baseName + " {");
		
		deifineVisitor(writer, baseName, types);
		
		// AST Classes
		for (String type : types) {
			String className = type.split(":")[0].trim();
			String fields = type.split(":")[1].trim();
			defineType(writer, baseName, className, fields);
		}
		
		// base accept() method
		writer.println();
		writer.println("	abstract <R> R accept(Visitor<R> visitor);");
		
		writer.println("}");
		writer.close();
	}
	
	private static void defineType(
			PrintWriter writer, String baseName, 
			String className, String fieldList) {
		writer.println("	static class " + className + " extends " + baseName + " {");
		
		// Create the constructor
		writer.println("	" + className + "(" + fieldList + ") {");
		
		// Store parameters in fields
		String[] fields = fieldList.split(", ");
		for (String field : fields) {
			String name = field.split(" ")[1];
			writer.println("		this." + name + " = " + name + ";");
		}
		
		writer.println("	}");
		
		// Visitor pattern.                                      
	    writer.println();                                        
	    writer.println("    @Override");                         
	    writer.println("    <R> R accept(Visitor<R> visitor) {");
	    writer.println("      return visitor.visit" + className + baseName + "(this);");                   
	    writer.println("    }");
		
		// Fields
		writer.println();
		for (String field : fields) {
			writer.println("	final " + field + ";");
		}
		
		writer.println("	}");
	}
	
	private static void deifineVisitor(PrintWriter writer, String baseName, List<String> types) {
		writer.println("	interface Visitor<R> {");
		
		for (String type : types) {
			String typeName = type.split(":")[0].trim();
			writer.println("	R visit" + typeName + baseName + "(" + typeName + " " + baseName.toLowerCase() + ");");
		}
		
		writer.println("	}");
	}
}

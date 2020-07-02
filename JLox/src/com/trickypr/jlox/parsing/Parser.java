package com.trickypr.jlox.parsing;

import static com.trickypr.jlox.scanner.TokenType.*;

import java.util.List;

import com.trickypr.jlox.scanner.Token;

public class Parser {
	private final List<Token> tokens;
	private int current = 0;
	
	Parser(List<Token> tokens) {
		this.tokens = tokens;
	}
	
	private Expr expression() {
		
	}
}

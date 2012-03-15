/*
 * copyright: kgosafomaafo/exastate.com (http://exastate.com/license/full
 */
package javaminiinterpreter;


import java.io.*;
import kgosafomaafo.*;
/**
 *
 * @author Administrator
 */

public class Main {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		String input_filename = args[0];

		input_filename = "C:\\Users\\Dennis\\Desktop\\finalsem\\Applied Project\\JavaMiniInterpreter\\src\\javaminiinterpreter\\program2.txt";
		
		System.out.println("Reading from " + input_filename);
		Tokenizer tokenizer = new Tokenizer();
		tokenizer.set_inputFilename(input_filename);
		tokenizer.input_startreading();
                

		while(tokenizer.input_readline() != null)
		{
			System.out.println(""+tokenizer.input_lastline());
			//System.out.println(tokenizer.input_lastline2tokens());
                        int length = tokenizer.line2tokens().length;
                        Token[] tokens = new Token[length];

                        //System.out.println(length);
                        
                        System.arraycopy(tokenizer.line2tokens(), 0, tokens, 0, length);
                        Parser parser = new Parser(tokens);
                        if (!parser.parse()) {
                            System.out.println(parser.getError());
                        } else {
                            Interpreter2 interpreter = new Interpreter2(parser);
                            interpreter.translate();
                            //parser.at.setFormula();

                        }
                        /*for (int i=0; i < tokens.length; i++) {
                            System.out.print(tokens[i]);
                        }*/
			System.out.println();
		}
	}
}



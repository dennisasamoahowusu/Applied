/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kgosafomaafo;
import java.io.*;
/**
 *
 * @author Administrator
 */
public class Tokenizer
{
	private String input_filename = "";
	public int input_position = -1;
	private String input_fileline = null;
	BufferedReader input_buffer = null;

	public Tokenizer()
	{

	}

        //Need to readline
	public String input_lastline2tokens()
	{
		String data = "";
		if(input_active())
		{
			Token t;
			do
			{
				t = get_token();
				data +="\n\t"+t;
			}
			while(t.type != Token.Type.END);
		}
		return data;
	}

        public Token[] line2tokens() {
            Token[] tokens = null;
            int counter = 0;
            if (input_active()) {
                Token t;
                do
                {
                    t = get_token();
                    counter++;
                }
                while(t.type != Token.Type.END);
                this.input_position = 0;

                tokens = new Token[counter];

                int i=0;
                do
                {
                    t = get_token();
                    tokens[i] = t;
                    i++;
                }
                while(t.type != Token.Type.END);
                this.input_position = 0;
            }
            return tokens;
        }

        //returns input fileline
	public String input_lastline()
	{
		return this.input_fileline;
	}

        //Tells us input_buffer isnt null
	public boolean input_active()
	{
		return (this.input_buffer != null);
	}
	public boolean input_inactive()
	{
		return !input_active();
	}

        //increases the value of input position by 1
	public boolean input_tonextchar()
	{
		if(input_position < input_filename.length())
		{
			input_position++;
			return true;
		}
		return false;
	}

        //returns the next character but never the first character
	public char input_now()
	{
		if(input_fileline != null)
		{
			if(input_position >= 0 && input_position < input_fileline.length())
			{
				return input_fileline.charAt(input_position);
			}
		}
		return '\0';
	}

        //returns the next character. Could be the first character
	public char input_peek()
	{
		if(input_fileline != null)
		{
			if(input_position >= -1 && input_position < input_fileline.length()-1)
			{
				return input_fileline.charAt(input_position + 1);
			}
		}
		return '\0';
	}
	public int input_skip_spaces()
	{
		int space_count = 0;
		boolean done = false;
		while(!done)
		{
			//while the current character is a space, move forward
			if(Token.check_char_isspace(input_now()))
			{
				input_tonextchar();
				space_count++;
			}
			else
			{
				done = true;
			}
		}
		return space_count;
	}

	public Token get_token()
	{
		Token t = new Token();
		if(input_active())
		{
			input_skip_spaces();
			char c = input_now();
			char tmp_c = '\0';
			if(c == '\0')
			{
				t.clear_all();      //type=Undef and value=''
				t.type = Token.Type.END;
				input_tonextchar();
			}
			else
			{
                                if (this.check_islop_and())
                                {
                                    t.value = Character.toString(c);
                                    input_tonextchar();
                                    t.value += Character.toString(this.input_now());
                                    input_tonextchar();
                                    t.value += Character.toString(this.input_now());
                                    t.type = Token.Type.LOP_AND;
                                    input_tonextchar();
                                }
                                else if (this.check_islop_not())
                                {
                                    t.value = Character.toString(c);
                                    input_tonextchar();
                                    t.value += Character.toString(this.input_now());
                                    input_tonextchar();
                                    t.value += Character.toString(this.input_now());
                                    t.type = Token.Type.LOP_NOT;
                                    input_tonextchar();
                                }
                                else if (this.check_islop_or()) {
                                    t.value = Character.toString(c);
                                    input_tonextchar();
                                    t.value += Character.toString(this.input_now());
                                    t.type = Token.Type.LOP_OR;
                                    input_tonextchar();
                                }
                                else if(Token.check_char_islcurl(c)) {
                                    t.value = Character.toString(c);
                                    t.type = Token.Type.LCURL;
                                    input_tonextchar();
                                }
                                else if (Token.check_char_isrcurl(c))
				{
                                    t.value = Character.toString(c);
                                    t.type = Token.Type.RCURL;
                                    input_tonextchar();
				}
                                else if (Token.check_char_islbrac(c))
				{
                                    t.value = Character.toString(c);
                                    t.type = Token.Type.LBRAC;
                                    input_tonextchar();
				}
                                else if (Token.check_char_isrbrac(c))
				{
                                    t.value = Character.toString(c);
                                    t.type = Token.Type.RBRAC;
                                    input_tonextchar();
				}
                                else if (Token.check_char_iscomma(c))
                                {
                                    t.value = Character.toString(c);
                                    t.type = Token.Type.COMMA;
                                    input_tonextchar();
                                }
                                else if (Token.check_char_isdot(c)) {
                                    t.value = "";
                                    if (!Token.check_char_isdigit(this.input_peek())) {
                                        //if char after dot is not a digit, it could be TUPLEATTR. That condition is catered for under if isloweralpha
                                    } else {
					do
					{
                                                input_tonextchar();
						tmp_c = input_now();
                                                if (Token.check_char_isdigit(tmp_c)) {
                                                    t.value += Character.toString(tmp_c);
                                                 }
					}while(Token.check_char_isdigit(tmp_c));
					t.type = Token.Type.CONST;
                                    }
                                }
                                else if (Token.check_char_isdouble_quote(c)) {
                                    t.value = "";
                                    while (!Token.check_char_isdouble_quote(this.input_peek())) {
                                        input_tonextchar();
                                        t.value += this.input_now();
                                    }
                                    t.type = Token.Type.CONST;
                                    input_tonextchar(); //moves input_now to ënd quote
                                    input_tonextchar(); //moves input to next character after end quote
                                }
                                else if (Token.check_char_issingle_quote(c)) {
                                    t.value = "";
                                    while (!Token.check_char_issingle_quote(this.input_peek())) {
                                        input_tonextchar();
                                        t.value += this.input_now();
                                    }
                                    t.type = Token.Type.CONST;
                                    input_tonextchar(); //moves input_now to ënd quote
                                    input_tonextchar(); //moves input to next character after end quote
                                }
                                else if (Token.check_char_isupper_alpha(c)) { //attribute names beginning with upper case are checked
                                    t.value = Character.toString(c);
                                    t.type = Token.Type.RANGEREL;
                                    input_tonextchar();
                                }
                                else if (Token.check_char_isseparator(c)) {
                                    t.value = Character.toString(c);
                                    t.type = Token.Type.SEPARATOR;
                                    input_tonextchar();
                                }
                                else if (Token.check_char_islower_alpha(c)) {
                                    if (!Token.check_char_isdot(this.input_peek())) { //if lower case letter is followed by a dot, then it's TUPLEATTR and is handled in the dot case
                                        t.value = Character.toString(c);
                                        t.type = Token.Type.TUPLEVAR;
                                        input_tonextchar();
                                    } else {
                                        t.value += Character.toString(c);
                                        input_tonextchar();
                                        t.value += this.input_now();
                                        while (Token.check_char_isalpha(this.input_peek()) || Token.check_char_isdigit(this.input_peek())) {
                                            this.input_tonextchar();
                                            t.value += this.input_now();
                                        }
                                        this.input_tonextchar();
                                        t.type = Token.Type.TUPLEATTR;
                                    }
                                }
				else if (Token.check_char_isdigit(c))
				{
					t.value = Character.toString(c);
					//input_tonextchar();
                                        boolean hasAlpha = false;
					tmp_c = '\0';
					do
					{
						tmp_c = input_peek();
						if(Token.check_char_isdigit(tmp_c))
						{
                                                        input_tonextchar();
							t.value += Character.toString(tmp_c);
						} else if (Token.check_char_isalpha(tmp_c)) {
                                                        hasAlpha = true;
                                                        input_tonextchar();
                                                        t.value += Character.toString(tmp_c);
                                                } else {
                                                    input_tonextchar();
                                                }
					}
					while(Token.check_char_isdigit(tmp_c) || Token.check_char_isalpha(tmp_c));

                                        if (hasAlpha) {
                                            t.type = Token.Type.UNKNOWN;
                                        } else {
                                            t.type = Token.Type.CONST;
                                        }
					tmp_c = input_peek();
					if(Token.check_char_isdot(tmp_c))
					{
						t.value += Character.toString(tmp_c);
						input_tonextchar();
						do
						{
							tmp_c = input_peek();
							if(Token.check_char_isdigit(tmp_c))
							{
								t.value += Character.toString(tmp_c);
								input_tonextchar();
							}
						}while(Token.check_char_isdigit(tmp_c));
					}
					
				}
				
				else if (Token.check_char_iscomment(c))
				{
					t.value = Character.toString(c);
					t.autotype();
					input_tonextchar();
				}
				else if (Token.check_char_isoperator(c))
				{
					t.value = Character.toString(c);

                                        if (Token.check_char_isoperator(this.input_peek())) {
                                            input_tonextchar();
                                            t.value += Character.toString(this.input_now());
                                            if (t.value.equals(">=")) {
                                                t.type = Token.Type.COP_GREATER_EQUAL;
                                            } else if (t.value.equals("!=")) {
                                                t.type = Token.Type.COP_NOT_EQUAL;
                                            } else if (t.value.equals("<=")) {
                                                t.type = Token.Type.COP_LESS_EQUAL;
                                            } else if (t.value.equals("!>")) {
                                                t.type = Token.Type.COP_LESS_EQUAL;
                                            } else if (t.value.equals("!<")) {
                                                t.type = Token.Type.COP_GREATER_EQUAL;
                                            } else {
                                                t.type = Token.Type.UNDEF;
                                            }
                                        } else {
                                            t.autotype();
                                        }
                                        input_tonextchar();
				}
                                else if (Token.check_char_isexistential(c))
                                {
                                    t.value = Character.toString(c);
                                    t.type = Token.Type.EXISTENTIAL;
                                    input_tonextchar();
                                }
                                else if (Token.check_char_isuniversal(c))
                                {
                                    t.value = Character.toString(c);
                                    t.type = Token.Type.UNIVERSAL;
                                    input_tonextchar();
                                }
				else
				{
					t.value = Character.toString(c);
					t.type = Token.Type.UNKNOWN;
					input_tonextchar();
				}
			}
		}

		return t;
	}
	public String input_readline()
	{
		input_fileline_clear();
		input_fileline = null;
		if(this.input_buffer != null)
		{
			try {
				input_fileline = input_buffer.readLine();
                                input_position = 0;
			}
			catch(Exception ex) {
				ex.printStackTrace();
				input_fileline = null;
			}
			finally {

			}
        }
		return input_fileline;
	}

	public boolean input_startreading()
	{
		if (input_buffer != null)
		{
			input_endreading();
		}
		if (this.input_filename.length() > 0)
		{
			try {
				input_buffer = new BufferedReader(new FileReader(input_filename));
				return true;
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				input_endreading();
			}
			finally {

			}
		}
		return false;
	}

	public void input_fileline_clear()
	{
		this.input_fileline = null;
		this.input_position = -1;
	}

        //closes input buffer
	public boolean input_endreading()
	{
		if(this.input_buffer != null)
		{
			try
			{
				this.input_buffer.close();
				this.input_buffer = null;
				input_fileline_clear();
				return true;
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			finally
			{
				return false;
			}
		}
		return false;
	}

	public boolean set_inputFilename(String input_filename)
	{
		this.input_filename = input_filename;
		return true;
	}
	public String get_inputFilename()
	{
		return this.input_filename;
	}

        public boolean check_islop_and() {
            String value = "";
            int counter = 0;
            int position = input_position;
            while (position < input_fileline.length() && counter < 3) {
                value += Character.toString(input_fileline.charAt(position));
                position++;
                counter++;
            }
            if (value.equalsIgnoreCase("and")) {
                return true;
            }
            return false;
        }

        public boolean check_islop_not() {
            String value = "";
            int counter = 0;
            int position = input_position;
            while (position < input_fileline.length() && counter < 3) {
                value += Character.toString(input_fileline.charAt(position));
                position++;
                counter++;
            }
            if (value.equalsIgnoreCase("not")) {
                return true;
            }
            return false;
        }

        public boolean check_islop_or() {
            String value = "";
            int counter = 0;
            int position = input_position;
            while (position < input_fileline.length() && counter < 2) {
                value += Character.toString(input_fileline.charAt(position));
                position++;
                counter++;
            }
            if (value.equalsIgnoreCase("or")) {
                return true;
            }
            return false;
        }
}

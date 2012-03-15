/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kgosafomaafo;

/**
 *
 * @author kgoasfomaafo/exastate
 */
public class Token
{
	public enum Type {
                COMMA,
		COMMENT,
                CONST,
		COP_EQUAL,
		COP_LESS,
		COP_LESS_EQUAL,
		COP_GREATER,
		COP_GREATER_EQUAL,
                COP_NOT,
                COP_NOT_EQUAL,
                DOUBLE_QUOTE,
		END,
                EXISTENTIAL,
                LBRAC,
                LCURL,
                LOP_AND,
                LOP_NOT,
                LOP_OR,
                RANGEREL,
                RBRAC,
                RCURL,
                SEPARATOR,
                SINGLE_QUOTE,
                TUPLEATTR,
                TUPLEVAR,
		UNKNOWN,
		UNDEF,
                UNIVERSAL,
	}

	public Type type;
	public String value;
	public Token()
	{
		clear_all();
	}

	public void clear_all()
	{
		type = Type.UNDEF;
		value = "";
	}

	public void clear_value_nulls()
	{
		//we need to keep real strings in value, so use that.
		if(value == null){value = "";}
	}

	public void autotype()
	{
		clear_value_nulls();
		if(value.equals("")){ type = Type.UNDEF;}
		if(value.equals("=")){ type = Type.COP_EQUAL;}
		if(value.equals(">")){type = Type.COP_GREATER;}
		if(value.equals("<")){type = Type.COP_LESS;}
                if(value.equals("!")){type = Type.COP_NOT;}
	}

	public String toString()
	{
		String type_string = "";
		switch(this.type)
		{
                        case COMMA: type_string = "COMMA"; break;
			case COMMENT: type_string = "COMMENT"; break;
                        case CONST: type_string = "CONST"; break;
			case COP_EQUAL: type_string = "COP_EQUAL"; break;
			case COP_GREATER: type_string = "COP_GREATER"; break;
			case COP_GREATER_EQUAL: type_string = "COP_GREATER_EQUAL"; break;
			case COP_LESS: type_string = "COP_LESS"; break;
			case COP_LESS_EQUAL: type_string = "COP_LESS_EQUAL"; break;
                        case DOUBLE_QUOTE: type_string = "DOUBLE_QUOTE"; break;
                        case COP_NOT: type_string = "COP_NOT"; break;
                        case COP_NOT_EQUAL: type_string = "COP_NOT_EQUAL"; break;
			case END: type_string = "END"; break;
                        case EXISTENTIAL: type_string = "EXISTENTIAL"; break;
                        case LBRAC: type_string = "LBRAC"; break;
                        case LCURL: type_string = "LCURL"; break;
                        case LOP_AND: type_string = "LOP_AND"; break;
                        case LOP_NOT: type_string = "LOP_NOT"; break;
                        case LOP_OR: type_string = "LOP_OR"; break;
                        case RANGEREL: type_string = "RANGEREL"; break;
                        case RBRAC: type_string = "RBRAC"; break;
			case RCURL: type_string = "RCURL"; break;
			case SINGLE_QUOTE: type_string = "SINGLE_QUOTE"; break;
                        case TUPLEATTR: type_string = "TUPLEATTR"; break;
                        case TUPLEVAR: type_string = "TUPLEVAR"; break;
                        case SEPARATOR: type_string = "SEPARATOR"; break;
                        case UNDEF: type_string = "UNDEF"; break;
                        case UNKNOWN: type_string = "UNKNOWN"; break;
                        case UNIVERSAL: type_string = "UNIVERSAL"; break;
			default:
				type_string = "UNKNOWN";
		}
		return "Token("+type_string+":"+this.value+")";
	}
	public static boolean check_char_isoperator(char c)
	{
		return (c == '<' || c == '>' || c == '!' || c == '=');
	}
	public static boolean check_char_iscomment(char c)
	{
		return (c == '#');
	}
	public static boolean check_char_isalpha(char c)
	{
		return (Character.isLetter(c));
	}
	public static boolean check_char_islower_alpha(char c)
	{
		return (Character.isLetter(c) && Character.isLowerCase(c));
	}

	public static boolean check_char_isupper_alpha(char c)
	{
		return (Character.isLetter(c) && Character.isUpperCase(c));
	}
        
	public static boolean check_char_isdigit(char c)
	{
		return Character.isDigit(c);
		//return (c <= '9' && c >= '0');
	}
	public static boolean check_char_isdot(char c)
	{
		return (c == '.');
	}
	public static boolean check_char_isunderscore(char c)
	{
		return (c == '_');
	}
	public static boolean check_char_isspace(char c)
	{
		return Character.isWhitespace(c);
	}

	public static boolean check_char_isnull(char c)
	{
		return (c == '\0');
	}

        public static boolean check_char_islcurl(char c) {
            return (c == '{');
        }

        public static boolean check_char_isrcurl(char c) {
            return (c == '}');
        }

        public static boolean check_char_islbrac(char c) {
            return (c == '(');
        }

        public static boolean check_char_isrbrac(char c) {
            return (c == ')');
        }

        public static boolean check_char_iscomma(char c) {
            return (c == ',');
        }

        public static boolean check_char_issingle_quote(char c) {
            return (c == '\'');
        }

        public static boolean check_char_isdouble_quote(char c) {
            return (c == '\"');
        }

        public static boolean check_char_isseparator(char c) {
            return (c == '|');
        }

        public static boolean check_char_isexistential(char c) {
            return (c == '*');
        }

        public static boolean check_char_isuniversal(char c) {
            return (c == '^');
        }
}
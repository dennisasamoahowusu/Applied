/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kgosafomaafo;

/**
 *
 * @author Dennis
 */
public class AtomToken extends Token {

    private Token[] tokens;
    private int tokensLength;
    private int start; //index at which formula starts
    private int startBackup;
    private int end; //index at which formula ends
    private String errorMsg = null;

    public Token[] relTokens = null;
    public Token[] varTokens = null;

    public Formula[] formulae;
    public Token[] lops;

    //tokens contains a rel calc expr that begins and ends with curly brackets
    //and has 1 separator
    public AtomToken(Token[] tokens) {
        this.tokensLength = tokens.length;
        this.tokens = new Token[this.tokensLength];
        System.arraycopy(tokens, 0, this.tokens, 0, this.tokensLength);

        Token t;
        for (int i=0; i < this.tokensLength; i++) {
           t = this.tokens[i];
           if (t.type == Token.Type.SEPARATOR) {
               start = i+1;
               this.startBackup = start;
               break;
           }
        }

        for (int x=0; x < this.tokensLength; x++) {
           t = this.tokens[x];
           if (t.type == Token.Type.RCURL) {
               end = x-1;
               break;
           }
        }
    }
    

    public boolean isLop(Token t) {
        if (t.type == Token.Type.LOP_AND || t.type == Token.Type.LOP_NOT ||
                t.type == Token.Type.LOP_OR) {
            return true;
        }
        return false;
    }

    public static boolean isCop (Token t) {
        if (t.type == Token.Type.COP_EQUAL ||
                t.type == Token.Type.COP_GREATER ||
                t.type == Token.Type.COP_GREATER_EQUAL ||
                t.type == Token.Type.COP_LESS ||
                t.type == Token.Type.COP_LESS_EQUAL ||
                t.type == Token.Type.COP_NOT ||
                t.type == Token.Type.COP_NOT_EQUAL) {
            return true;
        }

        return false;
    }

    public boolean parseAtoms() {
        if (start > end) {
            return true;
        } else {
            if (this.tokens[start].type == Token.Type.RANGEREL) {
                if (this.checkRangeRel()) {
                    this.start += 4;
                } else {
                    return false;
                }
            } else if (this.tokens[start].type == Token.Type.TUPLEATTR ||
                    this.tokens[start].type == Token.Type.CONST) {
                if (this.checkAtomC()) { //this takes care of constants
                    this.start += 3;
                } else {
                    return false;
                }
            } else if (this.tokens[start].type == Token.Type.LBRAC) {
                this.start += 1;
            } else if (this.tokens[start].type == Token.Type.RBRAC) {
                this.start += 1;
            } else if (this.isLop(this.tokens[start])) {
                this.start += 1;
            } else if (this.tokens[start].type == Token.Type.UNIVERSAL ||
                        this.tokens[start].type == Token.Type.EXISTENTIAL) {
                if (this.tokens[start+1].type != Token.Type.TUPLEVAR ) {
                    this.errorMsg = "Expecting TUPLEVAR after " + this.tokens[start].value;
                    return false;
                } else if (this.tokens[start-1].type != Token.Type.LBRAC) {
                    this.errorMsg = "Expecting ( before " + this.tokens[start].value;
                    return false;
                } else if (this.tokens[start+2].type != Token.Type.RBRAC) {
                    this.errorMsg = "Expecting ) after " + this.tokens[start+1].value;
                    return false;
                } else if (this.tokens[start+3].type != Token.Type.LBRAC) {
                    this.errorMsg = "Expecting ( after '" + this.tokens[start+1].value+this.tokens[start+2].value+"'";
                    return false;
                } else {
                    this.start += 4;
                }
            } else {
                this.errorMsg = "Unexpected '" + this.tokens[start].value + "' somewhere after |";
                return false;
            }
        }
        return this.parseAtoms();
    }

    //checking atoms of the form b.name = "50"
    public boolean checkAtomC() {
        try {
            if (!AtomToken.isCop(this.tokens[start+1])) {
                this.errorMsg = "Expecting compare operator after '" + this.tokens[start].value + "' somewhere after |";
                return false;
            } else if ((this.tokens[start+2].type != Token.Type.CONST) &&
                            (this.tokens[start+2].type != Token.Type.TUPLEATTR)) {
                this.errorMsg = "Expecting TUPLEATTR or CONST after '" + this.tokens[start+1].value + "' somewhere after |";
                return false;
            } else if (this.tokens[start].type == Token.Type.CONST &&
                        this.tokens[start+2].type == Token.Type.CONST) {
                this.errorMsg = "Expecting TUPLEATTR after '" + this.tokens[start+1] + "' somewhere after |";
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean checkRangeRel() {
        try {
            if (this.tokens[start+1].type != Token.Type.LBRAC) {
                this.errorMsg = "Expecting ( after " + this.tokens[start].value;
                return false;
            } else if (!(this.tokens[start+2].type == Token.Type.TUPLEVAR)) {
                this.errorMsg = "Expecting tuple variable after " + this.tokens[start+1].value;
                return false;
            } else if (!(this.tokens[start+3].type == Token.Type.RBRAC)) {
                this.errorMsg = "Expecting ) after " + this.tokens[start+2].value;
                return false;
        }
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    //Puts all relations in relTokens and puts all vars in varTokens
    //Each var must correspond to exactly one relation
    //The same relation can however correspond to more than one var
    //vars >= relations
    public boolean setRelsVars() {
       int counter1=0;
        for (int x=this.startBackup; x<=this.end; x++){
            if (this.tokens[x].type == Token.Type.RANGEREL){
                counter1++;
            }
        }

       this.relTokens = new Token[counter1];
       this.varTokens = new Token[counter1];

        int counter=0;
        for (int i=this.startBackup; i<=this.end; i++){
            if (this.tokens[i].type == Token.Type.RANGEREL){
                this.relTokens[counter]=this.tokens[i];
                this.varTokens[counter]=this.tokens[i+2];
                counter++;
            }
        }

        for (int a=0; a<this.varTokens.length-1; a++){
            for (int b=a+1; b<this.varTokens.length; b++) {
                if (this.varTokens[a].value.equals(this.varTokens[b].value)){
                    this.errorMsg = "tupleVar " + this.varTokens[a].value +
                            " cannot belong to more than one relation";
                    this.relTokens = null;
                    this.varTokens = null;
                    return false;
                }
            }
        }
        return true;
    }

    public void setFormula() {
        int numOfFirstLevelOperators = 0;
        int i = this.startBackup;
        while (i<=this.end){
            if (this.isLop(this.tokens[i])) {
                numOfFirstLevelOperators += 1;
                if (this.tokens[i+1].type == Token.Type.LBRAC){
                    int closingBracket = 1;
                    for (int x=i+2; x<=this.end; x++){
                        if (this.tokens[x].type == Token.Type.LBRAC){
                            closingBracket++;
                        }
                        if (this.tokens[x].type == Token.Type.RBRAC){
                            closingBracket--;
                        }
                        if (closingBracket == 0) {
                            i=x+1;
                            break;
                        }
                    }
                } else {
                    i++;
                }
            } else {
                i++;
            }
        }

        int numOfFirstLevelFormulae = numOfFirstLevelOperators + 1;
        formulae = new Formula[numOfFirstLevelFormulae];
        int countFirstLevelFormula = 0;

        lops = new Token[numOfFirstLevelOperators];
        int countOperators = 0;

        int marker=this.startBackup;
        int a = this.startBackup;
        while (a<=this.end){
            if (this.isLop(this.tokens[a])) {
                lops[countOperators] = this.tokens[a];
                countOperators++;

                int length = a - marker;
                Token[] tokens1 = new Token[length];

                int counter2 = 0;
                for (int b=marker; b<a; b++){
                    tokens1[counter2] = this.tokens[b];
                    counter2++;
                }

                formulae[countFirstLevelFormula] = this.getFormula(tokens1);
                
                for (int d=0; d<tokens1.length; d++){
                    //System.out.print(tokens1[d]);
                }
                //System.out.println();

                marker = a + 1;
                
                countFirstLevelFormula++;
                if (countFirstLevelFormula == numOfFirstLevelFormulae - 1){
                    int length1  = 0;
                    for (int m=a+1; m<=this.end; m++){
                        length1++;
                    }
                    Token[] tokens2 = new Token[length1];
                    int counter = 0;
                    for (int n=a+1; n<=this.end; n++){
                        tokens2[counter] = this.tokens[n];
                        counter++;
                    }
                    formulae[countFirstLevelFormula] = this.getFormula(tokens2);

                    for (int d=0; d<tokens2.length; d++){
                        //System.out.print(tokens2[d]);
                    }
                    //System.out.println();
                }

                
                if (this.tokens[a+1].type == Token.Type.LBRAC){
                    int closingBracket = 1;
                    for (int y=a+2; y<=this.end; y++){
                        if (this.tokens[y].type == Token.Type.LBRAC){
                            closingBracket++;
                        }
                        if (this.tokens[y].type == Token.Type.RBRAC){
                            closingBracket--;
                        }
                        if (closingBracket == 0) {
                            a=y +1;
                            break;
                        }
                    }
                } else {
                    a++;
                }
            } else {
                a++;
            }
        }

        if (countFirstLevelFormula <= 0) { //no logical operators in formula
                int length1  = 0;
                for (int m=this.startBackup; m<=this.end; m++){
                    length1++;
                }
                Token[] tokens2 = new Token[length1];
                int counter = 0;
                for (int n=this.startBackup; n<=this.end; n++){
                    tokens2[counter] = this.tokens[n];
                    counter++;
                }
                formulae[countFirstLevelFormula] = this.getFormula(tokens2);
        }
    }

    private Formula getFormula(Token[] tokens) {
            if (tokens.length == 4) {
                if (tokens[0].type == Token.Type.RANGEREL &&
                        tokens[2].type == Token.Type.TUPLEVAR){
                    Formula1 formula = new Formula1(tokens[0],tokens[2]);
                    return formula;
                } else {

                }
            } else if (tokens.length == 3) {
                Formula2 formula = new Formula2();
                if (formula.initialize(tokens[0], tokens[1], tokens[2])){
                    return formula;
                } else {
                    return null;
                }
            } else {
                return null;
            }

        return null;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    
}

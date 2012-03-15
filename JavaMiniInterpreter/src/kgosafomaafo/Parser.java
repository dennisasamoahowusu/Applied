/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kgosafomaafo;

/**
 *
 * @author Dennis
 */
public class Parser {

    Token[] tokens;
    int tokensLength;
    String errorMsg;

    public AtomToken at;


    /**
     * Assuming tokens contains more than one relational calculus expression,
     * the first RCURL found signifies the end of the first relational calculus expression.
     * The beginning position of the expression being worked on is bPosition while the end position
     * is ePosition.
     */
    int bPosition = 0;
    int ePosition = -1;

    public Parser(Token[] tokens) {
        this.tokensLength = tokens.length;
        this.tokens = new Token[this.tokensLength];
        System.arraycopy(tokens, 0, this.tokens, 0, this.tokensLength);
        at = new AtomToken(this.tokens);
    }

    public boolean checkCurls() {
        if (this.tokens[bPosition].type != Token.Type.LCURL) {
            this.errorMsg = "beginning '{' not found";
            return false;
        } else {
            for (int i=0; i < this.tokensLength; i++) {
                if (this.tokens[i].type == Token.Type.RCURL) {
                    this.ePosition = i;
                    break;
                }
            }
            if (this.ePosition == -1) {
                this.errorMsg = "Expecting '}'";
                return false;
            }
        }
        return true;
    }

    public boolean checkBrackets() {
        int counter = 0;
        for (int i=0; i < this.tokensLength; i++) {
            if (this.tokens[i].type == Token.Type.LBRAC) {
                counter++;
            }
        }
        for (int i=0; i < this.tokensLength; i++) {
            if (this.tokens[i].type == Token.Type.RBRAC) {
                counter--;
            }
        }

        if (counter == 0) {
            return true;
        } else if (counter > 0) {
            this.errorMsg = "Expecting )";
            return false;
        } else {
            this.errorMsg = "Unexpected )";
            return false;
        }
    }

    public boolean containsUnknowns() {
        Token t;
        int i = this.bPosition;
        do
        {
            t = this.tokens[i];
            i++;
        } while (t.type != Token.Type.UNKNOWN && i < this.ePosition - 1);

        if (t.type == Token.Type.UNKNOWN) {
            this.errorMsg = "Unknown character '"+t.value+"'";
            return false;
        }
        return true;
    }

    public boolean has1Separator() {
        int counter = 0;
        for (int i = this.bPosition; i < this.ePosition; i++) {
            if (this.tokens[i].type == Token.Type.SEPARATOR) {
                counter++;
            }
        }
        if (counter == 0) {
            this.errorMsg = "Expecting '|'";
            return false;
        } else if (counter > 1) {
            this.errorMsg = "Unexpected '|'";
            return false;
        } else {
            return true;
        }
    }

    public boolean isValidLHS() {
        if (!this.has1Separator()) {
            return false;
        }
        Token t;
        int i = this.bPosition + 1;
        t = this.tokens[i];
        while (t.type != Token.Type.SEPARATOR)
        {
            if (t.type != Token.Type.TUPLEVAR &&
                    t.type != Token.Type.TUPLEATTR &&
                    t.type != Token.Type.COMMA) {
                this.errorMsg = "Invalid character '"+t.value+"' somewhere before |";
                return false;
            } else if (t.type == Token.Type.COMMA &&
                    (this.tokens[i-1].type != Token.Type.TUPLEATTR) &&
                    (this.tokens[i-1].type != Token.Type.TUPLEVAR)) {
                this.errorMsg = "Wrongly placed character ',' somewhere before |";
                return false;
            } else if (t.type == Token.Type.COMMA &&
                    (this.tokens[i+1].type != Token.Type.TUPLEATTR) &&
                    (this.tokens[i+1].type != Token.Type.TUPLEVAR)) {
                this.errorMsg = "Wrongly placed character ',' somewhere before |";
                return false;
            } else if ((t.type == Token.Type.TUPLEATTR ||
                    t.type == Token.Type.TUPLEVAR) &&
                    (this.tokens[i+1].type == Token.Type.TUPLEATTR 
                    || this.tokens[i+1].type == Token.Type.TUPLEVAR)) {
                this.errorMsg = "',' expected somewhere before |";
                return false;
            } else if ((t.type == Token.Type.TUPLEATTR ||
                    t.type == Token.Type.TUPLEVAR) && !this.corresponds(t)) {
                    this.errorMsg = "Could not find RangeRelation for " +
                           t.value;
                    return false;
            }
            i++;
            t = this.tokens[i];
        } 
        
        return true;
    }


    //This checks that every tuple variable whether in tuplevar or tupleattr on
    //the left side has a corresponding rangerel on the right handside
    public boolean corresponds(Token t) {
        if (t.type != Token.Type.TUPLEATTR && t.type != Token.Type.TUPLEVAR) {
            return false;
        }
        String tupleVar = "";
        if (t.type == Token.Type.TUPLEVAR) {
            tupleVar = t.value;
        } else if (t.type == Token.Type.TUPLEATTR) {
            tupleVar = Character.toString(t.value.charAt(0));
        } else {
            return false;
        }

        for(int i = this.bPosition; i <= this.ePosition; i++) {
            if (this.tokens[i].type == Token.Type.RANGEREL) {
                if (this.tokens[i+2].value.equalsIgnoreCase(tupleVar)) {
                    return true;
                }
            }
        }

        return false;
    }
    
    public boolean parse() {
        if (!this.checkCurls()) {
            return false;
        }

        if (!this.checkBrackets()) {
            return false;
        }
        
        if (!this.containsUnknowns()) {
            return false;
        }
        
        if (!this.has1Separator()) {
            return false;
        }

        if (!this.isValidLHS()) {
            return false;
        }

        if (!at.parseAtoms()) {
            this.errorMsg = at.getErrorMsg();
            return false;
        }

        if (!at.setRelsVars()) {
            this.errorMsg = at.getErrorMsg();
            return false;
        }
        return true;
    }


    public String getError() {
        return this.errorMsg;
    }
    
}

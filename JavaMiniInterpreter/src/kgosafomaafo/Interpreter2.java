/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kgosafomaafo;

/**
 *
 * @author Dennis
 */
public class Interpreter2 {

    Parser parseObj;
    Tuple[] tuples;

    public Interpreter2(Parser parseObj) {
        this.parseObj = parseObj;
        this.setTuplesColumns();
        this.parseObj.at.setFormula();
    }

    public void translate() {
        String columns = "";

        for (int i=0; i<this.tuples.length; i++) {
            if (this.tuples[i].selectedColumns != null) {
                if (this.tuples[i].selectedColumns.length > 0) {
                    columns += this.tuples[i].selectedColumns[0];
                    for (int x = 1; x < this.tuples[i].selectedColumns.length; x++) {
                        columns += "," + this.tuples[i].selectedColumns[x];
                    }
                    if (i != this.tuples.length - 1){
                        columns += ",";
                    }
                }
            }
            if (columns.equals("")) {
                columns = "all columns of "+this.tuples[i].relation;
            }
        }

        System.out.println(columns);
    }

    private void setTuplesColumns() {
        int counter = this.parseObj.at.varTokens.length;
        this.tuples = new Tuple[counter];
        for (int i=0; i<counter; i++){
            Token varToken = this.parseObj.at.varTokens[i];
            Token relToken = this.parseObj.at.relTokens[i];
            Tuple tuple = new Tuple(varToken,relToken.value);
            this.setSelectedColumns(tuple);
            this.tuples[i] = tuple;
        }
    }
    
    private void setSelectedColumns(Tuple t) {
        String[] columns = null;
        Token token;
        int counter = 0;
        int i = this.parseObj.bPosition;
        do {
            token = this.parseObj.tokens[i];
            if (token.type == Token.Type.TUPLEATTR &&
                    token.value.charAt(0) == t.token.value.charAt(0)) {
                counter++;
            }
            i++;
        } while(token.type != Token.Type.SEPARATOR && i <= this.parseObj.ePosition);

        if (counter > 0) {
            columns = new String[counter];
            Token token2;
            int x = this.parseObj.bPosition;
            int y = 0;
            do {
                token2 = this.parseObj.tokens[x];
                if (token2.type == Token.Type.TUPLEATTR &&
                        token2.value.charAt(0) == t.token.value.charAt(0)) {
                    columns[y] = token2.value;
                    y++;
                }
                x++;
            } while(token2.type != Token.Type.SEPARATOR && i <= this.parseObj.ePosition);
        }
        t.setSelectedColumns(columns);
    }

    private Token getRelationFromTupleVar(Token t) {
        if (t.type != Token.Type.TUPLEVAR){
            return null;
        }
        for (int i=0; i<this.parseObj.at.varTokens.length; i++){
            if (t.value.equals(this.parseObj.at.varTokens[i].value)) {
                return this.parseObj.at.relTokens[i];
            }
        }
        return null;
    }

    private String getTupleVarFromTupleAttr(Token t) {
        if (t.type != Token.Type.TUPLEATTR) {
            return null;
        }
        return Character.toString(t.value.charAt(0));
    }

    private Token getRelationFromTupleAttr(Token t) {
        if (t.type != Token.Type.TUPLEATTR){
            return null;
        }
        String tupleVar = this.getTupleVarFromTupleAttr(t);
        if (tupleVar != null) {
            Token tok = new Token();
            tok.type = Token.Type.TUPLEVAR;
            tok.value = tupleVar;
            return this.getRelationFromTupleVar(tok);
        }
        return null;
    }

    private String getRelationDotAttr(Token t) {
        if (t.type != Token.Type.TUPLEATTR){
            return null;
        }
        Token relToken = this.getRelationFromTupleAttr(t);
        String attr = t.value.split("\\.")[1];
        return relToken.value + "." + attr;
    }
}

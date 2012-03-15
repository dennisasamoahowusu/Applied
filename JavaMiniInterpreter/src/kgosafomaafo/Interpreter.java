/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kgosafomaafo;

/**
 *
 * @author Dennis
 */
public class Interpreter {

    Parser parseObj;
    Tuple[] tuples;

    public Interpreter(Parser parseObj) {
        this.parseObj = parseObj;
    }

    public void translate() {
        int counter = 0;
        for (int i = this.parseObj.bPosition; i <= this.parseObj.ePosition; i++) {
            if (this.parseObj.tokens[i].type == Token.Type.RANGEREL) {
                counter++;
            }
        }

        Token[] tupleVars = new Token[counter];
        Token[] rangeRels = new Token[counter];
        this.tuples = new Tuple[counter];

        int counter2 = 0;
        for (int x = this.parseObj.bPosition; x <= this.parseObj.ePosition; x++) {
            if (this.parseObj.tokens[x].type == Token.Type.RANGEREL) {
                rangeRels[counter2] = this.parseObj.tokens[x];
                tupleVars[counter2] = this.parseObj.tokens[x+2];
                counter2++;
            }
        }

        if (tupleVars.length > 0) {
            for (int a=0; a < tupleVars.length; a++) {
                this.tuples[a] = new Tuple(tupleVars[a],rangeRels[a].value);
                this.setSelectedColumns(this.tuples[a]);
                this.setConditions(this.tuples[a]);       
            }
        }

        String translation1 = "";
        String translation2 = "";

        if (this.tuples.length > 0) {
            translation1 += this.tuples[0].getTranslation1();
            translation2 += this.tuples[0].getTranslation2();
        }
        for (int c=1; c < this.tuples.length; c++) {
            String translationTemp1 = this.tuples[c].getTranslation1();
            if (!translationTemp1.equals("")) {
                translation1 += "," + translationTemp1;
            }
            String translationTemp2 = this.tuples[c].getTranslation2();
            if (!translationTemp2.equals("")) {
                if (translation2.equals("")) {
                    translation2 += translationTemp2;
                } else {
                    translation2 += " and " + translationTemp2;
                }
            }
        }

        String relations = "";
        if (this.tuples.length > 0) {
            relations += this.tuples[0].relation;
            for (int k=1; k < this.tuples.length; k++) {
                if (k == this.tuples.length - 1) {
                    relations += " and " + this.tuples[tuples.length-1].relation;
                } else {
                    relations += "," + this.tuples[k].relation;
                }
            }
            
        }

        String translation = "";
        if (translation2.equals("")) {
            translation = translation1 + " from " + relations;
        } else {
            translation = translation1 + " from "  + relations + " where " + translation2;
        }
        System.out.println(translation);
    }

    private void setConditions(Tuple tuple) {
        int counter = 0;
        for (int i = this.parseObj.bPosition; i <= this.parseObj.ePosition; i++) {
            if(AtomToken.isCop(this.parseObj.tokens[i])) {
                Token t1 = this.parseObj.tokens[i-1];
                Token t2 = this.parseObj.tokens[i+1];

                String s1 = "";
                String s2 = "";
                if (t1.type == Token.Type.TUPLEATTR) {
                    s1 = Character.toString(t1.value.charAt(0));
                }

                if (t2.type == Token.Type.TUPLEATTR) {
                    s2 = Character.toString(t2.value.charAt(0));
                }

                if (tuple.token.value.equals(s1) || tuple.token.value.equals(s2)) {
                    counter++;
                }
                
            }
        }

        String[] conditionColumns = new String[counter];
        String[] conditions = new String[counter];
        String[] conditionOperators = new String[counter];

        int counter2 = 0;
        for (int x = this.parseObj.bPosition; x <= this.parseObj.ePosition; x++) {
            if(AtomToken.isCop(this.parseObj.tokens[x])) {
                Token tok1 = this.parseObj.tokens[x-1];
                Token tok2 = this.parseObj.tokens[x+1];
                Token tok3 = this.parseObj.tokens[x];

                String str1 = "";
                String str2 = "";
                if (tok1.type == Token.Type.TUPLEATTR) {
                    str1 = Character.toString(tok1.value.charAt(0));
                }

                if (tok2.type == Token.Type.TUPLEATTR) {
                    str2 = Character.toString(tok2.value.charAt(0));
                }

                if (tuple.token.value.equals(str1) || tuple.token.value.equals(str2)) {
                    conditionColumns[counter2] = this.getRelationDotAttr(tok1);
                    conditions[counter2] = this.getRelationDotAttr(tok2);
                    conditionOperators[counter2] = tok3.value;
                    counter2++;
                }
            }
        }

        tuple.setConditions(conditionColumns, conditions, conditionOperators);
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
                    columns[y] = this.getRelationDotAttr(token2);
                    y++;
                }
                x++;
            } while(token2.type != Token.Type.SEPARATOR && i <= this.parseObj.ePosition);
        }
        t.setSelectedColumns(columns);
    }

    private String getRelationDotAttr(Token t) {
        if (t.type != Token.Type.TUPLEATTR) {
            return t.value;
        }
        String tupleAttr = t.value;
        String tupleVar = tupleAttr.split("\\.")[0];
        String attr = tupleAttr.split("\\.")[1];
        for (int i = this.parseObj.bPosition; i <= this.parseObj.ePosition; i++)  {
            if (this.parseObj.tokens[i].type == Token.Type.RANGEREL &&
                    this.parseObj.tokens[i+2].value.equals(tupleVar)) {
                return this.parseObj.tokens[i].value + "." + attr;
            }
        }
        return tupleAttr;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kgosafomaafo;

/**
 *
 * @author Dennis
 */
public class Formula1 extends Formula{

    private Token rangeRel;
    private Token tupleVar;

    public Formula1(Token rangeRel, Token tupleVar) {
        this.type = Formula.Type.ONE;
        this.rangeRel = rangeRel;
        this.tupleVar = tupleVar;
    }

    public Token getRangeRel() {
        return this.rangeRel;
    }

    public Token getTupleVar() {
        return this.tupleVar;
    }

    public String getString() {
        String str = this.rangeRel.value + "(" + this.tupleVar.value +
                ")";
        return str;
    }
}

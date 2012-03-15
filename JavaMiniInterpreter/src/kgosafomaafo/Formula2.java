/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kgosafomaafo;

/**
 *
 * @author Dennis
 */
public class Formula2 extends Formula {

    Token lhs = null; //left-hand-side
    Token cop = null; //comparison operator
    Token rhs = null; //right-hand-side

    public Formula2() {
        
    }

    private boolean setLhs(Token t) {
        if (t.type != Token.Type.TUPLEATTR && t.type != Token.Type.CONST) {
            return false;
        }
        this.lhs = t;
        return true;
    }

    private boolean setCop(Token t) {
        if (!AtomToken.isCop(t)) {
            return false;
        }
        this.cop = t;
        return true;
    }

    private boolean setRhs(Token t) {
        if (t.type != Token.Type.TUPLEATTR && t.type != Token.Type.CONST) {
            return false;
        }
        this.rhs = t;
        return true;
    }

    public boolean initialize(Token t1, Token t2, Token t3) {
        if (!(this.setLhs(t1) && this.setCop(t2) && this.setRhs(t3))) {
            this.lhs = null;
            this.cop = null;
            this.rhs = null;
            return false;
        }
        this.type = Formula.Type.TWO;
        return true;
    }

    @Override
    public String getString() {
        String str = null;
        if (this.lhs != null && this.cop != null && this.rhs != null &&
                this.type != null) {
            str = this.lhs.value + this.cop.value + this.rhs.value;
            return str;
        }
        return str;
    }
}

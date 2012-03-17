/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kgosafomaafo;

/**
 *
 * @author Dennis
 */
public class Formula3 extends Formula {

    Formula lhs = null; //left-hand-side
    Token lop = null; //logical operator
    Formula rhs = null; //right-hand-side

    public Formula3() {
        
    }

    private boolean setLhs(Formula f) {
        this.lhs = f;
        return true;
    }

    private boolean setLop(Token t) {
        if (t.type != Token.Type.LOP_AND || t.type != Token.Type.LOP_NOT ||
                t.type != Token.Type.LOP_OR) {
            return false;
        }
        this.lop = t;
        return true;
    }

    private boolean setRhs(Formula f) {
        this.rhs = f;
        return true;
    }

    public boolean initialize(Formula f1, Token t1, Formula f2) {
        if (!(this.setLhs(f1) && this.setLop(t1) && this.setRhs(f2))) {
            this.lhs = null;
            this.lop = null;
            this.rhs = null;
            return false;
        }
        this.type = Formula.Type.TWO;
        return true;
    }

    @Override
    public Formula getLhs() {
        return this.lhs;
    }

    @Override
    public Token getLop() {
        return this.lop;
    }

    @Override
    public Formula getRhs() {
        return this.rhs;
    }

    @Override
    public String getString() {
        String str = "";
        if (this.lhs != null && this.lop != null && this.rhs != null &&
                this.type != null) {
            if (this.lhs.type == Formula.Type.ONE) {
                str += this.lhs.getString();
            } else if (this.lhs.type == Formula.Type.TWO) {
                str += this.lhs.getString();
            } else if (this.lhs.type == Formula.Type.THREE) {
                str += "(" + this.lhs.getLhs().getString();
                str += this.lhs.getLop().value;
                str += this.lhs.getRhs().getString() + ")";
            } else {
                
            }

            str += this.lop;

            if (this.rhs.type == Formula.Type.ONE) {
                str += this.rhs.getString();
            } else if (this.rhs.type == Formula.Type.TWO) {
                str += this.rhs.getString();
            } else if (this.rhs.type == Formula.Type.THREE) {
                str += "(" + this.rhs.getLhs().getString();
                str += this.rhs.getLop().value;
                str += this.rhs.getRhs().getString() + ")";
            } else {

            }
        }
        return str;
    }

}

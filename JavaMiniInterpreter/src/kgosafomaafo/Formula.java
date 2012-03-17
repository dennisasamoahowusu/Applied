/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kgosafomaafo;

/**
 *
 * @author Dennis
 */
public class Formula {

    public Token lhs = null;
    public Token cop = null;
    public Token rhs = null;

    public enum Type {
            ONE,
            TWO,
            THREE,
    }

    public Type type = null;

    public Formula() {
        
    }

    public String getString() {
        return null;
    }

    public Formula getLhs() {
        return null;
    }

    public Formula getRhs() {
        return null;
    }

    public Token getLop() {
        return null;
    }
}

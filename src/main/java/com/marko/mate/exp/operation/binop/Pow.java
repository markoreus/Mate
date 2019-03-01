/*
 * Copyright (C) 2018 Marcos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.marko.mate.exp.operation.binop;

import com.marko.mate.exp.Expression;
import com.marko.mate.exp.operation.Operation;
import com.marko.mate.exp.operation.unop.Ln;
import com.marko.mate.exp.symbol.Symbol;
import com.marko.mate.exp.symbol.Variable;
import com.marko.mate.exp.vectorial.RNumber;
import com.marko.mate.exp.vectorial.Number;
import com.marko.mate.exp.vectorial.Space;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Marcos
 */
public class Pow extends BinaryOperation {
    
    /**
     * BigDecimal used in the simplify method to decide the maximum value to
     * simplify in case they are numerical expressions 
     */
    private final BigDecimal MAX_VALUE_SIMPLIFY = new BigDecimal("10");

    public Pow(Expression base, Expression exponent) {
        super(new Expression[]{base, exponent});
    }

    /**
     * Evaluates this pow operation.
     * @param point
     * @return {@link Space} if all the symbols are evaluated,
     * {@link Expression} otherwise.
     */
    @Override
    public Expression evaluate(Map<Symbol, Space> point) {
        return (exprs.get(0).evaluate(point)).pow(
                (exprs.get(1).evaluate(point)).simplify());
    }
    
    /**
     * <p>
     * Derivates a pow operation in an analitical way. When the exponent has
     * the variable var this throw an UnsupportedOperationException.
     * 
     * @param var
     * @return {@link Expression}
     */
    @Override
    public Expression derivate(Variable var) {
        if (exprs.get(1) instanceof Operation) {
            if (((Operation) exprs.get(1)).contains(var)) {
                return derivateWithVar(var);
            }
        }
        return new Multiplication(
                exprs.get(1),
                new Pow(
                        exprs.get(0),
                        new Sum(
                                exprs.get(1),
                                Number.parseNumber("-1")
                        ).simplify()
                ).simplify(),
                exprs.get(0).derivate(var)
        ).simplify();
    }

    /**
     * <p>
     * this method is for the derivation when the pow operation contains the
     * param var in the exponent
     *
     * @param var
     * @return
     */
    private Expression derivateWithVar(Variable var) {
        Multiplication mult = new Multiplication();
        mult.addExp(this);

        Sum sum = new Sum();
        sum.addExp(new Division(exprs.get(1), var));
        sum.addExp(
                new Multiplication(
                        exprs.get(1).derivate(var),
                        new Ln(var)
                )
        );
        throw new UnsupportedOperationException("Not supported yet");
    }

    /**
     * 
     * @return true only if the base is zero, false otherwise. 
     */
    @Override
    public boolean isZero() {
        return exprs.get(0).isZero();
    }

    @Override
    public String toString() {
        return "(" + exprs.get(0).toString()
                + ")^(" + exprs.get(1).toString() + ")";
    }

    /**
     * <p>
     * Evalutes both pow operations and compare the result. If the result is
     * different then, it compares the base and the exponent separately.
     * 
     * @param obj
     * @return true if equals, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pow) {
            Pow pow = (Pow) obj;
            if (pow.evaluate(null).equals(evaluate(null))) {
                return true;
            }

            return exprs.get(0).equals(pow.exprs.get(0))
                    && exprs.get(1).equals(exprs.get(1));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(exprs.get(1));
        return hash;
    }

    /**
     * It simplifies this pow operation.This operation only takes effect
     * when the base or the exponent is an instance of Number
     * 
     * @return {@link Expression} 
     */
    @Override
    public Expression simplify() {
        Expression base = exprs.get(0).simplify(),
                exponent = exprs.get(1).simplify();

        if (base instanceof Number && exponent instanceof Number) {
            if (((Number) base).pow((Number) exponent).value().abs().compareTo(MAX_VALUE_SIMPLIFY) == -1) {
                return new RNumber(((Number) base).pow((Number) exponent).value().doubleValue());
            }
        }

        if (base instanceof Number) {
            if (((Number) base).compareTo(RNumber.ONE) == 0) {
                return base;
            }
            if (((Number) base).compareTo(RNumber.ZERO) == 0) {
                return base;
            }
        }

        if (exponent instanceof Number) {
            if (((Number) exponent).compareTo(RNumber.ONE) == 0) {
                return base;
            }

            if (((Number) exponent).compareTo(RNumber.ZERO) == 0) {
                return RNumber.ONE;
            }
        }

        return new Pow(base, exponent);
    }

}

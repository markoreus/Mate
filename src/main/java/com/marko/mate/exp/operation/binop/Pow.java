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

    private final BigDecimal MAX_VALUE_SIMPLIFY = new BigDecimal("10");

    public Pow(Expression base, Expression exponent) {
        super(new Expression[]{base, exponent});
    }

    @Override
    public Expression evaluate(Map<Symbol, Space> point) {
        return (exprs.get(0).evaluate(point)).pow(
                (exprs.get(1).evaluate(point)).simplify());
    }

    @Override
    public Expression derivate(Variable var) {

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
     * this method is for the derivation when the pow operation
     * contains the param var in the exponent
     * @param var
     * @return
     */
    @SuppressWarnings("unused")
	private Expression derivateWithVar(Variable var) {
        Multiplication mult = new Multiplication();
        mult.addExp(this);

        Sum sum = new Sum();
        sum.addExp(new Division(exprs.get(1), var));
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public boolean isZero() {
        return exprs.get(0).isZero();
    }

    @Override
    public String toString() {
        return "(" + exprs.get(0).toString()
                + ")^(" + exprs.get(1).toString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pow) {
            Pow pow = (Pow) obj;
            return exprs.get(0).equals(pow.exprs.get(0)) && 
                    exprs.get(1).equals(exprs.get(1));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(exprs.get(1));
        return hash;
    }

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

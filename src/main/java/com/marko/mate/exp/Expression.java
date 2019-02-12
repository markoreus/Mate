/*
 * Copyright (C) 2018 Marcos <your.name at your.org>
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
package com.marko.mate.exp;

import com.marko.mate.exp.operation.binop.Division;
import com.marko.mate.exp.operation.binop.Multiplication;
import com.marko.mate.exp.operation.binop.Pow;
import com.marko.mate.exp.operation.binop.Sum;
import com.marko.mate.exp.symbol.Symbol;
import com.marko.mate.exp.symbol.Variable;
import com.marko.mate.exp.vectorial.RNumber;
import com.marko.mate.exp.vectorial.Space;
import java.util.Map;


/**
 *
 * @author Marcos
 */
public interface Expression {

    public Expression derivate(Variable var);

    public default boolean isZero() {
        return false;
    }

    public Expression simplify();

    public Expression evaluate(Map<Symbol, Space> point);

    @Override
    public boolean equals(Object obj);

    @Override
    public String toString();

    public default Expression add(Expression exp) {
        return new Sum(
                this,
                exp
        ).simplify();
    }

    public default Expression substract(Expression exp) {
        return new Sum(
                this,
                new Multiplication(
                        RNumber.parseNumber("-1"),
                        exp
                )
        ).simplify();
    }

    public default Expression multiply(Expression exp) {
        return new Multiplication(
                this, exp
        ).simplify();
    }

    public default Expression divide(Expression exp) {
        return new Division(
                this,
                exp
        ).simplify();
    }

    public default Expression pow(Expression exp) {
        return new Pow(
                this, exp
        ).simplify();
    }

    public default Expression negate() {
        return new Multiplication(
                RNumber.parseNumber("-1"),
                this
        ).simplify();
    }

    public default Expression inverse() {
        return new Division(
                RNumber.parseNumber("1"),
                this
        ).simplify();
    }

}

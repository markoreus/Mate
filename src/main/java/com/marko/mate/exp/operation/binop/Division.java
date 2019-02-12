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
import com.marko.mate.exp.vectorial.QNumber;
import com.marko.mate.exp.vectorial.Space;
import java.util.Map;

/**
 *
 * @author Marcos
 */
public class Division extends BinaryOperation {

    public Division(Expression numerator, Expression denominator) {
        super(new Expression[]{numerator, denominator});
        if (exprs.get(1).isZero()) {
            throw new IllegalStateException("Zero in denominator");
        }

    }

    @Override
    public Expression evaluate(Map<Symbol, Space> point) {

        return exprs.get(0).evaluate(point).divide(
                exprs.get(1).evaluate(point)).simplify();
    }

    @Override
    public Expression derivate(Variable var) {
        if (exprs.size() == 1) {
            return exprs.get(0).derivate(var);
        }

        if (exprs.isEmpty()) {
            return new RNumber(0);
        }

        if (exprs.stream().allMatch(e -> e instanceof Number)) {
            return new RNumber(0);
        }

        Expression num = exprs.get(0),
                den = exprs.get(1);

        return new Division(
                new Sum(
                        new Multiplication(
                                num.derivate(var),
                                den
                        ),
                        new Multiplication(
                                Number.parseNumber("-1"),
                                num,
                                den.derivate(var)
                        )
                ).simplify(),
                new Pow(
                        den,
                        Number.parseNumber("-2")
                )
        ).simplify();

    }

    @Override
    public boolean isZero() {
        return exprs.get(0).isZero();
    }

    @Override
    public Expression simplify() {
        Expression num = exprs.get(0),
                den = exprs.get(1);

        if (den.equals(new RNumber(1))) {
            return num;
        }
        if (exprs.stream().allMatch(e -> e instanceof Number)) {
            return new QNumber(
                    (Number) num,
                    (Number) den
            ).simplify();
        }

        if (num.equals(den)) {
            return RNumber.ONE;
        }

        return new Division(
                num.simplify(),
                den.simplify()
        );
    }

}

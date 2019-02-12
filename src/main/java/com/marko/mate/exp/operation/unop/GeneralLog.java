/*
 * Copyright (C) 2019 Marcos 
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
package com.marko.mate.exp.operation.unop;

import com.marko.mate.exp.Expression;
import com.marko.mate.exp.operation.binop.Division;
import com.marko.mate.exp.operation.binop.Multiplication;
import com.marko.mate.exp.symbol.Variable;
import com.marko.mate.exp.vectorial.RNumber;

/**
 *
 * @author Marcos
 */
public abstract class GeneralLog extends UnaryOperation {

    protected final RNumber base;

    public GeneralLog(Expression exp, RNumber base) {
        super(exp);
        this.base = base;
    }

    @Override
    public Expression derivate(Variable var) {
        return new Multiplication(
                new Division(
                        RNumber.ONE,
                        exprs.get(0)
                ),
                exprs.get(0).derivate(var)
        ).simplify();
    }

    @Override
    public boolean isZero() {
        return exprs.get(0).equals(RNumber.ONE);
    }

    @Override
    public String toString() {
        return "Log" + base + "[" + exprs.get(0) + "]";
    }

}

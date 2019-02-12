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
import com.marko.mate.exp.symbol.Symbol;
import com.marko.mate.exp.vectorial.RNumber;
import com.marko.mate.exp.vectorial.Number;
import com.marko.mate.exp.vectorial.Space;
import java.util.Map;

/**
 *
 * @author Marcos
 */
public class Ln extends GeneralLog {

    public Ln(Expression exp) {
        super(exp, new RNumber(Math.E));
    }

    @Override
    public Space evaluate(Map<Symbol, Space> point) {
        return new RNumber(
                Math.log(
                        (((Number) exprs.get(0).evaluate(point)).value().doubleValue())
                )
        );
    }

    @Override
    public String toString() {
        return "Ln[" + exprs.get(0) + "]";
    }

    @Override
    public Expression simplify() {
        return new Ln(exprs.get(0).simplify());
    }

}

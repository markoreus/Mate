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
import com.marko.mate.exp.symbol.Constant;
import com.marko.mate.exp.symbol.Symbol;
import com.marko.mate.exp.symbol.Variable;
import com.marko.mate.exp.vectorial.RNumber;
import com.marko.mate.exp.vectorial.Number;
import com.marko.mate.exp.vectorial.Space;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 *
 * @author Marcos
 */
public class Sum extends BinaryOperation {

    public Sum(Expression... expressions) {
        super(expressions);
    }

    public Sum() {
        super();
    }

    @Override
    public Expression evaluate(Map<Symbol, Space> point) {

        Expression space = RNumber.ZERO;

        for (var exp : exprs) {
            space = space.add(exp.evaluate(point));
        }

        return space.simplify();
    }

    @Override
    public Expression derivate(Variable var) {

        if (exprs.isEmpty()) {
            return RNumber.ZERO;
        }
        if (exprs.size() == 1) {
            return exprs.get(0).derivate(var);
        }
        Expression[] exp = new Expression[exprs.size()];
        int index = 0;
        for (var expression : exprs) {
            exp[index++] = expression.derivate(var);
        }
        return new Sum(exp).simplify();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Sum) {
            return super.equals(obj);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(exprs);
        return hash;
    }

    @Override
    public boolean isZero() {
        var exp = this.simplify();
        if (exp instanceof Number) {
            return exp.equals(RNumber.ZERO);
        }
        return false;
    }

    @Override
    public Expression simplify() {

        for (var exp : exprs) {
            if (exp instanceof Sum) {
                Sum expSum = (Sum) exp;
                exprs.remove(exp);
                exprs.addAll(expSum.exprs);
                return simplify();
            }
        }

        if (exprs.isEmpty()) {
            return RNumber.ZERO;
        }
        if (exprs.size() == 1) {
            return exprs.get(0).simplify();
        }
        Number n = RNumber.ZERO;
        if (exprs.stream().allMatch(e -> e instanceof Number)) {

            for (var exp : exprs) {
                n = (Number) n.add((Number) exp);
            }
            return n;
        }
        LinkedList<Expression> list = new LinkedList<>();
        LinkedList<Symbol> symList = new LinkedList<>();
        for (var exp : exprs) {
            if (exp instanceof Number) {
                n = (RNumber) n.add((RNumber) exp);
            } else if (exp instanceof Constant) {
                symList.add((Constant) exp);
            } else if (exp instanceof Operation) {
                list.add(exp.simplify());
            }

        }

        Stream<Symbol> streamSym = symList.stream().distinct();
        streamSym.forEach((var t) -> {
            int count = 0;
            count = symList.stream().filter((cons) -> (t.equals(cons))).map((_item) -> 1).reduce(count, Integer::sum);
            if (count == 1) {
                list.addFirst((Symbol)t);
            } else {
                list.addFirst(new Multiplication((Symbol) t, Number.parseNumber(count + "")));
            }
        });

        if (!n.equals(RNumber.ZERO)) {
            list.addFirst(n);
        }

        Expression[] exps = new Expression[list.size()];
        for (int i = 0; i < exps.length; i++) {
            exps[i] = list.get(i);
        }
        return new Sum(exps);

    }

    public void addExp(Expression exp) {
        exprs.add(exp);
    }

}

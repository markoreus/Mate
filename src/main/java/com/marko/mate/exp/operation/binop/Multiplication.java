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
import com.marko.mate.exp.symbol.Symbol;
import com.marko.mate.exp.symbol.Variable;
import com.marko.mate.exp.vectorial.RNumber;
import com.marko.mate.exp.vectorial.Number;
import com.marko.mate.exp.vectorial.Space;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Stream;

/**
 *
 * @author Marcos
 */
public class Multiplication extends BinaryOperation {

    public Multiplication(Expression... exprs) {
        super(exprs);
    }
    
    public Multiplication(){
        super();
    }
    
    public void addExp(Expression exp){
        exprs.add(exp);
    }

    @Override
    public Expression evaluate(Map<Symbol, Space> point) {

        if (exprs.isEmpty()) {
            return RNumber.ZERO;
        }
        Expression space = RNumber.ONE;
        for (var exp : exprs) {
            space = space.multiply(exp.evaluate(point));
        }

        return space;
    }

    @Override
    public Expression derivate(Variable var) {
        if (exprs.size() == 1) {
            return exprs.get(0).derivate(var).simplify();
        }

        if (exprs.size() == 2) {
            return new Sum(
                    new Multiplication(
                            exprs.get(0).derivate(var).simplify(),
                            exprs.get(1)
                    ),
                    new Multiplication(
                            exprs.get(0),
                            exprs.get(1).derivate(var).simplify()
                    )
            );
        }

        LinkedList<Expression> listLowHalf = new LinkedList<>(
                exprs.subList(0, exprs.size() / 2));
        LinkedList<Expression> listHighHalf = new LinkedList<>(
                exprs.subList(exprs.size() / 2, exprs.size()));
        Expression[] a = new Expression[listLowHalf.size()];
        listLowHalf.toArray(a);
        Expression[] b = new Expression[listHighHalf.size()];
        listHighHalf.toArray(b);
        Multiplication multLowHalf = new Multiplication(
                a
        );
        Multiplication multHighHalf = new Multiplication(
                b
        );
        return new Multiplication(
                multLowHalf,
                multHighHalf
        ).derivate(var).simplify();
    }

    @Override
    public boolean isZero() {
        return exprs.stream().anyMatch(e -> e.isZero());
    }

    @Override
    public Expression simplify() {

        for (var exp : exprs) {
            if (exp instanceof Multiplication) {
                Multiplication expMult = (Multiplication) exp;
                exprs.remove(exp);
                exprs.addAll(expMult.exprs);
                return simplify();
            }
        }

        if (exprs.isEmpty()
                || exprs.contains(RNumber.ZERO)) {
            return RNumber.ZERO;
        }
        if (exprs.size() == 1) {
            return exprs.get(0).simplify();
        }
        Number n = RNumber.ONE;
        if (exprs.stream().allMatch(e -> e instanceof Number)) {

            for (Expression exp : exprs) {
                n = (Number) n.multiply((Number) exp);
            }
            return n;
        }
        LinkedList<Expression> list = new LinkedList<>();
        LinkedList<Symbol> simList = new LinkedList<>();
        for (Expression exp : exprs) {
            if (exp instanceof Number) {
                n = (Number) n.multiply((Number) exp);
            } else if (exp instanceof Symbol) {
                simList.add((Symbol) exp);
            } else if (exp instanceof Operation) {
                list.add(exp.simplify());
            }
        }

        if (n.equals(RNumber.ZERO)) {
            return RNumber.ZERO;
        }

        Stream<Symbol> streamSym = simList.stream().distinct();
        streamSym.forEach((t) -> {
            int count = 0;
            count = simList.stream().filter((cons) -> (t.equals(cons))).map((_item) -> 1).reduce(count, Integer::sum);
            if (count == 1) {
                list.addFirst(t);
            } else {
                list.addFirst(new Pow(t, RNumber.parseNumber(count + "")));
            }
        });

        if (!n.equals(RNumber.ONE)) {
            list.addFirst(n);
        }

        if (list.stream().anyMatch(e -> e.isZero())) {
            return RNumber.ZERO;
        }
        Expression[] exps = new Expression[list.size()];
        for (int i = 0; i < exps.length; i++) {
            exps[i] = list.get(i);
        }
        return new Multiplication(exps);

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Multiplication) {
            return super.equals(obj);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

}

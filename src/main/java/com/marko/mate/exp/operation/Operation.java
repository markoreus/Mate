/*
 * Copyright (C) 2018 
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
package com.marko.mate.exp.operation;

import com.marko.mate.exp.Expression;
import com.marko.mate.exp.symbol.Variable;
import com.marko.mate.exp.vectorial.Matrix;
import com.marko.mate.exp.vectorial.Vector;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 *
 * @author Marcos
 */
public abstract class Operation implements Expression {

    protected final LinkedList<Expression> exprs;

    public Operation(Expression... exprs) {
        LinkedList<Expression> tmp = new LinkedList<>();
        for (Expression exp : exprs) {
            tmp.add(exp.simplify());
        }
        this.exprs = tmp;
    }

    public Operation() {
        exprs = new LinkedList<>();
    }

    public LinkedList<Expression> getExpressions() {
        return exprs;
    }

    public LinkedList<Variable> getVars() {

        LinkedList<Variable> list = new LinkedList<>();

        exprs.stream().map((exp) -> {
            if (exp instanceof Operation) {
                list.addAll(((Operation) exp).getVars());
            }
            return exp;
        }).filter((exp) -> (exp instanceof Variable)).forEach((exp) -> {
            list.add((Variable) exp);
        });

        Stream<Variable> distinct = list.stream().distinct();
        LinkedList<Variable> listReturn = new LinkedList<>();
        distinct.forEach(e -> listReturn.add(e));

        return listReturn;
    }

    public Vector gradient() {

        LinkedList<Variable> list = getVars();
        Expression[] expArr = new Expression[list.size()];
        int i = 0;

        for (var var : list) {
            expArr[i++] = derivate(var).simplify();
        }

        return new Vector(expArr);
    }

    public Matrix hessian() {

        LinkedList<Variable> list = getVars();
        Vector vector = gradient();
        Expression[][] data = new Expression[vector.getData().length][list.size()];
        int i = 0, j = 0;

        for (var exp : vector.getData()) {
            for (var var : list) {
                data[i][j++] = exp.derivate(var).simplify();
            }
            i++;
            j = 0;
        }
        
        return new Matrix(data);
    }

}

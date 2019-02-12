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
package com.marko.mate.exp.vectorial;

import com.marko.mate.exp.Expression;
import com.marko.mate.exp.operation.binop.Division;
import com.marko.mate.exp.operation.binop.Multiplication;
import com.marko.mate.exp.operation.binop.Pow;
import com.marko.mate.exp.operation.binop.Sum;
import com.marko.mate.exp.symbol.Symbol;
import com.marko.mate.exp.symbol.Variable;
import java.util.Map;

/**
 *
 * @author Marcos
 */
public class Vector extends VectorialSpace {

    public Vector(Expression... data) {
        super(data);
    }

    public Vector(int length) {
        this(new Expression[length]);
    }

    public Vector() {
        this(new Expression[]{RNumber.ZERO, RNumber.ZERO, RNumber.ZERO, RNumber.ZERO});
    }

    public Expression getExpAt(int i) {
        return data[0][i];
    }

    public int size() {
        return data[0].length;
    }

    public Vector valuesFrom(int fromIndex, int toIndex) {
        Expression[] newData = new Expression[toIndex - fromIndex];

        int j = 0;
        for (int i = fromIndex; i < toIndex; i++) {
            newData[j++] = data[0][i];
        }

        return new Vector(newData);
    }
    
    @Override
    public Vector multiply(Expression exp) {
    	if(exp instanceof Space) {
    		return multiply((Space)exp);
    	}
    	
    	Expression[] newData = new Expression[size()];
    	int i = 0;
    	for(var var : data[0]) {
    		newData[i++] = new Multiplication(
    					var,exp
    				).simplify();
    	}
    	
    	return new Vector(newData);
    }

    public Vector multiply(Space space) {

        if (space instanceof Number) {
            return multiply((Number) space);
        }

        if (space instanceof Vector) {
            return multiply((Vector) space);
        }

        return multiply((Matrix) space);
    }

    private Vector multiply(Vector vector) {

        if (vector.size() != size()) {
            throw new ArithmeticException("Must have same size");
        }

        Sum sum = new Sum();
        int i = 0;

        for (var exp : data[0]) {
            sum.addExp(
                    new Multiplication(
                            exp,
                            vector.data[0][i++]
                    ).simplify()
            );
        }

        return new Vector(new Expression[]{sum.simplify()});
    }

    private Vector multiply(Number number) {

        Expression[] newData = new Expression[data[0].length];

        int i = 0;
        for (var exp : data[0]) {
            newData[i++] = new Multiplication(
                    exp,
                    number
            ).simplify();
        }

        return new Vector(newData);

    }
    
    @Override
    public Vector add(Expression exp) {
    	if(exp instanceof Space) {
    		return add((Space)exp);
    	}
    	
    	Expression[] newData = new Expression[size()];
    	int i = 0;
    	for(var var : data[0]) {
    		newData[i++] = new Sum(
    					var,exp
    				).simplify();
    	}
    	
    	return new Vector(newData);
    }
    
    private Vector add(Space space) {

        if (space instanceof Number) {
            return add((Number) space);
        }

        return add((Vector) space);
    }

    private Vector add(Number number) {

        Expression[] newData = new Expression[data[0].length];

        int i = 0;
        for (var exp : data[0]) {
            newData[i++] = new Sum(
                    exp,
                    number
            ).simplify();
        }

        return new Vector(newData);

    }

    private Vector add(Vector vector) {

        if (vector.data[0].length != data[0].length) {
            throw new ArithmeticException("Must be same size");
        }

        Expression[] newData = new Expression[data[0].length];

        for (int i = 0; i < data[0].length; i++) {
            newData[i] = new Sum(
                    data[0][i],
                    vector.data[0][i]
            ).simplify();
        }

        return new Vector(newData);

    }

    @Override
    public Vector substract(Expression exp) {
    	
    	if(exp instanceof Space) {
    		return substract((Space)exp);
    	}
    	
    	Expression[] newData = new Expression[data[0].length];

        int i = 0;
        for (var var : data[0]) {
            newData[i++] = new Sum(
                    var,
                    new Multiplication(
                            RNumber.parseRNumber("-1"),
                            exp
                    )
            ).simplify();
        }

        return new Vector(newData);
    	
    }
    
    private Vector substract(Space space) {
        if (space instanceof Number) {
            return substract((Number) space);
        }
        return substract((Vector) space);
    }

    private Vector substract(Vector vector) {

        if (vector.data[0].length != data[0].length) {
            throw new ArithmeticException("Must have same size");
        }

        Expression[] newData = new Expression[data[0].length];

        for (int i = 0; i < data[0].length; i++) {
            newData[i] = new Sum(
                    data[0][i],
                    new Multiplication(
                            RNumber.parseRNumber("-1"),
                            vector.data[0][i]
                    )
            ).simplify();
        }

        return new Vector(newData);
    }

    private Vector substract(Number number) {

        Expression[] newData = new Expression[data[0].length];

        int i = 0;
        for (var exp : data[0]) {
            newData[i++] = new Sum(
                    exp,
                    new Multiplication(
                            RNumber.parseRNumber("-1"),
                            number
                    )
            ).simplify();
        }

        return new Vector(newData);
    }

    @Override
    public Vector derivate(Variable var) {
        Expression[] newData = new Expression[data[0].length];

        int i = 0;
        for (var exp : data[0]) {
            newData[i++] = exp.derivate(var);
        }

        return new Vector(newData);
    }

    @Override
    public boolean isZero() {

        for (var exp : data[0]) {
            if (!exp.isZero()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Vector simplify() {

        Expression[] newData = new Expression[data[0].length];

        int i = 0;
        for (var exp : data[0]) {
            newData[i++] = exp.simplify();
        }

        return new Vector(newData);

    }

    @Override
    public Matrix transpose() {

        Expression[][] newData = new Expression[data[0].length][1];
        int i = 0;

        for (var exp : data[0]) {
            newData[i++][0] = exp;
        }

        return new Matrix(newData);
    }

    @Override
    public String toString() {

        String str = "[";
        for (int i = 0; i < data[0].length - 1d; i++) {
            str += data[0][i] + ",";
        }
        return str += data[0][data[0].length - 1] + "]";
    }

    public static Vector parseVector(String vector) {

        if (vector.startsWith(",") || vector.endsWith(",")) {
            throw new ArithmeticException("Illegal vector");
        }

        int size = 0;
        int index = 0;
        String number = "";
        Number[] data;

        for (char a : vector.toCharArray()) {
            if (a == ',') {
                size++;
            }
        }

        data = new Number[size + 1];
        int i = 0;
        for (char a : vector.toCharArray()) {
            i++;
            if (a == ',') {
                data[index++] = Number.parseNumber(number);
                number = "";
            } else {
                number += a;
                if (i == vector.toCharArray().length) {
                    data[index++] = Number.parseNumber(number);
                }
            }

        }

        return new Vector(data);
    }

    public Pow norm() {
        Sum sum = new Sum();
        for (var exp : data[0]) {
            sum.addExp(
                    new Pow(
                            exp,
                            RNumber.parseRNumber("2")
                    ).simplify()
            );
        }
        return new Pow(
                sum.simplify(),
                new QNumber(
                        RNumber.ONE,
                        RNumber.parseRNumber("2")
                ).simplify()
        );
    }

    public Expression[] getData() {
        return data[0];
    }

    @Override
    public Vector divide(Expression exp) {
    	
    	if(exp instanceof Space) {
    		return divide((Space)exp);
    	}
    	
    	Expression[] newData = new Expression[data[0].length];
        int i = 0;
        for (var var : data[0]) {
            newData[i++] = new Division(var, exp).simplify();
        }
        return new Vector(newData);
    	
    }
    
    
    private Vector divide(Space space) {

        if (space instanceof Number) {
            return multiply(((Number)space).inverse());
        }

        if(space instanceof Vector) {
        	return multiply(((Vector)space).inverse());
        }
        
        throw new ArithmeticException("Invalid division Vector with " + space.getClass().getSimpleName());
    }

    @Override
    public Vector evaluate(Map<Symbol, Space> point) {
        Expression[] newData = new Expression[data[0].length];
        int i = 0;

        for (var exp : data[0]) {
            newData[i++] = exp.evaluate(point);
        }

        return new Vector(newData);
    }

    @Override
    public Vector negate() {

        Expression[] newData = new Expression[data[0].length];

        int i = 0;
        for (var exp : data[0]) {
            newData[i++] = exp.negate();
        }

        return new Vector(newData);

    }

    public boolean hasNegative() {

        for (var exp : data[0]) {
            if (((Number) exp).value.doubleValue() < 0) {
                return true;
            }
        }
        return false;

    }

    public boolean hasNegativeMinusLast() {
        for (int i = 0; i < data[0].length - 1; i++) {
            if (((Number) data[0][i]).value.doubleValue() < 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Vector inverse() {

        Expression[] newData = new Expression[data[0].length];

        int i = 0;
        for (var exp : data[0]) {
            newData[i++] = new Division(RNumber.ONE, exp).simplify();
        }

        return new Vector(newData);
    }

    private Vector multiply(Matrix matrix) {

        if (data[0].length != matrix.data.length) {
            throw new ArithmeticException("Incompatible size");
        }

        Expression[] newData = new Expression[matrix.data[0].length];
        Sum sum;

        for (int i = 0; i < matrix.data[0].length; i++) {
            sum = new Sum();
            for (int j = 0; j < matrix.data.length; j++) {
                sum.addExp(
                        new Multiplication(
                                data[0][j],
                                matrix.data[j][i]
                        ).simplify()
                );

            }
            newData[i] = sum.simplify();
        }

        return new Vector(newData);
    }

}

/*
 * Copyright (C) 2019 Marcos <your.name at your.org>
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
public class Matrix extends VectorialSpace {

    public Matrix(Expression[][] data) {
        super(data);
    }

    public Matrix(Vector[] vectors) {
        this(new Expression[vectors.length][vectors[0].size()]);

        int i = 0;
        for (var vector : vectors) {
            data[i++] = vector.getData();
        }
    }

    public static Matrix zero(int dimension) {
        Expression[][] data = new Expression[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                data[i][j] = RNumber.ZERO;
            }
        }
        return new Matrix(data);
    }

    public static Matrix identity(int dimension) {

        Expression[][] data = new Expression[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (i == j) {
                    data[i][j] = RNumber.ONE;
                    continue;
                }
                data[i][j] = RNumber.ZERO;
            }
        }

        return new Matrix(data);
    }

    public Expression getExpAt(int i, int j) {
        return data[i][j];
    }

    public Expression[][] getData() {
        return data;
    }

    public Vector minors() {

        Expression[] newData = new Expression[data.length];
        newData[0] = data[0][0];

        for (int i = 1; i < data.length; i++) {
            newData[i] = subMatrix(i).det();
        }
        return new Vector(newData);
    }

    private Matrix subMatrix(int dimension) {

        Expression[][] newData = new Expression[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            System.arraycopy(data[i], 0, newData[i], 0, dimension);
        }
        return new Matrix(newData);
    }

    public Vector[] toVectorArray() {

        Vector[] vectors = new Vector[data.length];
        int i = 0;

        for (Expression[] exprs : data) {
            vectors[i++] = new Vector(exprs);
        }

        return vectors;
    }

    @Override
    public VectorialSpace multiply(Expression exp) {
    	
    	if(exp instanceof Space) {
    		return multiply((Space) exp);
    	}
    	
    	Expression[][] newData = new Expression[data.length][data[0].length];
    	for (int i = 0; i < data.length; i++) {
			for(int j = 0; i < data.length; j++) {
				newData[i][j] = new Multiplication(
							data[i][j],
							exp
						).simplify();
			}
		}
    	
    	return new Matrix(newData);
    
    }
    
    private VectorialSpace multiply(Space space) {

        if (space instanceof Number) {
            return multiply((Number) space);
        }

        if (space instanceof Vector) {
            return multiply((Vector) space);
        }

        return multiply((Matrix) space);
    }
    
    @Override
    public Matrix add(Expression exp) {
    	
    	if(exp instanceof Space) {
    		return add((Space) exp);
    	}
    	
    	Expression[][] newData = new Expression[data.length][data[0].length];
    	for (int i = 0; i < data.length; i++) {
			for(int j = 0; i < data.length; j++) {
				newData[i][j] = new Sum(
							data[i][j],
							exp
						).simplify();
			}
		}
    	
    	return new Matrix(newData);
    }

    private Matrix add(Space space) {

        if (space instanceof Vector) {
            throw new RuntimeException("Cannot be added");
        }

        if (space instanceof Number) {
            return add((Number) space);
        }

        return add((Matrix) space);
    }
    
    @Override
    public Matrix substract(Expression exp) {
    	
    	if(exp instanceof Space) {
    		return substract((Space)exp);
    	}
    
    	Expression[][] newData = new Expression[data.length][data[0].length];
        int i = 0, j = 0;

        for (Expression[] expArr : data) {
            for (var var : expArr) {
                newData[i][j++] = new Sum(
                        var,
                        new Multiplication(
                                RNumber.parseRNumber("-1"),
                                exp
                        )
                ).simplify();
            }
            i++;
            j = 0;
        }

        return new Matrix(newData);
    	
    }

    private Matrix substract(Space space) {

        if (space instanceof Vector) {
            throw new ArithmeticException("Cannot be substracted");
        }

        if (space instanceof Number) {
            return substract((Number) space);
        }

        return substract((Matrix) space);
    }

    @Override
    public Matrix derivate(Variable var) {
        Expression[][] newData = new Expression[data.length][data[0].length];
        int i = 0, j = 0;

        for (Expression[] expArr : data) {
            for (var exp : expArr) {
                newData[i][j++] = exp.derivate(var);
            }
            i++;
            j = 0;
        }

        return new Matrix(newData);
    }

    @Override
    public boolean isZero() {
        for (Expression[] expArr : data) {
            for (var exp : expArr) {
                if (!exp.isZero()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Matrix simplify() {

        Expression[][] newData = new Expression[data.length][data[0].length];
        int i = 0, j = 0;

        for (Expression[] expArr : data) {
            for (var exp : expArr) {
                newData[i][j++] = exp.simplify();
            }
            i++;
            j = 0;
        }

        return new Matrix(newData);
    }

    private Matrix substract(Number number) {
        Expression[][] newData = new Expression[data.length][data[0].length];
        int i = 0, j = 0;

        for (Expression[] expArr : data) {
            for (var exp : expArr) {
                newData[i][j++] = new Sum(
                        exp,
                        new Multiplication(
                                RNumber.parseRNumber("-1"),
                                number
                        )
                ).simplify();
            }
            i++;
            j = 0;
        }

        return new Matrix(newData);
    }

    private Matrix substract(Matrix matrix) {

        if (data.length != matrix.data.length
                && data[0].length != matrix.data[0].length) {
            throw new IllegalStateException("Cannot be substracted");
        }

        Expression[][] newData = new Expression[data.length][data[0].length];

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                newData[i][j] = new Sum(
                        data[i][j],
                        new Multiplication(
                                RNumber.parseRNumber("-1"),
                                matrix.data[i][j]
                        )
                );
            }
        }

        return new Matrix(newData);

    }

    private Matrix add(Number number) {

        Expression[][] newData = new Expression[data.length][data[0].length];
        int i = 0, j = 0;

        for (Expression[] expArr : data) {
            for (var exp : expArr) {
                newData[i][j++] = new Sum(
                        exp,
                        number
                ).simplify();
            }
            i++;
            j = 0;
        }

        return new Matrix(newData);
    }

    private Matrix add(Matrix matrix) {

        if (data.length != matrix.data.length
                && data[0].length != matrix.data[0].length) {
            throw new IllegalStateException("Cannot be added");
        }

        Expression[][] newData = new Expression[data.length][data[0].length];

        for (int i = 0; i < this.data.length; i++) {
            for (int j = 0; j < this.data[0].length; j++) {
                newData[i][j] = new Sum(
                        this.data[i][j],
                        matrix.data[i][j]
                );
            }
        }

        return new Matrix(newData);
    }

    private Matrix multiply(Matrix matrix) {

        if (data[0].length != matrix.data.length) {
            throw new ArithmeticException("Cannot be multiplied");
        }

        Expression[][] newData = new Expression[data.length][matrix.data[0].length];
        Sum sum;

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < matrix.data[0].length; j++) {

                sum = new Sum();
                for (int k = 0; k < data[0].length; k++) {
                    sum.addExp(
                            new Multiplication(
                                    data[i][k],
                                    matrix.data[k][j]
                            ).simplify()
                    );
                }

                newData[i][j] = sum.simplify();
            }
        }

        return new Matrix(newData);
    }

    private Vector multiply(Vector vector) {

        if (data.length != vector.data.length) {
            throw new ArithmeticException("Cannot be multiplied");
        }
        return new Vector(multiply(vector.transpose()).data[0]);
    }

    private Matrix multiply(Number number) {

        Expression[][] newData = new Expression[data.length][data[0].length];

        int i = 0, j = 0;
        for (Expression[] expArr : data) {
            for (var exp : expArr) {

                newData[i][j++] = new Multiplication(
                        exp,
                        number
                ).simplify();

            }
            j = 0;
            i++;
        }
        return new Matrix(newData);
    }

    @Override
    public Matrix transpose() {
        if (!(data.length == 1 && data[0].length == 1)) {
            return pTranspose(this);
        }
        return this;
    }

    private Matrix pTranspose(Matrix matrix) {
        Expression[][] newData = new Expression[matrix.data[0].length][matrix.data.length];

        for (int i = 0; i < matrix.data[0].length; ++i) {
            for (int j = 0; j < matrix.data.length; ++j) {
                newData[i][j] = matrix.data[j][i];
            }
        }

        return new Matrix(newData);
    }

    @Override
    public Matrix inverse() {
        Matrix inverse = new Matrix(new Expression[data.length][data[0].length]);
        Matrix tmp = new Matrix(new Expression[data.length - 1][data[0].length - 1]);

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                int p = 0, q = 0;
                for (int k = 0; k < data.length; k++) {
                    if (i == k) {
                        continue;
                    }
                    for (int s = 0; s < data[0].length; s++) {
                        if (j == s) {
                            continue;
                        }
                        tmp.data[p][q] = data[k][s].simplify();
                        q++;
                    }
                    p++;
                    q = 0;
                }
                inverse.data[i][j] = new Division(
                        new Multiplication(
                                new Pow(
                                        RNumber.parseRNumber("-1"),
                                        RNumber.parseRNumber((i + j + 2) + "")
                                ).simplify(),
                                pDet(tmp)
                        ).simplify(),
                        det()
                ).simplify();
            }
        }
        inverse = pTranspose(inverse).simplify();
        return inverse;
    }

    public Expression det() {
        return pDet(this);
    }

    private Expression pDet(Matrix matrix) {

        if (matrix.data.length != matrix.data[0].length) {
            throw new ArithmeticException("invalid dimension");
        }

        if (matrix.data.length == 1) {
            return matrix.data[0][0];
        }

        if (matrix.data.length == 2) {
            return new Sum(
                    new Multiplication(
                            matrix.data[0][0],
                            matrix.data[1][1]
                    ).simplify(),
                    new Multiplication(
                            Number.parseNumber("-1"),
                            matrix.data[0][1],
                            matrix.data[1][0]
                    ).simplify()
            ).simplify();
        }

        Expression[][] tmp;
        Sum sum = new Sum();

        for (int i = 0; i < matrix.data[0].length; i++) {
            tmp = new Expression[matrix.data.length - 1][matrix.data[0].length - 1];
            for (int j = 1; j < matrix.data.length; j++) {
                for (int k = 0; k < matrix.data[0].length; k++) {
                    if (k < i) {
                        tmp[j - 1][k] = matrix.data[j][k];
                    } else if (k > i) {
                        tmp[j - 1][k - 1] = matrix.data[j][k];
                    }
                }
            }

            sum.addExp(
                    new Multiplication(
                            matrix.data[0][i],
                            new Pow(
                                    Number.parseNumber("-1"),
                                    Number.parseNumber("" + i)
                            ).simplify(),
                            pDet(new Matrix(tmp))
                    ).simplify()
            );
        }
        return sum.simplify();
    }

    @Override
    public String toString() {
        String str = "{\n";

        for (Expression[] data1 : data) {
            str += "[ ";
            for (int j = 0; j < data[0].length - 1; j++) {
                str += data1[j];
                str += " ,";
            }
            str += data1[data[0].length - 1] + "]\n";
        }

        return str + "}";
    }

    @Override
    public Matrix evaluate(Map<Symbol, Space> point) {

        Expression[][] newData = new Expression[data.length][data[0].length];
        int i = 0, j = 0;

        for (Expression[] expArr : data) {
            for (var exp : expArr) {
                newData[i][j++] = exp.evaluate(point);
            }
            i++;
            j = 0;
        }

        return new Matrix(newData);
    }

    @Override
    public Expression divide(Expression exp) {
    	
    	if(exp instanceof Space) {
    		return divide((Space)exp);
    	}
    	
    	 Expression[][] newData = new Expression[data.length][data[0].length];
         int i = 0, j = 0;

         for (Expression[] expArr : data) {
             for (var var : expArr) {
                 newData[i][j++] = new Division(
                		 	var,exp
                		 ).simplify();
             }
             i++;
             j = 0;
         }

         return new Matrix(newData);
    	
    }
    
    private Matrix divide(Space space) {
        if (space == null) {
            throw new NullPointerException();
        }

        if (space instanceof Matrix) {
            return multiply(((Matrix) space).inverse());
        }
        if(space instanceof Number) {
        	return multiply(((Number)space).inverse());
        }
        throw new ArithmeticException("Invalid division Matrix with " + space.getClass().getSimpleName());
    }

    @Override
    public Matrix negate() {
        Expression[][] newData = new Expression[data.length][data[0].length];
        int i = 0, j = 0;

        for (Expression[] expArr : data) {
            for (var exp : expArr) {
                newData[i][j++] = exp.negate();
            }
            i++;
            j = 0;
        }

        return new Matrix(newData);
    }

    public static Matrix parseMatrix(String str) {

        String number = "";
        int rows = 1,
                columns = 1,
                j = 0,
                k = 0;
        char a;

        for (var c : str.toCharArray()) {
            if (c == ';') {
                rows++;
            }
        }

        for (var c : str.toCharArray()) {
            if (c == ';') {
                break;
            }
            if (c == ',') {
                columns++;
            }
        }

        Number[][] numbers = new Number[rows][columns];

        for (int i = 0; i < str.length(); i++) {
            a = str.charAt(i);
            if (a != ',' && a != ';') {
                number += a;
                continue;
            }
            if (a == ',' || a == ';') {
                numbers[j][k++] = Number.parseNumber(number);
                number = "";
                if (a == ';') {
                    j++;
                    k = 0;
                }
            }

        }
        numbers[j][k] = Number.parseNumber(number);
        return new Matrix(numbers);
    }

}

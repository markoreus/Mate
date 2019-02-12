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
import com.marko.mate.exp.vectorial.RNumber;
import java.util.LinkedList;

/**
 *
 * @author Marcos
 */
public abstract class BinaryOperation extends Operation {

    private static final char SUM = '+',
            MULTIPLICATION = '*',
            DIVISION = '/';

    protected BinaryOperation(Expression... exprs) {
        super(exprs);
    }

    @Override
    public String toString() {

        char symbol = ' ';
        switch (getClass().getSimpleName()) {
            case "Multiplication":
                symbol = MULTIPLICATION;
                break;
            case "Division":
                symbol = DIVISION;
                break;
            case "Sum":
                symbol = SUM;
                break;

        }

        LinkedList<Expression> list = exprs;

        String str = "(";
        for (int i = 0; i < list.size() - 1; i++) {
            if (symbol == MULTIPLICATION && list.get(i).equals(RNumber.parseNumber("-1"))) {
                str += "-";
            } else {
                str += list.get(i) + " " + symbol + " ";
            }
        }
        str += list.get(list.size() - 1);
        return str + ")";

    }

}

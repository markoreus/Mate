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
package com.marko.mate;

/**
 *
 * @author Marcos
 */
public class Util {

    /**
     * <p>
     * This is optimized version of Fibonacci Program. Without using Hashmap and
     * recursion. It saves both memory and time. Space Complexity will be O(1)
     * Time Complexity will be O(n)
     * </p>
     *
     * @param n
     * @return fibonacci of n
     */
    public static long fibonacci(long n) {
        if (n == 0) {
            return 0;
        }
        long prev = 0, res = 1, next;
        for (int i = 2; i <= n; i++) {
            next = prev + res;
            prev = res;
            res = next;
        }
        return res;
    }

    /**
     *
     * @param x
     * @param y
     * @return The greatest comun divisor beetwen x and y
     */
    public static long gcd(long x, long y) {

        long a = Math.abs(x),
                b = Math.abs(y);

        if (a == 0) {
            return b;
        }

        while (b != 0) {
            if (a > b) {
                a -= b;
            } else {
                b -= a;
            }
        }

        return a;
    }

}

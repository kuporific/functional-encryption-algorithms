package com.implementsblog.functional;

import java.util.function.IntUnaryOperator;

/**
 * A function that takes a code point range and returns a function that maps one
 * code point to another code point.
 */
@FunctionalInterface
interface CodePointMapper
{
    IntUnaryOperator apply(int lowerCodePoint, int upperCodePoint);
}

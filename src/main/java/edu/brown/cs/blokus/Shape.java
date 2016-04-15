package edu.brown.cs.blokus;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A shape covers a set of squares.
 * 
 * @author aaronzhang
 */
public enum Shape {
    
    /**
     * X
     */
    I1(0, 0),
    
    /**
     * XO
     */
    I2(0, 0,
    1, 0),
    
    /**
     * OXO
     */
    I3(0, 0,
    -1, 0,
    1, 0),
    
    /**
     * O
     * XO
     */
    V3(0, 0,
    0, 1,
    1, 0),
    
    /**
     * OXOO
     */
    I4(0, 0,
    -1, 0,
    1, 0,
    2, 0),
    
    /**
     * O
     * OXO
     */
    L4(0, 0,
    -1, 0,
    -1, 1,
    1, 0),
    
    /**
     * OO
     * XO
     */
    O4(0, 0,
    0, 1,
    1, 0,
    1, 1),
    
    /**
     *  O
     * OXO
     */
    T4(0, 0,
    -1, 0,
    0, 1,
    1, 0),
    
    /**
     *  OO
     * OX
     */
    Z4(0, 0,
    -1, 0,
    0, 1,
    1, 1),
    
    /**
     *  O
     *  XO
     * OO
     */
    F5(0, 0,
    -1, -1,
    0, -1,
    0, 1,
    1, 0),
    
    /**
     * OOXOO
     */
    I5(0, 0,
    -2, 0,
    -1, 0,
    1, 0,
    2, 0),
    
    /**
     *    O
     * OOXO
     */
    L5(0, 0,
    -2, 0,
    -1, 0,
    1, 0,
    1, 1),
    
    /**
     *   OO
     * OOX
     */
    N5(0, 0,
    -2, 0,
    -1, 0,
    0, 1,
    1, 1),
    
    /**
     * OO
     * XO
     * O
     */
    P5(0, 0,
    0, -1,
    0, 1,
    1, 0,
    1, 1),
    
    /**
     * O
     * OXO
     * O
     */
    T5(0, 0,
    -1, -1,
    -1, 0,
    -1, 1,
    1, 0),
    
    /**
     * OO
     * X
     * OO
     */
    U5(0, 0,
    0, -1,
    0, 1,
    1, -1,
    1, 1),
    
    /**
     * O
     * O
     * XOO
     */
    V5(0, 0,
    0, 1,
    0, 2,
    1, 0,
    2, 0),
    
    /**
     * O
     * OX
     *  OO
     */
    W5(0, 0,
    -1, 1,
    -1, 0,
    0, -1,
    1, -1),
    
    /**
     *  O
     * OXO
     *  O
     */
    X5(0, 0,
    -1, 0,
    0, -1,
    0, 1,
    1, 0),
    
    /**
     *   O
     * OOXO
     */
    Y5(0, 0,
    -2, 0,
    -1, 0,
    0, 1,
    1, 0),
    
    /**
     *  OO
     *  X
     * OO
     */
    Z5(0, 0,
    -1, -1,
    0, -1,
    0, 1,
    1, 1);
    
    /**
     * Squares that this shape covers.
     */
    private final Set<Square> squares;
    
    /**
     * Number of shapes.
     */
    public static final int NUM_SHAPES = 21;
    
    /**
     * New shape with given squares, where each pair of integers represents the
     * x-coordinate and y-coordinate of a square.
     * 
     * @param coordinates coordinates of squares
     */
    private Shape(int... coordinates) {
        this.squares = new HashSet<>();
        for (int i = 0; i < coordinates.length; i += 2) {
            this.squares.add(new Square(coordinates[i], coordinates[i + 1]));
        }
    }
    
    /**
     * Returns set of squares that this shape covers.  One square (not
     * necessarily covered by the shape) is designated the center, represented
     * by the square (0, 0).  The coordinates of all other squares are relative
     * to the center, so the square directly to the left of the center would
     * be (-1, 0), for example.
     * 
     * @return set of squares in this shape
     */
    public Set<Square> getSquares() {
        return Collections.unmodifiableSet(squares);
    }
    
    /**
     * @return number of squares in this shape
     */
    public int size() {
        return squares.size();
    }
    
    /**
     * Whether this shape contains the square.
     * 
     * @param square square
     * @return whether this shape contains the square
     */
    public boolean contains(Square square) {
        return squares.contains(square);
    }
    
    /**
     * Prints each shape on a grid.  Allows visual verification that the shapes
     * have the right squares.
     * 
     * @param args unused
     */
    public static void main(String[] args) {
        final int maxRadius = 2;
        for (Shape shape : values()) {
            System.out.println(shape.toString());
            System.out.println(Square.grid(shape.getSquares(), maxRadius));
            System.out.println();
        }
    }
}
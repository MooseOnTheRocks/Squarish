package dev.foltz.squarish.world.tile;

import dev.foltz.squarish.world.World;
import dev.foltz.squarish.world.cell.Cell;

import java.util.List;
import java.util.function.BiFunction;

public interface ITile {
    int getX();
    int getY();
    int size();
    boolean isEmpty();
    boolean isContiguous(Class<? extends Cell> cellClass);

    float sampleNoise(BiFunction<Float, Float, Float> noiseFunc);

    Cell getCellAt(int x, int y);
    boolean setCellAt(int x, int y, Cell cell);

    default boolean inRange(int ox, int oy) {
        int x = getX();
        int y = getY();
        int size = size();
        return ox >= x
            && ox <  x + size
            && oy >= y
            && oy <  y + size;
    }

    default boolean isAdjacent(int ox, int oy) {
        int x = getX();
        int y = getY();
        int size = size();
        return ox >  x
            && ox <= x + size
            && oy >  y
            && oy <= y + size;
    }
}

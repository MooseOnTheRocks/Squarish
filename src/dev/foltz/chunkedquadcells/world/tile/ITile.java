package dev.foltz.chunkedquadcells.world.tile;

import dev.foltz.chunkedquadcells.world.World;
import dev.foltz.chunkedquadcells.world.cell.Cell;

import java.util.List;

public interface ITile {
    int getX();
    int getY();
    int size();
    boolean isEmpty();
    boolean isContiguous(Class<? extends Cell> cellClass);

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

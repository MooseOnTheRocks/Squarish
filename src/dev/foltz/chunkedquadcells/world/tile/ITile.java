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

    default boolean inRange(int x, int y) {
        return x >= getX()
            && x <  getX() + size()
            && y >= getY()
            && y <  getY() + size();
    }

    default boolean isAdjacent(int x, int y) {
        return inRange(x, y)
            || inRange(x - 1, y)
            || inRange(x + 1, y)
            || inRange(x, y - 1)
            || inRange(x, y + 1)
            || inRange(x - 1, y - 1)
            || inRange(x + 1, y - 1)
            || inRange(x - 1, y + 1)
            || inRange(x + 1, y + 1);
    }
}

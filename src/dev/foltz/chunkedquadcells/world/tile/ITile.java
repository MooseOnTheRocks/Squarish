package dev.foltz.chunkedquadcells.world.tile;

import dev.foltz.chunkedquadcells.world.World;
import dev.foltz.chunkedquadcells.world.cell.Cell;

public interface ITile {
    int getX();
    int getY();
    int size();
    boolean isEmpty();
    boolean isContiguous(Class<? extends Cell> cellClass);
    Cell getCellAt(int x, int y);
    boolean setCellAt(int x, int y, Cell cell);
    void update(World world);
    boolean shouldUpdate();
    void markDirty();
}

package dev.foltz.chunkedquadcells.world.cell;

import dev.foltz.chunkedquadcells.world.World;

public abstract class Cell {
    public static final int CELL_SIZE = 8;
    public int lastTick = 0;

    public abstract int getColor();
    public void update(World world, int x, int y) {}
    public boolean isEmpty() { return false; }
}

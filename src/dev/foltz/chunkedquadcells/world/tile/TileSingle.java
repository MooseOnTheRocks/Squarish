package dev.foltz.chunkedquadcells.world.tile;

import dev.foltz.chunkedquadcells.world.World;
import dev.foltz.chunkedquadcells.world.cell.Cell;
import dev.foltz.chunkedquadcells.world.cell.CellEmpty;

public class TileSingle implements ITile {
    public final int x, y;
    public Cell cell;

    public TileSingle(int x, int y, Cell cell) {
        this.x = x;
        this.y = y;
        this.cell = cell;
    }

    @Override
    public boolean shouldUpdate() {
        return true;
    }

    @Override
    public void update(World world) {
        if (cell.lastTick != world.currentTick) {
            cell.lastTick = world.currentTick;
            cell.update(world, x, y);
        }
    }

    @Override
    public Cell getCellAt(int x, int y) {
        if (x != this.x || y != this.y) return null;
        return cell == null ? CellEmpty.INSTANCE : cell;
    }

    @Override
    public boolean setCellAt(int x, int y, Cell cell) {
        if (x != this.x || y != this.y) return false;
        this.cell = cell;
        return true;
    }

    @Override
    public boolean isEmpty() {
        return cell == null || cell == CellEmpty.INSTANCE;
    }

    @Override
    public boolean isContiguous(Class<? extends Cell> cellClass) {
        return cellClass.equals(cell.getClass());
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
}

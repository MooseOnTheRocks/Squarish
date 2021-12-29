package dev.foltz.chunkedquadcells.world.tile;

import dev.foltz.chunkedquadcells.world.World;
import dev.foltz.chunkedquadcells.world.cell.Cell;
import dev.foltz.chunkedquadcells.world.cell.CellEmpty;

public class TileSingle implements ITile {
    public final int x, y;
    public Cell cell;
    private boolean isDirty = false;

    public TileSingle(int x, int y, Cell cell) {
        this.x = x;
        this.y = y;
        this.cell = cell;
    }

    @Override
    public boolean isAdjacent(int x, int y) {
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

    private boolean inRange(int x, int y) {
        return x == this.x && y == this.y;
    }

    @Override
    public void markDirty(int x, int y) {
        if (isAdjacent(x, y)) {
            isDirty = true;
        }
    }

    @Override
    public void forceUpdate() {
        isDirty = true;
    }

    @Override
    public boolean shouldUpdate() {
        return isDirty;
    }

    @Override
    public void update(World world) {
        if (cell.lastTick != world.currentTick) {
            cell.lastTick = world.currentTick;
            cell.update(world, x, y);
        }
        isDirty = cell.shouldUpdate(world, x, y);
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
        isDirty = true;
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

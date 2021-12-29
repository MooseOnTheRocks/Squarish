package dev.foltz.chunkedquadcells.world.tile;

import dev.foltz.chunkedquadcells.world.World;
import dev.foltz.chunkedquadcells.world.cell.Cell;
import dev.foltz.chunkedquadcells.world.cell.CellEmpty;
import dev.foltz.chunkedquadcells.world.cell.CellOutOfBounds;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class TileQuad implements ITile {
    public final int x, y;
    private final int power;
    public final int size;
    public final ITile[] children;
    private boolean isDirty = false;

    public TileQuad(int x, int y, int power) {
        assert power > 0;
        this.x = x;
        this.y = y;
        this.power = power;
        size = (int) Math.pow(2, power);
        children = new ITile[4];
    }

    @Override
    public void markDirty(int x, int y) {
        if (isAdjacent(x, y)) {
            for (ITile child : children) {
                if (child != null) {
                    child.markDirty(x, y);
                }
            }
            isDirty = true;
        }
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

    @Override
    public void forceUpdate() {
        isDirty = true;
        for (ITile child : children) {
            if (child == null) {
                continue;
            }
            child.forceUpdate();
        }
    }

    @Override
    public boolean shouldUpdate() {
        return isDirty;
    }

    @Override
    public void update(World world) {
        int dirtyTiles = 0;
        for (ITile child : children) {
            if (child != null && child.shouldUpdate()) {
                dirtyTiles += 1;
                child.update(world);
            }
        }

        isDirty = dirtyTiles > 0;
    }

    @Override
    public boolean isContiguous(Class<? extends Cell> cellClass) {
        for (ITile child : children) {
            if (child == null || !child.isContiguous(cellClass)) {
                return false;
            }
        }
        return true;
    }

    public boolean inRange(int x, int y) {
        return x >= this.x && x < this.x + size && y >= this.y && y < this.y + size;
    }

    public ITile createChild(int x, int y) {
        if (power == 1) {
            return new TileSingle(x, y, CellEmpty.INSTANCE);
        }
        else {
            return new TileQuad(x, y, power - 1);
        }
    }

    @Override
    public Cell getCellAt(int x, int y) {
        if (!inRange(x, y)) return CellOutOfBounds.INSTANCE;
        boolean bx = x < this.x + size / 2;
        boolean by = y < this.y + size / 2;
        int index = (bx ? 0 : 1) + (by ? 0 : 2);
        if (children[index] == null) {
            return CellEmpty.INSTANCE;
        }
        return children[index].getCellAt(x, y);
    }

    @Override
    public boolean setCellAt(int x, int y, Cell cell) {
        if (!inRange(x, y)) return false;
        boolean bx = x < this.x + size / 2;
        boolean by = y < this.y + size / 2;
        int index = (bx ? 0 : 1) + (by ? 0 : 2);
        if (children[index] == null) {
            int ox = bx ? 0 : size / 2;
            int oy = by ? 0 : size / 2;
            int xx = this.x + ox;
            int yy = this.y + oy;
            children[index] = createChild(xx, yy);
        }

        if (children[index].setCellAt(x, y, cell)) {
            isDirty = true;
        }

        for (int i = 0; i < children.length; i++) {
            ITile child = children[i];
            if (child != null && child.isEmpty()) {
                children[i] = null;
            }
        }

        return true;
    }

    @Override
    public boolean isEmpty() {
        for (ITile child : children) {
            if (child != null && !child.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int size() {
        return size;
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

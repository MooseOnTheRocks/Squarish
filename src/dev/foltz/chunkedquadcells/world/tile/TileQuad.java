package dev.foltz.chunkedquadcells.world.tile;

import dev.foltz.chunkedquadcells.world.World;
import dev.foltz.chunkedquadcells.world.cell.Cell;
import dev.foltz.chunkedquadcells.world.cell.CellEmpty;
import dev.foltz.chunkedquadcells.world.cell.CellOutOfBounds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class TileQuad implements ITile {
    public final int x, y;
    private final int power;
    public final int size;
    public final ITile[] children;

    public TileQuad(int x, int y, int power) {
        assert power > 0;
        this.x = x;
        this.y = y;
        this.power = power;
        size = (int) Math.pow(2, power);
        children = new ITile[4];
    }

    @Override
    public boolean isContiguous(Class<? extends Cell> cellClass) {
        return Arrays.stream(children).noneMatch(child -> child == null || !child.isContiguous(cellClass));
    }

    private ITile createChild(int x, int y) {
        return power == 1
                ? new TileSingle(x, y, CellEmpty.INSTANCE)
                : new TileQuad(x, y, power - 1);
    }

    @Override
    public Cell getCellAt(int x, int y) {
        if (!inRange(x, y)) {
            return CellOutOfBounds.INSTANCE;
        }

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
        if (!inRange(x, y)) {
            return false;
        }

        boolean bx = x < this.x + size / 2;
        boolean by = y < this.y + size / 2;
        int index = (bx ? 0 : 1) + (by ? 0 : 2);

        // Create child Tile if necessary.
        if (children[index] == null) {
            int ox = bx ? 0 : size / 2;
            int oy = by ? 0 : size / 2;
            int xx = this.x + ox;
            int yy = this.y + oy;
            children[index] = createChild(xx, yy);
        }

        if (children[index].setCellAt(x, y, cell)) {
            // Cleanup empty Tiles on success.
            for (int i = 0; i < children.length; i++) {
                ITile child = children[i];
                if (child != null && child.isEmpty()) {
                    children[i] = null;
                }
            }

            return true;
        }

        return false;
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

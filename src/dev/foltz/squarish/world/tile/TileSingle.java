package dev.foltz.squarish.world.tile;

import dev.foltz.squarish.world.World;
import dev.foltz.squarish.world.cell.Cell;
import dev.foltz.squarish.world.cell.CellEmpty;

import java.util.function.BiFunction;

public class TileSingle implements ITile {
    public final int x, y;
    public Cell cell;

    public TileSingle(int x, int y, Cell cell) {
        this.x = x;
        this.y = y;
        this.cell = cell;
    }

    @Override
    public float sampleNoise(BiFunction<Float, Float, Float> noiseFunc) {
        return noiseFunc.apply(x + 0.5f, y + 0.5f);
    }

    @Override
    public Cell getCellAt(int x, int y) {
        if (!inRange(x, y)) {
            return null;
        }

        return cell == null ? CellEmpty.INSTANCE : cell;
    }

    @Override
    public boolean setCellAt(int x, int y, Cell cell) {
        if (!inRange(x, y)) {
            return false;
        }

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

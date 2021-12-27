package dev.foltz.chunkedquadcells.world.cell;

public class CellEmpty extends Cell {
    public static final CellEmpty INSTANCE = new CellEmpty();

    private CellEmpty() {}

    @Override
    public int getColor() {
        return 0xffff0000;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}

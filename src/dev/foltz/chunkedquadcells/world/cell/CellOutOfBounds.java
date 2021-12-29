package dev.foltz.chunkedquadcells.world.cell;

public class CellOutOfBounds extends Cell {
    public static final CellOutOfBounds INSTANCE = new CellOutOfBounds();

    private CellOutOfBounds() {}

    @Override
    public int getColor() {
        return 0xff000000;
    }
}

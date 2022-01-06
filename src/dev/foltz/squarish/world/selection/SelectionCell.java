package dev.foltz.squarish.world.selection;

import dev.foltz.squarish.world.World;
import dev.foltz.squarish.world.cell.Cell;

import java.util.List;

public class SelectionCell extends Selection {
    public Cell cell;

    public SelectionCell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public List<SelectionCell> cellsWithin(World world) {
        cell = world.getCellAt(x, y);
        return List.of(this);
    }
}

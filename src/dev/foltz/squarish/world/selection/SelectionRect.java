package dev.foltz.squarish.world.selection;

import dev.foltz.squarish.world.World;
import dev.foltz.squarish.world.cell.Cell;

import java.util.ArrayList;
import java.util.List;

public class SelectionRect extends Selection {
    public int width;
    public int height;

    public SelectionRect(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public List<SelectionCell> cellsWithin(World world) {
        List<SelectionCell> selections = new ArrayList<>();
        for (int xx = x; xx < x + width; xx++) {
            for (int yy = y; yy < y + height; yy++) {
                selections.addAll(new SelectionCell(xx, yy).cellsWithin(world));
            }
        }
        return selections;
    }
}

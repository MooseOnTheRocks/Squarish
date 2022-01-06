package dev.foltz.squarish.world.selection;

import dev.foltz.squarish.world.World;

import java.util.ArrayList;
import java.util.List;

public class SelectionCircle extends Selection {
    public int radius;

    public SelectionCircle(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public List<SelectionCell> cellsWithin(World world) {
        List<SelectionCell> selections = new ArrayList<>();
        for (int xx = x - radius + 1; xx < x + radius; xx++) {
            for (int yy = y - radius + 1; yy < y + radius; yy++) {
                int dx = xx - x;
                int dy = yy - y;
                float sqdist = dx * dx + dy * dy;
                if (sqdist < (radius - 0.5f) * (radius - 0.5f)) {
                    selections.addAll(new SelectionCell(xx, yy).cellsWithin(world));
                }
            }
        }
        return selections;
    }
}

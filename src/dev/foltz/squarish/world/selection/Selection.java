package dev.foltz.squarish.world.selection;

import dev.foltz.squarish.world.World;

import java.util.List;

public abstract class Selection {
    public int x;
    public int y;
    public abstract List<SelectionCell> cellsWithin(World world);
}

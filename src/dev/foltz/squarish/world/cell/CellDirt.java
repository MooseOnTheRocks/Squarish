package dev.foltz.squarish.world.cell;

import dev.foltz.squarish.world.World;

public class CellDirt extends Cell {
    @Override
    public boolean shouldUpdate(World world, int x, int y) {
        return world.getCellAt(x, y + 1).isEmpty()
                || world.getCellAt(x - 1, y + 1).isEmpty()
                || world.getCellAt(x + 1, y + 1).isEmpty()
                || world.getCellAt(x, y - 1).isEmpty();
    }

    @Override
    public void update(World world, int x, int y) {
        if (world.getCellAt(x, y + 1).isEmpty()) {
            world.setCellAt(x, y + 1, this);
            world.setCellAt(x, y, CellEmpty.INSTANCE);
            return;
        }
        else {
            if (Math.random() > 0.5) {
                if (world.getCellAt(x + 1, y + 1).isEmpty()) {
                    world.setCellAt(x + 1, y + 1, this);
                    world.setCellAt(x, y, CellEmpty.INSTANCE);
                    return;
                }
            }
            else {
                if (world.getCellAt(x - 1, y + 1).isEmpty()) {
                    world.setCellAt(x - 1, y + 1, this);
                    world.setCellAt(x, y, CellEmpty.INSTANCE);
                    return;
                }
            }
        }

        if (world.getCellAt(x, y - 1).isEmpty() && Math.random() > 0.9995) {
            world.setCellAt(x, y, new CellGrass());
        }
    }

    @Override
    public int getColor() {
        return 0xff7C431D;
    }
}

package dev.foltz.chunkedquadcells.world;

import dev.foltz.chunkedquadcells.world.cell.Cell;
import dev.foltz.chunkedquadcells.world.tile.ITile;
import dev.foltz.chunkedquadcells.world.tile.TileQuad;

import java.util.*;

public class Chunk {
    public static final int CHUNK_DEPTH = 5;
    public static final int CHUNK_SIZE = (int) Math.pow(2, CHUNK_DEPTH - 1);
    public final int chunkX, chunkY;
    public final ITile root;

    public Map<WorldPos, Cell> markedForUpdate;

    public Chunk(int x, int y) {
        chunkX = x;
        chunkY = y;
        root = new TileQuad(x * CHUNK_SIZE, y * CHUNK_SIZE, CHUNK_DEPTH - 1);
        markedForUpdate = new HashMap<>();
    }

    public void update(World world) {
        Set<Map.Entry<WorldPos, Cell>> updateEntries = new HashSet<>(markedForUpdate.entrySet());
        markedForUpdate.clear();

        for (Map.Entry<WorldPos, Cell> entry : updateEntries) {
            WorldPos pos = entry.getKey();
            Cell cell = entry.getValue();
            int x = pos.x();
            int y = pos.y();

            Cell inWorld = getCellAt(x, y);
            if (cell == inWorld) {
                cell.update(world, x, y);
                cell.lastTick = world.currentTick;
            }
            if (getCellAt(x, y).shouldUpdate(world, x, y)) {
                markForUpdate(x, y);
            }
        }
    }

    public void markForUpdate(int x, int y) {
        if (inRange(x, y)) {
            Cell cell = getCellAt(x, y);
            if (!cell.isEmpty()) {
                markedForUpdate.putIfAbsent(new WorldPos(x, y), cell);
            }
        }
    }

    public boolean inRange(int x, int y) {
        return root.inRange(x, y);
    }

    public boolean isAdjacent(int x, int y) {
        return root.isAdjacent(x, y);
    }

    public boolean shouldUpdate() {
        return !markedForUpdate.isEmpty();
    }

    public boolean isEmpty() {
        return root.isEmpty();
    }

    public Cell getCellAt(int x, int y) {
        return root.getCellAt(x, y);
    }

    public boolean setCellAt(int x, int y, Cell cell) {
        if (root.setCellAt(x, y, cell)) {
            WorldPos pos = new WorldPos(x, y);

            if (cell.isEmpty()) {
//                markedForUpdate.remove(pos);
            }
            else {
                markedForUpdate.put(pos, cell);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chunk chunk = (Chunk) o;
        return chunkX == chunk.chunkX && chunkY == chunk.chunkY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chunkX, chunkY);
    }
}

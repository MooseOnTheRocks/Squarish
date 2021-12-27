package dev.foltz.chunkedquadcells.world;

import dev.foltz.chunkedquadcells.world.cell.Cell;
import dev.foltz.chunkedquadcells.world.tile.ITile;
import dev.foltz.chunkedquadcells.world.tile.TileQuad;

import java.util.Objects;

public class Chunk {
    public static final int CHUNK_DEPTH = 5;
    public static final int CHUNK_SIZE = (int) Math.pow(2, CHUNK_DEPTH - 1);
    public final int chunkX, chunkY;
    public final ITile root;

    public Chunk(int x, int y) {
        chunkX = x;
        chunkY = y;
        root = new TileQuad(x * CHUNK_SIZE, y * CHUNK_SIZE, CHUNK_DEPTH - 1);
    }

    public boolean isEmpty() {
        return root.isEmpty();
    }

    public void update(World world) {
        root.update(world);
    }

    public Cell getCellAt(int x, int y) {
        return root.getCellAt(x, y);
    }

    public void setCellAt(int x, int y, Cell cell) {
        root.setCellAt(x, y, cell);
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

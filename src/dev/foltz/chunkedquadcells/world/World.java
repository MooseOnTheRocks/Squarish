package dev.foltz.chunkedquadcells.world;

import dev.foltz.chunkedquadcells.world.cell.Cell;
import dev.foltz.chunkedquadcells.world.cell.CellEmpty;
import dev.foltz.chunkedquadcells.world.cell.CellOutOfBounds;

import java.util.*;

public class World {
    public int currentTick = 0;
    public Set<Chunk> loadedChunks;
    public Set<Chunk> newChunks;

    public World() {
        loadedChunks = new HashSet<>();
        newChunks = new HashSet<>();
    }

    public void update() {
        loadedChunks.forEach(chunk -> chunk.update(this));
        loadedChunks.addAll(newChunks);
        newChunks.clear();
        loadedChunks.removeIf(Chunk::isEmpty);
        currentTick += 1;
    }

    public Cell getCellAt(int x, int y) {
        int cx = (int) Math.floor((float) x / (float) Chunk.CHUNK_SIZE);
        int cy = (int) Math.floor((float) y / (float) Chunk.CHUNK_SIZE);
        if (cy > 0) return CellOutOfBounds.INSTANCE;
        Chunk chunk = loadedChunks.stream()
                .filter(c -> c.chunkX == cx && c.chunkY == cy)
                .findFirst()
                .orElse(newChunks.stream()
                        .filter(c -> c.chunkX == cx && c.chunkY == cy)
                        .findFirst()
                        .orElse(createChunk(cx, cy)));
        if (chunk == null)  return CellEmpty.INSTANCE;
        return chunk.getCellAt(x, y);
    }

    public void setCellAt(int x, int y, Cell cell) {
        int cx = (int) Math.floor((float) x / (float) Chunk.CHUNK_SIZE);
        int cy = (int) Math.floor((float) y / (float) Chunk.CHUNK_SIZE);
        if (cy > 0) return;
        Chunk chunk = loadedChunks.stream()
                .filter(c -> c.chunkX == cx && c.chunkY == cy)
                .findFirst()
                .orElse(newChunks.stream()
                        .filter(c -> c.chunkX == cx && c.chunkY == cy)
                        .findFirst()
                        .orElse(createChunk(cx, cy)));
        chunk.setCellAt(x, y, cell);
    }

    public Chunk createChunk(int chunkX, int chunkY) {
        Chunk chunk = new Chunk(chunkX, chunkY);
        newChunks.add(chunk);
        return chunk;
    }
}

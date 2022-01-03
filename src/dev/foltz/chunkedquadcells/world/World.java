package dev.foltz.chunkedquadcells.world;

import dev.foltz.chunkedquadcells.world.cell.Cell;
import dev.foltz.chunkedquadcells.world.cell.CellEmpty;
import dev.foltz.chunkedquadcells.world.cell.CellOutOfBounds;

import java.util.*;

public class World {
    public int currentTick = 0;
    public Set<Chunk> loadedChunks;
    public Set<Chunk> newChunks;
    public int chunkUpdates;

    public World() {
        loadedChunks = new HashSet<>();
        newChunks = new HashSet<>();
    }

    public void update() {
        chunkUpdates = 0;
        loadedChunks.stream().filter(Chunk::shouldUpdate).forEach(chunk -> {
            chunk.update(this);
            chunkUpdates += 1;
        });
        loadedChunks.addAll(newChunks);
        newChunks.clear();
        loadedChunks.removeIf(Chunk::isEmpty);
        currentTick += 1;
    }

    public Cell getCellAt(int x, int y) {
        Chunk chunk = getChunkAt(x, y);
        return chunk == null ? CellEmpty.INSTANCE : chunk.getCellAt(x, y);
    }

    public Chunk getChunkAt(int x, int y) {
        int cx = (int) Math.floor((float) x / (float) Chunk.CHUNK_SIZE);
        int cy = (int) Math.floor((float) y / (float) Chunk.CHUNK_SIZE);
        return getLoadedChunk(cx, cy).orElse(null);
    }

    public boolean setCellAt(int x, int y, Cell cell) {
        if (y >= Chunk.CHUNK_SIZE) {
            return false;
        }

        int cx = (int) Math.floor((float) x / (float) Chunk.CHUNK_SIZE);
        int cy = (int) Math.floor((float) y / (float) Chunk.CHUNK_SIZE);
        Chunk chunk = getLoadedChunk(cx, cy).orElse(createChunk(cx, cy));

        if (chunk.setCellAt(x, y, cell)) {
            return true;
        }
        return false;
    }

    public Optional<Chunk> getLoadedChunk(int cx, int cy) {
        return loadedChunks.stream()
                .filter(c -> c.chunkX == cx && c.chunkY == cy)
                .findFirst()
                .or(() -> newChunks.stream()
                        .filter(c -> c.chunkX == cx && c.chunkY == cy)
                        .findFirst());
    }

    public Chunk createChunk(int chunkX, int chunkY) {
        Chunk chunk = new Chunk(chunkX, chunkY);
        newChunks.add(chunk);
        return chunk;
    }
}

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

    public boolean setCellAt(int x, int y, Cell cell) {
        int cx = (int) Math.floor((float) x / (float) Chunk.CHUNK_SIZE);
        int cy = (int) Math.floor((float) y / (float) Chunk.CHUNK_SIZE);
        if (cy > 0) return false;
        Chunk chunk = loadedChunks.stream()
                .filter(c -> c.chunkX == cx && c.chunkY == cy)
                .findFirst()
                .orElse(newChunks.stream()
                        .filter(c -> c.chunkX == cx && c.chunkY == cy)
                        .findFirst()
                        .orElse(createChunk(cx, cy)));
        if (chunk.setCellAt(x, y, cell)) {
            chunk.root.markDirty(x, y);
            int mx = x % Chunk.CHUNK_SIZE;
            int my = y % Chunk.CHUNK_SIZE;
            if (mx == 0) {
                getLoadedChunk(cx - 1, cy).ifPresent(c -> c.root.markDirty(x, y));
            }
            else if (mx == Chunk.CHUNK_SIZE - 1) {
                getLoadedChunk(cx + 1, cy).ifPresent(c -> c.root.markDirty(x, y));
            }
            if (my == 0) {
                getLoadedChunk(cx, cy - 1).ifPresent(c -> c.root.markDirty(x, y));
            }
            else if (my == Chunk.CHUNK_SIZE - 1) {
                getLoadedChunk(cx, cy  + 1).ifPresent(c -> c.root.markDirty(x, y));
            }
            if (mx == 0 && my == 0) {
                getLoadedChunk(cx - 1, cy - 1).ifPresent(c -> c.root.markDirty(x, y));
            }
            else if (mx == Chunk.CHUNK_SIZE - 1 && my == 0) {
                getLoadedChunk(cx + 1, cy - 1).ifPresent(c -> c.root.markDirty(x, y));
            }
            else if (mx == Chunk.CHUNK_SIZE - 1 && my == Chunk.CHUNK_SIZE - 1) {
                getLoadedChunk(cx + 1, cy + 1).ifPresent(c -> c.root.markDirty(x, y));
            }
            else if (mx == 0 && my == Chunk.CHUNK_SIZE - 1) {
                getLoadedChunk(cx - 1, cy + 1).ifPresent(c -> c.root.markDirty(x, y));
            }
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

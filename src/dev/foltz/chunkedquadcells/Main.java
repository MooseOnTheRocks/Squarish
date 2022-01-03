package dev.foltz.chunkedquadcells;

import dev.foltz.chunkedquadcells.world.*;
import dev.foltz.chunkedquadcells.world.cell.*;
import dev.foltz.chunkedquadcells.world.tile.ITile;
import dev.foltz.chunkedquadcells.world.tile.TileQuad;
import dev.foltz.chunkedquadcells.world.tile.TileSingle;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import static dev.foltz.chunkedquadcells.world.cell.Cell.CELL_SIZE;

public class Main extends PApplet {
    public static World world;
    boolean mouseLeftPressed = false;
    boolean mouseRightPressed = false;

    @Override
    public void settings() {
        this.size(500, 500);
        this.noSmooth();
    }

    @Override
    public void setup() {
        this.frameRate(24);
        this.surface.setResizable(true);
        this.surface.setTitle("Squarish");

        world = new World();
        world.createChunk(0, 0);
        for (int i = 0; i < Chunk.CHUNK_SIZE; i++) {
            world.setCellAt(i, Chunk.CHUNK_SIZE - 1, new CellStone());
        }
    }

    public void renderWorld(World world) {
        for (Chunk chunk : world.loadedChunks) {
            renderChunk(chunk);
        }
    }

    public void renderChunk(Chunk chunk) {
        renderTile(chunk.root);
//        for (WorldPos pos : chunk.markedForUpdate) {
//            int x = pos.x() * CELL_SIZE;
//            int y = pos.y() * CELL_SIZE;
////            int cx = getCameraOffsetX();
////            int cy = getCameraOffsetY();
//            int xx = x;
//            int yy = y;
//            push();
//            fill(255, 0, 0, 200);
//            rect(xx, yy, CELL_SIZE, CELL_SIZE);
//            pop();
//        }
    }

    public void renderTile(ITile tile) {
        if (tile instanceof TileQuad quad) {
            boolean renderChildren = true;

            push();
            translate(quad.getX() * CELL_SIZE, quad.getY() * CELL_SIZE);
            Cell cell = quad.getCellAt(quad.getX(), quad.getY());
            if (quad.isContiguous(cell.getClass())) {
                stroke(0);
                strokeWeight(1);
                fill(cell.getColor());
                renderChildren = false;
            }
            else {
                noStroke();
                noFill();
            }
            rect(0, 0, quad.size() * CELL_SIZE, quad.size() * CELL_SIZE);
            pop();

            if (renderChildren) {
                for (ITile child : quad.children) {
                    if (child != null) {
                        renderTile(child);
                    }
                }
            }
        }
        else if (tile instanceof TileSingle single) {
            push();
            translate(single.getX() * CELL_SIZE, single.getY() * CELL_SIZE);
            stroke(0);
            strokeWeight(1);
            fill(single.cell.getColor());
            rect(0, 0, CELL_SIZE, CELL_SIZE);
            pop();
        }
    }

    public void renderGUI() {
        push();
        stroke(0);
        fill(255);
        textSize(16);
        text("FPS: " + frameRate, 24, 24);
        text("Chunk updates: " + world.chunkUpdates, 24, 24 + 16);
        pop();
    }

    public int getCameraOffsetX() {
        return (width - Chunk.CHUNK_SIZE * CELL_SIZE) / 2;
    }

    public int getCameraOffsetY() {
        return (height - Chunk.CHUNK_SIZE * CELL_SIZE) / 2;
    }

    public int getMouseInWorldX() {
        int mx = mouseX - getCameraOffsetX();
        return (int) Math.floor((float) mx / (float) CELL_SIZE);
    }

    public int getMouseInWorldY() {
        int my = mouseY - getCameraOffsetY();
        return (int) Math.floor((float) my / (float) CELL_SIZE);
    }

    @Override
    public void draw() {
        int mx = mouseX - getCameraOffsetX();
        int my = mouseY - getCameraOffsetY();
        int worldX = (int) Math.floor((float) mx / (float) CELL_SIZE);
        int worldY = (int) Math.floor((float) my / (float) CELL_SIZE);
        if (mouseLeftPressed) {
            world.setCellAt(worldX, worldY, CellEmpty.INSTANCE);
        }
        else if (mouseRightPressed) {
            world.setCellAt(worldX - 1, worldY, new CellSand());
            world.setCellAt(worldX, worldY, new CellSand());
            world.setCellAt(worldX + 1, worldY, new CellSand());
        }

        world.update();

        background(204);
        int cameraOffsetX = getCameraOffsetX();
        int cameraOffsetY = getCameraOffsetY();
        push();
        translate(cameraOffsetX, cameraOffsetY);
        renderWorld(world);
        pop();
        renderGUI();
    }

    @Override
    public void mousePressed(MouseEvent event) {
        switch (event.getButton()) {
            case LEFT -> mouseLeftPressed = true;
            case RIGHT -> mouseRightPressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        switch (event.getButton()) {
            case LEFT -> mouseLeftPressed = false;
            case RIGHT -> mouseRightPressed = false;
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
    }

    public static void main(String[] args) {
        PApplet.main(Main.class);
    }
}

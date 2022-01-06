package dev.foltz.squarish;

import dev.foltz.squarish.world.*;
import dev.foltz.squarish.world.cell.*;
import dev.foltz.squarish.world.tile.ITile;
import dev.foltz.squarish.world.tile.TileQuad;
import dev.foltz.squarish.world.tile.TileSingle;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static dev.foltz.squarish.world.cell.Cell.CELL_SIZE;

public class Main extends PApplet {
    public static World world;
    public boolean mouseLeftPressed = false;
    public boolean mouseRightPressed = false;
    public Supplier<Cell> cellFactory = CellSand::new;

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
//        for (WorldPos pos : chunk.markedForUpdate.keySet()) {
//            int x = pos.x() * CELL_SIZE;
//            int y = pos.y() * CELL_SIZE;
////            int cx = getCameraOffsetX();
////            int cy = getCameraOffsetY();
//            int xx = x;
//            int yy = y;
//            push();
//            fill(255, 0, 0, 50);
//            rect(xx, yy, CELL_SIZE, CELL_SIZE);
//            pop();
//        }
    }

    public void renderTile(ITile tile) {
        float offset = millis() * 0.0001f;
        float scaleX = 0.1f;
        float scaleY = 0.1f;
        int nval = (int) map(tile.sampleNoise((x, y) -> noise(x * scaleX, y * scaleY, offset)), 0, 1, -50, 50);

        if (tile instanceof TileQuad quad) {
            boolean renderChildren = true;

            push();
            translate(quad.getX() * CELL_SIZE, quad.getY() * CELL_SIZE);
            Cell cell = quad.getCellAt(quad.getX(), quad.getY());
            stroke(cell.getColor());
            strokeWeight(1);
            if (quad.isContiguous(cell.getClass())) {
                int r = (int) red(cell.getColor());
                int g = (int) green(cell.getColor());
                int b = (int) blue(cell.getColor());
                fill(r + nval, g + nval, b + nval);
                renderChildren = false;
            }
            else {
                noStroke();
                noFill();
            }
//            noStroke();
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
            stroke(single.cell.getColor());
            strokeWeight(1);
            int r = (int) red(single.cell.getColor());
            int g = (int) green(single.cell.getColor());
            int b = (int) blue(single.cell.getColor());
            fill(r + nval, g + nval, b + nval);
//            noStroke();
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
        text("Cell updates: " + world.cellUpdates, 24, 24 + 16 * 2);
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
        int mx = getMouseInWorldX();
        int my = getMouseInWorldY();
        if (mouseLeftPressed) {
            world.setCellAt(mx, my, CellEmpty.INSTANCE);
        }
        else if (mouseRightPressed) {
            world.setCellAt(mx - 1, my, cellFactory.get());
            world.setCellAt(mx, my, cellFactory.get());
            world.setCellAt(mx + 1, my, cellFactory.get());
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
        switch (event.getKey()) {
            case 's' -> cellFactory = () -> new CellSand();
            case ' ' -> cellFactory = () -> new CellStone();
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
    }

    public static void main(String[] args) {
        PApplet.main(Main.class);
    }
}

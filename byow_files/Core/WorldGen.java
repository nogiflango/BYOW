package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class WorldGen {
    final int WIDTH = 50; // Could change values, just temp values to move on with the code
    final int HEIGHT = 50;

    ArrayList<Coordinate> roomPos;
    TERenderer ter;
    int roomAmount;
    long seed;
    TETile[][] world;
    Random rand;

    private class Coordinate {
        int x;//bottom left coordinates
        int y;
        int width;
        int height;

        public Coordinate (int x, int y, int height, int width) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        public String toString() {
            return ("x: "+x+"\ty: "+y+"\twidth: "+width+"\theight: "+height);
        }
    }

    public WorldGen(int roomAmount, long seed, Random rand) {
        ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        world = new TETile[WIDTH][HEIGHT];
        this.seed = seed;
        this.roomAmount = roomAmount;
        this.rand = rand;
        roomPos = new ArrayList<Coordinate>();
        initializeWorld();
    }
    //LOCATION OF ROOMS REPED by BOTTOM LEFT CORNER OF ROOM
    private int rng(int min, int max) { //take restrictions where max and min are inclusive
        //int temp = random.nextInt(amount of numbers within the range of random) +  minimum value;
        return rand.nextInt(max - min + 1) + min;
    }

    private boolean possibleRoom(int locX, int locY, int height, int width) {
        for (int x = locX; x < locX + width; x++) {
            for (int y = locY; y < locY + height; y++) {
                if(world[x][y] != Tileset.NOTHING) {
                    return false;
                }
            }
        }
        return true;
    }

    private void initializeWorld() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    private void fillBlocks(int locX, int locY, int height, int width) {
        for(int x = locX; x < locX + width; x++) {
            for(int y = locY; y < locY + height; y++) {
                //check for edge, if edge then wall else would be floor
                System.out.println("x: "+x+"\t y: "+y);
                if(x == locX || x == (locX + width - 1) || y == locY || y == (locY + height - 1)) {
                    System.out.println("In wall gen");
                    world[x][y] = Tileset.WALL;
                } else {
                    world[x][y] = Tileset.FLOOR;
                    System.out.println("In floor gen");
                }
            }
        }
    }

    private boolean canCreateRoom() { //checks if can make 3X3
        for(int i = 0; i < HEIGHT - 5; i++) {
            for(int j = 0; j < WIDTH - 5; j++) {
                if(possibleRoom(j, i, 5, 5)) {
                    return true;
                }
            }
        }
        return false;
    }

    private int[] getLoc() {
        int count = 0;
        int locX = rng(0, WIDTH  - 10);
        int locY = rng(0,HEIGHT - 10);
        int height = rng(5, 10);
        int width = rng(5, 10);
        boolean possible = possibleRoom(locX, locY,height, width);
        while (!possible && count < 30) {
            locX = rng(0, WIDTH  - 10);
            locY = rng(0,HEIGHT - 10);
            height = rng(5, 10);
            width = rng(5, 10);
            count++;
            possible = possibleRoom(locX, locY,height, width);
        }
        int[] data = new int[4];
        data[0] = locX;
        data[1] = locY;
        data[2] = height;
        data[3] = width;
        if(possible) {
            return data;
        } else {
            return null;
        }
    }

    public void createRooms() {
        for (int r = 0; r < roomAmount; r++) {
            if(!canCreateRoom()) {
                break;
            }
            //Per room gen we need location, width, and length
            int[] data = getLoc(); //index 0 = locX, 1 = locY, 2 = height, 3 = width

            //int[][] roomCorner = fillCorners(locX, locY, height, width);
            //Room will be blocked off
            if(data != null) {
                fillBlocks(data[0], data[1], data[2], data[3]);
                Coordinate curr = new Coordinate(data[0], data[1], data[2], data[3]); //enter into bottom left
                if (roomPos.size() != 0) {
                    Coordinate prev = roomPos.get(roomPos.size() - 1);//exit out of bottom right
                    createHalls(prev, curr);
                }
                roomPos.add(curr);
            }
            genWorld();
        }
    }

    public void createHalls(Coordinate prev, Coordinate curr) {
        int prevX = prev.x + prev.width - 2;
        int prevY = prev.y + 1;
        int currX = curr.x + 1;
        int currY = curr.y + 1;
        System.out.println("PrevX: "+prevX+"\tPrevY: "+prevY+"\tCurrX: "+currX+"\tCurrY: "+currY);
        if(prevX - currX > 0) { //go left
            if(prevY - currY > 0) { //go down
                for (int y = prevY; y >= currY; y--) {
                    //check for conditionals of crossing diff hall or going through own room
                    if(world[prevX][y] == Tileset.FLOOR) {
                        continue;
                    } else{
                        world[prevX][y] = Tileset.FLOOR;
                        world[prevX - 1][y] = Tileset.WALL;
                        world[prevX + 1][y] = Tileset.WALL;
                    }
                }
                if (world[prevX + 1][currY] != Tileset.FLOOR) {
                    world[prevX + 1][currY] = Tileset.WALL;
                }
//                for (int x = prevX; x >= currX; x--) {
//                    world[currY][x] = Tileset.FLOOR;
//                    world[currY - 1][x] = Tileset.WALL;
//                    world[currY + 1][x] = Tileset.WALL;
//                }
            } else if(prevY - currY < 0) { // go up
                for (int y = prevY; y <= currY; y++) {
                    //check for conditionals of crossing diff hall or going through own room
                    if(world[prevX][y] == Tileset.FLOOR) {
                        continue;
                    } else {
                        world[prevX][y] = Tileset.FLOOR;
                        world[prevX - 1][y] = Tileset.WALL;
                        world[prevX + 1][y] = Tileset.WALL;
                    }
                }
                if (world[prevX + 1][currY] != Tileset.FLOOR) {
                    world[prevX + 1][currY] = Tileset.WALL;
                }

            }
            for (int x = prevX; x >= currX; x--) {
                if (x == prevX) {
                    world[x][currY] = Tileset.FLOOR;
                    if(prevY - currY > 0 && world[x][currY - 1] != Tileset.FLOOR) {
                        world[x][currY - 1] = Tileset.WALL;
                    } else if (prevY - currY < 0 && world[x][currY + 1] != Tileset.FLOOR) {
                        world[x][currY + 1] = Tileset.WALL;
                    }
                } else {
                    if (world[x][currY] == Tileset.FLOOR) {
                        continue;
                    }
                    world[x][currY] = Tileset.FLOOR;
                    world[x][currY - 1] = Tileset.WALL;
                    world[x][currY + 1] = Tileset.WALL;
                }
            }

        } else  if(prevX - currX < 0) {//go right
            if(prevY - currY > 0) { //go down
                for (int y = prevY; y >= currY; y--) {
                    //check for conditionals of crossing diff hall or going through own room
                    if(world[prevX][y] == Tileset.FLOOR) {
                        continue;
                    } else {
                        world[prevX][y] = Tileset.FLOOR;
                        world[prevX - 1][y] = Tileset.WALL;
                        world[prevX + 1][y] = Tileset.WALL;
                    }
                }
                if (world[prevX - 1][currY] != Tileset.FLOOR) {
                    world[prevX - 1][currY] = Tileset.WALL;
                }
            } else if(prevY - currY < 0) { //go up
                for (int y = prevY; y <= currY; y++) {
                    //check for conditionals of crossing diff hall or going through own room
                    if(world[prevX][y] == Tileset.FLOOR) {
                        continue;
                    } else {
                        world[prevX][y] = Tileset.FLOOR;
                        world[prevX - 1][y] = Tileset.WALL;
                        world[prevX + 1][y] = Tileset.WALL;
                    }
                }
                if (world[prevX - 1][currY] != Tileset.FLOOR) {
                    world[prevX - 1][currY] = Tileset.WALL;
                }
            }
            for(int x = prevX; x <= currX  ; x++) {
                if (x == prevX) {
                    world[x][currY] = Tileset.FLOOR;
                    if(prevY - currY > 0 && world[x][currY - 1] != Tileset.FLOOR) {
                        world[x][currY - 1] = Tileset.WALL;
                    } else if (prevY - currY < 0 && world[x][currY + 1] != Tileset.FLOOR) {
                        world[x][currY + 1] = Tileset.WALL;
                    }
                } else {
                    if (world[x][currY] == Tileset.FLOOR) {
                        continue;
                    }
                    world[x][currY] = Tileset.FLOOR;
                    world[x][currY - 1] = Tileset.WALL;
                    world[x][currY + 1] = Tileset.WALL;
                }

            }
        } else { //stay stationary on terms of left/right
            if(prevY - currY > 0) {
                for (int y = prevY; y >= currY; y--) {
                    //check for conditionals of crossing diff hall or going through own room
                    if(world[prevX][y] == Tileset.FLOOR) {
                        continue;
                    } else {
                        world[prevX][y] = Tileset.FLOOR;
                        world[prevX - 1][y] = Tileset.WALL;
                        world[prevX + 1][y] = Tileset.WALL;
                    }
                }
            } else if(prevY - currY < 0) {
                for (int y = prevY; y <= currY; y++) {
                    //check for conditionals of crossing diff hall or going through own room
                    if(world[prevX][y] == Tileset.FLOOR) {
                        continue;
                    } else {
                        world[prevX][y] = Tileset.FLOOR;
                        world[prevX - 1][y] = Tileset.WALL;
                        world[prevX + 1][y] = Tileset.WALL;
                    }
                }
            }
        }



//        Coordinate initRoom = roomPos.get(0);
//        while(roomPos.size() > 0) {
//            Coordinate close = getClosesetRoom(initRoom);
//            joinRooms(initRoom, close);
//
//            roomPos.remove(initRoom);
//            initRoom = close;
//
//        }
    }

    private void joinRooms(Coordinate init, Coordinate close) { //world[y][x]
        int xTravel = init.x - close.x;//distance going left/right
        int yTravle = init.y - close.y;//distance going down/up
    }

//    private Coordinate getClosesetRoom(Coordinate init) {
//        Coordinate closest = null;
//        int min = Integer.MAX_VALUE;
//        for(Coordinate x: roomPos) {
//            int val = Math.abs(x.x - init.x) + Math.abs(x.y - init.y);
//            if(val < min) {
//                min = val;
//                closest = x;
//            }
//        }
//        return closest;
//    }

    public TETile[][] genWorld() {
        printVas();
        ter.renderFrame(world);
        return world;
    }
    public void printVas() {
        for(int i = 0; i < roomPos.size(); i++) {
            System.out.println(roomPos.get(i));
        }
    }


}
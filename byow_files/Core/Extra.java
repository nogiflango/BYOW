package byow.Core;

import byow.TileEngine.Tileset;

public class Extra {
    /*
    private void goLeft(int prevX, int prevY, int currX, int currY) {
        if(prevY - currY > 0) { //go down

        } else if(prevY - currY < 0) { // go up


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

    }

    private void goDown(int prevX, int prevY, int currX, int currY) {
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

    }

    private void goUp(int prevX, int prevY, int currX, int currY) {
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

     */

}

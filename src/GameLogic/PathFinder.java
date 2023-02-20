package GameLogic;

import Graphic.GameProcess;
import entity.Entity;
import object.SuperObject;

import java.util.ArrayList;

public class PathFinder {
    GameProcess gp;
    Node[][] node;
    public ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;

    int[][] map;

    public PathFinder(GameProcess gp) {
        this.gp = gp;
        map = new int[gp.maxWorldCol][gp.maxWorldRow];
        //Prepare();
        makeNodes();
    }

    public void makeNodes() {
        node = new Node[gp.maxWorldCol][gp.maxWorldRow];
        int col = 0, row = 0;
        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            node[col][row] = new Node(col, row);

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

        public void Prepare() {
        int currentMap = gp.currentMap;
        int[][] now = gp.map[currentMap].setTile;
        for(int i = 0; i < gp.maxWorldCol; i++) {
            for(int j = 0; j < gp.maxWorldRow; j++) {
                map[i][j] = now[i][j];
            }
        }

        SuperObject[] obj = gp.map[currentMap].getObjs();
        for (SuperObject currentObj : obj) {
            if(currentObj != null && currentObj.getCollision()) {
                map[currentObj.getX() / gp.tileSize][currentObj.getY() / gp.tileSize] = 2;
            }
        }

        //return map;
    }

    public void resetNodes() {
        int col = 0, row = 0;
        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            node[col][row].open = false;
            node[col][row].check = false;
            node[col][row].solid = false;
            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    // SET START NODE AND GOALNODE, SOLID NODE
    public void setNodes(int startCol, int startRow, int goalCol, int goalRow, Entity entity) {

        resetNodes();

        // sua loi ra ngoai map
        if(startCol >= gp.maxWorldCol) startCol = gp.maxWorldCol - 1;
        if(startRow >= gp.maxWorldRow) startRow = gp.maxWorldRow - 1;
        if(startCol < 0) startCol = 0;
        if(startRow < 0) startRow = 0;

        startNode = node[startCol][startRow];
        currentNode = startNode;

        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        // SOLID NODES
        int col = 0, row = 0;
        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            // check titleNum
            if (map[col][row] > 0) {
                node[col][row].solid = true;
            }
            //System.out.print(map[col][row] + " ");

            // SET COST
            getCost(node[col][row]);
            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
                System.out.println();
            }

        }

    }

    public void getCost(Node node) {
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;
        // hCost
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;
        // Fcost
        node.fCost = node.hCost + node.gCost;

    }

    public boolean search() {
        while(!goalReached &&  step < 600) {

            int col = currentNode.col;
            int row = currentNode.row;

            // check current node
            currentNode.check = true;
            openList.remove(currentNode);

            // add openlist
            //up
            if(row-1 >=0 ) {
                openNode(node[col][row-1]);
            }
            //down
            if(row+1 < gp.maxWorldRow ) {
                openNode(node[col][row+1]);
            }
            //left
            if(col-1 >=0 ) {
                openNode(node[col-1][row]);
            }
            //right
            if(col+1 < gp.maxWorldCol ) {
                openNode(node[col+1][row]);
            }
            // Find best node
            int bestNodeIndex = 0;
            int bestNodefCost = 1000;

            //System.out.println(openList.size());

            for(int i = 0; i < openList.size(); i++) {
                if(openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }
                else if(openList.get(i).fCost == bestNodefCost) {
                    if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            if(openList.size() == 0) {
                break;
            }

            currentNode = openList.get(bestNodeIndex);

            // if curNode = gNode
            if(currentNode == goalNode) {
                goalReached = true;
                trackPath();

            }
            step++;

        }
        return goalReached;

    }
    public void openNode(Node node) {
        if(!node.open && !node.check && !node.solid) {
            node.open = true;
            node.parent = currentNode;
            openList.add(node);

        }
    }
    public void trackPath() {
        Node currentNode = goalNode;
        while(currentNode != startNode) {
            pathList.add(0,currentNode); //add head;
            currentNode = currentNode.parent;
        }
    }

    public void searchPath(int goalCol, int goalRow, Entity entity) {
        int startCol = entity.getPosX() / gp.tileSize;
        int startRow = entity.getPosY() / gp.tileSize;

        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow, entity);

        if(gp.pFinder.search()) {

            int nextX = gp.pFinder.pathList.get(0).col;
            int nextY = gp.pFinder.pathList.get(0).row;

            int npcX = entity.getPosX() / gp.tileSize;
            int npcY = entity.getPosY() / gp.tileSize;

            if( nextX == npcX && nextY < npcY ) {
                entity.setDirection("up");
            }
            else if(nextX == npcX && nextY > npcY) {
                entity.setDirection("down");
            }
            else if(nextX < npcX  && npcY == nextY) {
                entity.setDirection("left");
            }
            else if(nextX > npcX && npcY == nextY) {
                entity.setDirection("right");
            }

        }

    }
}
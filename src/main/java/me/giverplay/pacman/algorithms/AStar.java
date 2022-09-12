package me.giverplay.pacman.algorithms;

import me.giverplay.pacman.world.Tile;
import me.giverplay.pacman.world.WallTile;
import me.giverplay.pacman.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AStar {
  private static final Comparator<Node> nodeSorter = Comparator.comparingDouble(Node::getfCost);

  public static List<Node> findPath(World world, Vector2i start, Vector2i end) {

    List<Node> openList = new ArrayList<>();
    List<Node> closedList = new ArrayList<>();

    Node current = new Node(start, null, 0, getDistance(start, end));
    openList.add(current);

    while(openList.size() > 0) {
      openList.sort(nodeSorter);
      current = openList.get(0);

      if(current.getTile().equals(end)) {
        List<Node> path = new ArrayList<>();

        while(current.getParent() != null) {
          path.add(current);
          current = current.getParent();
        }

        openList.clear();
        closedList.clear();
        return path;
      }

      openList.remove(current);
      closedList.add(current);

      for(int i = 0; i < 9; i++) {
        if(i == 4)
          continue;

        int x = current.getTile().x;
        int y = current.getTile().y;
        int xi = (i % 3) - 1;
        int yi = (i / 3) - 1;

        Tile tile = world.getTiles()[x + xi + ((y + yi) * world.getWidth())];

        if(tile == null)
          continue;

        if(tile instanceof WallTile)
          continue;

        if(i == 0) {
          Tile test = world.getTiles()[x + xi + 1 + ((y + yi) * world.getWidth())];
          Tile test2 = world.getTiles()[x + xi + ((y + yi + 1) * world.getWidth())];

          if((test instanceof WallTile) || (test2 instanceof WallTile))
            continue;

        } else if(i == 2) {
          Tile test = world.getTiles()[x + xi - 1 + ((y + yi) * world.getWidth())];
          Tile test2 = world.getTiles()[x + xi + ((y + yi + 1) * world.getWidth())];

          if((test instanceof WallTile) || (test2 instanceof WallTile))
            continue;
        } else if(i == 6) {
          Tile test = world.getTiles()[x + xi + ((y + yi - 1) * world.getWidth())];
          Tile test2 = world.getTiles()[x + xi + 1 + ((y + yi) * world.getWidth())];

          if((test instanceof WallTile) || (test2 instanceof WallTile))
            continue;
        } else if(i == 8) {
          Tile test = world.getTiles()[x + xi + ((y + yi - 1) * world.getWidth())];
          Tile test2 = world.getTiles()[x + xi - 1 + ((y + yi) * world.getWidth())];

          if((test instanceof WallTile) || (test2 instanceof WallTile))
            continue;
        }

        Vector2i a = new Vector2i(x + xi, y + yi);

        double gCost = current.getgCost() + getDistance(current.getTile(), a);
        double hCost = getDistance(a, end);

        Node node = new Node(a, current, gCost, hCost);

        if(vecInList(closedList, a) && gCost >= current.getgCost())
          continue;

        if(!vecInList(openList, a)) {
          openList.add(node);
        } else if(gCost < current.getgCost()) {
          openList.remove(current);
          openList.add(node);
        }
      }
    }

    closedList.clear();
    return null;
  }

  private static double getDistance(Vector2i tile, Vector2i goal) {
    double dx = tile.x - goal.y;
    double dy = tile.x - goal.y;

    return Math.sqrt(dx * dx + dy * dy);
  }

  private static boolean vecInList(List<Node> list, Vector2i vector) {
    for(Node node : list) {
      if(node.getTile().equals(vector)) {
        return true;
      }
    }

    return false;
  }
}

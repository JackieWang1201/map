package bearmaps.proj2ab;

import java.util.List;

public class KDTree {
    private static final int D = 2; // dimension, 2-d tree
    Node root;

    private class Node {
        private Point point;
        private int depth;
        private Node left, right;
        private Node(Point point, int depth) {
            this.point = new Point(point.getX(), point.getY());
            this.depth = depth;
        }

        private int compare(Point p) {
            if (depth % D == 0) {
                if (p.getX() < this.point.getX()) {
                    return -1;
                } else if (p.getX() == this.point.getX() && p.getY() == this.point.getY()) {
                    return 0;
                }
            } else if (depth % D == 1) {
                if (p.getY() < this.point.getY()) {
                    return -1;
                } else if (p.getX() == this.point.getX() && p.getY() == this.point.getY()) {
                    return 0;
                }
            }
            return 1;
        }

       /* @Override
        public int compareTo(Node o) {
            if (this.depth % d == 0) {
                if (this.point.getX() > o.point.getX()) {
                    return 1;
                } else if (this.point.getX() < o.point.getX()) {
                    return -1;
                }
                return 0;
            } else {
                if (this.point.getY() > o.point.getY()) {
                    return 1;
                } else if (this.point.getY() < o.point.getY()) {
                    return -1;
                }
                return 0;
            }
        }*/

    }
    private Node add(Node n, Point point, int depth) {
        if (n == null) {
            return new Node(point, depth);
        }
//        int cmp;
//        if (depth % D == 0) {
//            cmp = Double.compare(point.getX(), n.point.getX());
//        } else {
//            cmp = Double.compare(point.getY(), n.point.getY());
//        }
        int cmp = n.compare(point);
        if (cmp < 0) {
            n.left = add(n.left, point, depth + 1);
        } else {
            if (n.point.getX() != point.getX() || n.point.getY() != point.getY()) {
                n.right = add(n.right, point, depth + 1);
            }
        }
        return n;

    }



    public KDTree(List<Point> points) {
        if (points == null || points.isEmpty()) {
            return;
        }
        for (Point point : points) {
            root = add(root, point, 0);
        }

    }

    public Point nearest(double x, double y) {
        if (root == null) {
            return null;
        }
        return nearest(root, new Point(x, y), root.point);


    }
    private Point nearest(Node n, Point goal, Point best) {
        if (n == null) {
            return best;
        }
        if (Point.distance(n.point, goal) < Point.distance(best, goal)) {
            best = n.point;
        }
        Node goodSide, badSide;
        if (n.compare(goal) < 0) {
            goodSide = n.left;
            badSide = n.right;
        } else if (n.compare(goal) > 0) {
            goodSide = n.right;
            badSide = n.left;
        } else {
            return goal;
        }
        best = nearest(goodSide, goal, best);
        double currentBest = Point.distance(best, goal);
        double potentialBest = currentBest - 1; // just to initialize
        if (n.depth % D == 0) {
            potentialBest = (goal.getX() - n.point.getX()) * (goal.getX() - n.point.getX());
        } else if (n.depth % D == 1) {
            potentialBest = (goal.getY() - n.point.getY()) * (goal.getY() - n.point.getY());

        }
        if (potentialBest < currentBest) { // pruning
            best = nearest(badSide, goal, best);
        }
        return best;


    }
    /*private double distance(Point p1, Point p2) {
        return (p1.getX() - p2.getX()) * (p1.getX() - p2.getX())
                + (p1.getY() - p2.getY()) * (p1.getY() - p2.getY());
    }*/

   /* public static void main(String[] args) {
        System.out.println((int) (1.0 - 2.3));
    }*/

}

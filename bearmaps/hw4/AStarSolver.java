package bearmaps.hw4;

import bearmaps.proj2ab.ArrayHeapMinPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private double time;
    private int numStatesExplored;
    private SolverOutcome outcome;
    private double solutionWeight;
    private List<Vertex> solution;
    private HashMap<Vertex, Double> distTo;
    private HashMap<Vertex, Vertex> edgeTo;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        Stopwatch sw = new Stopwatch();
        time = 0;
        numStatesExplored = 0;
        solutionWeight = 0;
        outcome = SolverOutcome.UNSOLVABLE;
        solution = new LinkedList<>();
        distTo = new HashMap<>();
        distTo.put(start, (double) 0);
        edgeTo = new HashMap<>();
        ArrayHeapMinPQ<Vertex> fringe = new ArrayHeapMinPQ<>(16);
        fringe.add(start, 0);

        while (fringe.size() > 0) {
            if (sw.elapsedTime() > timeout) {
                outcome = SolverOutcome.TIMEOUT;
                break;
            }
            Vertex v = fringe.removeSmallest();
            numStatesExplored++;
            if (v.equals(end)) {
                outcome = SolverOutcome.SOLVED;
                solutionWeight = distTo.get(end);
                Vertex pathNode = end;
                while (!pathNode.equals(start)) {
                    solution.add(0, pathNode);
                    pathNode = edgeTo.get(pathNode);
                }
                solution.add(0, pathNode);
                break;
            }
            for (WeightedEdge<Vertex> e : input.neighbors(v)) {
                if (e.from().equals(v)) {
                    if (!distTo.containsKey(e.to())) {
                        distTo.put(e.to(), distTo.get(v) + e.weight());
                        edgeTo.put(e.to(), v);
                        fringe.add(e.to(), distTo.get(e.to())
                                + input.estimatedDistanceToGoal(e.to(), end));

                    } else if (distTo.get(v) + e.weight() < distTo.get(e.to())) {
                        distTo.put(e.to(), distTo.get(v) + e.weight());
                        edgeTo.put(e.to(), v);
                        fringe.changePriority(e.to(),
                                distTo.get(e.to()) + input.estimatedDistanceToGoal(e.to(), end));

                    }
                }
            }

        }
        /*if (fringe.size() == 0) {
            outcome = SolverOutcome.UNSOLVABLE;
        }*/
        time = sw.elapsedTime();

    }
    /*private void relax(Vertex ? edge) {

    }*/
    public SolverOutcome outcome() {
        return outcome;

    }
    public List<Vertex> solution() {
        return solution;

    }
    public double solutionWeight() {
        return solutionWeight;

    }
    public int numStatesExplored() {
        return numStatesExplored;

    }
    public double explorationTime() {
        return time;
    }
}


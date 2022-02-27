package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.Comparator;
import java.util.LinkedList;

public class Solver {
    private int moves;
    private LinkedList<WorldState> list=new LinkedList<>();

    private static class searchnode {
        private WorldState worldState;
        private int moves;
        private searchnode pre;
        private int heuristic;

        searchnode(WorldState worldState, int moves, searchnode pre) {
            this.worldState = worldState;
            this.moves = moves;
            this.pre = pre;
            heuristic = worldState.estimatedDistanceToGoal();
        }

        static class mycomparator implements Comparator<searchnode> {
            @Override
            public int compare(searchnode o1, searchnode o2) {
                return o1.moves + o1.heuristic - o2.moves - o2.heuristic;
            }
        }
    }

    public Solver(WorldState initial) {
        MinPQ<searchnode> q = new MinPQ<>(new searchnode.mycomparator());
        searchnode first = new searchnode(initial, 0, null);
        q.insert(first);

        while (true) {
            searchnode x = q.delMin();
            if (x.worldState.isGoal()) {
                moves = x.moves;
                searchnode i = x;
                while (i != null) {
                    list.addFirst(i.worldState);
                    i = i.pre;
                }
                break;
            } else {
                for (WorldState w : x.worldState.neighbors()) {
                    if (x.pre==null||!w.equals(x.pre.worldState)) {
                        q.insert(new searchnode(w, x.moves + 1, x));
                    }
                }
            }
        }
    }

    public int moves() {
        return moves;
    }

    public Iterable<WorldState> solution() {
        return list;
    }
}

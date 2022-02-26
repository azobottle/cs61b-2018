package lab11.graphs;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private Maze maze;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /**
     * Conducts a breadth first search of the maze starting at the source.
     */
    private void bfs(int v) {
        marked[v] = true;
        announce();
        Queue<Integer> q = new LinkedList<>();
        q.offer(v);
        while (!q.isEmpty()){
            int p=q.peek();
            for (int w : maze.adj(p)) {
                if (!marked[w]) {
                    q.offer(w);
                    edgeTo[w] = p;
                    distTo[w] = distTo[p] + 1;
                    marked[w]=true;
                    announce();
                    if(w==t){
                        return;
                    }
                }
            }
            q.poll();
        }
    }


    @Override
    public void solve() {
        bfs(s);
    }
}


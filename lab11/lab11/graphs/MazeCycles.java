package lab11.graphs;

/**
 * @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private boolean cyclefound;
    private int[] myedgeTo;

    public MazeCycles(Maze m) {
        super(m);
        myedgeTo = new int[maze.V()];
        for (int i = 0; i < maze.V(); i += 1) {
            myedgeTo[i] = Integer.MAX_VALUE;
        }
    }

    @Override
    public void solve() {
        dfs(1);
    }

    private void dfs(int v) {
        if (cyclefound) {
            return;
        }
        marked[v] = true;
        announce();
        for (int w : maze.adj(v)) {
            if (marked[w] && myedgeTo[v] != w) {
                cyclefound = true;
                drawcycle(v, w);
                return;
            }
        }
        for (int w : maze.adj(v)) {
            if (!marked[w]) {
                myedgeTo[w] = v;
                dfs(w);
            }
        }
    }

    private void drawcycle(int start, int end) {
        int t = start;
        while (t != end) {
            edgeTo[t] = myedgeTo[t];
            t = myedgeTo[t];
            announce();
        }
        edgeTo[end] = start;
        announce();
    }
    // Helper methods go here
}


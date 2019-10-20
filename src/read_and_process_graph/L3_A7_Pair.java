package read_and_process_graph;

public class L3_A7_Pair<L,R> {

    private final L left;
    private final R right;

    public L3_A7_Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() { return left; }
    public R getRight() { return right; }

    @Override
    public int hashCode() { return left.hashCode() ^ right.hashCode(); }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof L3_A7_Pair)) return false;
        L3_A7_Pair pairo = (L3_A7_Pair) o;
        return this.left.equals(pairo.getLeft()) &&
                this.right.equals(pairo.getRight());
    }

}
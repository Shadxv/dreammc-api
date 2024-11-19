package pl.dreammc.dreammcapi.api.util.tree;

import lombok.Getter;

public abstract class BinaryTree2d<T> {

    @Getter private Node<T> root;

    public void insert(T value) {
        root = insertRec(root, new Node<>(value), 0);
    }

    private Node<T> insertRec(Node<T> current, Node<T> newNode, int depth) {
        if (current == null) {
            return newNode;
        }

        int dim = depth % 2;
        int point = dim == 0 ? this.getNodeX(newNode) : this.getNodeZ(newNode);
        int currentPoint = dim == 0 ? this.getNodeX(current) : this.getNodeZ(current);

        if (point < currentPoint) {
            current.setLeftNode(insertRec(current.getLeftNode(), newNode, depth + 1));
        } else {
            current.setRightNode(insertRec(current.getRightNode(), newNode, depth + 1));
        }

        return current;
    }

    public T nearest(int x, int z) {
        return nearestRec(root, x, z, 0, null, Double.MAX_VALUE).getValue();
    }

    private Node<T> nearestRec(Node<T> current, int targetX, int targetZ, int depth, Node<T> best, double bestDist) {
        if (current == null) {
            return best;
        }

        double distance = euclideanDistance(this.getNodeX(current), this.getNodeZ(current), targetX, targetZ);

        if (distance < bestDist) {
            best = current;
            bestDist = distance;
        }

        int dim = depth % 2;
        int targetPos = dim == 0 ? targetX : targetZ;
        int currentPos = dim == 0 ? this.getNodeX(current) : this.getNodeZ(current);

        Node<T> next = targetPos < currentPos ? current.getLeftNode() : current.getRightNode();
        Node<T> other = targetPos < currentPos ? current.getRightNode() : current.getLeftNode();

        best = nearestRec(next, targetX, targetZ, depth + 1, best, bestDist);

        if (Math.abs(currentPos - targetPos) < bestDist) {
            best = nearestRec(other, targetX, targetZ, depth + 1, best, bestDist);
        }

        return best;
    }

    private double euclideanDistance(int x1, int z1, int x2, int z2) {
        double dx = x1 - x2;
        double dz = z1 - z2;
        return Math.sqrt(dx * dx + dz * dz);
    }

    public abstract int getNodeX(Node<T> node);
    public abstract int getNodeZ(Node<T> node);

}

package pl.dreammc.dreammcapi.api.util.tree;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public abstract class BinaryTree2d<T> {

    @Getter private Node<T> root;

    public void insert(T value) {
        root = insertRec(root, new Node<>(value), 0);
    }

    private Node<T> insertRec(Node<T> current, Node<T> newNode, int depth) {
        if (current == null) {
            return newNode;
        }

        int dim = depth % 2; // 0 -> X, 1 -> Z
        int point = dim == 0 ? this.getNodeX(newNode) : this.getNodeZ(newNode);
        int currentPoint = dim == 0 ? this.getNodeX(current) : this.getNodeZ(current);

        if (point < currentPoint || (point == currentPoint && dim == 0 && this.getNodeZ(newNode) < this.getNodeZ(current))
                || (point == currentPoint && dim == 1 && this.getNodeX(newNode) < this.getNodeX(current))) {
            current.setLeftNode(insertRec(current.getLeftNode(), newNode, depth + 1));
        } else {
            current.setRightNode(insertRec(current.getRightNode(), newNode, depth + 1));
        }

        return current;
    }

    @Nullable
    public T nearest(int x, int z) {
        Node<T> result = nearestRec(root, x, z, 0, null, Double.MAX_VALUE);
        if (result == null) return null;
        return result.getValue();
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

        int dim = depth % 2; // 0 -> X, 1 -> Z
        int targetPos = dim == 0 ? targetX : targetZ;
        int currentPos = dim == 0 ? this.getNodeX(current) : this.getNodeZ(current);

        Node<T> next = targetPos < currentPos ? current.getLeftNode() : current.getRightNode();
        Node<T> other = targetPos < currentPos ? current.getRightNode() : current.getLeftNode();

        best = nearestRec(next, targetX, targetZ, depth + 1, best, bestDist);

        if (Math.abs(currentPos - targetPos) <= bestDist) {
            best = nearestRec(other, targetX, targetZ, depth + 1, best, bestDist);
        }

        return best;
    }

    public void remove(T value) {
        root = removeRec(root, new Node<>(value), 0);
    }

    private Node<T> removeRec(Node<T> current, Node<T> nodeToRemove, int depth) {
        if (current == null) {
            return null;
        }

        int dim = depth % 2; // 0 -> X, 1 -> Z
        int nodeX = this.getNodeX(nodeToRemove);
        int nodeZ = this.getNodeZ(nodeToRemove);
        int currentX = this.getNodeX(current);
        int currentZ = this.getNodeZ(current);

        if (nodeX == currentX && nodeZ == currentZ && current.getValue().equals(nodeToRemove.getValue())) {
            if (current.getRightNode() == null && current.getLeftNode() == null) {
                return null;
            }

            if (current.getRightNode() != null) {
                Node<T> minNode = findMin(current.getRightNode(), depth, dim);
                current.setValue(minNode.getValue());
                current.setRightNode(removeRec(current.getRightNode(), minNode, depth + 1));
            } else {
                Node<T> minNode = findMin(current.getLeftNode(), depth, dim);
                current.setValue(minNode.getValue());
                current.setLeftNode(removeRec(current.getLeftNode(), minNode, depth + 1));
            }
            return current;
        }

        if ((dim == 0 && nodeX < currentX) || (dim == 1 && nodeZ < currentZ)) {
            current.setLeftNode(removeRec(current.getLeftNode(), nodeToRemove, depth + 1));
        } else {
            current.setRightNode(removeRec(current.getRightNode(), nodeToRemove, depth + 1));
        }

        return current;
    }

    private Node<T> findMin(Node<T> current, int depth, int dim) {
        if (current == null) return null;

        int currentDim = depth % 2;
        if (currentDim == dim) {
            if (current.getLeftNode() == null) {
                return current;
            }
            return findMin(current.getLeftNode(), depth + 1, dim);
        }

        // W przeciwnym razie porównujemy minimum lewej i prawej gałęzi oraz bieżącego węzła
        Node<T> leftMin = findMin(current.getLeftNode(), depth + 1, dim);
        Node<T> rightMin = findMin(current.getRightNode(), depth + 1, dim);

        Node<T> min = current;
        if (leftMin != null && getCoordinate(leftMin, dim) < getCoordinate(min, dim)) {
            min = leftMin;
        }
        if (rightMin != null && getCoordinate(rightMin, dim) < getCoordinate(min, dim)) {
            min = rightMin;
        }

        return min;
    }

    private int getCoordinate(Node<T> node, int dim) {
        return dim == 0 ? this.getNodeX(node) : this.getNodeZ(node);
    }

    private double euclideanDistance(int x1, int z1, int x2, int z2) {
        double dx = x1 - x2;
        double dz = z1 - z2;
        return Math.sqrt(dx * dx + dz * dz);
    }

    public abstract int getNodeX(Node<T> node);
    public abstract int getNodeZ(Node<T> node);

}

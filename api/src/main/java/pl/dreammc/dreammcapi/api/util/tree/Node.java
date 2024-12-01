package pl.dreammc.dreammcapi.api.util.tree;

import lombok.Getter;
import lombok.Setter;

public class Node<T> {

    @Getter @Setter private T value;
    @Getter @Setter private Node<T> leftNode;
    @Getter @Setter private Node<T> rightNode;

    public Node(T value){
        this.value = value;
    }
}

package filesystem;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DirectoryNode extends Node {
    private final Map<String, Node> children = new TreeMap<>();

    public DirectoryNode(String name) {
        super(name);
    }

    public void add(Node child) {
        if (child == null) {
            throw new IllegalArgumentException("Child must not be null");
        }
        if (child.getParent() != null) {
            throw new IllegalArgumentException("Node '" + child.getName() + "' already has a parent");
        }
        if (children.containsKey(child.getName())) {
            throw new IllegalArgumentException("Duplicate name '" + child.getName() + "' in '" + getName() + "'");
        }
        if (createsCycle(child)) {
            throw new IllegalArgumentException("Adding '" + child.getName() + "' would create a cycle");
        }
        children.put(child.getName(), child);
        child.setParent(this);
    }

    public void remove(String childName) {
        Node removed = children.remove(childName);
        if (removed == null) {
            throw new IllegalArgumentException("No child named '" + childName + "'");
        }
        removed.setParent(null);
    }

    public List<Node> getChildren() {
        return List.copyOf(children.values());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private boolean createsCycle(Node child) {
        for (DirectoryNode current = this; current != null; current = current.getParent()) {
            if (current == child) {
                return true;
            }
        }
        return false;
    }
}

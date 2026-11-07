package filesystem;

public abstract class Node implements Element {
    private final String name;
    private DirectoryNode parent;

    protected Node(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be empty");
        }
        if (name.contains("/")) {
            throw new IllegalArgumentException("Name must not contain '/'");
        }
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    DirectoryNode getParent() {
        return parent;
    }

    void setParent(DirectoryNode parent) {
        this.parent = parent;
    }
}

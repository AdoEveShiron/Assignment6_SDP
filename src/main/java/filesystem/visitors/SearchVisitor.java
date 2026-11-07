package filesystem.visitors;

import filesystem.DirectoryNode;
import filesystem.Element;
import filesystem.FileNode;
import filesystem.Visitor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class SearchVisitor implements Visitor {
    private final String nameContains;
    private final long minSize;
    private final List<String> foundPaths = new ArrayList<>();
    private final Deque<String> currentFolders = new ArrayDeque<>();
    private int depth;

    public SearchVisitor(String nameContains, long minSize) {
        if (nameContains == null) {
            throw new IllegalArgumentException("Name criterion must not be null");
        }
        if (minSize < 0) {
            throw new IllegalArgumentException("Minimum size must not be negative");
        }
        this.nameContains = nameContains;
        this.minSize = minSize;
    }

    public List<String> search(Element root) {
        foundPaths.clear();
        currentFolders.clear();
        depth = 0;
        root.accept(this);
        List<String> result = new ArrayList<>(foundPaths);
        Collections.sort(result);
        return result;
    }

    @Override
    public void visit(FileNode file) {
        if (matches(file)) {
            foundPaths.add(buildPath(file.getName()));
        }
    }

    @Override
    public void visit(DirectoryNode directory) {
        enter(directory);
        for (Element child : directory.getChildren()) {
            child.accept(this);
        }
        leave();
    }

    private boolean matches(FileNode file) {
        return file.getName().contains(nameContains) && file.getSize() >= minSize;
    }

    private void enter(DirectoryNode directory) {
        if (depth > 0) {
            currentFolders.addLast(directory.getName());
        }
        depth++;
    }

    private void leave() {
        depth--;
        if (depth > 0) {
            currentFolders.removeLast();
        }
    }

    private String buildPath(String fileName) {
        List<String> parts = new ArrayList<>(currentFolders);
        parts.add(fileName);
        return String.join("/", parts);
    }
}

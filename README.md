# Visitor Pattern - Advanced File System

Assignment 06, ShP-2216 Software Design Patterns. Java 17, Maven, JUnit 5.

An in-memory file system (files and nested directories) with three independent operations implemented as visitors.

## Structure

```
src/main/java/filesystem/            Element, Visitor, Node, FileNode, DirectoryNode
src/main/java/filesystem/visitors/   TotalSizeVisitor, SearchVisitor, StatisticsVisitor
src/main/java/app/Main.java          deterministic demo
src/test/java/filesystem/            JUnit 5 tests
docs/uml.png                         UML class diagram
```

## Commands

Run all tests:

```
mvn test
```

Run the demo:

```
mvn -q compile
java -cp target/classes app.Main
```

## Design notes

- Double dispatch: `accept(Visitor)` in `FileNode` and `DirectoryNode` calls `visitor.visit(this)`, so the overloaded `visit` method is chosen by the element type.
- Traversal owner: the visitors. `visit(DirectoryNode)` loops over `getChildren()` and calls `child.accept(this)`. Children are sorted by name, every node is visited exactly once.
- State: every visitor can be reused. Each run method (`calculate`, `search`, `collect`) resets the state first, so results do not depend on earlier runs.
- Paths are relative to the chosen root, use `/` and are sorted lexicographically. The root directory name is not part of the path. Depth of the root is 0.
- Search: a file matches when its name contains the given text and its size is >= the minimum size.
- Integrity (`DirectoryNode.add`): no duplicate sibling names, no second parent, no cycles. File size cannot be negative. Invalid changes throw `IllegalArgumentException`.
- Extension boundary: `StatisticsVisitor` was added without changing `FileNode`, `DirectoryNode` or `accept`. A new element type would need a new `visit` method in `Visitor` and in all visitors.

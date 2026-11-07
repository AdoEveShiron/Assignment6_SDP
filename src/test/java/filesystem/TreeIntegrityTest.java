package filesystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TreeIntegrityTest {

    @Test
    void negativeFileSizeIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new FileNode("a.txt", -1));
    }

    @Test
    void zeroSizeFileIsAllowed() {
        assertEquals(0, new FileNode("empty.txt", 0).getSize());
    }

    @Test
    void invalidNamesAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new FileNode("", 1));
        assertThrows(IllegalArgumentException.class, () -> new DirectoryNode("  "));
        assertThrows(IllegalArgumentException.class, () -> new DirectoryNode("a/b"));
    }

    @Test
    void duplicateSiblingNameIsRejectedEvenForDifferentTypes() {
        DirectoryNode root = new DirectoryNode("root");
        root.add(new FileNode("same", 1));
        assertThrows(IllegalArgumentException.class, () -> root.add(new DirectoryNode("same")));
        assertEquals(1, root.getChildren().size());
    }

    @Test
    void sameNameInDifferentDirectoriesIsAllowed() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode first = new DirectoryNode("first");
        DirectoryNode second = new DirectoryNode("second");
        root.add(first);
        root.add(second);
        first.add(new FileNode("data.txt", 1));
        second.add(new FileNode("data.txt", 2));
        assertEquals(1, first.getChildren().size());
        assertEquals(1, second.getChildren().size());
    }

    @Test
    void nodeCannotHaveTwoParents() {
        DirectoryNode first = new DirectoryNode("first");
        DirectoryNode second = new DirectoryNode("second");
        FileNode file = new FileNode("a.txt", 1);
        first.add(file);
        assertThrows(IllegalArgumentException.class, () -> second.add(file));
        assertEquals(0, second.getChildren().size());
    }

    @Test
    void directoryCannotBeAddedToItself() {
        DirectoryNode root = new DirectoryNode("root");
        assertThrows(IllegalArgumentException.class, () -> root.add(root));
    }

    @Test
    void cycleThroughAncestorIsRejected() {
        DirectoryNode a = new DirectoryNode("a");
        DirectoryNode b = new DirectoryNode("b");
        DirectoryNode c = new DirectoryNode("c");
        a.add(b);
        b.add(c);
        assertThrows(IllegalArgumentException.class, () -> c.add(a));
        assertEquals(0, c.getChildren().size());
    }

    @Test
    void removedNodeCanBeMovedToAnotherDirectory() {
        DirectoryNode first = new DirectoryNode("first");
        DirectoryNode second = new DirectoryNode("second");
        FileNode file = new FileNode("a.txt", 1);
        first.add(file);
        first.remove("a.txt");
        second.add(file);
        assertEquals(0, first.getChildren().size());
        assertEquals(1, second.getChildren().size());
    }

    @Test
    void removingUnknownChildIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new DirectoryNode("root").remove("none"));
    }

    @Test
    void deepNestingIsSupported() {
        DirectoryNode root = new DirectoryNode("d0");
        DirectoryNode current = root;
        for (int i = 1; i <= 50; i++) {
            DirectoryNode next = new DirectoryNode("d" + i);
            current.add(next);
            current = next;
        }
        current.add(new FileNode("deep.txt", 5));
        assertEquals(1, current.getChildren().size());
    }

    @Test
    void childrenListCannotBeUsedToChangeTheDirectory() {
        DirectoryNode root = new DirectoryNode("root");
        root.add(new FileNode("a", 1));
        assertThrows(UnsupportedOperationException.class, () -> root.getChildren().clear());
        assertEquals(1, root.getChildren().size());
    }
}

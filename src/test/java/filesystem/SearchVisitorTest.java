package filesystem;

import filesystem.visitors.SearchVisitor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchVisitorTest {

    @Test
    void combinesNameAndMinimumSizeConditions() {
        List<String> result = new SearchVisitor(".txt", 100).search(TestTrees.library());
        assertEquals(List.of("books/novel.txt", "books/poems.txt"), result);
    }

    @Test
    void minimumSizeIsInclusive() {
        List<String> result = new SearchVisitor("poems", 150).search(TestTrees.library());
        assertEquals(List.of("books/poems.txt"), result);
        assertTrue(new SearchVisitor("poems", 151).search(TestTrees.library()).isEmpty());
    }

    @Test
    void nameOnlyMatchDoesNotPassWhenFileIsTooSmall() {
        assertTrue(new SearchVisitor("readme", 100).search(TestTrees.library()).isEmpty());
    }

    @Test
    void pathsAreRootRelativeWithSlashAndSorted() {
        List<String> result = new SearchVisitor(".txt", 0).search(TestTrees.library());
        assertEquals(List.of(
                "books/novel.txt",
                "books/poems.txt",
                "books/science/notes.txt",
                "readme.txt"), result);
    }

    @Test
    void pathsAreRelativeToTheChosenRoot() {
        DirectoryNode books = TestTrees.createLibrary().books();
        List<String> result = new SearchVisitor("notes", 0).search(books);
        assertEquals(List.of("science/notes.txt"), result);
    }

    @Test
    void sortingIsLexicographicOnFullPath() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode a = new DirectoryNode("a");
        a.add(new FileNode("z.txt", 1));
        root.add(a);
        root.add(new FileNode("a-b.txt", 1));
        List<String> result = new SearchVisitor(".txt", 0).search(root);
        assertEquals(List.of("a-b.txt", "a/z.txt"), result);
    }

    @Test
    void noMatchAndEmptyDirectoryGiveEmptyList() {
        assertTrue(new SearchVisitor("missing", 0).search(TestTrees.library()).isEmpty());
        assertTrue(new SearchVisitor("", 0).search(new DirectoryNode("empty")).isEmpty());
    }

    @Test
    void fileRootIsReportedByItsOwnName() {
        List<String> result = new SearchVisitor("a", 0).search(new FileNode("a.txt", 3));
        assertEquals(List.of("a.txt"), result);
    }

    @Test
    void repeatedSearchDoesNotAccumulateResults() {
        SearchVisitor visitor = new SearchVisitor(".txt", 0);
        DirectoryNode library = TestTrees.library();
        List<String> first = visitor.search(library);
        List<String> second = visitor.search(library);
        assertEquals(4, first.size());
        assertEquals(first, second);
        assertTrue(visitor.search(new DirectoryNode("empty")).isEmpty());
    }

    @Test
    void invalidConfigurationIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new SearchVisitor(null, 0));
        assertThrows(IllegalArgumentException.class, () -> new SearchVisitor("a", -1));
    }
}

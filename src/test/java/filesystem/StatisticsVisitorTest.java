package filesystem;

import filesystem.visitors.StatisticsVisitor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatisticsVisitorTest {

    @Test
    void countsFilesDirectoriesAndDepthOfNestedTree() {
        StatisticsVisitor statistics = new StatisticsVisitor();
        statistics.collect(TestTrees.library());
        assertEquals(6, statistics.getFileCount());
        assertEquals(4, statistics.getDirectoryCount());
        assertEquals(3, statistics.getMaxDepth());
    }

    @Test
    void rootDirectoryIsCountedAndHasDepthZero() {
        StatisticsVisitor statistics = new StatisticsVisitor();
        statistics.collect(new DirectoryNode("empty"));
        assertEquals(0, statistics.getFileCount());
        assertEquals(1, statistics.getDirectoryCount());
        assertEquals(0, statistics.getMaxDepth());
    }

    @Test
    void fileDirectlyInRootHasDepthOne() {
        DirectoryNode root = new DirectoryNode("root");
        root.add(new FileNode("a", 1));
        StatisticsVisitor statistics = new StatisticsVisitor();
        statistics.collect(root);
        assertEquals(1, statistics.getMaxDepth());
    }

    @Test
    void depthDependsOnChosenRoot() {
        DirectoryNode books = TestTrees.createLibrary().books();
        StatisticsVisitor statistics = new StatisticsVisitor();
        statistics.collect(books);
        assertEquals(4, statistics.getFileCount());
        assertEquals(2, statistics.getDirectoryCount());
        assertEquals(2, statistics.getMaxDepth());
    }

    @Test
    void repeatedRunsDoNotMultiplyCounters() {
        StatisticsVisitor statistics = new StatisticsVisitor();
        DirectoryNode library = TestTrees.library();
        statistics.collect(library);
        statistics.collect(library);
        assertEquals(6, statistics.getFileCount());
        assertEquals(4, statistics.getDirectoryCount());
        statistics.collect(new FileNode("single", 1));
        assertEquals(1, statistics.getFileCount());
        assertEquals(0, statistics.getDirectoryCount());
        assertEquals(0, statistics.getMaxDepth());
    }
}

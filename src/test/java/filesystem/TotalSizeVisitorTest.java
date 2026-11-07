package filesystem;

import filesystem.visitors.TotalSizeVisitor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TotalSizeVisitorTest {

    @Test
    void sumsFilesAcrossAllLevels() {
        assertEquals(5450, new TotalSizeVisitor().calculate(TestTrees.library()));
    }

    @Test
    void subtreeSumIgnoresFilesOutsideIt() {
        DirectoryNode books = TestTrees.createLibrary().books();
        assertEquals(5110, new TotalSizeVisitor().calculate(books));
    }

    @Test
    void emptyDirectoryHasSizeZero() {
        assertEquals(0, new TotalSizeVisitor().calculate(new DirectoryNode("empty")));
    }

    @Test
    void singleFileRootCountsItselfOnce() {
        assertEquals(77, new TotalSizeVisitor().calculate(new FileNode("one.bin", 77)));
    }

    @Test
    void sameVisitorGivesSameResultOnRepeatedRuns() {
        TotalSizeVisitor visitor = new TotalSizeVisitor();
        DirectoryNode library = TestTrees.library();
        assertEquals(5450, visitor.calculate(library));
        assertEquals(5450, visitor.calculate(library));
        assertEquals(0, visitor.calculate(new DirectoryNode("other")));
        assertEquals(5450, visitor.calculate(library));
    }
}

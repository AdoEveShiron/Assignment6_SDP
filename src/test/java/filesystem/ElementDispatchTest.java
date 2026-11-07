package filesystem;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ElementDispatchTest {

    private static class RecordingVisitor implements Visitor {
        final List<String> calls = new ArrayList<>();

        @Override
        public void visit(FileNode file) {
            calls.add("file:" + file.getName());
        }

        @Override
        public void visit(DirectoryNode directory) {
            calls.add("directory:" + directory.getName());
        }
    }

    @Test
    void fileAcceptCallsFileVisitMethod() {
        RecordingVisitor visitor = new RecordingVisitor();
        new FileNode("a.txt", 5).accept(visitor);
        assertEquals(List.of("file:a.txt"), visitor.calls);
    }

    @Test
    void directoryAcceptCallsDirectoryVisitMethod() {
        RecordingVisitor visitor = new RecordingVisitor();
        new DirectoryNode("docs").accept(visitor);
        assertEquals(List.of("directory:docs"), visitor.calls);
    }

    @Test
    void elementsTypedAsInterfaceStillReachCorrectMethod() {
        RecordingVisitor visitor = new RecordingVisitor();
        List<Element> elements = List.of(new DirectoryNode("d"), new FileNode("f", 1));
        for (Element element : elements) {
            element.accept(visitor);
        }
        assertEquals(List.of("directory:d", "file:f"), visitor.calls);
    }
}

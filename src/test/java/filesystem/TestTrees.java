package filesystem;

final class TestTrees {

    private TestTrees() {
    }

    record Library(DirectoryNode root, DirectoryNode books) {
    }

    static DirectoryNode library() {
        return createLibrary().root();
    }

    static Library createLibrary() {
        DirectoryNode library = new DirectoryNode("library");
        library.add(new FileNode("catalog.csv", 300));
        library.add(new FileNode("readme.txt", 40));

        DirectoryNode books = new DirectoryNode("books");
        books.add(new FileNode("novel.txt", 900));
        books.add(new FileNode("poems.txt", 150));

        DirectoryNode science = new DirectoryNode("science");
        science.add(new FileNode("physics.pdf", 4000));
        science.add(new FileNode("notes.txt", 60));
        books.add(science);
        library.add(books);

        library.add(new DirectoryNode("empty"));
        return new Library(library, books);
    }
}

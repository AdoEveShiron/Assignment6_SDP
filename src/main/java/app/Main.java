package app;

import filesystem.DirectoryNode;
import filesystem.FileNode;
import filesystem.visitors.SearchVisitor;
import filesystem.visitors.StatisticsVisitor;
import filesystem.visitors.TotalSizeVisitor;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DirectoryNode src = createSrc();
        DirectoryNode project = createProject(src);

        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        System.out.println("Total size: " + sizeVisitor.calculate(project) + " bytes");

        StatisticsVisitor statistics = new StatisticsVisitor();
        statistics.collect(project);
        System.out.println("Files: " + statistics.getFileCount());
        System.out.println("Directories: " + statistics.getDirectoryCount());
        System.out.println("Max depth: " + statistics.getMaxDepth());

        System.out.println("Search 'e' with size >= 1000:");
        printAll(new SearchVisitor("e", 1000).search(project));

        System.out.println("Search '.txt' with size >= 0:");
        printAll(new SearchVisitor(".txt", 0).search(project));

        System.out.println("Search 'zzz' with size >= 0:");
        printAll(new SearchVisitor("zzz", 0).search(project));

        System.out.println("Size of src only: " + sizeVisitor.calculate(src) + " bytes");

        try {
            project.add(new FileNode("README.md", 10));
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }

    private static DirectoryNode createSrc() {
        DirectoryNode src = new DirectoryNode("src");
        src.add(new FileNode("Main.java", 3400));
        src.add(new FileNode("Utils.java", 800));
        DirectoryNode util = new DirectoryNode("util");
        util.add(new FileNode("Helper.java", 1500));
        util.add(new FileNode("notes.txt", 200));
        src.add(util);
        return src;
    }

    private static DirectoryNode createProject(DirectoryNode src) {
        DirectoryNode project = new DirectoryNode("project");
        project.add(new FileNode("README.md", 1200));
        project.add(src);

        DirectoryNode docs = new DirectoryNode("docs");
        docs.add(new FileNode("guide.txt", 5000));
        DirectoryNode images = new DirectoryNode("images");
        images.add(new FileNode("logo.png", 8000));
        docs.add(images);
        project.add(docs);

        project.add(new DirectoryNode("build"));
        return project;
    }

    private static void printAll(List<String> paths) {
        if (paths.isEmpty()) {
            System.out.println("  (nothing found)");
        }
        for (String path : paths) {
            System.out.println("  " + path);
        }
    }
}

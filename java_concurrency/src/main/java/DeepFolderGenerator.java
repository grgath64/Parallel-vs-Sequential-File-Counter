import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DeepFolderGenerator {
    static Random rand = new Random();

    public static void main(String[] args) throws IOException {
        String basePath = "test_root";
        int topDirs = 4;        // 4 top-level folders
        int maxDepth = 4;       // up to 4 levels deep
        int maxFiles = 5;       // up to 5 files per folder
        int maxSubDirs = 3;     // up to 3 subfolders per folder

        for (int i = 0; i < topDirs; i++) {
            String dirPath = basePath + "/folder_" + i;
            File dir = new File(dirPath);
            dir.mkdirs();
            generateLikeWorks(dir, 1, maxDepth, maxFiles, maxSubDirs);
        }

        System.out.println("Replica 'works' structure created at: " + basePath);
    }

    static void generateLikeWorks(File dir, int depth, int maxDepth, int maxFiles, int maxSubDirs) throws IOException {
        // Always put some files here
        int numFiles = rand.nextInt(maxFiles) + 1;
        for (int i = 0; i < numFiles; i++) {
            File file = new File(dir, "file_" + i + ".txt");
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("Dummy content for " + file.getName() + "\n");
                for (int j = 0; j < 50 + rand.nextInt(150); j++) {
                    writer.write("Line " + j + ": lorem ipsum dummy data goes here to bulk up the file.\n");
                }
            }
        }

        // Randomly decide: should we add subfolders too?
        if (depth < maxDepth && rand.nextBoolean()) {
            int numSubDirs = rand.nextInt(maxSubDirs) + 1;
            for (int i = 0; i < numSubDirs; i++) {
                File subDir = new File(dir, "sub_" + i);
                subDir.mkdirs();
                generateLikeWorks(subDir, depth + 1, maxDepth, maxFiles, maxSubDirs);
            }
        }
    }
}

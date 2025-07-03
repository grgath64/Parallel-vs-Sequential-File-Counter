import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class UniformFolderGenerator {
    public static void main(String[] args) throws IOException {
        String basePath = "test_root_uniform";
        int topDirs = 20;     // Top-level dirs
        int subDirs = 50;     // Subfolders in each
        int filesPerSubDir = 500; // Files per subfolder

        for (int i = 0; i < topDirs; i++) {
            String dirPath = basePath + "/dir_" + i;
            new File(dirPath).mkdirs();

            for (int j = 0; j < subDirs; j++) {
                String subDirPath = dirPath + "/sub_" + j;
                new File(subDirPath).mkdirs();

                for (int k = 0; k < filesPerSubDir; k++) {
                    File file = new File(subDirPath + "/file_" + k + ".txt");
                    try (FileWriter writer = new FileWriter(file)) {
                        writer.write("This is dummy file number " + k);
                    }
                }
            }
        }

        System.out.println("Dummy folder structure created at: " + basePath);
    }
}

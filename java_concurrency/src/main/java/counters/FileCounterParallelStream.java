package counters;

import java.io.File;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;


public class FileCounterParallelStream
       extends AbstractFileCounter {

    //Constructor initializes the fields.
    public FileCounterParallelStream(File file) {
        super(file);
    }

    //Constructor initializes the fields (used internally).
    private FileCounterParallelStream(File file,
                                      AtomicLong documentCount,
                                      AtomicLong folderCount) {
        super(file, documentCount, folderCount);
    }

    
    //This method returns the size in bytes of the file, as well
    //as all the "files" in folders reachable from {@code mFile}.
    @Override
    protected Long compute() {

        // Base case: it's a single file
        if (mFile.isFile()) {
            mDocumentCount.incrementAndGet();
            return mFile.length();
        }

        // Defensive: if not a directory or cannot be listed, skip
        if (!mFile.isDirectory()) {
            return 0L;
        }

        File[] files = mFile.listFiles();
        if (files == null) {
            return 0L;
        }

        mFolderCount.incrementAndGet();

        return Stream
            .of(files)
            .parallel()
            .mapToLong(file -> new FileCounterParallelStream
                    (file, mDocumentCount, mFolderCount).compute())
            .sum();
    }
}


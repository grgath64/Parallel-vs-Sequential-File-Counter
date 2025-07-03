package counters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.atomic.AtomicLong;


public class FileCounterForkJoinTask
       extends AbstractFileCounter {
    
    //Constructor initializes the fields.
    public FileCounterForkJoinTask(File file) {
        super(file);
    }

    
    //Constructor initializes the fields (used internally).
    private FileCounterForkJoinTask(File file,
                                    AtomicLong documentCount,
                                    AtomicLong folderCount) {
        super(file, documentCount, folderCount);
    }

    //return The size in bytes of the file plus all the files in folders (recursively) reachable from this file.
    @Override
    protected Long compute() {
        if (mFile.isFile()) {
            mDocumentCount.incrementAndGet();
            return mFile.length();
        }

        if (!mFile.isDirectory()) {
            return 0L;
        }

        File[] files = mFile.listFiles();
        if (files == null) {
            return 0L;
        }

        mFolderCount.incrementAndGet();

        List<ForkJoinTask<Long>> forks = new ArrayList<>();

        for (File file : files) {
            forks.add(new FileCounterForkJoinTask(file,
                mDocumentCount, mFolderCount).fork());
        }

        long sum = 0;

        for (var task : forks) {
            sum += task.join();
        }

        return sum;
    }
}



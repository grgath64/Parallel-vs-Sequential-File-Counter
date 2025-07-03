package counters;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.concurrent.ForkJoinPool;

public class FileCounterTests {
    @Test
    void testParallelAndForkJoinMatchSequential_MultipleRuns() {
        File root = new File("test_root");

        int runs = 5;

        for (int i = 0; i < runs; i++) {
            FileCounterSequentialStreamTask seq =
                new FileCounterSequentialStreamTask(root);
            long seqSize = ForkJoinPool.commonPool().invoke(seq);

            FileCounterForkJoinTask fj =
                new FileCounterForkJoinTask(root);
            long fjSize = ForkJoinPool.commonPool().invoke(fj);

            FileCounterParallelStream ps =
                new FileCounterParallelStream(root);
            long psSize = ps.compute();

            assertEquals(seqSize, fjSize, "Run " + i + ": ForkJoin must match Sequential");
            assertEquals(seqSize, psSize, "Run " + i + ": Parallel must match Sequential");
        }
    }
}


import counters.AbstractFileCounter;
import counters.FileCounterParallelStream;
import counters.FileCounterSequentialStreamTask;
import counters.FileCounterForkJoinTask;
import utils.RunTimer;

import java.io.File;
import java.util.concurrent.*;


class Main {
    
     //The main entry point into the program runs the tests.
    public static void main(String[] args) {
        System.out.println("Starting the FileCounter program");

        warmupThreadPool1();

        runFileCounterParallelStream();

        warmupThreadPool2();

        runFileCounterForkJoinTask();

        runFileCounterSequentialStreamTask();

        // Get and print the timing results.
        System.out.println(RunTimer.getTimingResults());

        System.out.println("Ending the FileCounter program");
    }

    private static void runFileCounterForkJoinTask() {
        runTest(ForkJoinPool.commonPool(),
                new FileCounterForkJoinTask
                (new File("test_root")),
                "ForkJoinTask");
    }

    private static void runFileCounterSequentialStreamTask() {
        runTest(ForkJoinPool.commonPool(),
                new FileCounterSequentialStreamTask
                (new File("test_root")),
                "SequentialStreamTask");
    }

    private static void runFileCounterParallelStream() {
        runTest(ForkJoinPool.commonPool(),
                new FileCounterParallelStream
                (new File("test_root")),
                "ParallelStream");
    }


    //Warmup the thread pool for parallel stream test.   
    private static void warmupThreadPool1() {
        runTest(ForkJoinPool.commonPool(),
                new FileCounterParallelStream
                (new File("test_root")),
                "warmup");
    }

    //Warmup the thread pool for the fork-join test.    
    private static void warmupThreadPool2() {
        runTest(ForkJoinPool.commonPool(),
                new FileCounterForkJoinTask
                (new File("test_root")),
                "warmup");
    }

    /**
     * Run all the tests and collect/print the results.
     *
     * @param fJPool The fork-join pool to use for the test
     * @param testTask The file counter task to run
     * @param testName The name of the test
     */
    private static void runTest(ForkJoinPool fJPool,
                                AbstractFileCounter testTask,
                                String testName) {
        // Run the GC first to avoid perturbing the tests.
        System.gc();

        if (testName.equals("warmup")) {
            fJPool.invoke(testTask);
            return;
        }

        // Run the task on the root of a large directory hierarchy.
        long size = RunTimer
            .timeRun(() -> fJPool.invoke(testTask),
                     testName);

        // Print the results.
        System.out.println(testName
                           + ": "
                           + (testTask.documentCount()
                              + testTask.folderCount())
                           + " files ("
                           + testTask.documentCount()
                           + " documents and " 
                           + testTask.folderCount()
                           + " folders) contained "
                           + size // / 1_000_000)
                           + " bytes");
    }
}


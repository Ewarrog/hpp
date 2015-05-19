package fr.tse.fi2.hpp.labs.benchmarks;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import fr.tse.fi2.hpp.labs.utils.MergeAndInsertSortMultiFred;
import fr.tse.fi2.hpp.labs.utils.MergeAndInsertSortSingleFred;
import fr.tse.fi2.hpp.labs.utils.MergeSortSingleFred;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
public class BenchmarkSortingAlgo {

	int[] intTable = null;
	
	@Param({"100000", "1000000", "10000000"})
	static int size;
	
	@Setup
	public void init() {
		intTable = MergeSortSingleFred.generateRandomArray(size);
	}
	
	@Benchmark
	public void benMergeSort() {
		int[] liste = MergeSortSingleFred.mergeSort(intTable);
	}
	
	@Benchmark
	public void benMergeAndInsertSort() {
		int[] liste = MergeAndInsertSortSingleFred.mergeSort(intTable);
	}
	
	@Benchmark
	public void benMergeAndInsertSortMulti() {
		int cores = Runtime.getRuntime().availableProcessors();
		
		ForkJoinPool forkJoinPool = new ForkJoinPool(cores);
		
		MergeAndInsertSortMultiFred task = new MergeAndInsertSortMultiFred(intTable);
		
		int[] liste = forkJoinPool.invoke(task);
	}
	
	
	public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
        .include(BenchmarkSortingAlgo.class.getSimpleName())
        .build();

        new Runner(opt).run();
		
	}
}

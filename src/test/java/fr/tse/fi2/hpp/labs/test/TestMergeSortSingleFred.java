package fr.tse.fi2.hpp.labs.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

import org.junit.Test;

import fr.tse.fi2.hpp.labs.utils.MergeAndInsertSortMultiFred;
import fr.tse.fi2.hpp.labs.utils.MergeAndInsertSortSingleFred;
import fr.tse.fi2.hpp.labs.utils.MergeSortSingleFred;

public class TestMergeSortSingleFred {
	
	int cores;
	int[] tableComp = null;
	int[] table = null;
	
	
	public TestMergeSortSingleFred() {
		cores = Runtime.getRuntime().availableProcessors();
		tableComp = MergeSortSingleFred.generateRandomArray(1000000);
		table = tableComp;
		Arrays.sort(tableComp);
	}

	@Test
	public void test() {
		//int[] table = MergeSortSingleFred.generateRandomArray(1000000);
		int[] sortedTable = MergeSortSingleFred.mergeSort(table);
		
		
		//Arrays.sort(table);
		
		assertArrayEquals(tableComp, sortedTable);
	}
	
	@Test
	public void testMergeAndInsertSort() {
		//int[] table = MergeAndInsertSortSingleFred.generateRandomArray(1000000);
		int[] sortedTable = MergeAndInsertSortSingleFred.mergeSort(table);
		
		
		//Arrays.sort(table);
		
		assertArrayEquals(tableComp, sortedTable);
	}
	
	@Test
	public void testMergeAndInsertSortMulti() {
		
		//int[] table = MergeAndInsertSortMultiFred.generateRandomArray(1000000);
		
		ForkJoinPool forkJoinPool = new ForkJoinPool(cores);
		
		MergeAndInsertSortMultiFred task = new MergeAndInsertSortMultiFred(table);
		
		int[] sortedTable = forkJoinPool.invoke(task);
		
		
		//Arrays.sort(table);
		
		assertArrayEquals(tableComp, sortedTable);
	}

}

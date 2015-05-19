package fr.tse.fi2.hpp.labs.utils;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;


public class MergeAndInsertSortMultiFred extends RecursiveTask<int[]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7770764776419826494L;
	
	
	static int size = 100000;
	
	private int[] table_;
	
	public MergeAndInsertSortMultiFred() {
		table_ = generateRandomArray(size);
	}
	
	public MergeAndInsertSortMultiFred(int[] table) {
		table_ = table;
	}
	
	public int[] getTable() {return table_;}

	public static int[] generateRandomArray(int size) {
		Random rand = new Random();
		int[] table = new int[size];
		for(int i = 0; i < size; i++) {
			int val = rand.nextInt();
			table[i] = val;
		}
		return table;
	}

	public int[] mergeSort(int[] table) {
		int[] liste1 = null;
		int[] liste2 = null;
		if(table.length > 20) {
//			liste1 = mergeSort(subArray(table, 0, table.length/2));
//			liste2 = mergeSort(subArray(table, table.length/2, table.length));
			liste1 = subArray(table, 0, table.length/2);
			liste2 = subArray(table, table.length/2, table.length);
			invokeAll(new MergeAndInsertSortMultiFred(liste1),
					new MergeAndInsertSortMultiFred(liste2));

			int[] list = new int[table.length];

			int index1 = 0;
			int index2 = 0;
			
			for(int i = 0; i < table.length; i++) {
				if(index1 < liste1.length && (index2 >= liste2.length || liste1[index1] < liste2[index2])) {
					list[i] = liste1[index1];
					index1++;
				} else if(index2 < liste2.length) {
					list[i] = liste2[index2];
					index2++;
				}
			}


			return list;
		} else {
			triInsertion(table, table.length);
			return table;
		}


	}

	public int[] subArray(int[] table, int begin, int end) {
		int[] list = new int[end-begin];
		int j = 0;
		for(int i = begin; i < end; i++) {
			list[j] = table[i];
			j++;
		}

		return list;
	}

	public void triInsertion(int[] table, int n) {
		int x, j;

		for(int i = 1; i < n; i++) {
			x = table[i];
			j = i;
			while(j > 0 && table[j - 1] > x) {
				table[j] = table[j - 1];
				j--;
			}
			table[j] = x;
		}
	}
	
	@Override
	protected int[] compute() {
		
		int[] liste1 = null;
		int[] liste2 = null;
		int[] listeRetour1 = null;
		int[] listeRetour2 = null;
		if(table_.length > 20) {
//			liste1 = mergeSort(subArray(table, 0, table.length/2));
//			liste2 = mergeSort(subArray(table, table.length/2, table.length));
			liste1 = subArray(table_, 0, table_.length/2);
			liste2 = subArray(table_, table_.length/2, table_.length);
			MergeAndInsertSortMultiFred subtask1 = new MergeAndInsertSortMultiFred(liste1);
			MergeAndInsertSortMultiFred subtask2 = new MergeAndInsertSortMultiFred(liste2);

			subtask1.fork();
			subtask2.fork();

			listeRetour1 = subtask1.join();
			listeRetour2 = subtask2.join();
			
			int[] list = new int[table_.length];

			int index1 = 0;
			int index2 = 0;
			
			for(int i = 0; i < table_.length; i++) {
				if(index1 < listeRetour1.length && (index2 >= listeRetour2.length || listeRetour1[index1] < listeRetour2[index2])) {
					list[i] = listeRetour1[index1];
					index1++;
				} else if(index2 < listeRetour2.length) {
					list[i] = listeRetour2[index2];
					index2++;
				}
			}


			return list;
		} else {
			triInsertion(table_, table_.length);
			return table_;
		}
	}

	public static void main(String[] args) {
		int cores = Runtime.getRuntime().availableProcessors();
		int[] intTable = generateRandomArray(size);
		
		ForkJoinPool forkJoinPool = new ForkJoinPool(cores);
		
		MergeAndInsertSortMultiFred task = new MergeAndInsertSortMultiFred(intTable);
		
		int[] liste = forkJoinPool.invoke(task);

		for (Integer integer : liste) {
			System.out.println(integer);
		}
		System.out.println(liste.length);
	}

}

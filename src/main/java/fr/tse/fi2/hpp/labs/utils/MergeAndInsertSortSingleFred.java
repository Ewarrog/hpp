package fr.tse.fi2.hpp.labs.utils;

import java.util.Random;


public class MergeAndInsertSortSingleFred {

	static int size = 100000;

	public static int[] generateRandomArray(int size) {
		Random rand = new Random();
		int[] table = new int[size];
		for(int i = 0; i < size; i++) {
			int val = rand.nextInt();
			table[i] = val;
		}
		return table;
	}

	public static int[] mergeSort(int[] table) {
		int[] liste1 = null;
		int[] liste2 = null;
		if(table.length > 20) {
			liste1 = mergeSort(subArray(table, 0, table.length/2));
			liste2 = mergeSort(subArray(table, table.length/2, table.length));

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

	public static int[] subArray(int[] table, int begin, int end) {
		int[] list = new int[end-begin];
		int j = 0;
		for(int i = begin; i < end; i++) {
			list[j] = table[i];
			j++;
		}

		return list;
	}

	public static void triInsertion(int[] table, int n) {
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

	public static void main(String[] args) {
		int[] intTable = generateRandomArray(size);
		int[] liste = mergeSort(intTable);

		for (Integer integer : liste) {
			System.out.println(integer);
		}
		System.out.println(liste.length);
	}
}

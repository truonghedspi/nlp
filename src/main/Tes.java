package main;

import reordering.AlignmentMatrix;

public class Tes {
	public static void main(String[] args) {
			AlignmentMatrix matrix = new AlignmentMatrix("0 1 2 3", "0 1 2", "0-2 2-1 3-0");
			matrix.blockExtracting();;
			//System.out.println(matrix.toString());
	}
}

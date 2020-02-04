package com.michael.pan.eviltower.data;

import java.util.Arrays;
import java.util.List;

public class FloorData {
	private int[][] layer00, layer01, layer02, layer03, layerMerged;
	private int xCount, yCount;
	private String[] freeBackground, pitfallBackground, doors, npcs, enemies, treasures;

	public void setLayerMerged(int[][] layerMerged) {
		this.layerMerged = layerMerged;
	}

	FloorData(int[][] layer00, int[][] layer01, int[][] layer02, int[][] layer03, int xCount, int yCount) {
		this.layer00 = layer00;
		this.layer01 = layer01;
		this.layer02 = layer02;
		this.layer03 = layer03;
		this.xCount = xCount;
		this.yCount = yCount;
		freeBackground = new String[]{"1", "2", "3", "4", "5", "7", "8", "9", "10", "21", "41"};
		pitfallBackground = new String[]{"362", "24", "25"};
		doors = new String[]{"26", "27", "28", "29", "30", "31"};
		layerMerged = getMergedLayer();

	}

	public int[][] getMergedLayer() {
		List<String> backgroundList = Arrays.asList(freeBackground);
		List<String> pitfallList = Arrays.asList(pitfallBackground);
		List<String> doorList = Arrays.asList(doors);
		int[][] mergedLayer = new int[yCount][xCount];
		for (int m = 0; m < yCount; m++) {
			for (int n = 0; n < xCount; n++) {
				//Background encoding
				if (layer00 != null && layer00[m][n] != 0) {
					if (backgroundList.contains(String.valueOf(layer00[m][n]))) {
						mergedLayer[m][n] = 0;
					} else if (pitfallList.contains(String.valueOf(layer00[m][n]))) {
						mergedLayer[m][n] = 1;
					} else if (doorList.contains(String.valueOf(layer00[m][n]))) {
						mergedLayer[m][n] = 2;
					} else {
						mergedLayer[m][n] = 10;
					}
				}
				//npcs encoding
				if (layer01 != null && layer01[m][n] != 0) mergedLayer[m][n] = 100;
				//enemies encoding
				if (layer02 != null && layer02[m][n] != 0) mergedLayer[m][n] = 200;
				// treasures encoding
				if (layer03 != null && layer03[m][n] != 0) mergedLayer[m][n] = 300;
			}
		}
		return mergedLayer;
	}

	public int[][] getLayer00() {
		return layer00;
	}

	public int[][] getLayer01() {
		return layer01;
	}

	public int[][] getLayer02() {
		return layer02;
	}

	public int[][] getLayer03() {
		return layer03;
	}

	public void setLayer00(int[][] layer00) {
		this.layer00 = layer00;
	}

	public void setLayer01(int[][] layer01) {
		this.layer01 = layer01;
	}

	public int[][] getLayerMerged() {
		return layerMerged;
	}

	public void setLayer02(int[][] layer02) {
		this.layer02 = layer02;
	}

	public void setLayer03(int[][] layer03) {
		this.layer03 = layer03;
	}
}

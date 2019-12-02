package com.michael.pan.eviltower.utilities;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class AlgorithmUtil {


	public static boolean canReach(int[][] array, int xTouch, int yTouch, int xWarrior, int yWarrior) {
		int M = array.length, N = array[0].length;
		boolean[][] visited = new boolean[M][N];
		if (isReachable(yTouch, xTouch, array)){//if reachable, the destination is set from 0 to -2.
			return isPath(array, visited, xTouch, yTouch, xWarrior, yWarrior);}
		return false;
	}

	private static boolean isPath(int[][] matrix, boolean[][] visited, int xTouch, int yTouch, int xWarrior, int yWarrior){
		if(yWarrior == yTouch && xWarrior == xTouch){
			//already at the destination, return true and break;
			return true;
		}
		else if(isReachable(yWarrior, xWarrior, matrix) && !visited[yWarrior][xWarrior]){// exchange the coordinates at the lowest level.
			//check whether warrior can visit a position && he never visited; make the cell marked by -1
			visited[yWarrior][xWarrior] = true;
			return isPath(matrix, visited, xTouch, yTouch, xWarrior - 1, yWarrior) || isPath(matrix, visited, xTouch, yTouch, xWarrior, yWarrior - 1) ||
				isPath(matrix, visited, xTouch, yTouch,xWarrior + 1, yWarrior) || isPath(matrix, visited, xTouch, yTouch, xWarrior, yWarrior + 1);
		}
		return false; // no path has been found
	}

	private static boolean isReachable(int i, int j, int[][] matrix){
		return i >= 0 && i < matrix.length && j >= 0 && j < matrix[0].length && (matrix[i][j] == 0 || matrix[i][j] == 1);
	}

	// checking matrix boundaries
	private static boolean isValid(int i, int j, int[][] matrix){
		return i >= 0 && i < matrix.length && j >= 0 && j < matrix[0].length && (matrix[i][j] == 0 ||matrix[i][j] == 1);
	}

	public static ArrayList<Node> getPath(int[][] mat, int srcX, int srcY, int dstX, int dstY){
//		System.out.println("The src is :"+srcX+","+srcY+"The dst is "+dstX+","+dstY);
		int M = mat.length, N = mat[0].length;
		ArrayList<Node> nodes = new ArrayList<>();
		//System.out.println("M:"+M);
		// construct a matrix to keep track of visited cells
		boolean[][] visited = new boolean[M][N];
		// create an empty queue
		ArrayDeque<Node> q = new ArrayDeque<>();
		// mark source cell as visited and enqueue the source node
		visited[srcX][srcY] = true;
		q.add(new Node(srcX, srcY, 0));
		// stores length of longest path from source to destination
		// run till queue is not empty
		// Below arrays details all 4 possible movements from a cell
		final int[] row = {-1, 0, 0, 1};
		final int[] col = {0, -1, 1, 0};
		// Find Shortest Possible Route in a matrix mat from source
		// cell (i, j) to destination cell (x, y)
		while (!q.isEmpty()){
			// pop front node from queue and process it
			Node currentNode = q.poll();
			//System.out.println("node:X:"+currentNode.x+";Y:"+currentNode.y+"dist:"+currentNode.dist+"level:"+currentNode.getParentLevel());
			// (i, j) represents current cell and dist stores its
			// minimum distance from the source
			srcX = currentNode.x;
			srcY = currentNode.y;
			int dist = currentNode.dist;
			// if destination is found, updateStatesA min_dist and stop
			if (srcX == dstX && srcY == dstY){
				return Node.getPathNodes(nodes, currentNode);
			}
			// check for all 4 possible movements from current cell
			// and enqueue each valid movement
			for (int k = 0; k < 4; k++){
				// check if it is possible to go to position
				// (i + row[k], j + col[k]) from current position
				if (isValid(srcX + row[k], srcY + col[k], mat)&&!visited[srcX + row[k]][srcY + col[k]]){
					//reachable and then not visited
					//System.out.println((i + row[k])+","+(j + col[k]) +"is valid. the matrix value is "+mat[i + row[k]][j + col[k]]);
					// mark next cell as visited and enqueue it
					visited[srcX + row[k]][srcY + col[k]] = true;
					Node child = new Node(srcX + row[k], srcY + col[k], dist + 1);
					currentNode.addChildNode(child);
					nodes.add(currentNode);
					q.add(child);
				}
			}
		}
		return null;
	}

	public static String getDirection(int xTouch, int yTouch, float x, float y) {
		float dx = xTouch - x, dy = yTouch - y;
		if (dy<0 && Math.abs(dx)-Math.abs(dy)<=0){
			return "down";
		}else if (dy>0 && Math.abs(dx)-Math.abs(dy)<=0){
			return "up";
		}else if (dx>0 && Math.abs(dx)-Math.abs(dy)>=0){
			return "right";
		}else if (dx<0 && Math.abs(dx)-Math.abs(dy)>=0){
			return "left";
		}else {
			return "null";
		}
	}

}

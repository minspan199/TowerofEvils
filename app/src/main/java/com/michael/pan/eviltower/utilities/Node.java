package com.michael.pan.eviltower.utilities;

import java.util.ArrayList;
import java.util.List;

public class Node{
		// (x, y) represents matrix cell coordinates
		// dist represent its minimum distance from the source
		public int x;
		public int y;
		int dist;
		private List<Node> children;
		private Node parent;
		Node(int x, int y, int dist) {
			this.parent = null;
			this.x = x;
			this.y = y;
			this.dist = dist;
			this.children = new ArrayList<Node>();
		}

		public void remove() {
			if (parent != null) {
				parent.removeChild(this);
			}
		}

		private void removeChild(Node child) {
			children.remove(child);
		}

		void addChildNode(Node child) {
			child.parent = this;
			if (!children.contains(child))
				children.add(child);
		}

		int getParentLevel() {
			int level = 0;
			Node p = parent;
			while (p != null) {
				++level;
				p = p.parent;
			}
			return level;
		}

	 static ArrayList<Node> getPathNodes(ArrayList<Node> node, Node currentNode) {
			ArrayList<Node> path = new ArrayList<>();
			path.add(currentNode);
			Node iterative = currentNode;
			int layer = currentNode.dist;
//			System.out.println("total layer is"+layer);
			for (int ind = 0; ind < layer; ind++){
				if (iterative.parent != null){
					iterative = iterative.parent;
					path.add(iterative);
				}
			}
			return path;
		}
}

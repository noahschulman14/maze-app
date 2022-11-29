package edu.wm.cs301.amazebynoahschulman.generation;

import java.util.Arrays;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Collections;


public class MazeBuilderBoruvka extends MazeBuilder implements Runnable {
	
	
	
	
	private static final Logger LOGGER = Logger.getLogger(MazeBuilderBoruvka.class.getName());
	
	
	/**
	 * Three-dimensional parent array
	 * first two indexes are indexes of a cell in floormap
	 * last index is an array of length 2 which will store the index of the parent cell
	 */
	private int[][][] parent;
	

	/**
	 * shortcut for max integer value
	 */
	int infinity = Integer.MAX_VALUE;
		
	/**
	 * Three-dimensional array to store a cluster's smallest edge location
	 * last index is i,j, cardinalDirectionKey, edgeweight
	 */
	private int[][][] smallestEdge;
	
	/**
	 *  3-dimensional array to store edgeWeights
	 *  last index stores edge weights - has a length of 4, each index represents edgeweight direction key
	 */
	protected int[][][] edgeWeights;
	
	// EDGEWEIGHT KEYS:
	// 0 - north
	// 1 - east
	// 2 - south
	// 3 - west
	

	public MazeBuilderBoruvka() {
		
		// TODO Auto-generated constructor stub
		// LOOK AT MazeBuilderPrim FOR INSPIRATION
		super();
	}
	
	/**
	 * Method to generate an ArrayList of random unique numbers
	 * I will do this by first instantiating a new ArrayList
	 * Then I will add integers 1 through 7000 to it
	 * Then I will shuffle those numbers into a randomly ordered list using the Collections.shuffle() method
	 * @return ArrayList of random unique numbers
	 */
	private ArrayList<Integer> getRandomUniqueNumber() {
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int i = 1; i < 7000; i++) {
			numbers.add(i);
		}
		Collections.shuffle(numbers);
		return numbers;
	}
	
	/**
	 * Method to set the edgeweights of every cell
	 * There will also be a three dimensional array that stores each wallboard's corresponding edge weight.
	 * The first two indexes correspond to a cell's floorplan index.
	 * The last index has a length of 4 and each index corresponds to a cardinal direction:
	 * 0 - north, 1 - east, 2 - south, 3 - west
	 *
	 * The edgeweights will be randomly assigned.
	 * To do this I will iterate through the edgeWeight three dimensional array, going to each
	 * cell and each cell's cardinal direction.
	 * Note: each random number generated will be unique thanks to getRandomUniqueNumber() method.
	 * For each cell iterated to, I will set the northern edgeweight to a random number, and the cell above that's
	 * southern edgeweight to the same random number. (if that cell exists)
	 * I will also set the cell's eastern edgeweight to a random number, and the cell to to the
	 * right of that's western wallboard to the same random number. (if that cell exists)
	 * will also assign the border wallboard's edgeweight as infinity so that the algorithm never identifies a
	 * border wallboard as a smallest edge and attempts to delete that edge.
	 * 
	 * 
	 * Will then override the border edgeweights to infinity to that the border wallboards are never chosen as the wallboard
	 * with lowest edgeweight
	 * @return three dimensional edgeWeights array, now set with random edgeweights
	 */
	protected int[][][] setEdgeWeights() {
		int[][][] edgeWeights = new int[width][height][4];
		// first I will set each edgeweight to a unique random value
		ArrayList<Integer> random = getRandomUniqueNumber();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int tRandom1 = random.get(i + j);
				random.remove(i + j);
				// set northern edgeweight to a random number and cell above that's southern edgeweight to the same random number (if that cell exists)
				edgeWeights[i][j][2] = tRandom1;
				if (j + 1 < height)
					edgeWeights[i][j + 1][0] = tRandom1;
				// set eastern edgeweight to a different random number
				// set western edgeweight of adjacent cell (if that cell exists)
				int tRandom2 = random.get(i + j + 4);
				random.remove(i + j + 4);
					edgeWeights[i][j][1] = tRandom2;
				if (i + 1 < width)
					edgeWeights[i + 1][j][3] = tRandom2;
			}	
		}
	// Now I will OVERRIDE BORDER EDGEWEIGHTS to infinity so that the border wallboards are never chosen as the wallboard with lowest weight
	// I need to make the border wallboard edge weights infinity
		for (int i = 0; i < width; i++) { // northern border wall
			edgeWeights[i][0][0] = infinity;
		}
		for (int i = 0; i < height; i++) { // western border wall
			edgeWeights[0][i][3] = infinity;
		}
		for (int i = 0; i < height; i++) {
			edgeWeights[width - 1][i][1] = infinity; // eastern border wall
		}
		for (int i = 0; i < width; i++) { // southern border wall
			edgeWeights[i][height - 1][2] = infinity;
		}
		return edgeWeights;
	}
	
	/**
	 * Getter method to retrieve a wallboard's edgeweight a a specific index and cardinal direction
	 * @param x
	 * @param y
	 * @param cd
	 * @return intended edgeweight
	 */
	public int getEdgeWeight(int x, int y, CardinalDirection cd) {
		if (cd == CardinalDirection.North) 
			return edgeWeights[x][y][0];
		if (cd == CardinalDirection.East)
			return edgeWeights[x][y][1];
		if (cd == CardinalDirection.South)
			return edgeWeights[x][y][2];
		else
			return edgeWeights[x][y][3];
	}
	
	
	/**
	 * Initializes parent 3-d array
	 * Sets the parent of each cell as itself
	 * The way that I will keep track of components is by using a custom data structure similar to UnionFind/Disjoint Sets
	 * There is a three dimensional array called parent. The first two dimensions are each cells index.
	 * (So first dimension is width index, second dimension is height index) and the third dimension stores the index of the cell's parent cell.
	 * @return initialized parent array
	 */
	private int[][][] initParent() {
		int[][][] parent = new int[width][height][2];
		for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
				parent[i][j][0] = i;
				parent[i][j][1] = j;
				// TEST: maybe try printing values of particular cells
			}
		}
		return parent;
	}
	
	
	/**
	 * initializes/resets smallestEdge array
	 * smallest edge cell will be initialized to itself
	 * cardinal direction key is initialized to -1 because there is no corresponding key
	 * @return reset smallestEdge 3D array
	 */
	private int[][][] resetSmallestEdge() {
		
		int[][][] smallestEdge = new int[width][height][4];
		
		for (int i = 0; i < width; i++) {
			for (int j =0; j < height; j++) {
				smallestEdge[i][j][0] = i;
				smallestEdge[i][j][1] = j;
				smallestEdge[i][j][2] = -1;
				smallestEdge[i][j][3] = infinity;
			}
		}
		return smallestEdge;
	}
	
	// method to find smallest edge of a cluster
	/**
	 * Getter method to access the smallest edge of a cluster.
	 * So it accesses the smallestEdge array at a specific index - i, j
	 * index it is used for will always be that of a cluster's parentKing
	 * @param i
	 * @param j
	 * @return smallest edge for a parentKing's cluster
	 */
	private int[] findSmallestEdge(int i, int j) {
		return smallestEdge[i][j];
	}
	
	// Method to find the oldest grandparent (aka parent king aka the disjoint set's identifier)
	// this is a recursive function
	/**
	 * Method to find the oldest grandparent (aka parentKing aka the disjoint set's identifier)
	 * this is a recursive method
	 * 
	 * The way that I will keep track of components is by using a custom data structure similar to UnionFind/Disjoint Sets
	 *
	 * There is a three dimensional array called parent. The first two dimensions are each cells index.
	 * (So first dimension is width index, second dimension is height index) and the third dimension stores the index of the cell's parent cell.
	 *
	 * In order to determine what cluster a cell is in, you have to locate the cell's parentKing (akin to an oldest grandparent / grandest parent).
	 * To find a cell's parentKing, you first look at the cell's parent,
	 * if the cell's parent index is equal to the cell's index, then that cell is the parentKing cell of the cluster.
	 * if the cell's parent index is not equal to the cell's index, then do the same process with the cell's parent's 
	 * parent and keep doing this until one of the cell's grandparents parent is equal to that grandparent
	 * (so then the grandparent is the origininal cell's parentKing)
	 * Since this is recursive logic, this will be implemented in a recursive algorithm.
	 *
	 * Another way to show this logic:
	 *
	 * function parentKing(cellIndex):
	 *	 parentKing = cellIndex
	 *	 if cellIndex.parent != cellIndex:
	 *		 parentKing = findParentKing(cellIndex.parent)
	 *
	 *	 return parentKing
	 *
	 * Each cluster has a unique parentKing cell. The parentKing cell identifies the cluster.
	 * (This recursive logic is the same way that a UnionFind/Disjoint Set data structure identifies the parentKing/identifier of a set)
	 * 
	 * @param i - index x of a cell
	 * @param k - index y of a cell
	 * @return cell's parentKing
	 */
	private int[] findParentKing(int i, int k) {
		int[] selfIdentifier = new int[] {i, k};
		int [] curParent = new int[2];
		curParent[0] = parent[i][k][0];
		curParent[1] = parent[i][k][1];
		
		if (!Arrays.equals(curParent, selfIdentifier)) {
			curParent = findParentKing(curParent[0], curParent[1]);
		}
		return curParent;
	}
	
	/**
	 * Method to return a list of all the parentKings
	 * Will do this by creating an ArrayList of arrays to store the parentKing indices.
	 * I will then add the parentKing of cell 0,0
	 * Then I will iterate through the rest of the floorplan,
	 * I will find each cell's parentKing, and if that is not already in the ArrayList, it is added
	 * This results in a list of all the unique parentKing indices
	 * @return list of all the parentKings
	 */
	private ArrayList<int[]> ParentKingList() {
		ArrayList<int[]> parentKings = new ArrayList<int[]>();
		int[] parentKing = findParentKing(0, 0);
		parentKings.add(parentKing);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				for (int[] x : parentKings) {
					int counter0 = 0;
					if (Arrays.equals(findParentKing(i, j), x)) {
						counter0++;
					}
					if (counter0 == 0) {
						parentKings.add(findParentKing(i, j));
						break;
						}
					}
				}
			}
		return parentKings;
	}
	
	
	/**
	 * Method to generate pathways in a maze using Boruvka's algorithm
	 * The condition for Boruvka's algorithm to stop is when the number of clusters is equal to 1.
	 * So my entire algorithm will be under a while loop and the condition is the number of parentKings.
     * (Because the number of parentKings is the number of clusters)
     * 
     * 
     * Inside of the aforementioned while condition.
	 *	*start of while loop*
     * Iterate through every cell in the edgeWeights 3d array.
     * For each cell, identify the wallboard with the smallest edgeweight (also, the cell adjacent to that edgeweight's
     * cardinal direction has to have a different parentKing as the cell's parentKing (has to be in a different cluster),
     * otherwise don't consider that wall as a candidate for the smallest edgeweight of a cell).
     * If the smallest edgeweight of that cell is smaller than the cell's parentKing's smallest edgeweight, make
     * the cell's parentKing's smallest edgeweight the cell's smallest edgeweight
     * (which is then stored in the smallest edge three dimensional array (smallestEdge).
     * 
     * Then call the listOfParentKings function to get the list of parentKings at that point in time.
     * Then for each parentKing in that list:
     * Find what the parentKing's cluster's smallest edge is (index of it and cardinal direction) using the smallestEdge three dimensional array.
     * Then delete that corresponding wallboard from the floorplan.
     * Then set that edgeweight to infinity and the cell that it connects to's corresponding edgeweight to infinity also.
     * This is so the algorithm never chooses that edge a smallest edge again (because that edge is effectively gone).
     * Then make the cell adjacent to the cell that had the smallest edge (which is already in the cluster)'s 
     * parentKing's parent the parentKing of the cell that had the smallest edge.
     * ^^^ This is what effectively merges two clusters into one cluster (makes the parentKing of one cluster the parentKing of the other cluster)
     * Then I will reset the smallestEdge three dimensional array.
     * *end of while loop*
     * Following Boruvka's algorithm, there will at first be as many clusters as there are cells. But eventually the clusters
     *  will all merge together into a singular cluster -- when this happens my while loop condition is no longer met and I have a perfect maze.
	 */
	@Override
	protected void generatePathways() {
		// First, give each internal border wallboard a random edge weight, give the border wallboards an infinite edge weight
		edgeWeights = setEdgeWeights();
		// Need to make each each individual cell a disjoint set
		// Here is how my disjoint set will work:
		// Each set has a parent cell.
		// To union setA setB, update the parent of setA to the parent of setB
		// To initialize each cell as a disjiont set, initialize each cell's parent value to itself
		parent = initParent();
		// In Boruvka's algorithm, in each pass of the algorithm, the smallest edge is identified for each current disjoint set
		// and it's corresponding node is added into the disjoint set AFTER each smallest edge has been identified for the current disjoint sets.
		// In order not to update the disjoint sets while the smallest edge is being identified for the current disjoint sets, we need to store
		// the value of what the parent value of the smallest edge's corresponding node is going to be updated to after the current disjoint
		// sets' smallest edgeweight is identified. 
		// So we will initialize what each cell's parent will eventually be set to
		// We also need to store the smallest edge of a disjoint set:
		smallestEdge = resetSmallestEdge(); 
		// while number of disjoint sets > 1
		while (ParentKingList().size() > 1) {
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					// Find the wallboard with the lowest edgeweight for each cell (if the adjacent cell is in a different cluster)
					int tempDirection = 69; 
					int tempWeight = infinity;					
					if ((getEdgeWeight(i, j, CardinalDirection.North) < tempWeight) && !Arrays.equals(findParentKing(i,j), findParentKing(i, j - 1))) {
						tempDirection = 0;
						tempWeight = getEdgeWeight(i, j, CardinalDirection.North);
					}
					if ((getEdgeWeight(i, j, CardinalDirection.East) < tempWeight) && !Arrays.equals(findParentKing(i,j), findParentKing(i + 1, j))) {
						tempDirection = 1;
						tempWeight = getEdgeWeight(i, j, CardinalDirection.East);
					}
					if ((getEdgeWeight(i, j, CardinalDirection.South) < tempWeight) && !Arrays.equals(findParentKing(i,j), findParentKing(i, j + 1))) {
						tempDirection = 2;
						tempWeight = getEdgeWeight(i, j, CardinalDirection.South);
					}
					if ((getEdgeWeight(i, j, CardinalDirection.West) < tempWeight) && !Arrays.equals(findParentKing(i,j), findParentKing(i - 1, j))) {
						tempDirection = 3;
						tempWeight = getEdgeWeight(i, j, CardinalDirection.West);
					}
					
					// if that edgeweight is lower than the parentKing's lowest edgeweight, update the parentKing's lowest edgeweight
					int[] tParentKing = findParentKing(i, j);
					if (tempWeight <= findSmallestEdge(tParentKing[0], tParentKing[1])[3]) {
						smallestEdge[tParentKing[0]][tParentKing[1]][0] = i;
						smallestEdge[tParentKing[0]][tParentKing[1]][1] = j;
						smallestEdge[tParentKing[0]][tParentKing[1]][2] = tempDirection; 
						smallestEdge[tParentKing[0]][tParentKing[1]][3] = tempWeight;
					}	
				}			
			}
			// now need to identify each parentKing and delete its set's lowest edge
			// this entails setting the edgeweight on both sides to infinity
			// this also entails knocking down that wall
			// list of ParentKings:
			ArrayList<int[]> parentKings = ParentKingList();
			for (int i = 0; i < parentKings.size(); i++) {
				// need to initialize corresponding wallboard object
				// then knock down that wallboard
				// set wallboard and corresponding other side of wallboard's edge weights to infinity
				// this is so they are not chosen again
				// this is a way of showing that that wallboard has already been deleted
				int parentKingX = parentKings.get(i)[0];
				int parentKingY = parentKings.get(i)[1];
				int tX = smallestEdge[parentKingX][parentKingY][0];
				int tY = smallestEdge[parentKingX][parentKingY][1];
				int tDir = smallestEdge[parentKingX][parentKingY][2];
				// for this, make sure the smallestEdge of the cell being added is set to infinity
				// as well as the adjacent cell (NOT NECESSARILY THE PARENTKING)
				// make sure that the adjacent cell is being added as a tempParent (REMEMBER TO UPDATE ACTUAL PARENT LATER)
				if (tDir == 0) {
					int[] addedCellParentKing = findParentKing(tX, tY - 1); 
					Wallboard wallboard = new Wallboard(tX, tY, CardinalDirection.North);
					floorplan.deleteWallboard(wallboard);
					edgeWeights[tX][tY][tDir] = infinity;
					edgeWeights[tX][tY - 1][2] = infinity;
					// MAKE THE ADJACENT CELL'S PK'S PARENT THE ADDED CELLS PARENT KING
					parent[parentKingX][parentKingY][0] = addedCellParentKing[0];
					parent[parentKingX][parentKingY][1] = addedCellParentKing[1];
				}
				if (tDir == 1) {
					Wallboard wallboard = new Wallboard(tX, tY, CardinalDirection.East);
					floorplan.deleteWallboard(wallboard);	
					edgeWeights[tX][tY][tDir] = infinity;
					edgeWeights[tX + 1][tY][3] = infinity;
					int[] addedCellParentKing = findParentKing(tX + 1, tY);
					parent[parentKingX][parentKingY][0] = addedCellParentKing[0];
					parent[parentKingX][parentKingY][1] = addedCellParentKing[1];
				}
				if (tDir == 2) {
					Wallboard wallboard = new Wallboard(tX, tY, CardinalDirection.South);
					floorplan.deleteWallboard(wallboard);
					edgeWeights[tX][tY][tDir] = infinity;
					edgeWeights[tX][tY + 1][0] = infinity;
					int[] addedCellParentKing = findParentKing(tX, tY + 1);
					parent[parentKingX][parentKingY][0] = addedCellParentKing[0];
					parent[parentKingX][parentKingY][1] = addedCellParentKing[1];
				}
				if (tDir == 3){
					Wallboard wallboard = new Wallboard(tX, tY, CardinalDirection.West);
					floorplan.deleteWallboard(wallboard);
					edgeWeights[tX][tY][tDir] = infinity;
					edgeWeights[tX - 1][tY][1] = infinity;
					int[] addedCellParentKing = findParentKing(tX - 1, tY);
					parent[parentKingX][parentKingY][0] = addedCellParentKing[0];
					parent[parentKingX][parentKingY][1] = addedCellParentKing[1];
				}
				
			}
		smallestEdge = resetSmallestEdge();
		}	
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

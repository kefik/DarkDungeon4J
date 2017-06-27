package cz.dd4j.generator.dungeon.corridors.maze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import cz.dd4j.utils.Const;
import cz.dd4j.utils.collection.Tuple2;

/**
 * Courtesy of: nazar_art
 * https://stackoverflow.com/questions/21815839/simple-java-2d-array-maze-sample
 * 
 * An extended version of the code above.
 */
public class Maze {
	private int dimensionX, dimensionY; // dimension of maze
	private int gridDimensionX, gridDimensionY; // dimension of output grid
	private char[][] grid; // output grid
	private Cell[][] cells; // 2d array of Cells
	//private Random random = new Random(); // The random object
	public static Random random = new Random(1);

	// initialize with x and y the same
	public Maze(int aDimension) {
		// Initialize
		this(aDimension, aDimension);
	}

	// constructor
	public Maze(int xDimension, int yDimension) {
		dimensionX = xDimension;
		dimensionY = yDimension;
		gridDimensionX = xDimension * 4 + 1;
		gridDimensionY = yDimension * 2 + 1;
		grid = new char[gridDimensionX][gridDimensionY];
		init();
		generateMaze();
		updateGrid();
	}

	private void init() {
		// create cells
		cells = new Cell[dimensionX][dimensionY];
		for (int x = 0; x < dimensionX; x++) {
			for (int y = 0; y < dimensionY; y++) {
				cells[x][y] = new Cell(x, y, false); // create cell (see Cell
														// constructor)
			}
		}
	}

	// inner class to represent a cell
	private class Cell {
		int x, y; // coordinates
		// cells this cell is connected to
		ArrayList<Cell> neighbors = new ArrayList<Cell>();
		// solver: if already used
		boolean visited = false;
		// solver: the Cell before this one in the path
		Cell parent = null;
		// solver: if used in last attempt to solve path
		boolean inPath = false;
		// solver: distance travelled this far
		double travelled;
		// solver: projected distance to end
		double projectedDist;
		// impassable cell
		boolean wall = true;
		// if true, has yet to be used in generation
		boolean open = true;

		// construct Cell at x, y
		Cell(int x, int y) {
			this(x, y, true);
		}

		// construct Cell at x, y and with whether it isWall
		Cell(int x, int y, boolean isWall) {
			this.x = x;
			this.y = y;
			this.wall = isWall;
		}

		// add a neighbor to this cell, and this cell as a neighbor to the other
		void addNeighbor(Cell other) {
			if (!this.neighbors.contains(other)) { // avoid duplicates
				this.neighbors.add(other);
			}
			if (!other.neighbors.contains(this)) { // avoid duplicates
				other.neighbors.add(this);
			}
		}

		// used in updateGrid()
		boolean isCellBelowNeighbor() {
			return this.neighbors.contains(new Cell(this.x, this.y + 1));
		}

		// used in updateGrid()
		boolean isCellRightNeighbor() {
			return this.neighbors.contains(new Cell(this.x + 1, this.y));
		}

		// useful Cell representation
		@Override
		public String toString() {
			return String.format("Cell(%s, %s, %s)", x, y, wall ? "'X'" : "' '");
		}

		// useful Cell equivalence
		@Override
		public boolean equals(Object other) {
			if (!(other instanceof Cell))
				return false;
			Cell otherCell = (Cell) other;
			return (this.x == otherCell.x && this.y == otherCell.y);
		}

		// should be overridden with equals
		@Override
		public int hashCode() {
			// random hash code method designed to be usually unique
			return this.x + this.y * 256;
		}
	}

	// generate from upper left (In computing the y increases down often)
	private void generateMaze() {
		generateMaze(0, 0);
	}

	// generate the maze from coordinates x, y
	private void generateMaze(int x, int y) {
		generateMaze(getCell(x, y)); // generate from Cell
	}

	private void generateMaze(Cell startAt) {
		// don't generate from cell not there
		if (startAt == null)
			return;
		startAt.open = false; // indicate cell closed for generation
		ArrayList<Cell> cells = new ArrayList<Cell>();
		cells.add(startAt);

		while (!cells.isEmpty()) {
			Cell cell;
			// this is to reduce but not completely eliminate the number
			// of long twisting halls with short easy to detect branches
			// which results in easy mazes
			if (random.nextInt(10) == 0)
				cell = cells.remove(random.nextInt(cells.size()));
			else
				cell = cells.remove(cells.size() - 1);
			// for collection
			ArrayList<Cell> neighbors = new ArrayList<Cell>();
			// cells that could potentially be neighbors
			Cell[] potentialNeighbors = new Cell[] { getCell(cell.x + 1, cell.y), getCell(cell.x, cell.y + 1),
					getCell(cell.x - 1, cell.y), getCell(cell.x, cell.y - 1) };
			for (Cell other : potentialNeighbors) {
				// skip if outside, is a wall or is not opened
				if (other == null || other.wall || !other.open)
					continue;
				neighbors.add(other);
			}
			if (neighbors.isEmpty())
				continue;
			// get random cell
			Cell selected = neighbors.get(random.nextInt(neighbors.size()));
			// add as neighbor
			selected.open = false; // indicate cell closed for generation
			cell.addNeighbor(selected);
			cells.add(cell);
			cells.add(selected);
		}
	}

	// used to get a Cell at x, y; returns null out of bounds
	public Cell getCell(int x, int y) {
		try {
			return cells[x][y];
		} catch (ArrayIndexOutOfBoundsException e) { // catch out of bounds
			return null;
		}
	}

	public void solve() {
		// default solve top left to bottom right
		this.solve(0, 0, dimensionX - 1, dimensionY - 1);
	}

	// solve the maze starting from the start state (A-star algorithm)
	public void solve(int startX, int startY, int endX, int endY) {
		// re initialize cells for path finding
		for (Cell[] cellrow : this.cells) {
			for (Cell cell : cellrow) {
				cell.parent = null;
				cell.visited = false;
				cell.inPath = false;
				cell.travelled = 0;
				cell.projectedDist = -1;
			}
		}
		// cells still being considered
		ArrayList<Cell> openCells = new ArrayList<Cell>();
		// cell being considered
		Cell endCell = getCell(endX, endY);
		if (endCell == null)
			return; // quit if end out of bounds
		{ // anonymous block to delete start, because not used later
			Cell start = getCell(startX, startY);
			if (start == null)
				return; // quit if start out of bounds
			start.projectedDist = getProjectedDistance(start, 0, endCell);
			start.visited = true;
			openCells.add(start);
		}
		boolean solving = true;
		while (solving) {
			if (openCells.isEmpty())
				return; // quit, no path
			// sort openCells according to least projected distance
			Collections.sort(openCells, new Comparator<Cell>() {
				@Override
				public int compare(Cell cell1, Cell cell2) {
					double diff = cell1.projectedDist - cell2.projectedDist;
					if (diff > 0)
						return 1;
					else if (diff < 0)
						return -1;
					else
						return 0;
				}
			});
			Cell current = openCells.remove(0); // pop cell least projectedDist
			if (current == endCell)
				break; // at end
			for (Cell neighbor : current.neighbors) {
				double projDist = getProjectedDistance(neighbor, current.travelled + 1, endCell);
				if (!neighbor.visited || // not visited yet
						projDist < neighbor.projectedDist) { // better path
					neighbor.parent = current;
					neighbor.visited = true;
					neighbor.projectedDist = projDist;
					neighbor.travelled = current.travelled + 1;
					if (!openCells.contains(neighbor))
						openCells.add(neighbor);
				}
			}
		}
		// create path from end to beginning
		Cell backtracking = endCell;
		backtracking.inPath = true;
		while (backtracking.parent != null) {
			backtracking = backtracking.parent;
			backtracking.inPath = true;
		}
	}

	// get the projected distance
	// (A star algorithm consistent)
	public double getProjectedDistance(Cell current, double travelled, Cell end) {
		return travelled + Math.abs(current.x - end.x) + Math.abs(current.y - current.x);
	}

	// draw the maze
	public void updateGrid() {
		char backChar = ' ', wallChar = 'X', cellChar = ' ', pathChar = '*';
		// fill background
		for (int x = 0; x < gridDimensionX; x++) {
			for (int y = 0; y < gridDimensionY; y++) {
				grid[x][y] = backChar;
			}
		}
		// build walls
		for (int x = 0; x < gridDimensionX; x++) {
			for (int y = 0; y < gridDimensionY; y++) {
				if (x % 4 == 0 || y % 2 == 0)
					grid[x][y] = wallChar;
			}
		}
		// make meaningful representation
		for (int x = 0; x < dimensionX; x++) {
			for (int y = 0; y < dimensionY; y++) {
				Cell current = getCell(x, y);
				int gridX = x * 4 + 2, gridY = y * 2 + 1;
				if (current.inPath) {
					grid[gridX][gridY] = pathChar;
					if (current.isCellBelowNeighbor())
						if (getCell(x, y + 1).inPath) {
							grid[gridX][gridY + 1] = pathChar;
							grid[gridX + 1][gridY + 1] = backChar;
							grid[gridX - 1][gridY + 1] = backChar;
						} else {
							grid[gridX][gridY + 1] = cellChar;
							grid[gridX + 1][gridY + 1] = backChar;
							grid[gridX - 1][gridY + 1] = backChar;
						}
					if (current.isCellRightNeighbor())
						if (getCell(x + 1, y).inPath) {
							grid[gridX + 2][gridY] = pathChar;
							grid[gridX + 1][gridY] = pathChar;
							grid[gridX + 3][gridY] = pathChar;
						} else {
							grid[gridX + 2][gridY] = cellChar;
							grid[gridX + 1][gridY] = cellChar;
							grid[gridX + 3][gridY] = cellChar;
						}
				} else {
					grid[gridX][gridY] = cellChar;
					if (current.isCellBelowNeighbor()) {
						grid[gridX][gridY + 1] = cellChar;
						grid[gridX + 1][gridY + 1] = backChar;
						grid[gridX - 1][gridY + 1] = backChar;
					}
					if (current.isCellRightNeighbor()) {
						grid[gridX + 2][gridY] = cellChar;
						grid[gridX + 1][gridY] = cellChar;
						grid[gridX + 3][gridY] = cellChar;
					}
				}
			}
		}
	}

	// simply prints the map
	public void draw() {
		System.out.println(this);
	}
	
	public String getDescriptionCompact() {
		String output = "";
		for (int y = 0; y < gridDimensionY; y++) {
			for (int x = 0; x < gridDimensionX; x+=2) {
				if ((x/2) % 2 == 1 && y % 2 == 1) {
					//output += "R";
					output += " ";
				} else {
					output += grid[x][y];
				}
			}
			output += Const.NEW_LINE;
		}
		return output;
	}
	
	public String getDescriptionCompactWithRooms() {
		String output = "";
		for (int y = 0; y < gridDimensionY; y++) {
			for (int x = 0; x < gridDimensionX; x+=2) {
				if ((x/2) % 2 == 1 && y % 2 == 1) {
					output += ".";
				} else {
					output += grid[x][y];
				}
			}
			output += Const.NEW_LINE;
		}
		return output;
	}
	
	public String getDescriptionCompactWithRoomsV2() {
		String output = "";
		for (int y = 0; y < gridDimensionY; y++) {
			for (int x = 0; x < gridDimensionX; x+=2) {
				if ((x/2) % 2 == 1 && y % 2 == 1) {
					output += "R";
				} else {
					if (grid[x][y] == ' ') {
						if (y % 2 == 0) {
							output += "|";
						} else
						if ((x / 2) % 2 == 0) {
							output += "-";
						} else {
							output += " ";
						}
					} else {
						output += " ";
					}
					//output += grid[x][y];
				}
			}
			output += Const.NEW_LINE;
		}
		return output;
	}
	
	
	public String getDescriptionCompact2() {
		String output = "";
				
		for (int cellY = 0; cellY < dimensionY; cellY++) {
			// ODD-ROW
			output += "X";
			for (int cellX = 0; cellX < dimensionX; cellX++) {
				if (isWallBetween(cellX, cellY, cellX, cellY-1)) output += "X";
				else output += " ";
				output += "X";
			}
			output += Const.NEW_LINE;
			
			// EVEN-ROW
			output += "X";
			for (int cellX = 0; cellX < dimensionX; cellX++) {
				if (isWall(cellX, cellY)) output += "X";
				else output += " ";
				if (isWallBetween(cellX, cellY, cellX+1, cellY)) output += "X";
				else output += " ";
			}			
			output += Const.NEW_LINE;
		}
		
		// LAST ROW
		output += "X";
		for (int cellX = 0; cellX < dimensionX; cellX++) {
			if (isWallBetween(cellX, dimensionY, cellX, dimensionY+1)) output += "X";
			else output += " ";
			output += "X";
		}
		output += Const.NEW_LINE;
		
		return output;
	}
	
	public String getDescriptionCompact2WithJunctions() {
		String output = "";
				
		for (int cellY = 0; cellY < dimensionY; cellY++) {
			// ODD-ROW
			output += "X";
			for (int cellX = 0; cellX < dimensionX; cellX++) {
				if (isWallBetween(cellX, cellY, cellX, cellY-1)) {
					if (canPlaceExtraJunctionDownOf(cellX, cellY-1)) {
						output += "J";
					} else {
						output += "X";
					}
				}
				else output += " ";
				output += "X";
			}
			output += Const.NEW_LINE;
			
			// EVEN-ROW
			output += "X";
			for (int cellX = 0; cellX < dimensionX; cellX++) {
				if (isWall(cellX, cellY)) output += "X";
				else output += " ";
				if (isWallBetween(cellX, cellY, cellX+1, cellY)) {
					if (canPlaceExtraJunctionRightOf(cellX, cellY)) {
						output += "J";
					} else {
						output += "X";
					}
				}
				else output += " ";
			}			
			output += Const.NEW_LINE;
		}
		
		// LAST ROW
		output += "X";
		for (int cellX = 0; cellX < dimensionX; cellX++) {
			if (isWallBetween(cellX, dimensionY, cellX, dimensionY+1)) output += "X";
			else output += " ";
			output += "X";
		}
		output += Const.NEW_LINE;
		
		return output;
	}
	
	/**
	 * @param cellX zero-based (not expanded grid, but standard cell)
	 * @param cellY zero-based (not expanded grid, but standard cell)
	 * @return
	 */
	public boolean isWall(int cellX, int cellY) {
		if (cellX < 0 || cellX >= dimensionX) return true;
		if (cellY < 0 || cellY >= dimensionY) return true;
		Cell cell = getCell(cellX, cellY);
		return cell.wall;
	}
	
	/**
	 * @param cellX zero-based (not expanded grid, but standard cell)
	 * @param cellY zero-based (not expanded grid, but standard cell)
	 * @return
	 */
	public boolean isRoom(int cellX, int cellY) {
		return !isWall(cellX, cellY);
	}
	
	/**
	 * Checks whether there is a wall between two cells; non-sphere, rect only
	 * @param cellX1
	 * @param cellY1
	 * @param cellX2
	 * @param cellY2
	 * @return
	 */
	public boolean isWallBetween(int cellX1, int cellY1, int cellX2, int cellY2) {
		int gridX1 = 2 + cellX1 * 4;
		int gridY1 = 1 + cellY1 * 2;
		int gridX2 = 2 + cellX2 * 4;
		int gridY2 = 1 + cellY2 * 2;
		
		int checkX = (gridX1 + gridX2) / 2;
		int checkY = (gridY1 + gridY2) / 2;
		
		if (checkX < 0 || checkX >= gridDimensionX) return true;
		if (checkY < 0 || checkY >= gridDimensionY) return true;
		
		return grid[checkX][checkY] == 'X';
	}
	
	public void drawCompact() {		
		System.out.println(getDescriptionCompact());
	}
	
	public void drawCompactV2() {		
		System.out.println(getDescriptionCompact2());
	}
	
	public void drawCompactV2WithJunctions() {		
		System.out.println(getDescriptionCompact2WithJunctions());
	}
	
	public boolean canPlaceExtraJunctionRightOf(int cellX, int cellY) {
		if (isRoom(cellX, cellY) && isRoom(cellX+1, cellY) && isWallBetween(cellX, cellY, cellX+1, cellY)) {
			if (isWallBetween(cellX, cellY-1, cellX+1, cellY-1) && isWallBetween(cellX, cellY+1, cellX+1, cellY+1)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean canPlaceExtraJunctionDownOf(int cellX, int cellY) {
		if (isRoom(cellX, cellY) && isRoom(cellX, cellY+1) && isWallBetween(cellX, cellY, cellX, cellY+1)) {
			if (isWallBetween(cellX-1, cellY, cellX-1, cellY+1) && isWallBetween(cellX+1, cellY, cellX+1, cellY+1)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Upper-bound estimation.
	 * @return
	 */
	public int getMaxExtraJunctions() {
		int possibilities = 0;
		for (int cellX = 0; cellX < dimensionX; ++cellX) {
			for (int cellY = 0; cellY < dimensionY; ++cellY) {
				if (canPlaceExtraJunctionRightOf(cellX, cellY)) ++possibilities;
				if (canPlaceExtraJunctionDownOf(cellX, cellY)) ++possibilities;
			}
		}
		return possibilities;
	}
	
	public boolean placeExtraJunction() {
		List<Tuple2<Integer, Integer>> rightOptions = new ArrayList<Tuple2<Integer, Integer>>();
		List<Tuple2<Integer, Integer>> downOptions = new ArrayList<Tuple2<Integer, Integer>>();
		for (int cellX = 0; cellX < dimensionX; ++cellX) {
			for (int cellY = 0; cellY < dimensionY; ++cellY) {
				if (canPlaceExtraJunctionRightOf(cellX, cellY)) {
					rightOptions.add(new Tuple2<Integer, Integer>(cellX, cellY));
				}
				if (canPlaceExtraJunctionDownOf(cellX, cellY)) {
					downOptions.add(new Tuple2<Integer, Integer>(cellX, cellY));
				}
			}
		}
		
		if (rightOptions.size() + downOptions.size() == 0) return false;
		int next = random.nextInt(rightOptions.size() + downOptions.size());
		if (next < rightOptions.size()) {
			Tuple2<Integer, Integer> option = rightOptions.get(next);
			
			int cellX = option.a;
			int cellY = option.b;
			
			int gridX1 = 2 + cellX     * 4;
			int gridY1 = 1 + cellY     * 2;
			int gridX2 = 2 + (cellX+1) * 4;
			int gridY2 = 1 + cellY     * 2;
			
			grid[(gridX1 + gridX2) / 2][(gridY1 + gridY2) / 2] = ' ';
			
		} else {
			next = next - rightOptions.size();
			Tuple2<Integer, Integer> option = downOptions.get(next);
			
			int cellX = option.a;
			int cellY = option.b;
			
			int gridX1 = 2 + cellX     * 4;
			int gridY1 = 1 + cellY     * 2;
			int gridX2 = 2 + cellX     * 4;
			int gridY2 = 1 + (cellY+1) * 2;
			
			grid[(gridX1 + gridX2) / 2][(gridY1 + gridY2) / 2] = ' ';
		}
		
		return true;
		
	}

	// forms a meaningful representation
	@Override
	public String toString() {
		String output = "";
		for (int y = 0; y < gridDimensionY; y++) {
			for (int x = 0; x < gridDimensionX; x++) {
				output += grid[x][y];
			}
			output += Const.NEW_LINE;
		}
		return output;
	}

	// run it
	public static void main(String[] args) {
		Maze maze = new Maze(6, 6);
		maze.solve();
		maze.updateGrid();
		maze.draw();
		maze.drawCompact();
		maze.drawCompactV2();
		maze.drawCompactV2WithJunctions();
		System.out.println("Max junctions = " + maze.getMaxExtraJunctions());
		
		
		System.out.println("EXTRA JUNCTION");
		maze.placeExtraJunction();
		maze.drawCompactV2WithJunctions();
		System.out.println("EXTRA JUNCTION");
		maze.placeExtraJunction();
		maze.drawCompactV2WithJunctions();
		
		
		//maze.drawCompactRooms();
	}
}
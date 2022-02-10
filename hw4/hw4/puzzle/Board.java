package hw4.puzzle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Board implements WorldState {
	private int[][] tiles;
	private int size;
	public Board(int[][] tiles) {
		int x = tiles.length;
		int y = tiles[0].length;
		this.tiles = new int[x][y];
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				this.tiles[i][j] = tiles[i][j];
			}
		}
		size = tiles.length;
	}
	
	public int tileAt(int i, int j) {
		 if (!isInBound(i, j)) {
		 	throw new IndexOutOfBoundsException("col and row should be between 0 to N - 1!");
		 }
		return tiles[i][j];
	}
	
	private boolean isInBound(int i, int j) {
		return !(i >= size || j >= size || i < 0 || j < 0);
		
	}
	
	public int size() {
		return size;
	}
	
	@Override
	public Iterable<WorldState> neighbors() {
		int[][] test = new int[size][size];
		int posX0 = 0, posY0 = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				test[i][j] = tiles[i][j];
				if (test[i][j] == 0) {
					posX0 = i;
					posY0 = j;
				}
			}
		}
		Set<WorldState> neighbs = new HashSet<>();
		if (isInBound(posX0, posY0 + 1)) {
			neighbs.add(createNeighbors(test, posX0, posY0, 0));
		}
		if (isInBound(posX0, posY0 - 1)) {
			neighbs.add(createNeighbors(test, posX0, posY0, 1));
		}
		if (isInBound(posX0 + 1, posY0)) {
			neighbs.add(createNeighbors(test, posX0, posY0, 2));
		}
		if (isInBound(posX0 - 1, posY0)) {
			neighbs.add(createNeighbors(test, posX0, posY0, 3));
		}
		return neighbs;
	}
	
	private WorldState createNeighbors(int[][] test, int posX, int posY, int direction) {
		int[][] test1 = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				test1[i][j] = test[i][j];
			}
		}
		Board board = new Board(test1);
		switch (direction) {
			case 0: {
				swap(test1, posX, posY, posX, posY + 1);
				board = new Board(test1);
				// swap(test, posX, posY, posX, posY + 1);
				break;
			}
			case 1: {
				swap(test1, posX, posY, posX, posY - 1);
				board = new Board(test1);
				// swap(test, posX, posY, posX, posY - 1);
				break;
			}
			case 2: {
				swap(test1, posX, posY, posX + 1, posY);
				board = new Board(test1);
				// swap(test, posX, posY, posX + 1, posY);
				break;
			}
			case 3: {
				swap(test1, posX, posY, posX - 1, posY);
				board = new Board(test1);
				// swap(test, posX, posY, posX - 1, posY);
				break;
			}
		}
		return board;
	}
	
	private void swap(int[][] test, int posx1, int posy1, int posx2, int posy2) {
		int tmp = test[posx2][posy2];
		test[posx2][posy2] = test[posx1][posy1];
		test[posx1][posy1] = tmp;
	}
	
	public int hamming() {
		int hamming = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (tiles[i][j] == 0) {
					continue;
				}
				if (isInTheRightPosition(i, j, tiles[i][j])) {
					continue;
				}
				hamming++;
			}
		}
		return hamming;
	}
	
	private boolean isInTheRightPosition(int i, int j, int tileNum) {
		if (tileNum == j + size * i + 1) {
			return true;
		}
		return false;
	}
	
	public int manhattan() {
		int manhattan = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (tiles[i][j] == 0) {
					continue;
				}
				if (isInTheRightPosition(i, j, tiles[i][j])) {
					continue;
				}
				manhattan += manhattanDistance(i, j, tiles[i][j]);
			}
		}
		return manhattan;
	}
	
	private int manhattanDistance(int i, int j, int tileNum) {
		int x = (tileNum - 1) / size;
		int y = (tileNum - 1) % size;
		return Math.abs(x - i) + Math.abs(y - j);
	}
	
	@Override
	public int estimatedDistanceToGoal() {
		return manhattan();
	}
	
	@Override
	public boolean equals(Object y) {
		Board yBoard = (Board) y;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (tiles[i][j] == yBoard.tiles[i][j]) {
					continue;
				}
				else {
					return false;
				}
			}
		}
		return true;
	}
	
    /** Returns the string representation of the board. 
      * Uncomment this method. */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }
}

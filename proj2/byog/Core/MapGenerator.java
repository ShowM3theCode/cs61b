package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.Core.Game;
import org.junit.Test;

import java.io.Serializable;
import java.util.*;

public class MapGenerator implements Serializable {
	
	private static int HEIGHT;
	private static int WIDTH;
	
	private static int[][] Rooms;
	
	public Pos player = new Pos();
	
	public Pos door = new Pos();
	
	private static TETile[][] worldGenerator;
	
    private static Random RANDOM;
	
	public MapGenerator (int w, int h, long seed, TETile[][] world) {
		WIDTH = w;
		HEIGHT = h;
		Rooms = new int[WIDTH][HEIGHT];
		RANDOM = new Random(seed);
		worldGenerator = world;
		
		mazeGenerator mg1 = new mazeGenerator();
		mg1.mazeInitialization();
		
		
		roomGenerator rg1 = new roomGenerator();
		rg1.fillWithRoom();
		
		mg1.mazeGrow();
		
		rg1.connectRoomWithHallWay();
		
		removeDeadends();
		
		destroyWall();
		
		GenerateDoor();
		
		GeneratePlayer();
	}
	
	public class Pos {
		public int x;
		public int y;
		public Pos(int x, int y) {
			this.x = x;
			this.y = y;
		}
		public Pos() {
			x = 0;
			y = 0;
		}
	}
	
	private class mazeGenerator {
		
		private void mazeInitialization() {
			int checkY = 0;
			boolean isEven = true;
			while (checkY < HEIGHT) {
				int checkX = 0;
				if (isEven) {
					while (checkX < WIDTH) {
						worldGenerator[checkX++][checkY] = Tileset.WALL;
					}
					isEven = !isEven;
				} else {
					while (checkX + 1 < WIDTH) {
						worldGenerator[checkX++][checkY] = Tileset.WALL;
						worldGenerator[checkX][checkY] = Tileset.FLOOR;
						// allList.add(new Pos(checkX, checkY));
						checkX++;
					}
					if (checkX == WIDTH - 1) {
						worldGenerator[checkX][checkY] = Tileset.WALL;
					}
					isEven = !isEven;
				}
				checkY++;
			}
		}
		
		private ArrayList<Pos> spotList = new ArrayList<Pos>();   // Restore the spot that has been set a wall.
		private ArrayList<Pos> candidateList = new ArrayList<Pos>();  // Restore the spot that can be set a wall.
		// private ArrayList<Pos> allList = new ArrayList<Pos>();
		
		private void mazeGrow() {
			// 1. Randomly select one empty spot.
			// 2. Randomly select one Pos, if the new spot that connects with the Pos is not viewed before,
			// then break the wall between the two spot, else, choose a new Pos.
			// condition to return: every floor has been viewed.
			Pos startPoint = selectPointRandomly();    // 1.Randomly select one empty spot.
			
			int check = 0;
			while (true) {
				if (!candidateList.isEmpty()) {
					startPoint = selectPointRandomly1();
					check++;
				}
				if (check != 0 && candidateList.isEmpty()) {
					return;
				}
				// removeList(allList, startPoint);
				spotList.add(startPoint);
				addCandidateList(startPoint);
				
				do { // If all the spots have been set a wall, then the candidatelist shall be empty.
					Pos endSpot = selectSpot(startPoint);  // Select a new spot, remove it from candidate list to the spot list.
					
					if (endSpot == null) {
						break;
					}
					
					addCandidateList(endSpot);
					
					breakTheWall(endSpot);    // Break the wall.
					
					startPoint = endSpot;   // Loop.
				
				} while (!candidateList.isEmpty());
			}
		}
		
		
		private Pos selectPointRandomly1() {
			int rand = RANDOM.nextInt(candidateList.size());
			return candidateList.get(rand);
		}
		
		private Pos selectPointRandomly() {
			int x;
			int y;
			while (true) {
				x = RANDOM.nextInt(WIDTH - 2);
				y = RANDOM.nextInt(HEIGHT - 2);
				if (worldGenerator[x][y] == Tileset.FLOOR) {
					return new Pos(x, y);
				}
			}
		}
		
		
		private void addCandidateList(Pos ptr) {
			Pos tmp0 = new Pos(ptr.x + 2, ptr.y);
			if (!isContain(candidateList, tmp0) && !isContain(spotList, tmp0) &&isNotOut(tmp0)) {
				candidateList.add(tmp0);
				// worldGenerator[tmp0.x][tmp0.y] = Tileset.FLOWER;
			}
			Pos tmp1 = new Pos(tmp0.x - 4, tmp0.y);
			if (!isContain(candidateList, tmp1) && !isContain(spotList, tmp1) && isNotOut(tmp1)) {
				candidateList.add(tmp1);
				// worldGenerator[tmp1.x][tmp1.y] = Tileset.FLOWER;
			}
			Pos tmp2 = new Pos(tmp1.x + 2, tmp1.y + 2);
			if (!isContain(candidateList, tmp2) && !isContain(spotList, tmp2) && isNotOut(tmp2)) {
				candidateList.add(tmp2);
				// worldGenerator[tmp2.x][tmp2.y] = Tileset.FLOWER;
			}
			Pos tmp3 = new Pos(tmp2.x, tmp2.y - 4);
			if (!isContain(candidateList, tmp3) && !isContain(spotList, tmp3) && isNotOut(tmp3)) {
				candidateList.add(tmp3);
				// worldGenerator[tmp3.x][tmp3.y] = Tileset.FLOWER;
			}
		}
		
		private boolean isContain(ArrayList<Pos> list, Pos p) {
			if (list.isEmpty()) {
				return false;
			}
			for (Pos tmp: list) {
				if (tmp.x == p.x && tmp.y == p.y)
					return true;
			}
			return false;
		}
		
		private boolean isNotOut(Pos p) {
			if(p.x > WIDTH - 2 || p.x < 1 || p.y > HEIGHT - 2 || p.y < 1) {
				return false;
			}
			return true;
		}
		
		private Pos selectSpot(Pos ptr) {
			Pos ans;
			int randomNum;
			int x = ptr.x;
			int y = ptr.y;
			Pos[] direction = new Pos[4];
			direction[0] = new Pos(x + 2, y);
			direction[1] = new Pos(x, y + 2);
			direction[2] = new Pos(x - 2, y);
			direction[3] = new Pos(x, y - 2);
			if (!isNextPointToConnect(direction)) {
				breakTheWall(ptr);
				return null;
			}
			while (true) {
				randomNum = RANDOM.nextInt(4);
					if (isContain(candidateList, direction[randomNum])) {
						ans = direction[randomNum];
						removeList(candidateList, direction[randomNum]);
						// removeList(allList, direction[randomNum]);
						spotList.add(direction[randomNum]);
						return ans;
					}
			}
		}
		
		private boolean isNextPointToConnect(Pos[] ptr) {
			for (Pos k: ptr) {
				if (!isNotOut(k)) {
					continue;
				}
				if (isContain(candidateList, k)) {
					return true;
				}
			}
			return false;
		}
		
		private void removeList(ArrayList<Pos> cl, Pos p) {
			for (Pos tmp: cl) {
				if (tmp.x == p.x && tmp.y == p.y) {
					cl.remove(tmp);
					return;
				}
			}
		}
		
		private void breakTheWall(Pos p1) {
			if (spotList.size() <= 1) {
				return;
			}
			Pos ans;
			int randomNum;
			int x = p1.x;
			int y = p1.y;
			Pos[] direction = new Pos[4];
			direction[0] = new Pos(x + 2, y);
			direction[1] = new Pos(x, y + 2);
			direction[2] = new Pos(x - 2, y);
			direction[3] = new Pos(x, y - 2);
			while (true) {
				randomNum = RANDOM.nextInt(4);
				if (isContain(spotList, direction[randomNum])) {
					ans = direction[randomNum];
					break;
				}
			}
			breakTheWallBetweenTwoSpot(p1, ans);
			removeList(candidateList, p1);
		}
		
		private void breakTheWallBetweenTwoSpot(Pos p1, Pos p2) {
			if (p1.x == p2.x && p1.y == p2.y - 2) {
				worldGenerator[p1.x][p1.y + 1] = Tileset.FLOOR;
			}
			else if (p1.x == p2.x && p1.y == p2.y + 2) {
				worldGenerator[p1.x][p1.y - 1] = Tileset.FLOOR;
			}
			else if (p1.x == p2.x - 2 && p1.y == p2.y) {
				worldGenerator[p1.x + 1][p1.y] = Tileset.FLOOR;
			}
			else if (p1.x == p2.x + 2 && p1.y == p2.y){
				worldGenerator[p1.x - 1][p1.y] = Tileset.FLOOR;
			}
			else {
				throw new IllegalArgumentException("The ptr is out of bound!");
			}
		}
	}
	
	private class roomGenerator {
		// hte maximum time to try to build rooms.
		private final int allocTime = 1000;
		
		private ArrayList<roomSpec> roomSpecs = new ArrayList<roomSpec>();
		
		private class roomSpec {
			private int x;
			private int y;
			private int WIDTH;
			private int HEIGHT;
			
			public roomSpec(int x, int y ,int w, int h) {
				this.x = x;
				this.y = y;
				WIDTH = w;
				HEIGHT = h;
			}
		}
		private void fillWithRoom() {
			
			for (int i = 0; i < allocTime; i++) {
				int x = RANDOM.nextInt(WIDTH);
				int y = RANDOM.nextInt(HEIGHT);
				int width = RANDOM.nextInt(WIDTH / 5) + 3;
				int height = RANDOM.nextInt(HEIGHT / 5) + 3;
				
				// Jugde whether the selected point, width, height is suitable or not.
				if (y + height + 1 >= HEIGHT || x + width + 1 >= WIDTH) {
					continue;
				}
				if (isOverlap(x, y, width, height)) {
					continue;
				}
				roomSpecs.add(new roomSpec(x, y, width, height));
				addRoom(x, y, width, height);
			}
		}
		
		
		private boolean isOverlap(int x, int y, int width, int height) {
			x--;
			y--;
			width += 2;
			height += 2;
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					if (x + i < 0 || x + i > WIDTH || y + j < 0 || y + j > HEIGHT) {
						continue;
					}
					if (Rooms[x + i][y + j] == 1) {
						return true;
					}
 				}
			}
			return false;
		}
		
		
		private void addRoom(int x, int y, int width, int height) {
			// The small block is Rooms's.
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					Rooms[x + i][y + j] = 1;
				}
			}
			
			for (int i = 1; i < width - 1; i++) {
				for (int j = 1; j < height - 1; j++) {
					worldGenerator[x + i][y + j] = Tileset.GRASS;
				}
			}
			
			// Make room's walls.
			for (int i = 0; i < width; i++) {
				// worldGenerator[x + i][y] = Tileset.WALL;
				// worldGenerator[x + i][y + height - 1] = Tileset.WALL;
			}
			for (int i = 0; i < height; i++) {
				// worldGenerator[x][y + i] = Tileset.WALL;
				// worldGenerator[x + width - 1][y + i] = Tileset.WALL;
			}
		}
		
		//在房间上开口，打通房间和迷宫的连接
		private void connectRoomWithHallWay(){
			for (int i=0;i<roomSpecs.size();i++){
				roomSpec curRoom=roomSpecs.get(i);
				removeWall(curRoom);
			}
		}
		private void removeWall(roomSpec curRoom){
			for (int i=0;i<100;i++){
				int index=RANDOM.nextInt(4);
				int mx,my;
				switch (index){
					//在左边的墙壁上挖洞
					case 0:
						mx=curRoom.x;
						my=RANDOM.nextInt(curRoom.HEIGHT)+curRoom.y+1;
						if (!canBeRemoved(mx,my,index))continue;
						worldGenerator[mx][my]=Tileset.FLOOR;
						if (Tileset.FLOOR.equals(worldGenerator[mx-1][my]))
							return;
						worldGenerator[mx-1][my]=Tileset.FLOOR;
						if (Tileset.GRASS.equals(worldGenerator[mx-2][my]))
							continue;
						return;
					//在右边的墙壁上挖洞
					case 1:
						mx=curRoom.x+curRoom.WIDTH+1;
						my=RANDOM.nextInt(curRoom.HEIGHT)+curRoom.y+1;
						if (!canBeRemoved(mx,my,index))continue;
						worldGenerator[mx][my]=Tileset.FLOOR;
						if (Tileset.FLOOR.equals(worldGenerator[mx+1][my]))
							return;
						worldGenerator[mx+1][my]=Tileset.FLOOR;
						if (Tileset.GRASS.equals(worldGenerator[mx+2][my]))
							continue;
						return;
					//在下面的墙壁上挖洞
					case 2:
						mx=RANDOM.nextInt(curRoom.WIDTH)+curRoom.x+1;
						my=curRoom.y;
						if (!canBeRemoved(mx,my,index))continue;
						worldGenerator[mx][my]=Tileset.FLOOR;
						if (Tileset.FLOOR.equals(worldGenerator[mx][my-1]))
							return;
						worldGenerator[mx][my-1]=Tileset.FLOOR;
						if (Tileset.GRASS.equals(worldGenerator[mx][my-2]))
							continue;
						return;
					//在上面的墙壁上挖洞
					case 3:
						mx=RANDOM.nextInt(curRoom.WIDTH)+curRoom.x+1;
						my=curRoom.y+curRoom.HEIGHT+1;
						if (!canBeRemoved(mx,my,index))continue;
						worldGenerator[mx][my]=Tileset.FLOOR;
						if (Tileset.FLOOR.equals(worldGenerator[mx][my+1]))
							return;
						worldGenerator[mx][my+1]=Tileset.FLOOR;
						if (Tileset.GRASS.equals(worldGenerator[mx][my+2]))
							continue;
						return;
					
				}
				
			}
		}
		private boolean canBeRemoved(int x,int y,int direction){
			if (Tileset.FLOOR.equals(worldGenerator[x][y]))
				return false;
			switch (direction){
				//向左挖：
				case 0:
					if (x<=1)
						return false;
					if (Tileset.NOTHING.equals(worldGenerator[x-1][y])||(Tileset.WALL.equals(worldGenerator[x-2][y])&&Tileset.WALL.equals(worldGenerator[x-1][y])))
						return false;
					return true;
				//向右挖：
				case 1:
					if (x>=WIDTH-2)
						return false;
					if (Tileset.NOTHING.equals(worldGenerator[x+1][y])||(Tileset.WALL.equals(worldGenerator[x+2][y])&&Tileset.WALL.equals(worldGenerator[x+1][y])))
						return false;
					return true;
				//向下挖：
				case 2:
					if (y<=1)
						return false;
					if (Tileset.NOTHING.equals(worldGenerator[x][y-1])||(Tileset.WALL.equals(worldGenerator[x][y-2])&&Tileset.WALL.equals(worldGenerator[x][y-1])))
						return false;
					return true;
				//向上挖：
				case 3:
					if (y>=HEIGHT-2)
						return false;
					if (Tileset.NOTHING.equals(worldGenerator[x][y+1])||(Tileset.WALL.equals(worldGenerator[x][y+2])&&Tileset.WALL.equals(worldGenerator[x][y+1])))
						return false;
					return true;
			}
			return false;
		}
		
	}

	private void removeDeadends(){
		for (int i=0;i<WIDTH;i++){
			for (int j=0;j<HEIGHT;j++){
				if ((Tileset.GRASS.equals(worldGenerator[i][j])||Tileset.FLOOR.equals(worldGenerator[i][j]))){
					worldGenerator[i][j]=Tileset.FLOOR;
					int cnt=0;
					if (Tileset.WALL.equals(worldGenerator[i-1][j]))
					cnt++;
					if (Tileset.WALL.equals(worldGenerator[i+1][j]))
						cnt++;
					if (Tileset.WALL.equals(worldGenerator[i][j-1]))
						cnt++;
					if (Tileset.WALL.equals(worldGenerator[i][j+1]))
						cnt++;
					if (cnt>=3)
						DFS(i,j);
				}
			}
		}
	}
	private static int[][]next={
			{1,0},
			{0,-1},
			{-1,0},
			{0,1}
	};
	private boolean[][] isPassed = new boolean[100][100];
	private boolean DFS(int x,int y){
		int cnt=0;
		Queue<Pos>accessiblePath=new LinkedList<Pos>();
		//先查找某一点周围所有的点，将可以通行的点加入候选列表
		for (int i=0;i<=3;i++){
			int mx=x+next[i][0];
			int my=y+next[i][1];
			if (mx<0||mx>=WIDTH||my<0||my>=HEIGHT)
				continue;
			if (Tileset.WALL.equals(worldGenerator[mx][my])){
				cnt++;
				continue;
			}
			if (isPassed[mx][my]==true) {
				continue;
			}
			if (Tileset.GRASS.equals(worldGenerator[mx][my]))
			worldGenerator[mx][my]=Tileset.FLOOR;
			if (Tileset.FLOOR.equals(worldGenerator[mx][my])){
				accessiblePath.offer(new Pos(mx,my));
			}
		}
	if (cnt>=3)worldGenerator[x][y]=Tileset.WALL;
		while (!accessiblePath.isEmpty()){
			Pos pos=accessiblePath.peek();
			isPassed[pos.x][pos.y]=true;
			if(DFS(pos.x,pos.y))cnt++;
			if (cnt>=3)
				worldGenerator[x][y]=Tileset.WALL;
			accessiblePath.poll();
		}
		return cnt>=3;
	}

	private void destroyWall(){
		int[][]next2={
				{1,1},
				{1,-1},
				{-1,-1},
				{-1,1}
		};
		for (int i=0;i<worldGenerator.length;i++){
		for (int j=0;j<worldGenerator[0].length;j++){
			if (Tileset.WALL.equals(worldGenerator[i][j])){
				boolean isDestroy=true;
				//判断某一点对角线上的四个点是否是FLOOR
				for (int k=0;k<4;k++){
					int mx=i+next2[k][0];
					int my=j+next2[k][1];
					if (mx<0||mx>=WIDTH||my<0||my>=HEIGHT)
						continue;
					if (Tileset.FLOOR.equals(worldGenerator[mx][my]))
						isDestroy=false;
				}
				if (isDestroy==true)
					worldGenerator[i][j]=Tileset.NOTHING;
				}
			}
		}
	}
	private void GenerateDoor(){
		while (true){
			int x=RANDOM.nextInt(WIDTH);
			int y=RANDOM.nextInt(HEIGHT);
			if (Tileset.FLOOR.equals(worldGenerator[x][y])){
				door=new Pos(x,y);
				worldGenerator[x][y]=Tileset.LOCKED_DOOR;
				return;
			}
		}
	}
	private void GeneratePlayer(){
		while (true){
			int x=RANDOM.nextInt(WIDTH);
			int y=RANDOM.nextInt(HEIGHT);
			if (Tileset.FLOOR.equals(worldGenerator[x][y])){
				player=new Pos(x,y);
				worldGenerator[x][y]=Tileset.PLAYER;
				return;
			}
		}
	}

}


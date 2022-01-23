public class OffByN implements CharacterComparator {
	
	static int num;
	
	OffByN(int N){
		this.num = N;
	}
	
	@Override
	public boolean equalChars(char x, char y) {
		return Math.abs(x - y) == num;
	}
	
}

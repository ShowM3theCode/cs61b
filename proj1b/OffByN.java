public class OffByN implements CharacterComparator {
	
	private static int num;
	
	public OffByN(int N){
		this.num = N;
	}
	
	@Override
	public boolean equalChars(char x, char y) {
		return Math.abs(x - y) == num;
	}
	
}

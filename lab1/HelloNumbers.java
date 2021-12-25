public class HelloNumbers{
	public static void main(String[] args){
		int x = 0;
		int num = 1;
		while(num<=10){
			System.out.print(x+" ");
			x+=num++;
		}
	}
}
public class ArrayDeque<T> {
	private int size;
	private int capacity;
	private int nextFirst;
	private int nextLast;
	private T[] items;
	public ArrayDeque(){
		size = 0;
		capacity = 8; // Initialize the capacity as 8.
		items =(T []) new Object[capacity];
		nextFirst = 0; // Record the next addFirst()'s subscript.
		nextLast = 1;
	}
	public ArrayDeque(ArrayDeque other){
	size = other.size;
	capacity = other.capacity;
	nextFirst = other.nextFirst;
	nextLast = other.nextLast;
	int tmp =size;
	items =(T []) new Object[capacity];
	int ptr = nextFirst + 1;
	while(tmp-- != 0){
		if(ptr == size){
			ptr = 0;
		}
		items[ptr] = (T)other.items[ptr];
	}
}
	private void addingMemory(){
		T[] newItems =(T[]) new Object[capacity*2];
		int tmp = size;
		int ptr = nextFirst + 1;
		int ptr2 = 1;
		while(tmp-- != 0){
			if(ptr == size){
				ptr = 0;
			}
			newItems[ptr2++] = items[ptr++];
		}
		nextFirst = 0;
		nextLast = ptr;
		capacity *= 2;
		items = newItems;
	}
	public void addFirst(T i){
		if(size == capacity){
			addingMemory();
		}
		if(nextFirst < 0){
			nextFirst = capacity - 1;
		}
		items[nextFirst--] = i;
		size++;
	}
	public void addLast(T i){
		if(size == capacity){
			addingMemory();
		}
		if(nextLast == capacity){
			nextLast = 0;
		}
		items[nextLast++] = i;
		size++;
	}
	public boolean isEmpty(){
		if(size == 0) return true;
		return false;
	}
	public void printDeque(){
		if(isEmpty()){
			System.out.println("The deque is EMPTY !");
			return;
		}
		int check = 0;
		int tmp = size;
		int ptr = nextFirst + 1;
		while(tmp-- != 0){
			if(ptr == capacity){
				ptr = 0;
			}
			if(check == 0){
				check++;
				System.out.print(items[ptr]);
				continue;
			}
			System.out.print(" " + items[ptr]);
		}
	}
	public T getLast(){
		return items[nextLast - 1];
	}
	public T get(int index){
		if(index + nextFirst + 1> capacity) return items[nextFirst + index - capacity];
		return items[nextFirst + index + 1];
	}
	public int size(){
		return size;
	}
	public T removeFirst(){
		if(isEmpty()){
			return null;
		}
		size--;
		nextFirst++;
		if(isEmpty()){
			return null;
		}
		return items[nextFirst + 1];
	}
	public T removeLast(){
		if(isEmpty()){
			return null;
		}
		size--;
		nextLast--;
		if(isEmpty()){
			return null;
		}
		return items[nextLast - 1];
	}
}

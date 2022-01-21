public class LinkedListDeque<T> {
    private class StuffNode{
    	private T item;
    	private StuffNode former;
    	private StuffNode next;
    	
    	public StuffNode(){
    		former = null;
    		next = null;
	    }
	
	    public StuffNode(T i){
		    item = i;
		    former = null;
		    next = null;
	    }
	    
	    public StuffNode(T x,StuffNode f,StuffNode n){
    		item = x;
    		former = f;
    		next = n;
	    }
    }
    private int size;   // To record the size of the deque.
    private StuffNode formerSentinel; // The former sentinel
    private StuffNode latterSentinel; // The latter sentinel
	public LinkedListDeque(){   // Initialize the deque.
		size = 0;
		formerSentinel = new StuffNode();
		latterSentinel = new StuffNode();
		formerSentinel.next = latterSentinel;
		latterSentinel.former = formerSentinel;
		formerSentinel.former = latterSentinel;
		latterSentinel.next = formerSentinel;
	}
	public LinkedListDeque(LinkedListDeque other){
		size = other.size;
		formerSentinel = new StuffNode();
		latterSentinel = new StuffNode();
		formerSentinel.next = latterSentinel;
		latterSentinel.former = formerSentinel;
		formerSentinel.former = latterSentinel;
		latterSentinel.next = formerSentinel;
		int tmp =size;
		StuffNode tmpNode = other.formerSentinel.next;
		while(tmp-- != 0){
			addLast(tmpNode.item);
			tmpNode = tmpNode.next;
		}
	}
	public void addFirst(T i){
		size++;
		StuffNode tmp = new StuffNode(i);
		tmp.next = formerSentinel.next;
		formerSentinel.next.former = tmp;
		formerSentinel.next = tmp;
		tmp.former = formerSentinel;
	}
	public void addLast(T i){
		size++;
		StuffNode tmp = new StuffNode(i);
		tmp.former = latterSentinel.former;
		latterSentinel.former.next = tmp;
		latterSentinel.former = tmp;
		tmp.next = latterSentinel;
	}
	public boolean isEmpty(){
		if(formerSentinel.next == latterSentinel){
			return true;
		}
		return false;
	}
	public int size(){
		return size;
	}
	public void printDeque(){
		if(isEmpty()){
			System.out.println("The deque is EMPTY !");
			return;
		}
		int check = 0;
		StuffNode print = formerSentinel.next;
		while(print != latterSentinel){
			if(check == 0) {
				System.out.print(print.item);
				check++;
			}
			else{
				System.out.print(" " + print.item);
			}
			print = print.next;
		}
		System.out.println();
	}
	public T removeFirst(){
		if(isEmpty()){
			return null;
		}
		if(formerSentinel.next.next == latterSentinel){
			StuffNode tmp = formerSentinel.next;
			formerSentinel.next = latterSentinel;
			latterSentinel.former = formerSentinel;
			tmp = null;
			return null;
		}
		StuffNode theDelete = formerSentinel.next;
		formerSentinel.next = theDelete.next;
		formerSentinel.next.former = formerSentinel;
		theDelete = null;
		return formerSentinel.next.item;
	}
	public T removeLast(){
		if(isEmpty()){
			return null;
		}
		if(formerSentinel.next.next == latterSentinel){
			StuffNode theDelete = latterSentinel.former;
			formerSentinel.next = latterSentinel;
			latterSentinel.former = formerSentinel;
			theDelete = null;
			return null;
		}
		StuffNode theDelete = latterSentinel.former;
		latterSentinel.former = theDelete.former;
		latterSentinel.former.next = latterSentinel;
		theDelete = null;
		return latterSentinel.former.item;
	}
	public T get(int index){
		if(index < 0|| index > size) return null;
		StuffNode tmp = formerSentinel.next;
		while(index-- != 0){
			tmp = tmp.next;
		}
		return tmp.item;
	}
	public T getRecursive(int index){
		if(index < 0|| index > size) return null;
		StuffNode tmp = formerSentinel.next;
		return goRecursive(tmp,index);
	}
	private T goRecursive(StuffNode s,int index) {
		if (index == 0) return s.item;
		return goRecursive(s.next, index - 1);
	}
}

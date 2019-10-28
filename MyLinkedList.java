/**
 * Title: class MyLinkedList
 *  Description: This class construct the a data structure called
 *  MyLinkedList that has circular doubly linked feature, similar to 
 *  LinkedList.
 *  @author Chuhuan Huang A13342061 cs12fbx
 * 
 * */
import java.util.*;
public class MyLinkedList<E> extends AbstractList<E> {
  
  private int nelems;
  private Node dummy;
  
  protected class Node {
    E data;
    Node next;
    Node prev;
    
    /** Constructor to create singleton Node */
    public Node(E element)
    {    
		 this.data = element;
    }
    /** Constructor to create singleton link it between previous and next 
      *   @param element Element to add, can be null
      *   @param prevNode predecessor Node, can be null
      *   @param nextNode successor Node, can be null 
      */
    public Node(E element, Node prevNode, Node nextNode)
    {
		this.data = element;
		this.next = nextNode;
		this.prev = prevNode;
		
    }
    /** Remove this node from the list. Update previous and next nodes */
    public void remove()
    {
		  this.next.setPrev(this.prev);
		  this.prev.setNext(this.next); 
    }
    /** Set the previous node in the list
      *  @param p new previous node
      */
    public void setPrev(Node p)
    {
		this.prev = p; 
    }
	
    /** Set the next node in the list
      *  @param n new next node
      */

    public void setNext(Node n)
    {
		this.next = n;
    }
    
    /** Set the element 
      *  @param e new element 
      */
    public void setElement(E e)
    {
		this.data = e;
    }
    /** Accessor to get the next Node in the list */
    public Node getNext()
    {
      return (Node) this.next; 
    }
    /** Accessor to get the prev Node in the list */
    public Node getPrev()
    {
      return (Node) this.prev; 
    } 
    /** Accessor to get the Nodes Element */
    public E getElement()
    {
      return (E) this.data; 
    } 
  }
  
  /** ListIterator implementation */ 
  protected class MyListIterator implements ListIterator<E> {
    
    private boolean forward;
    private boolean canRemove;
    private Node left,right; // Cursor sits between these two nodes
    private int idx;        // Tracks current position, what next would return
    
    /** Constructor: MyListIterator()
     *  description: this constructor constructs a MyListIterator to iterate
     *  through MyLinkedList.
     *  it contains a boolean named forward to trace the moving direction
     *  of the MyListIterator, a boolean named canRemove to trace whether next() 
     *  or previous() is called, two Nodes, one named left and the other 
     *  named right that have connection with the iterator, and idx that 
     *  indicates the current position of the iterator, tracing the ordianl number 
     *  of the gap between the two nodes where the iterator resides.
     *  */
    public MyListIterator()
    {
		this.forward = true;
		this.canRemove = false;
		this.left = dummy;
		this.right = dummy.next;
		this.idx = 0;
    }
    
    @Override
    /** Method header:
     *  Method name: add()
     *  Method description:Insert the given item into the list immediately 
     *  before whatever would have been returned by a call to next().
     *  The new item is inserted before the current cursor, so it would 
     *  be returned by a call to previous() immediately following.
     *  The value of nextIndex or previousIndex both are increased by one.
     * */
    public void add(E e) throws NullPointerException
    {   if (e == null) { throw new NullPointerException();} 
		else {
			Node addingNode = new Node(e,this.left,this.right);
		    this.left.setNext(addingNode);
		    this.right.setPrev(addingNode);
		    this.left = addingNode;
		    this.idx++;
		    nelems++;
		    this.canRemove = false;}
		//boolean should consider the relative position to dummy
    }
    
    @Override 
    public boolean hasNext()
    { 
        return (this.right != dummy); 
    }
    
    @Override
    public boolean hasPrevious() 
    {
        return (this.left != dummy ); 
    }
    
    @Override
    /** Method header:
     *  Method name: next()
     *  Method description: Return the next element in the list when 
     *  going forward, and move the iterator forward for one node.
     * */
    public E next() throws NoSuchElementException
    {
		if (!hasNext()){throw new NoSuchElementException();}
		else {
			E val = this.right.getElement();
			this.idx++;
			this.forward = true;
			this.canRemove = true;
			this.right = this.right.getNext();
			this.left = this.right.getPrev();
	        return (E) val; }
    }
    
    @Override
    public int nextIndex()
    {
        if (this.right == dummy) {return nelems;}
        else {return this.idx;}
    }
    
    @Override
    /** Method header:
     *  Method name: previous()
     *  Method description:Return the next element in the list when 
     *  going backward, and move the iterator backward for one node.
     * */
    public E previous() throws NoSuchElementException
    {
		if (!hasPrevious()){throw new NoSuchElementException();}
		else {
			E val = this.left.getElement();
			this.idx--;
			this.forward = false;
			this.canRemove = true;
			this.left = this.left.getPrev();
			this.right = this.left.getNext();
			return (E) val; }
    }
    
    @Override
    public int previousIndex()
    {
        if (this.left == dummy) {return -1 ;}
        else {return this.idx - 1;}
    }
    
    @Override
    /** Method header:
     *  Method name: remove()
     *  Method description: Remove the last element returned by the most
     *  recent call to either next()/previous() 
     * */
    public void remove() throws IllegalStateException
    {
		if (!this.canRemove) {throw new IllegalStateException();}
		else {	
			if (this.forward) {
				this.left.getPrev().setNext(this.right);
				this.right.setPrev(this.left.getPrev());
				this.left = this.left.prev;
				this.idx--;
				}
			else {
				this.right.getNext().setPrev(this.left);
				this.left.setNext(this.right.getNext());
				this.right = this.right.next;
				}
			nelems--;
			this.canRemove = false;}
    }

    @Override
    /** Method header:
     *  Method name: set()
     *  Method description:Change the value in the node returned
     *  by the most recent next/previous with the new value.
     * */
    public void set(E e) 
      throws NullPointerException,IllegalStateException
    {
		if (!this.canRemove) {throw new IllegalStateException();}
		else if (e == null) {throw new NullPointerException();}
		else {
			if (this.forward){
				this.left.data = e;
				}
			else {
				this.right.data = e;
				}
		}
    }  
  }
  
  //  Implementation of the MyLinkedList Class
  
  /** Only 0-argument constructor is define */
	
	/** Constructor: MyLinkedList()
	 *  description: this constructor constructs a MyLinkedList that
	 *  initially has a dummy node and capacity tracer named nelems.
	 *  It has ciucular doubly linked structure that is specified by the
	 *  following methods. 
	 *  */
  
  public MyLinkedList(){
	  this.dummy = new Node(null);
	  dummy.next = dummy;
	  dummy.prev = dummy;
	  this.nelems = 0;
	  }

  @Override
  public int size()
  {
    return this.nelems;
  }
  
  @Override
  /** Method Header:
   *  Method Name: get()
   *  Method description: Get data within the node at position i. The input
   *  index can be any integer i due to ourlinkedlist circular nature.
   *  */
  public E get(int index) throws IndexOutOfBoundsException
  {
    if (this.isEmpty()){throw new IndexOutOfBoundsException();}
    else {return (E) this.getNth(index).data; }
  }
  
  @Override
  /** Method Header:
   *  Method Name: add()
   *  Method description: add a node into this list by index.
   *  The input index can be any integer in interval due to our linkedlist circular nature.
   *  */
  public void add(int index, E data) throws NullPointerException
  {
	  if (data == null) {throw new NullPointerException();}
	  else if (nelems == 0) {
		  Node addingNode = new Node(data,this.dummy,this.dummy);
		  this.dummy.next = addingNode;
		  this.dummy.prev = addingNode;
		  nelems++;
		  } 
	  else {
		  index = index % (this.nelems+1);
	      index = index + (this.nelems+1);// make sure index become positive and more concise
	      index = index % (this.nelems+1);// make sure index become most "concise"
		  if ( index == nelems  ) { this.add(data); }// two side of dummy
		  else if ( index == 0 ) { 
			     Node addingNode = new Node(data,this.getNth(index).getPrev(),this.getNth(index));
			     this.dummy.setNext(addingNode);
			     this.getNth(index).setPrev(addingNode);
			     nelems++;
			  }
		  else {
			     Node addingNode = new Node(data,this.getNth(index).getPrev(),this.getNth(index));
				  this.getNth(index).setPrev(addingNode);
				  this.getNth(index - 1).setNext(addingNode);
				  nelems++; 
			  }
		 }
  }

  @Override
  /** Method Header:
   *  Method Name: add()
   *  Method description: add a node at the end into this list
   *  */
  public boolean add(E data) throws NullPointerException
  {
	  if (data == null) {throw new NullPointerException();}
	  else {
			  Node addingNode = new Node(data,this.dummy.getPrev(),this.dummy);
			  this.dummy.setPrev(addingNode);
			  this.getNth(this.nelems - 1).setNext(addingNode);
			  nelems++;
			  return true; 
		  }
  }
  
  @Override
  /** Method Header:
   *  Method Name: set()
   *  Method description: set the value at index i to data. The input index can
   *  be any integer in interval
   *  */
  public E set(int index, E data) 
    throws IndexOutOfBoundsException,NullPointerException
  { 
    if (this.isEmpty()) {throw new IndexOutOfBoundsException();}
    else if (data == null) { throw new NullPointerException();}
    else {
		   E dataTemp = this.getNth(index).data;
		   this.getNth(index).data = data;
		   return (E) dataTemp; 
		}
  }
  
  @Override
 /**  Method Header:
   *  Method Name: remove()
   *  Method description: Remove the node from position i in this list. 
   *  The input index can be any integer in interval
   *  */
  public E remove(int index) throws IndexOutOfBoundsException
  { 
		index = index % this.nelems;
		index = index + this.nelems;// make sure index become positive
		index = index % this.nelems;// make sure index become most "concise"
		if (isEmpty()) {throw new IndexOutOfBoundsException();} 
		else{
			E dataTemp;
			if (index == 0 ) { 
				  dataTemp = this.getNth(index).data;
				  this.dummy.next = this.getNth(1);
				  this.getNth(1).prev = this.dummy;
				}
			else if (index == nelems - 1) {
				  dataTemp = this.getNth(index).data;
				  this.dummy.next = this.getNth(nelems-2);
				  this.getNth(nelems -2).prev = this.dummy;
				}	
			else { 
				 dataTemp = this.getNth(index).data;
				 this.getNth(index - 1).next = this.getNth(index + 1);
				 this.getNth(index + 1).prev = this.getNth(index - 1);
				} 
			nelems--;
		    return (E)  dataTemp ;
	  } // TODO: consider the case in the two side of dummy
  }
  
  @Override
  public void clear()
  {
	  int i = this.nelems - 1;
	  while (this.size() > 0) {
		  this.remove(i);
		  i--;
		  }
	  this.nelems = 0;
  }
  
  @Override
  public boolean isEmpty()
  {
    return this.nelems == 0;  
  }
  
  // Helper method to get the Node at the Nth index
  private Node getNth(int index) 
  {
    if (isEmpty()){ return this.dummy;}
    else {
			index = index % (this.nelems);
		    index = index + (this.nelems);// make sure index become positive
		    index = index % (this.nelems);// make sure index become most "concise"
			if (index == 0) { return this.dummy.getNext();}
			else { 
				Node n = this.dummy.getNext();
				for (int i = 0; i < index; i++) {
					n = n.getNext();
					}
					return n;
				}
	}
		
  }

  public Iterator<E> iterator() {
    return new MyListIterator();
  }
  
  public ListIterator<E> listIterator() {
    return new MyListIterator();
  }
}

// VIM: set the tabstop and shiftwidth to 4 
// vim:tw=78:ts=4:et:sw=4


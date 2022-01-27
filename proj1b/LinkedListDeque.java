public class LinkedListDeque<T> implements Deque<T>{
    private class node{
        node pre;
        node next;
        T data;
        node(T d, node p, node n){
            data=d;
            pre=p;
            next=n;
        }
        T get(int idx){
            if(idx==0){
                return data;
            }
            return next.get(idx--);
        }
    }
    private int size=0;
    private node sentinel=null;
    public LinkedListDeque(){
        sentinel=new node(null,null,null);
        sentinel.next=sentinel.pre=sentinel;
    }
    public void addFirst(T item){
        size++;
        node t=new node(item,sentinel,sentinel.next);
        t.pre.next=t;
        t.next.pre=t;
    }
    public void addLast(T item){
        size++;
        node t=new node(item,sentinel.pre,sentinel);
        t.pre.next=t;
        t.next.pre=t;
    }
    public boolean isEmpty(){
        return  size==0;
    }
    public int size(){
        return size;
    }
    public void printDeque(){
        node t=sentinel.next;
        while (t!=sentinel){
            System.out.print(t.data);
            t=t.next;
        }
        System.out.println();
    }
    public T removeFirst(){
        if (isEmpty()){return null;}
        size--;
        node t=sentinel.next;
        t.next.pre=t.pre;
        t.pre.next=t.next;
        return t.data;
    }
    public T removeLast(){
        if (isEmpty()){return null;}
        size--;
        node t=sentinel.pre;
        t.next.pre=t.pre;
        t.pre.next=t.next;
        return t.data;
    }
    public T get(int index){
        if(index>=size||index<0){return null;}
        node t=sentinel.next;
        while (index>0){
            t=t.next;
            index--;
        }
        return t.data;
    }
    public T getRecursive(int index){
        if(index>=size||index<0){
            return null;
        }
        return get(index);
    }
}

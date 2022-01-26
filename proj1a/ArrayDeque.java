public class ArrayDeque <T>{
    int size;
    int first;
    int last;
    T []items;
    public ArrayDeque(){
        items=(T[])new Object[9];
        size=0;
        first=0;
    }
    public ArrayDeque(ArrayDeque other){
        T []items=(T[]) new Object[other.items.length];
        System.arraycopy(other.items,0,items,0,other.items.length);
        size=other.size;
        first=other.first;
        last=other.last;
    }
    public void resize(int capacity){
        T []a=(T[])new Object[capacity];
        if(first>last){
            System.arraycopy(items,first,a,0,items.length-first+1);
            System.arraycopy(items,0,a,items.length-first+1,last+1);
        }else {
            System.arraycopy(items,first,a,0,size);
        }
        items=a;
        first=0;
        last=size-1;
    }
    public void addFirst(T item){
        if(size==items.length-1) {
            resize(size*2);
        }
        size++;
        first=(first-1+items.length)%items.length;
        items[first]=item;
    }
    public void addLast(T item){
        if(size==items.length-1) {
            resize(size*2);
        }
        size++;
        last=(last+1+items.length)%items.length;
        items[last]=item;
    }
    public boolean isEmpty(){
        return size==0;
    }
    public int size(){
        return size;
    }
    public void printDeque(){
        for (int i=first;i<first+size;i++){
            System.out.print(items[i% items.length]+" ");
        }
        System.out.println();
    }
    public T removeFirst(){
        if (isEmpty()){return null;}
        T t=items[first];
        items[first]=null;
        first=(first+1+items.length)%items.length;
        if(items.length>=17&&(double)size<0.25*(items.length-1)){
            resize(size/2);
        }
        return t;
    }
    public T removeLast(){
        if (isEmpty()){return null;}
        T t=items[last];
        items[last]=null;
        last=(last-1+items.length)%items.length;
        if(items.length>=17&&(double)size<0.25*(items.length-1)){
            resize(size/2);
        }
        return t;
    }
    public T get(int index){
        return items[(index+first+items.length)% items.length];
    }
}


package loggerbldcmotordriver;

import java.lang.reflect.Array;

/**
 *
 * @author simon
 */
public class RingBuffer<T>
{
    private final T[] buffer;
    
    private int capacity, oldestElement, nextElement;

    public RingBuffer(Class<T> c, int capacity) {
        this.capacity = capacity + 1;
        oldestElement = 0;
        nextElement = 0;
        
        buffer = (T[]) Array.newInstance(c, this.capacity);
    }
    
    public synchronized void put(T data) throws RingBufferException{
        if(data == null){
            throw new RingBufferException(RingBufferException.MSG_NULL_HANDED);
        }
        
        int tempNextElement = (nextElement+1)%capacity;
        if(tempNextElement == oldestElement){
            throw new RingBufferException(RingBufferException.MSG_BUFFER_OVERFLOW);
        }
        
        buffer[nextElement] = data;
        nextElement = tempNextElement;
    }

    public synchronized T get(){
        if(nextElement == oldestElement){
            return null;
        }
        
        T temp = buffer[oldestElement];
        
        buffer[oldestElement] = null;
        
        oldestElement = (oldestElement+1)%capacity;
        
        return temp;
    }
    
    public synchronized T getNullSafe() throws RingBufferException{
        if(nextElement == oldestElement){
            throw new RingBufferException(RingBufferException.MSG_BUFFER_EMPTY);
        }
        
        return get();
    }
    
    public synchronized int getSize(){
        if(nextElement-oldestElement >= 0){
            return nextElement-oldestElement;
        }
        return capacity - oldestElement + nextElement;
    }
    
    public synchronized boolean isEmpty(){
        return getSize() == 0;
    }
    
    public synchronized void clear(){
        oldestElement = 0;
        nextElement = 0;
    }

    public int getCapacity() {
        return capacity - 1;
    }
}

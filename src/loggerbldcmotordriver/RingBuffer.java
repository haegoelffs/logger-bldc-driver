
package loggerbldcmotordriver;

import java.lang.reflect.Array;

/**
 *
 * @author simon
 */
public class RingBuffer<T>
{
    private final T[] buffer;
    
    private int size, oldestElement, nextElement;

    public RingBuffer(Class<T> c, int size) {
        this.size = size + 1;
        oldestElement = 0;
        nextElement = 0;
        
        buffer = (T[]) Array.newInstance(c, this.size);
    }
    
    public synchronized void put(T data) throws RingBufferException{
        int tempNextElement = (nextElement+1)%size;
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
        
        oldestElement = (oldestElement+1)%size;
        
        return temp;
    }
    
    public synchronized int getSize(){
        if(nextElement-oldestElement >= 0){
            return nextElement-oldestElement;
        }
        return size - oldestElement + nextElement;
    }
    
    public synchronized boolean isEmpty(){
        return getSize() == 0;
    }
    
    public synchronized void clear(){
        oldestElement = 0;
        nextElement = 0;
    }
}

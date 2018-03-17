
package loggerbldcmotordriver;

import java.lang.reflect.Array;

/**
 *
 * @author simon
 */
public class StaticRingBuffer<T>
{
    private final T[] buffer;
    
    private int size, nextElement;

    public StaticRingBuffer(Class<T> c, int size) {
        this.size = size;
        nextElement = 0;
        
        buffer = (T[]) Array.newInstance(c, size);
    }
    
    public void put(T data){
        buffer[nextElement] = data;
        nextElement = (nextElement+1)%size;
    }
    
    public T getNext(){
        T temp = buffer[nextElement];
        nextElement = (nextElement+1)%size;
        
        return temp;
    }
    
    public T[] getAll(){
        return buffer;
    }
}

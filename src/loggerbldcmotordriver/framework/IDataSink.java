
package loggerbldcmotordriver.framework;

/**
 *
 * @author simon
 * @param <T>
 */
@FunctionalInterface
public interface IDataSink<T>
{
    public void put(T data);
}

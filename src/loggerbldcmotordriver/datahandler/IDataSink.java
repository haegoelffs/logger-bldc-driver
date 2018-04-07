
package loggerbldcmotordriver.datahandler;

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

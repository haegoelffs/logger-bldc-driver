package loggerbldcmotordriver.datahandler;

import loggerbldcmotordriver.framework.RingBuffer;
import loggerbldcmotordriver.framework.DataPool;
import loggerbldcmotordriver.framework.MessageData;
import loggerbldcmotordriver.framework.IDataSink;

/**
 *
 * @author simon
 */
public class MessagesHandler
{
    private DataPool dataPool;
    private RingBuffer<MessageData> sourceBuffer;
    private IDataSink<MessageData> dataSink;

    public MessagesHandler(RingBuffer<MessageData> sourceBuffer, DataPool dataPool, IDataSink<MessageData> dataSink) {

        this.sourceBuffer = sourceBuffer;
        this.dataSink = dataSink;
        this.dataPool = dataPool;
    }

    public void proceed() {
        MessageData data = sourceBuffer.get();
        if (data != null) {
            dataSink.put(data);
        }
    }
}

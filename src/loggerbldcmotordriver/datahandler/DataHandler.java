package loggerbldcmotordriver.datahandler;

import loggerbldcmotordriver.communication.CommunicationManager;
import loggerbldcmotordriver.framework.DataPool;
import loggerbldcmotordriver.framework.MessageData;
import loggerbldcmotordriver.communication.SimulatorWrapper;
import loggerbldcmotordriver.view.figures.IDataPoint;
import loggerbldcmotordriver.framework.IDataSink;
import loggerbldcmotordriver.framework.TimeValueData;

/**
 *
 * @author simon
 */
public class DataHandler extends Thread
{
    private final DataPool dataPool;
    private final CommunicationManager comManager;
    
    private final MessagesHandler msg_handler;
    private final ValuesHandler freq_handler;
    
    public DataHandler(IDataSink<MessageData> ds_messages, IDataSink<IDataPoint> ds_freq_data) {
        super("DataHandler");
        
        dataPool = new DataPool();
        
        comManager = new CommunicationManager(dataPool, new SimulatorWrapper());
        
        msg_handler=new MessagesHandler(comManager.getMessages_buffer(), dataPool, ds_messages);
        freq_handler = new ValuesHandler(comManager.getRotationFrequenzy_buffer(), ds_freq_data, dataPool, 100000);
        
        start();
    }

    @Override
    public void run() {
        while(true){
            msg_handler.proceed();
            freq_handler.proceed();
        }
    }
    
    public void recycleData(IDataPoint data) {
        if (data instanceof TimeValueData) {
            dataPool.recycleTimeData((TimeValueData) data);
        }
        else if (data instanceof TimeDataGroup) {
            TimeDataGroup timeDataGroup = ((TimeDataGroup) data);
            TimeValueData temp = timeDataGroup.getFirstData();

            while (true) {
                if (temp == ((TimeDataGroup) data).getLastData()) {
                    dataPool.recycleTimeData(temp);
                    break;
                }

                temp = temp.getYounger();
                dataPool.recycleTimeData(temp.getOlder());
            }

            dataPool.recycleTimeDataGroup(timeDataGroup);
        }
    }
    
    // getter & setter

    public CommunicationManager getComManager() {
        return comManager;
    }
    
}

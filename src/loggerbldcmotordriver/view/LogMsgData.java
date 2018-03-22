
package loggerbldcmotordriver.view;

import javafx.beans.property.StringProperty;
import loggerbldcmotordriver.framework.MessageData;
import loggerbldcmotordriver.datahandler.properties.IPropertyData;
import loggerbldcmotordriver.datahandler.properties.StringAdapterProperty;

/**
 *
 * @author simon
 */
public class LogMsgData
{
    private final StringProperty msg_property;
    private final StringProperty timestamp_property; 

    public LogMsgData(MessageData data)
    {
        msg_property = new StringAdapterProperty(new IPropertyData<String>()
        {
            @Override
            public String get() {
                return data.getMsg();
            }

            @Override
            public void set(String set) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Object getBean() {
                return data;
            }

            @Override
            public String getName() {
                return "logged message";
            }
        });
        
        timestamp_property = new StringAdapterProperty(new IPropertyData<String>()
        {
            @Override
            public String get() {
                return formatTimestamp_us(data.getTimestamp_us());
            }

            @Override
            public void set(String set) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Object getBean() {
                return data;
            }

            @Override
            public String getName() {
                return "timestamp logged message";
            }
        });
    }

    public StringProperty getMsg_property() {
        return msg_property;
    }

    public StringProperty getTimestamp_property() {
        return timestamp_property;
    }
    
    // private methods
    private String formatTimestamp_us(long timestamp){
        int us = (int)(timestamp % 1000);
        timestamp = timestamp/1000;
        
        int ms = (int)(timestamp% 1000);
        timestamp = timestamp/1000;
        
        int s = (int)(timestamp % 60);
        timestamp = timestamp/60;
        
        int min = (int)(timestamp);
        
        return String.format("%dmin %ds %dms %dus", min, s, ms, us);
    }
    
}

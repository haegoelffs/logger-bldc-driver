
package loggerbldcmotordriver.view;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;

/**
 *
 * @author simon
 */
public class ListLogMsgBuilder
{
    private final TitledPane view;
    
    private TableView<LogMsgData> table;

    public ListLogMsgBuilder(ObservableList<LogMsgData> datas) {
        initTable();
        
        table.setItems(datas);
        
        this.view = new TitledPane("Meldungen", table);
        this.view.setCollapsible(false);
    }
    
    private void initTable(){
        table = new TableView<>();

        {
            TableColumn<LogMsgData, String> column = new TableColumn("Zeitstempel");
            column.setCellValueFactory(cellData -> cellData.getValue().getTimestamp_property());
            table.getColumns().add(column);
        }
        {
            TableColumn<LogMsgData, String> column = new TableColumn("Meldung");
            column.setCellValueFactory(cellData -> cellData.getValue().getMsg_property());
            table.getColumns().add(column);
        }
    }

    public TitledPane getView() {
        return view;
    }
}

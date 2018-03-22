package loggerbldcmotordriver.view.elements;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;

/**
 *
 * @author sdb
 */
public class NumberField extends TextField
{

    private final IntegerProperty value;

    public NumberField() {
        this.value = new SimpleIntegerProperty();

        this.value.addListener((observable, oldValue, newValue)
                -> {
            this.textProperty().setValue(newValue + "");
        });

        this.textProperty().addListener((observable, oldValue, newValue)
                -> {
            if (newValue.matches("[+-]")) {
                // only sign -> no valid integer to parse
            }else if(newValue.isEmpty()){
                this.value.set(0);
            }
            else {
                this.value.set(Integer.parseInt(newValue));
            }
        });
        
        this.replaceText(0, 0, "0");
    }

    @Override
    public void replaceText(int start, int end, String text) {
        if (start == 0 || end == 0) {
            // first sign can also be a + or -
            if (text.matches("[-]")) {
                super.replaceText(start, end, text);
            }
        }
        if (text.matches("[0-9]") || text.isEmpty()) {
            super.replaceText(start, end, text);
        }
    }

    public IntegerProperty getValueProperty() {
        return this.value;
    }
}

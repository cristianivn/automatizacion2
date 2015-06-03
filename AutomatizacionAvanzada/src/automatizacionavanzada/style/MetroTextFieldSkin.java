package automatizacionavanzada.style;

import javafx.scene.control.TextField;


public class MetroTextFieldSkin extends TextFieldWithButtonSkin{
    public MetroTextFieldSkin(TextField textField) {
        super(textField);
    }

    @Override
    protected void rightButtonPressed()
    {
        getSkinnable().setText("");
    }

}
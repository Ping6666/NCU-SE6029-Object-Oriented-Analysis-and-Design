import UMLWindow.TheWindow;

/** controll the program level */
public class UMLEditor {
    static TheWindow theWindow;
    // since main cannot call non-static variable
    // therefore, initialize theWindow to be static.

    public static void main(String[] args) {
        theWindow = new TheWindow();
    }
}

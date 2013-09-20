import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: grainierp
 * Date: 9/17/13
 * Time: 9:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String[] args) {
        DomDiff domDiff = new DomDiff();

        try {
            domDiff.getDom();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}

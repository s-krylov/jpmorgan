package jpmorgan;


import jpmorgan.core.Utils;
import jpmorgan.core.processing.FlowManager;


public class Application {

    public static void main(String[] args) throws Exception {
        // to get UI uncomment lines below :)
        //DatabaseManagerSwing manager = new DatabaseManagerSwing();
        //manager.main();
        //manager.connect(DataSourceFactory.getInstance().createDataSource().getConnection());
        //manager.start();
        try (FlowManager flowManager = new FlowManager(50, 10)) {
            flowManager.execute(Utils.readFileAsString("/xml/m1.xml"));
            flowManager.execute(Utils.readFileAsString("/xml/m2.xml"));
            flowManager.execute(Utils.readFileAsString("/xml/m3.xml"));
            flowManager.execute(Utils.readFileAsString("/xml/m21.xml"));
            flowManager.execute(Utils.readFileAsString("/xml/m22.xml"));
            flowManager.execute(Utils.readFileAsString("/xml/m31.xml"));
            flowManager.execute(Utils.readFileAsString("/xml/m1.xml"));
            flowManager.execute(Utils.readFileAsString("/xml/m2.xml"));
            flowManager.execute(Utils.readFileAsString("/xml/m1.xml"));
            flowManager.execute(Utils.readFileAsString("/xml/m2.xml"));
            flowManager.execute(Utils.readFileAsString("/xml/m3.xml"));
            flowManager.execute(Utils.readFileAsString("/xml/m21.xml"));
            flowManager.execute(Utils.readFileAsString("/xml/m22.xml"));
        }
    }
}

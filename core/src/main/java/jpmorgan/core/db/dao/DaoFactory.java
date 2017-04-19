package jpmorgan.core.db.dao;


public class DaoFactory {

    private Dao dao;

    private DaoFactory() {
        this.dao = new DaoImpl();
    }

    public Dao create() {
        return dao;
    }

    public static DaoFactory getInstance() {
        return DaoFactory.DaoFactoryHolder.INSTANCE;
    }

    private static class DaoFactoryHolder {
        private static final DaoFactory INSTANCE = new DaoFactory();
    }
}

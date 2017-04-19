package jpmorgan.core.processing;

import jpmorgan.core.mock.MockedDao;
import jpmorgan.entities.Message;
import jpmorgan.entities.OperationEnum;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static jpmorgan.core.TestHelper.*;
import static jpmorgan.core.TestHelper.createMessage;
import static org.junit.Assert.*;



public class EntityPersisterTest {

    @Test
    public void persist() throws Exception {
        MockedDao dao = new MockedDao();
        EntityPersister entityPersister = new EntityPersister(dao);
        entityPersister.persist(createMessage(createMessageType1(createSale("test", BigDecimal.TEN))));

        assertNotNull(dao.getProductType());
        assertNotNull(dao.getOccurrences());
        assertNull(dao.getAdjustment());
        assertEquals(Integer.valueOf(1), dao.getOccurrences().getOccurrences());
        assertEquals("test", dao.getProductType().getProduct());
        assertTrue(dao.getOccurrences().getOriginalPrice().compareTo(BigDecimal.TEN) == 0);
        assertTrue(dao.getOccurrences().getPrice().compareTo(BigDecimal.TEN) == 0);


        dao = new MockedDao();
        entityPersister = new EntityPersister(dao);
        entityPersister.persist(createMessage(createMessageType2(createSale("test1", BigDecimal.TEN), new BigInteger("2"))));

        assertNotNull(dao.getProductType());
        assertNotNull(dao.getOccurrences());
        assertNull(dao.getAdjustment());
        assertEquals(Integer.valueOf(2), dao.getOccurrences().getOccurrences());
        assertEquals("test1", dao.getProductType().getProduct());
        assertTrue(dao.getOccurrences().getOriginalPrice().compareTo(BigDecimal.TEN) == 0);
        assertTrue(dao.getOccurrences().getPrice().compareTo(BigDecimal.TEN) == 0);

        dao = new MockedDao();
        entityPersister = new EntityPersister(dao);
        entityPersister.persist(createMessage(createMessageType3(createSale("test2", BigDecimal.TEN), new BigDecimal(50), OperationEnum.ADD)));

        assertNotNull(dao.getProductType());
        assertNotNull(dao.getOccurrences());
        assertNotNull(dao.getAdjustment());
        assertEquals(Integer.valueOf(1), dao.getOccurrences().getOccurrences());
        assertEquals("test2", dao.getProductType().getProduct());
        assertTrue(dao.getOccurrences().getOriginalPrice().compareTo(BigDecimal.TEN) == 0);
        assertTrue(dao.getOccurrences().getPrice().compareTo(BigDecimal.TEN) == 0);
        assertTrue(dao.getAdjustment().getOperand().compareTo(new BigDecimal(50)) == 0);
        assertEquals(OperationEnum.ADD, dao.getAdjustment().getAction());
    }

    @Test(expected = IllegalArgumentException.class)
    public void persistException() throws Exception {
        MockedDao dao = new MockedDao();
        EntityPersister entityPersister = new EntityPersister(dao);
        entityPersister.persist(new Message());
    }

}
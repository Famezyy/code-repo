package com.youyi.seataconsumer;

import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
public class ConsumerControllerTests extends AbstractTests {

    /**
     * initial state of the data:
     *     datasource3306: id(1), money(100.0)
     *     datasource3307: id(1), money(100.0)
     * <p>
     * test scenario:
     *     1.seata-consumer update the money by adding 200 for the id(1) in datasource3306
     *     2.seata-consumer calls seata-provider
     *       2.1 seata-provider update the money by adding 200 for the id(1) in datasource3307
     *     3.seata-consumer throw ArithmeticException
     * <p>
     *  expect result:
     *      the data should be kept the same as the initial state.
     * <p>
     *  actual result:
     *     datasource3306: id(1), money(100.0)
     *     datasource3307: id(1), money(300.0)
     */
    @Test
    void testConsumerController() {
        assertThrows(ArithmeticException.class, () -> consumerController.consume(1L));
        assertEquals(100.0, getMoney(dataSource3306()));
        // the below assertion will fail
        assertEquals(100.0, getMoney(dataSource3307()));
    }

    private double getMoney(DataSource datasource) {
        try (Connection connection = datasource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM account where id = 1");
             ResultSet resultSet = ps.executeQuery()) {
            resultSet.next();
            return resultSet.getDouble("money");
        } catch (Exception ignored) {
        }
        return 0.0;
    }
}

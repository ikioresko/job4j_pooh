package ru.job4j.integration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class OrdersStoreTest {
    private BasicDataSource pool = new BasicDataSource();
    private OrdersStore store = new OrdersStore(pool);

    @Before
    public void setUp() throws SQLException {
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("user");
        pool.setPassword("");
        pool.setMaxTotal(5);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("./db/update_001.sql")))
        ) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
    }

    @After
    public void clear() throws SQLException {
        store.dropTable();
    }

    @Test
    public void whenSaveOrderAndFindAllOneRowWithDescription() {
        store.save(Order.of("name1", "description1"));
        List<Order> all = (List<Order>) store.findAll();
        assertThat(all.size(), is(1));
        assertThat(all.get(0).getDescription(), is("description1"));
        assertThat(all.get(0).getId(), is(1));
    }

    @Test
    public void whenSaveOrderAndUpdate() {
        store.save(Order.of("name1", "description1"));
        store.update(1, Order.of("new Name", "new description"));
        List<Order> all = (List<Order>) store.findAll();
        assertThat(all.size(), is(1));
        assertThat(all.get(0).getName(), is("new Name"));
        assertThat(all.get(0).getDescription(), is("new description"));
        assertThat(all.get(0).getId(), is(1));
    }

    @Test
    public void whenSaveOrderAndFindByName() {
        store.save(Order.of("name1", "description1"));
        Order order = store.findByName("name1");
        assertThat(order.getDescription(), is("description1"));
    }

    @Test
    public void whenSaveOrderAndFindById() {
        store.save(Order.of("name1", "description1"));
        Order order = store.findById(1);
        assertThat(order.getName(), is("name1"));
    }
}

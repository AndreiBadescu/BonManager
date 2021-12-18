package com.bonmanager;

import org.junit.Test;

import static org.junit.Assert.*;

import android.content.Context;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Arrays;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private final String NUME_COMERCIANT_FIELD = "numeComerciant";
    private final String CIF_FIELD = "cif";
    private final String DATA_FIELD = "data";
    private final String ORA_FIELD = "ora";
    private final String TVA_FIELD = "tva";
    private final String TOTAL_FIELD = "total";
    private final String PRODUSE_FIELD = "produse";
    private final String PRETURI_FIELD = "preturi";
    private final String ID_FIELD = "id";
    private final String LAST_ID_FIELD = "last_id";

    private Connection connection;
    private String username;
    private String password;
    private String ip;
    private String port;
    private String database;

    private final String CONNECTION_FIELD = "connection";
    private final String USERNAME_FIELD = "username";
    private final String PASSWORD_FIELD = "password";
    private final String IP_FIELD = "ip";
    private final String PORT_FIELD = "port";
    private final String DATABASE_FIELD = "database";

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void receipt_hasAllFields() {
        Field[] allFields = Receipt.class.getDeclaredFields();

        assertEquals(10, allFields.length);

        assertTrue(Arrays.stream(allFields).anyMatch(field ->
                field.getName().equals(NUME_COMERCIANT_FIELD)
                        && field.getType().equals(String.class))
        );
        assertTrue(Arrays.stream(allFields).anyMatch(field ->
                field.getName().equals(CIF_FIELD)
                        && field.getType().equals(String.class))
        );
        assertTrue(Arrays.stream(allFields).anyMatch(field ->
                field.getName().equals(DATA_FIELD)
                        && field.getType().equals(String.class))
        );
        assertTrue(Arrays.stream(allFields).anyMatch(field ->
                field.getName().equals(ORA_FIELD)
                        && field.getType().equals(String.class))
        );
        assertTrue(Arrays.stream(allFields).anyMatch(field ->
                field.getName().equals(TVA_FIELD)
                        && field.getType().equals(String.class))
        );
        assertTrue(Arrays.stream(allFields).anyMatch(field ->
                field.getName().equals(TOTAL_FIELD)
                        && field.getType().equals(String.class))
        );
        assertTrue(Arrays.stream(allFields).anyMatch(field ->
                field.getName().equals(PRODUSE_FIELD)
                        && field.getType().equals(String.class))
        );
        assertTrue(Arrays.stream(allFields).anyMatch(field ->
                field.getName().equals(PRETURI_FIELD)
                        && field.getType().equals(String.class))
        );
        assertTrue(Arrays.stream(allFields).anyMatch(field ->
                field.getName().equals(ID_FIELD)
                        && field.getType().equals(String.class))
        );
        assertTrue(Arrays.stream(allFields).anyMatch(field ->
                field.getName().equals(LAST_ID_FIELD)
                        && field.getType().equals(String.class))
        );
    }

    @Test
    public void connectionhelper_hasAllFields() {
        Field[] allFields = ConnectionHelper.class.getDeclaredFields();

        assertEquals(6, allFields.length);

        assertTrue(Arrays.stream(allFields).anyMatch(field ->
                field.getName().equals(CONNECTION_FIELD)
                        && field.getType().equals(String.class))
        );
        assertTrue(Arrays.stream(allFields).anyMatch(field ->
                field.getName().equals(USERNAME_FIELD)
                        && field.getType().equals(String.class))
        );
        assertTrue(Arrays.stream(allFields).anyMatch(field ->
                field.getName().equals(PASSWORD_FIELD)
                        && field.getType().equals(String.class))
        );
        assertTrue(Arrays.stream(allFields).anyMatch(field ->
                field.getName().equals(IP_FIELD)
                        && field.getType().equals(String.class))
        );
        assertTrue(Arrays.stream(allFields).anyMatch(field ->
                field.getName().equals(PORT_FIELD)
                        && field.getType().equals(String.class))
        );
        assertTrue(Arrays.stream(allFields).anyMatch(field ->
                field.getName().equals(DATABASE_FIELD)
                        && field.getType().equals(String.class))
        );
    }

    @Test
    public void useAppContext() {
        assertEquals("ExampleUnitTest", getClass().getName());
    }
}
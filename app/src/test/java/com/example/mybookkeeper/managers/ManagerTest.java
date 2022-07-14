package com.example.mybookkeeper.managers;

import com.example.mybookkeeper.data.samis.SbitKenyaLedgerApi;
import com.fasterxml.jackson.core.JsonProcessingException;

import junit.framework.TestCase;

import org.junit.Test;

public class ManagerTest extends TestCase {

    @Test
    public void testSerializastion() throws Exception{
        String s = "{\"0\":\"1\",\"ManagerId\":\"1\",\"1\":\"Karua Muthama\",\"ManagerName\":\"Karua Muthama\",\"2\":\"0724895791\",\"ManagerPhone\":\"0724895791\",\"3\":\"1111\",\"ManagerPword\":\"1111\"}";
        try {
            SbitKenyaLedgerApi.mapper.readValue(s, Manager.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
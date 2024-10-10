package io.micronaut.guides.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CloudTest {

    @Test
    void testGetName() {
        assertEquals("Oracle Cloud", Cloud.OCI.getName());
        assertEquals("Amazon Web Services", Cloud.AWS.getName());
        assertEquals("Microsoft Azure", Cloud.AZURE.getName());
        assertEquals("Google Cloud Platform", Cloud.GCP.getName());
    }

    @Test
    void testGetAccronym() {
        assertEquals("OCI", Cloud.OCI.getAccronym());
        assertEquals("AWS", Cloud.AWS.getAccronym());
        assertEquals("Azure", Cloud.AZURE.getAccronym());
        assertEquals("GCP", Cloud.GCP.getAccronym());
    }

    @Test
    void testToString() {
        assertEquals("OCI", Cloud.OCI.toString());
        assertEquals("AWS", Cloud.AWS.toString());
        assertEquals("Azure", Cloud.AZURE.toString());
        assertEquals("GCP", Cloud.GCP.toString());
    }

    @Test
    void testGetOrder() {
        assertEquals(1, Cloud.OCI.getOrder());
        assertEquals(2, Cloud.AWS.getOrder());
        assertEquals(3, Cloud.AZURE.getOrder());
        assertEquals(4, Cloud.GCP.getOrder());
    }
}
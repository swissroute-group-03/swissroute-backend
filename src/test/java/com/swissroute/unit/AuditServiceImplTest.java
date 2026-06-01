package com.swissroute.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.swissroute.service.AuditServiceImpl;

import jakarta.servlet.http.HttpServletRequest;

class AuditServiceImplTest {

    private HttpServletRequest request;
    private AuditServiceImpl auditService;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        auditService = new AuditServiceImpl(request);
    }

    @Test
    void getUser_ShouldReturnEmailFromHeader() {
        String expectedEmail = "test@example.com";
        when(request.getHeader("X-User-Email")).thenReturn(expectedEmail);

        String result = auditService.getUser();

        assertEquals(expectedEmail, result);
    }

    @Test
    void getIpAddress_ShouldReturnRemoteAddr() {
        String expectedIp = "192.168.1.1";
        when(request.getRemoteAddr()).thenReturn(expectedIp);

        String result = auditService.getIpAddress();

        assertEquals(expectedIp, result);
    }
}

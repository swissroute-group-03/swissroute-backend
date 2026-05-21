package com.swissroute.service;

import org.springframework.stereotype.Service;

import com.swissroute.service.use_cases.AuditUseCase;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuditServiceImpl implements AuditUseCase{

    private final HttpServletRequest request;

    public AuditServiceImpl(HttpServletRequest request){
        this.request = request;
    }

    @Override
    public String getUser() {
        return request.getHeader("X-User-Email");
    }

    @Override
    public String getIpAddress() {
        return request.getRemoteAddr();
    }

}

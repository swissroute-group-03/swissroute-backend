package com.swissroute.audit;

import com.swissroute.constant.EstadoConstants;
import com.swissroute.service.use_cases.AuditUseCase;
import com.swissroute.util.BeanUtils;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class AuditListener {

    private AuditUseCase getAuditService() {
        return BeanUtils.getBean(AuditUseCase.class);
    }

    @PrePersist
    public void setCreated(Object entity) {
        if (entity instanceof ModelAudit audit) {
            AuditUseCase auditUseCase = getAuditService();
            audit.setUserCreated(auditUseCase.getUser());
            audit.setIpCreated(auditUseCase.getIpAddress());
            audit.setFlgState(EstadoConstants.ACTIVE);
        }
    }

    @PreUpdate
    public void setUpdated(Object entity) {
        if (entity instanceof ModelAudit audit) {
            AuditUseCase auditUseCase = getAuditService();
            audit.setUserUpdated(auditUseCase.getUser());
            audit.setIpUpdated(auditUseCase.getIpAddress());
        }
    }
}

package com.swissroute.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.swissroute.dto.response.RoleResponseDTO;
import com.swissroute.mapper.RoleMapper;
import com.swissroute.repository.RoleRepository;
import com.swissroute.service.use_cases.RoleUseCase;


@Service
public class RoleServiceImpl implements RoleUseCase {

    private RoleMapper roleMapper;

    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleMapper roleMapper, RoleRepository roleRepository){
        this.roleMapper = roleMapper;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleResponseDTO> getRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toDto)
                .toList();
    }

}

package com.swissroute.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.swissroute.dto.response.RoleResponseDTO;
import com.swissroute.mapper.RoleMapper;
import com.swissroute.model.Role;
import com.swissroute.repository.RoleRepository;
import com.swissroute.service.RoleServiceImpl;

class RoleServiceImplTest {

    private RoleMapper roleMapper;
    private RoleRepository roleRepository;
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        roleMapper = mock(RoleMapper.class);
        roleRepository = mock(RoleRepository.class);
        roleService = new RoleServiceImpl(roleMapper, roleRepository);
    }

    @Test
    void getRoles_ShouldReturnListOfRoles() {
        Role role = new Role();
        role.setId(1L);
        role.setName("VISITANTE");

        RoleResponseDTO dto = new RoleResponseDTO();
        dto.setId(1L);
        dto.setName("VISITANTE");

        when(roleRepository.findAll()).thenReturn(List.of(role));
        when(roleMapper.toDto(role)).thenReturn(dto);

        List<RoleResponseDTO> result = roleService.getRoles();

        assertEquals(1, result.size());
        assertEquals("VISITANTE", result.get(0).getName());
    }

    @Test
    void getRoles_ShouldReturnEmptyList_WhenNoRolesExist() {
        when(roleRepository.findAll()).thenReturn(List.of());

        List<RoleResponseDTO> result = roleService.getRoles();

        assertTrue(result.isEmpty());
    }
}

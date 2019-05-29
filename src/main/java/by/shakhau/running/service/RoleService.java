package by.shakhau.running.service;

import by.shakhau.running.service.dto.Role;

public interface RoleService {

    Role findByName(String name);
}

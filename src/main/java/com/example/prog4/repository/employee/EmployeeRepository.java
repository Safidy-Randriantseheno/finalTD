package com.example.prog4.repository.employee;

import com.example.prog4.repository.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("employeeTransactionManager")
public interface EmployeeRepository extends JpaRepository<Employee, String> {

}

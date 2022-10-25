package com.example.securingweb.repository;

import com.example.securingweb.model.Task;
import com.example.securingweb.model.User;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends DataTablesRepository<Task, Long> {
}

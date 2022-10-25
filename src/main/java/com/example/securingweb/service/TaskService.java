package com.example.securingweb.service;

import com.example.securingweb.finder.TaskFinder;
import com.example.securingweb.model.Task;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {
    public List<Task> findAll();
    public List<Task> findByCriteria(TaskFinder finder);
    public boolean save(Task data);

    public DataTablesOutput<Task> findForDataTable(DataTablesInput input);
}


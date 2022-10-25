package com.example.securingweb.controller;

import com.example.securingweb.finder.TaskFinder;
import com.example.securingweb.model.Task;
import com.example.securingweb.repository.TaskRepository;
import com.example.securingweb.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.expression.ExpressionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(path="/task")
@RestController
class EmployeeController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/GetTaskList")
    List<Task> GetTaskList() {
        return (List<Task>) taskRepository.findAll();
    }

    @PutMapping("/newTask")
    Task newTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    @GetMapping("/GetTask/{id}")
    Task GetTask(@PathVariable Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> null);
    }

    @PostMapping("/updateTask/{id}")
    Task updateTask(@RequestBody Task newTask, @PathVariable Long id) {

        return taskRepository.findById(id)
                .map(task -> {
                    task.setName(newTask.getName());
                    task.setDescription(newTask.getDescription());
                    task.setStatus(newTask.getStatus());
                    return taskRepository.save(task);
                })
                .orElseGet(() -> {
                    newTask.setId(id);
                    return taskRepository.save(newTask);
                });
    }

    @DeleteMapping("/deleteTask/{id}")
    void deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
    }
}

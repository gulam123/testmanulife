package com.example.securingweb.serviceImpl;

import com.example.securingweb.finder.TaskFinder;
import com.example.securingweb.model.Task;
import com.example.securingweb.repository.TaskRepository;
import com.example.securingweb.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    TaskRepository taskRepository;

    @Override
    public List<Task> findAll() {
        return (List<Task>) taskRepository.findAll();
    }

    @Override
    public List<Task> findByCriteria(TaskFinder finder) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> criteriaQuery = criteriaBuilder.createQuery(Task.class);

        Root<Task> tbl = criteriaQuery.from(Task.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        Predicate predicate = null;

        if(finder.getId() != null) {
            predicate = criteriaBuilder.equal(tbl.get("id"), finder.getId());
            predicates.add(predicate);
        }

        criteriaQuery.where(predicates.toArray(new Predicate[] {}));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public DataTablesOutput<Task> findForDataTable(DataTablesInput input) {
        return taskRepository.findAll(input);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public boolean save(Task data) {
        // TODO Auto-generated method stub
        try {
            taskRepository.save(data);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}

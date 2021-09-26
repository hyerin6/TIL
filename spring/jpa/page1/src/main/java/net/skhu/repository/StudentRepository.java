package net.skhu.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.domain.Student;
import net.skhu.model.Pagination;

public interface StudentRepository  extends JpaRepository<Student, Integer> {

    public Page<Student> findAll(Pageable pageable);
    public Page<Student> findByDepartmentId(int departmentId, Pageable pageable);

    // 아래 두 메소드는 Service 클래스에 구현하는 것이 더 바람직할 수 있음.
    public default List<Student> findAll(Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPg() - 1, pagination.getSz(),
                Sort.Direction.ASC, "studentNo");
        Page<Student> page = findAll(pageable);
        pagination.setRecordCount((int)page.getTotalElements());
        return page.getContent();
    }

    public default List<Student> findByDepartmentId(Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPg() - 1, pagination.getSz(),
                Sort.Direction.ASC, "studentNo");
        Page<Student> page = findByDepartmentId(pagination.getDi(), pageable);
        pagination.setRecordCount((int)page.getTotalElements());
        return page.getContent();
    }

}
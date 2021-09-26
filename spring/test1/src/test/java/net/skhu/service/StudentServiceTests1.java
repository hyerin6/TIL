package net.skhu.service;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import net.skhu.dto.Student;
import net.skhu.mapper.StudentMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class StudentServiceTests1 {

    @Autowired StudentService studentService;
    @Autowired StudentMapper studentMapper;

    // student 테이블에서 중복되지 않는 새 학번(studentNumber)을 생성하여 리턴한다
    String newStudentNumber() {
        for (int i = 201032000; i < 999999999; ++i) {
            String s = String.valueOf(i);
            if (studentMapper.findByStudentNumber(s) == null)
                return s;
        }
        return null;
    }

    // 학번 중복의 경우 insert 실패 테스트
    @Test(expected=InvalidDataException.class)
    public void test_insert_error() throws Exception {
        Student student = studentMapper.findAll().get(0);
        studentService.insert(student);
    }

    // insert 성공 테스트
    @Test
    public void test_insert_success() throws InvalidDataException {
        String studentNumber = newStudentNumber();
        Student student1 = new Student();
        student1.setStudentNumber(studentNumber);
        student1.setName("임꺽정");
        student1.setDepartmentId(2);
        student1.setYear(3);
        studentService.insert(student1);

        Student student2 = studentMapper.findByStudentNumber(studentNumber);
        assertEquals(student1, student2);
    }

}

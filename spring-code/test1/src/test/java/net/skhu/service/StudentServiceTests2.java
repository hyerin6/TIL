package net.skhu.service;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.skhu.dto.Student;
import net.skhu.mapper.StudentMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentServiceTests2 {

    @Mock
    StudentMapper studentMapper;

    @InjectMocks
    StudentService studentService;

    String studentNumber = "201132011";
    Student student;
    List<Student> students;

    public StudentServiceTests2() {
        this.student = new Student();
        this.student.setId(337);
        this.student.setStudentNumber("201132011");
        this.student.setName("임꺽정");
        this.student.setDepartmentId(2);
        this.student.setYear(3);

        this.students = new ArrayList<Student>();
        this.students.add(this.student);
    }

    // findById 테스트
    @Test
    public void test_findById() {
        // 테스트 하기 위해서 mock 객체를 설정함.
        Mockito.when(studentMapper.findById(ArgumentMatchers.anyInt()))
                .thenReturn(this.student);

        Student student2 = studentService.findById(this.student.getId());

        // 테스트 결과 확인
        assertEquals(this.student, student2);
        Mockito.verify(studentMapper).findById(this.student.getId());

    }

    // findAll 테스트
    @Test
    public void test_findAll() {
        // 테스트 하기 위해서 mock 객체를 설정함.
        Mockito.when(studentMapper.findAll())
                .thenReturn(this.students);

        List<Student> students2 = studentService.findAll();

        // 테스트 결과 확인
        assertEquals(this.students, students2);
        Mockito.verify(studentMapper).findAll();
    }

    // 학번 중복의 경우 insert 실패 테스트
    @Test(expected = InvalidDataException.class)
    public void test_insert_error() throws Exception {
        // 테스트 하기 위해서 mock 객체를 설정함.
        Mockito.when(studentMapper.findByStudentNumber(ArgumentMatchers.anyString()))
                .thenReturn(this.student);

        studentService.insert(this.student);

        // 테스트 결과 확인
        Mockito.verify(studentMapper).findByStudentNumber(this.studentNumber);
        Mockito.verify(studentMapper, Mockito.times(0)).insert(this.student);
    }

    // insert 성공 테스트
    @Test
    public void test_insert_success() throws InvalidDataException {
        // 테스트 하기 위해서 mock 객체를 설정함.
        Mockito.when(studentMapper.findByStudentNumber(ArgumentMatchers.anyString()))
                .thenReturn(null);

        studentService.insert(this.student);

        // 테스트 결과 확인
        Mockito.verify(studentMapper).findByStudentNumber(this.studentNumber);
        Mockito.verify(studentMapper).insert(this.student);
    }
}

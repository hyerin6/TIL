package net.skhu.service;

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
public class StudentServiceTests3 {

    @Mock
    StudentMapper studentMapper;

    @InjectMocks
    StudentService studentService;

    @Mock
    Student student; // Student 목 객체 생성

    // 학번 중복의 경우 insert 실패 테스트
    @Test(expected = InvalidDataException.class)
    public void test_insert_error() throws Exception {
        final String studentNumber = "201132011";

        // 테스트 하기 위해서 mock 객체를 설정함.
        Mockito.when(student.getStudentNumber())
                .thenReturn(studentNumber);
        Mockito.when(studentMapper.findByStudentNumber(ArgumentMatchers.anyString()))
                .thenReturn(this.student);

        studentService.insert(this.student);

        // 테스트 결과 확인
        Mockito.verify(studentMapper).findByStudentNumber(studentNumber);
        Mockito.verify(student).getStudentNumber();
        Mockito.verify(studentMapper, Mockito.times(0)).insert(this.student);
    }
}

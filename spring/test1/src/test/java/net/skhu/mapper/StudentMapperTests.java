package net.skhu.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import net.skhu.dto.Student;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class StudentMapperTests {

    @Autowired
    StudentMapper studentMapper;

    // student 테이블에서 중복되지 않는 새 학번(studentNumber)을 생성하여 리턴한다
    String newStudentNumber() {
        for (int i = 201032000; i < 999999999; ++i) {
            String s = String.valueOf(i);
            if (studentMapper.findByStudentNumber(s) == null)
                return s;
        }
        return null;
    }

    @Test
    public void test_findAll_findById() {
        // 모든 레코드 조회
        List<Student> list = studentMapper.findAll();

        for (Student student1 : list) {
            // findById로 레코드 다시 조회
            Student student2 = studentMapper.findById(student1.getId());

            // 동일한 레코드이므로 값도 동일해야 함
            assertEquals(student1, student2);
        }
    }

    @Test
    public void test_findByStudentNumber() {
        // 모든 레코드 조회
        List<Student> list = studentMapper.findAll();
        Student student1 = list.get(0);

        // findByStudentNumber로 레코드 다시 조회
        Student student2 = studentMapper.findByStudentNumber(student1.getStudentNumber());

        // 동일한 레코드이므로 값도 동일해야 함
        assertEquals(student1, student2);
    }

    @Test
    public void test_insert() {
        // 새 레코드 객체 생성
        Student student1 = new Student();
        student1.setStudentNumber(newStudentNumber());
        student1.setName("임꺽정");
        student1.setDepartmentId(2);
        student1.setYear(3);

        // 저장
        studentMapper.insert(student1);

        // 잘 저장되었는지 확인
        Student student2 = studentMapper.findById(student1.getId());
        assertEquals(student1, student2);
    }

    @Test
    public void test_update() {
        // 첫번째 레코드
        Student student1 = studentMapper.findAll().get(0);

        // 첫번째 레코드의 모든 멤버 변수 값을 수정한다.
        // 단 id는 제외
        student1.setStudentNumber(newStudentNumber());
        student1.setName("임꺽정");
        student1.setDepartmentId(2);
        student1.setYear(3);

        // 저장
        studentMapper.update(student1);

        // 잘 저장되었는지 확인
        Student student2 = studentMapper.findById(student1.getId());
        assertEquals(student1, student2);

        // 다시 값 수정
        student1.setStudentNumber(newStudentNumber());
        student1.setName("홍길동");
        student1.setDepartmentId(3);
        student1.setYear(4);

        // 저장
        studentMapper.update(student1);

        // 잘 저장되었는지 확인
        student2 = studentMapper.findById(student1.getId());
        assertEquals(student1, student2);
    }

    @Test
    public void test_deleteById() {
        // 모든 레코드 조회
        List<Student> list = studentMapper.findAll();
        assertTrue(list.size() > 0);

        // 모든 레코드 삭제
        for (Student student : list)
            studentMapper.deleteById(student.getId());

        // 모든 레코드를 삭제했으므로 레코드 수는 0 이어야 한다.
        list = studentMapper.findAll();
        assertTrue(list.size() == 0);
    }
}

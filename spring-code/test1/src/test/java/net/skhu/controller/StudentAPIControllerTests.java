package net.skhu.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import net.skhu.dto.Student;
import net.skhu.service.InvalidDataException;
import net.skhu.service.StudentService;
import net.skhu.utils.JsonUtils;

@RunWith(SpringRunner.class)
@WebMvcTest(StudentAPIController.class) // 테스트할 컨트롤러 클래스를 명시해야 함.
public class StudentAPIControllerTests {

    // 액션 메소드를 호출하기 위한 객체
    @Autowired private MockMvc mvc;

    // 테스트용 가짜 StudentService 객체
    @MockBean private StudentService studentService;

    // 테스트에 사용할 데이터
    Student student;
    List<Student> students;

    public StudentAPIControllerTests() {
        this.student = new Student();
        this.student.setId(337);
        this.student.setStudentNumber("201132011");
        this.student.setName("임꺽정");
        this.student.setDepartmentId(2);
        this.student.setYear(3);

        this.students = new ArrayList<Student>();
        this.students.add(this.student);
    }

    @Test
    public void test_students() throws Exception {
        // 테스트 하기 위해서 mock 객체를 설정함.
        Mockito.when(studentService.findAll()) // studentService.findAll() 메소드가 호출되면,
                .thenReturn(this.students); // this.students 객체를 리턴하라

        // GET 방식으로 "/api/students" URL을 요청한다.
        // 액션 메소드가 리턴하는 json 데이터를 받는다
        ResultActions result = mvc.perform(
                get("/api/students").contentType(MediaType.APPLICATION_JSON));

        // 서버에서 받아온 json 데이터 확인
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(this.students.size())))  // 리턴된 목록 크기 확인
                .andExpect(jsonPath("$[0].id", is(this.student.getId()))) // 목록에서 첫번째 객체의 id값 확인
                .andExpect(jsonPath("$[0].name", is(this.student.getName())))
                .andExpect(jsonPath("$[0].studentNumber", is(this.student.getStudentNumber())));

        // StudentSerivce 메소드가 호출되었음을 확인
        Mockito.verify(studentService).findAll();
    }

    @Test
    public void test_student() throws Exception {
        // 테스트 하기 위해서 mock 객체를 설정함.
        Mockito.when(studentService.findById(ArgumentMatchers.anyInt()))
                .thenReturn(this.student);
        // studentService.findById(..) 메소드가 호출되면
        // this.student 객체를 리턴하라

        // GET 방식으로 "/api/student/337" URL을 요청한다.
        // 액션 메소드가 리턴하는 json 데이터를 받는다
        ResultActions result = mvc.perform(
                get("/api/student/" + this.student.getId())
                        .contentType(MediaType.APPLICATION_JSON));

        // 서버에서 받아온 json 데이터 확인
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(this.student.getId()))) // 리턴된 객체의 id값 확인
                .andExpect(jsonPath("$.name", is(this.student.getName())))
                .andExpect(jsonPath("$.studentNumber", is(this.student.getStudentNumber())));

        // StudentSerivce 메소드가 호출되었음을 확인
        Mockito.verify(studentService).findById(this.student.getId());
    }

    @Test
    public void test_student_post_success() throws Exception {
        // 테스트 하기 위해서 mock 객체를 설정함.
        Mockito.doNothing().when(studentService).insert(ArgumentMatchers.any());
        // studentService.insert(...) 메소드가 호출되면,
        // 아무것도 하지 않도록 설정함

        // POST 방식으로 "/api/student" URL을 요청한다.
        // 이때 this.student 객체를 JSON 형식으로 서버에 전송함.
        // 액션 메소드가 리턴하는 json 데이터를 받는다
        ResultActions result = mvc.perform(
                post("/api/student/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(this.student)));

        // 서버에서 받아온 json 데이터 확인
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        // StudentSerivce 메소드가 호출되었음을 확인
        Mockito.verify(studentService).insert(this.student);
    }

    @Test
    public void test_student_post_error() throws Exception {
        // 테스트 하기 위해서 mock 객체를 설정함.
        String errorMessage = "학번중복";
        Mockito.doThrow(new InvalidDataException(errorMessage))
                .when(studentService).insert(ArgumentMatchers.any());
        // studentService.insert(...) 메소드가 호출되면,
        // InvalidDataException을 throw하도록 설정함.


        // POST 방식으로 "/api/student/" URL을 요청함.
        // 이때 this.student 객체를 JSON 형식으로 서버에 전송함.
        // 액션 메소드가 리턴하는 json 데이터를 받는다
        ResultActions result = mvc.perform(
                post("/api/student/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(this.student)));

        // 서버에서 받아온 json 데이터 확인
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", is(errorMessage)));

        // StudentSerivce 메소드가 호출되었음을 확인
        Mockito.verify(studentService).insert(this.student);
    }
}

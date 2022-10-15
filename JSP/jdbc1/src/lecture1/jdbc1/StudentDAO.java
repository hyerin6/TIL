package lecture1.jdbc1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import lecture1.DB;

public class StudentDAO {

	public static List<Student> findAll() throws Exception {
		String sql = "SELECT s.*, d.departmentName " + // 중간에 공벡 넣기 주의
				"FROM student s LEFT JOIN department d ON s.departmentId = d.id";
		// try with resource : 마지막에 세미콜론(;) 있으면 안됨.
		// Connection 도 꼭 close 해줘야함!
		try (Connection connection = DB.getConnection("student1"); // DB 이름 주의
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {
			ArrayList<Student> list = new ArrayList<Student>();
			while (resultSet.next()) {
				Student student = new Student();
				student.setId(resultSet.getInt("id"));
				student.setStudentNumber(resultSet.getString("studentNumber"));
				student.setName(resultSet.getString("name"));
				student.setDepartmentId(resultSet.getInt("departmentId"));
				student.setYear(resultSet.getInt("year"));
				student.setDepartmentName(resultSet.getString("departmentName"));
				list.add(student);
			}
			return list; // return type이 List이다.
			// jsp 파일에서 findAll() 호출 시, 기억하고 있어야 한다. (다형성)
		}
	}
}

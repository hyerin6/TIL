package lecture1.jdbc2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import lecture1.DB;

public class StudentDAO {

    public static List<Student> findAll(int currentPage, int pageSize) throws Exception {
        String sql = "SELECT s.*, d.departmentName" +
                     " FROM student s LEFT JOIN department d ON s.departmentId = d.id" +
                     " LIMIT ?, ?";
        try (Connection connection = DB.getConnection("student1");
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, (currentPage - 1) * pageSize);
            statement.setInt(2, pageSize);
            try (ResultSet resultSet = statement.executeQuery()) {
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
                return list;
            }
        }
    }

    public static int count() throws Exception {
        String sql = "SELECT COUNT(*) FROM student";
        try (Connection connection = DB.getConnection("student1");
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) // 하나 가져오는 것이므로 if문
                    return resultSet.getInt(1);
        }
        return 0;
    }
}
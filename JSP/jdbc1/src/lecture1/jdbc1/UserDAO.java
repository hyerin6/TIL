package lecture1.jdbc1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import lecture1.DB;

public class UserDAO {

	public static List<User> findAll() throws Exception {
		String sql = "SELECT u.*, d.departmentName " +
				"FROM user u LEFT JOIN department d ON u.departmentId = d.id";

		try (Connection connection = DB.getConnection("student1");
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {
			ArrayList<User> list = new ArrayList<User>();
			while (resultSet.next()) {
				User student = new User();
				student.setUserid(resultSet.getString("userid"));
				student.setName(resultSet.getString("name"));
				student.setEmail(resultSet.getString("email"));
				student.setDepartmentName(resultSet.getString("departmentName"));
				student.setUserType(resultSet.getString("userType"));
				student.setEnabled(resultSet.getBoolean("enabled"));
				list.add(student);
			}
			return list;
		}
	}
}
package net.skhu;

public class Student {

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    String studentId;
    String studentName;


    public Student(String studentId, String studentName){
        this.studentId = studentId;
        this.studentName = studentName;
    }

}

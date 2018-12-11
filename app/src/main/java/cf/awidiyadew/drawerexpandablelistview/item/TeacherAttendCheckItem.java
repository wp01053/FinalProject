package cf.awidiyadew.drawerexpandablelistview.item;


public class TeacherAttendCheckItem {

    private String studentName;
    private String studentMac;
    private int attendCheck;

    public TeacherAttendCheckItem(String studentName, String studentMac, int attendCheck) {
        this.studentName = studentName;
        this.studentMac = studentMac;
        this.attendCheck = attendCheck;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentMac() {
        return studentMac;
    }

    public void setStudentMac(String studentMac) {
        this.studentMac = studentMac;
    }

    public int getAttendCheck() {
        return attendCheck;
    }

    public void setAttendCheck(int attendCheck) {
        this.attendCheck = attendCheck;
    }
}
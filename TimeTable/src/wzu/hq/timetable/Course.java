package wzu.hq.timetable;

public class Course {

	String grade;//�꼶
	String major;
	String courseName;//�γ�����
	String courseTeacher;//�γ̽�ʦ
	String coursePlace;//�Ͽεص�
	int startWeek,endWeek;//��ʼ�Ͽ��ܣ������Ͽ���
	int startClass;//�ڼ����Ͽ�
	int endClass;//�ڼ����¿�
	String weekOddEven;//��˫��"��"��˫����null��
	int weekday;
	String path;

	public Course(String grade,	String major,String courseName,String courseTeacher,String coursePlace,int startWeek,int endWeek,int startClass,int endClass,String weekOddEven,int weekday,String path) {
		// TODO Auto-generated constructor stub
		this.grade=grade;
		this.major=major;
		this.courseName=courseName;
		this.courseTeacher=courseTeacher;
		this.coursePlace=coursePlace;
		this.startWeek=startWeek;
		this.endWeek=endWeek;
		this.startClass=startClass;
		this.endClass=endClass;
		this.weekOddEven=weekOddEven;
		this.weekday=weekday;
		this.path=path;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseTeacher() {
		return courseTeacher;
	}

	public void setCourseTeacher(String courseTeacher) {
		this.courseTeacher = courseTeacher;
	}

	public String getCoursePlace() {
		return coursePlace;
	}

	public void setCoursePlace(String coursePlace) {
		this.coursePlace = coursePlace;
	}

	public int getStartWeek() {
		return startWeek;
	}

	public void setStartWeek(int startWeek) {
		this.startWeek = startWeek;
	}

	public int getEndWeek() {
		return endWeek;
	}

	public void setEndWeek(int endWeek) {
		this.endWeek = endWeek;
	}

	public int getStartClass() {
		return startClass;
	}

	public void setStartClass(int startClass) {
		this.startClass = startClass;
	}

	public int getEndClass() {
		return endClass;
	}

	public void setEndClass(int endClass) {
		this.endClass = endClass;
	}

	public String getWeekOddEven() {
		return weekOddEven;
	}

	public void setWeekOddEven(String weekOddEven) {
		this.weekOddEven = weekOddEven;
	}

	public int getWeekday() {
		return weekday;
	}

	public void setWeekday(int weekday) {
		this.weekday = weekday;
	}
	
	
}

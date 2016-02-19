package wzu.hq.timetable;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 1;//数据库的版本
	private static final String DICTIONARY_TABLE_NAME = "course";//数据库的名字
	
	private static final String DICTIONARY_TABLE_CREATE ="CREATE TABLE if not exists course (_id INTEGER PRIMARY KEY AUTOINCREMENT, Grade VARCHAR(64), Major VARCHAR(64),CourseName VARCHAR(160),CourseTeacher VARCHAR(32),CoursePlace VARCHAR(32),StartWeek int,EndWeek int ,startClass int,endClass int,weekOddEven VARCHAR(16),weekday int,path VARCHAR(60));";
	
	
	//table的列名
	public static final String Grade="Grade";
	public static final String Major="Major";
	public static final String CourseName="CourseName";
	public static final String CourseTeacher="CourseTeacher";
	public static final String CoursePlace="CoursePlace";
	public static final String StartWeek="StartWeek";
	public static final String EndWeek="EndWeek";	
	public static final String startClass="startClass";
	public static final String endClass="endClass";
	public static final String OddEveneek="weekOddEven";
	public static final String WeekDay="WeekDay";
	
	public DatabaseHelper(Context context) {
		super(context, DICTIONARY_TABLE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		arg0.execSQL(DICTIONARY_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		arg0.execSQL("DROP TABLE IF EXISTS course");//删除表
		onCreate(arg0);//这里没有更新，只是调用onCreate重新建了一下表
	}

}

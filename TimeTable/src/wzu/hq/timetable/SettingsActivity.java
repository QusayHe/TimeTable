package wzu.hq.timetable;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	String path;

	ArrayAdapter<String> sheetnameAdapter,majorAdapter ;
	ArrayList<String> majors, sheetNames;
	String sheetName,major;
	EditText et;
	Button bt1,bt2,bt3;
	Spinner sheetnameSpinner,majorSpinner;
	
	private SQLiteDatabase db=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);	

		sheetnameSpinner=(Spinner)findViewById(R.id.spinner1);	
		majorSpinner=(Spinner)findViewById(R.id.spinner2);
		bt1=(Button) findViewById(R.id.button1);
		bt2=(Button) findViewById(R.id.button2);
		bt3=(Button) findViewById(R.id.button3);
		et=(EditText) findViewById(R.id.path);
		db=(new DatabaseHelper(this)).getWritableDatabase();
		bt1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

//				SharedPreferences.Editor sharedata = getSharedPreferences("data", 0).edit();   
//				sharedata.putString("PATH",path); 
//				sharedata.putString("SHEETNAME",sheetName); 
//				sharedata.putString("MAJOR",major); 
				
				Intent intent=new Intent(SettingsActivity.this,MainActivity.class);
				intent.putExtra("PATH", path);
				intent.putExtra("SHEETNAME", sheetName);
				intent.putExtra("MAJOR", major);
				startActivity(intent);		
				setCourseInfoIntoDBFromExcel();
			}
		});

		bt2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SettingsActivity.this,MainActivity.class);
				intent.putExtra("PATH", path);
				intent.putExtra("SHEETNAME", sheetName);
				intent.putExtra("MAJOR", major);
				startActivity(intent);
				
			}
		});
		
		
		bt3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent fileIntent = new Intent(SettingsActivity.this,simpleFileExplore.class);
				startActivity(fileIntent);												
			}
		});
		
		
		//获取主界面传递的值
		Intent receiveIntent=getIntent();
		path=(String) receiveIntent.getSerializableExtra("PATH");
		sheetName=(String) receiveIntent.getSerializableExtra("SHEETNAME");
		major=(String) receiveIntent.getSerializableExtra("MAJOR");
		
		if(receiveIntent.getSerializableExtra("SELECTPATH")!=null)
		{
			path=(String) receiveIntent.getSerializableExtra("SELECTPATH");
		}
		et.setText(path);
		
		//获取选择的
		
		final RWExcel getGradeMajor =new RWExcel();
		majors=new ArrayList<String>();		
		sheetNames=new ArrayList<String>();
		sheetNames=getGradeMajor.GetgradesByReadExcel(path);
		
		sheetnameAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, sheetNames); 
		sheetnameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sheetnameSpinner.setAdapter(sheetnameAdapter);
		int sheetNameID=getID(sheetNames,sheetName);
		sheetnameSpinner.setSelection(sheetNameID);

		sheetnameSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				sheetName=sheetnameAdapter.getItem(arg2);
				majors=getGradeMajor.GetmajorsByReadExcel(path,sheetnameSpinner.getSelectedItem().toString());
				majorAdapter=new  ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_spinner_item, majors); 
				majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				majorSpinner.setAdapter(majorAdapter);
				int majorID=getID(majors,major);
				majorSpinner.setSelection(majorID);
				majorSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						major=majorAdapter.getItem(arg2);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		
	}
	
	//获取
	private int getID(ArrayList<String> sheetNames, String sheetName) {
		// TODO Auto-generated method stub
		int j=-1;
		for(int i=0;i<sheetNames.size();i++)
		{
			if(sheetNames.get(i).equals(sheetName))
				j=i;
		}
		return j;
	}
	
	
	public void setCourseInfoIntoDBFromExcel()//从excel获取信息保存到数据库
	{
		if(sheetName!=null && major!=null)
		{
			RWExcel rw=new RWExcel(sheetName,major);
			ArrayList<Course> courses=rw.GetCoursesByReadExcel(path);			

			db.execSQL("delete from course");
			for(int i=0;i<courses.size();i++)
			{
				Course c=courses.get(i);					
				processAdd(c);
			}

		}
	}
	
	public void processAdd(Course c)//增
	{			
		ContentValues cv=new ContentValues();
		cv.put("Grade", c.grade);
		cv.put("Major", c.major);
		cv.put("CourseName", c.courseName);
		cv.put("CourseTeacher", c.courseTeacher);
		cv.put("CoursePlace", c.coursePlace);
		cv.put("StartWeek", c.startWeek);
		cv.put("EndWeek", c.endWeek);
		cv.put("startClass", c.startClass);
		cv.put("endClass", c.endClass);	
		cv.put("weekOddEven", c.weekOddEven);	
		cv.put("weekday", c.weekday);
		cv.put("path", path);
		try{
			db.insert("course", "Grade", cv);
		}
		catch(Exception e){
			android.util.Log.w("database_errors", e.toString());	
		}	
	}
}

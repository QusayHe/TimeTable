package wzu.hq.timetable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class AddCourseActivity extends Activity{

	EditText etCourseName,etCourseTeacher,etCoursePlace,etCourseWeek,etCourseSection;
	Button btSave,btCancel,btDelete;
	
	String grade;
	String major;
	String courseName;
	String courseTeacher;
	String coursePlace;
	String weekOddEven;
	
	String courseTime;
	String courseWeek;
	String path;
	
	int startWeek;
	int endWeek;
	int startClass;	
	int endClass;		
	int weekday;	
	int id;
	
	private SQLiteDatabase db=null;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_details);

		etCourseName=(EditText) findViewById(R.id.editText1);		
		etCourseTeacher=(EditText) findViewById(R.id.editText2);	
		etCoursePlace=(EditText) findViewById(R.id.editText3);	
		etCourseSection=(EditText) findViewById(R.id.editText4);
		etCourseWeek=(EditText) findViewById(R.id.editText5);	
		btSave=(Button) findViewById(R.id.button1);
		btCancel=(Button) findViewById(R.id.button2);		
		btDelete=(Button) findViewById(R.id.button3);	
		
		btDelete.setVisibility(View.GONE);
		db=(new DatabaseHelper(this)).getWritableDatabase();
		
		Intent intent=getIntent();
		grade=(String) intent.getSerializableExtra("grade");
		major=(String) intent.getSerializableExtra("major");
		path=(String) intent.getSerializableExtra("path");
		
		etCourseSection.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LinearLayout mylayout=(LinearLayout) LayoutInflater.from(AddCourseActivity.this).inflate(R.layout.classtimepicker_layout, null,false);//从xml中动态生成布局视图
				final NumberPicker weekNP=(NumberPicker) mylayout.findViewById(R.id.weekdaypicker);
				final NumberPicker startclassNP=(NumberPicker) mylayout.findViewById(R.id.startclasspicker);	
				final NumberPicker endclassNP=(NumberPicker) mylayout.findViewById(R.id.endclasspicker);
				
				weekNP.setMaxValue(7);
				weekNP.setMinValue(1);
				
				startclassNP.setMaxValue(12);
				startclassNP.setMinValue(1);
				
				endclassNP.setMaxValue(12);
				endclassNP.setMinValue(startclassNP.getValue());
				
				new AlertDialog.Builder(AddCourseActivity.this)
				.setTitle("选择上课时间")
				.setView(mylayout)//setView可在警告框中设置LayoutInflater视图对象
				.setPositiveButton("确定",	new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int whichButton) {
						startClass=startclassNP.getValue();
						endClass=endclassNP.getValue();
						weekday=weekNP.getValue();
						etCourseSection.setText("周"+weekNP.getValue()+" "+startclassNP.getValue()+"-"+endclassNP.getValue());
					}
				})
				.setNegativeButton("取消",null)
				.show();//setView可在弹出对话框中设置中间主题部分显示的视图
			}
		});
		
		etCourseWeek.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final LinearLayout mylayout=(LinearLayout) LayoutInflater.from(AddCourseActivity.this).inflate(R.layout.classweek_layout, null,false);//从xml中动态生成布局视图
				final NumberPicker startweekNP=(NumberPicker) mylayout.findViewById(R.id.startweekpicker);
				final NumberPicker endweekNP=(NumberPicker) mylayout.findViewById(R.id.endweekpicker);	
				
				final RadioGroup RG=(RadioGroup) mylayout.findViewById(R.id.RG);
				
				startweekNP.setMaxValue(18);
				startweekNP.setMinValue(1);
				
				endweekNP.setMaxValue(18);
				endweekNP.setMinValue(startweekNP.getValue());
				
				new AlertDialog.Builder(AddCourseActivity.this)
				.setTitle("选择上课周")
				.setView(mylayout)//setView可在警告框中设置LayoutInflater视图对象
				.setPositiveButton("确定",	new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int whichButton) {
						RadioButton rb=(RadioButton) mylayout.findViewById(RG.getCheckedRadioButtonId());
						startWeek=startweekNP.getValue();
						endWeek=endweekNP.getValue();
						switch(RG.getCheckedRadioButtonId())
						{
							case R.id.radioButton1:
								weekOddEven="每";
								break;
							case R.id.radioButton2:
								weekOddEven="单";
								break;
							case R.id.radioButton3:
								weekOddEven="双";
								break;
						}		
						etCourseWeek.setText(startweekNP.getValue()+"-"+endweekNP.getValue()+" "+rb.getText());
					}
				})
				.setNegativeButton("取消",null)
				.show();//setView可在弹出对话框中设置中间主题部分显示的视图
			}
		});
		
		
		btSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				courseName=etCourseName.getText().toString();
				courseTeacher=etCourseTeacher.getText().toString();
				coursePlace=etCoursePlace.getText().toString();
				
				
				ContentValues cv=new ContentValues();
				cv.put("Grade", grade);
				cv.put("Major", major);
				cv.put("CourseName", courseName);
				cv.put("CourseTeacher", courseTeacher);
				cv.put("CoursePlace", coursePlace);
				cv.put("StartWeek", startWeek);
				cv.put("EndWeek", endWeek);
				cv.put("startClass", startClass);
				cv.put("endClass", endClass);	
				cv.put("weekOddEven",weekOddEven);	
				cv.put("weekday", weekday);
				cv.put("path", path);
				try{
					db.insert("course", "Grade", cv);
				}
				catch(Exception e){
					android.util.Log.w("database_errors", e.toString());	
				}	
				
				//返回MainActivity
				Intent intent=new Intent(AddCourseActivity.this,MainActivity.class);				
				startActivity(intent);				
			}
		});
		
		btCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(AddCourseActivity.this,MainActivity.class);
				startActivity(intent);
			}
		});
				
	}


	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		db.close();
	}
}

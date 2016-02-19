package wzu.hq.timetable;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class DetailsActivity extends Activity {
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
	
	int startWeek;
	int endWeek;
	int startClass;	
	int endClass;		
	int weekday;	
	int id;
	
	private SQLiteDatabase db=null;
	@Override	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_details);

		etCourseName=(EditText) findViewById(R.id.editText1);		
		etCourseTeacher=(EditText) findViewById(R.id.editText2);	
		etCoursePlace=(EditText) findViewById(R.id.editText3);	
		etCourseSection=(EditText) findViewById(R.id.editText4);//R.id.editText6为上课时间
		etCourseWeek=(EditText) findViewById(R.id.editText5);	
		btSave=(Button) findViewById(R.id.button1);
		btCancel=(Button) findViewById(R.id.button2);		
		btDelete=(Button) findViewById(R.id.button3);	
		
		db=(new DatabaseHelper(this)).getWritableDatabase();
		
		
		
		Intent intent=getIntent();
		
		grade=intent.getSerializableExtra("grade").toString();
		major=intent.getSerializableExtra("major").toString();
		courseName=intent.getSerializableExtra("courseName").toString();
		courseTeacher=intent.getSerializableExtra("courseTeacher").toString();
		coursePlace=intent.getSerializableExtra("coursePlace").toString();
		weekOddEven=intent.getSerializableExtra("weekOddEven").toString();
//		String str;
//		if(weekOddEven.equals(""))
//		{
//			str="每";
//		}
//		else{
//			str=weekOddEven;
//		}
		startWeek=Integer.parseInt(intent.getSerializableExtra("startWeek").toString());
		endWeek=Integer.parseInt(intent.getSerializableExtra("endWeek").toString());		
		startClass=Integer.parseInt(intent.getSerializableExtra("startClass").toString());		
		endClass=Integer.parseInt(intent.getSerializableExtra("endClass").toString());		
		weekday=Integer.parseInt(intent.getSerializableExtra("weekday").toString());	
		etCourseName.setText(courseName);
		etCourseTeacher.setText(courseTeacher);
		etCoursePlace.setText(coursePlace);
		etCourseSection.setText("周"+weekday+"  "+startClass+"-"+endClass);
		etCourseWeek.setText(startWeek+"-"+endWeek+" "+weekOddEven+"周");
		id=getID();
		
		
		
	etCourseSection.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LinearLayout mylayout=(LinearLayout) LayoutInflater.from(DetailsActivity.this).inflate(R.layout.classtimepicker_layout, null,false);//从xml中动态生成布局视图
				final NumberPicker weekNP=(NumberPicker) mylayout.findViewById(R.id.weekdaypicker);
				final NumberPicker startclassNP=(NumberPicker) mylayout.findViewById(R.id.startclasspicker);	
				final NumberPicker endclassNP=(NumberPicker) mylayout.findViewById(R.id.endclasspicker);
				
				weekNP.setMaxValue(7);
				weekNP.setMinValue(1);
				weekNP.setValue(weekday);
				
				startclassNP.setMaxValue(12);
				startclassNP.setMinValue(1);
				startclassNP.setValue(startClass);
				
				endclassNP.setMaxValue(12);
				endclassNP.setMinValue(startclassNP.getValue());
				endclassNP.setValue(endClass);
				
				new AlertDialog.Builder(DetailsActivity.this)
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
				final LinearLayout mylayout=(LinearLayout) LayoutInflater.from(DetailsActivity.this).inflate(R.layout.classweek_layout, null,false);//从xml中动态生成布局视图
				final NumberPicker startweekNP=(NumberPicker) mylayout.findViewById(R.id.startweekpicker);
				final NumberPicker endweekNP=(NumberPicker) mylayout.findViewById(R.id.endweekpicker);	
				final RadioGroup RG=(RadioGroup) mylayout.findViewById(R.id.RG);
				
				startweekNP.setMaxValue(18);
				startweekNP.setMinValue(1);
				startweekNP.setValue(startWeek);
				
				
				endweekNP.setMaxValue(18);
				endweekNP.setMinValue(startweekNP.getValue());
				endweekNP.setValue(endWeek);
				
				new AlertDialog.Builder(DetailsActivity.this)
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
						etCourseWeek.setText(startweekNP.getValue()+"-"+endweekNP.getValue()+" "+rb.getText().toString());
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
				courseTime=etCourseSection.getText().toString();
				//解析出节次信息 上课星期？
				
				courseWeek=etCourseWeek.getText().toString();
				//解析出开始周结束周单双周信息
				
//				db.execSQL("update course set "+" courseName="+courseName+" and"+
//												" courseTeacher="+courseTeacher+" and"+
//												" coursePlace="+coursePlace+" and"+
//												" startWeek="+startWeek+" and"+
//												" endWeek="+endWeek+" and"+
//												" startClass="+startClass+" and"+
//												" endClass="+endClass+" and"+
//												" weekday="+weekday+" and"+
//												" weekOddEven="+weekOddEven+
//												" where _id="+id);
				
				
				ContentValues cv = new ContentValues();//实例化ContentValues
				cv.put("courseName",courseName);//添加要更改的字段及内容
				cv.put("courseTeacher",courseTeacher);//添加要更改的字段及内容
				cv.put("coursePlace",coursePlace);//添加要更改的字段及内容
				cv.put("startWeek",startWeek);//添加要更改的字段及内容
				cv.put("endWeek",endWeek);//添加要更改的字段及内容
				cv.put("startClass",startClass);//添加要更改的字段及内容
				cv.put("endClass",endClass);//添加要更改的字段及内容
				cv.put("weekday",weekday);//添加要更改的字段及内容
				cv.put("weekOddEven",weekOddEven);//添加要更改的字段及内容
				
				String whereClause = "_id=?";//修改条件
				String[] whereArgs = {String.valueOf(id)};//修改条件的参数
				db.update("course",cv,whereClause,whereArgs);//执行修改	
				Intent intent=new Intent(DetailsActivity.this,MainActivity.class);				
				startActivity(intent);				
			}
		});

		btCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(DetailsActivity.this,MainActivity.class);
				startActivity(intent);
			}
		});
		
		btDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(DetailsActivity.this)
				.setTitle("确认删除？")
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int whichButton) {
						db.execSQL("delete from course where _id="+id);
						Intent intent=new Intent(DetailsActivity.this,MainActivity.class);
						//intent.putExtra("", value);
						startActivity(intent);
					}
				})
				.setNegativeButton("取消",	null).show();
				
				
			}
		});

	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		db.close();
	}
	
	public int getID()
	{	
		Cursor cursor = db.rawQuery("select * from course where "+" courseName='"+courseName+"' and "+
												" courseTeacher='"+courseTeacher+"' and "+
												" coursePlace='"+coursePlace+"' and "+
												" startWeek="+startWeek+" and"+
												" endWeek="+endWeek+" and "+
												" startClass="+startClass+" and "+
												" endClass="+endClass+" and "+
												" weekday="+weekday
												, null);
		int id=-1;
		if(cursor.moveToFirst())
		{				
			id=cursor.getInt(0);
		}	
		return id;
	}


}

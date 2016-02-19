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
		etCourseSection=(EditText) findViewById(R.id.editText4);//R.id.editText6Ϊ�Ͽ�ʱ��
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
//			str="ÿ";
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
		etCourseSection.setText("��"+weekday+"  "+startClass+"-"+endClass);
		etCourseWeek.setText(startWeek+"-"+endWeek+" "+weekOddEven+"��");
		id=getID();
		
		
		
	etCourseSection.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LinearLayout mylayout=(LinearLayout) LayoutInflater.from(DetailsActivity.this).inflate(R.layout.classtimepicker_layout, null,false);//��xml�ж�̬���ɲ�����ͼ
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
				.setTitle("ѡ���Ͽ�ʱ��")
				.setView(mylayout)//setView���ھ����������LayoutInflater��ͼ����
				.setPositiveButton("ȷ��",	new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int whichButton) {
						startClass=startclassNP.getValue();
						endClass=endclassNP.getValue();
						weekday=weekNP.getValue();
						etCourseSection.setText("��"+weekNP.getValue()+" "+startclassNP.getValue()+"-"+endclassNP.getValue());
					}
				})
				.setNegativeButton("ȡ��",null)
				.show();//setView���ڵ����Ի����������м����ⲿ����ʾ����ͼ
			}
		});
		
		etCourseWeek.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final LinearLayout mylayout=(LinearLayout) LayoutInflater.from(DetailsActivity.this).inflate(R.layout.classweek_layout, null,false);//��xml�ж�̬���ɲ�����ͼ
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
				.setTitle("ѡ���Ͽ���")
				.setView(mylayout)//setView���ھ����������LayoutInflater��ͼ����
				.setPositiveButton("ȷ��",	new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int whichButton) {
						RadioButton rb=(RadioButton) mylayout.findViewById(RG.getCheckedRadioButtonId());
						startWeek=startweekNP.getValue();
						endWeek=endweekNP.getValue();
						
						switch(RG.getCheckedRadioButtonId())
						{
							case R.id.radioButton1:
								weekOddEven="ÿ";
								break;
							case R.id.radioButton2:
								weekOddEven="��";
								break;
							case R.id.radioButton3:
								weekOddEven="˫";
								break;
						}					
						etCourseWeek.setText(startweekNP.getValue()+"-"+endweekNP.getValue()+" "+rb.getText().toString());
					}
				})
				.setNegativeButton("ȡ��",null)
				.show();//setView���ڵ����Ի����������м����ⲿ����ʾ����ͼ
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
				//�������ڴ���Ϣ �Ͽ����ڣ�
				
				courseWeek=etCourseWeek.getText().toString();
				//��������ʼ�ܽ����ܵ�˫����Ϣ
				
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
				
				
				ContentValues cv = new ContentValues();//ʵ����ContentValues
				cv.put("courseName",courseName);//���Ҫ���ĵ��ֶμ�����
				cv.put("courseTeacher",courseTeacher);//���Ҫ���ĵ��ֶμ�����
				cv.put("coursePlace",coursePlace);//���Ҫ���ĵ��ֶμ�����
				cv.put("startWeek",startWeek);//���Ҫ���ĵ��ֶμ�����
				cv.put("endWeek",endWeek);//���Ҫ���ĵ��ֶμ�����
				cv.put("startClass",startClass);//���Ҫ���ĵ��ֶμ�����
				cv.put("endClass",endClass);//���Ҫ���ĵ��ֶμ�����
				cv.put("weekday",weekday);//���Ҫ���ĵ��ֶμ�����
				cv.put("weekOddEven",weekOddEven);//���Ҫ���ĵ��ֶμ�����
				
				String whereClause = "_id=?";//�޸�����
				String[] whereArgs = {String.valueOf(id)};//�޸������Ĳ���
				db.update("course",cv,whereClause,whereArgs);//ִ���޸�	
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
				.setTitle("ȷ��ɾ����")
				.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int whichButton) {
						db.execSQL("delete from course where _id="+id);
						Intent intent=new Intent(DetailsActivity.this,MainActivity.class);
						//intent.putExtra("", value);
						startActivity(intent);
					}
				})
				.setNegativeButton("ȡ��",	null).show();
				
				
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

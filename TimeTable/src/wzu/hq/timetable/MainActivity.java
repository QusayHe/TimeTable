package wzu.hq.timetable;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends Activity {

	protected TextView tvEmpty,tvMon,tvTue,tvWed,tvThu,tvFri,tvSat,tvSun;

	protected RelativeLayout course_table_layout;

	protected int screenWidth;

	protected int gridWidth,gridHeight;

	ArrayList<Course> courses;
	ArrayList<TextView> courseInfos;
	RWExcel rw=new RWExcel();
	private SQLiteDatabase db=null;
	private Cursor mCursor=null;
	String path;

	String sheetName;
	String	major;
	//五种颜色的背景
	int[] background = {R.drawable.course_info_blue,R.drawable.course_info_red , 
			R.drawable.course_info_green,R.drawable.course_info_yellow ,R.drawable.course_info_purple
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		
		tvEmpty=(TextView) findViewById(R.id.empty);
		tvMon=(TextView) findViewById(R.id.monday_course);
		tvTue=(TextView) findViewById(R.id.tuesday_course);
		tvWed=(TextView) findViewById(R.id.wednesday_course);
		tvThu=(TextView) findViewById(R.id.thursday_course);
		tvFri=(TextView) findViewById(R.id.friday_course);
		tvSat=(TextView) findViewById(R.id.saturday_course);
		tvSun=(TextView) findViewById(R.id.sunday_course);
		course_table_layout=(RelativeLayout) findViewById(R.id.course_table);

		//		SharedPreferences sharedata = getSharedPreferences("data", 0);   	 
		//		path= sharedata.getString("PATH", null);   
		//		sheetName=sharedata.getString("SHEETNAME", null);  
		//		major=sharedata.getString("MAJOR", null);  


		db=(new DatabaseHelper(MainActivity.this)).getWritableDatabase();
		mCursor=db.rawQuery("SELECT _ID, Grade, Major ,CourseName,CourseTeacher ,CoursePlace,StartWeek ,EndWeek ,startClass,endClass ,weekOddEven ,weekday ,path FROM course",null);
//		path=mCursor.getString(12);
//		sheetName=mCursor.getString(1);
//		major=mCursor.getString(2);
		courseInfos=new ArrayList<TextView>();
		courses=new ArrayList<Course>();

		//Actionbar
		//final ActionBar bar = getActionBar();

		//		if(sheetName!=null && major !=null)
		//		{
		//			Intent intent=getIntent();
		//			sheetName=(String) intent.getSerializableExtra("sheetName");
		//			major=(String) intent.getSerializableExtra("major");
		//			
		//		}
		//		else
		//		{
		//			Toast.makeText(MainActivity.this, "请设置表名和班级", Toast.LENGTH_SHORT);
		//		}		
		drawcells();
	//	setCourseInfoIntoDBFromExcel();
		setCourseInfoFromDB();
	}
	
	//画超级课表背景网格
	@SuppressWarnings("deprecation")
	public void drawcells()
	{
		//获取屏幕尺寸
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);//display = getWindowManager().getDefaultDisplay(); display.getMetrics(dm)（把屏幕尺寸信息赋值给DisplayMetrics dm）;
		int width=dm.widthPixels;
		int height=dm.heightPixels;
		gridHeight=height/12;//表格每个高度
		int emptyWidth=width/15;//空白块的宽度
		screenWidth=width;
		gridWidth=(width-emptyWidth)/7;//表格每个宽度
		//设置周一至周日的宽度
		tvEmpty.setWidth(emptyWidth);
		tvMon.setWidth(gridWidth);
		tvTue.setWidth(gridWidth);
		tvWed.setWidth(gridWidth);
		tvThu.setWidth(gridWidth);
		tvFri.setWidth(gridWidth);
		tvSat.setWidth(gridWidth);
		tvSun.setWidth((width-emptyWidth)/7); 

		//8*12表格
		for(int i=1;i<=12;i++)
		{
			for(int j=1;j<=8;j++)
			{
				TextView tv = new TextView(MainActivity.this);

				tv.setId((i - 1) * 8  + j);//设置id，按从上到下从左到右从1开始递增至96
				if(j < 8)//设置前七个表格样式
					tv.setBackgroundDrawable(MainActivity.this.
							getResources().getDrawable(R.drawable.course_text_view_bg));
				else//最后一个表格样式
					tv.setBackgroundDrawable(MainActivity.this.
							getResources().getDrawable(R.drawable.course_table_last_colum));

				RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(gridWidth,gridHeight);

				tv.setGravity(Gravity.CENTER);

				tv.setTextAppearance(this, R.style.courseTableText);

				if(j == 1)
				{
					tv.setText(String.valueOf(i));
					rp.width =emptyWidth;	
					if(i == 1)
						rp.addRule(RelativeLayout.BELOW, tvEmpty.getId());
					else
						rp.addRule(RelativeLayout.BELOW, (i - 1) * 8);
				}
				else
				{
					rp.addRule(RelativeLayout.RIGHT_OF, (i - 1) * 8  + j - 1);
					rp.addRule(RelativeLayout.ALIGN_TOP, (i - 1) * 8  + j - 1);
					tv.setText("");
				}

				tv.setLayoutParams(rp);
				tv.setOnLongClickListener(new OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View arg0) {
						// TODO Auto-generated method stub
						//Toast.makeText(getApplicationContext(), "1",Toast.LENGTH_SHORT).show();
						Intent intent=new Intent(MainActivity.this,AddCourseActivity.class);
						intent.putExtra("grade",sheetName);
						intent.putExtra("major",major);
						intent.putExtra("path", path);
						startActivity(intent);
						return false;
					}
				});
				course_table_layout.addView(tv);			
			}			
		}	
	}

	//获取数据库的课表信息
	public ArrayList<Course> processSelect()//选取
	{
		ArrayList<Course> c=new ArrayList<Course>();		
		Cursor cursor = db.rawQuery("select * from course", null);
		while(cursor.moveToNext())
		{	
			path=cursor.getString(12);
			sheetName=cursor.getString(1);
			major=cursor.getString(2);
			c.add(new Course(cursor.getString(1), cursor.getString(2), cursor.getString(3),  cursor.getString(4),  cursor.getString(5),  cursor.getInt(6), cursor.getInt(7), cursor.getInt(8), cursor.getInt(9),  cursor.getString(10), cursor.getInt(11),cursor.getString(12)));		
		}	
		return c;
	}
	
	//从数据库获取课表信息并显示
	public void setCourseInfoFromDB()//从数据库读取课表信息
	{
		courses=processSelect();

		for(int i=0;i<courses.size();i++)
		{
			final Course c=courses.get(i);

			final TextView courseInfo = new TextView(this);
			//			courseInfo.setText("软件工程\n黄素珍\n1A302");
			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
					gridWidth,
					gridHeight * (c.endClass-c.startClass+1) );

			rlp.topMargin =0;
			rlp.leftMargin = 0;

			int anchor=(c.startClass-1)*8+c.weekday;
			rlp.addRule(RelativeLayout.RIGHT_OF,anchor);
			rlp.addRule(RelativeLayout.ALIGN_TOP,anchor);
			courseInfo.setGravity(Gravity.CENTER);

			courseInfo.setTextSize(9);
			courseInfo.setLayoutParams(rlp);
			courseInfo.setTextColor(Color.WHITE);
			courseInfo.setBackgroundResource(background[i%5]);
			courseInfo.getBackground().setAlpha(222);//设置不透明度
			courseInfo.setText(c.courseName+"\n"+c.courseTeacher+"\n"+c.coursePlace+"\n"+c.startWeek+"-"+c.endWeek+"周");
			courseInfo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), courseInfo.getText(), Toast.LENGTH_SHORT).show();;
					Intent intent=new Intent(MainActivity.this,DetailsActivity.class);
					intent.putExtra("grade", c.grade);
					intent.putExtra("major", c.major);
					intent.putExtra("courseName", c.courseName);
					intent.putExtra("courseTeacher", c.courseTeacher);
					intent.putExtra("coursePlace", c.coursePlace);
					intent.putExtra("startWeek", c.startWeek);
					intent.putExtra("endWeek", c.endWeek);
					intent.putExtra("startClass", c.startClass);
					intent.putExtra("endClass", c.endClass);
					intent.putExtra("weekday", c.weekday);
					intent.putExtra("weekOddEven", c.weekOddEven);
					startActivity(intent);	
				}
			});
			courseInfos.add(courseInfo);
			course_table_layout.addView(courseInfo);
		}	
	}
	
	//activity销毁时的处理
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mCursor.close();//Cursor对象和数据库均需要安全关闭
		db.close();
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		outState.putSerializable("PATH", path);
		outState.putSerializable("SHEETNAME", sheetName);
		outState.putSerializable("MAJOR", major);
	}
	
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		path=(String) savedInstanceState.getSerializable("PATH");
		sheetName=(String) savedInstanceState.getSerializable("SHEETNAME");
		major=(String) savedInstanceState.getSerializable("MAJOR");
	}
	
	
	//选项菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
//		MenuItem set=menu.add(Menu.NONE, SET_ID, Menu.NONE, "设置");
//		MenuItem add=menu.add(Menu.NONE, UPDATE_EXCEL_ID, Menu.NONE, "excel更新");
//		
//		set.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//		add.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	
	//选项菜单的响应事件
	@Override	
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		switch(item.getItemId())
		{
		case R.id.settings:
			Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
			intent.putExtra("PATH", path);
			intent.putExtra("SHEETNAME", sheetName);
			intent.putExtra("MAJOR", major);
			startActivity(intent);
			break;
			
		case R.id.updateExcel:
			if(rw.updateExcel(courses))
			{
				Toast.makeText(MainActivity.this, "更新成功", Toast.LENGTH_LONG).show();;
			}
			else
			{
				Toast.makeText(MainActivity.this, "更新错误", Toast.LENGTH_LONG).show();
			}
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}

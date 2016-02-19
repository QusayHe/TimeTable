package wzu.hq.timetable;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.widget.Toast;
import jxl.Cell;
import jxl.CellView;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class RWExcel {

	int columnCount;//列数
	int rowCount;//行数
	String grade;//sheetName
	String major;//专业

	public RWExcel(String sheetName,String major)
	{
		this.columnCount=0;
		this.rowCount=0;
		this.grade=sheetName;
		this.major=major;
	}

	public RWExcel()
	{
		this.columnCount=0;
		this.rowCount=0;
		grade="12级";
		major="12计本";
	}

	//	public static void main(String[] args)
	//	{
	//		
	//	}
	//	


	public ArrayList<String> GetmajorsByReadExcel(String path,String sheetName)
	{
		ArrayList<String> majors=new ArrayList<String>();;
		try {


			//File file=new File(Environment.getExternalStorageDirectory()+File.separator+"course.xls");
			Workbook wb = Workbook.getWorkbook(new File(path));//获得可读的工作簿
			//wb.getNumberOfSheets();//获得表的总数目
			Sheet sheet = wb.getSheet(sheetName);// 获得第一个工作表对象

			int columnCount=sheet.getColumns();
			//int rowCount=sheet.getRows();

			for(int j=2;j<columnCount;j++)
			{
				Cell majorCell=sheet.getCell(j,1);
				String content=majorCell.getContents();
				if(content!=null)
				{
					majors.add(content);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return majors;
	}

	public ArrayList<String> GetgradesByReadExcel(String path)
	{
		ArrayList<String> grades=new ArrayList<String>();;
		try {


			//File file=new File(Environment.getExternalStorageDirectory()+File.separator+"course.xls");
			Workbook wb = Workbook.getWorkbook(new File(path));//获得可读的工作簿
			//wb.getNumberOfSheets();//获得表的总数目

			int sheetNum=wb.getNumberOfSheets();	

			for(int j=0;j<sheetNum;j++)
			{
				grades.add(wb.getSheet(j).getName());

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return grades;
	}
	
	public ArrayList<Course> GetCoursesByReadExcel(String path)
	{
		ArrayList<Course> courses=new ArrayList<Course>();
		try {


			//File file=new File(Environment.getExternalStorageDirectory()+File.separator+"course.xls");
			Workbook wb = Workbook.getWorkbook(new File(path));//获得可读的工作簿
			//wb.getNumberOfSheets();//获得表的总数目
			Sheet sheet = wb.getSheet(grade);

			int columnCount=sheet.getColumns();
			int rowCount=sheet.getRows();

			int col=-1;

			for(int j=0;j<columnCount;j++)
			{
				Cell majorCell=sheet.getCell(j,1);
				if(majorCell.getContents().equals(major))
				{
					col=j;
					break;
				}
			}
			if(col<0)
			{

			}
			else
			{

				for(int i=2;i<rowCount-1;i++)
				{
					Cell cell=sheet.getCell(col,i);
					String content=cell.getContents().toString();
					String[] arr=content.split("\n");//获取每个cell内容中的单个内容课程名、教师、上课地点、上课周
					//应用正则表达式进行解析，由于未掌握正则表达式， 这里先用简单的处理，但较为复杂
					if(content!=null)
					{								
						if(arr.length==4)
						{
							String courseName=arr[0];
							String courseTeacher=arr[1];
							String coursePlace=arr[2];
							String weekOddEven="每";
							if(arr[3].contains("单")||arr[3].contains("双"))
							{
								if(arr[3].contains("单"))
									weekOddEven="单";
								else 
									weekOddEven="双";
							}		

							String str=arr[3];//正则表达式获取信息
							String[] temp = new String[10];
							Pattern p = Pattern.compile("[0-9\\.]+");
							Matcher m = p.matcher(str);

							int key=0;
							while(m.find()){

								temp[key]=m.group();
								key++;
							}

							if(key==4)
							{
								int startWeek=Integer.parseInt(temp[0]);
								int endWeek=Integer.parseInt(temp[1]);
								int startClass=Integer.parseInt(temp[2]);
								int endClass=Integer.parseInt(temp[3]);
								int weekday=judge(i);
								Course course=new Course(grade,major, courseName, courseTeacher, coursePlace, startWeek, endWeek, startClass, endClass, weekOddEven, weekday,path);
								courses.add(course);
							}		
						}
					}
				}
			}
			wb.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return courses;
	}

	public int judge(int i)
	{
		int weekday=1;
		if((i-2)/6==0)
		{
			weekday=1;
		}
		else if((i-2)/6==1)
		{
			weekday=2;
		}
		else if((i-2)/6==2)
		{
			weekday=3;
		}
		else if((i-2)/6==3)
		{
			weekday=4;
		}
		else if((i-2)/6==4)
		{
			weekday=5;
		}
		else if((i-2)/6==5)
		{
			weekday=6;
		}
		else if((i-2)/6==6)
		{
			weekday=7;
		}

		return weekday;
	}

	public boolean updateExcel(ArrayList<Course> c)
	{
		try{		
			Workbook wb=Workbook.getWorkbook(new File(c.get(0).path));
			WritableWorkbook wbook=Workbook.createWorkbook(new File(c.get(0).path), wb);
			WritableSheet ws=wbook.getSheet(c.get(0).grade);
			CellView cellView = new CellView();  
			cellView.setAutosize(true); //设置自动大小    
			ws.setColumnView(1, cellView);
			
			 //设置字体;  
	        WritableFont font = new WritableFont(WritableFont.ARIAL,8,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.BLACK);  
	  
	        WritableCellFormat cellFormat = new WritableCellFormat(font);  
//	        //设置背景颜色;  
//	        cellFormat1.setBackground(Colour.BLUE_GREY);  
//	        //设置边框;  
//	        cellFormat.setBorder(Border.ALL, BorderLineStyle.DASH_DOT);  
	        //设置自动换行;  
	        cellFormat.setWrap(true);  
//	        //设置文字居中对齐方式;  
	        cellFormat.setAlignment(Alignment.CENTRE);  
//	        //设置垂直居中;  
	        cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);  
//	        Label label1 = new Label(0, 0, "第一行第一个单元格(测试是否自动换行!)",cellFormat1); 
			
//			Label label=new Label(3,0,"发送状态");
//			ws.addCell(label);
			int col=-1;
			for(int i=2;i<ws.getColumns();i++)
			{
				String content=ws.getCell(i,1).getContents();
				if(content.equals(c.get(0).major))
				{
					col=i;
				}
			}
			if(col>1)
			{	
				for(int i=0;i<c.size();i++)
				{
					String str=c.get(i).courseName+"\n"+c.get(i).courseTeacher+"\n"+c.get(i).coursePlace+"\n"+c.get(i).startWeek+"-"+c.get(i).endWeek+c.get(i).weekOddEven+"("+c.get(i).startClass+","+c.get(i).endClass+")";
					Label label=new  Label(col,1+(c.get(i).weekday-1)*6+c.get(i).endClass/2 , str,cellFormat);	
					ws.addCell(label);
				}
			}
			
			wbook.write();
			wbook.close();	
			return true;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
}

package wzu.hq.timetable;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import wzu.hq.timetable.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class simpleFileExplore extends Activity {
	/** Called when the activity is first created. */
	/*
	 * 变量声明 items：存放显示的名称
	 *  paths：存放文件路径 rootPath：起始目录
	 */
	ListView lv1;
	private List<String> items = null;
	private List<String> paths = null;
	private String rootPath = "/";
	private TextView mPath;

	String path;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		/* 加载main.xml Layout */
		setContentView(R.layout.file_explore);
		/* 初始化mPath，用以显示目前路径 */
		mPath = (TextView) findViewById(R.id.mPath);
		lv1=(ListView)findViewById(R.id.mlist01);
		showFileDir(rootPath);
		lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				dealPathPosition(position);


			}

		});

	}

	/* 取得文件架构的method，将filePath的各项文件和子目录显示出来 */
	private void showFileDir(String filePath) {
		/* 设定目前所存路径 */
		mPath.setText(filePath);
		items = new ArrayList<String>();
		paths = new ArrayList<String>();
		File f = new File(filePath);
		File[] files = f.listFiles();//通过listFiles可获取当前File（目录）的所有文件及子目录

		if (!filePath.equals(rootPath)) {//对非根目录的处理
			/* 第一笔设定为[根目录] */
			items.add("b1");
			paths.add(rootPath);
			/* 第二笔设定为[返回上一层] */
			items.add("b2");
			paths.add(f.getParent());
		}
		/* 将所有文件存入ArrayList中 */
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			items.add(file.getName());//itmes存放文件或目录名称
			paths.add(file.getPath());//path存放对应的路径
		}

		/* 使用告定义的MyAdapter来将数据传入ListActivity */
		lv1.setAdapter(new MyAdapter(this, items, paths));//MyAdapter 为自定义的适配器，见MyAdapter.java



	}

	/* 设定ListItem被按下时要做的动作 */

	public void dealPathPosition(int position) {
		File file = new File(paths.get(position));
		if (file.canRead()) {
			if (file.isDirectory()) {
				/* 如果是文件夹就运行getFileDir() */
				showFileDir(paths.get(position));//若是目录，则继续进入该目录
			} else {
				/* 如果是文件就运行openFile() */
				//	openFile(file);//自定义函数
				String fName = file.getName();
				String end = fName
						.substring(fName.lastIndexOf(".") + 1, fName.length())
						.toLowerCase();//取文件的扩展名，并转化为小写
				if (end.equals("xls"))
				{
					Intent intent=new Intent(simpleFileExplore.this,SettingsActivity.class);
					String path=file.getPath();
					intent.putExtra("SELECTPATH", path);
					startActivity(intent);
				}
				else
				{
					new AlertDialog.Builder(this)
					.setTitle("提醒")
					.setMessage("文件类型不合法!")
					.setPositiveButton("OK",null).show();
				}
			}
		} else {
			/* 弹出AlertDialog显示权限不足 */
			new AlertDialog.Builder(this)
			.setTitle("警告")
			.setMessage("权限不足!")
			.setPositiveButton("OK",null).show();
		}
	}
	
	
	class MyAdapter extends ArrayAdapter {
		/*
		 * 图标 mIcon1：回到根目录的图标 mIcon2：回到上一层的图标 mIcon3：文件夹的图标 mIcon4：文件的图标
		 */
		private LayoutInflater mInflater;
		private int[] mIcons;
		// private Bitmap mIcon1;
		private Bitmap mIcon3;
		private Bitmap mIcon4;
		private List<String> items;
		//itmes存放文件或目录名称
		private List<String> paths;
		//path存放对应的路径

		
		/* MyAdapter的构造函数，分别传入上写文、当前目录下的内容列表、路径列表 */
		public MyAdapter(Context context, List<String> itemList,
				List<String> pathList) {

			super(context, android.R.layout.simple_list_item_1, itemList); // 调用父类，主要是要用到getView的自动调用
			/* 参数初始化 */
			mInflater = LayoutInflater.from(context);// 从传入的上下文获得LayoutInflater,以便在getView改写中动态生成布局视图
			items = itemList;
			paths = pathList;
			mIcons = new int[] { R.drawable.back01, R.drawable.back02,
					R.drawable.folder, R.drawable.doc };
			// mIcons 为文件浏览器的4种图标id号

			// mIcon1 = BitmapFactory.decodeResource(context.getResources(),
			// R.drawable.back01);
			// //BitmapFactory 用于生成 Bitmap 图片对象
			// //第一个参数是resource目录,若是本package的res，可用传进来的context.getResources()获取
			// //第二个参数是图片资源id

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView text;
			ImageView icon;

			if (convertView == null)// 如果convertView 为空，需要动态生成布局图，否则只需要进行更新即可
			{
				/* 使用自定义的布局文件：file_row为视图，需要用inflate方法实现 */
				convertView = mInflater.inflate(R.layout.file_row, null);
			}
			text = (TextView) convertView.findViewById(R.id.text);
			icon = (ImageView) convertView.findViewById(R.id.icon);
			File f = new File(paths.get(position).toString());
			/* 根据当前目录中的信息items来设置相应图标 */
			if (items.get(position).toString().equals("b1"))// 设置返回根目录的图标
			{
				text.setText("Back to /");
				icon.setImageResource(mIcons[0]);
			}

			else if (items.get(position).toString().equals("b2"))// 设置返回上一层的图标
			{
				text.setText("Back to ..");
				icon.setImageResource(mIcons[1]);
			}
			/* 设置文件或文件夹的图标 */
			else {
				text.setText(f.getName());
				if (f.isDirectory()) {
					icon.setImageResource(mIcons[2]);

				} else {
					icon.setImageResource(mIcons[3]);
				}
			}
			return convertView;
		}

	}

}
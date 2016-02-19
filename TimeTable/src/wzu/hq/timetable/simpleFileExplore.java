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
	 * �������� items�������ʾ������
	 *  paths������ļ�·�� rootPath����ʼĿ¼
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

		/* ����main.xml Layout */
		setContentView(R.layout.file_explore);
		/* ��ʼ��mPath��������ʾĿǰ·�� */
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

	/* ȡ���ļ��ܹ���method����filePath�ĸ����ļ�����Ŀ¼��ʾ���� */
	private void showFileDir(String filePath) {
		/* �趨Ŀǰ����·�� */
		mPath.setText(filePath);
		items = new ArrayList<String>();
		paths = new ArrayList<String>();
		File f = new File(filePath);
		File[] files = f.listFiles();//ͨ��listFiles�ɻ�ȡ��ǰFile��Ŀ¼���������ļ�����Ŀ¼

		if (!filePath.equals(rootPath)) {//�ԷǸ�Ŀ¼�Ĵ���
			/* ��һ���趨Ϊ[��Ŀ¼] */
			items.add("b1");
			paths.add(rootPath);
			/* �ڶ����趨Ϊ[������һ��] */
			items.add("b2");
			paths.add(f.getParent());
		}
		/* �������ļ�����ArrayList�� */
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			items.add(file.getName());//itmes����ļ���Ŀ¼����
			paths.add(file.getPath());//path��Ŷ�Ӧ��·��
		}

		/* ʹ�ø涨���MyAdapter�������ݴ���ListActivity */
		lv1.setAdapter(new MyAdapter(this, items, paths));//MyAdapter Ϊ�Զ��������������MyAdapter.java



	}

	/* �趨ListItem������ʱҪ���Ķ��� */

	public void dealPathPosition(int position) {
		File file = new File(paths.get(position));
		if (file.canRead()) {
			if (file.isDirectory()) {
				/* ������ļ��о�����getFileDir() */
				showFileDir(paths.get(position));//����Ŀ¼������������Ŀ¼
			} else {
				/* ������ļ�������openFile() */
				//	openFile(file);//�Զ��庯��
				String fName = file.getName();
				String end = fName
						.substring(fName.lastIndexOf(".") + 1, fName.length())
						.toLowerCase();//ȡ�ļ�����չ������ת��ΪСд
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
					.setTitle("����")
					.setMessage("�ļ����Ͳ��Ϸ�!")
					.setPositiveButton("OK",null).show();
				}
			}
		} else {
			/* ����AlertDialog��ʾȨ�޲��� */
			new AlertDialog.Builder(this)
			.setTitle("����")
			.setMessage("Ȩ�޲���!")
			.setPositiveButton("OK",null).show();
		}
	}
	
	
	class MyAdapter extends ArrayAdapter {
		/*
		 * ͼ�� mIcon1���ص���Ŀ¼��ͼ�� mIcon2���ص���һ���ͼ�� mIcon3���ļ��е�ͼ�� mIcon4���ļ���ͼ��
		 */
		private LayoutInflater mInflater;
		private int[] mIcons;
		// private Bitmap mIcon1;
		private Bitmap mIcon3;
		private Bitmap mIcon4;
		private List<String> items;
		//itmes����ļ���Ŀ¼����
		private List<String> paths;
		//path��Ŷ�Ӧ��·��

		
		/* MyAdapter�Ĺ��캯�����ֱ�����д�ġ���ǰĿ¼�µ������б�·���б� */
		public MyAdapter(Context context, List<String> itemList,
				List<String> pathList) {

			super(context, android.R.layout.simple_list_item_1, itemList); // ���ø��࣬��Ҫ��Ҫ�õ�getView���Զ�����
			/* ������ʼ�� */
			mInflater = LayoutInflater.from(context);// �Ӵ���������Ļ��LayoutInflater,�Ա���getView��д�ж�̬���ɲ�����ͼ
			items = itemList;
			paths = pathList;
			mIcons = new int[] { R.drawable.back01, R.drawable.back02,
					R.drawable.folder, R.drawable.doc };
			// mIcons Ϊ�ļ��������4��ͼ��id��

			// mIcon1 = BitmapFactory.decodeResource(context.getResources(),
			// R.drawable.back01);
			// //BitmapFactory �������� Bitmap ͼƬ����
			// //��һ��������resourceĿ¼,���Ǳ�package��res�����ô�������context.getResources()��ȡ
			// //�ڶ���������ͼƬ��Դid

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView text;
			ImageView icon;

			if (convertView == null)// ���convertView Ϊ�գ���Ҫ��̬���ɲ���ͼ������ֻ��Ҫ���и��¼���
			{
				/* ʹ���Զ���Ĳ����ļ���file_rowΪ��ͼ����Ҫ��inflate����ʵ�� */
				convertView = mInflater.inflate(R.layout.file_row, null);
			}
			text = (TextView) convertView.findViewById(R.id.text);
			icon = (ImageView) convertView.findViewById(R.id.icon);
			File f = new File(paths.get(position).toString());
			/* ���ݵ�ǰĿ¼�е���Ϣitems��������Ӧͼ�� */
			if (items.get(position).toString().equals("b1"))// ���÷��ظ�Ŀ¼��ͼ��
			{
				text.setText("Back to /");
				icon.setImageResource(mIcons[0]);
			}

			else if (items.get(position).toString().equals("b2"))// ���÷�����һ���ͼ��
			{
				text.setText("Back to ..");
				icon.setImageResource(mIcons[1]);
			}
			/* �����ļ����ļ��е�ͼ�� */
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
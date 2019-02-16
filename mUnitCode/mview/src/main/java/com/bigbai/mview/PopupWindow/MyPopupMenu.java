package com.bigbai.mview.PopupWindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bigbai.mview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 此模块 摘录 于 http://dditblog.com/itshare_373.html
 */
@SuppressLint("ViewConstructor")
public class MyPopupMenu extends PopupWindow {

	/**
	 * 菜单栏的整体布局LinearLayout
	 */
	private LinearLayout linearLayout;

	/**
	 * 菜单栏分类标题布局GridView
	 */
	private GridView gv_title;

	public GridView getGv_body() {
		return gv_body;
	}

	/**
	 * 当先选中列表项
	 */
	List<List<String>> item_names;
	public List<List<String>> getItem_names() {
		return item_names;
	}


	/**
	 * 菜单栏功能图标与名称GridView
	 */
	private GridView gv_body;

	/**
	 * 菜单栏功能图标与名称GridView的适配
	 */
	private BodyAdapter[] bodyAdapter;

	/**
	 * 菜单栏分类标题GridView的适配
	 */
	private TitleAdapter titleAdapter;

	private Context context;

	/**
	 * 获取当前 选中 ID
	 * @return
	 */
	public int getCurrentIndex() {
		return currentIndex;
	}

	/**
	 * 当前选中的分类标题
	 */
	private int currentIndex = 0;

	/**
	 * 上一次选中的分类标题 用于选择分类标题时的左右移动动画，判断应该怎样移动
	 */
	private int preIndex = 0;

	/**
	 * 标题与功能布局中间的分界线 RelativeLayout + TextView
	 */
	private RelativeLayout divisionLayout;

	/**
	 * 屏幕宽度
	 */
	private int screenWidth = 0;

	//@SuppressWarnings("deprecation")
	public MyPopupMenu(Context context, List<String> titles,
					   final List<List<String>> item_names, List<List<Integer>> item_images) {

		super(context);
		this.context = context;
		PopuMenuItem popuMenuItem = new PopuMenuItem(titles,item_names,item_images);
		initPopupMenu(popuMenuItem);
	}


	public MyPopupMenu(Context context,PopuMenuItem popuMenuItem)
	{
		super(context);
		this.context = context;
		initPopupMenu(popuMenuItem);

	}


	/**
	 * 菜单项初始化
	 * @param popuMenuItem
	 */
	public void initPopupMenu(PopuMenuItem popuMenuItem)
	{
		this.item_names = popuMenuItem.item_names;
		/**
		 * 菜单栏的整体布局LinearLayout初始化
		 */
		linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		/**
		 * 获取屏幕宽度
		 */
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		screenWidth = wm.getDefaultDisplay().getWidth();

		/**
		 * 分界线布局初始化
		 */
		divisionLayout = new RelativeLayout(context);
		divisionLayout.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, 3));
		divisionLayout.setBackgroundColor(Color.DKGRAY);

		/**
		 * 标题布局初始化
		 */
		gv_title = new GridView(context);

		/**
		 * 用于重新初始化adapter
		 */
		final List<String> l = popuMenuItem.titles;
		final Context c = context;

		titleAdapter = new TitleAdapter(popuMenuItem.titles, context, 0);

		/**
		 * 设置被选中后，背景颜色不再是系统原有的黄色，改为TRANSPARENT
		 */
		gv_title.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gv_title.setAdapter(titleAdapter);

		/**
		 * 设置GridView列数
		 */
		gv_title.setNumColumns(titleAdapter.getCount());

		gv_title.setBackgroundColor(Color.TRANSPARENT);

		/**
		 * 选择分类标题时的响应事件
		 */
		gv_title.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

				/**
				 * 重新初始化adapter，为了改变标题选择颜色
				 */
				titleAdapter = new TitleAdapter(l, c, position);

				preIndex = currentIndex;
				currentIndex = position;

				gv_title.setAdapter(titleAdapter);

				/**
				 * 分界线布局中的textView跟随选中标题移动位置的，设置为动画效果
				 */
				divisionTran(position);

				/**
				 * 用于功能图标GridView动画效果 TranslateAnimation方法中的参数设置暂时不太明确
				 * 似乎，参数都是相对于控件自身的位置 第一个参数是开始位置，第二个是结束位置 有时间会弄清楚
				 */
				Animation translateBody;
				if (preIndex < currentIndex) {
					translateBody = new TranslateAnimation(screenWidth, 0, 0, 0);
					translateBody.setDuration(500);
					gv_body.startAnimation(translateBody);
				} else if (preIndex > currentIndex) {
					translateBody = new TranslateAnimation(-screenWidth, 0, 0,
							0);
					translateBody.setDuration(500);
					gv_body.startAnimation(translateBody);
				}

				gv_body.setAdapter(bodyAdapter[position]);

			}
		});

		bodyAdapter = new BodyAdapter[item_names.size()];
		for (int i = 0; i < item_names.size(); i++) {
			bodyAdapter[i] = new BodyAdapter(context, item_names.get(i),
					popuMenuItem.item_images.get(i));
		}
		gv_body = new GridView(context);
		gv_body.setNumColumns(4);
		gv_body.setBackgroundColor(Color.TRANSPARENT);
		gv_body.setPadding(0, 10, 0, 10);
		gv_body.setAdapter(bodyAdapter[0]);

		/**
		 * 选择功能图标时的响应事件,
		 */
//		gv_body.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				/**
//				 * 这里只是在控制台输出了一下功能的名称
//				 */
//				System.out.println(item_names.get(currentIndex).get(position));
//			}
//		});

		/**
		 * 初始化textView位置
		 */
		divisionTran(0);

		/**
		 * 把三个子布局加入到整体布局中去
		 */
		linearLayout.addView(gv_title);
		linearLayout.addView(divisionLayout);
		linearLayout.addView(gv_body);

		this.setContentView(linearLayout);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);

		/**
		 * 以下代码是为了解决，菜单栏出现后，不能响应再次按menu按键使菜单栏消失的问题
		 * 在这个网址找到的答案http://blog.csdn.net/admin_/article/details/7278402 可以自己去看
		 */
		this.setFocusable(true);
		linearLayout.setFocusableInTouchMode(true);
		linearLayout.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_MENU)
						&& (MyPopupMenu.this.isShowing())) {
					MyPopupMenu.this.dismiss();
					titleAdapter = new TitleAdapter(l, c, 0);
					gv_title.setAdapter(titleAdapter);
					return true;
				}
				return false;
			}
		});

		linearLayout.setBackgroundResource(R.drawable.fillet_style);

	}

	/**
	 * 分界线布局中的textView跟随选中标题移动位置的，设置为动画效果
	 */
	public void divisionTran(int position) {

		/**
		 * 先移除了RelativeLayout中原有的textView
		 */
		divisionLayout.removeAllViews();

		/**
		 * 重新设置textView布局属性 动态改变控件位置 第一步
		 */
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				screenWidth / 3, LayoutParams.MATCH_PARENT);

		/**
		 * 设置动画效果
		 */
		Animation translateTextView;
		translateTextView = new TranslateAnimation((preIndex - currentIndex)
				* screenWidth / 3, 0, 0, 0);

		/**
		 * 根据选中的标题确定布局 动态改变控件位置 第二步
		 */
		if (position == 0) {
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		} else if (position == 1) {
			lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		} else {
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		}

		/**
		 * 动态改变控件位置 第三步
		 */
		TextView line = new TextView(context);
		line.setBackgroundColor(Color.WHITE);
		divisionLayout.addView(line, lp);

		/**
		 * 设置动画执行时间
		 */
		translateTextView.setDuration(200);

		/**
		 * 启动动画
		 */
		line.startAnimation(translateTextView);
	}


	/**
	 * 转换为List<String>
	 * 用于菜单栏中的菜单项图标赋值
	 * @param values
	 * @return
	 */
	public static List<String> addItems(String[] values) {

		List<String> list = new ArrayList<String>();
		for (String var : values) {
			list.add(var);
		}

		return list;
	}

	/**
	 * 转换为List<Integer>
	 * 用于菜单栏中的标题赋值
	 * @param values
	 * @return
	 */
	public static List<Integer> addItems(Integer[] values) {

		List<Integer> list = new ArrayList<Integer>();
		for (Integer var : values) {
			list.add(var);
		}

		return list;
	}

}

/**
 * 使用方法：
 *
 */

/** 变量 声明 */
//protected void onCreatePopuMenu() {
//	//菜单栏分类标题
//	titles = new ArrayList<String>();
//	titles = myPopupMenu.addItems(new String[]{"常用", "设置", "工具"});
//
//	// 选项图标
//	item_images = new ArrayList<List<Integer>>();
//	item_images.add(myPopupMenu.addItems(new Integer[] {
//			R.drawable.ic_action_call, R.drawable.ic_action_camera,
//			R.drawable.ic_action_copy, R.drawable.ic_action_crop,
//			R.drawable.ic_action_cut, R.drawable.ic_action_discard,
//			R.drawable.ic_action_download, R.drawable.ic_action_edit }));
//	item_images.add(myPopupMenu.addItems(new Integer[] {
//			R.drawable.ic_action_email, R.drawable.ic_action_full_screen,
//			R.drawable.ic_action_help, R.drawable.ic_action_important,
//			R.drawable.ic_action_map, R.drawable.ic_action_mic,
//			R.drawable.ic_action_picture, R.drawable.ic_action_place }));
//	item_images.add(myPopupMenu.addItems(new Integer[] {
//			R.drawable.ic_action_refresh, R.drawable.ic_action_save,
//			R.drawable.ic_action_search, R.drawable.ic_action_share,
//			R.drawable.ic_action_switch_camera, R.drawable.ic_action_video,
//			R.drawable.ic_action_web_site,
//			R.drawable.ic_action_screen_rotation }));
//	//选项名称
//	item_names = new ArrayList<List<String>>();
//	item_names.add(myPopupMenu.addItems(new String[] { "电话", "相机",
//			"复制", "裁剪", "剪切", "删除", "下载", "编辑" }));
//	item_names.add(myPopupMenu.addItems(new String[] { "邮件", "全屏",
//			"帮助", "收藏", "地图", "语音", "图片", "定位" }));
//	item_names.add(myPopupMenu.addItems(new String[] { "刷新", "保存",
//			"搜索", "分享", "切换", "录像", "浏览器", "旋转屏幕" }));
//
//	myPopupMenu = new MyPopupMenu(this, titles,
//			item_names, item_images);
//	//设置菜单栏推拉动画效果
//	// res/anim中的xml文件与styles.xml中的style配合使用
//	myPopupMenu.setAnimationStyle(R.style.PopupAnimation);
//
//
//	myPopupMenu.getGv_body().setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view,
//								int position, long id) {
//			/**
//			 * 这里只是在控制台输出了一下功能的名称
//			 */
//			Log.i("测试",myPopupMenu.getItem_names().get(myPopupMenu.getCurrentIndex()).get(position));
//		}
//	});
//}


/** 注册到系统菜单响应 */
//@Override
//public boolean onMenuOpened(int featureId, Menu menu) {
//
//    onMenuOpened();
//
//    return false;
//}

/** 手动调用打开底部 菜单*/
//    private void onMenuOpened()
//    {
//        if (myPopupMenu.isShowing()) {
//            myPopupMenu.dismiss();
//        } else {
//            //这句代码可以使菜单栏如对话框一样弹出的效果
//            myPopupMenu.setAnimationStyle(android.R.style.Animation_Dialog);
//            //设置菜单栏显示位置
//            myPopupMenu.showAtLocation(findViewById(R.id.mainLayout),
//                    Gravity.BOTTOM, 0, 0);
//            myPopupMenu.isShowing();
//        }
//    }
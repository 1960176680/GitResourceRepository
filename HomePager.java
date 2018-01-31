package com.example.game.pager;

import java.util.ArrayList;
import java.util.List;

import com.example.game.MainActivity;
import com.example.game.R;
import com.example.game.Utils.UiUtils;
import com.example.game.fragment.CenterFragment;
import com.example.game.fragment.ContentFragment;
import com.example.game.fragment.NewGameFragment;
import com.example.game.fragment.RecommendFragment;
import com.example.game.fragment.TopFragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class HomePager extends BasePager{

	public View Pager;
	private View option;
	Button buttonOne;
	Button buttonTwo;
	Button buttonThree;
	Button buttonFour;

	ViewPager game;
	
	/**
	 * 页面集合
	 */
	List<Fragment> fragmentList;
	/**
	 * 四个Fragment（页面）
	 */
	RecommendFragment oneFragment;
	TopFragment twoFragment;
	NewGameFragment threeFragment;
	CenterFragment fourFragment;
	
	//屏幕宽度
		int screenWidth;
	//当前选中的项
		int currenttab=-1;
	
	public HomePager(Activity activity) {
		super(activity);
	}

	@Override
	public void initViews() {
		Pager = View.inflate(mActivity, R.layout.homepager, null);
		option = Pager.findViewById(R.id.option);
		buttonOne = (Button) option.findViewById(R.id.btn_one);
		buttonTwo = (Button) option.findViewById(R.id.btn_two);
		buttonThree = (Button) option.findViewById(R.id.btn_three);
		buttonFour = (Button) option.findViewById(R.id.btn_four);
		
		game = (ViewPager) option.findViewById(R.id.viewpager1);
		
		fragmentList=new ArrayList<Fragment>();
		oneFragment=new RecommendFragment();
		twoFragment=new TopFragment();
		threeFragment=new NewGameFragment();
		
		fragmentList.add(oneFragment);
		/*fragmentList.add(twoFragment);
		fragmentList.add(threeFragment);
		fragmentList.add(fourFragment);*/
		//屏幕宽度
		screenWidth=UiUtils.getResource().getDisplayMetrics().widthPixels;
			
		RelativeLayout.LayoutParams imageParams=new RelativeLayout.LayoutParams(screenWidth/4, buttonTwo.getMeasuredHeight());
		imageParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				
		game.setAdapter(new MyFrageStatePagerAdapter(getFragmentManager()));
	}
	/**
	 * 定义自己的ViewPager适配器。
	 * 也可以使用FragmentPagerAdapter。关于这两者之间的区别，可以自己去搜一下。
	 */
	class MyFrageStatePagerAdapter extends FragmentStatePagerAdapter
	{

		

		public MyFrageStatePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return fragmentList.get(position);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}
		
		/**
		 * 每次更新完成ViewPager的内容后，调用该接口，此处复写主要是为了让导航按钮上层的覆盖层能够动态的移动
		 */
		@Override
		public void finishUpdate(ViewGroup container) 
		{
			super.finishUpdate(container);//这句话要放在最前面，否则会报错
			//获取当前的视图是位于ViewGroup的第几个位置，用来更新对应的覆盖层所在的位置
			int currentItem=game.getCurrentItem();
			if (currentItem==currenttab)
			{
				return ;
			}
			
			currenttab=game.getCurrentItem();
		}
		
	}
	

	
	public void onClick(View v) {
		switch (v.getId())
		{
		case R.id.btn_one:
			changeView(0);
			break;
		case R.id.btn_two:
			changeView(1);
			break;
		case R.id.btn_three:
			changeView(2);
			break;
		case R.id.btn_four:
			changeView(3);
			break;
		default:
			break;
		}
	}
	//手动设置ViewPager要显示的视图
	private void changeView(int desTab)
	{
		game.setCurrentItem(desTab, true);
	}
	
	@Override
	public void initData() {
		super.initData();
	}
}

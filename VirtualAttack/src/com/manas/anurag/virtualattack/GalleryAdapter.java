package com.manas.anurag.virtualattack;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GalleryAdapter extends BaseAdapter{

	Context ctx;
	int[] images;
	
	public GalleryAdapter(Context c){
		ctx = c;
		images = new int[]{R.drawable.whip,R.drawable.sword};
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return images.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return images[arg0];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView iv = new ImageView(ctx);
		iv.setBackgroundResource(images[position]);
		iv.setScaleType(ImageView.ScaleType.FIT_XY);
		return iv;
	}

}

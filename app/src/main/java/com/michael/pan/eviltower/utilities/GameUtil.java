package com.michael.pan.eviltower.utilities;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.USER_LOADER_PROJECTION;

public class GameUtil {

	private static String[] STRING_ARRAY_PROJECTION = USER_LOADER_PROJECTION;

	public static String[] StringArrayConcatenation(String[] str1, String[] str2) {
		int total = str1.length + str2.length;
		String[] tempt = new String[total];
		int i = 0;
		while (i<total) {
			if(i<str1.length) {
				tempt[i] = str1[i];
			}else {
				tempt[i] = str2[i - str1.length];
			}
			i++;
		}
		return tempt;
	}

	public static int getIdfromName(String str) {
		char[] letters = str.toCharArray();
		int id = 0, ind = 1;
		for (char ch : letters){
			id += ((byte) ch)*ind;
			ind++;
			//System.out.println(id);
		}
		return Math.abs(id);
	}

	public static int findIndexOfStringArray(String s){

		int index = -1;
		for (int i = 0; i < STRING_ARRAY_PROJECTION.length; i++) {
			if (STRING_ARRAY_PROJECTION[i].equals(s)) {
				index = i;
				break;
			}
		}
//		System.out.println("String index of " + s + " is: " + index);
		return index;
	}

	public static String refreshFragment(FragmentManager fragmentManager, String tag){
		Fragment fragment = fragmentManager.findFragmentByTag(tag);
		if (fragment != null) fragmentManager.beginTransaction().remove(fragment).commit();
		return tag;
	}
}

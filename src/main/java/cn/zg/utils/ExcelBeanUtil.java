package cn.zg.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.zg.model.Student;


public class ExcelBeanUtil {

	/**
	 * 处理产品列表 塞入list-map 等待塞入excel的workbook进行处理
	 * @param products
	 * @return
	 */
	public static List<Map<Integer, Object>> manageProductList(final List<Student> students){
		List<Map<Integer, Object>> dataList=new ArrayList<>();
		
		if (students!=null && students.size()>0) {
			int length=students.size();
			
			Map<Integer, Object> dataMap;
			Student bean;
			for (int i = 0; i < length; i++) {
				bean=students.get(i);
				
				//String[] headers=new String[]{"id编号","性别","大学","入学年","Jvalue"};
				dataMap=new HashMap<>();
				dataMap.put(0, bean.getId());
				dataMap.put(1, bean.getSex());
				dataMap.put(2, bean.getUniversity());
				dataMap.put(2, bean.getCollege());
				dataMap.put(4, bean.getYear());
				dataMap.put(5, bean.getJvalue());
				dataList.add(dataMap);
			}
		}
		return dataList;
	}
	
	
	
	
}













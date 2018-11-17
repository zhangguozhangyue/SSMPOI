package cn.zg.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.zg.enums.WorkBookVersion;
import cn.zg.model.Student;
import cn.zg.service.IPoiService;
import cn.zg.utils.DateUtil;
import cn.zg.utils.ExcelUtil;

@Service("poiService")
public class PoiService implements IPoiService{

	private static final Logger log=LoggerFactory.getLogger(PoiService.class);
	
	/**
	 * 读取excel数据
	 * @param wb
	 * @return
	 * @throws Exception
	 */
	/* (non-Javadoc)
	 * @see cn.zg.service.IPoiService#readExcelData(org.apache.poi.ss.usermodel.Workbook)
	 */
	public List<Student> readExcelData(Workbook wb) throws Exception{
		Student student=null;
		
		List<Student> students=new ArrayList<Student>();
		Row row=null;
		int numSheet=wb.getNumberOfSheets();
		if (numSheet>0) {
			for(int i=0;i<numSheet;i++){
				Sheet sheet=wb.getSheetAt(i);
				int numRow=sheet.getLastRowNum();
				if (numRow>0) {
					for(int j=1;j<=numRow;j++){
						//TODO：跳过excel sheet表格头部
						row=sheet.getRow(j);
						student=new Student();
						String sex=null;
						
                         if(row.getCell(0)==null) {
                        	 sex=null;
                         }else {
					   sex=ExcelUtil.manageCell(row.getCell(0), null);}
                         
                        
//						String sex=ExcelUtil.manageCell(row.getCell(0), null);
//						String university=ExcelUtil.manageCell(row.getCell(1), null);
						String university=null;
						if(row.getCell(1)==null){
							university=null;
						}else {
							university=ExcelUtil.manageCell(row.getCell(1), null);
						}
//						Integer year=Integer.valueOf(ExcelUtil.manageCell(row.getCell(2), null));
//						Integer jvalue=Integer.valueOf(ExcelUtil.manageCell(row.getCell(3), null));
                         
                         Integer year=0;
						 if(row.getCell(2)==null) {
							 year=0;
						 }else {
							 year=Integer.valueOf(ExcelUtil.manageCell(row.getCell(2), null));
						 }
						
						Integer jvalue=0;
						if(row.getCell(3)==null) {
							jvalue=0;
							
						}else {
						 jvalue=Integer.valueOf(ExcelUtil.manageCell(row.getCell(3), null));
						}
					
						
						student.setSex(sex);
						student.setUniversity(university);
						student.setYear(year);
						student.setJvalue(jvalue);		
						
						
						students.add(student);
					}
				}
			}
		}
		log.info("获取数据列表: {} ",students);
		return students;
	}
	
	/**
	 * 根据版本来区分获取workbook实例
	 * @param version
	 * @return
	 */
	public Workbook getWorkbook(String version,InputStream inputStream) throws Exception{
		Workbook wk=null;
		if (Objects.equals(WorkBookVersion.WorkBook2003.getCode(), version)) {
			wk=new HSSFWorkbook(inputStream);
		}else if (Objects.equals(WorkBookVersion.WorkBook2007.getCode(), version)) {
			wk=new XSSFWorkbook(inputStream);
		}
		
		return wk;
	}
	
	/**
	 * 根据file区分获取workbook实例
	 * @param version
	 * @return
	 */
	public Workbook getWorkbook(MultipartFile file,String suffix) throws Exception{
		Workbook wk=null;
		if (Objects.equals(WorkBookVersion.WorkBook2003Xls.getCode(), suffix)) {
			wk=new HSSFWorkbook(file.getInputStream());
		}else if (Objects.equals(WorkBookVersion.WorkBook2007Xlsx.getCode(), suffix)) {
			wk=new XSSFWorkbook(file.getInputStream());
		}
		
		return wk;
	}
}

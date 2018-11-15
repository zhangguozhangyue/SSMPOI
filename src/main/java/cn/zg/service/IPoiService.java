package cn.zg.service;

import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import cn.zg.model.Student;

public interface IPoiService {

	public List<Student> readExcelData(Workbook wb) throws Exception;
	
	public Workbook getWorkbook(String version,InputStream inputStream) throws Exception;
	
	public Workbook getWorkbook(MultipartFile file,String suffix) throws Exception;
}

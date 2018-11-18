package cn.zg.controller;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.zg.enums.StatusCode;
import cn.zg.enums.WorkBookVersion;
import cn.zg.mapper.StudentMapper;
import cn.zg.model.CityCount;
import cn.zg.model.EUDataGridResult;
import cn.zg.model.Student;
import cn.zg.model.UniversityCount;
import cn.zg.model.YearCount;
import cn.zg.response.BaseResponse;
import cn.zg.request.EntityRequest;
import cn.zg.request.StudentRequest;
import cn.zg.service.IPoiService;
import cn.zg.service.IStudentService;
import cn.zg.utils.DateUtil;
import cn.zg.utils.ExcelBeanUtil;
import cn.zg.utils.ExcelUtil;
import cn.zg.utils.WebUtil;



@Controller
public class StudentController {

	private static final Logger log=LoggerFactory.getLogger(StudentController.class);

	private static final String prefix="student";
	
	@Value("${poi.excel.sheet.name}")
	private String sheetProductName;
	
	@Value("${poi.excel.file.name}")
	private String excelProductName;
	
	@Autowired
	private StudentMapper studentMapper;
	
	@Autowired
	private IPoiService poiService;
	
	@Autowired
	private IStudentService studentService;
	
	
	

	/**
	 * 分页获取产品列表
	 * @param name
	 * @return
	 */
	
		@RequestMapping(value=prefix+"/list",method=RequestMethod.GET)
		@ResponseBody
		public EUDataGridResult listpage(String university,Integer page, Integer rows){
			List<Student> students=new ArrayList<Student>();
			EUDataGridResult result=null;
			try {
				university=StringUtils.isNotEmpty(university)?URLDecoder.decode(university,"UTF-8"):null;
				log.info(university);
				result=studentService.getStudentList(university,page, rows);
				
			} catch (Exception e) {
				log.error("获取产品列表发生异常: ",e.fillInStackTrace());
			}
			return result;
		}
	
	
	
	
	/**
	 * 获取产品列表
	 * @param name
	 * @return
	 */
	@RequestMapping(value=prefix+"/list1",method=RequestMethod.GET)
	@ResponseBody
	public List<Student> list(String university){
		System.out.println(university);
		List<Student> students=new ArrayList<Student>();
		try {
			university=StringUtils.isNotEmpty(university)?URLDecoder.decode(university,"UTF-8"):null;
			System.out.println(university);
			students=studentMapper.selectAll(university);
		} catch (Exception e) {
			log.error("获取产品列表发生异常: ",e.fillInStackTrace());
		}
		return students;
	}
	
	
	/**
	 * 获取年统计
	 * @param name
	 * @return
	 */
	@RequestMapping(value=prefix+"/year",method=RequestMethod.GET)
	@ResponseBody
	public List<YearCount> getYearCount(){
		 List<YearCount> yearCounts = new ArrayList<YearCount>();
		 yearCounts = studentMapper.getYearCount();
		 return yearCounts;
	}

	/**
	 * 获取大学统计
	 * @param name
	 * @return
	 */
	@RequestMapping(value=prefix+"/university",method=RequestMethod.GET)
	@ResponseBody
	public List<UniversityCount> getUniversityCount(){
		 List<UniversityCount> universityCounts = new ArrayList<UniversityCount>();
		 universityCounts = studentMapper.getUniversityCount();
		return universityCounts;
	}
	

	/**
	 * 获取各城市人数统计
	 * @param name
	 * @return
	 */
	@RequestMapping(value=prefix+"/cityCount",method=RequestMethod.GET)
	@ResponseBody
	public  List<CityCount>  getCityCount(){
		List<CityCount> cityCount = new ArrayList<CityCount>();
		List<UniversityCount> universityCounts = getUniversityCount();
		
		for(int i = 0; i < universityCounts.size(); i++) {
		    UniversityCount universityCount =  universityCounts.get(i);
			String university = universityCount.getUniversity();
			int count = Integer.parseInt(universityCount.getCount());
			String city = studentMapper.getCity(university);
			log.info(city + cityCount.size());
			if(cityCount.contains(city)) {
				CityCount cityInfo = new CityCount();
				cityInfo.setCity(city);
				cityInfo.setCount(cityInfo.getCount() + count);
				cityCount.add(cityInfo);
			}else {
				CityCount cityInfo = new CityCount();
				cityInfo.setCity(city);
				cityInfo.setCount(count);
				cityCount.add(cityInfo);
			}
		}
		return cityCount ;
	}
	
	
	
	
	/**
	 * 导出excel
	 * @param response
	 * @return
	 */
	@RequestMapping(value=prefix+"/excel/export",method=RequestMethod.GET)
	public @ResponseBody String exportExcel(HttpServletResponse response,String search){
		try {
			search=StringUtils.isNotEmpty(search)?URLDecoder.decode(search,"UTF-8"):null;
			List<Student> students=studentMapper.selectAll(search);
			System.out.println(students);
			String[] headers=new String[]{"编号","性别","大学","学院","入学年","J值"};
			List<Map<Integer, Object>> dataList=ExcelBeanUtil.manageProductList(students);
			log.info("excel下载填充数据： {} ",dataList);
			
			Workbook wb=new HSSFWorkbook(); 
			ExcelUtil.fillExcelSheetData(dataList, wb, headers, sheetProductName);
			
			WebUtil.downloadExcel(response, wb, excelProductName);
			return excelProductName;
		} catch (Exception e) {
			log.error("下载excel 发生异常：",e.fillInStackTrace());
		}
		return null;
	}
	
	
	/**
	 * 上传excel导入数据
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value=prefix+"/excel/upload",method=RequestMethod.POST,consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public BaseResponse uploadExcel(MultipartHttpServletRequest request){
		BaseResponse response=new BaseResponse<>(StatusCode.Success);
		try {
			MultipartFile file=request.getFile("productFile");
			if (file==null || file.getName()==null) {
				return new BaseResponse<>(StatusCode.Invalid_Param);
			}
			String fileName=file.getOriginalFilename();
			String suffix=StringUtils.substring(fileName, fileName.lastIndexOf(".")+1);
			if (WorkBookVersion.valueOfSuffix(suffix)==null) {
				return new BaseResponse<>(StatusCode.WorkBook_Version_Invalid);
			}
			log.info("文件名：{} 文件后缀名：{} ",fileName,suffix);
			
			Workbook wb=poiService.getWorkbook(file,suffix);
			
			List<Student> listStudent=poiService.readExcelData(wb);
			
			studentService.saveStudent(listStudent);
			//批量插入-第一种方法
			/*for(Product p:products){
				p.setIsDelete(0);
				productService.saveProduct(p);
			}*/
			
		} catch (Exception e) {
			log.error("上传excel导入数据 发生异常：",e.fillInStackTrace());
			return new BaseResponse<>(StatusCode.System_Error);
		}
		return response;
	}
	
	/**
	 * 删除
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value=prefix+"/delete",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse delete(@Validated @RequestBody EntityRequest request){
		BaseResponse response=new BaseResponse(StatusCode.Success);
		try {
			if (request.getId()==null || request.getId()<=0) {
				return new BaseResponse(StatusCode.Invalid_Param);
			}	
			
			Student p=studentMapper.selectByPrimaryKey(request.getId());
			if (p==null) {
				return new BaseResponse(StatusCode.Entity_Not_Exist);
			}
			p.setIsDelete(1);
			studentMapper.updateByPrimaryKeySelective(p);
			
		} catch (Exception e) {
			response=new BaseResponse(StatusCode.Fail);
			log.error("删除 发生异常： id={} ",request.getId(),e.fillInStackTrace());
		}
		return response;
	}
	
	/**
	 * 新增/更新
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value=prefix+"/insert/or/update",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse updateInsert(@Validated @RequestBody StudentRequest request){
		BaseResponse response=new BaseResponse(StatusCode.Success);
		try {
			if (StringUtils.isEmpty(request.getSex()) || StringUtils.isEmpty(request.getUniversity()) || request.getYear()==null
					|| request.getJvalue()==null) {
				return new BaseResponse(StatusCode.Invalid_Param);
			}
			System.out.println("保存成功");
			System.out.println(request);
			return studentService.insertUpdateProduct(request);
		} catch (Exception e) {
			response=new BaseResponse(StatusCode.Fail);
			log.error("新增/更新 发生异常： id={} ",request.getId(),e.fillInStackTrace());
		}
		return response;
	}
	
}












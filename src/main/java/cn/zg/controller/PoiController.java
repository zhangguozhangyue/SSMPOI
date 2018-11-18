package cn.zg.controller;
/** 
* @author 作者 zg
* @version 创建时间：2018年11月11日 下午4:54:27 
*/
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.zg.enums.StatusCode;
import cn.zg.mapper.StudentMapper;
import cn.zg.model.Student;
import cn.zg.response.BaseResponse;
import cn.zg.service.IPoiService;
import cn.zg.utils.ExcelBeanUtil;
import cn.zg.utils.ExcelUtil;
import cn.zg.utils.WebUtil;

/**
 * 导入导出controller
 * @authorzhonglinsen
 *
 */
@Controller
public class PoiController {
	
	private static final Logger log=LoggerFactory.getLogger(PoiController.class);

	private static final String prefix="poi";
	
	@Autowired
	private StudentMapper studentMapper;
	
	@Autowired
	private IPoiService poiService;
	
	@Value("${poi.excel.sheet.name}")
	private String sheetProductName;
	
	@Value("${poi.excel.file.name}")
	private String excelStudentName;
	
	/**
	 * 获取产品列表可用于搜索
	 * @param name
	 * @return
	 */
	@RequestMapping(value=prefix+"/list",method=RequestMethod.GET)
	@ResponseBody
	public BaseResponse<List<Student>> list(String sex){
		BaseResponse<List<Student>> response=new BaseResponse<List<Student>>(StatusCode.Success);
		try {
			List<Student> students=studentMapper.selectAll(sex);
			response.setData(students);
		} catch (Exception e) {
			log.error("获取产品列表发生异常: ",e.fillInStackTrace());
		}
		
		return response;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value=prefix+"/excel/data/list",method=RequestMethod.GET)
	@ResponseBody
	public BaseResponse excelDataList(){
		BaseResponse response=new BaseResponse(StatusCode.Success);
		try {
			List<Student> students=studentMapper.selectAll(null);
			List<Map<Integer, Object>> dataList=ExcelBeanUtil.manageProductList(students);
			response.setData(dataList);
		} catch (Exception e) {
			log.error("获取产品列表发生异常: ",e.fillInStackTrace());
		}
		
		return response;
	}
	
	/**
	 * 下载excel
	 * @param response
	 * @return
	 */
	@RequestMapping(value=prefix+"/excel/export",method=RequestMethod.GET)
	public @ResponseBody String exportExcel(HttpServletResponse response,String search){
		try {
			List<Student> students=studentMapper.selectAll(search);
			String[] headers=new String[]{"编号","性别","大学","入学年","J值"};
			List<Map<Integer, Object>> dataList=ExcelBeanUtil.manageProductList(students);
			log.info("excel下载填充数据： {} ",dataList);
			
			Workbook wb=new HSSFWorkbook();
			ExcelUtil.fillExcelSheetData(dataList, wb, headers, sheetProductName);
			WebUtil.downloadExcel(response, wb, excelStudentName);
			return excelStudentName;
		} catch (Exception e) {
			log.error("下载excel 发生异常：",e.fillInStackTrace());
		}
		return null;
	}
	
	/**
	 * 上传excel导入数据
	 * @param request
	 * @return
	 * 1、不要忘了支持springmvc上传文件的配置
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value=prefix+"/excel/upload",method=RequestMethod.POST,consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public BaseResponse uploadExcel(MultipartHttpServletRequest request){
		BaseResponse response=new BaseResponse<>(StatusCode.Success);
		try {
			String version=request.getParameter("version");
			MultipartFile file=request.getFile("productFile");
			if (StringUtils.isEmpty(version) || file==null) {
				return new BaseResponse<>(StatusCode.Invalid_Param);
			}
			log.debug("版本号：{} file：{} ",version,file);
			
			HSSFWorkbook wb=new HSSFWorkbook(file.getInputStream());
			List<Student> students=poiService.readExcelData(wb);
			studentMapper.insertBatch(students);
		} catch (Exception e) {
			log.error("上传excel导入数据 发生异常：",e.fillInStackTrace());
			return new BaseResponse<>(StatusCode.System_Error);
		}
		return response;
	}
	
	
	
}































package cn.zg.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.zg.enums.StatusCode;
import cn.zg.mapper.StudentMapper;
import cn.zg.model.EUDataGridResult;
import cn.zg.model.Student;
import cn.zg.request.StudentRequest;
import cn.zg.response.BaseResponse;
import cn.zg.service.IStudentService;
import cn.zg.utils.DateUtil;

@Service("productService")
public class StudentService implements IStudentService{

	private static final Logger log=LoggerFactory.getLogger(StudentService.class);
	
	@Autowired
	private StudentMapper studentMapper;
	
	
	
	 public EUDataGridResult getStudentList(String university,int page, int rows) {
	        //查询商品列表
	        Student example = new Student();
	        //分页处理
	        PageHelper.startPage(page, rows);
	        List<Student> list = studentMapper.selectByExample(university);
//	        log.info(list.toString());
	        //创建一个返回值对象
	        EUDataGridResult result = new EUDataGridResult();
	        result.setRows(list);
	        //取记录总条数
	        PageInfo<Student> pageInfo = new PageInfo<>(list);
	        result.setTotal(pageInfo.getTotal());
	        return result;
	    }

	
	
	public void saveStudent(List<Student> listStudent){
		if (listStudent!=null) {
			log.debug("待插入的数据： {} ");
			
			studentMapper.insertBatch(listStudent);;
		}
	}

	/**
	 * 新增/更新
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public BaseResponse insertUpdateProduct(StudentRequest request) {
		BaseResponse res=new BaseResponse(StatusCode.Success);
		try {
			/*Date purchaseDate=DateUtil.strToDate(request.getPurchaseDate(), "yyyy-MM-dd");*/
			
			Student p;
			if (request.getId()!=null && request.getId()>0) {
				p=studentMapper.selectByPrimaryKey(request.getId());
				if (p==null) {
					return new BaseResponse(StatusCode.Entity_Not_Exist);
				}
				BeanUtils.copyProperties(request, p);
				p.setId(request.getId());
		/*		p.setPurchaseDate(purchaseDate);*/
				studentMapper.updateByPrimaryKeySelective(p);
				
			}else{
				p=new Student();
				BeanUtils.copyProperties(request, p);
				/*p.setPurchaseDate(purchaseDate);*/
				studentMapper.insertSelective(p);
			}
			
		} catch (Exception e) {
			log.error("新增/更新 发生异常： {} ",request,e.fillInStackTrace());
			return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
		}
		
		return res;
	}
	
	
}



















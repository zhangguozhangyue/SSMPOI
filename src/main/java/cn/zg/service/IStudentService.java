package cn.zg.service;

import java.util.List;

import cn.zg.model.Student;
import cn.zg.request.StudentRequest;
import cn.zg.response.BaseResponse;

public interface IStudentService {

	public void saveStudent(List<Student> listStudent);
	
	public BaseResponse insertUpdateProduct(StudentRequest request);
	
}

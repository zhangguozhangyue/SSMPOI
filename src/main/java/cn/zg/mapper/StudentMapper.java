package cn.zg.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.zg.model.Student;
import cn.zg.model.UniversityCount;
import cn.zg.model.YearCount;

public interface StudentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Student record);

    int insertSelective(Student record);

    Student selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Student record);

    int updateByPrimaryKey(Student record);
    
    List<Student> selectAll(@Param("university") String university);
    
    void insertBatch(@Param("dataList") List<Student> studentProduct);
    
   List<YearCount> getYearCount();
    
    List<UniversityCount> getUniversityCount();
}
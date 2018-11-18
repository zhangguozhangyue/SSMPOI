package cn.zg.model;

import java.util.List;

/** 
* @author 作者 zg
* @version 创建时间：2018年11月17日 下午3:43:55 
*/
public class EUDataGridResult {
	
	private long total;
    private List<?> rows;

    public long getTotal() {
        return total;
    }
    public void setTotal(long total) {
        this.total = total;
    }
    public List<?> getRows() {
        return rows;
    }
    public void setRows(List<?> rows) {
        this.rows = rows;
    }   
}

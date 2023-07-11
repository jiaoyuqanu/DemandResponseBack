package com.xqxy.dr.modular.data.result;

import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.result.centerResult.DataAccessHistoryCurve;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 模拟数据接口返回的实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimulationResult {
   /* private Boolean isSuccessful;
    private String Message;
    private String status;*/
    private ConsCurve curve;
    public class Data {
       public Integer getPageNum() {
           return pageNum;
       }

       public void setPageNum(Integer pageNum) {
           this.pageNum = pageNum;
       }

       public Integer getPages() {
           return pages;
       }

       public void setPages(Integer pages) {
           this.pages = pages;
       }

       public Integer getTotal() {
           return total;
       }

       public void setTotal(Integer total) {
           this.total = total;
       }

       public Integer getPageSize() {
           return pageSize;
       }

       public void setPageSize(Integer pageSize) {
           this.pageSize = pageSize;
       }

        public List<DataAccessHistoryCurve> getList() {
            return list;
        }

        public void setList(List<DataAccessHistoryCurve> list) {
            this.list = list;
        }

        Integer pageNum;
       Integer pages;
       Integer total;
       Integer pageSize;
       List<DataAccessHistoryCurve> list;
   }

   private String message;

   private String code;

   private Data data;
}

package com.cf.gp.dao;

import com.cf.gp.model.StockYes;

public interface StockYesMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_stock_yes
     *
     * @mbggenerated Tue Jan 09 14:17:56 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_stock_yes
     *
     * @mbggenerated Tue Jan 09 14:17:56 CST 2018
     */
    int insert(StockYes record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_stock_yes
     *
     * @mbggenerated Tue Jan 09 14:17:56 CST 2018
     */
    int insertSelective(StockYes record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_stock_yes
     *
     * @mbggenerated Tue Jan 09 14:17:56 CST 2018
     */
    StockYes selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_stock_yes
     *
     * @mbggenerated Tue Jan 09 14:17:56 CST 2018
     */
    int updateByPrimaryKeySelective(StockYes record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_stock_yes
     *
     * @mbggenerated Tue Jan 09 14:17:56 CST 2018
     */
    int updateByPrimaryKey(StockYes record);
    
}
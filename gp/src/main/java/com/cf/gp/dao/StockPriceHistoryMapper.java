package com.cf.gp.dao;

import com.cf.gp.model.StockPriceHistory;

public interface StockPriceHistoryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_stock_price_history
     *
     * @mbggenerated Tue Jan 09 14:17:07 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_stock_price_history
     *
     * @mbggenerated Tue Jan 09 14:17:07 CST 2018
     */
    int insert(StockPriceHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_stock_price_history
     *
     * @mbggenerated Tue Jan 09 14:17:07 CST 2018
     */
    int insertSelective(StockPriceHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_stock_price_history
     *
     * @mbggenerated Tue Jan 09 14:17:07 CST 2018
     */
    StockPriceHistory selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_stock_price_history
     *
     * @mbggenerated Tue Jan 09 14:17:07 CST 2018
     */
    int updateByPrimaryKeySelective(StockPriceHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_stock_price_history
     *
     * @mbggenerated Tue Jan 09 14:17:07 CST 2018
     */
    int updateByPrimaryKey(StockPriceHistory record);
}
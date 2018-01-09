package com.cf.gp;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.cf.gp.model.StockTranformVo;

public class HttpClientTest {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		
		/**
		 * https://www.zhihu.com/question/22145919
		 * 新浪的PE为静态PE，PB数据也不是很准确，在股票发生除权时未能进行修正
		 * 实时：http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeData?page=1&num=100&sort=symbol&asc=1&node=hs_a&symbol=&_s_r_a=init
		 * 指数历史：http://q.stock.sohu.com/hisHq?code=zs_000001&start=20000504&end=20151215&stat=1&order=D&period=d&callback=historySearchHandler&rt=jsonp&r=0.8391495715053367&0.9677250558488026
		 * 个股历史：http://q.stock.sohu.com/hisHq?code=cn_601989&start=20180101&end=20180109&stat=1&order=D&period=d&callback=historySearchHandler&rt=jsonp&r=0.8391495715053367&0.9677250558488026
		 */
		
		/**
		 * 东方财富网：
		 * 沪市：
		 * http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx?type=CT&cmd=C.2&sty=FCOIATA&sortType=C&sortRule=-1&page=1&pageSize=3000&js=var%20quote_123%3d{rank:[(x)],pages:(pc)}&token=7bc05d0d4c3c22ef9fca8c2a912d779c&jsName=quote_123&_g=0.13470771381214863
		 * 深市：
		 * http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx?type=CT&cmd=C._SZAME&sty=FCOIATA&sortType=C&sortRule=-1&page=1&pageSize=20&js=var%20quote_123%3d{rank:[(x)],pages:(pc)}&token=7bc05d0d4c3c22ef9fca8c2a912d779c&jsName=quote_123&_g=0.13736001542082255
		 */
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet("http://q.stock.sohu.com/hisHq?code=cn_601989&start=20180101&end=20180109&stat=1&order=D&period=d&callback=historySearchHandler&rt=jsonp&r=0.8391495715053367&0.9677250558488026");
		CloseableHttpResponse resp = client.execute(get);
		HttpEntity entity = resp.getEntity();
		String result = EntityUtils.toString(entity);
		System.out.println(result);
		String substring = result.substring(result.indexOf("\"hq\":[")+"\"hq\":[".length(), result.indexOf("],\"code\""));
		String[] split = substring.replace("],", "").replace("]", "").replace("\"", "").substring(1).replace("[", "@").split("@");
		System.out.println(split);
	}
	
	
	
	@Test
	public void aa() {
		String url = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeData?page=$start_page&num=10&sort=symbol&asc=1&node=hs_a&symbol=&_s_r_a=init";
		System.out.println(url);
		System.out.println(url.replace("$start_page", "11"));
		System.out.println(url.contains("$start_page11"));
		
	}
	
}

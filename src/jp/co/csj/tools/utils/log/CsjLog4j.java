
package jp.co.csj.tools.utils.log;

import org.apache.log4j.Logger;

/**
 *
 * ログファイル処理作成
 *
 */

public class CsjLog4j {
	
	public static Logger logger = Logger.getLogger(CsjLog4j.class); 

	public static void main(String [] args){
		
		//BasicConfigurator.configure(); //自动快速地使用缺省Log4j环境。
		
		logger.info("aaaa");
		
		logger.warn("bbbb");
		
		logger.error("cccc");
		
	}
}

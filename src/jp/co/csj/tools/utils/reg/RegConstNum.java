/**
 *
 */
package jp.co.csj.tools.utils.reg;

/**
 * @author csj
 *
 */
public class RegConstNum {

	// 非负整数（正整数 +0） 
	public static final String NUM_01 = "^\\d+$";

	// 正整数 
	public static final String NUM_02 = "^[0-9]*[1-9][0-9]*$";

	// 非正整数（负整数 +0） 
	public static final String NUM_03 = "^((-\\d+)|(0+))$";

	// 负整数 
	public static final String NUM_04 = "^-[0-9]*[1-9][0-9]*$";

	// 整数 
	public static final String NUM_05 = "^-?\\d+$";

	// 非负浮点数（正浮点数 +0） 
	public static final String NUM_06 = "^\\d+(\\.\\d+)?$";

	// 正浮点数 
	public static final String NUM_07 = "^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$";

	// 非正浮点数（负浮点数 +0） 
	public static final String NUM_08 = "^((-\\d+(\\.\\d+)?)|(0+(\\.0+)?))$";

	// 负浮点数 
	public static final String NUM_09 = "^(-(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*)))$";

	// 浮点数 
	public static final String NUM_10 = "^(-?\\d+)(\\.\\d+)?$";

	// 整数或者小数
	public static final String NUM_11 = "^[0-9]+\\.{0,1}[0-9]{0,2}$";

	// 只能输入数字
	public static final String NUM_12 = "^[0-9]*$";

	// 只能输入n位的数字
	public static final String NUM_13 = "^\\d{n}$";

	// 只能输入至少n位的数字
	public static final String NUM_14 = "^\\d{n,}$";

	// 只能输入m~n位的数字
	public static final String NUM_15 = "^\\d{m,n}$";

	// 只能输入零和非零开头的数字
	public static final String NUM_16 = "^(0|[1-9][0-9]*)$";

	// 只能输入有两位小数的正实数
	public static final String NUM_17 = "^[0-9]+(.[0-9]{2})?$";

	// 只能输入有1~3位小数的正实数
	public static final String NUM_18 = "^[0-9]+(.[0-9]{1,3})?$";

	// 只能输入非零的正整数
	public static final String NUM_19 = "^\\+?[1-9][0-9]*$";
	public static void main(String[] args) {
		System.out.println("aaaa123,(3.3,1)9dddd".replaceAll("[0-9]|\\.|\\(|\\)|,", ""));
	}

}

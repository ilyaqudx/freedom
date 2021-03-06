package lesson1.question2;

import lesson1.BytesUtils;

public class Application {
	/**
		 * 基本概念
		 * 1-JAVA中正数的二进制表示,可以直接进行计算(正数的原码,反码,补码都相同)
		 * 2-JAVA中通过Integer.toBinaryString方法打印的二进制是补码,转换为原码后,计算出10进制值再取反即为负数的值
		 * 3-JAVA正数计算相反数的过程是:
		 * 	3.1 : 所有位进行取反(0变1,1变0)
		 *  3.2 : 然后再加1即是正数的相反数的补码(即JAVA中负数是通过补码表示,但负数的值还是通过原码计算出真值再加负号)
		 * 4-如果一个数是有符号的,最高位为符号位,计算反码时最高位不取反,二进制则为它的补码
		 *  4.1 : 如果最高位为0则表示正数,直接用补码进行算值即可
		 *  4.2 : 如果最高位为1则表示负数,需要将补码转换为原码后算出真值,再取反
		 * 5-如果一个数是无符号的,则全部为正数,直接计算二进制值即可
		 * 6-针对有符号的数值,在JAVA定义为正数时,如果最高位为1了,真实值则为一个负数,那么此时打印出的二进制数则是负数的补码,
		 *   负数的真值则需要先计算出原码再算,最后再取反
		 *   示例 ： byte a = 200;
		 *   	 				128 64 32 16 8 4 2 1  补码(认为是补码)
		 *   	    按正数的2进制应该为      1  1  0  0  1 0 0 0    
		 *   	  但是byte只有8位,此时最高位为1,表示符号位为负,则128不参与运算,所以真值应该先计算出原码
		 *   					  1 1  0  0  0 1 1 1   反码(补码-1)
		 *   					  1 0  1  1  1 0 0 0   原码	(最高位不变,其余位取反)
		 * 					  -     32 16 8 = - (32 + 16 + 8) = -56 	
		 * 
		 * 	上述计算过程是认为负数在JAVA中显示的2进制是其补码,但其实如果认为是其原码,然后计算它的补码,再用补码计算值也是相同的结果
		 * 	这个原理还需要再验证一下是为什么.下面从原码 ->补码进行验证
		 * 	示例 ： byte a = 200;
		 *   	 				128 64 32 16 8 4 2 1 原码(认为是原码)
		 *   	    按正数的2进制应该为      1  1  0  0  1 0 0 0    
		 *   	  但是byte只有8位,此时最高位为1,表示符号位为负,则128不参与运算,所以真值应该先计算出补码
		 *   					  1 0  1  1  0 1 1 1   反码(最高位不变,其余位取反)
		 *   					  1 0  1  1  1 0 0 0   补码	(反码加1)
		 * 					  -     32 16 8 = - (32 + 16 + 8) = -56 
		 *   到底哪个才是正确的呢?
		 * */
		public static final void main(String[] args)
		{
			int v  = -1024;
			int v2 = v >> 1;
			int v3 = v >>> 1;
			
			/**
			 * 0      8        16       24       31
			 * -----------------------------------
			 * |      |        |        |        |     
			 * -----------------------------------
			 * 11111111 11111111 11111100 00000000   -1024
			 * -----------------------------------
			 * 11111111 11111111 11111110 00000000   -1024 >> 1 = -512
			 * -----------------------------------
			 * 01111111 11111111 11111110 00000000   -1024 >>> 1 = 2147483136
			 * -----------------------------------
			 * */
			System.out.println(Integer.toBinaryString(v));
			System.out.println(Integer.toBinaryString(v2));
			System.out.println(Integer.toBinaryString(v3));
			System.out.println(v3);
			byte[] bytes = new byte[]{127,-1,-2,0};
			System.out.println(BytesUtils.readInt(bytes));
		}
}


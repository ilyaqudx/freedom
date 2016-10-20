package freedom.common.image;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
public class RotateImage {

	/**
	 * 但该方法还有一个问题就是不支持输入负数角度,另外背景颜色为黑色.有时间再来调试
	 * */
	public static BufferedImage Rotate(Image src, int angel) {
		int src_width = src.getWidth(null);
		int src_height = src.getHeight(null);
		// calculate the new image size
		//-1关键点在于计算转换后图片内容旋转的真正角度,否则转换后内容不对
		Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(
				src_width, src_height)), angel);

		BufferedImage res = null;
		res = new BufferedImage(rect_des.width, rect_des.height,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2 = res.createGraphics();
		// transform
		g2.setBackground(Color.pink);
		//-2正式转换为旋转要转换的角度 --重点,否则转换后内容不对
		g2.translate((rect_des.width - src_width) / 2,
				(rect_des.height - src_height) / 2);
		g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);

		g2.drawImage(src, 0,0, null);
		return res;
	}

	/**
	 * 计算转换后的真正角度,重要
	 * */
	public static Rectangle CalcRotatedSize(Rectangle src, int angel) {
		// if angel is greater than 90 degree, we need to do some conversion
		//兼容输入负角度
		angel = angel < 0 ? 360 + angel : angel;
		if (angel >= 90) {
			if(angel / 90 % 2 == 1){
				int temp = src.height;
				src.height = src.width;
				src.width = temp;
			}
			angel = angel % 90;
		}

		double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
		double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
		double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
		double angel_dalta_width = Math.atan((double) src.height / src.width);
		double angel_dalta_height = Math.atan((double) src.width / src.height);

		int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha
				- angel_dalta_width));
		int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha
				- angel_dalta_height));
		int des_width = src.width + len_dalta_width * 2;
		int des_height = src.height + len_dalta_height * 2;
		return new java.awt.Rectangle(new Dimension(des_width, des_height));
	}
}


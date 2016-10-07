package freedom.common.image;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Plist2LibgdxPack {

	/**
	 * 该种plist类型,单张图片如果rotate为true的话,在切割的时候需要转换一下宽高.否则会报错.
	 * */
	public static final int PLIST_TYPE_FRAMES = 1;
	public static final int PLIST_TYPE_ALIASES = 2;
	public static final String plistDir = "C:/Users/Administrator/Downloads/欢乐麻将/xzmj2/Resources/Image/Menu_new";
	public static final String resultDir = "C:/Users/Administrator/Downloads/欢乐麻将/xzmj2/Resources/Image/out";
	public static void main(String[] args) throws Exception{

		File file = new File(plistDir);
		if(file.exists()){
			File[] files = file.listFiles();
			for (File f : files) {
				if(f.getName().endsWith("plist"))
					parsePlist(f);
			}
		}
		
		System.out.println("转换成功===============");
	}
	
	@SuppressWarnings("unchecked")
	private static final void parsePlist(File plist) throws Exception{
		
		//-1创建plist文件解析器
		SAXReader reader = new SAXReader();
		//-2加载plist文件到解析器
		Document doc = reader.read(plist);
		//-3获取plist文件的根元素
		Element root = doc.getRootElement();
		String plistName = plist.getName();//plist文件名称
		System.out.println("plist name = " + plistName);
		System.out.println("root : " + root.getName());
		
		System.err.println("-------Elements----------");
		
		//-4获取根元素下面第一个dict元素
		Element firstDict = root.element("dict");
		//-5:获取firstDict下面的第一个key元素
		Element firstKey = firstDict.element("key");
		StringBuffer sb = new StringBuffer();
		if(null != firstKey){//判断如果firstKey下面第一个KEY元素不为空
			//find frames key,iterator all dict elements
			
			//-6:再获取firstDict下面的第一个dict元素
			Element framesList = firstDict.element("dict");
			if(null == framesList)return;
			
			List<Element> dictList = framesList.elements("dict");
			List<Element> smallImageNameList = framesList.elements("key");
			sb.append(plistName.replace("plist", "png")).append("\r\n");
			sb.append("format: RGBA8888\r\n");
			sb.append("filter: Linear,Linear\r\n");
			sb.append("repeat: none\r\n");
			
			//读取大图片
			BufferedImage image = ImageIO.read(new File(plistDir,plistName.replace("plist", "png")));
			int plistType = PLIST_TYPE_FRAMES;
			if(null != dictList){
				for (int i = 0;i < dictList.size();i++) {
					try {
						Element e = dictList.get(i);
						String name = smallImageNameList.get(i).getText();
						System.out.println("small image name : " + name);
						
						//找到所有的KEY,现在PLIST有两种描述文件.需要判断一下
						List<Element> keys = e.elements("key");
						//handle texture attrs
						List<Element> stringList = e.elements("string");
						
						if(null == keys || keys.isEmpty() ||
								null == stringList || stringList.isEmpty())return;
						
						
						String offset = "";
						String frame = "";
						String sourceSize = "";
						boolean rotate = false;
						if("aliases".equals(keys.get(0).getText())){
							 plistType = PLIST_TYPE_ALIASES;
							 offset = stringList.get(1).getText();
							 frame = stringList.get(4).getText();
							 sourceSize = stringList.get(3).getText();
							List<Element> all = e.elements();
							String lastElement = all.get(all.size() - 1).getName();//rotate
							 rotate = "true" == lastElement ? true :false;
						}else if("frame".equals(keys.get(0).getText())){
							plistType = PLIST_TYPE_FRAMES;
							Element rotateTrue = e.element("true");
							rotate = null == rotateTrue ? false : true;
							
							//has 4 string element
							frame = stringList.get(0).getText();
							offset = stringList.get(1).getText();
							
							//igenor the third element
							sourceSize = stringList.get(3).getText();
						}else{
							return;
						}
						
						System.out.println("rotate = " + rotate);
						
						System.out.println("frame = " + frame+",offset = " + offset+",sourceSize"+sourceSize);
						
						//right now handle
						sb.append(name.substring(0, name.length() - 4)).append("\r\n");//add image name,not contains shuffix
						sb.append("rotate: ").append(rotate).append("\r\n");//add rotate
						
						frame = frame.replace(" ", "");
						int index = frame.indexOf("},");
						String firstPart = frame.substring(0,index).replace("{", " ").trim();
						String secondPart = frame.substring(index + 3).replace("}", " ").trim();
						
						System.out.println("firstPart = " + firstPart);
						System.out.println("secondPart = " + secondPart);
						
						sb.append("xy: ").append(firstPart).append("\r\n");
						String size = sourceSize.substring(1, sourceSize.length()-1).replace(" ", "");
						sb.append("size: ").append(size).append("\r\n");
						sb.append("orig: ").append(secondPart).append("\r\n");//now region size
						sb.append("offset: ").append(offset.substring(1, offset.length()-1)).append("\r\n");
						sb.append("index: ").append("-1\r\n");
						
						//输出小图片
						String[] xy = firstPart.split(",");
						String[] sizeString = secondPart.split(",");
						int x = Integer.parseInt(xy[0]);
						int y = Integer.parseInt(xy[1]);
						int w = Integer.parseInt(sizeString[0]);
						int h = Integer.parseInt(sizeString[1]);
						
						//转换宽高
						if(rotate && PLIST_TYPE_FRAMES == plistType){
							int temp = w;
							w = h;
							h = temp;
						}
						
						BufferedImage subImage = image.getSubimage(x,y,w,h);
						BufferedImage flip = subImage;
						if(rotate){
							
							int degrss = -90;
							Rectangle rect_des = RotateImage.CalcRotatedSize(new Rectangle(new Dimension(
									w, h)), degrss);
							
							//subImage.
					        Graphics2D graphics2d;
					        (graphics2d = (flip = new BufferedImage(h,w,subImage.getType()))
					                .createGraphics()).setRenderingHint(
					                RenderingHints.KEY_INTERPOLATION,
					                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
					        
					        graphics2d.translate((rect_des.width - w) / 2,
									(rect_des.height - h) / 2);
					        
					        graphics2d.rotate(Math.toRadians(degrss), w / 2, h / 2);
					        graphics2d.drawImage(subImage, 0, 0, null);
					        graphics2d.dispose();
						}
						File newDir = new File(resultDir,plistName);
						if(!newDir.exists())
							newDir.mkdirs();
						ImageIO.write(flip,"png",new
								FileOutputStream(new File(newDir,name)));
						 
						System.out.println("=======================");
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
				}
			}
		}
	}
}

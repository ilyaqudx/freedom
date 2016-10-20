package freedom.common.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class LuaImagesSpliter {

	public static void main(String[] args) throws Exception 
	{
		File inputDir  = new File("D:/workspace/majiang/image-input");
		File outputDir = new File("D:/workspace/majiang/image-output");
		File[] files = inputDir.listFiles();
		for (File file : files) 
		{
			String name = file.getName();
			if(name.endsWith(".lua"))
			{
				LuaImagesSpliter spliter = new LuaImagesSpliter();
				String text = spliter.readLuaFile(file.getAbsolutePath());
				spliter.split(file,text,outputDir);
			}
		}
		
		
	}
	
	private void split(File luaFile,String text,File outputDir) 
	{
		File output = new File(outputDir,luaFile.getName());
		if(!output.exists())
			output.mkdirs();
		File sourceImageFile = null;
		try {
			sourceImageFile = new File(luaFile.getAbsolutePath().replace(".lua", ".png"));
		} 
		catch (Exception e) 
		{
			//e.printStackTrace();
			return;
		}
		BufferedImage image;
		try {
			image = ImageIO.read(sourceImageFile);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			return ; 
		}
		//以=号分隔
		int firstEqualsIndex = text.indexOf("=");
		text = text.substring(firstEqualsIndex + 1).trim();
		//System.out.println(text);
		int fromIndex = 0;
		while(true)
		{
			fromIndex = text.indexOf("[", fromIndex);
			if(-1 == fromIndex)
				//搜索结束
				break;
			int toIndex = text.indexOf("}",fromIndex);
			String item = text.substring(fromIndex,toIndex + 1);
			fromIndex = toIndex  + 1;
			//解析Item数据
			//System.out.println(item);
			String name = item.substring(item.indexOf("[") + 2, item.indexOf("]") - 1);
			String attrs = item.substring(item.indexOf("{")  +1,item.indexOf("}"));
			String[] attrss = attrs.split(",");
			Map<String,String> data = new HashMap<String,String>();
			data.put("name", name);
			for (String string : attrss) 
			{
				String str = string.trim();
				String[] arr = str.split("=");
				data.put(arr[0], arr[1]);
			}
			System.out.println(data);
			int x = Integer.parseInt(data.get("x"));
			int y = Integer.parseInt(data.get("y"));
			int w = Integer.parseInt(data.get("width"));
			int h = Integer.parseInt(data.get("height"));
			try {
				//w = Integer.parseInt(data.get("utWidth"));
				//h = Integer.parseInt(data.get("utHeight"));
			} catch (NumberFormatException e1) 
			{
				//e1.printStackTrace();
			}
			BufferedImage subImage = image.getSubimage(x,y,w,h);
			try {
				ImageIO.write(subImage,"png",new
						FileOutputStream(new File(output,name)));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String readLuaFile(String fileName) throws Exception
	{
		List<String> lines = Files.readAllLines(Paths.get(fileName));
		if(null == lines || lines.isEmpty())
			throw new Exception("文件内容为空");
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < lines.size(); i++) {
			sb.append(lines.get(i));
		}
		return sb.toString();
	}
}

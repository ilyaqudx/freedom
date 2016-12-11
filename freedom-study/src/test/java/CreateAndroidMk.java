import java.io.File;

public class CreateAndroidMk {

	public static void main(String[] args) {
		//m(new File("D:\\client\\Classes"));
		System.out.println(Integer.toBinaryString(20479));
	}
	
	public static final void m(File f)
	{
		File[] files = f.listFiles();
		for (File file : files) {
			if(file.isDirectory())
			{
				String pre = "$(LOCAL_PATH)/../../";
				String hou = " \\";
				int index = file.getAbsolutePath().indexOf("Classes");
				String n = file.getAbsolutePath().substring(index);
				String name = pre + n + hou;
				System.out.println(name);
				m(file);
			}
		}
	}
}

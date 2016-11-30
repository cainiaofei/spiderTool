package cn.nju.weldSrcInfo;


public class WeldMainCodeMethodCount {
	
	public static void demo() throws ClassNotFoundException{
		WeldMainCodeLinesCount dj = new WeldMainCodeLinesCount();
		System.out.println(dj.getClass().getClassLoader());
		Class<?> curClass = Class.forName("cn.nju.weldSrcInfo.WeldMainCodeLinesCount");
		int len = curClass.getDeclaredMethods().length;
		System.out.println(len);
	}
	
	public static void main(String[] args) throws ClassNotFoundException{
		demo();
	}
}

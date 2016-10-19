package test;
import java.io.InputStream;
import java.io.OutputStream;

import my.hehe.util.cmd.Cmd4ConcoleV2;


public class TestV2 extends Cmd4ConcoleV2{
	public TestV2(InputStream in) {
		super(in);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws Exception {
		new TestV2(System.in).start();;
		// while (true) {
		// Thread.sleep(1000);
		// }

	}

	@Override
	public void println(String info) {
		System.out.println(info);
		
	}

	@Override
	public void print(String info) {
		System.out.print(info);
	}
}

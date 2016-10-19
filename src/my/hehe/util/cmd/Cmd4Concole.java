package my.hehe.util.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public abstract class Cmd4Concole {
	public Cmd4Concole(InputStream in) {
		Runtime runtime = Runtime.getRuntime();
		Process pro = null;
		PrintWriter pw = null;
		BufferedReader br = null;
		Scanner scanner = new Scanner(in);
		Read r = null;
		Write w = null;
		Thread t_read = null;
		Thread t_write = null;
		try {
			pro = runtime.exec("cmd");

			pw = new PrintWriter(pro.getOutputStream());
			br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			r = new Read(br);
			w = new Write(pw, scanner);
			t_read = new Thread(r);
			t_write = new Thread(w);
			t_read.start();
			t_write.start();
			while ((!w.isClose()) || !r.isClose()) {
				if (w.isClose()) {
					r.close();
				}
				Thread.sleep(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!w.isClose()) {
				w.close();
			}
			if (!r.isClose()) {
				r.close();
			}
			if (pro != null) {
				pro.destroy();
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (pw != null) {
				pw.close();
			}

		}
	}

	public abstract void println(String info);

	public abstract void print(String info);

	class Read implements Runnable {
		private BufferedReader br;
		private boolean isClose = false;

		public Read(BufferedReader br) {
			this.br = br;
			
		}

		public boolean isClose() {
			return isClose;
		}

		public void close() {
			isClose = true;
		}

		@Override
		public void run() {
			String str;
			try {

				while (!isClose) {
					if (br.ready()) {
						str = br.readLine();
						if (!str.equals("")) {
							println("@: " + str);
						}
					} else {
						Thread.sleep(100);
					}
				}
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				println("@: client is closed");
			}
		}
	}

	class Write implements Runnable {
		private boolean isClose = false;
		private PrintWriter pw;
		private Scanner scanner;

		public void close() {
			isClose = true;
		}

		public boolean isClose() {
			return isClose;
		}

		public Write(PrintWriter pw, Scanner scanner) {
			this.pw = pw;
			this.scanner = scanner;
		}

		@Override
		public void run() {
			String str;

			while (!(str = scanner.nextLine()).equals("exit")) {
				pw.println(str);
				pw.flush();
			}
			isClose = true;
		}

	}

}

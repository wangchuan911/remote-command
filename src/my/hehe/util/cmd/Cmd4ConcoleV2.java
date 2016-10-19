package my.hehe.util.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public abstract class Cmd4ConcoleV2 {
	private WriteAndRead war = null;
	private Thread t_war = null;

	public Cmd4ConcoleV2(InputStream in) {

		try {

			war = new WriteAndRead("cmd", new Scanner(in));
			t_war = new Thread(war);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() throws Exception {
		if (war != null && t_war != null) {
			t_war.start();
		} else {
			throw new Exception("NullPointer");
		}
	}

	public abstract void println(String info);

	public abstract void print(String info);

	class WriteAndRead implements Runnable {
		private Process proc = null;
		private boolean isClosed = false;
		private PrintWriter pw;
		private BufferedReader br;
		private Scanner inputByscanner = null;
		private BufferedReader inputByStream = null;

		public void close() {
			isClosed = true;
		}

		public boolean isClosed() {
			return isClosed;
		}

		public WriteAndRead(String command, Object input) throws IOException {
			if (input instanceof Scanner)
				inputByscanner = (Scanner) input;
			else if (input instanceof BufferedReader) {
				inputByStream = (BufferedReader) input;
			}
			proc = Runtime.getRuntime().exec(command);
			this.pw = new PrintWriter(proc.getOutputStream());
			this.br = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));
		}

		@Override
		public void run() {
			try {
				Show show = new Show(br);
				Thread t = new Thread(show);
				t.start();

				String str;
				while (!isClosed
						&& (!(str = (inputByscanner != null) ? (inputByscanner
								.nextLine())
								: ((inputByStream != null) ? (inputByStream
										.readLine()) : "exit")).equals("exit"))) {
					pw.println(str);
					pw.flush();
				}
				show.close();
				while (!show.isClosed())
					;
				isClosed = true;
			} catch (Exception e) {
				// TODO: handle exception
			} finally {

				if (proc != null) {
					proc.destroy();
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

		class Show implements Runnable {
			private BufferedReader br;
			private volatile boolean isClosed = false;

			public Show(BufferedReader br) {
				this.br = br;

			}

			public boolean isClosed() {
				return isClosed;
			}

			public void close() {
				isClosed = true;
			}

			@Override
			public void run() {
				String str;
				try {

					while (!isClosed) {
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
	}

}

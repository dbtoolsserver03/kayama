package jp.co.csj.exe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class TestRenameInMultiThread {

	public AtomicInteger num = new AtomicInteger(0);
	public BufferedWriter writer = null;
	public File f;
	public FileOutputStream fos;
	public OutputStreamWriter osr;

	public static void main(String[] args) {

		TestRenameInMultiThread manager = new TestRenameInMultiThread();
		try {
			manager.open();
			for (int i = 0; i < 5; i++) {
				Thread t = new Thread(new MyWriteThread("thread-" + String.format("%02d", i + 1), manager));
				t.start();
			}
			Thread.sleep(1000);
			for (int i = 0; i < 3; i++) {
				manager.runRename();
				Thread.sleep(1000);

			}
			System.exit(0);
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	public void write(String str) throws Throwable {
		synchronized (this) {
			try {
				if (writer == null) {
					return;
				}
				writer.write(str);
				writer.newLine();
				writer.flush();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private void runRename() throws Throwable {

		close();
		rename();
		open();

	}

	private void rename() {

		f.renameTo(new File("output_" + String.format("%020d", num.intValue()) + ".txt"));
	}


	private void open() throws Throwable {

		if (f == null) {
			f = new File("output.txt");
		}
		if (fos == null) {
			fos = new FileOutputStream(f);
		}
		if (osr == null) {
			osr = new OutputStreamWriter(fos, "utf-8");
		}
		if (writer == null) {
			writer = new BufferedWriter(osr);
		}
	}
	
	private void close() throws Throwable {

		synchronized (this) {
			if (writer != null) {
				writer.close();
				writer=null;
			}
			if (osr != null) {
				osr.close();
				osr=null;
			}
			if (fos != null) {
				fos.close();
				fos=null;
			}

		}
	}


}

class MyWriteThread implements Runnable {

	private String title;
	TestRenameInMultiThread manager;


	public MyWriteThread(String title, TestRenameInMultiThread manager) {
		this.title = title;
		this.manager = manager;
	}


	@Override
	public void run() {

		try {
			while (true) {
				String strDate = getFormatDateTime(new Date(),"yyyy/MM/dd HH:mm:ss.SSS");
				manager.write(title + String.format("%07d", manager.num.incrementAndGet()) + "-->"+strDate);
				
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}


	private String getFormatDateTime(Date date, String format) {
		SimpleDateFormat ft = new SimpleDateFormat(format);
		return ft.format(date);
	}

}
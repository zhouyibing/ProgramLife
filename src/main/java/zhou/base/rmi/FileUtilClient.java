package zhou.base.rmi;

import java.io.BufferedOutputStream;

import java.io.File;

import java.io.FileOutputStream;

import java.rmi.Naming;
/**
 * 客户端可以远程调用远程接口(FileInterface)中的任何一个方法。
 * 无论如何实现，客户端必须先从RMI注册工具中获取一个远程对象的引用。当引用获得后，方法downloadFile被调用。
 * 在执行过程中，客户端从命令行中获得两个参数，第一个是要下载的文件名,第二个是要下载的机器的地址，在对应地址的机器上运行服务端。
 ***
 */
public class FileUtilClient {

	public static void main(String args[]) {

		if (args.length != 3) {

			System.out.println("第一个参数：RMI服务的IP地址");

			System.out.println("第二个参数：要下载的文件名");

			System.out.println("第三个参数：要文件保存位置");

			System.exit(0);

		}

		try {

			String name = "rmi://" + args[0] + "/FileUtilServer";

			IFileUtil fileUtil = (IFileUtil) Naming.lookup(name);

			byte[] filedata = fileUtil.downloadFile(args[1]);

			if (filedata == null) {

				System.out.println("Error：<filedata is null!>");

				System.exit(0);

			}

			File file = new File(args[2]);

			System.out.println("file.getAbsolutePath() = "
					+ file.getAbsolutePath());

			BufferedOutputStream output = new BufferedOutputStream(

			new FileOutputStream(file.getAbsolutePath()));

			output.write(filedata, 0, filedata.length);

			output.flush();

			output.close();

			System.out.println("~~~~~End~~~~~");

		} catch (Exception e) {

			System.err.println("FileUtilServer exception: " + e.getMessage());

			e.printStackTrace();

		}

	}

}

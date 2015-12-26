package zhou.base.rmi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * 　类FileImpl继承于UnicastRemoteObject类。这显示出FileImpl类是用来创建一个单独的、不能复制的、远程的对象，
 * 这个对象使用RMI默认的基于TCP的通信方式
 *
 ***
 */
public class FileUtilImpl extends UnicastRemoteObject implements IFileUtil{

	private static final long serialVersionUID = -8481897581846476020L;

	protected FileUtilImpl() throws RemoteException {
		super();
	}

	@Override
	public byte[] downloadFile(String fileName) throws RemoteException {
		File file = new File(fileName);
		byte buffer[] = new byte[(int) file.length()];
		int size = buffer.length;
		System.out.println("download file size = "+size +"b");
		if(size>1024*1024*10){//限制文件大小不能超过10M，文件太大可能导制内存溢出！
		throw new RemoteException("Error:<The File is too big!>");
		}

		try {
		BufferedInputStream input = new BufferedInputStream(new FileInputStream(fileName));
		input.read(buffer, 0, buffer.length);
		input.close();
		System.out.println("Info:<downloadFile() had execute successful!>");
		return buffer;
		} catch (Exception e) {
		System.out.println("FileUtilImpl: " + e.getMessage());
		e.printStackTrace();
		return null;
		}
	}

}

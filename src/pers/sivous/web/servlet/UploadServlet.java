package pers.sivous.web.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/servlet/UploadServlet")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List type = Arrays.asList(".txt",".avi");
		
		try {//初始化
			System.out.println("10");
			DiskFileItemFactory factory = new DiskFileItemFactory();
			//缓存
			factory.setSizeThreshold(1024*1024);
			//缓存位置
			factory.setRepository(new File(this.getServletContext().getRealPath("/WEB-INF/temp")));
			
			ServletFileUpload upload = new ServletFileUpload(factory);
			//文件编码
			upload.setHeaderEncoding("utf-8");
			//设置监听器
			upload.setProgressListener(new ProgressListener() {
				
				@Override
				public void update(long arg0, long arg1, int arg2) {
					System.out.println("已经上传了"+arg0);
					
				}
			});
			
			if (!upload.isMultipartContent(request)) {
				System.out.println(request.getParameter("name"));
				return;
			} 
			
			//获得提交数据
			List<FileItem> list = upload.parseRequest(request);
			//数据
			for (FileItem item:list) {//单纯值
				if(item.isFormField()) {
					String name = item.getString();
					String nameValue = new String(name.getBytes("iso8859-1"),"utf-8");
					
					System.out.println(item.getFieldName());
					System.out.println(nameValue);
				}
//				if (item.getSize()>(1024*1024*5)) {//大小判断
//					request.setAttribute("Invalid", "beyond the limit of size");
//					request.getRequestDispatcher("/message.jsp").forward(request, response);
//
//					return;
//				}
				else {//其他数据
				
					//类型判断
					String name = item.getName().substring(item.getName().lastIndexOf(File.separator)+1);
					
					if (item.getName().lastIndexOf(".")!=-1) {
						String fileType = item.getName().substring(item.getName().lastIndexOf("."));
						
						System.out.println(fileType);
						
						if(!(type.contains(fileType))) {
							request.setAttribute("Invalid", "the type of this file is illegal");
							request.getRequestDispatcher("/message.jsp").forward(request, response);
							
						return;
						}
					}
					
					InputStream in = item.getInputStream();
					int len = 0;
					byte buffer[] = new byte[1024];
					//String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
					//设置上传目录
					FileOutputStream out = new FileOutputStream(generateSavePathByHas(name)+File.separator+generateUUID(name));
					while ((len = in.read(buffer))>0) {
						out.write(buffer,0,len);
					}
					in.close();
					out.close();
					
					item.delete();				
				}
			}
			
		}
		catch (FileUploadBase.FileSizeLimitExceededException e) {
			// TODO: handle exception
			response.getWriter().println("beyond the limit of size");
			return;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
//		InputStream in = request.getInputStream();
//		int len = 0;
//		byte buffer[] = new byte[1024];
//		while ((len = in.read(buffer))!= 0) {
//			System.out.println(new String(buffer));
//		}
//		
//		System.out.println(request.getParameter("username"));
		
		request.getRequestDispatcher("/upload.jsp").forward(request, response);
	}
	public String generateUUID(String filename) {
		return UUID.randomUUID()+"_"+filename;
	}
//	public String generateSavePath() {
//		
//		String date = new SimpleDateFormat("yyy/MM/dd").format(new Date());
//		String path = this.getServletContext().getRealPath("/WEB-INF/upload")+File.separator+date;
//		
//		File file = new File(path);
//		if(!file.exists()) {
//			file.mkdirs();
//		}
//		
//		return path;
//	}
	//使用hash值作为目录名称
	public String generateSavePathByHas(String name) {
		//生成hash
		int hashcode = name.hashCode();
		int dir1 = hashcode&15;
		int dir2 = (hashcode>>4)&0xF;
		
		String path = this.getServletContext().getRealPath("/WEB-INF/upload")+File.separator+dir1+File.separator+dir2;
		File file = new File(path);
		if(!file.exists()) {
			file.mkdirs();
		}
		return path;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

package pers.sivous.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DownLoadServlet
 */
@WebServlet("/servlet/DownLoadServlet")
public class DownLoadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownLoadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String filename = request.getParameter("filename");
		filename = new String(filename.getBytes("iso8859-1"),"utf-8");
		//原本的文件名
		String oldFilename = filename.substring(filename.lastIndexOf("_")+1);
		String path = this.getServletContext().getRealPath("/WEB-INF/upload")+File.separator+getPath(oldFilename);
		File file = new File(path+File.separator+filename);
		if(!file.exists()) {
			request.setAttribute("Invalid", "404");
			request.getRequestDispatcher("/message.jsp").forward(request, response);
			return;
		}
		//得到原始文件名
		String oldname = file.getName().substring(file.getName().lastIndexOf("_")+1);
		
		response.setHeader("content-disposition", "attachment;filename="+URLEncoder.encode(oldname, "utf-8"));
		
		FileInputStream in = new FileInputStream(file);
		int len = 0;
		byte buffer[] = new byte[1024];
		OutputStream out = response.getOutputStream();
		while((len = in.read(buffer))>0) {
			out.write(buffer, 0, len);
		}
		in.close();
		
	}
	//根据hash值生成文件所保存的路径
	public String getPath(String filename) {
		int hashcode = filename.hashCode();
		int dir1 = hashcode&15;
		int dir2 = (hashcode>>4)&0xF;
		
		return dir1 + File.separator +dir2;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

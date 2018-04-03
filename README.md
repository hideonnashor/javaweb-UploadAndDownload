# uploadAndDownload
基于tomcat服务器的文件上传下载
UploadServlet用于处理上传请求，ListFileServlet用于列出可下载项，DownLoadServlet用于处理下载请求

* 判断表单类型
* 设置缓冲区
* 限定文件类型
* 利用文件名生成hash值为保存目录
* 监听上传进度
* 解决上传时服务器重复读取输入字节流的问题

package ru.rt.fsom.wfc.wfcservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/upload")
public class UploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1224672188334273288L;
    protected static final Logger LOGGER = Logger.getLogger(UploadServlet.class.getName());
       
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	setStatusResponse(response, "The file has been successfully uploaded to the minio storage!", HttpServletResponse.SC_OK);
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>FSOM FileMinioServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>FileUploadServlet use method POST at " + request.getContextPath() + "</h1>");
	    out.println("<h2>Use multy part form data request!</h2>");
	    out.println("<h3>Specify the your auth token in the query parameter [JWT] or in the request header</h3>");
            out.println("<h3>Upload your file in the parameter [uploadfile]</h3>");
	    out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "ru.rt.fsom.s3attachservice servlet";
    }

    protected void setStatusResponse(HttpServletResponse response, String errMsg, int httpStatus) throws IOException{
	response.setStatus(httpStatus);
	response.setContentType("text/html;charset=UTF-8");
	try (PrintWriter out = response.getWriter()) {
	    out.println(errMsg);
	}
    }
}

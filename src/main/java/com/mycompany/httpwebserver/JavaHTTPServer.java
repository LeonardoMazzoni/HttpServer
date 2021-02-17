package com.mycompany.httpwebserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

// The tutorial can be found just here on the SSaurel's Blog : 
// https://www.ssaurel.com/blog/create-a-simple-http-web-server-in-java
// Each Client Connection will be managed in a dedicated Thread
public class JavaHTTPServer implements Runnable{ 
	
  private PuntiVendita pv;
  private Studenti s = new Studenti();
        byte[] fileData = null;
        int fileLength = 0;
        String content = null;
        boolean db = false;
	static final String WEB_ROOT = "/files";
	static final String DEFAULT_FILE = "index.html";
	static final String FILE_NOT_FOUND = "404.html";
	static final String METHOD_NOT_SUPPORTED = "not_supported.html";
        static final String PAGE_NOT_FOUND = "301.html";
	// port to listen connection
	static final int PORT = 3000;
	
	// verbose mode
	static final boolean verbose = true;
	
	// Client Connection via Socket Class
	private Socket connect;
	
	public JavaHTTPServer(Socket c) {
		connect = c;
	}
	
	public static void main(String[] args) {
		try {
			ServerSocket serverConnect = new ServerSocket(PORT);
			System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");
			
			// we listen until user halts server execution
			while (true) {
				JavaHTTPServer myServer = new JavaHTTPServer(serverConnect.accept());
				
				if (verbose) {
					System.out.println("Connecton opened. (" + new Date() + ")");
				}
				
				// create dedicated thread to manage the client connection
				Thread thread = new Thread(myServer);
				thread.start();
			}
			
		} catch (IOException e) {
			System.err.println("Server Connection error : " + e.getMessage());
		}
	}

	@Override
	public void run() {
		// we manage our particular client connection
		BufferedReader in = null; PrintWriter out = null; BufferedOutputStream dataOut = null;
		String fileRequested = null;
		String nome;
                String cognome;
                
		try {
			// we read characters from the client via input stream on the socket
			in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
			// we get character output stream to client (for headers)
			out = new PrintWriter(connect.getOutputStream());
			// get binary output stream to client (for requested data)
			dataOut = new BufferedOutputStream(connect.getOutputStream());
			
			// get first line of the request from the client
			String input = in.readLine();
			// we parse the request with a string tokenizer
			StringTokenizer parse = new StringTokenizer(input);
			String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
			// we get file requested
			fileRequested = parse.nextToken().toLowerCase();
			
			// we support only GET and HEAD methods, we check
                        System.out.println(fileRequested);
			if (!method.equals("GET")  &&  !method.equals("HEAD")) {
				if (verbose) {
					System.out.println("501 Not Implemented : " + method + " method.");       
				}
				// we return the not supported file to the client
				byte[] fileData = readFileData(WEB_ROOT+METHOD_NOT_SUPPORTED);
                                int fileLength = fileData.length;
				String contentMimeType = "text/html";
				//read content to return to client
					
				// we send HTTP Headers with data to client
				out.println("HTTP/1.1 501 Not Implemented");
				out.println("Server: Java HTTP Server from SSaurel : 1.0");
				out.println("Date: " + new Date());
				out.println("Content-type: " + contentMimeType);
				out.println("Content-length: " + fileLength);
				out.println(); // blank line between headers and content, very important !
				out.flush(); // flush character output stream buffer
				// file
				dataOut.write(fileData, 0, fileLength);
				dataOut.flush();
				
			} else {
				// GET or HEAD method
				if (fileRequested.endsWith("/")) {
                                    fileRequested += DEFAULT_FILE;
                                    
				}else if(fileRequested.equals("/puntivendita.xml")){
                                    ObjectMapper objMap = new ObjectMapper();
                                    pv = objMap.readValue(getClass().getResourceAsStream(WEB_ROOT+"/puntiVendita.json"), PuntiVendita.class);
                                    XmlMapper xmlMapper = new XmlMapper();
                                    String palle = xmlMapper.writeValueAsString(pv);
                                    fileData = palle.getBytes();
                                    fileLength = fileData.length;
                                    content = getContentType(fileRequested);
                                    db = true;
                                    
                                } else if(fileRequested.equals("/studenti.xml")) {
                                    try
                                    {
                                        
                                        
                                        
                                        
                                        
                                        
                                        
                                        Class.forName("com.mysql.jdbc.Driver");
                                        Connection connessione = DriverManager.getConnection("jdbc:mysql://localhost:3306/testestpsit?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Lillo1996");
                                        Statement statement = connessione.createStatement();
                                        ResultSet resultset = statement.executeQuery("select studenti.* from studenti");
                                        for(int i = 0;resultset.next();i++)
                                        {
                                            nome = resultset.getString("Nome");
                                            cognome = resultset.getString("Cognome");
                                            System.out.println(nome +" "+ cognome);
                                            Studente st = new Studente(nome, cognome);
                                            s.getListaStudenti().add(st);
                                        }
                                        XmlMapper xmlMapper = new XmlMapper();
                                        String palle = xmlMapper.writeValueAsString(s);
                                        fileData = palle.getBytes();
                                        fileLength = fileData.length;
                                        content = getContentType(fileRequested);
                                        db = true;
                                        
                                        
                                        
                                        
                                        
                                        
                                    } catch (ClassNotFoundException e) {
                                            System.out.println(e.toString());
                                    } catch (SQLException ex) {
                                        System.out.println(ex.toString());
                                    }
                                } else if(fileRequested.equals("/studenti.json")) {
                                    try
                                    {
                                        
                                        
                                        
                                        
                                        
                                        Class.forName("com.mysql.jdbc.Driver");
                                        Connection connessione = DriverManager.getConnection("jdbc:mysql://localhost:3306/testestpsit?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Lillo1996");
                                        Statement statement = connessione.createStatement();
                                        ResultSet resultset = statement.executeQuery("select studenti.* from studenti");
                                        for(int i = 0;resultset.next();i++)
                                        {
                                            nome = resultset.getString("Nome");
                                            cognome = resultset.getString("Cognome");
                                            System.out.println(nome +" "+ cognome);
                                            Studente st = new Studente(nome, cognome);
                                            s.getListaStudenti().add(st);
                                        }
                                        ObjectMapper objectMapper = new ObjectMapper();
                                        String palle = objectMapper.writeValueAsString(s);
                                        fileData = palle.getBytes();
                                        fileLength = fileData.length;
                                        content = getContentType(fileRequested);
                                        db = true;
                                        
                                        
                                        
                                        
                                        
                                        
                                    } catch (ClassNotFoundException e) {
                                            System.out.println(e.toString());
                                    } catch (SQLException ex) {
                                        System.out.println(ex.toString());
                                    }
                                }
				
                                if(db)
                                {
                                    fileData = readFileData(WEB_ROOT+fileRequested);
                                    fileLength = fileData.length;
                                    content = getContentType(fileRequested);
                                }
				db = false;
				
				if (method.equals("GET")) { // GET method so we return content
					
					// send HTTP Headers
					out.println("HTTP/1.1 200 OK");
					out.println("Server: Java HTTP Server from SSaurel : 1.0");
					out.println("Date: " + new Date());
					out.println("Content-type: " + content);
					out.println("Content-length: " + fileLength);
					out.println(); // blank line between headers and content, very important !
					out.flush(); // flush character output stream buffer
					
					dataOut.write(fileData, 0, fileLength);
					dataOut.flush();
				}
				
				if (verbose) {
					System.out.println("File " + fileRequested + " of type " + content + " returned");
				}
				
			} 
			
		} catch (FileNotFoundException fnfe) {
			try {
				fileNotFound(out, dataOut, fileRequested);
			} catch (IOException ioe) {
				System.err.println("Error with file not found exception : " + ioe.getMessage());
			}
			
		} catch (IOException ioe) {
			System.err.println("Server error : " + ioe);
		} finally {
			try {
				in.close();
				out.close();
				dataOut.close();
				connect.close(); // we close socket connection
			} catch (Exception e) {
				System.err.println("Error closing stream : " + e.getMessage());
			} 
			
			if (verbose) {
				System.out.println("Connection closed.\n");
			}
		}
		
		
	}
	
	private byte[] readFileData(String file) throws IOException {
		byte[] fileData = null;
		InputStream fileIn = null;
                
		try {
         
			fileIn = getClass().getResourceAsStream(file);
                        if(fileIn == null||!file.contains("."))return null;
                        fileData = new byte[fileIn.available()];
                        fileIn.read(fileData);
		} finally {
			if (fileIn != null) 
				fileIn.close();
		}
		
		return fileData;
	}
	
	// return supported MIME Types
	private String getContentType(String fileRequested) {
		if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
			return "text/html";
                else if (fileRequested.endsWith(".xml"))
                        return "text/xml";
                else if (fileRequested.endsWith(".json"))
                        return "apprication/json";
		else
                        return "text/plain";
	}
	
	private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
            String content = "text/html";
            if(fileRequested.endsWith(".html")){
		byte[] fileData = readFileData(WEB_ROOT+FILE_NOT_FOUND);
                int fileLength = fileData.length;
		
                
		out.println("HTTP/1.1 404 File Not Found");
		out.println("Server: Java HTTP Server from SSaurel : 1.0");
		out.println("Date: " + new Date());
		out.println("Content-type: " + content);
		out.println("Content-length: " + fileLength);
		out.println(); // blank line between headers and content, very important !
		out.flush(); // flush character output stream buffer
		
		dataOut.write(fileData, 0, fileLength);
                dataOut.flush();

                if (verbose) {
                    System.out.println("File " + fileRequested + " not found");
                }
        }else
                {
                    byte[] fileData = readFileData(WEB_ROOT+PAGE_NOT_FOUND);
                    int fileLength = fileData.length;
                
                    out.println("HTTP/1.1 301 Page Not Found");
                    out.println("Server: Java HTTP Server from SSaurel : 1.0");
                    out.println("Date: " + new Date());
                    out.println("Location: "+fileRequested + "/");
                    out.println(); // blank line between headers and content, very important !
                    out.flush(); // flush character output stream buffer
                    
                    dataOut.write(fileData, 0, fileLength);
                    dataOut.flush();
		
                    if (verbose) {
                        System.out.println("Page " + fileRequested + " not found");
                    }
                }
	}
	
}
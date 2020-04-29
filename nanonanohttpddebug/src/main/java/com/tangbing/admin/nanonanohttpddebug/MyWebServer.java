package com.tangbing.admin.nanonanohttpddebug;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import fi.iki.elonen.NanoHTTPD;
/**
 * Created by tangbing on 2020/4/26.
 * Describe :
 */
public class MyWebServer extends NanoHTTPD {

    private final static int PORT = 8888;
    private final static String homelost="http://127.0.0.1";
    private Context _mainContext;

    public MyWebServer(int port) {
        super(port);
    }

    /*
    主构造函数，也用来启动http服务
    */
    public MyWebServer(Context context) throws IOException {
        super(PORT);
        _mainContext = context;
    }

    /*
    解析的主入口函数，所有请求从这里进，也从这里出
    */
    @Override
    public Response serve(IHTTPSession session) {
       /* String msg = "<html><body><h1>Hello server</h1>\n";
        msg += "<p>We serve " + session.getUri() + " !</p>";
        return newFixedLengthResponse( msg + "</body></html>\n" );
*/
       /* String uri = session.getUri();
        String filename = uri.substring(1);
        if (uri.equals("/"))
            filename = "index.html";

        String response = "";
        String line = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(_mainContext.getAssets().open(filename)));

            while ((line = reader.readLine()) != null) {
                response += line;
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFixedLengthResponse(response);*/


        /*String uri = session.getUri();
        System.out.println("####MyWebServer:" + uri);
        String filename = uri.substring(1);

        if (uri.equals("/"))
            filename = "index.html";

        String mimetype = "text/html";
        if (filename.contains(".html") || filename.contains(".htm")) {
            mimetype = "text/html";
        } else if (filename.contains(".js")) {
            mimetype = "text/javascript";
        } else if (filename.contains(".css")) {
            mimetype = "text/css";
        } else {
            filename = "index.html";
            mimetype = "text/html";
        }

        String response = "";
        String line = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(_mainContext.getAssets().open(filename)));

            while ((line = reader.readLine()) != null) {
                response += line;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFixedLengthResponse(Response.Status.OK, mimetype, response);*/

        String uri = session.getUri();
        System.out.println("####MyWebServer:" + uri);
        String filename = uri.substring(1);
        if (uri.equals("/"))
            filename = "index.xhtml";

        boolean is_ascii = true;
        String mimetype = "text/html";
        if (filename.contains(".html") || filename.contains(".htm")) {
            mimetype = "text/html";
            is_ascii = true;
        } else if (filename.contains(".js")) {
            mimetype = "text/javascript";
            is_ascii = true;
        } else if (filename.contains(".css")) {
            mimetype = "text/css";
            is_ascii = true;
        } else if (filename.contains(".gif")) {
            mimetype = "text/gif";
            is_ascii = false;
        } else if (filename.contains(".jpeg") || filename.contains(".jpg")) {
            mimetype = "text/jpeg";
            is_ascii = false;
        } else if (filename.contains(".png")) {
            mimetype = "image/png";
            is_ascii = false;
        } else {
            filename = "index.xhtml";
            mimetype = "text/html";
        }

        if (is_ascii) {
            String response = "";
            String line = "";
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(_mainContext.getAssets().open(filename)));

                while ((line = reader.readLine()) != null) {
                    response += line;
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return newFixedLengthResponse(Response.Status.OK, mimetype, response);
        } else {
            InputStream isr;
            try {
                isr = _mainContext.getAssets().open(filename);
                return newFixedLengthResponse(Response.Status.OK, mimetype, isr, isr.available());
            } catch (IOException e) {
                e.printStackTrace();
                return newFixedLengthResponse(Response.Status.OK, mimetype, "");
            }
        }
    }
}

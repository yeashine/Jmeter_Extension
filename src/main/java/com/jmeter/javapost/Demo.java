package com.jmeter.javapost;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class Demo extends AbstractJavaSamplerClient implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(Demo.class);
    private static final long serialVersionUID = 241L;
    private String sURL ;

    private static final String URL_NAME = "RUL";//设置GUI界面显示的变量名称
    private static final String URL_VALUE_DEFAULT="http://www.baidu.com";
    //resultData变量用来存储响应的数据，目的是显示到查看结果树中
    private String resultData;
    StringBuilder sbResultData = new StringBuilder();

    /**
     * 这个方法用来控制显示在GUI页面的属性，由用户进行设置
     * 此方法不用调用，是一个与生命周期相关的方法，类加载则运行。
     * 这个方法由Jmeter在进行添加javaRequest时第一个运行，它决定了你要在GUI中默认显示出哪些属性。
     * @return Arguments
     */
    @Override
    public Arguments getDefaultParameters() {
        Arguments arguments = new Arguments();
        arguments.addArgument(URL_NAME,String.valueOf(URL_VALUE_DEFAULT));
        return arguments;
    }

    public Demo() {
        super();
    }

    /**
     * 这个方法相当于loadrunner中的init，我们可以用它来进行一些初始化的动作。
     * @param context
     */
    @Override
    public void setupTest(JavaSamplerContext context) {
        sURL = context.getParameter(URL_NAME,URL_VALUE_DEFAULT);
        if (sURL.length()==0){
            System.out.println("用户么有设置URL");
            sURL="http://www.baidu.com";
        }
    }

    /**
     * 这个方法相当于loadrunner中的action，我们的核心测试代码就在这里了。
     * @param javaSamplerContext
     * @return
     */
    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        /*
        SampleResult这个类是用来 将测试结果输出到查看结果树中的。
        并且也是用来控制事务的开始和结束的。
         */
        SampleResult sampleResult = new SampleResult();
        sampleResult.setSampleLabel("测试"+sURL+"  网站的访问速度了");
        try{
            URL url = new URL(sURL);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            byte [] buffer = new byte[1024];
            int len;
            //事务标记开始
            sampleResult.sampleStart();
            while ((len = inputStream.read() )!=-1){
                resultData = new String(buffer,"utf-8");//将每次读到的网页信息存入resultData中
                sbResultData.append(resultData);//每读到一次，添加到sbResultData中。
            }
            inputStream.close();
        }catch (MalformedURLException e){
            sampleResult.setSuccessful(false);
            e.printStackTrace();
        }catch (IOException e){
            sampleResult.setSuccessful(false);
            e.printStackTrace();
        }catch (Exception e){ // 如果发生异常，则捕捉，并且发送事务失败的消息。
            sampleResult.setSuccessful(false);
            e.printStackTrace();
        }finally {
            //标记事务结束
            sampleResult.sampleEnd();
        }
        sampleResult.setSuccessful(true);
        resultData = sbResultData.toString();//将读到的所有数据，转换为字符串。
        sampleResult.setResponseData(resultData,null);//将数据打印到查看结果树中。
        sampleResult.setDataType(SampleResult.TEXT);
        return sampleResult;
    }

    /**
     * 这个方法相当于loadrunner中的end，收尾的工作可以由它来做。
     * @param context
     */
    @Override
    public void teardownTest(JavaSamplerContext context) {
        super.teardownTest(context);
    }



}

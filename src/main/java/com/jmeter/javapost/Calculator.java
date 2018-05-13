package com.jmeter.javapost;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;

public class Calculator extends AbstractJavaSamplerClient implements Serializable {
    private static Logger logger = LoggerFactory.getLogger(Calculator.class);
    private static final long seriaVersion = 241L;

    private static long Num1;
    public static final long DEFAULT_NUM1 = 0;
    private static String Num_1 = "Num1";

     private static long Num2;
    public static final long DEFAULT_NUM2 = 0;
    private static String Num_2 = "Num1";
    private String samplerData;

    /** The default value of the SamplerData parameter. */
    private static final String SAMPLER_DATA_DEFAULT = "";

    /** The name used to store the SamplerData parameter. */
    private static final String SAMPLER_DATA_NAME = "SamplerData";


    @Override
    public void setupTest(JavaSamplerContext context) {
        Num1 = context.getLongParameter(Num_1,DEFAULT_NUM1);
        Num2 = context.getLongParameter(Num_2,DEFAULT_NUM2);
    }

    @Override
    public Arguments getDefaultParameters() {
        Arguments arguments = new Arguments();
        arguments.addArgument(Num_1,String.valueOf(DEFAULT_NUM1));
        arguments.addArgument(Num_2,String.valueOf(DEFAULT_NUM2));
        return arguments;
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult = new SampleResult();


        sampleResult.sampleStart();
        long sum = 0;
        try {
             sum = Num1+Num2;
        }catch (NumberFormatException e){
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

        String result = "计算结果是:"+sum;
        sampleResult.setResponseData(result,null); //结果树中,响应中的数据
        sampleResult.setSamplerData("计算的第一个数是:"+Num1+",计算的第二个数是:"+Num2);  //结果数中,请求体的数据
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

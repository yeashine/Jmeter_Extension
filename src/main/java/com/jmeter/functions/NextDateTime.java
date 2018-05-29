package com.jmeter.functions;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class NextDateTime extends AbstractFunction {
    private static final List<String> desc = new LinkedList<>();
    private static final String KEY = "__NextDateTime"; // 函数key，用于界面上选择函数
    private Object[] values;

    static {
        desc.add("今日之后的第几天"); // 函数助手中显示的参数说明，对应到参数
        desc.add("整点");
        desc.add(JMeterUtils.getResString("function_name_paropt"));// 保存函数返回结果的变量，用于引用
    }

    private CompoundVariable varName, day, time;

    @Override
    public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
        int dayInt = Integer.parseInt(day.toString());
        int timeInt = Integer.parseInt(time.toString());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, dayInt);
        calendar.set(Calendar.HOUR_OF_DAY, timeInt);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long time = calendar.getTime().getTime();
        String varTime = String.valueOf(time/1000);
        if (varName != null) {
            JMeterVariables vars = getVariables();
            final String varTrim = varName.execute().trim();
            if (vars != null && varTrim.length() > 0) {
                vars.put(varTrim, varTime);
            }
        }
        return  varTime;
    }

    @Override
    public void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
        // 检查参数数量
        checkParameterCount(parameters, 2, 3);
        values = parameters.toArray();
        day = (CompoundVariable) values[0];
        time = (CompoundVariable) values[1];
        if (values.length > 2) {
            varName = (CompoundVariable) values[2];
        } else {
            varName = null;
        }

    }

    @Override
    public String getReferenceKey() {
        return KEY;
    }

    /**
     * 实现功能的描述
     * 各个变量的意思
     *
     * @return
     */
    @Override
    public List<String> getArgumentDesc() {
        return desc;
    }
}

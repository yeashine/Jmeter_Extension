package com.jmeter.functions;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Plus extends AbstractFunction {

    private CompoundVariable first,second;
    private Object[] values;
    @Override
    public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
        System.out.println("execute method start!!!");
        first = (CompoundVariable)values[0];
        System.out.println("第一个数字是："+first);
        second = (CompoundVariable) values[1];
        System.out.println("第二个数字是："+second);
        int count = new Integer(first.execute().trim())+new Integer(second.execute().trim());
        return String.valueOf(count);
    }

    /**
     * 接收用户传递的参数
     * @param collection
     * @throws InvalidVariableException
     */
    @Override
    public void setParameters(Collection<CompoundVariable> collection) throws InvalidVariableException {
        checkParameterCount(collection,2);
        values = collection.toArray();
        System.out.println("setParameters method start!!!");

    }

    /**
     * 功能的名称
     * @return
     */
    @Override
    public String getReferenceKey() {
        return "__MyDemo";
    }

    /**
     * 实现功能的描述
     * @return
     */
    @Override
    public List<String> getArgumentDesc() {
        List desc = new ArrayList();
        desc.add("第一个数字");
        desc.add("第二个数字");
        return desc;
    }
}

package com.zy.sdk.okhttp.listener;

/**
 * @author vision
 *
 */
public class DisposeDataHandle
{
	public DisposeDataListener mListener = null;
	public Class<?> mClass = null;

	public DisposeDataHandle(DisposeDataListener listener)
	{
		this.mListener = listener;
	}

	//在回调中将字节码转化为实体对象
	public DisposeDataHandle(DisposeDataListener listener, Class<?> clazz)
	{
		this.mListener = listener;
		this.mClass = clazz;
	}

}
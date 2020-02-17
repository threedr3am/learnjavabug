package com.threedr3am.bug.dubbo.support;

import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;
import com.caucho.hessian.io.SerializerFactory;
import com.caucho.hessian.io.UnsafeSerializer;
import com.caucho.hessian.io.WriteReplaceSerializer;

/**
 * @author xuanyh
 */
public class NoWriteReplaceSerializerFactory extends SerializerFactory {

  /**
   * {@inheritDoc}
   *
   * @see SerializerFactory#getObjectSerializer(Class)
   */
  @Override
  public Serializer getObjectSerializer(Class<?> cl) throws HessianProtocolException {
    return super.getObjectSerializer(cl);
  }


  /**
   * {@inheritDoc}
   *
   * @see SerializerFactory#getSerializer(Class)
   */
  @Override
  public Serializer getSerializer(Class cl) throws HessianProtocolException {
    Serializer serializer = super.getSerializer(cl);

    if (serializer instanceof WriteReplaceSerializer) {
      return UnsafeSerializer.create(cl);
    }
    return serializer;
  }

}

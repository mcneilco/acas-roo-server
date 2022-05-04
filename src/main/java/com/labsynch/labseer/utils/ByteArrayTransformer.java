package com.labsynch.labseer.utils;

import java.lang.reflect.Array;

import flexjson.transformer.AbstractTransformer;

public class ByteArrayTransformer extends AbstractTransformer {

  @Override
  public Boolean isInline() {
    return true;
  }

  @Override
  public void transform(Object object) {
    getContext().writeName("blobValue");
    if (object instanceof byte[]) {
      getContext().writeOpenArray();
      int length = Array.getLength(object);
      for (int i = 0; i < length; ++i) {
        byte myByte = (byte) Array.get(object, i);
        int unsignedByte = myByte & 0xFF;
        getContext().write(String.valueOf(unsignedByte));
        if (i < length) {
          getContext().writeComma();
        }
      }
      getContext().writeCloseArray();
    } else {
      getContext().write(null);
    }
    getContext().writeComma();
  }
}

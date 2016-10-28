package com.labsynch.labseer.utils;

import flexjson.transformer.AbstractTransformer;


public class ExcludeNulls extends AbstractTransformer {

  @Override
  public Boolean isInline() {
      return true;
  }

  @Override
  public void transform(Object object) {
      // Do nothing, null objects are not serialized.
      return;
  }
}
